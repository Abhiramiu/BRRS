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

import com.bornfire.brrs.entities.BRRS_M_CA4_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA4_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA4_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA4_RESUB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA4_RESUB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA4_Summary_Repo;
import com.bornfire.brrs.entities.M_CA4_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_CA4_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA4_Detail_Entity;
import com.bornfire.brrs.entities.M_CA4_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_CA4_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_CA4_Summary_Entity;
import com.bornfire.brrs.entities.M_EPR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12H_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12H_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12H_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12H_Resub_Summary_Entity;

@Component
@Service

public class BRRS_M_CA4_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA4_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_CA4_Summary_Repo brrs_m_ca4_summary_repo;

	@Autowired
	BRRS_M_CA4_Detail_Repo brrs_m_ca4_detail_repo;

	@Autowired
	BRRS_M_CA4_Archival_Summary_Repo m_ca4_Archival_Summary_Repo;

	@Autowired
	BRRS_M_CA4_Detail_Repo BRRS_M_CA4_Detail_Repo;

	@Autowired
	BRRS_M_CA4_Archival_Detail_Repo BRRS_M_CA4_Archival_Detail_Repo;

	@Autowired
	BRRS_M_CA4_RESUB_Summary_Repo brrs_m_ca4_resub_summary_repo;

	@Autowired
	BRRS_M_CA4_RESUB_Detail_Repo brrs_m_ca4_resub_detail_repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_CA4View(String reportId, String fromdate, String todate, String currency,
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
				List<M_CA4_Archival_Summary_Entity> T1Master = m_ca4_Archival_Summary_Repo.getdatabydateListarchival(d1,
						version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_CA4_Resub_Summary_Entity> T1Master = brrs_m_ca4_resub_summary_repo.getdatabydateListarchival(d1,
						version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_CA4_Summary_Entity> T1Master = brrs_m_ca4_summary_repo.getdatabydateList(todate);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_CA4_Archival_Detail_Entity> T1Master = BRRS_M_CA4_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_CA4_Resub_Detail_Entity> T1Master = brrs_m_ca4_resub_detail_repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_CA4_Detail_Entity> T1Master = BRRS_M_CA4_Detail_Repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_CA4");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_CA4_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		M_CA4_Summary_Entity existing = brrs_m_ca4_summary_repo.findById(updatedEntity.getReport_date()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		M_CA4_Detail_Entity existingDetail = brrs_m_ca4_detail_repo.findById(updatedEntity.getReport_date())
				.orElseGet(() -> {
					M_CA4_Detail_Entity d = new M_CA4_Detail_Entity();
					d.setReport_date(updatedEntity.getReport_date());
					return d;
				});

		try {
			// 1️⃣ Loop from R10 to R58 and copy fields

			for (int i = 10; i <= 58; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "item", "amt_name_of_sub1", "amt_name_of_sub2", "amt_name_of_sub3",
						"amt_name_of_sub4", "amt_name_of_sub5", "tot_amt" };

				for (String field : fields) {
					String getterName = "get" + prefix + field; // e.g., getR10_item
					String setterName = "set" + prefix + field; // e.g., setR10_item

					try {
						Method getter = M_CA4_Summary_Entity.class.getMethod(getterName);
						Method setter = M_CA4_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Method detailSetter = M_CA4_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		brrs_m_ca4_detail_repo.save(existingDetail);
		brrs_m_ca4_summary_repo.save(existing);
	}

	public void updateResubReport(M_CA4_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReport_date();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = brrs_m_ca4_resub_summary_repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_CA4_Resub_Summary_Entity resubSummary = new M_CA4_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReport_date(reportDate);
		resubSummary.setReport_version(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_CA4_Resub_Detail_Entity resubDetail = new M_CA4_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReport_date(reportDate);
		resubDetail.setReport_version(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_CA4_Archival_Summary_Entity archSummary = new M_CA4_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReport_date(reportDate);
		archSummary.setReport_version(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_CA4_Archival_Detail_Entity archDetail = new M_CA4_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReport_date(reportDate);
		archDetail.setReport_version(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		brrs_m_ca4_resub_summary_repo.save(resubSummary);
		brrs_m_ca4_resub_detail_repo.save(resubDetail);

		m_ca4_Archival_Summary_Repo.save(archSummary);
		BRRS_M_CA4_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_CA4Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA4_Archival_Summary_Entity> latestArchivalList = m_ca4_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA4_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_CA4 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_CA4Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_CA4_Archival_Summary_Entity> repoData = m_ca4_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_CA4_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_CA4_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching m_ca4 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_CA4Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
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
				return getExcelM_CA4ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA4ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_CA4EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_CA4_Summary_Entity> dataList = brrs_m_ca4_summary_repo
						.getdatabydateList(todate);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_CA4 report. Returning empty result.");
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
							M_CA4_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

// row10

							// Column 2 - name of sub1
							Cell cellC = row.createCell(2);
							if (record.getR10_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							Cell cellD = row.createCell(3);
							if (record.getR10_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							Cell cellE = row.createCell(4);
							if (record.getR10_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							Cell cellF = row.createCell(5);
							if (record.getR10_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub4
							Cell cellG = row.createCell(6);
							if (record.getR10_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR10_tot_amt() != null) {
								cellG.setCellValue(record.getR10_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row11
							row = sheet.getRow(10);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR11_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR11_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR11_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR11_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR11_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR11_tot_amt() != null) {
								cellG.setCellValue(record.getR11_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR13_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR13_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR13_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR13_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR13_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR13_tot_amt() != null) {
								cellG.setCellValue(record.getR13_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR14_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR14_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR14_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR14_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR14_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR14_tot_amt() != null) {
								cellG.setCellValue(record.getR14_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR17_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR17_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR17_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR17_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR17_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 2 - name of sub1 cellC = row.createCell(2); if
							 * (record.getR17_amt_name_of_sub1() != null) {
							 * cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
							 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
							 * cellC.setCellStyle(textStyle); }
							 * 
							 * // Column 3 - name of sub2 cellD = row.createCell(3); if
							 * (record.getR17_amt_name_of_sub2() != null) {
							 * cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
							 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
							 * cellD.setCellStyle(textStyle); }
							 * 
							 * // Column 4 - name of sub3 cellE = row.createCell(4); if
							 * (record.getR17_amt_name_of_sub3() != null) {
							 * cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
							 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
							 * cellE.setCellStyle(textStyle); }
							 * 
							 * // Column 5 - name of sub4 cellF = row.createCell(5); if
							 * (record.getR17_amt_name_of_sub4() != null) {
							 * cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
							 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
							 * cellF.setCellStyle(textStyle); }
							 * 
							 * // Column 6 - name of sub5 cellG = row.createCell(6); if
							 * (record.getR17_amt_name_of_sub5() != null) {
							 * cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
							 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
							 * cellG.setCellStyle(textStyle); }
							 */
							// COLUMN TOTAL
							cellG = row.getCell(7);
							if (cellG == null) {
								cellG = row.createCell(7);
							}

							if (record.getR17_tot_amt() != null) {
								cellG.setCellValue(record.getR17_tot_amt().doubleValue());
							} else {
								cellG.setCellValue("");
							}

							// row18
							row = sheet.getRow(17);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR18_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR18_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR18_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR18_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR18_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR18_tot_amt() != null) {
								cellG.setCellValue(record.getR18_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR19_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR19_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR19_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR19_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR19_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR19_tot_amt() != null) {
								cellG.setCellValue(record.getR19_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR22_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR22_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR22_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR22_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR22_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 3 - name of sub1 cellC = row.createCell(2); if
							 * (record.getR22_amt_name_of_sub1() != null) {
							 * cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
							 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
							 * cellC.setCellStyle(textStyle); }
							 * 
							 * // Column 4 - name of sub2 cellD = row.createCell(3); if
							 * (record.getR22_amt_name_of_sub2() != null) {
							 * cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
							 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
							 * cellD.setCellStyle(textStyle); }
							 * 
							 * // Column 5 - name of sub3 cellE = row.createCell(4); if
							 * (record.getR22_amt_name_of_sub3() != null) {
							 * cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
							 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
							 * cellE.setCellStyle(textStyle); }
							 * 
							 * // Column 6 - name of sub4 cellF = row.createCell(5); if
							 * (record.getR22_amt_name_of_sub4() != null) {
							 * cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
							 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
							 * cellF.setCellStyle(textStyle); }
							 * 
							 * // Column 7 - name of sub5 cellG = row.createCell(6); if
							 * (record.getR22_amt_name_of_sub5() != null) {
							 * cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
							 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
							 * cellG.setCellStyle(textStyle); }
							 */

							// COLUMN TOTAL
							cellG = row.getCell(7);
							if (cellG == null) {
								cellG = row.createCell(7);
							}

							if (record.getR22_tot_amt() != null) {
								cellG.setCellValue(record.getR22_tot_amt().doubleValue());
							} else {
								cellG.setCellValue("");
							}

							// row23
							row = sheet.getRow(22);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR23_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR23_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR23_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR23_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR23_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR23_tot_amt() != null) {
								cellG.setCellValue(record.getR23_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR24_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR24_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR24_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR24_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR24_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR24_tot_amt() != null) {
								cellG.setCellValue(record.getR24_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR27_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR27_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR27_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR27_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR27_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}
							// COLUMN TOTAL
							cellG = row.getCell(7);
							if (cellG == null) {
								cellG = row.createCell(7);
							}

							if (record.getR27_tot_amt() != null) {
								cellG.setCellValue(record.getR27_tot_amt().doubleValue());
							} else {
								cellG.setCellValue("");
							}

							// row28
							row = sheet.getRow(27);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR28_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR28_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR28_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR28_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR28_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR28_tot_amt() != null) {
								cellG.setCellValue(record.getR28_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row29
							row = sheet.getRow(28);
							// Column 1 - item

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR29_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR29_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR29_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR29_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR29_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR29_tot_amt() != null) {
								cellG.setCellValue(record.getR29_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// row32
							row = sheet.getRow(31);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR32_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR32_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR32_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR32_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR32_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}
							// COLUMN TOTAL
							cellG = row.getCell(7);
							if (cellG == null) {
								cellG = row.createCell(7);
							}

							if (record.getR32_tot_amt() != null) {
								cellG.setCellValue(record.getR32_tot_amt().doubleValue());
							} else {
								cellG.setCellValue("");
							}

							// row33
							row = sheet.getRow(32);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR33_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR33_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR33_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR33_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR33_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR33_tot_amt() != null) {
								cellG.setCellValue(record.getR33_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							/*
							 * // row33 row = sheet.getRow(32);
							 * 
							 * 
							 * // Column 2 - name of sub1 cellC = row.createCell(2); if
							 * (record.getR33_amt_name_of_sub1() != null) {
							 * cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
							 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
							 * cellC.setCellStyle(textStyle); }
							 * 
							 * // Column 3 - name of sub2 cellD = row.createCell(3); if
							 * (record.getR33_amt_name_of_sub2() != null) {
							 * cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
							 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
							 * cellD.setCellStyle(textStyle); }
							 * 
							 * // Column 4 - name of sub3 cellE = row.createCell(4); if
							 * (record.getR33_amt_name_of_sub3() != null) {
							 * cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
							 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
							 * cellE.setCellStyle(textStyle); }
							 * 
							 * // Column 5 - name of sub4 cellF = row.createCell(5); if
							 * (record.getR33_amt_name_of_sub4() != null) {
							 * cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
							 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
							 * cellF.setCellStyle(textStyle); }
							 * 
							 * // Column 6 - name of sub5 cellG = row.createCell(6); if
							 * (record.getR33_amt_name_of_sub5() != null) {
							 * cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
							 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
							 * cellG.setCellStyle(textStyle); }
							 */

							// row34
							row = sheet.getRow(33);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR34_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR34_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR34_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR34_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR34_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR34_tot_amt() != null) {
								cellG.setCellValue(record.getR34_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ====================== R36 ======================
							// row36
							row = sheet.getRow(35);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR36_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR36_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR36_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR36_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR36_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ====================== R37 ======================
							row = sheet.getRow(36);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR37_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR37_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR37_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR37_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR37_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR37_tot_amt() != null) {
								cellG.setCellValue(record.getR37_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ====================== R38 ======================
							row = sheet.getRow(37);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR38_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR38_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR38_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR38_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR38_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR38_tot_amt() != null) {
								cellG.setCellValue(record.getR38_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ====================== R39 ======================
							// row39
							row = sheet.getRow(38);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR39_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR39_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR39_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR39_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR39_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ====================== R40 ======================
							row = sheet.getRow(39);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR40_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR40_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR40_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR40_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR40_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR40_tot_amt() != null) {
								cellG.setCellValue(record.getR40_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// =========================
							// Row for R41
							// =========================
							row = sheet.getRow(40);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR41_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR41_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR41_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR41_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR41_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR41_tot_amt() != null) {
								cellG.setCellValue(record.getR41_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// =========================
							// Row for R42
							// =========================
							row = sheet.getRow(41);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR42_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR42_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR42_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR42_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR42_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR42_tot_amt() != null) {
								cellG.setCellValue(record.getR42_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// =========================
							// Row for R43
							// =========================
							row = sheet.getRow(42);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR43_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR43_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR43_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR43_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR43_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 2 - name of sub1 cellC = row.createCell(2); if
							 * (record.getR43_amt_name_of_sub1() != null) {
							 * cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
							 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
							 * cellC.setCellStyle(textStyle); }
							 * 
							 * // Column 3 - name of sub2 cellD = row.createCell(3); if
							 * (record.getR43_amt_name_of_sub2() != null) {
							 * cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
							 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
							 * cellD.setCellStyle(textStyle); }
							 * 
							 * // Column 4 - name of sub3 cellE = row.createCell(4); if
							 * (record.getR43_amt_name_of_sub3() != null) {
							 * cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
							 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
							 * cellE.setCellStyle(textStyle); }
							 * 
							 * // Column 5 - name of sub4 cellF = row.createCell(5); if
							 * (record.getR43_amt_name_of_sub4() != null) {
							 * cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
							 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
							 * cellF.setCellStyle(textStyle); }
							 * 
							 * // Column 6 - name of sub5 cellG = row.createCell(6); if
							 * (record.getR43_amt_name_of_sub5() != null) {
							 * cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
							 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
							 * cellG.setCellStyle(textStyle); }
							 */

							// row44
							// row44
							row = sheet.getRow(43);
							if (row == null)
								row = sheet.createRow(43);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR44_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR44_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR44_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR44_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR44_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}
							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR44_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row45
							row = sheet.getRow(44);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR45_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR45_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR45_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR45_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR45_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR45_tot_amt() != null) {
								cellG.setCellValue(record.getR45_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ================= R46 =================
							// row46
							row = sheet.getRow(45);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR46_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR46_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR46_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR46_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR46_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ================= R47 =================
							// row47
							row = sheet.getRow(46);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR47_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR47_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR47_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR47_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR47_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ================= R48 =================
							row = sheet.getRow(47);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR48_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}
							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR48_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}
							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR48_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}
							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR48_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}
							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR48_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}
							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR48_tot_amt() != null) {
								cellG.setCellValue(record.getR48_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ================= R49 =================
							// row49
							row = sheet.getRow(48);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR49_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR49_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR49_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR49_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR49_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ================= R50 =================
							// row50
							row = sheet.getRow(49);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR50_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR50_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR50_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR50_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR50_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							// ====================== R51 ======================
							row = sheet.getRow(50);

							// Column 2 - name of sub1
							cellC = row.createCell(2);
							if (record.getR51_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 3 - name of sub2
							cellD = row.createCell(3);
							if (record.getR51_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// Column 4 - name of sub3
							cellE = row.createCell(4);
							if (record.getR51_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
								cellE.setCellStyle(numberStyle);
							} else {
								cellE.setCellValue("");
								cellE.setCellStyle(textStyle);
							}

							// Column 5 - name of sub4
							cellF = row.createCell(5);
							if (record.getR51_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
								cellF.setCellStyle(numberStyle);
							} else {
								cellF.setCellValue("");
								cellF.setCellStyle(textStyle);
							}

							// Column 6 - name of sub5
							cellG = row.createCell(6);
							if (record.getR51_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// Column 7 total
							cellG = row.createCell(7);
							if (record.getR51_tot_amt() != null) {
								cellG.setCellValue(record.getR51_tot_amt().doubleValue());
								cellG.setCellStyle(numberStyle);
							} else {
								cellG.setCellValue("");
								cellG.setCellStyle(textStyle);
							}

							// ====================== R52 ======================
							// row52
							row = sheet.getRow(51);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR52_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR52_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR52_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR52_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR52_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR52_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR52_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// ====================== R53 ======================

							// row53
							row = sheet.getRow(52);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR53_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR53_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR53_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR53_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR53_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR53_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR53_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row54
							row = sheet.getRow(53);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR54_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR54_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR54_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR54_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR54_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}
							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR54_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR54_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row55
							row = sheet.getRow(54);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR55_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR55_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR55_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR55_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR55_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR55_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR55_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row56
							row = sheet.getRow(55);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR56_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR56_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR56_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR56_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR56_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR56_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR56_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row57
							row = sheet.getRow(56);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR57_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR57_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR57_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR57_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR57_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
							}

							/*
							 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR57_tot_amt() !=
							 * null) { cellH.setCellValue(record.getR57_tot_amt().doubleValue());
							 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
							 * cellH.setCellStyle(textStyle); }
							 */

							// row58
							row = sheet.getRow(57);

							// Column 3 - name of sub1
							cellC = row.getCell(2);
							if (cellC == null)
								cellC = row.createCell(2);
							if (record.getR58_amt_name_of_sub1() != null) {
								cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue());
							} else {
								cellC.setCellValue(0);
							}

							// Column 4 - name of sub2
							cellD = row.getCell(3);
							if (cellD == null)
								cellD = row.createCell(3);
							if (record.getR58_amt_name_of_sub2() != null) {
								cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue());
							} else {
								cellD.setCellValue(0);
							}

							// Column 5 - name of sub3
							cellE = row.getCell(4);
							if (cellE == null)
								cellE = row.createCell(4);
							if (record.getR58_amt_name_of_sub3() != null) {
								cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue());
							} else {
								cellE.setCellValue(0);
							}

							// Column 6 - name of sub4
							cellF = row.getCell(5);
							if (cellF == null)
								cellF = row.createCell(5);
							if (record.getR58_amt_name_of_sub4() != null) {
								cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue());
							} else {
								cellF.setCellValue(0);
							}

							// Column 7 - name of sub5
							cellG = row.getCell(6);
							if (cellG == null)
								cellG = row.createCell(6);
							if (record.getR58_amt_name_of_sub5() != null) {
								cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue());
							} else {
								cellG.setCellValue(0);
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
	public byte[] BRRS_M_CA4EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");
		
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA4ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA4ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 
		else {
		List<M_CA4_Summary_Entity> dataList = brrs_m_ca4_summary_repo.getdatabydateList(todate);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA4 report. Returning empty result.");
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
					M_CA4_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row10

					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					Cell cellD = row.createCell(4);
					if (record.getR10_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 6 - name of sub3
					Cell cellE = row.createCell(6);
					if (record.getR10_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 8 - name of sub4
					Cell cellF = row.createCell(8);
					if (record.getR10_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 10 - name of sub4
					Cell cellG = row.createCell(10);
					if (record.getR10_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 12 total
					cellG = row.createCell(12);
					if (record.getR10_tot_amt() != null) {
						cellG.setCellValue(record.getR10_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR11_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR11_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR11_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR11_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR11_tot_amt() != null) {
						cellG.setCellValue(record.getR11_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR13_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR13_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR13_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR13_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR13_tot_amt() != null) {
						cellG.setCellValue(record.getR13_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR14_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR14_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR14_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR14_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR14_tot_amt() != null) {
						cellG.setCellValue(record.getR14_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR17_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR17_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR17_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR17_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR17_tot_amt() != null) {
						cellG.setCellValue(record.getR17_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR18_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR18_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR18_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR18_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR18_tot_amt() != null) {
						cellG.setCellValue(record.getR18_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR19_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR19_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR19_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR19_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR19_tot_amt() != null) {
						cellG.setCellValue(record.getR19_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/*
					 * 
					 * // row22 row = sheet.getRow(21);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR22_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR22_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR22_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR22_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR22_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * //COLUMN TOTAL cellG = row.getCell(12); if (cellG == null) { cellG =
					 * row.createCell(12); }
					 * 
					 * if (record.getR22_tot_amt() != null) {
					 * cellG.setCellValue(record.getR22_tot_amt().doubleValue()); } else {
					 * cellG.setCellValue(""); }
					 * 
					 */

					// row23
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR23_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR23_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR23_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR23_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR23_tot_amt() != null) {
						cellG.setCellValue(record.getR23_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR24_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR24_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR24_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR24_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR24_tot_amt() != null) {
						cellG.setCellValue(record.getR24_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(21);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR27_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR27_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR27_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR27_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR27_tot_amt() != null) {
						cellG.setCellValue(record.getR27_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row28
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR28_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR28_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR28_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR28_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR28_tot_amt() != null) {
						cellG.setCellValue(record.getR28_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column 1 - item

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR29_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR29_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR29_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR29_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR29_tot_amt() != null) {
						cellG.setCellValue(record.getR29_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR32_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR32_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR32_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR32_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR32_tot_amt() != null) {
						cellG.setCellValue(record.getR32_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row33
					row = sheet.getRow(27);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR33_tot_amt() != null) {
						cellG.setCellValue(record.getR33_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(28);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR34_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR34_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR34_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR34_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR34_tot_amt() != null) {
						cellG.setCellValue(record.getR34_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(30);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR36_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR36_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR36_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR36_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R37 ======================
					row = sheet.getRow(31);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR37_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR37_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR37_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR37_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR37_tot_amt() != null) {
						cellG.setCellValue(record.getR37_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R38 ======================
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR38_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR38_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR38_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR38_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR38_tot_amt() != null) {
						cellG.setCellValue(record.getR38_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R39 ======================
					// row39
					row = sheet.getRow(33);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR39_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR39_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR39_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR39_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R40 ======================
					row = sheet.getRow(34);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR40_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR40_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR40_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR40_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR40_tot_amt() != null) {
						cellG.setCellValue(record.getR40_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(35);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR41_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR41_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR41_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR41_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR41_tot_amt() != null) {
						cellG.setCellValue(record.getR41_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(36);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR42_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR42_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR42_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR42_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR42_tot_amt() != null) {
						cellG.setCellValue(record.getR42_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(37);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR43_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR43_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR43_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR43_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row44
					// row44
					row = sheet.getRow(38);
					if (row == null)
						row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR44_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR44_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR44_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR44_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(12); if (record.getR44_tot_amt()
					 * != null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row45
					row = sheet.getRow(39);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR45_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR45_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR45_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR45_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR45_tot_amt() != null) {
						cellG.setCellValue(record.getR45_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R46 =================
					// row46
					row = sheet.getRow(40);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR46_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR46_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR46_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR46_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R47 =================
					// row47
					row = sheet.getRow(41);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR47_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR47_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR47_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR47_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R48 =================
					row = sheet.getRow(42);
					/*
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR48_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); } // Column 3 - name of sub2 cellD =
					 * row.createCell(4); if (record.getR48_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); } // Column 4 - name of sub3 cellE =
					 * row.createCell(6); if (record.getR48_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); } // Column 5 - name of sub4 cellF =
					 * row.createCell(8); if (record.getR48_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); } // Column 6 - name of sub5 cellG =
					 * row.createCell(10); if (record.getR48_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); } // Column 7 total cellG =
					 * row.createCell(12); if (record.getR48_tot_amt() != null) {
					 * cellG.setCellValue(record.getR48_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ================= R49 ================= // row49 row = sheet.getRow(43);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR49_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR49_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR49_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR49_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR49_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ================= R50 ================= // row50 row = sheet.getRow(44);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR50_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR50_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR50_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR50_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR50_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ====================== R51 ====================== row = sheet.getRow(45);
					 * 
					 * 
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR51_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); }
					 * 
					 * // Column 3 - name of sub2 cellD = row.createCell(4); if
					 * (record.getR51_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); }
					 * 
					 * // Column 4 - name of sub3 cellE = row.createCell(6); if
					 * (record.getR51_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 * 
					 * // Column 5 - name of sub4 cellF = row.createCell(8); if
					 * (record.getR51_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); }
					 * 
					 * // Column 6 - name of sub5 cellG = row.createCell(10); if
					 * (record.getR51_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * // Column 7 total cellG = row.createCell(12); if (record.getR51_tot_amt() !=
					 * null) { cellG.setCellValue(record.getR51_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ====================== R52 ====================== // row52 row =
					 * sheet.getRow(46);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR52_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR52_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR52_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR52_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR52_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 */

					// ====================== R53 ======================

					// row53
					row = sheet.getRow(44);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR53_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR53_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR53_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR53_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row54
					row = sheet.getRow(45);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR54_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR54_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR54_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR54_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * 
					 * 
					 * // row55 row = sheet.getRow(49);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR55_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR55_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR55_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR55_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR55_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row56 row = sheet.getRow(50);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR56_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR56_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR56_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR56_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR56_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // row57 row = sheet.getRow(51);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR57_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR57_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR57_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR57_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR57_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row58 row = sheet.getRow(52);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR58_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR58_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR58_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR58_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR58_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 */

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
	public byte[] getExcelM_CA4ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA4ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 

		List<M_CA4_Archival_Summary_Entity> dataList = m_ca4_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA4 report. Returning empty result.");
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
					M_CA4_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row10

					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					Cell cellD = row.createCell(3);
					if (record.getR10_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					Cell cellE = row.createCell(4);
					if (record.getR10_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					Cell cellF = row.createCell(5);
					if (record.getR10_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					Cell cellG = row.createCell(6);
					if (record.getR10_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR11_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR11_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR11_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR11_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR13_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR13_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR13_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR13_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR14_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR14_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR14_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR14_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR17_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR17_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR17_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR17_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR18_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR18_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR18_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR18_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR19_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR19_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR19_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR19_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					// Column 3 - name of sub1
					cellC = row.createCell(2);
					if (record.getR22_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					cellD = row.createCell(3);
					if (record.getR22_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 5 - name of sub3
					cellE = row.createCell(4);
					if (record.getR22_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					cellF = row.createCell(5);
					if (record.getR22_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 7 - name of sub5
					cellG = row.createCell(6);
					if (record.getR22_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR23_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR23_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR23_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR23_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR24_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR24_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR24_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR24_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR27_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR27_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR27_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR27_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row28
					row = sheet.getRow(27);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR28_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR28_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR28_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR28_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column 1 - item

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR29_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR29_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR29_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR29_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR32_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR32_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR32_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR32_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR34_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR34_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR34_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR34_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(35);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR36_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR36_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR36_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR36_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R37 ======================
					row = sheet.getRow(36);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR37_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR37_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR37_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR37_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R38 ======================
					row = sheet.getRow(37);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR38_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR38_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR38_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR38_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R39 ======================
					// row39
					row = sheet.getRow(38);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR39_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR39_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR39_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR39_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R40 ======================
					row = sheet.getRow(39);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR40_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR40_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR40_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR40_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(40);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR41_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR41_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR41_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR41_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(41);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR42_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR42_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR42_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR42_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(42);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR43_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR43_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR43_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR43_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row44
					// row44
					row = sheet.getRow(43);
					if (row == null)
						row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR44_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR44_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR44_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR44_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR44_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row45
					row = sheet.getRow(44);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR45_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR45_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR45_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR45_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R46 =================
					// row46
					row = sheet.getRow(45);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR46_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR46_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR46_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR46_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R47 =================
					// row47
					row = sheet.getRow(46);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR47_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR47_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR47_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR47_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R48 =================
					row = sheet.getRow(47);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR48_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR48_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR48_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR48_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}
					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR48_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R49 =================
					// row49
					row = sheet.getRow(48);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR49_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR49_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR49_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR49_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR49_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R50 =================
					// row50
					row = sheet.getRow(49);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR50_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR50_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR50_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR50_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR50_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R51 ======================
					row = sheet.getRow(50);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR51_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR51_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR51_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR51_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR51_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R52 ======================
					// row52
					row = sheet.getRow(51);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR52_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR52_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR52_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR52_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR52_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR52_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR52_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// ====================== R53 ======================

					// row53
					row = sheet.getRow(52);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR53_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR53_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR53_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR53_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR53_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR53_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row54
					row = sheet.getRow(53);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR54_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR54_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR54_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR54_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR54_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR54_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row55
					row = sheet.getRow(54);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR55_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR55_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR55_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR55_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR55_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR55_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR55_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row56
					row = sheet.getRow(55);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR56_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR56_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR56_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR56_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR56_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR56_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR56_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row57
					row = sheet.getRow(56);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR57_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR57_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR57_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR57_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR57_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR57_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR57_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row58
					row = sheet.getRow(57);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR58_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR58_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR58_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR58_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR58_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
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
	public byte[] BRRS_M_CA4ArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA4_Archival_Summary_Entity> dataList = m_ca4_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA4 report. Returning empty result.");
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
					M_CA4_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row10

					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					Cell cellD = row.createCell(4);
					if (record.getR10_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 6 - name of sub3
					Cell cellE = row.createCell(6);
					if (record.getR10_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 8 - name of sub4
					Cell cellF = row.createCell(8);
					if (record.getR10_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 10 - name of sub4
					Cell cellG = row.createCell(10);
					if (record.getR10_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 12 total
					cellG = row.createCell(12);
					if (record.getR10_tot_amt() != null) {
						cellG.setCellValue(record.getR10_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR11_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR11_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR11_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR11_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR11_tot_amt() != null) {
						cellG.setCellValue(record.getR11_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR13_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR13_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR13_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR13_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR13_tot_amt() != null) {
						cellG.setCellValue(record.getR13_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR14_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR14_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR14_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR14_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR14_tot_amt() != null) {
						cellG.setCellValue(record.getR14_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR17_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR17_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR17_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR17_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR17_tot_amt() != null) {
						cellG.setCellValue(record.getR17_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR18_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR18_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR18_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR18_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR18_tot_amt() != null) {
						cellG.setCellValue(record.getR18_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR19_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR19_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR19_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR19_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR19_tot_amt() != null) {
						cellG.setCellValue(record.getR19_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/*
					 * 
					 * // row22 row = sheet.getRow(21);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR22_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR22_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR22_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR22_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR22_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * //COLUMN TOTAL cellG = row.getCell(12); if (cellG == null) { cellG =
					 * row.createCell(12); }
					 * 
					 * if (record.getR22_tot_amt() != null) {
					 * cellG.setCellValue(record.getR22_tot_amt().doubleValue()); } else {
					 * cellG.setCellValue(""); }
					 * 
					 */

					// row23
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR23_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR23_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR23_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR23_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR23_tot_amt() != null) {
						cellG.setCellValue(record.getR23_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR24_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR24_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR24_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR24_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR24_tot_amt() != null) {
						cellG.setCellValue(record.getR24_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(21);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR27_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR27_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR27_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR27_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR27_tot_amt() != null) {
						cellG.setCellValue(record.getR27_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row28
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR28_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR28_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR28_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR28_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR28_tot_amt() != null) {
						cellG.setCellValue(record.getR28_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column 1 - item

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR29_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR29_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR29_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR29_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR29_tot_amt() != null) {
						cellG.setCellValue(record.getR29_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR32_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR32_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR32_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR32_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR32_tot_amt() != null) {
						cellG.setCellValue(record.getR32_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row33
					row = sheet.getRow(27);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR33_tot_amt() != null) {
						cellG.setCellValue(record.getR33_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(28);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR34_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR34_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR34_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR34_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR34_tot_amt() != null) {
						cellG.setCellValue(record.getR34_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(30);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR36_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR36_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR36_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR36_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R37 ======================
					row = sheet.getRow(31);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR37_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR37_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR37_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR37_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR37_tot_amt() != null) {
						cellG.setCellValue(record.getR37_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R38 ======================
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR38_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR38_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR38_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR38_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR38_tot_amt() != null) {
						cellG.setCellValue(record.getR38_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R39 ======================
					// row39
					row = sheet.getRow(33);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR39_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR39_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR39_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR39_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R40 ======================
					row = sheet.getRow(34);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR40_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR40_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR40_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR40_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR40_tot_amt() != null) {
						cellG.setCellValue(record.getR40_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(35);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR41_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR41_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR41_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR41_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR41_tot_amt() != null) {
						cellG.setCellValue(record.getR41_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(36);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR42_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR42_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR42_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR42_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR42_tot_amt() != null) {
						cellG.setCellValue(record.getR42_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(37);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR43_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR43_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR43_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR43_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row44
					// row44
					row = sheet.getRow(38);
					if (row == null)
						row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR44_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR44_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR44_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR44_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(12); if (record.getR44_tot_amt()
					 * != null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row45
					row = sheet.getRow(39);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR45_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR45_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR45_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR45_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR45_tot_amt() != null) {
						cellG.setCellValue(record.getR45_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R46 =================
					// row46
					row = sheet.getRow(40);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR46_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR46_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR46_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR46_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R47 =================
					// row47
					row = sheet.getRow(41);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR47_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR47_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR47_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR47_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R48 =================
					row = sheet.getRow(42);
					/*
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR48_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); } // Column 3 - name of sub2 cellD =
					 * row.createCell(4); if (record.getR48_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); } // Column 4 - name of sub3 cellE =
					 * row.createCell(6); if (record.getR48_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); } // Column 5 - name of sub4 cellF =
					 * row.createCell(8); if (record.getR48_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); } // Column 6 - name of sub5 cellG =
					 * row.createCell(10); if (record.getR48_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); } // Column 7 total cellG =
					 * row.createCell(12); if (record.getR48_tot_amt() != null) {
					 * cellG.setCellValue(record.getR48_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ================= R49 ================= // row49 row = sheet.getRow(43);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR49_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR49_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR49_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR49_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR49_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ================= R50 ================= // row50 row = sheet.getRow(44);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR50_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR50_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR50_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR50_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR50_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ====================== R51 ====================== row = sheet.getRow(45);
					 * 
					 * 
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR51_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); }
					 * 
					 * // Column 3 - name of sub2 cellD = row.createCell(4); if
					 * (record.getR51_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); }
					 * 
					 * // Column 4 - name of sub3 cellE = row.createCell(6); if
					 * (record.getR51_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 * 
					 * // Column 5 - name of sub4 cellF = row.createCell(8); if
					 * (record.getR51_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); }
					 * 
					 * // Column 6 - name of sub5 cellG = row.createCell(10); if
					 * (record.getR51_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * // Column 7 total cellG = row.createCell(12); if (record.getR51_tot_amt() !=
					 * null) { cellG.setCellValue(record.getR51_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ====================== R52 ====================== // row52 row =
					 * sheet.getRow(46);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR52_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR52_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR52_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR52_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR52_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 */

					// ====================== R53 ======================

					// row53
					row = sheet.getRow(44);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR53_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR53_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR53_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR53_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row54
					row = sheet.getRow(45);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR54_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR54_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR54_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR54_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * 
					 * 
					 * // row55 row = sheet.getRow(49);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR55_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR55_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR55_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR55_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR55_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row56 row = sheet.getRow(50);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR56_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR56_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR56_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR56_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR56_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // row57 row = sheet.getRow(51);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR57_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR57_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR57_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR57_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR57_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row58 row = sheet.getRow(52);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR58_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR58_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR58_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR58_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR58_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 */

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
	public byte[] BRRS_M_CA4ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA4ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		}
	}

		List<M_CA4_Resub_Summary_Entity> dataList = brrs_m_ca4_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA4 report. Returning empty result.");
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

					M_CA4_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row10

					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					Cell cellD = row.createCell(3);
					if (record.getR10_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					Cell cellE = row.createCell(4);
					if (record.getR10_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					Cell cellF = row.createCell(5);
					if (record.getR10_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					Cell cellG = row.createCell(6);
					if (record.getR10_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(7);
					if (record.getR10_tot_amt() != null) {
						cellG.setCellValue(record.getR10_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR11_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR11_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR11_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR11_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(7);
					if (record.getR11_tot_amt() != null) {
						cellG.setCellValue(record.getR11_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR13_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR13_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR13_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR13_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR14_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR14_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR14_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR14_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR17_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR17_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR17_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR17_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR18_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR18_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR18_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR18_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR19_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR19_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR19_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR19_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					// Column 3 - name of sub1
					cellC = row.createCell(2);
					if (record.getR22_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					cellD = row.createCell(3);
					if (record.getR22_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 5 - name of sub3
					cellE = row.createCell(4);
					if (record.getR22_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 6 - name of sub4
					cellF = row.createCell(5);
					if (record.getR22_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 7 - name of sub5
					cellG = row.createCell(6);
					if (record.getR22_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR23_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR23_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR23_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR23_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR24_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR24_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR24_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR24_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR27_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR27_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR27_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR27_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row28
					row = sheet.getRow(27);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR28_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR28_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR28_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR28_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column 1 - item

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR29_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR29_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR29_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR29_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR32_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR32_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR32_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR32_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR34_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR34_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR34_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR34_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(35);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR36_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR36_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR36_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR36_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R37 ======================
					row = sheet.getRow(36);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR37_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR37_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR37_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR37_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R38 ======================
					row = sheet.getRow(37);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR38_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR38_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR38_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR38_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R39 ======================
					// row39
					row = sheet.getRow(38);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR39_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR39_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR39_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR39_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R40 ======================
					row = sheet.getRow(39);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR40_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR40_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR40_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR40_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(40);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR41_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR41_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR41_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR41_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(41);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR42_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR42_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR42_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR42_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(42);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR43_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR43_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR43_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR43_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row44
					// row44
					row = sheet.getRow(43);
					if (row == null)
						row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR44_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR44_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR44_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR44_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR44_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row45
					row = sheet.getRow(44);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR45_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR45_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR45_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR45_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R46 =================
					// row46
					row = sheet.getRow(45);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR46_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR46_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR46_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR46_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R47 =================
					// row47
					row = sheet.getRow(46);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR47_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR47_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR47_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR47_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R48 =================
					row = sheet.getRow(47);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR48_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}
					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR48_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}
					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR48_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR48_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}
					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR48_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R49 =================
					// row49
					row = sheet.getRow(48);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR49_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR49_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR49_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR49_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR49_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R50 =================
					// row50
					row = sheet.getRow(49);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR50_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR50_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR50_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR50_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR50_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R51 ======================
					row = sheet.getRow(50);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR51_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(3);
					if (record.getR51_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(4);
					if (record.getR51_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(5);
					if (record.getR51_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(6);
					if (record.getR51_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R52 ======================
					// row52
					row = sheet.getRow(51);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR52_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR52_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR52_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR52_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR52_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR52_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR52_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// ====================== R53 ======================

					// row53
					row = sheet.getRow(52);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR53_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR53_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR53_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR53_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR53_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR53_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row54
					row = sheet.getRow(53);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR54_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR54_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR54_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR54_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR54_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR54_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row55
					row = sheet.getRow(54);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR55_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR55_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR55_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR55_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR55_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR55_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR55_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row56
					row = sheet.getRow(55);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR56_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR56_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR56_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR56_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR56_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR56_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR56_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row57
					row = sheet.getRow(56);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR57_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR57_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR57_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR57_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR57_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					/*
					 * // Column 7 - TOTAL cellH = row.createCell(7); if (record.getR57_tot_amt() !=
					 * null) { cellH.setCellValue(record.getR57_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row58
					row = sheet.getRow(57);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR58_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					if (record.getR58_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					if (record.getR58_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					if (record.getR58_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					if (record.getR58_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
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
	public byte[] BRRS_M_CA4ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA4_Resub_Summary_Entity> dataList = brrs_m_ca4_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA4 report. Returning empty result.");
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
					M_CA4_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row10

					// Column 2 - name of sub1
					Cell cellC = row.createCell(2);
					if (record.getR10_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR10_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - name of sub2
					Cell cellD = row.createCell(4);
					if (record.getR10_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR10_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 6 - name of sub3
					Cell cellE = row.createCell(6);
					if (record.getR10_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR10_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 8 - name of sub4
					Cell cellF = row.createCell(8);
					if (record.getR10_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR10_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 10 - name of sub4
					Cell cellG = row.createCell(10);
					if (record.getR10_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR10_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 12 total
					cellG = row.createCell(12);
					if (record.getR10_tot_amt() != null) {
						cellG.setCellValue(record.getR10_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR11_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR11_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR11_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR11_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR11_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR11_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR11_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR11_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR11_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR11_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR11_tot_amt() != null) {
						cellG.setCellValue(record.getR11_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR13_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR13_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR13_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR13_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR13_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR13_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR13_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR13_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR13_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR13_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR13_tot_amt() != null) {
						cellG.setCellValue(record.getR13_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR14_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR14_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR14_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR14_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR14_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR14_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR14_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR14_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR14_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR14_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR14_tot_amt() != null) {
						cellG.setCellValue(record.getR14_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR17_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR17_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR17_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR17_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR17_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR17_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR17_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR17_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR17_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR17_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR17_tot_amt() != null) {
						cellG.setCellValue(record.getR17_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR18_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR18_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR18_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR18_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR18_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR18_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR18_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR18_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR18_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR18_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR18_tot_amt() != null) {
						cellG.setCellValue(record.getR18_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR19_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR19_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR19_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR19_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR19_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR19_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR19_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR19_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR19_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR19_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR19_tot_amt() != null) {
						cellG.setCellValue(record.getR19_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					/*
					 * 
					 * // row22 row = sheet.getRow(21);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR22_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR22_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR22_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR22_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR22_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR22_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR22_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR22_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR22_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR22_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * //COLUMN TOTAL cellG = row.getCell(12); if (cellG == null) { cellG =
					 * row.createCell(12); }
					 * 
					 * if (record.getR22_tot_amt() != null) {
					 * cellG.setCellValue(record.getR22_tot_amt().doubleValue()); } else {
					 * cellG.setCellValue(""); }
					 * 
					 */

					// row23
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR23_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR23_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR23_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR23_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR23_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR23_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR23_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR23_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR23_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR23_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR23_tot_amt() != null) {
						cellG.setCellValue(record.getR23_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR24_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR24_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR24_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR24_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR24_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR24_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR24_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR24_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR24_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR24_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR24_tot_amt() != null) {
						cellG.setCellValue(record.getR24_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(21);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR27_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR27_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR27_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR27_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR27_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR27_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR27_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR27_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR27_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR27_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR27_tot_amt() != null) {
						cellG.setCellValue(record.getR27_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row28
					row = sheet.getRow(22);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR28_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR28_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR28_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR28_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR28_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR28_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR28_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR28_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR28_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR28_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR28_tot_amt() != null) {
						cellG.setCellValue(record.getR28_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column 1 - item

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR29_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR29_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR29_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR29_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR29_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR29_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR29_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR29_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR29_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR29_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR29_tot_amt() != null) {
						cellG.setCellValue(record.getR29_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR32_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR32_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR32_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR32_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR32_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR32_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR32_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR32_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR32_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR32_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					// COLUMN TOTAL
					cellG = row.getCell(12);
					if (cellG == null) {
						cellG = row.createCell(12);
					}

					if (record.getR32_tot_amt() != null) {
						cellG.setCellValue(record.getR32_tot_amt().doubleValue());
					} else {
						cellG.setCellValue("");
					}

					// row33
					row = sheet.getRow(27);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR33_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR33_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR33_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR33_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR33_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR33_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR33_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR33_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR33_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR33_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR33_tot_amt() != null) {
						cellG.setCellValue(record.getR33_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(28);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR34_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR34_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR34_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR34_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR34_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR34_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR34_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR34_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR34_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR34_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR34_tot_amt() != null) {
						cellG.setCellValue(record.getR34_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R36 ======================
					// row36
					row = sheet.getRow(30);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR36_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR36_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR36_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR36_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR36_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR36_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR36_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR36_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR36_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR36_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R37 ======================
					row = sheet.getRow(31);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR37_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR37_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR37_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR37_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR37_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR37_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR37_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR37_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR37_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR37_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR37_tot_amt() != null) {
						cellG.setCellValue(record.getR37_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R38 ======================
					row = sheet.getRow(32);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR38_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR38_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR38_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR38_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR38_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR38_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR38_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR38_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR38_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR38_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR38_tot_amt() != null) {
						cellG.setCellValue(record.getR38_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ====================== R39 ======================
					// row39
					row = sheet.getRow(33);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR39_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR39_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR39_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR39_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR39_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR39_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR39_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR39_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR39_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR39_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ====================== R40 ======================
					row = sheet.getRow(34);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR40_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR40_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR40_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR40_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR40_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR40_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR40_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR40_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR40_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR40_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR40_tot_amt() != null) {
						cellG.setCellValue(record.getR40_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R41
					// =========================
					row = sheet.getRow(35);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR41_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR41_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR41_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR41_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR41_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR41_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR41_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR41_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR41_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR41_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR41_tot_amt() != null) {
						cellG.setCellValue(record.getR41_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R42
					// =========================
					row = sheet.getRow(36);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR42_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR42_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR42_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR42_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR42_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR42_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR42_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR42_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR42_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR42_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}
					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR42_tot_amt() != null) {
						cellG.setCellValue(record.getR42_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// =========================
					// Row for R43
					// =========================
					row = sheet.getRow(37);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR43_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR43_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR43_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR43_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR43_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR43_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR43_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR43_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR43_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR43_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row44
					// row44
					row = sheet.getRow(38);
					if (row == null)
						row = sheet.createRow(43);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR44_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR44_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR44_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR44_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR44_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR44_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR44_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR44_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR44_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR44_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * // Column 7 - TOTAL cellH = row.createCell(12); if (record.getR44_tot_amt()
					 * != null) { cellH.setCellValue(record.getR44_tot_amt().doubleValue());
					 * cellH.setCellStyle(numberStyle); } else { cellH.setCellValue("");
					 * cellH.setCellStyle(textStyle); }
					 */

					// row45
					row = sheet.getRow(39);

					// Column 2 - name of sub1
					cellC = row.createCell(2);
					if (record.getR45_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR45_amt_name_of_sub1().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 3 - name of sub2
					cellD = row.createCell(4);
					if (record.getR45_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR45_amt_name_of_sub2().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// Column 4 - name of sub3
					cellE = row.createCell(6);
					if (record.getR45_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR45_amt_name_of_sub3().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// Column 5 - name of sub4
					cellF = row.createCell(8);
					if (record.getR45_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR45_amt_name_of_sub4().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// Column 6 - name of sub5
					cellG = row.createCell(10);
					if (record.getR45_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR45_amt_name_of_sub5().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// Column 7 total
					cellG = row.createCell(12);
					if (record.getR45_tot_amt() != null) {
						cellG.setCellValue(record.getR45_tot_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// ================= R46 =================
					// row46
					row = sheet.getRow(40);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR46_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR46_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR46_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR46_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR46_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR46_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR46_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR46_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR46_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR46_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R47 =================
					// row47
					row = sheet.getRow(41);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR47_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR47_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR47_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR47_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR47_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR47_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR47_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR47_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR47_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR47_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// ================= R48 =================
					row = sheet.getRow(42);
					/*
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR48_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR48_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); } // Column 3 - name of sub2 cellD =
					 * row.createCell(4); if (record.getR48_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR48_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); } // Column 4 - name of sub3 cellE =
					 * row.createCell(6); if (record.getR48_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR48_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); } // Column 5 - name of sub4 cellF =
					 * row.createCell(8); if (record.getR48_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR48_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); } // Column 6 - name of sub5 cellG =
					 * row.createCell(10); if (record.getR48_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR48_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); } // Column 7 total cellG =
					 * row.createCell(12); if (record.getR48_tot_amt() != null) {
					 * cellG.setCellValue(record.getR48_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ================= R49 ================= // row49 row = sheet.getRow(43);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR49_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR49_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR49_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR49_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR49_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR49_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR49_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR49_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR49_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR49_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ================= R50 ================= // row50 row = sheet.getRow(44);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR50_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR50_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR50_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR50_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR50_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR50_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR50_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR50_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR50_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR50_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // ====================== R51 ====================== row = sheet.getRow(45);
					 * 
					 * 
					 * // Column 2 - name of sub1 cellC = row.createCell(2); if
					 * (record.getR51_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR51_amt_name_of_sub1().doubleValue());
					 * cellC.setCellStyle(numberStyle); } else { cellC.setCellValue("");
					 * cellC.setCellStyle(textStyle); }
					 * 
					 * // Column 3 - name of sub2 cellD = row.createCell(4); if
					 * (record.getR51_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR51_amt_name_of_sub2().doubleValue());
					 * cellD.setCellStyle(numberStyle); } else { cellD.setCellValue("");
					 * cellD.setCellStyle(textStyle); }
					 * 
					 * // Column 4 - name of sub3 cellE = row.createCell(6); if
					 * (record.getR51_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR51_amt_name_of_sub3().doubleValue());
					 * cellE.setCellStyle(numberStyle); } else { cellE.setCellValue("");
					 * cellE.setCellStyle(textStyle); }
					 * 
					 * // Column 5 - name of sub4 cellF = row.createCell(8); if
					 * (record.getR51_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR51_amt_name_of_sub4().doubleValue());
					 * cellF.setCellStyle(numberStyle); } else { cellF.setCellValue("");
					 * cellF.setCellStyle(textStyle); }
					 * 
					 * // Column 6 - name of sub5 cellG = row.createCell(10); if
					 * (record.getR51_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR51_amt_name_of_sub5().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * // Column 7 total cellG = row.createCell(12); if (record.getR51_tot_amt() !=
					 * null) { cellG.setCellValue(record.getR51_tot_amt().doubleValue());
					 * cellG.setCellStyle(numberStyle); } else { cellG.setCellValue("");
					 * cellG.setCellStyle(textStyle); }
					 * 
					 * 
					 * 
					 * // ====================== R52 ====================== // row52 row =
					 * sheet.getRow(46);
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR52_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR52_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR52_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR52_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR52_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR52_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR52_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR52_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR52_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR52_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 */

					// ====================== R53 ======================

					// row53
					row = sheet.getRow(44);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR53_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR53_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR53_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR53_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR53_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR53_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR53_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR53_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR53_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR53_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}

					// row54
					row = sheet.getRow(45);

					// Column 3 - name of sub1
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					if (record.getR54_amt_name_of_sub1() != null) {
						cellC.setCellValue(record.getR54_amt_name_of_sub1().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// Column 4 - name of sub2
					cellD = row.getCell(4);
					if (cellD == null)
						cellD = row.createCell(4);
					if (record.getR54_amt_name_of_sub2() != null) {
						cellD.setCellValue(record.getR54_amt_name_of_sub2().doubleValue());
					} else {
						cellD.setCellValue(0);
					}

					// Column 5 - name of sub3
					cellE = row.getCell(6);
					if (cellE == null)
						cellE = row.createCell(6);
					if (record.getR54_amt_name_of_sub3() != null) {
						cellE.setCellValue(record.getR54_amt_name_of_sub3().doubleValue());
					} else {
						cellE.setCellValue(0);
					}

					// Column 6 - name of sub4
					cellF = row.getCell(8);
					if (cellF == null)
						cellF = row.createCell(8);
					if (record.getR54_amt_name_of_sub4() != null) {
						cellF.setCellValue(record.getR54_amt_name_of_sub4().doubleValue());
					} else {
						cellF.setCellValue(0);
					}

					// Column 7 - name of sub5
					cellG = row.getCell(10);
					if (cellG == null)
						cellG = row.createCell(10);
					if (record.getR54_amt_name_of_sub5() != null) {
						cellG.setCellValue(record.getR54_amt_name_of_sub5().doubleValue());
					} else {
						cellG.setCellValue(0);
					}
					/*
					 * 
					 * 
					 * // row55 row = sheet.getRow(49);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR55_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR55_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR55_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR55_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR55_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR55_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR55_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR55_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR55_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR55_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row56 row = sheet.getRow(50);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR56_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR56_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR56_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR56_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR56_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR56_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR56_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR56_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR56_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR56_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * // row57 row = sheet.getRow(51);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR57_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR57_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR57_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR57_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR57_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR57_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR57_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR57_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR57_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR57_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 * 
					 * 
					 * 
					 * // row58 row = sheet.getRow(52);
					 * 
					 * 
					 * // Column 3 - name of sub1 cellC = row.getCell(2); if (cellC == null) cellC =
					 * row.createCell(2); if (record.getR58_amt_name_of_sub1() != null) {
					 * cellC.setCellValue(record.getR58_amt_name_of_sub1().doubleValue()); } else {
					 * cellC.setCellValue(0); }
					 * 
					 * // Column 4 - name of sub2 cellD = row.getCell(4); if (cellD == null) cellD =
					 * row.createCell(4); if (record.getR58_amt_name_of_sub2() != null) {
					 * cellD.setCellValue(record.getR58_amt_name_of_sub2().doubleValue()); } else {
					 * cellD.setCellValue(0); }
					 * 
					 * // Column 5 - name of sub3 cellE = row.getCell(6); if (cellE == null) cellE =
					 * row.createCell(6); if (record.getR58_amt_name_of_sub3() != null) {
					 * cellE.setCellValue(record.getR58_amt_name_of_sub3().doubleValue()); } else {
					 * cellE.setCellValue(0); }
					 * 
					 * // Column 6 - name of sub4 cellF = row.getCell(8); if (cellF == null) cellF =
					 * row.createCell(8); if (record.getR58_amt_name_of_sub4() != null) {
					 * cellF.setCellValue(record.getR58_amt_name_of_sub4().doubleValue()); } else {
					 * cellF.setCellValue(0); }
					 * 
					 * // Column 7 - name of sub5 cellG = row.getCell(10); if (cellG == null) cellG
					 * = row.createCell(10); if (record.getR58_amt_name_of_sub5() != null) {
					 * cellG.setCellValue(record.getR58_amt_name_of_sub5().doubleValue()); } else {
					 * cellG.setCellValue(0); }
					 */

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