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

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.bornfire.brrs.entities.UserProfileRep;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.io.Serializable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;


@Component
@Service

public class BRRS_M_SIR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SIR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	UserProfileRep userProfileRep;


	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ------------------------------
	// Returns the ModelAndView for M_SIR report based on type and version
	// ------------------------------
	public ModelAndView getBRRS_M_SIRView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {

		ModelAndView mv = new ModelAndView();
		
		
		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);
		
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
				String sql = "SELECT * FROM BRRS_M_SIR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
				List<M_SIR_Archival_Summary_Entity> T1Master = jdbcTemplate.query(sql, new Object[]{d1, version}, new BeanPropertyRowMapper<>(M_SIR_Archival_Summary_Entity.class));
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				String sql = "SELECT * FROM BRRS_M_SIR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
				List<M_SIR_Resub_Summary_Entity> T1Master = jdbcTemplate.query(sql, new Object[]{d1, version}, new BeanPropertyRowMapper<>(M_SIR_Resub_Summary_Entity.class));

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				String sql = "SELECT * FROM BRRS_M_SIR_SUMMARYTABLE WHERE REPORT_DATE = ?";
				List<M_SIR_Summary_Entity> T1Master = jdbcTemplate.query(sql, new Object[]{d1}, new BeanPropertyRowMapper<>(M_SIR_Summary_Entity.class));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					String sql = "SELECT * FROM BRRS_M_SIR_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
					List<M_SIR_Archival_Detail_Entity> T1Master = jdbcTemplate.query(sql, new Object[]{d1, version}, new BeanPropertyRowMapper<>(M_SIR_Archival_Detail_Entity.class));
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					String sql = "SELECT * FROM BRRS_M_SIR_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
					List<M_SIR_Resub_Detail_Entity> T1Master = jdbcTemplate.query(sql, new Object[]{d1, version}, new BeanPropertyRowMapper<>(M_SIR_Resub_Detail_Entity.class));

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					String sql = "SELECT * FROM BRRS_M_SIR_DETAILTABLE WHERE REPORT_DATE = ?";
					List<M_SIR_Detail_Entity> T1Master = jdbcTemplate.query(sql, new Object[]{dateformat.parse(todate)}, new BeanPropertyRowMapper<>(M_SIR_Detail_Entity.class));
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

	// ------------------------------
	// Updates the normal summary and detail records for a given report date
	// ------------------------------
	@Transactional
	public void updateReport(M_SIR_Summary_Entity updatedEntity) {

		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// 🔹 Fetch existing SUMMARY
		String sumSql = "SELECT * FROM BRRS_M_SIR_SUMMARYTABLE WHERE REPORT_DATE = ?";
		List<M_SIR_Summary_Entity> sumList = jdbcTemplate.query(sumSql, new Object[]{updatedEntity.getReportDate()}, new BeanPropertyRowMapper<>(M_SIR_Summary_Entity.class));
		if (sumList.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate());
		}
		M_SIR_Summary_Entity existingSummary = sumList.get(0);
		
		// 🔹 Create Audit Copy before editing
		M_SIR_Summary_Entity oldcopy = new M_SIR_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);
		
		// 🔹 Fetch or create DETAIL
		String dtlSql = "SELECT * FROM BRRS_M_SIR_DETAILTABLE WHERE REPORT_DATE = ?";
		List<M_SIR_Detail_Entity> dtlList = jdbcTemplate.query(dtlSql, new Object[]{updatedEntity.getReportDate()}, new BeanPropertyRowMapper<>(M_SIR_Detail_Entity.class));
		M_SIR_Detail_Entity detailEntity;
		boolean isNewDetail = false;
		if (dtlList.isEmpty()) {
			detailEntity = new M_SIR_Detail_Entity();
			detailEntity.setReportDate(updatedEntity.getReportDate());
			isNewDetail = true;
		} else {
			detailEntity = dtlList.get(0);
		}

		try {
			// 1️⃣ Loop from R13 to R17
			for (int i = 13; i <= 17; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
						"amt_gt24m", "risk_gt24m", "capital_gt24m" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existingSummary);

						// --- FIX: Normalize nulls vs empty strings to prevent audit bloat ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						summarySetter.invoke(existingSummary, newValue);

						Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());
						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// 2️⃣ Loop for R12 Fields
			String[] totalFields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
					"amt_gt24m", "risk_gt24m", "capital_gt24m" };
			for (String field : totalFields) {
				String getterName = "getR12_" + field;
				String setterName = "setR12_" + field;

				try {
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
					Object newValue = getter.invoke(updatedEntity);
					Object existingValue = getter.invoke(existingSummary);

					// --- FIX: Normalize nulls vs empty strings ---
					String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
					String newValStr = (newValue == null) ? "" : newValue.toString().trim();

					if (currentValStr.equals(newValStr)) {
						continue;
					}

					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					summarySetter.invoke(existingSummary, newValue);

					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());
					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		try {
			// 3️⃣ Loop from R19 to R23
			for (int i = 19; i <= 23; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
						"amt_gt24m", "risk_gt24m", "capital_gt24m" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existingSummary);

						// --- FIX: Normalize nulls vs empty strings ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						summarySetter.invoke(existingSummary, newValue);

						Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());
						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// 4️⃣ Loop for R18 Fields
			String[] totalFields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
					"amt_gt24m", "risk_gt24m", "capital_gt24m" };
			for (String field : totalFields) {
				String getterName = "getR18_" + field;
				String setterName = "setR18_" + field;

				try {
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
					Object newValue = getter.invoke(updatedEntity);
					Object existingValue = getter.invoke(existingSummary);

					// --- FIX: Normalize nulls vs empty strings ---
					String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
					String newValStr = (newValue == null) ? "" : newValue.toString().trim();

					if (currentValStr.equals(newValStr)) {
						continue;
					}

					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					summarySetter.invoke(existingSummary, newValue);

					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());
					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		try {
			// 5️⃣ Loop from R24 to R26
			for (int i = 24; i <= 26; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
						"amt_gt24m", "risk_gt24m", "capital_gt24m" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existingSummary);

						// --- FIX: Normalize nulls vs empty strings ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						summarySetter.invoke(existingSummary, newValue);

						Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());
						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		try {
			// 6️⃣ Loop from R28 to R32
			for (int i = 28; i <= 32; i++) {
				String prefix = "R" + i + "_";
				String[] fields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
						"amt_gt24m", "risk_gt24m", "capital_gt24m" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
						Object newValue = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existingSummary);

						// --- FIX: Normalize nulls vs empty strings ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						summarySetter.invoke(existingSummary, newValue);

						Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());
						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// 7️⃣ Loop for R27 Fields
			String[] totalFields = { "amt_6m", "risk_6m", "capital_6m", "amt_6to24m", "risk_6to24m", "capital_6to24m",
					"amt_gt24m", "risk_gt24m", "capital_gt24m" };
			for (String field : totalFields) {
				String getterName = "getR27_" + field;
				String setterName = "setR27_" + field;

				try {
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
					Object newValue = getter.invoke(updatedEntity);
					Object existingValue = getter.invoke(existingSummary);

					// --- FIX: Normalize nulls vs empty strings ---
					String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
					String newValStr = (newValue == null) ? "" : newValue.toString().trim();

					if (currentValStr.equals(newValStr)) {
						continue;
					}

					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					summarySetter.invoke(existingSummary, newValue);

					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());
					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		try {
			// 8️⃣ Loop for R33 Fields
			String[] fields = { "capital_6m", "capital_6to24m", "capital_gt24m" };
			String prefix = "R33_";

			for (String field : fields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
					Object newValue = getter.invoke(updatedEntity);
					Object existingValue = getter.invoke(existingSummary);

					// --- FIX: Normalize nulls vs empty strings ---
					String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
					String newValStr = (newValue == null) ? "" : newValue.toString().trim();

					if (currentValStr.equals(newValStr)) {
						continue;
					}

					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					summarySetter.invoke(existingSummary, newValue);

					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());
					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					continue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating R33 fields", e);
		}

		try {
			// 9️⃣ Loop for R35 Fields
			String[] fields = { "tot_spec_risk_ch" };
			String prefix = "R35_";

			for (String field : fields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_SIR_Summary_Entity.class.getMethod(getterName);
					Object newValue = getter.invoke(updatedEntity);
					Object existingValue = getter.invoke(existingSummary);

					// --- FIX: Normalize nulls vs empty strings ---
					String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
					String newValStr = (newValue == null) ? "" : newValue.toString().trim();

					if (currentValStr.equals(newValStr)) {
						continue;
					}

					Method summarySetter = M_SIR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					summarySetter.invoke(existingSummary, newValue);

					Method detailSetter = M_SIR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());
					detailSetter.invoke(detailEntity, newValue);

				} catch (NoSuchMethodException e) {
					continue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating R35 fields", e);
		}

		String changes = auditService.getChanges(oldcopy, existingSummary);

		System.out.println("M_SIR Changes Length = "
		+ (changes == null ? 0 : changes.length()));

		System.out.println("Saving Summary & Detail tables");

		// Save Summary & Detail first
		java.util.Map<String, Object> sumKeys = new java.util.HashMap<>();
		sumKeys.put("REPORT_DATE", existingSummary.getReportDate());
		saveEntityToTable(existingSummary, "BRRS_M_SIR_SUMMARYTABLE", sumKeys);

		if (isNewDetail) {
			insertEntityToTable(detailEntity, "BRRS_M_SIR_DETAILTABLE");
		} else {
			java.util.Map<String, Object> dtlKeys = new java.util.HashMap<>();
			dtlKeys.put("REPORT_DATE", detailEntity.getReportDate());
			saveEntityToTable(detailEntity, "BRRS_M_SIR_DETAILTABLE", dtlKeys);
		}

		// Audit only if within DB limit
		try {

		
		if (changes != null && !changes.isEmpty()) {

		    if (changes.length() <= 2000) {

		        auditService.compareEntitiesmanual(
		                oldcopy,
		                existingSummary,
		                updatedEntity.getReportDate().toString(),
		                "M_SIR Summary Screen",
		                "BRRS_M_SIR_SUMMARY");

		    } else {

		        System.out.println(
		                "Audit skipped because MODI_DETAILS exceeds 2000 characters. Length = "
		                        + changes.length());
		    }
		}
		} catch (Exception ex) {

		System.out.println("Audit Error : " + ex.getMessage());
		ex.printStackTrace();


		}

		System.out.println("Update completed successfully");

	}

	@Transactional
	public void updateResubReport(M_SIR_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------
		String sqlMax = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SIR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";
		BigDecimal maxResubVer = null;
		try {
			maxResubVer = jdbcTemplate.queryForObject(sqlMax, new Object[]{reportDate}, BigDecimal.class);
		} catch (Exception e) {
			// ignore or handle if null
		}

		if (maxResubVer == null) {
			throw new RuntimeException("No record for: " + reportDate);
		}

		// 🔹 Fetch the existing active record for auditing before incrementing the
		// version
		String sqlOld = "SELECT * FROM BRRS_M_SIR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_SIR_Resub_Summary_Entity> oldList = jdbcTemplate.query(sqlOld, new Object[]{reportDate, maxResubVer}, new BeanPropertyRowMapper<>(M_SIR_Resub_Summary_Entity.class));
		if (oldList.isEmpty()) {
			throw new RuntimeException("Could not fetch current maximum version record for auditing.");
		}
		M_SIR_Resub_Summary_Entity oldcopy = oldList.get(0);

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
		// 6️⃣ AUDIT EVALUATION (EXCLUDE ADMIN ATTRIBUTE DELTAS)
		// ====================================================
		M_SIR_Resub_Summary_Entity populatedSummaryForAudit = new M_SIR_Resub_Summary_Entity();
		BeanUtils.copyProperties(updatedEntity, populatedSummaryForAudit, "reportDate", "reportVersion",
				"reportResubDate");

		// Align administrative keys with oldcopy so they do not trigger false audit
		// changes
		populatedSummaryForAudit.setReportDate(oldcopy.getReportDate());
		populatedSummaryForAudit.setReportVersion(oldcopy.getReportVersion());
		populatedSummaryForAudit.setReportResubDate(oldcopy.getReportResubDate());

		String changes = auditService.getChanges(oldcopy, populatedSummaryForAudit);
		System.out.println("M_SIR Resub Changes Length = " + (changes != null ? changes.length() : 0));

		// ====================================================
		// 7️⃣ SAVE ALL WITH SAME DATA
		// ====================================================
		System.out.println("Saving Resub & Archival tables");
		insertEntityToTable(resubSummary, "BRRS_M_SIR_RESUB_SUMMARYTABLE");
		insertEntityToTable(resubDetail, "BRRS_M_SIR_RESUB_DETAILTABLE");
		insertEntityToTable(archSummary, "BRRS_M_SIR_ARCHIVALTABLE_SUMMARY");
		insertEntityToTable(archDetail, "BRRS_M_SIR_ARCHIVALTABLE_DETAIL");

		// Only invoke audit logger if real physical modifications exist
		if (changes != null && !changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, populatedSummaryForAudit,
					updatedEntity.getReportDate().toString(), "M_SIR Resub Screen", "BRRS_M_SIR_RESUB_SUMMARY");
		}

		System.out.println("Resubmission update completed successfully");
	}

	// ------------------------------
	// Retrieves a list of report dates, versions, and resubmission dates
	// ------------------------------
	public List<Object[]> getM_SIRResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM BRRS_M_SIR_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
			List<M_SIR_Archival_Summary_Entity> latestArchivalList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_SIR_Archival_Summary_Entity.class));

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

	// ------------------------------
	// Retrieves a list of report dates, versions, and archival dates
	// ------------------------------
	public List<Object[]> getM_SIRArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			String sql = "SELECT * FROM BRRS_M_SIR_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
			List<M_SIR_Archival_Summary_Entity> repoData = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_SIR_Archival_Summary_Entity.class));

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SIR_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
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

	// ------------------------------
	// Generates the normal Excel spreadsheet byte array for M_SIR
	// ------------------------------
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

				String sql = "SELECT * FROM BRRS_M_SIR_SUMMARYTABLE WHERE REPORT_DATE = ?";
				List<M_SIR_Summary_Entity> dataList = jdbcTemplate.query(sql, new Object[]{dateformat.parse(todate)}, new BeanPropertyRowMapper<>(M_SIR_Summary_Entity.class));

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
					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_SIR_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							Cell R12Cell = row.createCell(2);

							if (record.getReportDate() != null) {

								R12Cell.setCellValue(record.getReportDate());

								R12Cell.setCellStyle(dateStyle);

							} else {

								R12Cell.setCellValue("");

								R12Cell.setCellStyle(textStyle);
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
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SIR SUMMARY", null,
								"BRRS_M_SIR_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	// ------------------------------
	// Generates the Email Excel spreadsheet byte array for M_SIR
	// ------------------------------
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
			String sql = "SELECT * FROM BRRS_M_SIR_SUMMARYTABLE WHERE REPORT_DATE = ?";
			List<M_SIR_Summary_Entity> dataList = jdbcTemplate.query(sql, new Object[]{dateformat.parse(todate)}, new BeanPropertyRowMapper<>(M_SIR_Summary_Entity.class));

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
				int startRow = 5;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_SIR_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						Cell R12Cell = row.createCell(2);

						if (record.getReportDate() != null) {

							R12Cell.setCellValue(record.getReportDate());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
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
				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SIR EMAIL SUMMARY", null,
							"BRRS_M_SIR_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// ------------------------------
	// Generates the Archival Excel spreadsheet byte array for M_SIR
	// ------------------------------
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

		String sql = "SELECT * FROM BRRS_M_SIR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_SIR_Archival_Summary_Entity> dataList = jdbcTemplate.query(sql, new Object[]{dateformat.parse(todate), version}, new BeanPropertyRowMapper<>(M_SIR_Archival_Summary_Entity.class));

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
			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(2);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SIR ARCHIVAL SUMMARY", null,
						"BRRS_M_SIR_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// ------------------------------
	// Generates the Email Archival Excel spreadsheet byte array for M_SIR
	// ------------------------------
	public byte[] BRRS_M_SIREmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		String sql = "SELECT * FROM BRRS_M_SIR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_SIR_Archival_Summary_Entity> dataList = jdbcTemplate.query(sql, new Object[]{dateformat.parse(todate), version}, new BeanPropertyRowMapper<>(M_SIR_Archival_Summary_Entity.class));

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
			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(2);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SIR EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_SIR_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// ------------------------------
	// Generates the Resubmission Excel spreadsheet byte array for M_SIR
	// ------------------------------
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

		String sql = "SELECT * FROM BRRS_M_SIR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_SIR_Resub_Summary_Entity> dataList = jdbcTemplate.query(sql, new Object[]{dateformat.parse(todate), version}, new BeanPropertyRowMapper<>(M_SIR_Resub_Summary_Entity.class));

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
			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SIR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(2);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SIR RESUB SUMMARY", null,
						"BRRS_M_SIR_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// ------------------------------
	// Generates the Resubmission Email Excel spreadsheet byte array for M_SIR
	// ------------------------------
	public byte[] BRRS_M_SIRResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		String sql = "SELECT * FROM BRRS_M_SIR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_SIR_Resub_Summary_Entity> dataList = jdbcTemplate.query(sql, new Object[]{dateformat.parse(todate), version}, new BeanPropertyRowMapper<>(M_SIR_Resub_Summary_Entity.class));

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
			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SIR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(2);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SIR EMAIL RESUB SUMMARY", null,
						"BRRS_M_SIR_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// ------------------------------
	// Converts a camelCase property name to snake_case column name
	// ------------------------------
	private String camelToSnake(String str) {
		if (str == null) return null;
		if (str.contains("_")) {
			return str.toUpperCase();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				sb.append('_');
			}
			sb.append(Character.toUpperCase(c));
		}
		return sb.toString();
	}

	// ------------------------------
	// Saves the entity fields to the database table matching the key columns
	// ------------------------------
	private void saveEntityToTable(Object entity, String tableName, java.util.Map<String, Object> keys) {
		StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
		List<Object> params = new ArrayList<>();
		
		Method[] methods = entity.getClass().getMethods();
		boolean first = true;
		for (Method method : methods) {
			String name = method.getName();
			if (name.startsWith("get") && !name.equals("getClass")) {
				String fieldName = name.substring(3);
				String propertyName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
				String columnName = camelToSnake(propertyName);
				
				if (keys.containsKey(propertyName) || keys.containsKey(columnName)) {
					continue;
				}
				
				try {
					Object val = method.invoke(entity);
					if (!first) {
						sql.append(", ");
					}
					sql.append(columnName).append(" = ?");
					params.add(val);
					first = false;
				} catch (Exception e) {
					// skip
				}
			}
		}
		
		sql.append(" WHERE ");
		boolean firstKey = true;
		for (java.util.Map.Entry<String, Object> entry : keys.entrySet()) {
			if (!firstKey) {
				sql.append(" AND ");
			}
			sql.append(camelToSnake(entry.getKey())).append(" = ?");
			params.add(entry.getValue());
			firstKey = false;
		}
		
		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	// ------------------------------
	// Inserts the entity fields dynamically into the database table
	// ------------------------------
	private void insertEntityToTable(Object entity, String tableName) {
		StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
		StringBuilder values = new StringBuilder(" VALUES (");
		List<Object> params = new ArrayList<>();
		
		Method[] methods = entity.getClass().getMethods();
		boolean first = true;
		for (Method method : methods) {
			String name = method.getName();
			if (name.startsWith("get") && !name.equals("getClass")) {
				String fieldName = name.substring(3);
				String propertyName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
				String columnName = camelToSnake(propertyName);
				
				try {
					Object val = method.invoke(entity);
					if (!first) {
						sql.append(", ");
						values.append(", ");
					}
					sql.append(columnName);
					values.append("?");
					params.add(val);
					first = false;
				} catch (Exception e) {
					// skip
				}
			}
		}
		sql.append(")").append(values).append(")");
		
		jdbcTemplate.update(sql.toString(), params.toArray());
	}

// ------------------------------
// Base entity class for M_SIR containing shared fields and getters/setters
// ------------------------------
public static class M_SIR_Base_Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String r12_product;
    private BigDecimal r12_amt_6m;
    private BigDecimal r12_risk_6m;
    private BigDecimal r12_capital_6m;
    private BigDecimal r12_amt_6to24m;
    private BigDecimal r12_risk_6to24m;
    private BigDecimal r12_capital_6to24m;
    private BigDecimal r12_amt_gt24m;
    private BigDecimal r12_risk_gt24m;
    private BigDecimal r12_capital_gt24m;
    private String r13_product;
    private BigDecimal r13_amt_6m;
    private BigDecimal r13_risk_6m;
    private BigDecimal r13_capital_6m;
    private BigDecimal r13_amt_6to24m;
    private BigDecimal r13_risk_6to24m;
    private BigDecimal r13_capital_6to24m;
    private BigDecimal r13_amt_gt24m;
    private BigDecimal r13_risk_gt24m;
    private BigDecimal r13_capital_gt24m;
    private String r14_product;
    private BigDecimal r14_amt_6m;
    private BigDecimal r14_risk_6m;
    private BigDecimal r14_capital_6m;
    private BigDecimal r14_amt_6to24m;
    private BigDecimal r14_risk_6to24m;
    private BigDecimal r14_capital_6to24m;
    private BigDecimal r14_amt_gt24m;
    private BigDecimal r14_risk_gt24m;
    private BigDecimal r14_capital_gt24m;
    private String r15_product;
    private BigDecimal r15_amt_6m;
    private BigDecimal r15_risk_6m;
    private BigDecimal r15_capital_6m;
    private BigDecimal r15_amt_6to24m;
    private BigDecimal r15_risk_6to24m;
    private BigDecimal r15_capital_6to24m;
    private BigDecimal r15_amt_gt24m;
    private BigDecimal r15_risk_gt24m;
    private BigDecimal r15_capital_gt24m;
    private String r16_product;
    private BigDecimal r16_amt_6m;
    private BigDecimal r16_risk_6m;
    private BigDecimal r16_capital_6m;
    private BigDecimal r16_amt_6to24m;
    private BigDecimal r16_risk_6to24m;
    private BigDecimal r16_capital_6to24m;
    private BigDecimal r16_amt_gt24m;
    private BigDecimal r16_risk_gt24m;
    private BigDecimal r16_capital_gt24m;
    private String r17_product;
    private BigDecimal r17_amt_6m;
    private BigDecimal r17_risk_6m;
    private BigDecimal r17_capital_6m;
    private BigDecimal r17_amt_6to24m;
    private BigDecimal r17_risk_6to24m;
    private BigDecimal r17_capital_6to24m;
    private BigDecimal r17_amt_gt24m;
    private BigDecimal r17_risk_gt24m;
    private BigDecimal r17_capital_gt24m;
    private String r18_product;
    private BigDecimal r18_amt_6m;
    private BigDecimal r18_risk_6m;
    private BigDecimal r18_capital_6m;
    private BigDecimal r18_amt_6to24m;
    private BigDecimal r18_risk_6to24m;
    private BigDecimal r18_capital_6to24m;
    private BigDecimal r18_amt_gt24m;
    private BigDecimal r18_risk_gt24m;
    private BigDecimal r18_capital_gt24m;
    private String r19_product;
    private BigDecimal r19_amt_6m;
    private BigDecimal r19_risk_6m;
    private BigDecimal r19_capital_6m;
    private BigDecimal r19_amt_6to24m;
    private BigDecimal r19_risk_6to24m;
    private BigDecimal r19_capital_6to24m;
    private BigDecimal r19_amt_gt24m;
    private BigDecimal r19_risk_gt24m;
    private BigDecimal r19_capital_gt24m;
    private String r20_product;
    private BigDecimal r20_amt_6m;
    private BigDecimal r20_risk_6m;
    private BigDecimal r20_capital_6m;
    private BigDecimal r20_amt_6to24m;
    private BigDecimal r20_risk_6to24m;
    private BigDecimal r20_capital_6to24m;
    private BigDecimal r20_amt_gt24m;
    private BigDecimal r20_risk_gt24m;
    private BigDecimal r20_capital_gt24m;
    private String r21_product;
    private BigDecimal r21_amt_6m;
    private BigDecimal r21_risk_6m;
    private BigDecimal r21_capital_6m;
    private BigDecimal r21_amt_6to24m;
    private BigDecimal r21_risk_6to24m;
    private BigDecimal r21_capital_6to24m;
    private BigDecimal r21_amt_gt24m;
    private BigDecimal r21_risk_gt24m;
    private BigDecimal r21_capital_gt24m;
    private String r22_product;
    private BigDecimal r22_amt_6m;
    private BigDecimal r22_risk_6m;
    private BigDecimal r22_capital_6m;
    private BigDecimal r22_amt_6to24m;
    private BigDecimal r22_risk_6to24m;
    private BigDecimal r22_capital_6to24m;
    private BigDecimal r22_amt_gt24m;
    private BigDecimal r22_risk_gt24m;
    private BigDecimal r22_capital_gt24m;
    private String r23_product;
    private BigDecimal r23_amt_6m;
    private BigDecimal r23_risk_6m;
    private BigDecimal r23_capital_6m;
    private BigDecimal r23_amt_6to24m;
    private BigDecimal r23_risk_6to24m;
    private BigDecimal r23_capital_6to24m;
    private BigDecimal r23_amt_gt24m;
    private BigDecimal r23_risk_gt24m;
    private BigDecimal r23_capital_gt24m;
    private String r24_product;
    private BigDecimal r24_amt_6m;
    private BigDecimal r24_risk_6m;
    private BigDecimal r24_capital_6m;
    private BigDecimal r24_amt_6to24m;
    private BigDecimal r24_risk_6to24m;
    private BigDecimal r24_capital_6to24m;
    private BigDecimal r24_amt_gt24m;
    private BigDecimal r24_risk_gt24m;
    private BigDecimal r24_capital_gt24m;
    private String r25_product;
    private BigDecimal r25_amt_6m;
    private BigDecimal r25_risk_6m;
    private BigDecimal r25_capital_6m;
    private BigDecimal r25_amt_6to24m;
    private BigDecimal r25_risk_6to24m;
    private BigDecimal r25_capital_6to24m;
    private BigDecimal r25_amt_gt24m;
    private BigDecimal r25_risk_gt24m;
    private BigDecimal r25_capital_gt24m;
    private String r26_product;
    private BigDecimal r26_amt_6m;
    private BigDecimal r26_risk_6m;
    private BigDecimal r26_capital_6m;
    private BigDecimal r26_amt_6to24m;
    private BigDecimal r26_risk_6to24m;
    private BigDecimal r26_capital_6to24m;
    private BigDecimal r26_amt_gt24m;
    private BigDecimal r26_risk_gt24m;
    private BigDecimal r26_capital_gt24m;
    private String r27_product;
    private BigDecimal r27_amt_6m;
    private BigDecimal r27_risk_6m;
    private BigDecimal r27_capital_6m;
    private BigDecimal r27_amt_6to24m;
    private BigDecimal r27_risk_6to24m;
    private BigDecimal r27_capital_6to24m;
    private BigDecimal r27_amt_gt24m;
    private BigDecimal r27_risk_gt24m;
    private BigDecimal r27_capital_gt24m;
    private String r28_product;
    private BigDecimal r28_amt_6m;
    private BigDecimal r28_risk_6m;
    private BigDecimal r28_capital_6m;
    private BigDecimal r28_amt_6to24m;
    private BigDecimal r28_risk_6to24m;
    private BigDecimal r28_capital_6to24m;
    private BigDecimal r28_amt_gt24m;
    private BigDecimal r28_risk_gt24m;
    private BigDecimal r28_capital_gt24m;
    private String r29_product;
    private BigDecimal r29_amt_6m;
    private BigDecimal r29_risk_6m;
    private BigDecimal r29_capital_6m;
    private BigDecimal r29_amt_6to24m;
    private BigDecimal r29_risk_6to24m;
    private BigDecimal r29_capital_6to24m;
    private BigDecimal r29_amt_gt24m;
    private BigDecimal r29_risk_gt24m;
    private BigDecimal r29_capital_gt24m;
    private String r30_product;
    private BigDecimal r30_amt_6m;
    private BigDecimal r30_risk_6m;
    private BigDecimal r30_capital_6m;
    private BigDecimal r30_amt_6to24m;
    private BigDecimal r30_risk_6to24m;
    private BigDecimal r30_capital_6to24m;
    private BigDecimal r30_amt_gt24m;
    private BigDecimal r30_risk_gt24m;
    private BigDecimal r30_capital_gt24m;
    private String r31_product;
    private BigDecimal r31_amt_6m;
    private BigDecimal r31_risk_6m;
    private BigDecimal r31_capital_6m;
    private BigDecimal r31_amt_6to24m;
    private BigDecimal r31_risk_6to24m;
    private BigDecimal r31_capital_6to24m;
    private BigDecimal r31_amt_gt24m;
    private BigDecimal r31_risk_gt24m;
    private BigDecimal r31_capital_gt24m;
    private String r32_product;
    private BigDecimal r32_amt_6m;
    private BigDecimal r32_risk_6m;
    private BigDecimal r32_capital_6m;
    private BigDecimal r32_amt_6to24m;
    private BigDecimal r32_risk_6to24m;
    private BigDecimal r32_capital_6to24m;
    private BigDecimal r32_amt_gt24m;
    private BigDecimal r32_risk_gt24m;
    private BigDecimal r32_capital_gt24m;
    private String r33_product;
    private BigDecimal r33_amt_6m;
    private BigDecimal r33_risk_6m;
    private BigDecimal r33_capital_6m;
    private BigDecimal r33_amt_6to24m;
    private BigDecimal r33_risk_6to24m;
    private BigDecimal r33_capital_6to24m;
    private BigDecimal r33_amt_gt24m;
    private BigDecimal r33_risk_gt24m;
    private BigDecimal r33_capital_gt24m;
    private BigDecimal r35_tot_spec_risk_ch;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date reportDate;
    private BigDecimal reportVersion;
    private String report_frequency;
    private String report_code;
    private String report_desc;
    private String entity_flg;
    private String modify_flg;
    private String del_flg;

    public String getR12_product() {
        return r12_product;
    }
    public void setR12_product(String r12_product) {
        this.r12_product = r12_product;
    }
    public BigDecimal getR12_amt_6m() {
        return r12_amt_6m;
    }
    public void setR12_amt_6m(BigDecimal r12_amt_6m) {
        this.r12_amt_6m = r12_amt_6m;
    }
    public BigDecimal getR12_risk_6m() {
        return r12_risk_6m;
    }
    public void setR12_risk_6m(BigDecimal r12_risk_6m) {
        this.r12_risk_6m = r12_risk_6m;
    }
    public BigDecimal getR12_capital_6m() {
        return r12_capital_6m;
    }
    public void setR12_capital_6m(BigDecimal r12_capital_6m) {
        this.r12_capital_6m = r12_capital_6m;
    }
    public BigDecimal getR12_amt_6to24m() {
        return r12_amt_6to24m;
    }
    public void setR12_amt_6to24m(BigDecimal r12_amt_6to24m) {
        this.r12_amt_6to24m = r12_amt_6to24m;
    }
    public BigDecimal getR12_risk_6to24m() {
        return r12_risk_6to24m;
    }
    public void setR12_risk_6to24m(BigDecimal r12_risk_6to24m) {
        this.r12_risk_6to24m = r12_risk_6to24m;
    }
    public BigDecimal getR12_capital_6to24m() {
        return r12_capital_6to24m;
    }
    public void setR12_capital_6to24m(BigDecimal r12_capital_6to24m) {
        this.r12_capital_6to24m = r12_capital_6to24m;
    }
    public BigDecimal getR12_amt_gt24m() {
        return r12_amt_gt24m;
    }
    public void setR12_amt_gt24m(BigDecimal r12_amt_gt24m) {
        this.r12_amt_gt24m = r12_amt_gt24m;
    }
    public BigDecimal getR12_risk_gt24m() {
        return r12_risk_gt24m;
    }
    public void setR12_risk_gt24m(BigDecimal r12_risk_gt24m) {
        this.r12_risk_gt24m = r12_risk_gt24m;
    }
    public BigDecimal getR12_capital_gt24m() {
        return r12_capital_gt24m;
    }
    public void setR12_capital_gt24m(BigDecimal r12_capital_gt24m) {
        this.r12_capital_gt24m = r12_capital_gt24m;
    }
    public String getR13_product() {
        return r13_product;
    }
    public void setR13_product(String r13_product) {
        this.r13_product = r13_product;
    }
    public BigDecimal getR13_amt_6m() {
        return r13_amt_6m;
    }
    public void setR13_amt_6m(BigDecimal r13_amt_6m) {
        this.r13_amt_6m = r13_amt_6m;
    }
    public BigDecimal getR13_risk_6m() {
        return r13_risk_6m;
    }
    public void setR13_risk_6m(BigDecimal r13_risk_6m) {
        this.r13_risk_6m = r13_risk_6m;
    }
    public BigDecimal getR13_capital_6m() {
        return r13_capital_6m;
    }
    public void setR13_capital_6m(BigDecimal r13_capital_6m) {
        this.r13_capital_6m = r13_capital_6m;
    }
    public BigDecimal getR13_amt_6to24m() {
        return r13_amt_6to24m;
    }
    public void setR13_amt_6to24m(BigDecimal r13_amt_6to24m) {
        this.r13_amt_6to24m = r13_amt_6to24m;
    }
    public BigDecimal getR13_risk_6to24m() {
        return r13_risk_6to24m;
    }
    public void setR13_risk_6to24m(BigDecimal r13_risk_6to24m) {
        this.r13_risk_6to24m = r13_risk_6to24m;
    }
    public BigDecimal getR13_capital_6to24m() {
        return r13_capital_6to24m;
    }
    public void setR13_capital_6to24m(BigDecimal r13_capital_6to24m) {
        this.r13_capital_6to24m = r13_capital_6to24m;
    }
    public BigDecimal getR13_amt_gt24m() {
        return r13_amt_gt24m;
    }
    public void setR13_amt_gt24m(BigDecimal r13_amt_gt24m) {
        this.r13_amt_gt24m = r13_amt_gt24m;
    }
    public BigDecimal getR13_risk_gt24m() {
        return r13_risk_gt24m;
    }
    public void setR13_risk_gt24m(BigDecimal r13_risk_gt24m) {
        this.r13_risk_gt24m = r13_risk_gt24m;
    }
    public BigDecimal getR13_capital_gt24m() {
        return r13_capital_gt24m;
    }
    public void setR13_capital_gt24m(BigDecimal r13_capital_gt24m) {
        this.r13_capital_gt24m = r13_capital_gt24m;
    }
    public String getR14_product() {
        return r14_product;
    }
    public void setR14_product(String r14_product) {
        this.r14_product = r14_product;
    }
    public BigDecimal getR14_amt_6m() {
        return r14_amt_6m;
    }
    public void setR14_amt_6m(BigDecimal r14_amt_6m) {
        this.r14_amt_6m = r14_amt_6m;
    }
    public BigDecimal getR14_risk_6m() {
        return r14_risk_6m;
    }
    public void setR14_risk_6m(BigDecimal r14_risk_6m) {
        this.r14_risk_6m = r14_risk_6m;
    }
    public BigDecimal getR14_capital_6m() {
        return r14_capital_6m;
    }
    public void setR14_capital_6m(BigDecimal r14_capital_6m) {
        this.r14_capital_6m = r14_capital_6m;
    }
    public BigDecimal getR14_amt_6to24m() {
        return r14_amt_6to24m;
    }
    public void setR14_amt_6to24m(BigDecimal r14_amt_6to24m) {
        this.r14_amt_6to24m = r14_amt_6to24m;
    }
    public BigDecimal getR14_risk_6to24m() {
        return r14_risk_6to24m;
    }
    public void setR14_risk_6to24m(BigDecimal r14_risk_6to24m) {
        this.r14_risk_6to24m = r14_risk_6to24m;
    }
    public BigDecimal getR14_capital_6to24m() {
        return r14_capital_6to24m;
    }
    public void setR14_capital_6to24m(BigDecimal r14_capital_6to24m) {
        this.r14_capital_6to24m = r14_capital_6to24m;
    }
    public BigDecimal getR14_amt_gt24m() {
        return r14_amt_gt24m;
    }
    public void setR14_amt_gt24m(BigDecimal r14_amt_gt24m) {
        this.r14_amt_gt24m = r14_amt_gt24m;
    }
    public BigDecimal getR14_risk_gt24m() {
        return r14_risk_gt24m;
    }
    public void setR14_risk_gt24m(BigDecimal r14_risk_gt24m) {
        this.r14_risk_gt24m = r14_risk_gt24m;
    }
    public BigDecimal getR14_capital_gt24m() {
        return r14_capital_gt24m;
    }
    public void setR14_capital_gt24m(BigDecimal r14_capital_gt24m) {
        this.r14_capital_gt24m = r14_capital_gt24m;
    }
    public String getR15_product() {
        return r15_product;
    }
    public void setR15_product(String r15_product) {
        this.r15_product = r15_product;
    }
    public BigDecimal getR15_amt_6m() {
        return r15_amt_6m;
    }
    public void setR15_amt_6m(BigDecimal r15_amt_6m) {
        this.r15_amt_6m = r15_amt_6m;
    }
    public BigDecimal getR15_risk_6m() {
        return r15_risk_6m;
    }
    public void setR15_risk_6m(BigDecimal r15_risk_6m) {
        this.r15_risk_6m = r15_risk_6m;
    }
    public BigDecimal getR15_capital_6m() {
        return r15_capital_6m;
    }
    public void setR15_capital_6m(BigDecimal r15_capital_6m) {
        this.r15_capital_6m = r15_capital_6m;
    }
    public BigDecimal getR15_amt_6to24m() {
        return r15_amt_6to24m;
    }
    public void setR15_amt_6to24m(BigDecimal r15_amt_6to24m) {
        this.r15_amt_6to24m = r15_amt_6to24m;
    }
    public BigDecimal getR15_risk_6to24m() {
        return r15_risk_6to24m;
    }
    public void setR15_risk_6to24m(BigDecimal r15_risk_6to24m) {
        this.r15_risk_6to24m = r15_risk_6to24m;
    }
    public BigDecimal getR15_capital_6to24m() {
        return r15_capital_6to24m;
    }
    public void setR15_capital_6to24m(BigDecimal r15_capital_6to24m) {
        this.r15_capital_6to24m = r15_capital_6to24m;
    }
    public BigDecimal getR15_amt_gt24m() {
        return r15_amt_gt24m;
    }
    public void setR15_amt_gt24m(BigDecimal r15_amt_gt24m) {
        this.r15_amt_gt24m = r15_amt_gt24m;
    }
    public BigDecimal getR15_risk_gt24m() {
        return r15_risk_gt24m;
    }
    public void setR15_risk_gt24m(BigDecimal r15_risk_gt24m) {
        this.r15_risk_gt24m = r15_risk_gt24m;
    }
    public BigDecimal getR15_capital_gt24m() {
        return r15_capital_gt24m;
    }
    public void setR15_capital_gt24m(BigDecimal r15_capital_gt24m) {
        this.r15_capital_gt24m = r15_capital_gt24m;
    }
    public String getR16_product() {
        return r16_product;
    }
    public void setR16_product(String r16_product) {
        this.r16_product = r16_product;
    }
    public BigDecimal getR16_amt_6m() {
        return r16_amt_6m;
    }
    public void setR16_amt_6m(BigDecimal r16_amt_6m) {
        this.r16_amt_6m = r16_amt_6m;
    }
    public BigDecimal getR16_risk_6m() {
        return r16_risk_6m;
    }
    public void setR16_risk_6m(BigDecimal r16_risk_6m) {
        this.r16_risk_6m = r16_risk_6m;
    }
    public BigDecimal getR16_capital_6m() {
        return r16_capital_6m;
    }
    public void setR16_capital_6m(BigDecimal r16_capital_6m) {
        this.r16_capital_6m = r16_capital_6m;
    }
    public BigDecimal getR16_amt_6to24m() {
        return r16_amt_6to24m;
    }
    public void setR16_amt_6to24m(BigDecimal r16_amt_6to24m) {
        this.r16_amt_6to24m = r16_amt_6to24m;
    }
    public BigDecimal getR16_risk_6to24m() {
        return r16_risk_6to24m;
    }
    public void setR16_risk_6to24m(BigDecimal r16_risk_6to24m) {
        this.r16_risk_6to24m = r16_risk_6to24m;
    }
    public BigDecimal getR16_capital_6to24m() {
        return r16_capital_6to24m;
    }
    public void setR16_capital_6to24m(BigDecimal r16_capital_6to24m) {
        this.r16_capital_6to24m = r16_capital_6to24m;
    }
    public BigDecimal getR16_amt_gt24m() {
        return r16_amt_gt24m;
    }
    public void setR16_amt_gt24m(BigDecimal r16_amt_gt24m) {
        this.r16_amt_gt24m = r16_amt_gt24m;
    }
    public BigDecimal getR16_risk_gt24m() {
        return r16_risk_gt24m;
    }
    public void setR16_risk_gt24m(BigDecimal r16_risk_gt24m) {
        this.r16_risk_gt24m = r16_risk_gt24m;
    }
    public BigDecimal getR16_capital_gt24m() {
        return r16_capital_gt24m;
    }
    public void setR16_capital_gt24m(BigDecimal r16_capital_gt24m) {
        this.r16_capital_gt24m = r16_capital_gt24m;
    }
    public String getR17_product() {
        return r17_product;
    }
    public void setR17_product(String r17_product) {
        this.r17_product = r17_product;
    }
    public BigDecimal getR17_amt_6m() {
        return r17_amt_6m;
    }
    public void setR17_amt_6m(BigDecimal r17_amt_6m) {
        this.r17_amt_6m = r17_amt_6m;
    }
    public BigDecimal getR17_risk_6m() {
        return r17_risk_6m;
    }
    public void setR17_risk_6m(BigDecimal r17_risk_6m) {
        this.r17_risk_6m = r17_risk_6m;
    }
    public BigDecimal getR17_capital_6m() {
        return r17_capital_6m;
    }
    public void setR17_capital_6m(BigDecimal r17_capital_6m) {
        this.r17_capital_6m = r17_capital_6m;
    }
    public BigDecimal getR17_amt_6to24m() {
        return r17_amt_6to24m;
    }
    public void setR17_amt_6to24m(BigDecimal r17_amt_6to24m) {
        this.r17_amt_6to24m = r17_amt_6to24m;
    }
    public BigDecimal getR17_risk_6to24m() {
        return r17_risk_6to24m;
    }
    public void setR17_risk_6to24m(BigDecimal r17_risk_6to24m) {
        this.r17_risk_6to24m = r17_risk_6to24m;
    }
    public BigDecimal getR17_capital_6to24m() {
        return r17_capital_6to24m;
    }
    public void setR17_capital_6to24m(BigDecimal r17_capital_6to24m) {
        this.r17_capital_6to24m = r17_capital_6to24m;
    }
    public BigDecimal getR17_amt_gt24m() {
        return r17_amt_gt24m;
    }
    public void setR17_amt_gt24m(BigDecimal r17_amt_gt24m) {
        this.r17_amt_gt24m = r17_amt_gt24m;
    }
    public BigDecimal getR17_risk_gt24m() {
        return r17_risk_gt24m;
    }
    public void setR17_risk_gt24m(BigDecimal r17_risk_gt24m) {
        this.r17_risk_gt24m = r17_risk_gt24m;
    }
    public BigDecimal getR17_capital_gt24m() {
        return r17_capital_gt24m;
    }
    public void setR17_capital_gt24m(BigDecimal r17_capital_gt24m) {
        this.r17_capital_gt24m = r17_capital_gt24m;
    }
    public String getR18_product() {
        return r18_product;
    }
    public void setR18_product(String r18_product) {
        this.r18_product = r18_product;
    }
    public BigDecimal getR18_amt_6m() {
        return r18_amt_6m;
    }
    public void setR18_amt_6m(BigDecimal r18_amt_6m) {
        this.r18_amt_6m = r18_amt_6m;
    }
    public BigDecimal getR18_risk_6m() {
        return r18_risk_6m;
    }
    public void setR18_risk_6m(BigDecimal r18_risk_6m) {
        this.r18_risk_6m = r18_risk_6m;
    }
    public BigDecimal getR18_capital_6m() {
        return r18_capital_6m;
    }
    public void setR18_capital_6m(BigDecimal r18_capital_6m) {
        this.r18_capital_6m = r18_capital_6m;
    }
    public BigDecimal getR18_amt_6to24m() {
        return r18_amt_6to24m;
    }
    public void setR18_amt_6to24m(BigDecimal r18_amt_6to24m) {
        this.r18_amt_6to24m = r18_amt_6to24m;
    }
    public BigDecimal getR18_risk_6to24m() {
        return r18_risk_6to24m;
    }
    public void setR18_risk_6to24m(BigDecimal r18_risk_6to24m) {
        this.r18_risk_6to24m = r18_risk_6to24m;
    }
    public BigDecimal getR18_capital_6to24m() {
        return r18_capital_6to24m;
    }
    public void setR18_capital_6to24m(BigDecimal r18_capital_6to24m) {
        this.r18_capital_6to24m = r18_capital_6to24m;
    }
    public BigDecimal getR18_amt_gt24m() {
        return r18_amt_gt24m;
    }
    public void setR18_amt_gt24m(BigDecimal r18_amt_gt24m) {
        this.r18_amt_gt24m = r18_amt_gt24m;
    }
    public BigDecimal getR18_risk_gt24m() {
        return r18_risk_gt24m;
    }
    public void setR18_risk_gt24m(BigDecimal r18_risk_gt24m) {
        this.r18_risk_gt24m = r18_risk_gt24m;
    }
    public BigDecimal getR18_capital_gt24m() {
        return r18_capital_gt24m;
    }
    public void setR18_capital_gt24m(BigDecimal r18_capital_gt24m) {
        this.r18_capital_gt24m = r18_capital_gt24m;
    }
    public String getR19_product() {
        return r19_product;
    }
    public void setR19_product(String r19_product) {
        this.r19_product = r19_product;
    }
    public BigDecimal getR19_amt_6m() {
        return r19_amt_6m;
    }
    public void setR19_amt_6m(BigDecimal r19_amt_6m) {
        this.r19_amt_6m = r19_amt_6m;
    }
    public BigDecimal getR19_risk_6m() {
        return r19_risk_6m;
    }
    public void setR19_risk_6m(BigDecimal r19_risk_6m) {
        this.r19_risk_6m = r19_risk_6m;
    }
    public BigDecimal getR19_capital_6m() {
        return r19_capital_6m;
    }
    public void setR19_capital_6m(BigDecimal r19_capital_6m) {
        this.r19_capital_6m = r19_capital_6m;
    }
    public BigDecimal getR19_amt_6to24m() {
        return r19_amt_6to24m;
    }
    public void setR19_amt_6to24m(BigDecimal r19_amt_6to24m) {
        this.r19_amt_6to24m = r19_amt_6to24m;
    }
    public BigDecimal getR19_risk_6to24m() {
        return r19_risk_6to24m;
    }
    public void setR19_risk_6to24m(BigDecimal r19_risk_6to24m) {
        this.r19_risk_6to24m = r19_risk_6to24m;
    }
    public BigDecimal getR19_capital_6to24m() {
        return r19_capital_6to24m;
    }
    public void setR19_capital_6to24m(BigDecimal r19_capital_6to24m) {
        this.r19_capital_6to24m = r19_capital_6to24m;
    }
    public BigDecimal getR19_amt_gt24m() {
        return r19_amt_gt24m;
    }
    public void setR19_amt_gt24m(BigDecimal r19_amt_gt24m) {
        this.r19_amt_gt24m = r19_amt_gt24m;
    }
    public BigDecimal getR19_risk_gt24m() {
        return r19_risk_gt24m;
    }
    public void setR19_risk_gt24m(BigDecimal r19_risk_gt24m) {
        this.r19_risk_gt24m = r19_risk_gt24m;
    }
    public BigDecimal getR19_capital_gt24m() {
        return r19_capital_gt24m;
    }
    public void setR19_capital_gt24m(BigDecimal r19_capital_gt24m) {
        this.r19_capital_gt24m = r19_capital_gt24m;
    }
    public String getR20_product() {
        return r20_product;
    }
    public void setR20_product(String r20_product) {
        this.r20_product = r20_product;
    }
    public BigDecimal getR20_amt_6m() {
        return r20_amt_6m;
    }
    public void setR20_amt_6m(BigDecimal r20_amt_6m) {
        this.r20_amt_6m = r20_amt_6m;
    }
    public BigDecimal getR20_risk_6m() {
        return r20_risk_6m;
    }
    public void setR20_risk_6m(BigDecimal r20_risk_6m) {
        this.r20_risk_6m = r20_risk_6m;
    }
    public BigDecimal getR20_capital_6m() {
        return r20_capital_6m;
    }
    public void setR20_capital_6m(BigDecimal r20_capital_6m) {
        this.r20_capital_6m = r20_capital_6m;
    }
    public BigDecimal getR20_amt_6to24m() {
        return r20_amt_6to24m;
    }
    public void setR20_amt_6to24m(BigDecimal r20_amt_6to24m) {
        this.r20_amt_6to24m = r20_amt_6to24m;
    }
    public BigDecimal getR20_risk_6to24m() {
        return r20_risk_6to24m;
    }
    public void setR20_risk_6to24m(BigDecimal r20_risk_6to24m) {
        this.r20_risk_6to24m = r20_risk_6to24m;
    }
    public BigDecimal getR20_capital_6to24m() {
        return r20_capital_6to24m;
    }
    public void setR20_capital_6to24m(BigDecimal r20_capital_6to24m) {
        this.r20_capital_6to24m = r20_capital_6to24m;
    }
    public BigDecimal getR20_amt_gt24m() {
        return r20_amt_gt24m;
    }
    public void setR20_amt_gt24m(BigDecimal r20_amt_gt24m) {
        this.r20_amt_gt24m = r20_amt_gt24m;
    }
    public BigDecimal getR20_risk_gt24m() {
        return r20_risk_gt24m;
    }
    public void setR20_risk_gt24m(BigDecimal r20_risk_gt24m) {
        this.r20_risk_gt24m = r20_risk_gt24m;
    }
    public BigDecimal getR20_capital_gt24m() {
        return r20_capital_gt24m;
    }
    public void setR20_capital_gt24m(BigDecimal r20_capital_gt24m) {
        this.r20_capital_gt24m = r20_capital_gt24m;
    }
    public String getR21_product() {
        return r21_product;
    }
    public void setR21_product(String r21_product) {
        this.r21_product = r21_product;
    }
    public BigDecimal getR21_amt_6m() {
        return r21_amt_6m;
    }
    public void setR21_amt_6m(BigDecimal r21_amt_6m) {
        this.r21_amt_6m = r21_amt_6m;
    }
    public BigDecimal getR21_risk_6m() {
        return r21_risk_6m;
    }
    public void setR21_risk_6m(BigDecimal r21_risk_6m) {
        this.r21_risk_6m = r21_risk_6m;
    }
    public BigDecimal getR21_capital_6m() {
        return r21_capital_6m;
    }
    public void setR21_capital_6m(BigDecimal r21_capital_6m) {
        this.r21_capital_6m = r21_capital_6m;
    }
    public BigDecimal getR21_amt_6to24m() {
        return r21_amt_6to24m;
    }
    public void setR21_amt_6to24m(BigDecimal r21_amt_6to24m) {
        this.r21_amt_6to24m = r21_amt_6to24m;
    }
    public BigDecimal getR21_risk_6to24m() {
        return r21_risk_6to24m;
    }
    public void setR21_risk_6to24m(BigDecimal r21_risk_6to24m) {
        this.r21_risk_6to24m = r21_risk_6to24m;
    }
    public BigDecimal getR21_capital_6to24m() {
        return r21_capital_6to24m;
    }
    public void setR21_capital_6to24m(BigDecimal r21_capital_6to24m) {
        this.r21_capital_6to24m = r21_capital_6to24m;
    }
    public BigDecimal getR21_amt_gt24m() {
        return r21_amt_gt24m;
    }
    public void setR21_amt_gt24m(BigDecimal r21_amt_gt24m) {
        this.r21_amt_gt24m = r21_amt_gt24m;
    }
    public BigDecimal getR21_risk_gt24m() {
        return r21_risk_gt24m;
    }
    public void setR21_risk_gt24m(BigDecimal r21_risk_gt24m) {
        this.r21_risk_gt24m = r21_risk_gt24m;
    }
    public BigDecimal getR21_capital_gt24m() {
        return r21_capital_gt24m;
    }
    public void setR21_capital_gt24m(BigDecimal r21_capital_gt24m) {
        this.r21_capital_gt24m = r21_capital_gt24m;
    }
    public String getR22_product() {
        return r22_product;
    }
    public void setR22_product(String r22_product) {
        this.r22_product = r22_product;
    }
    public BigDecimal getR22_amt_6m() {
        return r22_amt_6m;
    }
    public void setR22_amt_6m(BigDecimal r22_amt_6m) {
        this.r22_amt_6m = r22_amt_6m;
    }
    public BigDecimal getR22_risk_6m() {
        return r22_risk_6m;
    }
    public void setR22_risk_6m(BigDecimal r22_risk_6m) {
        this.r22_risk_6m = r22_risk_6m;
    }
    public BigDecimal getR22_capital_6m() {
        return r22_capital_6m;
    }
    public void setR22_capital_6m(BigDecimal r22_capital_6m) {
        this.r22_capital_6m = r22_capital_6m;
    }
    public BigDecimal getR22_amt_6to24m() {
        return r22_amt_6to24m;
    }
    public void setR22_amt_6to24m(BigDecimal r22_amt_6to24m) {
        this.r22_amt_6to24m = r22_amt_6to24m;
    }
    public BigDecimal getR22_risk_6to24m() {
        return r22_risk_6to24m;
    }
    public void setR22_risk_6to24m(BigDecimal r22_risk_6to24m) {
        this.r22_risk_6to24m = r22_risk_6to24m;
    }
    public BigDecimal getR22_capital_6to24m() {
        return r22_capital_6to24m;
    }
    public void setR22_capital_6to24m(BigDecimal r22_capital_6to24m) {
        this.r22_capital_6to24m = r22_capital_6to24m;
    }
    public BigDecimal getR22_amt_gt24m() {
        return r22_amt_gt24m;
    }
    public void setR22_amt_gt24m(BigDecimal r22_amt_gt24m) {
        this.r22_amt_gt24m = r22_amt_gt24m;
    }
    public BigDecimal getR22_risk_gt24m() {
        return r22_risk_gt24m;
    }
    public void setR22_risk_gt24m(BigDecimal r22_risk_gt24m) {
        this.r22_risk_gt24m = r22_risk_gt24m;
    }
    public BigDecimal getR22_capital_gt24m() {
        return r22_capital_gt24m;
    }
    public void setR22_capital_gt24m(BigDecimal r22_capital_gt24m) {
        this.r22_capital_gt24m = r22_capital_gt24m;
    }
    public String getR23_product() {
        return r23_product;
    }
    public void setR23_product(String r23_product) {
        this.r23_product = r23_product;
    }
    public BigDecimal getR23_amt_6m() {
        return r23_amt_6m;
    }
    public void setR23_amt_6m(BigDecimal r23_amt_6m) {
        this.r23_amt_6m = r23_amt_6m;
    }
    public BigDecimal getR23_risk_6m() {
        return r23_risk_6m;
    }
    public void setR23_risk_6m(BigDecimal r23_risk_6m) {
        this.r23_risk_6m = r23_risk_6m;
    }
    public BigDecimal getR23_capital_6m() {
        return r23_capital_6m;
    }
    public void setR23_capital_6m(BigDecimal r23_capital_6m) {
        this.r23_capital_6m = r23_capital_6m;
    }
    public BigDecimal getR23_amt_6to24m() {
        return r23_amt_6to24m;
    }
    public void setR23_amt_6to24m(BigDecimal r23_amt_6to24m) {
        this.r23_amt_6to24m = r23_amt_6to24m;
    }
    public BigDecimal getR23_risk_6to24m() {
        return r23_risk_6to24m;
    }
    public void setR23_risk_6to24m(BigDecimal r23_risk_6to24m) {
        this.r23_risk_6to24m = r23_risk_6to24m;
    }
    public BigDecimal getR23_capital_6to24m() {
        return r23_capital_6to24m;
    }
    public void setR23_capital_6to24m(BigDecimal r23_capital_6to24m) {
        this.r23_capital_6to24m = r23_capital_6to24m;
    }
    public BigDecimal getR23_amt_gt24m() {
        return r23_amt_gt24m;
    }
    public void setR23_amt_gt24m(BigDecimal r23_amt_gt24m) {
        this.r23_amt_gt24m = r23_amt_gt24m;
    }
    public BigDecimal getR23_risk_gt24m() {
        return r23_risk_gt24m;
    }
    public void setR23_risk_gt24m(BigDecimal r23_risk_gt24m) {
        this.r23_risk_gt24m = r23_risk_gt24m;
    }
    public BigDecimal getR23_capital_gt24m() {
        return r23_capital_gt24m;
    }
    public void setR23_capital_gt24m(BigDecimal r23_capital_gt24m) {
        this.r23_capital_gt24m = r23_capital_gt24m;
    }
    public String getR24_product() {
        return r24_product;
    }
    public void setR24_product(String r24_product) {
        this.r24_product = r24_product;
    }
    public BigDecimal getR24_amt_6m() {
        return r24_amt_6m;
    }
    public void setR24_amt_6m(BigDecimal r24_amt_6m) {
        this.r24_amt_6m = r24_amt_6m;
    }
    public BigDecimal getR24_risk_6m() {
        return r24_risk_6m;
    }
    public void setR24_risk_6m(BigDecimal r24_risk_6m) {
        this.r24_risk_6m = r24_risk_6m;
    }
    public BigDecimal getR24_capital_6m() {
        return r24_capital_6m;
    }
    public void setR24_capital_6m(BigDecimal r24_capital_6m) {
        this.r24_capital_6m = r24_capital_6m;
    }
    public BigDecimal getR24_amt_6to24m() {
        return r24_amt_6to24m;
    }
    public void setR24_amt_6to24m(BigDecimal r24_amt_6to24m) {
        this.r24_amt_6to24m = r24_amt_6to24m;
    }
    public BigDecimal getR24_risk_6to24m() {
        return r24_risk_6to24m;
    }
    public void setR24_risk_6to24m(BigDecimal r24_risk_6to24m) {
        this.r24_risk_6to24m = r24_risk_6to24m;
    }
    public BigDecimal getR24_capital_6to24m() {
        return r24_capital_6to24m;
    }
    public void setR24_capital_6to24m(BigDecimal r24_capital_6to24m) {
        this.r24_capital_6to24m = r24_capital_6to24m;
    }
    public BigDecimal getR24_amt_gt24m() {
        return r24_amt_gt24m;
    }
    public void setR24_amt_gt24m(BigDecimal r24_amt_gt24m) {
        this.r24_amt_gt24m = r24_amt_gt24m;
    }
    public BigDecimal getR24_risk_gt24m() {
        return r24_risk_gt24m;
    }
    public void setR24_risk_gt24m(BigDecimal r24_risk_gt24m) {
        this.r24_risk_gt24m = r24_risk_gt24m;
    }
    public BigDecimal getR24_capital_gt24m() {
        return r24_capital_gt24m;
    }
    public void setR24_capital_gt24m(BigDecimal r24_capital_gt24m) {
        this.r24_capital_gt24m = r24_capital_gt24m;
    }
    public String getR25_product() {
        return r25_product;
    }
    public void setR25_product(String r25_product) {
        this.r25_product = r25_product;
    }
    public BigDecimal getR25_amt_6m() {
        return r25_amt_6m;
    }
    public void setR25_amt_6m(BigDecimal r25_amt_6m) {
        this.r25_amt_6m = r25_amt_6m;
    }
    public BigDecimal getR25_risk_6m() {
        return r25_risk_6m;
    }
    public void setR25_risk_6m(BigDecimal r25_risk_6m) {
        this.r25_risk_6m = r25_risk_6m;
    }
    public BigDecimal getR25_capital_6m() {
        return r25_capital_6m;
    }
    public void setR25_capital_6m(BigDecimal r25_capital_6m) {
        this.r25_capital_6m = r25_capital_6m;
    }
    public BigDecimal getR25_amt_6to24m() {
        return r25_amt_6to24m;
    }
    public void setR25_amt_6to24m(BigDecimal r25_amt_6to24m) {
        this.r25_amt_6to24m = r25_amt_6to24m;
    }
    public BigDecimal getR25_risk_6to24m() {
        return r25_risk_6to24m;
    }
    public void setR25_risk_6to24m(BigDecimal r25_risk_6to24m) {
        this.r25_risk_6to24m = r25_risk_6to24m;
    }
    public BigDecimal getR25_capital_6to24m() {
        return r25_capital_6to24m;
    }
    public void setR25_capital_6to24m(BigDecimal r25_capital_6to24m) {
        this.r25_capital_6to24m = r25_capital_6to24m;
    }
    public BigDecimal getR25_amt_gt24m() {
        return r25_amt_gt24m;
    }
    public void setR25_amt_gt24m(BigDecimal r25_amt_gt24m) {
        this.r25_amt_gt24m = r25_amt_gt24m;
    }
    public BigDecimal getR25_risk_gt24m() {
        return r25_risk_gt24m;
    }
    public void setR25_risk_gt24m(BigDecimal r25_risk_gt24m) {
        this.r25_risk_gt24m = r25_risk_gt24m;
    }
    public BigDecimal getR25_capital_gt24m() {
        return r25_capital_gt24m;
    }
    public void setR25_capital_gt24m(BigDecimal r25_capital_gt24m) {
        this.r25_capital_gt24m = r25_capital_gt24m;
    }
    public String getR26_product() {
        return r26_product;
    }
    public void setR26_product(String r26_product) {
        this.r26_product = r26_product;
    }
    public BigDecimal getR26_amt_6m() {
        return r26_amt_6m;
    }
    public void setR26_amt_6m(BigDecimal r26_amt_6m) {
        this.r26_amt_6m = r26_amt_6m;
    }
    public BigDecimal getR26_risk_6m() {
        return r26_risk_6m;
    }
    public void setR26_risk_6m(BigDecimal r26_risk_6m) {
        this.r26_risk_6m = r26_risk_6m;
    }
    public BigDecimal getR26_capital_6m() {
        return r26_capital_6m;
    }
    public void setR26_capital_6m(BigDecimal r26_capital_6m) {
        this.r26_capital_6m = r26_capital_6m;
    }
    public BigDecimal getR26_amt_6to24m() {
        return r26_amt_6to24m;
    }
    public void setR26_amt_6to24m(BigDecimal r26_amt_6to24m) {
        this.r26_amt_6to24m = r26_amt_6to24m;
    }
    public BigDecimal getR26_risk_6to24m() {
        return r26_risk_6to24m;
    }
    public void setR26_risk_6to24m(BigDecimal r26_risk_6to24m) {
        this.r26_risk_6to24m = r26_risk_6to24m;
    }
    public BigDecimal getR26_capital_6to24m() {
        return r26_capital_6to24m;
    }
    public void setR26_capital_6to24m(BigDecimal r26_capital_6to24m) {
        this.r26_capital_6to24m = r26_capital_6to24m;
    }
    public BigDecimal getR26_amt_gt24m() {
        return r26_amt_gt24m;
    }
    public void setR26_amt_gt24m(BigDecimal r26_amt_gt24m) {
        this.r26_amt_gt24m = r26_amt_gt24m;
    }
    public BigDecimal getR26_risk_gt24m() {
        return r26_risk_gt24m;
    }
    public void setR26_risk_gt24m(BigDecimal r26_risk_gt24m) {
        this.r26_risk_gt24m = r26_risk_gt24m;
    }
    public BigDecimal getR26_capital_gt24m() {
        return r26_capital_gt24m;
    }
    public void setR26_capital_gt24m(BigDecimal r26_capital_gt24m) {
        this.r26_capital_gt24m = r26_capital_gt24m;
    }
    public String getR27_product() {
        return r27_product;
    }
    public void setR27_product(String r27_product) {
        this.r27_product = r27_product;
    }
    public BigDecimal getR27_amt_6m() {
        return r27_amt_6m;
    }
    public void setR27_amt_6m(BigDecimal r27_amt_6m) {
        this.r27_amt_6m = r27_amt_6m;
    }
    public BigDecimal getR27_risk_6m() {
        return r27_risk_6m;
    }
    public void setR27_risk_6m(BigDecimal r27_risk_6m) {
        this.r27_risk_6m = r27_risk_6m;
    }
    public BigDecimal getR27_capital_6m() {
        return r27_capital_6m;
    }
    public void setR27_capital_6m(BigDecimal r27_capital_6m) {
        this.r27_capital_6m = r27_capital_6m;
    }
    public BigDecimal getR27_amt_6to24m() {
        return r27_amt_6to24m;
    }
    public void setR27_amt_6to24m(BigDecimal r27_amt_6to24m) {
        this.r27_amt_6to24m = r27_amt_6to24m;
    }
    public BigDecimal getR27_risk_6to24m() {
        return r27_risk_6to24m;
    }
    public void setR27_risk_6to24m(BigDecimal r27_risk_6to24m) {
        this.r27_risk_6to24m = r27_risk_6to24m;
    }
    public BigDecimal getR27_capital_6to24m() {
        return r27_capital_6to24m;
    }
    public void setR27_capital_6to24m(BigDecimal r27_capital_6to24m) {
        this.r27_capital_6to24m = r27_capital_6to24m;
    }
    public BigDecimal getR27_amt_gt24m() {
        return r27_amt_gt24m;
    }
    public void setR27_amt_gt24m(BigDecimal r27_amt_gt24m) {
        this.r27_amt_gt24m = r27_amt_gt24m;
    }
    public BigDecimal getR27_risk_gt24m() {
        return r27_risk_gt24m;
    }
    public void setR27_risk_gt24m(BigDecimal r27_risk_gt24m) {
        this.r27_risk_gt24m = r27_risk_gt24m;
    }
    public BigDecimal getR27_capital_gt24m() {
        return r27_capital_gt24m;
    }
    public void setR27_capital_gt24m(BigDecimal r27_capital_gt24m) {
        this.r27_capital_gt24m = r27_capital_gt24m;
    }
    public String getR28_product() {
        return r28_product;
    }
    public void setR28_product(String r28_product) {
        this.r28_product = r28_product;
    }
    public BigDecimal getR28_amt_6m() {
        return r28_amt_6m;
    }
    public void setR28_amt_6m(BigDecimal r28_amt_6m) {
        this.r28_amt_6m = r28_amt_6m;
    }
    public BigDecimal getR28_risk_6m() {
        return r28_risk_6m;
    }
    public void setR28_risk_6m(BigDecimal r28_risk_6m) {
        this.r28_risk_6m = r28_risk_6m;
    }
    public BigDecimal getR28_capital_6m() {
        return r28_capital_6m;
    }
    public void setR28_capital_6m(BigDecimal r28_capital_6m) {
        this.r28_capital_6m = r28_capital_6m;
    }
    public BigDecimal getR28_amt_6to24m() {
        return r28_amt_6to24m;
    }
    public void setR28_amt_6to24m(BigDecimal r28_amt_6to24m) {
        this.r28_amt_6to24m = r28_amt_6to24m;
    }
    public BigDecimal getR28_risk_6to24m() {
        return r28_risk_6to24m;
    }
    public void setR28_risk_6to24m(BigDecimal r28_risk_6to24m) {
        this.r28_risk_6to24m = r28_risk_6to24m;
    }
    public BigDecimal getR28_capital_6to24m() {
        return r28_capital_6to24m;
    }
    public void setR28_capital_6to24m(BigDecimal r28_capital_6to24m) {
        this.r28_capital_6to24m = r28_capital_6to24m;
    }
    public BigDecimal getR28_amt_gt24m() {
        return r28_amt_gt24m;
    }
    public void setR28_amt_gt24m(BigDecimal r28_amt_gt24m) {
        this.r28_amt_gt24m = r28_amt_gt24m;
    }
    public BigDecimal getR28_risk_gt24m() {
        return r28_risk_gt24m;
    }
    public void setR28_risk_gt24m(BigDecimal r28_risk_gt24m) {
        this.r28_risk_gt24m = r28_risk_gt24m;
    }
    public BigDecimal getR28_capital_gt24m() {
        return r28_capital_gt24m;
    }
    public void setR28_capital_gt24m(BigDecimal r28_capital_gt24m) {
        this.r28_capital_gt24m = r28_capital_gt24m;
    }
    public String getR29_product() {
        return r29_product;
    }
    public void setR29_product(String r29_product) {
        this.r29_product = r29_product;
    }
    public BigDecimal getR29_amt_6m() {
        return r29_amt_6m;
    }
    public void setR29_amt_6m(BigDecimal r29_amt_6m) {
        this.r29_amt_6m = r29_amt_6m;
    }
    public BigDecimal getR29_risk_6m() {
        return r29_risk_6m;
    }
    public void setR29_risk_6m(BigDecimal r29_risk_6m) {
        this.r29_risk_6m = r29_risk_6m;
    }
    public BigDecimal getR29_capital_6m() {
        return r29_capital_6m;
    }
    public void setR29_capital_6m(BigDecimal r29_capital_6m) {
        this.r29_capital_6m = r29_capital_6m;
    }
    public BigDecimal getR29_amt_6to24m() {
        return r29_amt_6to24m;
    }
    public void setR29_amt_6to24m(BigDecimal r29_amt_6to24m) {
        this.r29_amt_6to24m = r29_amt_6to24m;
    }
    public BigDecimal getR29_risk_6to24m() {
        return r29_risk_6to24m;
    }
    public void setR29_risk_6to24m(BigDecimal r29_risk_6to24m) {
        this.r29_risk_6to24m = r29_risk_6to24m;
    }
    public BigDecimal getR29_capital_6to24m() {
        return r29_capital_6to24m;
    }
    public void setR29_capital_6to24m(BigDecimal r29_capital_6to24m) {
        this.r29_capital_6to24m = r29_capital_6to24m;
    }
    public BigDecimal getR29_amt_gt24m() {
        return r29_amt_gt24m;
    }
    public void setR29_amt_gt24m(BigDecimal r29_amt_gt24m) {
        this.r29_amt_gt24m = r29_amt_gt24m;
    }
    public BigDecimal getR29_risk_gt24m() {
        return r29_risk_gt24m;
    }
    public void setR29_risk_gt24m(BigDecimal r29_risk_gt24m) {
        this.r29_risk_gt24m = r29_risk_gt24m;
    }
    public BigDecimal getR29_capital_gt24m() {
        return r29_capital_gt24m;
    }
    public void setR29_capital_gt24m(BigDecimal r29_capital_gt24m) {
        this.r29_capital_gt24m = r29_capital_gt24m;
    }
    public String getR30_product() {
        return r30_product;
    }
    public void setR30_product(String r30_product) {
        this.r30_product = r30_product;
    }
    public BigDecimal getR30_amt_6m() {
        return r30_amt_6m;
    }
    public void setR30_amt_6m(BigDecimal r30_amt_6m) {
        this.r30_amt_6m = r30_amt_6m;
    }
    public BigDecimal getR30_risk_6m() {
        return r30_risk_6m;
    }
    public void setR30_risk_6m(BigDecimal r30_risk_6m) {
        this.r30_risk_6m = r30_risk_6m;
    }
    public BigDecimal getR30_capital_6m() {
        return r30_capital_6m;
    }
    public void setR30_capital_6m(BigDecimal r30_capital_6m) {
        this.r30_capital_6m = r30_capital_6m;
    }
    public BigDecimal getR30_amt_6to24m() {
        return r30_amt_6to24m;
    }
    public void setR30_amt_6to24m(BigDecimal r30_amt_6to24m) {
        this.r30_amt_6to24m = r30_amt_6to24m;
    }
    public BigDecimal getR30_risk_6to24m() {
        return r30_risk_6to24m;
    }
    public void setR30_risk_6to24m(BigDecimal r30_risk_6to24m) {
        this.r30_risk_6to24m = r30_risk_6to24m;
    }
    public BigDecimal getR30_capital_6to24m() {
        return r30_capital_6to24m;
    }
    public void setR30_capital_6to24m(BigDecimal r30_capital_6to24m) {
        this.r30_capital_6to24m = r30_capital_6to24m;
    }
    public BigDecimal getR30_amt_gt24m() {
        return r30_amt_gt24m;
    }
    public void setR30_amt_gt24m(BigDecimal r30_amt_gt24m) {
        this.r30_amt_gt24m = r30_amt_gt24m;
    }
    public BigDecimal getR30_risk_gt24m() {
        return r30_risk_gt24m;
    }
    public void setR30_risk_gt24m(BigDecimal r30_risk_gt24m) {
        this.r30_risk_gt24m = r30_risk_gt24m;
    }
    public BigDecimal getR30_capital_gt24m() {
        return r30_capital_gt24m;
    }
    public void setR30_capital_gt24m(BigDecimal r30_capital_gt24m) {
        this.r30_capital_gt24m = r30_capital_gt24m;
    }
    public String getR31_product() {
        return r31_product;
    }
    public void setR31_product(String r31_product) {
        this.r31_product = r31_product;
    }
    public BigDecimal getR31_amt_6m() {
        return r31_amt_6m;
    }
    public void setR31_amt_6m(BigDecimal r31_amt_6m) {
        this.r31_amt_6m = r31_amt_6m;
    }
    public BigDecimal getR31_risk_6m() {
        return r31_risk_6m;
    }
    public void setR31_risk_6m(BigDecimal r31_risk_6m) {
        this.r31_risk_6m = r31_risk_6m;
    }
    public BigDecimal getR31_capital_6m() {
        return r31_capital_6m;
    }
    public void setR31_capital_6m(BigDecimal r31_capital_6m) {
        this.r31_capital_6m = r31_capital_6m;
    }
    public BigDecimal getR31_amt_6to24m() {
        return r31_amt_6to24m;
    }
    public void setR31_amt_6to24m(BigDecimal r31_amt_6to24m) {
        this.r31_amt_6to24m = r31_amt_6to24m;
    }
    public BigDecimal getR31_risk_6to24m() {
        return r31_risk_6to24m;
    }
    public void setR31_risk_6to24m(BigDecimal r31_risk_6to24m) {
        this.r31_risk_6to24m = r31_risk_6to24m;
    }
    public BigDecimal getR31_capital_6to24m() {
        return r31_capital_6to24m;
    }
    public void setR31_capital_6to24m(BigDecimal r31_capital_6to24m) {
        this.r31_capital_6to24m = r31_capital_6to24m;
    }
    public BigDecimal getR31_amt_gt24m() {
        return r31_amt_gt24m;
    }
    public void setR31_amt_gt24m(BigDecimal r31_amt_gt24m) {
        this.r31_amt_gt24m = r31_amt_gt24m;
    }
    public BigDecimal getR31_risk_gt24m() {
        return r31_risk_gt24m;
    }
    public void setR31_risk_gt24m(BigDecimal r31_risk_gt24m) {
        this.r31_risk_gt24m = r31_risk_gt24m;
    }
    public BigDecimal getR31_capital_gt24m() {
        return r31_capital_gt24m;
    }
    public void setR31_capital_gt24m(BigDecimal r31_capital_gt24m) {
        this.r31_capital_gt24m = r31_capital_gt24m;
    }
    public String getR32_product() {
        return r32_product;
    }
    public void setR32_product(String r32_product) {
        this.r32_product = r32_product;
    }
    public BigDecimal getR32_amt_6m() {
        return r32_amt_6m;
    }
    public void setR32_amt_6m(BigDecimal r32_amt_6m) {
        this.r32_amt_6m = r32_amt_6m;
    }
    public BigDecimal getR32_risk_6m() {
        return r32_risk_6m;
    }
    public void setR32_risk_6m(BigDecimal r32_risk_6m) {
        this.r32_risk_6m = r32_risk_6m;
    }
    public BigDecimal getR32_capital_6m() {
        return r32_capital_6m;
    }
    public void setR32_capital_6m(BigDecimal r32_capital_6m) {
        this.r32_capital_6m = r32_capital_6m;
    }
    public BigDecimal getR32_amt_6to24m() {
        return r32_amt_6to24m;
    }
    public void setR32_amt_6to24m(BigDecimal r32_amt_6to24m) {
        this.r32_amt_6to24m = r32_amt_6to24m;
    }
    public BigDecimal getR32_risk_6to24m() {
        return r32_risk_6to24m;
    }
    public void setR32_risk_6to24m(BigDecimal r32_risk_6to24m) {
        this.r32_risk_6to24m = r32_risk_6to24m;
    }
    public BigDecimal getR32_capital_6to24m() {
        return r32_capital_6to24m;
    }
    public void setR32_capital_6to24m(BigDecimal r32_capital_6to24m) {
        this.r32_capital_6to24m = r32_capital_6to24m;
    }
    public BigDecimal getR32_amt_gt24m() {
        return r32_amt_gt24m;
    }
    public void setR32_amt_gt24m(BigDecimal r32_amt_gt24m) {
        this.r32_amt_gt24m = r32_amt_gt24m;
    }
    public BigDecimal getR32_risk_gt24m() {
        return r32_risk_gt24m;
    }
    public void setR32_risk_gt24m(BigDecimal r32_risk_gt24m) {
        this.r32_risk_gt24m = r32_risk_gt24m;
    }
    public BigDecimal getR32_capital_gt24m() {
        return r32_capital_gt24m;
    }
    public void setR32_capital_gt24m(BigDecimal r32_capital_gt24m) {
        this.r32_capital_gt24m = r32_capital_gt24m;
    }
    public String getR33_product() {
        return r33_product;
    }
    public void setR33_product(String r33_product) {
        this.r33_product = r33_product;
    }
    public BigDecimal getR33_amt_6m() {
        return r33_amt_6m;
    }
    public void setR33_amt_6m(BigDecimal r33_amt_6m) {
        this.r33_amt_6m = r33_amt_6m;
    }
    public BigDecimal getR33_risk_6m() {
        return r33_risk_6m;
    }
    public void setR33_risk_6m(BigDecimal r33_risk_6m) {
        this.r33_risk_6m = r33_risk_6m;
    }
    public BigDecimal getR33_capital_6m() {
        return r33_capital_6m;
    }
    public void setR33_capital_6m(BigDecimal r33_capital_6m) {
        this.r33_capital_6m = r33_capital_6m;
    }
    public BigDecimal getR33_amt_6to24m() {
        return r33_amt_6to24m;
    }
    public void setR33_amt_6to24m(BigDecimal r33_amt_6to24m) {
        this.r33_amt_6to24m = r33_amt_6to24m;
    }
    public BigDecimal getR33_risk_6to24m() {
        return r33_risk_6to24m;
    }
    public void setR33_risk_6to24m(BigDecimal r33_risk_6to24m) {
        this.r33_risk_6to24m = r33_risk_6to24m;
    }
    public BigDecimal getR33_capital_6to24m() {
        return r33_capital_6to24m;
    }
    public void setR33_capital_6to24m(BigDecimal r33_capital_6to24m) {
        this.r33_capital_6to24m = r33_capital_6to24m;
    }
    public BigDecimal getR33_amt_gt24m() {
        return r33_amt_gt24m;
    }
    public void setR33_amt_gt24m(BigDecimal r33_amt_gt24m) {
        this.r33_amt_gt24m = r33_amt_gt24m;
    }
    public BigDecimal getR33_risk_gt24m() {
        return r33_risk_gt24m;
    }
    public void setR33_risk_gt24m(BigDecimal r33_risk_gt24m) {
        this.r33_risk_gt24m = r33_risk_gt24m;
    }
    public BigDecimal getR33_capital_gt24m() {
        return r33_capital_gt24m;
    }
    public void setR33_capital_gt24m(BigDecimal r33_capital_gt24m) {
        this.r33_capital_gt24m = r33_capital_gt24m;
    }
    public BigDecimal getR35_tot_spec_risk_ch() {
        return r35_tot_spec_risk_ch;
    }
    public void setR35_tot_spec_risk_ch(BigDecimal r35_tot_spec_risk_ch) {
        this.r35_tot_spec_risk_ch = r35_tot_spec_risk_ch;
    }
    public Date getReportDate() {
        return reportDate;
    }
    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
    public BigDecimal getReportVersion() {
        return reportVersion;
    }
    public void setReportVersion(BigDecimal reportVersion) {
        this.reportVersion = reportVersion;
    }
    public String getReport_frequency() {
        return report_frequency;
    }
    public void setReport_frequency(String report_frequency) {
        this.report_frequency = report_frequency;
    }
    public String getReport_code() {
        return report_code;
    }
    public void setReport_code(String report_code) {
        this.report_code = report_code;
    }
    public String getReport_desc() {
        return report_desc;
    }
    public void setReport_desc(String report_desc) {
        this.report_desc = report_desc;
    }
    public String getEntity_flg() {
        return entity_flg;
    }
    public void setEntity_flg(String entity_flg) {
        this.entity_flg = entity_flg;
    }
    public String getModify_flg() {
        return modify_flg;
    }
    public void setModify_flg(String modify_flg) {
        this.modify_flg = modify_flg;
    }
    public String getDel_flg() {
        return del_flg;
    }
    public void setDel_flg(String del_flg) {
        this.del_flg = del_flg;
    }
}

// ------------------------------
// Summary table entity
// ------------------------------
public static class M_SIR_Summary_Entity extends M_SIR_Base_Entity {
}

// ------------------------------
// Detail table entity
// ------------------------------
public static class M_SIR_Detail_Entity extends M_SIR_Base_Entity {
}

// ------------------------------
// Resubmission summary table entity
// ------------------------------
public static class M_SIR_Resub_Summary_Entity extends M_SIR_Base_Entity {
    private Date reportResubDate;
    public Date getReportResubDate() { return reportResubDate; }
    public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
}

// ------------------------------
// Resubmission detail table entity
// ------------------------------
public static class M_SIR_Resub_Detail_Entity extends M_SIR_Base_Entity {
    private Date reportResubDate;
    public Date getReportResubDate() { return reportResubDate; }
    public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
}

// ------------------------------
// Archival summary table entity
// ------------------------------
public static class M_SIR_Archival_Summary_Entity extends M_SIR_Base_Entity {
    private Date reportResubDate;
    public Date getReportResubDate() { return reportResubDate; }
    public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
}

// ------------------------------
// Archival detail table entity
// ------------------------------
public static class M_SIR_Archival_Detail_Entity extends M_SIR_Base_Entity {
    private Date reportResubDate;
    public Date getReportResubDate() { return reportResubDate; }
    public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
}

// ------------------------------
// Composite Primary Key class for M_SIR
// ------------------------------
public static class M_SIR_PK implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date reportDate;
    private BigDecimal reportVersion;
    public M_SIR_PK() {}
    public M_SIR_PK(Date reportDate, BigDecimal reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }
    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
    public BigDecimal getReportVersion() { return reportVersion; }
    public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof M_SIR_PK)) return false;
        M_SIR_PK that = (M_SIR_PK) o;
        return java.util.Objects.equals(reportDate, that.reportDate) &&
               java.util.Objects.equals(reportVersion, that.reportVersion);
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(reportDate, reportVersion);
    }
}
}
