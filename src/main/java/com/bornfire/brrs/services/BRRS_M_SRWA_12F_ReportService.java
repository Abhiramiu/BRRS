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

import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Summary_Repo;
import com.bornfire.brrs.entities.M_SRWA_12F_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Archival_Summary_Entity;

@Component
@Service

public class BRRS_M_SRWA_12F_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12F_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SRWA_12F_Summary_Repo brrs_M_SRWA_12F_summary_repo;

	@Autowired
	BRRS_M_SRWA_12F_Detail_Repo brrs_M_SRWA_12F_detail_repo;

	@Autowired
	BRRS_M_SRWA_12F_Archival_Summary_Repo M_SRWA_12F_Archival_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12F_Archival_Detail_Repo BRRS_M_SRWA_12F_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SRWA_12F_Resub_Summary_Repo brrs_M_SRWA_12F_resub_summary_repo;

	@Autowired
	BRRS_M_SRWA_12F_Resub_Detail_Repo brrs_M_SRWA_12F_resub_detail_repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SRWA12FView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

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

				List<M_SRWA_12F_Archival_Summary_Entity> T1Master = M_SRWA_12F_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);

				System.out.println("Archival Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- RESUB SUMMARY ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_SRWA_12F_Resub_Summary_Entity> T1Master = brrs_M_SRWA_12F_resub_summary_repo
						.getdatabydateListarchival(d1, version);

				System.out.println("Resub Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- NORMAL SUMMARY ----------
			else {

				List<M_SRWA_12F_Summary_Entity> T1Master = brrs_M_SRWA_12F_summary_repo.getdatabydateList(d1);

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

					List<M_SRWA_12F_Archival_Detail_Entity> T1Master = BRRS_M_SRWA_12F_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Archival Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}

				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12F_Resub_Detail_Entity> T1Master = brrs_M_SRWA_12F_resub_detail_repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}

				// ---------- NORMAL DETAIL ----------
				else {

					List<M_SRWA_12F_Detail_Entity> T1Master = brrs_M_SRWA_12F_detail_repo.getdatabydateList(d1);

					System.out.println("Normal Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12F");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	@Transactional
	public void updateReport(M_SRWA_12F_Summary_Entity updatedEntity) {

		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// 🔹 Fetch existing SUMMARY
		M_SRWA_12F_Summary_Entity existingSummary = brrs_M_SRWA_12F_summary_repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		// 🔹 Fetch or create DETAIL
		M_SRWA_12F_Detail_Entity detailEntity = brrs_M_SRWA_12F_detail_repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					M_SRWA_12F_Detail_Entity d = new M_SRWA_12F_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {
			// 🔁 Loop R11 to R37
			for (int i = 11; i <= 37; i++) {

				String prefix = "R" + i + "_";
				String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT",
						"RISK_WEIGHT", "RISK_WEIGHTED_AMT" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_SRWA_12F_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_SRWA_12F_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_SRWA_12F_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());

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
		brrs_M_SRWA_12F_summary_repo.save(existingSummary);
		brrs_M_SRWA_12F_detail_repo.save(detailEntity);

		System.out.println("Update completed successfully");
	}

	@Transactional
	public void updateResubReport(M_SRWA_12F_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = brrs_M_SRWA_12F_resub_summary_repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_SRWA_12F_Resub_Summary_Entity resubSummary = new M_SRWA_12F_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_SRWA_12F_Resub_Detail_Entity resubDetail = new M_SRWA_12F_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12F_Archival_Summary_Entity archSummary = new M_SRWA_12F_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12F_Archival_Detail_Entity archDetail = new M_SRWA_12F_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		brrs_M_SRWA_12F_resub_summary_repo.save(resubSummary);
		brrs_M_SRWA_12F_resub_detail_repo.save(resubDetail);

		M_SRWA_12F_Archival_Summary_Repo.save(archSummary);
		BRRS_M_SRWA_12F_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_SRWA_12FResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_SRWA_12F_Archival_Summary_Entity> latestArchivalList = M_SRWA_12F_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SRWA_12F_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12F Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getM_SRWA_12FArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SRWA_12F_Archival_Summary_Entity> repoData = M_SRWA_12F_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12F_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12F_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12F Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getM_SRWA_12FExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_SRWA_12FARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12FResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SRWA_12FEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				List<M_SRWA_12F_Summary_Entity> dataList = brrs_M_SRWA_12F_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SRWA_12F report. Returning empty result.");
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
							M_SRWA_12F_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							Cell cell1 = row.createCell(1);
							if (record.getR11_NAME_OF_CORPORATE() != null) {
								cell1.setCellValue(record.getR11_NAME_OF_CORPORATE());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR11_CREDIT_RATING() != null) {
								cell2.setCellValue(record.getR11_CREDIT_RATING());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR11_RATING_AGENCY() != null) {
								cell3.setCellValue(record.getR11_RATING_AGENCY());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// row11
							// Column E
							Cell cell4 = row.createCell(4);
							if (record.getR11_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR11_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row11
							// Column F
							Cell cell5 = row.createCell(5);
							if (record.getR11_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR11_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);

							cell1 = row.createCell(1);
							if (record.getR12_NAME_OF_CORPORATE() != null) {
								cell1.setCellValue(record.getR12_NAME_OF_CORPORATE());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CREDIT_RATING() != null) {
								cell2.setCellValue(record.getR12_CREDIT_RATING());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_RATING_AGENCY() != null) {
								cell3.setCellValue(record.getR12_RATING_AGENCY());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR12_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR12_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row12
							// Column F
							cell5 = row.createCell(5);
							if (record.getR12_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR12_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);
							// R13_NAME_OF_CORPORATE
							cell1 = row.createCell(1);
							if (record.getR13_NAME_OF_CORPORATE() != null) {
								cell1.setCellValue(record.getR13_NAME_OF_CORPORATE());
							} else {
								cell1.setCellValue("");
							}
							cell1.setCellStyle(textStyle);

							// R13_CREDIT_RATING
							cell2 = row.createCell(2);
							if (record.getR13_CREDIT_RATING() != null) {
								cell2.setCellValue(record.getR13_CREDIT_RATING());
							} else {
								cell2.setCellValue("");
							}
							cell2.setCellStyle(textStyle);

							// R13_RATING_AGENCY
							cell3 = row.createCell(3);
							if (record.getR13_RATING_AGENCY() != null) {
								cell3.setCellValue(record.getR13_RATING_AGENCY());
							} else {
								cell3.setCellValue("");
							}
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR13_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR13_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row13
							// Column F
							cell5 = row.createCell(5);
							if (record.getR13_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR13_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);
							// R14_NAME_OF_CORPORATE
							cell1 = row.createCell(1);
							if (record.getR14_NAME_OF_CORPORATE() != null) {
								cell1.setCellValue(record.getR14_NAME_OF_CORPORATE());
							} else {
								cell1.setCellValue("");
							}
							cell1.setCellStyle(textStyle);

							// R14_CREDIT_RATING
							cell2 = row.createCell(2);
							if (record.getR14_CREDIT_RATING() != null) {
								cell2.setCellValue(record.getR14_CREDIT_RATING());
							} else {
								cell2.setCellValue("");
							}
							cell2.setCellStyle(textStyle);

							// R14_RATING_AGENCY
							cell3 = row.createCell(3);
							if (record.getR14_RATING_AGENCY() != null) {
								cell3.setCellValue(record.getR14_RATING_AGENCY());
							} else {
								cell3.setCellValue("");
							}
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR14_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR14_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row14
							// Column F
							cell5 = row.createCell(5);
							if (record.getR14_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR14_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row15
							row = sheet.getRow(14);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR15_NAME_OF_CORPORATE() != null ? record.getR15_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR15_CREDIT_RATING() != null ? record.getR15_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR15_RATING_AGENCY() != null ? record.getR15_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR15_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR15_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row15
							// Column F
							cell5 = row.createCell(5);
							if (record.getR15_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR15_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(15);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR16_NAME_OF_CORPORATE() != null ? record.getR16_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR16_CREDIT_RATING() != null ? record.getR16_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR16_RATING_AGENCY() != null ? record.getR16_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR16_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR16_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row16
							// Column F
							cell5 = row.createCell(5);
							if (record.getR16_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR16_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR17_NAME_OF_CORPORATE() != null ? record.getR17_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR17_CREDIT_RATING() != null ? record.getR17_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR17_RATING_AGENCY() != null ? record.getR17_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR17_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR17_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row17
							// Column F
							cell5 = row.createCell(5);
							if (record.getR17_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR17_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);
							// R18 Cells
							cell1 = row.createCell(1); // adjust column index as needed
							cell1.setCellValue(
									record.getR18_NAME_OF_CORPORATE() != null ? record.getR18_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR18_CREDIT_RATING() != null ? record.getR18_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR18_RATING_AGENCY() != null ? record.getR18_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR18_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR18_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row18
							// Column F
							cell5 = row.createCell(5);
							if (record.getR18_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR18_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR19_NAME_OF_CORPORATE() != null ? record.getR19_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR19_CREDIT_RATING() != null ? record.getR19_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR19_RATING_AGENCY() != null ? record.getR19_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR19_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR19_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row19
							// Column F
							cell5 = row.createCell(5);
							if (record.getR19_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR19_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR20_NAME_OF_CORPORATE() != null ? record.getR20_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR20_CREDIT_RATING() != null ? record.getR20_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR20_RATING_AGENCY() != null ? record.getR20_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR20_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR20_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row20
							// Column F
							cell5 = row.createCell(5);
							if (record.getR20_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR20_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row21
							row = sheet.getRow(20);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR21_NAME_OF_CORPORATE() != null ? record.getR21_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR21_CREDIT_RATING() != null ? record.getR21_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR21_RATING_AGENCY() != null ? record.getR21_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR21_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR21_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row21
							// Column F
							cell5 = row.createCell(5);
							if (record.getR21_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR21_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR22_NAME_OF_CORPORATE() != null ? record.getR22_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR22_CREDIT_RATING() != null ? record.getR22_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR22_RATING_AGENCY() != null ? record.getR22_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR22_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR22_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row22
							// Column F
							cell5 = row.createCell(5);
							if (record.getR22_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR22_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR23_NAME_OF_CORPORATE() != null ? record.getR23_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR23_CREDIT_RATING() != null ? record.getR23_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR23_RATING_AGENCY() != null ? record.getR23_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR23_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR23_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row23
							// Column F
							cell5 = row.createCell(5);
							if (record.getR23_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR23_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR24_NAME_OF_CORPORATE() != null ? record.getR24_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR24_CREDIT_RATING() != null ? record.getR24_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR24_RATING_AGENCY() != null ? record.getR24_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR24_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR24_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row24
							// Column F
							cell5 = row.createCell(5);
							if (record.getR24_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR24_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR25_NAME_OF_CORPORATE() != null ? record.getR25_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR25_CREDIT_RATING() != null ? record.getR25_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR25_RATING_AGENCY() != null ? record.getR25_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR25_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR25_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row25
							// Column F
							cell5 = row.createCell(5);
							if (record.getR25_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR25_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR26_NAME_OF_CORPORATE() != null ? record.getR26_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR26_CREDIT_RATING() != null ? record.getR26_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR26_RATING_AGENCY() != null ? record.getR26_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR26_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR26_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row26
							// Column F
							cell5 = row.createCell(5);
							if (record.getR26_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR26_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR27_NAME_OF_CORPORATE() != null ? record.getR27_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR27_CREDIT_RATING() != null ? record.getR27_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR27_RATING_AGENCY() != null ? record.getR27_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR27_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR27_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row27
							// Column F
							cell5 = row.createCell(5);
							if (record.getR27_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR27_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row28
							row = sheet.getRow(27);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR28_NAME_OF_CORPORATE() != null ? record.getR28_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR28_CREDIT_RATING() != null ? record.getR28_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR28_RATING_AGENCY() != null ? record.getR28_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR28_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR28_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row28
							// Column F
							cell5 = row.createCell(5);
							if (record.getR28_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR28_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row29
							row = sheet.getRow(28);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR29_NAME_OF_CORPORATE() != null ? record.getR29_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR29_CREDIT_RATING() != null ? record.getR29_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR29_RATING_AGENCY() != null ? record.getR29_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR29_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR29_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row29
							// Column F
							cell5 = row.createCell(5);
							if (record.getR29_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR29_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row30
							row = sheet.getRow(29);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR30_NAME_OF_CORPORATE() != null ? record.getR30_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR30_CREDIT_RATING() != null ? record.getR30_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR30_RATING_AGENCY() != null ? record.getR30_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR30_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR30_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row30
							// Column F
							cell5 = row.createCell(5);
							if (record.getR30_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR30_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row31
							row = sheet.getRow(30);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR31_NAME_OF_CORPORATE() != null ? record.getR31_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR31_CREDIT_RATING() != null ? record.getR31_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR31_RATING_AGENCY() != null ? record.getR31_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR31_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR31_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row31
							// Column F
							cell5 = row.createCell(5);
							if (record.getR31_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR31_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row32
							row = sheet.getRow(31);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR32_NAME_OF_CORPORATE() != null ? record.getR32_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR32_CREDIT_RATING() != null ? record.getR32_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR32_RATING_AGENCY() != null ? record.getR32_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR32_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR32_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row32
							// Column F
							cell5 = row.createCell(5);
							if (record.getR32_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR32_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row33
							row = sheet.getRow(32);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR33_NAME_OF_CORPORATE() != null ? record.getR33_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR33_CREDIT_RATING() != null ? record.getR33_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR33_RATING_AGENCY() != null ? record.getR33_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR33_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR33_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row33
							// Column F
							cell5 = row.createCell(5);
							if (record.getR33_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR33_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row34
							row = sheet.getRow(33);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR34_NAME_OF_CORPORATE() != null ? record.getR34_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR34_CREDIT_RATING() != null ? record.getR34_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR34_RATING_AGENCY() != null ? record.getR34_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR34_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR34_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row34
							// Column F
							cell5 = row.createCell(5);
							if (record.getR34_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR34_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row35
							row = sheet.getRow(34);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR35_NAME_OF_CORPORATE() != null ? record.getR35_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR35_CREDIT_RATING() != null ? record.getR35_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR35_RATING_AGENCY() != null ? record.getR35_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR35_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR35_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							cell5 = row.createCell(5);
							if (record.getR35_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR35_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row35
							// Column F
							row = sheet.getRow(35);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR36_NAME_OF_CORPORATE() != null ? record.getR36_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR36_CREDIT_RATING() != null ? record.getR36_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR36_RATING_AGENCY() != null ? record.getR36_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR36_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR36_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row36
							// Column F
							cell5 = row.createCell(5);
							if (record.getR36_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR36_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SRWA_12FEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12FEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12FEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_SRWA_12F_Summary_Entity> dataList = brrs_M_SRWA_12F_summary_repo
					.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_SRWA_12F report. Returning empty result.");
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
						M_SRWA_12F_Summary_Entity record1 = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

// row11
						// Column E
						Cell cell1 = row.createCell(1);
						if (record1.getR11_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR11_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record1.getR11_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR11_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(4);
						if (record1.getR11_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR11_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						// row11
						// Column E
						Cell cell4 = row.createCell(6);
						if (record1.getR11_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row11
						// Column F
						Cell cell5 = row.createCell(7);
						if (record1.getR11_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}
						// row11
						// Column F
						Cell cell6 = row.createCell(8);
						if (record1.getR11_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR11_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);

						cell1 = row.createCell(1);
						if (record1.getR12_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR12_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR12_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR12_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR12_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR12_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						// Column E
						cell4 = row.createCell(6);
						if (record1.getR12_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row12
						// Column F
						cell5 = row.createCell(7);
						if (record1.getR12_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}
						cell6 = row.createCell(8);
						if (record1.getR12_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR12_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);

						cell1 = row.createCell(1);
						if (record1.getR13_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR13_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR13_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR13_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR13_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR13_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR13_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR13_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR13_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR13_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row14
						row = sheet.getRow(13);

						cell1 = row.createCell(1);
						if (record1.getR14_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR14_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR14_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR14_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR14_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR14_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR14_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR14_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR14_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR14_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row15
						row = sheet.getRow(14);

						cell1 = row.createCell(1);
						if (record1.getR15_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR15_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR15_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR15_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR15_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR15_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR15_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR15_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR15_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR15_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);

						cell1 = row.createCell(1);
						if (record1.getR16_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR16_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR16_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR16_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR16_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR16_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR16_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR16_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR16_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR16_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);

						cell1 = row.createCell(1);
						if (record1.getR17_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR17_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR17_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR17_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR17_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR17_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR17_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR17_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR17_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR17_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);

						cell1 = row.createCell(1);
						if (record1.getR18_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR18_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR18_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR18_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR18_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR18_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR18_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR18_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR18_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR18_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row19
						row = sheet.getRow(18);

						cell1 = row.createCell(1);
						if (record1.getR19_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR19_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR19_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR19_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR19_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR19_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR19_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR19_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR19_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR19_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);

						cell1 = row.createCell(1);
						if (record1.getR20_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR20_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR20_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR20_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR20_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR20_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR20_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR20_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR20_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR20_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row21
						row = sheet.getRow(20);

						cell1 = row.createCell(1);
						if (record1.getR21_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR21_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR21_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR21_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR21_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR21_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR21_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR21_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR21_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR21_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// ==========================
						// R22
						// ==========================
						row = sheet.getRow(21);

						cell1 = row.createCell(1);
						if (record1.getR22_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR22_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR22_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR22_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR22_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR22_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR22_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR22_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR22_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR22_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// ==========================
						// R23
						// ==========================
						row = sheet.getRow(22);

						cell1 = row.createCell(1);
						if (record1.getR23_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR23_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR23_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR23_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR23_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR23_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR23_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR23_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR23_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR23_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// ==========================
						// R24
						// ==========================
						row = sheet.getRow(23);

						cell1 = row.createCell(1);
						if (record1.getR24_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR24_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR24_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR24_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR24_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR24_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR24_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR24_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR24_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR24_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}
						// row25
						row = sheet.getRow(24);

						cell1 = row.createCell(1);
						if (record1.getR25_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR25_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR25_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR25_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR25_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR25_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR25_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR25_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR25_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR25_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row26
						row = sheet.getRow(25);

						cell1 = row.createCell(1);
						if (record1.getR26_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR26_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR26_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR26_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR26_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR26_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR26_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR26_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR26_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR26_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);

						cell1 = row.createCell(1);
						if (record1.getR27_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR27_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR27_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR27_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR27_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR27_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR27_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR27_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR27_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR27_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row28
						row = sheet.getRow(27);

						cell1 = row.createCell(1);
						if (record1.getR28_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR28_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR28_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR28_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR28_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR28_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR28_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR28_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR28_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR28_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row29
						row = sheet.getRow(28);

						cell1 = row.createCell(1);
						if (record1.getR29_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR29_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR29_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR29_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR29_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR29_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR29_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR29_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR29_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR29_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row30
						row = sheet.getRow(29);

						cell1 = row.createCell(1);
						if (record1.getR30_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR30_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR30_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR30_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR30_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR30_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR30_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR30_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR30_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR30_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row31
						row = sheet.getRow(30);

						cell1 = row.createCell(1);
						if (record1.getR31_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR31_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR31_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR31_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR31_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR31_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR31_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR31_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR31_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR31_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row32
						row = sheet.getRow(31);

						cell1 = row.createCell(1);
						if (record1.getR32_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR32_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR32_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR32_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR32_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR32_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR32_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR32_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR32_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR32_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row33
						row = sheet.getRow(32);

						cell1 = row.createCell(1);
						if (record1.getR33_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR33_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR33_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR33_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR33_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR33_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR33_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR33_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR33_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR33_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row34
						row = sheet.getRow(33);

						cell1 = row.createCell(1);
						if (record1.getR34_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR34_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR34_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR34_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR34_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR34_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR34_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR34_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR34_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR34_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row35
						row = sheet.getRow(34);

						cell1 = row.createCell(1);
						if (record1.getR35_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR35_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR35_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR35_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR35_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR35_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR35_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR35_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR35_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR35_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row36
						row = sheet.getRow(35);

						cell1 = row.createCell(1);
						if (record1.getR36_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR36_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR36_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR36_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR36_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR36_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR36_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR36_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR36_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR36_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row37
						row = sheet.getRow(36);

						cell1 = row.createCell(1);
						if (record1.getR37_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR37_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR37_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR37_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR37_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR37_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR37_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR37_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR37_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR37_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR37_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR37_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
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
	public byte[] getExcelM_SRWA_12FARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12FEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SRWA_12F_Archival_Summary_Entity> dataList = M_SRWA_12F_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12F report. Returning empty result.");
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
					M_SRWA_12F_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cell1 = row.createCell(1);
					if (record.getR11_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR11_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record.getR11_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR11_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR11_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR11_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell1 = row.createCell(1);
					if (record.getR12_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR12_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR12_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR12_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// R13_NAME_OF_CORPORATE
					cell1 = row.createCell(1);
					if (record.getR13_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR13_NAME_OF_CORPORATE());
					} else {
						cell1.setCellValue("");
					}
					cell1.setCellStyle(textStyle);

					// R13_CREDIT_RATING
					cell2 = row.createCell(2);
					if (record.getR13_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR13_CREDIT_RATING());
					} else {
						cell2.setCellValue("");
					}
					cell2.setCellStyle(textStyle);

					// R13_RATING_AGENCY
					cell3 = row.createCell(3);
					if (record.getR13_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR13_RATING_AGENCY());
					} else {
						cell3.setCellValue("");
					}
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// R14_NAME_OF_CORPORATE
					cell1 = row.createCell(1);
					if (record.getR14_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR14_NAME_OF_CORPORATE());
					} else {
						cell1.setCellValue("");
					}
					cell1.setCellStyle(textStyle);

					// R14_CREDIT_RATING
					cell2 = row.createCell(2);
					if (record.getR14_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR14_CREDIT_RATING());
					} else {
						cell2.setCellValue("");
					}
					cell2.setCellStyle(textStyle);

					// R14_RATING_AGENCY
					cell3 = row.createCell(3);
					if (record.getR14_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR14_RATING_AGENCY());
					} else {
						cell3.setCellValue("");
					}
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR15_NAME_OF_CORPORATE() != null ? record.getR15_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR15_CREDIT_RATING() != null ? record.getR15_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR15_RATING_AGENCY() != null ? record.getR15_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR16_NAME_OF_CORPORATE() != null ? record.getR16_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR16_CREDIT_RATING() != null ? record.getR16_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR16_RATING_AGENCY() != null ? record.getR16_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR17_NAME_OF_CORPORATE() != null ? record.getR17_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR17_CREDIT_RATING() != null ? record.getR17_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR17_RATING_AGENCY() != null ? record.getR17_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// R18 Cells
					cell1 = row.createCell(1); // adjust column index as needed
					cell1.setCellValue(
							record.getR18_NAME_OF_CORPORATE() != null ? record.getR18_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR18_CREDIT_RATING() != null ? record.getR18_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR18_RATING_AGENCY() != null ? record.getR18_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR19_NAME_OF_CORPORATE() != null ? record.getR19_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR19_CREDIT_RATING() != null ? record.getR19_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR19_RATING_AGENCY() != null ? record.getR19_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR20_NAME_OF_CORPORATE() != null ? record.getR20_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR20_CREDIT_RATING() != null ? record.getR20_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR20_RATING_AGENCY() != null ? record.getR20_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR21_NAME_OF_CORPORATE() != null ? record.getR21_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR21_CREDIT_RATING() != null ? record.getR21_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR21_RATING_AGENCY() != null ? record.getR21_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR22_NAME_OF_CORPORATE() != null ? record.getR22_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR22_CREDIT_RATING() != null ? record.getR22_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR22_RATING_AGENCY() != null ? record.getR22_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR23_NAME_OF_CORPORATE() != null ? record.getR23_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR23_CREDIT_RATING() != null ? record.getR23_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR23_RATING_AGENCY() != null ? record.getR23_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR24_NAME_OF_CORPORATE() != null ? record.getR24_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR24_CREDIT_RATING() != null ? record.getR24_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR24_RATING_AGENCY() != null ? record.getR24_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR25_NAME_OF_CORPORATE() != null ? record.getR25_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR25_CREDIT_RATING() != null ? record.getR25_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR25_RATING_AGENCY() != null ? record.getR25_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR26_NAME_OF_CORPORATE() != null ? record.getR26_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR26_CREDIT_RATING() != null ? record.getR26_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR26_RATING_AGENCY() != null ? record.getR26_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR27_NAME_OF_CORPORATE() != null ? record.getR27_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR27_CREDIT_RATING() != null ? record.getR27_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR27_RATING_AGENCY() != null ? record.getR27_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR28_NAME_OF_CORPORATE() != null ? record.getR28_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR28_CREDIT_RATING() != null ? record.getR28_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR28_RATING_AGENCY() != null ? record.getR28_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR29_NAME_OF_CORPORATE() != null ? record.getR29_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR29_CREDIT_RATING() != null ? record.getR29_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR29_RATING_AGENCY() != null ? record.getR29_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR30_NAME_OF_CORPORATE() != null ? record.getR30_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR30_CREDIT_RATING() != null ? record.getR30_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR30_RATING_AGENCY() != null ? record.getR30_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR31_NAME_OF_CORPORATE() != null ? record.getR31_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR31_CREDIT_RATING() != null ? record.getR31_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR31_RATING_AGENCY() != null ? record.getR31_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR32_NAME_OF_CORPORATE() != null ? record.getR32_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR32_CREDIT_RATING() != null ? record.getR32_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR32_RATING_AGENCY() != null ? record.getR32_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR33_NAME_OF_CORPORATE() != null ? record.getR33_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR33_CREDIT_RATING() != null ? record.getR33_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR33_RATING_AGENCY() != null ? record.getR33_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR34_NAME_OF_CORPORATE() != null ? record.getR34_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR34_CREDIT_RATING() != null ? record.getR34_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR34_RATING_AGENCY() != null ? record.getR34_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR35_NAME_OF_CORPORATE() != null ? record.getR35_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR35_CREDIT_RATING() != null ? record.getR35_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR35_RATING_AGENCY() != null ? record.getR35_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					// Column F
					row = sheet.getRow(35);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR36_NAME_OF_CORPORATE() != null ? record.getR36_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR36_CREDIT_RATING() != null ? record.getR36_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR36_RATING_AGENCY() != null ? record.getR36_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SRWA_12FEmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SRWA_12F_Archival_Summary_Entity> dataList = M_SRWA_12F_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12F report. Returning empty result.");
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
					M_SRWA_12F_Archival_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row11
					// Column E
					Cell cell1 = row.createCell(1);
					if (record1.getR11_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR11_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record1.getR11_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR11_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(4);
					if (record1.getR11_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR11_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row11
					// Column E
					Cell cell4 = row.createCell(6);
					if (record1.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(7);
					if (record1.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// row11
					// Column F
					Cell cell6 = row.createCell(8);
					if (record1.getR11_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR11_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell1 = row.createCell(1);
					if (record1.getR12_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR12_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR12_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR12_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR12_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR12_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(6);
					if (record1.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(7);
					if (record1.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					cell6 = row.createCell(8);
					if (record1.getR12_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR12_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell1 = row.createCell(1);
					if (record1.getR13_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR13_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR13_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR13_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR13_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR13_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR13_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR13_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell1 = row.createCell(1);
					if (record1.getR14_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR14_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR14_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR14_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR14_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR14_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR14_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR14_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					cell1 = row.createCell(1);
					if (record1.getR15_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR15_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR15_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR15_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR15_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR15_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR15_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR15_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell1 = row.createCell(1);
					if (record1.getR16_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR16_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR16_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR16_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR16_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR16_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR16_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR16_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					cell1 = row.createCell(1);
					if (record1.getR17_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR17_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR17_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR17_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR17_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR17_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR17_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR17_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell1 = row.createCell(1);
					if (record1.getR18_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR18_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR18_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR18_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR18_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR18_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR18_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR18_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					cell1 = row.createCell(1);
					if (record1.getR19_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR19_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR19_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR19_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR19_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR19_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR19_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR19_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell1 = row.createCell(1);
					if (record1.getR20_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR20_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR20_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR20_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR20_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR20_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR20_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR20_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					cell1 = row.createCell(1);
					if (record1.getR21_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR21_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR21_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR21_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR21_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR21_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR21_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR21_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R22
					// ==========================
					row = sheet.getRow(21);

					cell1 = row.createCell(1);
					if (record1.getR22_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR22_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR22_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR22_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR22_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR22_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR22_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR22_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R23
					// ==========================
					row = sheet.getRow(22);

					cell1 = row.createCell(1);
					if (record1.getR23_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR23_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR23_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR23_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR23_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR23_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR23_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR23_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R24
					// ==========================
					row = sheet.getRow(23);

					cell1 = row.createCell(1);
					if (record1.getR24_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR24_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR24_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR24_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR24_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR24_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR24_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR24_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell1 = row.createCell(1);
					if (record1.getR25_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR25_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR25_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR25_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR25_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR25_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR25_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR25_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell1 = row.createCell(1);
					if (record1.getR26_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR26_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR26_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR26_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR26_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR26_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR26_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR26_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell1 = row.createCell(1);
					if (record1.getR27_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR27_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR27_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR27_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR27_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR27_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR27_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR27_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cell1 = row.createCell(1);
					if (record1.getR28_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR28_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR28_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR28_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR28_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR28_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR28_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR28_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell1 = row.createCell(1);
					if (record1.getR29_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR29_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR29_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR29_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR29_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR29_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR29_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR29_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell1 = row.createCell(1);
					if (record1.getR30_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR30_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR30_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR30_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR30_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR30_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR30_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR30_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					cell1 = row.createCell(1);
					if (record1.getR31_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR31_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR31_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR31_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR31_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR31_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR31_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR31_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell1 = row.createCell(1);
					if (record1.getR32_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR32_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR32_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR32_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR32_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR32_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR32_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR32_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell1 = row.createCell(1);
					if (record1.getR33_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR33_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR33_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR33_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR33_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR33_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR33_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR33_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					cell1 = row.createCell(1);
					if (record1.getR34_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR34_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR34_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR34_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR34_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR34_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR34_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR34_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell1 = row.createCell(1);
					if (record1.getR35_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR35_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR35_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR35_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR35_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR35_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR35_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR35_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell1 = row.createCell(1);
					if (record1.getR36_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR36_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR36_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR36_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR36_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR36_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR36_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR36_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);

					cell1 = row.createCell(1);
					if (record1.getR37_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR37_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR37_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR37_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR37_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR37_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR37_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR37_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR37_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR37_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR37_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR37_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SRWA_12FResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12FEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SRWA_12F_Resub_Summary_Entity> dataList = brrs_M_SRWA_12F_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12F report. Returning empty result.");
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

					M_SRWA_12F_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.createCell(1);
					if (record.getR11_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR11_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record.getR11_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR11_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR11_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR11_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell1 = row.createCell(1);
					if (record.getR12_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR12_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR12_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR12_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// R13_NAME_OF_CORPORATE
					cell1 = row.createCell(1);
					if (record.getR13_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR13_NAME_OF_CORPORATE());
					} else {
						cell1.setCellValue("");
					}
					cell1.setCellStyle(textStyle);

					// R13_CREDIT_RATING
					cell2 = row.createCell(2);
					if (record.getR13_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR13_CREDIT_RATING());
					} else {
						cell2.setCellValue("");
					}
					cell2.setCellStyle(textStyle);

					// R13_RATING_AGENCY
					cell3 = row.createCell(3);
					if (record.getR13_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR13_RATING_AGENCY());
					} else {
						cell3.setCellValue("");
					}
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// R14_NAME_OF_CORPORATE
					cell1 = row.createCell(1);
					if (record.getR14_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR14_NAME_OF_CORPORATE());
					} else {
						cell1.setCellValue("");
					}
					cell1.setCellStyle(textStyle);

					// R14_CREDIT_RATING
					cell2 = row.createCell(2);
					if (record.getR14_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR14_CREDIT_RATING());
					} else {
						cell2.setCellValue("");
					}
					cell2.setCellStyle(textStyle);

					// R14_RATING_AGENCY
					cell3 = row.createCell(3);
					if (record.getR14_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR14_RATING_AGENCY());
					} else {
						cell3.setCellValue("");
					}
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR15_NAME_OF_CORPORATE() != null ? record.getR15_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR15_CREDIT_RATING() != null ? record.getR15_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR15_RATING_AGENCY() != null ? record.getR15_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR16_NAME_OF_CORPORATE() != null ? record.getR16_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR16_CREDIT_RATING() != null ? record.getR16_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR16_RATING_AGENCY() != null ? record.getR16_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR17_NAME_OF_CORPORATE() != null ? record.getR17_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR17_CREDIT_RATING() != null ? record.getR17_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR17_RATING_AGENCY() != null ? record.getR17_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// R18 Cells
					cell1 = row.createCell(1); // adjust column index as needed
					cell1.setCellValue(
							record.getR18_NAME_OF_CORPORATE() != null ? record.getR18_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR18_CREDIT_RATING() != null ? record.getR18_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR18_RATING_AGENCY() != null ? record.getR18_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR19_NAME_OF_CORPORATE() != null ? record.getR19_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR19_CREDIT_RATING() != null ? record.getR19_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR19_RATING_AGENCY() != null ? record.getR19_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR20_NAME_OF_CORPORATE() != null ? record.getR20_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR20_CREDIT_RATING() != null ? record.getR20_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR20_RATING_AGENCY() != null ? record.getR20_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR21_NAME_OF_CORPORATE() != null ? record.getR21_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR21_CREDIT_RATING() != null ? record.getR21_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR21_RATING_AGENCY() != null ? record.getR21_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR22_NAME_OF_CORPORATE() != null ? record.getR22_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR22_CREDIT_RATING() != null ? record.getR22_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR22_RATING_AGENCY() != null ? record.getR22_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR23_NAME_OF_CORPORATE() != null ? record.getR23_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR23_CREDIT_RATING() != null ? record.getR23_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR23_RATING_AGENCY() != null ? record.getR23_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR24_NAME_OF_CORPORATE() != null ? record.getR24_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR24_CREDIT_RATING() != null ? record.getR24_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR24_RATING_AGENCY() != null ? record.getR24_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR25_NAME_OF_CORPORATE() != null ? record.getR25_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR25_CREDIT_RATING() != null ? record.getR25_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR25_RATING_AGENCY() != null ? record.getR25_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR26_NAME_OF_CORPORATE() != null ? record.getR26_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR26_CREDIT_RATING() != null ? record.getR26_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR26_RATING_AGENCY() != null ? record.getR26_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR27_NAME_OF_CORPORATE() != null ? record.getR27_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR27_CREDIT_RATING() != null ? record.getR27_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR27_RATING_AGENCY() != null ? record.getR27_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR28_NAME_OF_CORPORATE() != null ? record.getR28_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR28_CREDIT_RATING() != null ? record.getR28_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR28_RATING_AGENCY() != null ? record.getR28_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR29_NAME_OF_CORPORATE() != null ? record.getR29_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR29_CREDIT_RATING() != null ? record.getR29_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR29_RATING_AGENCY() != null ? record.getR29_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR30_NAME_OF_CORPORATE() != null ? record.getR30_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR30_CREDIT_RATING() != null ? record.getR30_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR30_RATING_AGENCY() != null ? record.getR30_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR31_NAME_OF_CORPORATE() != null ? record.getR31_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR31_CREDIT_RATING() != null ? record.getR31_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR31_RATING_AGENCY() != null ? record.getR31_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR32_NAME_OF_CORPORATE() != null ? record.getR32_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR32_CREDIT_RATING() != null ? record.getR32_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR32_RATING_AGENCY() != null ? record.getR32_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR33_NAME_OF_CORPORATE() != null ? record.getR33_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR33_CREDIT_RATING() != null ? record.getR33_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR33_RATING_AGENCY() != null ? record.getR33_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR34_NAME_OF_CORPORATE() != null ? record.getR34_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR34_CREDIT_RATING() != null ? record.getR34_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR34_RATING_AGENCY() != null ? record.getR34_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR35_NAME_OF_CORPORATE() != null ? record.getR35_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR35_CREDIT_RATING() != null ? record.getR35_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR35_RATING_AGENCY() != null ? record.getR35_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					// Column F
					row = sheet.getRow(35);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR36_NAME_OF_CORPORATE() != null ? record.getR36_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR36_CREDIT_RATING() != null ? record.getR36_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR36_RATING_AGENCY() != null ? record.getR36_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SRWA_12FEmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SRWA_12F_Resub_Summary_Entity> dataList = brrs_M_SRWA_12F_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12F report. Returning empty result.");
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
					M_SRWA_12F_Resub_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row11
					// Column E
					Cell cell1 = row.createCell(1);
					if (record1.getR11_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR11_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record1.getR11_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR11_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(4);
					if (record1.getR11_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR11_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row11
					// Column E
					Cell cell4 = row.createCell(6);
					if (record1.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(7);
					if (record1.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// row11
					// Column F
					Cell cell6 = row.createCell(8);
					if (record1.getR11_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR11_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell1 = row.createCell(1);
					if (record1.getR12_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR12_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR12_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR12_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR12_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR12_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(6);
					if (record1.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(7);
					if (record1.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					cell6 = row.createCell(8);
					if (record1.getR12_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR12_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell1 = row.createCell(1);
					if (record1.getR13_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR13_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR13_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR13_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR13_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR13_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR13_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR13_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell1 = row.createCell(1);
					if (record1.getR14_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR14_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR14_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR14_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR14_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR14_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR14_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR14_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					cell1 = row.createCell(1);
					if (record1.getR15_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR15_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR15_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR15_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR15_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR15_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR15_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR15_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell1 = row.createCell(1);
					if (record1.getR16_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR16_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR16_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR16_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR16_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR16_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR16_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR16_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					cell1 = row.createCell(1);
					if (record1.getR17_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR17_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR17_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR17_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR17_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR17_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR17_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR17_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell1 = row.createCell(1);
					if (record1.getR18_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR18_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR18_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR18_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR18_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR18_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR18_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR18_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					cell1 = row.createCell(1);
					if (record1.getR19_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR19_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR19_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR19_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR19_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR19_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR19_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR19_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell1 = row.createCell(1);
					if (record1.getR20_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR20_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR20_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR20_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR20_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR20_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR20_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR20_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					cell1 = row.createCell(1);
					if (record1.getR21_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR21_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR21_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR21_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR21_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR21_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR21_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR21_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R22
					// ==========================
					row = sheet.getRow(21);

					cell1 = row.createCell(1);
					if (record1.getR22_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR22_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR22_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR22_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR22_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR22_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR22_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR22_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R23
					// ==========================
					row = sheet.getRow(22);

					cell1 = row.createCell(1);
					if (record1.getR23_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR23_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR23_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR23_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR23_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR23_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR23_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR23_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R24
					// ==========================
					row = sheet.getRow(23);

					cell1 = row.createCell(1);
					if (record1.getR24_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR24_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR24_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR24_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR24_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR24_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR24_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR24_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell1 = row.createCell(1);
					if (record1.getR25_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR25_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR25_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR25_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR25_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR25_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR25_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR25_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell1 = row.createCell(1);
					if (record1.getR26_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR26_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR26_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR26_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR26_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR26_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR26_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR26_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell1 = row.createCell(1);
					if (record1.getR27_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR27_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR27_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR27_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR27_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR27_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR27_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR27_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cell1 = row.createCell(1);
					if (record1.getR28_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR28_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR28_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR28_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR28_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR28_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR28_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR28_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell1 = row.createCell(1);
					if (record1.getR29_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR29_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR29_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR29_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR29_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR29_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR29_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR29_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell1 = row.createCell(1);
					if (record1.getR30_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR30_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR30_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR30_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR30_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR30_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR30_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR30_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					cell1 = row.createCell(1);
					if (record1.getR31_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR31_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR31_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR31_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR31_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR31_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR31_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR31_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell1 = row.createCell(1);
					if (record1.getR32_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR32_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR32_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR32_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR32_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR32_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR32_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR32_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell1 = row.createCell(1);
					if (record1.getR33_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR33_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR33_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR33_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR33_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR33_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR33_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR33_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					cell1 = row.createCell(1);
					if (record1.getR34_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR34_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR34_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR34_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR34_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR34_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR34_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR34_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell1 = row.createCell(1);
					if (record1.getR35_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR35_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR35_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR35_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR35_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR35_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR35_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR35_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell1 = row.createCell(1);
					if (record1.getR36_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR36_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR36_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR36_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR36_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR36_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR36_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR36_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);

					cell1 = row.createCell(1);
					if (record1.getR37_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR37_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR37_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR37_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR37_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR37_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR37_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR37_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR37_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR37_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR37_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR37_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
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