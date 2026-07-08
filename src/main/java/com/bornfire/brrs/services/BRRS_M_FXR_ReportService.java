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
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service

// ------------------------------
// Service class for handling M_FXR report data logic and generation
// ------------------------------
public class BRRS_M_FXR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_FXR_ReportService.class);

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
	// Method to retrieve the ModelAndView view for the FXR report
	// ------------------------------
	public ModelAndView getM_FXRView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

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
				String sql = "SELECT * FROM BRRS_M_FXR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
				List<M_FXR_Archival_Summary_Entity> T1Master = jdbcTemplate.query(
						sql, new BeanPropertyRowMapper<>(M_FXR_Archival_Summary_Entity.class), d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				String sql = "SELECT * FROM BRRS_M_FXR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
				List<M_FXR_Resub_Summary_Entity> T1Master = jdbcTemplate.query(
						sql, new BeanPropertyRowMapper<>(M_FXR_Resub_Summary_Entity.class), d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				String sql = "SELECT * FROM BRRS_M_FXR_SUMMARYTABLE WHERE REPORT_DATE = ?";
				List<M_FXR_Summary_Entity> T1Master = jdbcTemplate.query(
						sql, new BeanPropertyRowMapper<>(M_FXR_Summary_Entity.class), d1);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					String sql = "SELECT * FROM BRRS_M_FXR_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
					List<M_FXR_Archival_Detail_Entity> T1Master = jdbcTemplate.query(
							sql, new BeanPropertyRowMapper<>(M_FXR_Archival_Detail_Entity.class), d1, version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					String sql = "SELECT * FROM BRRS_M_FXR_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
					List<M_FXR_Resub_Detail_Entity> T1Master = jdbcTemplate.query(
							sql, new BeanPropertyRowMapper<>(M_FXR_Resub_Detail_Entity.class), d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {
					String sql = "SELECT * FROM BRRS_M_FXR_DETAILTABLE WHERE REPORT_DATE = ?";
					List<M_FXR_Detail_Entity> T1Master = jdbcTemplate.query(
							sql, new BeanPropertyRowMapper<>(M_FXR_Detail_Entity.class), dateformat.parse(todate));
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

	// ------------------------------
	// Method to query a summary record by report date
	// ------------------------------
	private M_FXR_Summary_Entity findSummaryById(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_FXR_SUMMARYTABLE WHERE REPORT_DATE = ?";
		List<M_FXR_Summary_Entity> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_FXR_Summary_Entity.class), reportDate);
		return list.isEmpty() ? null : list.get(0);
	}

	// ------------------------------
	// Method to query a detail record by report date
	// ------------------------------
	private M_FXR_Detail_Entity findDetailById(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_FXR_DETAILTABLE WHERE REPORT_DATE = ?";
		List<M_FXR_Detail_Entity> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_FXR_Detail_Entity.class), reportDate);
		return list.isEmpty() ? null : list.get(0);
	}

	// ------------------------------
	// Method to dynamically save or update an entity to the database using reflection
	// ------------------------------
	private void saveEntity(Object entity, String tableName) {
		try {
			Class<?> clazz = entity.getClass();
			java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
			
			List<String> pkColumns = new ArrayList<>();
			List<Object> pkValues = new ArrayList<>();
			
			List<String> dataColumns = new ArrayList<>();
			List<Object> dataValues = new ArrayList<>();
			
			for (java.lang.reflect.Field field : fields) {
				field.setAccessible(true);
				
				if (field.getName().startsWith("$") || field.getName().equals("serialVersionUID")) {
					continue;
				}
				
				String columnName = null;
				if (field.isAnnotationPresent(Column.class)) {
					columnName = field.getAnnotation(Column.class).name();
				}
				if (columnName == null || columnName.isEmpty()) {
					columnName = field.getName().toUpperCase();
				}
				
				Object value = field.get(entity);
				if (value instanceof java.util.Date) {
					if (field.isAnnotationPresent(Temporal.class) && 
						field.getAnnotation(Temporal.class).value() == TemporalType.DATE) {
						value = new java.sql.Date(((java.util.Date) value).getTime());
					} else {
						value = new java.sql.Timestamp(((java.util.Date) value).getTime());
					}
				}
				
				if (field.isAnnotationPresent(Id.class)) {
					pkColumns.add(columnName);
					pkValues.add(value);
				} else {
					dataColumns.add(columnName);
					dataValues.add(value);
				}
			}
			
			StringBuilder selectSql = new StringBuilder("SELECT COUNT(*) FROM ").append(tableName).append(" WHERE ");
			for (int i = 0; i < pkColumns.size(); i++) {
				if (i > 0) selectSql.append(" AND ");
				selectSql.append(pkColumns.get(i)).append(" = ?");
			}
			
			Integer count = jdbcTemplate.queryForObject(selectSql.toString(), Integer.class, pkValues.toArray());
			
			if (count != null && count > 0) {
				StringBuilder updateSql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
				List<Object> params = new ArrayList<>();
				for (int i = 0; i < dataColumns.size(); i++) {
					if (i > 0) updateSql.append(", ");
					updateSql.append(dataColumns.get(i)).append(" = ?");
					params.add(dataValues.get(i));
				}
				updateSql.append(" WHERE ");
				for (int i = 0; i < pkColumns.size(); i++) {
					if (i > 0) updateSql.append(" AND ");
					updateSql.append(pkColumns.get(i)).append(" = ?");
					params.add(pkValues.get(i));
				}
				jdbcTemplate.update(updateSql.toString(), params.toArray());
			} else {
				StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
				StringBuilder placeholders = new StringBuilder();
				List<Object> params = new ArrayList<>();
				
				int colIndex = 0;
				for (int i = 0; i < pkColumns.size(); i++) {
					if (colIndex > 0) {
						insertSql.append(", ");
						placeholders.append(", ");
					}
					insertSql.append(pkColumns.get(i));
					placeholders.append("?");
					params.add(pkValues.get(i));
					colIndex++;
				}
				for (int i = 0; i < dataColumns.size(); i++) {
					if (colIndex > 0) {
						insertSql.append(", ");
						placeholders.append(", ");
					}
					insertSql.append(dataColumns.get(i));
					placeholders.append("?");
					params.add(dataValues.get(i));
					colIndex++;
				}
				insertSql.append(") VALUES (").append(placeholders).append(")");
				jdbcTemplate.update(insertSql.toString(), params.toArray());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error saving entity to table " + tableName, e);
		}
	}

	// ------------------------------
	// Method to update Part 1 fields of the FXR summary and detail report
	// ------------------------------
	public void updateReport1(M_FXR_Summary_Entity updatedEntity) {
		System.out.println("Came to services1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FXR_Summary_Entity existingSummary = findSummaryById(updatedEntity.getReportDate());
		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate());
		}
		// 🔹 Create Audit Copy before editing
		M_FXR_Summary_Entity oldcopy = new M_FXR_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);
		// 🔹 Fetch or create DETAIL
		M_FXR_Detail_Entity detailEntity = findDetailById(updatedEntity.getReportDate());
		if (detailEntity == null) {
			detailEntity = new M_FXR_Detail_Entity();
			detailEntity.setReportDate(updatedEntity.getReportDate());
		}

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

		// Evaluate the actual changes calculated post-normalization
		String changes = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_FXR Changes Length = " + changes.length());

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		saveEntity(existingSummary, "BRRS_M_FXR_SUMMARYTABLE");
		saveEntity(detailEntity, "BRRS_M_FXR_DETAILTABLE");

		if (changes != null && !changes.isEmpty()) {

			if (changes.length() > 2000) {
				changes = changes.substring(0, 2000);
			}

			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReportDate().toString(),
					"M_FXR Summary Screen", "BRRS_M_FXR_SUMMARY");
		}

		System.out.println("Update completed successfully");
	}

	// ------------------------------
	// Method to update Part 2 fields of the FXR summary and detail report
	// ------------------------------
	public void updateReport2(M_FXR_Summary_Entity updatedEntity) {
		System.out.println("Came to services2");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FXR_Summary_Entity existingSummary = findSummaryById(updatedEntity.getReportDate());
		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate());
		}
		// 🔹 Create Audit Copy before editing
		M_FXR_Summary_Entity oldcopy = new M_FXR_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);
		// 🔹 Fetch or create DETAIL
		M_FXR_Detail_Entity detailEntity = findDetailById(updatedEntity.getReportDate());
		if (detailEntity == null) {
			detailEntity = new M_FXR_Detail_Entity();
			detailEntity.setReportDate(updatedEntity.getReportDate());
		}

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
		// Evaluate the actual changes calculated post-normalization
		String changes = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_FXR Changes Length = " + changes.length());

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		saveEntity(existingSummary, "BRRS_M_FXR_SUMMARYTABLE");
		saveEntity(detailEntity, "BRRS_M_FXR_DETAILTABLE");

		if (changes != null && !changes.isEmpty()) {

			if (changes.length() > 2000) {
				changes = changes.substring(0, 2000);
			}

			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReportDate().toString(),
					"M_FXR Summary Screen", "BRRS_M_FXR_SUMMARY");
		}

		System.out.println("Update completed successfully");
	}

	// ------------------------------
	// Method to update Part 3 fields of the FXR summary and detail report
	// ------------------------------
	public void updateReport3(M_FXR_Summary_Entity updatedEntity) {
		System.out.println("Came to services3");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FXR_Summary_Entity existingSummary = findSummaryById(updatedEntity.getReportDate());
		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate());
		}
		// 🔹 Create Audit Copy before editing
		M_FXR_Summary_Entity oldcopy = new M_FXR_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);
		// 🔹 Fetch or create DETAIL
		M_FXR_Detail_Entity detailEntity = findDetailById(updatedEntity.getReportDate());
		if (detailEntity == null) {
			detailEntity = new M_FXR_Detail_Entity();
			detailEntity.setReportDate(updatedEntity.getReportDate());
		}

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

		// Evaluate the actual changes calculated post-normalization
		String changes = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_FXR Changes Length = " + changes.length());

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables
		saveEntity(existingSummary, "BRRS_M_FXR_SUMMARYTABLE");
		saveEntity(detailEntity, "BRRS_M_FXR_DETAILTABLE");

		if (changes != null && !changes.isEmpty()) {

			if (changes.length() > 2000) {
				changes = changes.substring(0, 2000);
			}

			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReportDate().toString(),
					"M_FXR Summary Screen", "BRRS_M_FXR_SUMMARY");
		}

		System.out.println("Update completed successfully");
	}

	// ------------------------------
	// Method to insert resubmitted summary and detail records
	// ------------------------------
	public void updateResubReport(M_FXR_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_FXR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";
		BigDecimal maxResubVer = null;
		try {
			maxResubVer = jdbcTemplate.queryForObject(sql, BigDecimal.class, reportDate);
		} catch (Exception e) {
			maxResubVer = null;
		}

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

		saveEntity(resubSummary, "BRRS_M_FXR_RESUB_SUMMARYTABLE");
		saveEntity(resubDetail, "BRRS_M_FXR_RESUB_DETAILTABLE");

		saveEntity(archSummary, "BRRS_M_FXR_ARCHIVALTABLE_SUMMARY");
		saveEntity(archDetail, "BRRS_M_FXR_ARCHIVALTABLE_DETAIL");
	}

	// ------------------------------
	// Method to retrieve the resubmission summary list
	// ------------------------------
	public List<Object[]> getM_FXRResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM BRRS_M_FXR_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
			List<M_FXR_Archival_Summary_Entity> latestArchivalList = jdbcTemplate.query(
					sql, new BeanPropertyRowMapper<>(M_FXR_Archival_Summary_Entity.class));

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
	// ------------------------------
	// Method to retrieve the archival summary list
	// ------------------------------
	public List<Object[]> getM_FXRArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			String sql = "SELECT * FROM BRRS_M_FXR_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
			List<M_FXR_Archival_Summary_Entity> repoData = jdbcTemplate.query(
					sql, new BeanPropertyRowMapper<>(M_FXR_Archival_Summary_Entity.class));

			if (repoData != null && !repoData.isEmpty()) {
				for (M_FXR_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
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

	// ------------------------------
	// Method to generate the Excel file for the FXR report
	// ------------------------------
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

				String sql = "SELECT * FROM BRRS_M_FXR_SUMMARYTABLE WHERE REPORT_DATE = ?";
				List<M_FXR_Summary_Entity> dataList = jdbcTemplate.query(
						sql, new BeanPropertyRowMapper<>(M_FXR_Summary_Entity.class), dateformat.parse(todate));

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

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {

							M_FXR_Summary_Entity record = dataList.get(i);
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
							row = sheet.getRow(10);
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
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FXR SUMMARY", null,
								"BRRS_M_FXR_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	// ------------------------------
	// Method to generate the email Excel file for the FXR report
	// ------------------------------
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
			String sql = "SELECT * FROM BRRS_M_FXR_SUMMARYTABLE WHERE REPORT_DATE = ?";
			List<M_FXR_Summary_Entity> dataList = jdbcTemplate.query(
					sql, new BeanPropertyRowMapper<>(M_FXR_Summary_Entity.class), dateformat.parse(todate));

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
				int startRow = 5;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {

						M_FXR_Summary_Entity record = dataList.get(i);
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
						row = sheet.getRow(10);
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
				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FXR EMAIL SUMMARY", null,
							"BRRS_M_FXR_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	// ------------------------------
	// Method to generate the archival Excel file for the FXR report
	// ------------------------------
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

		String sql = "SELECT * FROM BRRS_M_FXR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_FXR_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				sql, new BeanPropertyRowMapper<>(M_FXR_Archival_Summary_Entity.class), dateformat.parse(todate), version);

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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_FXR_Archival_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(10);
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FXR ARCHIVAL SUMMARY", null,
						"BRRS_M_FXR_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	// ------------------------------
	// Method to generate the email archival Excel file for the FXR report
	// ------------------------------
	public byte[] BRRS_M_FXREmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		String sql = "SELECT * FROM BRRS_M_FXR_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_FXR_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				sql, new BeanPropertyRowMapper<>(M_FXR_Archival_Summary_Entity.class), dateformat.parse(todate), version);

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
			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_FXR_Archival_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(10);
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FXR EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_FXR_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	// ------------------------------
	// Method to generate the resubmission Excel file for the FXR report
	// ------------------------------
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

		String sql = "SELECT * FROM BRRS_M_FXR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_FXR_Resub_Summary_Entity> dataList = jdbcTemplate.query(
				sql, new BeanPropertyRowMapper<>(M_FXR_Resub_Summary_Entity.class), dateformat.parse(todate), version);

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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_FXR_Resub_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(10);
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FXR RESUB SUMMARY", null,
						"BRRS_M_FXR_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// Resub Email Excel
	// ------------------------------
	// Method to generate the email resubmission Excel file for the FXR report
	// ------------------------------
	public byte[] BRRS_M_FXRResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		String sql = "SELECT * FROM BRRS_M_FXR_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_FXR_Resub_Summary_Entity> dataList = jdbcTemplate.query(
				sql, new BeanPropertyRowMapper<>(M_FXR_Resub_Summary_Entity.class), dateformat.parse(todate), version);

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
			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_FXR_Resub_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(10);
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FXR EMAIL RESUB SUMMARY", null,
						"BRRS_M_FXR_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}



	// ------------------------------
	// Primary key class for FXR archival and resubmission entities
	// ------------------------------
public static class M_FXR_PK implements Serializable {

    private Date reportDate;
    private BigDecimal reportVersion;

    // default constructor
    public M_FXR_PK() {}

    // parameterized constructor
    public M_FXR_PK(Date reportDate, BigDecimal reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof M_FXR_PK)) return false;
        M_FXR_PK that = (M_FXR_PK) o;
        return Objects.equals(reportDate, that.reportDate) &&
               Objects.equals(reportVersion, that.reportVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportDate, reportVersion);
    }

    // getters & setters
    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date reportDate) { this.reportDate = reportDate; }

    public BigDecimal getReportVersion() { return reportVersion; }
    public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
}





	// ------------------------------
	// Entity representing the summary table for the FXR report
	// ------------------------------
public static class M_FXR_Summary_Entity {

   // ===== PRIMARY KEY =====

    @Id
    @Column(name = "REPORT_DATE")
    @Temporal(TemporalType.DATE)
    private Date reportDate;

    // ===== R11 =====

    @Column(name = "R11_NET_SPOT_POSITION")
    private BigDecimal r11_net_spot_position;

    @Column(name = "R11_NET_FORWARD_POSITION")
    private BigDecimal r11_net_forward_position;

    @Column(name = "R11_GUARANTEES")
    private BigDecimal r11_guarantees;

    @Column(name = "R11_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r11_net_future_inc_or_exp;

    @Column(name = "R11_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r11_net_delta_wei_fx_opt_posi;

    @Column(name = "R11_OTHER_ITEMS")
    private BigDecimal r11_other_items;

    @Column(name = "R11_NET_LONG_POSITION")
    private BigDecimal r11_net_long_position;

    @Column(name = "R11_OR")
    private BigDecimal r11_or;

    @Column(name = "R11_NET_SHORT_POSITION")
    private BigDecimal r11_net_short_position;

    // ===== R12 =====

    @Column(name = "R12_NET_SPOT_POSITION")
    private BigDecimal r12_net_spot_position;

    @Column(name = "R12_NET_FORWARD_POSITION")
    private BigDecimal r12_net_forward_position;

    @Column(name = "R12_GUARANTEES")
    private BigDecimal r12_guarantees;

    @Column(name = "R12_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r12_net_future_inc_or_exp;

    @Column(name = "R12_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r12_net_delta_wei_fx_opt_posi;

    @Column(name = "R12_OTHER_ITEMS")
    private BigDecimal r12_other_items;

    @Column(name = "R12_NET_LONG_POSITION")
    private BigDecimal r12_net_long_position;

    @Column(name = "R12_OR")
    private BigDecimal r12_or;

    @Column(name = "R12_NET_SHORT_POSITION")
    private BigDecimal r12_net_short_position;

    // ===== R13 =====

    @Column(name = "R13_NET_SPOT_POSITION")
    private BigDecimal r13_net_spot_position;

    @Column(name = "R13_NET_FORWARD_POSITION")
    private BigDecimal r13_net_forward_position;

    @Column(name = "R13_GUARANTEES")
    private BigDecimal r13_guarantees;

    @Column(name = "R13_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r13_net_future_inc_or_exp;

    @Column(name = "R13_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r13_net_delta_wei_fx_opt_posi;

    @Column(name = "R13_OTHER_ITEMS")
    private BigDecimal r13_other_items;

    @Column(name = "R13_NET_LONG_POSITION")
    private BigDecimal r13_net_long_position;

    @Column(name = "R13_OR")
    private BigDecimal r13_or;

    @Column(name = "R13_NET_SHORT_POSITION")
    private BigDecimal r13_net_short_position;

    // ===== R14 =====

    @Column(name = "R14_NET_SPOT_POSITION")
    private BigDecimal r14_net_spot_position;

    @Column(name = "R14_NET_FORWARD_POSITION")
    private BigDecimal r14_net_forward_position;

    @Column(name = "R14_GUARANTEES")
    private BigDecimal r14_guarantees;

    @Column(name = "R14_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r14_net_future_inc_or_exp;

    @Column(name = "R14_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r14_net_delta_wei_fx_opt_posi;

    @Column(name = "R14_OTHER_ITEMS")
    private BigDecimal r14_other_items;

    @Column(name = "R14_NET_LONG_POSITION")
    private BigDecimal r14_net_long_position;

    @Column(name = "R14_OR")
    private BigDecimal r14_or;

    @Column(name = "R14_NET_SHORT_POSITION")
    private BigDecimal r14_net_short_position;

    // ===== R15 =====

    @Column(name = "R15_NET_SPOT_POSITION")
    private BigDecimal r15_net_spot_position;

    @Column(name = "R15_NET_FORWARD_POSITION")
    private BigDecimal r15_net_forward_position;

    @Column(name = "R15_GUARANTEES")
    private BigDecimal r15_guarantees;

    @Column(name = "R15_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r15_net_future_inc_or_exp;

    @Column(name = "R15_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r15_net_delta_wei_fx_opt_posi;

    @Column(name = "R15_OTHER_ITEMS")
    private BigDecimal r15_other_items;

    @Column(name = "R15_NET_LONG_POSITION")
    private BigDecimal r15_net_long_position;

    @Column(name = "R15_OR")
    private BigDecimal r15_or;

    @Column(name = "R15_NET_SHORT_POSITION")
    private BigDecimal r15_net_short_position;

    // ===== R16 =====

    @Column(name = "R16_NET_SPOT_POSITION")
    private BigDecimal r16_net_spot_position;

    @Column(name = "R16_NET_FORWARD_POSITION")
    private BigDecimal r16_net_forward_position;

    @Column(name = "R16_GUARANTEES")
    private BigDecimal r16_guarantees;

    @Column(name = "R16_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r16_net_future_inc_or_exp;

    @Column(name = "R16_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r16_net_delta_wei_fx_opt_posi;

    @Column(name = "R16_OTHER_ITEMS")
    private BigDecimal r16_other_items;

    @Column(name = "R16_NET_LONG_POSITION")
    private BigDecimal r16_net_long_position;

    @Column(name = "R16_OR")
    private BigDecimal r16_or;

    @Column(name = "R16_NET_SHORT_POSITION")
    private BigDecimal r16_net_short_position;

    // ===== R17 =====

    @Column(name = "R17_NET_LONG_POSITION")
    private BigDecimal r17_net_long_position;

    @Column(name = "R17_OR")
    private BigDecimal r17_or;

    @Column(name = "R17_NET_SHORT_POSITION")
    private BigDecimal r17_net_short_position;

    // ===== R21 =====

    @Column(name = "R21_LONG")
    private BigDecimal r21_long;

    @Column(name = "R21_SHORT")
    private BigDecimal r21_short;

    @Column(name = "R21_TOTAL_GROSS_LONG_SHORT")
    private BigDecimal r21_total_gross_long_short;

    @Column(name = "R21_NET_POSITION")
    private BigDecimal r21_net_position;

    // ===== R22 =====

    @Column(name = "R22_LONG")
    private BigDecimal r22_long;

    @Column(name = "R22_SHORT")
    private BigDecimal r22_short;

    @Column(name = "R22_TOTAL_GROSS_LONG_SHORT")
    private BigDecimal r22_total_gross_long_short;

    @Column(name = "R22_NET_POSITION")
    private BigDecimal r22_net_position;

    // ===== R23 =====

    @Column(name = "R23_NET_POSITION")
    private BigDecimal r23_net_position;

    // ===== R29 & R30 =====

    @Column(name = "R29_GREATER_NET_LONG_OR_SHORT")
    private BigDecimal r29_greater_net_long_or_short;

    @Column(name = "R29_ABS_VALUE_NET_GOLD_POSI")
    private BigDecimal r29_abs_value_net_gold_posi;

    @Column(name = "R29_CAPITAL_CHARGE")
    private BigDecimal r29_capital_charge;

    @Column(name = "R30_CAPITAL_REQUIRE")
    private BigDecimal r30_capital_require;

    // ===== COMMON FIELDS =====

    @Column(name = "REPORT_VERSION")
    private BigDecimal report_version;

    @Column(name = "REPORT_FREQUENCY")
    private String report_frequency;

    @Column(name = "REPORT_CODE")
    private String report_code;

    @Column(name = "REPORT_DESC")
    private String report_desc;

    @Column(name = "ENTITY_FLG")
    private String entity_flg;

    @Column(name = "MODIFY_FLG")
    private String modify_flg;

    @Column(name = "DEL_FLG")
    private String del_flg;

	public BigDecimal getR11_net_spot_position() {
		return r11_net_spot_position;
	}

	public void setR11_net_spot_position(BigDecimal r11_net_spot_position) {
		this.r11_net_spot_position = r11_net_spot_position;
	}

	public BigDecimal getR11_net_forward_position() {
		return r11_net_forward_position;
	}

	public void setR11_net_forward_position(BigDecimal r11_net_forward_position) {
		this.r11_net_forward_position = r11_net_forward_position;
	}

	public BigDecimal getR11_guarantees() {
		return r11_guarantees;
	}

	public void setR11_guarantees(BigDecimal r11_guarantees) {
		this.r11_guarantees = r11_guarantees;
	}

	public BigDecimal getR11_net_future_inc_or_exp() {
		return r11_net_future_inc_or_exp;
	}

	public void setR11_net_future_inc_or_exp(BigDecimal r11_net_future_inc_or_exp) {
		this.r11_net_future_inc_or_exp = r11_net_future_inc_or_exp;
	}

	public BigDecimal getR11_net_delta_wei_fx_opt_posi() {
		return r11_net_delta_wei_fx_opt_posi;
	}

	public void setR11_net_delta_wei_fx_opt_posi(BigDecimal r11_net_delta_wei_fx_opt_posi) {
		this.r11_net_delta_wei_fx_opt_posi = r11_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR11_other_items() {
		return r11_other_items;
	}

	public void setR11_other_items(BigDecimal r11_other_items) {
		this.r11_other_items = r11_other_items;
	}

	public BigDecimal getR11_net_long_position() {
		return r11_net_long_position;
	}

	public void setR11_net_long_position(BigDecimal r11_net_long_position) {
		this.r11_net_long_position = r11_net_long_position;
	}

	public BigDecimal getR11_or() {
		return r11_or;
	}

	public void setR11_or(BigDecimal r11_or) {
		this.r11_or = r11_or;
	}

	public BigDecimal getR11_net_short_position() {
		return r11_net_short_position;
	}

	public void setR11_net_short_position(BigDecimal r11_net_short_position) {
		this.r11_net_short_position = r11_net_short_position;
	}

	public BigDecimal getR12_net_spot_position() {
		return r12_net_spot_position;
	}

	public void setR12_net_spot_position(BigDecimal r12_net_spot_position) {
		this.r12_net_spot_position = r12_net_spot_position;
	}

	public BigDecimal getR12_net_forward_position() {
		return r12_net_forward_position;
	}

	public void setR12_net_forward_position(BigDecimal r12_net_forward_position) {
		this.r12_net_forward_position = r12_net_forward_position;
	}

	public BigDecimal getR12_guarantees() {
		return r12_guarantees;
	}

	public void setR12_guarantees(BigDecimal r12_guarantees) {
		this.r12_guarantees = r12_guarantees;
	}

	public BigDecimal getR12_net_future_inc_or_exp() {
		return r12_net_future_inc_or_exp;
	}

	public void setR12_net_future_inc_or_exp(BigDecimal r12_net_future_inc_or_exp) {
		this.r12_net_future_inc_or_exp = r12_net_future_inc_or_exp;
	}

	public BigDecimal getR12_net_delta_wei_fx_opt_posi() {
		return r12_net_delta_wei_fx_opt_posi;
	}

	public void setR12_net_delta_wei_fx_opt_posi(BigDecimal r12_net_delta_wei_fx_opt_posi) {
		this.r12_net_delta_wei_fx_opt_posi = r12_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR12_other_items() {
		return r12_other_items;
	}

	public void setR12_other_items(BigDecimal r12_other_items) {
		this.r12_other_items = r12_other_items;
	}

	public BigDecimal getR12_net_long_position() {
		return r12_net_long_position;
	}

	public void setR12_net_long_position(BigDecimal r12_net_long_position) {
		this.r12_net_long_position = r12_net_long_position;
	}

	public BigDecimal getR12_or() {
		return r12_or;
	}

	public void setR12_or(BigDecimal r12_or) {
		this.r12_or = r12_or;
	}

	public BigDecimal getR12_net_short_position() {
		return r12_net_short_position;
	}

	public void setR12_net_short_position(BigDecimal r12_net_short_position) {
		this.r12_net_short_position = r12_net_short_position;
	}

	public BigDecimal getR13_net_spot_position() {
		return r13_net_spot_position;
	}

	public void setR13_net_spot_position(BigDecimal r13_net_spot_position) {
		this.r13_net_spot_position = r13_net_spot_position;
	}

	public BigDecimal getR13_net_forward_position() {
		return r13_net_forward_position;
	}

	public void setR13_net_forward_position(BigDecimal r13_net_forward_position) {
		this.r13_net_forward_position = r13_net_forward_position;
	}

	public BigDecimal getR13_guarantees() {
		return r13_guarantees;
	}

	public void setR13_guarantees(BigDecimal r13_guarantees) {
		this.r13_guarantees = r13_guarantees;
	}

	public BigDecimal getR13_net_future_inc_or_exp() {
		return r13_net_future_inc_or_exp;
	}

	public void setR13_net_future_inc_or_exp(BigDecimal r13_net_future_inc_or_exp) {
		this.r13_net_future_inc_or_exp = r13_net_future_inc_or_exp;
	}

	public BigDecimal getR13_net_delta_wei_fx_opt_posi() {
		return r13_net_delta_wei_fx_opt_posi;
	}

	public void setR13_net_delta_wei_fx_opt_posi(BigDecimal r13_net_delta_wei_fx_opt_posi) {
		this.r13_net_delta_wei_fx_opt_posi = r13_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR13_other_items() {
		return r13_other_items;
	}

	public void setR13_other_items(BigDecimal r13_other_items) {
		this.r13_other_items = r13_other_items;
	}

	public BigDecimal getR13_net_long_position() {
		return r13_net_long_position;
	}

	public void setR13_net_long_position(BigDecimal r13_net_long_position) {
		this.r13_net_long_position = r13_net_long_position;
	}

	public BigDecimal getR13_or() {
		return r13_or;
	}

	public void setR13_or(BigDecimal r13_or) {
		this.r13_or = r13_or;
	}

	public BigDecimal getR13_net_short_position() {
		return r13_net_short_position;
	}

	public void setR13_net_short_position(BigDecimal r13_net_short_position) {
		this.r13_net_short_position = r13_net_short_position;
	}

	public BigDecimal getR14_net_spot_position() {
		return r14_net_spot_position;
	}

	public void setR14_net_spot_position(BigDecimal r14_net_spot_position) {
		this.r14_net_spot_position = r14_net_spot_position;
	}

	public BigDecimal getR14_net_forward_position() {
		return r14_net_forward_position;
	}

	public void setR14_net_forward_position(BigDecimal r14_net_forward_position) {
		this.r14_net_forward_position = r14_net_forward_position;
	}

	public BigDecimal getR14_guarantees() {
		return r14_guarantees;
	}

	public void setR14_guarantees(BigDecimal r14_guarantees) {
		this.r14_guarantees = r14_guarantees;
	}

	public BigDecimal getR14_net_future_inc_or_exp() {
		return r14_net_future_inc_or_exp;
	}

	public void setR14_net_future_inc_or_exp(BigDecimal r14_net_future_inc_or_exp) {
		this.r14_net_future_inc_or_exp = r14_net_future_inc_or_exp;
	}

	public BigDecimal getR14_net_delta_wei_fx_opt_posi() {
		return r14_net_delta_wei_fx_opt_posi;
	}

	public void setR14_net_delta_wei_fx_opt_posi(BigDecimal r14_net_delta_wei_fx_opt_posi) {
		this.r14_net_delta_wei_fx_opt_posi = r14_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR14_other_items() {
		return r14_other_items;
	}

	public void setR14_other_items(BigDecimal r14_other_items) {
		this.r14_other_items = r14_other_items;
	}

	public BigDecimal getR14_net_long_position() {
		return r14_net_long_position;
	}

	public void setR14_net_long_position(BigDecimal r14_net_long_position) {
		this.r14_net_long_position = r14_net_long_position;
	}

	public BigDecimal getR14_or() {
		return r14_or;
	}

	public void setR14_or(BigDecimal r14_or) {
		this.r14_or = r14_or;
	}

	public BigDecimal getR14_net_short_position() {
		return r14_net_short_position;
	}

	public void setR14_net_short_position(BigDecimal r14_net_short_position) {
		this.r14_net_short_position = r14_net_short_position;
	}

	public BigDecimal getR15_net_spot_position() {
		return r15_net_spot_position;
	}

	public void setR15_net_spot_position(BigDecimal r15_net_spot_position) {
		this.r15_net_spot_position = r15_net_spot_position;
	}

	public BigDecimal getR15_net_forward_position() {
		return r15_net_forward_position;
	}

	public void setR15_net_forward_position(BigDecimal r15_net_forward_position) {
		this.r15_net_forward_position = r15_net_forward_position;
	}

	public BigDecimal getR15_guarantees() {
		return r15_guarantees;
	}

	public void setR15_guarantees(BigDecimal r15_guarantees) {
		this.r15_guarantees = r15_guarantees;
	}

	public BigDecimal getR15_net_future_inc_or_exp() {
		return r15_net_future_inc_or_exp;
	}

	public void setR15_net_future_inc_or_exp(BigDecimal r15_net_future_inc_or_exp) {
		this.r15_net_future_inc_or_exp = r15_net_future_inc_or_exp;
	}

	public BigDecimal getR15_net_delta_wei_fx_opt_posi() {
		return r15_net_delta_wei_fx_opt_posi;
	}

	public void setR15_net_delta_wei_fx_opt_posi(BigDecimal r15_net_delta_wei_fx_opt_posi) {
		this.r15_net_delta_wei_fx_opt_posi = r15_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR15_other_items() {
		return r15_other_items;
	}

	public void setR15_other_items(BigDecimal r15_other_items) {
		this.r15_other_items = r15_other_items;
	}

	public BigDecimal getR15_net_long_position() {
		return r15_net_long_position;
	}

	public void setR15_net_long_position(BigDecimal r15_net_long_position) {
		this.r15_net_long_position = r15_net_long_position;
	}

	public BigDecimal getR15_or() {
		return r15_or;
	}

	public void setR15_or(BigDecimal r15_or) {
		this.r15_or = r15_or;
	}

	public BigDecimal getR15_net_short_position() {
		return r15_net_short_position;
	}

	public void setR15_net_short_position(BigDecimal r15_net_short_position) {
		this.r15_net_short_position = r15_net_short_position;
	}

	public BigDecimal getR16_net_spot_position() {
		return r16_net_spot_position;
	}

	public void setR16_net_spot_position(BigDecimal r16_net_spot_position) {
		this.r16_net_spot_position = r16_net_spot_position;
	}

	public BigDecimal getR16_net_forward_position() {
		return r16_net_forward_position;
	}

	public void setR16_net_forward_position(BigDecimal r16_net_forward_position) {
		this.r16_net_forward_position = r16_net_forward_position;
	}

	public BigDecimal getR16_guarantees() {
		return r16_guarantees;
	}

	public void setR16_guarantees(BigDecimal r16_guarantees) {
		this.r16_guarantees = r16_guarantees;
	}

	public BigDecimal getR16_net_future_inc_or_exp() {
		return r16_net_future_inc_or_exp;
	}

	public void setR16_net_future_inc_or_exp(BigDecimal r16_net_future_inc_or_exp) {
		this.r16_net_future_inc_or_exp = r16_net_future_inc_or_exp;
	}

	public BigDecimal getR16_net_delta_wei_fx_opt_posi() {
		return r16_net_delta_wei_fx_opt_posi;
	}

	public void setR16_net_delta_wei_fx_opt_posi(BigDecimal r16_net_delta_wei_fx_opt_posi) {
		this.r16_net_delta_wei_fx_opt_posi = r16_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR16_other_items() {
		return r16_other_items;
	}

	public void setR16_other_items(BigDecimal r16_other_items) {
		this.r16_other_items = r16_other_items;
	}

	public BigDecimal getR16_net_long_position() {
		return r16_net_long_position;
	}

	public void setR16_net_long_position(BigDecimal r16_net_long_position) {
		this.r16_net_long_position = r16_net_long_position;
	}

	public BigDecimal getR16_or() {
		return r16_or;
	}

	public void setR16_or(BigDecimal r16_or) {
		this.r16_or = r16_or;
	}

	public BigDecimal getR16_net_short_position() {
		return r16_net_short_position;
	}

	public void setR16_net_short_position(BigDecimal r16_net_short_position) {
		this.r16_net_short_position = r16_net_short_position;
	}

	public BigDecimal getR17_net_long_position() {
		return r17_net_long_position;
	}

	public void setR17_net_long_position(BigDecimal r17_net_long_position) {
		this.r17_net_long_position = r17_net_long_position;
	}

	public BigDecimal getR17_or() {
		return r17_or;
	}

	public void setR17_or(BigDecimal r17_or) {
		this.r17_or = r17_or;
	}

	public BigDecimal getR17_net_short_position() {
		return r17_net_short_position;
	}

	public void setR17_net_short_position(BigDecimal r17_net_short_position) {
		this.r17_net_short_position = r17_net_short_position;
	}

	public BigDecimal getR21_long() {
		return r21_long;
	}

	public void setR21_long(BigDecimal r21_long) {
		this.r21_long = r21_long;
	}

	public BigDecimal getR21_short() {
		return r21_short;
	}

	public void setR21_short(BigDecimal r21_short) {
		this.r21_short = r21_short;
	}

	public BigDecimal getR21_total_gross_long_short() {
		return r21_total_gross_long_short;
	}

	public void setR21_total_gross_long_short(BigDecimal r21_total_gross_long_short) {
		this.r21_total_gross_long_short = r21_total_gross_long_short;
	}

	public BigDecimal getR21_net_position() {
		return r21_net_position;
	}

	public void setR21_net_position(BigDecimal r21_net_position) {
		this.r21_net_position = r21_net_position;
	}

	public BigDecimal getR22_long() {
		return r22_long;
	}

	public void setR22_long(BigDecimal r22_long) {
		this.r22_long = r22_long;
	}

	public BigDecimal getR22_short() {
		return r22_short;
	}

	public void setR22_short(BigDecimal r22_short) {
		this.r22_short = r22_short;
	}

	public BigDecimal getR22_total_gross_long_short() {
		return r22_total_gross_long_short;
	}

	public void setR22_total_gross_long_short(BigDecimal r22_total_gross_long_short) {
		this.r22_total_gross_long_short = r22_total_gross_long_short;
	}

	public BigDecimal getR22_net_position() {
		return r22_net_position;
	}

	public void setR22_net_position(BigDecimal r22_net_position) {
		this.r22_net_position = r22_net_position;
	}

	public BigDecimal getR23_net_position() {
		return r23_net_position;
	}

	public void setR23_net_position(BigDecimal r23_net_position) {
		this.r23_net_position = r23_net_position;
	}

	public BigDecimal getR29_greater_net_long_or_short() {
		return r29_greater_net_long_or_short;
	}

	public void setR29_greater_net_long_or_short(BigDecimal r29_greater_net_long_or_short) {
		this.r29_greater_net_long_or_short = r29_greater_net_long_or_short;
	}

	public BigDecimal getR29_abs_value_net_gold_posi() {
		return r29_abs_value_net_gold_posi;
	}

	public void setR29_abs_value_net_gold_posi(BigDecimal r29_abs_value_net_gold_posi) {
		this.r29_abs_value_net_gold_posi = r29_abs_value_net_gold_posi;
	}

	public BigDecimal getR29_capital_charge() {
		return r29_capital_charge;
	}

	public void setR29_capital_charge(BigDecimal r29_capital_charge) {
		this.r29_capital_charge = r29_capital_charge;
	}

	public BigDecimal getR30_capital_require() {
		return r30_capital_require;
	}

	public void setR30_capital_require(BigDecimal r30_capital_require) {
		this.r30_capital_require = r30_capital_require;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}



	public BigDecimal getReport_version() {
		return report_version;
	}

	public void setReport_version(BigDecimal report_version) {
		this.report_version = report_version;
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

	public M_FXR_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

}



	// ------------------------------
	// Entity representing the detail table for the FXR report
	// ------------------------------
public static class M_FXR_Detail_Entity {

	 // ===== PRIMARY KEY =====

    @Id
    @Column(name = "REPORT_DATE")
    @Temporal(TemporalType.DATE)
    private Date reportDate;

    // ===== R11 =====

    @Column(name = "R11_NET_SPOT_POSITION")
    private BigDecimal r11_net_spot_position;

    @Column(name = "R11_NET_FORWARD_POSITION")
    private BigDecimal r11_net_forward_position;

    @Column(name = "R11_GUARANTEES")
    private BigDecimal r11_guarantees;

    @Column(name = "R11_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r11_net_future_inc_or_exp;

    @Column(name = "R11_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r11_net_delta_wei_fx_opt_posi;

    @Column(name = "R11_OTHER_ITEMS")
    private BigDecimal r11_other_items;

    @Column(name = "R11_NET_LONG_POSITION")
    private BigDecimal r11_net_long_position;

    @Column(name = "R11_OR")
    private BigDecimal r11_or;

    @Column(name = "R11_NET_SHORT_POSITION")
    private BigDecimal r11_net_short_position;

    // ===== R12 =====

    @Column(name = "R12_NET_SPOT_POSITION")
    private BigDecimal r12_net_spot_position;

    @Column(name = "R12_NET_FORWARD_POSITION")
    private BigDecimal r12_net_forward_position;

    @Column(name = "R12_GUARANTEES")
    private BigDecimal r12_guarantees;

    @Column(name = "R12_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r12_net_future_inc_or_exp;

    @Column(name = "R12_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r12_net_delta_wei_fx_opt_posi;

    @Column(name = "R12_OTHER_ITEMS")
    private BigDecimal r12_other_items;

    @Column(name = "R12_NET_LONG_POSITION")
    private BigDecimal r12_net_long_position;

    @Column(name = "R12_OR")
    private BigDecimal r12_or;

    @Column(name = "R12_NET_SHORT_POSITION")
    private BigDecimal r12_net_short_position;

    // ===== R13 =====

    @Column(name = "R13_NET_SPOT_POSITION")
    private BigDecimal r13_net_spot_position;

    @Column(name = "R13_NET_FORWARD_POSITION")
    private BigDecimal r13_net_forward_position;

    @Column(name = "R13_GUARANTEES")
    private BigDecimal r13_guarantees;

    @Column(name = "R13_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r13_net_future_inc_or_exp;

    @Column(name = "R13_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r13_net_delta_wei_fx_opt_posi;

    @Column(name = "R13_OTHER_ITEMS")
    private BigDecimal r13_other_items;

    @Column(name = "R13_NET_LONG_POSITION")
    private BigDecimal r13_net_long_position;

    @Column(name = "R13_OR")
    private BigDecimal r13_or;

    @Column(name = "R13_NET_SHORT_POSITION")
    private BigDecimal r13_net_short_position;

    // ===== R14 =====

    @Column(name = "R14_NET_SPOT_POSITION")
    private BigDecimal r14_net_spot_position;

    @Column(name = "R14_NET_FORWARD_POSITION")
    private BigDecimal r14_net_forward_position;

    @Column(name = "R14_GUARANTEES")
    private BigDecimal r14_guarantees;

    @Column(name = "R14_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r14_net_future_inc_or_exp;

    @Column(name = "R14_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r14_net_delta_wei_fx_opt_posi;

    @Column(name = "R14_OTHER_ITEMS")
    private BigDecimal r14_other_items;

    @Column(name = "R14_NET_LONG_POSITION")
    private BigDecimal r14_net_long_position;

    @Column(name = "R14_OR")
    private BigDecimal r14_or;

    @Column(name = "R14_NET_SHORT_POSITION")
    private BigDecimal r14_net_short_position;

    // ===== R15 =====

    @Column(name = "R15_NET_SPOT_POSITION")
    private BigDecimal r15_net_spot_position;

    @Column(name = "R15_NET_FORWARD_POSITION")
    private BigDecimal r15_net_forward_position;

    @Column(name = "R15_GUARANTEES")
    private BigDecimal r15_guarantees;

    @Column(name = "R15_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r15_net_future_inc_or_exp;

    @Column(name = "R15_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r15_net_delta_wei_fx_opt_posi;

    @Column(name = "R15_OTHER_ITEMS")
    private BigDecimal r15_other_items;

    @Column(name = "R15_NET_LONG_POSITION")
    private BigDecimal r15_net_long_position;

    @Column(name = "R15_OR")
    private BigDecimal r15_or;

    @Column(name = "R15_NET_SHORT_POSITION")
    private BigDecimal r15_net_short_position;

    // ===== R16 =====

    @Column(name = "R16_NET_SPOT_POSITION")
    private BigDecimal r16_net_spot_position;

    @Column(name = "R16_NET_FORWARD_POSITION")
    private BigDecimal r16_net_forward_position;

    @Column(name = "R16_GUARANTEES")
    private BigDecimal r16_guarantees;

    @Column(name = "R16_NET_FUTURE_INC_OR_EXP")
    private BigDecimal r16_net_future_inc_or_exp;

    @Column(name = "R16_NET_DELTA_WEI_FX_OPT_POSI")
    private BigDecimal r16_net_delta_wei_fx_opt_posi;

    @Column(name = "R16_OTHER_ITEMS")
    private BigDecimal r16_other_items;

    @Column(name = "R16_NET_LONG_POSITION")
    private BigDecimal r16_net_long_position;

    @Column(name = "R16_OR")
    private BigDecimal r16_or;

    @Column(name = "R16_NET_SHORT_POSITION")
    private BigDecimal r16_net_short_position;

    // ===== R17 =====

    @Column(name = "R17_NET_LONG_POSITION")
    private BigDecimal r17_net_long_position;

    @Column(name = "R17_OR")
    private BigDecimal r17_or;

    @Column(name = "R17_NET_SHORT_POSITION")
    private BigDecimal r17_net_short_position;

    // ===== R21 =====

    @Column(name = "R21_LONG")
    private BigDecimal r21_long;

    @Column(name = "R21_SHORT")
    private BigDecimal r21_short;

    @Column(name = "R21_TOTAL_GROSS_LONG_SHORT")
    private BigDecimal r21_total_gross_long_short;

    @Column(name = "R21_NET_POSITION")
    private BigDecimal r21_net_position;

    // ===== R22 =====

    @Column(name = "R22_LONG")
    private BigDecimal r22_long;

    @Column(name = "R22_SHORT")
    private BigDecimal r22_short;

    @Column(name = "R22_TOTAL_GROSS_LONG_SHORT")
    private BigDecimal r22_total_gross_long_short;

    @Column(name = "R22_NET_POSITION")
    private BigDecimal r22_net_position;

    // ===== R23 =====

    @Column(name = "R23_NET_POSITION")
    private BigDecimal r23_net_position;

    // ===== R29 & R30 =====

    @Column(name = "R29_GREATER_NET_LONG_OR_SHORT")
    private BigDecimal r29_greater_net_long_or_short;

    @Column(name = "R29_ABS_VALUE_NET_GOLD_POSI")
    private BigDecimal r29_abs_value_net_gold_posi;

    @Column(name = "R29_CAPITAL_CHARGE")
    private BigDecimal r29_capital_charge;

    @Column(name = "R30_CAPITAL_REQUIRE")
    private BigDecimal r30_capital_require;

    // ===== COMMON FIELDS =====

    @Column(name = "REPORT_VERSION")
    private BigDecimal report_version;

    @Column(name = "REPORT_FREQUENCY")
    private String report_frequency;

    @Column(name = "REPORT_CODE")
    private String report_code;

    @Column(name = "REPORT_DESC")
    private String report_desc;

    @Column(name = "ENTITY_FLG")
    private String entity_flg;

    @Column(name = "MODIFY_FLG")
    private String modify_flg;

    @Column(name = "DEL_FLG")
    private String del_flg;

	public BigDecimal getR11_net_spot_position() {
		return r11_net_spot_position;
	}

	public void setR11_net_spot_position(BigDecimal r11_net_spot_position) {
		this.r11_net_spot_position = r11_net_spot_position;
	}

	public BigDecimal getR11_net_forward_position() {
		return r11_net_forward_position;
	}

	public void setR11_net_forward_position(BigDecimal r11_net_forward_position) {
		this.r11_net_forward_position = r11_net_forward_position;
	}

	public BigDecimal getR11_guarantees() {
		return r11_guarantees;
	}

	public void setR11_guarantees(BigDecimal r11_guarantees) {
		this.r11_guarantees = r11_guarantees;
	}

	public BigDecimal getR11_net_future_inc_or_exp() {
		return r11_net_future_inc_or_exp;
	}

	public void setR11_net_future_inc_or_exp(BigDecimal r11_net_future_inc_or_exp) {
		this.r11_net_future_inc_or_exp = r11_net_future_inc_or_exp;
	}

	public BigDecimal getR11_net_delta_wei_fx_opt_posi() {
		return r11_net_delta_wei_fx_opt_posi;
	}

	public void setR11_net_delta_wei_fx_opt_posi(BigDecimal r11_net_delta_wei_fx_opt_posi) {
		this.r11_net_delta_wei_fx_opt_posi = r11_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR11_other_items() {
		return r11_other_items;
	}

	public void setR11_other_items(BigDecimal r11_other_items) {
		this.r11_other_items = r11_other_items;
	}

	public BigDecimal getR11_net_long_position() {
		return r11_net_long_position;
	}

	public void setR11_net_long_position(BigDecimal r11_net_long_position) {
		this.r11_net_long_position = r11_net_long_position;
	}

	public BigDecimal getR11_or() {
		return r11_or;
	}

	public void setR11_or(BigDecimal r11_or) {
		this.r11_or = r11_or;
	}

	public BigDecimal getR11_net_short_position() {
		return r11_net_short_position;
	}

	public void setR11_net_short_position(BigDecimal r11_net_short_position) {
		this.r11_net_short_position = r11_net_short_position;
	}

	public BigDecimal getR12_net_spot_position() {
		return r12_net_spot_position;
	}

	public void setR12_net_spot_position(BigDecimal r12_net_spot_position) {
		this.r12_net_spot_position = r12_net_spot_position;
	}

	public BigDecimal getR12_net_forward_position() {
		return r12_net_forward_position;
	}

	public void setR12_net_forward_position(BigDecimal r12_net_forward_position) {
		this.r12_net_forward_position = r12_net_forward_position;
	}

	public BigDecimal getR12_guarantees() {
		return r12_guarantees;
	}

	public void setR12_guarantees(BigDecimal r12_guarantees) {
		this.r12_guarantees = r12_guarantees;
	}

	public BigDecimal getR12_net_future_inc_or_exp() {
		return r12_net_future_inc_or_exp;
	}

	public void setR12_net_future_inc_or_exp(BigDecimal r12_net_future_inc_or_exp) {
		this.r12_net_future_inc_or_exp = r12_net_future_inc_or_exp;
	}

	public BigDecimal getR12_net_delta_wei_fx_opt_posi() {
		return r12_net_delta_wei_fx_opt_posi;
	}

	public void setR12_net_delta_wei_fx_opt_posi(BigDecimal r12_net_delta_wei_fx_opt_posi) {
		this.r12_net_delta_wei_fx_opt_posi = r12_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR12_other_items() {
		return r12_other_items;
	}

	public void setR12_other_items(BigDecimal r12_other_items) {
		this.r12_other_items = r12_other_items;
	}

	public BigDecimal getR12_net_long_position() {
		return r12_net_long_position;
	}

	public void setR12_net_long_position(BigDecimal r12_net_long_position) {
		this.r12_net_long_position = r12_net_long_position;
	}

	public BigDecimal getR12_or() {
		return r12_or;
	}

	public void setR12_or(BigDecimal r12_or) {
		this.r12_or = r12_or;
	}

	public BigDecimal getR12_net_short_position() {
		return r12_net_short_position;
	}

	public void setR12_net_short_position(BigDecimal r12_net_short_position) {
		this.r12_net_short_position = r12_net_short_position;
	}

	public BigDecimal getR13_net_spot_position() {
		return r13_net_spot_position;
	}

	public void setR13_net_spot_position(BigDecimal r13_net_spot_position) {
		this.r13_net_spot_position = r13_net_spot_position;
	}

	public BigDecimal getR13_net_forward_position() {
		return r13_net_forward_position;
	}

	public void setR13_net_forward_position(BigDecimal r13_net_forward_position) {
		this.r13_net_forward_position = r13_net_forward_position;
	}

	public BigDecimal getR13_guarantees() {
		return r13_guarantees;
	}

	public void setR13_guarantees(BigDecimal r13_guarantees) {
		this.r13_guarantees = r13_guarantees;
	}

	public BigDecimal getR13_net_future_inc_or_exp() {
		return r13_net_future_inc_or_exp;
	}

	public void setR13_net_future_inc_or_exp(BigDecimal r13_net_future_inc_or_exp) {
		this.r13_net_future_inc_or_exp = r13_net_future_inc_or_exp;
	}

	public BigDecimal getR13_net_delta_wei_fx_opt_posi() {
		return r13_net_delta_wei_fx_opt_posi;
	}

	public void setR13_net_delta_wei_fx_opt_posi(BigDecimal r13_net_delta_wei_fx_opt_posi) {
		this.r13_net_delta_wei_fx_opt_posi = r13_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR13_other_items() {
		return r13_other_items;
	}

	public void setR13_other_items(BigDecimal r13_other_items) {
		this.r13_other_items = r13_other_items;
	}

	public BigDecimal getR13_net_long_position() {
		return r13_net_long_position;
	}

	public void setR13_net_long_position(BigDecimal r13_net_long_position) {
		this.r13_net_long_position = r13_net_long_position;
	}

	public BigDecimal getR13_or() {
		return r13_or;
	}

	public void setR13_or(BigDecimal r13_or) {
		this.r13_or = r13_or;
	}

	public BigDecimal getR13_net_short_position() {
		return r13_net_short_position;
	}

	public void setR13_net_short_position(BigDecimal r13_net_short_position) {
		this.r13_net_short_position = r13_net_short_position;
	}

	public BigDecimal getR14_net_spot_position() {
		return r14_net_spot_position;
	}

	public void setR14_net_spot_position(BigDecimal r14_net_spot_position) {
		this.r14_net_spot_position = r14_net_spot_position;
	}

	public BigDecimal getR14_net_forward_position() {
		return r14_net_forward_position;
	}

	public void setR14_net_forward_position(BigDecimal r14_net_forward_position) {
		this.r14_net_forward_position = r14_net_forward_position;
	}

	public BigDecimal getR14_guarantees() {
		return r14_guarantees;
	}

	public void setR14_guarantees(BigDecimal r14_guarantees) {
		this.r14_guarantees = r14_guarantees;
	}

	public BigDecimal getR14_net_future_inc_or_exp() {
		return r14_net_future_inc_or_exp;
	}

	public void setR14_net_future_inc_or_exp(BigDecimal r14_net_future_inc_or_exp) {
		this.r14_net_future_inc_or_exp = r14_net_future_inc_or_exp;
	}

	public BigDecimal getR14_net_delta_wei_fx_opt_posi() {
		return r14_net_delta_wei_fx_opt_posi;
	}

	public void setR14_net_delta_wei_fx_opt_posi(BigDecimal r14_net_delta_wei_fx_opt_posi) {
		this.r14_net_delta_wei_fx_opt_posi = r14_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR14_other_items() {
		return r14_other_items;
	}

	public void setR14_other_items(BigDecimal r14_other_items) {
		this.r14_other_items = r14_other_items;
	}

	public BigDecimal getR14_net_long_position() {
		return r14_net_long_position;
	}

	public void setR14_net_long_position(BigDecimal r14_net_long_position) {
		this.r14_net_long_position = r14_net_long_position;
	}

	public BigDecimal getR14_or() {
		return r14_or;
	}

	public void setR14_or(BigDecimal r14_or) {
		this.r14_or = r14_or;
	}

	public BigDecimal getR14_net_short_position() {
		return r14_net_short_position;
	}

	public void setR14_net_short_position(BigDecimal r14_net_short_position) {
		this.r14_net_short_position = r14_net_short_position;
	}

	public BigDecimal getR15_net_spot_position() {
		return r15_net_spot_position;
	}

	public void setR15_net_spot_position(BigDecimal r15_net_spot_position) {
		this.r15_net_spot_position = r15_net_spot_position;
	}

	public BigDecimal getR15_net_forward_position() {
		return r15_net_forward_position;
	}

	public void setR15_net_forward_position(BigDecimal r15_net_forward_position) {
		this.r15_net_forward_position = r15_net_forward_position;
	}

	public BigDecimal getR15_guarantees() {
		return r15_guarantees;
	}

	public void setR15_guarantees(BigDecimal r15_guarantees) {
		this.r15_guarantees = r15_guarantees;
	}

	public BigDecimal getR15_net_future_inc_or_exp() {
		return r15_net_future_inc_or_exp;
	}

	public void setR15_net_future_inc_or_exp(BigDecimal r15_net_future_inc_or_exp) {
		this.r15_net_future_inc_or_exp = r15_net_future_inc_or_exp;
	}

	public BigDecimal getR15_net_delta_wei_fx_opt_posi() {
		return r15_net_delta_wei_fx_opt_posi;
	}

	public void setR15_net_delta_wei_fx_opt_posi(BigDecimal r15_net_delta_wei_fx_opt_posi) {
		this.r15_net_delta_wei_fx_opt_posi = r15_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR15_other_items() {
		return r15_other_items;
	}

	public void setR15_other_items(BigDecimal r15_other_items) {
		this.r15_other_items = r15_other_items;
	}

	public BigDecimal getR15_net_long_position() {
		return r15_net_long_position;
	}

	public void setR15_net_long_position(BigDecimal r15_net_long_position) {
		this.r15_net_long_position = r15_net_long_position;
	}

	public BigDecimal getR15_or() {
		return r15_or;
	}

	public void setR15_or(BigDecimal r15_or) {
		this.r15_or = r15_or;
	}

	public BigDecimal getR15_net_short_position() {
		return r15_net_short_position;
	}

	public void setR15_net_short_position(BigDecimal r15_net_short_position) {
		this.r15_net_short_position = r15_net_short_position;
	}

	public BigDecimal getR16_net_spot_position() {
		return r16_net_spot_position;
	}

	public void setR16_net_spot_position(BigDecimal r16_net_spot_position) {
		this.r16_net_spot_position = r16_net_spot_position;
	}

	public BigDecimal getR16_net_forward_position() {
		return r16_net_forward_position;
	}

	public void setR16_net_forward_position(BigDecimal r16_net_forward_position) {
		this.r16_net_forward_position = r16_net_forward_position;
	}

	public BigDecimal getR16_guarantees() {
		return r16_guarantees;
	}

	public void setR16_guarantees(BigDecimal r16_guarantees) {
		this.r16_guarantees = r16_guarantees;
	}

	public BigDecimal getR16_net_future_inc_or_exp() {
		return r16_net_future_inc_or_exp;
	}

	public void setR16_net_future_inc_or_exp(BigDecimal r16_net_future_inc_or_exp) {
		this.r16_net_future_inc_or_exp = r16_net_future_inc_or_exp;
	}

	public BigDecimal getR16_net_delta_wei_fx_opt_posi() {
		return r16_net_delta_wei_fx_opt_posi;
	}

	public void setR16_net_delta_wei_fx_opt_posi(BigDecimal r16_net_delta_wei_fx_opt_posi) {
		this.r16_net_delta_wei_fx_opt_posi = r16_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR16_other_items() {
		return r16_other_items;
	}

	public void setR16_other_items(BigDecimal r16_other_items) {
		this.r16_other_items = r16_other_items;
	}

	public BigDecimal getR16_net_long_position() {
		return r16_net_long_position;
	}

	public void setR16_net_long_position(BigDecimal r16_net_long_position) {
		this.r16_net_long_position = r16_net_long_position;
	}

	public BigDecimal getR16_or() {
		return r16_or;
	}

	public void setR16_or(BigDecimal r16_or) {
		this.r16_or = r16_or;
	}

	public BigDecimal getR16_net_short_position() {
		return r16_net_short_position;
	}

	public void setR16_net_short_position(BigDecimal r16_net_short_position) {
		this.r16_net_short_position = r16_net_short_position;
	}

	public BigDecimal getR17_net_long_position() {
		return r17_net_long_position;
	}

	public void setR17_net_long_position(BigDecimal r17_net_long_position) {
		this.r17_net_long_position = r17_net_long_position;
	}

	public BigDecimal getR17_or() {
		return r17_or;
	}

	public void setR17_or(BigDecimal r17_or) {
		this.r17_or = r17_or;
	}

	public BigDecimal getR17_net_short_position() {
		return r17_net_short_position;
	}

	public void setR17_net_short_position(BigDecimal r17_net_short_position) {
		this.r17_net_short_position = r17_net_short_position;
	}

	public BigDecimal getR21_long() {
		return r21_long;
	}

	public void setR21_long(BigDecimal r21_long) {
		this.r21_long = r21_long;
	}

	public BigDecimal getR21_short() {
		return r21_short;
	}

	public void setR21_short(BigDecimal r21_short) {
		this.r21_short = r21_short;
	}

	public BigDecimal getR21_total_gross_long_short() {
		return r21_total_gross_long_short;
	}

	public void setR21_total_gross_long_short(BigDecimal r21_total_gross_long_short) {
		this.r21_total_gross_long_short = r21_total_gross_long_short;
	}

	public BigDecimal getR21_net_position() {
		return r21_net_position;
	}

	public void setR21_net_position(BigDecimal r21_net_position) {
		this.r21_net_position = r21_net_position;
	}

	public BigDecimal getR22_long() {
		return r22_long;
	}

	public void setR22_long(BigDecimal r22_long) {
		this.r22_long = r22_long;
	}

	public BigDecimal getR22_short() {
		return r22_short;
	}

	public void setR22_short(BigDecimal r22_short) {
		this.r22_short = r22_short;
	}

	public BigDecimal getR22_total_gross_long_short() {
		return r22_total_gross_long_short;
	}

	public void setR22_total_gross_long_short(BigDecimal r22_total_gross_long_short) {
		this.r22_total_gross_long_short = r22_total_gross_long_short;
	}

	public BigDecimal getR22_net_position() {
		return r22_net_position;
	}

	public void setR22_net_position(BigDecimal r22_net_position) {
		this.r22_net_position = r22_net_position;
	}

	public BigDecimal getR23_net_position() {
		return r23_net_position;
	}

	public void setR23_net_position(BigDecimal r23_net_position) {
		this.r23_net_position = r23_net_position;
	}

	public BigDecimal getR29_greater_net_long_or_short() {
		return r29_greater_net_long_or_short;
	}

	public void setR29_greater_net_long_or_short(BigDecimal r29_greater_net_long_or_short) {
		this.r29_greater_net_long_or_short = r29_greater_net_long_or_short;
	}

	public BigDecimal getR29_abs_value_net_gold_posi() {
		return r29_abs_value_net_gold_posi;
	}

	public void setR29_abs_value_net_gold_posi(BigDecimal r29_abs_value_net_gold_posi) {
		this.r29_abs_value_net_gold_posi = r29_abs_value_net_gold_posi;
	}

	public BigDecimal getR29_capital_charge() {
		return r29_capital_charge;
	}

	public void setR29_capital_charge(BigDecimal r29_capital_charge) {
		this.r29_capital_charge = r29_capital_charge;
	}

	public BigDecimal getR30_capital_require() {
		return r30_capital_require;
	}

	public void setR30_capital_require(BigDecimal r30_capital_require) {
		this.r30_capital_require = r30_capital_require;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}


	public BigDecimal getReport_version() {
		return report_version;
	}

	public void setReport_version(BigDecimal report_version) {
		this.report_version = report_version;
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

	public M_FXR_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

}



	// ------------------------------
	// Entity representing the archival summary table for the FXR report
	// ------------------------------
public static class M_FXR_Archival_Summary_Entity {

//	private String r11_currency;
	private BigDecimal r11_net_spot_position;
	private BigDecimal r11_net_forward_position;
	private BigDecimal r11_guarantees;
	private BigDecimal r11_net_future_inc_or_exp;
	private BigDecimal r11_net_delta_wei_fx_opt_posi;
	private BigDecimal r11_other_items;
	private BigDecimal r11_net_long_position;
	private BigDecimal r11_or;
	private BigDecimal r11_net_short_position;
//	private String r12_currency;
	private BigDecimal r12_net_spot_position;
	private BigDecimal r12_net_forward_position;
	private BigDecimal r12_guarantees;
	private BigDecimal r12_net_future_inc_or_exp;
	private BigDecimal r12_net_delta_wei_fx_opt_posi;
	private BigDecimal r12_other_items;
	private BigDecimal r12_net_long_position;
	private BigDecimal r12_or;
	private BigDecimal r12_net_short_position;
//	private String r13_currency;
	private BigDecimal r13_net_spot_position;
	private BigDecimal r13_net_forward_position;
	private BigDecimal r13_guarantees;
	private BigDecimal r13_net_future_inc_or_exp;
	private BigDecimal r13_net_delta_wei_fx_opt_posi;
	private BigDecimal r13_other_items;
	private BigDecimal r13_net_long_position;
	private BigDecimal r13_or;
	private BigDecimal r13_net_short_position;
//	private String r14_currency;
	private BigDecimal r14_net_spot_position;
	private BigDecimal r14_net_forward_position;
	private BigDecimal r14_guarantees;
	private BigDecimal r14_net_future_inc_or_exp;
	private BigDecimal r14_net_delta_wei_fx_opt_posi;
	private BigDecimal r14_other_items;
	private BigDecimal r14_net_long_position;
	private BigDecimal r14_or;
	private BigDecimal r14_net_short_position;
//	private String r15_currency;
	private BigDecimal r15_net_spot_position;
	private BigDecimal r15_net_forward_position;
	private BigDecimal r15_guarantees;
	private BigDecimal r15_net_future_inc_or_exp;
	private BigDecimal r15_net_delta_wei_fx_opt_posi;
	private BigDecimal r15_other_items;
	private BigDecimal r15_net_long_position;
	private BigDecimal r15_or;
	private BigDecimal r15_net_short_position;
//	private String r16_currency;
	private BigDecimal r16_net_spot_position;
	private BigDecimal r16_net_forward_position;
	private BigDecimal r16_guarantees;
	private BigDecimal r16_net_future_inc_or_exp;
	private BigDecimal r16_net_delta_wei_fx_opt_posi;
	private BigDecimal r16_other_items;
	private BigDecimal r16_net_long_position;
	private BigDecimal r16_or;
	private BigDecimal r16_net_short_position;

	private BigDecimal r17_net_long_position;
	private BigDecimal r17_or;
	private BigDecimal r17_net_short_position;

	private BigDecimal r21_long;
	private BigDecimal r21_short;
	private BigDecimal r21_total_gross_long_short;
	private BigDecimal r21_net_position;
	private BigDecimal r22_long;
	private BigDecimal r22_short;
	private BigDecimal r22_total_gross_long_short;
	private BigDecimal r22_net_position;

	private BigDecimal r23_net_position;

	private BigDecimal r29_greater_net_long_or_short;
	private BigDecimal r29_abs_value_net_gold_posi;
	// private BigDecimal r29_capital_require;
	private BigDecimal r29_capital_charge;
	private BigDecimal r30_capital_require;

@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;

	private String report_frequency;
	private String report_code;
	private String report_desc;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;

	public BigDecimal getR11_net_spot_position() {
		return r11_net_spot_position;
	}

	public void setR11_net_spot_position(BigDecimal r11_net_spot_position) {
		this.r11_net_spot_position = r11_net_spot_position;
	}

	public BigDecimal getR11_net_forward_position() {
		return r11_net_forward_position;
	}

	public void setR11_net_forward_position(BigDecimal r11_net_forward_position) {
		this.r11_net_forward_position = r11_net_forward_position;
	}

	public BigDecimal getR11_guarantees() {
		return r11_guarantees;
	}

	public void setR11_guarantees(BigDecimal r11_guarantees) {
		this.r11_guarantees = r11_guarantees;
	}

	public BigDecimal getR11_net_future_inc_or_exp() {
		return r11_net_future_inc_or_exp;
	}

	public void setR11_net_future_inc_or_exp(BigDecimal r11_net_future_inc_or_exp) {
		this.r11_net_future_inc_or_exp = r11_net_future_inc_or_exp;
	}

	public BigDecimal getR11_net_delta_wei_fx_opt_posi() {
		return r11_net_delta_wei_fx_opt_posi;
	}

	public void setR11_net_delta_wei_fx_opt_posi(BigDecimal r11_net_delta_wei_fx_opt_posi) {
		this.r11_net_delta_wei_fx_opt_posi = r11_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR11_other_items() {
		return r11_other_items;
	}

	public void setR11_other_items(BigDecimal r11_other_items) {
		this.r11_other_items = r11_other_items;
	}

	public BigDecimal getR11_net_long_position() {
		return r11_net_long_position;
	}

	public void setR11_net_long_position(BigDecimal r11_net_long_position) {
		this.r11_net_long_position = r11_net_long_position;
	}

	public BigDecimal getR11_or() {
		return r11_or;
	}

	public void setR11_or(BigDecimal r11_or) {
		this.r11_or = r11_or;
	}

	public BigDecimal getR11_net_short_position() {
		return r11_net_short_position;
	}

	public void setR11_net_short_position(BigDecimal r11_net_short_position) {
		this.r11_net_short_position = r11_net_short_position;
	}

	public BigDecimal getR12_net_spot_position() {
		return r12_net_spot_position;
	}

	public void setR12_net_spot_position(BigDecimal r12_net_spot_position) {
		this.r12_net_spot_position = r12_net_spot_position;
	}

	public BigDecimal getR12_net_forward_position() {
		return r12_net_forward_position;
	}

	public void setR12_net_forward_position(BigDecimal r12_net_forward_position) {
		this.r12_net_forward_position = r12_net_forward_position;
	}

	public BigDecimal getR12_guarantees() {
		return r12_guarantees;
	}

	public void setR12_guarantees(BigDecimal r12_guarantees) {
		this.r12_guarantees = r12_guarantees;
	}

	public BigDecimal getR12_net_future_inc_or_exp() {
		return r12_net_future_inc_or_exp;
	}

	public void setR12_net_future_inc_or_exp(BigDecimal r12_net_future_inc_or_exp) {
		this.r12_net_future_inc_or_exp = r12_net_future_inc_or_exp;
	}

	public BigDecimal getR12_net_delta_wei_fx_opt_posi() {
		return r12_net_delta_wei_fx_opt_posi;
	}

	public void setR12_net_delta_wei_fx_opt_posi(BigDecimal r12_net_delta_wei_fx_opt_posi) {
		this.r12_net_delta_wei_fx_opt_posi = r12_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR12_other_items() {
		return r12_other_items;
	}

	public void setR12_other_items(BigDecimal r12_other_items) {
		this.r12_other_items = r12_other_items;
	}

	public BigDecimal getR12_net_long_position() {
		return r12_net_long_position;
	}

	public void setR12_net_long_position(BigDecimal r12_net_long_position) {
		this.r12_net_long_position = r12_net_long_position;
	}

	public BigDecimal getR12_or() {
		return r12_or;
	}

	public void setR12_or(BigDecimal r12_or) {
		this.r12_or = r12_or;
	}

	public BigDecimal getR12_net_short_position() {
		return r12_net_short_position;
	}

	public void setR12_net_short_position(BigDecimal r12_net_short_position) {
		this.r12_net_short_position = r12_net_short_position;
	}

	public BigDecimal getR13_net_spot_position() {
		return r13_net_spot_position;
	}

	public void setR13_net_spot_position(BigDecimal r13_net_spot_position) {
		this.r13_net_spot_position = r13_net_spot_position;
	}

	public BigDecimal getR13_net_forward_position() {
		return r13_net_forward_position;
	}

	public void setR13_net_forward_position(BigDecimal r13_net_forward_position) {
		this.r13_net_forward_position = r13_net_forward_position;
	}

	public BigDecimal getR13_guarantees() {
		return r13_guarantees;
	}

	public void setR13_guarantees(BigDecimal r13_guarantees) {
		this.r13_guarantees = r13_guarantees;
	}

	public BigDecimal getR13_net_future_inc_or_exp() {
		return r13_net_future_inc_or_exp;
	}

	public void setR13_net_future_inc_or_exp(BigDecimal r13_net_future_inc_or_exp) {
		this.r13_net_future_inc_or_exp = r13_net_future_inc_or_exp;
	}

	public BigDecimal getR13_net_delta_wei_fx_opt_posi() {
		return r13_net_delta_wei_fx_opt_posi;
	}

	public void setR13_net_delta_wei_fx_opt_posi(BigDecimal r13_net_delta_wei_fx_opt_posi) {
		this.r13_net_delta_wei_fx_opt_posi = r13_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR13_other_items() {
		return r13_other_items;
	}

	public void setR13_other_items(BigDecimal r13_other_items) {
		this.r13_other_items = r13_other_items;
	}

	public BigDecimal getR13_net_long_position() {
		return r13_net_long_position;
	}

	public void setR13_net_long_position(BigDecimal r13_net_long_position) {
		this.r13_net_long_position = r13_net_long_position;
	}

	public BigDecimal getR13_or() {
		return r13_or;
	}

	public void setR13_or(BigDecimal r13_or) {
		this.r13_or = r13_or;
	}

	public BigDecimal getR13_net_short_position() {
		return r13_net_short_position;
	}

	public void setR13_net_short_position(BigDecimal r13_net_short_position) {
		this.r13_net_short_position = r13_net_short_position;
	}

	public BigDecimal getR14_net_spot_position() {
		return r14_net_spot_position;
	}

	public void setR14_net_spot_position(BigDecimal r14_net_spot_position) {
		this.r14_net_spot_position = r14_net_spot_position;
	}

	public BigDecimal getR14_net_forward_position() {
		return r14_net_forward_position;
	}

	public void setR14_net_forward_position(BigDecimal r14_net_forward_position) {
		this.r14_net_forward_position = r14_net_forward_position;
	}

	public BigDecimal getR14_guarantees() {
		return r14_guarantees;
	}

	public void setR14_guarantees(BigDecimal r14_guarantees) {
		this.r14_guarantees = r14_guarantees;
	}

	public BigDecimal getR14_net_future_inc_or_exp() {
		return r14_net_future_inc_or_exp;
	}

	public void setR14_net_future_inc_or_exp(BigDecimal r14_net_future_inc_or_exp) {
		this.r14_net_future_inc_or_exp = r14_net_future_inc_or_exp;
	}

	public BigDecimal getR14_net_delta_wei_fx_opt_posi() {
		return r14_net_delta_wei_fx_opt_posi;
	}

	public void setR14_net_delta_wei_fx_opt_posi(BigDecimal r14_net_delta_wei_fx_opt_posi) {
		this.r14_net_delta_wei_fx_opt_posi = r14_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR14_other_items() {
		return r14_other_items;
	}

	public void setR14_other_items(BigDecimal r14_other_items) {
		this.r14_other_items = r14_other_items;
	}

	public BigDecimal getR14_net_long_position() {
		return r14_net_long_position;
	}

	public void setR14_net_long_position(BigDecimal r14_net_long_position) {
		this.r14_net_long_position = r14_net_long_position;
	}

	public BigDecimal getR14_or() {
		return r14_or;
	}

	public void setR14_or(BigDecimal r14_or) {
		this.r14_or = r14_or;
	}

	public BigDecimal getR14_net_short_position() {
		return r14_net_short_position;
	}

	public void setR14_net_short_position(BigDecimal r14_net_short_position) {
		this.r14_net_short_position = r14_net_short_position;
	}

	public BigDecimal getR15_net_spot_position() {
		return r15_net_spot_position;
	}

	public void setR15_net_spot_position(BigDecimal r15_net_spot_position) {
		this.r15_net_spot_position = r15_net_spot_position;
	}

	public BigDecimal getR15_net_forward_position() {
		return r15_net_forward_position;
	}

	public void setR15_net_forward_position(BigDecimal r15_net_forward_position) {
		this.r15_net_forward_position = r15_net_forward_position;
	}

	public BigDecimal getR15_guarantees() {
		return r15_guarantees;
	}

	public void setR15_guarantees(BigDecimal r15_guarantees) {
		this.r15_guarantees = r15_guarantees;
	}

	public BigDecimal getR15_net_future_inc_or_exp() {
		return r15_net_future_inc_or_exp;
	}

	public void setR15_net_future_inc_or_exp(BigDecimal r15_net_future_inc_or_exp) {
		this.r15_net_future_inc_or_exp = r15_net_future_inc_or_exp;
	}

	public BigDecimal getR15_net_delta_wei_fx_opt_posi() {
		return r15_net_delta_wei_fx_opt_posi;
	}

	public void setR15_net_delta_wei_fx_opt_posi(BigDecimal r15_net_delta_wei_fx_opt_posi) {
		this.r15_net_delta_wei_fx_opt_posi = r15_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR15_other_items() {
		return r15_other_items;
	}

	public void setR15_other_items(BigDecimal r15_other_items) {
		this.r15_other_items = r15_other_items;
	}

	public BigDecimal getR15_net_long_position() {
		return r15_net_long_position;
	}

	public void setR15_net_long_position(BigDecimal r15_net_long_position) {
		this.r15_net_long_position = r15_net_long_position;
	}

	public BigDecimal getR15_or() {
		return r15_or;
	}

	public void setR15_or(BigDecimal r15_or) {
		this.r15_or = r15_or;
	}

	public BigDecimal getR15_net_short_position() {
		return r15_net_short_position;
	}

	public void setR15_net_short_position(BigDecimal r15_net_short_position) {
		this.r15_net_short_position = r15_net_short_position;
	}

	public BigDecimal getR16_net_spot_position() {
		return r16_net_spot_position;
	}

	public void setR16_net_spot_position(BigDecimal r16_net_spot_position) {
		this.r16_net_spot_position = r16_net_spot_position;
	}

	public BigDecimal getR16_net_forward_position() {
		return r16_net_forward_position;
	}

	public void setR16_net_forward_position(BigDecimal r16_net_forward_position) {
		this.r16_net_forward_position = r16_net_forward_position;
	}

	public BigDecimal getR16_guarantees() {
		return r16_guarantees;
	}

	public void setR16_guarantees(BigDecimal r16_guarantees) {
		this.r16_guarantees = r16_guarantees;
	}

	public BigDecimal getR16_net_future_inc_or_exp() {
		return r16_net_future_inc_or_exp;
	}

	public void setR16_net_future_inc_or_exp(BigDecimal r16_net_future_inc_or_exp) {
		this.r16_net_future_inc_or_exp = r16_net_future_inc_or_exp;
	}

	public BigDecimal getR16_net_delta_wei_fx_opt_posi() {
		return r16_net_delta_wei_fx_opt_posi;
	}

	public void setR16_net_delta_wei_fx_opt_posi(BigDecimal r16_net_delta_wei_fx_opt_posi) {
		this.r16_net_delta_wei_fx_opt_posi = r16_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR16_other_items() {
		return r16_other_items;
	}

	public void setR16_other_items(BigDecimal r16_other_items) {
		this.r16_other_items = r16_other_items;
	}

	public BigDecimal getR16_net_long_position() {
		return r16_net_long_position;
	}

	public void setR16_net_long_position(BigDecimal r16_net_long_position) {
		this.r16_net_long_position = r16_net_long_position;
	}

	public BigDecimal getR16_or() {
		return r16_or;
	}

	public void setR16_or(BigDecimal r16_or) {
		this.r16_or = r16_or;
	}

	public BigDecimal getR16_net_short_position() {
		return r16_net_short_position;
	}

	public void setR16_net_short_position(BigDecimal r16_net_short_position) {
		this.r16_net_short_position = r16_net_short_position;
	}

	public BigDecimal getR17_net_long_position() {
		return r17_net_long_position;
	}

	public void setR17_net_long_position(BigDecimal r17_net_long_position) {
		this.r17_net_long_position = r17_net_long_position;
	}

	public BigDecimal getR17_or() {
		return r17_or;
	}

	public void setR17_or(BigDecimal r17_or) {
		this.r17_or = r17_or;
	}

	public BigDecimal getR17_net_short_position() {
		return r17_net_short_position;
	}

	public void setR17_net_short_position(BigDecimal r17_net_short_position) {
		this.r17_net_short_position = r17_net_short_position;
	}

	public BigDecimal getR21_long() {
		return r21_long;
	}

	public void setR21_long(BigDecimal r21_long) {
		this.r21_long = r21_long;
	}

	public BigDecimal getR21_short() {
		return r21_short;
	}

	public void setR21_short(BigDecimal r21_short) {
		this.r21_short = r21_short;
	}

	public BigDecimal getR21_total_gross_long_short() {
		return r21_total_gross_long_short;
	}

	public void setR21_total_gross_long_short(BigDecimal r21_total_gross_long_short) {
		this.r21_total_gross_long_short = r21_total_gross_long_short;
	}

	public BigDecimal getR21_net_position() {
		return r21_net_position;
	}

	public void setR21_net_position(BigDecimal r21_net_position) {
		this.r21_net_position = r21_net_position;
	}

	public BigDecimal getR22_long() {
		return r22_long;
	}

	public void setR22_long(BigDecimal r22_long) {
		this.r22_long = r22_long;
	}

	public BigDecimal getR22_short() {
		return r22_short;
	}

	public void setR22_short(BigDecimal r22_short) {
		this.r22_short = r22_short;
	}

	public BigDecimal getR22_total_gross_long_short() {
		return r22_total_gross_long_short;
	}

	public void setR22_total_gross_long_short(BigDecimal r22_total_gross_long_short) {
		this.r22_total_gross_long_short = r22_total_gross_long_short;
	}

	public BigDecimal getR22_net_position() {
		return r22_net_position;
	}

	public void setR22_net_position(BigDecimal r22_net_position) {
		this.r22_net_position = r22_net_position;
	}

	public BigDecimal getR23_net_position() {
		return r23_net_position;
	}

	public void setR23_net_position(BigDecimal r23_net_position) {
		this.r23_net_position = r23_net_position;
	}

	public BigDecimal getR29_greater_net_long_or_short() {
		return r29_greater_net_long_or_short;
	}

	public void setR29_greater_net_long_or_short(BigDecimal r29_greater_net_long_or_short) {
		this.r29_greater_net_long_or_short = r29_greater_net_long_or_short;
	}

	public BigDecimal getR29_abs_value_net_gold_posi() {
		return r29_abs_value_net_gold_posi;
	}

	public void setR29_abs_value_net_gold_posi(BigDecimal r29_abs_value_net_gold_posi) {
		this.r29_abs_value_net_gold_posi = r29_abs_value_net_gold_posi;
	}

	public BigDecimal getR29_capital_charge() {
		return r29_capital_charge;
	}

	public void setR29_capital_charge(BigDecimal r29_capital_charge) {
		this.r29_capital_charge = r29_capital_charge;
	}

	public BigDecimal getR30_capital_require() {
		return r30_capital_require;
	}

	public void setR30_capital_require(BigDecimal r30_capital_require) {
		this.r30_capital_require = r30_capital_require;
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

	public M_FXR_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Date getReportResubDate() {
		return reportResubDate;
	}

	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
	}

}



	// ------------------------------
	// Entity representing the archival detail table for the FXR report
	// ------------------------------
public static class M_FXR_Archival_Detail_Entity {

//	private String r11_currency;
	private BigDecimal r11_net_spot_position;
	private BigDecimal r11_net_forward_position;
	private BigDecimal r11_guarantees;
	private BigDecimal r11_net_future_inc_or_exp;
	private BigDecimal r11_net_delta_wei_fx_opt_posi;
	private BigDecimal r11_other_items;
	private BigDecimal r11_net_long_position;
	private BigDecimal r11_or;
	private BigDecimal r11_net_short_position;
//	private String r12_currency;
	private BigDecimal r12_net_spot_position;
	private BigDecimal r12_net_forward_position;
	private BigDecimal r12_guarantees;
	private BigDecimal r12_net_future_inc_or_exp;
	private BigDecimal r12_net_delta_wei_fx_opt_posi;
	private BigDecimal r12_other_items;
	private BigDecimal r12_net_long_position;
	private BigDecimal r12_or;
	private BigDecimal r12_net_short_position;
//	private String r13_currency;
	private BigDecimal r13_net_spot_position;
	private BigDecimal r13_net_forward_position;
	private BigDecimal r13_guarantees;
	private BigDecimal r13_net_future_inc_or_exp;
	private BigDecimal r13_net_delta_wei_fx_opt_posi;
	private BigDecimal r13_other_items;
	private BigDecimal r13_net_long_position;
	private BigDecimal r13_or;
	private BigDecimal r13_net_short_position;
//	private String r14_currency;
	private BigDecimal r14_net_spot_position;
	private BigDecimal r14_net_forward_position;
	private BigDecimal r14_guarantees;
	private BigDecimal r14_net_future_inc_or_exp;
	private BigDecimal r14_net_delta_wei_fx_opt_posi;
	private BigDecimal r14_other_items;
	private BigDecimal r14_net_long_position;
	private BigDecimal r14_or;
	private BigDecimal r14_net_short_position;
//	private String r15_currency;
	private BigDecimal r15_net_spot_position;
	private BigDecimal r15_net_forward_position;
	private BigDecimal r15_guarantees;
	private BigDecimal r15_net_future_inc_or_exp;
	private BigDecimal r15_net_delta_wei_fx_opt_posi;
	private BigDecimal r15_other_items;
	private BigDecimal r15_net_long_position;
	private BigDecimal r15_or;
	private BigDecimal r15_net_short_position;
//	private String r16_currency;
	private BigDecimal r16_net_spot_position;
	private BigDecimal r16_net_forward_position;
	private BigDecimal r16_guarantees;
	private BigDecimal r16_net_future_inc_or_exp;
	private BigDecimal r16_net_delta_wei_fx_opt_posi;
	private BigDecimal r16_other_items;
	private BigDecimal r16_net_long_position;
	private BigDecimal r16_or;
	private BigDecimal r16_net_short_position;

	private BigDecimal r17_net_long_position;
	private BigDecimal r17_or;
	private BigDecimal r17_net_short_position;

	private BigDecimal r21_long;
	private BigDecimal r21_short;
	private BigDecimal r21_total_gross_long_short;
	private BigDecimal r21_net_position;
	private BigDecimal r22_long;
	private BigDecimal r22_short;
	private BigDecimal r22_total_gross_long_short;
	private BigDecimal r22_net_position;

	private BigDecimal r23_net_position;

	private BigDecimal r29_greater_net_long_or_short;
	private BigDecimal r29_abs_value_net_gold_posi;
	// private BigDecimal r29_capital_require;
	private BigDecimal r29_capital_charge;
	private BigDecimal r30_capital_require;

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;

	private String report_frequency;
	private String report_code;
	private String report_desc;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;

	public BigDecimal getR11_net_spot_position() {
		return r11_net_spot_position;
	}

	public void setR11_net_spot_position(BigDecimal r11_net_spot_position) {
		this.r11_net_spot_position = r11_net_spot_position;
	}

	public BigDecimal getR11_net_forward_position() {
		return r11_net_forward_position;
	}

	public void setR11_net_forward_position(BigDecimal r11_net_forward_position) {
		this.r11_net_forward_position = r11_net_forward_position;
	}

	public BigDecimal getR11_guarantees() {
		return r11_guarantees;
	}

	public void setR11_guarantees(BigDecimal r11_guarantees) {
		this.r11_guarantees = r11_guarantees;
	}

	public BigDecimal getR11_net_future_inc_or_exp() {
		return r11_net_future_inc_or_exp;
	}

	public void setR11_net_future_inc_or_exp(BigDecimal r11_net_future_inc_or_exp) {
		this.r11_net_future_inc_or_exp = r11_net_future_inc_or_exp;
	}

	public BigDecimal getR11_net_delta_wei_fx_opt_posi() {
		return r11_net_delta_wei_fx_opt_posi;
	}

	public void setR11_net_delta_wei_fx_opt_posi(BigDecimal r11_net_delta_wei_fx_opt_posi) {
		this.r11_net_delta_wei_fx_opt_posi = r11_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR11_other_items() {
		return r11_other_items;
	}

	public void setR11_other_items(BigDecimal r11_other_items) {
		this.r11_other_items = r11_other_items;
	}

	public BigDecimal getR11_net_long_position() {
		return r11_net_long_position;
	}

	public void setR11_net_long_position(BigDecimal r11_net_long_position) {
		this.r11_net_long_position = r11_net_long_position;
	}

	public BigDecimal getR11_or() {
		return r11_or;
	}

	public void setR11_or(BigDecimal r11_or) {
		this.r11_or = r11_or;
	}

	public BigDecimal getR11_net_short_position() {
		return r11_net_short_position;
	}

	public void setR11_net_short_position(BigDecimal r11_net_short_position) {
		this.r11_net_short_position = r11_net_short_position;
	}

	public BigDecimal getR12_net_spot_position() {
		return r12_net_spot_position;
	}

	public void setR12_net_spot_position(BigDecimal r12_net_spot_position) {
		this.r12_net_spot_position = r12_net_spot_position;
	}

	public BigDecimal getR12_net_forward_position() {
		return r12_net_forward_position;
	}

	public void setR12_net_forward_position(BigDecimal r12_net_forward_position) {
		this.r12_net_forward_position = r12_net_forward_position;
	}

	public BigDecimal getR12_guarantees() {
		return r12_guarantees;
	}

	public void setR12_guarantees(BigDecimal r12_guarantees) {
		this.r12_guarantees = r12_guarantees;
	}

	public BigDecimal getR12_net_future_inc_or_exp() {
		return r12_net_future_inc_or_exp;
	}

	public void setR12_net_future_inc_or_exp(BigDecimal r12_net_future_inc_or_exp) {
		this.r12_net_future_inc_or_exp = r12_net_future_inc_or_exp;
	}

	public BigDecimal getR12_net_delta_wei_fx_opt_posi() {
		return r12_net_delta_wei_fx_opt_posi;
	}

	public void setR12_net_delta_wei_fx_opt_posi(BigDecimal r12_net_delta_wei_fx_opt_posi) {
		this.r12_net_delta_wei_fx_opt_posi = r12_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR12_other_items() {
		return r12_other_items;
	}

	public void setR12_other_items(BigDecimal r12_other_items) {
		this.r12_other_items = r12_other_items;
	}

	public BigDecimal getR12_net_long_position() {
		return r12_net_long_position;
	}

	public void setR12_net_long_position(BigDecimal r12_net_long_position) {
		this.r12_net_long_position = r12_net_long_position;
	}

	public BigDecimal getR12_or() {
		return r12_or;
	}

	public void setR12_or(BigDecimal r12_or) {
		this.r12_or = r12_or;
	}

	public BigDecimal getR12_net_short_position() {
		return r12_net_short_position;
	}

	public void setR12_net_short_position(BigDecimal r12_net_short_position) {
		this.r12_net_short_position = r12_net_short_position;
	}

	public BigDecimal getR13_net_spot_position() {
		return r13_net_spot_position;
	}

	public void setR13_net_spot_position(BigDecimal r13_net_spot_position) {
		this.r13_net_spot_position = r13_net_spot_position;
	}

	public BigDecimal getR13_net_forward_position() {
		return r13_net_forward_position;
	}

	public void setR13_net_forward_position(BigDecimal r13_net_forward_position) {
		this.r13_net_forward_position = r13_net_forward_position;
	}

	public BigDecimal getR13_guarantees() {
		return r13_guarantees;
	}

	public void setR13_guarantees(BigDecimal r13_guarantees) {
		this.r13_guarantees = r13_guarantees;
	}

	public BigDecimal getR13_net_future_inc_or_exp() {
		return r13_net_future_inc_or_exp;
	}

	public void setR13_net_future_inc_or_exp(BigDecimal r13_net_future_inc_or_exp) {
		this.r13_net_future_inc_or_exp = r13_net_future_inc_or_exp;
	}

	public BigDecimal getR13_net_delta_wei_fx_opt_posi() {
		return r13_net_delta_wei_fx_opt_posi;
	}

	public void setR13_net_delta_wei_fx_opt_posi(BigDecimal r13_net_delta_wei_fx_opt_posi) {
		this.r13_net_delta_wei_fx_opt_posi = r13_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR13_other_items() {
		return r13_other_items;
	}

	public void setR13_other_items(BigDecimal r13_other_items) {
		this.r13_other_items = r13_other_items;
	}

	public BigDecimal getR13_net_long_position() {
		return r13_net_long_position;
	}

	public void setR13_net_long_position(BigDecimal r13_net_long_position) {
		this.r13_net_long_position = r13_net_long_position;
	}

	public BigDecimal getR13_or() {
		return r13_or;
	}

	public void setR13_or(BigDecimal r13_or) {
		this.r13_or = r13_or;
	}

	public BigDecimal getR13_net_short_position() {
		return r13_net_short_position;
	}

	public void setR13_net_short_position(BigDecimal r13_net_short_position) {
		this.r13_net_short_position = r13_net_short_position;
	}

	public BigDecimal getR14_net_spot_position() {
		return r14_net_spot_position;
	}

	public void setR14_net_spot_position(BigDecimal r14_net_spot_position) {
		this.r14_net_spot_position = r14_net_spot_position;
	}

	public BigDecimal getR14_net_forward_position() {
		return r14_net_forward_position;
	}

	public void setR14_net_forward_position(BigDecimal r14_net_forward_position) {
		this.r14_net_forward_position = r14_net_forward_position;
	}

	public BigDecimal getR14_guarantees() {
		return r14_guarantees;
	}

	public void setR14_guarantees(BigDecimal r14_guarantees) {
		this.r14_guarantees = r14_guarantees;
	}

	public BigDecimal getR14_net_future_inc_or_exp() {
		return r14_net_future_inc_or_exp;
	}

	public void setR14_net_future_inc_or_exp(BigDecimal r14_net_future_inc_or_exp) {
		this.r14_net_future_inc_or_exp = r14_net_future_inc_or_exp;
	}

	public BigDecimal getR14_net_delta_wei_fx_opt_posi() {
		return r14_net_delta_wei_fx_opt_posi;
	}

	public void setR14_net_delta_wei_fx_opt_posi(BigDecimal r14_net_delta_wei_fx_opt_posi) {
		this.r14_net_delta_wei_fx_opt_posi = r14_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR14_other_items() {
		return r14_other_items;
	}

	public void setR14_other_items(BigDecimal r14_other_items) {
		this.r14_other_items = r14_other_items;
	}

	public BigDecimal getR14_net_long_position() {
		return r14_net_long_position;
	}

	public void setR14_net_long_position(BigDecimal r14_net_long_position) {
		this.r14_net_long_position = r14_net_long_position;
	}

	public BigDecimal getR14_or() {
		return r14_or;
	}

	public void setR14_or(BigDecimal r14_or) {
		this.r14_or = r14_or;
	}

	public BigDecimal getR14_net_short_position() {
		return r14_net_short_position;
	}

	public void setR14_net_short_position(BigDecimal r14_net_short_position) {
		this.r14_net_short_position = r14_net_short_position;
	}

	public BigDecimal getR15_net_spot_position() {
		return r15_net_spot_position;
	}

	public void setR15_net_spot_position(BigDecimal r15_net_spot_position) {
		this.r15_net_spot_position = r15_net_spot_position;
	}

	public BigDecimal getR15_net_forward_position() {
		return r15_net_forward_position;
	}

	public void setR15_net_forward_position(BigDecimal r15_net_forward_position) {
		this.r15_net_forward_position = r15_net_forward_position;
	}

	public BigDecimal getR15_guarantees() {
		return r15_guarantees;
	}

	public void setR15_guarantees(BigDecimal r15_guarantees) {
		this.r15_guarantees = r15_guarantees;
	}

	public BigDecimal getR15_net_future_inc_or_exp() {
		return r15_net_future_inc_or_exp;
	}

	public void setR15_net_future_inc_or_exp(BigDecimal r15_net_future_inc_or_exp) {
		this.r15_net_future_inc_or_exp = r15_net_future_inc_or_exp;
	}

	public BigDecimal getR15_net_delta_wei_fx_opt_posi() {
		return r15_net_delta_wei_fx_opt_posi;
	}

	public void setR15_net_delta_wei_fx_opt_posi(BigDecimal r15_net_delta_wei_fx_opt_posi) {
		this.r15_net_delta_wei_fx_opt_posi = r15_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR15_other_items() {
		return r15_other_items;
	}

	public void setR15_other_items(BigDecimal r15_other_items) {
		this.r15_other_items = r15_other_items;
	}

	public BigDecimal getR15_net_long_position() {
		return r15_net_long_position;
	}

	public void setR15_net_long_position(BigDecimal r15_net_long_position) {
		this.r15_net_long_position = r15_net_long_position;
	}

	public BigDecimal getR15_or() {
		return r15_or;
	}

	public void setR15_or(BigDecimal r15_or) {
		this.r15_or = r15_or;
	}

	public BigDecimal getR15_net_short_position() {
		return r15_net_short_position;
	}

	public void setR15_net_short_position(BigDecimal r15_net_short_position) {
		this.r15_net_short_position = r15_net_short_position;
	}

	public BigDecimal getR16_net_spot_position() {
		return r16_net_spot_position;
	}

	public void setR16_net_spot_position(BigDecimal r16_net_spot_position) {
		this.r16_net_spot_position = r16_net_spot_position;
	}

	public BigDecimal getR16_net_forward_position() {
		return r16_net_forward_position;
	}

	public void setR16_net_forward_position(BigDecimal r16_net_forward_position) {
		this.r16_net_forward_position = r16_net_forward_position;
	}

	public BigDecimal getR16_guarantees() {
		return r16_guarantees;
	}

	public void setR16_guarantees(BigDecimal r16_guarantees) {
		this.r16_guarantees = r16_guarantees;
	}

	public BigDecimal getR16_net_future_inc_or_exp() {
		return r16_net_future_inc_or_exp;
	}

	public void setR16_net_future_inc_or_exp(BigDecimal r16_net_future_inc_or_exp) {
		this.r16_net_future_inc_or_exp = r16_net_future_inc_or_exp;
	}

	public BigDecimal getR16_net_delta_wei_fx_opt_posi() {
		return r16_net_delta_wei_fx_opt_posi;
	}

	public void setR16_net_delta_wei_fx_opt_posi(BigDecimal r16_net_delta_wei_fx_opt_posi) {
		this.r16_net_delta_wei_fx_opt_posi = r16_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR16_other_items() {
		return r16_other_items;
	}

	public void setR16_other_items(BigDecimal r16_other_items) {
		this.r16_other_items = r16_other_items;
	}

	public BigDecimal getR16_net_long_position() {
		return r16_net_long_position;
	}

	public void setR16_net_long_position(BigDecimal r16_net_long_position) {
		this.r16_net_long_position = r16_net_long_position;
	}

	public BigDecimal getR16_or() {
		return r16_or;
	}

	public void setR16_or(BigDecimal r16_or) {
		this.r16_or = r16_or;
	}

	public BigDecimal getR16_net_short_position() {
		return r16_net_short_position;
	}

	public void setR16_net_short_position(BigDecimal r16_net_short_position) {
		this.r16_net_short_position = r16_net_short_position;
	}

	public BigDecimal getR17_net_long_position() {
		return r17_net_long_position;
	}

	public void setR17_net_long_position(BigDecimal r17_net_long_position) {
		this.r17_net_long_position = r17_net_long_position;
	}

	public BigDecimal getR17_or() {
		return r17_or;
	}

	public void setR17_or(BigDecimal r17_or) {
		this.r17_or = r17_or;
	}

	public BigDecimal getR17_net_short_position() {
		return r17_net_short_position;
	}

	public void setR17_net_short_position(BigDecimal r17_net_short_position) {
		this.r17_net_short_position = r17_net_short_position;
	}

	public BigDecimal getR21_long() {
		return r21_long;
	}

	public void setR21_long(BigDecimal r21_long) {
		this.r21_long = r21_long;
	}

	public BigDecimal getR21_short() {
		return r21_short;
	}

	public void setR21_short(BigDecimal r21_short) {
		this.r21_short = r21_short;
	}

	public BigDecimal getR21_total_gross_long_short() {
		return r21_total_gross_long_short;
	}

	public void setR21_total_gross_long_short(BigDecimal r21_total_gross_long_short) {
		this.r21_total_gross_long_short = r21_total_gross_long_short;
	}

	public BigDecimal getR21_net_position() {
		return r21_net_position;
	}

	public void setR21_net_position(BigDecimal r21_net_position) {
		this.r21_net_position = r21_net_position;
	}

	public BigDecimal getR22_long() {
		return r22_long;
	}

	public void setR22_long(BigDecimal r22_long) {
		this.r22_long = r22_long;
	}

	public BigDecimal getR22_short() {
		return r22_short;
	}

	public void setR22_short(BigDecimal r22_short) {
		this.r22_short = r22_short;
	}

	public BigDecimal getR22_total_gross_long_short() {
		return r22_total_gross_long_short;
	}

	public void setR22_total_gross_long_short(BigDecimal r22_total_gross_long_short) {
		this.r22_total_gross_long_short = r22_total_gross_long_short;
	}

	public BigDecimal getR22_net_position() {
		return r22_net_position;
	}

	public void setR22_net_position(BigDecimal r22_net_position) {
		this.r22_net_position = r22_net_position;
	}

	public BigDecimal getR23_net_position() {
		return r23_net_position;
	}

	public void setR23_net_position(BigDecimal r23_net_position) {
		this.r23_net_position = r23_net_position;
	}

	public BigDecimal getR29_greater_net_long_or_short() {
		return r29_greater_net_long_or_short;
	}

	public void setR29_greater_net_long_or_short(BigDecimal r29_greater_net_long_or_short) {
		this.r29_greater_net_long_or_short = r29_greater_net_long_or_short;
	}

	public BigDecimal getR29_abs_value_net_gold_posi() {
		return r29_abs_value_net_gold_posi;
	}

	public void setR29_abs_value_net_gold_posi(BigDecimal r29_abs_value_net_gold_posi) {
		this.r29_abs_value_net_gold_posi = r29_abs_value_net_gold_posi;
	}

	public BigDecimal getR29_capital_charge() {
		return r29_capital_charge;
	}

	public void setR29_capital_charge(BigDecimal r29_capital_charge) {
		this.r29_capital_charge = r29_capital_charge;
	}

	public BigDecimal getR30_capital_require() {
		return r30_capital_require;
	}

	public void setR30_capital_require(BigDecimal r30_capital_require) {
		this.r30_capital_require = r30_capital_require;
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

	public M_FXR_Archival_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getReportResubDate() {
		return reportResubDate;
	}

	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
	}

}



	// ------------------------------
	// Entity representing the resubmission summary table for the FXR report
	// ------------------------------
public static class M_FXR_Resub_Summary_Entity {

//	private String r11_currency;
	private BigDecimal r11_net_spot_position;
	private BigDecimal r11_net_forward_position;
	private BigDecimal r11_guarantees;
	private BigDecimal r11_net_future_inc_or_exp;
	private BigDecimal r11_net_delta_wei_fx_opt_posi;
	private BigDecimal r11_other_items;
	private BigDecimal r11_net_long_position;
	private BigDecimal r11_or;
	private BigDecimal r11_net_short_position;
//	private String r12_currency;
	private BigDecimal r12_net_spot_position;
	private BigDecimal r12_net_forward_position;
	private BigDecimal r12_guarantees;
	private BigDecimal r12_net_future_inc_or_exp;
	private BigDecimal r12_net_delta_wei_fx_opt_posi;
	private BigDecimal r12_other_items;
	private BigDecimal r12_net_long_position;
	private BigDecimal r12_or;
	private BigDecimal r12_net_short_position;
//	private String r13_currency;
	private BigDecimal r13_net_spot_position;
	private BigDecimal r13_net_forward_position;
	private BigDecimal r13_guarantees;
	private BigDecimal r13_net_future_inc_or_exp;
	private BigDecimal r13_net_delta_wei_fx_opt_posi;
	private BigDecimal r13_other_items;
	private BigDecimal r13_net_long_position;
	private BigDecimal r13_or;
	private BigDecimal r13_net_short_position;
//	private String r14_currency;
	private BigDecimal r14_net_spot_position;
	private BigDecimal r14_net_forward_position;
	private BigDecimal r14_guarantees;
	private BigDecimal r14_net_future_inc_or_exp;
	private BigDecimal r14_net_delta_wei_fx_opt_posi;
	private BigDecimal r14_other_items;
	private BigDecimal r14_net_long_position;
	private BigDecimal r14_or;
	private BigDecimal r14_net_short_position;
//	private String r15_currency;
	private BigDecimal r15_net_spot_position;
	private BigDecimal r15_net_forward_position;
	private BigDecimal r15_guarantees;
	private BigDecimal r15_net_future_inc_or_exp;
	private BigDecimal r15_net_delta_wei_fx_opt_posi;
	private BigDecimal r15_other_items;
	private BigDecimal r15_net_long_position;
	private BigDecimal r15_or;
	private BigDecimal r15_net_short_position;
//	private String r16_currency;
	private BigDecimal r16_net_spot_position;
	private BigDecimal r16_net_forward_position;
	private BigDecimal r16_guarantees;
	private BigDecimal r16_net_future_inc_or_exp;
	private BigDecimal r16_net_delta_wei_fx_opt_posi;
	private BigDecimal r16_other_items;
	private BigDecimal r16_net_long_position;
	private BigDecimal r16_or;
	private BigDecimal r16_net_short_position;

	private BigDecimal r17_net_long_position;
	private BigDecimal r17_or;
	private BigDecimal r17_net_short_position;

	private BigDecimal r21_long;
	private BigDecimal r21_short;
	private BigDecimal r21_total_gross_long_short;
	private BigDecimal r21_net_position;
	private BigDecimal r22_long;
	private BigDecimal r22_short;
	private BigDecimal r22_total_gross_long_short;
	private BigDecimal r22_net_position;

	private BigDecimal r23_net_position;

	private BigDecimal r29_greater_net_long_or_short;
	private BigDecimal r29_abs_value_net_gold_posi;
	// private BigDecimal r29_capital_require;
	private BigDecimal r29_capital_charge;
	private BigDecimal r30_capital_require;

@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;

	private String report_frequency;
	private String report_code;
	private String report_desc;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;

	public BigDecimal getR11_net_spot_position() {
		return r11_net_spot_position;
	}

	public void setR11_net_spot_position(BigDecimal r11_net_spot_position) {
		this.r11_net_spot_position = r11_net_spot_position;
	}

	public BigDecimal getR11_net_forward_position() {
		return r11_net_forward_position;
	}

	public void setR11_net_forward_position(BigDecimal r11_net_forward_position) {
		this.r11_net_forward_position = r11_net_forward_position;
	}

	public BigDecimal getR11_guarantees() {
		return r11_guarantees;
	}

	public void setR11_guarantees(BigDecimal r11_guarantees) {
		this.r11_guarantees = r11_guarantees;
	}

	public BigDecimal getR11_net_future_inc_or_exp() {
		return r11_net_future_inc_or_exp;
	}

	public void setR11_net_future_inc_or_exp(BigDecimal r11_net_future_inc_or_exp) {
		this.r11_net_future_inc_or_exp = r11_net_future_inc_or_exp;
	}

	public BigDecimal getR11_net_delta_wei_fx_opt_posi() {
		return r11_net_delta_wei_fx_opt_posi;
	}

	public void setR11_net_delta_wei_fx_opt_posi(BigDecimal r11_net_delta_wei_fx_opt_posi) {
		this.r11_net_delta_wei_fx_opt_posi = r11_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR11_other_items() {
		return r11_other_items;
	}

	public void setR11_other_items(BigDecimal r11_other_items) {
		this.r11_other_items = r11_other_items;
	}

	public BigDecimal getR11_net_long_position() {
		return r11_net_long_position;
	}

	public void setR11_net_long_position(BigDecimal r11_net_long_position) {
		this.r11_net_long_position = r11_net_long_position;
	}

	public BigDecimal getR11_or() {
		return r11_or;
	}

	public void setR11_or(BigDecimal r11_or) {
		this.r11_or = r11_or;
	}

	public BigDecimal getR11_net_short_position() {
		return r11_net_short_position;
	}

	public void setR11_net_short_position(BigDecimal r11_net_short_position) {
		this.r11_net_short_position = r11_net_short_position;
	}

	public BigDecimal getR12_net_spot_position() {
		return r12_net_spot_position;
	}

	public void setR12_net_spot_position(BigDecimal r12_net_spot_position) {
		this.r12_net_spot_position = r12_net_spot_position;
	}

	public BigDecimal getR12_net_forward_position() {
		return r12_net_forward_position;
	}

	public void setR12_net_forward_position(BigDecimal r12_net_forward_position) {
		this.r12_net_forward_position = r12_net_forward_position;
	}

	public BigDecimal getR12_guarantees() {
		return r12_guarantees;
	}

	public void setR12_guarantees(BigDecimal r12_guarantees) {
		this.r12_guarantees = r12_guarantees;
	}

	public BigDecimal getR12_net_future_inc_or_exp() {
		return r12_net_future_inc_or_exp;
	}

	public void setR12_net_future_inc_or_exp(BigDecimal r12_net_future_inc_or_exp) {
		this.r12_net_future_inc_or_exp = r12_net_future_inc_or_exp;
	}

	public BigDecimal getR12_net_delta_wei_fx_opt_posi() {
		return r12_net_delta_wei_fx_opt_posi;
	}

	public void setR12_net_delta_wei_fx_opt_posi(BigDecimal r12_net_delta_wei_fx_opt_posi) {
		this.r12_net_delta_wei_fx_opt_posi = r12_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR12_other_items() {
		return r12_other_items;
	}

	public void setR12_other_items(BigDecimal r12_other_items) {
		this.r12_other_items = r12_other_items;
	}

	public BigDecimal getR12_net_long_position() {
		return r12_net_long_position;
	}

	public void setR12_net_long_position(BigDecimal r12_net_long_position) {
		this.r12_net_long_position = r12_net_long_position;
	}

	public BigDecimal getR12_or() {
		return r12_or;
	}

	public void setR12_or(BigDecimal r12_or) {
		this.r12_or = r12_or;
	}

	public BigDecimal getR12_net_short_position() {
		return r12_net_short_position;
	}

	public void setR12_net_short_position(BigDecimal r12_net_short_position) {
		this.r12_net_short_position = r12_net_short_position;
	}

	public BigDecimal getR13_net_spot_position() {
		return r13_net_spot_position;
	}

	public void setR13_net_spot_position(BigDecimal r13_net_spot_position) {
		this.r13_net_spot_position = r13_net_spot_position;
	}

	public BigDecimal getR13_net_forward_position() {
		return r13_net_forward_position;
	}

	public void setR13_net_forward_position(BigDecimal r13_net_forward_position) {
		this.r13_net_forward_position = r13_net_forward_position;
	}

	public BigDecimal getR13_guarantees() {
		return r13_guarantees;
	}

	public void setR13_guarantees(BigDecimal r13_guarantees) {
		this.r13_guarantees = r13_guarantees;
	}

	public BigDecimal getR13_net_future_inc_or_exp() {
		return r13_net_future_inc_or_exp;
	}

	public void setR13_net_future_inc_or_exp(BigDecimal r13_net_future_inc_or_exp) {
		this.r13_net_future_inc_or_exp = r13_net_future_inc_or_exp;
	}

	public BigDecimal getR13_net_delta_wei_fx_opt_posi() {
		return r13_net_delta_wei_fx_opt_posi;
	}

	public void setR13_net_delta_wei_fx_opt_posi(BigDecimal r13_net_delta_wei_fx_opt_posi) {
		this.r13_net_delta_wei_fx_opt_posi = r13_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR13_other_items() {
		return r13_other_items;
	}

	public void setR13_other_items(BigDecimal r13_other_items) {
		this.r13_other_items = r13_other_items;
	}

	public BigDecimal getR13_net_long_position() {
		return r13_net_long_position;
	}

	public void setR13_net_long_position(BigDecimal r13_net_long_position) {
		this.r13_net_long_position = r13_net_long_position;
	}

	public BigDecimal getR13_or() {
		return r13_or;
	}

	public void setR13_or(BigDecimal r13_or) {
		this.r13_or = r13_or;
	}

	public BigDecimal getR13_net_short_position() {
		return r13_net_short_position;
	}

	public void setR13_net_short_position(BigDecimal r13_net_short_position) {
		this.r13_net_short_position = r13_net_short_position;
	}

	public BigDecimal getR14_net_spot_position() {
		return r14_net_spot_position;
	}

	public void setR14_net_spot_position(BigDecimal r14_net_spot_position) {
		this.r14_net_spot_position = r14_net_spot_position;
	}

	public BigDecimal getR14_net_forward_position() {
		return r14_net_forward_position;
	}

	public void setR14_net_forward_position(BigDecimal r14_net_forward_position) {
		this.r14_net_forward_position = r14_net_forward_position;
	}

	public BigDecimal getR14_guarantees() {
		return r14_guarantees;
	}

	public void setR14_guarantees(BigDecimal r14_guarantees) {
		this.r14_guarantees = r14_guarantees;
	}

	public BigDecimal getR14_net_future_inc_or_exp() {
		return r14_net_future_inc_or_exp;
	}

	public void setR14_net_future_inc_or_exp(BigDecimal r14_net_future_inc_or_exp) {
		this.r14_net_future_inc_or_exp = r14_net_future_inc_or_exp;
	}

	public BigDecimal getR14_net_delta_wei_fx_opt_posi() {
		return r14_net_delta_wei_fx_opt_posi;
	}

	public void setR14_net_delta_wei_fx_opt_posi(BigDecimal r14_net_delta_wei_fx_opt_posi) {
		this.r14_net_delta_wei_fx_opt_posi = r14_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR14_other_items() {
		return r14_other_items;
	}

	public void setR14_other_items(BigDecimal r14_other_items) {
		this.r14_other_items = r14_other_items;
	}

	public BigDecimal getR14_net_long_position() {
		return r14_net_long_position;
	}

	public void setR14_net_long_position(BigDecimal r14_net_long_position) {
		this.r14_net_long_position = r14_net_long_position;
	}

	public BigDecimal getR14_or() {
		return r14_or;
	}

	public void setR14_or(BigDecimal r14_or) {
		this.r14_or = r14_or;
	}

	public BigDecimal getR14_net_short_position() {
		return r14_net_short_position;
	}

	public void setR14_net_short_position(BigDecimal r14_net_short_position) {
		this.r14_net_short_position = r14_net_short_position;
	}

	public BigDecimal getR15_net_spot_position() {
		return r15_net_spot_position;
	}

	public void setR15_net_spot_position(BigDecimal r15_net_spot_position) {
		this.r15_net_spot_position = r15_net_spot_position;
	}

	public BigDecimal getR15_net_forward_position() {
		return r15_net_forward_position;
	}

	public void setR15_net_forward_position(BigDecimal r15_net_forward_position) {
		this.r15_net_forward_position = r15_net_forward_position;
	}

	public BigDecimal getR15_guarantees() {
		return r15_guarantees;
	}

	public void setR15_guarantees(BigDecimal r15_guarantees) {
		this.r15_guarantees = r15_guarantees;
	}

	public BigDecimal getR15_net_future_inc_or_exp() {
		return r15_net_future_inc_or_exp;
	}

	public void setR15_net_future_inc_or_exp(BigDecimal r15_net_future_inc_or_exp) {
		this.r15_net_future_inc_or_exp = r15_net_future_inc_or_exp;
	}

	public BigDecimal getR15_net_delta_wei_fx_opt_posi() {
		return r15_net_delta_wei_fx_opt_posi;
	}

	public void setR15_net_delta_wei_fx_opt_posi(BigDecimal r15_net_delta_wei_fx_opt_posi) {
		this.r15_net_delta_wei_fx_opt_posi = r15_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR15_other_items() {
		return r15_other_items;
	}

	public void setR15_other_items(BigDecimal r15_other_items) {
		this.r15_other_items = r15_other_items;
	}

	public BigDecimal getR15_net_long_position() {
		return r15_net_long_position;
	}

	public void setR15_net_long_position(BigDecimal r15_net_long_position) {
		this.r15_net_long_position = r15_net_long_position;
	}

	public BigDecimal getR15_or() {
		return r15_or;
	}

	public void setR15_or(BigDecimal r15_or) {
		this.r15_or = r15_or;
	}

	public BigDecimal getR15_net_short_position() {
		return r15_net_short_position;
	}

	public void setR15_net_short_position(BigDecimal r15_net_short_position) {
		this.r15_net_short_position = r15_net_short_position;
	}

	public BigDecimal getR16_net_spot_position() {
		return r16_net_spot_position;
	}

	public void setR16_net_spot_position(BigDecimal r16_net_spot_position) {
		this.r16_net_spot_position = r16_net_spot_position;
	}

	public BigDecimal getR16_net_forward_position() {
		return r16_net_forward_position;
	}

	public void setR16_net_forward_position(BigDecimal r16_net_forward_position) {
		this.r16_net_forward_position = r16_net_forward_position;
	}

	public BigDecimal getR16_guarantees() {
		return r16_guarantees;
	}

	public void setR16_guarantees(BigDecimal r16_guarantees) {
		this.r16_guarantees = r16_guarantees;
	}

	public BigDecimal getR16_net_future_inc_or_exp() {
		return r16_net_future_inc_or_exp;
	}

	public void setR16_net_future_inc_or_exp(BigDecimal r16_net_future_inc_or_exp) {
		this.r16_net_future_inc_or_exp = r16_net_future_inc_or_exp;
	}

	public BigDecimal getR16_net_delta_wei_fx_opt_posi() {
		return r16_net_delta_wei_fx_opt_posi;
	}

	public void setR16_net_delta_wei_fx_opt_posi(BigDecimal r16_net_delta_wei_fx_opt_posi) {
		this.r16_net_delta_wei_fx_opt_posi = r16_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR16_other_items() {
		return r16_other_items;
	}

	public void setR16_other_items(BigDecimal r16_other_items) {
		this.r16_other_items = r16_other_items;
	}

	public BigDecimal getR16_net_long_position() {
		return r16_net_long_position;
	}

	public void setR16_net_long_position(BigDecimal r16_net_long_position) {
		this.r16_net_long_position = r16_net_long_position;
	}

	public BigDecimal getR16_or() {
		return r16_or;
	}

	public void setR16_or(BigDecimal r16_or) {
		this.r16_or = r16_or;
	}

	public BigDecimal getR16_net_short_position() {
		return r16_net_short_position;
	}

	public void setR16_net_short_position(BigDecimal r16_net_short_position) {
		this.r16_net_short_position = r16_net_short_position;
	}

	public BigDecimal getR17_net_long_position() {
		return r17_net_long_position;
	}

	public void setR17_net_long_position(BigDecimal r17_net_long_position) {
		this.r17_net_long_position = r17_net_long_position;
	}

	public BigDecimal getR17_or() {
		return r17_or;
	}

	public void setR17_or(BigDecimal r17_or) {
		this.r17_or = r17_or;
	}

	public BigDecimal getR17_net_short_position() {
		return r17_net_short_position;
	}

	public void setR17_net_short_position(BigDecimal r17_net_short_position) {
		this.r17_net_short_position = r17_net_short_position;
	}

	public BigDecimal getR21_long() {
		return r21_long;
	}

	public void setR21_long(BigDecimal r21_long) {
		this.r21_long = r21_long;
	}

	public BigDecimal getR21_short() {
		return r21_short;
	}

	public void setR21_short(BigDecimal r21_short) {
		this.r21_short = r21_short;
	}

	public BigDecimal getR21_total_gross_long_short() {
		return r21_total_gross_long_short;
	}

	public void setR21_total_gross_long_short(BigDecimal r21_total_gross_long_short) {
		this.r21_total_gross_long_short = r21_total_gross_long_short;
	}

	public BigDecimal getR21_net_position() {
		return r21_net_position;
	}

	public void setR21_net_position(BigDecimal r21_net_position) {
		this.r21_net_position = r21_net_position;
	}

	public BigDecimal getR22_long() {
		return r22_long;
	}

	public void setR22_long(BigDecimal r22_long) {
		this.r22_long = r22_long;
	}

	public BigDecimal getR22_short() {
		return r22_short;
	}

	public void setR22_short(BigDecimal r22_short) {
		this.r22_short = r22_short;
	}

	public BigDecimal getR22_total_gross_long_short() {
		return r22_total_gross_long_short;
	}

	public void setR22_total_gross_long_short(BigDecimal r22_total_gross_long_short) {
		this.r22_total_gross_long_short = r22_total_gross_long_short;
	}

	public BigDecimal getR22_net_position() {
		return r22_net_position;
	}

	public void setR22_net_position(BigDecimal r22_net_position) {
		this.r22_net_position = r22_net_position;
	}

	public BigDecimal getR23_net_position() {
		return r23_net_position;
	}

	public void setR23_net_position(BigDecimal r23_net_position) {
		this.r23_net_position = r23_net_position;
	}

	public BigDecimal getR29_greater_net_long_or_short() {
		return r29_greater_net_long_or_short;
	}

	public void setR29_greater_net_long_or_short(BigDecimal r29_greater_net_long_or_short) {
		this.r29_greater_net_long_or_short = r29_greater_net_long_or_short;
	}

	public BigDecimal getR29_abs_value_net_gold_posi() {
		return r29_abs_value_net_gold_posi;
	}

	public void setR29_abs_value_net_gold_posi(BigDecimal r29_abs_value_net_gold_posi) {
		this.r29_abs_value_net_gold_posi = r29_abs_value_net_gold_posi;
	}

	public BigDecimal getR29_capital_charge() {
		return r29_capital_charge;
	}

	public void setR29_capital_charge(BigDecimal r29_capital_charge) {
		this.r29_capital_charge = r29_capital_charge;
	}

	public BigDecimal getR30_capital_require() {
		return r30_capital_require;
	}

	public void setR30_capital_require(BigDecimal r30_capital_require) {
		this.r30_capital_require = r30_capital_require;
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

	public M_FXR_Resub_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getReportResubDate() {
		return reportResubDate;
	}

	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
	}

}



	// ------------------------------
	// Entity representing the resubmission detail table for the FXR report
	// ------------------------------
public static class M_FXR_Resub_Detail_Entity {

//	private String r11_currency;
	private BigDecimal r11_net_spot_position;
	private BigDecimal r11_net_forward_position;
	private BigDecimal r11_guarantees;
	private BigDecimal r11_net_future_inc_or_exp;
	private BigDecimal r11_net_delta_wei_fx_opt_posi;
	private BigDecimal r11_other_items;
	private BigDecimal r11_net_long_position;
	private BigDecimal r11_or;
	private BigDecimal r11_net_short_position;
//	private String r12_currency;
	private BigDecimal r12_net_spot_position;
	private BigDecimal r12_net_forward_position;
	private BigDecimal r12_guarantees;
	private BigDecimal r12_net_future_inc_or_exp;
	private BigDecimal r12_net_delta_wei_fx_opt_posi;
	private BigDecimal r12_other_items;
	private BigDecimal r12_net_long_position;
	private BigDecimal r12_or;
	private BigDecimal r12_net_short_position;
//	private String r13_currency;
	private BigDecimal r13_net_spot_position;
	private BigDecimal r13_net_forward_position;
	private BigDecimal r13_guarantees;
	private BigDecimal r13_net_future_inc_or_exp;
	private BigDecimal r13_net_delta_wei_fx_opt_posi;
	private BigDecimal r13_other_items;
	private BigDecimal r13_net_long_position;
	private BigDecimal r13_or;
	private BigDecimal r13_net_short_position;
//	private String r14_currency;
	private BigDecimal r14_net_spot_position;
	private BigDecimal r14_net_forward_position;
	private BigDecimal r14_guarantees;
	private BigDecimal r14_net_future_inc_or_exp;
	private BigDecimal r14_net_delta_wei_fx_opt_posi;
	private BigDecimal r14_other_items;
	private BigDecimal r14_net_long_position;
	private BigDecimal r14_or;
	private BigDecimal r14_net_short_position;
//	private String r15_currency;
	private BigDecimal r15_net_spot_position;
	private BigDecimal r15_net_forward_position;
	private BigDecimal r15_guarantees;
	private BigDecimal r15_net_future_inc_or_exp;
	private BigDecimal r15_net_delta_wei_fx_opt_posi;
	private BigDecimal r15_other_items;
	private BigDecimal r15_net_long_position;
	private BigDecimal r15_or;
	private BigDecimal r15_net_short_position;
//	private String r16_currency;
	private BigDecimal r16_net_spot_position;
	private BigDecimal r16_net_forward_position;
	private BigDecimal r16_guarantees;
	private BigDecimal r16_net_future_inc_or_exp;
	private BigDecimal r16_net_delta_wei_fx_opt_posi;
	private BigDecimal r16_other_items;
	private BigDecimal r16_net_long_position;
	private BigDecimal r16_or;
	private BigDecimal r16_net_short_position;

	private BigDecimal r17_net_long_position;
	private BigDecimal r17_or;
	private BigDecimal r17_net_short_position;

	private BigDecimal r21_long;
	private BigDecimal r21_short;
	private BigDecimal r21_total_gross_long_short;
	private BigDecimal r21_net_position;
	private BigDecimal r22_long;
	private BigDecimal r22_short;
	private BigDecimal r22_total_gross_long_short;
	private BigDecimal r22_net_position;

	private BigDecimal r23_net_position;

	private BigDecimal r29_greater_net_long_or_short;
	private BigDecimal r29_abs_value_net_gold_posi;
	// private BigDecimal r29_capital_require;
	private BigDecimal r29_capital_charge;
	private BigDecimal r30_capital_require;

@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;

	private String report_frequency;
	private String report_code;
	private String report_desc;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;

	public BigDecimal getR11_net_spot_position() {
		return r11_net_spot_position;
	}

	public void setR11_net_spot_position(BigDecimal r11_net_spot_position) {
		this.r11_net_spot_position = r11_net_spot_position;
	}

	public BigDecimal getR11_net_forward_position() {
		return r11_net_forward_position;
	}

	public void setR11_net_forward_position(BigDecimal r11_net_forward_position) {
		this.r11_net_forward_position = r11_net_forward_position;
	}

	public BigDecimal getR11_guarantees() {
		return r11_guarantees;
	}

	public void setR11_guarantees(BigDecimal r11_guarantees) {
		this.r11_guarantees = r11_guarantees;
	}

	public BigDecimal getR11_net_future_inc_or_exp() {
		return r11_net_future_inc_or_exp;
	}

	public void setR11_net_future_inc_or_exp(BigDecimal r11_net_future_inc_or_exp) {
		this.r11_net_future_inc_or_exp = r11_net_future_inc_or_exp;
	}

	public BigDecimal getR11_net_delta_wei_fx_opt_posi() {
		return r11_net_delta_wei_fx_opt_posi;
	}

	public void setR11_net_delta_wei_fx_opt_posi(BigDecimal r11_net_delta_wei_fx_opt_posi) {
		this.r11_net_delta_wei_fx_opt_posi = r11_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR11_other_items() {
		return r11_other_items;
	}

	public void setR11_other_items(BigDecimal r11_other_items) {
		this.r11_other_items = r11_other_items;
	}

	public BigDecimal getR11_net_long_position() {
		return r11_net_long_position;
	}

	public void setR11_net_long_position(BigDecimal r11_net_long_position) {
		this.r11_net_long_position = r11_net_long_position;
	}

	public BigDecimal getR11_or() {
		return r11_or;
	}

	public void setR11_or(BigDecimal r11_or) {
		this.r11_or = r11_or;
	}

	public BigDecimal getR11_net_short_position() {
		return r11_net_short_position;
	}

	public void setR11_net_short_position(BigDecimal r11_net_short_position) {
		this.r11_net_short_position = r11_net_short_position;
	}

	public BigDecimal getR12_net_spot_position() {
		return r12_net_spot_position;
	}

	public void setR12_net_spot_position(BigDecimal r12_net_spot_position) {
		this.r12_net_spot_position = r12_net_spot_position;
	}

	public BigDecimal getR12_net_forward_position() {
		return r12_net_forward_position;
	}

	public void setR12_net_forward_position(BigDecimal r12_net_forward_position) {
		this.r12_net_forward_position = r12_net_forward_position;
	}

	public BigDecimal getR12_guarantees() {
		return r12_guarantees;
	}

	public void setR12_guarantees(BigDecimal r12_guarantees) {
		this.r12_guarantees = r12_guarantees;
	}

	public BigDecimal getR12_net_future_inc_or_exp() {
		return r12_net_future_inc_or_exp;
	}

	public void setR12_net_future_inc_or_exp(BigDecimal r12_net_future_inc_or_exp) {
		this.r12_net_future_inc_or_exp = r12_net_future_inc_or_exp;
	}

	public BigDecimal getR12_net_delta_wei_fx_opt_posi() {
		return r12_net_delta_wei_fx_opt_posi;
	}

	public void setR12_net_delta_wei_fx_opt_posi(BigDecimal r12_net_delta_wei_fx_opt_posi) {
		this.r12_net_delta_wei_fx_opt_posi = r12_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR12_other_items() {
		return r12_other_items;
	}

	public void setR12_other_items(BigDecimal r12_other_items) {
		this.r12_other_items = r12_other_items;
	}

	public BigDecimal getR12_net_long_position() {
		return r12_net_long_position;
	}

	public void setR12_net_long_position(BigDecimal r12_net_long_position) {
		this.r12_net_long_position = r12_net_long_position;
	}

	public BigDecimal getR12_or() {
		return r12_or;
	}

	public void setR12_or(BigDecimal r12_or) {
		this.r12_or = r12_or;
	}

	public BigDecimal getR12_net_short_position() {
		return r12_net_short_position;
	}

	public void setR12_net_short_position(BigDecimal r12_net_short_position) {
		this.r12_net_short_position = r12_net_short_position;
	}

	public BigDecimal getR13_net_spot_position() {
		return r13_net_spot_position;
	}

	public void setR13_net_spot_position(BigDecimal r13_net_spot_position) {
		this.r13_net_spot_position = r13_net_spot_position;
	}

	public BigDecimal getR13_net_forward_position() {
		return r13_net_forward_position;
	}

	public void setR13_net_forward_position(BigDecimal r13_net_forward_position) {
		this.r13_net_forward_position = r13_net_forward_position;
	}

	public BigDecimal getR13_guarantees() {
		return r13_guarantees;
	}

	public void setR13_guarantees(BigDecimal r13_guarantees) {
		this.r13_guarantees = r13_guarantees;
	}

	public BigDecimal getR13_net_future_inc_or_exp() {
		return r13_net_future_inc_or_exp;
	}

	public void setR13_net_future_inc_or_exp(BigDecimal r13_net_future_inc_or_exp) {
		this.r13_net_future_inc_or_exp = r13_net_future_inc_or_exp;
	}

	public BigDecimal getR13_net_delta_wei_fx_opt_posi() {
		return r13_net_delta_wei_fx_opt_posi;
	}

	public void setR13_net_delta_wei_fx_opt_posi(BigDecimal r13_net_delta_wei_fx_opt_posi) {
		this.r13_net_delta_wei_fx_opt_posi = r13_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR13_other_items() {
		return r13_other_items;
	}

	public void setR13_other_items(BigDecimal r13_other_items) {
		this.r13_other_items = r13_other_items;
	}

	public BigDecimal getR13_net_long_position() {
		return r13_net_long_position;
	}

	public void setR13_net_long_position(BigDecimal r13_net_long_position) {
		this.r13_net_long_position = r13_net_long_position;
	}

	public BigDecimal getR13_or() {
		return r13_or;
	}

	public void setR13_or(BigDecimal r13_or) {
		this.r13_or = r13_or;
	}

	public BigDecimal getR13_net_short_position() {
		return r13_net_short_position;
	}

	public void setR13_net_short_position(BigDecimal r13_net_short_position) {
		this.r13_net_short_position = r13_net_short_position;
	}

	public BigDecimal getR14_net_spot_position() {
		return r14_net_spot_position;
	}

	public void setR14_net_spot_position(BigDecimal r14_net_spot_position) {
		this.r14_net_spot_position = r14_net_spot_position;
	}

	public BigDecimal getR14_net_forward_position() {
		return r14_net_forward_position;
	}

	public void setR14_net_forward_position(BigDecimal r14_net_forward_position) {
		this.r14_net_forward_position = r14_net_forward_position;
	}

	public BigDecimal getR14_guarantees() {
		return r14_guarantees;
	}

	public void setR14_guarantees(BigDecimal r14_guarantees) {
		this.r14_guarantees = r14_guarantees;
	}

	public BigDecimal getR14_net_future_inc_or_exp() {
		return r14_net_future_inc_or_exp;
	}

	public void setR14_net_future_inc_or_exp(BigDecimal r14_net_future_inc_or_exp) {
		this.r14_net_future_inc_or_exp = r14_net_future_inc_or_exp;
	}

	public BigDecimal getR14_net_delta_wei_fx_opt_posi() {
		return r14_net_delta_wei_fx_opt_posi;
	}

	public void setR14_net_delta_wei_fx_opt_posi(BigDecimal r14_net_delta_wei_fx_opt_posi) {
		this.r14_net_delta_wei_fx_opt_posi = r14_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR14_other_items() {
		return r14_other_items;
	}

	public void setR14_other_items(BigDecimal r14_other_items) {
		this.r14_other_items = r14_other_items;
	}

	public BigDecimal getR14_net_long_position() {
		return r14_net_long_position;
	}

	public void setR14_net_long_position(BigDecimal r14_net_long_position) {
		this.r14_net_long_position = r14_net_long_position;
	}

	public BigDecimal getR14_or() {
		return r14_or;
	}

	public void setR14_or(BigDecimal r14_or) {
		this.r14_or = r14_or;
	}

	public BigDecimal getR14_net_short_position() {
		return r14_net_short_position;
	}

	public void setR14_net_short_position(BigDecimal r14_net_short_position) {
		this.r14_net_short_position = r14_net_short_position;
	}

	public BigDecimal getR15_net_spot_position() {
		return r15_net_spot_position;
	}

	public void setR15_net_spot_position(BigDecimal r15_net_spot_position) {
		this.r15_net_spot_position = r15_net_spot_position;
	}

	public BigDecimal getR15_net_forward_position() {
		return r15_net_forward_position;
	}

	public void setR15_net_forward_position(BigDecimal r15_net_forward_position) {
		this.r15_net_forward_position = r15_net_forward_position;
	}

	public BigDecimal getR15_guarantees() {
		return r15_guarantees;
	}

	public void setR15_guarantees(BigDecimal r15_guarantees) {
		this.r15_guarantees = r15_guarantees;
	}

	public BigDecimal getR15_net_future_inc_or_exp() {
		return r15_net_future_inc_or_exp;
	}

	public void setR15_net_future_inc_or_exp(BigDecimal r15_net_future_inc_or_exp) {
		this.r15_net_future_inc_or_exp = r15_net_future_inc_or_exp;
	}

	public BigDecimal getR15_net_delta_wei_fx_opt_posi() {
		return r15_net_delta_wei_fx_opt_posi;
	}

	public void setR15_net_delta_wei_fx_opt_posi(BigDecimal r15_net_delta_wei_fx_opt_posi) {
		this.r15_net_delta_wei_fx_opt_posi = r15_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR15_other_items() {
		return r15_other_items;
	}

	public void setR15_other_items(BigDecimal r15_other_items) {
		this.r15_other_items = r15_other_items;
	}

	public BigDecimal getR15_net_long_position() {
		return r15_net_long_position;
	}

	public void setR15_net_long_position(BigDecimal r15_net_long_position) {
		this.r15_net_long_position = r15_net_long_position;
	}

	public BigDecimal getR15_or() {
		return r15_or;
	}

	public void setR15_or(BigDecimal r15_or) {
		this.r15_or = r15_or;
	}

	public BigDecimal getR15_net_short_position() {
		return r15_net_short_position;
	}

	public void setR15_net_short_position(BigDecimal r15_net_short_position) {
		this.r15_net_short_position = r15_net_short_position;
	}

	public BigDecimal getR16_net_spot_position() {
		return r16_net_spot_position;
	}

	public void setR16_net_spot_position(BigDecimal r16_net_spot_position) {
		this.r16_net_spot_position = r16_net_spot_position;
	}

	public BigDecimal getR16_net_forward_position() {
		return r16_net_forward_position;
	}

	public void setR16_net_forward_position(BigDecimal r16_net_forward_position) {
		this.r16_net_forward_position = r16_net_forward_position;
	}

	public BigDecimal getR16_guarantees() {
		return r16_guarantees;
	}

	public void setR16_guarantees(BigDecimal r16_guarantees) {
		this.r16_guarantees = r16_guarantees;
	}

	public BigDecimal getR16_net_future_inc_or_exp() {
		return r16_net_future_inc_or_exp;
	}

	public void setR16_net_future_inc_or_exp(BigDecimal r16_net_future_inc_or_exp) {
		this.r16_net_future_inc_or_exp = r16_net_future_inc_or_exp;
	}

	public BigDecimal getR16_net_delta_wei_fx_opt_posi() {
		return r16_net_delta_wei_fx_opt_posi;
	}

	public void setR16_net_delta_wei_fx_opt_posi(BigDecimal r16_net_delta_wei_fx_opt_posi) {
		this.r16_net_delta_wei_fx_opt_posi = r16_net_delta_wei_fx_opt_posi;
	}

	public BigDecimal getR16_other_items() {
		return r16_other_items;
	}

	public void setR16_other_items(BigDecimal r16_other_items) {
		this.r16_other_items = r16_other_items;
	}

	public BigDecimal getR16_net_long_position() {
		return r16_net_long_position;
	}

	public void setR16_net_long_position(BigDecimal r16_net_long_position) {
		this.r16_net_long_position = r16_net_long_position;
	}

	public BigDecimal getR16_or() {
		return r16_or;
	}

	public void setR16_or(BigDecimal r16_or) {
		this.r16_or = r16_or;
	}

	public BigDecimal getR16_net_short_position() {
		return r16_net_short_position;
	}

	public void setR16_net_short_position(BigDecimal r16_net_short_position) {
		this.r16_net_short_position = r16_net_short_position;
	}

	public BigDecimal getR17_net_long_position() {
		return r17_net_long_position;
	}

	public void setR17_net_long_position(BigDecimal r17_net_long_position) {
		this.r17_net_long_position = r17_net_long_position;
	}

	public BigDecimal getR17_or() {
		return r17_or;
	}

	public void setR17_or(BigDecimal r17_or) {
		this.r17_or = r17_or;
	}

	public BigDecimal getR17_net_short_position() {
		return r17_net_short_position;
	}

	public void setR17_net_short_position(BigDecimal r17_net_short_position) {
		this.r17_net_short_position = r17_net_short_position;
	}

	public BigDecimal getR21_long() {
		return r21_long;
	}

	public void setR21_long(BigDecimal r21_long) {
		this.r21_long = r21_long;
	}

	public BigDecimal getR21_short() {
		return r21_short;
	}

	public void setR21_short(BigDecimal r21_short) {
		this.r21_short = r21_short;
	}

	public BigDecimal getR21_total_gross_long_short() {
		return r21_total_gross_long_short;
	}

	public void setR21_total_gross_long_short(BigDecimal r21_total_gross_long_short) {
		this.r21_total_gross_long_short = r21_total_gross_long_short;
	}

	public BigDecimal getR21_net_position() {
		return r21_net_position;
	}

	public void setR21_net_position(BigDecimal r21_net_position) {
		this.r21_net_position = r21_net_position;
	}

	public BigDecimal getR22_long() {
		return r22_long;
	}

	public void setR22_long(BigDecimal r22_long) {
		this.r22_long = r22_long;
	}

	public BigDecimal getR22_short() {
		return r22_short;
	}

	public void setR22_short(BigDecimal r22_short) {
		this.r22_short = r22_short;
	}

	public BigDecimal getR22_total_gross_long_short() {
		return r22_total_gross_long_short;
	}

	public void setR22_total_gross_long_short(BigDecimal r22_total_gross_long_short) {
		this.r22_total_gross_long_short = r22_total_gross_long_short;
	}

	public BigDecimal getR22_net_position() {
		return r22_net_position;
	}

	public void setR22_net_position(BigDecimal r22_net_position) {
		this.r22_net_position = r22_net_position;
	}

	public BigDecimal getR23_net_position() {
		return r23_net_position;
	}

	public void setR23_net_position(BigDecimal r23_net_position) {
		this.r23_net_position = r23_net_position;
	}

	public BigDecimal getR29_greater_net_long_or_short() {
		return r29_greater_net_long_or_short;
	}

	public void setR29_greater_net_long_or_short(BigDecimal r29_greater_net_long_or_short) {
		this.r29_greater_net_long_or_short = r29_greater_net_long_or_short;
	}

	public BigDecimal getR29_abs_value_net_gold_posi() {
		return r29_abs_value_net_gold_posi;
	}

	public void setR29_abs_value_net_gold_posi(BigDecimal r29_abs_value_net_gold_posi) {
		this.r29_abs_value_net_gold_posi = r29_abs_value_net_gold_posi;
	}

	public BigDecimal getR29_capital_charge() {
		return r29_capital_charge;
	}

	public void setR29_capital_charge(BigDecimal r29_capital_charge) {
		this.r29_capital_charge = r29_capital_charge;
	}

	public BigDecimal getR30_capital_require() {
		return r30_capital_require;
	}

	public void setR30_capital_require(BigDecimal r30_capital_require) {
		this.r30_capital_require = r30_capital_require;
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

	public M_FXR_Resub_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getReportResubDate() {
		return reportResubDate;
	}

	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
	}
}



}
