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

import com.bornfire.brrs.entities.BRRS_M_CA3_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA3_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA3_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA3_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA3_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA3_Summary_Repo;
import com.bornfire.brrs.entities.M_CR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA3_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_CA3_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA3_Detail_Entity;
import com.bornfire.brrs.entities.M_CA3_Resub_Detail_Entity;
import com.bornfire.brrs.entities.M_CA3_Resub_Summary_Entity;
import com.bornfire.brrs.entities.M_CA3_Summary_Entity;

@Component
@Service

public class BRRS_M_CA3_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA3_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_CA3_Summary_Repo brrs_M_CA3_summary_repo;

	@Autowired
	BRRS_M_CA3_Detail_Repo brrs_M_CA3_detail_repo;

	@Autowired
	BRRS_M_CA3_Archival_Summary_Repo M_CA3_Archival_Summary_Repo;

	@Autowired
	BRRS_M_CA3_Archival_Detail_Repo BRRS_M_CA3_Archival_Detail_Repo;

	@Autowired
	BRRS_M_CA3_Resub_Summary_Repo M_CA3_Resub_Summary_Repo;

	@Autowired
	BRRS_M_CA3_Resub_Detail_Repo M_CA3_Resub_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_CA3View(String reportId, String fromdate, String todate, String currency,
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
				List<M_CA3_Archival_Summary_Entity> T1Master = M_CA3_Archival_Summary_Repo.getdatabydateListarchival(d1,
						version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_CA3_Resub_Summary_Entity> T1Master = M_CA3_Resub_Summary_Repo.getdatabydateListarchival(d1,
						version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_CA3_Summary_Entity> T1Master = brrs_M_CA3_summary_repo
						.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_CA3_Archival_Detail_Entity> T1Master = BRRS_M_CA3_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_CA3_Resub_Detail_Entity> T1Master = M_CA3_Resub_Detail_Repo.getdatabydateListarchival(d1,
							version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_CA3_Detail_Entity> T1Master = brrs_M_CA3_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_CA3");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_CA3_Summary_Entity updatedEntity) {
		M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException("Record not found"));

		M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
				.orElse(new M_CA3_Detail_Entity());

		try {
			// Use "AMOUNT" because that is what is in your Entity class
			String[] fields = { "AMOUNT" };

			// Update the loop to cover all your rows (10 to 60)
			for (int i = 10; i <= 60; i++) {
				String prefix = "R" + i + "_";
				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);
						Object newValue = getter.invoke(updatedEntity);

						if (newValue != null) {
							Method sumSetter = M_CA3_Summary_Entity.class.getMethod(setterName, BigDecimal.class);
							sumSetter.invoke(existingSummary, newValue);

							Method detSetter = M_CA3_Detail_Entity.class.getMethod(setterName, BigDecimal.class);
							detSetter.invoke(existingDetail, newValue);
						}
					} catch (NoSuchMethodException e) {
						continue; // Skip if R-number doesn't exist (e.g., R21, R22)
					}
				}
			}

			brrs_M_CA3_summary_repo.save(existingSummary);
			brrs_M_CA3_detail_repo.save(existingDetail);

		} catch (Exception e) {
			throw new RuntimeException("Update failed", e);
		}
	}

	public void updateReport2(M_CA3_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// 1️⃣ Fetch existing SUMMARY
		M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		// 2️⃣ Fetch or create DETAIL
		M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseGet(() -> {
					M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
					d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
					return d;
				});

		try {

			// 🔁 Loop R24 → R27 (AMOUNT)
			for (int i = 24; i <= 27; i++) {

				String prefix = "R" + i + "_";
				String[] fields = { "AMOUNT" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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

			// 🔁 Handle R28 and R29 (AMOUNT)
			int[] totals = { 28, 29 };
			for (int i : totals) {

				String prefix = "R" + i + "_";
				String[] fields = { "AMOUNT" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						// ✅ set into SUMMARY
						summarySetter.invoke(existingSummary, newValue);

						// ✅ set into DETAIL
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save BOTH (same transaction)
		brrs_M_CA3_summary_repo.save(existingSummary);
		brrs_M_CA3_detail_repo.save(existingDetail);
	}

	public void updateReport3(M_CA3_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// 1️⃣ Fetch existing SUMMARY
		M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		// 2️⃣ Fetch or create DETAIL
		M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseGet(() -> {
					M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
					d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
					return d;
				});

		try {

			// 🔁 Loop R36 → R40 (AMOUNT)
			for (int i = 36; i <= 40; i++) {

				String prefix = "R" + i + "_";
				String[] fields = { "AMOUNT" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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

			// 🔁 Handle R41 (AMOUNT)
			String[] totalFields = { "AMOUNT" };
			for (String field : totalFields) {

				String getterName = "getR41_" + field;
				String setterName = "setR41_" + field;

				try {
					Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

					Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);

					// ✅ set into SUMMARY
					summarySetter.invoke(existingSummary, newValue);

					// ✅ set into DETAIL
					detailSetter.invoke(existingDetail, newValue);

				} catch (NoSuchMethodException e) {
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save BOTH (same transaction)
		brrs_M_CA3_summary_repo.save(existingSummary);
		brrs_M_CA3_detail_repo.save(existingDetail);
	}

	public void updateReport4(M_CA3_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// 1️⃣ Fetch existing SUMMARY
		M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		// 2️⃣ Fetch or create DETAIL
		M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseGet(() -> {
					M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
					d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
					return d;
				});

		try {

			// 🔁 Copy R44, R45, R46 (AMOUNT)
			int[] rows = { 44, 45, 46 };

			for (int i : rows) {

				String prefix = "R" + i + "_";
				String[] fields = { "AMOUNT" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save BOTH (same transaction)
		brrs_M_CA3_summary_repo.save(existingSummary);
		brrs_M_CA3_detail_repo.save(existingDetail);
	}

	public void updateReport5(M_CA3_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// 1️⃣ Fetch existing SUMMARY
		M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		// 2️⃣ Fetch or create DETAIL
		M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseGet(() -> {
					M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
					d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
					return d;
				});

		try {

			// 🔁 Loop R50 → R55 (AMOUNT)
			for (int i = 50; i <= 55; i++) {

				String prefix = "R" + i + "_";
				String[] fields = { "AMOUNT" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save BOTH (same transaction)
		brrs_M_CA3_summary_repo.save(existingSummary);
		brrs_M_CA3_detail_repo.save(existingDetail);
	}

	public void updateReport6(M_CA3_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// 1️⃣ Fetch existing SUMMARY
		M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		// 2️⃣ Fetch or create DETAIL
		M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseGet(() -> {
					M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
					d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
					return d;
				});

		try {

			// 🔁 Loop R58 → R60 (AMOUNT)
			for (int i = 58; i <= 60; i++) {

				String prefix = "R" + i + "_";
				String[] fields = { "AMOUNT" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save BOTH (same transaction)
		brrs_M_CA3_summary_repo.save(existingSummary);
		brrs_M_CA3_detail_repo.save(existingDetail);
	}

	public void updateResubReport(M_CA3_Resub_Summary_Entity updatedEntity) {

		   System.out.println("Came toM_C Resub Service");
		Date reportDate = updatedEntity.getReportDate();

		BigDecimal maxResubVer = M_CA3_Resub_Summary_Repo.findMaxVersion(reportDate);
		if (maxResubVer == null) {
			throw new RuntimeException("No record for report date: " + reportDate);
		}

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);
		Date now = new Date();

		M_CA3_Resub_Summary_Entity resubSummary = new M_CA3_Resub_Summary_Entity();
		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");
		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		M_CA3_Resub_Detail_Entity resubDetail = new M_CA3_Resub_Detail_Entity();
		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");
		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		M_CA3_Archival_Summary_Entity archSummary = new M_CA3_Archival_Summary_Entity();
		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");
		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion);
		archSummary.setReportResubDate(now);

		M_CA3_Archival_Detail_Entity archDetail = new M_CA3_Archival_Detail_Entity();
		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");
		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion);
		archDetail.setReportResubDate(now);

		M_CA3_Resub_Summary_Repo.save(resubSummary);
		M_CA3_Resub_Detail_Repo.save(resubDetail);
		M_CA3_Archival_Summary_Repo.save(archSummary);
		BRRS_M_CA3_Archival_Detail_Repo.save(archDetail);
	}

	public List<Object[]> getM_CA3Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA3_Archival_Summary_Entity> latestArchivalList = M_CA3_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA3_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_CA3 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_CA3Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_CA3_Archival_Summary_Entity> repoData = M_CA3_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_CA3_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_CA3_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_CA3 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_CA3Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_CA3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA3ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_CA3EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_CA3_Summary_Entity> dataList = brrs_M_CA3_summary_repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_CA3 report. Returning empty result.");
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
							M_CA3_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							// row10
							// Column b

							// column c
							Cell cell2 = row.createCell(2);
							if (record.getR10_AMOUNT() != null) {
								cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row11
							row = sheet.getRow(10);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR11_AMOUNT() != null) {
								cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR12_AMOUNT() != null) {
								cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR13_AMOUNT() != null) {
								cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR14_AMOUNT() != null) {
								cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row15
							row = sheet.getRow(14);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR15_AMOUNT() != null) {
								cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(15);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR16_AMOUNT() != null) {
								cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR17_AMOUNT() != null) {
								cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR18_AMOUNT() != null) {
								cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR19_AMOUNT() != null) {
								cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR20_AMOUNT() != null) {
								cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");
							}

							// row24
							row = sheet.getRow(23);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR24_AMOUNT() != null) {
								cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR25_AMOUNT() != null) {
								cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR26_AMOUNT() != null) {
								cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR27_AMOUNT() != null) {
								cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row28
							row = sheet.getRow(27);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR28_AMOUNT() != null) {
								cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");
							}

							// row29
							row = sheet.getRow(28);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR29_AMOUNT() != null) {
								cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row36
							row = sheet.getRow(35);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR36_AMOUNT() != null) {
								cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							// row37
							row = sheet.getRow(36);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR37_AMOUNT() != null) {
								cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row38
							row = sheet.getRow(37);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR38_AMOUNT() != null) {
								cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row39
							row = sheet.getRow(38);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR39_AMOUNT() != null) {
								cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row40
							row = sheet.getRow(39);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR40_AMOUNT() != null) {
								cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row41
							row = sheet.getRow(40);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR41_AMOUNT() != null) {
								cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row44
							row = sheet.getRow(43);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR44_AMOUNT() != null) {
								cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row45
							row = sheet.getRow(44);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR45_AMOUNT() != null) {
								cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row46
							row = sheet.getRow(45);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR46_AMOUNT() != null) {
								cell2.setCellValue(record.getR46_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row50
							row = sheet.getRow(49);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR50_AMOUNT() != null) {
								cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row51
							row = sheet.getRow(50);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR51_AMOUNT() != null) {
								cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row52
							row = sheet.getRow(51);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR52_AMOUNT() != null) {
								cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row53
							row = sheet.getRow(52);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR53_AMOUNT() != null) {
								cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row54
							row = sheet.getRow(53);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR54_AMOUNT() != null) {
								cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row55
							row = sheet.getRow(54);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR55_AMOUNT() != null) {
								cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row58
							row = sheet.getRow(57);
							// Column b

							// Column c
							cell2 = row.createCell(2);
							if (record.getR58_AMOUNT() != null) {
								cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row59
							row = sheet.getRow(58);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR59_AMOUNT() != null) {
								cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

							}

							// row60
							row = sheet.getRow(14);
							// Column b

							// Column c
							cell2 = row.getCell(2);
							if (record.getR15_AMOUNT() != null) {
								cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

							} else {
								cell2.setCellValue("");

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
	public byte[] BRRS_M_CA3EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA3ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA3ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_CA3_Summary_Entity> dataList = brrs_M_CA3_summary_repo.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_CA3 report. Returning empty result.");
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
						M_CA3_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// row10
						// Column b

						// column c
						Cell cell2 = row.createCell(2);
						if (record.getR10_AMOUNT() != null) {
							cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row11
						row = sheet.getRow(10);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR11_AMOUNT() != null) {
							cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR12_AMOUNT() != null) {
							cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR13_AMOUNT() != null) {
							cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row14
						row = sheet.getRow(13);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR14_AMOUNT() != null) {
							cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row15
						row = sheet.getRow(14);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR15_AMOUNT() != null) {
							cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR16_AMOUNT() != null) {
							cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR17_AMOUNT() != null) {
							cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR18_AMOUNT() != null) {
							cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row19
						row = sheet.getRow(18);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR19_AMOUNT() != null) {
							cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);
						// Column b

						// Column c
						cell2 = row.getCell(2);
						if (record.getR20_AMOUNT() != null) {
							cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");
						}

						// row24
						row = sheet.getRow(23);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR24_AMOUNT() != null) {
							cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row25
						row = sheet.getRow(24);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR25_AMOUNT() != null) {
							cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row26
						row = sheet.getRow(25);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR26_AMOUNT() != null) {
							cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR27_AMOUNT() != null) {
							cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row28
						row = sheet.getRow(27);
						// Column b

						// Column c
						cell2 = row.getCell(2);
						if (record.getR28_AMOUNT() != null) {
							cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");
						}

						// row29
						row = sheet.getRow(28);
						// Column b

						// Column c
						cell2 = row.getCell(2);
						if (record.getR29_AMOUNT() != null) {
							cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");

						}
						
						
						// row36
						row = sheet.getRow(33);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR36_AMOUNT() != null) {
							cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						// row37
						row = sheet.getRow(34);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR37_AMOUNT() != null) {
							cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row38
						row = sheet.getRow(35);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR38_AMOUNT() != null) {
							cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row39
						row = sheet.getRow(36);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR39_AMOUNT() != null) {
							cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row40
						row = sheet.getRow(40);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR40_AMOUNT() != null) {
							cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						/*
						 * // row41 row = sheet.getRow(38);
						 * 
						 * // Column b
						 * 
						 * // Column c cell2 = row.getCell(2); if (record.getR41_AMOUNT() != null) {
						 * cell2.setCellValue(record.getR41_AMOUNT().doubleValue());
						 * 
						 * } else { cell2.setCellValue("");
						 * 
						 * }
						 */

						// row44
						row = sheet.getRow(41);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR44_AMOUNT() != null) {
							cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						/*
						 * // row45 row = sheet.getRow(44); // Column b
						 * 
						 * // Column c cell2 = row.getCell(2); if (record.getR45_AMOUNT() != null) {
						 * cell2.setCellValue(record.getR45_AMOUNT().doubleValue());
						 * 
						 * } else { cell2.setCellValue("");
						 * 
						 * }
						 * 
						 * // row46 row = sheet.getRow(45); // Column b
						 * 
						 * // Column c cell2 = row.getCell(2); if (record.getR46_AMOUNT() != null) {
						 * cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
						 * 
						 * } else { cell2.setCellValue("");
						 * 
						 * }
						 */

						// row50
						row = sheet.getRow(46);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR50_AMOUNT() != null) {
							cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row51
						row = sheet.getRow(47);

						// Column c
						cell2 = row.createCell(2);
						if (record.getR51_AMOUNT() != null) {
							cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row52
						row = sheet.getRow(48);

						// Column c
						cell2 = row.createCell(2);
						if (record.getR52_AMOUNT() != null) {
							cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row53
						row = sheet.getRow(49);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR53_AMOUNT() != null) {
							cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row54
						row = sheet.getRow(53);
						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR54_AMOUNT() != null) {
							cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row55
						row = sheet.getRow(50);

						// Column c
						cell2 = row.getCell(2);
						if (record.getR55_AMOUNT() != null) {
							cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");

						}

						// row58
						row = sheet.getRow(54);

						// Column b

						// Column c
						cell2 = row.createCell(2);
						if (record.getR58_AMOUNT() != null) {
							cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						/*
						 * //row59 row = sheet.getRow(58); // Column b
						 * 
						 * 
						 * // Column c cell2 = row.getCell(2); if (record.getR59_AMOUNT() != null) {
						 * cell2.setCellValue(record.getR59_AMOUNT().doubleValue());
						 * 
						 * } else { cell2.setCellValue("");
						 * 
						 * }
						 */

						// row60
						row = sheet.getRow(54);

						// Column c
						cell2 = row.getCell(2);
						if (record.getR59_AMOUNT() != null) {
							cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

						} else {
							cell2.setCellValue("");

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

	// Archival format excel
	public byte[] getExcelM_CA3ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA3ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA3_Archival_Summary_Entity> dataList = M_CA3_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA3 report. Returning empty result.");
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
					M_CA3_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row10
					// Column b

					// column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row36
					row = sheet.getRow(35);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// row37
					row = sheet.getRow(36);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row44
					row = sheet.getRow(43);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row46
					row = sheet.getRow(45);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row50
					row = sheet.getRow(49);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row58
					row = sheet.getRow(57);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row60
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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
	public byte[] BRRS_M_CA3ArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA3_Archival_Summary_Entity> dataList = M_CA3_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA3 report. Returning empty result.");
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
					M_CA3_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row10
					// Column b

					// column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}
					
					
					// row36
					row = sheet.getRow(33);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// row37
					row = sheet.getRow(34);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(35);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(36);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(40);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * // row41 row = sheet.getRow(38);
					 * 
					 * // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR41_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR41_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row44
					row = sheet.getRow(41);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * // row45 row = sheet.getRow(44); // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR45_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR45_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 * 
					 * // row46 row = sheet.getRow(45); // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR46_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row50
					row = sheet.getRow(46);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(47);

					// Column c
					cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(48);

					// Column c
					cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(49);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(50);

					// Column c
					cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row58
					row = sheet.getRow(54);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * //row59 row = sheet.getRow(58); // Column b
					 * 
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR59_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR59_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row60
					row = sheet.getRow(54);

					// Column c
					cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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

	// Resub Format excel
	public byte[] BRRS_M_CA3ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA3ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA3_Resub_Summary_Entity> dataList = M_CA3_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA3 report. Returning empty result.");
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

					M_CA3_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row10
					// Column b

					// column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row36
					row = sheet.getRow(35);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// row37
					row = sheet.getRow(36);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row44
					row = sheet.getRow(43);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row46
					row = sheet.getRow(45);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row50
					row = sheet.getRow(49);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row58
					row = sheet.getRow(57);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row60
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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
	public byte[] BRRS_M_CA3ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA3_Resub_Summary_Entity> dataList = M_CA3_Resub_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA3 report. Returning empty result.");
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
					M_CA3_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row10
					// Column b

					// column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column b

					// Column c
					cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}
					
					
					// row36
					row = sheet.getRow(33);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					// row37
					row = sheet.getRow(34);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(35);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(36);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(40);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * // row41 row = sheet.getRow(38);
					 * 
					 * // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR41_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR41_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row44
					row = sheet.getRow(41);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * // row45 row = sheet.getRow(44); // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR45_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR45_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 * 
					 * // row46 row = sheet.getRow(45); // Column b
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR46_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row50
					row = sheet.getRow(46);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(47);

					// Column c
					cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(48);

					// Column c
					cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(49);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(50);

					// Column c
					cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					// row58
					row = sheet.getRow(54);

					// Column b

					// Column c
					cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * //row59 row = sheet.getRow(58); // Column b
					 * 
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR59_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR59_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					// row60
					row = sheet.getRow(54);

					// Column c
					cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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