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
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
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

import com.bornfire.brrs.entities.BRRS_M_SIR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SIR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SIR_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SIR_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SIR_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SIR_Summary_Repo;
import com.bornfire.brrs.entities.M_SIR_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SIR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SIR_Detail_Entity;
import com.bornfire.brrs.entities.M_SIR_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_SIR_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_SIR_Summary_Entity;

@Component
@Service

public class BRRS_M_SIR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SIR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SIR_Summary_Repo brrs_M_SIR_summary_repo;

	@Autowired
	BRRS_M_SIR_Detail_Repo brrs_M_SIR_detail_repo;

	@Autowired
	BRRS_M_SIR_Archival_Summary_Repo M_SIR_Archival_Summary_Repo;

	@Autowired
	BRRS_M_SIR_Archival_Detail_Repo BRRS_M_SIR_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SIR_Resub_Summary_Repo brrs_M_SIR_resub_summary_repo;

	@Autowired
	BRRS_M_SIR_Resub_Detail_Repo brrs_M_SIR_resub_detail_repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_SIRView(String reportId, String fromdate, String todate, String currency,
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
				List<M_SIR_Archival_Summary_Entity> T1Master = M_SIR_Archival_Summary_Repo.getdatabydateListarchival(d1,
						version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_SIR_Resub_Summary_Entity> T1Master = brrs_M_SIR_resub_summary_repo.getdatabydateListarchival(d1,
						version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_SIR_Summary_Entity> T1Master = brrs_M_SIR_summary_repo.getdatabydateList(d1);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_SIR_Archival_Detail_Entity> T1Master = BRRS_M_SIR_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_SIR_Resub_Detail_Entity> T1Master = brrs_M_SIR_resub_detail_repo
							.getdatabydateListarchival(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_SIR_Detail_Entity> T1Master = brrs_M_SIR_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SIR");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	@Transactional
	public void updateReport(M_SIR_Summary_Entity updatedEntity) {

		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// 🔹 Fetch existing SUMMARY
		M_SIR_Summary_Entity existingSummary = brrs_M_SIR_summary_repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		// 🔹 Fetch or create DETAIL
		M_SIR_Detail_Entity detailEntity = brrs_M_SIR_detail_repo.findById(updatedEntity.getReportDate())
				.orElseGet(() -> {
					M_SIR_Detail_Entity d = new M_SIR_Detail_Entity();
					d.setReportDate(updatedEntity.getReportDate());
					return d;
				});

		try {
			// 1️⃣ Loop from R11 to R50 and copy fields
			for (int i = 13; i <= 17; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
						"amt_gt24m", "risk_gt24m", "capital_gt24m" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}
			String[] totalFields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
					"amt_gt24m", "risk_gt24m", "capital_gt24m" };
			for (String field : totalFields) {
				String getterName = "getR12_" + field;
				String setterName = "setR12_" + field;

				try {
					// Getter from UPDATED entity
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);

					Object newValue = getter.invoke(updatedEntity);

					// SUMMARY setter
					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					summarySetter.invoke(existingSummary, newValue);

					// DETAIL setter
					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					// Skip if not present
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		try {

			for (int i = 19; i <= 23; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
						"amt_gt24m", "risk_gt24m", "capital_gt24m" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}
			String[] totalFields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
					"amt_gt24m", "risk_gt24m", "capital_gt24m" };
			for (String field : totalFields) {
				String getterName = "getR18_" + field;
				String setterName = "setR18_" + field;

				try {
					// Getter from UPDATED entity
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);

					Object newValue = getter.invoke(updatedEntity);

					// SUMMARY setter
					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					summarySetter.invoke(existingSummary, newValue);

					// DETAIL setter
					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					// Skip if not present
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// ------------------------------------------------------------------------
		try {

			for (int i = 24; i <= 26; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
						"amt_gt24m", "risk_gt24m", "capital_gt24m" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}
		}

		catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		try {

			for (int i = 28; i <= 32; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
						"amt_gt24m", "risk_gt24m", "capital_gt24m" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}
			String[] totalFields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
					"amt_gt24m", "risk_gt24m", "capital_gt24m" };
			for (String field : totalFields) {
				String getterName = "getR27_" + field;
				String setterName = "setR27_" + field;

				try {
					// Getter from UPDATED entity
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);

					Object newValue = getter.invoke(updatedEntity);

					// SUMMARY setter
					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					summarySetter.invoke(existingSummary, newValue);

					// DETAIL setter
					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					// Skip if not present
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		try {
			String[] fields = { "capital_6m", "capital_6to24m", "capital_gt24m" };

			String prefix = "R33_";

			for (String field : fields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					// Getter from UPDATED entity
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);

					Object newValue = getter.invoke(updatedEntity);

					// SUMMARY setter
					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					summarySetter.invoke(existingSummary, newValue);

					// DETAIL setter
					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing fields
					continue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating R33 fields", e);
		}
		try {
			String[] fields = { "tot_spec_risk_ch" }; // 👈 only the suffix

			String prefix = "R35_";

			for (String field : fields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					// Getter from UPDATED entity
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);

					Object newValue = getter.invoke(updatedEntity);

					// SUMMARY setter
					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					summarySetter.invoke(existingSummary, newValue);

					// DETAIL setter
					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing fields
					continue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating R35 fields", e);
		}

		// 3️⃣ Save updated entity
		brrs_M_SIR_summary_repo.save(existingSummary);
		brrs_M_SIR_detail_repo.save(detailEntity);
	}

	public void updateResubReport(M_SIR_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = brrs_M_SIR_resub_summary_repo.findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_SIR_Resub_Summary_Entity resubSummary = new M_SIR_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_SIR_Resub_Detail_Entity resubDetail = new M_SIR_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_SIR_Archival_Summary_Entity archSummary = new M_SIR_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_SIR_Archival_Detail_Entity archDetail = new M_SIR_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		brrs_M_SIR_resub_summary_repo.save(resubSummary);
		brrs_M_SIR_resub_detail_repo.save(resubDetail);

		M_SIR_Archival_Summary_Repo.save(archSummary);
		BRRS_M_SIR_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_SIRResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_SIR_Archival_Summary_Entity> latestArchivalList = M_SIR_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SIR_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SIR Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getM_SIRArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SIR_Archival_Summary_Entity> repoData = M_SIR_Archival_Summary_Repo.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SIR_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SIR_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SIR Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] BRRS_M_SIRExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_SIRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SIRResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SIREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_SIR_Summary_Entity> dataList = brrs_M_SIR_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SIR report. Returning empty result.");
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
					// Create pure light green style (Excel highlight green)
					XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
					greenStyle.cloneStyleFrom(textStyle);

					byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
					XSSFColor green = new XSSFColor(rgb, null);

					greenStyle.setFillForegroundColor(green);
					greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

					greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
					CellStyle percentStyle = workbook.createCellStyle();
					percentStyle.cloneStyleFrom(numberStyle);
					percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
					percentStyle.setAlignment(HorizontalAlignment.RIGHT);
					int startRow = 11;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_SIR_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
//NORMAL

							// row11
							// Column C
							row = sheet.getRow(11);

							Cell cell1 = row.createCell(2);
							if (record.getR12_amt_6m() != null) {
								cell1.setCellValue(record.getR12_amt_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row11
							// Column E
							cell1 = row.createCell(4);
							if (record.getR12_capital_6m() != null) {
								cell1.setCellValue(record.getR12_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row11
							// Column F

							cell1 = row.createCell(5);
							if (record.getR12_amt_6to24m() != null) {
								cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row11
							// Column H
							cell1 = row.createCell(7);
							if (record.getR12_capital_6to24m() != null) {
								cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row11
							// Column I

							cell1 = row.createCell(8);
							if (record.getR12_amt_gt24m() != null) {
								cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// row11
							// Column K
							cell1 = row.createCell(10);
							if (record.getR12_capital_gt24m() != null) {
								cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
//-------------

							// row12
							// Column b
							row = sheet.getRow(12);

							// row12
							// Column C

							cell1 = row.getCell(2);
							if (record.getR13_amt_6m() != null) {
								cell1.setCellValue(record.getR13_amt_6m().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.createCell(4);
							if (record.getR13_capital_6m() != null) {
								cell1.setCellValue(record.getR13_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR13_amt_6to24m() != null) {
								cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.createCell(7);
							if (record.getR13_capital_6to24m() != null) {
								cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR13_amt_gt24m() != null) {
								cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							cell1 = row.createCell(10);
							if (record.getR13_capital_gt24m() != null) {
								cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// row13
							// Column b
							row = sheet.getRow(13);

							// row12
							// Column C

							cell1 = row.getCell(2);
							if (record.getR14_amt_6m() != null) {
								cell1.setCellValue(record.getR14_amt_6m().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.createCell(4);
							if (record.getR14_capital_6m() != null) {
								cell1.setCellValue(record.getR14_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR14_amt_6to24m() != null) {
								cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.createCell(7);
							if (record.getR14_capital_6to24m() != null) {
								cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR14_amt_gt24m() != null) {
								cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							cell1 = row.createCell(10);
							if (record.getR14_capital_gt24m() != null) {
								cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// row14
							// Column b
							row = sheet.getRow(14);

							// row12
							// Column C

							cell1 = row.getCell(2);
							if (record.getR15_amt_6m() != null) {
								cell1.setCellValue(record.getR15_amt_6m().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.createCell(4);
							if (record.getR15_capital_6m() != null) {
								cell1.setCellValue(record.getR15_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR15_amt_6to24m() != null) {
								cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell1 = row.createCell(7);
							if (record.getR15_capital_6to24m() != null) {
								cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR15_amt_gt24m() != null) {
								cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							cell1 = row.createCell(10);
							if (record.getR15_capital_gt24m() != null) {
								cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
//				//row15  (R16)
							// Column B
							row = sheet.getRow(15);
							cell1 = row.getCell(1);

							// Column C
							cell1 = row.getCell(2);
							if (record.getR16_amt_6m() != null) {
								cell1.setCellValue(record.getR16_amt_6m().doubleValue());
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR16_capital_6m() != null) {
								cell1.setCellValue(record.getR16_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR16_amt_6to24m() != null) {
								cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR16_capital_6to24m() != null) {
								cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR16_amt_gt24m() != null) {
								cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR16_capital_gt24m() != null) {
								cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row16 (R17)
							row = sheet.getRow(16);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR17_amt_6m() != null)
								cell1.setCellValue(record.getR17_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR17_capital_6m() != null) {
								cell1.setCellValue(record.getR17_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR17_amt_6to24m() != null)
								cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR17_capital_6to24m() != null) {
								cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR17_amt_gt24m() != null)
								cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR17_capital_gt24m() != null) {
								cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row17 (R18)
							row = sheet.getRow(17);

							// Column B

							// Column C

							cell1 = row.createCell(2);
							if (record.getR18_amt_6m() != null) {
								cell1.setCellValue(record.getR18_amt_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// Column E
							cell1 = row.createCell(4);
							if (record.getR18_capital_6m() != null) {
								cell1.setCellValue(record.getR18_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F

							cell1 = row.createCell(5);
							if (record.getR18_amt_6to24m() != null) {
								cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// Column H
							cell1 = row.createCell(7);
							if (record.getR18_capital_6to24m() != null) {
								cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I

							cell1 = row.createCell(8);
							if (record.getR18_amt_gt24m() != null) {
								cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// Column K
							cell1 = row.createCell(10);
							if (record.getR18_capital_gt24m() != null) {
								cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row18 (R19)
							row = sheet.getRow(18);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR19_amt_6m() != null)
								cell1.setCellValue(record.getR19_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR19_capital_6m() != null) {
								cell1.setCellValue(record.getR19_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR19_amt_6to24m() != null)
								cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR19_capital_6to24m() != null) {
								cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR19_amt_gt24m() != null)
								cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR19_capital_gt24m() != null) {
								cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row19 (R20)
							row = sheet.getRow(19);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR20_amt_6m() != null)
								cell1.setCellValue(record.getR20_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR20_capital_6m() != null) {
								cell1.setCellValue(record.getR20_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR20_amt_6to24m() != null)
								cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR20_capital_6to24m() != null) {
								cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR20_amt_gt24m() != null)
								cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR20_capital_gt24m() != null) {
								cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row20 (R21)
							row = sheet.getRow(20);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR21_amt_6m() != null)
								cell1.setCellValue(record.getR21_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR21_capital_6m() != null) {
								cell1.setCellValue(record.getR21_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR21_amt_6to24m() != null)
								cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR21_capital_6to24m() != null) {
								cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR21_amt_gt24m() != null)
								cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR21_capital_gt24m() != null) {
								cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row21 (R22)
							row = sheet.getRow(21);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR22_amt_6m() != null)
								cell1.setCellValue(record.getR22_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR22_capital_6m() != null) {
								cell1.setCellValue(record.getR22_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR22_amt_6to24m() != null)
								cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR22_capital_6to24m() != null) {
								cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR22_amt_gt24m() != null)
								cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR22_capital_gt24m() != null) {
								cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row22 (R23)
							row = sheet.getRow(22);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR23_amt_6m() != null)
								cell1.setCellValue(record.getR23_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR23_capital_6m() != null) {
								cell1.setCellValue(record.getR23_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR23_amt_6to24m() != null)
								cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR23_capital_6to24m() != null) {
								cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR23_amt_gt24m() != null)
								cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR23_capital_gt24m() != null) {
								cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row23 (R24)
							row = sheet.getRow(23);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR24_amt_6m() != null)
								cell1.setCellValue(record.getR24_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR24_capital_6m() != null) {
								cell1.setCellValue(record.getR24_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR24_amt_6to24m() != null)
								cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR24_capital_6to24m() != null) {
								cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR24_amt_gt24m() != null)
								cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR24_capital_gt24m() != null) {
								cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row24 (R25)
							row = sheet.getRow(24);

							// Column C
							cell1 = row.getCell(2);
							if (record.getR25_amt_6m() != null)
								cell1.setCellValue(record.getR25_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR25_capital_6m() != null) {
								cell1.setCellValue(record.getR25_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR25_amt_6to24m() != null)
								cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR25_capital_6to24m() != null) {
								cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR25_amt_gt24m() != null)
								cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR25_capital_gt24m() != null) {
								cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row25 (R26)
							row = sheet.getRow(25);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR26_amt_6m() != null)
								cell1.setCellValue(record.getR26_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR26_capital_6m() != null) {
								cell1.setCellValue(record.getR26_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR26_amt_6to24m() != null)
								cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR26_capital_6to24m() != null) {
								cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR26_amt_gt24m() != null)
								cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR26_capital_gt24m() != null) {
								cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row26 (R27)
							row = sheet.getRow(26);

							// Column B

							// Column C

							cell1 = row.createCell(2);
							if (record.getR27_amt_6m() != null) {
								cell1.setCellValue(record.getR27_amt_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// Column E
							cell1 = row.createCell(4);
							if (record.getR27_capital_6m() != null) {
								cell1.setCellValue(record.getR27_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F

							cell1 = row.createCell(5);
							if (record.getR27_amt_6to24m() != null) {
								cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// Column H
							cell1 = row.createCell(7);
							if (record.getR27_capital_6to24m() != null) {
								cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I

							cell1 = row.createCell(8);
							if (record.getR27_amt_gt24m() != null) {
								cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// Column K
							cell1 = row.createCell(10);
							if (record.getR27_capital_gt24m() != null) {
								cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row27 (R28)
							row = sheet.getRow(27);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR28_amt_6m() != null)
								cell1.setCellValue(record.getR28_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR28_capital_6m() != null) {
								cell1.setCellValue(record.getR28_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR28_amt_6to24m() != null)
								cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR28_capital_6to24m() != null) {
								cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR28_amt_gt24m() != null)
								cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR28_capital_gt24m() != null) {
								cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row28 (R29)
							row = sheet.getRow(28);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR29_amt_6m() != null)
								cell1.setCellValue(record.getR29_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR29_capital_6m() != null) {
								cell1.setCellValue(record.getR29_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR29_amt_6to24m() != null)
								cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR29_capital_6to24m() != null) {
								cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR29_amt_gt24m() != null)
								cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR29_capital_gt24m() != null) {
								cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row29 (R30)
							row = sheet.getRow(29);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR30_amt_6m() != null)
								cell1.setCellValue(record.getR30_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR30_capital_6m() != null) {
								cell1.setCellValue(record.getR30_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR30_amt_6to24m() != null)
								cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR30_capital_6to24m() != null) {
								cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR30_amt_gt24m() != null)
								cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR30_capital_gt24m() != null) {
								cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row30 (R31)
							row = sheet.getRow(30);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR31_amt_6m() != null)
								cell1.setCellValue(record.getR31_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR31_capital_6m() != null) {
								cell1.setCellValue(record.getR31_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR31_amt_6to24m() != null)
								cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR31_capital_6to24m() != null) {
								cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR31_amt_gt24m() != null)
								cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR31_capital_gt24m() != null) {
								cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row31 (R32)
							row = sheet.getRow(31);

							// Column B

							// Column C
							cell1 = row.getCell(2);
							if (record.getR32_amt_6m() != null)
								cell1.setCellValue(record.getR32_amt_6m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column E
							cell1 = row.createCell(4);
							if (record.getR32_capital_6m() != null) {
								cell1.setCellValue(record.getR32_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column F
							cell1 = row.getCell(5);
							if (record.getR32_amt_6to24m() != null)
								cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR32_capital_6to24m() != null) {
								cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column I
							cell1 = row.getCell(8);
							if (record.getR32_amt_gt24m() != null)
								cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
							else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR32_capital_gt24m() != null) {
								cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row32 (R33)
							row = sheet.getRow(32);

							// Column B

							// Column E
							cell1 = row.createCell(4);
							if (record.getR33_capital_6m() != null) {
								cell1.setCellValue(record.getR33_capital_6m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column H
							cell1 = row.createCell(7);
							if (record.getR33_capital_6to24m() != null) {
								cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// Column K
							cell1 = row.createCell(10);
							if (record.getR33_capital_gt24m() != null) {
								cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}
							// row34
							// Column K
							row = sheet.getRow(34);
							cell1 = row.createCell(4);
							if (record.getR35_tot_spec_risk_ch() != null) {
								cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
								cell1.setCellStyle(greenStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SIREmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SIREmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SIRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_SIR_Summary_Entity> dataList = brrs_M_SIR_summary_repo.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_SIR report. Returning empty result.");
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
				// Create pure light green style (Excel highlight green)
				XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
				greenStyle.cloneStyleFrom(textStyle);

				byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
				XSSFColor green = new XSSFColor(rgb, null);

				greenStyle.setFillForegroundColor(green);
				greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
				CellStyle percentStyle = workbook.createCellStyle();
				percentStyle.cloneStyleFrom(numberStyle);
				percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
				percentStyle.setAlignment(HorizontalAlignment.RIGHT);
				int startRow = 11;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_SIR_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL

						// row11
						// Column C
						row = sheet.getRow(11);

						Cell cell1 = row.createCell(2);
						if (record.getR12_amt_6m() != null) {
							cell1.setCellValue(record.getR12_amt_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column E
						cell1 = row.createCell(4);
						if (record.getR12_capital_6m() != null) {
							cell1.setCellValue(record.getR12_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column F

						cell1 = row.createCell(5);
						if (record.getR12_amt_6to24m() != null) {
							cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column H
						cell1 = row.createCell(7);
						if (record.getR12_capital_6to24m() != null) {
							cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row11
						// Column I

						cell1 = row.createCell(8);
						if (record.getR12_amt_gt24m() != null) {
							cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row11
						// Column K
						cell1 = row.createCell(10);
						if (record.getR12_capital_gt24m() != null) {
							cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// -------------

						// row12
						// Column b
						row = sheet.getRow(12);

						// row12
						// Column C

						cell1 = row.getCell(2);
						if (record.getR13_amt_6m() != null) {
							cell1.setCellValue(record.getR13_amt_6m().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.createCell(4);
						if (record.getR13_capital_6m() != null) {
							cell1.setCellValue(record.getR13_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR13_amt_6to24m() != null) {
							cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.createCell(7);
						if (record.getR13_capital_6to24m() != null) {
							cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR13_amt_gt24m() != null) {
							cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						cell1 = row.createCell(10);
						if (record.getR13_capital_gt24m() != null) {
							cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row13
						// Column b
						row = sheet.getRow(13);

						// row12
						// Column C

						cell1 = row.getCell(2);
						if (record.getR14_amt_6m() != null) {
							cell1.setCellValue(record.getR14_amt_6m().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.createCell(4);
						if (record.getR14_capital_6m() != null) {
							cell1.setCellValue(record.getR14_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR14_amt_6to24m() != null) {
							cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.createCell(7);
						if (record.getR14_capital_6to24m() != null) {
							cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR14_amt_gt24m() != null) {
							cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						cell1 = row.createCell(10);
						if (record.getR14_capital_gt24m() != null) {
							cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// row14
						// Column b
						row = sheet.getRow(14);

						// row12
						// Column C

						cell1 = row.getCell(2);
						if (record.getR15_amt_6m() != null) {
							cell1.setCellValue(record.getR15_amt_6m().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.createCell(4);
						if (record.getR15_capital_6m() != null) {
							cell1.setCellValue(record.getR15_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR15_amt_6to24m() != null) {
							cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell1 = row.createCell(7);
						if (record.getR15_capital_6to24m() != null) {
							cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR15_amt_gt24m() != null) {
							cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						cell1 = row.createCell(10);
						if (record.getR15_capital_gt24m() != null) {
							cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
//					//row15  (R16)
						// Column B
						row = sheet.getRow(15);
						cell1 = row.getCell(1);

						// Column C
						cell1 = row.getCell(2);
						if (record.getR16_amt_6m() != null) {
							cell1.setCellValue(record.getR16_amt_6m().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR16_capital_6m() != null) {
							cell1.setCellValue(record.getR16_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR16_amt_6to24m() != null) {
							cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR16_capital_6to24m() != null) {
							cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR16_amt_gt24m() != null) {
							cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR16_capital_gt24m() != null) {
							cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row16 (R17)
						row = sheet.getRow(16);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR17_amt_6m() != null)
							cell1.setCellValue(record.getR17_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR17_capital_6m() != null) {
							cell1.setCellValue(record.getR17_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR17_amt_6to24m() != null)
							cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR17_capital_6to24m() != null) {
							cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR17_amt_gt24m() != null)
							cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR17_capital_gt24m() != null) {
							cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row17 (R18)
						row = sheet.getRow(17);

						// Column B

						// Column C

						cell1 = row.createCell(2);
						if (record.getR18_amt_6m() != null) {
							cell1.setCellValue(record.getR18_amt_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// Column E
						cell1 = row.createCell(4);
						if (record.getR18_capital_6m() != null) {
							cell1.setCellValue(record.getR18_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F

						cell1 = row.createCell(5);
						if (record.getR18_amt_6to24m() != null) {
							cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// Column H
						cell1 = row.createCell(7);
						if (record.getR18_capital_6to24m() != null) {
							cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I

						cell1 = row.createCell(8);
						if (record.getR18_amt_gt24m() != null) {
							cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// Column K
						cell1 = row.createCell(10);
						if (record.getR18_capital_gt24m() != null) {
							cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row18 (R19)
						row = sheet.getRow(18);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR19_amt_6m() != null)
							cell1.setCellValue(record.getR19_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR19_capital_6m() != null) {
							cell1.setCellValue(record.getR19_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR19_amt_6to24m() != null)
							cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR19_capital_6to24m() != null) {
							cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR19_amt_gt24m() != null)
							cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR19_capital_gt24m() != null) {
							cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row19 (R20)
						row = sheet.getRow(19);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR20_amt_6m() != null)
							cell1.setCellValue(record.getR20_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR20_capital_6m() != null) {
							cell1.setCellValue(record.getR20_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR20_amt_6to24m() != null)
							cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR20_capital_6to24m() != null) {
							cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR20_amt_gt24m() != null)
							cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR20_capital_gt24m() != null) {
							cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row20 (R21)
						row = sheet.getRow(20);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR21_amt_6m() != null)
							cell1.setCellValue(record.getR21_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR21_capital_6m() != null) {
							cell1.setCellValue(record.getR21_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR21_amt_6to24m() != null)
							cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR21_capital_6to24m() != null) {
							cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR21_amt_gt24m() != null)
							cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR21_capital_gt24m() != null) {
							cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row21 (R22)
						row = sheet.getRow(21);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR22_amt_6m() != null)
							cell1.setCellValue(record.getR22_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR22_capital_6m() != null) {
							cell1.setCellValue(record.getR22_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR22_amt_6to24m() != null)
							cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR22_capital_6to24m() != null) {
							cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR22_amt_gt24m() != null)
							cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR22_capital_gt24m() != null) {
							cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row22 (R23)
						row = sheet.getRow(22);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR23_amt_6m() != null)
							cell1.setCellValue(record.getR23_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR23_capital_6m() != null) {
							cell1.setCellValue(record.getR23_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR23_amt_6to24m() != null)
							cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR23_capital_6to24m() != null) {
							cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR23_amt_gt24m() != null)
							cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR23_capital_gt24m() != null) {
							cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row23 (R24)
						row = sheet.getRow(23);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR24_amt_6m() != null)
							cell1.setCellValue(record.getR24_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR24_capital_6m() != null) {
							cell1.setCellValue(record.getR24_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR24_amt_6to24m() != null)
							cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR24_capital_6to24m() != null) {
							cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR24_amt_gt24m() != null)
							cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR24_capital_gt24m() != null) {
							cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row24 (R25)
						row = sheet.getRow(24);

						// Column C
						cell1 = row.getCell(2);
						if (record.getR25_amt_6m() != null)
							cell1.setCellValue(record.getR25_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR25_capital_6m() != null) {
							cell1.setCellValue(record.getR25_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR25_amt_6to24m() != null)
							cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR25_capital_6to24m() != null) {
							cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR25_amt_gt24m() != null)
							cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR25_capital_gt24m() != null) {
							cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row25 (R26)
						row = sheet.getRow(25);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR26_amt_6m() != null)
							cell1.setCellValue(record.getR26_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR26_capital_6m() != null) {
							cell1.setCellValue(record.getR26_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR26_amt_6to24m() != null)
							cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR26_capital_6to24m() != null) {
							cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR26_amt_gt24m() != null)
							cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR26_capital_gt24m() != null) {
							cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row26 (R27)
						row = sheet.getRow(26);

						// Column B

						// Column C

						cell1 = row.createCell(2);
						if (record.getR27_amt_6m() != null) {
							cell1.setCellValue(record.getR27_amt_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// Column E
						cell1 = row.createCell(4);
						if (record.getR27_capital_6m() != null) {
							cell1.setCellValue(record.getR27_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F

						cell1 = row.createCell(5);
						if (record.getR27_amt_6to24m() != null) {
							cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// Column H
						cell1 = row.createCell(7);
						if (record.getR27_capital_6to24m() != null) {
							cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I

						cell1 = row.createCell(8);
						if (record.getR27_amt_gt24m() != null) {
							cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// Column K
						cell1 = row.createCell(10);
						if (record.getR27_capital_gt24m() != null) {
							cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row27 (R28)
						row = sheet.getRow(27);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR28_amt_6m() != null)
							cell1.setCellValue(record.getR28_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR28_capital_6m() != null) {
							cell1.setCellValue(record.getR28_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR28_amt_6to24m() != null)
							cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR28_capital_6to24m() != null) {
							cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR28_amt_gt24m() != null)
							cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR28_capital_gt24m() != null) {
							cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row28 (R29)
						row = sheet.getRow(28);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR29_amt_6m() != null)
							cell1.setCellValue(record.getR29_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR29_capital_6m() != null) {
							cell1.setCellValue(record.getR29_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR29_amt_6to24m() != null)
							cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR29_capital_6to24m() != null) {
							cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR29_amt_gt24m() != null)
							cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR29_capital_gt24m() != null) {
							cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row29 (R30)
						row = sheet.getRow(29);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR30_amt_6m() != null)
							cell1.setCellValue(record.getR30_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR30_capital_6m() != null) {
							cell1.setCellValue(record.getR30_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR30_amt_6to24m() != null)
							cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR30_capital_6to24m() != null) {
							cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR30_amt_gt24m() != null)
							cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR30_capital_gt24m() != null) {
							cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row30 (R31)
						row = sheet.getRow(30);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR31_amt_6m() != null)
							cell1.setCellValue(record.getR31_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR31_capital_6m() != null) {
							cell1.setCellValue(record.getR31_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR31_amt_6to24m() != null)
							cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR31_capital_6to24m() != null) {
							cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR31_amt_gt24m() != null)
							cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR31_capital_gt24m() != null) {
							cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row31 (R32)
						row = sheet.getRow(31);

						// Column B

						// Column C
						cell1 = row.getCell(2);
						if (record.getR32_amt_6m() != null)
							cell1.setCellValue(record.getR32_amt_6m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column E
						cell1 = row.createCell(4);
						if (record.getR32_capital_6m() != null) {
							cell1.setCellValue(record.getR32_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column F
						cell1 = row.getCell(5);
						if (record.getR32_amt_6to24m() != null)
							cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR32_capital_6to24m() != null) {
							cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column I
						cell1 = row.getCell(8);
						if (record.getR32_amt_gt24m() != null)
							cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
						else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR32_capital_gt24m() != null) {
							cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row32 (R33)
						row = sheet.getRow(32);

						// Column B

						// Column E
						cell1 = row.createCell(4);
						if (record.getR33_capital_6m() != null) {
							cell1.setCellValue(record.getR33_capital_6m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column H
						cell1 = row.createCell(7);
						if (record.getR33_capital_6to24m() != null) {
							cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// Column K
						cell1 = row.createCell(10);
						if (record.getR33_capital_gt24m() != null) {
							cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}
						// row34
						// Column K
						row = sheet.getRow(34);
						cell1 = row.createCell(4);
						if (record.getR35_tot_spec_risk_ch() != null) {
							cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
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
	public byte[] getExcelM_SIRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SIREmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SIR_Archival_Summary_Entity> dataList = M_SIR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SIR report. Returning empty result.");
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
			// Create pure light green style (Excel highlight green)
			XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
			greenStyle.cloneStyleFrom(textStyle);

			byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
			XSSFColor green = new XSSFColor(rgb, null);

			greenStyle.setFillForegroundColor(green);
			greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL

					// row11
					// Column C
					row = sheet.getRow(11);

					Cell cell1 = row.createCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.createCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F

					cell1 = row.createCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column H
					cell1 = row.createCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column I

					cell1 = row.createCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cell1 = row.createCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//-------------

					// row12
					// Column b
					row = sheet.getRow(12);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row13
					// Column b
					row = sheet.getRow(13);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row14
					// Column b
					row = sheet.getRow(14);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//				//row15  (R16)
					// Column B
					row = sheet.getRow(15);
					cell1 = row.getCell(1);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row16 (R17)
					row = sheet.getRow(16);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR17_amt_6m() != null)
						cell1.setCellValue(record.getR17_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR17_capital_6m() != null) {
						cell1.setCellValue(record.getR17_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR17_amt_6to24m() != null)
						cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR17_capital_6to24m() != null) {
						cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR17_amt_gt24m() != null)
						cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR17_capital_gt24m() != null) {
						cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row17 (R18)
					row = sheet.getRow(17);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR18_amt_6m() != null) {
						cell1.setCellValue(record.getR18_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR18_capital_6m() != null) {
						cell1.setCellValue(record.getR18_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR18_amt_6to24m() != null) {
						cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR18_capital_6to24m() != null) {
						cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR18_amt_gt24m() != null) {
						cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR18_capital_gt24m() != null) {
						cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row18 (R19)
					row = sheet.getRow(18);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR19_amt_6m() != null)
						cell1.setCellValue(record.getR19_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR19_capital_6m() != null) {
						cell1.setCellValue(record.getR19_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR19_amt_6to24m() != null)
						cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR19_capital_6to24m() != null) {
						cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR19_amt_gt24m() != null)
						cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR19_capital_gt24m() != null) {
						cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19 (R20)
					row = sheet.getRow(19);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR20_amt_6m() != null)
						cell1.setCellValue(record.getR20_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR20_capital_6m() != null) {
						cell1.setCellValue(record.getR20_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR20_amt_6to24m() != null)
						cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR20_capital_6to24m() != null) {
						cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR20_amt_gt24m() != null)
						cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR20_capital_gt24m() != null) {
						cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row20 (R21)
					row = sheet.getRow(20);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR21_amt_6m() != null)
						cell1.setCellValue(record.getR21_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR21_capital_6m() != null) {
						cell1.setCellValue(record.getR21_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR21_amt_6to24m() != null)
						cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR21_capital_6to24m() != null) {
						cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR21_amt_gt24m() != null)
						cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR21_capital_gt24m() != null) {
						cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21 (R22)
					row = sheet.getRow(21);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR22_amt_6m() != null)
						cell1.setCellValue(record.getR22_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR22_capital_6m() != null) {
						cell1.setCellValue(record.getR22_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR22_amt_6to24m() != null)
						cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR22_capital_6to24m() != null) {
						cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR22_amt_gt24m() != null)
						cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR22_capital_gt24m() != null) {
						cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22 (R23)
					row = sheet.getRow(22);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR23_amt_6m() != null)
						cell1.setCellValue(record.getR23_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR23_capital_6m() != null) {
						cell1.setCellValue(record.getR23_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR23_amt_6to24m() != null)
						cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR23_capital_6to24m() != null) {
						cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR23_amt_gt24m() != null)
						cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR23_capital_gt24m() != null) {
						cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row23 (R24)
					row = sheet.getRow(23);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR24_amt_6m() != null)
						cell1.setCellValue(record.getR24_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR24_capital_6m() != null) {
						cell1.setCellValue(record.getR24_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR24_amt_6to24m() != null)
						cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR24_capital_6to24m() != null) {
						cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR24_amt_gt24m() != null)
						cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR24_capital_gt24m() != null) {
						cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row24 (R25)
					row = sheet.getRow(24);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR25_amt_6m() != null)
						cell1.setCellValue(record.getR25_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR25_capital_6m() != null) {
						cell1.setCellValue(record.getR25_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR25_amt_6to24m() != null)
						cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR25_capital_6to24m() != null) {
						cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR25_amt_gt24m() != null)
						cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR25_capital_gt24m() != null) {
						cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row25 (R26)
					row = sheet.getRow(25);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR26_amt_6m() != null)
						cell1.setCellValue(record.getR26_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR26_capital_6m() != null) {
						cell1.setCellValue(record.getR26_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR26_amt_6to24m() != null)
						cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR26_capital_6to24m() != null) {
						cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR26_amt_gt24m() != null)
						cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR26_capital_gt24m() != null) {
						cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row26 (R27)
					row = sheet.getRow(26);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR27_amt_6m() != null) {
						cell1.setCellValue(record.getR27_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR27_capital_6m() != null) {
						cell1.setCellValue(record.getR27_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR27_amt_6to24m() != null) {
						cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR27_capital_6to24m() != null) {
						cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR27_amt_gt24m() != null) {
						cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR27_capital_gt24m() != null) {
						cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row27 (R28)
					row = sheet.getRow(27);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR28_amt_6m() != null)
						cell1.setCellValue(record.getR28_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR28_capital_6m() != null) {
						cell1.setCellValue(record.getR28_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR28_amt_6to24m() != null)
						cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR28_capital_6to24m() != null) {
						cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR28_amt_gt24m() != null)
						cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR28_capital_gt24m() != null) {
						cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row28 (R29)
					row = sheet.getRow(28);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR29_amt_6m() != null)
						cell1.setCellValue(record.getR29_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR29_capital_6m() != null) {
						cell1.setCellValue(record.getR29_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR29_amt_6to24m() != null)
						cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR29_capital_6to24m() != null) {
						cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR29_amt_gt24m() != null)
						cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR29_capital_gt24m() != null) {
						cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row29 (R30)
					row = sheet.getRow(29);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR30_amt_6m() != null)
						cell1.setCellValue(record.getR30_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR30_capital_6m() != null) {
						cell1.setCellValue(record.getR30_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR30_amt_6to24m() != null)
						cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR30_capital_6to24m() != null) {
						cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR30_amt_gt24m() != null)
						cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR30_capital_gt24m() != null) {
						cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row30 (R31)
					row = sheet.getRow(30);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR31_amt_6m() != null)
						cell1.setCellValue(record.getR31_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR31_capital_6m() != null) {
						cell1.setCellValue(record.getR31_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR31_amt_6to24m() != null)
						cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR31_capital_6to24m() != null) {
						cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR31_amt_gt24m() != null)
						cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR31_capital_gt24m() != null) {
						cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row31 (R32)
					row = sheet.getRow(31);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR32_amt_6m() != null)
						cell1.setCellValue(record.getR32_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR32_capital_6m() != null) {
						cell1.setCellValue(record.getR32_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR32_amt_6to24m() != null)
						cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR32_capital_6to24m() != null) {
						cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR32_amt_gt24m() != null)
						cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR32_capital_gt24m() != null) {
						cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row32 (R33)
					row = sheet.getRow(32);

					// Column B

					// Column E
					cell1 = row.createCell(4);
					if (record.getR33_capital_6m() != null) {
						cell1.setCellValue(record.getR33_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR33_capital_6to24m() != null) {
						cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR33_capital_gt24m() != null) {
						cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row34
					// Column K
					row = sheet.getRow(34);
					cell1 = row.createCell(4);
					if (record.getR35_tot_spec_risk_ch() != null) {
						cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SIREmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SIR_Archival_Summary_Entity> dataList = M_SIR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SIR report. Returning empty result.");
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
			// Create pure light green style (Excel highlight green)
			XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
			greenStyle.cloneStyleFrom(textStyle);

			byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
			XSSFColor green = new XSSFColor(rgb, null);

			greenStyle.setFillForegroundColor(green);
			greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL

					// row11
					// Column C
					row = sheet.getRow(11);

					Cell cell1 = row.createCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.createCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F

					cell1 = row.createCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column H
					cell1 = row.createCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column I

					cell1 = row.createCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cell1 = row.createCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// -------------

					// row12
					// Column b
					row = sheet.getRow(12);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row13
					// Column b
					row = sheet.getRow(13);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row14
					// Column b
					row = sheet.getRow(14);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//					//row15  (R16)
					// Column B
					row = sheet.getRow(15);
					cell1 = row.getCell(1);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row16 (R17)
					row = sheet.getRow(16);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR17_amt_6m() != null)
						cell1.setCellValue(record.getR17_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR17_capital_6m() != null) {
						cell1.setCellValue(record.getR17_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR17_amt_6to24m() != null)
						cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR17_capital_6to24m() != null) {
						cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR17_amt_gt24m() != null)
						cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR17_capital_gt24m() != null) {
						cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row17 (R18)
					row = sheet.getRow(17);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR18_amt_6m() != null) {
						cell1.setCellValue(record.getR18_amt_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR18_capital_6m() != null) {
						cell1.setCellValue(record.getR18_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR18_amt_6to24m() != null) {
						cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR18_capital_6to24m() != null) {
						cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR18_amt_gt24m() != null) {
						cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR18_capital_gt24m() != null) {
						cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row18 (R19)
					row = sheet.getRow(18);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR19_amt_6m() != null)
						cell1.setCellValue(record.getR19_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR19_capital_6m() != null) {
						cell1.setCellValue(record.getR19_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR19_amt_6to24m() != null)
						cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR19_capital_6to24m() != null) {
						cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR19_amt_gt24m() != null)
						cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR19_capital_gt24m() != null) {
						cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19 (R20)
					row = sheet.getRow(19);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR20_amt_6m() != null)
						cell1.setCellValue(record.getR20_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR20_capital_6m() != null) {
						cell1.setCellValue(record.getR20_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR20_amt_6to24m() != null)
						cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR20_capital_6to24m() != null) {
						cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR20_amt_gt24m() != null)
						cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR20_capital_gt24m() != null) {
						cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row20 (R21)
					row = sheet.getRow(20);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR21_amt_6m() != null)
						cell1.setCellValue(record.getR21_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR21_capital_6m() != null) {
						cell1.setCellValue(record.getR21_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR21_amt_6to24m() != null)
						cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR21_capital_6to24m() != null) {
						cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR21_amt_gt24m() != null)
						cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR21_capital_gt24m() != null) {
						cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21 (R22)
					row = sheet.getRow(21);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR22_amt_6m() != null)
						cell1.setCellValue(record.getR22_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR22_capital_6m() != null) {
						cell1.setCellValue(record.getR22_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR22_amt_6to24m() != null)
						cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR22_capital_6to24m() != null) {
						cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR22_amt_gt24m() != null)
						cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR22_capital_gt24m() != null) {
						cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22 (R23)
					row = sheet.getRow(22);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR23_amt_6m() != null)
						cell1.setCellValue(record.getR23_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR23_capital_6m() != null) {
						cell1.setCellValue(record.getR23_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR23_amt_6to24m() != null)
						cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR23_capital_6to24m() != null) {
						cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR23_amt_gt24m() != null)
						cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR23_capital_gt24m() != null) {
						cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row23 (R24)
					row = sheet.getRow(23);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR24_amt_6m() != null)
						cell1.setCellValue(record.getR24_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR24_capital_6m() != null) {
						cell1.setCellValue(record.getR24_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR24_amt_6to24m() != null)
						cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR24_capital_6to24m() != null) {
						cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR24_amt_gt24m() != null)
						cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR24_capital_gt24m() != null) {
						cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row24 (R25)
					row = sheet.getRow(24);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR25_amt_6m() != null)
						cell1.setCellValue(record.getR25_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR25_capital_6m() != null) {
						cell1.setCellValue(record.getR25_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR25_amt_6to24m() != null)
						cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR25_capital_6to24m() != null) {
						cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR25_amt_gt24m() != null)
						cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR25_capital_gt24m() != null) {
						cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row25 (R26)
					row = sheet.getRow(25);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR26_amt_6m() != null)
						cell1.setCellValue(record.getR26_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR26_capital_6m() != null) {
						cell1.setCellValue(record.getR26_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR26_amt_6to24m() != null)
						cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR26_capital_6to24m() != null) {
						cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR26_amt_gt24m() != null)
						cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR26_capital_gt24m() != null) {
						cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row26 (R27)
					row = sheet.getRow(26);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR27_amt_6m() != null) {
						cell1.setCellValue(record.getR27_amt_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR27_capital_6m() != null) {
						cell1.setCellValue(record.getR27_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR27_amt_6to24m() != null) {
						cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR27_capital_6to24m() != null) {
						cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR27_amt_gt24m() != null) {
						cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR27_capital_gt24m() != null) {
						cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row27 (R28)
					row = sheet.getRow(27);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR28_amt_6m() != null)
						cell1.setCellValue(record.getR28_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR28_capital_6m() != null) {
						cell1.setCellValue(record.getR28_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR28_amt_6to24m() != null)
						cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR28_capital_6to24m() != null) {
						cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR28_amt_gt24m() != null)
						cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR28_capital_gt24m() != null) {
						cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row28 (R29)
					row = sheet.getRow(28);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR29_amt_6m() != null)
						cell1.setCellValue(record.getR29_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR29_capital_6m() != null) {
						cell1.setCellValue(record.getR29_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR29_amt_6to24m() != null)
						cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR29_capital_6to24m() != null) {
						cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR29_amt_gt24m() != null)
						cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR29_capital_gt24m() != null) {
						cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row29 (R30)
					row = sheet.getRow(29);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR30_amt_6m() != null)
						cell1.setCellValue(record.getR30_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR30_capital_6m() != null) {
						cell1.setCellValue(record.getR30_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR30_amt_6to24m() != null)
						cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR30_capital_6to24m() != null) {
						cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR30_amt_gt24m() != null)
						cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR30_capital_gt24m() != null) {
						cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row30 (R31)
					row = sheet.getRow(30);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR31_amt_6m() != null)
						cell1.setCellValue(record.getR31_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR31_capital_6m() != null) {
						cell1.setCellValue(record.getR31_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR31_amt_6to24m() != null)
						cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR31_capital_6to24m() != null) {
						cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR31_amt_gt24m() != null)
						cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR31_capital_gt24m() != null) {
						cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row31 (R32)
					row = sheet.getRow(31);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR32_amt_6m() != null)
						cell1.setCellValue(record.getR32_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR32_capital_6m() != null) {
						cell1.setCellValue(record.getR32_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR32_amt_6to24m() != null)
						cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR32_capital_6to24m() != null) {
						cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR32_amt_gt24m() != null)
						cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR32_capital_gt24m() != null) {
						cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row32 (R33)
					row = sheet.getRow(32);

					// Column B

					// Column E
					cell1 = row.createCell(4);
					if (record.getR33_capital_6m() != null) {
						cell1.setCellValue(record.getR33_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR33_capital_6to24m() != null) {
						cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR33_capital_gt24m() != null) {
						cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row34
					// Column K
					row = sheet.getRow(34);
					cell1 = row.createCell(4);
					if (record.getR35_tot_spec_risk_ch() != null) {
						cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SIRResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SIRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SIR_Resub_Summary_Entity> dataList = brrs_M_SIR_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SIR report. Returning empty result.");
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
			// Create pure light green style (Excel highlight green)
			XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
			greenStyle.cloneStyleFrom(textStyle);

			byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
			XSSFColor green = new XSSFColor(rgb, null);

			greenStyle.setFillForegroundColor(green);
			greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SIR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL

					// row11
					// Column C
					row = sheet.getRow(11);

					Cell cell1 = row.createCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.createCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F

					cell1 = row.createCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column H
					cell1 = row.createCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column I

					cell1 = row.createCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cell1 = row.createCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//-------------

					// row12
					// Column b
					row = sheet.getRow(12);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row13
					// Column b
					row = sheet.getRow(13);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row14
					// Column b
					row = sheet.getRow(14);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//				//row15  (R16)
					// Column B
					row = sheet.getRow(15);
					cell1 = row.getCell(1);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row16 (R17)
					row = sheet.getRow(16);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR17_amt_6m() != null)
						cell1.setCellValue(record.getR17_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR17_capital_6m() != null) {
						cell1.setCellValue(record.getR17_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR17_amt_6to24m() != null)
						cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR17_capital_6to24m() != null) {
						cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR17_amt_gt24m() != null)
						cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR17_capital_gt24m() != null) {
						cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row17 (R18)
					row = sheet.getRow(17);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR18_amt_6m() != null) {
						cell1.setCellValue(record.getR18_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR18_capital_6m() != null) {
						cell1.setCellValue(record.getR18_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR18_amt_6to24m() != null) {
						cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR18_capital_6to24m() != null) {
						cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR18_amt_gt24m() != null) {
						cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR18_capital_gt24m() != null) {
						cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row18 (R19)
					row = sheet.getRow(18);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR19_amt_6m() != null)
						cell1.setCellValue(record.getR19_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR19_capital_6m() != null) {
						cell1.setCellValue(record.getR19_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR19_amt_6to24m() != null)
						cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR19_capital_6to24m() != null) {
						cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR19_amt_gt24m() != null)
						cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR19_capital_gt24m() != null) {
						cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19 (R20)
					row = sheet.getRow(19);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR20_amt_6m() != null)
						cell1.setCellValue(record.getR20_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR20_capital_6m() != null) {
						cell1.setCellValue(record.getR20_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR20_amt_6to24m() != null)
						cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR20_capital_6to24m() != null) {
						cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR20_amt_gt24m() != null)
						cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR20_capital_gt24m() != null) {
						cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row20 (R21)
					row = sheet.getRow(20);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR21_amt_6m() != null)
						cell1.setCellValue(record.getR21_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR21_capital_6m() != null) {
						cell1.setCellValue(record.getR21_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR21_amt_6to24m() != null)
						cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR21_capital_6to24m() != null) {
						cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR21_amt_gt24m() != null)
						cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR21_capital_gt24m() != null) {
						cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21 (R22)
					row = sheet.getRow(21);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR22_amt_6m() != null)
						cell1.setCellValue(record.getR22_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR22_capital_6m() != null) {
						cell1.setCellValue(record.getR22_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR22_amt_6to24m() != null)
						cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR22_capital_6to24m() != null) {
						cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR22_amt_gt24m() != null)
						cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR22_capital_gt24m() != null) {
						cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22 (R23)
					row = sheet.getRow(22);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR23_amt_6m() != null)
						cell1.setCellValue(record.getR23_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR23_capital_6m() != null) {
						cell1.setCellValue(record.getR23_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR23_amt_6to24m() != null)
						cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR23_capital_6to24m() != null) {
						cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR23_amt_gt24m() != null)
						cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR23_capital_gt24m() != null) {
						cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row23 (R24)
					row = sheet.getRow(23);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR24_amt_6m() != null)
						cell1.setCellValue(record.getR24_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR24_capital_6m() != null) {
						cell1.setCellValue(record.getR24_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR24_amt_6to24m() != null)
						cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR24_capital_6to24m() != null) {
						cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR24_amt_gt24m() != null)
						cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR24_capital_gt24m() != null) {
						cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row24 (R25)
					row = sheet.getRow(24);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR25_amt_6m() != null)
						cell1.setCellValue(record.getR25_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR25_capital_6m() != null) {
						cell1.setCellValue(record.getR25_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR25_amt_6to24m() != null)
						cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR25_capital_6to24m() != null) {
						cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR25_amt_gt24m() != null)
						cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR25_capital_gt24m() != null) {
						cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row25 (R26)
					row = sheet.getRow(25);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR26_amt_6m() != null)
						cell1.setCellValue(record.getR26_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR26_capital_6m() != null) {
						cell1.setCellValue(record.getR26_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR26_amt_6to24m() != null)
						cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR26_capital_6to24m() != null) {
						cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR26_amt_gt24m() != null)
						cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR26_capital_gt24m() != null) {
						cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row26 (R27)
					row = sheet.getRow(26);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR27_amt_6m() != null) {
						cell1.setCellValue(record.getR27_amt_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR27_capital_6m() != null) {
						cell1.setCellValue(record.getR27_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR27_amt_6to24m() != null) {
						cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR27_capital_6to24m() != null) {
						cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR27_amt_gt24m() != null) {
						cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR27_capital_gt24m() != null) {
						cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row27 (R28)
					row = sheet.getRow(27);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR28_amt_6m() != null)
						cell1.setCellValue(record.getR28_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR28_capital_6m() != null) {
						cell1.setCellValue(record.getR28_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR28_amt_6to24m() != null)
						cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR28_capital_6to24m() != null) {
						cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR28_amt_gt24m() != null)
						cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR28_capital_gt24m() != null) {
						cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row28 (R29)
					row = sheet.getRow(28);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR29_amt_6m() != null)
						cell1.setCellValue(record.getR29_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR29_capital_6m() != null) {
						cell1.setCellValue(record.getR29_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR29_amt_6to24m() != null)
						cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR29_capital_6to24m() != null) {
						cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR29_amt_gt24m() != null)
						cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR29_capital_gt24m() != null) {
						cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row29 (R30)
					row = sheet.getRow(29);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR30_amt_6m() != null)
						cell1.setCellValue(record.getR30_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR30_capital_6m() != null) {
						cell1.setCellValue(record.getR30_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR30_amt_6to24m() != null)
						cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR30_capital_6to24m() != null) {
						cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR30_amt_gt24m() != null)
						cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR30_capital_gt24m() != null) {
						cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row30 (R31)
					row = sheet.getRow(30);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR31_amt_6m() != null)
						cell1.setCellValue(record.getR31_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR31_capital_6m() != null) {
						cell1.setCellValue(record.getR31_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR31_amt_6to24m() != null)
						cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR31_capital_6to24m() != null) {
						cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR31_amt_gt24m() != null)
						cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR31_capital_gt24m() != null) {
						cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row31 (R32)
					row = sheet.getRow(31);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR32_amt_6m() != null)
						cell1.setCellValue(record.getR32_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR32_capital_6m() != null) {
						cell1.setCellValue(record.getR32_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR32_amt_6to24m() != null)
						cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR32_capital_6to24m() != null) {
						cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR32_amt_gt24m() != null)
						cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR32_capital_gt24m() != null) {
						cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row32 (R33)
					row = sheet.getRow(32);

					// Column B

					// Column E
					cell1 = row.createCell(4);
					if (record.getR33_capital_6m() != null) {
						cell1.setCellValue(record.getR33_capital_6m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR33_capital_6to24m() != null) {
						cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR33_capital_gt24m() != null) {
						cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row34
					// Column K
					row = sheet.getRow(34);
					cell1 = row.createCell(4);
					if (record.getR35_tot_spec_risk_ch() != null) {
						cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
						cell1.setCellStyle(greenStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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
	public byte[] BRRS_M_SIRResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SIR_Resub_Summary_Entity> dataList = brrs_M_SIR_resub_summary_repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SIR report. Returning empty result.");
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
			// Create pure light green style (Excel highlight green)
			XSSFCellStyle greenStyle = (XSSFCellStyle) workbook.createCellStyle();
			greenStyle.cloneStyleFrom(textStyle);

			byte[] rgb = new byte[] { (byte) 146, (byte) 208, (byte) 80 }; // exact Excel light green
			XSSFColor green = new XSSFColor(rgb, null);

			greenStyle.setFillForegroundColor(green);
			greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			greenStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL

					// row11
					// Column C
					row = sheet.getRow(11);

					Cell cell1 = row.createCell(2);
					if (record.getR12_amt_6m() != null) {
						cell1.setCellValue(record.getR12_amt_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.createCell(4);
					if (record.getR12_capital_6m() != null) {
						cell1.setCellValue(record.getR12_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F

					cell1 = row.createCell(5);
					if (record.getR12_amt_6to24m() != null) {
						cell1.setCellValue(record.getR12_amt_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column H
					cell1 = row.createCell(7);
					if (record.getR12_capital_6to24m() != null) {
						cell1.setCellValue(record.getR12_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column I

					cell1 = row.createCell(8);
					if (record.getR12_amt_gt24m() != null) {
						cell1.setCellValue(record.getR12_amt_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cell1 = row.createCell(10);
					if (record.getR12_capital_gt24m() != null) {
						cell1.setCellValue(record.getR12_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// -------------

					// row12
					// Column b
					row = sheet.getRow(12);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR13_amt_6m() != null) {
						cell1.setCellValue(record.getR13_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR13_capital_6m() != null) {
						cell1.setCellValue(record.getR13_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR13_amt_6to24m() != null) {
						cell1.setCellValue(record.getR13_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR13_capital_6to24m() != null) {
						cell1.setCellValue(record.getR13_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR13_amt_gt24m() != null) {
						cell1.setCellValue(record.getR13_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR13_capital_gt24m() != null) {
						cell1.setCellValue(record.getR13_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row13
					// Column b
					row = sheet.getRow(13);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR14_amt_6m() != null) {
						cell1.setCellValue(record.getR14_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR14_capital_6m() != null) {
						cell1.setCellValue(record.getR14_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR14_amt_6to24m() != null) {
						cell1.setCellValue(record.getR14_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR14_capital_6to24m() != null) {
						cell1.setCellValue(record.getR14_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR14_amt_gt24m() != null) {
						cell1.setCellValue(record.getR14_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR14_capital_gt24m() != null) {
						cell1.setCellValue(record.getR14_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row14
					// Column b
					row = sheet.getRow(14);

					// row12
					// Column C

					cell1 = row.getCell(2);
					if (record.getR15_amt_6m() != null) {
						cell1.setCellValue(record.getR15_amt_6m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(4);
					if (record.getR15_capital_6m() != null) {
						cell1.setCellValue(record.getR15_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR15_amt_6to24m() != null) {
						cell1.setCellValue(record.getR15_amt_6to24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.createCell(7);
					if (record.getR15_capital_6to24m() != null) {
						cell1.setCellValue(record.getR15_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR15_amt_gt24m() != null) {
						cell1.setCellValue(record.getR15_amt_gt24m().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell1 = row.createCell(10);
					if (record.getR15_capital_gt24m() != null) {
						cell1.setCellValue(record.getR15_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
//					//row15  (R16)
					// Column B
					row = sheet.getRow(15);
					cell1 = row.getCell(1);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR16_amt_6m() != null) {
						cell1.setCellValue(record.getR16_amt_6m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR16_capital_6m() != null) {
						cell1.setCellValue(record.getR16_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR16_amt_6to24m() != null) {
						cell1.setCellValue(record.getR16_amt_6to24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR16_capital_6to24m() != null) {
						cell1.setCellValue(record.getR16_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR16_amt_gt24m() != null) {
						cell1.setCellValue(record.getR16_amt_gt24m().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR16_capital_gt24m() != null) {
						cell1.setCellValue(record.getR16_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row16 (R17)
					row = sheet.getRow(16);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR17_amt_6m() != null)
						cell1.setCellValue(record.getR17_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR17_capital_6m() != null) {
						cell1.setCellValue(record.getR17_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR17_amt_6to24m() != null)
						cell1.setCellValue(record.getR17_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR17_capital_6to24m() != null) {
						cell1.setCellValue(record.getR17_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR17_amt_gt24m() != null)
						cell1.setCellValue(record.getR17_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR17_capital_gt24m() != null) {
						cell1.setCellValue(record.getR17_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row17 (R18)
					row = sheet.getRow(17);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR18_amt_6m() != null) {
						cell1.setCellValue(record.getR18_amt_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR18_capital_6m() != null) {
						cell1.setCellValue(record.getR18_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR18_amt_6to24m() != null) {
						cell1.setCellValue(record.getR18_amt_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR18_capital_6to24m() != null) {
						cell1.setCellValue(record.getR18_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR18_amt_gt24m() != null) {
						cell1.setCellValue(record.getR18_amt_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR18_capital_gt24m() != null) {
						cell1.setCellValue(record.getR18_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row18 (R19)
					row = sheet.getRow(18);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR19_amt_6m() != null)
						cell1.setCellValue(record.getR19_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR19_capital_6m() != null) {
						cell1.setCellValue(record.getR19_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR19_amt_6to24m() != null)
						cell1.setCellValue(record.getR19_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR19_capital_6to24m() != null) {
						cell1.setCellValue(record.getR19_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR19_amt_gt24m() != null)
						cell1.setCellValue(record.getR19_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR19_capital_gt24m() != null) {
						cell1.setCellValue(record.getR19_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row19 (R20)
					row = sheet.getRow(19);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR20_amt_6m() != null)
						cell1.setCellValue(record.getR20_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR20_capital_6m() != null) {
						cell1.setCellValue(record.getR20_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR20_amt_6to24m() != null)
						cell1.setCellValue(record.getR20_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR20_capital_6to24m() != null) {
						cell1.setCellValue(record.getR20_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR20_amt_gt24m() != null)
						cell1.setCellValue(record.getR20_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR20_capital_gt24m() != null) {
						cell1.setCellValue(record.getR20_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row20 (R21)
					row = sheet.getRow(20);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR21_amt_6m() != null)
						cell1.setCellValue(record.getR21_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR21_capital_6m() != null) {
						cell1.setCellValue(record.getR21_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR21_amt_6to24m() != null)
						cell1.setCellValue(record.getR21_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR21_capital_6to24m() != null) {
						cell1.setCellValue(record.getR21_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR21_amt_gt24m() != null)
						cell1.setCellValue(record.getR21_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR21_capital_gt24m() != null) {
						cell1.setCellValue(record.getR21_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row21 (R22)
					row = sheet.getRow(21);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR22_amt_6m() != null)
						cell1.setCellValue(record.getR22_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR22_capital_6m() != null) {
						cell1.setCellValue(record.getR22_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR22_amt_6to24m() != null)
						cell1.setCellValue(record.getR22_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR22_capital_6to24m() != null) {
						cell1.setCellValue(record.getR22_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR22_amt_gt24m() != null)
						cell1.setCellValue(record.getR22_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR22_capital_gt24m() != null) {
						cell1.setCellValue(record.getR22_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row22 (R23)
					row = sheet.getRow(22);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR23_amt_6m() != null)
						cell1.setCellValue(record.getR23_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR23_capital_6m() != null) {
						cell1.setCellValue(record.getR23_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR23_amt_6to24m() != null)
						cell1.setCellValue(record.getR23_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR23_capital_6to24m() != null) {
						cell1.setCellValue(record.getR23_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR23_amt_gt24m() != null)
						cell1.setCellValue(record.getR23_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR23_capital_gt24m() != null) {
						cell1.setCellValue(record.getR23_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row23 (R24)
					row = sheet.getRow(23);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR24_amt_6m() != null)
						cell1.setCellValue(record.getR24_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR24_capital_6m() != null) {
						cell1.setCellValue(record.getR24_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR24_amt_6to24m() != null)
						cell1.setCellValue(record.getR24_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR24_capital_6to24m() != null) {
						cell1.setCellValue(record.getR24_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR24_amt_gt24m() != null)
						cell1.setCellValue(record.getR24_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR24_capital_gt24m() != null) {
						cell1.setCellValue(record.getR24_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row24 (R25)
					row = sheet.getRow(24);

					// Column C
					cell1 = row.getCell(2);
					if (record.getR25_amt_6m() != null)
						cell1.setCellValue(record.getR25_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR25_capital_6m() != null) {
						cell1.setCellValue(record.getR25_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR25_amt_6to24m() != null)
						cell1.setCellValue(record.getR25_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR25_capital_6to24m() != null) {
						cell1.setCellValue(record.getR25_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR25_amt_gt24m() != null)
						cell1.setCellValue(record.getR25_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR25_capital_gt24m() != null) {
						cell1.setCellValue(record.getR25_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row25 (R26)
					row = sheet.getRow(25);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR26_amt_6m() != null)
						cell1.setCellValue(record.getR26_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR26_capital_6m() != null) {
						cell1.setCellValue(record.getR26_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR26_amt_6to24m() != null)
						cell1.setCellValue(record.getR26_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR26_capital_6to24m() != null) {
						cell1.setCellValue(record.getR26_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR26_amt_gt24m() != null)
						cell1.setCellValue(record.getR26_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR26_capital_gt24m() != null) {
						cell1.setCellValue(record.getR26_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row26 (R27)
					row = sheet.getRow(26);

					// Column B

					// Column C

					cell1 = row.createCell(2);
					if (record.getR27_amt_6m() != null) {
						cell1.setCellValue(record.getR27_amt_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column E
					cell1 = row.createCell(4);
					if (record.getR27_capital_6m() != null) {
						cell1.setCellValue(record.getR27_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F

					cell1 = row.createCell(5);
					if (record.getR27_amt_6to24m() != null) {
						cell1.setCellValue(record.getR27_amt_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column H
					cell1 = row.createCell(7);
					if (record.getR27_capital_6to24m() != null) {
						cell1.setCellValue(record.getR27_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I

					cell1 = row.createCell(8);
					if (record.getR27_amt_gt24m() != null) {
						cell1.setCellValue(record.getR27_amt_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column K
					cell1 = row.createCell(10);
					if (record.getR27_capital_gt24m() != null) {
						cell1.setCellValue(record.getR27_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row27 (R28)
					row = sheet.getRow(27);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR28_amt_6m() != null)
						cell1.setCellValue(record.getR28_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR28_capital_6m() != null) {
						cell1.setCellValue(record.getR28_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR28_amt_6to24m() != null)
						cell1.setCellValue(record.getR28_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR28_capital_6to24m() != null) {
						cell1.setCellValue(record.getR28_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR28_amt_gt24m() != null)
						cell1.setCellValue(record.getR28_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR28_capital_gt24m() != null) {
						cell1.setCellValue(record.getR28_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row28 (R29)
					row = sheet.getRow(28);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR29_amt_6m() != null)
						cell1.setCellValue(record.getR29_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR29_capital_6m() != null) {
						cell1.setCellValue(record.getR29_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR29_amt_6to24m() != null)
						cell1.setCellValue(record.getR29_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR29_capital_6to24m() != null) {
						cell1.setCellValue(record.getR29_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR29_amt_gt24m() != null)
						cell1.setCellValue(record.getR29_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR29_capital_gt24m() != null) {
						cell1.setCellValue(record.getR29_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row29 (R30)
					row = sheet.getRow(29);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR30_amt_6m() != null)
						cell1.setCellValue(record.getR30_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR30_capital_6m() != null) {
						cell1.setCellValue(record.getR30_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR30_amt_6to24m() != null)
						cell1.setCellValue(record.getR30_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR30_capital_6to24m() != null) {
						cell1.setCellValue(record.getR30_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR30_amt_gt24m() != null)
						cell1.setCellValue(record.getR30_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR30_capital_gt24m() != null) {
						cell1.setCellValue(record.getR30_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row30 (R31)
					row = sheet.getRow(30);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR31_amt_6m() != null)
						cell1.setCellValue(record.getR31_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR31_capital_6m() != null) {
						cell1.setCellValue(record.getR31_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR31_amt_6to24m() != null)
						cell1.setCellValue(record.getR31_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR31_capital_6to24m() != null) {
						cell1.setCellValue(record.getR31_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR31_amt_gt24m() != null)
						cell1.setCellValue(record.getR31_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR31_capital_gt24m() != null) {
						cell1.setCellValue(record.getR31_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row31 (R32)
					row = sheet.getRow(31);

					// Column B

					// Column C
					cell1 = row.getCell(2);
					if (record.getR32_amt_6m() != null)
						cell1.setCellValue(record.getR32_amt_6m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column E
					cell1 = row.createCell(4);
					if (record.getR32_capital_6m() != null) {
						cell1.setCellValue(record.getR32_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column F
					cell1 = row.getCell(5);
					if (record.getR32_amt_6to24m() != null)
						cell1.setCellValue(record.getR32_amt_6to24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR32_capital_6to24m() != null) {
						cell1.setCellValue(record.getR32_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column I
					cell1 = row.getCell(8);
					if (record.getR32_amt_gt24m() != null)
						cell1.setCellValue(record.getR32_amt_gt24m().doubleValue());
					else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR32_capital_gt24m() != null) {
						cell1.setCellValue(record.getR32_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row32 (R33)
					row = sheet.getRow(32);

					// Column B

					// Column E
					cell1 = row.createCell(4);
					if (record.getR33_capital_6m() != null) {
						cell1.setCellValue(record.getR33_capital_6m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column H
					cell1 = row.createCell(7);
					if (record.getR33_capital_6to24m() != null) {
						cell1.setCellValue(record.getR33_capital_6to24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column K
					cell1 = row.createCell(10);
					if (record.getR33_capital_gt24m() != null) {
						cell1.setCellValue(record.getR33_capital_gt24m().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row34
					// Column K
					row = sheet.getRow(34);
					cell1 = row.createCell(4);
					if (record.getR35_tot_spec_risk_ch() != null) {
						cell1.setCellValue(record.getR35_tot_spec_risk_ch().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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