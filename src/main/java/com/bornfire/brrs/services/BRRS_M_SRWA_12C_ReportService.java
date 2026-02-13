
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
import java.util.Locale;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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

import com.bornfire.brrs.entities.BRRS_M_SRWA_12C_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12C_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12C_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12C_RESUB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12C_RESUB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12C_Summary_Repo;
import com.bornfire.brrs.entities.M_SRWA_12C_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12C_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12C_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12C_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12C_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12C_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12C_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12C_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12C_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12C_Summary_Entity;

@Component
@Service

public class BRRS_M_SRWA_12C_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12C_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SRWA_12C_Summary_Repo BRRS_M_SRWA_12C_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12C_Archival_Summary_Repo brrs_m_srwa_12c_archival_summary_repo;

	@Autowired
	BRRS_M_SRWA_12C_Archival_Detail_Repo BRRS_M_SRWA_12C_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SRWA_12C_Detail_Repo BRRS_M_SRWA_12C_Detail_Repo;

	@Autowired
	BRRS_M_SRWA_12C_RESUB_Summary_Repo BRRS_M_SRWA_12C_resub_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12C_RESUB_Detail_Repo BRRS_M_SRWA_12C_resub_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_SRWA_12CView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12C_Archival_Summary_Entity> T1Master = brrs_m_srwa_12c_archival_summary_repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12C_RESUB_Summary_Entity> T1Master = BRRS_M_SRWA_12C_resub_Summary_Repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_SRWA_12C_Summary_Entity> T1Master = new ArrayList<M_SRWA_12C_Summary_Entity>();
				try {
					Date d2 = dateformat.parse(todate);
					T1Master = BRRS_M_SRWA_12C_Summary_Repo.getdatabydateList(dateformat.parse(todate));
					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "summary");

					mv.addObject("report_date", dateformat.format(d2));

				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12C_Archival_Detail_Entity> T1Master = BRRS_M_SRWA_12C_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					List<M_SRWA_12C_RESUB_Detail_Entity> T1Master = BRRS_M_SRWA_12C_resub_Detail_Repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_SRWA_12C_Detail_Entity> T1Master = BRRS_M_SRWA_12C_Detail_Repo
							.getdatabydateList(dateformat.parse(todate));

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12C");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	// Archival View
	public List<Object[]> getM_SRWA_12CArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SRWA_12C_Archival_Summary_Entity> repoData = brrs_m_srwa_12c_archival_summary_repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12C_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12C_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12C Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Resub View
	public List<Object[]> getM_SRWA_12CResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_SRWA_12C_RESUB_Summary_Entity> latestArchivalList = BRRS_M_SRWA_12C_resub_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SRWA_12C_RESUB_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12C Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public void updateReport(M_SRWA_12C_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		Date reportDate = updatedEntity.getReport_date();
		List<M_SRWA_12C_Summary_Entity> existingList = BRRS_M_SRWA_12C_Summary_Repo.getdatabydateList(reportDate);

		M_SRWA_12C_Detail_Entity existingDetail = BRRS_M_SRWA_12C_Detail_Repo.findById(updatedEntity.getReport_date())
				.orElseGet(() -> {
					M_SRWA_12C_Detail_Entity d = new M_SRWA_12C_Detail_Entity();
					d.setReport_date(updatedEntity.getReport_date());
					return d;
				});

		M_SRWA_12C_Summary_Entity existing;

		if (!existingList.isEmpty()) {
			existing = existingList.get(0);
			System.out.println(" Existing record found for date: " + reportDate);
		} else {
			existing = new M_SRWA_12C_Summary_Entity();
			existing.setReport_date(reportDate);
			System.out.println("⚠️ No record found — creating new entry for date: " + reportDate);
		}
		try {
			// -------------------------------
			// ✅ COLUMN C
			// -------------------------------
			int[] cRows = { 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28 };
			for (int row : cRows) {
				String prefix = "R" + row + "_";
				String[] fields = { "NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12C_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12C_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue; // Skip missing field
					}
				}
			}

			// -------------------------------
			// ✅ COLUMNS D, E, F, G
			// -------------------------------
			int[] commonRows = { 12, 13, 14, 15, 16, 20, 21, 22, 23, 24, 25, 26, 27, 28 };

			for (int row : commonRows) {
				String prefix = "R" + row + "_";

				// ✅ Fields for D, E, F, G
				String[] fields = { "NUMBER_OF_FAILED_TRADES", // D
						"POSITIVE_CURRENT_EXPOSURE", // E
						"RISK_MULTIPLIER", // F
						"RWA" // G
				};

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12C_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12C_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Method detailSetter = M_SRWA_12C_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						continue; // Skip missing field gracefully
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// ✅ Save updated entity
		BRRS_M_SRWA_12C_Summary_Repo.save(existing);
		BRRS_M_SRWA_12C_Detail_Repo.save(existingDetail);
	}

	public void updateResubReport(M_SRWA_12C_RESUB_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReport_date();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = BRRS_M_SRWA_12C_resub_Summary_Repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_SRWA_12C_RESUB_Summary_Entity resubSummary = new M_SRWA_12C_RESUB_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReport_date(reportDate);
		resubSummary.setReport_version(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_SRWA_12C_RESUB_Detail_Entity resubDetail = new M_SRWA_12C_RESUB_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReport_date(reportDate);
		resubDetail.setReport_version(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12C_Archival_Summary_Entity archSummary = new M_SRWA_12C_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReport_date(reportDate);
		archSummary.setReport_version(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12C_Archival_Detail_Entity archDetail = new M_SRWA_12C_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReport_date(reportDate);
		archDetail.setReport_version(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		BRRS_M_SRWA_12C_resub_Summary_Repo.save(resubSummary);
		BRRS_M_SRWA_12C_resub_Detail_Repo.save(resubDetail);

		brrs_m_srwa_12c_archival_summary_repo.save(archSummary);
		BRRS_M_SRWA_12C_Archival_Detail_Repo.save(archDetail);
	}

	public void updateReportResub(M_SRWA_12C_Summary_Entity updatedEntity) {
		System.out.println("Came to Resub Service");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Use entity field directly (same name as in entity)
		Date report_date = updatedEntity.getReport_date();
		BigDecimal newVersion = BigDecimal.ONE;

		try {
			// ✅ use the same variable name as in repo method
			Optional<M_SRWA_12C_Archival_Summary_Entity> latestArchivalOpt = brrs_m_srwa_12c_archival_summary_repo
					.getLatestArchivalVersionByDate(report_date);

			// Determine next version
			if (latestArchivalOpt.isPresent()) {
				M_SRWA_12C_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
				try {
					newVersion = latestArchival.getReport_version().add(BigDecimal.ONE);
				} catch (NumberFormatException e) {
					System.err.println("Invalid version format. Defaulting to version 1");
					newVersion = BigDecimal.ONE;
				}
			} else {
				System.out.println("No previous archival found for date: " + report_date);
			}

			// Prevent duplicate version
			boolean exists = brrs_m_srwa_12c_archival_summary_repo
					.findByReport_dateAndReport_version(report_date, newVersion).isPresent();

			if (exists) {
				throw new RuntimeException("Version " + newVersion + " already exists for report date " + report_date);
			}

			// Copy summary entity to archival entity
			M_SRWA_12C_Archival_Summary_Entity archivalEntity = new M_SRWA_12C_Archival_Summary_Entity();
			org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

			archivalEntity.setReport_date(report_date);
			archivalEntity.setReport_version(newVersion);
			archivalEntity.setReportResubDate(new Date());

			System.out.println("Saving new archival version: " + newVersion);
			brrs_m_srwa_12c_archival_summary_repo.save(archivalEntity);

			System.out.println("Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creating archival resubmission record", e);
		}
	}

	// NORMAL FORMAT EXCEL
	public byte[] getBRRS_M_SRWA_12CExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= DOWNLOAD DETAILS =======");
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
				return getExcelM_SRWA_12CARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12CResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		else {
			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SRWA_12CEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				List<M_SRWA_12C_Summary_Entity> dataList = BRRS_M_SRWA_12C_Summary_Repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SRWA_12C report. Returning empty result.");
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

					CellStyle percentStyle = workbook.createCellStyle();
					percentStyle.cloneStyleFrom(numberStyle);
					percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
					percentStyle.setAlignment(HorizontalAlignment.CENTER);
					// --- End of Style Definitions ---

					int startRow = 11;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_SRWA_12C_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							// row12
							// Column D
							Cell cell3 = row.createCell(3);
							if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							Cell cell4 = row.createCell(4);
							if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							/*
							 * // Column F Cell cell5 = row.createCell(5); if
							 * (record.getR12_RISK_MULTIPLIER() != null) {
							 * cell5.setCellValue(record.getR12_RISK_MULTIPLIER().doubleValue() / 100);
							 * cell5.setCellStyle(percentStyle); } else { cell5.setCellValue("");
							 * cell5.setCellStyle(textStyle); }
							 */

							// row13
							row = sheet.getRow(12);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR13_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							/*
							 * // Column F Cell cell5 = row.createCell(5); if
							 * (record.getR13_RISK_MULTIPLIER() != null) {
							 * 
							 * cell5.setCellValue(record.getR13_RISK_MULTIPLIER().doubleValue() / 100);
							 * cell5.setCellStyle(percentStyle);
							 * 
							 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
							 */

							// row14
							row = sheet.getRow(13);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							/*
							 * // Column F cell5 = row.createCell(5); if (record.getR14_RISK_MULTIPLIER() !=
							 * null) { cell5.setCellValue(record.getR14_RISK_MULTIPLIER().doubleValue() /
							 * 100); cell5.setCellStyle(percentStyle);
							 * 
							 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
							 */
							// row15
							row = sheet.getRow(14);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR15_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR15_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

							/*
							 * //row16 row = sheet.getRow(15); // Column D cell3 = row.getCell(3); if
							 * (record.getR16_NUMBER_OF_FAILED_TRADES() != null) {
							 * cell3.setCellValue(record.getR16_NUMBER_OF_FAILED_TRADES().doubleValue());
							 * 
							 * } else { cell3.setCellValue("");
							 * 
							 * } // Column E cell4 = row.getCell(4); if
							 * (record.getR16_POSITIVE_CURRENT_EXPOSURE() != null) {
							 * cell4.setCellValue(record.getR16_POSITIVE_CURRENT_EXPOSURE().doubleValue());
							 * 
							 * } else { cell4.setCellValue("");
							 * 
							 * }
							 */

							// row20
							row = sheet.getRow(19);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR20_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR20_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

							// row21
							row = sheet.getRow(20);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR23_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SRWA_12CEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		List<M_SRWA_12C_Summary_Entity> dataList = BRRS_M_SRWA_12C_Summary_Repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12C report. Returning empty result.");
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
			percentStyle.setAlignment(HorizontalAlignment.CENTER);
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12C_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F Cell cell5 = row.createCell(5); if
					 * (record.getR12_RISK_MULTIPLIER() != null) {
					 * cell5.setCellValue(record.getR12_RISK_MULTIPLIER().doubleValue() / 100);
					 * cell5.setCellStyle(percentStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */

					// row13
					row = sheet.getRow(12);
					// Column D
					/*
					 * cell3 = row.createCell(3); if (record.getR13_NUMBER_OF_FAILED_TRADES() !=
					 * null) {
					 * cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); } // Column E cell4 = row.createCell(4); if
					 * (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 */
					/*
					 * // Column F Cell cell5 = row.createCell(5); if
					 * (record.getR13_RISK_MULTIPLIER() != null) {
					 * 
					 * cell5.setCellValue(record.getR13_RISK_MULTIPLIER().doubleValue() / 100);
					 * cell5.setCellStyle(percentStyle);
					 * 
					 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
					 */

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F cell5 = row.createCell(5); if (record.getR14_RISK_MULTIPLIER() !=
					 * null) { cell5.setCellValue(record.getR14_RISK_MULTIPLIER().doubleValue() /
					 * 100); cell5.setCellStyle(percentStyle);
					 * 
					 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
					 */
					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR15_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR15_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

					/*
					 * //row16 row = sheet.getRow(15); // Column D cell3 = row.getCell(3); if
					 * (record.getR16_NUMBER_OF_FAILED_TRADES() != null) {
					 * cell3.setCellValue(record.getR16_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * 
					 * } else { cell3.setCellValue("");
					 * 
					 * } // Column E cell4 = row.getCell(4); if
					 * (record.getR16_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR16_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * 
					 * } else { cell4.setCellValue("");
					 * 
					 * }
					 */

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR20_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR20_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					/*
					 * cell3 = row.createCell(3); if (record.getR23_NUMBER_OF_FAILED_TRADES() !=
					 * null) {
					 * cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); } // Column E cell4 = row.createCell(4); if
					 * (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 */

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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

	// Archival Format Excel
	public byte[] getExcelM_SRWA_12CARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12CARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}
		logger.info("Service: Starting Excel generation for Archival Format .");

		List<M_SRWA_12C_Archival_Summary_Entity> dataList = brrs_m_srwa_12c_archival_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12C report. Returning empty result.");
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
					M_SRWA_12C_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F Cell cell5 = row.createCell(5); if
					 * (record.getR12_RISK_MULTIPLIER() != null) {
					 * cell5.setCellValue(record.getR12_RISK_MULTIPLIER().doubleValue() / 100);
					 * cell5.setCellStyle(percentStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
//					// Column F
//					Cell cell5 = row.createCell(5);
//					if (record.getR13_RISK_MULTIPLIER() != null) {
//						
//						cell5.setCellValue(record.getR13_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F cell5 = row.createCell(5); if (record.getR14_RISK_MULTIPLIER() !=
					 * null) { cell5.setCellValue(record.getR14_RISK_MULTIPLIER().doubleValue() /
					 * 100); cell5.setCellStyle(percentStyle);
					 * 
					 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
					 */
					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F cell5 = row.createCell(5); if (record.getR15_RISK_MULTIPLIER() !=
					 * null) { cell5.setCellValue(record.getR15_RISK_MULTIPLIER().doubleValue() /
					 * 100); cell5.setCellStyle(percentStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */

					/*
					 * //row16 row = sheet.getRow(15); // Column D cell3 = row.getCell(3); if
					 * (record.getR16_NUMBER_OF_FAILED_TRADES() != null) {
					 * cell3.setCellValue(record.getR16_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * 
					 * } else { cell3.setCellValue("");
					 * 
					 * } // Column E cell4 = row.getCell(4); if
					 * (record.getR16_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR16_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * 
					 * } else { cell4.setCellValue("");
					 * 
					 * }
					 */

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F cell5 = row.createCell(5); if (record.getR20_RISK_MULTIPLIER() !=
					 * null) { cell5.setCellValue(record.getR20_RISK_MULTIPLIER().doubleValue() /
					 * 100); cell5.setCellStyle(percentStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SRWA_12CARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process for Archival Email.");

		List<M_SRWA_12C_Archival_Summary_Entity> dataList = brrs_m_srwa_12c_archival_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12C report. Returning empty result.");
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
			percentStyle.setAlignment(HorizontalAlignment.CENTER);
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12C_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F Cell cell5 = row.createCell(5); if
					 * (record.getR12_RISK_MULTIPLIER() != null) {
					 * cell5.setCellValue(record.getR12_RISK_MULTIPLIER().doubleValue() / 100);
					 * cell5.setCellStyle(percentStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */

					// row13
					row = sheet.getRow(12);
					// Column D
					/*
					 * cell3 = row.createCell(3); if (record.getR13_NUMBER_OF_FAILED_TRADES() !=
					 * null) {
					 * cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); } // Column E cell4 = row.createCell(4); if
					 * (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 */
					/*
					 * // Column F Cell cell5 = row.createCell(5); if
					 * (record.getR13_RISK_MULTIPLIER() != null) {
					 * 
					 * cell5.setCellValue(record.getR13_RISK_MULTIPLIER().doubleValue() / 100);
					 * cell5.setCellStyle(percentStyle);
					 * 
					 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
					 */

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F cell5 = row.createCell(5); if (record.getR14_RISK_MULTIPLIER() !=
					 * null) { cell5.setCellValue(record.getR14_RISK_MULTIPLIER().doubleValue() /
					 * 100); cell5.setCellStyle(percentStyle);
					 * 
					 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
					 */
					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR15_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR15_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

					/*
					 * //row16 row = sheet.getRow(15); // Column D cell3 = row.getCell(3); if
					 * (record.getR16_NUMBER_OF_FAILED_TRADES() != null) {
					 * cell3.setCellValue(record.getR16_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * 
					 * } else { cell3.setCellValue("");
					 * 
					 * } // Column E cell4 = row.getCell(4); if
					 * (record.getR16_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR16_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * 
					 * } else { cell4.setCellValue("");
					 * 
					 * }
					 */

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR20_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR20_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					/*
					 * cell3 = row.createCell(3); if (record.getR23_NUMBER_OF_FAILED_TRADES() !=
					 * null) {
					 * cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); } // Column E cell4 = row.createCell(4); if
					 * (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 */

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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

	// Resub Format Excel
	public byte[] BRRS_M_SRWA_12CResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12CResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}
		logger.info("Service: Starting Excel generation process in memory for RESUB Format Excel.");

		List<M_SRWA_12C_RESUB_Summary_Entity> dataList = BRRS_M_SRWA_12C_resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12Creport. Returning empty result.");
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

					M_SRWA_12C_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F Cell cell5 = row.createCell(5); if
					 * (record.getR12_RISK_MULTIPLIER() != null) {
					 * cell5.setCellValue(record.getR12_RISK_MULTIPLIER().doubleValue() / 100);
					 * cell5.setCellStyle(percentStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F Cell cell5 = row.createCell(5); if
					 * (record.getR13_RISK_MULTIPLIER() != null) {
					 * 
					 * cell5.setCellValue(record.getR13_RISK_MULTIPLIER().doubleValue() / 100);
					 * cell5.setCellStyle(percentStyle);
					 * 
					 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
					 */

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F cell5 = row.createCell(5); if (record.getR14_RISK_MULTIPLIER() !=
					 * null) { cell5.setCellValue(record.getR14_RISK_MULTIPLIER().doubleValue() /
					 * 100); cell5.setCellStyle(percentStyle);
					 * 
					 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
					 */
					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR15_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR15_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

					/*
					 * //row16 row = sheet.getRow(15); // Column D cell3 = row.getCell(3); if
					 * (record.getR16_NUMBER_OF_FAILED_TRADES() != null) {
					 * cell3.setCellValue(record.getR16_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * 
					 * } else { cell3.setCellValue("");
					 * 
					 * } // Column E cell4 = row.getCell(4); if
					 * (record.getR16_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR16_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * 
					 * } else { cell4.setCellValue("");
					 * 
					 * }
					 */

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR20_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR20_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SRWA_12CResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting RESUB EMAIL Excel generation.");

		List<M_SRWA_12C_RESUB_Summary_Entity> dataList = BRRS_M_SRWA_12C_resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12C report. Returning empty result.");
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
			percentStyle.setAlignment(HorizontalAlignment.CENTER);
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12C_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F Cell cell5 = row.createCell(5); if
					 * (record.getR12_RISK_MULTIPLIER() != null) {
					 * cell5.setCellValue(record.getR12_RISK_MULTIPLIER().doubleValue() / 100);
					 * cell5.setCellStyle(percentStyle); } else { cell5.setCellValue("");
					 * cell5.setCellStyle(textStyle); }
					 */

					// row13
					row = sheet.getRow(12);
					// Column D
					/*
					 * cell3 = row.createCell(3); if (record.getR13_NUMBER_OF_FAILED_TRADES() !=
					 * null) {
					 * cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); } // Column E cell4 = row.createCell(4); if
					 * (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 */
					/*
					 * // Column F Cell cell5 = row.createCell(5); if
					 * (record.getR13_RISK_MULTIPLIER() != null) {
					 * 
					 * cell5.setCellValue(record.getR13_RISK_MULTIPLIER().doubleValue() / 100);
					 * cell5.setCellStyle(percentStyle);
					 * 
					 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
					 */

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					/*
					 * // Column F cell5 = row.createCell(5); if (record.getR14_RISK_MULTIPLIER() !=
					 * null) { cell5.setCellValue(record.getR14_RISK_MULTIPLIER().doubleValue() /
					 * 100); cell5.setCellStyle(percentStyle);
					 * 
					 * } else { cell5.setCellValue(""); cell5.setCellStyle(textStyle); }
					 */
					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR15_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR15_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

					/*
					 * //row16 row = sheet.getRow(15); // Column D cell3 = row.getCell(3); if
					 * (record.getR16_NUMBER_OF_FAILED_TRADES() != null) {
					 * cell3.setCellValue(record.getR16_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * 
					 * } else { cell3.setCellValue("");
					 * 
					 * } // Column E cell4 = row.getCell(4); if
					 * (record.getR16_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR16_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * 
					 * } else { cell4.setCellValue("");
					 * 
					 * }
					 */

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
//					// Column F
//					cell5 = row.createCell(5);
//					if (record.getR20_RISK_MULTIPLIER() != null) {
//						cell5.setCellValue(record.getR20_RISK_MULTIPLIER().doubleValue() / 100);
//						cell5.setCellStyle(percentStyle);
//					} else {
//						cell5.setCellValue("");
//						cell5.setCellStyle(textStyle);
//					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					/*
					 * cell3 = row.createCell(3); if (record.getR23_NUMBER_OF_FAILED_TRADES() !=
					 * null) {
					 * cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); } // Column E cell4 = row.createCell(4); if
					 * (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
					 * cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
					 * cell4.setCellStyle(numberStyle); } else { cell4.setCellValue("");
					 * cell4.setCellStyle(textStyle); }
					 */

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
