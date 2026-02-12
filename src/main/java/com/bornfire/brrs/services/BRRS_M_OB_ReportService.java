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

import com.bornfire.brrs.entities.BRRS_M_OB_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OB_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OB_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_OB_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_OB_Summary_Repo;
import com.bornfire.brrs.entities.M_LA2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_OB_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Detail_Entity;
import com.bornfire.brrs.entities.M_OB_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_OB_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_OB_Summary_Entity;

@Component
@Service

public class BRRS_M_OB_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OB_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_OB_Summary_Repo brrs_M_OB_summary_repo;

	@Autowired
	BRRS_M_OB_Detail_Repo brrs_M_OB_detail_repo;

	@Autowired
	BRRS_M_OB_Archival_Summary_Repo M_OB_Archival_Summary_Repo;

	@Autowired
	BRRS_M_OB_Archival_Detail_Repo BRRS_M_OB_Archival_Detail_Repo;

	@Autowired
	BRRS_M_OB_Resub_Summary_Repo M_OB_Resub_Summary_Repo;

	@Autowired
	BRRS_M_OB_Resub_Detail_Repo M_OB_Resub_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_OBView(String reportId, String fromdate, String todate, String currency,
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
				List<M_OB_Archival_Summary_Entity> T1Master = M_OB_Archival_Summary_Repo.getdatabydateListarchival(d1,
						version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_OB_Resub_Summary_Entity> T1Master = M_OB_Resub_Summary_Repo.getdatabydateListarchival(d1,
						version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_OB_Summary_Entity> T1Master = brrs_M_OB_summary_repo.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_OB_Archival_Detail_Entity> T1Master = BRRS_M_OB_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_OB_Resub_Detail_Entity> T1Master = M_OB_Resub_Detail_Repo.getdatabydateListarchival(d1,
							version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_OB_Detail_Entity> T1Master = brrs_M_OB_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_OB");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_OB_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// 1️⃣ Fetch existing SUMMARY
		M_OB_Summary_Entity existingSummary = brrs_M_OB_summary_repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		// 2️⃣ Fetch or create DETAIL
		M_OB_Detail_Entity existingDetail = brrs_M_OB_detail_repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					M_OB_Detail_Entity d = new M_OB_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});
		try {
			// ❌ Rows to skip in main loop (formula rows)
			int[] skipRows = { 15, 29, 38, 41, 44, 49, 52, 58, 64 };

			// 🔁 Main loop: R12 → R63
			for (int i = 12; i <= 63; i++) {

				boolean skip = false;
				for (int s : skipRows) {
					if (i == s) {
						skip = true;
						break;
					}
				}
				if (skip)
					continue;

				String prefix = "R" + i + "_";
				String[] fields = { "OTHER_BORROW" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_OB_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_OB_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Method detailSetter = M_OB_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						System.out.println("Updating " + prefix + field + " = " + newValue);

						// ✅ set into SUMMARY (managed entity)
						summarySetter.invoke(existingSummary, newValue);

						// ✅ set into DETAIL (managed entity)
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						// skip missing fields safely
					}
				}
			}

			// 🔁 Formula rows
			int[] targetRows = { 11, 15, 29, 38, 41, 44, 49, 52, 58, 64 };

			for (int i : targetRows) {

				String prefix = "R" + i + "_";
				String[] fields = { "OTHER_BORROW" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_OB_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_OB_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Method detailSetter = M_OB_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						System.out.println("Updating formula " + prefix + field + " = " + newValue);

						summarySetter.invoke(existingSummary, newValue);
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						// skip missing fields
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while updating OB report fields", e);
		}

		// 3️⃣ Save & FLUSH BOTH (force DB write)
		brrs_M_OB_summary_repo.saveAndFlush(existingSummary);
		brrs_M_OB_detail_repo.saveAndFlush(existingDetail);

		System.out.println("✅ OB Summary and Detail updated successfully for date: ");
	}

	public void updateResubReport(M_OB_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = M_OB_Resub_Summary_Repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_OB_Resub_Summary_Entity resubSummary = new M_OB_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_OB_Resub_Detail_Entity resubDetail = new M_OB_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_OB_Archival_Summary_Entity archSummary = new M_OB_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_OB_Archival_Detail_Entity archDetail = new M_OB_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		M_OB_Resub_Summary_Repo.save(resubSummary);
		M_OB_Resub_Detail_Repo.save(resubDetail);

		M_OB_Archival_Summary_Repo.save(archSummary);
		BRRS_M_OB_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_OBResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_OB_Archival_Summary_Entity> latestArchivalList = M_OB_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_OB_Archival_Summary_Entity entity : latestArchivalList) {
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

	public List<Object[]> getM_OBArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_OB_Archival_Summary_Entity> repoData = M_OB_Archival_Summary_Repo.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_OB_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_OB_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_OB Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_OBExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_OBARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OBResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_OBEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_OB_Summary_Entity> dataList = brrs_M_OB_summary_repo.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_OB report. Returning empty result.");
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
							M_OB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

//							Cell cell2 = row.createCell(1);
//							if (record.getR11_OTHER_BORROW() != null) {
//								cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//								cell2.setCellStyle(numberStyle);
//							} else {
//								cell2.setCellValue("");
//								cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(11);

							Cell cell2 = row.createCell(1);
							if (record.getR12_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(12);

							cell2 = row.createCell(1);
							if (record.getR13_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);
							cell2 = row.createCell(1);
							if (record.getR14_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(14);
//							cell2 = row.createCell(1);
//							if (record.getR15_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(15);
							cell2 = row.createCell(1);
							if (record.getR16_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(16);
							cell2 = row.createCell(1);
							if (record.getR17_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(17);
							cell2 = row.createCell(1);
							if (record.getR18_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(18);
							cell2 = row.createCell(1);
							if (record.getR19_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(19);
							cell2 = row.createCell(1);
							if (record.getR20_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(20);
							cell2 = row.createCell(1);
							if (record.getR21_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(21);
							cell2 = row.createCell(1);
							if (record.getR22_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(22);
							cell2 = row.createCell(1);
							if (record.getR23_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(23);
							cell2 = row.createCell(1);
							if (record.getR24_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(24);
							cell2 = row.createCell(1);
							if (record.getR25_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(25);
							cell2 = row.createCell(1);
							if (record.getR26_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(26);
							cell2 = row.createCell(1);
							if (record.getR27_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(27);
							cell2 = row.createCell(1);
							if (record.getR28_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(28);
//							cell2 = row.createCell(1);
//							if (record.getR29_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(29);
							cell2 = row.createCell(1);
							if (record.getR30_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(30);
							cell2 = row.createCell(1);
							if (record.getR31_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(31);
							cell2 = row.createCell(1);
							if (record.getR32_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(32);
							cell2 = row.createCell(1);
							if (record.getR33_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(33);
							cell2 = row.createCell(1);
							if (record.getR34_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(34);
							cell2 = row.createCell(1);
							if (record.getR35_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(35);
							cell2 = row.createCell(1);
							if (record.getR36_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(36);
							cell2 = row.createCell(1);
							if (record.getR37_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(37);
//							cell2 = row.createCell(1);
//							if (record.getR38_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(38);
							cell2 = row.createCell(1);
							if (record.getR39_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(39);
							cell2 = row.createCell(1);
							if (record.getR40_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(40);
//							cell2 = row.createCell(1);
//							if (record.getR41_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(41);
							cell2 = row.createCell(1);
							if (record.getR42_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(42);
							cell2 = row.createCell(1);
							if (record.getR43_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(43);
//							cell2 = row.createCell(1);
//							if (record.getR44_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(44);
							cell2 = row.createCell(1);
							if (record.getR45_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(45);
							cell2 = row.createCell(1);
							if (record.getR46_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(46);
							cell2 = row.createCell(1);
							if (record.getR47_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(47);
							cell2 = row.createCell(1);
							if (record.getR48_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(48);
//							cell2 = row.createCell(1);
//							if (record.getR49_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(49);
							cell2 = row.createCell(1);
							if (record.getR50_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(50);
							cell2 = row.createCell(1);
							if (record.getR51_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(51);
//							cell2 = row.createCell(1);
//							if (record.getR52_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(52);
							cell2 = row.createCell(1);
							if (record.getR53_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(53);
							cell2 = row.createCell(1);
							if (record.getR54_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(54);
							cell2 = row.createCell(1);
							if (record.getR55_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(55);
							cell2 = row.createCell(1);
							if (record.getR56_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(56);
							cell2 = row.createCell(1);
							if (record.getR57_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(57);
//							cell2 = row.createCell(1);
//							if (record.getR58_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(58);
							cell2 = row.createCell(1);
							if (record.getR59_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(59);
							cell2 = row.createCell(1);
							if (record.getR60_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(60);
							cell2 = row.createCell(1);
							if (record.getR61_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(61);
							cell2 = row.createCell(1);
							if (record.getR62_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(62);
							cell2 = row.createCell(1);
							if (record.getR63_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							row = sheet.getRow(63);
//							
//							cell2 = row.createCell(1);
//							if (record.getR64_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

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
	public byte[] BRRS_M_OBEmailExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OBArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OBResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_OB_Summary_Entity> dataList = brrs_M_OB_summary_repo.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_OB report. Returning empty result.");
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
						M_OB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

//						Cell cell2 = row.createCell(1);
//						if (record.getR11_OTHER_BORROW() != null) {
//							cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//							cell2.setCellStyle(numberStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(11);

						Cell cell2 = row.createCell(1);
						if (record.getR12_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(12);

						cell2 = row.createCell(1);
						if (record.getR13_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(13);
						cell2 = row.createCell(1);
						if (record.getR14_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(14);
//						cell2 = row.createCell(1);
//						if (record.getR15_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(15);
						cell2 = row.createCell(1);
						if (record.getR16_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(16);
						cell2 = row.createCell(1);
						if (record.getR17_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(17);
						cell2 = row.createCell(1);
						if (record.getR18_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(18);
						cell2 = row.createCell(1);
						if (record.getR19_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(19);
						cell2 = row.createCell(1);
						if (record.getR20_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(20);
						cell2 = row.createCell(1);
						if (record.getR21_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(21);
						cell2 = row.createCell(1);
						if (record.getR22_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(22);
						cell2 = row.createCell(1);
						if (record.getR23_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(23);
						cell2 = row.createCell(1);
						if (record.getR24_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(24);
						cell2 = row.createCell(1);
						if (record.getR25_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(25);
						cell2 = row.createCell(1);
						if (record.getR26_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(26);
						cell2 = row.createCell(1);
						if (record.getR27_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(27);
						cell2 = row.createCell(1);
						if (record.getR28_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(28);
//						cell2 = row.createCell(1);
//						if (record.getR29_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(29);
						cell2 = row.createCell(1);
						if (record.getR30_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(30);
						cell2 = row.createCell(1);
						if (record.getR31_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(31);
						cell2 = row.createCell(1);
						if (record.getR32_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(32);
						cell2 = row.createCell(1);
						if (record.getR33_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(33);
						cell2 = row.createCell(1);
						if (record.getR34_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(34);
						cell2 = row.createCell(1);
						if (record.getR35_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(35);
						cell2 = row.createCell(1);
						if (record.getR36_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(36);
						cell2 = row.createCell(1);
						if (record.getR37_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(37);
//						cell2 = row.createCell(1);
//						if (record.getR38_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(38);
						cell2 = row.createCell(1);
						if (record.getR39_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(39);
						cell2 = row.createCell(1);
						if (record.getR40_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(40);
//						cell2 = row.createCell(1);
//						if (record.getR41_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(41);
						cell2 = row.createCell(1);
						if (record.getR42_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(42);
						cell2 = row.createCell(1);
						if (record.getR43_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(43);
//						cell2 = row.createCell(1);
//						if (record.getR44_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(44);
						cell2 = row.createCell(1);
						if (record.getR45_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(45);
						cell2 = row.createCell(1);
						if (record.getR46_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(46);
						cell2 = row.createCell(1);
						if (record.getR47_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(47);
						cell2 = row.createCell(1);
						if (record.getR48_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(48);
//						cell2 = row.createCell(1);
//						if (record.getR49_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(49);
						cell2 = row.createCell(1);
						if (record.getR50_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(50);
						cell2 = row.createCell(1);
						if (record.getR51_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(51);
//						cell2 = row.createCell(1);
//						if (record.getR52_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(52);
						cell2 = row.createCell(1);
						if (record.getR53_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(53);
						cell2 = row.createCell(1);
						if (record.getR54_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(54);
						cell2 = row.createCell(1);
						if (record.getR55_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(55);
						cell2 = row.createCell(1);
						if (record.getR56_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(56);
						cell2 = row.createCell(1);
						if (record.getR57_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(57);
//						cell2 = row.createCell(1);
//						if (record.getR58_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(58);
						cell2 = row.createCell(1);
						if (record.getR59_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(59);
						cell2 = row.createCell(1);
						if (record.getR60_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(60);
						cell2 = row.createCell(1);
						if (record.getR61_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(61);
						cell2 = row.createCell(1);
						if (record.getR62_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(62);
						cell2 = row.createCell(1);
						if (record.getR63_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						row = sheet.getRow(63);
//						
//						cell2 = row.createCell(1);
//						if (record.getR64_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

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
	public byte[] getExcelM_OBARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OBArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OB_Archival_Summary_Entity> dataList = M_OB_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OB report. Returning empty result.");
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
					M_OB_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(11);

					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

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
	public byte[] BRRS_M_OBArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_OB_Archival_Summary_Entity> dataList = M_OB_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OB report. Returning empty result.");
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
					M_OB_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(11);

					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

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
	public byte[] BRRS_M_OBResubExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OBResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OB_Resub_Summary_Entity> dataList = M_OB_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OB report. Returning empty result.");
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

					M_OB_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(11);

					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

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
	public byte[] BRRS_M_OBResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_OB_Resub_Summary_Entity> dataList = M_OB_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OB report. Returning empty result.");
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
					M_OB_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(11);

					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

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