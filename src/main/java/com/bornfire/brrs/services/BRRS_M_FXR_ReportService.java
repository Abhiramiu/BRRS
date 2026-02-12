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

import com.bornfire.brrs.entities.BRRS_M_FXR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_FXR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_FXR_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_FXR_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_FXR_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_FXR_Summary_Repo;
import com.bornfire.brrs.entities.M_FXR_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_FXR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_FXR_Detail_Entity;
import com.bornfire.brrs.entities.M_FXR_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_FXR_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_FXR_Summary_Entity;

@Component
@Service

public class BRRS_M_FXR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_FXR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_FXR_Summary_Repo brrs_M_FXR_summary_repo;

	@Autowired
	BRRS_M_FXR_Detail_Repo brrs_M_FXR_detail_repo;

	@Autowired
	BRRS_M_FXR_Archival_Summary_Repo M_FXR_Archival_Summary_Repo;

	@Autowired
	BRRS_M_FXR_Archival_Detail_Repo BRRS_M_FXR_Archival_Detail_Repo;

	@Autowired
	BRRS_M_FXR_Resub_Summary_Repo brrs_M_FXR_resub_summary_repo;

	@Autowired
	BRRS_M_FXR_Resub_Detail_Repo brrs_M_FXR_resub_detail_repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_FXRView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

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
				List<M_FXR_Archival_Summary_Entity> T1Master = M_FXR_Archival_Summary_Repo.getdatabydateListarchival(d1,
						version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_FXR_Resub_Summary_Entity> T1Master = brrs_M_FXR_resub_summary_repo.getdatabydateListarchival(d1,
						version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_FXR_Summary_Entity> T1Master = brrs_M_FXR_summary_repo.getdatabydateList(d1);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_FXR_Archival_Detail_Entity> T1Master = BRRS_M_FXR_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_FXR_Resub_Detail_Entity> T1Master = brrs_M_FXR_resub_detail_repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_FXR_Detail_Entity> T1Master = brrs_M_FXR_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_FXR");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport1(M_FXR_Summary_Entity updatedEntity) {
		System.out.println("Came to services1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FXR_Summary_Entity existingSummary = brrs_M_FXR_summary_repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
		// 🔹 Fetch or create DETAIL
		M_FXR_Detail_Entity detailEntity = brrs_M_FXR_detail_repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					M_FXR_Detail_Entity d = new M_FXR_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {
			// 1️⃣ Loop from R11 to R16 and copy fields
			for (int i = 11; i <= 16; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "net_spot_position", "net_forward_position", "guarantees", "net_future_inc_or_exp",
						"net_delta_wei_fx_opt_posi", "other_items", "net_long_position", "or", "net_short_position" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R17 totals
			String[] totalFields = { "net_long_position", "net_short_position" };
			for (String field : totalFields) {
				String getterName = "getR17_" + field;
				String setterName = "setR17_" + field;

				try {
					// Getter from UPDATED entity
					Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

					Object newValue = getter.invoke(updatedEntity);

					// SUMMARY setter
					Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					summarySetter.invoke(existingSummary, newValue);

					// DETAIL setter
					Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					// Skip if not present
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		brrs_M_FXR_summary_repo.save(existingSummary);
		brrs_M_FXR_detail_repo.save(detailEntity);
	}

	public void updateReport2(M_FXR_Summary_Entity updatedEntity) {
		System.out.println("Came to services2");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FXR_Summary_Entity existingSummary = brrs_M_FXR_summary_repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
		// 🔹 Fetch or create DETAIL
		M_FXR_Detail_Entity detailEntity = brrs_M_FXR_detail_repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					M_FXR_Detail_Entity d = new M_FXR_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {
			// 1️⃣ Loop from R11 to R50 and copy fields
			for (int i = 21; i <= 22; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "long", "short" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
				String[] formulaFields = { "total_gross_long_short", "net_position" };
				for (String field : formulaFields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// 2️⃣ Handle R23 totals
			String getterName = "getR23_net_position";
			String setterName = "setR23_net_position";

			try {
				// Getter from UPDATED entity
				Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

				Object newValue = getter.invoke(updatedEntity);

				// SUMMARY setter
				Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

				summarySetter.invoke(existingSummary, newValue);

				// DETAIL setter
				Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

				detailSetter.invoke(detailEntity, newValue);

			} catch (NoSuchMethodException e) {
				// Skip if not present
				// continue;
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		brrs_M_FXR_summary_repo.save(existingSummary);
		brrs_M_FXR_detail_repo.save(detailEntity);
	}

	public void updateReport3(M_FXR_Summary_Entity updatedEntity) {
		System.out.println("Came to services3");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FXR_Summary_Entity existingSummary = brrs_M_FXR_summary_repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
		// 🔹 Fetch or create DETAIL
		M_FXR_Detail_Entity detailEntity = brrs_M_FXR_detail_repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					M_FXR_Detail_Entity d = new M_FXR_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {

			String[] fields = { "greater_net_long_or_short", "abs_value_net_gold_posi", "capital_charge" };

			for (String field : fields) {
				String getterName = "getR29_" + field;
				String setterName = "setR29_" + field;

				try {
					// Getter from UPDATED entity
					Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

					Object newValue = getter.invoke(updatedEntity);

					// SUMMARY setter
					Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					summarySetter.invoke(existingSummary, newValue);

					// DETAIL setter
					Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing fields
					continue;
				}
			}

			String getterName = "getR30_capital_require";
			String setterName = "setR30_capital_require";

			try {
				// Getter from UPDATED entity
				Method getter = M_FXR_Summary_Entity.class.getMethod(getterName);

				Object newValue = getter.invoke(updatedEntity);

				// SUMMARY setter
				Method summarySetter = M_FXR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

				summarySetter.invoke(existingSummary, newValue);

				// DETAIL setter
				Method detailSetter = M_FXR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

				detailSetter.invoke(detailEntity, newValue);

			} catch (NoSuchMethodException e) {
				// Skip if not present
				// continue;
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		brrs_M_FXR_summary_repo.save(existingSummary);
		brrs_M_FXR_detail_repo.save(detailEntity);
	}

	public void updateResubReport(M_FXR_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = brrs_M_FXR_resub_summary_repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_FXR_Resub_Summary_Entity resubSummary = new M_FXR_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_FXR_Resub_Detail_Entity resubDetail = new M_FXR_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_FXR_Archival_Summary_Entity archSummary = new M_FXR_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_FXR_Archival_Detail_Entity archDetail = new M_FXR_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		brrs_M_FXR_resub_summary_repo.save(resubSummary);
		brrs_M_FXR_resub_detail_repo.save(resubDetail);

		M_FXR_Archival_Summary_Repo.save(archSummary);
		BRRS_M_FXR_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_FXRResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_FXR_Archival_Summary_Entity> latestArchivalList = M_FXR_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_FXR_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_FXR Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getM_FXRArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_FXR_Archival_Summary_Entity> repoData = M_FXR_Archival_Summary_Repo.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_FXR_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_FXR_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_FXR Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getM_FXRExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_FXRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_FXRResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_FXREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_FXR_Summary_Entity> dataList = brrs_M_FXR_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_FXR report. Returning empty result.");
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

							M_FXR_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							// row12
							// Column C
							Cell cell2 = row.createCell(2);
							if (record.getR11_net_spot_position() != null) {
								cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row12
							// Column D
							Cell cell3 = row.createCell(3);
							if (record.getR11_net_forward_position() != null) {
								cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
								cell3.setCellStyle(numberStyle);

							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(numberStyle);

							}

							// row12
							// Column E
							Cell cell4 = row.createCell(4);
							if (record.getR11_guarantees() != null) {
								cell4.setCellValue(record.getR11_guarantees().doubleValue());
								cell4.setCellStyle(numberStyle);

							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(numberStyle);

							}

							// row12
							// Column F
							Cell cell5 = row.createCell(5);
							if (record.getR11_net_future_inc_or_exp() != null) {
								cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
								cell5.setCellStyle(numberStyle);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(numberStyle);

							}

							// row12
							// Column G
							Cell cell6 = row.createCell(6);
							if (record.getR11_net_delta_wei_fx_opt_posi() != null) {
								cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(numberStyle);
							}

							// row12
							// Column H
							Cell cell7 = row.createCell(7);
							if (record.getR11_other_items() != null) {
								cell7.setCellValue(record.getR11_other_items().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(numberStyle);

							}

							// row12
							// Column I
							Cell cell8 = row.createCell(8);
							if (record.getR11_net_long_position() != null) {
								cell8.setCellValue(record.getR11_net_long_position().doubleValue());
								cell8.setCellStyle(numberStyle);

							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(numberStyle);

							}

							// row12
							// Column J
							Cell cell9 = row.createCell(9);
							if (record.getR11_or() != null) {
								cell9.setCellValue(record.getR11_or().doubleValue());
								cell9.setCellStyle(numberStyle);

							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(numberStyle);

							}

							// row12
							// Column K
							Cell cell10 = row.createCell(10);
							if (record.getR11_net_short_position() != null) {
								cell10.setCellValue(record.getR11_net_short_position().doubleValue());
								cell10.setCellStyle(numberStyle);

							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(numberStyle);

							}

							// row13
							row = sheet.getRow(11);
							// row13
							// Column C
							cell2 = row.createCell(2);
							if (record.getR12_net_spot_position() != null) {
								cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row13
							// Column D
							cell3 = row.createCell(3);
							if (record.getR12_net_forward_position() != null) {
								cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
								cell3.setCellStyle(numberStyle);

							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(numberStyle);

							}

							// row13
							// Column E
							cell4 = row.createCell(4);
							if (record.getR12_guarantees() != null) {
								cell4.setCellValue(record.getR12_guarantees().doubleValue());
								cell4.setCellStyle(numberStyle);

							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(numberStyle);

							}

							// row13
							// Column F
							cell5 = row.createCell(5);
							if (record.getR12_net_future_inc_or_exp() != null) {
								cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
								cell5.setCellStyle(numberStyle);

							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(numberStyle);

							}

							// row13
							// Column G
							cell6 = row.createCell(6);
							if (record.getR12_net_delta_wei_fx_opt_posi() != null) {
								cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(numberStyle);
							}

							// row13
							// Column H
							cell7 = row.createCell(7);
							if (record.getR12_other_items() != null) {
								cell7.setCellValue(record.getR12_other_items().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(numberStyle);

							}

							// row13
							// Column I
							cell8 = row.createCell(8);
							if (record.getR12_net_long_position() != null) {
								cell8.setCellValue(record.getR12_net_long_position().doubleValue());
								cell8.setCellStyle(numberStyle);

							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(numberStyle);

							}

							// row13
							// Column J
							cell9 = row.createCell(9);
							if (record.getR12_or() != null) {
								cell9.setCellValue(record.getR12_or().doubleValue());
								cell9.setCellStyle(numberStyle);

							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(numberStyle);

							}

							// row12
							// Column K
							Cell cell12 = row.createCell(10);
							if (record.getR12_net_short_position() != null) {
								cell12.setCellValue(record.getR12_net_short_position().doubleValue());
								cell12.setCellStyle(numberStyle);

							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(numberStyle);

							}

							// row14 (R13)
							row = sheet.getRow(12);

// Column C
							cell2 = row.createCell(2);
							if (record.getR13_net_spot_position() != null) {
								cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// Column D
							cell3 = row.createCell(3);
							if (record.getR13_net_forward_position() != null) {
								cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(numberStyle);
							}

// Column E
							cell4 = row.createCell(4);
							if (record.getR13_guarantees() != null) {
								cell4.setCellValue(record.getR13_guarantees().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(numberStyle);
							}

// Column F
							cell5 = row.createCell(5);
							if (record.getR13_net_future_inc_or_exp() != null) {
								cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(numberStyle);
							}

// Column G
							cell6 = row.createCell(6);
							if (record.getR13_net_delta_wei_fx_opt_posi() != null) {
								cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(numberStyle);
							}

// Column H
							cell7 = row.createCell(7);
							if (record.getR13_other_items() != null) {
								cell7.setCellValue(record.getR13_other_items().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(numberStyle);
							}

// Column I
							cell8 = row.createCell(8);
							if (record.getR13_net_long_position() != null) {
								cell8.setCellValue(record.getR13_net_long_position().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(numberStyle);
							}

// Column J
							cell9 = row.createCell(9);
							if (record.getR13_or() != null) {
								cell9.setCellValue(record.getR13_or().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(numberStyle);
							}

// Column K
							cell10 = row.createCell(10);
							if (record.getR13_net_short_position() != null) {
								cell10.setCellValue(record.getR13_net_short_position().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(numberStyle);
							}

// row15  (R14)
							row = sheet.getRow(13);

// Column C
							cell2 = row.createCell(2);
							if (record.getR14_net_spot_position() != null) {
								cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// Column D
							cell3 = row.createCell(3);
							if (record.getR14_net_forward_position() != null) {
								cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(numberStyle);
							}

// Column E
							cell4 = row.createCell(4);
							if (record.getR14_guarantees() != null) {
								cell4.setCellValue(record.getR14_guarantees().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(numberStyle);
							}

// Column F
							cell5 = row.createCell(5);
							if (record.getR14_net_future_inc_or_exp() != null) {
								cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(numberStyle);
							}

// Column G
							cell6 = row.createCell(6);
							if (record.getR14_net_delta_wei_fx_opt_posi() != null) {
								cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(numberStyle);
							}

// Column H
							cell7 = row.createCell(7);
							if (record.getR14_other_items() != null) {
								cell7.setCellValue(record.getR14_other_items().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(numberStyle);
							}

// Column I
							cell8 = row.createCell(8);
							if (record.getR14_net_long_position() != null) {
								cell8.setCellValue(record.getR14_net_long_position().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(numberStyle);
							}

// Column J
							cell9 = row.createCell(9);
							if (record.getR14_or() != null) {
								cell9.setCellValue(record.getR14_or().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(numberStyle);
							}

// Column K
							cell10 = row.createCell(10);
							if (record.getR14_net_short_position() != null) {
								cell10.setCellValue(record.getR14_net_short_position().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(numberStyle);
							}
// row16 (R15)
							row = sheet.getRow(14);

// Column C
							cell2 = row.createCell(2);
							if (record.getR15_net_spot_position() != null) {
								cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// Column D
							cell3 = row.createCell(3);
							if (record.getR15_net_forward_position() != null) {
								cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(numberStyle);
							}

// Column E
							cell4 = row.createCell(4);
							if (record.getR15_guarantees() != null) {
								cell4.setCellValue(record.getR15_guarantees().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(numberStyle);
							}

// Column F
							cell5 = row.createCell(5);
							if (record.getR15_net_future_inc_or_exp() != null) {
								cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(numberStyle);
							}

// Column G
							cell6 = row.createCell(6);
							if (record.getR15_net_delta_wei_fx_opt_posi() != null) {
								cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(numberStyle);
							}

// Column H
							cell7 = row.createCell(7);
							if (record.getR15_other_items() != null) {
								cell7.setCellValue(record.getR15_other_items().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(numberStyle);
							}

// Column I
							cell8 = row.createCell(8);
							if (record.getR15_net_long_position() != null) {
								cell8.setCellValue(record.getR15_net_long_position().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(numberStyle);
							}

// Column J
							cell9 = row.createCell(9);
							if (record.getR15_or() != null) {
								cell9.setCellValue(record.getR15_or().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(numberStyle);
							}

// Column K
							cell10 = row.createCell(10);
							if (record.getR15_net_short_position() != null) {
								cell10.setCellValue(record.getR15_net_short_position().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(numberStyle);
							}

// row17 (R16)
							row = sheet.getRow(15);

// Column C
							cell2 = row.createCell(2);
							if (record.getR16_net_spot_position() != null) {
								cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// Column D
							cell3 = row.createCell(3);
							if (record.getR16_net_forward_position() != null) {
								cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(numberStyle);
							}

// Column E
							cell4 = row.createCell(4);
							if (record.getR16_guarantees() != null) {
								cell4.setCellValue(record.getR16_guarantees().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(numberStyle);
							}

// Column F
							cell5 = row.createCell(5);
							if (record.getR16_net_future_inc_or_exp() != null) {
								cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(numberStyle);
							}

// Column G
							cell6 = row.createCell(6);
							if (record.getR16_net_delta_wei_fx_opt_posi() != null) {
								cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(numberStyle);
							}

// Column H
							cell7 = row.createCell(7);
							if (record.getR16_other_items() != null) {
								cell7.setCellValue(record.getR16_other_items().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(numberStyle);
							}

// Column I
							cell8 = row.createCell(8);
							if (record.getR16_net_long_position() != null) {
								cell8.setCellValue(record.getR16_net_long_position().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(numberStyle);
							}

// Column J
							cell9 = row.createCell(9);
							if (record.getR16_or() != null) {
								cell9.setCellValue(record.getR16_or().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(numberStyle);
							}

// Column K
							cell10 = row.createCell(10);
							if (record.getR16_net_short_position() != null) {
								cell10.setCellValue(record.getR16_net_short_position().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(numberStyle);
							}

// row18 (R17)

							row = sheet.getRow(20);
// Column G
							Cell cell21 = row.createCell(6);
							if (record.getR21_long() != null) {
								cell21.setCellValue(record.getR21_long().doubleValue());
								cell21.setCellStyle(numberStyle);
							} else {
								cell21.setCellValue("");
								cell21.setCellStyle(numberStyle);
							}

// Column H
							Cell cell22 = row.createCell(7);
							if (record.getR21_short() != null) {
								cell22.setCellValue(record.getR21_short().doubleValue());
								cell22.setCellStyle(numberStyle);
							} else {
								cell22.setCellValue("");
								cell22.setCellStyle(numberStyle);
							}
							row = sheet.getRow(21);
// Column G
							Cell cell22g = row.createCell(6);
							if (record.getR22_long() != null) {
								cell22g.setCellValue(record.getR22_long().doubleValue());
								cell22g.setCellStyle(numberStyle);
							} else {
								cell22g.setCellValue("");
								cell22g.setCellStyle(numberStyle);
							}

// Column H
							Cell cell23 = row.createCell(7);
							if (record.getR22_short() != null) {
								cell23.setCellValue(record.getR22_short().doubleValue());
								cell23.setCellStyle(numberStyle);
							} else {
								cell23.setCellValue("");
								cell23.setCellStyle(numberStyle);
							}
							row = sheet.getRow(29);
// Column I
//							Cell cell29 = row.createCell(8);
//							if (record.getR30_capital_require() != null) {
//								cell29.setCellValue(record.getR30_capital_require().doubleValue());
//								cell29.setCellStyle(numberStyle);
//							} else {
//								cell29.setCellValue("");
//								cell29.setCellStyle(numberStyle);
//							}
//NORMAL

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
	public byte[] BRRS_M_FXREmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_FXREmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_FXRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_FXR_Summary_Entity> dataList = brrs_M_FXR_summary_repo.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_FXR report. Returning empty result.");
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

//EMAIL
				int startRow = 10;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {

						M_FXR_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// row12
						// Column C
						Cell cell2 = row.createCell(2);
						if (record.getR11_net_spot_position() != null) {
							cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row12
						// Column D
						Cell cell3 = row.createCell(3);
						if (record.getR11_net_forward_position() != null) {
							cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
							cell3.setCellStyle(numberStyle);

						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(numberStyle);

						}

						// row12
						// Column E
						Cell cell4 = row.createCell(4);
						if (record.getR11_guarantees() != null) {
							cell4.setCellValue(record.getR11_guarantees().doubleValue());
							cell4.setCellStyle(numberStyle);

						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(numberStyle);

						}

						// row12
						// Column F
						Cell cell5 = row.createCell(5);
						if (record.getR11_net_future_inc_or_exp() != null) {
							cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
							cell5.setCellStyle(numberStyle);

						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(numberStyle);

						}

						// row12
						// Column G
						Cell cell6 = row.createCell(6);
						if (record.getR11_net_delta_wei_fx_opt_posi() != null) {
							cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(numberStyle);
						}

						// row12
						// Column H
						Cell cell7 = row.createCell(7);
						if (record.getR11_other_items() != null) {
							cell7.setCellValue(record.getR11_other_items().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(numberStyle);

						}

						// row12
						// Column I
						Cell cell8 = row.createCell(8);
						if (record.getR11_net_long_position() != null) {
							cell8.setCellValue(record.getR11_net_long_position().doubleValue());
							cell8.setCellStyle(numberStyle);

						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(numberStyle);

						}

						// row12
						// Column J
						Cell cell9 = row.createCell(9);
						if (record.getR11_or() != null) {
							cell9.setCellValue(record.getR11_or().doubleValue());
							cell9.setCellStyle(numberStyle);

						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(numberStyle);

						}

						// row12
						// Column K
						Cell cell10 = row.createCell(10);
						if (record.getR11_net_short_position() != null) {
							cell10.setCellValue(record.getR11_net_short_position().doubleValue());
							cell10.setCellStyle(numberStyle);

						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(numberStyle);

						}

						// row13
						row = sheet.getRow(11);
						// row13
						// Column C
						cell2 = row.createCell(2);
						if (record.getR12_net_spot_position() != null) {
							cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row13
						// Column D
						cell3 = row.createCell(3);
						if (record.getR12_net_forward_position() != null) {
							cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
							cell3.setCellStyle(numberStyle);

						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(numberStyle);

						}

						// row13
						// Column E
						cell4 = row.createCell(4);
						if (record.getR12_guarantees() != null) {
							cell4.setCellValue(record.getR12_guarantees().doubleValue());
							cell4.setCellStyle(numberStyle);

						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(numberStyle);

						}

						// row13
						// Column F
						cell5 = row.createCell(5);
						if (record.getR12_net_future_inc_or_exp() != null) {
							cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
							cell5.setCellStyle(numberStyle);

						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(numberStyle);

						}

						// row13
						// Column G
						cell6 = row.createCell(6);
						if (record.getR12_net_delta_wei_fx_opt_posi() != null) {
							cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(numberStyle);
						}

						// row13
						// Column H
						cell7 = row.createCell(7);
						if (record.getR12_other_items() != null) {
							cell7.setCellValue(record.getR12_other_items().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(numberStyle);

						}

						// row13
						// Column I
						cell8 = row.createCell(8);
						if (record.getR12_net_long_position() != null) {
							cell8.setCellValue(record.getR12_net_long_position().doubleValue());
							cell8.setCellStyle(numberStyle);

						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(numberStyle);

						}

						// row13
						// Column J
						cell9 = row.createCell(9);
						if (record.getR12_or() != null) {
							cell9.setCellValue(record.getR12_or().doubleValue());
							cell9.setCellStyle(numberStyle);

						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(numberStyle);

						}

						// row12
						// Column K
						Cell cell12 = row.createCell(10);
						if (record.getR12_net_short_position() != null) {
							cell12.setCellValue(record.getR12_net_short_position().doubleValue());
							cell12.setCellStyle(numberStyle);

						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(numberStyle);

						}

						// row14 (R13)
						row = sheet.getRow(12);

// Column C
						cell2 = row.createCell(2);
						if (record.getR13_net_spot_position() != null) {
							cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// Column D
						cell3 = row.createCell(3);
						if (record.getR13_net_forward_position() != null) {
							cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(numberStyle);
						}

// Column E
						cell4 = row.createCell(4);
						if (record.getR13_guarantees() != null) {
							cell4.setCellValue(record.getR13_guarantees().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(numberStyle);
						}

// Column F
						cell5 = row.createCell(5);
						if (record.getR13_net_future_inc_or_exp() != null) {
							cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(numberStyle);
						}

// Column G
						cell6 = row.createCell(6);
						if (record.getR13_net_delta_wei_fx_opt_posi() != null) {
							cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(numberStyle);
						}

// Column H
						cell7 = row.createCell(7);
						if (record.getR13_other_items() != null) {
							cell7.setCellValue(record.getR13_other_items().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(numberStyle);
						}

// Column I
						cell8 = row.createCell(8);
						if (record.getR13_net_long_position() != null) {
							cell8.setCellValue(record.getR13_net_long_position().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(numberStyle);
						}

// Column J
						cell9 = row.createCell(9);
						if (record.getR13_or() != null) {
							cell9.setCellValue(record.getR13_or().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(numberStyle);
						}

// Column K
						cell10 = row.createCell(10);
						if (record.getR13_net_short_position() != null) {
							cell10.setCellValue(record.getR13_net_short_position().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(numberStyle);
						}

// row15  (R14)
						row = sheet.getRow(13);

// Column C
						cell2 = row.createCell(2);
						if (record.getR14_net_spot_position() != null) {
							cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// Column D
						cell3 = row.createCell(3);
						if (record.getR14_net_forward_position() != null) {
							cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(numberStyle);
						}

// Column E
						cell4 = row.createCell(4);
						if (record.getR14_guarantees() != null) {
							cell4.setCellValue(record.getR14_guarantees().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(numberStyle);
						}

// Column F
						cell5 = row.createCell(5);
						if (record.getR14_net_future_inc_or_exp() != null) {
							cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(numberStyle);
						}

// Column G
						cell6 = row.createCell(6);
						if (record.getR14_net_delta_wei_fx_opt_posi() != null) {
							cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(numberStyle);
						}

// Column H
						cell7 = row.createCell(7);
						if (record.getR14_other_items() != null) {
							cell7.setCellValue(record.getR14_other_items().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(numberStyle);
						}

// Column I
						cell8 = row.createCell(8);
						if (record.getR14_net_long_position() != null) {
							cell8.setCellValue(record.getR14_net_long_position().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(numberStyle);
						}

// Column J
						cell9 = row.createCell(9);
						if (record.getR14_or() != null) {
							cell9.setCellValue(record.getR14_or().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(numberStyle);
						}

// Column K
						cell10 = row.createCell(10);
						if (record.getR14_net_short_position() != null) {
							cell10.setCellValue(record.getR14_net_short_position().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(numberStyle);
						}
// row16 (R15)
						row = sheet.getRow(14);

// Column C
						cell2 = row.createCell(2);
						if (record.getR15_net_spot_position() != null) {
							cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// Column D
						cell3 = row.createCell(3);
						if (record.getR15_net_forward_position() != null) {
							cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(numberStyle);
						}

// Column E
						cell4 = row.createCell(4);
						if (record.getR15_guarantees() != null) {
							cell4.setCellValue(record.getR15_guarantees().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(numberStyle);
						}

// Column F
						cell5 = row.createCell(5);
						if (record.getR15_net_future_inc_or_exp() != null) {
							cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(numberStyle);
						}

// Column G
						cell6 = row.createCell(6);
						if (record.getR15_net_delta_wei_fx_opt_posi() != null) {
							cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(numberStyle);
						}

// Column H
						cell7 = row.createCell(7);
						if (record.getR15_other_items() != null) {
							cell7.setCellValue(record.getR15_other_items().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(numberStyle);
						}

// Column I
						cell8 = row.createCell(8);
						if (record.getR15_net_long_position() != null) {
							cell8.setCellValue(record.getR15_net_long_position().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(numberStyle);
						}

// Column J
						cell9 = row.createCell(9);
						if (record.getR15_or() != null) {
							cell9.setCellValue(record.getR15_or().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(numberStyle);
						}

// Column K
						cell10 = row.createCell(10);
						if (record.getR15_net_short_position() != null) {
							cell10.setCellValue(record.getR15_net_short_position().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(numberStyle);
						}

// row17 (R16)
						row = sheet.getRow(15);

// Column C
						cell2 = row.createCell(2);
						if (record.getR16_net_spot_position() != null) {
							cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// Column D
						cell3 = row.createCell(3);
						if (record.getR16_net_forward_position() != null) {
							cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(numberStyle);
						}

// Column E
						cell4 = row.createCell(4);
						if (record.getR16_guarantees() != null) {
							cell4.setCellValue(record.getR16_guarantees().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(numberStyle);
						}

// Column F
						cell5 = row.createCell(5);
						if (record.getR16_net_future_inc_or_exp() != null) {
							cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(numberStyle);
						}

// Column G
						cell6 = row.createCell(6);
						if (record.getR16_net_delta_wei_fx_opt_posi() != null) {
							cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(numberStyle);
						}

// Column H
						cell7 = row.createCell(7);
						if (record.getR16_other_items() != null) {
							cell7.setCellValue(record.getR16_other_items().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(numberStyle);
						}

// Column I
						cell8 = row.createCell(8);
						if (record.getR16_net_long_position() != null) {
							cell8.setCellValue(record.getR16_net_long_position().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(numberStyle);
						}

// Column J
						cell9 = row.createCell(9);
						if (record.getR16_or() != null) {
							cell9.setCellValue(record.getR16_or().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(numberStyle);
						}

// Column K
						cell10 = row.createCell(10);
						if (record.getR16_net_short_position() != null) {
							cell10.setCellValue(record.getR16_net_short_position().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(numberStyle);
						}

						row = sheet.getRow(20);
// Column G
						Cell cell21 = row.createCell(6);
						if (record.getR21_long() != null) {
							cell21.setCellValue(record.getR21_long().doubleValue());
							cell21.setCellStyle(numberStyle);
						} else {
							cell21.setCellValue("");
							cell21.setCellStyle(numberStyle);
						}

// Column H
						Cell cell22 = row.createCell(7);
						if (record.getR21_short() != null) {
							cell22.setCellValue(record.getR21_short().doubleValue());
							cell22.setCellStyle(numberStyle);
						} else {
							cell22.setCellValue("");
							cell22.setCellStyle(numberStyle);
						}
// Column I
						Cell cell22I = row.createCell(8);
						if (record.getR21_total_gross_long_short() != null) {
							cell22I.setCellValue(record.getR21_total_gross_long_short().doubleValue());
							cell22I.setCellStyle(numberStyle);
						} else {
							cell22I.setCellValue("");
							cell22I.setCellStyle(numberStyle);
						}
// Column I
						Cell cell21I = row.createCell(9);
						if (record.getR21_net_position() != null) {
							cell21I.setCellValue(record.getR21_net_position().doubleValue());
							cell21I.setCellStyle(numberStyle);
						} else {
							cell21I.setCellValue("");
							cell21I.setCellStyle(numberStyle);
						}
						row = sheet.getRow(21);
// Column G
						cell22 = row.createCell(6);
						if (record.getR22_long() != null) {
							cell22.setCellValue(record.getR22_long().doubleValue());
							cell22.setCellStyle(numberStyle);
						} else {
							cell22.setCellValue("");
							cell22.setCellStyle(numberStyle);
						}

// Column H
						Cell cell23 = row.createCell(7);
						if (record.getR22_short() != null) {
							cell23.setCellValue(record.getR22_short().doubleValue());
							cell23.setCellStyle(numberStyle);
						} else {
							cell23.setCellValue("");
							cell23.setCellStyle(numberStyle);
						}
// Column I
						cell22I = row.createCell(8);
						if (record.getR22_total_gross_long_short() != null) {
							cell22I.setCellValue(record.getR22_total_gross_long_short().doubleValue());
							cell22I.setCellStyle(numberStyle);
						} else {
							cell22I.setCellValue("");
							cell22I.setCellStyle(numberStyle);
						}
// Column I
						cell21I = row.createCell(9);
						if (record.getR22_net_position() != null) {
							cell21I.setCellValue(record.getR22_net_position().doubleValue());
							cell21I.setCellStyle(numberStyle);
						} else {
							cell21I.setCellValue("");
							cell21I.setCellStyle(numberStyle);
						}
						row = sheet.getRow(22);
// Column I
						Cell cell23I = row.createCell(9);
						if (record.getR23_net_position() != null) {
							cell23I.setCellValue(record.getR23_net_position().doubleValue());
							cell23I.setCellStyle(numberStyle);
						} else {
							cell23I.setCellValue("");
							cell23I.setCellStyle(numberStyle);
						}
						row = sheet.getRow(28);
// Column I
						Cell cell28I = row.createCell(6);
						if (record.getR29_greater_net_long_or_short() != null) {
							cell28I.setCellValue(record.getR29_greater_net_long_or_short().doubleValue());
							cell28I.setCellStyle(numberStyle);
						} else {
							cell28I.setCellValue("");
							cell28I.setCellStyle(numberStyle);
						}
// Column I
						Cell cell28II = row.createCell(7);
						if (record.getR29_abs_value_net_gold_posi() != null) {
							cell28II.setCellValue(record.getR29_abs_value_net_gold_posi().doubleValue());
							cell28II.setCellStyle(numberStyle);
						} else {
							cell28II.setCellValue("");
							cell28II.setCellStyle(numberStyle);
						}

// Column I
						Cell cell29 = row.createCell(9);
						if (record.getR29_capital_charge() != null) {
							cell29.setCellValue(record.getR29_capital_charge().doubleValue());
							cell29.setCellStyle(numberStyle);
						} else {
							cell29.setCellValue("");
							cell29.setCellStyle(numberStyle);
						}

//						row = sheet.getRow(28);
//// Column I
//						Cell cell30 = row.createCell(8);
//						if (record.getR30_capital_require() != null) {
//							cell30.setCellValue(record.getR30_capital_require().doubleValue());
//							cell30.setCellStyle(numberStyle);
//						} else {
//							cell30.setCellValue("");
//							cell30.setCellStyle(numberStyle);
//						}

//EMAIL

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
	public byte[] getExcelM_FXRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_FXREmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_FXR_Archival_Summary_Entity> dataList = M_FXR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_FXR report. Returning empty result.");
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

					M_FXR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR11_net_spot_position() != null) {
						cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_net_forward_position() != null) {
						cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_guarantees() != null) {
						cell4.setCellValue(record.getR11_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR11_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR11_other_items() != null) {
						cell7.setCellValue(record.getR11_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR11_net_long_position() != null) {
						cell8.setCellValue(record.getR11_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row12
					// Column J
					Cell cell9 = row.createCell(9);
					if (record.getR11_or() != null) {
						cell9.setCellValue(record.getR11_or().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row12
					// Column K
					Cell cell10 = row.createCell(10);
					if (record.getR11_net_short_position() != null) {
						cell10.setCellValue(record.getR11_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);

					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);

					}

					// row13
					row = sheet.getRow(11);
					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR12_net_spot_position() != null) {
						cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR12_net_forward_position() != null) {
						cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_guarantees() != null) {
						cell4.setCellValue(record.getR12_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR12_other_items() != null) {
						cell7.setCellValue(record.getR12_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR12_net_long_position() != null) {
						cell8.setCellValue(record.getR12_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row13
					// Column J
					cell9 = row.createCell(9);
					if (record.getR12_or() != null) {
						cell9.setCellValue(record.getR12_or().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row12
					// Column K
					Cell cell12 = row.createCell(10);
					if (record.getR12_net_short_position() != null) {
						cell12.setCellValue(record.getR12_net_short_position().doubleValue());
						cell12.setCellStyle(numberStyle);

					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(numberStyle);

					}

					// row14 (R13)
					row = sheet.getRow(12);

// Column C
					cell2 = row.createCell(2);
					if (record.getR13_net_spot_position() != null) {
						cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR13_net_forward_position() != null) {
						cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR13_guarantees() != null) {
						cell4.setCellValue(record.getR13_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR13_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR13_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR13_other_items() != null) {
						cell7.setCellValue(record.getR13_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR13_net_long_position() != null) {
						cell8.setCellValue(record.getR13_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR13_or() != null) {
						cell9.setCellValue(record.getR13_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR13_net_short_position() != null) {
						cell10.setCellValue(record.getR13_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

// row15  (R14)
					row = sheet.getRow(13);

// Column C
					cell2 = row.createCell(2);
					if (record.getR14_net_spot_position() != null) {
						cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR14_net_forward_position() != null) {
						cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR14_guarantees() != null) {
						cell4.setCellValue(record.getR14_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR14_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR14_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR14_other_items() != null) {
						cell7.setCellValue(record.getR14_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR14_net_long_position() != null) {
						cell8.setCellValue(record.getR14_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR14_or() != null) {
						cell9.setCellValue(record.getR14_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR14_net_short_position() != null) {
						cell10.setCellValue(record.getR14_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}
// row16 (R15)
					row = sheet.getRow(14);

// Column C
					cell2 = row.createCell(2);
					if (record.getR15_net_spot_position() != null) {
						cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR15_net_forward_position() != null) {
						cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR15_guarantees() != null) {
						cell4.setCellValue(record.getR15_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR15_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR15_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR15_other_items() != null) {
						cell7.setCellValue(record.getR15_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR15_net_long_position() != null) {
						cell8.setCellValue(record.getR15_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR15_or() != null) {
						cell9.setCellValue(record.getR15_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR15_net_short_position() != null) {
						cell10.setCellValue(record.getR15_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

// row17 (R16)
					row = sheet.getRow(15);

// Column C
					cell2 = row.createCell(2);
					if (record.getR16_net_spot_position() != null) {
						cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR16_net_forward_position() != null) {
						cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR16_guarantees() != null) {
						cell4.setCellValue(record.getR16_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR16_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR16_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR16_other_items() != null) {
						cell7.setCellValue(record.getR16_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR16_net_long_position() != null) {
						cell8.setCellValue(record.getR16_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR16_or() != null) {
						cell9.setCellValue(record.getR16_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR16_net_short_position() != null) {
						cell10.setCellValue(record.getR16_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

					row = sheet.getRow(20);
// Column G
					Cell cell21 = row.createCell(6);
					if (record.getR21_long() != null) {
						cell21.setCellValue(record.getR21_long().doubleValue());
						cell21.setCellStyle(numberStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(numberStyle);
					}

// Column H
					Cell cell22 = row.createCell(7);
					if (record.getR21_short() != null) {
						cell22.setCellValue(record.getR21_short().doubleValue());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(numberStyle);
					}
					row = sheet.getRow(21);
// Column G
					cell22 = row.createCell(6);
					if (record.getR22_long() != null) {
						cell22.setCellValue(record.getR22_long().doubleValue());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(numberStyle);
					}

// Column H
					Cell cell23 = row.createCell(7);
					if (record.getR22_short() != null) {
						cell23.setCellValue(record.getR22_short().doubleValue());
						cell23.setCellStyle(numberStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(numberStyle);
					}
//					row = sheet.getRow(29);
//// Column I
//					Cell cell29 = row.createCell(8);
//					if (record.getR30_capital_require() != null) {
//						cell29.setCellValue(record.getR30_capital_require().doubleValue());
//						cell29.setCellStyle(numberStyle);
//					} else {
//						cell29.setCellValue("");
//						cell23.setCellStyle(numberStyle);
//					}
//NORMAL

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
	public byte[] BRRS_M_FXREmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_FXR_Archival_Summary_Entity> dataList = M_FXR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_FXR report. Returning empty result.");
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

//EMAIL
			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_FXR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR11_net_spot_position() != null) {
						cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_net_forward_position() != null) {
						cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_guarantees() != null) {
						cell4.setCellValue(record.getR11_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR11_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR11_other_items() != null) {
						cell7.setCellValue(record.getR11_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR11_net_long_position() != null) {
						cell8.setCellValue(record.getR11_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row12
					// Column J
					Cell cell9 = row.createCell(9);
					if (record.getR11_or() != null) {
						cell9.setCellValue(record.getR11_or().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row12
					// Column K
					Cell cell10 = row.createCell(10);
					if (record.getR11_net_short_position() != null) {
						cell10.setCellValue(record.getR11_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);

					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);

					}

					// row13
					row = sheet.getRow(11);
					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR12_net_spot_position() != null) {
						cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR12_net_forward_position() != null) {
						cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_guarantees() != null) {
						cell4.setCellValue(record.getR12_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR12_other_items() != null) {
						cell7.setCellValue(record.getR12_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR12_net_long_position() != null) {
						cell8.setCellValue(record.getR12_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row13
					// Column J
					cell9 = row.createCell(9);
					if (record.getR12_or() != null) {
						cell9.setCellValue(record.getR12_or().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row12
					// Column K
					Cell cell12 = row.createCell(10);
					if (record.getR12_net_short_position() != null) {
						cell12.setCellValue(record.getR12_net_short_position().doubleValue());
						cell12.setCellStyle(numberStyle);

					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(numberStyle);

					}

					// row14 (R13)
					row = sheet.getRow(12);

// Column C
					cell2 = row.createCell(2);
					if (record.getR13_net_spot_position() != null) {
						cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR13_net_forward_position() != null) {
						cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR13_guarantees() != null) {
						cell4.setCellValue(record.getR13_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR13_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR13_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR13_other_items() != null) {
						cell7.setCellValue(record.getR13_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR13_net_long_position() != null) {
						cell8.setCellValue(record.getR13_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR13_or() != null) {
						cell9.setCellValue(record.getR13_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR13_net_short_position() != null) {
						cell10.setCellValue(record.getR13_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

// row15  (R14)
					row = sheet.getRow(13);

// Column C
					cell2 = row.createCell(2);
					if (record.getR14_net_spot_position() != null) {
						cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR14_net_forward_position() != null) {
						cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR14_guarantees() != null) {
						cell4.setCellValue(record.getR14_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR14_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR14_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR14_other_items() != null) {
						cell7.setCellValue(record.getR14_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR14_net_long_position() != null) {
						cell8.setCellValue(record.getR14_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR14_or() != null) {
						cell9.setCellValue(record.getR14_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR14_net_short_position() != null) {
						cell10.setCellValue(record.getR14_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}
// row16 (R15)
					row = sheet.getRow(14);

// Column C
					cell2 = row.createCell(2);
					if (record.getR15_net_spot_position() != null) {
						cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR15_net_forward_position() != null) {
						cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR15_guarantees() != null) {
						cell4.setCellValue(record.getR15_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR15_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR15_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR15_other_items() != null) {
						cell7.setCellValue(record.getR15_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR15_net_long_position() != null) {
						cell8.setCellValue(record.getR15_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR15_or() != null) {
						cell9.setCellValue(record.getR15_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR15_net_short_position() != null) {
						cell10.setCellValue(record.getR15_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

// row17 (R16)
					row = sheet.getRow(15);

// Column C
					cell2 = row.createCell(2);
					if (record.getR16_net_spot_position() != null) {
						cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR16_net_forward_position() != null) {
						cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR16_guarantees() != null) {
						cell4.setCellValue(record.getR16_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR16_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR16_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR16_other_items() != null) {
						cell7.setCellValue(record.getR16_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR16_net_long_position() != null) {
						cell8.setCellValue(record.getR16_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR16_or() != null) {
						cell9.setCellValue(record.getR16_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR16_net_short_position() != null) {
						cell10.setCellValue(record.getR16_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

					row = sheet.getRow(20);
// Column G
					Cell cell21 = row.createCell(6);
					if (record.getR21_long() != null) {
						cell21.setCellValue(record.getR21_long().doubleValue());
						cell21.setCellStyle(numberStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(numberStyle);
					}

// Column H
					Cell cell22 = row.createCell(7);
					if (record.getR21_short() != null) {
						cell22.setCellValue(record.getR21_short().doubleValue());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(numberStyle);
					}
// Column I
					Cell cell22I = row.createCell(8);
					if (record.getR21_total_gross_long_short() != null) {
						cell22I.setCellValue(record.getR21_total_gross_long_short().doubleValue());
						cell22I.setCellStyle(numberStyle);
					} else {
						cell22I.setCellValue("");
						cell22I.setCellStyle(numberStyle);
					}
// Column I
					Cell cell21I = row.createCell(9);
					if (record.getR21_net_position() != null) {
						cell21I.setCellValue(record.getR21_net_position().doubleValue());
						cell21I.setCellStyle(numberStyle);
					} else {
						cell21I.setCellValue("");
						cell21I.setCellStyle(numberStyle);
					}
					row = sheet.getRow(21);
// Column G
					cell22 = row.createCell(6);
					if (record.getR22_long() != null) {
						cell22.setCellValue(record.getR22_long().doubleValue());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(numberStyle);
					}

// Column H
					Cell cell23 = row.createCell(7);
					if (record.getR22_short() != null) {
						cell23.setCellValue(record.getR22_short().doubleValue());
						cell23.setCellStyle(numberStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(numberStyle);
					}
// Column I
					cell22I = row.createCell(8);
					if (record.getR22_total_gross_long_short() != null) {
						cell22I.setCellValue(record.getR22_total_gross_long_short().doubleValue());
						cell22I.setCellStyle(numberStyle);
					} else {
						cell22I.setCellValue("");
						cell22I.setCellStyle(numberStyle);
					}
// Column I
					cell21I = row.createCell(9);
					if (record.getR22_net_position() != null) {
						cell21I.setCellValue(record.getR22_net_position().doubleValue());
						cell21I.setCellStyle(numberStyle);
					} else {
						cell21I.setCellValue("");
						cell21I.setCellStyle(numberStyle);
					}
					row = sheet.getRow(22);
// Column I
					Cell cell23I = row.createCell(9);
					if (record.getR23_net_position() != null) {
						cell23I.setCellValue(record.getR23_net_position().doubleValue());
						cell23I.setCellStyle(numberStyle);
					} else {
						cell23I.setCellValue("");
						cell23I.setCellStyle(numberStyle);
					}
					row = sheet.getRow(28);
// Column I
					Cell cell28I = row.createCell(6);
					if (record.getR29_greater_net_long_or_short() != null) {
						cell28I.setCellValue(record.getR29_greater_net_long_or_short().doubleValue());
						cell28I.setCellStyle(numberStyle);
					} else {
						cell28I.setCellValue("");
						cell28I.setCellStyle(numberStyle);
					}
// Column I
					Cell cell28II = row.createCell(7);
					if (record.getR29_abs_value_net_gold_posi() != null) {
						cell28II.setCellValue(record.getR29_abs_value_net_gold_posi().doubleValue());
						cell28II.setCellStyle(numberStyle);
					} else {
						cell28II.setCellValue("");
						cell28II.setCellStyle(numberStyle);
					}

// Column I
					Cell cell29 = row.createCell(9);
					if (record.getR29_capital_charge() != null) {
						cell29.setCellValue(record.getR29_capital_charge().doubleValue());
						cell29.setCellStyle(numberStyle);
					} else {
						cell29.setCellValue("");
						cell29.setCellStyle(numberStyle);
					}

//					row = sheet.getRow(28);
//// Column I
//					Cell cell30 = row.createCell(8);
//					if (record.getR30_capital_require() != null) {
//						cell30.setCellValue(record.getR30_capital_require().doubleValue());
//						cell30.setCellStyle(numberStyle);
//					} else {
//						cell30.setCellValue("");
//						cell30.setCellStyle(numberStyle);
//					}

//EMAIL

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
	public byte[] BRRS_M_FXRResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_FXRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_FXR_Resub_Summary_Entity> dataList = brrs_M_FXR_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_FXR report. Returning empty result.");
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

					M_FXR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR11_net_spot_position() != null) {
						cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_net_forward_position() != null) {
						cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_guarantees() != null) {
						cell4.setCellValue(record.getR11_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR11_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR11_other_items() != null) {
						cell7.setCellValue(record.getR11_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR11_net_long_position() != null) {
						cell8.setCellValue(record.getR11_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row12
					// Column J
					Cell cell9 = row.createCell(9);
					if (record.getR11_or() != null) {
						cell9.setCellValue(record.getR11_or().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row12
					// Column K
					Cell cell10 = row.createCell(10);
					if (record.getR11_net_short_position() != null) {
						cell10.setCellValue(record.getR11_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);

					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);

					}

					// row13
					row = sheet.getRow(11);
					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR12_net_spot_position() != null) {
						cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR12_net_forward_position() != null) {
						cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_guarantees() != null) {
						cell4.setCellValue(record.getR12_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR12_other_items() != null) {
						cell7.setCellValue(record.getR12_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR12_net_long_position() != null) {
						cell8.setCellValue(record.getR12_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row13
					// Column J
					cell9 = row.createCell(9);
					if (record.getR12_or() != null) {
						cell9.setCellValue(record.getR12_or().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row12
					// Column K
					Cell cell12 = row.createCell(10);
					if (record.getR12_net_short_position() != null) {
						cell12.setCellValue(record.getR12_net_short_position().doubleValue());
						cell12.setCellStyle(numberStyle);

					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(numberStyle);

					}

					// row14 (R13)
					row = sheet.getRow(12);

// Column C
					cell2 = row.createCell(2);
					if (record.getR13_net_spot_position() != null) {
						cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR13_net_forward_position() != null) {
						cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR13_guarantees() != null) {
						cell4.setCellValue(record.getR13_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR13_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR13_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR13_other_items() != null) {
						cell7.setCellValue(record.getR13_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR13_net_long_position() != null) {
						cell8.setCellValue(record.getR13_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR13_or() != null) {
						cell9.setCellValue(record.getR13_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR13_net_short_position() != null) {
						cell10.setCellValue(record.getR13_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

// row15  (R14)
					row = sheet.getRow(13);

// Column C
					cell2 = row.createCell(2);
					if (record.getR14_net_spot_position() != null) {
						cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR14_net_forward_position() != null) {
						cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR14_guarantees() != null) {
						cell4.setCellValue(record.getR14_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR14_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR14_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR14_other_items() != null) {
						cell7.setCellValue(record.getR14_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR14_net_long_position() != null) {
						cell8.setCellValue(record.getR14_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR14_or() != null) {
						cell9.setCellValue(record.getR14_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR14_net_short_position() != null) {
						cell10.setCellValue(record.getR14_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}
// row16 (R15)
					row = sheet.getRow(14);

// Column C
					cell2 = row.createCell(2);
					if (record.getR15_net_spot_position() != null) {
						cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR15_net_forward_position() != null) {
						cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR15_guarantees() != null) {
						cell4.setCellValue(record.getR15_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR15_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR15_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR15_other_items() != null) {
						cell7.setCellValue(record.getR15_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR15_net_long_position() != null) {
						cell8.setCellValue(record.getR15_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR15_or() != null) {
						cell9.setCellValue(record.getR15_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR15_net_short_position() != null) {
						cell10.setCellValue(record.getR15_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

// row17 (R16)
					row = sheet.getRow(15);

// Column C
					cell2 = row.createCell(2);
					if (record.getR16_net_spot_position() != null) {
						cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR16_net_forward_position() != null) {
						cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR16_guarantees() != null) {
						cell4.setCellValue(record.getR16_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR16_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR16_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR16_other_items() != null) {
						cell7.setCellValue(record.getR16_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR16_net_long_position() != null) {
						cell8.setCellValue(record.getR16_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR16_or() != null) {
						cell9.setCellValue(record.getR16_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR16_net_short_position() != null) {
						cell10.setCellValue(record.getR16_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

					row = sheet.getRow(20);
// Column G
					Cell cell21 = row.createCell(6);
					if (record.getR21_long() != null) {
						cell21.setCellValue(record.getR21_long().doubleValue());
						cell21.setCellStyle(numberStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(numberStyle);
					}

// Column H
					Cell cell22 = row.createCell(7);
					if (record.getR21_short() != null) {
						cell22.setCellValue(record.getR21_short().doubleValue());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(numberStyle);
					}
					row = sheet.getRow(21);
// Column G
					Cell cell22g = row.createCell(6);
					if (record.getR22_long() != null) {
						cell22g.setCellValue(record.getR22_long().doubleValue());
						cell22g.setCellStyle(numberStyle);
					} else {
						cell22g.setCellValue("");
						cell22g.setCellStyle(numberStyle);
					}

// Column H
					Cell cell23 = row.createCell(7);
					if (record.getR22_short() != null) {
						cell23.setCellValue(record.getR22_short().doubleValue());
						cell23.setCellStyle(numberStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(numberStyle);
					}
//					row = sheet.getRow(29);
//// Column I
//					Cell cell29 = row.createCell(8);
//					if (record.getR30_capital_require() != null) {
//						cell29.setCellValue(record.getR30_capital_require().doubleValue());
//						cell29.setCellStyle(numberStyle);
//					} else {
//						cell29.setCellValue("");
//						cell23.setCellStyle(numberStyle);
//					}
//NORMAL

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
	public byte[] BRRS_M_FXRResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_FXR_Resub_Summary_Entity> dataList = brrs_M_FXR_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_FXR report. Returning empty result.");
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

//EMAIL
			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_FXR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR11_net_spot_position() != null) {
						cell2.setCellValue(record.getR11_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_net_forward_position() != null) {
						cell3.setCellValue(record.getR11_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_guarantees() != null) {
						cell4.setCellValue(record.getR11_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR11_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR11_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR11_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR11_other_items() != null) {
						cell7.setCellValue(record.getR11_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR11_net_long_position() != null) {
						cell8.setCellValue(record.getR11_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row12
					// Column J
					Cell cell9 = row.createCell(9);
					if (record.getR11_or() != null) {
						cell9.setCellValue(record.getR11_or().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row12
					// Column K
					Cell cell10 = row.createCell(10);
					if (record.getR11_net_short_position() != null) {
						cell10.setCellValue(record.getR11_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);

					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);

					}

					// row13
					row = sheet.getRow(11);
					// row13
					// Column C
					cell2 = row.createCell(2);
					if (record.getR12_net_spot_position() != null) {
						cell2.setCellValue(record.getR12_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR12_net_forward_position() != null) {
						cell3.setCellValue(record.getR12_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_guarantees() != null) {
						cell4.setCellValue(record.getR12_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR12_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR12_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR12_other_items() != null) {
						cell7.setCellValue(record.getR12_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR12_net_long_position() != null) {
						cell8.setCellValue(record.getR12_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row13
					// Column J
					cell9 = row.createCell(9);
					if (record.getR12_or() != null) {
						cell9.setCellValue(record.getR12_or().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row12
					// Column K
					Cell cell12 = row.createCell(10);
					if (record.getR12_net_short_position() != null) {
						cell12.setCellValue(record.getR12_net_short_position().doubleValue());
						cell12.setCellStyle(numberStyle);

					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(numberStyle);

					}

					// row14 (R13)
					row = sheet.getRow(12);

// Column C
					cell2 = row.createCell(2);
					if (record.getR13_net_spot_position() != null) {
						cell2.setCellValue(record.getR13_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR13_net_forward_position() != null) {
						cell3.setCellValue(record.getR13_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR13_guarantees() != null) {
						cell4.setCellValue(record.getR13_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR13_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR13_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR13_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR13_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR13_other_items() != null) {
						cell7.setCellValue(record.getR13_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR13_net_long_position() != null) {
						cell8.setCellValue(record.getR13_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR13_or() != null) {
						cell9.setCellValue(record.getR13_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR13_net_short_position() != null) {
						cell10.setCellValue(record.getR13_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

// row15  (R14)
					row = sheet.getRow(13);

// Column C
					cell2 = row.createCell(2);
					if (record.getR14_net_spot_position() != null) {
						cell2.setCellValue(record.getR14_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR14_net_forward_position() != null) {
						cell3.setCellValue(record.getR14_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR14_guarantees() != null) {
						cell4.setCellValue(record.getR14_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR14_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR14_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR14_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR14_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR14_other_items() != null) {
						cell7.setCellValue(record.getR14_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR14_net_long_position() != null) {
						cell8.setCellValue(record.getR14_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR14_or() != null) {
						cell9.setCellValue(record.getR14_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR14_net_short_position() != null) {
						cell10.setCellValue(record.getR14_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}
// row16 (R15)
					row = sheet.getRow(14);

// Column C
					cell2 = row.createCell(2);
					if (record.getR15_net_spot_position() != null) {
						cell2.setCellValue(record.getR15_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR15_net_forward_position() != null) {
						cell3.setCellValue(record.getR15_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR15_guarantees() != null) {
						cell4.setCellValue(record.getR15_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR15_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR15_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR15_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR15_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR15_other_items() != null) {
						cell7.setCellValue(record.getR15_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR15_net_long_position() != null) {
						cell8.setCellValue(record.getR15_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR15_or() != null) {
						cell9.setCellValue(record.getR15_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR15_net_short_position() != null) {
						cell10.setCellValue(record.getR15_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

// row17 (R16)
					row = sheet.getRow(15);

// Column C
					cell2 = row.createCell(2);
					if (record.getR16_net_spot_position() != null) {
						cell2.setCellValue(record.getR16_net_spot_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// Column D
					cell3 = row.createCell(3);
					if (record.getR16_net_forward_position() != null) {
						cell3.setCellValue(record.getR16_net_forward_position().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);
					}

// Column E
					cell4 = row.createCell(4);
					if (record.getR16_guarantees() != null) {
						cell4.setCellValue(record.getR16_guarantees().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);
					}

// Column F
					cell5 = row.createCell(5);
					if (record.getR16_net_future_inc_or_exp() != null) {
						cell5.setCellValue(record.getR16_net_future_inc_or_exp().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);
					}

// Column G
					cell6 = row.createCell(6);
					if (record.getR16_net_delta_wei_fx_opt_posi() != null) {
						cell6.setCellValue(record.getR16_net_delta_wei_fx_opt_posi().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

// Column H
					cell7 = row.createCell(7);
					if (record.getR16_other_items() != null) {
						cell7.setCellValue(record.getR16_other_items().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);
					}

// Column I
					cell8 = row.createCell(8);
					if (record.getR16_net_long_position() != null) {
						cell8.setCellValue(record.getR16_net_long_position().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);
					}

// Column J
					cell9 = row.createCell(9);
					if (record.getR16_or() != null) {
						cell9.setCellValue(record.getR16_or().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);
					}

// Column K
					cell10 = row.createCell(10);
					if (record.getR16_net_short_position() != null) {
						cell10.setCellValue(record.getR16_net_short_position().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(numberStyle);
					}

					row = sheet.getRow(20);
// Column G
					Cell cell21 = row.createCell(6);
					if (record.getR21_long() != null) {
						cell21.setCellValue(record.getR21_long().doubleValue());
						cell21.setCellStyle(numberStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(numberStyle);
					}

// Column H
					Cell cell22 = row.createCell(7);
					if (record.getR21_short() != null) {
						cell22.setCellValue(record.getR21_short().doubleValue());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(numberStyle);
					}
// Column I
					Cell cell22I = row.createCell(8);
					if (record.getR21_total_gross_long_short() != null) {
						cell22I.setCellValue(record.getR21_total_gross_long_short().doubleValue());
						cell22I.setCellStyle(numberStyle);
					} else {
						cell22I.setCellValue("");
						cell22I.setCellStyle(numberStyle);
					}
// Column I
					Cell cell21I = row.createCell(9);
					if (record.getR21_net_position() != null) {
						cell21I.setCellValue(record.getR21_net_position().doubleValue());
						cell21I.setCellStyle(numberStyle);
					} else {
						cell21I.setCellValue("");
						cell21I.setCellStyle(numberStyle);
					}
					row = sheet.getRow(21);
// Column G
					cell22 = row.createCell(6);
					if (record.getR22_long() != null) {
						cell22.setCellValue(record.getR22_long().doubleValue());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(numberStyle);
					}

// Column H
					Cell cell23 = row.createCell(7);
					if (record.getR22_short() != null) {
						cell23.setCellValue(record.getR22_short().doubleValue());
						cell23.setCellStyle(numberStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(numberStyle);
					}
// Column I
					cell22I = row.createCell(8);
					if (record.getR22_total_gross_long_short() != null) {
						cell22I.setCellValue(record.getR22_total_gross_long_short().doubleValue());
						cell22I.setCellStyle(numberStyle);
					} else {
						cell22I.setCellValue("");
						cell22I.setCellStyle(numberStyle);
					}
// Column I
					cell21I = row.createCell(9);
					if (record.getR22_net_position() != null) {
						cell21I.setCellValue(record.getR22_net_position().doubleValue());
						cell21I.setCellStyle(numberStyle);
					} else {
						cell21I.setCellValue("");
						cell21I.setCellStyle(numberStyle);
					}
					row = sheet.getRow(22);
// Column I
					Cell cell23I = row.createCell(9);
					if (record.getR23_net_position() != null) {
						cell23I.setCellValue(record.getR23_net_position().doubleValue());
						cell23I.setCellStyle(numberStyle);
					} else {
						cell23I.setCellValue("");
						cell23I.setCellStyle(numberStyle);
					}
					row = sheet.getRow(28);
// Column I
					Cell cell28I = row.createCell(6);
					if (record.getR29_greater_net_long_or_short() != null) {
						cell28I.setCellValue(record.getR29_greater_net_long_or_short().doubleValue());
						cell28I.setCellStyle(numberStyle);
					} else {
						cell28I.setCellValue("");
						cell28I.setCellStyle(numberStyle);
					}
// Column I
					Cell cell28II = row.createCell(7);
					if (record.getR29_abs_value_net_gold_posi() != null) {
						cell28II.setCellValue(record.getR29_abs_value_net_gold_posi().doubleValue());
						cell28II.setCellStyle(numberStyle);
					} else {
						cell28II.setCellValue("");
						cell28II.setCellStyle(numberStyle);
					}

// Column I
					Cell cell29 = row.createCell(9);
					if (record.getR29_capital_charge() != null) {
						cell29.setCellValue(record.getR29_capital_charge().doubleValue());
						cell29.setCellStyle(numberStyle);
					} else {
						cell29.setCellValue("");
						cell29.setCellStyle(numberStyle);
					}

//					row = sheet.getRow(28);
//// Column I
//					Cell cell30 = row.createCell(8);
//					if (record.getR30_capital_require() != null) {
//						cell30.setCellValue(record.getR30_capital_require().doubleValue());
//						cell30.setCellStyle(numberStyle);
//					} else {
//						cell30.setCellValue("");
//						cell30.setCellStyle(numberStyle);
//					}

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