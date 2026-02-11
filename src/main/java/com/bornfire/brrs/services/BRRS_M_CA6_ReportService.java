
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

import com.bornfire.brrs.entities.BRRS_M_CA6_Archival_Detail_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA6_Archival_Detail_Repo2;
import com.bornfire.brrs.entities.BRRS_M_CA6_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA6_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_CA6_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA6_Detail_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA6_Detail_Repo2;
import com.bornfire.brrs.entities.BRRS_M_CA6_RESUB_Detail_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA6_RESUB_Detail_Repo2;
import com.bornfire.brrs.entities.BRRS_M_CA6_RESUB_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA6_RESUB_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_CA6_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA6_Summary_Repo2;
import com.bornfire.brrs.entities.M_CA6_Archival_Detail_Entity1;
import com.bornfire.brrs.entities.M_CA6_Archival_Detail_Entity2;
import com.bornfire.brrs.entities.M_CA6_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA6_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_CA6_Detail_Entity1;
import com.bornfire.brrs.entities.M_CA6_Detail_Entity2;
import com.bornfire.brrs.entities.M_CA6_RESUB_Detail_Entity1;
import com.bornfire.brrs.entities.M_CA6_RESUB_Detail_Entity2;
import com.bornfire.brrs.entities.M_CA6_RESUB_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA6_RESUB_Summary_Entity2;
import com.bornfire.brrs.entities.M_CA6_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA6_Summary_Entity2;

@Component
@Service

public class BRRS_M_CA6_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA6_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_CA6_Detail_Repo M_CA6_DETAIL_Repo;

	@Autowired
	BRRS_M_CA6_Detail_Repo1 M_CA6_DETAIL_Repo1;

	@Autowired
	BRRS_M_CA6_Detail_Repo2 M_CA6_DETAIL_Repo2;

	@Autowired
	BRRS_M_CA6_Archival_Detail_Repo1 M_CA6_DETAILArchival_Repo1;

	@Autowired
	BRRS_M_CA6_Archival_Detail_Repo2 M_CA6_DETAILArchival_Repo2;

	@Autowired
	BRRS_M_CA6_Summary_Repo1 M_CA6_Summary_Repo1;

	@Autowired
	BRRS_M_CA6_Summary_Repo2 M_CA6_Summary_Repo2;
	@Autowired
	BRRS_M_CA6_Archival_Summary_Repo1 BRRS_M_CA6_Archival_Summary_Repo1;
	@Autowired
	BRRS_M_CA6_Archival_Summary_Repo2 BRRS_M_CA6_Archival_Summary_Repo2;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	@Autowired
	BRRS_M_CA6_RESUB_Summary_Repo1 brrs_m_ca6_resub_summary_repo1;

	@Autowired
	BRRS_M_CA6_RESUB_Summary_Repo2 brrs_m_ca6_resub_summary_repo2;

	@Autowired
	BRRS_M_CA6_RESUB_Detail_Repo1 brrs_m_ca6_resub_detail_repo1;

	@Autowired
	BRRS_M_CA6_RESUB_Detail_Repo2 brrs_m_ca6_resub_detail_repo2;

	public ModelAndView getM_CA6View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW DEBUG =======");
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

				List<M_CA6_Archival_Summary_Entity1> T1Master = BRRS_M_CA6_Archival_Summary_Repo1
						.getdatabydateListarchival(d1, version);
				List<M_CA6_Archival_Summary_Entity2> T2Master = BRRS_M_CA6_Archival_Summary_Repo2
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("displaymode", "summary");

			}
			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_CA6_RESUB_Summary_Entity1> T1Master = brrs_m_ca6_resub_summary_repo1
						.getdatabydateListarchival(d1, version);
				List<M_CA6_RESUB_Summary_Entity2> T2Master = brrs_m_ca6_resub_summary_repo2
						.getdatabydateListarchival(d1, version);
				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("displaymode", "resubSummary");
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_CA6_Summary_Entity1> T1Master = M_CA6_Summary_Repo1.getdatabydateList(d1);
				List<M_CA6_Summary_Entity2> T2Master = M_CA6_Summary_Repo2.getdatabydateList(d1);

				System.out.println("T1Master Size: " + T1Master.size());
				System.out.println("T2Master Size: " + T2Master.size());

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("displaymode", "summary");
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_CA6_Archival_Detail_Entity1> T1Master = M_CA6_DETAILArchival_Repo1
							.getdatabydateListarchival(dateformat.parse(todate), version);
					List<M_CA6_Archival_Detail_Entity2> T2Master = M_CA6_DETAILArchival_Repo2
							.getdatabydateListarchival(dateformat.parse(todate), version);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
					mv.addObject("reportsummary1", T2Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_CA6_RESUB_Detail_Entity1> T1Master = brrs_m_ca6_resub_detail_repo1
							.getdatabydateListarchival(dateformat.parse(todate), version);
					List<M_CA6_RESUB_Detail_Entity2> T2Master = brrs_m_ca6_resub_detail_repo2
							.getdatabydateListarchival(dateformat.parse(todate), version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
					mv.addObject("reportsummary1", T2Master);

				}
				// DETAIL + NORMAL
				else {

					List<M_CA6_Detail_Entity1> T1Master = M_CA6_DETAIL_Repo1
							.getdatabydateList(dateformat.parse(todate));
					List<M_CA6_Detail_Entity2> T2Master = M_CA6_DETAIL_Repo2
							.getdatabydateList(dateformat.parse(todate));

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
					mv.addObject("reportsummary1", T2Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_CA6");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	public void updateResubReport(M_CA6_RESUB_Summary_Entity1 updatedEntity1,
			M_CA6_RESUB_Summary_Entity2 updatedEntity2) {

		Date reportDate1 = updatedEntity1.getReportDate();
		Date reportDate2 = updatedEntity2.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = brrs_m_ca6_resub_summary_repo1.findMaxVersion(reportDate1);
		BigDecimal maxResubVer2 = brrs_m_ca6_resub_summary_repo2.findMaxVersion(reportDate2);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate1);
		if (maxResubVer2 == null)
			throw new RuntimeException("No record for: " + reportDate2);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_CA6_RESUB_Summary_Entity1 resubSummary = new M_CA6_RESUB_Summary_Entity1();
		M_CA6_RESUB_Summary_Entity2 resubSummary2 = new M_CA6_RESUB_Summary_Entity2();

		BeanUtils.copyProperties(updatedEntity1, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate1);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity2, resubSummary2, "reportDate", "reportVersion", "reportResubDate");

		resubSummary2.setReportDate(reportDate2);
		resubSummary2.setReportVersion(newVersion);
		resubSummary2.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_CA6_RESUB_Detail_Entity1 resubDetail = new M_CA6_RESUB_Detail_Entity1();
		M_CA6_RESUB_Detail_Entity2 resubDetail2 = new M_CA6_RESUB_Detail_Entity2();

		BeanUtils.copyProperties(updatedEntity1, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate1);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity2, resubDetail2, "reportDate", "reportVersion", "reportResubDate");

		resubDetail2.setReportDate(reportDate2);
		resubDetail2.setReportVersion(newVersion);
		resubDetail2.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_CA6_Archival_Summary_Entity1 archSummary = new M_CA6_Archival_Summary_Entity1();
		M_CA6_Archival_Summary_Entity2 archSummary2 = new M_CA6_Archival_Summary_Entity2();

		BeanUtils.copyProperties(updatedEntity1, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate1);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity2, archSummary2, "reportDate", "reportVersion", "reportResubDate");

		archSummary2.setReportDate(reportDate2);
		archSummary2.setReportVersion(newVersion); // SAME VERSION
		archSummary2.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_CA6_Archival_Detail_Entity1 archDetail = new M_CA6_Archival_Detail_Entity1();
		M_CA6_Archival_Detail_Entity2 archDetail2 = new M_CA6_Archival_Detail_Entity2();

		BeanUtils.copyProperties(updatedEntity1, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate1);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		BeanUtils.copyProperties(updatedEntity2, archDetail2, "reportDate", "reportVersion", "reportResubDate");

		archDetail2.setReportDate(reportDate2);
		archDetail2.setReportVersion(newVersion); // SAME VERSION
		archDetail2.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		brrs_m_ca6_resub_summary_repo1.save(resubSummary);
		brrs_m_ca6_resub_summary_repo2.save(resubSummary2);

		brrs_m_ca6_resub_detail_repo1.save(resubDetail);
		brrs_m_ca6_resub_detail_repo2.save(resubDetail2);

		BRRS_M_CA6_Archival_Summary_Repo1.save(archSummary);
		BRRS_M_CA6_Archival_Summary_Repo2.save(archSummary2);

		M_CA6_DETAILArchival_Repo1.save(archDetail);
		M_CA6_DETAILArchival_Repo2.save(archDetail2);

	}

	public void updateReport1(M_CA6_Summary_Entity1 entity1) {
		System.out.println("Report Date: " + entity1.getReportDate());

		M_CA6_Summary_Entity1 existing = M_CA6_Summary_Repo1.findById(entity1.getReportDate()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + entity1.getReportDate()));

		// --------------------------
		// Update R28–R34 amounts
		// --------------------------

		try {
			for (int i = 12; i <= 16; i++) {
				String prefix = "R" + i + "_";
				// You can add more fields here if needed later
				String[] fields = { "CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT",
						"AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA6_Summary_Entity1.class.getMethod(getterName);
						Method setter = M_CA6_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(entity1);
						setter.invoke(existing, newValue);
					} catch (NoSuchMethodException e) {
						// if any field is missing in entity class, skip it
						continue;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating R12–R16 fields", e);
		}

		try {
			for (int i = 20; i <= 24; i++) {
				String prefix = "R" + i + "_";
				// You can add more fields here if needed later
				String[] fields = { "CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT",
						"AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA6_Summary_Entity1.class.getMethod(getterName);
						Method setter = M_CA6_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(entity1);
						setter.invoke(existing, newValue);
					} catch (NoSuchMethodException e) {
						// if any field is missing in entity class, skip it
						continue;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating R20-24 fields", e);
		}
		M_CA6_Summary_Repo1.save(existing);
	}

	public void updatedetail1(M_CA6_Detail_Entity1 entity1) {
		System.out.println("Report Date: " + entity1.getReportDate());

		M_CA6_Detail_Entity1 existing = M_CA6_DETAIL_Repo1.findById(entity1.getReportDate()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + entity1.getReportDate()));

		// --------------------------
		// Update R28–R34 amounts
		// --------------------------

		try {
			for (int i = 12; i <= 16; i++) {
				String prefix = "R" + i + "_";
				// You can add more fields here if needed later
				String[] fields = { "CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT",
						"AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA6_Detail_Entity1.class.getMethod(getterName);
						Method setter = M_CA6_Detail_Entity1.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(entity1);
						setter.invoke(existing, newValue);
					} catch (NoSuchMethodException e) {
						// if any field is missing in entity class, skip it
						continue;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating R12–R16 fields", e);
		}

		try {
			for (int i = 20; i <= 24; i++) {
				String prefix = "R" + i + "_";
				// You can add more fields here if needed later
				String[] fields = { "CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT",
						"AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA6_Detail_Entity1.class.getMethod(getterName);
						Method setter = M_CA6_Detail_Entity1.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(entity1);
						setter.invoke(existing, newValue);
					} catch (NoSuchMethodException e) {
						// if any field is missing in entity class, skip it
						continue;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating R20-24 fields", e);
		}
		M_CA6_DETAIL_Repo1.save(existing);
	}

	public void updateReport2(M_CA6_Summary_Entity2 entity) {
		System.out.println("Report Date: " + entity.getReportDate());

		M_CA6_Summary_Entity2 existing = M_CA6_Summary_Repo2.findById(entity.getReportDate())
				.orElseThrow(() -> new RuntimeException("Record not found for REPORT_DATE: " + entity.getReportDate()));

		// --------------------------
		// Update R28–R34 amounts
		// --------------------------
		try {
			for (int i = 28; i <= 34; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "PRODUCT", "AMOUNT" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA6_Summary_Entity2.class.getMethod(getterName);
						Method setter = M_CA6_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(entity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// Update R35 total
			Method getter = M_CA6_Summary_Entity2.class.getMethod("getR35_AMOUNT");
			Method setter = M_CA6_Summary_Entity2.class.getMethod("setR35_AMOUNT", getter.getReturnType());
			setter.invoke(existing, getter.invoke(entity));

		} catch (Exception e) {
			throw new RuntimeException("Error while updating R28–R35 fields", e);
		}

		// --------------------------
		// Update R40–R46 + R47 amounts
		// --------------------------
		try {
			for (int i = 40; i <= 46; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "PRODUCT", "AMOUNT" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA6_Summary_Entity2.class.getMethod(getterName);
						Method setter = M_CA6_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(entity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// Update R47
			Method getter = M_CA6_Summary_Entity2.class.getMethod("getR47_AMOUNT");
			Method setter = M_CA6_Summary_Entity2.class.getMethod("setR47_AMOUNT", getter.getReturnType());
			setter.invoke(existing, getter.invoke(entity));

		} catch (Exception e) {
			throw new RuntimeException("Error while updating R40–R47 fields", e);
		}

		// --------------------------
		// ✅ Update date fields
		// --------------------------
		try {
			existing.setR28_ISSUANCE_DATE(entity.getR28_ISSUANCE_DATE());
			existing.setR28_CONTRACTUAL_MATURITY_DATE(entity.getR28_CONTRACTUAL_MATURITY_DATE());
			existing.setR28_EFFECTIVE_MATURITY_DATE(entity.getR28_EFFECTIVE_MATURITY_DATE());

			existing.setR29_ISSUANCE_DATE(entity.getR29_ISSUANCE_DATE());
			existing.setR29_CONTRACTUAL_MATURITY_DATE(entity.getR29_CONTRACTUAL_MATURITY_DATE());
			existing.setR29_EFFECTIVE_MATURITY_DATE(entity.getR29_EFFECTIVE_MATURITY_DATE());

			existing.setR30_ISSUANCE_DATE(entity.getR30_ISSUANCE_DATE());
			existing.setR30_CONTRACTUAL_MATURITY_DATE(entity.getR30_CONTRACTUAL_MATURITY_DATE());
			existing.setR30_EFFECTIVE_MATURITY_DATE(entity.getR30_EFFECTIVE_MATURITY_DATE());

			existing.setR31_ISSUANCE_DATE(entity.getR31_ISSUANCE_DATE());
			existing.setR31_CONTRACTUAL_MATURITY_DATE(entity.getR31_CONTRACTUAL_MATURITY_DATE());
			existing.setR31_EFFECTIVE_MATURITY_DATE(entity.getR31_EFFECTIVE_MATURITY_DATE());

			existing.setR32_ISSUANCE_DATE(entity.getR32_ISSUANCE_DATE());
			existing.setR32_CONTRACTUAL_MATURITY_DATE(entity.getR32_CONTRACTUAL_MATURITY_DATE());
			existing.setR32_EFFECTIVE_MATURITY_DATE(entity.getR32_EFFECTIVE_MATURITY_DATE());

			existing.setR33_ISSUANCE_DATE(entity.getR33_ISSUANCE_DATE());
			existing.setR33_CONTRACTUAL_MATURITY_DATE(entity.getR33_CONTRACTUAL_MATURITY_DATE());
			existing.setR33_EFFECTIVE_MATURITY_DATE(entity.getR33_EFFECTIVE_MATURITY_DATE());

			existing.setR34_ISSUANCE_DATE(entity.getR34_ISSUANCE_DATE());
			existing.setR34_CONTRACTUAL_MATURITY_DATE(entity.getR34_CONTRACTUAL_MATURITY_DATE());
			existing.setR34_EFFECTIVE_MATURITY_DATE(entity.getR34_EFFECTIVE_MATURITY_DATE());

			// Update dates R40–R46
			existing.setR40_ISSUANCE_DATE(entity.getR40_ISSUANCE_DATE());
			existing.setR40_CONTRACTUAL_MATURITY_DATE(entity.getR40_CONTRACTUAL_MATURITY_DATE());
			existing.setR40_EFFECTIVE_MATURITY_DATE(entity.getR40_EFFECTIVE_MATURITY_DATE());

			existing.setR41_ISSUANCE_DATE(entity.getR41_ISSUANCE_DATE());
			existing.setR41_CONTRACTUAL_MATURITY_DATE(entity.getR41_CONTRACTUAL_MATURITY_DATE());
			existing.setR41_EFFECTIVE_MATURITY_DATE(entity.getR41_EFFECTIVE_MATURITY_DATE());

			existing.setR42_ISSUANCE_DATE(entity.getR42_ISSUANCE_DATE());
			existing.setR42_CONTRACTUAL_MATURITY_DATE(entity.getR42_CONTRACTUAL_MATURITY_DATE());
			existing.setR42_EFFECTIVE_MATURITY_DATE(entity.getR42_EFFECTIVE_MATURITY_DATE());

			existing.setR43_ISSUANCE_DATE(entity.getR43_ISSUANCE_DATE());
			existing.setR43_CONTRACTUAL_MATURITY_DATE(entity.getR43_CONTRACTUAL_MATURITY_DATE());
			existing.setR43_EFFECTIVE_MATURITY_DATE(entity.getR43_EFFECTIVE_MATURITY_DATE());

			existing.setR44_ISSUANCE_DATE(entity.getR44_ISSUANCE_DATE());
			existing.setR44_CONTRACTUAL_MATURITY_DATE(entity.getR44_CONTRACTUAL_MATURITY_DATE());
			existing.setR44_EFFECTIVE_MATURITY_DATE(entity.getR44_EFFECTIVE_MATURITY_DATE());

			existing.setR45_ISSUANCE_DATE(entity.getR45_ISSUANCE_DATE());
			existing.setR45_CONTRACTUAL_MATURITY_DATE(entity.getR45_CONTRACTUAL_MATURITY_DATE());
			existing.setR45_EFFECTIVE_MATURITY_DATE(entity.getR45_EFFECTIVE_MATURITY_DATE());

			existing.setR46_ISSUANCE_DATE(entity.getR46_ISSUANCE_DATE());
			existing.setR46_CONTRACTUAL_MATURITY_DATE(entity.getR46_CONTRACTUAL_MATURITY_DATE());
			existing.setR46_EFFECTIVE_MATURITY_DATE(entity.getR46_EFFECTIVE_MATURITY_DATE());

		} catch (Exception e) {
			throw new RuntimeException("Error while updating date fields", e);
		}

		// --------------------------
		// Save updated entity
		// --------------------------
		M_CA6_Summary_Repo2.save(existing);
	}

	public void updateDetial2(M_CA6_Detail_Entity2 entity) {
		System.out.println("Report Date: " + entity.getReportDate());

		M_CA6_Detail_Entity2 existing = M_CA6_DETAIL_Repo2.findById(entity.getReportDate())
				.orElseThrow(() -> new RuntimeException("Record not found for REPORT_DATE: " + entity.getReportDate()));

		// --------------------------
		// Update R28–R34 amounts
		// --------------------------
		try {
			for (int i = 28; i <= 34; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "PRODUCT", "AMOUNT" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA6_Detail_Entity2.class.getMethod(getterName);
						Method setter = M_CA6_Detail_Entity2.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(entity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// Update R35 total
			Method getter = M_CA6_Detail_Entity2.class.getMethod("getR35_AMOUNT");
			Method setter = M_CA6_Detail_Entity2.class.getMethod("setR35_AMOUNT", getter.getReturnType());
			setter.invoke(existing, getter.invoke(entity));

		} catch (Exception e) {
			throw new RuntimeException("Error while updating R28–R35 fields", e);
		}

		// --------------------------
		// Update R40–R46 + R47 amounts
		// --------------------------
		try {
			for (int i = 40; i <= 46; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "PRODUCT", "AMOUNT" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA6_Detail_Entity2.class.getMethod(getterName);
						Method setter = M_CA6_Detail_Entity2.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(entity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// Update R47
			Method getter = M_CA6_Detail_Entity2.class.getMethod("getR47_AMOUNT");
			Method setter = M_CA6_Detail_Entity2.class.getMethod("setR47_AMOUNT", getter.getReturnType());
			setter.invoke(existing, getter.invoke(entity));

		} catch (Exception e) {
			throw new RuntimeException("Error while updating R40–R47 fields", e);
		}

		// --------------------------
		// ✅ Update date fields
		// --------------------------
		try {
			existing.setR28_ISSUANCE_DATE(entity.getR28_ISSUANCE_DATE());
			existing.setR28_CONTRACTUAL_MATURITY_DATE(entity.getR28_CONTRACTUAL_MATURITY_DATE());
			existing.setR28_EFFECTIVE_MATURITY_DATE(entity.getR28_EFFECTIVE_MATURITY_DATE());

			existing.setR29_ISSUANCE_DATE(entity.getR29_ISSUANCE_DATE());
			existing.setR29_CONTRACTUAL_MATURITY_DATE(entity.getR29_CONTRACTUAL_MATURITY_DATE());
			existing.setR29_EFFECTIVE_MATURITY_DATE(entity.getR29_EFFECTIVE_MATURITY_DATE());

			existing.setR30_ISSUANCE_DATE(entity.getR30_ISSUANCE_DATE());
			existing.setR30_CONTRACTUAL_MATURITY_DATE(entity.getR30_CONTRACTUAL_MATURITY_DATE());
			existing.setR30_EFFECTIVE_MATURITY_DATE(entity.getR30_EFFECTIVE_MATURITY_DATE());

			existing.setR31_ISSUANCE_DATE(entity.getR31_ISSUANCE_DATE());
			existing.setR31_CONTRACTUAL_MATURITY_DATE(entity.getR31_CONTRACTUAL_MATURITY_DATE());
			existing.setR31_EFFECTIVE_MATURITY_DATE(entity.getR31_EFFECTIVE_MATURITY_DATE());

			existing.setR32_ISSUANCE_DATE(entity.getR32_ISSUANCE_DATE());
			existing.setR32_CONTRACTUAL_MATURITY_DATE(entity.getR32_CONTRACTUAL_MATURITY_DATE());
			existing.setR32_EFFECTIVE_MATURITY_DATE(entity.getR32_EFFECTIVE_MATURITY_DATE());

			existing.setR33_ISSUANCE_DATE(entity.getR33_ISSUANCE_DATE());
			existing.setR33_CONTRACTUAL_MATURITY_DATE(entity.getR33_CONTRACTUAL_MATURITY_DATE());
			existing.setR33_EFFECTIVE_MATURITY_DATE(entity.getR33_EFFECTIVE_MATURITY_DATE());

			existing.setR34_ISSUANCE_DATE(entity.getR34_ISSUANCE_DATE());
			existing.setR34_CONTRACTUAL_MATURITY_DATE(entity.getR34_CONTRACTUAL_MATURITY_DATE());
			existing.setR34_EFFECTIVE_MATURITY_DATE(entity.getR34_EFFECTIVE_MATURITY_DATE());

			// Update dates R40–R46
			existing.setR40_ISSUANCE_DATE(entity.getR40_ISSUANCE_DATE());
			existing.setR40_CONTRACTUAL_MATURITY_DATE(entity.getR40_CONTRACTUAL_MATURITY_DATE());
			existing.setR40_EFFECTIVE_MATURITY_DATE(entity.getR40_EFFECTIVE_MATURITY_DATE());

			existing.setR41_ISSUANCE_DATE(entity.getR41_ISSUANCE_DATE());
			existing.setR41_CONTRACTUAL_MATURITY_DATE(entity.getR41_CONTRACTUAL_MATURITY_DATE());
			existing.setR41_EFFECTIVE_MATURITY_DATE(entity.getR41_EFFECTIVE_MATURITY_DATE());

			existing.setR42_ISSUANCE_DATE(entity.getR42_ISSUANCE_DATE());
			existing.setR42_CONTRACTUAL_MATURITY_DATE(entity.getR42_CONTRACTUAL_MATURITY_DATE());
			existing.setR42_EFFECTIVE_MATURITY_DATE(entity.getR42_EFFECTIVE_MATURITY_DATE());

			existing.setR43_ISSUANCE_DATE(entity.getR43_ISSUANCE_DATE());
			existing.setR43_CONTRACTUAL_MATURITY_DATE(entity.getR43_CONTRACTUAL_MATURITY_DATE());
			existing.setR43_EFFECTIVE_MATURITY_DATE(entity.getR43_EFFECTIVE_MATURITY_DATE());

			existing.setR44_ISSUANCE_DATE(entity.getR44_ISSUANCE_DATE());
			existing.setR44_CONTRACTUAL_MATURITY_DATE(entity.getR44_CONTRACTUAL_MATURITY_DATE());
			existing.setR44_EFFECTIVE_MATURITY_DATE(entity.getR44_EFFECTIVE_MATURITY_DATE());

			existing.setR45_ISSUANCE_DATE(entity.getR45_ISSUANCE_DATE());
			existing.setR45_CONTRACTUAL_MATURITY_DATE(entity.getR45_CONTRACTUAL_MATURITY_DATE());
			existing.setR45_EFFECTIVE_MATURITY_DATE(entity.getR45_EFFECTIVE_MATURITY_DATE());

			existing.setR46_ISSUANCE_DATE(entity.getR46_ISSUANCE_DATE());
			existing.setR46_CONTRACTUAL_MATURITY_DATE(entity.getR46_CONTRACTUAL_MATURITY_DATE());
			existing.setR46_EFFECTIVE_MATURITY_DATE(entity.getR46_EFFECTIVE_MATURITY_DATE());

		} catch (Exception e) {
			throw new RuntimeException("Error while updating date fields", e);
		}

		// --------------------------
		// Save updated entity
		// --------------------------
		M_CA6_DETAIL_Repo2.save(existing);
	}

////////////////////////////////////////// RESUBMISSION///////////////////////////////////////////////////////////////////
/// Report Date | Report Version | Domain
/// RESUB VIEW

	public List<Object[]> getM_CA6Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA6_Archival_Summary_Entity1> latestArchivalList = BRRS_M_CA6_Archival_Summary_Repo1
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA6_Archival_Summary_Entity1 entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_CA6 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_CA6Archival() {
		List<Object[]> archivalList = new ArrayList<>();
		try {
			List<M_CA6_Archival_Summary_Entity1> latestArchivalList = BRRS_M_CA6_Archival_Summary_Repo1
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA6_Archival_Summary_Entity1 entity : latestArchivalList) {
					archivalList.add(new Object[] { entity.getReportDate(), entity.getReportVersion() });
				}
				System.out.println("Fetched " + archivalList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_CA6 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	public void updateReportReSub(M_CA6_Summary_Entity1 updatedEntity1, M_CA6_Summary_Entity2 updatedEntity2) {

		System.out.println("Came to M_CA6 Resub Service");
		System.out.println("Report Date: " + updatedEntity1.getReportDate());

		Date reportDate = updatedEntity1.getReportDate();
		BigDecimal newVersion = BigDecimal.ONE;

		try {
			// 🔹 Fetch the latest archival version for this report date from Entity1
			Optional<M_CA6_Archival_Summary_Entity1> latestArchivalOpt1 = BRRS_M_CA6_Archival_Summary_Repo1
					.getLatestArchivalVersionByDate(reportDate);

			if (latestArchivalOpt1.isPresent()) {
				M_CA6_Archival_Summary_Entity1 latestArchival = latestArchivalOpt1.get();
				try {
					newVersion = latestArchival.getReportVersion().add(BigDecimal.ONE);
				} catch (NumberFormatException e) {
					System.err.println("Invalid version format. Defaulting to version 1");
					newVersion = BigDecimal.ONE;
				}
			} else {
				System.out.println("No previous archival found for date: " + reportDate);
			}

			// 🔹 Prevent duplicate version number in Repo1
			boolean exists = BRRS_M_CA6_Archival_Summary_Repo1.findByReportDateAndReportVersion(reportDate, newVersion)
					.isPresent();

			if (exists) {
				throw new RuntimeException("⚠ Version " + newVersion + " already exists for report date " + reportDate);
			}

			// Copy data from summary to archival entities for all 3 entities
			M_CA6_Archival_Summary_Entity1 archivalEntity1 = new M_CA6_Archival_Summary_Entity1();
			M_CA6_Archival_Summary_Entity2 archivalEntity2 = new M_CA6_Archival_Summary_Entity2();

			org.springframework.beans.BeanUtils.copyProperties(updatedEntity1, archivalEntity1);
			org.springframework.beans.BeanUtils.copyProperties(updatedEntity2, archivalEntity2);

			// Set common fields
			Date now = new Date();
			archivalEntity1.setReportDate(reportDate);
			archivalEntity2.setReportDate(reportDate);

			archivalEntity1.setReportVersion(newVersion);
			archivalEntity2.setReportVersion(newVersion);

			archivalEntity1.setReportResubDate(now);
			archivalEntity2.setReportResubDate(now);

			System.out.println("Saving new archival version: " + newVersion);

			// Save to all three archival repositories
			BRRS_M_CA6_Archival_Summary_Repo1.save(archivalEntity1);
			BRRS_M_CA6_Archival_Summary_Repo2.save(archivalEntity2);

			System.out.println("Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creating M_CA6 archival resubmission record", e);
		}
	}

	// Normal Format Excel
	public byte[] getM_CA6Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= Download SCREEN =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");

		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return getExcelM_CA6ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA6ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
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
				return getEmail_M_CA6Excel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} 
			
			else {
				
			
		List<M_CA6_Summary_Entity1> dataList = M_CA6_Summary_Repo1.getdatabydateList(dateformat.parse(todate));

		List<M_CA6_Summary_Entity2> dataList1 = M_CA6_Summary_Repo2.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(templateDir);
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
			// --- End of Style Definitions ---
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_CA6_Summary_Entity1 record = dataList.get(i);
					M_CA6_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("enterred serice method.....");
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.getRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row12
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");

					}

					// row13
					// Column E
					cell4 = row.getCell(4);
					if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");

					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");

					}

					// row14
					// Column E
					cell4 = row.getCell(4);
					if (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");

					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");

					}

					// row15
					// Column E
					cell4 = row.getCell(4);
					if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row16
					row = sheet.getRow(15);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row16
					// Column E
					cell4 = row.getCell(4);
					if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row20
					// Column E
					cell4 = row.getCell(4);
					if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row21
					// Column E
					cell4 = row.getCell(4);
					if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row22
					// Column E
					cell4 = row.getCell(4);
					if (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row23
					// Column E
					cell4 = row.getCell(4);
					if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row24
					// Column E
					cell4 = row.getCell(4);
					if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row28
					row = sheet.getRow(27);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR28_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR28_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR28_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR28_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR28_AMOUNT() != null) {
						cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR29_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR29_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR29_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR29_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR29_AMOUNT() != null) {
						cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row30
					row = sheet.getRow(29);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR30_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR30_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR30_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR30_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR30_AMOUNT() != null) {
						cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row31
					row = sheet.getRow(30);
					// Column D

					cell3 = row.getCell(2);
					if (record2.getR31_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR31_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR31_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR31_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR31_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR31_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR31_AMOUNT() != null) {
						cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row32
					row = sheet.getRow(31);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR32_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR32_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR32_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR32_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR32_AMOUNT() != null) {
						cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row33
					row = sheet.getRow(32);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR33_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR33_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR33_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR33_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR33_AMOUNT() != null) {
						cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row34
					row = sheet.getRow(33);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR34_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR34_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR34_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR34_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR34_AMOUNT() != null) {
						cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row40
					row = sheet.getRow(39);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR40_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR40_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR40_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR40_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR40_AMOUNT() != null) {
						cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row41
					row = sheet.getRow(40);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR41_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR41_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR41_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR41_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR41_AMOUNT() != null) {
						cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row42
					row = sheet.getRow(41);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR42_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR42_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR42_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR42_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR42_AMOUNT() != null) {
						cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row43
					row = sheet.getRow(42);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR43_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR43_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR43_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR43_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR43_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR43_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR43_AMOUNT() != null) {
						cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row44
					row = sheet.getRow(43);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR44_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR44_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR44_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR44_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR44_AMOUNT() != null) {
						cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row45
					row = sheet.getRow(44);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR45_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR45_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR45_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR45_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR45_AMOUNT() != null) {
						cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row46
					row = sheet.getRow(45);
					// Column D

					cell3 = row.getCell(2);
					if (record2.getR46_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR46_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR46_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR46_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR46_AMOUNT() != null) {
						cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
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
	public byte[] getEmail_M_CA6Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		List<M_CA6_Summary_Entity1> dataList = M_CA6_Summary_Repo1.getdatabydateList(dateformat.parse(todate));

		List<M_CA6_Summary_Entity2> dataList1 = M_CA6_Summary_Repo2.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(templateDir);
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
			// --- End of Style Definitions ---
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_CA6_Summary_Entity1 record = dataList.get(i);
					M_CA6_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("enterred serice method.....");
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.getRow(startRow + i);
					}

					Cell cell2 = row.getCell(2);
					// row12
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row12
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");

					}

					// row13
					// Column E
					cell4 = row.getCell(4);
					if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");

					}

					/*
					 * // row14 row = sheet.getRow(13); // Column D cell3 = row.getCell(3); if
					 * (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT()
					 * .doubleValue()); cell3.setCellStyle(numberStyle); } else {
					 * cell3.setCellValue("");
					 * 
					 * }
					 * 
					 * // row14 // Column E cell4 = row.getCell(4); if
					 * (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().
					 * doubleValue()); cell4.setCellStyle(numberStyle); } else {
					 * cell4.setCellValue("");
					 * 
					 * }
					 */

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");

					}

					// row15
					// Column E
					cell4 = row.getCell(4);
					if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row16
					row = sheet.getRow(15);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row16
					// Column E
					cell4 = row.getCell(4);
					if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row20
					// Column E
					cell4 = row.getCell(4);
					if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row21
					// Column E
					cell4 = row.getCell(4);
					if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row22
					/*
					 * row = sheet.getRow(21); // Column D cell3 = row.getCell(3); if
					 * (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT()
					 * .doubleValue()); cell3.setCellStyle(numberStyle); } else {
					 * cell3.setCellValue(""); }
					 * 
					 * // row22 // Column E cell4 = row.getCell(4); if
					 * (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().
					 * doubleValue()); cell4.setCellStyle(numberStyle); } else {
					 * cell4.setCellValue(""); }
					 */

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row23
					// Column E
					cell4 = row.getCell(4);
					if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row24
					// Column E
					cell4 = row.getCell(4);
					if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row28
					row = sheet.getRow(27);

					cell2 = row.getCell(1);
					if (record2.getR28_PRODUCT() != null) {
						cell2.setCellValue(record2.getR28_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR28_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR28_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR28_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR28_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR28_AMOUNT() != null) {
						cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);

					cell2 = row.getCell(1);
					if (record2.getR29_PRODUCT() != null) {
						cell2.setCellValue(record2.getR29_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR29_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR29_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR29_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR29_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR29_AMOUNT() != null) {
						cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row30
					row = sheet.getRow(29);

					cell2 = row.getCell(1);
					if (record2.getR30_PRODUCT() != null) {
						cell2.setCellValue(record2.getR30_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR30_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR30_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR30_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR30_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR30_AMOUNT() != null) {
						cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row31
					row = sheet.getRow(30);

					cell2 = row.getCell(1);
					if (record2.getR31_PRODUCT() != null) {
						cell2.setCellValue(record2.getR31_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR31_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR31_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);

					/*
					 * if (record2.getR31_CONTRACTUAL_MATURITY_DATE() != null) {
					 * cell3.setCellValue(record2.getR31_CONTRACTUAL_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); } cell3 =
					 * row.getCell(4); if (record2.getR31_EFFECTIVE_MATURITY_DATE() != null) {
					 * cell3.setCellValue(record2.getR31_EFFECTIVE_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); }
					 */

					cell3 = row.getCell(5);
					if (record2.getR31_AMOUNT() != null) {
						cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row32
					row = sheet.getRow(31);

					cell2 = row.getCell(1);
					if (record2.getR32_PRODUCT() != null) {
						cell2.setCellValue(record2.getR32_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR32_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR32_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR32_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR32_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR32_AMOUNT() != null) {
						cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row33
					row = sheet.getRow(32);

					cell2 = row.getCell(1);
					if (record2.getR33_PRODUCT() != null) {
						cell2.setCellValue(record2.getR33_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR33_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR33_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR33_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR33_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR33_AMOUNT() != null) {
						cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row34
					row = sheet.getRow(33);

					cell2 = row.getCell(1);
					if (record2.getR34_PRODUCT() != null) {
						cell2.setCellValue(record2.getR34_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR34_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR34_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR34_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR34_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR34_AMOUNT() != null) {
						cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row40
					row = sheet.getRow(39);

					cell2 = row.getCell(1);
					if (record2.getR40_PRODUCT() != null) {
						cell2.setCellValue(record2.getR40_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR40_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR40_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR40_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR40_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR40_AMOUNT() != null) {
						cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row41
					row = sheet.getRow(40);
					cell2 = row.getCell(1);
					if (record2.getR41_PRODUCT() != null) {
						cell2.setCellValue(record2.getR41_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR41_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR41_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR41_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR41_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR41_AMOUNT() != null) {
						cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row42
					row = sheet.getRow(41);
					cell2 = row.getCell(1);
					if (record2.getR42_PRODUCT() != null) {
						cell2.setCellValue(record2.getR42_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR42_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR42_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR42_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR42_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR42_AMOUNT() != null) {
						cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row43
					row = sheet.getRow(42);
					cell2 = row.getCell(1);
					if (record2.getR43_PRODUCT() != null) {
						cell2.setCellValue(record2.getR43_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR43_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR43_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					/*
					 * cell3 = row.getCell(3); if (record2.getR43_CONTRACTUAL_MATURITY_DATE() !=
					 * null) { cell3.setCellValue(record2.getR43_CONTRACTUAL_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); } cell3 =
					 * row.getCell(4); if (record2.getR43_EFFECTIVE_MATURITY_DATE() != null) {
					 * cell3.setCellValue(record2.getR43_EFFECTIVE_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); }
					 */

					cell3 = row.getCell(5);
					if (record2.getR43_AMOUNT() != null) {
						cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row44
					row = sheet.getRow(43);
					cell2 = row.getCell(1);
					if (record2.getR44_PRODUCT() != null) {
						cell2.setCellValue(record2.getR44_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR44_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR44_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR44_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR44_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR44_AMOUNT() != null) {
						cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row45
					row = sheet.getRow(44);
					cell2 = row.getCell(1);
					if (record2.getR45_PRODUCT() != null) {
						cell2.setCellValue(record2.getR45_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR45_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR45_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR45_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR45_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR45_AMOUNT() != null) {
						cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row46
					row = sheet.getRow(45);
					cell2 = row.getCell(1);
					if (record2.getR46_PRODUCT() != null) {
						cell2.setCellValue(record2.getR46_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR46_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR46_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR46_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR46_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR46_AMOUNT() != null) {
						cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
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
	public byte[] getExcelM_CA6ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CA6ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA6_Archival_Summary_Entity1> dataList = BRRS_M_CA6_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_CA6_Archival_Summary_Entity2> dataList1 = BRRS_M_CA6_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BWRBR report. Returning empty result.");
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

					M_CA6_Archival_Summary_Entity1 record = dataList.get(i);
					M_CA6_Archival_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row16
					row = sheet.getRow(15);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row28
					row = sheet.getRow(27);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR28_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR28_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR28_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR28_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR28_AMOUNT() != null) {
						cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR29_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR29_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR29_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR29_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR29_AMOUNT() != null) {
						cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row30
					row = sheet.getRow(29);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR30_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR30_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR30_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR30_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR30_AMOUNT() != null) {
						cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row31
					row = sheet.getRow(30);
					// Column D

					cell3 = row.getCell(2);
					if (record2.getR31_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR31_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR31_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR31_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR31_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR31_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR31_AMOUNT() != null) {
						cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row32
					row = sheet.getRow(31);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR32_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR32_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR32_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR32_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR32_AMOUNT() != null) {
						cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row33
					row = sheet.getRow(32);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR33_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR33_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR33_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR33_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR33_AMOUNT() != null) {
						cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row34
					row = sheet.getRow(33);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR34_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR34_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR34_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR34_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR34_AMOUNT() != null) {
						cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row40
					row = sheet.getRow(39);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR40_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR40_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR40_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR40_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR40_AMOUNT() != null) {
						cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row41
					row = sheet.getRow(40);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR41_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR41_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR41_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR41_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR41_AMOUNT() != null) {
						cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row42
					row = sheet.getRow(41);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR42_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR42_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR42_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR42_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR42_AMOUNT() != null) {
						cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row43
					row = sheet.getRow(42);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR43_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR43_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR43_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR43_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR43_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR43_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR43_AMOUNT() != null) {
						cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row44
					row = sheet.getRow(43);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR44_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR44_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR44_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR44_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR44_AMOUNT() != null) {
						cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row45
					row = sheet.getRow(44);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR45_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR45_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR45_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR45_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR45_AMOUNT() != null) {
						cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row46
					row = sheet.getRow(45);
					// Column D

					cell3 = row.getCell(2);
					if (record2.getR46_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR46_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR46_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR46_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR46_AMOUNT() != null) {
						cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
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
	public byte[] BRRS_M_CA6ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		List<M_CA6_Archival_Summary_Entity1> dataList = BRRS_M_CA6_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);

		List<M_CA6_Archival_Summary_Entity2> dataList1 = BRRS_M_CA6_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(templateDir);
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
			// --- End of Style Definitions ---
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_CA6_Archival_Summary_Entity1 record = dataList.get(i);
					M_CA6_Archival_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("enterred serice method.....");
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.getRow(startRow + i);
					}

					Cell cell2 = row.getCell(2);
					// row12
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row12
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");

					}

					// row13
					// Column E
					cell4 = row.getCell(4);
					if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");

					}

					/*
					 * // row14 row = sheet.getRow(13); // Column D cell3 = row.getCell(3); if
					 * (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT()
					 * .doubleValue()); cell3.setCellStyle(numberStyle); } else {
					 * cell3.setCellValue("");
					 * 
					 * }
					 * 
					 * // row14 // Column E cell4 = row.getCell(4); if
					 * (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().
					 * doubleValue()); cell4.setCellStyle(numberStyle); } else {
					 * cell4.setCellValue("");
					 * 
					 * }
					 */

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");

					}

					// row15
					// Column E
					cell4 = row.getCell(4);
					if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row16
					row = sheet.getRow(15);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row16
					// Column E
					cell4 = row.getCell(4);
					if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row20
					// Column E
					cell4 = row.getCell(4);
					if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row21
					// Column E
					cell4 = row.getCell(4);
					if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row22
					/*
					 * row = sheet.getRow(21); // Column D cell3 = row.getCell(3); if
					 * (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT()
					 * .doubleValue()); cell3.setCellStyle(numberStyle); } else {
					 * cell3.setCellValue(""); }
					 * 
					 * // row22 // Column E cell4 = row.getCell(4); if
					 * (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().
					 * doubleValue()); cell4.setCellStyle(numberStyle); } else {
					 * cell4.setCellValue(""); }
					 */

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row23
					// Column E
					cell4 = row.getCell(4);
					if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row24
					// Column E
					cell4 = row.getCell(4);
					if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row28
					row = sheet.getRow(27);

					cell2 = row.getCell(1);
					if (record2.getR28_PRODUCT() != null) {
						cell2.setCellValue(record2.getR28_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR28_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR28_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR28_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR28_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR28_AMOUNT() != null) {
						cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);

					cell2 = row.getCell(1);
					if (record2.getR29_PRODUCT() != null) {
						cell2.setCellValue(record2.getR29_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR29_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR29_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR29_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR29_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR29_AMOUNT() != null) {
						cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row30
					row = sheet.getRow(29);

					cell2 = row.getCell(1);
					if (record2.getR30_PRODUCT() != null) {
						cell2.setCellValue(record2.getR30_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR30_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR30_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR30_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR30_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR30_AMOUNT() != null) {
						cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row31
					row = sheet.getRow(30);

					cell2 = row.getCell(1);
					if (record2.getR31_PRODUCT() != null) {
						cell2.setCellValue(record2.getR31_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR31_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR31_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);

					/*
					 * if (record2.getR31_CONTRACTUAL_MATURITY_DATE() != null) {
					 * cell3.setCellValue(record2.getR31_CONTRACTUAL_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); } cell3 =
					 * row.getCell(4); if (record2.getR31_EFFECTIVE_MATURITY_DATE() != null) {
					 * cell3.setCellValue(record2.getR31_EFFECTIVE_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); }
					 */

					cell3 = row.getCell(5);
					if (record2.getR31_AMOUNT() != null) {
						cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row32
					row = sheet.getRow(31);

					cell2 = row.getCell(1);
					if (record2.getR32_PRODUCT() != null) {
						cell2.setCellValue(record2.getR32_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR32_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR32_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR32_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR32_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR32_AMOUNT() != null) {
						cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row33
					row = sheet.getRow(32);

					cell2 = row.getCell(1);
					if (record2.getR33_PRODUCT() != null) {
						cell2.setCellValue(record2.getR33_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR33_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR33_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR33_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR33_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR33_AMOUNT() != null) {
						cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row34
					row = sheet.getRow(33);

					cell2 = row.getCell(1);
					if (record2.getR34_PRODUCT() != null) {
						cell2.setCellValue(record2.getR34_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR34_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR34_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR34_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR34_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR34_AMOUNT() != null) {
						cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row40
					row = sheet.getRow(39);

					cell2 = row.getCell(1);
					if (record2.getR40_PRODUCT() != null) {
						cell2.setCellValue(record2.getR40_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR40_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR40_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR40_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR40_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR40_AMOUNT() != null) {
						cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row41
					row = sheet.getRow(40);
					cell2 = row.getCell(1);
					if (record2.getR41_PRODUCT() != null) {
						cell2.setCellValue(record2.getR41_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR41_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR41_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR41_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR41_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR41_AMOUNT() != null) {
						cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row42
					row = sheet.getRow(41);
					cell2 = row.getCell(1);
					if (record2.getR42_PRODUCT() != null) {
						cell2.setCellValue(record2.getR42_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR42_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR42_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR42_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR42_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR42_AMOUNT() != null) {
						cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row43
					row = sheet.getRow(42);
					cell2 = row.getCell(1);
					if (record2.getR43_PRODUCT() != null) {
						cell2.setCellValue(record2.getR43_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR43_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR43_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					/*
					 * cell3 = row.getCell(3); if (record2.getR43_CONTRACTUAL_MATURITY_DATE() !=
					 * null) { cell3.setCellValue(record2.getR43_CONTRACTUAL_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); } cell3 =
					 * row.getCell(4); if (record2.getR43_EFFECTIVE_MATURITY_DATE() != null) {
					 * cell3.setCellValue(record2.getR43_EFFECTIVE_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); }
					 */

					cell3 = row.getCell(5);
					if (record2.getR43_AMOUNT() != null) {
						cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row44
					row = sheet.getRow(43);
					cell2 = row.getCell(1);
					if (record2.getR44_PRODUCT() != null) {
						cell2.setCellValue(record2.getR44_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR44_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR44_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR44_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR44_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR44_AMOUNT() != null) {
						cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row45
					row = sheet.getRow(44);
					cell2 = row.getCell(1);
					if (record2.getR45_PRODUCT() != null) {
						cell2.setCellValue(record2.getR45_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR45_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR45_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR45_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR45_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR45_AMOUNT() != null) {
						cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row46
					row = sheet.getRow(45);
					cell2 = row.getCell(1);
					if (record2.getR46_PRODUCT() != null) {
						cell2.setCellValue(record2.getR46_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR46_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR46_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR46_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR46_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR46_AMOUNT() != null) {
						cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
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
	public byte[] BRRS_M_CA6ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CA6ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}
		

		List<M_CA6_RESUB_Summary_Entity1> dataList = brrs_m_ca6_resub_summary_repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_CA6_RESUB_Summary_Entity2> dataList1 = brrs_m_ca6_resub_summary_repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA6 report. Returning empty result.");
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

					M_CA6_RESUB_Summary_Entity1 record = dataList.get(i);
					M_CA6_RESUB_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row16
					row = sheet.getRow(15);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row28
					row = sheet.getRow(27);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR28_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR28_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR28_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR28_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR28_AMOUNT() != null) {
						cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR29_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR29_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR29_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR29_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR29_AMOUNT() != null) {
						cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row30
					row = sheet.getRow(29);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR30_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR30_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR30_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR30_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR30_AMOUNT() != null) {
						cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row31
					row = sheet.getRow(30);
					// Column D

					cell3 = row.getCell(2);
					if (record2.getR31_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR31_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR31_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR31_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR31_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR31_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR31_AMOUNT() != null) {
						cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row32
					row = sheet.getRow(31);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR32_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR32_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR32_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR32_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR32_AMOUNT() != null) {
						cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row33
					row = sheet.getRow(32);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR33_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR33_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR33_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR33_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR33_AMOUNT() != null) {
						cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row34
					row = sheet.getRow(33);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR34_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR34_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR34_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR34_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR34_AMOUNT() != null) {
						cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row40
					row = sheet.getRow(39);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR40_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR40_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR40_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR40_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR40_AMOUNT() != null) {
						cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row41
					row = sheet.getRow(40);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR41_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR41_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR41_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR41_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR41_AMOUNT() != null) {
						cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row42
					row = sheet.getRow(41);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR42_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR42_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR42_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR42_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR42_AMOUNT() != null) {
						cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row43
					row = sheet.getRow(42);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR43_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR43_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR43_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR43_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR43_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR43_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR43_AMOUNT() != null) {
						cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row44
					row = sheet.getRow(43);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR44_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR44_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR44_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR44_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR44_AMOUNT() != null) {
						cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row45
					row = sheet.getRow(44);
					// Column D
					cell3 = row.getCell(2);
					if (record2.getR45_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR45_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR45_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR45_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR45_AMOUNT() != null) {
						cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row46
					row = sheet.getRow(45);
					// Column D

					cell3 = row.getCell(2);
					if (record2.getR46_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR46_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR46_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR46_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR46_AMOUNT() != null) {
						cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
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
	public byte[] BRRS_M_CA6ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		List<M_CA6_RESUB_Summary_Entity1> dataList = brrs_m_ca6_resub_summary_repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);

		List<M_CA6_RESUB_Summary_Entity2> dataList1 = brrs_m_ca6_resub_summary_repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(templateDir);
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
			// --- End of Style Definitions ---
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_CA6_RESUB_Summary_Entity1 record = dataList.get(i);
					M_CA6_RESUB_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("enterred serice method.....");
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.getRow(startRow + i);
					}

					Cell cell2 = row.getCell(2);
					// row12
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row12
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");

					}

					// row13
					// Column E
					cell4 = row.getCell(4);
					if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");

					}

					/*
					 * // row14 row = sheet.getRow(13); // Column D cell3 = row.getCell(3); if
					 * (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT()
					 * .doubleValue()); cell3.setCellStyle(numberStyle); } else {
					 * cell3.setCellValue("");
					 * 
					 * }
					 * 
					 * // row14 // Column E cell4 = row.getCell(4); if
					 * (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().
					 * doubleValue()); cell4.setCellStyle(numberStyle); } else {
					 * cell4.setCellValue("");
					 * 
					 * }
					 */

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");

					}

					// row15
					// Column E
					cell4 = row.getCell(4);
					if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row16
					row = sheet.getRow(15);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row16
					// Column E
					cell4 = row.getCell(4);
					if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row20
					// Column E
					cell4 = row.getCell(4);
					if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row21
					// Column E
					cell4 = row.getCell(4);
					if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row22
					/*
					 * row = sheet.getRow(21); // Column D cell3 = row.getCell(3); if
					 * (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT()
					 * .doubleValue()); cell3.setCellStyle(numberStyle); } else {
					 * cell3.setCellValue(""); }
					 * 
					 * // row22 // Column E cell4 = row.getCell(4); if
					 * (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
					 * cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().
					 * doubleValue()); cell4.setCellStyle(numberStyle); } else {
					 * cell4.setCellValue(""); }
					 */

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row23
					// Column E
					cell4 = row.getCell(4);
					if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row24
					// Column E
					cell4 = row.getCell(4);
					if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
					}

					// row28
					row = sheet.getRow(27);

					cell2 = row.getCell(1);
					if (record2.getR28_PRODUCT() != null) {
						cell2.setCellValue(record2.getR28_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR28_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR28_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR28_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR28_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR28_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR28_AMOUNT() != null) {
						cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row29
					row = sheet.getRow(28);

					cell2 = row.getCell(1);
					if (record2.getR29_PRODUCT() != null) {
						cell2.setCellValue(record2.getR29_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR29_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR29_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR29_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR29_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR29_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR29_AMOUNT() != null) {
						cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row30
					row = sheet.getRow(29);

					cell2 = row.getCell(1);
					if (record2.getR30_PRODUCT() != null) {
						cell2.setCellValue(record2.getR30_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR30_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR30_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR30_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR30_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR30_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR30_AMOUNT() != null) {
						cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row31
					row = sheet.getRow(30);

					cell2 = row.getCell(1);
					if (record2.getR31_PRODUCT() != null) {
						cell2.setCellValue(record2.getR31_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR31_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR31_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);

					/*
					 * if (record2.getR31_CONTRACTUAL_MATURITY_DATE() != null) {
					 * cell3.setCellValue(record2.getR31_CONTRACTUAL_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); } cell3 =
					 * row.getCell(4); if (record2.getR31_EFFECTIVE_MATURITY_DATE() != null) {
					 * cell3.setCellValue(record2.getR31_EFFECTIVE_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); }
					 */

					cell3 = row.getCell(5);
					if (record2.getR31_AMOUNT() != null) {
						cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row32
					row = sheet.getRow(31);

					cell2 = row.getCell(1);
					if (record2.getR32_PRODUCT() != null) {
						cell2.setCellValue(record2.getR32_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR32_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR32_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR32_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR32_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR32_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR32_AMOUNT() != null) {
						cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row33
					row = sheet.getRow(32);

					cell2 = row.getCell(1);
					if (record2.getR33_PRODUCT() != null) {
						cell2.setCellValue(record2.getR33_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR33_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR33_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR33_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR33_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR33_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR33_AMOUNT() != null) {
						cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row34
					row = sheet.getRow(33);

					cell2 = row.getCell(1);
					if (record2.getR34_PRODUCT() != null) {
						cell2.setCellValue(record2.getR34_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR34_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR34_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR34_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR34_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR34_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR34_AMOUNT() != null) {
						cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row40
					row = sheet.getRow(39);

					cell2 = row.getCell(1);
					if (record2.getR40_PRODUCT() != null) {
						cell2.setCellValue(record2.getR40_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR40_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR40_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR40_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR40_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR40_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR40_AMOUNT() != null) {
						cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row41
					row = sheet.getRow(40);
					cell2 = row.getCell(1);
					if (record2.getR41_PRODUCT() != null) {
						cell2.setCellValue(record2.getR41_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR41_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR41_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR41_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR41_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR41_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR41_AMOUNT() != null) {
						cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row42
					row = sheet.getRow(41);
					cell2 = row.getCell(1);
					if (record2.getR42_PRODUCT() != null) {
						cell2.setCellValue(record2.getR42_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR42_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR42_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR42_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR42_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR42_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR42_AMOUNT() != null) {
						cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row43
					row = sheet.getRow(42);
					cell2 = row.getCell(1);
					if (record2.getR43_PRODUCT() != null) {
						cell2.setCellValue(record2.getR43_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR43_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR43_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					/*
					 * cell3 = row.getCell(3); if (record2.getR43_CONTRACTUAL_MATURITY_DATE() !=
					 * null) { cell3.setCellValue(record2.getR43_CONTRACTUAL_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); } cell3 =
					 * row.getCell(4); if (record2.getR43_EFFECTIVE_MATURITY_DATE() != null) {
					 * cell3.setCellValue(record2.getR43_EFFECTIVE_MATURITY_DATE());
					 * cell3.setCellStyle(dateStyle); } else { cell3.setCellValue(""); }
					 */

					cell3 = row.getCell(5);
					if (record2.getR43_AMOUNT() != null) {
						cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row44
					row = sheet.getRow(43);
					cell2 = row.getCell(1);
					if (record2.getR44_PRODUCT() != null) {
						cell2.setCellValue(record2.getR44_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR44_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR44_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR44_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR44_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR44_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR44_AMOUNT() != null) {
						cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row45
					row = sheet.getRow(44);
					cell2 = row.getCell(1);
					if (record2.getR45_PRODUCT() != null) {
						cell2.setCellValue(record2.getR45_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}
					cell3 = row.getCell(2);
					if (record2.getR45_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR45_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR45_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR45_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR45_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR45_AMOUNT() != null) {
						cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
					}

					// row46
					row = sheet.getRow(45);
					cell2 = row.getCell(1);
					if (record2.getR46_PRODUCT() != null) {
						cell2.setCellValue(record2.getR46_PRODUCT());
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
					}

					cell3 = row.getCell(2);
					if (record2.getR46_ISSUANCE_DATE() != null) {
						cell3.setCellValue(record2.getR46_ISSUANCE_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(3);
					if (record2.getR46_CONTRACTUAL_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_CONTRACTUAL_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(4);
					if (record2.getR46_EFFECTIVE_MATURITY_DATE() != null) {
						cell3.setCellValue(record2.getR46_EFFECTIVE_MATURITY_DATE());
						cell3.setCellStyle(dateStyle);
					} else {
						cell3.setCellValue("");
					}
					cell3 = row.getCell(5);
					if (record2.getR46_AMOUNT() != null) {
						cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
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
