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

import com.bornfire.brrs.entities.BRRS_Q_SMME_DEP_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_SMME_DEP_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_SMME_DEP_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_SMME_DEP_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_SMME_DEP_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_SMME_DEP_Summary_Repo;
import com.bornfire.brrs.entities.Q_SMME_DEP_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Q_SMME_DEP_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_SMME_DEP_Detail_Entity;
import com.bornfire.brrs.entities.Q_SMME_DEP_Resub_Detail_Entity;
import com.bornfire.brrs.entities.Q_SMME_DEP_Resub_Summary_Entity;
import com.bornfire.brrs.entities.Q_SMME_DEP_Summary_Entity;

@Component
@Service

public class BRRS_Q_SMME_DEP_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_SMME_DEP_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_Q_SMME_DEP_Summary_Repo brrs_Q_SMME_DEP_summary_repo;

	@Autowired
	BRRS_Q_SMME_DEP_Detail_Repo brrs_Q_SMME_DEP_detail_repo;

	@Autowired
	BRRS_Q_SMME_DEP_Archival_Summary_Repo Q_SMME_DEP_Archival_Summary_Repo;

	@Autowired
	BRRS_Q_SMME_DEP_Archival_Detail_Repo BRRS_Q_SMME_DEP_Archival_Detail_Repo;

	@Autowired
	BRRS_Q_SMME_DEP_Resub_Summary_Repo brrs_Q_SMME_DEP_resub_summary_repo;

	@Autowired
	BRRS_Q_SMME_DEP_Resub_Detail_Repo brrs_Q_SMME_DEP_resub_detail_repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getQ_SMME_DEPview(String reportId, String fromdate, String todate, String currency,
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
				List<Q_SMME_DEP_Archival_Summary_Entity> T1Master = Q_SMME_DEP_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<Q_SMME_DEP_Resub_Summary_Entity> T1Master = brrs_Q_SMME_DEP_resub_summary_repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<Q_SMME_DEP_Summary_Entity> T1Master = brrs_Q_SMME_DEP_summary_repo.getdatabydateList(d1);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_SMME_DEP_Archival_Detail_Entity> T1Master = BRRS_Q_SMME_DEP_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_SMME_DEP_Resub_Detail_Entity> T1Master = brrs_Q_SMME_DEP_resub_detail_repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<Q_SMME_DEP_Detail_Entity> T1Master = brrs_Q_SMME_DEP_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/Q_SMME_DEP");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	@Transactional
	public void updateReport(Q_SMME_DEP_Summary_Entity updatedEntity) {

		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// 🔹 Fetch existing SUMMARY
		Q_SMME_DEP_Summary_Entity existingSummary = brrs_Q_SMME_DEP_summary_repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		// 🔹 Fetch or create DETAIL
		Q_SMME_DEP_Detail_Entity detailEntity = brrs_Q_SMME_DEP_detail_repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					Q_SMME_DEP_Detail_Entity d = new Q_SMME_DEP_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {

			// 🔁 Loop R11 to R48 (skip 28–31)
			for (int i = 11; i <= 48; i++) {

				if (i >= 28 && i <= 31) {
					continue; // skip rows 27–31
				}

				String prefix = "R" + i + "_";

				String[] fields = { "PRODUCT", "CURRENT", "CALL", "SAVINGS", "0_31D_NOTICE", "32_88D_NOTICE",
						"91D_DEPOSIT", "1_2M_FD", "4_6M_FD", "7_12M_FD", "13_18M_FD", "19_24M_FD", "OVER24_FD", "TOTAL",
						"NOACC" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {

						// 🔹 Get value from updated entity
						Method getter = Q_SMME_DEP_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// 🔹 Set in SUMMARY
						Method summarySetter = Q_SMME_DEP_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// 🔹 Set in DETAIL
						Method detailSetter = Q_SMME_DEP_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// If field not present → skip safely
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		brrs_Q_SMME_DEP_summary_repo.save(existingSummary);
		brrs_Q_SMME_DEP_detail_repo.save(detailEntity);

		System.out.println("Update completed successfully");
	}

	@Transactional
	public void updateResubReport(Q_SMME_DEP_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = brrs_Q_SMME_DEP_resub_summary_repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		Q_SMME_DEP_Resub_Summary_Entity resubSummary = new Q_SMME_DEP_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		Q_SMME_DEP_Resub_Detail_Entity resubDetail = new Q_SMME_DEP_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		Q_SMME_DEP_Archival_Summary_Entity archSummary = new Q_SMME_DEP_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		Q_SMME_DEP_Archival_Detail_Entity archDetail = new Q_SMME_DEP_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		brrs_Q_SMME_DEP_resub_summary_repo.save(resubSummary);
		brrs_Q_SMME_DEP_resub_detail_repo.save(resubDetail);

		Q_SMME_DEP_Archival_Summary_Repo.save(archSummary);
		BRRS_Q_SMME_DEP_Archival_Detail_Repo.save(archDetail);
	}

// RESUB VIEW
	public List<Object[]> getQ_SMME_DEPResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<Q_SMME_DEP_Archival_Summary_Entity> latestArchivalList = Q_SMME_DEP_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (Q_SMME_DEP_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching Q_SMME_DEP Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getQ_SMME_DEPArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<Q_SMME_DEP_Archival_Summary_Entity> repoData = Q_SMME_DEP_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Q_SMME_DEP_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Q_SMME_DEP_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching Q_SMME_DEP Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getQ_SMME_DEPExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelQ_SMME_DEPARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_SMME_DEPResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_Q_SMME_DEPEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				List<Q_SMME_DEP_Summary_Entity> dataList = brrs_Q_SMME_DEP_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_Q_SMME_DEP report. Returning empty result.");
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
							Q_SMME_DEP_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
//NORMAL

							// ROW11
							// COLUMN2
							Cell cell2 = row.createCell(1);
							if (record.getR11_CURRENT() != null) {
								cell2.setCellValue(record.getR11_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row11
							// Column3
							Cell cell3 = row.createCell(2);
							if (record.getR11_CALL() != null) {
								cell3.setCellValue(record.getR11_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// row11
							// Column4
							Cell cell4 = row.createCell(3);
							if (record.getR11_SAVINGS() != null) {
								cell4.setCellValue(record.getR11_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row11
							// Column5
							Cell cell5 = row.createCell(4);
							if (record.getR11_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR11_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row11
							// Column6
							Cell cell6 = row.createCell(5);
							if (record.getR11_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR11_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row11
							// Column7
							Cell cell7 = row.createCell(6);
							if (record.getR11_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR11_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// row11
							// Column8
							Cell cell8 = row.createCell(7);
							if (record.getR11_1_2M_FD() != null) {
								cell8.setCellValue(record.getR11_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// row11
							// Column9
							Cell cell9 = row.createCell(8);
							if (record.getR11_4_6M_FD() != null) {
								cell9.setCellValue(record.getR11_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// row11
							// Column10
							Cell cell10 = row.createCell(9);
							if (record.getR11_7_12M_FD() != null) {
								cell10.setCellValue(record.getR11_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// row11
							// Column11
							Cell cell11 = row.createCell(10);
							if (record.getR11_13_18M_FD() != null) {
								cell11.setCellValue(record.getR11_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// row11
							// Column12
							Cell cell12 = row.createCell(11);
							if (record.getR11_19_24M_FD() != null) {
								cell12.setCellValue(record.getR11_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// row11
							// Column13
							Cell cell13 = row.createCell(12);
							if (record.getR11_OVER24_FD() != null) {
								cell13.setCellValue(record.getR11_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// row11
							// Column14
//					Cell cell14 = row.createCell(13);
//					if (record.getR11_TOTAL() != null) {
//						cell14.setCellValue(record.getR11_TOTAL().doubleValue());
//						cell14.setCellStyle(numberStyle);
//					} else {
//						cell14.setCellValue("");
//						cell14.setCellStyle(textStyle);
//					}

							// row11
							// Column15
							Cell cell15 = row.createCell(14);
							if (record.getR11_NOACC() != null) {
								cell15.setCellValue(record.getR11_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							row = sheet.getRow(11);

							// Row 12
							// Column2
							cell2 = row.createCell(1);
							if (record.getR12_CURRENT() != null) {
								cell2.setCellValue(record.getR12_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR12_CALL() != null) {
								cell3.setCellValue(record.getR12_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR12_SAVINGS() != null) {
								cell4.setCellValue(record.getR12_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR12_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR12_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR12_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR12_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR12_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR12_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR12_1_2M_FD() != null) {
								cell8.setCellValue(record.getR12_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR12_4_6M_FD() != null) {
								cell9.setCellValue(record.getR12_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR12_7_12M_FD() != null) {
								cell10.setCellValue(record.getR12_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR12_13_18M_FD() != null) {
								cell11.setCellValue(record.getR12_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR12_19_24M_FD() != null) {
								cell12.setCellValue(record.getR12_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR12_OVER24_FD() != null) {
								cell13.setCellValue(record.getR12_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column14
//					cell14 = row.createCell(13);
//					if (record.getR12_TOTAL() != null) {
//					    cell14.setCellValue(record.getR12_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR12_NOACC() != null) {
								cell15.setCellValue(record.getR12_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							row = sheet.getRow(12);

							// Row 13
							// Column2
							cell2 = row.createCell(1);
							if (record.getR13_CURRENT() != null) {
								cell2.setCellValue(record.getR13_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR13_CALL() != null) {
								cell3.setCellValue(record.getR13_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR13_SAVINGS() != null) {
								cell4.setCellValue(record.getR13_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR13_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR13_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR13_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR13_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR13_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR13_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR13_1_2M_FD() != null) {
								cell8.setCellValue(record.getR13_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR13_4_6M_FD() != null) {
								cell9.setCellValue(record.getR13_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR13_7_12M_FD() != null) {
								cell10.setCellValue(record.getR13_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR13_13_18M_FD() != null) {
								cell11.setCellValue(record.getR13_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR13_19_24M_FD() != null) {
								cell12.setCellValue(record.getR13_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR13_OVER24_FD() != null) {
								cell13.setCellValue(record.getR13_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column14
//					cell14 = row.createCell(13);
//					if (record.getR13_TOTAL() != null) {
//					    cell14.setCellValue(record.getR13_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR13_NOACC() != null) {
								cell15.setCellValue(record.getR13_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);

							// Row 14
							// Column2
							cell2 = row.createCell(1);
							if (record.getR14_CURRENT() != null) {
								cell2.setCellValue(record.getR14_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR14_CALL() != null) {
								cell3.setCellValue(record.getR14_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR14_SAVINGS() != null) {
								cell4.setCellValue(record.getR14_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR14_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR14_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR14_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR14_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR14_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR14_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR14_1_2M_FD() != null) {
								cell8.setCellValue(record.getR14_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR14_4_6M_FD() != null) {
								cell9.setCellValue(record.getR14_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR14_7_12M_FD() != null) {
								cell10.setCellValue(record.getR14_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR14_13_18M_FD() != null) {
								cell11.setCellValue(record.getR14_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR14_19_24M_FD() != null) {
								cell12.setCellValue(record.getR14_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR14_OVER24_FD() != null) {
								cell13.setCellValue(record.getR14_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR14_NOACC() != null) {
								cell15.setCellValue(record.getR14_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 14
							row = sheet.getRow(14);

							// Row 15
							// Column2
							cell2 = row.createCell(1);
							if (record.getR15_CURRENT() != null) {
								cell2.setCellValue(record.getR15_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR15_CALL() != null) {
								cell3.setCellValue(record.getR15_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR15_SAVINGS() != null) {
								cell4.setCellValue(record.getR15_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR15_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR15_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR15_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR15_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR15_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR15_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR15_1_2M_FD() != null) {
								cell8.setCellValue(record.getR15_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR15_4_6M_FD() != null) {
								cell9.setCellValue(record.getR15_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR15_7_12M_FD() != null) {
								cell10.setCellValue(record.getR15_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR15_13_18M_FD() != null) {
								cell11.setCellValue(record.getR15_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR15_19_24M_FD() != null) {
								cell12.setCellValue(record.getR15_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR15_OVER24_FD() != null) {
								cell13.setCellValue(record.getR15_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR15_NOACC() != null) {
								cell15.setCellValue(record.getR15_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 15
							row = sheet.getRow(15);

							// Row 16
							// Column2
							cell2 = row.createCell(1);
							if (record.getR16_CURRENT() != null) {
								cell2.setCellValue(record.getR16_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR16_CALL() != null) {
								cell3.setCellValue(record.getR16_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR16_SAVINGS() != null) {
								cell4.setCellValue(record.getR16_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR16_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR16_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR16_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR16_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR16_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR16_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR16_1_2M_FD() != null) {
								cell8.setCellValue(record.getR16_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR16_4_6M_FD() != null) {
								cell9.setCellValue(record.getR16_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR16_7_12M_FD() != null) {
								cell10.setCellValue(record.getR16_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR16_13_18M_FD() != null) {
								cell11.setCellValue(record.getR16_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR16_19_24M_FD() != null) {
								cell12.setCellValue(record.getR16_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR16_OVER24_FD() != null) {
								cell13.setCellValue(record.getR16_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR16_NOACC() != null) {
								cell15.setCellValue(record.getR16_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 16
							row = sheet.getRow(16);

							// Row 17
							// Column2
							cell2 = row.createCell(1);
							if (record.getR17_CURRENT() != null) {
								cell2.setCellValue(record.getR17_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR17_CALL() != null) {
								cell3.setCellValue(record.getR17_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR17_SAVINGS() != null) {
								cell4.setCellValue(record.getR17_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR17_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR17_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR17_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR17_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR17_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR17_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR17_1_2M_FD() != null) {
								cell8.setCellValue(record.getR17_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR17_4_6M_FD() != null) {
								cell9.setCellValue(record.getR17_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR17_7_12M_FD() != null) {
								cell10.setCellValue(record.getR17_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR17_13_18M_FD() != null) {
								cell11.setCellValue(record.getR17_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR17_19_24M_FD() != null) {
								cell12.setCellValue(record.getR17_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR17_OVER24_FD() != null) {
								cell13.setCellValue(record.getR17_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR17_NOACC() != null) {
								cell15.setCellValue(record.getR17_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 17
							row = sheet.getRow(17);

							// Row 18
							// Column2
							cell2 = row.createCell(1);
							if (record.getR18_CURRENT() != null) {
								cell2.setCellValue(record.getR18_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR18_CALL() != null) {
								cell3.setCellValue(record.getR18_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR18_SAVINGS() != null) {
								cell4.setCellValue(record.getR18_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR18_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR18_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR18_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR18_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR18_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR18_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR18_1_2M_FD() != null) {
								cell8.setCellValue(record.getR18_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR18_4_6M_FD() != null) {
								cell9.setCellValue(record.getR18_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR18_7_12M_FD() != null) {
								cell10.setCellValue(record.getR18_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR18_13_18M_FD() != null) {
								cell11.setCellValue(record.getR18_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR18_19_24M_FD() != null) {
								cell12.setCellValue(record.getR18_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR18_OVER24_FD() != null) {
								cell13.setCellValue(record.getR18_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR18_NOACC() != null) {
								cell15.setCellValue(record.getR18_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 18
							row = sheet.getRow(18);

							// Row 19
							// Column2
							cell2 = row.createCell(1);
							if (record.getR19_CURRENT() != null) {
								cell2.setCellValue(record.getR19_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR19_CALL() != null) {
								cell3.setCellValue(record.getR19_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR19_SAVINGS() != null) {
								cell4.setCellValue(record.getR19_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR19_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR19_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR19_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR19_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR19_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR19_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR19_1_2M_FD() != null) {
								cell8.setCellValue(record.getR19_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR19_4_6M_FD() != null) {
								cell9.setCellValue(record.getR19_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR19_7_12M_FD() != null) {
								cell10.setCellValue(record.getR19_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR19_13_18M_FD() != null) {
								cell11.setCellValue(record.getR19_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR19_19_24M_FD() != null) {
								cell12.setCellValue(record.getR19_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR19_OVER24_FD() != null) {
								cell13.setCellValue(record.getR19_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR19_NOACC() != null) {
								cell15.setCellValue(record.getR19_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 19
							row = sheet.getRow(19);

							// Row 20
							// Column2
							cell2 = row.createCell(1);
							if (record.getR20_CURRENT() != null) {
								cell2.setCellValue(record.getR20_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR20_CALL() != null) {
								cell3.setCellValue(record.getR20_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR20_SAVINGS() != null) {
								cell4.setCellValue(record.getR20_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR20_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR20_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR20_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR20_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR20_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR20_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR20_1_2M_FD() != null) {
								cell8.setCellValue(record.getR20_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR20_4_6M_FD() != null) {
								cell9.setCellValue(record.getR20_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR20_7_12M_FD() != null) {
								cell10.setCellValue(record.getR20_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR20_13_18M_FD() != null) {
								cell11.setCellValue(record.getR20_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR20_19_24M_FD() != null) {
								cell12.setCellValue(record.getR20_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR20_OVER24_FD() != null) {
								cell13.setCellValue(record.getR20_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR20_NOACC() != null) {
								cell15.setCellValue(record.getR20_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 20
							row = sheet.getRow(20);

							// Row 21
							// Column2
							cell2 = row.createCell(1);
							if (record.getR21_CURRENT() != null) {
								cell2.setCellValue(record.getR21_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR21_CALL() != null) {
								cell3.setCellValue(record.getR21_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR21_SAVINGS() != null) {
								cell4.setCellValue(record.getR21_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR21_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR21_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR21_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR21_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR21_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR21_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR21_1_2M_FD() != null) {
								cell8.setCellValue(record.getR21_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR21_4_6M_FD() != null) {
								cell9.setCellValue(record.getR21_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR21_7_12M_FD() != null) {
								cell10.setCellValue(record.getR21_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR21_13_18M_FD() != null) {
								cell11.setCellValue(record.getR21_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR21_19_24M_FD() != null) {
								cell12.setCellValue(record.getR21_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR21_OVER24_FD() != null) {
								cell13.setCellValue(record.getR21_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR21_NOACC() != null) {
								cell15.setCellValue(record.getR21_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 21
							row = sheet.getRow(21);

							// Row 22
							// Column2
							cell2 = row.createCell(1);
							if (record.getR22_CURRENT() != null) {
								cell2.setCellValue(record.getR22_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR22_CALL() != null) {
								cell3.setCellValue(record.getR22_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR22_SAVINGS() != null) {
								cell4.setCellValue(record.getR22_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR22_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR22_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR22_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR22_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR22_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR22_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR22_1_2M_FD() != null) {
								cell8.setCellValue(record.getR22_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR22_4_6M_FD() != null) {
								cell9.setCellValue(record.getR22_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR22_7_12M_FD() != null) {
								cell10.setCellValue(record.getR22_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR22_13_18M_FD() != null) {
								cell11.setCellValue(record.getR22_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR22_19_24M_FD() != null) {
								cell12.setCellValue(record.getR22_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR22_OVER24_FD() != null) {
								cell13.setCellValue(record.getR22_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR22_NOACC() != null) {
								cell15.setCellValue(record.getR22_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 22
							row = sheet.getRow(22);

							// Row 23
							// Column2
							cell2 = row.createCell(1);
							if (record.getR23_CURRENT() != null) {
								cell2.setCellValue(record.getR23_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR23_CALL() != null) {
								cell3.setCellValue(record.getR23_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR23_SAVINGS() != null) {
								cell4.setCellValue(record.getR23_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR23_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR23_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR23_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR23_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR23_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR23_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR23_1_2M_FD() != null) {
								cell8.setCellValue(record.getR23_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR23_4_6M_FD() != null) {
								cell9.setCellValue(record.getR23_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR23_7_12M_FD() != null) {
								cell10.setCellValue(record.getR23_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR23_13_18M_FD() != null) {
								cell11.setCellValue(record.getR23_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR23_19_24M_FD() != null) {
								cell12.setCellValue(record.getR23_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR23_OVER24_FD() != null) {
								cell13.setCellValue(record.getR23_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR23_NOACC() != null) {
								cell15.setCellValue(record.getR23_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 23
							row = sheet.getRow(23);

							// Row 24
							// Column2
							cell2 = row.createCell(1);
							if (record.getR24_CURRENT() != null) {
								cell2.setCellValue(record.getR24_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR24_CALL() != null) {
								cell3.setCellValue(record.getR24_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR24_SAVINGS() != null) {
								cell4.setCellValue(record.getR24_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR24_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR24_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR24_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR24_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR24_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR24_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR24_1_2M_FD() != null) {
								cell8.setCellValue(record.getR24_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR24_4_6M_FD() != null) {
								cell9.setCellValue(record.getR24_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR24_7_12M_FD() != null) {
								cell10.setCellValue(record.getR24_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR24_13_18M_FD() != null) {
								cell11.setCellValue(record.getR24_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR24_19_24M_FD() != null) {
								cell12.setCellValue(record.getR24_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR24_OVER24_FD() != null) {
								cell13.setCellValue(record.getR24_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR24_NOACC() != null) {
								cell15.setCellValue(record.getR24_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 25
							row = sheet.getRow(25);

							// Row 26
							// Column2
							cell2 = row.createCell(1);
							if (record.getR26_CURRENT() != null) {
								cell2.setCellValue(record.getR26_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR26_CALL() != null) {
								cell3.setCellValue(record.getR26_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR26_SAVINGS() != null) {
								cell4.setCellValue(record.getR26_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR26_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR26_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR26_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR26_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR26_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR26_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR26_1_2M_FD() != null) {
								cell8.setCellValue(record.getR26_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR26_4_6M_FD() != null) {
								cell9.setCellValue(record.getR26_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR26_7_12M_FD() != null) {
								cell10.setCellValue(record.getR26_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR26_13_18M_FD() != null) {
								cell11.setCellValue(record.getR26_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR26_19_24M_FD() != null) {
								cell12.setCellValue(record.getR26_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR26_OVER24_FD() != null) {
								cell13.setCellValue(record.getR26_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column14
//					cell14 = row.createCell(13);
//					if (record.getR26_TOTAL() != null) {
//					    cell14.setCellValue(record.getR26_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR26_NOACC() != null) {
								cell15.setCellValue(record.getR26_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 26
							row = sheet.getRow(31);

							// Row 32
							// Column2
							cell2 = row.createCell(1);
							if (record.getR32_CURRENT() != null) {
								System.out.println(record.getR32_CURRENT());
								cell2.setCellValue(record.getR32_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR32_CALL() != null) {
								cell3.setCellValue(record.getR32_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR32_SAVINGS() != null) {
								cell4.setCellValue(record.getR32_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR32_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR32_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR32_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR32_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR32_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR32_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR32_1_2M_FD() != null) {
								cell8.setCellValue(record.getR32_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR32_4_6M_FD() != null) {
								cell9.setCellValue(record.getR32_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR32_7_12M_FD() != null) {
								cell10.setCellValue(record.getR32_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR32_13_18M_FD() != null) {
								cell11.setCellValue(record.getR32_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR32_19_24M_FD() != null) {
								cell12.setCellValue(record.getR32_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR32_OVER24_FD() != null) {
								cell13.setCellValue(record.getR32_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column14
//					cell14 = row.createCell(13);
//					if (record.getR32_TOTAL() != null) {
//					    cell14.setCellValue(record.getR32_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR32_NOACC() != null) {
								cell15.setCellValue(record.getR32_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 32
							row = sheet.getRow(32);

							// Row 33
							// Column2
							cell2 = row.createCell(1);
							if (record.getR33_CURRENT() != null) {
								cell2.setCellValue(record.getR33_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR33_CALL() != null) {
								cell3.setCellValue(record.getR33_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR33_SAVINGS() != null) {
								cell4.setCellValue(record.getR33_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR33_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR33_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR33_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR33_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR33_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR33_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR33_1_2M_FD() != null) {
								cell8.setCellValue(record.getR33_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR33_4_6M_FD() != null) {
								cell9.setCellValue(record.getR33_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR33_7_12M_FD() != null) {
								cell10.setCellValue(record.getR33_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR33_13_18M_FD() != null) {
								cell11.setCellValue(record.getR33_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR33_19_24M_FD() != null) {
								cell12.setCellValue(record.getR33_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR33_OVER24_FD() != null) {
								cell13.setCellValue(record.getR33_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column14
//					cell14 = row.createCell(13);
//					if (record.getR33_TOTAL() != null) {
//					    cell14.setCellValue(record.getR33_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR33_NOACC() != null) {
								cell15.setCellValue(record.getR33_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 33
							row = sheet.getRow(33);

							// Row 34
							// Column2
							cell2 = row.createCell(1);
							if (record.getR34_CURRENT() != null) {
								cell2.setCellValue(record.getR34_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR34_CALL() != null) {
								cell3.setCellValue(record.getR34_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR34_SAVINGS() != null) {
								cell4.setCellValue(record.getR34_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR34_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR34_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR34_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR34_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR34_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR34_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR34_1_2M_FD() != null) {
								cell8.setCellValue(record.getR34_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR34_4_6M_FD() != null) {
								cell9.setCellValue(record.getR34_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR34_7_12M_FD() != null) {
								cell10.setCellValue(record.getR34_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR34_13_18M_FD() != null) {
								cell11.setCellValue(record.getR34_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR34_19_24M_FD() != null) {
								cell12.setCellValue(record.getR34_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR34_OVER24_FD() != null) {
								cell13.setCellValue(record.getR34_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column14
//					cell14 = row.createCell(13);
//					if (record.getR34_TOTAL() != null) {
//					    cell14.setCellValue(record.getR34_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR34_NOACC() != null) {
								cell15.setCellValue(record.getR34_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 34
							row = sheet.getRow(34);

							// Row 35
							// Column2
							cell2 = row.createCell(1);
							if (record.getR35_CURRENT() != null) {
								cell2.setCellValue(record.getR35_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR35_CALL() != null) {
								cell3.setCellValue(record.getR35_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR35_SAVINGS() != null) {
								cell4.setCellValue(record.getR35_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR35_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR35_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR35_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR35_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR35_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR35_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR35_1_2M_FD() != null) {
								cell8.setCellValue(record.getR35_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR35_4_6M_FD() != null) {
								cell9.setCellValue(record.getR35_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR35_7_12M_FD() != null) {
								cell10.setCellValue(record.getR35_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR35_13_18M_FD() != null) {
								cell11.setCellValue(record.getR35_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR35_19_24M_FD() != null) {
								cell12.setCellValue(record.getR35_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR35_OVER24_FD() != null) {
								cell13.setCellValue(record.getR35_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column14
//					cell14 = row.createCell(13);
//					if (record.getR35_TOTAL() != null) {
//					    cell14.setCellValue(record.getR35_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR35_NOACC() != null) {
								cell15.setCellValue(record.getR35_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 35
							row = sheet.getRow(35);

							// Row 36
							// Column2
							cell2 = row.createCell(1);
							if (record.getR36_CURRENT() != null) {
								cell2.setCellValue(record.getR36_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR36_CALL() != null) {
								cell3.setCellValue(record.getR36_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR36_SAVINGS() != null) {
								cell4.setCellValue(record.getR36_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR36_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR36_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR36_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR36_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR36_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR36_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR36_1_2M_FD() != null) {
								cell8.setCellValue(record.getR36_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR36_4_6M_FD() != null) {
								cell9.setCellValue(record.getR36_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR36_7_12M_FD() != null) {
								cell10.setCellValue(record.getR36_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR36_13_18M_FD() != null) {
								cell11.setCellValue(record.getR36_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR36_19_24M_FD() != null) {
								cell12.setCellValue(record.getR36_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR36_OVER24_FD() != null) {
								cell13.setCellValue(record.getR36_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR36_NOACC() != null) {
								cell15.setCellValue(record.getR36_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 36
							row = sheet.getRow(36);

							// Row 37
							// Column2
							cell2 = row.createCell(1);
							if (record.getR37_CURRENT() != null) {
								cell2.setCellValue(record.getR37_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR37_CALL() != null) {
								cell3.setCellValue(record.getR37_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR37_SAVINGS() != null) {
								cell4.setCellValue(record.getR37_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR37_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR37_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR37_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR37_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR37_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR37_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR37_1_2M_FD() != null) {
								cell8.setCellValue(record.getR37_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR37_4_6M_FD() != null) {
								cell9.setCellValue(record.getR37_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR37_7_12M_FD() != null) {
								cell10.setCellValue(record.getR37_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR37_13_18M_FD() != null) {
								cell11.setCellValue(record.getR37_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR37_19_24M_FD() != null) {
								cell12.setCellValue(record.getR37_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR37_OVER24_FD() != null) {
								cell13.setCellValue(record.getR37_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column14
//					cell14 = row.createCell(13);
//					if (record.getR37_TOTAL() != null) {
//					    cell14.setCellValue(record.getR37_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR37_NOACC() != null) {
								cell15.setCellValue(record.getR37_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 37
							row = sheet.getRow(37);

							// Row 38
							// Column2
							cell2 = row.createCell(1);
							if (record.getR38_CURRENT() != null) {
								cell2.setCellValue(record.getR38_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR38_CALL() != null) {
								cell3.setCellValue(record.getR38_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR38_SAVINGS() != null) {
								cell4.setCellValue(record.getR38_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR38_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR38_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR38_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR38_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR38_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR38_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR38_1_2M_FD() != null) {
								cell8.setCellValue(record.getR38_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR38_4_6M_FD() != null) {
								cell9.setCellValue(record.getR38_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR38_7_12M_FD() != null) {
								cell10.setCellValue(record.getR38_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR38_13_18M_FD() != null) {
								cell11.setCellValue(record.getR38_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR38_19_24M_FD() != null) {
								cell12.setCellValue(record.getR38_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR38_OVER24_FD() != null) {
								cell13.setCellValue(record.getR38_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR38_NOACC() != null) {
								cell15.setCellValue(record.getR38_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 38
							row = sheet.getRow(38);

							// Row 39
							// Column2
							cell2 = row.createCell(1);
							if (record.getR39_CURRENT() != null) {
								cell2.setCellValue(record.getR39_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR39_CALL() != null) {
								cell3.setCellValue(record.getR39_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR39_SAVINGS() != null) {
								cell4.setCellValue(record.getR39_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR39_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR39_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR39_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR39_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR39_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR39_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR39_1_2M_FD() != null) {
								cell8.setCellValue(record.getR39_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR39_4_6M_FD() != null) {
								cell9.setCellValue(record.getR39_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR39_7_12M_FD() != null) {
								cell10.setCellValue(record.getR39_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR39_13_18M_FD() != null) {
								cell11.setCellValue(record.getR39_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR39_19_24M_FD() != null) {
								cell12.setCellValue(record.getR39_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR39_OVER24_FD() != null) {
								cell13.setCellValue(record.getR39_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column14
//					cell14 = row.createCell(13);
//					if (record.getR39_TOTAL() != null) {
//					    cell14.setCellValue(record.getR39_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR39_NOACC() != null) {
								cell15.setCellValue(record.getR39_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 39
							row = sheet.getRow(39);

							// Row 40
							// Column2
							cell2 = row.createCell(1);
							if (record.getR40_CURRENT() != null) {
								cell2.setCellValue(record.getR40_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR40_CALL() != null) {
								cell3.setCellValue(record.getR40_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR40_SAVINGS() != null) {
								cell4.setCellValue(record.getR40_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR40_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR40_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR40_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR40_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR40_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR40_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR40_1_2M_FD() != null) {
								cell8.setCellValue(record.getR40_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR40_4_6M_FD() != null) {
								cell9.setCellValue(record.getR40_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR40_7_12M_FD() != null) {
								cell10.setCellValue(record.getR40_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR40_13_18M_FD() != null) {
								cell11.setCellValue(record.getR40_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR40_19_24M_FD() != null) {
								cell12.setCellValue(record.getR40_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR40_OVER24_FD() != null) {
								cell13.setCellValue(record.getR40_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR40_NOACC() != null) {
								cell15.setCellValue(record.getR40_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 40
							row = sheet.getRow(40);

							// Row 41
							// Column2
							cell2 = row.createCell(1);
							if (record.getR41_CURRENT() != null) {
								cell2.setCellValue(record.getR41_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR41_CALL() != null) {
								cell3.setCellValue(record.getR41_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR41_SAVINGS() != null) {
								cell4.setCellValue(record.getR41_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR41_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR41_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR41_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR41_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR41_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR41_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR41_1_2M_FD() != null) {
								cell8.setCellValue(record.getR41_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR41_4_6M_FD() != null) {
								cell9.setCellValue(record.getR41_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR41_7_12M_FD() != null) {
								cell10.setCellValue(record.getR41_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR41_13_18M_FD() != null) {
								cell11.setCellValue(record.getR41_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR41_19_24M_FD() != null) {
								cell12.setCellValue(record.getR41_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR41_OVER24_FD() != null) {
								cell13.setCellValue(record.getR41_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR41_NOACC() != null) {
								cell15.setCellValue(record.getR41_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 41
							row = sheet.getRow(41);

							// Row 42
							// Column2
							cell2 = row.createCell(1);
							if (record.getR42_CURRENT() != null) {
								cell2.setCellValue(record.getR42_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR42_CALL() != null) {
								cell3.setCellValue(record.getR42_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR42_SAVINGS() != null) {
								cell4.setCellValue(record.getR42_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR42_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR42_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR42_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR42_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR42_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR42_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR42_1_2M_FD() != null) {
								cell8.setCellValue(record.getR42_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR42_4_6M_FD() != null) {
								cell9.setCellValue(record.getR42_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR42_7_12M_FD() != null) {
								cell10.setCellValue(record.getR42_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR42_13_18M_FD() != null) {
								cell11.setCellValue(record.getR42_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR42_19_24M_FD() != null) {
								cell12.setCellValue(record.getR42_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR42_OVER24_FD() != null) {
								cell13.setCellValue(record.getR42_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR42_NOACC() != null) {
								cell15.setCellValue(record.getR42_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 42
							row = sheet.getRow(42);

							// Row 43
							// Column2
							cell2 = row.createCell(1);
							if (record.getR43_CURRENT() != null) {
								cell2.setCellValue(record.getR43_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR43_CALL() != null) {
								cell3.setCellValue(record.getR43_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR43_SAVINGS() != null) {
								cell4.setCellValue(record.getR43_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR43_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR43_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR43_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR43_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR43_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR43_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR43_1_2M_FD() != null) {
								cell8.setCellValue(record.getR43_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR43_4_6M_FD() != null) {
								cell9.setCellValue(record.getR43_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR43_7_12M_FD() != null) {
								cell10.setCellValue(record.getR43_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR43_13_18M_FD() != null) {
								cell11.setCellValue(record.getR43_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR43_19_24M_FD() != null) {
								cell12.setCellValue(record.getR43_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR43_OVER24_FD() != null) {
								cell13.setCellValue(record.getR43_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR43_NOACC() != null) {
								cell15.setCellValue(record.getR43_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 43
							row = sheet.getRow(43);

							// Row 44
							// Column2
							cell2 = row.createCell(1);
							if (record.getR44_CURRENT() != null) {
								cell2.setCellValue(record.getR44_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR44_CALL() != null) {
								cell3.setCellValue(record.getR44_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR44_SAVINGS() != null) {
								cell4.setCellValue(record.getR44_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR44_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR44_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR44_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR44_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR44_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR44_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR44_1_2M_FD() != null) {
								cell8.setCellValue(record.getR44_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR44_4_6M_FD() != null) {
								cell9.setCellValue(record.getR44_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR44_7_12M_FD() != null) {
								cell10.setCellValue(record.getR44_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR44_13_18M_FD() != null) {
								cell11.setCellValue(record.getR44_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR44_19_24M_FD() != null) {
								cell12.setCellValue(record.getR44_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR44_OVER24_FD() != null) {
								cell13.setCellValue(record.getR44_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR44_NOACC() != null) {
								cell15.setCellValue(record.getR44_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 44
							row = sheet.getRow(44);

							// Row 45
							// Column2
							cell2 = row.createCell(1);
							if (record.getR45_CURRENT() != null) {
								cell2.setCellValue(record.getR45_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR45_CALL() != null) {
								cell3.setCellValue(record.getR45_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR45_SAVINGS() != null) {
								cell4.setCellValue(record.getR45_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR45_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR45_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR45_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR45_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR45_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR45_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR45_1_2M_FD() != null) {
								cell8.setCellValue(record.getR45_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR45_4_6M_FD() != null) {
								cell9.setCellValue(record.getR45_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR45_7_12M_FD() != null) {
								cell10.setCellValue(record.getR45_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR45_13_18M_FD() != null) {
								cell11.setCellValue(record.getR45_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR45_19_24M_FD() != null) {
								cell12.setCellValue(record.getR45_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR45_OVER24_FD() != null) {
								cell13.setCellValue(record.getR45_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR45_NOACC() != null) {
								cell15.setCellValue(record.getR45_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}

							// Assign Row 46
							row = sheet.getRow(46);

							// Row 47
							// Column2
							cell2 = row.createCell(1);
							if (record.getR47_CURRENT() != null) {
								cell2.setCellValue(record.getR47_CURRENT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// Column3
							cell3 = row.createCell(2);
							if (record.getR47_CALL() != null) {
								cell3.setCellValue(record.getR47_CALL().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							// Column4
							cell4 = row.createCell(3);
							if (record.getR47_SAVINGS() != null) {
								cell4.setCellValue(record.getR47_SAVINGS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// Column5
							cell5 = row.createCell(4);
							if (record.getR47_0_31D_NOTICE() != null) {
								cell5.setCellValue(record.getR47_0_31D_NOTICE().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// Column6
							cell6 = row.createCell(5);
							if (record.getR47_32_88D_NOTICE() != null) {
								cell6.setCellValue(record.getR47_32_88D_NOTICE().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// Column7
							cell7 = row.createCell(6);
							if (record.getR47_91D_DEPOSIT() != null) {
								cell7.setCellValue(record.getR47_91D_DEPOSIT().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}

							// Column8
							cell8 = row.createCell(7);
							if (record.getR47_1_2M_FD() != null) {
								cell8.setCellValue(record.getR47_1_2M_FD().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}

							// Column9
							cell9 = row.createCell(8);
							if (record.getR47_4_6M_FD() != null) {
								cell9.setCellValue(record.getR47_4_6M_FD().doubleValue());
								cell9.setCellStyle(numberStyle);
							} else {
								cell9.setCellValue("");
								cell9.setCellStyle(textStyle);
							}

							// Column10
							cell10 = row.createCell(9);
							if (record.getR47_7_12M_FD() != null) {
								cell10.setCellValue(record.getR47_7_12M_FD().doubleValue());
								cell10.setCellStyle(numberStyle);
							} else {
								cell10.setCellValue("");
								cell10.setCellStyle(textStyle);
							}

							// Column11
							cell11 = row.createCell(10);
							if (record.getR47_13_18M_FD() != null) {
								cell11.setCellValue(record.getR47_13_18M_FD().doubleValue());
								cell11.setCellStyle(numberStyle);
							} else {
								cell11.setCellValue("");
								cell11.setCellStyle(textStyle);
							}

							// Column12
							cell12 = row.createCell(11);
							if (record.getR47_19_24M_FD() != null) {
								cell12.setCellValue(record.getR47_19_24M_FD().doubleValue());
								cell12.setCellStyle(numberStyle);
							} else {
								cell12.setCellValue("");
								cell12.setCellStyle(textStyle);
							}

							// Column13
							cell13 = row.createCell(12);
							if (record.getR47_OVER24_FD() != null) {
								cell13.setCellValue(record.getR47_OVER24_FD().doubleValue());
								cell13.setCellStyle(numberStyle);
							} else {
								cell13.setCellValue("");
								cell13.setCellStyle(textStyle);
							}

							// Column15
							cell15 = row.createCell(14);
							if (record.getR47_NOACC() != null) {
								cell15.setCellValue(record.getR47_NOACC().doubleValue());
								cell15.setCellStyle(numberStyle);
							} else {
								cell15.setCellValue("");
								cell15.setCellStyle(textStyle);
							}
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
	public byte[] BRRS_Q_SMME_DEPEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_Q_SMME_DEPEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_SMME_DEPResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<Q_SMME_DEP_Summary_Entity> dataList = brrs_Q_SMME_DEP_summary_repo
					.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_Q_SMME_DEP report. Returning empty result.");
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

				int startRow = 9;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						Q_SMME_DEP_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL

						// ROW11
						// COLUMN2
						Cell cell2 = row.createCell(3);
						if (record.getR11_CURRENT() != null) {
							cell2.setCellValue(record.getR11_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row11
						// Column3
						Cell cell3 = row.createCell(4);
						if (record.getR11_CALL() != null) {
							cell3.setCellValue(record.getR11_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// row11
						// Column4
						Cell cell4 = row.createCell(5);
						if (record.getR11_SAVINGS() != null) {
							cell4.setCellValue(record.getR11_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row11
						// Column5
						Cell cell5 = row.createCell(6);
						if (record.getR11_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR11_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// row11
						// Column6
						Cell cell6 = row.createCell(7);
						if (record.getR11_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR11_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row11
						// Column7
						Cell cell7 = row.createCell(8);
						if (record.getR11_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR11_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						Cell cell8 = row.createCell(9);

						BigDecimal total = (record.getR11_1_2M_FD() == null ? BigDecimal.ZERO : record.getR11_1_2M_FD())
								.add(record.getR11_4_6M_FD() == null ? BigDecimal.ZERO : record.getR11_4_6M_FD());

						if (total.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}
						// row11
						// Column10
						Cell cell10 = row.createCell(10);
						if (record.getR11_7_12M_FD() != null) {
							cell10.setCellValue(record.getR11_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// row11
						// Column11
						Cell cell11 = row.createCell(11);
						if (record.getR11_13_18M_FD() != null) {
							cell11.setCellValue(record.getR11_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// row11
						// Column12
						Cell cell12 = row.createCell(12);
						if (record.getR11_19_24M_FD() != null) {
							cell12.setCellValue(record.getR11_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// row11
						// Column13
						Cell cell13 = row.createCell(13);
						if (record.getR11_OVER24_FD() != null) {
							cell13.setCellValue(record.getR11_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						row = sheet.getRow(10);

						// Row 12
						// Column2
						cell2 = row.createCell(3);
						if (record.getR12_CURRENT() != null) {
							cell2.setCellValue(record.getR12_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR12_CALL() != null) {
							cell3.setCellValue(record.getR12_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR12_SAVINGS() != null) {
							cell4.setCellValue(record.getR12_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR12_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR12_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR12_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR12_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR12_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR12_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(9);

						BigDecimal total1 = (record.getR12_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR12_1_2M_FD())
								.add(record.getR12_4_6M_FD() == null ? BigDecimal.ZERO : record.getR12_4_6M_FD());

						if (total1.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total1.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR12_7_12M_FD() != null) {
							cell10.setCellValue(record.getR12_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR12_13_18M_FD() != null) {
							cell11.setCellValue(record.getR12_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR12_19_24M_FD() != null) {
							cell12.setCellValue(record.getR12_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR12_OVER24_FD() != null) {
							cell13.setCellValue(record.getR12_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R13
						// ==========================

						row = sheet.getRow(11); // Adjust row index if needed

						// Column2
						cell2 = row.createCell(3);
						if (record.getR13_CURRENT() != null) {
							cell2.setCellValue(record.getR13_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR13_CALL() != null) {
							cell3.setCellValue(record.getR13_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR13_SAVINGS() != null) {
							cell4.setCellValue(record.getR13_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR13_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR13_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR13_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR13_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR13_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR13_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total7 = (record.getR13_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR13_1_2M_FD())
								.add(record.getR13_4_6M_FD() == null ? BigDecimal.ZERO : record.getR13_4_6M_FD());

						if (total7.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total7.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR13_7_12M_FD() != null) {
							cell10.setCellValue(record.getR13_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR13_13_18M_FD() != null) {
							cell11.setCellValue(record.getR13_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR13_19_24M_FD() != null) {
							cell12.setCellValue(record.getR13_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR13_OVER24_FD() != null) {
							cell13.setCellValue(record.getR13_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R14
						// ==========================

						row = sheet.getRow(12); // Adjust row index if needed

						// Column2
						cell2 = row.createCell(3);
						if (record.getR14_CURRENT() != null) {
							cell2.setCellValue(record.getR14_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR14_CALL() != null) {
							cell3.setCellValue(record.getR14_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR14_SAVINGS() != null) {
							cell4.setCellValue(record.getR14_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR14_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR14_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR14_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR14_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR14_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR14_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total8 = (record.getR14_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR14_1_2M_FD())
								.add(record.getR14_4_6M_FD() == null ? BigDecimal.ZERO : record.getR14_4_6M_FD());

						if (total8.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total8.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR14_7_12M_FD() != null) {
							cell10.setCellValue(record.getR14_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR14_13_18M_FD() != null) {
							cell11.setCellValue(record.getR14_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR14_19_24M_FD() != null) {
							cell12.setCellValue(record.getR14_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR14_OVER24_FD() != null) {
							cell13.setCellValue(record.getR14_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R15
						// ==========================

						row = sheet.getRow(19);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR15_CURRENT() != null) {
							cell2.setCellValue(record.getR15_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR15_CALL() != null) {
							cell3.setCellValue(record.getR15_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR15_SAVINGS() != null) {
							cell4.setCellValue(record.getR15_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR15_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR15_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR15_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR15_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR15_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR15_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total3 = (record.getR15_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR15_1_2M_FD())
								.add(record.getR15_4_6M_FD() == null ? BigDecimal.ZERO : record.getR15_4_6M_FD());

						if (total3.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total3.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR15_7_12M_FD() != null) {
							cell10.setCellValue(record.getR15_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR15_13_18M_FD() != null) {
							cell11.setCellValue(record.getR15_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR15_19_24M_FD() != null) {
							cell12.setCellValue(record.getR15_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR15_OVER24_FD() != null) {
							cell13.setCellValue(record.getR15_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R16
						// ==========================

						row = sheet.getRow(13);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR16_CURRENT() != null) {
							cell2.setCellValue(record.getR16_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR16_CALL() != null) {
							cell3.setCellValue(record.getR16_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR16_SAVINGS() != null) {
							cell4.setCellValue(record.getR16_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR16_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR16_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR16_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR16_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR16_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR16_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total11 = (record.getR16_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR16_1_2M_FD())
								.add(record.getR16_4_6M_FD() == null ? BigDecimal.ZERO : record.getR16_4_6M_FD());

						if (total11.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total11.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR16_7_12M_FD() != null) {
							cell10.setCellValue(record.getR16_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR16_13_18M_FD() != null) {
							cell11.setCellValue(record.getR16_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR16_19_24M_FD() != null) {
							cell12.setCellValue(record.getR16_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR16_OVER24_FD() != null) {
							cell13.setCellValue(record.getR16_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R17
						// ==========================

						row = sheet.getRow(14);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR17_CURRENT() != null) {
							cell2.setCellValue(record.getR17_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR17_CALL() != null) {
							cell3.setCellValue(record.getR17_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR17_SAVINGS() != null) {
							cell4.setCellValue(record.getR17_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR17_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR17_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR17_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR17_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR17_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR17_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total81 = (record.getR17_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR17_1_2M_FD())
								.add(record.getR17_4_6M_FD() == null ? BigDecimal.ZERO : record.getR17_4_6M_FD());

						if (total81.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total81.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR17_7_12M_FD() != null) {
							cell10.setCellValue(record.getR17_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR17_13_18M_FD() != null) {
							cell11.setCellValue(record.getR17_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR17_19_24M_FD() != null) {
							cell12.setCellValue(record.getR17_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR17_OVER24_FD() != null) {
							cell13.setCellValue(record.getR17_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R18
						// ==========================

						row = sheet.getRow(15);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR18_CURRENT() != null) {
							cell2.setCellValue(record.getR18_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR18_CALL() != null) {
							cell3.setCellValue(record.getR18_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR18_SAVINGS() != null) {
							cell4.setCellValue(record.getR18_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR18_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR18_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR18_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR18_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR18_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR18_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total4 = (record.getR18_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR18_1_2M_FD())
								.add(record.getR18_4_6M_FD() == null ? BigDecimal.ZERO : record.getR18_4_6M_FD());

						if (total4.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total4.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR18_7_12M_FD() != null) {
							cell10.setCellValue(record.getR18_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR18_13_18M_FD() != null) {
							cell11.setCellValue(record.getR18_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR18_19_24M_FD() != null) {
							cell12.setCellValue(record.getR18_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR18_OVER24_FD() != null) {
							cell13.setCellValue(record.getR18_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R19
						// ==========================

						row = sheet.getRow(16);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR19_CURRENT() != null) {
							cell2.setCellValue(record.getR19_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR19_CALL() != null) {
							cell3.setCellValue(record.getR19_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR19_SAVINGS() != null) {
							cell4.setCellValue(record.getR19_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR19_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR19_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR19_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR19_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR19_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR19_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total5 = (record.getR19_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR19_1_2M_FD())
								.add(record.getR19_4_6M_FD() == null ? BigDecimal.ZERO : record.getR19_4_6M_FD());

						if (total5.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total5.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR19_7_12M_FD() != null) {
							cell10.setCellValue(record.getR19_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR19_13_18M_FD() != null) {
							cell11.setCellValue(record.getR19_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR19_19_24M_FD() != null) {
							cell12.setCellValue(record.getR19_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR19_OVER24_FD() != null) {
							cell13.setCellValue(record.getR19_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R20
						// ==========================

						row = sheet.getRow(17);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR20_CURRENT() != null) {
							cell2.setCellValue(record.getR20_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR20_CALL() != null) {
							cell3.setCellValue(record.getR20_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR20_SAVINGS() != null) {
							cell4.setCellValue(record.getR20_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR20_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR20_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR20_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR20_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR20_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR20_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal totalR20 = (record.getR20_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR20_1_2M_FD())
								.add(record.getR20_4_6M_FD() == null ? BigDecimal.ZERO : record.getR20_4_6M_FD());

						if (totalR20.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR20.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR20_7_12M_FD() != null) {
							cell10.setCellValue(record.getR20_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR20_13_18M_FD() != null) {
							cell11.setCellValue(record.getR20_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR20_19_24M_FD() != null) {
							cell12.setCellValue(record.getR20_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR20_OVER24_FD() != null) {
							cell13.setCellValue(record.getR20_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R21
						// ==========================

						row = sheet.getRow(18);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR21_CURRENT() != null) {
							cell2.setCellValue(record.getR21_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR21_CALL() != null) {
							cell3.setCellValue(record.getR21_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR21_SAVINGS() != null) {
							cell4.setCellValue(record.getR21_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR21_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR21_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR21_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR21_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR21_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR21_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal totalR21 = (record.getR21_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR21_1_2M_FD())
								.add(record.getR21_4_6M_FD() == null ? BigDecimal.ZERO : record.getR21_4_6M_FD());

						if (totalR21.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR21.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR21_7_12M_FD() != null) {
							cell10.setCellValue(record.getR21_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR21_13_18M_FD() != null) {
							cell11.setCellValue(record.getR21_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR21_19_24M_FD() != null) {
							cell12.setCellValue(record.getR21_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR21_OVER24_FD() != null) {
							cell13.setCellValue(record.getR21_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R22
						// ==========================

						row = sheet.getRow(20);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR22_CURRENT() != null) {
							cell2.setCellValue(record.getR22_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR22_CALL() != null) {
							cell3.setCellValue(record.getR22_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR22_SAVINGS() != null) {
							cell4.setCellValue(record.getR22_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR22_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR22_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR22_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR22_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR22_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR22_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR22 = (record.getR22_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR22_1_2M_FD())
								.add(record.getR22_4_6M_FD() == null ? BigDecimal.ZERO : record.getR22_4_6M_FD());

						if (totalR22.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR22.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR22_7_12M_FD() != null) {
							cell10.setCellValue(record.getR22_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR22_13_18M_FD() != null) {
							cell11.setCellValue(record.getR22_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR22_19_24M_FD() != null) {
							cell12.setCellValue(record.getR22_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR22_OVER24_FD() != null) {
							cell13.setCellValue(record.getR22_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						row = sheet.getRow(21);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR27_CURRENT() != null) {
							cell2.setCellValue(record.getR27_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR27_CALL() != null) {
							cell3.setCellValue(record.getR27_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR27_SAVINGS() != null) {
							cell4.setCellValue(record.getR27_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR27_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR27_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR27_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR27_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR27_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR27_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR27 = (record.getR27_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR27_1_2M_FD())
								.add(record.getR27_4_6M_FD() == null ? BigDecimal.ZERO : record.getR27_4_6M_FD());

						if (totalR27.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR27.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR27_7_12M_FD() != null) {
							cell10.setCellValue(record.getR27_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR27_13_18M_FD() != null) {
							cell11.setCellValue(record.getR27_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR27_19_24M_FD() != null) {
							cell12.setCellValue(record.getR27_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR27_OVER24_FD() != null) {
							cell13.setCellValue(record.getR27_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R23
						// ==========================

						row = sheet.getRow(22);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR23_CURRENT() != null) {
							cell2.setCellValue(record.getR23_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR23_CALL() != null) {
							cell3.setCellValue(record.getR23_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR23_SAVINGS() != null) {
							cell4.setCellValue(record.getR23_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR23_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR23_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR23_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR23_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR23_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR23_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR23 = (record.getR23_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR23_1_2M_FD())
								.add(record.getR23_4_6M_FD() == null ? BigDecimal.ZERO : record.getR23_4_6M_FD());

						if (totalR23.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR23.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR23_7_12M_FD() != null) {
							cell10.setCellValue(record.getR23_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR23_13_18M_FD() != null) {
							cell11.setCellValue(record.getR23_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR23_19_24M_FD() != null) {
							cell12.setCellValue(record.getR23_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR23_OVER24_FD() != null) {
							cell13.setCellValue(record.getR23_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R24
						// ==========================

						row = sheet.getRow(23);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR24_CURRENT() != null) {
							cell2.setCellValue(record.getR24_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR24_CALL() != null) {
							cell3.setCellValue(record.getR24_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR24_SAVINGS() != null) {
							cell4.setCellValue(record.getR24_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR24_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR24_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR24_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR24_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR24_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR24_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR24 = (record.getR24_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR24_1_2M_FD())
								.add(record.getR24_4_6M_FD() == null ? BigDecimal.ZERO : record.getR24_4_6M_FD());

						if (totalR24.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR24.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR24_7_12M_FD() != null) {
							cell10.setCellValue(record.getR24_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR24_13_18M_FD() != null) {
							cell11.setCellValue(record.getR24_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR24_19_24M_FD() != null) {
							cell12.setCellValue(record.getR24_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR24_OVER24_FD() != null) {
							cell13.setCellValue(record.getR24_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R26
						// ==========================

						row = sheet.getRow(25);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR26_CURRENT() != null) {
							cell2.setCellValue(record.getR26_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR26_CALL() != null) {
							cell3.setCellValue(record.getR26_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR26_SAVINGS() != null) {
							cell4.setCellValue(record.getR26_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR26_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR26_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR26_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR26_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR26_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR26_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR26 = (record.getR26_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR26_1_2M_FD())
								.add(record.getR26_4_6M_FD() == null ? BigDecimal.ZERO : record.getR26_4_6M_FD());

						if (totalR26.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR26.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR26_7_12M_FD() != null) {
							cell10.setCellValue(record.getR26_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR26_13_18M_FD() != null) {
							cell11.setCellValue(record.getR26_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR26_19_24M_FD() != null) {
							cell12.setCellValue(record.getR26_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR26_OVER24_FD() != null) {
							cell13.setCellValue(record.getR26_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R32
						// ==========================

						row = sheet.getRow(33);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR32_CURRENT() != null) {
							cell2.setCellValue(record.getR32_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR32_CALL() != null) {
							cell3.setCellValue(record.getR32_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR32_SAVINGS() != null) {
							cell4.setCellValue(record.getR32_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR32_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR32_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR32_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR32_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR32_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR32_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR32 = (record.getR32_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR32_1_2M_FD())
								.add(record.getR32_4_6M_FD() == null ? BigDecimal.ZERO : record.getR32_4_6M_FD());

						if (totalR32.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR32.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR32_7_12M_FD() != null) {
							cell10.setCellValue(record.getR32_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR32_13_18M_FD() != null) {
							cell11.setCellValue(record.getR32_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR32_19_24M_FD() != null) {
							cell12.setCellValue(record.getR32_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR32_OVER24_FD() != null) {
							cell13.setCellValue(record.getR32_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R33
						// ==========================

						row = sheet.getRow(34);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR33_CURRENT() != null) {
							cell2.setCellValue(record.getR33_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR33_CALL() != null) {
							cell3.setCellValue(record.getR33_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR33_SAVINGS() != null) {
							cell4.setCellValue(record.getR33_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR33_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR33_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR33_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR33_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR33_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR33_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR33 = (record.getR33_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR33_1_2M_FD())
								.add(record.getR33_4_6M_FD() == null ? BigDecimal.ZERO : record.getR33_4_6M_FD());

						if (totalR33.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR33.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR33_7_12M_FD() != null) {
							cell10.setCellValue(record.getR33_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR33_13_18M_FD() != null) {
							cell11.setCellValue(record.getR33_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR33_19_24M_FD() != null) {
							cell12.setCellValue(record.getR33_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR33_OVER24_FD() != null) {
							cell13.setCellValue(record.getR33_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R34
						// ==========================

						row = sheet.getRow(35);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR34_CURRENT() != null) {
							cell2.setCellValue(record.getR34_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR34_CALL() != null) {
							cell3.setCellValue(record.getR34_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR34_SAVINGS() != null) {
							cell4.setCellValue(record.getR34_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR34_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR34_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR34_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR34_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR34_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR34_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR34 = (record.getR34_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR34_1_2M_FD())
								.add(record.getR34_4_6M_FD() == null ? BigDecimal.ZERO : record.getR34_4_6M_FD());

						if (totalR34.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR34.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR34_7_12M_FD() != null) {
							cell10.setCellValue(record.getR34_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR34_13_18M_FD() != null) {
							cell11.setCellValue(record.getR34_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR34_19_24M_FD() != null) {
							cell12.setCellValue(record.getR34_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR34_OVER24_FD() != null) {
							cell13.setCellValue(record.getR34_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R35
						// ==========================

						row = sheet.getRow(36);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR35_CURRENT() != null) {
							cell2.setCellValue(record.getR35_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR35_CALL() != null) {
							cell3.setCellValue(record.getR35_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR35_SAVINGS() != null) {
							cell4.setCellValue(record.getR35_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR35_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR35_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR35_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR35_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR35_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR35_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR35 = (record.getR35_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR35_1_2M_FD())
								.add(record.getR35_4_6M_FD() == null ? BigDecimal.ZERO : record.getR35_4_6M_FD());

						if (totalR35.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR35.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR35_7_12M_FD() != null) {
							cell10.setCellValue(record.getR35_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR35_13_18M_FD() != null) {
							cell11.setCellValue(record.getR35_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR35_19_24M_FD() != null) {
							cell12.setCellValue(record.getR35_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR35_OVER24_FD() != null) {
							cell13.setCellValue(record.getR35_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R36
						// ==========================

						row = sheet.getRow(43);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR36_CURRENT() != null) {
							cell2.setCellValue(record.getR36_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR36_CALL() != null) {
							cell3.setCellValue(record.getR36_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR36_SAVINGS() != null) {
							cell4.setCellValue(record.getR36_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR36_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR36_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR36_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR36_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR36_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR36_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR36 = (record.getR36_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR36_1_2M_FD())
								.add(record.getR36_4_6M_FD() == null ? BigDecimal.ZERO : record.getR36_4_6M_FD());

						if (totalR36.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR36.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR36_7_12M_FD() != null) {
							cell10.setCellValue(record.getR36_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR36_13_18M_FD() != null) {
							cell11.setCellValue(record.getR36_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR36_19_24M_FD() != null) {
							cell12.setCellValue(record.getR36_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR36_OVER24_FD() != null) {
							cell13.setCellValue(record.getR36_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R37
						// ==========================

						row = sheet.getRow(37);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR37_CURRENT() != null) {
							cell2.setCellValue(record.getR37_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR37_CALL() != null) {
							cell3.setCellValue(record.getR37_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR37_SAVINGS() != null) {
							cell4.setCellValue(record.getR37_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR37_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR37_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR37_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR37_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR37_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR37_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR37 = (record.getR37_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR37_1_2M_FD())
								.add(record.getR37_4_6M_FD() == null ? BigDecimal.ZERO : record.getR37_4_6M_FD());

						if (totalR37.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR37.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR37_7_12M_FD() != null) {
							cell10.setCellValue(record.getR37_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR37_13_18M_FD() != null) {
							cell11.setCellValue(record.getR37_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR37_19_24M_FD() != null) {
							cell12.setCellValue(record.getR37_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR37_OVER24_FD() != null) {
							cell13.setCellValue(record.getR37_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R38
						// ==========================

						row = sheet.getRow(38);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR38_CURRENT() != null) {
							cell2.setCellValue(record.getR38_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR38_CALL() != null) {
							cell3.setCellValue(record.getR38_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR38_SAVINGS() != null) {
							cell4.setCellValue(record.getR38_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR38_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR38_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR38_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR38_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR38_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR38_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR38 = (record.getR38_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR38_1_2M_FD())
								.add(record.getR38_4_6M_FD() == null ? BigDecimal.ZERO : record.getR38_4_6M_FD());

						if (totalR38.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR38.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR38_7_12M_FD() != null) {
							cell10.setCellValue(record.getR38_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR38_13_18M_FD() != null) {
							cell11.setCellValue(record.getR38_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR38_19_24M_FD() != null) {
							cell12.setCellValue(record.getR38_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR38_OVER24_FD() != null) {
							cell13.setCellValue(record.getR38_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R39
						// ==========================

						row = sheet.getRow(39);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR39_CURRENT() != null) {
							cell2.setCellValue(record.getR39_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR39_CALL() != null) {
							cell3.setCellValue(record.getR39_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR39_SAVINGS() != null) {
							cell4.setCellValue(record.getR39_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR39_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR39_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR39_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR39_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR39_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR39_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR39 = (record.getR39_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR39_1_2M_FD())
								.add(record.getR39_4_6M_FD() == null ? BigDecimal.ZERO : record.getR39_4_6M_FD());

						if (totalR39.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR39.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR39_7_12M_FD() != null) {
							cell10.setCellValue(record.getR39_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR39_13_18M_FD() != null) {
							cell11.setCellValue(record.getR39_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR39_19_24M_FD() != null) {
							cell12.setCellValue(record.getR39_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR39_OVER24_FD() != null) {
							cell13.setCellValue(record.getR39_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R40
						// ==========================

						row = sheet.getRow(40);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR40_CURRENT() != null) {
							cell2.setCellValue(record.getR40_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR40_CALL() != null) {
							cell3.setCellValue(record.getR40_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR40_SAVINGS() != null) {
							cell4.setCellValue(record.getR40_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR40_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR40_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR40_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR40_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR40_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR40_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR40 = (record.getR40_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR40_1_2M_FD())
								.add(record.getR40_4_6M_FD() == null ? BigDecimal.ZERO : record.getR40_4_6M_FD());

						if (totalR40.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR40.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR40_7_12M_FD() != null) {
							cell10.setCellValue(record.getR40_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR40_13_18M_FD() != null) {
							cell11.setCellValue(record.getR40_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR40_19_24M_FD() != null) {
							cell12.setCellValue(record.getR40_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR40_OVER24_FD() != null) {
							cell13.setCellValue(record.getR40_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R41
						// ==========================

						row = sheet.getRow(41);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR41_CURRENT() != null) {
							cell2.setCellValue(record.getR41_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR41_CALL() != null) {
							cell3.setCellValue(record.getR41_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR41_SAVINGS() != null) {
							cell4.setCellValue(record.getR41_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR41_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR41_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR41_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR41_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR41_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR41_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR41 = (record.getR41_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR41_1_2M_FD())
								.add(record.getR41_4_6M_FD() == null ? BigDecimal.ZERO : record.getR41_4_6M_FD());

						if (totalR41.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR41.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR41_7_12M_FD() != null) {
							cell10.setCellValue(record.getR41_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR41_13_18M_FD() != null) {
							cell11.setCellValue(record.getR41_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR41_19_24M_FD() != null) {
							cell12.setCellValue(record.getR41_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR41_OVER24_FD() != null) {
							cell13.setCellValue(record.getR41_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R42
						// ==========================

						row = sheet.getRow(42);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR42_CURRENT() != null) {
							cell2.setCellValue(record.getR42_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR42_CALL() != null) {
							cell3.setCellValue(record.getR42_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR42_SAVINGS() != null) {
							cell4.setCellValue(record.getR42_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR42_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR42_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR42_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR42_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR42_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR42_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR42 = (record.getR42_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR42_1_2M_FD())
								.add(record.getR42_4_6M_FD() == null ? BigDecimal.ZERO : record.getR42_4_6M_FD());

						if (totalR42.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR42.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR42_7_12M_FD() != null) {
							cell10.setCellValue(record.getR42_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR42_13_18M_FD() != null) {
							cell11.setCellValue(record.getR42_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR42_19_24M_FD() != null) {
							cell12.setCellValue(record.getR42_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR42_OVER24_FD() != null) {
							cell13.setCellValue(record.getR42_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R43
						// ==========================

						row = sheet.getRow(44);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR43_CURRENT() != null) {
							cell2.setCellValue(record.getR43_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR43_CALL() != null) {
							cell3.setCellValue(record.getR43_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR43_SAVINGS() != null) {
							cell4.setCellValue(record.getR43_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR43_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR43_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR43_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR43_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR43_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR43_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR43 = (record.getR43_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR43_1_2M_FD())
								.add(record.getR43_4_6M_FD() == null ? BigDecimal.ZERO : record.getR43_4_6M_FD());

						if (totalR43.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR43.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR43_7_12M_FD() != null) {
							cell10.setCellValue(record.getR43_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR43_13_18M_FD() != null) {
							cell11.setCellValue(record.getR43_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR43_19_24M_FD() != null) {
							cell12.setCellValue(record.getR43_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR43_OVER24_FD() != null) {
							cell13.setCellValue(record.getR43_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						
						row = sheet.getRow(45);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR48_CURRENT() != null) {
							cell2.setCellValue(record.getR48_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR48_CALL() != null) {
							cell3.setCellValue(record.getR48_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR48_SAVINGS() != null) {
							cell4.setCellValue(record.getR48_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR48_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR48_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR48_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR48_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR48_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR48_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR48 = (record.getR48_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR48_1_2M_FD())
								.add(record.getR48_4_6M_FD() == null ? BigDecimal.ZERO : record.getR48_4_6M_FD());

						if (totalR48.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR48.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR48_7_12M_FD() != null) {
							cell10.setCellValue(record.getR48_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR48_13_18M_FD() != null) {
							cell11.setCellValue(record.getR48_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR48_19_24M_FD() != null) {
							cell12.setCellValue(record.getR48_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR48_OVER24_FD() != null) {
							cell13.setCellValue(record.getR48_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R44
						// ==========================

						row = sheet.getRow(46);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR44_CURRENT() != null) {
							cell2.setCellValue(record.getR44_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR44_CALL() != null) {
							cell3.setCellValue(record.getR44_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR44_SAVINGS() != null) {
							cell4.setCellValue(record.getR44_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR44_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR44_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR44_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR44_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR44_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR44_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR44 = (record.getR44_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR44_1_2M_FD())
								.add(record.getR44_4_6M_FD() == null ? BigDecimal.ZERO : record.getR44_4_6M_FD());

						if (totalR44.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR44.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR44_7_12M_FD() != null) {
							cell10.setCellValue(record.getR44_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR44_13_18M_FD() != null) {
							cell11.setCellValue(record.getR44_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR44_19_24M_FD() != null) {
							cell12.setCellValue(record.getR44_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR44_OVER24_FD() != null) {
							cell13.setCellValue(record.getR44_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R45
						// ==========================

						row = sheet.getRow(47);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR45_CURRENT() != null) {
							cell2.setCellValue(record.getR45_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR45_CALL() != null) {
							cell3.setCellValue(record.getR45_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR45_SAVINGS() != null) {
							cell4.setCellValue(record.getR45_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR45_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR45_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR45_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR45_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR45_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR45_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR45 = (record.getR45_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR45_1_2M_FD())
								.add(record.getR45_4_6M_FD() == null ? BigDecimal.ZERO : record.getR45_4_6M_FD());

						if (totalR45.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR45.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR45_7_12M_FD() != null) {
							cell10.setCellValue(record.getR45_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR45_13_18M_FD() != null) {
							cell11.setCellValue(record.getR45_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR45_19_24M_FD() != null) {
							cell12.setCellValue(record.getR45_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR45_OVER24_FD() != null) {
							cell13.setCellValue(record.getR45_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R46
						// ==========================

						row = sheet.getRow(48);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR46_CURRENT() != null) {
							cell2.setCellValue(record.getR46_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR46_CALL() != null) {
							cell3.setCellValue(record.getR46_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR46_SAVINGS() != null) {
							cell4.setCellValue(record.getR46_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR46_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR46_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR46_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR46_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR46_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR46_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR46 = (record.getR46_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR46_1_2M_FD())
								.add(record.getR46_4_6M_FD() == null ? BigDecimal.ZERO : record.getR46_4_6M_FD());

						if (totalR46.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR46.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR46_7_12M_FD() != null) {
							cell10.setCellValue(record.getR46_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR46_13_18M_FD() != null) {
							cell11.setCellValue(record.getR46_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR46_19_24M_FD() != null) {
							cell12.setCellValue(record.getR46_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR46_OVER24_FD() != null) {
							cell13.setCellValue(record.getR46_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
//						// ==========================
//						// R47
//						// ==========================
//
//						row = sheet.getRow(48);
//
//						// Column2
//						cell2 = row.createCell(3);
//						if (record.getR47_CURRENT() != null) {
//							cell2.setCellValue(record.getR47_CURRENT().doubleValue());
//							cell2.setCellStyle(numberStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}
//
//						// Column3
//						cell3 = row.createCell(4);
//						if (record.getR47_CALL() != null) {
//							cell3.setCellValue(record.getR47_CALL().doubleValue());
//							cell3.setCellStyle(numberStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}
//
//						// Column4
//						cell4 = row.createCell(5);
//						if (record.getR47_SAVINGS() != null) {
//							cell4.setCellValue(record.getR47_SAVINGS().doubleValue());
//							cell4.setCellStyle(numberStyle);
//						} else {
//							cell4.setCellValue("");
//							cell4.setCellStyle(textStyle);
//						}
//
//						// Column5
//						cell5 = row.createCell(6);
//						if (record.getR47_0_31D_NOTICE() != null) {
//							cell5.setCellValue(record.getR47_0_31D_NOTICE().doubleValue());
//							cell5.setCellStyle(numberStyle);
//						} else {
//							cell5.setCellValue("");
//							cell5.setCellStyle(textStyle);
//						}
//
//						// Column6
//						cell6 = row.createCell(7);
//						if (record.getR47_32_88D_NOTICE() != null) {
//							cell6.setCellValue(record.getR47_32_88D_NOTICE().doubleValue());
//							cell6.setCellStyle(numberStyle);
//						} else {
//							cell6.setCellValue("");
//							cell6.setCellStyle(textStyle);
//						}
//
//						// Column7
//						cell7 = row.createCell(8);
//						if (record.getR47_91D_DEPOSIT() != null) {
//							cell7.setCellValue(record.getR47_91D_DEPOSIT().doubleValue());
//							cell7.setCellStyle(numberStyle);
//						} else {
//							cell7.setCellValue("");
//							cell7.setCellStyle(textStyle);
//						}
//
//						// Column8 (1-2M + 4-6M)
//						cell8 = row.createCell(9);
//						BigDecimal totalR47 = (record.getR47_1_2M_FD() == null ? BigDecimal.ZERO
//								: record.getR47_1_2M_FD())
//								.add(record.getR47_4_6M_FD() == null ? BigDecimal.ZERO : record.getR47_4_6M_FD());
//
//						if (totalR47.compareTo(BigDecimal.ZERO) != 0) {
//							cell8.setCellValue(totalR47.doubleValue());
//							cell8.setCellStyle(numberStyle);
//						} else {
//							cell8.setCellValue("");
//							cell8.setCellStyle(textStyle);
//						}
//
//						// Column10
//						cell10 = row.createCell(10);
//						if (record.getR47_7_12M_FD() != null) {
//							cell10.setCellValue(record.getR47_7_12M_FD().doubleValue());
//							cell10.setCellStyle(numberStyle);
//						} else {
//							cell10.setCellValue("");
//							cell10.setCellStyle(textStyle);
//						}
//
//						// Column11
//						cell11 = row.createCell(11);
//						if (record.getR47_13_18M_FD() != null) {
//							cell11.setCellValue(record.getR47_13_18M_FD().doubleValue());
//							cell11.setCellStyle(numberStyle);
//						} else {
//							cell11.setCellValue("");
//							cell11.setCellStyle(textStyle);
//						}
//
//						// Column12
//						cell12 = row.createCell(12);
//						if (record.getR47_19_24M_FD() != null) {
//							cell12.setCellValue(record.getR47_19_24M_FD().doubleValue());
//							cell12.setCellStyle(numberStyle);
//						} else {
//							cell12.setCellValue("");
//							cell12.setCellStyle(textStyle);
//						}
//
//						// Column13
//						cell13 = row.createCell(13);
//						if (record.getR47_OVER24_FD() != null) {
//							cell13.setCellValue(record.getR47_OVER24_FD().doubleValue());
//							cell13.setCellStyle(numberStyle);
//						} else {
//							cell13.setCellValue("");
//							cell13.setCellStyle(textStyle);
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
	public byte[] getExcelQ_SMME_DEPARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_Q_SMME_DEPEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<Q_SMME_DEP_Archival_Summary_Entity> dataList = Q_SMME_DEP_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_SMME_DEP report. Returning empty result.");
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
					Q_SMME_DEP_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL

					// ROW11
					// COLUMN2
					Cell cell2 = row.createCell(1);
					if (record.getR11_CURRENT() != null) {
						cell2.setCellValue(record.getR11_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					// Column3
					Cell cell3 = row.createCell(2);
					if (record.getR11_CALL() != null) {
						cell3.setCellValue(record.getR11_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row11
					// Column4
					Cell cell4 = row.createCell(3);
					if (record.getR11_SAVINGS() != null) {
						cell4.setCellValue(record.getR11_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column5
					Cell cell5 = row.createCell(4);
					if (record.getR11_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR11_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					// Column6
					Cell cell6 = row.createCell(5);
					if (record.getR11_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR11_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row11
					// Column7
					Cell cell7 = row.createCell(6);
					if (record.getR11_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR11_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row11
					// Column8
					Cell cell8 = row.createCell(7);
					if (record.getR11_1_2M_FD() != null) {
						cell8.setCellValue(record.getR11_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row11
					// Column9
					Cell cell9 = row.createCell(8);
					if (record.getR11_4_6M_FD() != null) {
						cell9.setCellValue(record.getR11_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row11
					// Column10
					Cell cell10 = row.createCell(9);
					if (record.getR11_7_12M_FD() != null) {
						cell10.setCellValue(record.getR11_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// row11
					// Column11
					Cell cell11 = row.createCell(10);
					if (record.getR11_13_18M_FD() != null) {
						cell11.setCellValue(record.getR11_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row11
					// Column12
					Cell cell12 = row.createCell(11);
					if (record.getR11_19_24M_FD() != null) {
						cell12.setCellValue(record.getR11_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// row11
					// Column13
					Cell cell13 = row.createCell(12);
					if (record.getR11_OVER24_FD() != null) {
						cell13.setCellValue(record.getR11_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// row11
					// Column14
//					Cell cell14 = row.createCell(13);
//					if (record.getR11_TOTAL() != null) {
//						cell14.setCellValue(record.getR11_TOTAL().doubleValue());
//						cell14.setCellStyle(numberStyle);
//					} else {
//						cell14.setCellValue("");
//						cell14.setCellStyle(textStyle);
//					}

					// row11
					// Column15
					Cell cell15 = row.createCell(14);
					if (record.getR11_NOACC() != null) {
						cell15.setCellValue(record.getR11_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);

					// Row 12
					// Column2
					cell2 = row.createCell(1);
					if (record.getR12_CURRENT() != null) {
						cell2.setCellValue(record.getR12_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR12_CALL() != null) {
						cell3.setCellValue(record.getR12_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR12_SAVINGS() != null) {
						cell4.setCellValue(record.getR12_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR12_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR12_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR12_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR12_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR12_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR12_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR12_1_2M_FD() != null) {
						cell8.setCellValue(record.getR12_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR12_4_6M_FD() != null) {
						cell9.setCellValue(record.getR12_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR12_7_12M_FD() != null) {
						cell10.setCellValue(record.getR12_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR12_13_18M_FD() != null) {
						cell11.setCellValue(record.getR12_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR12_19_24M_FD() != null) {
						cell12.setCellValue(record.getR12_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR12_OVER24_FD() != null) {
						cell13.setCellValue(record.getR12_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR12_TOTAL() != null) {
//					    cell14.setCellValue(record.getR12_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR12_NOACC() != null) {
						cell15.setCellValue(record.getR12_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					// Row 13
					// Column2
					cell2 = row.createCell(1);
					if (record.getR13_CURRENT() != null) {
						cell2.setCellValue(record.getR13_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR13_CALL() != null) {
						cell3.setCellValue(record.getR13_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR13_SAVINGS() != null) {
						cell4.setCellValue(record.getR13_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR13_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR13_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR13_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR13_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR13_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR13_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR13_1_2M_FD() != null) {
						cell8.setCellValue(record.getR13_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR13_4_6M_FD() != null) {
						cell9.setCellValue(record.getR13_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR13_7_12M_FD() != null) {
						cell10.setCellValue(record.getR13_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR13_13_18M_FD() != null) {
						cell11.setCellValue(record.getR13_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR13_19_24M_FD() != null) {
						cell12.setCellValue(record.getR13_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR13_OVER24_FD() != null) {
						cell13.setCellValue(record.getR13_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR13_TOTAL() != null) {
//					    cell14.setCellValue(record.getR13_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR13_NOACC() != null) {
						cell15.setCellValue(record.getR13_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					// Row 14
					// Column2
					cell2 = row.createCell(1);
					if (record.getR14_CURRENT() != null) {
						cell2.setCellValue(record.getR14_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR14_CALL() != null) {
						cell3.setCellValue(record.getR14_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR14_SAVINGS() != null) {
						cell4.setCellValue(record.getR14_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR14_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR14_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR14_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR14_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR14_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR14_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR14_1_2M_FD() != null) {
						cell8.setCellValue(record.getR14_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR14_4_6M_FD() != null) {
						cell9.setCellValue(record.getR14_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR14_7_12M_FD() != null) {
						cell10.setCellValue(record.getR14_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR14_13_18M_FD() != null) {
						cell11.setCellValue(record.getR14_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR14_19_24M_FD() != null) {
						cell12.setCellValue(record.getR14_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR14_OVER24_FD() != null) {
						cell13.setCellValue(record.getR14_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR14_NOACC() != null) {
						cell15.setCellValue(record.getR14_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 14
					row = sheet.getRow(14);

					// Row 15
					// Column2
					cell2 = row.createCell(1);
					if (record.getR15_CURRENT() != null) {
						cell2.setCellValue(record.getR15_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR15_CALL() != null) {
						cell3.setCellValue(record.getR15_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR15_SAVINGS() != null) {
						cell4.setCellValue(record.getR15_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR15_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR15_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR15_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR15_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR15_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR15_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR15_1_2M_FD() != null) {
						cell8.setCellValue(record.getR15_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR15_4_6M_FD() != null) {
						cell9.setCellValue(record.getR15_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR15_7_12M_FD() != null) {
						cell10.setCellValue(record.getR15_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR15_13_18M_FD() != null) {
						cell11.setCellValue(record.getR15_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR15_19_24M_FD() != null) {
						cell12.setCellValue(record.getR15_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR15_OVER24_FD() != null) {
						cell13.setCellValue(record.getR15_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR15_NOACC() != null) {
						cell15.setCellValue(record.getR15_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 15
					row = sheet.getRow(15);

					// Row 16
					// Column2
					cell2 = row.createCell(1);
					if (record.getR16_CURRENT() != null) {
						cell2.setCellValue(record.getR16_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR16_CALL() != null) {
						cell3.setCellValue(record.getR16_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR16_SAVINGS() != null) {
						cell4.setCellValue(record.getR16_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR16_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR16_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR16_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR16_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR16_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR16_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR16_1_2M_FD() != null) {
						cell8.setCellValue(record.getR16_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR16_4_6M_FD() != null) {
						cell9.setCellValue(record.getR16_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR16_7_12M_FD() != null) {
						cell10.setCellValue(record.getR16_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR16_13_18M_FD() != null) {
						cell11.setCellValue(record.getR16_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR16_19_24M_FD() != null) {
						cell12.setCellValue(record.getR16_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR16_OVER24_FD() != null) {
						cell13.setCellValue(record.getR16_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR16_NOACC() != null) {
						cell15.setCellValue(record.getR16_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 16
					row = sheet.getRow(16);

					// Row 17
					// Column2
					cell2 = row.createCell(1);
					if (record.getR17_CURRENT() != null) {
						cell2.setCellValue(record.getR17_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR17_CALL() != null) {
						cell3.setCellValue(record.getR17_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR17_SAVINGS() != null) {
						cell4.setCellValue(record.getR17_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR17_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR17_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR17_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR17_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR17_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR17_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR17_1_2M_FD() != null) {
						cell8.setCellValue(record.getR17_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR17_4_6M_FD() != null) {
						cell9.setCellValue(record.getR17_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR17_7_12M_FD() != null) {
						cell10.setCellValue(record.getR17_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR17_13_18M_FD() != null) {
						cell11.setCellValue(record.getR17_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR17_19_24M_FD() != null) {
						cell12.setCellValue(record.getR17_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR17_OVER24_FD() != null) {
						cell13.setCellValue(record.getR17_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR17_NOACC() != null) {
						cell15.setCellValue(record.getR17_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 17
					row = sheet.getRow(17);

					// Row 18
					// Column2
					cell2 = row.createCell(1);
					if (record.getR18_CURRENT() != null) {
						cell2.setCellValue(record.getR18_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR18_CALL() != null) {
						cell3.setCellValue(record.getR18_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR18_SAVINGS() != null) {
						cell4.setCellValue(record.getR18_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR18_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR18_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR18_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR18_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR18_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR18_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR18_1_2M_FD() != null) {
						cell8.setCellValue(record.getR18_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR18_4_6M_FD() != null) {
						cell9.setCellValue(record.getR18_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR18_7_12M_FD() != null) {
						cell10.setCellValue(record.getR18_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR18_13_18M_FD() != null) {
						cell11.setCellValue(record.getR18_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR18_19_24M_FD() != null) {
						cell12.setCellValue(record.getR18_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR18_OVER24_FD() != null) {
						cell13.setCellValue(record.getR18_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR18_NOACC() != null) {
						cell15.setCellValue(record.getR18_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 18
					row = sheet.getRow(18);

					// Row 19
					// Column2
					cell2 = row.createCell(1);
					if (record.getR19_CURRENT() != null) {
						cell2.setCellValue(record.getR19_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR19_CALL() != null) {
						cell3.setCellValue(record.getR19_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR19_SAVINGS() != null) {
						cell4.setCellValue(record.getR19_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR19_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR19_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR19_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR19_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR19_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR19_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR19_1_2M_FD() != null) {
						cell8.setCellValue(record.getR19_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR19_4_6M_FD() != null) {
						cell9.setCellValue(record.getR19_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR19_7_12M_FD() != null) {
						cell10.setCellValue(record.getR19_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR19_13_18M_FD() != null) {
						cell11.setCellValue(record.getR19_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR19_19_24M_FD() != null) {
						cell12.setCellValue(record.getR19_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR19_OVER24_FD() != null) {
						cell13.setCellValue(record.getR19_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR19_NOACC() != null) {
						cell15.setCellValue(record.getR19_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 19
					row = sheet.getRow(19);

					// Row 20
					// Column2
					cell2 = row.createCell(1);
					if (record.getR20_CURRENT() != null) {
						cell2.setCellValue(record.getR20_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR20_CALL() != null) {
						cell3.setCellValue(record.getR20_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR20_SAVINGS() != null) {
						cell4.setCellValue(record.getR20_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR20_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR20_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR20_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR20_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR20_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR20_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR20_1_2M_FD() != null) {
						cell8.setCellValue(record.getR20_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR20_4_6M_FD() != null) {
						cell9.setCellValue(record.getR20_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR20_7_12M_FD() != null) {
						cell10.setCellValue(record.getR20_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR20_13_18M_FD() != null) {
						cell11.setCellValue(record.getR20_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR20_19_24M_FD() != null) {
						cell12.setCellValue(record.getR20_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR20_OVER24_FD() != null) {
						cell13.setCellValue(record.getR20_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR20_NOACC() != null) {
						cell15.setCellValue(record.getR20_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 20
					row = sheet.getRow(20);

					// Row 21
					// Column2
					cell2 = row.createCell(1);
					if (record.getR21_CURRENT() != null) {
						cell2.setCellValue(record.getR21_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR21_CALL() != null) {
						cell3.setCellValue(record.getR21_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR21_SAVINGS() != null) {
						cell4.setCellValue(record.getR21_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR21_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR21_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR21_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR21_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR21_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR21_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR21_1_2M_FD() != null) {
						cell8.setCellValue(record.getR21_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR21_4_6M_FD() != null) {
						cell9.setCellValue(record.getR21_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR21_7_12M_FD() != null) {
						cell10.setCellValue(record.getR21_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR21_13_18M_FD() != null) {
						cell11.setCellValue(record.getR21_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR21_19_24M_FD() != null) {
						cell12.setCellValue(record.getR21_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR21_OVER24_FD() != null) {
						cell13.setCellValue(record.getR21_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR21_NOACC() != null) {
						cell15.setCellValue(record.getR21_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 21
					row = sheet.getRow(21);

					// Row 22
					// Column2
					cell2 = row.createCell(1);
					if (record.getR22_CURRENT() != null) {
						cell2.setCellValue(record.getR22_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR22_CALL() != null) {
						cell3.setCellValue(record.getR22_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR22_SAVINGS() != null) {
						cell4.setCellValue(record.getR22_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR22_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR22_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR22_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR22_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR22_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR22_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR22_1_2M_FD() != null) {
						cell8.setCellValue(record.getR22_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR22_4_6M_FD() != null) {
						cell9.setCellValue(record.getR22_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR22_7_12M_FD() != null) {
						cell10.setCellValue(record.getR22_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR22_13_18M_FD() != null) {
						cell11.setCellValue(record.getR22_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR22_19_24M_FD() != null) {
						cell12.setCellValue(record.getR22_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR22_OVER24_FD() != null) {
						cell13.setCellValue(record.getR22_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR22_NOACC() != null) {
						cell15.setCellValue(record.getR22_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 22
					row = sheet.getRow(22);

					// Row 23
					// Column2
					cell2 = row.createCell(1);
					if (record.getR23_CURRENT() != null) {
						cell2.setCellValue(record.getR23_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR23_CALL() != null) {
						cell3.setCellValue(record.getR23_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR23_SAVINGS() != null) {
						cell4.setCellValue(record.getR23_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR23_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR23_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR23_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR23_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR23_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR23_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR23_1_2M_FD() != null) {
						cell8.setCellValue(record.getR23_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR23_4_6M_FD() != null) {
						cell9.setCellValue(record.getR23_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR23_7_12M_FD() != null) {
						cell10.setCellValue(record.getR23_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR23_13_18M_FD() != null) {
						cell11.setCellValue(record.getR23_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR23_19_24M_FD() != null) {
						cell12.setCellValue(record.getR23_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR23_OVER24_FD() != null) {
						cell13.setCellValue(record.getR23_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR23_NOACC() != null) {
						cell15.setCellValue(record.getR23_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 23
					row = sheet.getRow(23);

					// Row 24
					// Column2
					cell2 = row.createCell(1);
					if (record.getR24_CURRENT() != null) {
						cell2.setCellValue(record.getR24_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR24_CALL() != null) {
						cell3.setCellValue(record.getR24_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR24_SAVINGS() != null) {
						cell4.setCellValue(record.getR24_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR24_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR24_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR24_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR24_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR24_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR24_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR24_1_2M_FD() != null) {
						cell8.setCellValue(record.getR24_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR24_4_6M_FD() != null) {
						cell9.setCellValue(record.getR24_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR24_7_12M_FD() != null) {
						cell10.setCellValue(record.getR24_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR24_13_18M_FD() != null) {
						cell11.setCellValue(record.getR24_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR24_19_24M_FD() != null) {
						cell12.setCellValue(record.getR24_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR24_OVER24_FD() != null) {
						cell13.setCellValue(record.getR24_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR24_NOACC() != null) {
						cell15.setCellValue(record.getR24_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 25
					row = sheet.getRow(25);

					// Row 26
					// Column2
					cell2 = row.createCell(1);
					if (record.getR26_CURRENT() != null) {
						cell2.setCellValue(record.getR26_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR26_CALL() != null) {
						cell3.setCellValue(record.getR26_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR26_SAVINGS() != null) {
						cell4.setCellValue(record.getR26_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR26_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR26_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR26_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR26_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR26_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR26_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR26_1_2M_FD() != null) {
						cell8.setCellValue(record.getR26_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR26_4_6M_FD() != null) {
						cell9.setCellValue(record.getR26_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR26_7_12M_FD() != null) {
						cell10.setCellValue(record.getR26_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR26_13_18M_FD() != null) {
						cell11.setCellValue(record.getR26_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR26_19_24M_FD() != null) {
						cell12.setCellValue(record.getR26_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR26_OVER24_FD() != null) {
						cell13.setCellValue(record.getR26_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR26_TOTAL() != null) {
//					    cell14.setCellValue(record.getR26_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR26_NOACC() != null) {
						cell15.setCellValue(record.getR26_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 26
					row = sheet.getRow(31);

					// Row 32
					// Column2
					cell2 = row.createCell(1);
					if (record.getR32_CURRENT() != null) {
						System.out.println(record.getR32_CURRENT());
						cell2.setCellValue(record.getR32_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR32_CALL() != null) {
						cell3.setCellValue(record.getR32_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR32_SAVINGS() != null) {
						cell4.setCellValue(record.getR32_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR32_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR32_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR32_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR32_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR32_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR32_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR32_1_2M_FD() != null) {
						cell8.setCellValue(record.getR32_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR32_4_6M_FD() != null) {
						cell9.setCellValue(record.getR32_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR32_7_12M_FD() != null) {
						cell10.setCellValue(record.getR32_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR32_13_18M_FD() != null) {
						cell11.setCellValue(record.getR32_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR32_19_24M_FD() != null) {
						cell12.setCellValue(record.getR32_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR32_OVER24_FD() != null) {
						cell13.setCellValue(record.getR32_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR32_TOTAL() != null) {
//					    cell14.setCellValue(record.getR32_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR32_NOACC() != null) {
						cell15.setCellValue(record.getR32_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 32
					row = sheet.getRow(32);

					// Row 33
					// Column2
					cell2 = row.createCell(1);
					if (record.getR33_CURRENT() != null) {
						cell2.setCellValue(record.getR33_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR33_CALL() != null) {
						cell3.setCellValue(record.getR33_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR33_SAVINGS() != null) {
						cell4.setCellValue(record.getR33_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR33_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR33_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR33_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR33_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR33_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR33_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR33_1_2M_FD() != null) {
						cell8.setCellValue(record.getR33_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR33_4_6M_FD() != null) {
						cell9.setCellValue(record.getR33_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR33_7_12M_FD() != null) {
						cell10.setCellValue(record.getR33_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR33_13_18M_FD() != null) {
						cell11.setCellValue(record.getR33_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR33_19_24M_FD() != null) {
						cell12.setCellValue(record.getR33_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR33_OVER24_FD() != null) {
						cell13.setCellValue(record.getR33_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR33_TOTAL() != null) {
//					    cell14.setCellValue(record.getR33_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR33_NOACC() != null) {
						cell15.setCellValue(record.getR33_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 33
					row = sheet.getRow(33);

					// Row 34
					// Column2
					cell2 = row.createCell(1);
					if (record.getR34_CURRENT() != null) {
						cell2.setCellValue(record.getR34_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR34_CALL() != null) {
						cell3.setCellValue(record.getR34_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR34_SAVINGS() != null) {
						cell4.setCellValue(record.getR34_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR34_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR34_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR34_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR34_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR34_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR34_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR34_1_2M_FD() != null) {
						cell8.setCellValue(record.getR34_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR34_4_6M_FD() != null) {
						cell9.setCellValue(record.getR34_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR34_7_12M_FD() != null) {
						cell10.setCellValue(record.getR34_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR34_13_18M_FD() != null) {
						cell11.setCellValue(record.getR34_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR34_19_24M_FD() != null) {
						cell12.setCellValue(record.getR34_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR34_OVER24_FD() != null) {
						cell13.setCellValue(record.getR34_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR34_TOTAL() != null) {
//					    cell14.setCellValue(record.getR34_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR34_NOACC() != null) {
						cell15.setCellValue(record.getR34_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 34
					row = sheet.getRow(34);

					// Row 35
					// Column2
					cell2 = row.createCell(1);
					if (record.getR35_CURRENT() != null) {
						cell2.setCellValue(record.getR35_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR35_CALL() != null) {
						cell3.setCellValue(record.getR35_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR35_SAVINGS() != null) {
						cell4.setCellValue(record.getR35_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR35_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR35_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR35_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR35_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR35_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR35_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR35_1_2M_FD() != null) {
						cell8.setCellValue(record.getR35_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR35_4_6M_FD() != null) {
						cell9.setCellValue(record.getR35_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR35_7_12M_FD() != null) {
						cell10.setCellValue(record.getR35_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR35_13_18M_FD() != null) {
						cell11.setCellValue(record.getR35_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR35_19_24M_FD() != null) {
						cell12.setCellValue(record.getR35_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR35_OVER24_FD() != null) {
						cell13.setCellValue(record.getR35_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR35_TOTAL() != null) {
//					    cell14.setCellValue(record.getR35_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR35_NOACC() != null) {
						cell15.setCellValue(record.getR35_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 35
					row = sheet.getRow(35);

					// Row 36
					// Column2
					cell2 = row.createCell(1);
					if (record.getR36_CURRENT() != null) {
						cell2.setCellValue(record.getR36_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR36_CALL() != null) {
						cell3.setCellValue(record.getR36_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR36_SAVINGS() != null) {
						cell4.setCellValue(record.getR36_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR36_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR36_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR36_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR36_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR36_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR36_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR36_1_2M_FD() != null) {
						cell8.setCellValue(record.getR36_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR36_4_6M_FD() != null) {
						cell9.setCellValue(record.getR36_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR36_7_12M_FD() != null) {
						cell10.setCellValue(record.getR36_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR36_13_18M_FD() != null) {
						cell11.setCellValue(record.getR36_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR36_19_24M_FD() != null) {
						cell12.setCellValue(record.getR36_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR36_OVER24_FD() != null) {
						cell13.setCellValue(record.getR36_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR36_NOACC() != null) {
						cell15.setCellValue(record.getR36_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 36
					row = sheet.getRow(36);

					// Row 37
					// Column2
					cell2 = row.createCell(1);
					if (record.getR37_CURRENT() != null) {
						cell2.setCellValue(record.getR37_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR37_CALL() != null) {
						cell3.setCellValue(record.getR37_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR37_SAVINGS() != null) {
						cell4.setCellValue(record.getR37_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR37_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR37_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR37_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR37_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR37_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR37_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR37_1_2M_FD() != null) {
						cell8.setCellValue(record.getR37_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR37_4_6M_FD() != null) {
						cell9.setCellValue(record.getR37_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR37_7_12M_FD() != null) {
						cell10.setCellValue(record.getR37_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR37_13_18M_FD() != null) {
						cell11.setCellValue(record.getR37_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR37_19_24M_FD() != null) {
						cell12.setCellValue(record.getR37_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR37_OVER24_FD() != null) {
						cell13.setCellValue(record.getR37_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR37_TOTAL() != null) {
//					    cell14.setCellValue(record.getR37_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR37_NOACC() != null) {
						cell15.setCellValue(record.getR37_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 37
					row = sheet.getRow(37);

					// Row 38
					// Column2
					cell2 = row.createCell(1);
					if (record.getR38_CURRENT() != null) {
						cell2.setCellValue(record.getR38_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR38_CALL() != null) {
						cell3.setCellValue(record.getR38_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR38_SAVINGS() != null) {
						cell4.setCellValue(record.getR38_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR38_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR38_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR38_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR38_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR38_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR38_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR38_1_2M_FD() != null) {
						cell8.setCellValue(record.getR38_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR38_4_6M_FD() != null) {
						cell9.setCellValue(record.getR38_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR38_7_12M_FD() != null) {
						cell10.setCellValue(record.getR38_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR38_13_18M_FD() != null) {
						cell11.setCellValue(record.getR38_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR38_19_24M_FD() != null) {
						cell12.setCellValue(record.getR38_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR38_OVER24_FD() != null) {
						cell13.setCellValue(record.getR38_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR38_NOACC() != null) {
						cell15.setCellValue(record.getR38_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 38
					row = sheet.getRow(38);

					// Row 39
					// Column2
					cell2 = row.createCell(1);
					if (record.getR39_CURRENT() != null) {
						cell2.setCellValue(record.getR39_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR39_CALL() != null) {
						cell3.setCellValue(record.getR39_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR39_SAVINGS() != null) {
						cell4.setCellValue(record.getR39_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR39_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR39_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR39_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR39_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR39_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR39_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR39_1_2M_FD() != null) {
						cell8.setCellValue(record.getR39_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR39_4_6M_FD() != null) {
						cell9.setCellValue(record.getR39_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR39_7_12M_FD() != null) {
						cell10.setCellValue(record.getR39_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR39_13_18M_FD() != null) {
						cell11.setCellValue(record.getR39_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR39_19_24M_FD() != null) {
						cell12.setCellValue(record.getR39_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR39_OVER24_FD() != null) {
						cell13.setCellValue(record.getR39_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR39_TOTAL() != null) {
//					    cell14.setCellValue(record.getR39_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR39_NOACC() != null) {
						cell15.setCellValue(record.getR39_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 39
					row = sheet.getRow(39);

					// Row 40
					// Column2
					cell2 = row.createCell(1);
					if (record.getR40_CURRENT() != null) {
						cell2.setCellValue(record.getR40_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR40_CALL() != null) {
						cell3.setCellValue(record.getR40_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR40_SAVINGS() != null) {
						cell4.setCellValue(record.getR40_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR40_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR40_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR40_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR40_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR40_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR40_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR40_1_2M_FD() != null) {
						cell8.setCellValue(record.getR40_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR40_4_6M_FD() != null) {
						cell9.setCellValue(record.getR40_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR40_7_12M_FD() != null) {
						cell10.setCellValue(record.getR40_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR40_13_18M_FD() != null) {
						cell11.setCellValue(record.getR40_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR40_19_24M_FD() != null) {
						cell12.setCellValue(record.getR40_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR40_OVER24_FD() != null) {
						cell13.setCellValue(record.getR40_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR40_NOACC() != null) {
						cell15.setCellValue(record.getR40_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 40
					row = sheet.getRow(40);

					// Row 41
					// Column2
					cell2 = row.createCell(1);
					if (record.getR41_CURRENT() != null) {
						cell2.setCellValue(record.getR41_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR41_CALL() != null) {
						cell3.setCellValue(record.getR41_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR41_SAVINGS() != null) {
						cell4.setCellValue(record.getR41_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR41_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR41_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR41_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR41_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR41_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR41_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR41_1_2M_FD() != null) {
						cell8.setCellValue(record.getR41_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR41_4_6M_FD() != null) {
						cell9.setCellValue(record.getR41_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR41_7_12M_FD() != null) {
						cell10.setCellValue(record.getR41_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR41_13_18M_FD() != null) {
						cell11.setCellValue(record.getR41_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR41_19_24M_FD() != null) {
						cell12.setCellValue(record.getR41_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR41_OVER24_FD() != null) {
						cell13.setCellValue(record.getR41_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR41_NOACC() != null) {
						cell15.setCellValue(record.getR41_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 41
					row = sheet.getRow(41);

					// Row 42
					// Column2
					cell2 = row.createCell(1);
					if (record.getR42_CURRENT() != null) {
						cell2.setCellValue(record.getR42_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR42_CALL() != null) {
						cell3.setCellValue(record.getR42_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR42_SAVINGS() != null) {
						cell4.setCellValue(record.getR42_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR42_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR42_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR42_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR42_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR42_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR42_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR42_1_2M_FD() != null) {
						cell8.setCellValue(record.getR42_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR42_4_6M_FD() != null) {
						cell9.setCellValue(record.getR42_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR42_7_12M_FD() != null) {
						cell10.setCellValue(record.getR42_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR42_13_18M_FD() != null) {
						cell11.setCellValue(record.getR42_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR42_19_24M_FD() != null) {
						cell12.setCellValue(record.getR42_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR42_OVER24_FD() != null) {
						cell13.setCellValue(record.getR42_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR42_NOACC() != null) {
						cell15.setCellValue(record.getR42_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 42
					row = sheet.getRow(42);

					// Row 43
					// Column2
					cell2 = row.createCell(1);
					if (record.getR43_CURRENT() != null) {
						cell2.setCellValue(record.getR43_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR43_CALL() != null) {
						cell3.setCellValue(record.getR43_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR43_SAVINGS() != null) {
						cell4.setCellValue(record.getR43_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR43_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR43_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR43_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR43_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR43_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR43_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR43_1_2M_FD() != null) {
						cell8.setCellValue(record.getR43_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR43_4_6M_FD() != null) {
						cell9.setCellValue(record.getR43_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR43_7_12M_FD() != null) {
						cell10.setCellValue(record.getR43_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR43_13_18M_FD() != null) {
						cell11.setCellValue(record.getR43_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR43_19_24M_FD() != null) {
						cell12.setCellValue(record.getR43_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR43_OVER24_FD() != null) {
						cell13.setCellValue(record.getR43_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR43_NOACC() != null) {
						cell15.setCellValue(record.getR43_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 43
					row = sheet.getRow(43);

					// Row 44
					// Column2
					cell2 = row.createCell(1);
					if (record.getR44_CURRENT() != null) {
						cell2.setCellValue(record.getR44_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR44_CALL() != null) {
						cell3.setCellValue(record.getR44_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR44_SAVINGS() != null) {
						cell4.setCellValue(record.getR44_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR44_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR44_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR44_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR44_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR44_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR44_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR44_1_2M_FD() != null) {
						cell8.setCellValue(record.getR44_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR44_4_6M_FD() != null) {
						cell9.setCellValue(record.getR44_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR44_7_12M_FD() != null) {
						cell10.setCellValue(record.getR44_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR44_13_18M_FD() != null) {
						cell11.setCellValue(record.getR44_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR44_19_24M_FD() != null) {
						cell12.setCellValue(record.getR44_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR44_OVER24_FD() != null) {
						cell13.setCellValue(record.getR44_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR44_NOACC() != null) {
						cell15.setCellValue(record.getR44_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 44
					row = sheet.getRow(44);

					// Row 45
					// Column2
					cell2 = row.createCell(1);
					if (record.getR45_CURRENT() != null) {
						cell2.setCellValue(record.getR45_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR45_CALL() != null) {
						cell3.setCellValue(record.getR45_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR45_SAVINGS() != null) {
						cell4.setCellValue(record.getR45_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR45_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR45_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR45_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR45_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR45_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR45_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR45_1_2M_FD() != null) {
						cell8.setCellValue(record.getR45_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR45_4_6M_FD() != null) {
						cell9.setCellValue(record.getR45_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR45_7_12M_FD() != null) {
						cell10.setCellValue(record.getR45_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR45_13_18M_FD() != null) {
						cell11.setCellValue(record.getR45_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR45_19_24M_FD() != null) {
						cell12.setCellValue(record.getR45_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR45_OVER24_FD() != null) {
						cell13.setCellValue(record.getR45_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR45_NOACC() != null) {
						cell15.setCellValue(record.getR45_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 46
					row = sheet.getRow(46);

					// Row 47
					// Column2
					cell2 = row.createCell(1);
					if (record.getR47_CURRENT() != null) {
						cell2.setCellValue(record.getR47_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR47_CALL() != null) {
						cell3.setCellValue(record.getR47_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR47_SAVINGS() != null) {
						cell4.setCellValue(record.getR47_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR47_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR47_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR47_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR47_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR47_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR47_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR47_1_2M_FD() != null) {
						cell8.setCellValue(record.getR47_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR47_4_6M_FD() != null) {
						cell9.setCellValue(record.getR47_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR47_7_12M_FD() != null) {
						cell10.setCellValue(record.getR47_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR47_13_18M_FD() != null) {
						cell11.setCellValue(record.getR47_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR47_19_24M_FD() != null) {
						cell12.setCellValue(record.getR47_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR47_OVER24_FD() != null) {
						cell13.setCellValue(record.getR47_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR47_NOACC() != null) {
						cell15.setCellValue(record.getR47_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
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
	public byte[] BRRS_Q_SMME_DEPEmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<Q_SMME_DEP_Archival_Summary_Entity> dataList = Q_SMME_DEP_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_SMME_DEP report. Returning empty result.");
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
						Q_SMME_DEP_Archival_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL

						// ROW11
						// COLUMN2
						Cell cell2 = row.createCell(3);
						if (record.getR11_CURRENT() != null) {
							cell2.setCellValue(record.getR11_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row11
						// Column3
						Cell cell3 = row.createCell(4);
						if (record.getR11_CALL() != null) {
							cell3.setCellValue(record.getR11_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// row11
						// Column4
						Cell cell4 = row.createCell(5);
						if (record.getR11_SAVINGS() != null) {
							cell4.setCellValue(record.getR11_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row11
						// Column5
						Cell cell5 = row.createCell(6);
						if (record.getR11_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR11_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// row11
						// Column6
						Cell cell6 = row.createCell(7);
						if (record.getR11_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR11_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row11
						// Column7
						Cell cell7 = row.createCell(8);
						if (record.getR11_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR11_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						Cell cell8 = row.createCell(9);

						BigDecimal total = (record.getR11_1_2M_FD() == null ? BigDecimal.ZERO : record.getR11_1_2M_FD())
								.add(record.getR11_4_6M_FD() == null ? BigDecimal.ZERO : record.getR11_4_6M_FD());

						if (total.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}
						// row11
						// Column10
						Cell cell10 = row.createCell(10);
						if (record.getR11_7_12M_FD() != null) {
							cell10.setCellValue(record.getR11_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// row11
						// Column11
						Cell cell11 = row.createCell(11);
						if (record.getR11_13_18M_FD() != null) {
							cell11.setCellValue(record.getR11_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// row11
						// Column12
						Cell cell12 = row.createCell(12);
						if (record.getR11_19_24M_FD() != null) {
							cell12.setCellValue(record.getR11_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// row11
						// Column13
						Cell cell13 = row.createCell(13);
						if (record.getR11_OVER24_FD() != null) {
							cell13.setCellValue(record.getR11_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						row = sheet.getRow(10);

						// Row 12
						// Column2
						cell2 = row.createCell(3);
						if (record.getR12_CURRENT() != null) {
							cell2.setCellValue(record.getR12_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR12_CALL() != null) {
							cell3.setCellValue(record.getR12_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR12_SAVINGS() != null) {
							cell4.setCellValue(record.getR12_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR12_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR12_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR12_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR12_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR12_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR12_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(9);

						BigDecimal total1 = (record.getR12_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR12_1_2M_FD())
								.add(record.getR12_4_6M_FD() == null ? BigDecimal.ZERO : record.getR12_4_6M_FD());

						if (total1.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total1.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR12_7_12M_FD() != null) {
							cell10.setCellValue(record.getR12_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR12_13_18M_FD() != null) {
							cell11.setCellValue(record.getR12_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR12_19_24M_FD() != null) {
							cell12.setCellValue(record.getR12_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR12_OVER24_FD() != null) {
							cell13.setCellValue(record.getR12_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R13
						// ==========================

						row = sheet.getRow(11); // Adjust row index if needed

						// Column2
						cell2 = row.createCell(3);
						if (record.getR13_CURRENT() != null) {
							cell2.setCellValue(record.getR13_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR13_CALL() != null) {
							cell3.setCellValue(record.getR13_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR13_SAVINGS() != null) {
							cell4.setCellValue(record.getR13_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR13_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR13_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR13_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR13_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR13_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR13_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total7 = (record.getR13_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR13_1_2M_FD())
								.add(record.getR13_4_6M_FD() == null ? BigDecimal.ZERO : record.getR13_4_6M_FD());

						if (total7.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total7.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR13_7_12M_FD() != null) {
							cell10.setCellValue(record.getR13_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR13_13_18M_FD() != null) {
							cell11.setCellValue(record.getR13_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR13_19_24M_FD() != null) {
							cell12.setCellValue(record.getR13_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR13_OVER24_FD() != null) {
							cell13.setCellValue(record.getR13_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R14
						// ==========================

						row = sheet.getRow(12); // Adjust row index if needed

						// Column2
						cell2 = row.createCell(3);
						if (record.getR14_CURRENT() != null) {
							cell2.setCellValue(record.getR14_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR14_CALL() != null) {
							cell3.setCellValue(record.getR14_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR14_SAVINGS() != null) {
							cell4.setCellValue(record.getR14_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR14_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR14_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR14_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR14_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR14_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR14_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total8 = (record.getR14_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR14_1_2M_FD())
								.add(record.getR14_4_6M_FD() == null ? BigDecimal.ZERO : record.getR14_4_6M_FD());

						if (total8.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total8.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR14_7_12M_FD() != null) {
							cell10.setCellValue(record.getR14_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR14_13_18M_FD() != null) {
							cell11.setCellValue(record.getR14_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR14_19_24M_FD() != null) {
							cell12.setCellValue(record.getR14_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR14_OVER24_FD() != null) {
							cell13.setCellValue(record.getR14_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R15
						// ==========================

						row = sheet.getRow(19);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR15_CURRENT() != null) {
							cell2.setCellValue(record.getR15_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR15_CALL() != null) {
							cell3.setCellValue(record.getR15_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR15_SAVINGS() != null) {
							cell4.setCellValue(record.getR15_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR15_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR15_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR15_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR15_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR15_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR15_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total3 = (record.getR15_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR15_1_2M_FD())
								.add(record.getR15_4_6M_FD() == null ? BigDecimal.ZERO : record.getR15_4_6M_FD());

						if (total3.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total3.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR15_7_12M_FD() != null) {
							cell10.setCellValue(record.getR15_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR15_13_18M_FD() != null) {
							cell11.setCellValue(record.getR15_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR15_19_24M_FD() != null) {
							cell12.setCellValue(record.getR15_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR15_OVER24_FD() != null) {
							cell13.setCellValue(record.getR15_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R16
						// ==========================

						row = sheet.getRow(13);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR16_CURRENT() != null) {
							cell2.setCellValue(record.getR16_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR16_CALL() != null) {
							cell3.setCellValue(record.getR16_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR16_SAVINGS() != null) {
							cell4.setCellValue(record.getR16_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR16_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR16_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR16_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR16_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR16_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR16_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total11 = (record.getR16_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR16_1_2M_FD())
								.add(record.getR16_4_6M_FD() == null ? BigDecimal.ZERO : record.getR16_4_6M_FD());

						if (total11.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total11.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR16_7_12M_FD() != null) {
							cell10.setCellValue(record.getR16_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR16_13_18M_FD() != null) {
							cell11.setCellValue(record.getR16_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR16_19_24M_FD() != null) {
							cell12.setCellValue(record.getR16_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR16_OVER24_FD() != null) {
							cell13.setCellValue(record.getR16_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R17
						// ==========================

						row = sheet.getRow(14);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR17_CURRENT() != null) {
							cell2.setCellValue(record.getR17_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR17_CALL() != null) {
							cell3.setCellValue(record.getR17_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR17_SAVINGS() != null) {
							cell4.setCellValue(record.getR17_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR17_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR17_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR17_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR17_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR17_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR17_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total81 = (record.getR17_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR17_1_2M_FD())
								.add(record.getR17_4_6M_FD() == null ? BigDecimal.ZERO : record.getR17_4_6M_FD());

						if (total81.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total81.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR17_7_12M_FD() != null) {
							cell10.setCellValue(record.getR17_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR17_13_18M_FD() != null) {
							cell11.setCellValue(record.getR17_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR17_19_24M_FD() != null) {
							cell12.setCellValue(record.getR17_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR17_OVER24_FD() != null) {
							cell13.setCellValue(record.getR17_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R18
						// ==========================

						row = sheet.getRow(15);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR18_CURRENT() != null) {
							cell2.setCellValue(record.getR18_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR18_CALL() != null) {
							cell3.setCellValue(record.getR18_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR18_SAVINGS() != null) {
							cell4.setCellValue(record.getR18_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR18_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR18_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR18_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR18_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR18_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR18_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total4 = (record.getR18_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR18_1_2M_FD())
								.add(record.getR18_4_6M_FD() == null ? BigDecimal.ZERO : record.getR18_4_6M_FD());

						if (total4.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total4.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR18_7_12M_FD() != null) {
							cell10.setCellValue(record.getR18_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR18_13_18M_FD() != null) {
							cell11.setCellValue(record.getR18_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR18_19_24M_FD() != null) {
							cell12.setCellValue(record.getR18_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR18_OVER24_FD() != null) {
							cell13.setCellValue(record.getR18_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R19
						// ==========================

						row = sheet.getRow(16);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR19_CURRENT() != null) {
							cell2.setCellValue(record.getR19_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR19_CALL() != null) {
							cell3.setCellValue(record.getR19_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR19_SAVINGS() != null) {
							cell4.setCellValue(record.getR19_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR19_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR19_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR19_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR19_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR19_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR19_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total5 = (record.getR19_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR19_1_2M_FD())
								.add(record.getR19_4_6M_FD() == null ? BigDecimal.ZERO : record.getR19_4_6M_FD());

						if (total5.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total5.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR19_7_12M_FD() != null) {
							cell10.setCellValue(record.getR19_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR19_13_18M_FD() != null) {
							cell11.setCellValue(record.getR19_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR19_19_24M_FD() != null) {
							cell12.setCellValue(record.getR19_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR19_OVER24_FD() != null) {
							cell13.setCellValue(record.getR19_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R20
						// ==========================

						row = sheet.getRow(17);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR20_CURRENT() != null) {
							cell2.setCellValue(record.getR20_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR20_CALL() != null) {
							cell3.setCellValue(record.getR20_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR20_SAVINGS() != null) {
							cell4.setCellValue(record.getR20_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR20_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR20_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR20_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR20_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR20_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR20_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal totalR20 = (record.getR20_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR20_1_2M_FD())
								.add(record.getR20_4_6M_FD() == null ? BigDecimal.ZERO : record.getR20_4_6M_FD());

						if (totalR20.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR20.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR20_7_12M_FD() != null) {
							cell10.setCellValue(record.getR20_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR20_13_18M_FD() != null) {
							cell11.setCellValue(record.getR20_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR20_19_24M_FD() != null) {
							cell12.setCellValue(record.getR20_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR20_OVER24_FD() != null) {
							cell13.setCellValue(record.getR20_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R21
						// ==========================

						row = sheet.getRow(18);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR21_CURRENT() != null) {
							cell2.setCellValue(record.getR21_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR21_CALL() != null) {
							cell3.setCellValue(record.getR21_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR21_SAVINGS() != null) {
							cell4.setCellValue(record.getR21_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR21_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR21_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR21_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR21_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR21_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR21_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal totalR21 = (record.getR21_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR21_1_2M_FD())
								.add(record.getR21_4_6M_FD() == null ? BigDecimal.ZERO : record.getR21_4_6M_FD());

						if (totalR21.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR21.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR21_7_12M_FD() != null) {
							cell10.setCellValue(record.getR21_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR21_13_18M_FD() != null) {
							cell11.setCellValue(record.getR21_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR21_19_24M_FD() != null) {
							cell12.setCellValue(record.getR21_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR21_OVER24_FD() != null) {
							cell13.setCellValue(record.getR21_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R22
						// ==========================

						row = sheet.getRow(20);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR22_CURRENT() != null) {
							cell2.setCellValue(record.getR22_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR22_CALL() != null) {
							cell3.setCellValue(record.getR22_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR22_SAVINGS() != null) {
							cell4.setCellValue(record.getR22_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR22_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR22_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR22_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR22_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR22_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR22_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR22 = (record.getR22_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR22_1_2M_FD())
								.add(record.getR22_4_6M_FD() == null ? BigDecimal.ZERO : record.getR22_4_6M_FD());

						if (totalR22.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR22.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR22_7_12M_FD() != null) {
							cell10.setCellValue(record.getR22_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR22_13_18M_FD() != null) {
							cell11.setCellValue(record.getR22_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR22_19_24M_FD() != null) {
							cell12.setCellValue(record.getR22_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR22_OVER24_FD() != null) {
							cell13.setCellValue(record.getR22_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						row = sheet.getRow(21);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR27_CURRENT() != null) {
							cell2.setCellValue(record.getR27_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR27_CALL() != null) {
							cell3.setCellValue(record.getR27_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR27_SAVINGS() != null) {
							cell4.setCellValue(record.getR27_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR27_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR27_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR27_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR27_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR27_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR27_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR27 = (record.getR27_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR27_1_2M_FD())
								.add(record.getR27_4_6M_FD() == null ? BigDecimal.ZERO : record.getR27_4_6M_FD());

						if (totalR27.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR27.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR27_7_12M_FD() != null) {
							cell10.setCellValue(record.getR27_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR27_13_18M_FD() != null) {
							cell11.setCellValue(record.getR27_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR27_19_24M_FD() != null) {
							cell12.setCellValue(record.getR27_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR27_OVER24_FD() != null) {
							cell13.setCellValue(record.getR27_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R23
						// ==========================

						row = sheet.getRow(22);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR23_CURRENT() != null) {
							cell2.setCellValue(record.getR23_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR23_CALL() != null) {
							cell3.setCellValue(record.getR23_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR23_SAVINGS() != null) {
							cell4.setCellValue(record.getR23_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR23_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR23_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR23_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR23_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR23_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR23_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR23 = (record.getR23_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR23_1_2M_FD())
								.add(record.getR23_4_6M_FD() == null ? BigDecimal.ZERO : record.getR23_4_6M_FD());

						if (totalR23.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR23.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR23_7_12M_FD() != null) {
							cell10.setCellValue(record.getR23_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR23_13_18M_FD() != null) {
							cell11.setCellValue(record.getR23_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR23_19_24M_FD() != null) {
							cell12.setCellValue(record.getR23_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR23_OVER24_FD() != null) {
							cell13.setCellValue(record.getR23_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R24
						// ==========================

						row = sheet.getRow(23);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR24_CURRENT() != null) {
							cell2.setCellValue(record.getR24_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR24_CALL() != null) {
							cell3.setCellValue(record.getR24_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR24_SAVINGS() != null) {
							cell4.setCellValue(record.getR24_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR24_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR24_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR24_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR24_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR24_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR24_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR24 = (record.getR24_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR24_1_2M_FD())
								.add(record.getR24_4_6M_FD() == null ? BigDecimal.ZERO : record.getR24_4_6M_FD());

						if (totalR24.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR24.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR24_7_12M_FD() != null) {
							cell10.setCellValue(record.getR24_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR24_13_18M_FD() != null) {
							cell11.setCellValue(record.getR24_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR24_19_24M_FD() != null) {
							cell12.setCellValue(record.getR24_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR24_OVER24_FD() != null) {
							cell13.setCellValue(record.getR24_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R26
						// ==========================

						row = sheet.getRow(25);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR26_CURRENT() != null) {
							cell2.setCellValue(record.getR26_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR26_CALL() != null) {
							cell3.setCellValue(record.getR26_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR26_SAVINGS() != null) {
							cell4.setCellValue(record.getR26_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR26_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR26_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR26_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR26_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR26_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR26_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR26 = (record.getR26_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR26_1_2M_FD())
								.add(record.getR26_4_6M_FD() == null ? BigDecimal.ZERO : record.getR26_4_6M_FD());

						if (totalR26.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR26.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR26_7_12M_FD() != null) {
							cell10.setCellValue(record.getR26_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR26_13_18M_FD() != null) {
							cell11.setCellValue(record.getR26_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR26_19_24M_FD() != null) {
							cell12.setCellValue(record.getR26_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR26_OVER24_FD() != null) {
							cell13.setCellValue(record.getR26_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R32
						// ==========================

						row = sheet.getRow(33);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR32_CURRENT() != null) {
							cell2.setCellValue(record.getR32_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR32_CALL() != null) {
							cell3.setCellValue(record.getR32_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR32_SAVINGS() != null) {
							cell4.setCellValue(record.getR32_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR32_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR32_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR32_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR32_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR32_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR32_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR32 = (record.getR32_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR32_1_2M_FD())
								.add(record.getR32_4_6M_FD() == null ? BigDecimal.ZERO : record.getR32_4_6M_FD());

						if (totalR32.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR32.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR32_7_12M_FD() != null) {
							cell10.setCellValue(record.getR32_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR32_13_18M_FD() != null) {
							cell11.setCellValue(record.getR32_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR32_19_24M_FD() != null) {
							cell12.setCellValue(record.getR32_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR32_OVER24_FD() != null) {
							cell13.setCellValue(record.getR32_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R33
						// ==========================

						row = sheet.getRow(34);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR33_CURRENT() != null) {
							cell2.setCellValue(record.getR33_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR33_CALL() != null) {
							cell3.setCellValue(record.getR33_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR33_SAVINGS() != null) {
							cell4.setCellValue(record.getR33_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR33_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR33_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR33_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR33_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR33_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR33_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR33 = (record.getR33_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR33_1_2M_FD())
								.add(record.getR33_4_6M_FD() == null ? BigDecimal.ZERO : record.getR33_4_6M_FD());

						if (totalR33.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR33.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR33_7_12M_FD() != null) {
							cell10.setCellValue(record.getR33_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR33_13_18M_FD() != null) {
							cell11.setCellValue(record.getR33_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR33_19_24M_FD() != null) {
							cell12.setCellValue(record.getR33_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR33_OVER24_FD() != null) {
							cell13.setCellValue(record.getR33_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R34
						// ==========================

						row = sheet.getRow(35);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR34_CURRENT() != null) {
							cell2.setCellValue(record.getR34_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR34_CALL() != null) {
							cell3.setCellValue(record.getR34_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR34_SAVINGS() != null) {
							cell4.setCellValue(record.getR34_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR34_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR34_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR34_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR34_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR34_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR34_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR34 = (record.getR34_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR34_1_2M_FD())
								.add(record.getR34_4_6M_FD() == null ? BigDecimal.ZERO : record.getR34_4_6M_FD());

						if (totalR34.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR34.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR34_7_12M_FD() != null) {
							cell10.setCellValue(record.getR34_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR34_13_18M_FD() != null) {
							cell11.setCellValue(record.getR34_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR34_19_24M_FD() != null) {
							cell12.setCellValue(record.getR34_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR34_OVER24_FD() != null) {
							cell13.setCellValue(record.getR34_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R35
						// ==========================

						row = sheet.getRow(36);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR35_CURRENT() != null) {
							cell2.setCellValue(record.getR35_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR35_CALL() != null) {
							cell3.setCellValue(record.getR35_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR35_SAVINGS() != null) {
							cell4.setCellValue(record.getR35_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR35_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR35_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR35_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR35_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR35_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR35_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR35 = (record.getR35_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR35_1_2M_FD())
								.add(record.getR35_4_6M_FD() == null ? BigDecimal.ZERO : record.getR35_4_6M_FD());

						if (totalR35.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR35.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR35_7_12M_FD() != null) {
							cell10.setCellValue(record.getR35_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR35_13_18M_FD() != null) {
							cell11.setCellValue(record.getR35_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR35_19_24M_FD() != null) {
							cell12.setCellValue(record.getR35_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR35_OVER24_FD() != null) {
							cell13.setCellValue(record.getR35_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R36
						// ==========================

						row = sheet.getRow(43);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR36_CURRENT() != null) {
							cell2.setCellValue(record.getR36_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR36_CALL() != null) {
							cell3.setCellValue(record.getR36_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR36_SAVINGS() != null) {
							cell4.setCellValue(record.getR36_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR36_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR36_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR36_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR36_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR36_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR36_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR36 = (record.getR36_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR36_1_2M_FD())
								.add(record.getR36_4_6M_FD() == null ? BigDecimal.ZERO : record.getR36_4_6M_FD());

						if (totalR36.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR36.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR36_7_12M_FD() != null) {
							cell10.setCellValue(record.getR36_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR36_13_18M_FD() != null) {
							cell11.setCellValue(record.getR36_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR36_19_24M_FD() != null) {
							cell12.setCellValue(record.getR36_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR36_OVER24_FD() != null) {
							cell13.setCellValue(record.getR36_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R37
						// ==========================

						row = sheet.getRow(37);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR37_CURRENT() != null) {
							cell2.setCellValue(record.getR37_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR37_CALL() != null) {
							cell3.setCellValue(record.getR37_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR37_SAVINGS() != null) {
							cell4.setCellValue(record.getR37_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR37_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR37_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR37_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR37_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR37_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR37_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR37 = (record.getR37_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR37_1_2M_FD())
								.add(record.getR37_4_6M_FD() == null ? BigDecimal.ZERO : record.getR37_4_6M_FD());

						if (totalR37.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR37.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR37_7_12M_FD() != null) {
							cell10.setCellValue(record.getR37_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR37_13_18M_FD() != null) {
							cell11.setCellValue(record.getR37_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR37_19_24M_FD() != null) {
							cell12.setCellValue(record.getR37_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR37_OVER24_FD() != null) {
							cell13.setCellValue(record.getR37_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R38
						// ==========================

						row = sheet.getRow(38);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR38_CURRENT() != null) {
							cell2.setCellValue(record.getR38_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR38_CALL() != null) {
							cell3.setCellValue(record.getR38_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR38_SAVINGS() != null) {
							cell4.setCellValue(record.getR38_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR38_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR38_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR38_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR38_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR38_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR38_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR38 = (record.getR38_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR38_1_2M_FD())
								.add(record.getR38_4_6M_FD() == null ? BigDecimal.ZERO : record.getR38_4_6M_FD());

						if (totalR38.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR38.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR38_7_12M_FD() != null) {
							cell10.setCellValue(record.getR38_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR38_13_18M_FD() != null) {
							cell11.setCellValue(record.getR38_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR38_19_24M_FD() != null) {
							cell12.setCellValue(record.getR38_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR38_OVER24_FD() != null) {
							cell13.setCellValue(record.getR38_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R39
						// ==========================

						row = sheet.getRow(39);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR39_CURRENT() != null) {
							cell2.setCellValue(record.getR39_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR39_CALL() != null) {
							cell3.setCellValue(record.getR39_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR39_SAVINGS() != null) {
							cell4.setCellValue(record.getR39_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR39_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR39_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR39_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR39_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR39_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR39_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR39 = (record.getR39_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR39_1_2M_FD())
								.add(record.getR39_4_6M_FD() == null ? BigDecimal.ZERO : record.getR39_4_6M_FD());

						if (totalR39.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR39.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR39_7_12M_FD() != null) {
							cell10.setCellValue(record.getR39_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR39_13_18M_FD() != null) {
							cell11.setCellValue(record.getR39_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR39_19_24M_FD() != null) {
							cell12.setCellValue(record.getR39_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR39_OVER24_FD() != null) {
							cell13.setCellValue(record.getR39_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R40
						// ==========================

						row = sheet.getRow(40);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR40_CURRENT() != null) {
							cell2.setCellValue(record.getR40_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR40_CALL() != null) {
							cell3.setCellValue(record.getR40_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR40_SAVINGS() != null) {
							cell4.setCellValue(record.getR40_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR40_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR40_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR40_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR40_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR40_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR40_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR40 = (record.getR40_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR40_1_2M_FD())
								.add(record.getR40_4_6M_FD() == null ? BigDecimal.ZERO : record.getR40_4_6M_FD());

						if (totalR40.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR40.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR40_7_12M_FD() != null) {
							cell10.setCellValue(record.getR40_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR40_13_18M_FD() != null) {
							cell11.setCellValue(record.getR40_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR40_19_24M_FD() != null) {
							cell12.setCellValue(record.getR40_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR40_OVER24_FD() != null) {
							cell13.setCellValue(record.getR40_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R41
						// ==========================

						row = sheet.getRow(41);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR41_CURRENT() != null) {
							cell2.setCellValue(record.getR41_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR41_CALL() != null) {
							cell3.setCellValue(record.getR41_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR41_SAVINGS() != null) {
							cell4.setCellValue(record.getR41_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR41_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR41_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR41_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR41_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR41_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR41_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR41 = (record.getR41_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR41_1_2M_FD())
								.add(record.getR41_4_6M_FD() == null ? BigDecimal.ZERO : record.getR41_4_6M_FD());

						if (totalR41.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR41.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR41_7_12M_FD() != null) {
							cell10.setCellValue(record.getR41_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR41_13_18M_FD() != null) {
							cell11.setCellValue(record.getR41_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR41_19_24M_FD() != null) {
							cell12.setCellValue(record.getR41_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR41_OVER24_FD() != null) {
							cell13.setCellValue(record.getR41_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R42
						// ==========================

						row = sheet.getRow(42);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR42_CURRENT() != null) {
							cell2.setCellValue(record.getR42_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR42_CALL() != null) {
							cell3.setCellValue(record.getR42_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR42_SAVINGS() != null) {
							cell4.setCellValue(record.getR42_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR42_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR42_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR42_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR42_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR42_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR42_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR42 = (record.getR42_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR42_1_2M_FD())
								.add(record.getR42_4_6M_FD() == null ? BigDecimal.ZERO : record.getR42_4_6M_FD());

						if (totalR42.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR42.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR42_7_12M_FD() != null) {
							cell10.setCellValue(record.getR42_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR42_13_18M_FD() != null) {
							cell11.setCellValue(record.getR42_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR42_19_24M_FD() != null) {
							cell12.setCellValue(record.getR42_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR42_OVER24_FD() != null) {
							cell13.setCellValue(record.getR42_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R43
						// ==========================

						row = sheet.getRow(44);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR43_CURRENT() != null) {
							cell2.setCellValue(record.getR43_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR43_CALL() != null) {
							cell3.setCellValue(record.getR43_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR43_SAVINGS() != null) {
							cell4.setCellValue(record.getR43_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR43_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR43_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR43_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR43_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR43_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR43_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR43 = (record.getR43_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR43_1_2M_FD())
								.add(record.getR43_4_6M_FD() == null ? BigDecimal.ZERO : record.getR43_4_6M_FD());

						if (totalR43.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR43.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR43_7_12M_FD() != null) {
							cell10.setCellValue(record.getR43_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR43_13_18M_FD() != null) {
							cell11.setCellValue(record.getR43_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR43_19_24M_FD() != null) {
							cell12.setCellValue(record.getR43_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR43_OVER24_FD() != null) {
							cell13.setCellValue(record.getR43_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						
						row = sheet.getRow(45);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR48_CURRENT() != null) {
							cell2.setCellValue(record.getR48_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR48_CALL() != null) {
							cell3.setCellValue(record.getR48_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR48_SAVINGS() != null) {
							cell4.setCellValue(record.getR48_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR48_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR48_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR48_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR48_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR48_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR48_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR48 = (record.getR48_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR48_1_2M_FD())
								.add(record.getR48_4_6M_FD() == null ? BigDecimal.ZERO : record.getR48_4_6M_FD());

						if (totalR48.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR48.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR48_7_12M_FD() != null) {
							cell10.setCellValue(record.getR48_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR48_13_18M_FD() != null) {
							cell11.setCellValue(record.getR48_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR48_19_24M_FD() != null) {
							cell12.setCellValue(record.getR48_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR48_OVER24_FD() != null) {
							cell13.setCellValue(record.getR48_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R44
						// ==========================

						row = sheet.getRow(46);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR44_CURRENT() != null) {
							cell2.setCellValue(record.getR44_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR44_CALL() != null) {
							cell3.setCellValue(record.getR44_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR44_SAVINGS() != null) {
							cell4.setCellValue(record.getR44_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR44_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR44_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR44_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR44_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR44_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR44_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR44 = (record.getR44_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR44_1_2M_FD())
								.add(record.getR44_4_6M_FD() == null ? BigDecimal.ZERO : record.getR44_4_6M_FD());

						if (totalR44.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR44.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR44_7_12M_FD() != null) {
							cell10.setCellValue(record.getR44_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR44_13_18M_FD() != null) {
							cell11.setCellValue(record.getR44_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR44_19_24M_FD() != null) {
							cell12.setCellValue(record.getR44_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR44_OVER24_FD() != null) {
							cell13.setCellValue(record.getR44_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R45
						// ==========================

						row = sheet.getRow(47);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR45_CURRENT() != null) {
							cell2.setCellValue(record.getR45_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR45_CALL() != null) {
							cell3.setCellValue(record.getR45_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR45_SAVINGS() != null) {
							cell4.setCellValue(record.getR45_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR45_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR45_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR45_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR45_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR45_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR45_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR45 = (record.getR45_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR45_1_2M_FD())
								.add(record.getR45_4_6M_FD() == null ? BigDecimal.ZERO : record.getR45_4_6M_FD());

						if (totalR45.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR45.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR45_7_12M_FD() != null) {
							cell10.setCellValue(record.getR45_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR45_13_18M_FD() != null) {
							cell11.setCellValue(record.getR45_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR45_19_24M_FD() != null) {
							cell12.setCellValue(record.getR45_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR45_OVER24_FD() != null) {
							cell13.setCellValue(record.getR45_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R46
						// ==========================

						row = sheet.getRow(48);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR46_CURRENT() != null) {
							cell2.setCellValue(record.getR46_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR46_CALL() != null) {
							cell3.setCellValue(record.getR46_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR46_SAVINGS() != null) {
							cell4.setCellValue(record.getR46_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR46_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR46_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR46_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR46_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR46_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR46_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR46 = (record.getR46_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR46_1_2M_FD())
								.add(record.getR46_4_6M_FD() == null ? BigDecimal.ZERO : record.getR46_4_6M_FD());

						if (totalR46.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR46.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR46_7_12M_FD() != null) {
							cell10.setCellValue(record.getR46_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR46_13_18M_FD() != null) {
							cell11.setCellValue(record.getR46_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR46_19_24M_FD() != null) {
							cell12.setCellValue(record.getR46_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR46_OVER24_FD() != null) {
							cell13.setCellValue(record.getR46_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
//						// ==========================
//						// R47
//						// ==========================
//
//						row = sheet.getRow(48);
//
//						// Column2
//						cell2 = row.createCell(3);
//						if (record.getR47_CURRENT() != null) {
//							cell2.setCellValue(record.getR47_CURRENT().doubleValue());
//							cell2.setCellStyle(numberStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}
//
//						// Column3
//						cell3 = row.createCell(4);
//						if (record.getR47_CALL() != null) {
//							cell3.setCellValue(record.getR47_CALL().doubleValue());
//							cell3.setCellStyle(numberStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}
//
//						// Column4
//						cell4 = row.createCell(5);
//						if (record.getR47_SAVINGS() != null) {
//							cell4.setCellValue(record.getR47_SAVINGS().doubleValue());
//							cell4.setCellStyle(numberStyle);
//						} else {
//							cell4.setCellValue("");
//							cell4.setCellStyle(textStyle);
//						}
//
//						// Column5
//						cell5 = row.createCell(6);
//						if (record.getR47_0_31D_NOTICE() != null) {
//							cell5.setCellValue(record.getR47_0_31D_NOTICE().doubleValue());
//							cell5.setCellStyle(numberStyle);
//						} else {
//							cell5.setCellValue("");
//							cell5.setCellStyle(textStyle);
//						}
//
//						// Column6
//						cell6 = row.createCell(7);
//						if (record.getR47_32_88D_NOTICE() != null) {
//							cell6.setCellValue(record.getR47_32_88D_NOTICE().doubleValue());
//							cell6.setCellStyle(numberStyle);
//						} else {
//							cell6.setCellValue("");
//							cell6.setCellStyle(textStyle);
//						}
//
//						// Column7
//						cell7 = row.createCell(8);
//						if (record.getR47_91D_DEPOSIT() != null) {
//							cell7.setCellValue(record.getR47_91D_DEPOSIT().doubleValue());
//							cell7.setCellStyle(numberStyle);
//						} else {
//							cell7.setCellValue("");
//							cell7.setCellStyle(textStyle);
//						}
//
//						// Column8 (1-2M + 4-6M)
//						cell8 = row.createCell(9);
//						BigDecimal totalR47 = (record.getR47_1_2M_FD() == null ? BigDecimal.ZERO
//								: record.getR47_1_2M_FD())
//								.add(record.getR47_4_6M_FD() == null ? BigDecimal.ZERO : record.getR47_4_6M_FD());
//
//						if (totalR47.compareTo(BigDecimal.ZERO) != 0) {
//							cell8.setCellValue(totalR47.doubleValue());
//							cell8.setCellStyle(numberStyle);
//						} else {
//							cell8.setCellValue("");
//							cell8.setCellStyle(textStyle);
//						}
//
//						// Column10
//						cell10 = row.createCell(10);
//						if (record.getR47_7_12M_FD() != null) {
//							cell10.setCellValue(record.getR47_7_12M_FD().doubleValue());
//							cell10.setCellStyle(numberStyle);
//						} else {
//							cell10.setCellValue("");
//							cell10.setCellStyle(textStyle);
//						}
//
//						// Column11
//						cell11 = row.createCell(11);
//						if (record.getR47_13_18M_FD() != null) {
//							cell11.setCellValue(record.getR47_13_18M_FD().doubleValue());
//							cell11.setCellStyle(numberStyle);
//						} else {
//							cell11.setCellValue("");
//							cell11.setCellStyle(textStyle);
//						}
//
//						// Column12
//						cell12 = row.createCell(12);
//						if (record.getR47_19_24M_FD() != null) {
//							cell12.setCellValue(record.getR47_19_24M_FD().doubleValue());
//							cell12.setCellStyle(numberStyle);
//						} else {
//							cell12.setCellValue("");
//							cell12.setCellStyle(textStyle);
//						}
//
//						// Column13
//						cell13 = row.createCell(13);
//						if (record.getR47_OVER24_FD() != null) {
//							cell13.setCellValue(record.getR47_OVER24_FD().doubleValue());
//							cell13.setCellStyle(numberStyle);
//						} else {
//							cell13.setCellValue("");
//							cell13.setCellStyle(textStyle);
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

	// Resub Format excel
	public byte[] BRRS_Q_SMME_DEPResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_SMME_DEPResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<Q_SMME_DEP_Resub_Summary_Entity> dataList = brrs_Q_SMME_DEP_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_SMME_DEP report. Returning empty result.");
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

					Q_SMME_DEP_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL

					// ROW11
					// COLUMN2
					Cell cell2 = row.createCell(1);
					if (record.getR11_CURRENT() != null) {
						cell2.setCellValue(record.getR11_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					// Column3
					Cell cell3 = row.createCell(2);
					if (record.getR11_CALL() != null) {
						cell3.setCellValue(record.getR11_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row11
					// Column4
					Cell cell4 = row.createCell(3);
					if (record.getR11_SAVINGS() != null) {
						cell4.setCellValue(record.getR11_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column5
					Cell cell5 = row.createCell(4);
					if (record.getR11_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR11_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					// Column6
					Cell cell6 = row.createCell(5);
					if (record.getR11_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR11_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row11
					// Column7
					Cell cell7 = row.createCell(6);
					if (record.getR11_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR11_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row11
					// Column8
					Cell cell8 = row.createCell(7);
					if (record.getR11_1_2M_FD() != null) {
						cell8.setCellValue(record.getR11_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// row11
					// Column9
					Cell cell9 = row.createCell(8);
					if (record.getR11_4_6M_FD() != null) {
						cell9.setCellValue(record.getR11_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// row11
					// Column10
					Cell cell10 = row.createCell(9);
					if (record.getR11_7_12M_FD() != null) {
						cell10.setCellValue(record.getR11_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// row11
					// Column11
					Cell cell11 = row.createCell(10);
					if (record.getR11_13_18M_FD() != null) {
						cell11.setCellValue(record.getR11_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row11
					// Column12
					Cell cell12 = row.createCell(11);
					if (record.getR11_19_24M_FD() != null) {
						cell12.setCellValue(record.getR11_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// row11
					// Column13
					Cell cell13 = row.createCell(12);
					if (record.getR11_OVER24_FD() != null) {
						cell13.setCellValue(record.getR11_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// row11
					// Column14
//					Cell cell14 = row.createCell(13);
//					if (record.getR11_TOTAL() != null) {
//						cell14.setCellValue(record.getR11_TOTAL().doubleValue());
//						cell14.setCellStyle(numberStyle);
//					} else {
//						cell14.setCellValue("");
//						cell14.setCellStyle(textStyle);
//					}

					// row11
					// Column15
					Cell cell15 = row.createCell(14);
					if (record.getR11_NOACC() != null) {
						cell15.setCellValue(record.getR11_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);

					// Row 12
					// Column2
					cell2 = row.createCell(1);
					if (record.getR12_CURRENT() != null) {
						cell2.setCellValue(record.getR12_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR12_CALL() != null) {
						cell3.setCellValue(record.getR12_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR12_SAVINGS() != null) {
						cell4.setCellValue(record.getR12_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR12_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR12_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR12_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR12_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR12_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR12_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR12_1_2M_FD() != null) {
						cell8.setCellValue(record.getR12_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR12_4_6M_FD() != null) {
						cell9.setCellValue(record.getR12_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR12_7_12M_FD() != null) {
						cell10.setCellValue(record.getR12_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR12_13_18M_FD() != null) {
						cell11.setCellValue(record.getR12_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR12_19_24M_FD() != null) {
						cell12.setCellValue(record.getR12_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR12_OVER24_FD() != null) {
						cell13.setCellValue(record.getR12_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR12_TOTAL() != null) {
//					    cell14.setCellValue(record.getR12_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR12_NOACC() != null) {
						cell15.setCellValue(record.getR12_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					// Row 13
					// Column2
					cell2 = row.createCell(1);
					if (record.getR13_CURRENT() != null) {
						cell2.setCellValue(record.getR13_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR13_CALL() != null) {
						cell3.setCellValue(record.getR13_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR13_SAVINGS() != null) {
						cell4.setCellValue(record.getR13_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR13_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR13_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR13_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR13_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR13_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR13_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR13_1_2M_FD() != null) {
						cell8.setCellValue(record.getR13_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR13_4_6M_FD() != null) {
						cell9.setCellValue(record.getR13_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR13_7_12M_FD() != null) {
						cell10.setCellValue(record.getR13_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR13_13_18M_FD() != null) {
						cell11.setCellValue(record.getR13_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR13_19_24M_FD() != null) {
						cell12.setCellValue(record.getR13_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR13_OVER24_FD() != null) {
						cell13.setCellValue(record.getR13_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR13_TOTAL() != null) {
//					    cell14.setCellValue(record.getR13_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR13_NOACC() != null) {
						cell15.setCellValue(record.getR13_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					// Row 14
					// Column2
					cell2 = row.createCell(1);
					if (record.getR14_CURRENT() != null) {
						cell2.setCellValue(record.getR14_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR14_CALL() != null) {
						cell3.setCellValue(record.getR14_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR14_SAVINGS() != null) {
						cell4.setCellValue(record.getR14_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR14_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR14_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR14_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR14_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR14_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR14_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR14_1_2M_FD() != null) {
						cell8.setCellValue(record.getR14_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR14_4_6M_FD() != null) {
						cell9.setCellValue(record.getR14_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR14_7_12M_FD() != null) {
						cell10.setCellValue(record.getR14_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR14_13_18M_FD() != null) {
						cell11.setCellValue(record.getR14_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR14_19_24M_FD() != null) {
						cell12.setCellValue(record.getR14_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR14_OVER24_FD() != null) {
						cell13.setCellValue(record.getR14_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR14_NOACC() != null) {
						cell15.setCellValue(record.getR14_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 14
					row = sheet.getRow(14);

					// Row 15
					// Column2
					cell2 = row.createCell(1);
					if (record.getR15_CURRENT() != null) {
						cell2.setCellValue(record.getR15_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR15_CALL() != null) {
						cell3.setCellValue(record.getR15_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR15_SAVINGS() != null) {
						cell4.setCellValue(record.getR15_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR15_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR15_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR15_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR15_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR15_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR15_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR15_1_2M_FD() != null) {
						cell8.setCellValue(record.getR15_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR15_4_6M_FD() != null) {
						cell9.setCellValue(record.getR15_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR15_7_12M_FD() != null) {
						cell10.setCellValue(record.getR15_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR15_13_18M_FD() != null) {
						cell11.setCellValue(record.getR15_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR15_19_24M_FD() != null) {
						cell12.setCellValue(record.getR15_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR15_OVER24_FD() != null) {
						cell13.setCellValue(record.getR15_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR15_NOACC() != null) {
						cell15.setCellValue(record.getR15_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 15
					row = sheet.getRow(15);

					// Row 16
					// Column2
					cell2 = row.createCell(1);
					if (record.getR16_CURRENT() != null) {
						cell2.setCellValue(record.getR16_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR16_CALL() != null) {
						cell3.setCellValue(record.getR16_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR16_SAVINGS() != null) {
						cell4.setCellValue(record.getR16_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR16_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR16_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR16_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR16_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR16_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR16_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR16_1_2M_FD() != null) {
						cell8.setCellValue(record.getR16_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR16_4_6M_FD() != null) {
						cell9.setCellValue(record.getR16_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR16_7_12M_FD() != null) {
						cell10.setCellValue(record.getR16_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR16_13_18M_FD() != null) {
						cell11.setCellValue(record.getR16_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR16_19_24M_FD() != null) {
						cell12.setCellValue(record.getR16_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR16_OVER24_FD() != null) {
						cell13.setCellValue(record.getR16_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR16_NOACC() != null) {
						cell15.setCellValue(record.getR16_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 16
					row = sheet.getRow(16);

					// Row 17
					// Column2
					cell2 = row.createCell(1);
					if (record.getR17_CURRENT() != null) {
						cell2.setCellValue(record.getR17_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR17_CALL() != null) {
						cell3.setCellValue(record.getR17_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR17_SAVINGS() != null) {
						cell4.setCellValue(record.getR17_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR17_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR17_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR17_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR17_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR17_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR17_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR17_1_2M_FD() != null) {
						cell8.setCellValue(record.getR17_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR17_4_6M_FD() != null) {
						cell9.setCellValue(record.getR17_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR17_7_12M_FD() != null) {
						cell10.setCellValue(record.getR17_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR17_13_18M_FD() != null) {
						cell11.setCellValue(record.getR17_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR17_19_24M_FD() != null) {
						cell12.setCellValue(record.getR17_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR17_OVER24_FD() != null) {
						cell13.setCellValue(record.getR17_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR17_NOACC() != null) {
						cell15.setCellValue(record.getR17_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 17
					row = sheet.getRow(17);

					// Row 18
					// Column2
					cell2 = row.createCell(1);
					if (record.getR18_CURRENT() != null) {
						cell2.setCellValue(record.getR18_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR18_CALL() != null) {
						cell3.setCellValue(record.getR18_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR18_SAVINGS() != null) {
						cell4.setCellValue(record.getR18_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR18_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR18_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR18_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR18_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR18_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR18_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR18_1_2M_FD() != null) {
						cell8.setCellValue(record.getR18_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR18_4_6M_FD() != null) {
						cell9.setCellValue(record.getR18_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR18_7_12M_FD() != null) {
						cell10.setCellValue(record.getR18_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR18_13_18M_FD() != null) {
						cell11.setCellValue(record.getR18_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR18_19_24M_FD() != null) {
						cell12.setCellValue(record.getR18_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR18_OVER24_FD() != null) {
						cell13.setCellValue(record.getR18_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR18_NOACC() != null) {
						cell15.setCellValue(record.getR18_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 18
					row = sheet.getRow(18);

					// Row 19
					// Column2
					cell2 = row.createCell(1);
					if (record.getR19_CURRENT() != null) {
						cell2.setCellValue(record.getR19_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR19_CALL() != null) {
						cell3.setCellValue(record.getR19_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR19_SAVINGS() != null) {
						cell4.setCellValue(record.getR19_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR19_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR19_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR19_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR19_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR19_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR19_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR19_1_2M_FD() != null) {
						cell8.setCellValue(record.getR19_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR19_4_6M_FD() != null) {
						cell9.setCellValue(record.getR19_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR19_7_12M_FD() != null) {
						cell10.setCellValue(record.getR19_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR19_13_18M_FD() != null) {
						cell11.setCellValue(record.getR19_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR19_19_24M_FD() != null) {
						cell12.setCellValue(record.getR19_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR19_OVER24_FD() != null) {
						cell13.setCellValue(record.getR19_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR19_NOACC() != null) {
						cell15.setCellValue(record.getR19_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 19
					row = sheet.getRow(19);

					// Row 20
					// Column2
					cell2 = row.createCell(1);
					if (record.getR20_CURRENT() != null) {
						cell2.setCellValue(record.getR20_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR20_CALL() != null) {
						cell3.setCellValue(record.getR20_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR20_SAVINGS() != null) {
						cell4.setCellValue(record.getR20_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR20_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR20_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR20_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR20_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR20_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR20_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR20_1_2M_FD() != null) {
						cell8.setCellValue(record.getR20_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR20_4_6M_FD() != null) {
						cell9.setCellValue(record.getR20_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR20_7_12M_FD() != null) {
						cell10.setCellValue(record.getR20_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR20_13_18M_FD() != null) {
						cell11.setCellValue(record.getR20_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR20_19_24M_FD() != null) {
						cell12.setCellValue(record.getR20_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR20_OVER24_FD() != null) {
						cell13.setCellValue(record.getR20_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR20_NOACC() != null) {
						cell15.setCellValue(record.getR20_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 20
					row = sheet.getRow(20);

					// Row 21
					// Column2
					cell2 = row.createCell(1);
					if (record.getR21_CURRENT() != null) {
						cell2.setCellValue(record.getR21_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR21_CALL() != null) {
						cell3.setCellValue(record.getR21_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR21_SAVINGS() != null) {
						cell4.setCellValue(record.getR21_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR21_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR21_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR21_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR21_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR21_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR21_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR21_1_2M_FD() != null) {
						cell8.setCellValue(record.getR21_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR21_4_6M_FD() != null) {
						cell9.setCellValue(record.getR21_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR21_7_12M_FD() != null) {
						cell10.setCellValue(record.getR21_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR21_13_18M_FD() != null) {
						cell11.setCellValue(record.getR21_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR21_19_24M_FD() != null) {
						cell12.setCellValue(record.getR21_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR21_OVER24_FD() != null) {
						cell13.setCellValue(record.getR21_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR21_NOACC() != null) {
						cell15.setCellValue(record.getR21_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 21
					row = sheet.getRow(21);

					// Row 22
					// Column2
					cell2 = row.createCell(1);
					if (record.getR22_CURRENT() != null) {
						cell2.setCellValue(record.getR22_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR22_CALL() != null) {
						cell3.setCellValue(record.getR22_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR22_SAVINGS() != null) {
						cell4.setCellValue(record.getR22_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR22_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR22_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR22_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR22_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR22_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR22_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR22_1_2M_FD() != null) {
						cell8.setCellValue(record.getR22_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR22_4_6M_FD() != null) {
						cell9.setCellValue(record.getR22_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR22_7_12M_FD() != null) {
						cell10.setCellValue(record.getR22_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR22_13_18M_FD() != null) {
						cell11.setCellValue(record.getR22_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR22_19_24M_FD() != null) {
						cell12.setCellValue(record.getR22_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR22_OVER24_FD() != null) {
						cell13.setCellValue(record.getR22_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR22_NOACC() != null) {
						cell15.setCellValue(record.getR22_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 22
					row = sheet.getRow(22);

					// Row 23
					// Column2
					cell2 = row.createCell(1);
					if (record.getR23_CURRENT() != null) {
						cell2.setCellValue(record.getR23_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR23_CALL() != null) {
						cell3.setCellValue(record.getR23_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR23_SAVINGS() != null) {
						cell4.setCellValue(record.getR23_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR23_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR23_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR23_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR23_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR23_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR23_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR23_1_2M_FD() != null) {
						cell8.setCellValue(record.getR23_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR23_4_6M_FD() != null) {
						cell9.setCellValue(record.getR23_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR23_7_12M_FD() != null) {
						cell10.setCellValue(record.getR23_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR23_13_18M_FD() != null) {
						cell11.setCellValue(record.getR23_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR23_19_24M_FD() != null) {
						cell12.setCellValue(record.getR23_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR23_OVER24_FD() != null) {
						cell13.setCellValue(record.getR23_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR23_NOACC() != null) {
						cell15.setCellValue(record.getR23_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 23
					row = sheet.getRow(23);

					// Row 24
					// Column2
					cell2 = row.createCell(1);
					if (record.getR24_CURRENT() != null) {
						cell2.setCellValue(record.getR24_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR24_CALL() != null) {
						cell3.setCellValue(record.getR24_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR24_SAVINGS() != null) {
						cell4.setCellValue(record.getR24_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR24_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR24_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR24_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR24_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR24_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR24_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR24_1_2M_FD() != null) {
						cell8.setCellValue(record.getR24_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR24_4_6M_FD() != null) {
						cell9.setCellValue(record.getR24_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR24_7_12M_FD() != null) {
						cell10.setCellValue(record.getR24_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR24_13_18M_FD() != null) {
						cell11.setCellValue(record.getR24_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR24_19_24M_FD() != null) {
						cell12.setCellValue(record.getR24_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR24_OVER24_FD() != null) {
						cell13.setCellValue(record.getR24_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR24_NOACC() != null) {
						cell15.setCellValue(record.getR24_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 25
					row = sheet.getRow(25);

					// Row 26
					// Column2
					cell2 = row.createCell(1);
					if (record.getR26_CURRENT() != null) {
						cell2.setCellValue(record.getR26_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR26_CALL() != null) {
						cell3.setCellValue(record.getR26_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR26_SAVINGS() != null) {
						cell4.setCellValue(record.getR26_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR26_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR26_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR26_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR26_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR26_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR26_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR26_1_2M_FD() != null) {
						cell8.setCellValue(record.getR26_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR26_4_6M_FD() != null) {
						cell9.setCellValue(record.getR26_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR26_7_12M_FD() != null) {
						cell10.setCellValue(record.getR26_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR26_13_18M_FD() != null) {
						cell11.setCellValue(record.getR26_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR26_19_24M_FD() != null) {
						cell12.setCellValue(record.getR26_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR26_OVER24_FD() != null) {
						cell13.setCellValue(record.getR26_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR26_TOTAL() != null) {
//					    cell14.setCellValue(record.getR26_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR26_NOACC() != null) {
						cell15.setCellValue(record.getR26_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 26
					row = sheet.getRow(31);

					// Row 32
					// Column2
					cell2 = row.createCell(1);
					if (record.getR32_CURRENT() != null) {
						System.out.println(record.getR32_CURRENT());
						cell2.setCellValue(record.getR32_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR32_CALL() != null) {
						cell3.setCellValue(record.getR32_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR32_SAVINGS() != null) {
						cell4.setCellValue(record.getR32_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR32_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR32_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR32_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR32_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR32_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR32_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR32_1_2M_FD() != null) {
						cell8.setCellValue(record.getR32_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR32_4_6M_FD() != null) {
						cell9.setCellValue(record.getR32_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR32_7_12M_FD() != null) {
						cell10.setCellValue(record.getR32_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR32_13_18M_FD() != null) {
						cell11.setCellValue(record.getR32_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR32_19_24M_FD() != null) {
						cell12.setCellValue(record.getR32_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR32_OVER24_FD() != null) {
						cell13.setCellValue(record.getR32_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR32_TOTAL() != null) {
//					    cell14.setCellValue(record.getR32_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR32_NOACC() != null) {
						cell15.setCellValue(record.getR32_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 32
					row = sheet.getRow(32);

					// Row 33
					// Column2
					cell2 = row.createCell(1);
					if (record.getR33_CURRENT() != null) {
						cell2.setCellValue(record.getR33_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR33_CALL() != null) {
						cell3.setCellValue(record.getR33_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR33_SAVINGS() != null) {
						cell4.setCellValue(record.getR33_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR33_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR33_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR33_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR33_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR33_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR33_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR33_1_2M_FD() != null) {
						cell8.setCellValue(record.getR33_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR33_4_6M_FD() != null) {
						cell9.setCellValue(record.getR33_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR33_7_12M_FD() != null) {
						cell10.setCellValue(record.getR33_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR33_13_18M_FD() != null) {
						cell11.setCellValue(record.getR33_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR33_19_24M_FD() != null) {
						cell12.setCellValue(record.getR33_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR33_OVER24_FD() != null) {
						cell13.setCellValue(record.getR33_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR33_TOTAL() != null) {
//					    cell14.setCellValue(record.getR33_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR33_NOACC() != null) {
						cell15.setCellValue(record.getR33_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 33
					row = sheet.getRow(33);

					// Row 34
					// Column2
					cell2 = row.createCell(1);
					if (record.getR34_CURRENT() != null) {
						cell2.setCellValue(record.getR34_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR34_CALL() != null) {
						cell3.setCellValue(record.getR34_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR34_SAVINGS() != null) {
						cell4.setCellValue(record.getR34_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR34_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR34_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR34_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR34_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR34_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR34_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR34_1_2M_FD() != null) {
						cell8.setCellValue(record.getR34_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR34_4_6M_FD() != null) {
						cell9.setCellValue(record.getR34_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR34_7_12M_FD() != null) {
						cell10.setCellValue(record.getR34_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR34_13_18M_FD() != null) {
						cell11.setCellValue(record.getR34_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR34_19_24M_FD() != null) {
						cell12.setCellValue(record.getR34_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR34_OVER24_FD() != null) {
						cell13.setCellValue(record.getR34_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR34_TOTAL() != null) {
//					    cell14.setCellValue(record.getR34_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR34_NOACC() != null) {
						cell15.setCellValue(record.getR34_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 34
					row = sheet.getRow(34);

					// Row 35
					// Column2
					cell2 = row.createCell(1);
					if (record.getR35_CURRENT() != null) {
						cell2.setCellValue(record.getR35_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR35_CALL() != null) {
						cell3.setCellValue(record.getR35_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR35_SAVINGS() != null) {
						cell4.setCellValue(record.getR35_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR35_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR35_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR35_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR35_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR35_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR35_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR35_1_2M_FD() != null) {
						cell8.setCellValue(record.getR35_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR35_4_6M_FD() != null) {
						cell9.setCellValue(record.getR35_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR35_7_12M_FD() != null) {
						cell10.setCellValue(record.getR35_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR35_13_18M_FD() != null) {
						cell11.setCellValue(record.getR35_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR35_19_24M_FD() != null) {
						cell12.setCellValue(record.getR35_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR35_OVER24_FD() != null) {
						cell13.setCellValue(record.getR35_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR35_TOTAL() != null) {
//					    cell14.setCellValue(record.getR35_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR35_NOACC() != null) {
						cell15.setCellValue(record.getR35_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 35
					row = sheet.getRow(35);

					// Row 36
					// Column2
					cell2 = row.createCell(1);
					if (record.getR36_CURRENT() != null) {
						cell2.setCellValue(record.getR36_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR36_CALL() != null) {
						cell3.setCellValue(record.getR36_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR36_SAVINGS() != null) {
						cell4.setCellValue(record.getR36_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR36_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR36_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR36_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR36_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR36_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR36_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR36_1_2M_FD() != null) {
						cell8.setCellValue(record.getR36_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR36_4_6M_FD() != null) {
						cell9.setCellValue(record.getR36_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR36_7_12M_FD() != null) {
						cell10.setCellValue(record.getR36_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR36_13_18M_FD() != null) {
						cell11.setCellValue(record.getR36_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR36_19_24M_FD() != null) {
						cell12.setCellValue(record.getR36_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR36_OVER24_FD() != null) {
						cell13.setCellValue(record.getR36_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR36_NOACC() != null) {
						cell15.setCellValue(record.getR36_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 36
					row = sheet.getRow(36);

					// Row 37
					// Column2
					cell2 = row.createCell(1);
					if (record.getR37_CURRENT() != null) {
						cell2.setCellValue(record.getR37_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR37_CALL() != null) {
						cell3.setCellValue(record.getR37_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR37_SAVINGS() != null) {
						cell4.setCellValue(record.getR37_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR37_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR37_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR37_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR37_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR37_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR37_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR37_1_2M_FD() != null) {
						cell8.setCellValue(record.getR37_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR37_4_6M_FD() != null) {
						cell9.setCellValue(record.getR37_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR37_7_12M_FD() != null) {
						cell10.setCellValue(record.getR37_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR37_13_18M_FD() != null) {
						cell11.setCellValue(record.getR37_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR37_19_24M_FD() != null) {
						cell12.setCellValue(record.getR37_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR37_OVER24_FD() != null) {
						cell13.setCellValue(record.getR37_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR37_TOTAL() != null) {
//					    cell14.setCellValue(record.getR37_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR37_NOACC() != null) {
						cell15.setCellValue(record.getR37_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 37
					row = sheet.getRow(37);

					// Row 38
					// Column2
					cell2 = row.createCell(1);
					if (record.getR38_CURRENT() != null) {
						cell2.setCellValue(record.getR38_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR38_CALL() != null) {
						cell3.setCellValue(record.getR38_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR38_SAVINGS() != null) {
						cell4.setCellValue(record.getR38_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR38_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR38_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR38_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR38_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR38_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR38_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR38_1_2M_FD() != null) {
						cell8.setCellValue(record.getR38_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR38_4_6M_FD() != null) {
						cell9.setCellValue(record.getR38_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR38_7_12M_FD() != null) {
						cell10.setCellValue(record.getR38_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR38_13_18M_FD() != null) {
						cell11.setCellValue(record.getR38_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR38_19_24M_FD() != null) {
						cell12.setCellValue(record.getR38_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR38_OVER24_FD() != null) {
						cell13.setCellValue(record.getR38_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR38_NOACC() != null) {
						cell15.setCellValue(record.getR38_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 38
					row = sheet.getRow(38);

					// Row 39
					// Column2
					cell2 = row.createCell(1);
					if (record.getR39_CURRENT() != null) {
						cell2.setCellValue(record.getR39_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR39_CALL() != null) {
						cell3.setCellValue(record.getR39_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR39_SAVINGS() != null) {
						cell4.setCellValue(record.getR39_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR39_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR39_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR39_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR39_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR39_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR39_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR39_1_2M_FD() != null) {
						cell8.setCellValue(record.getR39_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR39_4_6M_FD() != null) {
						cell9.setCellValue(record.getR39_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR39_7_12M_FD() != null) {
						cell10.setCellValue(record.getR39_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR39_13_18M_FD() != null) {
						cell11.setCellValue(record.getR39_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR39_19_24M_FD() != null) {
						cell12.setCellValue(record.getR39_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR39_OVER24_FD() != null) {
						cell13.setCellValue(record.getR39_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column14
//					cell14 = row.createCell(13);
//					if (record.getR39_TOTAL() != null) {
//					    cell14.setCellValue(record.getR39_TOTAL().doubleValue());
//					    cell14.setCellStyle(numberStyle);
//					} else {
//					    cell14.setCellValue("");
//					    cell14.setCellStyle(textStyle);
//					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR39_NOACC() != null) {
						cell15.setCellValue(record.getR39_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 39
					row = sheet.getRow(39);

					// Row 40
					// Column2
					cell2 = row.createCell(1);
					if (record.getR40_CURRENT() != null) {
						cell2.setCellValue(record.getR40_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR40_CALL() != null) {
						cell3.setCellValue(record.getR40_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR40_SAVINGS() != null) {
						cell4.setCellValue(record.getR40_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR40_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR40_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR40_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR40_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR40_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR40_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR40_1_2M_FD() != null) {
						cell8.setCellValue(record.getR40_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR40_4_6M_FD() != null) {
						cell9.setCellValue(record.getR40_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR40_7_12M_FD() != null) {
						cell10.setCellValue(record.getR40_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR40_13_18M_FD() != null) {
						cell11.setCellValue(record.getR40_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR40_19_24M_FD() != null) {
						cell12.setCellValue(record.getR40_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR40_OVER24_FD() != null) {
						cell13.setCellValue(record.getR40_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR40_NOACC() != null) {
						cell15.setCellValue(record.getR40_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 40
					row = sheet.getRow(40);

					// Row 41
					// Column2
					cell2 = row.createCell(1);
					if (record.getR41_CURRENT() != null) {
						cell2.setCellValue(record.getR41_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR41_CALL() != null) {
						cell3.setCellValue(record.getR41_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR41_SAVINGS() != null) {
						cell4.setCellValue(record.getR41_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR41_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR41_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR41_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR41_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR41_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR41_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR41_1_2M_FD() != null) {
						cell8.setCellValue(record.getR41_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR41_4_6M_FD() != null) {
						cell9.setCellValue(record.getR41_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR41_7_12M_FD() != null) {
						cell10.setCellValue(record.getR41_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR41_13_18M_FD() != null) {
						cell11.setCellValue(record.getR41_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR41_19_24M_FD() != null) {
						cell12.setCellValue(record.getR41_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR41_OVER24_FD() != null) {
						cell13.setCellValue(record.getR41_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR41_NOACC() != null) {
						cell15.setCellValue(record.getR41_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 41
					row = sheet.getRow(41);

					// Row 42
					// Column2
					cell2 = row.createCell(1);
					if (record.getR42_CURRENT() != null) {
						cell2.setCellValue(record.getR42_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR42_CALL() != null) {
						cell3.setCellValue(record.getR42_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR42_SAVINGS() != null) {
						cell4.setCellValue(record.getR42_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR42_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR42_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR42_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR42_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR42_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR42_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR42_1_2M_FD() != null) {
						cell8.setCellValue(record.getR42_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR42_4_6M_FD() != null) {
						cell9.setCellValue(record.getR42_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR42_7_12M_FD() != null) {
						cell10.setCellValue(record.getR42_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR42_13_18M_FD() != null) {
						cell11.setCellValue(record.getR42_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR42_19_24M_FD() != null) {
						cell12.setCellValue(record.getR42_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR42_OVER24_FD() != null) {
						cell13.setCellValue(record.getR42_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR42_NOACC() != null) {
						cell15.setCellValue(record.getR42_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 42
					row = sheet.getRow(42);

					// Row 43
					// Column2
					cell2 = row.createCell(1);
					if (record.getR43_CURRENT() != null) {
						cell2.setCellValue(record.getR43_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR43_CALL() != null) {
						cell3.setCellValue(record.getR43_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR43_SAVINGS() != null) {
						cell4.setCellValue(record.getR43_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR43_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR43_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR43_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR43_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR43_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR43_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR43_1_2M_FD() != null) {
						cell8.setCellValue(record.getR43_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR43_4_6M_FD() != null) {
						cell9.setCellValue(record.getR43_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR43_7_12M_FD() != null) {
						cell10.setCellValue(record.getR43_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR43_13_18M_FD() != null) {
						cell11.setCellValue(record.getR43_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR43_19_24M_FD() != null) {
						cell12.setCellValue(record.getR43_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR43_OVER24_FD() != null) {
						cell13.setCellValue(record.getR43_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR43_NOACC() != null) {
						cell15.setCellValue(record.getR43_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 43
					row = sheet.getRow(43);

					// Row 44
					// Column2
					cell2 = row.createCell(1);
					if (record.getR44_CURRENT() != null) {
						cell2.setCellValue(record.getR44_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR44_CALL() != null) {
						cell3.setCellValue(record.getR44_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR44_SAVINGS() != null) {
						cell4.setCellValue(record.getR44_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR44_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR44_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR44_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR44_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR44_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR44_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR44_1_2M_FD() != null) {
						cell8.setCellValue(record.getR44_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR44_4_6M_FD() != null) {
						cell9.setCellValue(record.getR44_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR44_7_12M_FD() != null) {
						cell10.setCellValue(record.getR44_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR44_13_18M_FD() != null) {
						cell11.setCellValue(record.getR44_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR44_19_24M_FD() != null) {
						cell12.setCellValue(record.getR44_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR44_OVER24_FD() != null) {
						cell13.setCellValue(record.getR44_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR44_NOACC() != null) {
						cell15.setCellValue(record.getR44_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 44
					row = sheet.getRow(44);

					// Row 45
					// Column2
					cell2 = row.createCell(1);
					if (record.getR45_CURRENT() != null) {
						cell2.setCellValue(record.getR45_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR45_CALL() != null) {
						cell3.setCellValue(record.getR45_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR45_SAVINGS() != null) {
						cell4.setCellValue(record.getR45_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR45_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR45_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR45_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR45_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR45_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR45_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR45_1_2M_FD() != null) {
						cell8.setCellValue(record.getR45_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR45_4_6M_FD() != null) {
						cell9.setCellValue(record.getR45_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR45_7_12M_FD() != null) {
						cell10.setCellValue(record.getR45_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR45_13_18M_FD() != null) {
						cell11.setCellValue(record.getR45_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR45_19_24M_FD() != null) {
						cell12.setCellValue(record.getR45_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR45_OVER24_FD() != null) {
						cell13.setCellValue(record.getR45_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR45_NOACC() != null) {
						cell15.setCellValue(record.getR45_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Assign Row 46
					row = sheet.getRow(46);

					// Row 47
					// Column2
					cell2 = row.createCell(1);
					if (record.getR47_CURRENT() != null) {
						cell2.setCellValue(record.getR47_CURRENT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Column3
					cell3 = row.createCell(2);
					if (record.getR47_CALL() != null) {
						cell3.setCellValue(record.getR47_CALL().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Column4
					cell4 = row.createCell(3);
					if (record.getR47_SAVINGS() != null) {
						cell4.setCellValue(record.getR47_SAVINGS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column5
					cell5 = row.createCell(4);
					if (record.getR47_0_31D_NOTICE() != null) {
						cell5.setCellValue(record.getR47_0_31D_NOTICE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column6
					cell6 = row.createCell(5);
					if (record.getR47_32_88D_NOTICE() != null) {
						cell6.setCellValue(record.getR47_32_88D_NOTICE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column7
					cell7 = row.createCell(6);
					if (record.getR47_91D_DEPOSIT() != null) {
						cell7.setCellValue(record.getR47_91D_DEPOSIT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column8
					cell8 = row.createCell(7);
					if (record.getR47_1_2M_FD() != null) {
						cell8.setCellValue(record.getR47_1_2M_FD().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column9
					cell9 = row.createCell(8);
					if (record.getR47_4_6M_FD() != null) {
						cell9.setCellValue(record.getR47_4_6M_FD().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column10
					cell10 = row.createCell(9);
					if (record.getR47_7_12M_FD() != null) {
						cell10.setCellValue(record.getR47_7_12M_FD().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column11
					cell11 = row.createCell(10);
					if (record.getR47_13_18M_FD() != null) {
						cell11.setCellValue(record.getR47_13_18M_FD().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Column12
					cell12 = row.createCell(11);
					if (record.getR47_19_24M_FD() != null) {
						cell12.setCellValue(record.getR47_19_24M_FD().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Column13
					cell13 = row.createCell(12);
					if (record.getR47_OVER24_FD() != null) {
						cell13.setCellValue(record.getR47_OVER24_FD().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Column15
					cell15 = row.createCell(14);
					if (record.getR47_NOACC() != null) {
						cell15.setCellValue(record.getR47_NOACC().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}
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
	public byte[] BRRS_Q_SMME_DEPResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<Q_SMME_DEP_Resub_Summary_Entity> dataList = brrs_Q_SMME_DEP_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_SMME_DEP report. Returning empty result.");
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
						Q_SMME_DEP_Resub_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL

						// ROW11
						// COLUMN2
						Cell cell2 = row.createCell(3);
						if (record.getR11_CURRENT() != null) {
							cell2.setCellValue(record.getR11_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row11
						// Column3
						Cell cell3 = row.createCell(4);
						if (record.getR11_CALL() != null) {
							cell3.setCellValue(record.getR11_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// row11
						// Column4
						Cell cell4 = row.createCell(5);
						if (record.getR11_SAVINGS() != null) {
							cell4.setCellValue(record.getR11_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row11
						// Column5
						Cell cell5 = row.createCell(6);
						if (record.getR11_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR11_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// row11
						// Column6
						Cell cell6 = row.createCell(7);
						if (record.getR11_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR11_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row11
						// Column7
						Cell cell7 = row.createCell(8);
						if (record.getR11_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR11_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						Cell cell8 = row.createCell(9);

						BigDecimal total = (record.getR11_1_2M_FD() == null ? BigDecimal.ZERO : record.getR11_1_2M_FD())
								.add(record.getR11_4_6M_FD() == null ? BigDecimal.ZERO : record.getR11_4_6M_FD());

						if (total.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}
						// row11
						// Column10
						Cell cell10 = row.createCell(10);
						if (record.getR11_7_12M_FD() != null) {
							cell10.setCellValue(record.getR11_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// row11
						// Column11
						Cell cell11 = row.createCell(11);
						if (record.getR11_13_18M_FD() != null) {
							cell11.setCellValue(record.getR11_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// row11
						// Column12
						Cell cell12 = row.createCell(12);
						if (record.getR11_19_24M_FD() != null) {
							cell12.setCellValue(record.getR11_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// row11
						// Column13
						Cell cell13 = row.createCell(13);
						if (record.getR11_OVER24_FD() != null) {
							cell13.setCellValue(record.getR11_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						row = sheet.getRow(10);

						// Row 12
						// Column2
						cell2 = row.createCell(3);
						if (record.getR12_CURRENT() != null) {
							cell2.setCellValue(record.getR12_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR12_CALL() != null) {
							cell3.setCellValue(record.getR12_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR12_SAVINGS() != null) {
							cell4.setCellValue(record.getR12_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR12_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR12_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR12_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR12_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR12_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR12_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(9);

						BigDecimal total1 = (record.getR12_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR12_1_2M_FD())
								.add(record.getR12_4_6M_FD() == null ? BigDecimal.ZERO : record.getR12_4_6M_FD());

						if (total1.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total1.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR12_7_12M_FD() != null) {
							cell10.setCellValue(record.getR12_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR12_13_18M_FD() != null) {
							cell11.setCellValue(record.getR12_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR12_19_24M_FD() != null) {
							cell12.setCellValue(record.getR12_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR12_OVER24_FD() != null) {
							cell13.setCellValue(record.getR12_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R13
						// ==========================

						row = sheet.getRow(11); // Adjust row index if needed

						// Column2
						cell2 = row.createCell(3);
						if (record.getR13_CURRENT() != null) {
							cell2.setCellValue(record.getR13_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR13_CALL() != null) {
							cell3.setCellValue(record.getR13_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR13_SAVINGS() != null) {
							cell4.setCellValue(record.getR13_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR13_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR13_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR13_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR13_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR13_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR13_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total7 = (record.getR13_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR13_1_2M_FD())
								.add(record.getR13_4_6M_FD() == null ? BigDecimal.ZERO : record.getR13_4_6M_FD());

						if (total7.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total7.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR13_7_12M_FD() != null) {
							cell10.setCellValue(record.getR13_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR13_13_18M_FD() != null) {
							cell11.setCellValue(record.getR13_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR13_19_24M_FD() != null) {
							cell12.setCellValue(record.getR13_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR13_OVER24_FD() != null) {
							cell13.setCellValue(record.getR13_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R14
						// ==========================

						row = sheet.getRow(12); // Adjust row index if needed

						// Column2
						cell2 = row.createCell(3);
						if (record.getR14_CURRENT() != null) {
							cell2.setCellValue(record.getR14_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR14_CALL() != null) {
							cell3.setCellValue(record.getR14_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR14_SAVINGS() != null) {
							cell4.setCellValue(record.getR14_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR14_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR14_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR14_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR14_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR14_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR14_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total8 = (record.getR14_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR14_1_2M_FD())
								.add(record.getR14_4_6M_FD() == null ? BigDecimal.ZERO : record.getR14_4_6M_FD());

						if (total8.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total8.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR14_7_12M_FD() != null) {
							cell10.setCellValue(record.getR14_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR14_13_18M_FD() != null) {
							cell11.setCellValue(record.getR14_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR14_19_24M_FD() != null) {
							cell12.setCellValue(record.getR14_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR14_OVER24_FD() != null) {
							cell13.setCellValue(record.getR14_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R15
						// ==========================

						row = sheet.getRow(19);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR15_CURRENT() != null) {
							cell2.setCellValue(record.getR15_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR15_CALL() != null) {
							cell3.setCellValue(record.getR15_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR15_SAVINGS() != null) {
							cell4.setCellValue(record.getR15_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR15_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR15_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR15_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR15_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR15_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR15_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total3 = (record.getR15_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR15_1_2M_FD())
								.add(record.getR15_4_6M_FD() == null ? BigDecimal.ZERO : record.getR15_4_6M_FD());

						if (total3.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total3.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR15_7_12M_FD() != null) {
							cell10.setCellValue(record.getR15_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR15_13_18M_FD() != null) {
							cell11.setCellValue(record.getR15_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR15_19_24M_FD() != null) {
							cell12.setCellValue(record.getR15_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR15_OVER24_FD() != null) {
							cell13.setCellValue(record.getR15_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R16
						// ==========================

						row = sheet.getRow(13);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR16_CURRENT() != null) {
							cell2.setCellValue(record.getR16_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR16_CALL() != null) {
							cell3.setCellValue(record.getR16_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR16_SAVINGS() != null) {
							cell4.setCellValue(record.getR16_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR16_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR16_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR16_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR16_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR16_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR16_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total11 = (record.getR16_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR16_1_2M_FD())
								.add(record.getR16_4_6M_FD() == null ? BigDecimal.ZERO : record.getR16_4_6M_FD());

						if (total11.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total11.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR16_7_12M_FD() != null) {
							cell10.setCellValue(record.getR16_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR16_13_18M_FD() != null) {
							cell11.setCellValue(record.getR16_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR16_19_24M_FD() != null) {
							cell12.setCellValue(record.getR16_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR16_OVER24_FD() != null) {
							cell13.setCellValue(record.getR16_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R17
						// ==========================

						row = sheet.getRow(14);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR17_CURRENT() != null) {
							cell2.setCellValue(record.getR17_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR17_CALL() != null) {
							cell3.setCellValue(record.getR17_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR17_SAVINGS() != null) {
							cell4.setCellValue(record.getR17_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR17_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR17_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR17_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR17_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR17_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR17_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total81 = (record.getR17_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR17_1_2M_FD())
								.add(record.getR17_4_6M_FD() == null ? BigDecimal.ZERO : record.getR17_4_6M_FD());

						if (total81.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total81.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR17_7_12M_FD() != null) {
							cell10.setCellValue(record.getR17_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR17_13_18M_FD() != null) {
							cell11.setCellValue(record.getR17_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR17_19_24M_FD() != null) {
							cell12.setCellValue(record.getR17_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR17_OVER24_FD() != null) {
							cell13.setCellValue(record.getR17_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R18
						// ==========================

						row = sheet.getRow(15);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR18_CURRENT() != null) {
							cell2.setCellValue(record.getR18_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR18_CALL() != null) {
							cell3.setCellValue(record.getR18_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR18_SAVINGS() != null) {
							cell4.setCellValue(record.getR18_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR18_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR18_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR18_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR18_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR18_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR18_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total4 = (record.getR18_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR18_1_2M_FD())
								.add(record.getR18_4_6M_FD() == null ? BigDecimal.ZERO : record.getR18_4_6M_FD());

						if (total4.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total4.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR18_7_12M_FD() != null) {
							cell10.setCellValue(record.getR18_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR18_13_18M_FD() != null) {
							cell11.setCellValue(record.getR18_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR18_19_24M_FD() != null) {
							cell12.setCellValue(record.getR18_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR18_OVER24_FD() != null) {
							cell13.setCellValue(record.getR18_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R19
						// ==========================

						row = sheet.getRow(16);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR19_CURRENT() != null) {
							cell2.setCellValue(record.getR19_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR19_CALL() != null) {
							cell3.setCellValue(record.getR19_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR19_SAVINGS() != null) {
							cell4.setCellValue(record.getR19_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR19_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR19_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR19_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR19_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR19_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR19_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal total5 = (record.getR19_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR19_1_2M_FD())
								.add(record.getR19_4_6M_FD() == null ? BigDecimal.ZERO : record.getR19_4_6M_FD());

						if (total5.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(total5.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR19_7_12M_FD() != null) {
							cell10.setCellValue(record.getR19_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR19_13_18M_FD() != null) {
							cell11.setCellValue(record.getR19_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR19_19_24M_FD() != null) {
							cell12.setCellValue(record.getR19_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR19_OVER24_FD() != null) {
							cell13.setCellValue(record.getR19_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R20
						// ==========================

						row = sheet.getRow(17);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR20_CURRENT() != null) {
							cell2.setCellValue(record.getR20_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR20_CALL() != null) {
							cell3.setCellValue(record.getR20_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR20_SAVINGS() != null) {
							cell4.setCellValue(record.getR20_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR20_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR20_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR20_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR20_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR20_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR20_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal totalR20 = (record.getR20_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR20_1_2M_FD())
								.add(record.getR20_4_6M_FD() == null ? BigDecimal.ZERO : record.getR20_4_6M_FD());

						if (totalR20.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR20.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR20_7_12M_FD() != null) {
							cell10.setCellValue(record.getR20_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR20_13_18M_FD() != null) {
							cell11.setCellValue(record.getR20_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR20_19_24M_FD() != null) {
							cell12.setCellValue(record.getR20_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR20_OVER24_FD() != null) {
							cell13.setCellValue(record.getR20_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R21
						// ==========================

						row = sheet.getRow(18);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR21_CURRENT() != null) {
							cell2.setCellValue(record.getR21_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR21_CALL() != null) {
							cell3.setCellValue(record.getR21_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR21_SAVINGS() != null) {
							cell4.setCellValue(record.getR21_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR21_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR21_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR21_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR21_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR21_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR21_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);

						BigDecimal totalR21 = (record.getR21_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR21_1_2M_FD())
								.add(record.getR21_4_6M_FD() == null ? BigDecimal.ZERO : record.getR21_4_6M_FD());

						if (totalR21.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR21.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR21_7_12M_FD() != null) {
							cell10.setCellValue(record.getR21_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR21_13_18M_FD() != null) {
							cell11.setCellValue(record.getR21_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR21_19_24M_FD() != null) {
							cell12.setCellValue(record.getR21_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR21_OVER24_FD() != null) {
							cell13.setCellValue(record.getR21_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R22
						// ==========================

						row = sheet.getRow(20);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR22_CURRENT() != null) {
							cell2.setCellValue(record.getR22_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR22_CALL() != null) {
							cell3.setCellValue(record.getR22_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR22_SAVINGS() != null) {
							cell4.setCellValue(record.getR22_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR22_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR22_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR22_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR22_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR22_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR22_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR22 = (record.getR22_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR22_1_2M_FD())
								.add(record.getR22_4_6M_FD() == null ? BigDecimal.ZERO : record.getR22_4_6M_FD());

						if (totalR22.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR22.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR22_7_12M_FD() != null) {
							cell10.setCellValue(record.getR22_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR22_13_18M_FD() != null) {
							cell11.setCellValue(record.getR22_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR22_19_24M_FD() != null) {
							cell12.setCellValue(record.getR22_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR22_OVER24_FD() != null) {
							cell13.setCellValue(record.getR22_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						row = sheet.getRow(21);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR27_CURRENT() != null) {
							cell2.setCellValue(record.getR27_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR27_CALL() != null) {
							cell3.setCellValue(record.getR27_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR27_SAVINGS() != null) {
							cell4.setCellValue(record.getR27_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR27_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR27_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR27_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR27_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR27_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR27_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR27 = (record.getR27_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR27_1_2M_FD())
								.add(record.getR27_4_6M_FD() == null ? BigDecimal.ZERO : record.getR27_4_6M_FD());

						if (totalR27.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR27.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR27_7_12M_FD() != null) {
							cell10.setCellValue(record.getR27_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR27_13_18M_FD() != null) {
							cell11.setCellValue(record.getR27_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR27_19_24M_FD() != null) {
							cell12.setCellValue(record.getR27_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR27_OVER24_FD() != null) {
							cell13.setCellValue(record.getR27_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R23
						// ==========================

						row = sheet.getRow(22);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR23_CURRENT() != null) {
							cell2.setCellValue(record.getR23_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR23_CALL() != null) {
							cell3.setCellValue(record.getR23_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR23_SAVINGS() != null) {
							cell4.setCellValue(record.getR23_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR23_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR23_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR23_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR23_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR23_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR23_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR23 = (record.getR23_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR23_1_2M_FD())
								.add(record.getR23_4_6M_FD() == null ? BigDecimal.ZERO : record.getR23_4_6M_FD());

						if (totalR23.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR23.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR23_7_12M_FD() != null) {
							cell10.setCellValue(record.getR23_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR23_13_18M_FD() != null) {
							cell11.setCellValue(record.getR23_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR23_19_24M_FD() != null) {
							cell12.setCellValue(record.getR23_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR23_OVER24_FD() != null) {
							cell13.setCellValue(record.getR23_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R24
						// ==========================

						row = sheet.getRow(23);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR24_CURRENT() != null) {
							cell2.setCellValue(record.getR24_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR24_CALL() != null) {
							cell3.setCellValue(record.getR24_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR24_SAVINGS() != null) {
							cell4.setCellValue(record.getR24_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR24_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR24_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR24_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR24_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR24_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR24_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR24 = (record.getR24_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR24_1_2M_FD())
								.add(record.getR24_4_6M_FD() == null ? BigDecimal.ZERO : record.getR24_4_6M_FD());

						if (totalR24.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR24.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR24_7_12M_FD() != null) {
							cell10.setCellValue(record.getR24_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR24_13_18M_FD() != null) {
							cell11.setCellValue(record.getR24_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR24_19_24M_FD() != null) {
							cell12.setCellValue(record.getR24_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR24_OVER24_FD() != null) {
							cell13.setCellValue(record.getR24_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R26
						// ==========================

						row = sheet.getRow(25);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR26_CURRENT() != null) {
							cell2.setCellValue(record.getR26_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR26_CALL() != null) {
							cell3.setCellValue(record.getR26_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR26_SAVINGS() != null) {
							cell4.setCellValue(record.getR26_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR26_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR26_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR26_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR26_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR26_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR26_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR26 = (record.getR26_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR26_1_2M_FD())
								.add(record.getR26_4_6M_FD() == null ? BigDecimal.ZERO : record.getR26_4_6M_FD());

						if (totalR26.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR26.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR26_7_12M_FD() != null) {
							cell10.setCellValue(record.getR26_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR26_13_18M_FD() != null) {
							cell11.setCellValue(record.getR26_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR26_19_24M_FD() != null) {
							cell12.setCellValue(record.getR26_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR26_OVER24_FD() != null) {
							cell13.setCellValue(record.getR26_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R32
						// ==========================

						row = sheet.getRow(33);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR32_CURRENT() != null) {
							cell2.setCellValue(record.getR32_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR32_CALL() != null) {
							cell3.setCellValue(record.getR32_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR32_SAVINGS() != null) {
							cell4.setCellValue(record.getR32_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR32_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR32_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR32_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR32_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR32_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR32_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR32 = (record.getR32_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR32_1_2M_FD())
								.add(record.getR32_4_6M_FD() == null ? BigDecimal.ZERO : record.getR32_4_6M_FD());

						if (totalR32.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR32.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR32_7_12M_FD() != null) {
							cell10.setCellValue(record.getR32_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR32_13_18M_FD() != null) {
							cell11.setCellValue(record.getR32_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR32_19_24M_FD() != null) {
							cell12.setCellValue(record.getR32_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR32_OVER24_FD() != null) {
							cell13.setCellValue(record.getR32_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R33
						// ==========================

						row = sheet.getRow(34);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR33_CURRENT() != null) {
							cell2.setCellValue(record.getR33_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR33_CALL() != null) {
							cell3.setCellValue(record.getR33_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR33_SAVINGS() != null) {
							cell4.setCellValue(record.getR33_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR33_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR33_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR33_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR33_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR33_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR33_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR33 = (record.getR33_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR33_1_2M_FD())
								.add(record.getR33_4_6M_FD() == null ? BigDecimal.ZERO : record.getR33_4_6M_FD());

						if (totalR33.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR33.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR33_7_12M_FD() != null) {
							cell10.setCellValue(record.getR33_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR33_13_18M_FD() != null) {
							cell11.setCellValue(record.getR33_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR33_19_24M_FD() != null) {
							cell12.setCellValue(record.getR33_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR33_OVER24_FD() != null) {
							cell13.setCellValue(record.getR33_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R34
						// ==========================

						row = sheet.getRow(35);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR34_CURRENT() != null) {
							cell2.setCellValue(record.getR34_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR34_CALL() != null) {
							cell3.setCellValue(record.getR34_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR34_SAVINGS() != null) {
							cell4.setCellValue(record.getR34_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR34_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR34_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR34_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR34_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR34_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR34_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR34 = (record.getR34_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR34_1_2M_FD())
								.add(record.getR34_4_6M_FD() == null ? BigDecimal.ZERO : record.getR34_4_6M_FD());

						if (totalR34.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR34.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR34_7_12M_FD() != null) {
							cell10.setCellValue(record.getR34_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR34_13_18M_FD() != null) {
							cell11.setCellValue(record.getR34_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR34_19_24M_FD() != null) {
							cell12.setCellValue(record.getR34_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR34_OVER24_FD() != null) {
							cell13.setCellValue(record.getR34_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R35
						// ==========================

						row = sheet.getRow(36);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR35_CURRENT() != null) {
							cell2.setCellValue(record.getR35_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR35_CALL() != null) {
							cell3.setCellValue(record.getR35_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR35_SAVINGS() != null) {
							cell4.setCellValue(record.getR35_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR35_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR35_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR35_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR35_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR35_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR35_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR35 = (record.getR35_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR35_1_2M_FD())
								.add(record.getR35_4_6M_FD() == null ? BigDecimal.ZERO : record.getR35_4_6M_FD());

						if (totalR35.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR35.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR35_7_12M_FD() != null) {
							cell10.setCellValue(record.getR35_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR35_13_18M_FD() != null) {
							cell11.setCellValue(record.getR35_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR35_19_24M_FD() != null) {
							cell12.setCellValue(record.getR35_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR35_OVER24_FD() != null) {
							cell13.setCellValue(record.getR35_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R36
						// ==========================

						row = sheet.getRow(43);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR36_CURRENT() != null) {
							cell2.setCellValue(record.getR36_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR36_CALL() != null) {
							cell3.setCellValue(record.getR36_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR36_SAVINGS() != null) {
							cell4.setCellValue(record.getR36_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR36_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR36_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR36_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR36_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR36_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR36_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR36 = (record.getR36_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR36_1_2M_FD())
								.add(record.getR36_4_6M_FD() == null ? BigDecimal.ZERO : record.getR36_4_6M_FD());

						if (totalR36.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR36.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR36_7_12M_FD() != null) {
							cell10.setCellValue(record.getR36_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR36_13_18M_FD() != null) {
							cell11.setCellValue(record.getR36_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR36_19_24M_FD() != null) {
							cell12.setCellValue(record.getR36_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR36_OVER24_FD() != null) {
							cell13.setCellValue(record.getR36_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R37
						// ==========================

						row = sheet.getRow(37);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR37_CURRENT() != null) {
							cell2.setCellValue(record.getR37_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR37_CALL() != null) {
							cell3.setCellValue(record.getR37_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR37_SAVINGS() != null) {
							cell4.setCellValue(record.getR37_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR37_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR37_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR37_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR37_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR37_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR37_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR37 = (record.getR37_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR37_1_2M_FD())
								.add(record.getR37_4_6M_FD() == null ? BigDecimal.ZERO : record.getR37_4_6M_FD());

						if (totalR37.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR37.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR37_7_12M_FD() != null) {
							cell10.setCellValue(record.getR37_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR37_13_18M_FD() != null) {
							cell11.setCellValue(record.getR37_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR37_19_24M_FD() != null) {
							cell12.setCellValue(record.getR37_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR37_OVER24_FD() != null) {
							cell13.setCellValue(record.getR37_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						// ==========================
						// R38
						// ==========================

						row = sheet.getRow(38);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR38_CURRENT() != null) {
							cell2.setCellValue(record.getR38_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR38_CALL() != null) {
							cell3.setCellValue(record.getR38_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR38_SAVINGS() != null) {
							cell4.setCellValue(record.getR38_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR38_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR38_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR38_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR38_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR38_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR38_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR38 = (record.getR38_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR38_1_2M_FD())
								.add(record.getR38_4_6M_FD() == null ? BigDecimal.ZERO : record.getR38_4_6M_FD());

						if (totalR38.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR38.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR38_7_12M_FD() != null) {
							cell10.setCellValue(record.getR38_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR38_13_18M_FD() != null) {
							cell11.setCellValue(record.getR38_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR38_19_24M_FD() != null) {
							cell12.setCellValue(record.getR38_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR38_OVER24_FD() != null) {
							cell13.setCellValue(record.getR38_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R39
						// ==========================

						row = sheet.getRow(39);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR39_CURRENT() != null) {
							cell2.setCellValue(record.getR39_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR39_CALL() != null) {
							cell3.setCellValue(record.getR39_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR39_SAVINGS() != null) {
							cell4.setCellValue(record.getR39_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR39_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR39_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR39_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR39_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR39_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR39_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR39 = (record.getR39_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR39_1_2M_FD())
								.add(record.getR39_4_6M_FD() == null ? BigDecimal.ZERO : record.getR39_4_6M_FD());

						if (totalR39.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR39.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR39_7_12M_FD() != null) {
							cell10.setCellValue(record.getR39_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR39_13_18M_FD() != null) {
							cell11.setCellValue(record.getR39_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR39_19_24M_FD() != null) {
							cell12.setCellValue(record.getR39_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR39_OVER24_FD() != null) {
							cell13.setCellValue(record.getR39_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R40
						// ==========================

						row = sheet.getRow(40);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR40_CURRENT() != null) {
							cell2.setCellValue(record.getR40_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR40_CALL() != null) {
							cell3.setCellValue(record.getR40_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR40_SAVINGS() != null) {
							cell4.setCellValue(record.getR40_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR40_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR40_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR40_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR40_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR40_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR40_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR40 = (record.getR40_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR40_1_2M_FD())
								.add(record.getR40_4_6M_FD() == null ? BigDecimal.ZERO : record.getR40_4_6M_FD());

						if (totalR40.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR40.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR40_7_12M_FD() != null) {
							cell10.setCellValue(record.getR40_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR40_13_18M_FD() != null) {
							cell11.setCellValue(record.getR40_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR40_19_24M_FD() != null) {
							cell12.setCellValue(record.getR40_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR40_OVER24_FD() != null) {
							cell13.setCellValue(record.getR40_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R41
						// ==========================

						row = sheet.getRow(41);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR41_CURRENT() != null) {
							cell2.setCellValue(record.getR41_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR41_CALL() != null) {
							cell3.setCellValue(record.getR41_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR41_SAVINGS() != null) {
							cell4.setCellValue(record.getR41_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR41_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR41_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR41_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR41_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR41_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR41_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR41 = (record.getR41_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR41_1_2M_FD())
								.add(record.getR41_4_6M_FD() == null ? BigDecimal.ZERO : record.getR41_4_6M_FD());

						if (totalR41.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR41.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR41_7_12M_FD() != null) {
							cell10.setCellValue(record.getR41_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR41_13_18M_FD() != null) {
							cell11.setCellValue(record.getR41_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR41_19_24M_FD() != null) {
							cell12.setCellValue(record.getR41_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR41_OVER24_FD() != null) {
							cell13.setCellValue(record.getR41_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R42
						// ==========================

						row = sheet.getRow(42);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR42_CURRENT() != null) {
							cell2.setCellValue(record.getR42_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR42_CALL() != null) {
							cell3.setCellValue(record.getR42_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR42_SAVINGS() != null) {
							cell4.setCellValue(record.getR42_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR42_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR42_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR42_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR42_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR42_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR42_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR42 = (record.getR42_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR42_1_2M_FD())
								.add(record.getR42_4_6M_FD() == null ? BigDecimal.ZERO : record.getR42_4_6M_FD());

						if (totalR42.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR42.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR42_7_12M_FD() != null) {
							cell10.setCellValue(record.getR42_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR42_13_18M_FD() != null) {
							cell11.setCellValue(record.getR42_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR42_19_24M_FD() != null) {
							cell12.setCellValue(record.getR42_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR42_OVER24_FD() != null) {
							cell13.setCellValue(record.getR42_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R43
						// ==========================

						row = sheet.getRow(44);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR43_CURRENT() != null) {
							cell2.setCellValue(record.getR43_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR43_CALL() != null) {
							cell3.setCellValue(record.getR43_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR43_SAVINGS() != null) {
							cell4.setCellValue(record.getR43_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR43_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR43_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR43_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR43_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR43_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR43_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR43 = (record.getR43_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR43_1_2M_FD())
								.add(record.getR43_4_6M_FD() == null ? BigDecimal.ZERO : record.getR43_4_6M_FD());

						if (totalR43.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR43.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR43_7_12M_FD() != null) {
							cell10.setCellValue(record.getR43_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR43_13_18M_FD() != null) {
							cell11.setCellValue(record.getR43_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR43_19_24M_FD() != null) {
							cell12.setCellValue(record.getR43_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR43_OVER24_FD() != null) {
							cell13.setCellValue(record.getR43_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						
						row = sheet.getRow(45);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR48_CURRENT() != null) {
							cell2.setCellValue(record.getR48_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR48_CALL() != null) {
							cell3.setCellValue(record.getR48_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR48_SAVINGS() != null) {
							cell4.setCellValue(record.getR48_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR48_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR48_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR48_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR48_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR48_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR48_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR48 = (record.getR48_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR48_1_2M_FD())
								.add(record.getR48_4_6M_FD() == null ? BigDecimal.ZERO : record.getR48_4_6M_FD());

						if (totalR48.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR48.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR48_7_12M_FD() != null) {
							cell10.setCellValue(record.getR48_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR48_13_18M_FD() != null) {
							cell11.setCellValue(record.getR48_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR48_19_24M_FD() != null) {
							cell12.setCellValue(record.getR48_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR48_OVER24_FD() != null) {
							cell13.setCellValue(record.getR48_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R44
						// ==========================

						row = sheet.getRow(46);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR44_CURRENT() != null) {
							cell2.setCellValue(record.getR44_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR44_CALL() != null) {
							cell3.setCellValue(record.getR44_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR44_SAVINGS() != null) {
							cell4.setCellValue(record.getR44_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR44_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR44_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR44_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR44_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR44_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR44_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR44 = (record.getR44_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR44_1_2M_FD())
								.add(record.getR44_4_6M_FD() == null ? BigDecimal.ZERO : record.getR44_4_6M_FD());

						if (totalR44.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR44.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR44_7_12M_FD() != null) {
							cell10.setCellValue(record.getR44_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR44_13_18M_FD() != null) {
							cell11.setCellValue(record.getR44_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR44_19_24M_FD() != null) {
							cell12.setCellValue(record.getR44_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR44_OVER24_FD() != null) {
							cell13.setCellValue(record.getR44_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R45
						// ==========================

						row = sheet.getRow(47);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR45_CURRENT() != null) {
							cell2.setCellValue(record.getR45_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR45_CALL() != null) {
							cell3.setCellValue(record.getR45_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR45_SAVINGS() != null) {
							cell4.setCellValue(record.getR45_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR45_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR45_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR45_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR45_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR45_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR45_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR45 = (record.getR45_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR45_1_2M_FD())
								.add(record.getR45_4_6M_FD() == null ? BigDecimal.ZERO : record.getR45_4_6M_FD());

						if (totalR45.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR45.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR45_7_12M_FD() != null) {
							cell10.setCellValue(record.getR45_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR45_13_18M_FD() != null) {
							cell11.setCellValue(record.getR45_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR45_19_24M_FD() != null) {
							cell12.setCellValue(record.getR45_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR45_OVER24_FD() != null) {
							cell13.setCellValue(record.getR45_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
						// ==========================
						// R46
						// ==========================

						row = sheet.getRow(48);

						// Column2
						cell2 = row.createCell(3);
						if (record.getR46_CURRENT() != null) {
							cell2.setCellValue(record.getR46_CURRENT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// Column3
						cell3 = row.createCell(4);
						if (record.getR46_CALL() != null) {
							cell3.setCellValue(record.getR46_CALL().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// Column4
						cell4 = row.createCell(5);
						if (record.getR46_SAVINGS() != null) {
							cell4.setCellValue(record.getR46_SAVINGS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// Column5
						cell5 = row.createCell(6);
						if (record.getR46_0_31D_NOTICE() != null) {
							cell5.setCellValue(record.getR46_0_31D_NOTICE().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// Column6
						cell6 = row.createCell(7);
						if (record.getR46_32_88D_NOTICE() != null) {
							cell6.setCellValue(record.getR46_32_88D_NOTICE().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// Column7
						cell7 = row.createCell(8);
						if (record.getR46_91D_DEPOSIT() != null) {
							cell7.setCellValue(record.getR46_91D_DEPOSIT().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						// Column8 (1-2M + 4-6M)
						cell8 = row.createCell(9);
						BigDecimal totalR46 = (record.getR46_1_2M_FD() == null ? BigDecimal.ZERO
								: record.getR46_1_2M_FD())
								.add(record.getR46_4_6M_FD() == null ? BigDecimal.ZERO : record.getR46_4_6M_FD());

						if (totalR46.compareTo(BigDecimal.ZERO) != 0) {
							cell8.setCellValue(totalR46.doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						// Column10
						cell10 = row.createCell(10);
						if (record.getR46_7_12M_FD() != null) {
							cell10.setCellValue(record.getR46_7_12M_FD().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						// Column11
						cell11 = row.createCell(11);
						if (record.getR46_13_18M_FD() != null) {
							cell11.setCellValue(record.getR46_13_18M_FD().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						// Column12
						cell12 = row.createCell(12);
						if (record.getR46_19_24M_FD() != null) {
							cell12.setCellValue(record.getR46_19_24M_FD().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						// Column13
						cell13 = row.createCell(13);
						if (record.getR46_OVER24_FD() != null) {
							cell13.setCellValue(record.getR46_OVER24_FD().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}
//						// ==========================
//						// R47
//						// ==========================
//
//						row = sheet.getRow(48);
//
//						// Column2
//						cell2 = row.createCell(3);
//						if (record.getR47_CURRENT() != null) {
//							cell2.setCellValue(record.getR47_CURRENT().doubleValue());
//							cell2.setCellStyle(numberStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}
//
//						// Column3
//						cell3 = row.createCell(4);
//						if (record.getR47_CALL() != null) {
//							cell3.setCellValue(record.getR47_CALL().doubleValue());
//							cell3.setCellStyle(numberStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}
//
//						// Column4
//						cell4 = row.createCell(5);
//						if (record.getR47_SAVINGS() != null) {
//							cell4.setCellValue(record.getR47_SAVINGS().doubleValue());
//							cell4.setCellStyle(numberStyle);
//						} else {
//							cell4.setCellValue("");
//							cell4.setCellStyle(textStyle);
//						}
//
//						// Column5
//						cell5 = row.createCell(6);
//						if (record.getR47_0_31D_NOTICE() != null) {
//							cell5.setCellValue(record.getR47_0_31D_NOTICE().doubleValue());
//							cell5.setCellStyle(numberStyle);
//						} else {
//							cell5.setCellValue("");
//							cell5.setCellStyle(textStyle);
//						}
//
//						// Column6
//						cell6 = row.createCell(7);
//						if (record.getR47_32_88D_NOTICE() != null) {
//							cell6.setCellValue(record.getR47_32_88D_NOTICE().doubleValue());
//							cell6.setCellStyle(numberStyle);
//						} else {
//							cell6.setCellValue("");
//							cell6.setCellStyle(textStyle);
//						}
//
//						// Column7
//						cell7 = row.createCell(8);
//						if (record.getR47_91D_DEPOSIT() != null) {
//							cell7.setCellValue(record.getR47_91D_DEPOSIT().doubleValue());
//							cell7.setCellStyle(numberStyle);
//						} else {
//							cell7.setCellValue("");
//							cell7.setCellStyle(textStyle);
//						}
//
//						// Column8 (1-2M + 4-6M)
//						cell8 = row.createCell(9);
//						BigDecimal totalR47 = (record.getR47_1_2M_FD() == null ? BigDecimal.ZERO
//								: record.getR47_1_2M_FD())
//								.add(record.getR47_4_6M_FD() == null ? BigDecimal.ZERO : record.getR47_4_6M_FD());
//
//						if (totalR47.compareTo(BigDecimal.ZERO) != 0) {
//							cell8.setCellValue(totalR47.doubleValue());
//							cell8.setCellStyle(numberStyle);
//						} else {
//							cell8.setCellValue("");
//							cell8.setCellStyle(textStyle);
//						}
//
//						// Column10
//						cell10 = row.createCell(10);
//						if (record.getR47_7_12M_FD() != null) {
//							cell10.setCellValue(record.getR47_7_12M_FD().doubleValue());
//							cell10.setCellStyle(numberStyle);
//						} else {
//							cell10.setCellValue("");
//							cell10.setCellStyle(textStyle);
//						}
//
//						// Column11
//						cell11 = row.createCell(11);
//						if (record.getR47_13_18M_FD() != null) {
//							cell11.setCellValue(record.getR47_13_18M_FD().doubleValue());
//							cell11.setCellStyle(numberStyle);
//						} else {
//							cell11.setCellValue("");
//							cell11.setCellStyle(textStyle);
//						}
//
//						// Column12
//						cell12 = row.createCell(12);
//						if (record.getR47_19_24M_FD() != null) {
//							cell12.setCellValue(record.getR47_19_24M_FD().doubleValue());
//							cell12.setCellStyle(numberStyle);
//						} else {
//							cell12.setCellValue("");
//							cell12.setCellStyle(textStyle);
//						}
//
//						// Column13
//						cell13 = row.createCell(13);
//						if (record.getR47_OVER24_FD() != null) {
//							cell13.setCellValue(record.getR47_OVER24_FD().doubleValue());
//							cell13.setCellStyle(numberStyle);
//						} else {
//							cell13.setCellValue("");
//							cell13.setCellStyle(textStyle);
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