
package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service
@Transactional
public class BRRS_M_CA6_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA6_ReportService.class);

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

	private static final String TBL_SUMMARY1 = "BRRS_M_CA6_SUMMARYTABLE1";
	private static final String TBL_SUMMARY2 = "BRRS_M_CA6_SUMMARYTABLE2";
	private static final String TBL_DETAIL1 = "BRRS_M_CA6_DETAILTABLE1";
	private static final String TBL_DETAIL2 = "BRRS_M_CA6_DETAILTABLE2";
	private static final String TBL_ARCH_SUMMARY1 = "BRRS_M_CA6_ARCHIVALTABLE_SUMMARY1";
	private static final String TBL_ARCH_SUMMARY2 = "BRRS_M_CA6_ARCHIVALTABLE_SUMMARY2";
	private static final String TBL_ARCH_DETAIL1 = "BRRS_M_CA6_ARCHIVALTABLE_DETAIL1";
	private static final String TBL_ARCH_DETAIL2 = "BRRS_M_CA6_ARCHIVALTABLE_DETAIL2";
	private static final String TBL_RESUB_SUMMARY1 = "BRRS_M_CA6_RESUB_SUMMARYTABLE1";
	private static final String TBL_RESUB_SUMMARY2 = "BRRS_M_CA6_RESUB_SUMMARYTABLE2";
	private static final String TBL_RESUB_DETAIL1 = "BRRS_M_CA6_RESUB_DETAILTABLE1";
	private static final String TBL_RESUB_DETAIL2 = "BRRS_M_CA6_RESUB_DETAILTABLE2";

	private <T> List<T> getByDate(String tableName, Date reportDate, Class<T> type) {
		String sql = "SELECT * FROM " + tableName + " WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type), reportDate);
	}

	private <T> Optional<T> getOneByDate(String tableName, Date reportDate, Class<T> type) {
		List<T> rows = getByDate(tableName, reportDate, type);
		return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
	}

	private <T> List<T> getByDateAndVersion(String tableName, Date reportDate, BigDecimal reportVersion, Class<T> type) {
		String sql = "SELECT * FROM " + tableName + " WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type), reportDate, reportVersion);
	}

	private <T> Optional<T> getLatestByDate(String tableName, Date reportDate, Class<T> type) {
		String sql = "SELECT * FROM " + tableName
				+ " WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<T> rows = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type), reportDate);
		return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
	}

	private <T> List<T> getAllWithVersion(String tableName, Class<T> type) {
		String sql = "SELECT * FROM " + tableName + " WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type));
	}

	private BigDecimal findMaxVersion(String tableName, Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM " + tableName + " WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, BigDecimal.class, reportDate);
	}

	private boolean existsByDateAndVersion(String tableName, Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, reportDate, reportVersion);
		return count != null && count > 0;
	}

	private void saveEntity(String tableName, Object entity, String... keyFields) {
		List<Field> fields = getPersistentFields(entity.getClass());
		List<Object> values = new ArrayList<>();
		List<String> columns = new ArrayList<>();

		for (Field field : fields) {
			field.setAccessible(true);
			columns.add(columnName(field.getName()));
			try {
				values.add(field.get(entity));
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Unable to read field " + field.getName(), e);
			}
		}

		String updateSql = buildUpdateSql(tableName, fields, keyFields);
		List<Object> updateArgs = buildUpdateArgs(entity, fields, keyFields);
		int updated = jdbcTemplate.update(updateSql, updateArgs.toArray());
		if (updated > 0) {
			return;
		}

		String placeholders = String.join(", ", Collections.nCopies(columns.size(), "?"));
		String insertSql = "INSERT INTO " + tableName + " (" + String.join(", ", columns) + ") VALUES (" + placeholders + ")";
		jdbcTemplate.update(insertSql, values.toArray());
	}

	private String buildUpdateSql(String tableName, List<Field> fields, String... keyFields) {
		List<String> keys = java.util.Arrays.asList(keyFields);
		List<String> setColumns = new ArrayList<>();
		List<String> whereColumns = new ArrayList<>();
		for (Field field : fields) {
			String column = columnName(field.getName());
			if (keys.contains(field.getName())) {
				whereColumns.add(column + " = ?");
			} else {
				setColumns.add(column + " = ?");
			}
		}
		return "UPDATE " + tableName + " SET " + String.join(", ", setColumns) + " WHERE " + String.join(" AND ", whereColumns);
	}

	private List<Object> buildUpdateArgs(Object entity, List<Field> fields, String... keyFields) {
		List<String> keys = java.util.Arrays.asList(keyFields);
		List<Object> args = new ArrayList<>();
		try {
			for (Field field : fields) {
				if (!keys.contains(field.getName())) {
					field.setAccessible(true);
					args.add(field.get(entity));
				}
			}
			for (Field field : fields) {
				if (keys.contains(field.getName())) {
					field.setAccessible(true);
					args.add(field.get(entity));
				}
			}
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Unable to build JDBC arguments", e);
		}
		return args;
	}

	private List<Field> getPersistentFields(Class<?> type) {
		List<Field> fields = new ArrayList<>();
		for (Field field : type.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				fields.add(field);
			}
		}
		return fields;
	}

	private String columnName(String fieldName) {
		return fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
	}

	public ModelAndView getM_CA6View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {

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

				List<M_CA6_Archival_Summary_Entity1> T1Master = getByDateAndVersion(TBL_ARCH_SUMMARY1, d1, version, M_CA6_Archival_Summary_Entity1.class);
				List<M_CA6_Archival_Summary_Entity2> T2Master = getByDateAndVersion(TBL_ARCH_SUMMARY2, d1, version, M_CA6_Archival_Summary_Entity2.class);

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("displaymode", "summary");

			}
			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_CA6_RESUB_Summary_Entity1> T1Master = getByDateAndVersion(TBL_RESUB_SUMMARY1, d1, version, M_CA6_RESUB_Summary_Entity1.class);
				List<M_CA6_RESUB_Summary_Entity2> T2Master = getByDateAndVersion(TBL_RESUB_SUMMARY2, d1, version, M_CA6_RESUB_Summary_Entity2.class);
				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("displaymode", "resubSummary");
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_CA6_Summary_Entity1> T1Master = getByDate(TBL_SUMMARY1, d1, M_CA6_Summary_Entity1.class);
				List<M_CA6_Summary_Entity2> T2Master = getByDate(TBL_SUMMARY2, d1, M_CA6_Summary_Entity2.class);

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

					List<M_CA6_Archival_Detail_Entity1> T1Master = getByDateAndVersion(TBL_ARCH_DETAIL1, dateformat.parse(todate), version, M_CA6_Archival_Detail_Entity1.class);
					List<M_CA6_Archival_Detail_Entity2> T2Master = getByDateAndVersion(TBL_ARCH_DETAIL2, dateformat.parse(todate), version, M_CA6_Archival_Detail_Entity2.class);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
					mv.addObject("reportsummary1", T2Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_CA6_RESUB_Detail_Entity1> T1Master = getByDateAndVersion(TBL_RESUB_DETAIL1, dateformat.parse(todate), version, M_CA6_RESUB_Detail_Entity1.class);
					List<M_CA6_RESUB_Detail_Entity2> T2Master = getByDateAndVersion(TBL_RESUB_DETAIL2, dateformat.parse(todate), version, M_CA6_RESUB_Detail_Entity2.class);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
					mv.addObject("reportsummary1", T2Master);

				}
				// DETAIL + NORMAL
				else {

					List<M_CA6_Detail_Entity1> T1Master = getByDate(TBL_DETAIL1, dateformat.parse(todate), M_CA6_Detail_Entity1.class);
					List<M_CA6_Detail_Entity2> T2Master = getByDate(TBL_DETAIL2, dateformat.parse(todate), M_CA6_Detail_Entity2.class);

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

		BigDecimal maxResubVer = findMaxVersion(TBL_RESUB_SUMMARY1, reportDate1);
		BigDecimal maxResubVer2 = findMaxVersion(TBL_RESUB_SUMMARY2, reportDate2);

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

		saveEntity(TBL_RESUB_SUMMARY1, resubSummary, "reportDate", "reportVersion");
		saveEntity(TBL_RESUB_SUMMARY2, resubSummary2, "reportDate", "reportVersion");

		saveEntity(TBL_RESUB_DETAIL1, resubDetail, "reportDate", "reportVersion");
		saveEntity(TBL_RESUB_DETAIL2, resubDetail2, "reportDate", "reportVersion");

		saveEntity(TBL_ARCH_SUMMARY1, archSummary, "reportDate", "reportVersion");
		saveEntity(TBL_ARCH_SUMMARY2, archSummary2, "reportDate", "reportVersion");

		saveEntity(TBL_ARCH_DETAIL1, archDetail, "reportDate", "reportVersion");
		saveEntity(TBL_ARCH_DETAIL2, archDetail2, "reportDate", "reportVersion");

	}

	public void updateReport1(M_CA6_Summary_Entity1 entity1) {
		System.out.println("Report Date: " + entity1.getReportDate());

		M_CA6_Summary_Entity1 existing = getOneByDate(TBL_SUMMARY1, entity1.getReportDate(), M_CA6_Summary_Entity1.class)
				.orElseThrow(
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
		saveEntity(TBL_SUMMARY1, existing, "reportDate");
	}

	public void updatedetail1(M_CA6_Detail_Entity1 entity1) {
		System.out.println("Report Date: " + entity1.getReportDate());

		M_CA6_Detail_Entity1 existing = getOneByDate(TBL_DETAIL1, entity1.getReportDate(), M_CA6_Detail_Entity1.class)
				.orElseThrow(
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
		saveEntity(TBL_DETAIL1, existing, "reportDate");
	}

	public void updateReport2(M_CA6_Summary_Entity2 entity) {
		System.out.println("Report Date: " + entity.getReportDate());

		M_CA6_Summary_Entity2 existing = getOneByDate(TBL_SUMMARY2, entity.getReportDate(), M_CA6_Summary_Entity2.class)
				
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
		saveEntity(TBL_SUMMARY2, existing, "reportDate");
	}

	public void updateDetial2(M_CA6_Detail_Entity2 entity) {
		System.out.println("Report Date: " + entity.getReportDate());

		M_CA6_Detail_Entity2 existing = getOneByDate(TBL_DETAIL2, entity.getReportDate(), M_CA6_Detail_Entity2.class)
				
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
		saveEntity(TBL_DETAIL2, existing, "reportDate");
	}

////////////////////////////////////////// RESUBMISSION///////////////////////////////////////////////////////////////////
/// Report Date | Report Version | Domain
/// RESUB VIEW

	public List<Object[]> getM_CA6Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA6_Archival_Summary_Entity1> latestArchivalList = getAllWithVersion(TBL_ARCH_SUMMARY1, M_CA6_Archival_Summary_Entity1.class);

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
			List<M_CA6_Archival_Summary_Entity1> latestArchivalList = getAllWithVersion(TBL_ARCH_SUMMARY1, M_CA6_Archival_Summary_Entity1.class);

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA6_Archival_Summary_Entity1 entity : latestArchivalList) {
					archivalList.add(new Object[] { 
							entity.getReportDate(), 
							entity.getReportVersion(),
							entity.getReportResubDate()});
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
			Optional<M_CA6_Archival_Summary_Entity1> latestArchivalOpt1 = getLatestByDate(TBL_ARCH_SUMMARY1, reportDate, M_CA6_Archival_Summary_Entity1.class);

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
			boolean exists = existsByDateAndVersion(TBL_ARCH_SUMMARY1, reportDate, newVersion);

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
			saveEntity(TBL_ARCH_SUMMARY1, archivalEntity1, "reportDate", "reportVersion");
			saveEntity(TBL_ARCH_SUMMARY2, archivalEntity2, "reportDate", "reportVersion");

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
				
			
		List<M_CA6_Summary_Entity1> dataList = getByDate(TBL_SUMMARY1, dateformat.parse(todate), M_CA6_Summary_Entity1.class);

		List<M_CA6_Summary_Entity2> dataList1 = getByDate(TBL_SUMMARY2, dateformat.parse(todate), M_CA6_Summary_Entity2.class);

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


			   try {

			       // Row 5 = Excel row 6
			       Row dateRow = sheet.getRow(5);

			       if (dateRow == null) {
			           dateRow = sheet.createRow(5);
			       }

			       // Column 2 = Excel column C
			       Cell dateCell = dateRow.getCell(2);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(2);
			       }

			       // Date conversion
			       SimpleDateFormat inputFormat =
			               new SimpleDateFormat("dd-MMM-yyyy");

			       SimpleDateFormat outputFormat =
			               new SimpleDateFormat("dd/MM/yyyy");

			       Date reportDateValue =
			               inputFormat.parse(todate);

			       // Set formatted date
			       dateCell.setCellValue(
			               outputFormat.format(reportDateValue));

			       dateCell.setCellStyle(textStyle);

			   } catch (ParseException e) {

			       logger.error("Error parsing todate: {}", todate, e);
			   }
			   
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA6 SUMMARY", null, "BRRS_M_CA6_SUMMARYTABLE");
			}
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

		List<M_CA6_Summary_Entity1> dataList = getByDate(TBL_SUMMARY1, dateformat.parse(todate), M_CA6_Summary_Entity1.class);

		List<M_CA6_Summary_Entity2> dataList1 = getByDate(TBL_SUMMARY2, dateformat.parse(todate), M_CA6_Summary_Entity2.class);

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


			   try {

			       // Row 5 = Excel row 6
			       Row dateRow = sheet.getRow(5);

			       if (dateRow == null) {
			           dateRow = sheet.createRow(5);
			       }

			       // Column 2 = Excel column C
			       Cell dateCell = dateRow.getCell(2);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(2);
			       }

			       // Date conversion
			       SimpleDateFormat inputFormat =
			               new SimpleDateFormat("dd-MMM-yyyy");

			       SimpleDateFormat outputFormat =
			               new SimpleDateFormat("dd/MM/yyyy");

			       Date reportDateValue =
			               inputFormat.parse(todate);

			       // Set formatted date
			       dateCell.setCellValue(
			               outputFormat.format(reportDateValue));

			       dateCell.setCellStyle(textStyle);

			   } catch (ParseException e) {

			       logger.error("Error parsing todate: {}", todate, e);
			   }
			   
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA6 EMAIL SUMMARY", null, "BRRS_M_CA6_SUMMARYTABLE");
			}
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

		List<M_CA6_Archival_Summary_Entity1> dataList = getByDateAndVersion(TBL_ARCH_SUMMARY1, dateformat.parse(todate), version, M_CA6_Archival_Summary_Entity1.class);
		List<M_CA6_Archival_Summary_Entity2> dataList1 = getByDateAndVersion(TBL_ARCH_SUMMARY2, dateformat.parse(todate), version, M_CA6_Archival_Summary_Entity2.class);

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


			   try {

			       // Row 5 = Excel row 6
			       Row dateRow = sheet.getRow(5);

			       if (dateRow == null) {
			           dateRow = sheet.createRow(5);
			       }

			       // Column 2 = Excel column C
			       Cell dateCell = dateRow.getCell(2);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(2);
			       }

			       // Date conversion
			       SimpleDateFormat inputFormat =
			               new SimpleDateFormat("dd-MMM-yyyy");

			       SimpleDateFormat outputFormat =
			               new SimpleDateFormat("dd/MM/yyyy");

			       Date reportDateValue =
			               inputFormat.parse(todate);

			       // Set formatted date
			       dateCell.setCellValue(
			               outputFormat.format(reportDateValue));

			       dateCell.setCellStyle(textStyle);

			   } catch (ParseException e) {

			       logger.error("Error parsing todate: {}", todate, e);
			   }
			   
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA6 ARCHIVAL SUMMARY", null, "BRRS_M_CA6_ARCHIVALTABLE_SUMMARY");
			}
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

		List<M_CA6_Archival_Summary_Entity1> dataList = getByDateAndVersion(TBL_ARCH_SUMMARY1, dateformat.parse(todate), version, M_CA6_Archival_Summary_Entity1.class);

		List<M_CA6_Archival_Summary_Entity2> dataList1 = getByDateAndVersion(TBL_ARCH_SUMMARY2, dateformat.parse(todate), version, M_CA6_Archival_Summary_Entity2.class);

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


			   try {

			       // Row 5 = Excel row 6
			       Row dateRow = sheet.getRow(5);

			       if (dateRow == null) {
			           dateRow = sheet.createRow(5);
			       }

			       // Column 2 = Excel column C
			       Cell dateCell = dateRow.getCell(2);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(2);
			       }

			       // Date conversion
			       SimpleDateFormat inputFormat =
			               new SimpleDateFormat("dd-MMM-yyyy");

			       SimpleDateFormat outputFormat =
			               new SimpleDateFormat("dd/MM/yyyy");

			       Date reportDateValue =
			               inputFormat.parse(todate);

			       // Set formatted date
			       dateCell.setCellValue(
			               outputFormat.format(reportDateValue));

			       dateCell.setCellStyle(textStyle);

			   } catch (ParseException e) {

			       logger.error("Error parsing todate: {}", todate, e);
			   }
			   
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA6 EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_CA6_ARCHIVALTABLE_SUMMARY");
			}
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
		

		List<M_CA6_RESUB_Summary_Entity1> dataList = getByDateAndVersion(TBL_RESUB_SUMMARY1, dateformat.parse(todate), version, M_CA6_RESUB_Summary_Entity1.class);
		List<M_CA6_RESUB_Summary_Entity2> dataList1 = getByDateAndVersion(TBL_RESUB_SUMMARY2, dateformat.parse(todate), version, M_CA6_RESUB_Summary_Entity2.class);

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


			   try {

			       // Row 5 = Excel row 6
			       Row dateRow = sheet.getRow(5);

			       if (dateRow == null) {
			           dateRow = sheet.createRow(5);
			       }

			       // Column 2 = Excel column C
			       Cell dateCell = dateRow.getCell(2);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(2);
			       }

			       // Date conversion
			       SimpleDateFormat inputFormat =
			               new SimpleDateFormat("dd-MMM-yyyy");

			       SimpleDateFormat outputFormat =
			               new SimpleDateFormat("dd/MM/yyyy");

			       Date reportDateValue =
			               inputFormat.parse(todate);

			       // Set formatted date
			       dateCell.setCellValue(
			               outputFormat.format(reportDateValue));

			       dateCell.setCellStyle(textStyle);

			   } catch (ParseException e) {

			       logger.error("Error parsing todate: {}", todate, e);
			   }
			   
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA6 RESUB SUMMARY", null, "BRRS_M_CA6_RESUB_SUMMARYTABLE");
			}
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

		List<M_CA6_RESUB_Summary_Entity1> dataList = getByDateAndVersion(TBL_RESUB_SUMMARY1, dateformat.parse(todate), version, M_CA6_RESUB_Summary_Entity1.class);

		List<M_CA6_RESUB_Summary_Entity2> dataList1 = getByDateAndVersion(TBL_RESUB_SUMMARY2, dateformat.parse(todate), version, M_CA6_RESUB_Summary_Entity2.class);

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


			   try {

			       // Row 5 = Excel row 6
			       Row dateRow = sheet.getRow(5);

			       if (dateRow == null) {
			           dateRow = sheet.createRow(5);
			       }

			       // Column 2 = Excel column C
			       Cell dateCell = dateRow.getCell(2);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(2);
			       }

			       // Date conversion
			       SimpleDateFormat inputFormat =
			               new SimpleDateFormat("dd-MMM-yyyy");

			       SimpleDateFormat outputFormat =
			               new SimpleDateFormat("dd/MM/yyyy");

			       Date reportDateValue =
			               inputFormat.parse(todate);

			       // Set formatted date
			       dateCell.setCellValue(
			               outputFormat.format(reportDateValue));

			       dateCell.setCellStyle(textStyle);

			   } catch (ParseException e) {

			       logger.error("Error parsing todate: {}", todate, e);
			   }
			   
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
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA6 EMAIL RESUB SUMMARY", null, "BRRS_M_CA6_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// M_CA6 DTO/entity classes are kept inside this service, matching the ADISB1 one-service pattern.

	public static class M_CA6_Summary_Entity1 {
		
		private Date R12_CALENDAR_YEAR;
	    private BigDecimal R12_CAP;
	    private BigDecimal R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R13_CALENDAR_YEAR;
	    private BigDecimal R13_CAP;
	    private BigDecimal R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R14_CALENDAR_YEAR;
	    private BigDecimal R14_CAP;
	    private BigDecimal R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R15_CALENDAR_YEAR;
	    private BigDecimal R15_CAP;
	    private BigDecimal R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R16_CALENDAR_YEAR;
	    private BigDecimal R16_CAP;
	    private BigDecimal R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R20_CALENDAR_YEAR;
	    private BigDecimal R20_CAP;
	    private BigDecimal R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R21_CALENDAR_YEAR;
	    private BigDecimal R21_CAP;
	    private BigDecimal R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R22_CALENDAR_YEAR;
	    private BigDecimal R22_CAP;
	    private BigDecimal R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R23_CALENDAR_YEAR;
	    private BigDecimal R23_CAP;
	    private BigDecimal R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R24_CALENDAR_YEAR;
	    private BigDecimal R24_CAP;
	    private BigDecimal R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
	    public String REPORT_FREQUENCY;
	    public String REPORT_CODE;
	    public String REPORT_DESC;
	    public String ENTITY_FLG;
	    public String MODIFY_FLG;
	    public String DEL_FLG;
		public Date getR12_CALENDAR_YEAR() {
			return R12_CALENDAR_YEAR;
		}
		public void setR12_CALENDAR_YEAR(Date r12_CALENDAR_YEAR) {
			R12_CALENDAR_YEAR = r12_CALENDAR_YEAR;
		}
		public BigDecimal getR12_CAP() {
			return R12_CAP;
		}
		public void setR12_CAP(BigDecimal r12_CAP) {
			R12_CAP = r12_CAP;
		}
		public BigDecimal getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR13_CALENDAR_YEAR() {
			return R13_CALENDAR_YEAR;
		}
		public void setR13_CALENDAR_YEAR(Date r13_CALENDAR_YEAR) {
			R13_CALENDAR_YEAR = r13_CALENDAR_YEAR;
		}
		public BigDecimal getR13_CAP() {
			return R13_CAP;
		}
		public void setR13_CAP(BigDecimal r13_CAP) {
			R13_CAP = r13_CAP;
		}
		public BigDecimal getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR14_CALENDAR_YEAR() {
			return R14_CALENDAR_YEAR;
		}
		public void setR14_CALENDAR_YEAR(Date r14_CALENDAR_YEAR) {
			R14_CALENDAR_YEAR = r14_CALENDAR_YEAR;
		}
		public BigDecimal getR14_CAP() {
			return R14_CAP;
		}
		public void setR14_CAP(BigDecimal r14_CAP) {
			R14_CAP = r14_CAP;
		}
		public BigDecimal getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR15_CALENDAR_YEAR() {
			return R15_CALENDAR_YEAR;
		}
		public void setR15_CALENDAR_YEAR(Date r15_CALENDAR_YEAR) {
			R15_CALENDAR_YEAR = r15_CALENDAR_YEAR;
		}
		public BigDecimal getR15_CAP() {
			return R15_CAP;
		}
		public void setR15_CAP(BigDecimal r15_CAP) {
			R15_CAP = r15_CAP;
		}
		public BigDecimal getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR16_CALENDAR_YEAR() {
			return R16_CALENDAR_YEAR;
		}
		public void setR16_CALENDAR_YEAR(Date r16_CALENDAR_YEAR) {
			R16_CALENDAR_YEAR = r16_CALENDAR_YEAR;
		}
		public BigDecimal getR16_CAP() {
			return R16_CAP;
		}
		public void setR16_CAP(BigDecimal r16_CAP) {
			R16_CAP = r16_CAP;
		}
		public BigDecimal getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR20_CALENDAR_YEAR() {
			return R20_CALENDAR_YEAR;
		}
		public void setR20_CALENDAR_YEAR(Date r20_CALENDAR_YEAR) {
			R20_CALENDAR_YEAR = r20_CALENDAR_YEAR;
		}
		public BigDecimal getR20_CAP() {
			return R20_CAP;
		}
		public void setR20_CAP(BigDecimal r20_CAP) {
			R20_CAP = r20_CAP;
		}
		public BigDecimal getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR21_CALENDAR_YEAR() {
			return R21_CALENDAR_YEAR;
		}
		public void setR21_CALENDAR_YEAR(Date r21_CALENDAR_YEAR) {
			R21_CALENDAR_YEAR = r21_CALENDAR_YEAR;
		}
		public BigDecimal getR21_CAP() {
			return R21_CAP;
		}
		public void setR21_CAP(BigDecimal r21_CAP) {
			R21_CAP = r21_CAP;
		}
		public BigDecimal getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR22_CALENDAR_YEAR() {
			return R22_CALENDAR_YEAR;
		}
		public void setR22_CALENDAR_YEAR(Date r22_CALENDAR_YEAR) {
			R22_CALENDAR_YEAR = r22_CALENDAR_YEAR;
		}
		public BigDecimal getR22_CAP() {
			return R22_CAP;
		}
		public void setR22_CAP(BigDecimal r22_CAP) {
			R22_CAP = r22_CAP;
		}
		public BigDecimal getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR23_CALENDAR_YEAR() {
			return R23_CALENDAR_YEAR;
		}
		public void setR23_CALENDAR_YEAR(Date r23_CALENDAR_YEAR) {
			R23_CALENDAR_YEAR = r23_CALENDAR_YEAR;
		}
		public BigDecimal getR23_CAP() {
			return R23_CAP;
		}
		public void setR23_CAP(BigDecimal r23_CAP) {
			R23_CAP = r23_CAP;
		}
		public BigDecimal getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR24_CALENDAR_YEAR() {
			return R24_CALENDAR_YEAR;
		}
		public void setR24_CALENDAR_YEAR(Date r24_CALENDAR_YEAR) {
			R24_CALENDAR_YEAR = r24_CALENDAR_YEAR;
		}
		public BigDecimal getR24_CAP() {
			return R24_CAP;
		}
		public void setR24_CAP(BigDecimal r24_CAP) {
			R24_CAP = r24_CAP;
		}
		public BigDecimal getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
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
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
		public String getDEL_FLG() {
			return DEL_FLG;
		}
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
		public M_CA6_Summary_Entity1() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	
	}


	public static class M_CA6_Summary_Entity2 {
		
		private String R28_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R28_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R28_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R28_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R28_AMOUNT;
	
	    private String R29_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R29_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R29_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R29_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R29_AMOUNT;
	
	    private String R30_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R30_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R30_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R30_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R30_AMOUNT;
	
	    private String R31_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R31_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R31_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R31_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R31_AMOUNT;
	
	    private String R32_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R32_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R32_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R32_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R32_AMOUNT;
	
	    private String R33_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R33_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R33_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R33_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R33_AMOUNT;
	
	    private String R34_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R34_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R34_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R34_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R34_AMOUNT;
	
	    private BigDecimal R35_AMOUNT;
	
	    private String R40_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R40_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R40_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R40_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R40_AMOUNT;
	
	    private String R41_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R41_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R41_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R41_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R41_AMOUNT;
	
	    private String R42_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R42_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R42_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R42_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R42_AMOUNT;
	
	    private String R43_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R43_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R43_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R43_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R43_AMOUNT;
	
	    private String R44_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R44_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R44_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R44_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R44_AMOUNT;
	
	    private String R45_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R45_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R45_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R45_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R45_AMOUNT;
	
	    private String R46_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R46_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R46_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R46_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R46_AMOUNT;
	
	    private BigDecimal R47_AMOUNT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
	    public String REPORT_FREQUENCY;
	    public String REPORT_CODE;
	    public String REPORT_DESC;
	    public String ENTITY_FLG;
	    public String MODIFY_FLG;
	    public String DEL_FLG;
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}
		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}
		public Date getR28_ISSUANCE_DATE() {
			return R28_ISSUANCE_DATE;
		}
		public void setR28_ISSUANCE_DATE(Date r28_ISSUANCE_DATE) {
			R28_ISSUANCE_DATE = r28_ISSUANCE_DATE;
		}
		public Date getR28_CONTRACTUAL_MATURITY_DATE() {
			return R28_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR28_CONTRACTUAL_MATURITY_DATE(Date r28_CONTRACTUAL_MATURITY_DATE) {
			R28_CONTRACTUAL_MATURITY_DATE = r28_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR28_EFFECTIVE_MATURITY_DATE() {
			return R28_EFFECTIVE_MATURITY_DATE;
		}
		public void setR28_EFFECTIVE_MATURITY_DATE(Date r28_EFFECTIVE_MATURITY_DATE) {
			R28_EFFECTIVE_MATURITY_DATE = r28_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}
		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}
		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}
		public Date getR29_ISSUANCE_DATE() {
			return R29_ISSUANCE_DATE;
		}
		public void setR29_ISSUANCE_DATE(Date r29_ISSUANCE_DATE) {
			R29_ISSUANCE_DATE = r29_ISSUANCE_DATE;
		}
		public Date getR29_CONTRACTUAL_MATURITY_DATE() {
			return R29_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR29_CONTRACTUAL_MATURITY_DATE(Date r29_CONTRACTUAL_MATURITY_DATE) {
			R29_CONTRACTUAL_MATURITY_DATE = r29_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR29_EFFECTIVE_MATURITY_DATE() {
			return R29_EFFECTIVE_MATURITY_DATE;
		}
		public void setR29_EFFECTIVE_MATURITY_DATE(Date r29_EFFECTIVE_MATURITY_DATE) {
			R29_EFFECTIVE_MATURITY_DATE = r29_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}
		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}
		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}
		public Date getR30_ISSUANCE_DATE() {
			return R30_ISSUANCE_DATE;
		}
		public void setR30_ISSUANCE_DATE(Date r30_ISSUANCE_DATE) {
			R30_ISSUANCE_DATE = r30_ISSUANCE_DATE;
		}
		public Date getR30_CONTRACTUAL_MATURITY_DATE() {
			return R30_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR30_CONTRACTUAL_MATURITY_DATE(Date r30_CONTRACTUAL_MATURITY_DATE) {
			R30_CONTRACTUAL_MATURITY_DATE = r30_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR30_EFFECTIVE_MATURITY_DATE() {
			return R30_EFFECTIVE_MATURITY_DATE;
		}
		public void setR30_EFFECTIVE_MATURITY_DATE(Date r30_EFFECTIVE_MATURITY_DATE) {
			R30_EFFECTIVE_MATURITY_DATE = r30_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}
		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}
		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}
		public Date getR31_ISSUANCE_DATE() {
			return R31_ISSUANCE_DATE;
		}
		public void setR31_ISSUANCE_DATE(Date r31_ISSUANCE_DATE) {
			R31_ISSUANCE_DATE = r31_ISSUANCE_DATE;
		}
		public Date getR31_CONTRACTUAL_MATURITY_DATE() {
			return R31_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR31_CONTRACTUAL_MATURITY_DATE(Date r31_CONTRACTUAL_MATURITY_DATE) {
			R31_CONTRACTUAL_MATURITY_DATE = r31_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR31_EFFECTIVE_MATURITY_DATE() {
			return R31_EFFECTIVE_MATURITY_DATE;
		}
		public void setR31_EFFECTIVE_MATURITY_DATE(Date r31_EFFECTIVE_MATURITY_DATE) {
			R31_EFFECTIVE_MATURITY_DATE = r31_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}
		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}
		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}
		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}
		public Date getR32_ISSUANCE_DATE() {
			return R32_ISSUANCE_DATE;
		}
		public void setR32_ISSUANCE_DATE(Date r32_ISSUANCE_DATE) {
			R32_ISSUANCE_DATE = r32_ISSUANCE_DATE;
		}
		public Date getR32_CONTRACTUAL_MATURITY_DATE() {
			return R32_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR32_CONTRACTUAL_MATURITY_DATE(Date r32_CONTRACTUAL_MATURITY_DATE) {
			R32_CONTRACTUAL_MATURITY_DATE = r32_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR32_EFFECTIVE_MATURITY_DATE() {
			return R32_EFFECTIVE_MATURITY_DATE;
		}
		public void setR32_EFFECTIVE_MATURITY_DATE(Date r32_EFFECTIVE_MATURITY_DATE) {
			R32_EFFECTIVE_MATURITY_DATE = r32_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}
		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}
		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}
		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}
		public Date getR33_ISSUANCE_DATE() {
			return R33_ISSUANCE_DATE;
		}
		public void setR33_ISSUANCE_DATE(Date r33_ISSUANCE_DATE) {
			R33_ISSUANCE_DATE = r33_ISSUANCE_DATE;
		}
		public Date getR33_CONTRACTUAL_MATURITY_DATE() {
			return R33_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR33_CONTRACTUAL_MATURITY_DATE(Date r33_CONTRACTUAL_MATURITY_DATE) {
			R33_CONTRACTUAL_MATURITY_DATE = r33_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR33_EFFECTIVE_MATURITY_DATE() {
			return R33_EFFECTIVE_MATURITY_DATE;
		}
		public void setR33_EFFECTIVE_MATURITY_DATE(Date r33_EFFECTIVE_MATURITY_DATE) {
			R33_EFFECTIVE_MATURITY_DATE = r33_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}
		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
		}
		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}
		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}
		public Date getR34_ISSUANCE_DATE() {
			return R34_ISSUANCE_DATE;
		}
		public void setR34_ISSUANCE_DATE(Date r34_ISSUANCE_DATE) {
			R34_ISSUANCE_DATE = r34_ISSUANCE_DATE;
		}
		public Date getR34_CONTRACTUAL_MATURITY_DATE() {
			return R34_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR34_CONTRACTUAL_MATURITY_DATE(Date r34_CONTRACTUAL_MATURITY_DATE) {
			R34_CONTRACTUAL_MATURITY_DATE = r34_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR34_EFFECTIVE_MATURITY_DATE() {
			return R34_EFFECTIVE_MATURITY_DATE;
		}
		public void setR34_EFFECTIVE_MATURITY_DATE(Date r34_EFFECTIVE_MATURITY_DATE) {
			R34_EFFECTIVE_MATURITY_DATE = r34_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR34_AMOUNT() {
			return R34_AMOUNT;
		}
		public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
			R34_AMOUNT = r34_AMOUNT;
		}
		public BigDecimal getR35_AMOUNT() {
			return R35_AMOUNT;
		}
		public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
			R35_AMOUNT = r35_AMOUNT;
		}
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}
		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}
		public Date getR40_ISSUANCE_DATE() {
			return R40_ISSUANCE_DATE;
		}
		public void setR40_ISSUANCE_DATE(Date r40_ISSUANCE_DATE) {
			R40_ISSUANCE_DATE = r40_ISSUANCE_DATE;
		}
		public Date getR40_CONTRACTUAL_MATURITY_DATE() {
			return R40_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR40_CONTRACTUAL_MATURITY_DATE(Date r40_CONTRACTUAL_MATURITY_DATE) {
			R40_CONTRACTUAL_MATURITY_DATE = r40_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR40_EFFECTIVE_MATURITY_DATE() {
			return R40_EFFECTIVE_MATURITY_DATE;
		}
		public void setR40_EFFECTIVE_MATURITY_DATE(Date r40_EFFECTIVE_MATURITY_DATE) {
			R40_EFFECTIVE_MATURITY_DATE = r40_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}
		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}
		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}
		public Date getR41_ISSUANCE_DATE() {
			return R41_ISSUANCE_DATE;
		}
		public void setR41_ISSUANCE_DATE(Date r41_ISSUANCE_DATE) {
			R41_ISSUANCE_DATE = r41_ISSUANCE_DATE;
		}
		public Date getR41_CONTRACTUAL_MATURITY_DATE() {
			return R41_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR41_CONTRACTUAL_MATURITY_DATE(Date r41_CONTRACTUAL_MATURITY_DATE) {
			R41_CONTRACTUAL_MATURITY_DATE = r41_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR41_EFFECTIVE_MATURITY_DATE() {
			return R41_EFFECTIVE_MATURITY_DATE;
		}
		public void setR41_EFFECTIVE_MATURITY_DATE(Date r41_EFFECTIVE_MATURITY_DATE) {
			R41_EFFECTIVE_MATURITY_DATE = r41_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}
		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}
		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}
		public Date getR42_ISSUANCE_DATE() {
			return R42_ISSUANCE_DATE;
		}
		public void setR42_ISSUANCE_DATE(Date r42_ISSUANCE_DATE) {
			R42_ISSUANCE_DATE = r42_ISSUANCE_DATE;
		}
		public Date getR42_CONTRACTUAL_MATURITY_DATE() {
			return R42_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR42_CONTRACTUAL_MATURITY_DATE(Date r42_CONTRACTUAL_MATURITY_DATE) {
			R42_CONTRACTUAL_MATURITY_DATE = r42_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR42_EFFECTIVE_MATURITY_DATE() {
			return R42_EFFECTIVE_MATURITY_DATE;
		}
		public void setR42_EFFECTIVE_MATURITY_DATE(Date r42_EFFECTIVE_MATURITY_DATE) {
			R42_EFFECTIVE_MATURITY_DATE = r42_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}
		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}
		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}
		public Date getR43_ISSUANCE_DATE() {
			return R43_ISSUANCE_DATE;
		}
		public void setR43_ISSUANCE_DATE(Date r43_ISSUANCE_DATE) {
			R43_ISSUANCE_DATE = r43_ISSUANCE_DATE;
		}
		public Date getR43_CONTRACTUAL_MATURITY_DATE() {
			return R43_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR43_CONTRACTUAL_MATURITY_DATE(Date r43_CONTRACTUAL_MATURITY_DATE) {
			R43_CONTRACTUAL_MATURITY_DATE = r43_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR43_EFFECTIVE_MATURITY_DATE() {
			return R43_EFFECTIVE_MATURITY_DATE;
		}
		public void setR43_EFFECTIVE_MATURITY_DATE(Date r43_EFFECTIVE_MATURITY_DATE) {
			R43_EFFECTIVE_MATURITY_DATE = r43_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}
		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}
		public String getR44_PRODUCT() {
			return R44_PRODUCT;
		}
		public void setR44_PRODUCT(String r44_PRODUCT) {
			R44_PRODUCT = r44_PRODUCT;
		}
		public Date getR44_ISSUANCE_DATE() {
			return R44_ISSUANCE_DATE;
		}
		public void setR44_ISSUANCE_DATE(Date r44_ISSUANCE_DATE) {
			R44_ISSUANCE_DATE = r44_ISSUANCE_DATE;
		}
		public Date getR44_CONTRACTUAL_MATURITY_DATE() {
			return R44_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR44_CONTRACTUAL_MATURITY_DATE(Date r44_CONTRACTUAL_MATURITY_DATE) {
			R44_CONTRACTUAL_MATURITY_DATE = r44_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR44_EFFECTIVE_MATURITY_DATE() {
			return R44_EFFECTIVE_MATURITY_DATE;
		}
		public void setR44_EFFECTIVE_MATURITY_DATE(Date r44_EFFECTIVE_MATURITY_DATE) {
			R44_EFFECTIVE_MATURITY_DATE = r44_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}
		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
		}
		public String getR45_PRODUCT() {
			return R45_PRODUCT;
		}
		public void setR45_PRODUCT(String r45_PRODUCT) {
			R45_PRODUCT = r45_PRODUCT;
		}
		public Date getR45_ISSUANCE_DATE() {
			return R45_ISSUANCE_DATE;
		}
		public void setR45_ISSUANCE_DATE(Date r45_ISSUANCE_DATE) {
			R45_ISSUANCE_DATE = r45_ISSUANCE_DATE;
		}
		public Date getR45_CONTRACTUAL_MATURITY_DATE() {
			return R45_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR45_CONTRACTUAL_MATURITY_DATE(Date r45_CONTRACTUAL_MATURITY_DATE) {
			R45_CONTRACTUAL_MATURITY_DATE = r45_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR45_EFFECTIVE_MATURITY_DATE() {
			return R45_EFFECTIVE_MATURITY_DATE;
		}
		public void setR45_EFFECTIVE_MATURITY_DATE(Date r45_EFFECTIVE_MATURITY_DATE) {
			R45_EFFECTIVE_MATURITY_DATE = r45_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR45_AMOUNT() {
			return R45_AMOUNT;
		}
		public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
			R45_AMOUNT = r45_AMOUNT;
		}
		public String getR46_PRODUCT() {
			return R46_PRODUCT;
		}
		public void setR46_PRODUCT(String r46_PRODUCT) {
			R46_PRODUCT = r46_PRODUCT;
		}
		public Date getR46_ISSUANCE_DATE() {
			return R46_ISSUANCE_DATE;
		}
		public void setR46_ISSUANCE_DATE(Date r46_ISSUANCE_DATE) {
			R46_ISSUANCE_DATE = r46_ISSUANCE_DATE;
		}
		public Date getR46_CONTRACTUAL_MATURITY_DATE() {
			return R46_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR46_CONTRACTUAL_MATURITY_DATE(Date r46_CONTRACTUAL_MATURITY_DATE) {
			R46_CONTRACTUAL_MATURITY_DATE = r46_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR46_EFFECTIVE_MATURITY_DATE() {
			return R46_EFFECTIVE_MATURITY_DATE;
		}
		public void setR46_EFFECTIVE_MATURITY_DATE(Date r46_EFFECTIVE_MATURITY_DATE) {
			R46_EFFECTIVE_MATURITY_DATE = r46_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR46_AMOUNT() {
			return R46_AMOUNT;
		}
		public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
			R46_AMOUNT = r46_AMOUNT;
		}
		public BigDecimal getR47_AMOUNT() {
			return R47_AMOUNT;
		}
		public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
			R47_AMOUNT = r47_AMOUNT;
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
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
		public String getDEL_FLG() {
			return DEL_FLG;
		}
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
		public M_CA6_Summary_Entity2() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	}


	public static class M_CA6_Detail_Entity1 {
	
		private Date R12_CALENDAR_YEAR;
		private BigDecimal R12_CAP;
		private BigDecimal R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R13_CALENDAR_YEAR;
		private BigDecimal R13_CAP;
		private BigDecimal R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R14_CALENDAR_YEAR;
		private BigDecimal R14_CAP;
		private BigDecimal R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R15_CALENDAR_YEAR;
		private BigDecimal R15_CAP;
		private BigDecimal R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R16_CALENDAR_YEAR;
		private BigDecimal R16_CAP;
		private BigDecimal R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R20_CALENDAR_YEAR;
		private BigDecimal R20_CAP;
		private BigDecimal R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R21_CALENDAR_YEAR;
		private BigDecimal R21_CAP;
		private BigDecimal R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R22_CALENDAR_YEAR;
		private BigDecimal R22_CAP;
		private BigDecimal R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R23_CALENDAR_YEAR;
		private BigDecimal R23_CAP;
		private BigDecimal R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R24_CALENDAR_YEAR;
		private BigDecimal R24_CAP;
		private BigDecimal R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
	
		public Date getR12_CALENDAR_YEAR() {
			return R12_CALENDAR_YEAR;
		}
	
		public void setR12_CALENDAR_YEAR(Date r12_CALENDAR_YEAR) {
			R12_CALENDAR_YEAR = r12_CALENDAR_YEAR;
		}
	
		public BigDecimal getR12_CAP() {
			return R12_CAP;
		}
	
		public void setR12_CAP(BigDecimal r12_CAP) {
			R12_CAP = r12_CAP;
		}
	
		public BigDecimal getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR13_CALENDAR_YEAR() {
			return R13_CALENDAR_YEAR;
		}
	
		public void setR13_CALENDAR_YEAR(Date r13_CALENDAR_YEAR) {
			R13_CALENDAR_YEAR = r13_CALENDAR_YEAR;
		}
	
		public BigDecimal getR13_CAP() {
			return R13_CAP;
		}
	
		public void setR13_CAP(BigDecimal r13_CAP) {
			R13_CAP = r13_CAP;
		}
	
		public BigDecimal getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR14_CALENDAR_YEAR() {
			return R14_CALENDAR_YEAR;
		}
	
		public void setR14_CALENDAR_YEAR(Date r14_CALENDAR_YEAR) {
			R14_CALENDAR_YEAR = r14_CALENDAR_YEAR;
		}
	
		public BigDecimal getR14_CAP() {
			return R14_CAP;
		}
	
		public void setR14_CAP(BigDecimal r14_CAP) {
			R14_CAP = r14_CAP;
		}
	
		public BigDecimal getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR15_CALENDAR_YEAR() {
			return R15_CALENDAR_YEAR;
		}
	
		public void setR15_CALENDAR_YEAR(Date r15_CALENDAR_YEAR) {
			R15_CALENDAR_YEAR = r15_CALENDAR_YEAR;
		}
	
		public BigDecimal getR15_CAP() {
			return R15_CAP;
		}
	
		public void setR15_CAP(BigDecimal r15_CAP) {
			R15_CAP = r15_CAP;
		}
	
		public BigDecimal getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR16_CALENDAR_YEAR() {
			return R16_CALENDAR_YEAR;
		}
	
		public void setR16_CALENDAR_YEAR(Date r16_CALENDAR_YEAR) {
			R16_CALENDAR_YEAR = r16_CALENDAR_YEAR;
		}
	
		public BigDecimal getR16_CAP() {
			return R16_CAP;
		}
	
		public void setR16_CAP(BigDecimal r16_CAP) {
			R16_CAP = r16_CAP;
		}
	
		public BigDecimal getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR20_CALENDAR_YEAR() {
			return R20_CALENDAR_YEAR;
		}
	
		public void setR20_CALENDAR_YEAR(Date r20_CALENDAR_YEAR) {
			R20_CALENDAR_YEAR = r20_CALENDAR_YEAR;
		}
	
		public BigDecimal getR20_CAP() {
			return R20_CAP;
		}
	
		public void setR20_CAP(BigDecimal r20_CAP) {
			R20_CAP = r20_CAP;
		}
	
		public BigDecimal getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR21_CALENDAR_YEAR() {
			return R21_CALENDAR_YEAR;
		}
	
		public void setR21_CALENDAR_YEAR(Date r21_CALENDAR_YEAR) {
			R21_CALENDAR_YEAR = r21_CALENDAR_YEAR;
		}
	
		public BigDecimal getR21_CAP() {
			return R21_CAP;
		}
	
		public void setR21_CAP(BigDecimal r21_CAP) {
			R21_CAP = r21_CAP;
		}
	
		public BigDecimal getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR22_CALENDAR_YEAR() {
			return R22_CALENDAR_YEAR;
		}
	
		public void setR22_CALENDAR_YEAR(Date r22_CALENDAR_YEAR) {
			R22_CALENDAR_YEAR = r22_CALENDAR_YEAR;
		}
	
		public BigDecimal getR22_CAP() {
			return R22_CAP;
		}
	
		public void setR22_CAP(BigDecimal r22_CAP) {
			R22_CAP = r22_CAP;
		}
	
		public BigDecimal getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR23_CALENDAR_YEAR() {
			return R23_CALENDAR_YEAR;
		}
	
		public void setR23_CALENDAR_YEAR(Date r23_CALENDAR_YEAR) {
			R23_CALENDAR_YEAR = r23_CALENDAR_YEAR;
		}
	
		public BigDecimal getR23_CAP() {
			return R23_CAP;
		}
	
		public void setR23_CAP(BigDecimal r23_CAP) {
			R23_CAP = r23_CAP;
		}
	
		public BigDecimal getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR24_CALENDAR_YEAR() {
			return R24_CALENDAR_YEAR;
		}
	
		public void setR24_CALENDAR_YEAR(Date r24_CALENDAR_YEAR) {
			R24_CALENDAR_YEAR = r24_CALENDAR_YEAR;
		}
	
		public BigDecimal getR24_CAP() {
			return R24_CAP;
		}
	
		public void setR24_CAP(BigDecimal r24_CAP) {
			R24_CAP = r24_CAP;
		}
	
		public BigDecimal getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
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
	
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
	
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
	
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
	
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
	
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
	
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
	
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
	
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
	
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
	
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
	
		public String getDEL_FLG() {
			return DEL_FLG;
		}
	
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
	
		public M_CA6_Detail_Entity1() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	}


	public static class M_CA6_Detail_Entity2 {
	
		private String R28_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R28_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R28_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R28_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R28_AMOUNT;
	
		private String R29_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R29_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R29_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R29_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R29_AMOUNT;
	
		private String R30_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R30_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R30_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R30_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R30_AMOUNT;
	
		private String R31_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R31_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R31_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R31_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R31_AMOUNT;
	
		private String R32_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R32_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R32_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R32_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R32_AMOUNT;
	
		private String R33_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R33_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R33_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R33_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R33_AMOUNT;
	
		private String R34_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R34_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R34_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R34_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R34_AMOUNT;
	
		private BigDecimal R35_AMOUNT;
	
		private String R40_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R40_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R40_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R40_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R40_AMOUNT;
	
		private String R41_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R41_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R41_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R41_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R41_AMOUNT;
	
		private String R42_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R42_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R42_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R42_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R42_AMOUNT;
	
		private String R43_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R43_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R43_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R43_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R43_AMOUNT;
	
		private String R44_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R44_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R44_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R44_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R44_AMOUNT;
	
		private String R45_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R45_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R45_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R45_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R45_AMOUNT;
	
		private String R46_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R46_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R46_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R46_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R46_AMOUNT;
	
		private BigDecimal R47_AMOUNT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
	
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}
	
		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}
	
		public Date getR28_ISSUANCE_DATE() {
			return R28_ISSUANCE_DATE;
		}
	
		public void setR28_ISSUANCE_DATE(Date r28_ISSUANCE_DATE) {
			R28_ISSUANCE_DATE = r28_ISSUANCE_DATE;
		}
	
		public Date getR28_CONTRACTUAL_MATURITY_DATE() {
			return R28_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR28_CONTRACTUAL_MATURITY_DATE(Date r28_CONTRACTUAL_MATURITY_DATE) {
			R28_CONTRACTUAL_MATURITY_DATE = r28_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR28_EFFECTIVE_MATURITY_DATE() {
			return R28_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR28_EFFECTIVE_MATURITY_DATE(Date r28_EFFECTIVE_MATURITY_DATE) {
			R28_EFFECTIVE_MATURITY_DATE = r28_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}
	
		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}
	
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}
	
		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}
	
		public Date getR29_ISSUANCE_DATE() {
			return R29_ISSUANCE_DATE;
		}
	
		public void setR29_ISSUANCE_DATE(Date r29_ISSUANCE_DATE) {
			R29_ISSUANCE_DATE = r29_ISSUANCE_DATE;
		}
	
		public Date getR29_CONTRACTUAL_MATURITY_DATE() {
			return R29_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR29_CONTRACTUAL_MATURITY_DATE(Date r29_CONTRACTUAL_MATURITY_DATE) {
			R29_CONTRACTUAL_MATURITY_DATE = r29_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR29_EFFECTIVE_MATURITY_DATE() {
			return R29_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR29_EFFECTIVE_MATURITY_DATE(Date r29_EFFECTIVE_MATURITY_DATE) {
			R29_EFFECTIVE_MATURITY_DATE = r29_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}
	
		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}
	
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}
	
		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}
	
		public Date getR30_ISSUANCE_DATE() {
			return R30_ISSUANCE_DATE;
		}
	
		public void setR30_ISSUANCE_DATE(Date r30_ISSUANCE_DATE) {
			R30_ISSUANCE_DATE = r30_ISSUANCE_DATE;
		}
	
		public Date getR30_CONTRACTUAL_MATURITY_DATE() {
			return R30_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR30_CONTRACTUAL_MATURITY_DATE(Date r30_CONTRACTUAL_MATURITY_DATE) {
			R30_CONTRACTUAL_MATURITY_DATE = r30_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR30_EFFECTIVE_MATURITY_DATE() {
			return R30_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR30_EFFECTIVE_MATURITY_DATE(Date r30_EFFECTIVE_MATURITY_DATE) {
			R30_EFFECTIVE_MATURITY_DATE = r30_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}
	
		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}
	
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}
	
		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}
	
		public Date getR31_ISSUANCE_DATE() {
			return R31_ISSUANCE_DATE;
		}
	
		public void setR31_ISSUANCE_DATE(Date r31_ISSUANCE_DATE) {
			R31_ISSUANCE_DATE = r31_ISSUANCE_DATE;
		}
	
		public Date getR31_CONTRACTUAL_MATURITY_DATE() {
			return R31_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR31_CONTRACTUAL_MATURITY_DATE(Date r31_CONTRACTUAL_MATURITY_DATE) {
			R31_CONTRACTUAL_MATURITY_DATE = r31_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR31_EFFECTIVE_MATURITY_DATE() {
			return R31_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR31_EFFECTIVE_MATURITY_DATE(Date r31_EFFECTIVE_MATURITY_DATE) {
			R31_EFFECTIVE_MATURITY_DATE = r31_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}
	
		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}
	
		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}
	
		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}
	
		public Date getR32_ISSUANCE_DATE() {
			return R32_ISSUANCE_DATE;
		}
	
		public void setR32_ISSUANCE_DATE(Date r32_ISSUANCE_DATE) {
			R32_ISSUANCE_DATE = r32_ISSUANCE_DATE;
		}
	
		public Date getR32_CONTRACTUAL_MATURITY_DATE() {
			return R32_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR32_CONTRACTUAL_MATURITY_DATE(Date r32_CONTRACTUAL_MATURITY_DATE) {
			R32_CONTRACTUAL_MATURITY_DATE = r32_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR32_EFFECTIVE_MATURITY_DATE() {
			return R32_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR32_EFFECTIVE_MATURITY_DATE(Date r32_EFFECTIVE_MATURITY_DATE) {
			R32_EFFECTIVE_MATURITY_DATE = r32_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}
	
		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}
	
		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}
	
		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}
	
		public Date getR33_ISSUANCE_DATE() {
			return R33_ISSUANCE_DATE;
		}
	
		public void setR33_ISSUANCE_DATE(Date r33_ISSUANCE_DATE) {
			R33_ISSUANCE_DATE = r33_ISSUANCE_DATE;
		}
	
		public Date getR33_CONTRACTUAL_MATURITY_DATE() {
			return R33_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR33_CONTRACTUAL_MATURITY_DATE(Date r33_CONTRACTUAL_MATURITY_DATE) {
			R33_CONTRACTUAL_MATURITY_DATE = r33_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR33_EFFECTIVE_MATURITY_DATE() {
			return R33_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR33_EFFECTIVE_MATURITY_DATE(Date r33_EFFECTIVE_MATURITY_DATE) {
			R33_EFFECTIVE_MATURITY_DATE = r33_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}
	
		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
		}
	
		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}
	
		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}
	
		public Date getR34_ISSUANCE_DATE() {
			return R34_ISSUANCE_DATE;
		}
	
		public void setR34_ISSUANCE_DATE(Date r34_ISSUANCE_DATE) {
			R34_ISSUANCE_DATE = r34_ISSUANCE_DATE;
		}
	
		public Date getR34_CONTRACTUAL_MATURITY_DATE() {
			return R34_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR34_CONTRACTUAL_MATURITY_DATE(Date r34_CONTRACTUAL_MATURITY_DATE) {
			R34_CONTRACTUAL_MATURITY_DATE = r34_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR34_EFFECTIVE_MATURITY_DATE() {
			return R34_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR34_EFFECTIVE_MATURITY_DATE(Date r34_EFFECTIVE_MATURITY_DATE) {
			R34_EFFECTIVE_MATURITY_DATE = r34_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR34_AMOUNT() {
			return R34_AMOUNT;
		}
	
		public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
			R34_AMOUNT = r34_AMOUNT;
		}
	
		public BigDecimal getR35_AMOUNT() {
			return R35_AMOUNT;
		}
	
		public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
			R35_AMOUNT = r35_AMOUNT;
		}
	
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}
	
		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}
	
		public Date getR40_ISSUANCE_DATE() {
			return R40_ISSUANCE_DATE;
		}
	
		public void setR40_ISSUANCE_DATE(Date r40_ISSUANCE_DATE) {
			R40_ISSUANCE_DATE = r40_ISSUANCE_DATE;
		}
	
		public Date getR40_CONTRACTUAL_MATURITY_DATE() {
			return R40_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR40_CONTRACTUAL_MATURITY_DATE(Date r40_CONTRACTUAL_MATURITY_DATE) {
			R40_CONTRACTUAL_MATURITY_DATE = r40_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR40_EFFECTIVE_MATURITY_DATE() {
			return R40_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR40_EFFECTIVE_MATURITY_DATE(Date r40_EFFECTIVE_MATURITY_DATE) {
			R40_EFFECTIVE_MATURITY_DATE = r40_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}
	
		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}
	
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}
	
		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}
	
		public Date getR41_ISSUANCE_DATE() {
			return R41_ISSUANCE_DATE;
		}
	
		public void setR41_ISSUANCE_DATE(Date r41_ISSUANCE_DATE) {
			R41_ISSUANCE_DATE = r41_ISSUANCE_DATE;
		}
	
		public Date getR41_CONTRACTUAL_MATURITY_DATE() {
			return R41_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR41_CONTRACTUAL_MATURITY_DATE(Date r41_CONTRACTUAL_MATURITY_DATE) {
			R41_CONTRACTUAL_MATURITY_DATE = r41_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR41_EFFECTIVE_MATURITY_DATE() {
			return R41_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR41_EFFECTIVE_MATURITY_DATE(Date r41_EFFECTIVE_MATURITY_DATE) {
			R41_EFFECTIVE_MATURITY_DATE = r41_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}
	
		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}
	
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}
	
		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}
	
		public Date getR42_ISSUANCE_DATE() {
			return R42_ISSUANCE_DATE;
		}
	
		public void setR42_ISSUANCE_DATE(Date r42_ISSUANCE_DATE) {
			R42_ISSUANCE_DATE = r42_ISSUANCE_DATE;
		}
	
		public Date getR42_CONTRACTUAL_MATURITY_DATE() {
			return R42_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR42_CONTRACTUAL_MATURITY_DATE(Date r42_CONTRACTUAL_MATURITY_DATE) {
			R42_CONTRACTUAL_MATURITY_DATE = r42_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR42_EFFECTIVE_MATURITY_DATE() {
			return R42_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR42_EFFECTIVE_MATURITY_DATE(Date r42_EFFECTIVE_MATURITY_DATE) {
			R42_EFFECTIVE_MATURITY_DATE = r42_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}
	
		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}
	
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}
	
		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}
	
		public Date getR43_ISSUANCE_DATE() {
			return R43_ISSUANCE_DATE;
		}
	
		public void setR43_ISSUANCE_DATE(Date r43_ISSUANCE_DATE) {
			R43_ISSUANCE_DATE = r43_ISSUANCE_DATE;
		}
	
		public Date getR43_CONTRACTUAL_MATURITY_DATE() {
			return R43_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR43_CONTRACTUAL_MATURITY_DATE(Date r43_CONTRACTUAL_MATURITY_DATE) {
			R43_CONTRACTUAL_MATURITY_DATE = r43_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR43_EFFECTIVE_MATURITY_DATE() {
			return R43_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR43_EFFECTIVE_MATURITY_DATE(Date r43_EFFECTIVE_MATURITY_DATE) {
			R43_EFFECTIVE_MATURITY_DATE = r43_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}
	
		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}
	
		public String getR44_PRODUCT() {
			return R44_PRODUCT;
		}
	
		public void setR44_PRODUCT(String r44_PRODUCT) {
			R44_PRODUCT = r44_PRODUCT;
		}
	
		public Date getR44_ISSUANCE_DATE() {
			return R44_ISSUANCE_DATE;
		}
	
		public void setR44_ISSUANCE_DATE(Date r44_ISSUANCE_DATE) {
			R44_ISSUANCE_DATE = r44_ISSUANCE_DATE;
		}
	
		public Date getR44_CONTRACTUAL_MATURITY_DATE() {
			return R44_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR44_CONTRACTUAL_MATURITY_DATE(Date r44_CONTRACTUAL_MATURITY_DATE) {
			R44_CONTRACTUAL_MATURITY_DATE = r44_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR44_EFFECTIVE_MATURITY_DATE() {
			return R44_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR44_EFFECTIVE_MATURITY_DATE(Date r44_EFFECTIVE_MATURITY_DATE) {
			R44_EFFECTIVE_MATURITY_DATE = r44_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}
	
		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
		}
	
		public String getR45_PRODUCT() {
			return R45_PRODUCT;
		}
	
		public void setR45_PRODUCT(String r45_PRODUCT) {
			R45_PRODUCT = r45_PRODUCT;
		}
	
		public Date getR45_ISSUANCE_DATE() {
			return R45_ISSUANCE_DATE;
		}
	
		public void setR45_ISSUANCE_DATE(Date r45_ISSUANCE_DATE) {
			R45_ISSUANCE_DATE = r45_ISSUANCE_DATE;
		}
	
		public Date getR45_CONTRACTUAL_MATURITY_DATE() {
			return R45_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR45_CONTRACTUAL_MATURITY_DATE(Date r45_CONTRACTUAL_MATURITY_DATE) {
			R45_CONTRACTUAL_MATURITY_DATE = r45_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR45_EFFECTIVE_MATURITY_DATE() {
			return R45_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR45_EFFECTIVE_MATURITY_DATE(Date r45_EFFECTIVE_MATURITY_DATE) {
			R45_EFFECTIVE_MATURITY_DATE = r45_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR45_AMOUNT() {
			return R45_AMOUNT;
		}
	
		public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
			R45_AMOUNT = r45_AMOUNT;
		}
	
		public String getR46_PRODUCT() {
			return R46_PRODUCT;
		}
	
		public void setR46_PRODUCT(String r46_PRODUCT) {
			R46_PRODUCT = r46_PRODUCT;
		}
	
		public Date getR46_ISSUANCE_DATE() {
			return R46_ISSUANCE_DATE;
		}
	
		public void setR46_ISSUANCE_DATE(Date r46_ISSUANCE_DATE) {
			R46_ISSUANCE_DATE = r46_ISSUANCE_DATE;
		}
	
		public Date getR46_CONTRACTUAL_MATURITY_DATE() {
			return R46_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR46_CONTRACTUAL_MATURITY_DATE(Date r46_CONTRACTUAL_MATURITY_DATE) {
			R46_CONTRACTUAL_MATURITY_DATE = r46_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR46_EFFECTIVE_MATURITY_DATE() {
			return R46_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR46_EFFECTIVE_MATURITY_DATE(Date r46_EFFECTIVE_MATURITY_DATE) {
			R46_EFFECTIVE_MATURITY_DATE = r46_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR46_AMOUNT() {
			return R46_AMOUNT;
		}
	
		public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
			R46_AMOUNT = r46_AMOUNT;
		}
	
		public BigDecimal getR47_AMOUNT() {
			return R47_AMOUNT;
		}
	
		public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
			R47_AMOUNT = r47_AMOUNT;
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
	
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
	
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
	
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
	
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
	
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
	
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
	
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
	
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
	
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
	
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
	
		public String getDEL_FLG() {
			return DEL_FLG;
		}
	
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
	
		public M_CA6_Detail_Entity2() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	}


	public static class M_CA6_Archival_Summary_Entity1 {
		
		private Date R12_CALENDAR_YEAR;
	    private BigDecimal R12_CAP;
	    private BigDecimal R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R13_CALENDAR_YEAR;
	    private BigDecimal R13_CAP;
	    private BigDecimal R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R14_CALENDAR_YEAR;
	    private BigDecimal R14_CAP;
	    private BigDecimal R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R15_CALENDAR_YEAR;
	    private BigDecimal R15_CAP;
	    private BigDecimal R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R16_CALENDAR_YEAR;
	    private BigDecimal R16_CAP;
	    private BigDecimal R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R20_CALENDAR_YEAR;
	    private BigDecimal R20_CAP;
	    private BigDecimal R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R21_CALENDAR_YEAR;
	    private BigDecimal R21_CAP;
	    private BigDecimal R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R22_CALENDAR_YEAR;
	    private BigDecimal R22_CAP;
	    private BigDecimal R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R23_CALENDAR_YEAR;
	    private BigDecimal R23_CAP;
	    private BigDecimal R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R24_CALENDAR_YEAR;
	    private BigDecimal R24_CAP;
	    private BigDecimal R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		    private Date reportResubDate;	
	    public String REPORT_FREQUENCY;
	    public String REPORT_CODE;
	    public String REPORT_DESC;
	    public String ENTITY_FLG;
	    public String MODIFY_FLG;
	    public String DEL_FLG;
		public Date getR12_CALENDAR_YEAR() {
			return R12_CALENDAR_YEAR;
		}
		public void setR12_CALENDAR_YEAR(Date r12_CALENDAR_YEAR) {
			R12_CALENDAR_YEAR = r12_CALENDAR_YEAR;
		}
		public BigDecimal getR12_CAP() {
			return R12_CAP;
		}
		public void setR12_CAP(BigDecimal r12_CAP) {
			R12_CAP = r12_CAP;
		}
		public BigDecimal getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR13_CALENDAR_YEAR() {
			return R13_CALENDAR_YEAR;
		}
		public void setR13_CALENDAR_YEAR(Date r13_CALENDAR_YEAR) {
			R13_CALENDAR_YEAR = r13_CALENDAR_YEAR;
		}
		public BigDecimal getR13_CAP() {
			return R13_CAP;
		}
		public void setR13_CAP(BigDecimal r13_CAP) {
			R13_CAP = r13_CAP;
		}
		public BigDecimal getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR14_CALENDAR_YEAR() {
			return R14_CALENDAR_YEAR;
		}
		public void setR14_CALENDAR_YEAR(Date r14_CALENDAR_YEAR) {
			R14_CALENDAR_YEAR = r14_CALENDAR_YEAR;
		}
		public BigDecimal getR14_CAP() {
			return R14_CAP;
		}
		public void setR14_CAP(BigDecimal r14_CAP) {
			R14_CAP = r14_CAP;
		}
		public BigDecimal getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR15_CALENDAR_YEAR() {
			return R15_CALENDAR_YEAR;
		}
		public void setR15_CALENDAR_YEAR(Date r15_CALENDAR_YEAR) {
			R15_CALENDAR_YEAR = r15_CALENDAR_YEAR;
		}
		public BigDecimal getR15_CAP() {
			return R15_CAP;
		}
		public void setR15_CAP(BigDecimal r15_CAP) {
			R15_CAP = r15_CAP;
		}
		public BigDecimal getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR16_CALENDAR_YEAR() {
			return R16_CALENDAR_YEAR;
		}
		public void setR16_CALENDAR_YEAR(Date r16_CALENDAR_YEAR) {
			R16_CALENDAR_YEAR = r16_CALENDAR_YEAR;
		}
		public BigDecimal getR16_CAP() {
			return R16_CAP;
		}
		public void setR16_CAP(BigDecimal r16_CAP) {
			R16_CAP = r16_CAP;
		}
		public BigDecimal getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR20_CALENDAR_YEAR() {
			return R20_CALENDAR_YEAR;
		}
		public void setR20_CALENDAR_YEAR(Date r20_CALENDAR_YEAR) {
			R20_CALENDAR_YEAR = r20_CALENDAR_YEAR;
		}
		public BigDecimal getR20_CAP() {
			return R20_CAP;
		}
		public void setR20_CAP(BigDecimal r20_CAP) {
			R20_CAP = r20_CAP;
		}
		public BigDecimal getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR21_CALENDAR_YEAR() {
			return R21_CALENDAR_YEAR;
		}
		public void setR21_CALENDAR_YEAR(Date r21_CALENDAR_YEAR) {
			R21_CALENDAR_YEAR = r21_CALENDAR_YEAR;
		}
		public BigDecimal getR21_CAP() {
			return R21_CAP;
		}
		public void setR21_CAP(BigDecimal r21_CAP) {
			R21_CAP = r21_CAP;
		}
		public BigDecimal getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR22_CALENDAR_YEAR() {
			return R22_CALENDAR_YEAR;
		}
		public void setR22_CALENDAR_YEAR(Date r22_CALENDAR_YEAR) {
			R22_CALENDAR_YEAR = r22_CALENDAR_YEAR;
		}
		public BigDecimal getR22_CAP() {
			return R22_CAP;
		}
		public void setR22_CAP(BigDecimal r22_CAP) {
			R22_CAP = r22_CAP;
		}
		public BigDecimal getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR23_CALENDAR_YEAR() {
			return R23_CALENDAR_YEAR;
		}
		public void setR23_CALENDAR_YEAR(Date r23_CALENDAR_YEAR) {
			R23_CALENDAR_YEAR = r23_CALENDAR_YEAR;
		}
		public BigDecimal getR23_CAP() {
			return R23_CAP;
		}
		public void setR23_CAP(BigDecimal r23_CAP) {
			R23_CAP = r23_CAP;
		}
		public BigDecimal getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR24_CALENDAR_YEAR() {
			return R24_CALENDAR_YEAR;
		}
		public void setR24_CALENDAR_YEAR(Date r24_CALENDAR_YEAR) {
			R24_CALENDAR_YEAR = r24_CALENDAR_YEAR;
		}
		public BigDecimal getR24_CAP() {
			return R24_CAP;
		}
		public void setR24_CAP(BigDecimal r24_CAP) {
			R24_CAP = r24_CAP;
		}
		public BigDecimal getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
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
		public Date getReportResubDate() {
			return reportResubDate;
		}
		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
		public String getDEL_FLG() {
			return DEL_FLG;
		}
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
		public M_CA6_Archival_Summary_Entity1() {
			super();
			// TODO Auto-generated constructor stub
		}
	}


	public static class M_CA6_Archival_Summary_Entity2 {
		
	
		private String R28_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R28_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R28_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R28_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R28_AMOUNT;
	
	    private String R29_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R29_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R29_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R29_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R29_AMOUNT;
	
	    private String R30_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R30_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R30_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R30_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R30_AMOUNT;
	
	    private String R31_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R31_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R31_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R31_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R31_AMOUNT;
	
	    private String R32_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R32_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R32_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R32_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R32_AMOUNT;
	
	    private String R33_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R33_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R33_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R33_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R33_AMOUNT;
	
	    private String R34_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R34_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R34_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R34_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R34_AMOUNT;
	
	    private BigDecimal R35_AMOUNT;
	
	    private String R40_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R40_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R40_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R40_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R40_AMOUNT;
	
	    private String R41_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R41_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R41_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R41_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R41_AMOUNT;
	
	    private String R42_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R42_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R42_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R42_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R42_AMOUNT;
	
	    private String R43_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R43_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R43_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R43_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R43_AMOUNT;
	
	    private String R44_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R44_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R44_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R44_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R44_AMOUNT;
	
	    private String R45_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R45_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R45_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R45_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R45_AMOUNT;
	
	    private String R46_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R46_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R46_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R46_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R46_AMOUNT;
	
	    private BigDecimal R47_AMOUNT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		    private Date reportResubDate;	
	    public String REPORT_FREQUENCY;
	    public String REPORT_CODE;
	    public String REPORT_DESC;
	    public String ENTITY_FLG;
	    public String MODIFY_FLG;
	    public String DEL_FLG;
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}
		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}
		public Date getR28_ISSUANCE_DATE() {
			return R28_ISSUANCE_DATE;
		}
		public void setR28_ISSUANCE_DATE(Date r28_ISSUANCE_DATE) {
			R28_ISSUANCE_DATE = r28_ISSUANCE_DATE;
		}
		public Date getR28_CONTRACTUAL_MATURITY_DATE() {
			return R28_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR28_CONTRACTUAL_MATURITY_DATE(Date r28_CONTRACTUAL_MATURITY_DATE) {
			R28_CONTRACTUAL_MATURITY_DATE = r28_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR28_EFFECTIVE_MATURITY_DATE() {
			return R28_EFFECTIVE_MATURITY_DATE;
		}
		public void setR28_EFFECTIVE_MATURITY_DATE(Date r28_EFFECTIVE_MATURITY_DATE) {
			R28_EFFECTIVE_MATURITY_DATE = r28_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}
		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}
		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}
		public Date getR29_ISSUANCE_DATE() {
			return R29_ISSUANCE_DATE;
		}
		public void setR29_ISSUANCE_DATE(Date r29_ISSUANCE_DATE) {
			R29_ISSUANCE_DATE = r29_ISSUANCE_DATE;
		}
		public Date getR29_CONTRACTUAL_MATURITY_DATE() {
			return R29_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR29_CONTRACTUAL_MATURITY_DATE(Date r29_CONTRACTUAL_MATURITY_DATE) {
			R29_CONTRACTUAL_MATURITY_DATE = r29_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR29_EFFECTIVE_MATURITY_DATE() {
			return R29_EFFECTIVE_MATURITY_DATE;
		}
		public void setR29_EFFECTIVE_MATURITY_DATE(Date r29_EFFECTIVE_MATURITY_DATE) {
			R29_EFFECTIVE_MATURITY_DATE = r29_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}
		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}
		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}
		public Date getR30_ISSUANCE_DATE() {
			return R30_ISSUANCE_DATE;
		}
		public void setR30_ISSUANCE_DATE(Date r30_ISSUANCE_DATE) {
			R30_ISSUANCE_DATE = r30_ISSUANCE_DATE;
		}
		public Date getR30_CONTRACTUAL_MATURITY_DATE() {
			return R30_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR30_CONTRACTUAL_MATURITY_DATE(Date r30_CONTRACTUAL_MATURITY_DATE) {
			R30_CONTRACTUAL_MATURITY_DATE = r30_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR30_EFFECTIVE_MATURITY_DATE() {
			return R30_EFFECTIVE_MATURITY_DATE;
		}
		public void setR30_EFFECTIVE_MATURITY_DATE(Date r30_EFFECTIVE_MATURITY_DATE) {
			R30_EFFECTIVE_MATURITY_DATE = r30_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}
		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}
		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}
		public Date getR31_ISSUANCE_DATE() {
			return R31_ISSUANCE_DATE;
		}
		public void setR31_ISSUANCE_DATE(Date r31_ISSUANCE_DATE) {
			R31_ISSUANCE_DATE = r31_ISSUANCE_DATE;
		}
		public Date getR31_CONTRACTUAL_MATURITY_DATE() {
			return R31_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR31_CONTRACTUAL_MATURITY_DATE(Date r31_CONTRACTUAL_MATURITY_DATE) {
			R31_CONTRACTUAL_MATURITY_DATE = r31_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR31_EFFECTIVE_MATURITY_DATE() {
			return R31_EFFECTIVE_MATURITY_DATE;
		}
		public void setR31_EFFECTIVE_MATURITY_DATE(Date r31_EFFECTIVE_MATURITY_DATE) {
			R31_EFFECTIVE_MATURITY_DATE = r31_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}
		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}
		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}
		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}
		public Date getR32_ISSUANCE_DATE() {
			return R32_ISSUANCE_DATE;
		}
		public void setR32_ISSUANCE_DATE(Date r32_ISSUANCE_DATE) {
			R32_ISSUANCE_DATE = r32_ISSUANCE_DATE;
		}
		public Date getR32_CONTRACTUAL_MATURITY_DATE() {
			return R32_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR32_CONTRACTUAL_MATURITY_DATE(Date r32_CONTRACTUAL_MATURITY_DATE) {
			R32_CONTRACTUAL_MATURITY_DATE = r32_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR32_EFFECTIVE_MATURITY_DATE() {
			return R32_EFFECTIVE_MATURITY_DATE;
		}
		public void setR32_EFFECTIVE_MATURITY_DATE(Date r32_EFFECTIVE_MATURITY_DATE) {
			R32_EFFECTIVE_MATURITY_DATE = r32_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}
		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}
		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}
		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}
		public Date getR33_ISSUANCE_DATE() {
			return R33_ISSUANCE_DATE;
		}
		public void setR33_ISSUANCE_DATE(Date r33_ISSUANCE_DATE) {
			R33_ISSUANCE_DATE = r33_ISSUANCE_DATE;
		}
		public Date getR33_CONTRACTUAL_MATURITY_DATE() {
			return R33_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR33_CONTRACTUAL_MATURITY_DATE(Date r33_CONTRACTUAL_MATURITY_DATE) {
			R33_CONTRACTUAL_MATURITY_DATE = r33_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR33_EFFECTIVE_MATURITY_DATE() {
			return R33_EFFECTIVE_MATURITY_DATE;
		}
		public void setR33_EFFECTIVE_MATURITY_DATE(Date r33_EFFECTIVE_MATURITY_DATE) {
			R33_EFFECTIVE_MATURITY_DATE = r33_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}
		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
		}
		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}
		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}
		public Date getR34_ISSUANCE_DATE() {
			return R34_ISSUANCE_DATE;
		}
		public void setR34_ISSUANCE_DATE(Date r34_ISSUANCE_DATE) {
			R34_ISSUANCE_DATE = r34_ISSUANCE_DATE;
		}
		public Date getR34_CONTRACTUAL_MATURITY_DATE() {
			return R34_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR34_CONTRACTUAL_MATURITY_DATE(Date r34_CONTRACTUAL_MATURITY_DATE) {
			R34_CONTRACTUAL_MATURITY_DATE = r34_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR34_EFFECTIVE_MATURITY_DATE() {
			return R34_EFFECTIVE_MATURITY_DATE;
		}
		public void setR34_EFFECTIVE_MATURITY_DATE(Date r34_EFFECTIVE_MATURITY_DATE) {
			R34_EFFECTIVE_MATURITY_DATE = r34_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR34_AMOUNT() {
			return R34_AMOUNT;
		}
		public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
			R34_AMOUNT = r34_AMOUNT;
		}
		public BigDecimal getR35_AMOUNT() {
			return R35_AMOUNT;
		}
		public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
			R35_AMOUNT = r35_AMOUNT;
		}
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}
		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}
		public Date getR40_ISSUANCE_DATE() {
			return R40_ISSUANCE_DATE;
		}
		public void setR40_ISSUANCE_DATE(Date r40_ISSUANCE_DATE) {
			R40_ISSUANCE_DATE = r40_ISSUANCE_DATE;
		}
		public Date getR40_CONTRACTUAL_MATURITY_DATE() {
			return R40_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR40_CONTRACTUAL_MATURITY_DATE(Date r40_CONTRACTUAL_MATURITY_DATE) {
			R40_CONTRACTUAL_MATURITY_DATE = r40_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR40_EFFECTIVE_MATURITY_DATE() {
			return R40_EFFECTIVE_MATURITY_DATE;
		}
		public void setR40_EFFECTIVE_MATURITY_DATE(Date r40_EFFECTIVE_MATURITY_DATE) {
			R40_EFFECTIVE_MATURITY_DATE = r40_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}
		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}
		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}
		public Date getR41_ISSUANCE_DATE() {
			return R41_ISSUANCE_DATE;
		}
		public void setR41_ISSUANCE_DATE(Date r41_ISSUANCE_DATE) {
			R41_ISSUANCE_DATE = r41_ISSUANCE_DATE;
		}
		public Date getR41_CONTRACTUAL_MATURITY_DATE() {
			return R41_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR41_CONTRACTUAL_MATURITY_DATE(Date r41_CONTRACTUAL_MATURITY_DATE) {
			R41_CONTRACTUAL_MATURITY_DATE = r41_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR41_EFFECTIVE_MATURITY_DATE() {
			return R41_EFFECTIVE_MATURITY_DATE;
		}
		public void setR41_EFFECTIVE_MATURITY_DATE(Date r41_EFFECTIVE_MATURITY_DATE) {
			R41_EFFECTIVE_MATURITY_DATE = r41_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}
		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}
		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}
		public Date getR42_ISSUANCE_DATE() {
			return R42_ISSUANCE_DATE;
		}
		public void setR42_ISSUANCE_DATE(Date r42_ISSUANCE_DATE) {
			R42_ISSUANCE_DATE = r42_ISSUANCE_DATE;
		}
		public Date getR42_CONTRACTUAL_MATURITY_DATE() {
			return R42_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR42_CONTRACTUAL_MATURITY_DATE(Date r42_CONTRACTUAL_MATURITY_DATE) {
			R42_CONTRACTUAL_MATURITY_DATE = r42_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR42_EFFECTIVE_MATURITY_DATE() {
			return R42_EFFECTIVE_MATURITY_DATE;
		}
		public void setR42_EFFECTIVE_MATURITY_DATE(Date r42_EFFECTIVE_MATURITY_DATE) {
			R42_EFFECTIVE_MATURITY_DATE = r42_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}
		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}
		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}
		public Date getR43_ISSUANCE_DATE() {
			return R43_ISSUANCE_DATE;
		}
		public void setR43_ISSUANCE_DATE(Date r43_ISSUANCE_DATE) {
			R43_ISSUANCE_DATE = r43_ISSUANCE_DATE;
		}
		public Date getR43_CONTRACTUAL_MATURITY_DATE() {
			return R43_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR43_CONTRACTUAL_MATURITY_DATE(Date r43_CONTRACTUAL_MATURITY_DATE) {
			R43_CONTRACTUAL_MATURITY_DATE = r43_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR43_EFFECTIVE_MATURITY_DATE() {
			return R43_EFFECTIVE_MATURITY_DATE;
		}
		public void setR43_EFFECTIVE_MATURITY_DATE(Date r43_EFFECTIVE_MATURITY_DATE) {
			R43_EFFECTIVE_MATURITY_DATE = r43_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}
		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}
		public String getR44_PRODUCT() {
			return R44_PRODUCT;
		}
		public void setR44_PRODUCT(String r44_PRODUCT) {
			R44_PRODUCT = r44_PRODUCT;
		}
		public Date getR44_ISSUANCE_DATE() {
			return R44_ISSUANCE_DATE;
		}
		public void setR44_ISSUANCE_DATE(Date r44_ISSUANCE_DATE) {
			R44_ISSUANCE_DATE = r44_ISSUANCE_DATE;
		}
		public Date getR44_CONTRACTUAL_MATURITY_DATE() {
			return R44_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR44_CONTRACTUAL_MATURITY_DATE(Date r44_CONTRACTUAL_MATURITY_DATE) {
			R44_CONTRACTUAL_MATURITY_DATE = r44_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR44_EFFECTIVE_MATURITY_DATE() {
			return R44_EFFECTIVE_MATURITY_DATE;
		}
		public void setR44_EFFECTIVE_MATURITY_DATE(Date r44_EFFECTIVE_MATURITY_DATE) {
			R44_EFFECTIVE_MATURITY_DATE = r44_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}
		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
		}
		public String getR45_PRODUCT() {
			return R45_PRODUCT;
		}
		public void setR45_PRODUCT(String r45_PRODUCT) {
			R45_PRODUCT = r45_PRODUCT;
		}
		public Date getR45_ISSUANCE_DATE() {
			return R45_ISSUANCE_DATE;
		}
		public void setR45_ISSUANCE_DATE(Date r45_ISSUANCE_DATE) {
			R45_ISSUANCE_DATE = r45_ISSUANCE_DATE;
		}
		public Date getR45_CONTRACTUAL_MATURITY_DATE() {
			return R45_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR45_CONTRACTUAL_MATURITY_DATE(Date r45_CONTRACTUAL_MATURITY_DATE) {
			R45_CONTRACTUAL_MATURITY_DATE = r45_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR45_EFFECTIVE_MATURITY_DATE() {
			return R45_EFFECTIVE_MATURITY_DATE;
		}
		public void setR45_EFFECTIVE_MATURITY_DATE(Date r45_EFFECTIVE_MATURITY_DATE) {
			R45_EFFECTIVE_MATURITY_DATE = r45_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR45_AMOUNT() {
			return R45_AMOUNT;
		}
		public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
			R45_AMOUNT = r45_AMOUNT;
		}
		public String getR46_PRODUCT() {
			return R46_PRODUCT;
		}
		public void setR46_PRODUCT(String r46_PRODUCT) {
			R46_PRODUCT = r46_PRODUCT;
		}
		public Date getR46_ISSUANCE_DATE() {
			return R46_ISSUANCE_DATE;
		}
		public void setR46_ISSUANCE_DATE(Date r46_ISSUANCE_DATE) {
			R46_ISSUANCE_DATE = r46_ISSUANCE_DATE;
		}
		public Date getR46_CONTRACTUAL_MATURITY_DATE() {
			return R46_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR46_CONTRACTUAL_MATURITY_DATE(Date r46_CONTRACTUAL_MATURITY_DATE) {
			R46_CONTRACTUAL_MATURITY_DATE = r46_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR46_EFFECTIVE_MATURITY_DATE() {
			return R46_EFFECTIVE_MATURITY_DATE;
		}
		public void setR46_EFFECTIVE_MATURITY_DATE(Date r46_EFFECTIVE_MATURITY_DATE) {
			R46_EFFECTIVE_MATURITY_DATE = r46_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR46_AMOUNT() {
			return R46_AMOUNT;
		}
		public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
			R46_AMOUNT = r46_AMOUNT;
		}
		public BigDecimal getR47_AMOUNT() {
			return R47_AMOUNT;
		}
		public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
			R47_AMOUNT = r47_AMOUNT;
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
		public Date getReportResubDate() {
			return reportResubDate;
		}
		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
		public String getDEL_FLG() {
			return DEL_FLG;
		}
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
		public M_CA6_Archival_Summary_Entity2() {
			super();
			// TODO Auto-generated constructor stub
		}
	}


	public static class M_CA6_Archival_Detail_Entity1 {
	
		private Date R12_CALENDAR_YEAR;
		private BigDecimal R12_CAP;
		private BigDecimal R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R13_CALENDAR_YEAR;
		private BigDecimal R13_CAP;
		private BigDecimal R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R14_CALENDAR_YEAR;
		private BigDecimal R14_CAP;
		private BigDecimal R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R15_CALENDAR_YEAR;
		private BigDecimal R15_CAP;
		private BigDecimal R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R16_CALENDAR_YEAR;
		private BigDecimal R16_CAP;
		private BigDecimal R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R20_CALENDAR_YEAR;
		private BigDecimal R20_CAP;
		private BigDecimal R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R21_CALENDAR_YEAR;
		private BigDecimal R21_CAP;
		private BigDecimal R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R22_CALENDAR_YEAR;
		private BigDecimal R22_CAP;
		private BigDecimal R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R23_CALENDAR_YEAR;
		private BigDecimal R23_CAP;
		private BigDecimal R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R24_CALENDAR_YEAR;
		private BigDecimal R24_CAP;
		private BigDecimal R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		private Date reportResubDate;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
	
		public Date getR12_CALENDAR_YEAR() {
			return R12_CALENDAR_YEAR;
		}
	
		public void setR12_CALENDAR_YEAR(Date r12_CALENDAR_YEAR) {
			R12_CALENDAR_YEAR = r12_CALENDAR_YEAR;
		}
	
		public BigDecimal getR12_CAP() {
			return R12_CAP;
		}
	
		public void setR12_CAP(BigDecimal r12_CAP) {
			R12_CAP = r12_CAP;
		}
	
		public BigDecimal getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR13_CALENDAR_YEAR() {
			return R13_CALENDAR_YEAR;
		}
	
		public void setR13_CALENDAR_YEAR(Date r13_CALENDAR_YEAR) {
			R13_CALENDAR_YEAR = r13_CALENDAR_YEAR;
		}
	
		public BigDecimal getR13_CAP() {
			return R13_CAP;
		}
	
		public void setR13_CAP(BigDecimal r13_CAP) {
			R13_CAP = r13_CAP;
		}
	
		public BigDecimal getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR14_CALENDAR_YEAR() {
			return R14_CALENDAR_YEAR;
		}
	
		public void setR14_CALENDAR_YEAR(Date r14_CALENDAR_YEAR) {
			R14_CALENDAR_YEAR = r14_CALENDAR_YEAR;
		}
	
		public BigDecimal getR14_CAP() {
			return R14_CAP;
		}
	
		public void setR14_CAP(BigDecimal r14_CAP) {
			R14_CAP = r14_CAP;
		}
	
		public BigDecimal getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR15_CALENDAR_YEAR() {
			return R15_CALENDAR_YEAR;
		}
	
		public void setR15_CALENDAR_YEAR(Date r15_CALENDAR_YEAR) {
			R15_CALENDAR_YEAR = r15_CALENDAR_YEAR;
		}
	
		public BigDecimal getR15_CAP() {
			return R15_CAP;
		}
	
		public void setR15_CAP(BigDecimal r15_CAP) {
			R15_CAP = r15_CAP;
		}
	
		public BigDecimal getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR16_CALENDAR_YEAR() {
			return R16_CALENDAR_YEAR;
		}
	
		public void setR16_CALENDAR_YEAR(Date r16_CALENDAR_YEAR) {
			R16_CALENDAR_YEAR = r16_CALENDAR_YEAR;
		}
	
		public BigDecimal getR16_CAP() {
			return R16_CAP;
		}
	
		public void setR16_CAP(BigDecimal r16_CAP) {
			R16_CAP = r16_CAP;
		}
	
		public BigDecimal getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR20_CALENDAR_YEAR() {
			return R20_CALENDAR_YEAR;
		}
	
		public void setR20_CALENDAR_YEAR(Date r20_CALENDAR_YEAR) {
			R20_CALENDAR_YEAR = r20_CALENDAR_YEAR;
		}
	
		public BigDecimal getR20_CAP() {
			return R20_CAP;
		}
	
		public void setR20_CAP(BigDecimal r20_CAP) {
			R20_CAP = r20_CAP;
		}
	
		public BigDecimal getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR21_CALENDAR_YEAR() {
			return R21_CALENDAR_YEAR;
		}
	
		public void setR21_CALENDAR_YEAR(Date r21_CALENDAR_YEAR) {
			R21_CALENDAR_YEAR = r21_CALENDAR_YEAR;
		}
	
		public BigDecimal getR21_CAP() {
			return R21_CAP;
		}
	
		public void setR21_CAP(BigDecimal r21_CAP) {
			R21_CAP = r21_CAP;
		}
	
		public BigDecimal getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR22_CALENDAR_YEAR() {
			return R22_CALENDAR_YEAR;
		}
	
		public void setR22_CALENDAR_YEAR(Date r22_CALENDAR_YEAR) {
			R22_CALENDAR_YEAR = r22_CALENDAR_YEAR;
		}
	
		public BigDecimal getR22_CAP() {
			return R22_CAP;
		}
	
		public void setR22_CAP(BigDecimal r22_CAP) {
			R22_CAP = r22_CAP;
		}
	
		public BigDecimal getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR23_CALENDAR_YEAR() {
			return R23_CALENDAR_YEAR;
		}
	
		public void setR23_CALENDAR_YEAR(Date r23_CALENDAR_YEAR) {
			R23_CALENDAR_YEAR = r23_CALENDAR_YEAR;
		}
	
		public BigDecimal getR23_CAP() {
			return R23_CAP;
		}
	
		public void setR23_CAP(BigDecimal r23_CAP) {
			R23_CAP = r23_CAP;
		}
	
		public BigDecimal getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR24_CALENDAR_YEAR() {
			return R24_CALENDAR_YEAR;
		}
	
		public void setR24_CALENDAR_YEAR(Date r24_CALENDAR_YEAR) {
			R24_CALENDAR_YEAR = r24_CALENDAR_YEAR;
		}
	
		public BigDecimal getR24_CAP() {
			return R24_CAP;
		}
	
		public void setR24_CAP(BigDecimal r24_CAP) {
			R24_CAP = r24_CAP;
		}
	
		public BigDecimal getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
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
	
		public Date getReportResubDate() {
			return reportResubDate;
		}
	
		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}
	
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
	
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
	
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
	
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
	
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
	
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
	
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
	
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
	
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
	
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
	
		public String getDEL_FLG() {
			return DEL_FLG;
		}
	
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
	
		public M_CA6_Archival_Detail_Entity1() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	}


	public static class M_CA6_Archival_Detail_Entity2 {
	
		private String R28_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R28_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R28_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R28_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R28_AMOUNT;
	
		private String R29_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R29_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R29_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R29_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R29_AMOUNT;
	
		private String R30_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R30_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R30_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R30_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R30_AMOUNT;
	
		private String R31_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R31_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R31_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R31_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R31_AMOUNT;
	
		private String R32_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R32_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R32_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R32_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R32_AMOUNT;
	
		private String R33_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R33_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R33_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R33_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R33_AMOUNT;
	
		private String R34_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R34_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R34_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R34_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R34_AMOUNT;
	
		private BigDecimal R35_AMOUNT;
	
		private String R40_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R40_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R40_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R40_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R40_AMOUNT;
	
		private String R41_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R41_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R41_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R41_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R41_AMOUNT;
	
		private String R42_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R42_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R42_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R42_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R42_AMOUNT;
	
		private String R43_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R43_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R43_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R43_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R43_AMOUNT;
	
		private String R44_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R44_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R44_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R44_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R44_AMOUNT;
	
		private String R45_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R45_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R45_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R45_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R45_AMOUNT;
	
		private String R46_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R46_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R46_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R46_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R46_AMOUNT;
	
		private BigDecimal R47_AMOUNT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		private Date reportResubDate;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
	
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}
	
		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}
	
		public Date getR28_ISSUANCE_DATE() {
			return R28_ISSUANCE_DATE;
		}
	
		public void setR28_ISSUANCE_DATE(Date r28_ISSUANCE_DATE) {
			R28_ISSUANCE_DATE = r28_ISSUANCE_DATE;
		}
	
		public Date getR28_CONTRACTUAL_MATURITY_DATE() {
			return R28_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR28_CONTRACTUAL_MATURITY_DATE(Date r28_CONTRACTUAL_MATURITY_DATE) {
			R28_CONTRACTUAL_MATURITY_DATE = r28_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR28_EFFECTIVE_MATURITY_DATE() {
			return R28_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR28_EFFECTIVE_MATURITY_DATE(Date r28_EFFECTIVE_MATURITY_DATE) {
			R28_EFFECTIVE_MATURITY_DATE = r28_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}
	
		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}
	
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}
	
		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}
	
		public Date getR29_ISSUANCE_DATE() {
			return R29_ISSUANCE_DATE;
		}
	
		public void setR29_ISSUANCE_DATE(Date r29_ISSUANCE_DATE) {
			R29_ISSUANCE_DATE = r29_ISSUANCE_DATE;
		}
	
		public Date getR29_CONTRACTUAL_MATURITY_DATE() {
			return R29_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR29_CONTRACTUAL_MATURITY_DATE(Date r29_CONTRACTUAL_MATURITY_DATE) {
			R29_CONTRACTUAL_MATURITY_DATE = r29_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR29_EFFECTIVE_MATURITY_DATE() {
			return R29_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR29_EFFECTIVE_MATURITY_DATE(Date r29_EFFECTIVE_MATURITY_DATE) {
			R29_EFFECTIVE_MATURITY_DATE = r29_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}
	
		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}
	
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}
	
		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}
	
		public Date getR30_ISSUANCE_DATE() {
			return R30_ISSUANCE_DATE;
		}
	
		public void setR30_ISSUANCE_DATE(Date r30_ISSUANCE_DATE) {
			R30_ISSUANCE_DATE = r30_ISSUANCE_DATE;
		}
	
		public Date getR30_CONTRACTUAL_MATURITY_DATE() {
			return R30_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR30_CONTRACTUAL_MATURITY_DATE(Date r30_CONTRACTUAL_MATURITY_DATE) {
			R30_CONTRACTUAL_MATURITY_DATE = r30_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR30_EFFECTIVE_MATURITY_DATE() {
			return R30_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR30_EFFECTIVE_MATURITY_DATE(Date r30_EFFECTIVE_MATURITY_DATE) {
			R30_EFFECTIVE_MATURITY_DATE = r30_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}
	
		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}
	
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}
	
		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}
	
		public Date getR31_ISSUANCE_DATE() {
			return R31_ISSUANCE_DATE;
		}
	
		public void setR31_ISSUANCE_DATE(Date r31_ISSUANCE_DATE) {
			R31_ISSUANCE_DATE = r31_ISSUANCE_DATE;
		}
	
		public Date getR31_CONTRACTUAL_MATURITY_DATE() {
			return R31_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR31_CONTRACTUAL_MATURITY_DATE(Date r31_CONTRACTUAL_MATURITY_DATE) {
			R31_CONTRACTUAL_MATURITY_DATE = r31_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR31_EFFECTIVE_MATURITY_DATE() {
			return R31_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR31_EFFECTIVE_MATURITY_DATE(Date r31_EFFECTIVE_MATURITY_DATE) {
			R31_EFFECTIVE_MATURITY_DATE = r31_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}
	
		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}
	
		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}
	
		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}
	
		public Date getR32_ISSUANCE_DATE() {
			return R32_ISSUANCE_DATE;
		}
	
		public void setR32_ISSUANCE_DATE(Date r32_ISSUANCE_DATE) {
			R32_ISSUANCE_DATE = r32_ISSUANCE_DATE;
		}
	
		public Date getR32_CONTRACTUAL_MATURITY_DATE() {
			return R32_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR32_CONTRACTUAL_MATURITY_DATE(Date r32_CONTRACTUAL_MATURITY_DATE) {
			R32_CONTRACTUAL_MATURITY_DATE = r32_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR32_EFFECTIVE_MATURITY_DATE() {
			return R32_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR32_EFFECTIVE_MATURITY_DATE(Date r32_EFFECTIVE_MATURITY_DATE) {
			R32_EFFECTIVE_MATURITY_DATE = r32_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}
	
		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}
	
		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}
	
		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}
	
		public Date getR33_ISSUANCE_DATE() {
			return R33_ISSUANCE_DATE;
		}
	
		public void setR33_ISSUANCE_DATE(Date r33_ISSUANCE_DATE) {
			R33_ISSUANCE_DATE = r33_ISSUANCE_DATE;
		}
	
		public Date getR33_CONTRACTUAL_MATURITY_DATE() {
			return R33_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR33_CONTRACTUAL_MATURITY_DATE(Date r33_CONTRACTUAL_MATURITY_DATE) {
			R33_CONTRACTUAL_MATURITY_DATE = r33_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR33_EFFECTIVE_MATURITY_DATE() {
			return R33_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR33_EFFECTIVE_MATURITY_DATE(Date r33_EFFECTIVE_MATURITY_DATE) {
			R33_EFFECTIVE_MATURITY_DATE = r33_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}
	
		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
		}
	
		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}
	
		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}
	
		public Date getR34_ISSUANCE_DATE() {
			return R34_ISSUANCE_DATE;
		}
	
		public void setR34_ISSUANCE_DATE(Date r34_ISSUANCE_DATE) {
			R34_ISSUANCE_DATE = r34_ISSUANCE_DATE;
		}
	
		public Date getR34_CONTRACTUAL_MATURITY_DATE() {
			return R34_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR34_CONTRACTUAL_MATURITY_DATE(Date r34_CONTRACTUAL_MATURITY_DATE) {
			R34_CONTRACTUAL_MATURITY_DATE = r34_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR34_EFFECTIVE_MATURITY_DATE() {
			return R34_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR34_EFFECTIVE_MATURITY_DATE(Date r34_EFFECTIVE_MATURITY_DATE) {
			R34_EFFECTIVE_MATURITY_DATE = r34_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR34_AMOUNT() {
			return R34_AMOUNT;
		}
	
		public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
			R34_AMOUNT = r34_AMOUNT;
		}
	
		public BigDecimal getR35_AMOUNT() {
			return R35_AMOUNT;
		}
	
		public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
			R35_AMOUNT = r35_AMOUNT;
		}
	
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}
	
		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}
	
		public Date getR40_ISSUANCE_DATE() {
			return R40_ISSUANCE_DATE;
		}
	
		public void setR40_ISSUANCE_DATE(Date r40_ISSUANCE_DATE) {
			R40_ISSUANCE_DATE = r40_ISSUANCE_DATE;
		}
	
		public Date getR40_CONTRACTUAL_MATURITY_DATE() {
			return R40_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR40_CONTRACTUAL_MATURITY_DATE(Date r40_CONTRACTUAL_MATURITY_DATE) {
			R40_CONTRACTUAL_MATURITY_DATE = r40_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR40_EFFECTIVE_MATURITY_DATE() {
			return R40_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR40_EFFECTIVE_MATURITY_DATE(Date r40_EFFECTIVE_MATURITY_DATE) {
			R40_EFFECTIVE_MATURITY_DATE = r40_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}
	
		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}
	
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}
	
		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}
	
		public Date getR41_ISSUANCE_DATE() {
			return R41_ISSUANCE_DATE;
		}
	
		public void setR41_ISSUANCE_DATE(Date r41_ISSUANCE_DATE) {
			R41_ISSUANCE_DATE = r41_ISSUANCE_DATE;
		}
	
		public Date getR41_CONTRACTUAL_MATURITY_DATE() {
			return R41_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR41_CONTRACTUAL_MATURITY_DATE(Date r41_CONTRACTUAL_MATURITY_DATE) {
			R41_CONTRACTUAL_MATURITY_DATE = r41_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR41_EFFECTIVE_MATURITY_DATE() {
			return R41_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR41_EFFECTIVE_MATURITY_DATE(Date r41_EFFECTIVE_MATURITY_DATE) {
			R41_EFFECTIVE_MATURITY_DATE = r41_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}
	
		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}
	
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}
	
		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}
	
		public Date getR42_ISSUANCE_DATE() {
			return R42_ISSUANCE_DATE;
		}
	
		public void setR42_ISSUANCE_DATE(Date r42_ISSUANCE_DATE) {
			R42_ISSUANCE_DATE = r42_ISSUANCE_DATE;
		}
	
		public Date getR42_CONTRACTUAL_MATURITY_DATE() {
			return R42_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR42_CONTRACTUAL_MATURITY_DATE(Date r42_CONTRACTUAL_MATURITY_DATE) {
			R42_CONTRACTUAL_MATURITY_DATE = r42_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR42_EFFECTIVE_MATURITY_DATE() {
			return R42_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR42_EFFECTIVE_MATURITY_DATE(Date r42_EFFECTIVE_MATURITY_DATE) {
			R42_EFFECTIVE_MATURITY_DATE = r42_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}
	
		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}
	
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}
	
		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}
	
		public Date getR43_ISSUANCE_DATE() {
			return R43_ISSUANCE_DATE;
		}
	
		public void setR43_ISSUANCE_DATE(Date r43_ISSUANCE_DATE) {
			R43_ISSUANCE_DATE = r43_ISSUANCE_DATE;
		}
	
		public Date getR43_CONTRACTUAL_MATURITY_DATE() {
			return R43_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR43_CONTRACTUAL_MATURITY_DATE(Date r43_CONTRACTUAL_MATURITY_DATE) {
			R43_CONTRACTUAL_MATURITY_DATE = r43_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR43_EFFECTIVE_MATURITY_DATE() {
			return R43_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR43_EFFECTIVE_MATURITY_DATE(Date r43_EFFECTIVE_MATURITY_DATE) {
			R43_EFFECTIVE_MATURITY_DATE = r43_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}
	
		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}
	
		public String getR44_PRODUCT() {
			return R44_PRODUCT;
		}
	
		public void setR44_PRODUCT(String r44_PRODUCT) {
			R44_PRODUCT = r44_PRODUCT;
		}
	
		public Date getR44_ISSUANCE_DATE() {
			return R44_ISSUANCE_DATE;
		}
	
		public void setR44_ISSUANCE_DATE(Date r44_ISSUANCE_DATE) {
			R44_ISSUANCE_DATE = r44_ISSUANCE_DATE;
		}
	
		public Date getR44_CONTRACTUAL_MATURITY_DATE() {
			return R44_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR44_CONTRACTUAL_MATURITY_DATE(Date r44_CONTRACTUAL_MATURITY_DATE) {
			R44_CONTRACTUAL_MATURITY_DATE = r44_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR44_EFFECTIVE_MATURITY_DATE() {
			return R44_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR44_EFFECTIVE_MATURITY_DATE(Date r44_EFFECTIVE_MATURITY_DATE) {
			R44_EFFECTIVE_MATURITY_DATE = r44_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}
	
		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
		}
	
		public String getR45_PRODUCT() {
			return R45_PRODUCT;
		}
	
		public void setR45_PRODUCT(String r45_PRODUCT) {
			R45_PRODUCT = r45_PRODUCT;
		}
	
		public Date getR45_ISSUANCE_DATE() {
			return R45_ISSUANCE_DATE;
		}
	
		public void setR45_ISSUANCE_DATE(Date r45_ISSUANCE_DATE) {
			R45_ISSUANCE_DATE = r45_ISSUANCE_DATE;
		}
	
		public Date getR45_CONTRACTUAL_MATURITY_DATE() {
			return R45_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR45_CONTRACTUAL_MATURITY_DATE(Date r45_CONTRACTUAL_MATURITY_DATE) {
			R45_CONTRACTUAL_MATURITY_DATE = r45_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR45_EFFECTIVE_MATURITY_DATE() {
			return R45_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR45_EFFECTIVE_MATURITY_DATE(Date r45_EFFECTIVE_MATURITY_DATE) {
			R45_EFFECTIVE_MATURITY_DATE = r45_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR45_AMOUNT() {
			return R45_AMOUNT;
		}
	
		public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
			R45_AMOUNT = r45_AMOUNT;
		}
	
		public String getR46_PRODUCT() {
			return R46_PRODUCT;
		}
	
		public void setR46_PRODUCT(String r46_PRODUCT) {
			R46_PRODUCT = r46_PRODUCT;
		}
	
		public Date getR46_ISSUANCE_DATE() {
			return R46_ISSUANCE_DATE;
		}
	
		public void setR46_ISSUANCE_DATE(Date r46_ISSUANCE_DATE) {
			R46_ISSUANCE_DATE = r46_ISSUANCE_DATE;
		}
	
		public Date getR46_CONTRACTUAL_MATURITY_DATE() {
			return R46_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR46_CONTRACTUAL_MATURITY_DATE(Date r46_CONTRACTUAL_MATURITY_DATE) {
			R46_CONTRACTUAL_MATURITY_DATE = r46_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR46_EFFECTIVE_MATURITY_DATE() {
			return R46_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR46_EFFECTIVE_MATURITY_DATE(Date r46_EFFECTIVE_MATURITY_DATE) {
			R46_EFFECTIVE_MATURITY_DATE = r46_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR46_AMOUNT() {
			return R46_AMOUNT;
		}
	
		public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
			R46_AMOUNT = r46_AMOUNT;
		}
	
		public BigDecimal getR47_AMOUNT() {
			return R47_AMOUNT;
		}
	
		public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
			R47_AMOUNT = r47_AMOUNT;
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
	
		public Date getReportResubDate() {
			return reportResubDate;
		}
	
		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}
	
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
	
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
	
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
	
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
	
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
	
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
	
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
	
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
	
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
	
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
	
		public String getDEL_FLG() {
			return DEL_FLG;
		}
	
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
	
		public M_CA6_Archival_Detail_Entity2() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	}


	public static class M_CA6_RESUB_Summary_Entity1 {
		
		private Date R12_CALENDAR_YEAR;
	    private BigDecimal R12_CAP;
	    private BigDecimal R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R13_CALENDAR_YEAR;
	    private BigDecimal R13_CAP;
	    private BigDecimal R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R14_CALENDAR_YEAR;
	    private BigDecimal R14_CAP;
	    private BigDecimal R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R15_CALENDAR_YEAR;
	    private BigDecimal R15_CAP;
	    private BigDecimal R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R16_CALENDAR_YEAR;
	    private BigDecimal R16_CAP;
	    private BigDecimal R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R20_CALENDAR_YEAR;
	    private BigDecimal R20_CAP;
	    private BigDecimal R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R21_CALENDAR_YEAR;
	    private BigDecimal R21_CAP;
	    private BigDecimal R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R22_CALENDAR_YEAR;
	    private BigDecimal R22_CAP;
	    private BigDecimal R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R23_CALENDAR_YEAR;
	    private BigDecimal R23_CAP;
	    private BigDecimal R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
	    private Date R24_CALENDAR_YEAR;
	    private BigDecimal R24_CAP;
	    private BigDecimal R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	    private BigDecimal R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		    private Date reportResubDate;	
	    public String REPORT_FREQUENCY;
	    public String REPORT_CODE;
	    public String REPORT_DESC;
	    public String ENTITY_FLG;
	    public String MODIFY_FLG;
	    public String DEL_FLG;
		public Date getR12_CALENDAR_YEAR() {
			return R12_CALENDAR_YEAR;
		}
		public void setR12_CALENDAR_YEAR(Date r12_CALENDAR_YEAR) {
			R12_CALENDAR_YEAR = r12_CALENDAR_YEAR;
		}
		public BigDecimal getR12_CAP() {
			return R12_CAP;
		}
		public void setR12_CAP(BigDecimal r12_CAP) {
			R12_CAP = r12_CAP;
		}
		public BigDecimal getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR13_CALENDAR_YEAR() {
			return R13_CALENDAR_YEAR;
		}
		public void setR13_CALENDAR_YEAR(Date r13_CALENDAR_YEAR) {
			R13_CALENDAR_YEAR = r13_CALENDAR_YEAR;
		}
		public BigDecimal getR13_CAP() {
			return R13_CAP;
		}
		public void setR13_CAP(BigDecimal r13_CAP) {
			R13_CAP = r13_CAP;
		}
		public BigDecimal getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR14_CALENDAR_YEAR() {
			return R14_CALENDAR_YEAR;
		}
		public void setR14_CALENDAR_YEAR(Date r14_CALENDAR_YEAR) {
			R14_CALENDAR_YEAR = r14_CALENDAR_YEAR;
		}
		public BigDecimal getR14_CAP() {
			return R14_CAP;
		}
		public void setR14_CAP(BigDecimal r14_CAP) {
			R14_CAP = r14_CAP;
		}
		public BigDecimal getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR15_CALENDAR_YEAR() {
			return R15_CALENDAR_YEAR;
		}
		public void setR15_CALENDAR_YEAR(Date r15_CALENDAR_YEAR) {
			R15_CALENDAR_YEAR = r15_CALENDAR_YEAR;
		}
		public BigDecimal getR15_CAP() {
			return R15_CAP;
		}
		public void setR15_CAP(BigDecimal r15_CAP) {
			R15_CAP = r15_CAP;
		}
		public BigDecimal getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR16_CALENDAR_YEAR() {
			return R16_CALENDAR_YEAR;
		}
		public void setR16_CALENDAR_YEAR(Date r16_CALENDAR_YEAR) {
			R16_CALENDAR_YEAR = r16_CALENDAR_YEAR;
		}
		public BigDecimal getR16_CAP() {
			return R16_CAP;
		}
		public void setR16_CAP(BigDecimal r16_CAP) {
			R16_CAP = r16_CAP;
		}
		public BigDecimal getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR20_CALENDAR_YEAR() {
			return R20_CALENDAR_YEAR;
		}
		public void setR20_CALENDAR_YEAR(Date r20_CALENDAR_YEAR) {
			R20_CALENDAR_YEAR = r20_CALENDAR_YEAR;
		}
		public BigDecimal getR20_CAP() {
			return R20_CAP;
		}
		public void setR20_CAP(BigDecimal r20_CAP) {
			R20_CAP = r20_CAP;
		}
		public BigDecimal getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR21_CALENDAR_YEAR() {
			return R21_CALENDAR_YEAR;
		}
		public void setR21_CALENDAR_YEAR(Date r21_CALENDAR_YEAR) {
			R21_CALENDAR_YEAR = r21_CALENDAR_YEAR;
		}
		public BigDecimal getR21_CAP() {
			return R21_CAP;
		}
		public void setR21_CAP(BigDecimal r21_CAP) {
			R21_CAP = r21_CAP;
		}
		public BigDecimal getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR22_CALENDAR_YEAR() {
			return R22_CALENDAR_YEAR;
		}
		public void setR22_CALENDAR_YEAR(Date r22_CALENDAR_YEAR) {
			R22_CALENDAR_YEAR = r22_CALENDAR_YEAR;
		}
		public BigDecimal getR22_CAP() {
			return R22_CAP;
		}
		public void setR22_CAP(BigDecimal r22_CAP) {
			R22_CAP = r22_CAP;
		}
		public BigDecimal getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR23_CALENDAR_YEAR() {
			return R23_CALENDAR_YEAR;
		}
		public void setR23_CALENDAR_YEAR(Date r23_CALENDAR_YEAR) {
			R23_CALENDAR_YEAR = r23_CALENDAR_YEAR;
		}
		public BigDecimal getR23_CAP() {
			return R23_CAP;
		}
		public void setR23_CAP(BigDecimal r23_CAP) {
			R23_CAP = r23_CAP;
		}
		public BigDecimal getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public Date getR24_CALENDAR_YEAR() {
			return R24_CALENDAR_YEAR;
		}
		public void setR24_CALENDAR_YEAR(Date r24_CALENDAR_YEAR) {
			R24_CALENDAR_YEAR = r24_CALENDAR_YEAR;
		}
		public BigDecimal getR24_CAP() {
			return R24_CAP;
		}
		public void setR24_CAP(BigDecimal r24_CAP) {
			R24_CAP = r24_CAP;
		}
		public BigDecimal getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public BigDecimal getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
		public void setR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
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
		public Date getReportResubDate() {
			return reportResubDate;
		}
		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
		public String getDEL_FLG() {
			return DEL_FLG;
		}
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
		public M_CA6_RESUB_Summary_Entity1() {
			super();
			// TODO Auto-generated constructor stub
		}
	}


	public static class M_CA6_RESUB_Summary_Entity2 {
		
	
		private String R28_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R28_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R28_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R28_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R28_AMOUNT;
	
	    private String R29_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R29_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R29_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R29_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R29_AMOUNT;
	
	    private String R30_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R30_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R30_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R30_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R30_AMOUNT;
	
	    private String R31_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R31_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R31_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R31_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R31_AMOUNT;
	
	    private String R32_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R32_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R32_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R32_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R32_AMOUNT;
	
	    private String R33_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R33_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R33_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R33_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R33_AMOUNT;
	
	    private String R34_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R34_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R34_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R34_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R34_AMOUNT;
	
	    private BigDecimal R35_AMOUNT;
	
	    private String R40_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R40_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R40_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R40_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R40_AMOUNT;
	
	    private String R41_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R41_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R41_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R41_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R41_AMOUNT;
	
	    private String R42_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R42_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R42_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R42_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R42_AMOUNT;
	
	    private String R43_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R43_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R43_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R43_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R43_AMOUNT;
	
	    private String R44_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R44_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R44_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R44_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R44_AMOUNT;
	
	    private String R45_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R45_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R45_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R45_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R45_AMOUNT;
	
	    private String R46_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R46_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R46_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date R46_EFFECTIVE_MATURITY_DATE;
	    private BigDecimal R46_AMOUNT;
	
	    private BigDecimal R47_AMOUNT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		    private Date reportResubDate;	
	    public String REPORT_FREQUENCY;
	    public String REPORT_CODE;
	    public String REPORT_DESC;
	    public String ENTITY_FLG;
	    public String MODIFY_FLG;
	    public String DEL_FLG;
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}
		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}
		public Date getR28_ISSUANCE_DATE() {
			return R28_ISSUANCE_DATE;
		}
		public void setR28_ISSUANCE_DATE(Date r28_ISSUANCE_DATE) {
			R28_ISSUANCE_DATE = r28_ISSUANCE_DATE;
		}
		public Date getR28_CONTRACTUAL_MATURITY_DATE() {
			return R28_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR28_CONTRACTUAL_MATURITY_DATE(Date r28_CONTRACTUAL_MATURITY_DATE) {
			R28_CONTRACTUAL_MATURITY_DATE = r28_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR28_EFFECTIVE_MATURITY_DATE() {
			return R28_EFFECTIVE_MATURITY_DATE;
		}
		public void setR28_EFFECTIVE_MATURITY_DATE(Date r28_EFFECTIVE_MATURITY_DATE) {
			R28_EFFECTIVE_MATURITY_DATE = r28_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}
		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}
		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}
		public Date getR29_ISSUANCE_DATE() {
			return R29_ISSUANCE_DATE;
		}
		public void setR29_ISSUANCE_DATE(Date r29_ISSUANCE_DATE) {
			R29_ISSUANCE_DATE = r29_ISSUANCE_DATE;
		}
		public Date getR29_CONTRACTUAL_MATURITY_DATE() {
			return R29_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR29_CONTRACTUAL_MATURITY_DATE(Date r29_CONTRACTUAL_MATURITY_DATE) {
			R29_CONTRACTUAL_MATURITY_DATE = r29_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR29_EFFECTIVE_MATURITY_DATE() {
			return R29_EFFECTIVE_MATURITY_DATE;
		}
		public void setR29_EFFECTIVE_MATURITY_DATE(Date r29_EFFECTIVE_MATURITY_DATE) {
			R29_EFFECTIVE_MATURITY_DATE = r29_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}
		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}
		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}
		public Date getR30_ISSUANCE_DATE() {
			return R30_ISSUANCE_DATE;
		}
		public void setR30_ISSUANCE_DATE(Date r30_ISSUANCE_DATE) {
			R30_ISSUANCE_DATE = r30_ISSUANCE_DATE;
		}
		public Date getR30_CONTRACTUAL_MATURITY_DATE() {
			return R30_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR30_CONTRACTUAL_MATURITY_DATE(Date r30_CONTRACTUAL_MATURITY_DATE) {
			R30_CONTRACTUAL_MATURITY_DATE = r30_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR30_EFFECTIVE_MATURITY_DATE() {
			return R30_EFFECTIVE_MATURITY_DATE;
		}
		public void setR30_EFFECTIVE_MATURITY_DATE(Date r30_EFFECTIVE_MATURITY_DATE) {
			R30_EFFECTIVE_MATURITY_DATE = r30_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}
		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}
		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}
		public Date getR31_ISSUANCE_DATE() {
			return R31_ISSUANCE_DATE;
		}
		public void setR31_ISSUANCE_DATE(Date r31_ISSUANCE_DATE) {
			R31_ISSUANCE_DATE = r31_ISSUANCE_DATE;
		}
		public Date getR31_CONTRACTUAL_MATURITY_DATE() {
			return R31_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR31_CONTRACTUAL_MATURITY_DATE(Date r31_CONTRACTUAL_MATURITY_DATE) {
			R31_CONTRACTUAL_MATURITY_DATE = r31_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR31_EFFECTIVE_MATURITY_DATE() {
			return R31_EFFECTIVE_MATURITY_DATE;
		}
		public void setR31_EFFECTIVE_MATURITY_DATE(Date r31_EFFECTIVE_MATURITY_DATE) {
			R31_EFFECTIVE_MATURITY_DATE = r31_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}
		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}
		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}
		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}
		public Date getR32_ISSUANCE_DATE() {
			return R32_ISSUANCE_DATE;
		}
		public void setR32_ISSUANCE_DATE(Date r32_ISSUANCE_DATE) {
			R32_ISSUANCE_DATE = r32_ISSUANCE_DATE;
		}
		public Date getR32_CONTRACTUAL_MATURITY_DATE() {
			return R32_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR32_CONTRACTUAL_MATURITY_DATE(Date r32_CONTRACTUAL_MATURITY_DATE) {
			R32_CONTRACTUAL_MATURITY_DATE = r32_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR32_EFFECTIVE_MATURITY_DATE() {
			return R32_EFFECTIVE_MATURITY_DATE;
		}
		public void setR32_EFFECTIVE_MATURITY_DATE(Date r32_EFFECTIVE_MATURITY_DATE) {
			R32_EFFECTIVE_MATURITY_DATE = r32_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}
		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}
		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}
		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}
		public Date getR33_ISSUANCE_DATE() {
			return R33_ISSUANCE_DATE;
		}
		public void setR33_ISSUANCE_DATE(Date r33_ISSUANCE_DATE) {
			R33_ISSUANCE_DATE = r33_ISSUANCE_DATE;
		}
		public Date getR33_CONTRACTUAL_MATURITY_DATE() {
			return R33_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR33_CONTRACTUAL_MATURITY_DATE(Date r33_CONTRACTUAL_MATURITY_DATE) {
			R33_CONTRACTUAL_MATURITY_DATE = r33_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR33_EFFECTIVE_MATURITY_DATE() {
			return R33_EFFECTIVE_MATURITY_DATE;
		}
		public void setR33_EFFECTIVE_MATURITY_DATE(Date r33_EFFECTIVE_MATURITY_DATE) {
			R33_EFFECTIVE_MATURITY_DATE = r33_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}
		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
		}
		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}
		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}
		public Date getR34_ISSUANCE_DATE() {
			return R34_ISSUANCE_DATE;
		}
		public void setR34_ISSUANCE_DATE(Date r34_ISSUANCE_DATE) {
			R34_ISSUANCE_DATE = r34_ISSUANCE_DATE;
		}
		public Date getR34_CONTRACTUAL_MATURITY_DATE() {
			return R34_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR34_CONTRACTUAL_MATURITY_DATE(Date r34_CONTRACTUAL_MATURITY_DATE) {
			R34_CONTRACTUAL_MATURITY_DATE = r34_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR34_EFFECTIVE_MATURITY_DATE() {
			return R34_EFFECTIVE_MATURITY_DATE;
		}
		public void setR34_EFFECTIVE_MATURITY_DATE(Date r34_EFFECTIVE_MATURITY_DATE) {
			R34_EFFECTIVE_MATURITY_DATE = r34_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR34_AMOUNT() {
			return R34_AMOUNT;
		}
		public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
			R34_AMOUNT = r34_AMOUNT;
		}
		public BigDecimal getR35_AMOUNT() {
			return R35_AMOUNT;
		}
		public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
			R35_AMOUNT = r35_AMOUNT;
		}
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}
		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}
		public Date getR40_ISSUANCE_DATE() {
			return R40_ISSUANCE_DATE;
		}
		public void setR40_ISSUANCE_DATE(Date r40_ISSUANCE_DATE) {
			R40_ISSUANCE_DATE = r40_ISSUANCE_DATE;
		}
		public Date getR40_CONTRACTUAL_MATURITY_DATE() {
			return R40_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR40_CONTRACTUAL_MATURITY_DATE(Date r40_CONTRACTUAL_MATURITY_DATE) {
			R40_CONTRACTUAL_MATURITY_DATE = r40_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR40_EFFECTIVE_MATURITY_DATE() {
			return R40_EFFECTIVE_MATURITY_DATE;
		}
		public void setR40_EFFECTIVE_MATURITY_DATE(Date r40_EFFECTIVE_MATURITY_DATE) {
			R40_EFFECTIVE_MATURITY_DATE = r40_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}
		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}
		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}
		public Date getR41_ISSUANCE_DATE() {
			return R41_ISSUANCE_DATE;
		}
		public void setR41_ISSUANCE_DATE(Date r41_ISSUANCE_DATE) {
			R41_ISSUANCE_DATE = r41_ISSUANCE_DATE;
		}
		public Date getR41_CONTRACTUAL_MATURITY_DATE() {
			return R41_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR41_CONTRACTUAL_MATURITY_DATE(Date r41_CONTRACTUAL_MATURITY_DATE) {
			R41_CONTRACTUAL_MATURITY_DATE = r41_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR41_EFFECTIVE_MATURITY_DATE() {
			return R41_EFFECTIVE_MATURITY_DATE;
		}
		public void setR41_EFFECTIVE_MATURITY_DATE(Date r41_EFFECTIVE_MATURITY_DATE) {
			R41_EFFECTIVE_MATURITY_DATE = r41_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}
		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}
		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}
		public Date getR42_ISSUANCE_DATE() {
			return R42_ISSUANCE_DATE;
		}
		public void setR42_ISSUANCE_DATE(Date r42_ISSUANCE_DATE) {
			R42_ISSUANCE_DATE = r42_ISSUANCE_DATE;
		}
		public Date getR42_CONTRACTUAL_MATURITY_DATE() {
			return R42_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR42_CONTRACTUAL_MATURITY_DATE(Date r42_CONTRACTUAL_MATURITY_DATE) {
			R42_CONTRACTUAL_MATURITY_DATE = r42_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR42_EFFECTIVE_MATURITY_DATE() {
			return R42_EFFECTIVE_MATURITY_DATE;
		}
		public void setR42_EFFECTIVE_MATURITY_DATE(Date r42_EFFECTIVE_MATURITY_DATE) {
			R42_EFFECTIVE_MATURITY_DATE = r42_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}
		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}
		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}
		public Date getR43_ISSUANCE_DATE() {
			return R43_ISSUANCE_DATE;
		}
		public void setR43_ISSUANCE_DATE(Date r43_ISSUANCE_DATE) {
			R43_ISSUANCE_DATE = r43_ISSUANCE_DATE;
		}
		public Date getR43_CONTRACTUAL_MATURITY_DATE() {
			return R43_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR43_CONTRACTUAL_MATURITY_DATE(Date r43_CONTRACTUAL_MATURITY_DATE) {
			R43_CONTRACTUAL_MATURITY_DATE = r43_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR43_EFFECTIVE_MATURITY_DATE() {
			return R43_EFFECTIVE_MATURITY_DATE;
		}
		public void setR43_EFFECTIVE_MATURITY_DATE(Date r43_EFFECTIVE_MATURITY_DATE) {
			R43_EFFECTIVE_MATURITY_DATE = r43_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}
		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}
		public String getR44_PRODUCT() {
			return R44_PRODUCT;
		}
		public void setR44_PRODUCT(String r44_PRODUCT) {
			R44_PRODUCT = r44_PRODUCT;
		}
		public Date getR44_ISSUANCE_DATE() {
			return R44_ISSUANCE_DATE;
		}
		public void setR44_ISSUANCE_DATE(Date r44_ISSUANCE_DATE) {
			R44_ISSUANCE_DATE = r44_ISSUANCE_DATE;
		}
		public Date getR44_CONTRACTUAL_MATURITY_DATE() {
			return R44_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR44_CONTRACTUAL_MATURITY_DATE(Date r44_CONTRACTUAL_MATURITY_DATE) {
			R44_CONTRACTUAL_MATURITY_DATE = r44_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR44_EFFECTIVE_MATURITY_DATE() {
			return R44_EFFECTIVE_MATURITY_DATE;
		}
		public void setR44_EFFECTIVE_MATURITY_DATE(Date r44_EFFECTIVE_MATURITY_DATE) {
			R44_EFFECTIVE_MATURITY_DATE = r44_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}
		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
		}
		public String getR45_PRODUCT() {
			return R45_PRODUCT;
		}
		public void setR45_PRODUCT(String r45_PRODUCT) {
			R45_PRODUCT = r45_PRODUCT;
		}
		public Date getR45_ISSUANCE_DATE() {
			return R45_ISSUANCE_DATE;
		}
		public void setR45_ISSUANCE_DATE(Date r45_ISSUANCE_DATE) {
			R45_ISSUANCE_DATE = r45_ISSUANCE_DATE;
		}
		public Date getR45_CONTRACTUAL_MATURITY_DATE() {
			return R45_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR45_CONTRACTUAL_MATURITY_DATE(Date r45_CONTRACTUAL_MATURITY_DATE) {
			R45_CONTRACTUAL_MATURITY_DATE = r45_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR45_EFFECTIVE_MATURITY_DATE() {
			return R45_EFFECTIVE_MATURITY_DATE;
		}
		public void setR45_EFFECTIVE_MATURITY_DATE(Date r45_EFFECTIVE_MATURITY_DATE) {
			R45_EFFECTIVE_MATURITY_DATE = r45_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR45_AMOUNT() {
			return R45_AMOUNT;
		}
		public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
			R45_AMOUNT = r45_AMOUNT;
		}
		public String getR46_PRODUCT() {
			return R46_PRODUCT;
		}
		public void setR46_PRODUCT(String r46_PRODUCT) {
			R46_PRODUCT = r46_PRODUCT;
		}
		public Date getR46_ISSUANCE_DATE() {
			return R46_ISSUANCE_DATE;
		}
		public void setR46_ISSUANCE_DATE(Date r46_ISSUANCE_DATE) {
			R46_ISSUANCE_DATE = r46_ISSUANCE_DATE;
		}
		public Date getR46_CONTRACTUAL_MATURITY_DATE() {
			return R46_CONTRACTUAL_MATURITY_DATE;
		}
		public void setR46_CONTRACTUAL_MATURITY_DATE(Date r46_CONTRACTUAL_MATURITY_DATE) {
			R46_CONTRACTUAL_MATURITY_DATE = r46_CONTRACTUAL_MATURITY_DATE;
		}
		public Date getR46_EFFECTIVE_MATURITY_DATE() {
			return R46_EFFECTIVE_MATURITY_DATE;
		}
		public void setR46_EFFECTIVE_MATURITY_DATE(Date r46_EFFECTIVE_MATURITY_DATE) {
			R46_EFFECTIVE_MATURITY_DATE = r46_EFFECTIVE_MATURITY_DATE;
		}
		public BigDecimal getR46_AMOUNT() {
			return R46_AMOUNT;
		}
		public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
			R46_AMOUNT = r46_AMOUNT;
		}
		public BigDecimal getR47_AMOUNT() {
			return R47_AMOUNT;
		}
		public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
			R47_AMOUNT = r47_AMOUNT;
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
		public Date getReportResubDate() {
			return reportResubDate;
		}
		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
		public String getDEL_FLG() {
			return DEL_FLG;
		}
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
		public M_CA6_RESUB_Summary_Entity2() {
			super();
			// TODO Auto-generated constructor stub
		}
	}


	public static class M_CA6_RESUB_Detail_Entity1 {
	
		private Date R12_CALENDAR_YEAR;
		private BigDecimal R12_CAP;
		private BigDecimal R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R13_CALENDAR_YEAR;
		private BigDecimal R13_CAP;
		private BigDecimal R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R14_CALENDAR_YEAR;
		private BigDecimal R14_CAP;
		private BigDecimal R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R15_CALENDAR_YEAR;
		private BigDecimal R15_CAP;
		private BigDecimal R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R16_CALENDAR_YEAR;
		private BigDecimal R16_CAP;
		private BigDecimal R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R20_CALENDAR_YEAR;
		private BigDecimal R20_CAP;
		private BigDecimal R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R21_CALENDAR_YEAR;
		private BigDecimal R21_CAP;
		private BigDecimal R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R22_CALENDAR_YEAR;
		private BigDecimal R22_CAP;
		private BigDecimal R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R23_CALENDAR_YEAR;
		private BigDecimal R23_CAP;
		private BigDecimal R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
	
		private Date R24_CALENDAR_YEAR;
		private BigDecimal R24_CAP;
		private BigDecimal R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		private BigDecimal R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		private Date reportResubDate;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
	
		public Date getR12_CALENDAR_YEAR() {
			return R12_CALENDAR_YEAR;
		}
	
		public void setR12_CALENDAR_YEAR(Date r12_CALENDAR_YEAR) {
			R12_CALENDAR_YEAR = r12_CALENDAR_YEAR;
		}
	
		public BigDecimal getR12_CAP() {
			return R12_CAP;
		}
	
		public void setR12_CAP(BigDecimal r12_CAP) {
			R12_CAP = r12_CAP;
		}
	
		public BigDecimal getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR13_CALENDAR_YEAR() {
			return R13_CALENDAR_YEAR;
		}
	
		public void setR13_CALENDAR_YEAR(Date r13_CALENDAR_YEAR) {
			R13_CALENDAR_YEAR = r13_CALENDAR_YEAR;
		}
	
		public BigDecimal getR13_CAP() {
			return R13_CAP;
		}
	
		public void setR13_CAP(BigDecimal r13_CAP) {
			R13_CAP = r13_CAP;
		}
	
		public BigDecimal getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR14_CALENDAR_YEAR() {
			return R14_CALENDAR_YEAR;
		}
	
		public void setR14_CALENDAR_YEAR(Date r14_CALENDAR_YEAR) {
			R14_CALENDAR_YEAR = r14_CALENDAR_YEAR;
		}
	
		public BigDecimal getR14_CAP() {
			return R14_CAP;
		}
	
		public void setR14_CAP(BigDecimal r14_CAP) {
			R14_CAP = r14_CAP;
		}
	
		public BigDecimal getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR15_CALENDAR_YEAR() {
			return R15_CALENDAR_YEAR;
		}
	
		public void setR15_CALENDAR_YEAR(Date r15_CALENDAR_YEAR) {
			R15_CALENDAR_YEAR = r15_CALENDAR_YEAR;
		}
	
		public BigDecimal getR15_CAP() {
			return R15_CAP;
		}
	
		public void setR15_CAP(BigDecimal r15_CAP) {
			R15_CAP = r15_CAP;
		}
	
		public BigDecimal getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR16_CALENDAR_YEAR() {
			return R16_CALENDAR_YEAR;
		}
	
		public void setR16_CALENDAR_YEAR(Date r16_CALENDAR_YEAR) {
			R16_CALENDAR_YEAR = r16_CALENDAR_YEAR;
		}
	
		public BigDecimal getR16_CAP() {
			return R16_CAP;
		}
	
		public void setR16_CAP(BigDecimal r16_CAP) {
			R16_CAP = r16_CAP;
		}
	
		public BigDecimal getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR20_CALENDAR_YEAR() {
			return R20_CALENDAR_YEAR;
		}
	
		public void setR20_CALENDAR_YEAR(Date r20_CALENDAR_YEAR) {
			R20_CALENDAR_YEAR = r20_CALENDAR_YEAR;
		}
	
		public BigDecimal getR20_CAP() {
			return R20_CAP;
		}
	
		public void setR20_CAP(BigDecimal r20_CAP) {
			R20_CAP = r20_CAP;
		}
	
		public BigDecimal getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR21_CALENDAR_YEAR() {
			return R21_CALENDAR_YEAR;
		}
	
		public void setR21_CALENDAR_YEAR(Date r21_CALENDAR_YEAR) {
			R21_CALENDAR_YEAR = r21_CALENDAR_YEAR;
		}
	
		public BigDecimal getR21_CAP() {
			return R21_CAP;
		}
	
		public void setR21_CAP(BigDecimal r21_CAP) {
			R21_CAP = r21_CAP;
		}
	
		public BigDecimal getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR22_CALENDAR_YEAR() {
			return R22_CALENDAR_YEAR;
		}
	
		public void setR22_CALENDAR_YEAR(Date r22_CALENDAR_YEAR) {
			R22_CALENDAR_YEAR = r22_CALENDAR_YEAR;
		}
	
		public BigDecimal getR22_CAP() {
			return R22_CAP;
		}
	
		public void setR22_CAP(BigDecimal r22_CAP) {
			R22_CAP = r22_CAP;
		}
	
		public BigDecimal getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR23_CALENDAR_YEAR() {
			return R23_CALENDAR_YEAR;
		}
	
		public void setR23_CALENDAR_YEAR(Date r23_CALENDAR_YEAR) {
			R23_CALENDAR_YEAR = r23_CALENDAR_YEAR;
		}
	
		public BigDecimal getR23_CAP() {
			return R23_CAP;
		}
	
		public void setR23_CAP(BigDecimal r23_CAP) {
			R23_CAP = r23_CAP;
		}
	
		public BigDecimal getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public Date getR24_CALENDAR_YEAR() {
			return R24_CALENDAR_YEAR;
		}
	
		public void setR24_CALENDAR_YEAR(Date r24_CALENDAR_YEAR) {
			R24_CALENDAR_YEAR = r24_CALENDAR_YEAR;
		}
	
		public BigDecimal getR24_CAP() {
			return R24_CAP;
		}
	
		public void setR24_CAP(BigDecimal r24_CAP) {
			R24_CAP = r24_CAP;
		}
	
		public BigDecimal getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(
				BigDecimal r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public BigDecimal getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() {
			return R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
		}
	
		public void setR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT(BigDecimal r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT) {
			R24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT = r24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT;
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
	
		public Date getReportResubDate() {
			return reportResubDate;
		}
	
		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}
	
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
	
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
	
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
	
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
	
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
	
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
	
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
	
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
	
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
	
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
	
		public String getDEL_FLG() {
			return DEL_FLG;
		}
	
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
	
		public M_CA6_RESUB_Detail_Entity1() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	}


	public static class M_CA6_RESUB_Detail_Entity2 {
	
		private String R28_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R28_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R28_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R28_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R28_AMOUNT;
	
		private String R29_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R29_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R29_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R29_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R29_AMOUNT;
	
		private String R30_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R30_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R30_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R30_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R30_AMOUNT;
	
		private String R31_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R31_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R31_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R31_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R31_AMOUNT;
	
		private String R32_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R32_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R32_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R32_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R32_AMOUNT;
	
		private String R33_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R33_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R33_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R33_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R33_AMOUNT;
	
		private String R34_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R34_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R34_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R34_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R34_AMOUNT;
	
		private BigDecimal R35_AMOUNT;
	
		private String R40_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R40_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R40_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R40_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R40_AMOUNT;
	
		private String R41_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R41_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R41_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R41_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R41_AMOUNT;
	
		private String R42_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R42_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R42_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R42_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R42_AMOUNT;
	
		private String R43_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R43_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R43_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R43_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R43_AMOUNT;
	
		private String R44_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R44_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R44_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R44_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R44_AMOUNT;
	
		private String R45_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R45_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R45_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R45_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R45_AMOUNT;
	
		private String R46_PRODUCT;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R46_ISSUANCE_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R46_CONTRACTUAL_MATURITY_DATE;
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date R46_EFFECTIVE_MATURITY_DATE;
		private BigDecimal R46_AMOUNT;
	
		private BigDecimal R47_AMOUNT;
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;
		private BigDecimal reportVersion;
		private Date reportResubDate;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;
	
		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}
	
		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}
	
		public Date getR28_ISSUANCE_DATE() {
			return R28_ISSUANCE_DATE;
		}
	
		public void setR28_ISSUANCE_DATE(Date r28_ISSUANCE_DATE) {
			R28_ISSUANCE_DATE = r28_ISSUANCE_DATE;
		}
	
		public Date getR28_CONTRACTUAL_MATURITY_DATE() {
			return R28_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR28_CONTRACTUAL_MATURITY_DATE(Date r28_CONTRACTUAL_MATURITY_DATE) {
			R28_CONTRACTUAL_MATURITY_DATE = r28_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR28_EFFECTIVE_MATURITY_DATE() {
			return R28_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR28_EFFECTIVE_MATURITY_DATE(Date r28_EFFECTIVE_MATURITY_DATE) {
			R28_EFFECTIVE_MATURITY_DATE = r28_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}
	
		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}
	
		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}
	
		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}
	
		public Date getR29_ISSUANCE_DATE() {
			return R29_ISSUANCE_DATE;
		}
	
		public void setR29_ISSUANCE_DATE(Date r29_ISSUANCE_DATE) {
			R29_ISSUANCE_DATE = r29_ISSUANCE_DATE;
		}
	
		public Date getR29_CONTRACTUAL_MATURITY_DATE() {
			return R29_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR29_CONTRACTUAL_MATURITY_DATE(Date r29_CONTRACTUAL_MATURITY_DATE) {
			R29_CONTRACTUAL_MATURITY_DATE = r29_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR29_EFFECTIVE_MATURITY_DATE() {
			return R29_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR29_EFFECTIVE_MATURITY_DATE(Date r29_EFFECTIVE_MATURITY_DATE) {
			R29_EFFECTIVE_MATURITY_DATE = r29_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}
	
		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}
	
		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}
	
		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}
	
		public Date getR30_ISSUANCE_DATE() {
			return R30_ISSUANCE_DATE;
		}
	
		public void setR30_ISSUANCE_DATE(Date r30_ISSUANCE_DATE) {
			R30_ISSUANCE_DATE = r30_ISSUANCE_DATE;
		}
	
		public Date getR30_CONTRACTUAL_MATURITY_DATE() {
			return R30_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR30_CONTRACTUAL_MATURITY_DATE(Date r30_CONTRACTUAL_MATURITY_DATE) {
			R30_CONTRACTUAL_MATURITY_DATE = r30_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR30_EFFECTIVE_MATURITY_DATE() {
			return R30_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR30_EFFECTIVE_MATURITY_DATE(Date r30_EFFECTIVE_MATURITY_DATE) {
			R30_EFFECTIVE_MATURITY_DATE = r30_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}
	
		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}
	
		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}
	
		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}
	
		public Date getR31_ISSUANCE_DATE() {
			return R31_ISSUANCE_DATE;
		}
	
		public void setR31_ISSUANCE_DATE(Date r31_ISSUANCE_DATE) {
			R31_ISSUANCE_DATE = r31_ISSUANCE_DATE;
		}
	
		public Date getR31_CONTRACTUAL_MATURITY_DATE() {
			return R31_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR31_CONTRACTUAL_MATURITY_DATE(Date r31_CONTRACTUAL_MATURITY_DATE) {
			R31_CONTRACTUAL_MATURITY_DATE = r31_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR31_EFFECTIVE_MATURITY_DATE() {
			return R31_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR31_EFFECTIVE_MATURITY_DATE(Date r31_EFFECTIVE_MATURITY_DATE) {
			R31_EFFECTIVE_MATURITY_DATE = r31_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}
	
		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}
	
		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}
	
		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}
	
		public Date getR32_ISSUANCE_DATE() {
			return R32_ISSUANCE_DATE;
		}
	
		public void setR32_ISSUANCE_DATE(Date r32_ISSUANCE_DATE) {
			R32_ISSUANCE_DATE = r32_ISSUANCE_DATE;
		}
	
		public Date getR32_CONTRACTUAL_MATURITY_DATE() {
			return R32_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR32_CONTRACTUAL_MATURITY_DATE(Date r32_CONTRACTUAL_MATURITY_DATE) {
			R32_CONTRACTUAL_MATURITY_DATE = r32_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR32_EFFECTIVE_MATURITY_DATE() {
			return R32_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR32_EFFECTIVE_MATURITY_DATE(Date r32_EFFECTIVE_MATURITY_DATE) {
			R32_EFFECTIVE_MATURITY_DATE = r32_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}
	
		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}
	
		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}
	
		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}
	
		public Date getR33_ISSUANCE_DATE() {
			return R33_ISSUANCE_DATE;
		}
	
		public void setR33_ISSUANCE_DATE(Date r33_ISSUANCE_DATE) {
			R33_ISSUANCE_DATE = r33_ISSUANCE_DATE;
		}
	
		public Date getR33_CONTRACTUAL_MATURITY_DATE() {
			return R33_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR33_CONTRACTUAL_MATURITY_DATE(Date r33_CONTRACTUAL_MATURITY_DATE) {
			R33_CONTRACTUAL_MATURITY_DATE = r33_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR33_EFFECTIVE_MATURITY_DATE() {
			return R33_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR33_EFFECTIVE_MATURITY_DATE(Date r33_EFFECTIVE_MATURITY_DATE) {
			R33_EFFECTIVE_MATURITY_DATE = r33_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}
	
		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
		}
	
		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}
	
		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}
	
		public Date getR34_ISSUANCE_DATE() {
			return R34_ISSUANCE_DATE;
		}
	
		public void setR34_ISSUANCE_DATE(Date r34_ISSUANCE_DATE) {
			R34_ISSUANCE_DATE = r34_ISSUANCE_DATE;
		}
	
		public Date getR34_CONTRACTUAL_MATURITY_DATE() {
			return R34_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR34_CONTRACTUAL_MATURITY_DATE(Date r34_CONTRACTUAL_MATURITY_DATE) {
			R34_CONTRACTUAL_MATURITY_DATE = r34_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR34_EFFECTIVE_MATURITY_DATE() {
			return R34_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR34_EFFECTIVE_MATURITY_DATE(Date r34_EFFECTIVE_MATURITY_DATE) {
			R34_EFFECTIVE_MATURITY_DATE = r34_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR34_AMOUNT() {
			return R34_AMOUNT;
		}
	
		public void setR34_AMOUNT(BigDecimal r34_AMOUNT) {
			R34_AMOUNT = r34_AMOUNT;
		}
	
		public BigDecimal getR35_AMOUNT() {
			return R35_AMOUNT;
		}
	
		public void setR35_AMOUNT(BigDecimal r35_AMOUNT) {
			R35_AMOUNT = r35_AMOUNT;
		}
	
		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}
	
		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}
	
		public Date getR40_ISSUANCE_DATE() {
			return R40_ISSUANCE_DATE;
		}
	
		public void setR40_ISSUANCE_DATE(Date r40_ISSUANCE_DATE) {
			R40_ISSUANCE_DATE = r40_ISSUANCE_DATE;
		}
	
		public Date getR40_CONTRACTUAL_MATURITY_DATE() {
			return R40_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR40_CONTRACTUAL_MATURITY_DATE(Date r40_CONTRACTUAL_MATURITY_DATE) {
			R40_CONTRACTUAL_MATURITY_DATE = r40_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR40_EFFECTIVE_MATURITY_DATE() {
			return R40_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR40_EFFECTIVE_MATURITY_DATE(Date r40_EFFECTIVE_MATURITY_DATE) {
			R40_EFFECTIVE_MATURITY_DATE = r40_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}
	
		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}
	
		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}
	
		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}
	
		public Date getR41_ISSUANCE_DATE() {
			return R41_ISSUANCE_DATE;
		}
	
		public void setR41_ISSUANCE_DATE(Date r41_ISSUANCE_DATE) {
			R41_ISSUANCE_DATE = r41_ISSUANCE_DATE;
		}
	
		public Date getR41_CONTRACTUAL_MATURITY_DATE() {
			return R41_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR41_CONTRACTUAL_MATURITY_DATE(Date r41_CONTRACTUAL_MATURITY_DATE) {
			R41_CONTRACTUAL_MATURITY_DATE = r41_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR41_EFFECTIVE_MATURITY_DATE() {
			return R41_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR41_EFFECTIVE_MATURITY_DATE(Date r41_EFFECTIVE_MATURITY_DATE) {
			R41_EFFECTIVE_MATURITY_DATE = r41_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}
	
		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}
	
		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}
	
		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}
	
		public Date getR42_ISSUANCE_DATE() {
			return R42_ISSUANCE_DATE;
		}
	
		public void setR42_ISSUANCE_DATE(Date r42_ISSUANCE_DATE) {
			R42_ISSUANCE_DATE = r42_ISSUANCE_DATE;
		}
	
		public Date getR42_CONTRACTUAL_MATURITY_DATE() {
			return R42_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR42_CONTRACTUAL_MATURITY_DATE(Date r42_CONTRACTUAL_MATURITY_DATE) {
			R42_CONTRACTUAL_MATURITY_DATE = r42_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR42_EFFECTIVE_MATURITY_DATE() {
			return R42_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR42_EFFECTIVE_MATURITY_DATE(Date r42_EFFECTIVE_MATURITY_DATE) {
			R42_EFFECTIVE_MATURITY_DATE = r42_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}
	
		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}
	
		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}
	
		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}
	
		public Date getR43_ISSUANCE_DATE() {
			return R43_ISSUANCE_DATE;
		}
	
		public void setR43_ISSUANCE_DATE(Date r43_ISSUANCE_DATE) {
			R43_ISSUANCE_DATE = r43_ISSUANCE_DATE;
		}
	
		public Date getR43_CONTRACTUAL_MATURITY_DATE() {
			return R43_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR43_CONTRACTUAL_MATURITY_DATE(Date r43_CONTRACTUAL_MATURITY_DATE) {
			R43_CONTRACTUAL_MATURITY_DATE = r43_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR43_EFFECTIVE_MATURITY_DATE() {
			return R43_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR43_EFFECTIVE_MATURITY_DATE(Date r43_EFFECTIVE_MATURITY_DATE) {
			R43_EFFECTIVE_MATURITY_DATE = r43_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}
	
		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}
	
		public String getR44_PRODUCT() {
			return R44_PRODUCT;
		}
	
		public void setR44_PRODUCT(String r44_PRODUCT) {
			R44_PRODUCT = r44_PRODUCT;
		}
	
		public Date getR44_ISSUANCE_DATE() {
			return R44_ISSUANCE_DATE;
		}
	
		public void setR44_ISSUANCE_DATE(Date r44_ISSUANCE_DATE) {
			R44_ISSUANCE_DATE = r44_ISSUANCE_DATE;
		}
	
		public Date getR44_CONTRACTUAL_MATURITY_DATE() {
			return R44_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR44_CONTRACTUAL_MATURITY_DATE(Date r44_CONTRACTUAL_MATURITY_DATE) {
			R44_CONTRACTUAL_MATURITY_DATE = r44_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR44_EFFECTIVE_MATURITY_DATE() {
			return R44_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR44_EFFECTIVE_MATURITY_DATE(Date r44_EFFECTIVE_MATURITY_DATE) {
			R44_EFFECTIVE_MATURITY_DATE = r44_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}
	
		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
		}
	
		public String getR45_PRODUCT() {
			return R45_PRODUCT;
		}
	
		public void setR45_PRODUCT(String r45_PRODUCT) {
			R45_PRODUCT = r45_PRODUCT;
		}
	
		public Date getR45_ISSUANCE_DATE() {
			return R45_ISSUANCE_DATE;
		}
	
		public void setR45_ISSUANCE_DATE(Date r45_ISSUANCE_DATE) {
			R45_ISSUANCE_DATE = r45_ISSUANCE_DATE;
		}
	
		public Date getR45_CONTRACTUAL_MATURITY_DATE() {
			return R45_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR45_CONTRACTUAL_MATURITY_DATE(Date r45_CONTRACTUAL_MATURITY_DATE) {
			R45_CONTRACTUAL_MATURITY_DATE = r45_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR45_EFFECTIVE_MATURITY_DATE() {
			return R45_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR45_EFFECTIVE_MATURITY_DATE(Date r45_EFFECTIVE_MATURITY_DATE) {
			R45_EFFECTIVE_MATURITY_DATE = r45_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR45_AMOUNT() {
			return R45_AMOUNT;
		}
	
		public void setR45_AMOUNT(BigDecimal r45_AMOUNT) {
			R45_AMOUNT = r45_AMOUNT;
		}
	
		public String getR46_PRODUCT() {
			return R46_PRODUCT;
		}
	
		public void setR46_PRODUCT(String r46_PRODUCT) {
			R46_PRODUCT = r46_PRODUCT;
		}
	
		public Date getR46_ISSUANCE_DATE() {
			return R46_ISSUANCE_DATE;
		}
	
		public void setR46_ISSUANCE_DATE(Date r46_ISSUANCE_DATE) {
			R46_ISSUANCE_DATE = r46_ISSUANCE_DATE;
		}
	
		public Date getR46_CONTRACTUAL_MATURITY_DATE() {
			return R46_CONTRACTUAL_MATURITY_DATE;
		}
	
		public void setR46_CONTRACTUAL_MATURITY_DATE(Date r46_CONTRACTUAL_MATURITY_DATE) {
			R46_CONTRACTUAL_MATURITY_DATE = r46_CONTRACTUAL_MATURITY_DATE;
		}
	
		public Date getR46_EFFECTIVE_MATURITY_DATE() {
			return R46_EFFECTIVE_MATURITY_DATE;
		}
	
		public void setR46_EFFECTIVE_MATURITY_DATE(Date r46_EFFECTIVE_MATURITY_DATE) {
			R46_EFFECTIVE_MATURITY_DATE = r46_EFFECTIVE_MATURITY_DATE;
		}
	
		public BigDecimal getR46_AMOUNT() {
			return R46_AMOUNT;
		}
	
		public void setR46_AMOUNT(BigDecimal r46_AMOUNT) {
			R46_AMOUNT = r46_AMOUNT;
		}
	
		public BigDecimal getR47_AMOUNT() {
			return R47_AMOUNT;
		}
	
		public void setR47_AMOUNT(BigDecimal r47_AMOUNT) {
			R47_AMOUNT = r47_AMOUNT;
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
	
		public Date getReportResubDate() {
			return reportResubDate;
		}
	
		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}
	
		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}
	
		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}
	
		public String getREPORT_CODE() {
			return REPORT_CODE;
		}
	
		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}
	
		public String getREPORT_DESC() {
			return REPORT_DESC;
		}
	
		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}
	
		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}
	
		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}
	
		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}
	
		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}
	
		public String getDEL_FLG() {
			return DEL_FLG;
		}
	
		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}
	
		public M_CA6_RESUB_Detail_Entity2() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	}

}
