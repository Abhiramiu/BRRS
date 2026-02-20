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

import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12E_LTV_Summary_Repo;
import com.bornfire.brrs.entities.M_SRWA_12E_LTV_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12E_LTV_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12E_LTV_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12E_LTV_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12E_LTV_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12E_LTV_Summary_Entity;

@Component
@Service

public class BRRS_M_SRWA_12E_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12E_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Summary_Repo brrs_M_SRWA_12E_LTV_summary_repo;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Detail_Repo brrs_M_SRWA_12E_LTV_detail_repo;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo M_SRWA_12E_LTV_Archival_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Archival_Detail_Repo BRRS_M_SRWA_12E_LTV_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Resub_Summary_Repo brrs_M_SRWA_12E_LTV_resub_summary_repo;

	@Autowired
	BRRS_M_SRWA_12E_LTV_Resub_Detail_Repo brrs_M_SRWA_12E_LTV_resub_detail_repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_SRWA_12E_LTVView(String reportId, String fromdate, String todate, String currency,
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
				List<M_SRWA_12E_LTV_Archival_Summary_Entity> T1Master = M_SRWA_12E_LTV_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12E_LTV_Resub_Summary_Entity> T1Master = brrs_M_SRWA_12E_LTV_resub_summary_repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_SRWA_12E_LTV_Summary_Entity> T1Master = brrs_M_SRWA_12E_LTV_summary_repo.getdatabydateList(d1);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12E_LTV_Archival_Detail_Entity> T1Master = BRRS_M_SRWA_12E_LTV_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12E_LTV_Resub_Detail_Entity> T1Master = brrs_M_SRWA_12E_LTV_resub_detail_repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_SRWA_12E_LTV_Detail_Entity> T1Master = brrs_M_SRWA_12E_LTV_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12E_LTV");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	@Transactional
	public void updateReport(M_SRWA_12E_LTV_Summary_Entity updatedEntity) {
		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// 🔹 Fetch existing SUMMARY
		M_SRWA_12E_LTV_Summary_Entity existingSummary = brrs_M_SRWA_12E_LTV_summary_repo
				.findById(updatedEntity.getReportDate()).orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		// 🔹 Fetch or create DETAIL
		M_SRWA_12E_LTV_Detail_Entity detailEntity = brrs_M_SRWA_12E_LTV_detail_repo
				.findById(updatedEntity.getReportDate()).orElseGet(() -> {
					M_SRWA_12E_LTV_Detail_Entity d = new M_SRWA_12E_LTV_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {
			// 1️⃣ Loop from R11 to R50 and copy fields
			for (int i = 13; i <= 19; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "PRODUCT", "PERFORMING_EXPOSURE", "NON_PERFORMING", "SPECIFIC_PROV",
						"UNSECURED_PORTION_NPL", "TOTAL" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_SRWA_12E_LTV_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_SRWA_12E_LTV_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_SRWA_12E_LTV_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		brrs_M_SRWA_12E_LTV_summary_repo.save(existingSummary);
		brrs_M_SRWA_12E_LTV_detail_repo.save(detailEntity);

		System.out.println("Update completed successfully");
	}

	public void updateResubReport(M_SRWA_12E_LTV_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = brrs_M_SRWA_12E_LTV_resub_summary_repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_SRWA_12E_LTV_Resub_Summary_Entity resubSummary = new M_SRWA_12E_LTV_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_SRWA_12E_LTV_Resub_Detail_Entity resubDetail = new M_SRWA_12E_LTV_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12E_LTV_Archival_Summary_Entity archSummary = new M_SRWA_12E_LTV_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12E_LTV_Archival_Detail_Entity archDetail = new M_SRWA_12E_LTV_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		brrs_M_SRWA_12E_LTV_resub_summary_repo.save(resubSummary);
		brrs_M_SRWA_12E_LTV_resub_detail_repo.save(resubDetail);

		M_SRWA_12E_LTV_Archival_Summary_Repo.save(archSummary);
		BRRS_M_SRWA_12E_LTV_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_SRWA_12E_LTVResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_SRWA_12E_LTV_Archival_Summary_Entity> latestArchivalList = M_SRWA_12E_LTV_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SRWA_12E_LTV_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12E_LTV Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getM_SRWA_12E_LTVArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SRWA_12E_LTV_Archival_Summary_Entity> repoData = M_SRWA_12E_LTV_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12E_LTV_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12E_LTV_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12E_LTV Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] BRRS_M_SRWA_12E_LTVExcel(String filename, String reportId, String fromdate, String todate,
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
				return getExcelM_SRWA_12E_LTVARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
						format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12E_LTVResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						format, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SRWA_12E_LTVEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				List<M_SRWA_12E_LTV_Summary_Entity> dataList = brrs_M_SRWA_12E_LTV_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SRWA_12E_LTV report. Returning empty result.");
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

					int startRow = 12;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_SRWA_12E_LTV_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
//NORMAL

							Cell cell2 = row.createCell(2);
							if (record.getR13_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR13_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR13_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);

							// ====================== R11 ======================
							cell2 = row.createCell(2);
							if (record.getR14_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							row = sheet.getRow(14);

							// ====================== R11 ======================
							cell2 = row.createCell(2);
							if (record.getR15_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							row = sheet.getRow(15);

							// ====================== R11 ======================
							cell2 = row.createCell(2);
							if (record.getR16_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR16_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR16_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							row = sheet.getRow(16);

							// ====================== R11 ======================
							cell2 = row.createCell(2);
							if (record.getR17_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR17_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR17_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							row = sheet.getRow(17);

							// ====================== R18 ======================
							cell2 = row.createCell(2);
							if (record.getR18_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR18_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR18_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR18_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR18_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR18_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
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
	}

	// Normal Email Excel
	public byte[] BRRS_M_SRWA_12E_LTVEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12E_LTVEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype,
						type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12E_LTVResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_SRWA_12E_LTV_Summary_Entity> dataList = brrs_M_SRWA_12E_LTV_summary_repo
					.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_SRWA_12E_LTV report. Returning empty result.");
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

				int startRow = 13;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_SRWA_12E_LTV_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL

						Cell cell2 = row.createCell(2);
						if (record.getR13_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record.getR13_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(4);
						if (record.getR13_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}
						Cell cell5 = row.createCell(5);
						if (record.getR13_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR13_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						row = sheet.getRow(14);

						// ====================== R11 ======================
						cell2 = row.createCell(2);
						if (record.getR14_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR14_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}
						cell5 = row.createCell(5);
						if (record.getR14_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR14_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						Cell cell6 = row.createCell(6);
						if (record.getR14_TOTAL() != null) {
							cell6.setCellValue(record.getR14_TOTAL().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						row = sheet.getRow(15);

						// ====================== R11 ======================
						cell2 = row.createCell(2);
						if (record.getR15_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR15_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR15_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR15_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}
						row = sheet.getRow(16);

						// ====================== R11 ======================
						cell2 = row.createCell(2);
						if (record.getR16_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR16_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR16_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR16_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR16_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR16_TOTAL() != null) {
							cell6.setCellValue(record.getR16_TOTAL().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						row = sheet.getRow(17);

						// ====================== R11 ======================
						cell2 = row.createCell(2);
						if (record.getR17_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR17_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR17_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}
						cell5 = row.createCell(5);
						if (record.getR17_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR17_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR17_TOTAL() != null) {
							cell6.setCellValue(record.getR17_TOTAL().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}
						row = sheet.getRow(18);

						// ====================== R18 ======================
						cell2 = row.createCell(2);
						if (record.getR19_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR19_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR19_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR19_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR19_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR19_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
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

	// Archival format excel
	public byte[] getExcelM_SRWA_12E_LTVARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12E_LTVEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype,
						type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SRWA_12E_LTV_Archival_Summary_Entity> dataList = M_SRWA_12E_LTV_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12E_LTV report. Returning empty result.");
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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12E_LTV_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL

					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(17);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR18_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR18_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR18_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR18_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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

	// Archival Email Excel
	public byte[] BRRS_M_SRWA_12E_LTVEmailArchivalExcel(String filename, String reportId, String fromdate,
			String todate, String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SRWA_12E_LTV_Archival_Summary_Entity> dataList = M_SRWA_12E_LTV_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12E_LTV report. Returning empty result.");
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

			int startRow = 13;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12E_LTV_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL

					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					Cell cell5 = row.createCell(5);
					if (record.getR13_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR13_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR14_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR14_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					Cell cell6 = row.createCell(6);
					if (record.getR14_TOTAL() != null) {
						cell6.setCellValue(record.getR14_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR15_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR15_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR16_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR16_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR16_TOTAL() != null) {
						cell6.setCellValue(record.getR16_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR17_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR17_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR17_TOTAL() != null) {
						cell6.setCellValue(record.getR17_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR19_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR19_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR19_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR19_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR19_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR19_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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

	// Resub Format excel
	public byte[] BRRS_M_SRWA_12E_LTVResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12E_LTVResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SRWA_12E_LTV_Resub_Summary_Entity> dataList = brrs_M_SRWA_12E_LTV_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12E_LTV report. Returning empty result.");
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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SRWA_12E_LTV_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL

					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(17);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR18_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR18_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR18_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR18_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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

	// Resub Email Excel
	public byte[] BRRS_M_SRWA_12E_LTVResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SRWA_12E_LTV_Resub_Summary_Entity> dataList = brrs_M_SRWA_12E_LTV_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12E_LTV report. Returning empty result.");
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

			int startRow = 13;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12E_LTV_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL

					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					Cell cell5 = row.createCell(5);
					if (record.getR13_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR13_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR14_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR14_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					Cell cell6 = row.createCell(6);
					if (record.getR14_TOTAL() != null) {
						cell6.setCellValue(record.getR14_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR15_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR15_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR16_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR16_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR16_TOTAL() != null) {
						cell6.setCellValue(record.getR16_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR17_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR17_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR17_TOTAL() != null) {
						cell6.setCellValue(record.getR17_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR19_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR19_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR19_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR19_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR19_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR19_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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