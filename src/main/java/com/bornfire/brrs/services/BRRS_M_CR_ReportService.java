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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional
public class BRRS_M_CR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CR_ReportService.class);

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

	private static final String TBL_SUMMARY = "BRRS_M_CR_SUMMARYTABLE";
	private static final String TBL_DETAIL = "BRRS_M_CR_DETAILTABLE";
	private static final String TBL_ARCH_SUMMARY = "BRRS_M_CR_ARCHIVALTABLE_SUMMARY";
	private static final String TBL_ARCH_DETAIL = "BRRS_M_CR_ARCHIVALTABLE_DETAIL";
	private static final String TBL_RESUB_SUMMARY = "BRRS_M_CR_RESUB_SUMMARYTABLE";
	private static final String TBL_RESUB_DETAIL = "BRRS_M_CR_RESUB_DETAILTABLE";

	private <T> List<T> getByDate(String tableName, Date reportDate, Class<T> type) {
		String sql = "SELECT * FROM " + tableName + " WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type), reportDate);
	}

	private <T> Optional<T> getOneByDate(String tableName, Date reportDate, Class<T> type) {
		List<T> rows = getByDate(tableName, reportDate, type);
		return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
	}

	private <T> List<T> getByDateAndVersion(String tableName, Date reportDate, BigDecimal reportVersion,
			Class<T> type) {
		String sql = "SELECT * FROM " + tableName + " WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type), reportDate, reportVersion);
	}

	private <T> List<T> getAllWithVersion(String tableName, Class<T> type) {
		String sql = "SELECT * FROM " + tableName + " WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(type));
	}

	private BigDecimal findMaxResubVersion(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM " + TBL_RESUB_SUMMARY + " WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, BigDecimal.class, reportDate);
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
		String insertSql = "INSERT INTO " + tableName + " (" + String.join(", ", columns) + ") VALUES (" + placeholders
				+ ")";
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
		return "UPDATE " + tableName + " SET " + String.join(", ", setColumns) + " WHERE "
				+ String.join(" AND ", whereColumns);
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
		if ("reportResubDate".equals(fieldName)) {
			return "REPORT_RESUBDATE";
		}
		return fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
	}

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_CRView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

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
				List<M_CR_Archival_Summary_Entity> T1Master = getByDateAndVersion(TBL_ARCH_SUMMARY, d1, version,
						M_CR_Archival_Summary_Entity.class);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_CR_Resub_Summary_Entity> T1Master = getByDateAndVersion(TBL_RESUB_SUMMARY, d1, version,
						M_CR_Resub_Summary_Entity.class);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_CR_Summary_Entity> T1Master = getByDate(TBL_SUMMARY, dateformat.parse(todate),
						M_CR_Summary_Entity.class);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_CR_Archival_Detail_Entity> T1Master = getByDateAndVersion(TBL_ARCH_DETAIL, d1, version,
							M_CR_Archival_Detail_Entity.class);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_CR_Resub_Detail_Entity> T1Master = getByDateAndVersion(TBL_RESUB_DETAIL, d1, version,
							M_CR_Resub_Detail_Entity.class);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_CR_Detail_Entity> T1Master = getByDate(TBL_DETAIL, dateformat.parse(todate),
							M_CR_Detail_Entity.class);
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_CR");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_CR_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Fetch existing SUMMARY
		M_CR_Summary_Entity existingSummary = getOneByDate(TBL_SUMMARY, updatedEntity.getReport_date(),
				M_CR_Summary_Entity.class)
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		// Audit old copy
		M_CR_Summary_Entity oldcopy = new M_CR_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);

		// Fetch existing DETAIL
		M_CR_Detail_Entity existingDetail = getOneByDate(TBL_DETAIL, updatedEntity.getReport_date(),
				M_CR_Detail_Entity.class).orElseGet(() -> {
					M_CR_Detail_Entity d = new M_CR_Detail_Entity();
					d.setReport_date(updatedEntity.getReport_date());
					return d;
				});

		try {

			// Loop R10 → R17
			for (int i = 10; i <= 17; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "PRODUCT", "TOTAL_LONG_POS", "TOTAL_SHORT_POS", "GROSS_OPEN_POS",
						"CHARGE_BASIS_RISK", "CAPITAL_CHARGE_BASIS_RISK", "NET_OPEN_POS", "CHARGE_DIR_RISK",
						"CAPITAL_CHARGE_DIR_RISK", "TOTAL_CAPITAL_CHARGE" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {

						Method getter = M_CR_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_CR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Method detailSetter = M_CR_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						// Update SUMMARY
						summarySetter.invoke(existingSummary, newValue);

						// Update DETAIL
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Audit comparison
		String changes = auditService.getChanges(oldcopy, existingSummary);

		if (!changes.isEmpty()) {

			saveEntity(TBL_SUMMARY, existingSummary, "report_date");
			saveEntity(TBL_DETAIL, existingDetail, "report_date");

			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M CR Summary Screen", "BRRS_M_CR_SUMMARY");
		}
	}

	public void updateResubReport(M_CR_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = findMaxResubVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_CR_Resub_Summary_Entity resubSummary = new M_CR_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_CR_Resub_Detail_Entity resubDetail = new M_CR_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_CR_Archival_Summary_Entity archSummary = new M_CR_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_CR_Archival_Detail_Entity archDetail = new M_CR_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		saveEntity(TBL_RESUB_SUMMARY, resubSummary, "reportDate", "reportVersion");
		saveEntity(TBL_RESUB_DETAIL, resubDetail, "reportDate", "reportVersion");

		saveEntity(TBL_ARCH_SUMMARY, archSummary, "reportDate", "reportVersion");
		saveEntity(TBL_ARCH_DETAIL, archDetail, "reportDate", "reportVersion");
	}

	public List<Object[]> getM_CRResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CR_Archival_Summary_Entity> latestArchivalList = getAllWithVersion(TBL_ARCH_SUMMARY,
					M_CR_Archival_Summary_Entity.class);

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CR_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_CR Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_CRArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_CR_Archival_Summary_Entity> repoData = getAllWithVersion(TBL_ARCH_SUMMARY,
					M_CR_Archival_Summary_Entity.class);

			if (repoData != null && !repoData.isEmpty()) {
				for (M_CR_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_CR_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_CR Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getBRRS_M_CRExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_CRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CRResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_CREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_CR_Summary_Entity> dataList = getByDate(TBL_SUMMARY, dateformat.parse(todate),
						M_CR_Summary_Entity.class);

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_CR report. Returning empty result.");
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
							M_CR_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							// REPORT_DATE
							row = sheet.getRow(5);
							Cell cell1 = row.getCell(1);
							if (cell1 == null) {
								cell1 = row.createCell(1);
							}

							if (record.getReport_date() != null) {
								cell1.setCellValue(record.getReport_date()); // java.util.Date
								cell1.setCellStyle(dateStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							// ROW 10 DOWNLAOD
							row = sheet.getRow(9);
							Cell cell2 = row.createCell(0);
							if (record.getR10_PRODUCT() != null) {
								cell2.setCellValue(record.getR10_PRODUCT());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(1);
							if (record.getR10_TOTAL_LONG_POS() != null) {
								cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(2);
							if (record.getR10_TOTAL_SHORT_POS() != null) {
								cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(3);
							if (record.getR10_GROSS_OPEN_POS() != null) {
								cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							row = sheet.getRow(10);

							// ====================== R11 ======================
							cell2 = row.createCell(0);
							if (record.getR11_PRODUCT() != null) {
								cell2.setCellValue(record.getR11_PRODUCT());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(1);
							if (record.getR11_TOTAL_LONG_POS() != null) {
								cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(2);
							if (record.getR11_TOTAL_SHORT_POS() != null) {
								cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(3);
							if (record.getR11_GROSS_OPEN_POS() != null) {
								cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// ====================== R12 ======================
							row = sheet.getRow(11);

							cell2 = row.createCell(0);
							if (record.getR12_PRODUCT() != null) {
								cell2.setCellValue(record.getR12_PRODUCT());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(1);
							if (record.getR12_TOTAL_LONG_POS() != null) {
								cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(2);
							if (record.getR12_TOTAL_SHORT_POS() != null) {
								cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(3);
							if (record.getR12_GROSS_OPEN_POS() != null) {
								cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// ====================== R13 ======================
							row = sheet.getRow(12);

							// repeat same pattern...
							cell2 = row.createCell(0);
							if (record.getR13_PRODUCT() != null) {
								cell2.setCellValue(record.getR13_PRODUCT());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(1);
							if (record.getR13_TOTAL_LONG_POS() != null) {
								cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(2);
							if (record.getR13_TOTAL_SHORT_POS() != null) {
								cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(3);
							if (record.getR13_GROSS_OPEN_POS() != null) {
								cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// ====================== R14 ======================
							row = sheet.getRow(13);

							cell2 = row.createCell(0);
							if (record.getR14_PRODUCT() != null) {
								cell2.setCellValue(record.getR14_PRODUCT());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(1);
							if (record.getR14_TOTAL_LONG_POS() != null) {
								cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(2);
							if (record.getR14_TOTAL_SHORT_POS() != null) {
								cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(3);
							if (record.getR14_GROSS_OPEN_POS() != null) {
								cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// ====================== R15 ======================
							row = sheet.getRow(14);

							cell2 = row.createCell(0);
							if (record.getR15_PRODUCT() != null) {
								cell2.setCellValue(record.getR15_PRODUCT());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(1);
							if (record.getR15_TOTAL_LONG_POS() != null) {
								cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(2);
							if (record.getR15_TOTAL_SHORT_POS() != null) {
								cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(3);
							if (record.getR15_GROSS_OPEN_POS() != null) {
								cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// ====================== R16 ======================
							row = sheet.getRow(15);

							cell2 = row.createCell(0);
							if (record.getR16_PRODUCT() != null) {
								cell2.setCellValue(record.getR16_PRODUCT());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(1);
							if (record.getR16_TOTAL_LONG_POS() != null) {
								cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(2);
							if (record.getR16_TOTAL_SHORT_POS() != null) {
								cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(3);
							if (record.getR16_GROSS_OPEN_POS() != null) {
								cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

						}

						workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CR SUMMARY", null,
								"BRRS_M_CR_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_CREmailExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CRArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_CR_Summary_Entity> dataList = getByDate(TBL_SUMMARY, dateformat.parse(todate),
					M_CR_Summary_Entity.class);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_CR report. Returning empty result.");
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
						M_CR_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// REPORT_DATE
						row = sheet.getRow(5);
						Cell cell1 = row.getCell(1);
						if (cell1 == null) {
							cell1 = row.createCell(1);
						}

						if (record.getReport_date() != null) {
							cell1.setCellValue(record.getReport_date()); // java.util.Date
							cell1.setCellStyle(dateStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						// ROW 10 DOWNLAOD
						row = sheet.getRow(9);

						Cell cell2 = row.createCell(0);
						if (record.getR10_PRODUCT() != null) {
							cell2.setCellValue(record.getR10_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(1);
						if (record.getR10_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(2);
						if (record.getR10_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(3);
						if (record.getR10_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						row = sheet.getRow(10);

						// ====================== R11 ======================
						cell2 = row.createCell(0);
						if (record.getR11_PRODUCT() != null) {
							cell2.setCellValue(record.getR11_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR11_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR11_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR11_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R12 ======================
						row = sheet.getRow(11);

						cell2 = row.createCell(0);
						if (record.getR12_PRODUCT() != null) {
							cell2.setCellValue(record.getR12_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR12_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR12_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR12_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R13 ======================
						row = sheet.getRow(12);

						// repeat same pattern...
						cell2 = row.createCell(0);
						if (record.getR13_PRODUCT() != null) {
							cell2.setCellValue(record.getR13_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR13_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR13_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR13_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R14 ======================
						row = sheet.getRow(13);

						cell2 = row.createCell(0);
						if (record.getR14_PRODUCT() != null) {
							cell2.setCellValue(record.getR14_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR14_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR14_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR14_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R15 ======================
						row = sheet.getRow(14);

						cell2 = row.createCell(0);
						if (record.getR15_PRODUCT() != null) {
							cell2.setCellValue(record.getR15_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR15_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR15_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR15_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R16 ======================
						row = sheet.getRow(15);

						cell2 = row.createCell(0);
						if (record.getR16_PRODUCT() != null) {
							cell2.setCellValue(record.getR16_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR16_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR16_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR16_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CR EMAIL SUMMARY", null,
							"BRRS_M_CR_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_CRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_CRArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CR_Archival_Summary_Entity> dataList = getByDateAndVersion(TBL_ARCH_SUMMARY, dateformat.parse(todate),
				version, M_CR_Archival_Summary_Entity.class);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CR report. Returning empty result.");
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
					M_CR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(5);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
						cell1.setCellValue(record.getReportDate()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ROW 10 DOWNLAOD
					row = sheet.getRow(9);
					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CR ARCHIVAL SUMMARY", null,
						"BRRS_M_CR_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_CRArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CR_Archival_Summary_Entity> dataList = getByDateAndVersion(TBL_ARCH_SUMMARY, dateformat.parse(todate),
				version, M_CR_Archival_Summary_Entity.class);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CR report. Returning empty result.");
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
					M_CR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(5);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
						cell1.setCellValue(record.getReportDate()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ROW 10 DOWNLAOD
					row = sheet.getRow(9);

					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CR EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_CR_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_CRResubExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_CRResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CR_Resub_Summary_Entity> dataList = getByDateAndVersion(TBL_RESUB_SUMMARY, dateformat.parse(todate),
				version, M_CR_Resub_Summary_Entity.class);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CR report. Returning empty result.");
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

					M_CR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(5);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
						cell1.setCellValue(record.getReportDate()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ROW 10 DOWNLAOD
					row = sheet.getRow(9);

					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CR RESUB SUMMARY", null,
						"BRRS_M_CR_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Resub Email Excel
	public byte[] BRRS_M_CRResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CR_Resub_Summary_Entity> dataList = getByDateAndVersion(TBL_RESUB_SUMMARY, dateformat.parse(todate),
				version, M_CR_Resub_Summary_Entity.class);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CR report. Returning empty result.");
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
					M_CR_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(5);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
						cell1.setCellValue(record.getReportDate()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ROW 10 DOWNLAOD
					row = sheet.getRow(9);

					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CR EMAIL RESUB SUMMARY", null,
						"BRRS_M_CR_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// M_CR entity classes 

	public static class M_CR_Summary_Entity {

		private String R10_PRODUCT;
		private BigDecimal R10_TOTAL_LONG_POS;
		private BigDecimal R10_TOTAL_SHORT_POS;
		private BigDecimal R10_GROSS_OPEN_POS;
		private BigDecimal R10_CHARGE_BASIS_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R10_NET_OPEN_POS;
		private BigDecimal R10_CHARGE_DIR_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R10_TOTAL_CAPITAL_CHARGE;
		private String R11_PRODUCT;
		private BigDecimal R11_TOTAL_LONG_POS;
		private BigDecimal R11_TOTAL_SHORT_POS;
		private BigDecimal R11_GROSS_OPEN_POS;
		private BigDecimal R11_CHARGE_BASIS_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R11_NET_OPEN_POS;
		private BigDecimal R11_CHARGE_DIR_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R11_TOTAL_CAPITAL_CHARGE;
		private String R12_PRODUCT;
		private BigDecimal R12_TOTAL_LONG_POS;
		private BigDecimal R12_TOTAL_SHORT_POS;
		private BigDecimal R12_GROSS_OPEN_POS;
		private BigDecimal R12_CHARGE_BASIS_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R12_NET_OPEN_POS;
		private BigDecimal R12_CHARGE_DIR_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R12_TOTAL_CAPITAL_CHARGE;
		private String R13_PRODUCT;
		private BigDecimal R13_TOTAL_LONG_POS;
		private BigDecimal R13_TOTAL_SHORT_POS;
		private BigDecimal R13_GROSS_OPEN_POS;
		private BigDecimal R13_CHARGE_BASIS_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R13_NET_OPEN_POS;
		private BigDecimal R13_CHARGE_DIR_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R13_TOTAL_CAPITAL_CHARGE;
		private String R14_PRODUCT;
		private BigDecimal R14_TOTAL_LONG_POS;
		private BigDecimal R14_TOTAL_SHORT_POS;
		private BigDecimal R14_GROSS_OPEN_POS;
		private BigDecimal R14_CHARGE_BASIS_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R14_NET_OPEN_POS;
		private BigDecimal R14_CHARGE_DIR_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R14_TOTAL_CAPITAL_CHARGE;
		private String R15_PRODUCT;
		private BigDecimal R15_TOTAL_LONG_POS;
		private BigDecimal R15_TOTAL_SHORT_POS;
		private BigDecimal R15_GROSS_OPEN_POS;
		private BigDecimal R15_CHARGE_BASIS_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R15_NET_OPEN_POS;
		private BigDecimal R15_CHARGE_DIR_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R15_TOTAL_CAPITAL_CHARGE;
		private String R16_PRODUCT;
		private BigDecimal R16_TOTAL_LONG_POS;
		private BigDecimal R16_TOTAL_SHORT_POS;
		private BigDecimal R16_GROSS_OPEN_POS;
		private BigDecimal R16_CHARGE_BASIS_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R16_NET_OPEN_POS;
		private BigDecimal R16_CHARGE_DIR_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R16_TOTAL_CAPITAL_CHARGE;
		private String R17_PRODUCT;
		private BigDecimal R17_TOTAL_LONG_POS;
		private BigDecimal R17_TOTAL_SHORT_POS;
		private BigDecimal R17_GROSS_OPEN_POS;
		private BigDecimal R17_CHARGE_BASIS_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R17_NET_OPEN_POS;
		private BigDecimal R17_CHARGE_DIR_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R17_TOTAL_CAPITAL_CHARGE;

		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;
		private BigDecimal REPORT_VERSION;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_TOTAL_LONG_POS() {
			return R10_TOTAL_LONG_POS;
		}

		public void setR10_TOTAL_LONG_POS(BigDecimal r10_TOTAL_LONG_POS) {
			R10_TOTAL_LONG_POS = r10_TOTAL_LONG_POS;
		}

		public BigDecimal getR10_TOTAL_SHORT_POS() {
			return R10_TOTAL_SHORT_POS;
		}

		public void setR10_TOTAL_SHORT_POS(BigDecimal r10_TOTAL_SHORT_POS) {
			R10_TOTAL_SHORT_POS = r10_TOTAL_SHORT_POS;
		}

		public BigDecimal getR10_GROSS_OPEN_POS() {
			return R10_GROSS_OPEN_POS;
		}

		public void setR10_GROSS_OPEN_POS(BigDecimal r10_GROSS_OPEN_POS) {
			R10_GROSS_OPEN_POS = r10_GROSS_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_BASIS_RISK() {
			return R10_CHARGE_BASIS_RISK;
		}

		public void setR10_CHARGE_BASIS_RISK(BigDecimal r10_CHARGE_BASIS_RISK) {
			R10_CHARGE_BASIS_RISK = r10_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_BASIS_RISK() {
			return R10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR10_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r10_CAPITAL_CHARGE_BASIS_RISK) {
			R10_CAPITAL_CHARGE_BASIS_RISK = r10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_NET_OPEN_POS() {
			return R10_NET_OPEN_POS;
		}

		public void setR10_NET_OPEN_POS(BigDecimal r10_NET_OPEN_POS) {
			R10_NET_OPEN_POS = r10_NET_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_DIR_RISK() {
			return R10_CHARGE_DIR_RISK;
		}

		public void setR10_CHARGE_DIR_RISK(BigDecimal r10_CHARGE_DIR_RISK) {
			R10_CHARGE_DIR_RISK = r10_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_DIR_RISK() {
			return R10_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR10_CAPITAL_CHARGE_DIR_RISK(BigDecimal r10_CAPITAL_CHARGE_DIR_RISK) {
			R10_CAPITAL_CHARGE_DIR_RISK = r10_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_TOTAL_CAPITAL_CHARGE() {
			return R10_TOTAL_CAPITAL_CHARGE;
		}

		public void setR10_TOTAL_CAPITAL_CHARGE(BigDecimal r10_TOTAL_CAPITAL_CHARGE) {
			R10_TOTAL_CAPITAL_CHARGE = r10_TOTAL_CAPITAL_CHARGE;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TOTAL_LONG_POS() {
			return R11_TOTAL_LONG_POS;
		}

		public void setR11_TOTAL_LONG_POS(BigDecimal r11_TOTAL_LONG_POS) {
			R11_TOTAL_LONG_POS = r11_TOTAL_LONG_POS;
		}

		public BigDecimal getR11_TOTAL_SHORT_POS() {
			return R11_TOTAL_SHORT_POS;
		}

		public void setR11_TOTAL_SHORT_POS(BigDecimal r11_TOTAL_SHORT_POS) {
			R11_TOTAL_SHORT_POS = r11_TOTAL_SHORT_POS;
		}

		public BigDecimal getR11_GROSS_OPEN_POS() {
			return R11_GROSS_OPEN_POS;
		}

		public void setR11_GROSS_OPEN_POS(BigDecimal r11_GROSS_OPEN_POS) {
			R11_GROSS_OPEN_POS = r11_GROSS_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_BASIS_RISK() {
			return R11_CHARGE_BASIS_RISK;
		}

		public void setR11_CHARGE_BASIS_RISK(BigDecimal r11_CHARGE_BASIS_RISK) {
			R11_CHARGE_BASIS_RISK = r11_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_BASIS_RISK() {
			return R11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR11_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r11_CAPITAL_CHARGE_BASIS_RISK) {
			R11_CAPITAL_CHARGE_BASIS_RISK = r11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_NET_OPEN_POS() {
			return R11_NET_OPEN_POS;
		}

		public void setR11_NET_OPEN_POS(BigDecimal r11_NET_OPEN_POS) {
			R11_NET_OPEN_POS = r11_NET_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_DIR_RISK() {
			return R11_CHARGE_DIR_RISK;
		}

		public void setR11_CHARGE_DIR_RISK(BigDecimal r11_CHARGE_DIR_RISK) {
			R11_CHARGE_DIR_RISK = r11_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_DIR_RISK() {
			return R11_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR11_CAPITAL_CHARGE_DIR_RISK(BigDecimal r11_CAPITAL_CHARGE_DIR_RISK) {
			R11_CAPITAL_CHARGE_DIR_RISK = r11_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_TOTAL_CAPITAL_CHARGE() {
			return R11_TOTAL_CAPITAL_CHARGE;
		}

		public void setR11_TOTAL_CAPITAL_CHARGE(BigDecimal r11_TOTAL_CAPITAL_CHARGE) {
			R11_TOTAL_CAPITAL_CHARGE = r11_TOTAL_CAPITAL_CHARGE;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TOTAL_LONG_POS() {
			return R12_TOTAL_LONG_POS;
		}

		public void setR12_TOTAL_LONG_POS(BigDecimal r12_TOTAL_LONG_POS) {
			R12_TOTAL_LONG_POS = r12_TOTAL_LONG_POS;
		}

		public BigDecimal getR12_TOTAL_SHORT_POS() {
			return R12_TOTAL_SHORT_POS;
		}

		public void setR12_TOTAL_SHORT_POS(BigDecimal r12_TOTAL_SHORT_POS) {
			R12_TOTAL_SHORT_POS = r12_TOTAL_SHORT_POS;
		}

		public BigDecimal getR12_GROSS_OPEN_POS() {
			return R12_GROSS_OPEN_POS;
		}

		public void setR12_GROSS_OPEN_POS(BigDecimal r12_GROSS_OPEN_POS) {
			R12_GROSS_OPEN_POS = r12_GROSS_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_BASIS_RISK() {
			return R12_CHARGE_BASIS_RISK;
		}

		public void setR12_CHARGE_BASIS_RISK(BigDecimal r12_CHARGE_BASIS_RISK) {
			R12_CHARGE_BASIS_RISK = r12_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_BASIS_RISK() {
			return R12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR12_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r12_CAPITAL_CHARGE_BASIS_RISK) {
			R12_CAPITAL_CHARGE_BASIS_RISK = r12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_NET_OPEN_POS() {
			return R12_NET_OPEN_POS;
		}

		public void setR12_NET_OPEN_POS(BigDecimal r12_NET_OPEN_POS) {
			R12_NET_OPEN_POS = r12_NET_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_DIR_RISK() {
			return R12_CHARGE_DIR_RISK;
		}

		public void setR12_CHARGE_DIR_RISK(BigDecimal r12_CHARGE_DIR_RISK) {
			R12_CHARGE_DIR_RISK = r12_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_DIR_RISK() {
			return R12_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR12_CAPITAL_CHARGE_DIR_RISK(BigDecimal r12_CAPITAL_CHARGE_DIR_RISK) {
			R12_CAPITAL_CHARGE_DIR_RISK = r12_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_TOTAL_CAPITAL_CHARGE() {
			return R12_TOTAL_CAPITAL_CHARGE;
		}

		public void setR12_TOTAL_CAPITAL_CHARGE(BigDecimal r12_TOTAL_CAPITAL_CHARGE) {
			R12_TOTAL_CAPITAL_CHARGE = r12_TOTAL_CAPITAL_CHARGE;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TOTAL_LONG_POS() {
			return R13_TOTAL_LONG_POS;
		}

		public void setR13_TOTAL_LONG_POS(BigDecimal r13_TOTAL_LONG_POS) {
			R13_TOTAL_LONG_POS = r13_TOTAL_LONG_POS;
		}

		public BigDecimal getR13_TOTAL_SHORT_POS() {
			return R13_TOTAL_SHORT_POS;
		}

		public void setR13_TOTAL_SHORT_POS(BigDecimal r13_TOTAL_SHORT_POS) {
			R13_TOTAL_SHORT_POS = r13_TOTAL_SHORT_POS;
		}

		public BigDecimal getR13_GROSS_OPEN_POS() {
			return R13_GROSS_OPEN_POS;
		}

		public void setR13_GROSS_OPEN_POS(BigDecimal r13_GROSS_OPEN_POS) {
			R13_GROSS_OPEN_POS = r13_GROSS_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_BASIS_RISK() {
			return R13_CHARGE_BASIS_RISK;
		}

		public void setR13_CHARGE_BASIS_RISK(BigDecimal r13_CHARGE_BASIS_RISK) {
			R13_CHARGE_BASIS_RISK = r13_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_BASIS_RISK() {
			return R13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR13_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r13_CAPITAL_CHARGE_BASIS_RISK) {
			R13_CAPITAL_CHARGE_BASIS_RISK = r13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_NET_OPEN_POS() {
			return R13_NET_OPEN_POS;
		}

		public void setR13_NET_OPEN_POS(BigDecimal r13_NET_OPEN_POS) {
			R13_NET_OPEN_POS = r13_NET_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_DIR_RISK() {
			return R13_CHARGE_DIR_RISK;
		}

		public void setR13_CHARGE_DIR_RISK(BigDecimal r13_CHARGE_DIR_RISK) {
			R13_CHARGE_DIR_RISK = r13_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_DIR_RISK() {
			return R13_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR13_CAPITAL_CHARGE_DIR_RISK(BigDecimal r13_CAPITAL_CHARGE_DIR_RISK) {
			R13_CAPITAL_CHARGE_DIR_RISK = r13_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_TOTAL_CAPITAL_CHARGE() {
			return R13_TOTAL_CAPITAL_CHARGE;
		}

		public void setR13_TOTAL_CAPITAL_CHARGE(BigDecimal r13_TOTAL_CAPITAL_CHARGE) {
			R13_TOTAL_CAPITAL_CHARGE = r13_TOTAL_CAPITAL_CHARGE;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TOTAL_LONG_POS() {
			return R14_TOTAL_LONG_POS;
		}

		public void setR14_TOTAL_LONG_POS(BigDecimal r14_TOTAL_LONG_POS) {
			R14_TOTAL_LONG_POS = r14_TOTAL_LONG_POS;
		}

		public BigDecimal getR14_TOTAL_SHORT_POS() {
			return R14_TOTAL_SHORT_POS;
		}

		public void setR14_TOTAL_SHORT_POS(BigDecimal r14_TOTAL_SHORT_POS) {
			R14_TOTAL_SHORT_POS = r14_TOTAL_SHORT_POS;
		}

		public BigDecimal getR14_GROSS_OPEN_POS() {
			return R14_GROSS_OPEN_POS;
		}

		public void setR14_GROSS_OPEN_POS(BigDecimal r14_GROSS_OPEN_POS) {
			R14_GROSS_OPEN_POS = r14_GROSS_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_BASIS_RISK() {
			return R14_CHARGE_BASIS_RISK;
		}

		public void setR14_CHARGE_BASIS_RISK(BigDecimal r14_CHARGE_BASIS_RISK) {
			R14_CHARGE_BASIS_RISK = r14_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_BASIS_RISK() {
			return R14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR14_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r14_CAPITAL_CHARGE_BASIS_RISK) {
			R14_CAPITAL_CHARGE_BASIS_RISK = r14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_NET_OPEN_POS() {
			return R14_NET_OPEN_POS;
		}

		public void setR14_NET_OPEN_POS(BigDecimal r14_NET_OPEN_POS) {
			R14_NET_OPEN_POS = r14_NET_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_DIR_RISK() {
			return R14_CHARGE_DIR_RISK;
		}

		public void setR14_CHARGE_DIR_RISK(BigDecimal r14_CHARGE_DIR_RISK) {
			R14_CHARGE_DIR_RISK = r14_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_DIR_RISK() {
			return R14_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR14_CAPITAL_CHARGE_DIR_RISK(BigDecimal r14_CAPITAL_CHARGE_DIR_RISK) {
			R14_CAPITAL_CHARGE_DIR_RISK = r14_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_TOTAL_CAPITAL_CHARGE() {
			return R14_TOTAL_CAPITAL_CHARGE;
		}

		public void setR14_TOTAL_CAPITAL_CHARGE(BigDecimal r14_TOTAL_CAPITAL_CHARGE) {
			R14_TOTAL_CAPITAL_CHARGE = r14_TOTAL_CAPITAL_CHARGE;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TOTAL_LONG_POS() {
			return R15_TOTAL_LONG_POS;
		}

		public void setR15_TOTAL_LONG_POS(BigDecimal r15_TOTAL_LONG_POS) {
			R15_TOTAL_LONG_POS = r15_TOTAL_LONG_POS;
		}

		public BigDecimal getR15_TOTAL_SHORT_POS() {
			return R15_TOTAL_SHORT_POS;
		}

		public void setR15_TOTAL_SHORT_POS(BigDecimal r15_TOTAL_SHORT_POS) {
			R15_TOTAL_SHORT_POS = r15_TOTAL_SHORT_POS;
		}

		public BigDecimal getR15_GROSS_OPEN_POS() {
			return R15_GROSS_OPEN_POS;
		}

		public void setR15_GROSS_OPEN_POS(BigDecimal r15_GROSS_OPEN_POS) {
			R15_GROSS_OPEN_POS = r15_GROSS_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_BASIS_RISK() {
			return R15_CHARGE_BASIS_RISK;
		}

		public void setR15_CHARGE_BASIS_RISK(BigDecimal r15_CHARGE_BASIS_RISK) {
			R15_CHARGE_BASIS_RISK = r15_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_BASIS_RISK() {
			return R15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR15_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r15_CAPITAL_CHARGE_BASIS_RISK) {
			R15_CAPITAL_CHARGE_BASIS_RISK = r15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_NET_OPEN_POS() {
			return R15_NET_OPEN_POS;
		}

		public void setR15_NET_OPEN_POS(BigDecimal r15_NET_OPEN_POS) {
			R15_NET_OPEN_POS = r15_NET_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_DIR_RISK() {
			return R15_CHARGE_DIR_RISK;
		}

		public void setR15_CHARGE_DIR_RISK(BigDecimal r15_CHARGE_DIR_RISK) {
			R15_CHARGE_DIR_RISK = r15_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_DIR_RISK() {
			return R15_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR15_CAPITAL_CHARGE_DIR_RISK(BigDecimal r15_CAPITAL_CHARGE_DIR_RISK) {
			R15_CAPITAL_CHARGE_DIR_RISK = r15_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_TOTAL_CAPITAL_CHARGE() {
			return R15_TOTAL_CAPITAL_CHARGE;
		}

		public void setR15_TOTAL_CAPITAL_CHARGE(BigDecimal r15_TOTAL_CAPITAL_CHARGE) {
			R15_TOTAL_CAPITAL_CHARGE = r15_TOTAL_CAPITAL_CHARGE;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TOTAL_LONG_POS() {
			return R16_TOTAL_LONG_POS;
		}

		public void setR16_TOTAL_LONG_POS(BigDecimal r16_TOTAL_LONG_POS) {
			R16_TOTAL_LONG_POS = r16_TOTAL_LONG_POS;
		}

		public BigDecimal getR16_TOTAL_SHORT_POS() {
			return R16_TOTAL_SHORT_POS;
		}

		public void setR16_TOTAL_SHORT_POS(BigDecimal r16_TOTAL_SHORT_POS) {
			R16_TOTAL_SHORT_POS = r16_TOTAL_SHORT_POS;
		}

		public BigDecimal getR16_GROSS_OPEN_POS() {
			return R16_GROSS_OPEN_POS;
		}

		public void setR16_GROSS_OPEN_POS(BigDecimal r16_GROSS_OPEN_POS) {
			R16_GROSS_OPEN_POS = r16_GROSS_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_BASIS_RISK() {
			return R16_CHARGE_BASIS_RISK;
		}

		public void setR16_CHARGE_BASIS_RISK(BigDecimal r16_CHARGE_BASIS_RISK) {
			R16_CHARGE_BASIS_RISK = r16_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_BASIS_RISK() {
			return R16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR16_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r16_CAPITAL_CHARGE_BASIS_RISK) {
			R16_CAPITAL_CHARGE_BASIS_RISK = r16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_NET_OPEN_POS() {
			return R16_NET_OPEN_POS;
		}

		public void setR16_NET_OPEN_POS(BigDecimal r16_NET_OPEN_POS) {
			R16_NET_OPEN_POS = r16_NET_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_DIR_RISK() {
			return R16_CHARGE_DIR_RISK;
		}

		public void setR16_CHARGE_DIR_RISK(BigDecimal r16_CHARGE_DIR_RISK) {
			R16_CHARGE_DIR_RISK = r16_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_DIR_RISK() {
			return R16_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR16_CAPITAL_CHARGE_DIR_RISK(BigDecimal r16_CAPITAL_CHARGE_DIR_RISK) {
			R16_CAPITAL_CHARGE_DIR_RISK = r16_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_TOTAL_CAPITAL_CHARGE() {
			return R16_TOTAL_CAPITAL_CHARGE;
		}

		public void setR16_TOTAL_CAPITAL_CHARGE(BigDecimal r16_TOTAL_CAPITAL_CHARGE) {
			R16_TOTAL_CAPITAL_CHARGE = r16_TOTAL_CAPITAL_CHARGE;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TOTAL_LONG_POS() {
			return R17_TOTAL_LONG_POS;
		}

		public void setR17_TOTAL_LONG_POS(BigDecimal r17_TOTAL_LONG_POS) {
			R17_TOTAL_LONG_POS = r17_TOTAL_LONG_POS;
		}

		public BigDecimal getR17_TOTAL_SHORT_POS() {
			return R17_TOTAL_SHORT_POS;
		}

		public void setR17_TOTAL_SHORT_POS(BigDecimal r17_TOTAL_SHORT_POS) {
			R17_TOTAL_SHORT_POS = r17_TOTAL_SHORT_POS;
		}

		public BigDecimal getR17_GROSS_OPEN_POS() {
			return R17_GROSS_OPEN_POS;
		}

		public void setR17_GROSS_OPEN_POS(BigDecimal r17_GROSS_OPEN_POS) {
			R17_GROSS_OPEN_POS = r17_GROSS_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_BASIS_RISK() {
			return R17_CHARGE_BASIS_RISK;
		}

		public void setR17_CHARGE_BASIS_RISK(BigDecimal r17_CHARGE_BASIS_RISK) {
			R17_CHARGE_BASIS_RISK = r17_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_BASIS_RISK() {
			return R17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR17_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r17_CAPITAL_CHARGE_BASIS_RISK) {
			R17_CAPITAL_CHARGE_BASIS_RISK = r17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_NET_OPEN_POS() {
			return R17_NET_OPEN_POS;
		}

		public void setR17_NET_OPEN_POS(BigDecimal r17_NET_OPEN_POS) {
			R17_NET_OPEN_POS = r17_NET_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_DIR_RISK() {
			return R17_CHARGE_DIR_RISK;
		}

		public void setR17_CHARGE_DIR_RISK(BigDecimal r17_CHARGE_DIR_RISK) {
			R17_CHARGE_DIR_RISK = r17_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_DIR_RISK() {
			return R17_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR17_CAPITAL_CHARGE_DIR_RISK(BigDecimal r17_CAPITAL_CHARGE_DIR_RISK) {
			R17_CAPITAL_CHARGE_DIR_RISK = r17_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_TOTAL_CAPITAL_CHARGE() {
			return R17_TOTAL_CAPITAL_CHARGE;
		}

		public void setR17_TOTAL_CAPITAL_CHARGE(BigDecimal r17_TOTAL_CAPITAL_CHARGE) {
			R17_TOTAL_CAPITAL_CHARGE = r17_TOTAL_CAPITAL_CHARGE;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
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

		public M_CR_Summary_Entity() {
			super();
		}

	}

	public static class M_CR_Detail_Entity {

		private String R10_PRODUCT;
		private BigDecimal R10_TOTAL_LONG_POS;
		private BigDecimal R10_TOTAL_SHORT_POS;
		private BigDecimal R10_GROSS_OPEN_POS;
		private BigDecimal R10_CHARGE_BASIS_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R10_NET_OPEN_POS;
		private BigDecimal R10_CHARGE_DIR_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R10_TOTAL_CAPITAL_CHARGE;
		private String R11_PRODUCT;
		private BigDecimal R11_TOTAL_LONG_POS;
		private BigDecimal R11_TOTAL_SHORT_POS;
		private BigDecimal R11_GROSS_OPEN_POS;
		private BigDecimal R11_CHARGE_BASIS_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R11_NET_OPEN_POS;
		private BigDecimal R11_CHARGE_DIR_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R11_TOTAL_CAPITAL_CHARGE;
		private String R12_PRODUCT;
		private BigDecimal R12_TOTAL_LONG_POS;
		private BigDecimal R12_TOTAL_SHORT_POS;
		private BigDecimal R12_GROSS_OPEN_POS;
		private BigDecimal R12_CHARGE_BASIS_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R12_NET_OPEN_POS;
		private BigDecimal R12_CHARGE_DIR_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R12_TOTAL_CAPITAL_CHARGE;
		private String R13_PRODUCT;
		private BigDecimal R13_TOTAL_LONG_POS;
		private BigDecimal R13_TOTAL_SHORT_POS;
		private BigDecimal R13_GROSS_OPEN_POS;
		private BigDecimal R13_CHARGE_BASIS_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R13_NET_OPEN_POS;
		private BigDecimal R13_CHARGE_DIR_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R13_TOTAL_CAPITAL_CHARGE;
		private String R14_PRODUCT;
		private BigDecimal R14_TOTAL_LONG_POS;
		private BigDecimal R14_TOTAL_SHORT_POS;
		private BigDecimal R14_GROSS_OPEN_POS;
		private BigDecimal R14_CHARGE_BASIS_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R14_NET_OPEN_POS;
		private BigDecimal R14_CHARGE_DIR_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R14_TOTAL_CAPITAL_CHARGE;
		private String R15_PRODUCT;
		private BigDecimal R15_TOTAL_LONG_POS;
		private BigDecimal R15_TOTAL_SHORT_POS;
		private BigDecimal R15_GROSS_OPEN_POS;
		private BigDecimal R15_CHARGE_BASIS_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R15_NET_OPEN_POS;
		private BigDecimal R15_CHARGE_DIR_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R15_TOTAL_CAPITAL_CHARGE;
		private String R16_PRODUCT;
		private BigDecimal R16_TOTAL_LONG_POS;
		private BigDecimal R16_TOTAL_SHORT_POS;
		private BigDecimal R16_GROSS_OPEN_POS;
		private BigDecimal R16_CHARGE_BASIS_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R16_NET_OPEN_POS;
		private BigDecimal R16_CHARGE_DIR_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R16_TOTAL_CAPITAL_CHARGE;
		private String R17_PRODUCT;
		private BigDecimal R17_TOTAL_LONG_POS;
		private BigDecimal R17_TOTAL_SHORT_POS;
		private BigDecimal R17_GROSS_OPEN_POS;
		private BigDecimal R17_CHARGE_BASIS_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R17_NET_OPEN_POS;
		private BigDecimal R17_CHARGE_DIR_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R17_TOTAL_CAPITAL_CHARGE;

		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;
		private BigDecimal REPORT_VERSION;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_TOTAL_LONG_POS() {
			return R10_TOTAL_LONG_POS;
		}

		public void setR10_TOTAL_LONG_POS(BigDecimal r10_TOTAL_LONG_POS) {
			R10_TOTAL_LONG_POS = r10_TOTAL_LONG_POS;
		}

		public BigDecimal getR10_TOTAL_SHORT_POS() {
			return R10_TOTAL_SHORT_POS;
		}

		public void setR10_TOTAL_SHORT_POS(BigDecimal r10_TOTAL_SHORT_POS) {
			R10_TOTAL_SHORT_POS = r10_TOTAL_SHORT_POS;
		}

		public BigDecimal getR10_GROSS_OPEN_POS() {
			return R10_GROSS_OPEN_POS;
		}

		public void setR10_GROSS_OPEN_POS(BigDecimal r10_GROSS_OPEN_POS) {
			R10_GROSS_OPEN_POS = r10_GROSS_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_BASIS_RISK() {
			return R10_CHARGE_BASIS_RISK;
		}

		public void setR10_CHARGE_BASIS_RISK(BigDecimal r10_CHARGE_BASIS_RISK) {
			R10_CHARGE_BASIS_RISK = r10_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_BASIS_RISK() {
			return R10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR10_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r10_CAPITAL_CHARGE_BASIS_RISK) {
			R10_CAPITAL_CHARGE_BASIS_RISK = r10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_NET_OPEN_POS() {
			return R10_NET_OPEN_POS;
		}

		public void setR10_NET_OPEN_POS(BigDecimal r10_NET_OPEN_POS) {
			R10_NET_OPEN_POS = r10_NET_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_DIR_RISK() {
			return R10_CHARGE_DIR_RISK;
		}

		public void setR10_CHARGE_DIR_RISK(BigDecimal r10_CHARGE_DIR_RISK) {
			R10_CHARGE_DIR_RISK = r10_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_DIR_RISK() {
			return R10_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR10_CAPITAL_CHARGE_DIR_RISK(BigDecimal r10_CAPITAL_CHARGE_DIR_RISK) {
			R10_CAPITAL_CHARGE_DIR_RISK = r10_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_TOTAL_CAPITAL_CHARGE() {
			return R10_TOTAL_CAPITAL_CHARGE;
		}

		public void setR10_TOTAL_CAPITAL_CHARGE(BigDecimal r10_TOTAL_CAPITAL_CHARGE) {
			R10_TOTAL_CAPITAL_CHARGE = r10_TOTAL_CAPITAL_CHARGE;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TOTAL_LONG_POS() {
			return R11_TOTAL_LONG_POS;
		}

		public void setR11_TOTAL_LONG_POS(BigDecimal r11_TOTAL_LONG_POS) {
			R11_TOTAL_LONG_POS = r11_TOTAL_LONG_POS;
		}

		public BigDecimal getR11_TOTAL_SHORT_POS() {
			return R11_TOTAL_SHORT_POS;
		}

		public void setR11_TOTAL_SHORT_POS(BigDecimal r11_TOTAL_SHORT_POS) {
			R11_TOTAL_SHORT_POS = r11_TOTAL_SHORT_POS;
		}

		public BigDecimal getR11_GROSS_OPEN_POS() {
			return R11_GROSS_OPEN_POS;
		}

		public void setR11_GROSS_OPEN_POS(BigDecimal r11_GROSS_OPEN_POS) {
			R11_GROSS_OPEN_POS = r11_GROSS_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_BASIS_RISK() {
			return R11_CHARGE_BASIS_RISK;
		}

		public void setR11_CHARGE_BASIS_RISK(BigDecimal r11_CHARGE_BASIS_RISK) {
			R11_CHARGE_BASIS_RISK = r11_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_BASIS_RISK() {
			return R11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR11_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r11_CAPITAL_CHARGE_BASIS_RISK) {
			R11_CAPITAL_CHARGE_BASIS_RISK = r11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_NET_OPEN_POS() {
			return R11_NET_OPEN_POS;
		}

		public void setR11_NET_OPEN_POS(BigDecimal r11_NET_OPEN_POS) {
			R11_NET_OPEN_POS = r11_NET_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_DIR_RISK() {
			return R11_CHARGE_DIR_RISK;
		}

		public void setR11_CHARGE_DIR_RISK(BigDecimal r11_CHARGE_DIR_RISK) {
			R11_CHARGE_DIR_RISK = r11_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_DIR_RISK() {
			return R11_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR11_CAPITAL_CHARGE_DIR_RISK(BigDecimal r11_CAPITAL_CHARGE_DIR_RISK) {
			R11_CAPITAL_CHARGE_DIR_RISK = r11_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_TOTAL_CAPITAL_CHARGE() {
			return R11_TOTAL_CAPITAL_CHARGE;
		}

		public void setR11_TOTAL_CAPITAL_CHARGE(BigDecimal r11_TOTAL_CAPITAL_CHARGE) {
			R11_TOTAL_CAPITAL_CHARGE = r11_TOTAL_CAPITAL_CHARGE;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TOTAL_LONG_POS() {
			return R12_TOTAL_LONG_POS;
		}

		public void setR12_TOTAL_LONG_POS(BigDecimal r12_TOTAL_LONG_POS) {
			R12_TOTAL_LONG_POS = r12_TOTAL_LONG_POS;
		}

		public BigDecimal getR12_TOTAL_SHORT_POS() {
			return R12_TOTAL_SHORT_POS;
		}

		public void setR12_TOTAL_SHORT_POS(BigDecimal r12_TOTAL_SHORT_POS) {
			R12_TOTAL_SHORT_POS = r12_TOTAL_SHORT_POS;
		}

		public BigDecimal getR12_GROSS_OPEN_POS() {
			return R12_GROSS_OPEN_POS;
		}

		public void setR12_GROSS_OPEN_POS(BigDecimal r12_GROSS_OPEN_POS) {
			R12_GROSS_OPEN_POS = r12_GROSS_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_BASIS_RISK() {
			return R12_CHARGE_BASIS_RISK;
		}

		public void setR12_CHARGE_BASIS_RISK(BigDecimal r12_CHARGE_BASIS_RISK) {
			R12_CHARGE_BASIS_RISK = r12_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_BASIS_RISK() {
			return R12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR12_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r12_CAPITAL_CHARGE_BASIS_RISK) {
			R12_CAPITAL_CHARGE_BASIS_RISK = r12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_NET_OPEN_POS() {
			return R12_NET_OPEN_POS;
		}

		public void setR12_NET_OPEN_POS(BigDecimal r12_NET_OPEN_POS) {
			R12_NET_OPEN_POS = r12_NET_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_DIR_RISK() {
			return R12_CHARGE_DIR_RISK;
		}

		public void setR12_CHARGE_DIR_RISK(BigDecimal r12_CHARGE_DIR_RISK) {
			R12_CHARGE_DIR_RISK = r12_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_DIR_RISK() {
			return R12_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR12_CAPITAL_CHARGE_DIR_RISK(BigDecimal r12_CAPITAL_CHARGE_DIR_RISK) {
			R12_CAPITAL_CHARGE_DIR_RISK = r12_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_TOTAL_CAPITAL_CHARGE() {
			return R12_TOTAL_CAPITAL_CHARGE;
		}

		public void setR12_TOTAL_CAPITAL_CHARGE(BigDecimal r12_TOTAL_CAPITAL_CHARGE) {
			R12_TOTAL_CAPITAL_CHARGE = r12_TOTAL_CAPITAL_CHARGE;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TOTAL_LONG_POS() {
			return R13_TOTAL_LONG_POS;
		}

		public void setR13_TOTAL_LONG_POS(BigDecimal r13_TOTAL_LONG_POS) {
			R13_TOTAL_LONG_POS = r13_TOTAL_LONG_POS;
		}

		public BigDecimal getR13_TOTAL_SHORT_POS() {
			return R13_TOTAL_SHORT_POS;
		}

		public void setR13_TOTAL_SHORT_POS(BigDecimal r13_TOTAL_SHORT_POS) {
			R13_TOTAL_SHORT_POS = r13_TOTAL_SHORT_POS;
		}

		public BigDecimal getR13_GROSS_OPEN_POS() {
			return R13_GROSS_OPEN_POS;
		}

		public void setR13_GROSS_OPEN_POS(BigDecimal r13_GROSS_OPEN_POS) {
			R13_GROSS_OPEN_POS = r13_GROSS_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_BASIS_RISK() {
			return R13_CHARGE_BASIS_RISK;
		}

		public void setR13_CHARGE_BASIS_RISK(BigDecimal r13_CHARGE_BASIS_RISK) {
			R13_CHARGE_BASIS_RISK = r13_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_BASIS_RISK() {
			return R13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR13_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r13_CAPITAL_CHARGE_BASIS_RISK) {
			R13_CAPITAL_CHARGE_BASIS_RISK = r13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_NET_OPEN_POS() {
			return R13_NET_OPEN_POS;
		}

		public void setR13_NET_OPEN_POS(BigDecimal r13_NET_OPEN_POS) {
			R13_NET_OPEN_POS = r13_NET_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_DIR_RISK() {
			return R13_CHARGE_DIR_RISK;
		}

		public void setR13_CHARGE_DIR_RISK(BigDecimal r13_CHARGE_DIR_RISK) {
			R13_CHARGE_DIR_RISK = r13_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_DIR_RISK() {
			return R13_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR13_CAPITAL_CHARGE_DIR_RISK(BigDecimal r13_CAPITAL_CHARGE_DIR_RISK) {
			R13_CAPITAL_CHARGE_DIR_RISK = r13_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_TOTAL_CAPITAL_CHARGE() {
			return R13_TOTAL_CAPITAL_CHARGE;
		}

		public void setR13_TOTAL_CAPITAL_CHARGE(BigDecimal r13_TOTAL_CAPITAL_CHARGE) {
			R13_TOTAL_CAPITAL_CHARGE = r13_TOTAL_CAPITAL_CHARGE;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TOTAL_LONG_POS() {
			return R14_TOTAL_LONG_POS;
		}

		public void setR14_TOTAL_LONG_POS(BigDecimal r14_TOTAL_LONG_POS) {
			R14_TOTAL_LONG_POS = r14_TOTAL_LONG_POS;
		}

		public BigDecimal getR14_TOTAL_SHORT_POS() {
			return R14_TOTAL_SHORT_POS;
		}

		public void setR14_TOTAL_SHORT_POS(BigDecimal r14_TOTAL_SHORT_POS) {
			R14_TOTAL_SHORT_POS = r14_TOTAL_SHORT_POS;
		}

		public BigDecimal getR14_GROSS_OPEN_POS() {
			return R14_GROSS_OPEN_POS;
		}

		public void setR14_GROSS_OPEN_POS(BigDecimal r14_GROSS_OPEN_POS) {
			R14_GROSS_OPEN_POS = r14_GROSS_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_BASIS_RISK() {
			return R14_CHARGE_BASIS_RISK;
		}

		public void setR14_CHARGE_BASIS_RISK(BigDecimal r14_CHARGE_BASIS_RISK) {
			R14_CHARGE_BASIS_RISK = r14_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_BASIS_RISK() {
			return R14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR14_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r14_CAPITAL_CHARGE_BASIS_RISK) {
			R14_CAPITAL_CHARGE_BASIS_RISK = r14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_NET_OPEN_POS() {
			return R14_NET_OPEN_POS;
		}

		public void setR14_NET_OPEN_POS(BigDecimal r14_NET_OPEN_POS) {
			R14_NET_OPEN_POS = r14_NET_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_DIR_RISK() {
			return R14_CHARGE_DIR_RISK;
		}

		public void setR14_CHARGE_DIR_RISK(BigDecimal r14_CHARGE_DIR_RISK) {
			R14_CHARGE_DIR_RISK = r14_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_DIR_RISK() {
			return R14_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR14_CAPITAL_CHARGE_DIR_RISK(BigDecimal r14_CAPITAL_CHARGE_DIR_RISK) {
			R14_CAPITAL_CHARGE_DIR_RISK = r14_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_TOTAL_CAPITAL_CHARGE() {
			return R14_TOTAL_CAPITAL_CHARGE;
		}

		public void setR14_TOTAL_CAPITAL_CHARGE(BigDecimal r14_TOTAL_CAPITAL_CHARGE) {
			R14_TOTAL_CAPITAL_CHARGE = r14_TOTAL_CAPITAL_CHARGE;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TOTAL_LONG_POS() {
			return R15_TOTAL_LONG_POS;
		}

		public void setR15_TOTAL_LONG_POS(BigDecimal r15_TOTAL_LONG_POS) {
			R15_TOTAL_LONG_POS = r15_TOTAL_LONG_POS;
		}

		public BigDecimal getR15_TOTAL_SHORT_POS() {
			return R15_TOTAL_SHORT_POS;
		}

		public void setR15_TOTAL_SHORT_POS(BigDecimal r15_TOTAL_SHORT_POS) {
			R15_TOTAL_SHORT_POS = r15_TOTAL_SHORT_POS;
		}

		public BigDecimal getR15_GROSS_OPEN_POS() {
			return R15_GROSS_OPEN_POS;
		}

		public void setR15_GROSS_OPEN_POS(BigDecimal r15_GROSS_OPEN_POS) {
			R15_GROSS_OPEN_POS = r15_GROSS_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_BASIS_RISK() {
			return R15_CHARGE_BASIS_RISK;
		}

		public void setR15_CHARGE_BASIS_RISK(BigDecimal r15_CHARGE_BASIS_RISK) {
			R15_CHARGE_BASIS_RISK = r15_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_BASIS_RISK() {
			return R15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR15_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r15_CAPITAL_CHARGE_BASIS_RISK) {
			R15_CAPITAL_CHARGE_BASIS_RISK = r15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_NET_OPEN_POS() {
			return R15_NET_OPEN_POS;
		}

		public void setR15_NET_OPEN_POS(BigDecimal r15_NET_OPEN_POS) {
			R15_NET_OPEN_POS = r15_NET_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_DIR_RISK() {
			return R15_CHARGE_DIR_RISK;
		}

		public void setR15_CHARGE_DIR_RISK(BigDecimal r15_CHARGE_DIR_RISK) {
			R15_CHARGE_DIR_RISK = r15_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_DIR_RISK() {
			return R15_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR15_CAPITAL_CHARGE_DIR_RISK(BigDecimal r15_CAPITAL_CHARGE_DIR_RISK) {
			R15_CAPITAL_CHARGE_DIR_RISK = r15_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_TOTAL_CAPITAL_CHARGE() {
			return R15_TOTAL_CAPITAL_CHARGE;
		}

		public void setR15_TOTAL_CAPITAL_CHARGE(BigDecimal r15_TOTAL_CAPITAL_CHARGE) {
			R15_TOTAL_CAPITAL_CHARGE = r15_TOTAL_CAPITAL_CHARGE;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TOTAL_LONG_POS() {
			return R16_TOTAL_LONG_POS;
		}

		public void setR16_TOTAL_LONG_POS(BigDecimal r16_TOTAL_LONG_POS) {
			R16_TOTAL_LONG_POS = r16_TOTAL_LONG_POS;
		}

		public BigDecimal getR16_TOTAL_SHORT_POS() {
			return R16_TOTAL_SHORT_POS;
		}

		public void setR16_TOTAL_SHORT_POS(BigDecimal r16_TOTAL_SHORT_POS) {
			R16_TOTAL_SHORT_POS = r16_TOTAL_SHORT_POS;
		}

		public BigDecimal getR16_GROSS_OPEN_POS() {
			return R16_GROSS_OPEN_POS;
		}

		public void setR16_GROSS_OPEN_POS(BigDecimal r16_GROSS_OPEN_POS) {
			R16_GROSS_OPEN_POS = r16_GROSS_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_BASIS_RISK() {
			return R16_CHARGE_BASIS_RISK;
		}

		public void setR16_CHARGE_BASIS_RISK(BigDecimal r16_CHARGE_BASIS_RISK) {
			R16_CHARGE_BASIS_RISK = r16_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_BASIS_RISK() {
			return R16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR16_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r16_CAPITAL_CHARGE_BASIS_RISK) {
			R16_CAPITAL_CHARGE_BASIS_RISK = r16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_NET_OPEN_POS() {
			return R16_NET_OPEN_POS;
		}

		public void setR16_NET_OPEN_POS(BigDecimal r16_NET_OPEN_POS) {
			R16_NET_OPEN_POS = r16_NET_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_DIR_RISK() {
			return R16_CHARGE_DIR_RISK;
		}

		public void setR16_CHARGE_DIR_RISK(BigDecimal r16_CHARGE_DIR_RISK) {
			R16_CHARGE_DIR_RISK = r16_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_DIR_RISK() {
			return R16_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR16_CAPITAL_CHARGE_DIR_RISK(BigDecimal r16_CAPITAL_CHARGE_DIR_RISK) {
			R16_CAPITAL_CHARGE_DIR_RISK = r16_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_TOTAL_CAPITAL_CHARGE() {
			return R16_TOTAL_CAPITAL_CHARGE;
		}

		public void setR16_TOTAL_CAPITAL_CHARGE(BigDecimal r16_TOTAL_CAPITAL_CHARGE) {
			R16_TOTAL_CAPITAL_CHARGE = r16_TOTAL_CAPITAL_CHARGE;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TOTAL_LONG_POS() {
			return R17_TOTAL_LONG_POS;
		}

		public void setR17_TOTAL_LONG_POS(BigDecimal r17_TOTAL_LONG_POS) {
			R17_TOTAL_LONG_POS = r17_TOTAL_LONG_POS;
		}

		public BigDecimal getR17_TOTAL_SHORT_POS() {
			return R17_TOTAL_SHORT_POS;
		}

		public void setR17_TOTAL_SHORT_POS(BigDecimal r17_TOTAL_SHORT_POS) {
			R17_TOTAL_SHORT_POS = r17_TOTAL_SHORT_POS;
		}

		public BigDecimal getR17_GROSS_OPEN_POS() {
			return R17_GROSS_OPEN_POS;
		}

		public void setR17_GROSS_OPEN_POS(BigDecimal r17_GROSS_OPEN_POS) {
			R17_GROSS_OPEN_POS = r17_GROSS_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_BASIS_RISK() {
			return R17_CHARGE_BASIS_RISK;
		}

		public void setR17_CHARGE_BASIS_RISK(BigDecimal r17_CHARGE_BASIS_RISK) {
			R17_CHARGE_BASIS_RISK = r17_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_BASIS_RISK() {
			return R17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR17_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r17_CAPITAL_CHARGE_BASIS_RISK) {
			R17_CAPITAL_CHARGE_BASIS_RISK = r17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_NET_OPEN_POS() {
			return R17_NET_OPEN_POS;
		}

		public void setR17_NET_OPEN_POS(BigDecimal r17_NET_OPEN_POS) {
			R17_NET_OPEN_POS = r17_NET_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_DIR_RISK() {
			return R17_CHARGE_DIR_RISK;
		}

		public void setR17_CHARGE_DIR_RISK(BigDecimal r17_CHARGE_DIR_RISK) {
			R17_CHARGE_DIR_RISK = r17_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_DIR_RISK() {
			return R17_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR17_CAPITAL_CHARGE_DIR_RISK(BigDecimal r17_CAPITAL_CHARGE_DIR_RISK) {
			R17_CAPITAL_CHARGE_DIR_RISK = r17_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_TOTAL_CAPITAL_CHARGE() {
			return R17_TOTAL_CAPITAL_CHARGE;
		}

		public void setR17_TOTAL_CAPITAL_CHARGE(BigDecimal r17_TOTAL_CAPITAL_CHARGE) {
			R17_TOTAL_CAPITAL_CHARGE = r17_TOTAL_CAPITAL_CHARGE;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
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

		public M_CR_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	public static class M_CR_Archival_Summary_Entity {

		private String R10_PRODUCT;
		private BigDecimal R10_TOTAL_LONG_POS;
		private BigDecimal R10_TOTAL_SHORT_POS;
		private BigDecimal R10_GROSS_OPEN_POS;
		private BigDecimal R10_CHARGE_BASIS_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R10_NET_OPEN_POS;
		private BigDecimal R10_CHARGE_DIR_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R10_TOTAL_CAPITAL_CHARGE;
		private String R11_PRODUCT;
		private BigDecimal R11_TOTAL_LONG_POS;
		private BigDecimal R11_TOTAL_SHORT_POS;
		private BigDecimal R11_GROSS_OPEN_POS;
		private BigDecimal R11_CHARGE_BASIS_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R11_NET_OPEN_POS;
		private BigDecimal R11_CHARGE_DIR_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R11_TOTAL_CAPITAL_CHARGE;
		private String R12_PRODUCT;
		private BigDecimal R12_TOTAL_LONG_POS;
		private BigDecimal R12_TOTAL_SHORT_POS;
		private BigDecimal R12_GROSS_OPEN_POS;
		private BigDecimal R12_CHARGE_BASIS_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R12_NET_OPEN_POS;
		private BigDecimal R12_CHARGE_DIR_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R12_TOTAL_CAPITAL_CHARGE;
		private String R13_PRODUCT;
		private BigDecimal R13_TOTAL_LONG_POS;
		private BigDecimal R13_TOTAL_SHORT_POS;
		private BigDecimal R13_GROSS_OPEN_POS;
		private BigDecimal R13_CHARGE_BASIS_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R13_NET_OPEN_POS;
		private BigDecimal R13_CHARGE_DIR_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R13_TOTAL_CAPITAL_CHARGE;
		private String R14_PRODUCT;
		private BigDecimal R14_TOTAL_LONG_POS;
		private BigDecimal R14_TOTAL_SHORT_POS;
		private BigDecimal R14_GROSS_OPEN_POS;
		private BigDecimal R14_CHARGE_BASIS_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R14_NET_OPEN_POS;
		private BigDecimal R14_CHARGE_DIR_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R14_TOTAL_CAPITAL_CHARGE;
		private String R15_PRODUCT;
		private BigDecimal R15_TOTAL_LONG_POS;
		private BigDecimal R15_TOTAL_SHORT_POS;
		private BigDecimal R15_GROSS_OPEN_POS;
		private BigDecimal R15_CHARGE_BASIS_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R15_NET_OPEN_POS;
		private BigDecimal R15_CHARGE_DIR_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R15_TOTAL_CAPITAL_CHARGE;
		private String R16_PRODUCT;
		private BigDecimal R16_TOTAL_LONG_POS;
		private BigDecimal R16_TOTAL_SHORT_POS;
		private BigDecimal R16_GROSS_OPEN_POS;
		private BigDecimal R16_CHARGE_BASIS_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R16_NET_OPEN_POS;
		private BigDecimal R16_CHARGE_DIR_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R16_TOTAL_CAPITAL_CHARGE;
		private String R17_PRODUCT;
		private BigDecimal R17_TOTAL_LONG_POS;
		private BigDecimal R17_TOTAL_SHORT_POS;
		private BigDecimal R17_GROSS_OPEN_POS;
		private BigDecimal R17_CHARGE_BASIS_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R17_NET_OPEN_POS;
		private BigDecimal R17_CHARGE_DIR_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R17_TOTAL_CAPITAL_CHARGE;

		private Date reportDate;

		private BigDecimal reportVersion;

		private Date reportResubDate;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_TOTAL_LONG_POS() {
			return R10_TOTAL_LONG_POS;
		}

		public void setR10_TOTAL_LONG_POS(BigDecimal r10_TOTAL_LONG_POS) {
			R10_TOTAL_LONG_POS = r10_TOTAL_LONG_POS;
		}

		public BigDecimal getR10_TOTAL_SHORT_POS() {
			return R10_TOTAL_SHORT_POS;
		}

		public void setR10_TOTAL_SHORT_POS(BigDecimal r10_TOTAL_SHORT_POS) {
			R10_TOTAL_SHORT_POS = r10_TOTAL_SHORT_POS;
		}

		public BigDecimal getR10_GROSS_OPEN_POS() {
			return R10_GROSS_OPEN_POS;
		}

		public void setR10_GROSS_OPEN_POS(BigDecimal r10_GROSS_OPEN_POS) {
			R10_GROSS_OPEN_POS = r10_GROSS_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_BASIS_RISK() {
			return R10_CHARGE_BASIS_RISK;
		}

		public void setR10_CHARGE_BASIS_RISK(BigDecimal r10_CHARGE_BASIS_RISK) {
			R10_CHARGE_BASIS_RISK = r10_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_BASIS_RISK() {
			return R10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR10_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r10_CAPITAL_CHARGE_BASIS_RISK) {
			R10_CAPITAL_CHARGE_BASIS_RISK = r10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_NET_OPEN_POS() {
			return R10_NET_OPEN_POS;
		}

		public void setR10_NET_OPEN_POS(BigDecimal r10_NET_OPEN_POS) {
			R10_NET_OPEN_POS = r10_NET_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_DIR_RISK() {
			return R10_CHARGE_DIR_RISK;
		}

		public void setR10_CHARGE_DIR_RISK(BigDecimal r10_CHARGE_DIR_RISK) {
			R10_CHARGE_DIR_RISK = r10_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_DIR_RISK() {
			return R10_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR10_CAPITAL_CHARGE_DIR_RISK(BigDecimal r10_CAPITAL_CHARGE_DIR_RISK) {
			R10_CAPITAL_CHARGE_DIR_RISK = r10_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_TOTAL_CAPITAL_CHARGE() {
			return R10_TOTAL_CAPITAL_CHARGE;
		}

		public void setR10_TOTAL_CAPITAL_CHARGE(BigDecimal r10_TOTAL_CAPITAL_CHARGE) {
			R10_TOTAL_CAPITAL_CHARGE = r10_TOTAL_CAPITAL_CHARGE;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TOTAL_LONG_POS() {
			return R11_TOTAL_LONG_POS;
		}

		public void setR11_TOTAL_LONG_POS(BigDecimal r11_TOTAL_LONG_POS) {
			R11_TOTAL_LONG_POS = r11_TOTAL_LONG_POS;
		}

		public BigDecimal getR11_TOTAL_SHORT_POS() {
			return R11_TOTAL_SHORT_POS;
		}

		public void setR11_TOTAL_SHORT_POS(BigDecimal r11_TOTAL_SHORT_POS) {
			R11_TOTAL_SHORT_POS = r11_TOTAL_SHORT_POS;
		}

		public BigDecimal getR11_GROSS_OPEN_POS() {
			return R11_GROSS_OPEN_POS;
		}

		public void setR11_GROSS_OPEN_POS(BigDecimal r11_GROSS_OPEN_POS) {
			R11_GROSS_OPEN_POS = r11_GROSS_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_BASIS_RISK() {
			return R11_CHARGE_BASIS_RISK;
		}

		public void setR11_CHARGE_BASIS_RISK(BigDecimal r11_CHARGE_BASIS_RISK) {
			R11_CHARGE_BASIS_RISK = r11_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_BASIS_RISK() {
			return R11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR11_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r11_CAPITAL_CHARGE_BASIS_RISK) {
			R11_CAPITAL_CHARGE_BASIS_RISK = r11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_NET_OPEN_POS() {
			return R11_NET_OPEN_POS;
		}

		public void setR11_NET_OPEN_POS(BigDecimal r11_NET_OPEN_POS) {
			R11_NET_OPEN_POS = r11_NET_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_DIR_RISK() {
			return R11_CHARGE_DIR_RISK;
		}

		public void setR11_CHARGE_DIR_RISK(BigDecimal r11_CHARGE_DIR_RISK) {
			R11_CHARGE_DIR_RISK = r11_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_DIR_RISK() {
			return R11_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR11_CAPITAL_CHARGE_DIR_RISK(BigDecimal r11_CAPITAL_CHARGE_DIR_RISK) {
			R11_CAPITAL_CHARGE_DIR_RISK = r11_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_TOTAL_CAPITAL_CHARGE() {
			return R11_TOTAL_CAPITAL_CHARGE;
		}

		public void setR11_TOTAL_CAPITAL_CHARGE(BigDecimal r11_TOTAL_CAPITAL_CHARGE) {
			R11_TOTAL_CAPITAL_CHARGE = r11_TOTAL_CAPITAL_CHARGE;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TOTAL_LONG_POS() {
			return R12_TOTAL_LONG_POS;
		}

		public void setR12_TOTAL_LONG_POS(BigDecimal r12_TOTAL_LONG_POS) {
			R12_TOTAL_LONG_POS = r12_TOTAL_LONG_POS;
		}

		public BigDecimal getR12_TOTAL_SHORT_POS() {
			return R12_TOTAL_SHORT_POS;
		}

		public void setR12_TOTAL_SHORT_POS(BigDecimal r12_TOTAL_SHORT_POS) {
			R12_TOTAL_SHORT_POS = r12_TOTAL_SHORT_POS;
		}

		public BigDecimal getR12_GROSS_OPEN_POS() {
			return R12_GROSS_OPEN_POS;
		}

		public void setR12_GROSS_OPEN_POS(BigDecimal r12_GROSS_OPEN_POS) {
			R12_GROSS_OPEN_POS = r12_GROSS_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_BASIS_RISK() {
			return R12_CHARGE_BASIS_RISK;
		}

		public void setR12_CHARGE_BASIS_RISK(BigDecimal r12_CHARGE_BASIS_RISK) {
			R12_CHARGE_BASIS_RISK = r12_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_BASIS_RISK() {
			return R12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR12_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r12_CAPITAL_CHARGE_BASIS_RISK) {
			R12_CAPITAL_CHARGE_BASIS_RISK = r12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_NET_OPEN_POS() {
			return R12_NET_OPEN_POS;
		}

		public void setR12_NET_OPEN_POS(BigDecimal r12_NET_OPEN_POS) {
			R12_NET_OPEN_POS = r12_NET_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_DIR_RISK() {
			return R12_CHARGE_DIR_RISK;
		}

		public void setR12_CHARGE_DIR_RISK(BigDecimal r12_CHARGE_DIR_RISK) {
			R12_CHARGE_DIR_RISK = r12_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_DIR_RISK() {
			return R12_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR12_CAPITAL_CHARGE_DIR_RISK(BigDecimal r12_CAPITAL_CHARGE_DIR_RISK) {
			R12_CAPITAL_CHARGE_DIR_RISK = r12_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_TOTAL_CAPITAL_CHARGE() {
			return R12_TOTAL_CAPITAL_CHARGE;
		}

		public void setR12_TOTAL_CAPITAL_CHARGE(BigDecimal r12_TOTAL_CAPITAL_CHARGE) {
			R12_TOTAL_CAPITAL_CHARGE = r12_TOTAL_CAPITAL_CHARGE;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TOTAL_LONG_POS() {
			return R13_TOTAL_LONG_POS;
		}

		public void setR13_TOTAL_LONG_POS(BigDecimal r13_TOTAL_LONG_POS) {
			R13_TOTAL_LONG_POS = r13_TOTAL_LONG_POS;
		}

		public BigDecimal getR13_TOTAL_SHORT_POS() {
			return R13_TOTAL_SHORT_POS;
		}

		public void setR13_TOTAL_SHORT_POS(BigDecimal r13_TOTAL_SHORT_POS) {
			R13_TOTAL_SHORT_POS = r13_TOTAL_SHORT_POS;
		}

		public BigDecimal getR13_GROSS_OPEN_POS() {
			return R13_GROSS_OPEN_POS;
		}

		public void setR13_GROSS_OPEN_POS(BigDecimal r13_GROSS_OPEN_POS) {
			R13_GROSS_OPEN_POS = r13_GROSS_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_BASIS_RISK() {
			return R13_CHARGE_BASIS_RISK;
		}

		public void setR13_CHARGE_BASIS_RISK(BigDecimal r13_CHARGE_BASIS_RISK) {
			R13_CHARGE_BASIS_RISK = r13_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_BASIS_RISK() {
			return R13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR13_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r13_CAPITAL_CHARGE_BASIS_RISK) {
			R13_CAPITAL_CHARGE_BASIS_RISK = r13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_NET_OPEN_POS() {
			return R13_NET_OPEN_POS;
		}

		public void setR13_NET_OPEN_POS(BigDecimal r13_NET_OPEN_POS) {
			R13_NET_OPEN_POS = r13_NET_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_DIR_RISK() {
			return R13_CHARGE_DIR_RISK;
		}

		public void setR13_CHARGE_DIR_RISK(BigDecimal r13_CHARGE_DIR_RISK) {
			R13_CHARGE_DIR_RISK = r13_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_DIR_RISK() {
			return R13_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR13_CAPITAL_CHARGE_DIR_RISK(BigDecimal r13_CAPITAL_CHARGE_DIR_RISK) {
			R13_CAPITAL_CHARGE_DIR_RISK = r13_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_TOTAL_CAPITAL_CHARGE() {
			return R13_TOTAL_CAPITAL_CHARGE;
		}

		public void setR13_TOTAL_CAPITAL_CHARGE(BigDecimal r13_TOTAL_CAPITAL_CHARGE) {
			R13_TOTAL_CAPITAL_CHARGE = r13_TOTAL_CAPITAL_CHARGE;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TOTAL_LONG_POS() {
			return R14_TOTAL_LONG_POS;
		}

		public void setR14_TOTAL_LONG_POS(BigDecimal r14_TOTAL_LONG_POS) {
			R14_TOTAL_LONG_POS = r14_TOTAL_LONG_POS;
		}

		public BigDecimal getR14_TOTAL_SHORT_POS() {
			return R14_TOTAL_SHORT_POS;
		}

		public void setR14_TOTAL_SHORT_POS(BigDecimal r14_TOTAL_SHORT_POS) {
			R14_TOTAL_SHORT_POS = r14_TOTAL_SHORT_POS;
		}

		public BigDecimal getR14_GROSS_OPEN_POS() {
			return R14_GROSS_OPEN_POS;
		}

		public void setR14_GROSS_OPEN_POS(BigDecimal r14_GROSS_OPEN_POS) {
			R14_GROSS_OPEN_POS = r14_GROSS_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_BASIS_RISK() {
			return R14_CHARGE_BASIS_RISK;
		}

		public void setR14_CHARGE_BASIS_RISK(BigDecimal r14_CHARGE_BASIS_RISK) {
			R14_CHARGE_BASIS_RISK = r14_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_BASIS_RISK() {
			return R14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR14_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r14_CAPITAL_CHARGE_BASIS_RISK) {
			R14_CAPITAL_CHARGE_BASIS_RISK = r14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_NET_OPEN_POS() {
			return R14_NET_OPEN_POS;
		}

		public void setR14_NET_OPEN_POS(BigDecimal r14_NET_OPEN_POS) {
			R14_NET_OPEN_POS = r14_NET_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_DIR_RISK() {
			return R14_CHARGE_DIR_RISK;
		}

		public void setR14_CHARGE_DIR_RISK(BigDecimal r14_CHARGE_DIR_RISK) {
			R14_CHARGE_DIR_RISK = r14_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_DIR_RISK() {
			return R14_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR14_CAPITAL_CHARGE_DIR_RISK(BigDecimal r14_CAPITAL_CHARGE_DIR_RISK) {
			R14_CAPITAL_CHARGE_DIR_RISK = r14_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_TOTAL_CAPITAL_CHARGE() {
			return R14_TOTAL_CAPITAL_CHARGE;
		}

		public void setR14_TOTAL_CAPITAL_CHARGE(BigDecimal r14_TOTAL_CAPITAL_CHARGE) {
			R14_TOTAL_CAPITAL_CHARGE = r14_TOTAL_CAPITAL_CHARGE;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TOTAL_LONG_POS() {
			return R15_TOTAL_LONG_POS;
		}

		public void setR15_TOTAL_LONG_POS(BigDecimal r15_TOTAL_LONG_POS) {
			R15_TOTAL_LONG_POS = r15_TOTAL_LONG_POS;
		}

		public BigDecimal getR15_TOTAL_SHORT_POS() {
			return R15_TOTAL_SHORT_POS;
		}

		public void setR15_TOTAL_SHORT_POS(BigDecimal r15_TOTAL_SHORT_POS) {
			R15_TOTAL_SHORT_POS = r15_TOTAL_SHORT_POS;
		}

		public BigDecimal getR15_GROSS_OPEN_POS() {
			return R15_GROSS_OPEN_POS;
		}

		public void setR15_GROSS_OPEN_POS(BigDecimal r15_GROSS_OPEN_POS) {
			R15_GROSS_OPEN_POS = r15_GROSS_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_BASIS_RISK() {
			return R15_CHARGE_BASIS_RISK;
		}

		public void setR15_CHARGE_BASIS_RISK(BigDecimal r15_CHARGE_BASIS_RISK) {
			R15_CHARGE_BASIS_RISK = r15_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_BASIS_RISK() {
			return R15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR15_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r15_CAPITAL_CHARGE_BASIS_RISK) {
			R15_CAPITAL_CHARGE_BASIS_RISK = r15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_NET_OPEN_POS() {
			return R15_NET_OPEN_POS;
		}

		public void setR15_NET_OPEN_POS(BigDecimal r15_NET_OPEN_POS) {
			R15_NET_OPEN_POS = r15_NET_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_DIR_RISK() {
			return R15_CHARGE_DIR_RISK;
		}

		public void setR15_CHARGE_DIR_RISK(BigDecimal r15_CHARGE_DIR_RISK) {
			R15_CHARGE_DIR_RISK = r15_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_DIR_RISK() {
			return R15_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR15_CAPITAL_CHARGE_DIR_RISK(BigDecimal r15_CAPITAL_CHARGE_DIR_RISK) {
			R15_CAPITAL_CHARGE_DIR_RISK = r15_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_TOTAL_CAPITAL_CHARGE() {
			return R15_TOTAL_CAPITAL_CHARGE;
		}

		public void setR15_TOTAL_CAPITAL_CHARGE(BigDecimal r15_TOTAL_CAPITAL_CHARGE) {
			R15_TOTAL_CAPITAL_CHARGE = r15_TOTAL_CAPITAL_CHARGE;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TOTAL_LONG_POS() {
			return R16_TOTAL_LONG_POS;
		}

		public void setR16_TOTAL_LONG_POS(BigDecimal r16_TOTAL_LONG_POS) {
			R16_TOTAL_LONG_POS = r16_TOTAL_LONG_POS;
		}

		public BigDecimal getR16_TOTAL_SHORT_POS() {
			return R16_TOTAL_SHORT_POS;
		}

		public void setR16_TOTAL_SHORT_POS(BigDecimal r16_TOTAL_SHORT_POS) {
			R16_TOTAL_SHORT_POS = r16_TOTAL_SHORT_POS;
		}

		public BigDecimal getR16_GROSS_OPEN_POS() {
			return R16_GROSS_OPEN_POS;
		}

		public void setR16_GROSS_OPEN_POS(BigDecimal r16_GROSS_OPEN_POS) {
			R16_GROSS_OPEN_POS = r16_GROSS_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_BASIS_RISK() {
			return R16_CHARGE_BASIS_RISK;
		}

		public void setR16_CHARGE_BASIS_RISK(BigDecimal r16_CHARGE_BASIS_RISK) {
			R16_CHARGE_BASIS_RISK = r16_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_BASIS_RISK() {
			return R16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR16_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r16_CAPITAL_CHARGE_BASIS_RISK) {
			R16_CAPITAL_CHARGE_BASIS_RISK = r16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_NET_OPEN_POS() {
			return R16_NET_OPEN_POS;
		}

		public void setR16_NET_OPEN_POS(BigDecimal r16_NET_OPEN_POS) {
			R16_NET_OPEN_POS = r16_NET_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_DIR_RISK() {
			return R16_CHARGE_DIR_RISK;
		}

		public void setR16_CHARGE_DIR_RISK(BigDecimal r16_CHARGE_DIR_RISK) {
			R16_CHARGE_DIR_RISK = r16_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_DIR_RISK() {
			return R16_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR16_CAPITAL_CHARGE_DIR_RISK(BigDecimal r16_CAPITAL_CHARGE_DIR_RISK) {
			R16_CAPITAL_CHARGE_DIR_RISK = r16_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_TOTAL_CAPITAL_CHARGE() {
			return R16_TOTAL_CAPITAL_CHARGE;
		}

		public void setR16_TOTAL_CAPITAL_CHARGE(BigDecimal r16_TOTAL_CAPITAL_CHARGE) {
			R16_TOTAL_CAPITAL_CHARGE = r16_TOTAL_CAPITAL_CHARGE;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TOTAL_LONG_POS() {
			return R17_TOTAL_LONG_POS;
		}

		public void setR17_TOTAL_LONG_POS(BigDecimal r17_TOTAL_LONG_POS) {
			R17_TOTAL_LONG_POS = r17_TOTAL_LONG_POS;
		}

		public BigDecimal getR17_TOTAL_SHORT_POS() {
			return R17_TOTAL_SHORT_POS;
		}

		public void setR17_TOTAL_SHORT_POS(BigDecimal r17_TOTAL_SHORT_POS) {
			R17_TOTAL_SHORT_POS = r17_TOTAL_SHORT_POS;
		}

		public BigDecimal getR17_GROSS_OPEN_POS() {
			return R17_GROSS_OPEN_POS;
		}

		public void setR17_GROSS_OPEN_POS(BigDecimal r17_GROSS_OPEN_POS) {
			R17_GROSS_OPEN_POS = r17_GROSS_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_BASIS_RISK() {
			return R17_CHARGE_BASIS_RISK;
		}

		public void setR17_CHARGE_BASIS_RISK(BigDecimal r17_CHARGE_BASIS_RISK) {
			R17_CHARGE_BASIS_RISK = r17_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_BASIS_RISK() {
			return R17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR17_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r17_CAPITAL_CHARGE_BASIS_RISK) {
			R17_CAPITAL_CHARGE_BASIS_RISK = r17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_NET_OPEN_POS() {
			return R17_NET_OPEN_POS;
		}

		public void setR17_NET_OPEN_POS(BigDecimal r17_NET_OPEN_POS) {
			R17_NET_OPEN_POS = r17_NET_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_DIR_RISK() {
			return R17_CHARGE_DIR_RISK;
		}

		public void setR17_CHARGE_DIR_RISK(BigDecimal r17_CHARGE_DIR_RISK) {
			R17_CHARGE_DIR_RISK = r17_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_DIR_RISK() {
			return R17_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR17_CAPITAL_CHARGE_DIR_RISK(BigDecimal r17_CAPITAL_CHARGE_DIR_RISK) {
			R17_CAPITAL_CHARGE_DIR_RISK = r17_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_TOTAL_CAPITAL_CHARGE() {
			return R17_TOTAL_CAPITAL_CHARGE;
		}

		public void setR17_TOTAL_CAPITAL_CHARGE(BigDecimal r17_TOTAL_CAPITAL_CHARGE) {
			R17_TOTAL_CAPITAL_CHARGE = r17_TOTAL_CAPITAL_CHARGE;
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

		public M_CR_Archival_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	public static class M_CR_Archival_Detail_Entity {

		private String R10_PRODUCT;
		private BigDecimal R10_TOTAL_LONG_POS;
		private BigDecimal R10_TOTAL_SHORT_POS;
		private BigDecimal R10_GROSS_OPEN_POS;
		private BigDecimal R10_CHARGE_BASIS_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R10_NET_OPEN_POS;
		private BigDecimal R10_CHARGE_DIR_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R10_TOTAL_CAPITAL_CHARGE;
		private String R11_PRODUCT;
		private BigDecimal R11_TOTAL_LONG_POS;
		private BigDecimal R11_TOTAL_SHORT_POS;
		private BigDecimal R11_GROSS_OPEN_POS;
		private BigDecimal R11_CHARGE_BASIS_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R11_NET_OPEN_POS;
		private BigDecimal R11_CHARGE_DIR_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R11_TOTAL_CAPITAL_CHARGE;
		private String R12_PRODUCT;
		private BigDecimal R12_TOTAL_LONG_POS;
		private BigDecimal R12_TOTAL_SHORT_POS;
		private BigDecimal R12_GROSS_OPEN_POS;
		private BigDecimal R12_CHARGE_BASIS_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R12_NET_OPEN_POS;
		private BigDecimal R12_CHARGE_DIR_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R12_TOTAL_CAPITAL_CHARGE;
		private String R13_PRODUCT;
		private BigDecimal R13_TOTAL_LONG_POS;
		private BigDecimal R13_TOTAL_SHORT_POS;
		private BigDecimal R13_GROSS_OPEN_POS;
		private BigDecimal R13_CHARGE_BASIS_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R13_NET_OPEN_POS;
		private BigDecimal R13_CHARGE_DIR_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R13_TOTAL_CAPITAL_CHARGE;
		private String R14_PRODUCT;
		private BigDecimal R14_TOTAL_LONG_POS;
		private BigDecimal R14_TOTAL_SHORT_POS;
		private BigDecimal R14_GROSS_OPEN_POS;
		private BigDecimal R14_CHARGE_BASIS_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R14_NET_OPEN_POS;
		private BigDecimal R14_CHARGE_DIR_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R14_TOTAL_CAPITAL_CHARGE;
		private String R15_PRODUCT;
		private BigDecimal R15_TOTAL_LONG_POS;
		private BigDecimal R15_TOTAL_SHORT_POS;
		private BigDecimal R15_GROSS_OPEN_POS;
		private BigDecimal R15_CHARGE_BASIS_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R15_NET_OPEN_POS;
		private BigDecimal R15_CHARGE_DIR_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R15_TOTAL_CAPITAL_CHARGE;
		private String R16_PRODUCT;
		private BigDecimal R16_TOTAL_LONG_POS;
		private BigDecimal R16_TOTAL_SHORT_POS;
		private BigDecimal R16_GROSS_OPEN_POS;
		private BigDecimal R16_CHARGE_BASIS_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R16_NET_OPEN_POS;
		private BigDecimal R16_CHARGE_DIR_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R16_TOTAL_CAPITAL_CHARGE;
		private String R17_PRODUCT;
		private BigDecimal R17_TOTAL_LONG_POS;
		private BigDecimal R17_TOTAL_SHORT_POS;
		private BigDecimal R17_GROSS_OPEN_POS;
		private BigDecimal R17_CHARGE_BASIS_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R17_NET_OPEN_POS;
		private BigDecimal R17_CHARGE_DIR_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R17_TOTAL_CAPITAL_CHARGE;
		private Date reportDate;

		private BigDecimal reportVersion;

		private Date reportResubDate;

		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_TOTAL_LONG_POS() {
			return R10_TOTAL_LONG_POS;
		}

		public void setR10_TOTAL_LONG_POS(BigDecimal r10_TOTAL_LONG_POS) {
			R10_TOTAL_LONG_POS = r10_TOTAL_LONG_POS;
		}

		public BigDecimal getR10_TOTAL_SHORT_POS() {
			return R10_TOTAL_SHORT_POS;
		}

		public void setR10_TOTAL_SHORT_POS(BigDecimal r10_TOTAL_SHORT_POS) {
			R10_TOTAL_SHORT_POS = r10_TOTAL_SHORT_POS;
		}

		public BigDecimal getR10_GROSS_OPEN_POS() {
			return R10_GROSS_OPEN_POS;
		}

		public void setR10_GROSS_OPEN_POS(BigDecimal r10_GROSS_OPEN_POS) {
			R10_GROSS_OPEN_POS = r10_GROSS_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_BASIS_RISK() {
			return R10_CHARGE_BASIS_RISK;
		}

		public void setR10_CHARGE_BASIS_RISK(BigDecimal r10_CHARGE_BASIS_RISK) {
			R10_CHARGE_BASIS_RISK = r10_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_BASIS_RISK() {
			return R10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR10_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r10_CAPITAL_CHARGE_BASIS_RISK) {
			R10_CAPITAL_CHARGE_BASIS_RISK = r10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_NET_OPEN_POS() {
			return R10_NET_OPEN_POS;
		}

		public void setR10_NET_OPEN_POS(BigDecimal r10_NET_OPEN_POS) {
			R10_NET_OPEN_POS = r10_NET_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_DIR_RISK() {
			return R10_CHARGE_DIR_RISK;
		}

		public void setR10_CHARGE_DIR_RISK(BigDecimal r10_CHARGE_DIR_RISK) {
			R10_CHARGE_DIR_RISK = r10_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_DIR_RISK() {
			return R10_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR10_CAPITAL_CHARGE_DIR_RISK(BigDecimal r10_CAPITAL_CHARGE_DIR_RISK) {
			R10_CAPITAL_CHARGE_DIR_RISK = r10_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_TOTAL_CAPITAL_CHARGE() {
			return R10_TOTAL_CAPITAL_CHARGE;
		}

		public void setR10_TOTAL_CAPITAL_CHARGE(BigDecimal r10_TOTAL_CAPITAL_CHARGE) {
			R10_TOTAL_CAPITAL_CHARGE = r10_TOTAL_CAPITAL_CHARGE;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TOTAL_LONG_POS() {
			return R11_TOTAL_LONG_POS;
		}

		public void setR11_TOTAL_LONG_POS(BigDecimal r11_TOTAL_LONG_POS) {
			R11_TOTAL_LONG_POS = r11_TOTAL_LONG_POS;
		}

		public BigDecimal getR11_TOTAL_SHORT_POS() {
			return R11_TOTAL_SHORT_POS;
		}

		public void setR11_TOTAL_SHORT_POS(BigDecimal r11_TOTAL_SHORT_POS) {
			R11_TOTAL_SHORT_POS = r11_TOTAL_SHORT_POS;
		}

		public BigDecimal getR11_GROSS_OPEN_POS() {
			return R11_GROSS_OPEN_POS;
		}

		public void setR11_GROSS_OPEN_POS(BigDecimal r11_GROSS_OPEN_POS) {
			R11_GROSS_OPEN_POS = r11_GROSS_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_BASIS_RISK() {
			return R11_CHARGE_BASIS_RISK;
		}

		public void setR11_CHARGE_BASIS_RISK(BigDecimal r11_CHARGE_BASIS_RISK) {
			R11_CHARGE_BASIS_RISK = r11_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_BASIS_RISK() {
			return R11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR11_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r11_CAPITAL_CHARGE_BASIS_RISK) {
			R11_CAPITAL_CHARGE_BASIS_RISK = r11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_NET_OPEN_POS() {
			return R11_NET_OPEN_POS;
		}

		public void setR11_NET_OPEN_POS(BigDecimal r11_NET_OPEN_POS) {
			R11_NET_OPEN_POS = r11_NET_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_DIR_RISK() {
			return R11_CHARGE_DIR_RISK;
		}

		public void setR11_CHARGE_DIR_RISK(BigDecimal r11_CHARGE_DIR_RISK) {
			R11_CHARGE_DIR_RISK = r11_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_DIR_RISK() {
			return R11_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR11_CAPITAL_CHARGE_DIR_RISK(BigDecimal r11_CAPITAL_CHARGE_DIR_RISK) {
			R11_CAPITAL_CHARGE_DIR_RISK = r11_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_TOTAL_CAPITAL_CHARGE() {
			return R11_TOTAL_CAPITAL_CHARGE;
		}

		public void setR11_TOTAL_CAPITAL_CHARGE(BigDecimal r11_TOTAL_CAPITAL_CHARGE) {
			R11_TOTAL_CAPITAL_CHARGE = r11_TOTAL_CAPITAL_CHARGE;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TOTAL_LONG_POS() {
			return R12_TOTAL_LONG_POS;
		}

		public void setR12_TOTAL_LONG_POS(BigDecimal r12_TOTAL_LONG_POS) {
			R12_TOTAL_LONG_POS = r12_TOTAL_LONG_POS;
		}

		public BigDecimal getR12_TOTAL_SHORT_POS() {
			return R12_TOTAL_SHORT_POS;
		}

		public void setR12_TOTAL_SHORT_POS(BigDecimal r12_TOTAL_SHORT_POS) {
			R12_TOTAL_SHORT_POS = r12_TOTAL_SHORT_POS;
		}

		public BigDecimal getR12_GROSS_OPEN_POS() {
			return R12_GROSS_OPEN_POS;
		}

		public void setR12_GROSS_OPEN_POS(BigDecimal r12_GROSS_OPEN_POS) {
			R12_GROSS_OPEN_POS = r12_GROSS_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_BASIS_RISK() {
			return R12_CHARGE_BASIS_RISK;
		}

		public void setR12_CHARGE_BASIS_RISK(BigDecimal r12_CHARGE_BASIS_RISK) {
			R12_CHARGE_BASIS_RISK = r12_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_BASIS_RISK() {
			return R12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR12_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r12_CAPITAL_CHARGE_BASIS_RISK) {
			R12_CAPITAL_CHARGE_BASIS_RISK = r12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_NET_OPEN_POS() {
			return R12_NET_OPEN_POS;
		}

		public void setR12_NET_OPEN_POS(BigDecimal r12_NET_OPEN_POS) {
			R12_NET_OPEN_POS = r12_NET_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_DIR_RISK() {
			return R12_CHARGE_DIR_RISK;
		}

		public void setR12_CHARGE_DIR_RISK(BigDecimal r12_CHARGE_DIR_RISK) {
			R12_CHARGE_DIR_RISK = r12_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_DIR_RISK() {
			return R12_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR12_CAPITAL_CHARGE_DIR_RISK(BigDecimal r12_CAPITAL_CHARGE_DIR_RISK) {
			R12_CAPITAL_CHARGE_DIR_RISK = r12_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_TOTAL_CAPITAL_CHARGE() {
			return R12_TOTAL_CAPITAL_CHARGE;
		}

		public void setR12_TOTAL_CAPITAL_CHARGE(BigDecimal r12_TOTAL_CAPITAL_CHARGE) {
			R12_TOTAL_CAPITAL_CHARGE = r12_TOTAL_CAPITAL_CHARGE;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TOTAL_LONG_POS() {
			return R13_TOTAL_LONG_POS;
		}

		public void setR13_TOTAL_LONG_POS(BigDecimal r13_TOTAL_LONG_POS) {
			R13_TOTAL_LONG_POS = r13_TOTAL_LONG_POS;
		}

		public BigDecimal getR13_TOTAL_SHORT_POS() {
			return R13_TOTAL_SHORT_POS;
		}

		public void setR13_TOTAL_SHORT_POS(BigDecimal r13_TOTAL_SHORT_POS) {
			R13_TOTAL_SHORT_POS = r13_TOTAL_SHORT_POS;
		}

		public BigDecimal getR13_GROSS_OPEN_POS() {
			return R13_GROSS_OPEN_POS;
		}

		public void setR13_GROSS_OPEN_POS(BigDecimal r13_GROSS_OPEN_POS) {
			R13_GROSS_OPEN_POS = r13_GROSS_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_BASIS_RISK() {
			return R13_CHARGE_BASIS_RISK;
		}

		public void setR13_CHARGE_BASIS_RISK(BigDecimal r13_CHARGE_BASIS_RISK) {
			R13_CHARGE_BASIS_RISK = r13_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_BASIS_RISK() {
			return R13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR13_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r13_CAPITAL_CHARGE_BASIS_RISK) {
			R13_CAPITAL_CHARGE_BASIS_RISK = r13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_NET_OPEN_POS() {
			return R13_NET_OPEN_POS;
		}

		public void setR13_NET_OPEN_POS(BigDecimal r13_NET_OPEN_POS) {
			R13_NET_OPEN_POS = r13_NET_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_DIR_RISK() {
			return R13_CHARGE_DIR_RISK;
		}

		public void setR13_CHARGE_DIR_RISK(BigDecimal r13_CHARGE_DIR_RISK) {
			R13_CHARGE_DIR_RISK = r13_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_DIR_RISK() {
			return R13_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR13_CAPITAL_CHARGE_DIR_RISK(BigDecimal r13_CAPITAL_CHARGE_DIR_RISK) {
			R13_CAPITAL_CHARGE_DIR_RISK = r13_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_TOTAL_CAPITAL_CHARGE() {
			return R13_TOTAL_CAPITAL_CHARGE;
		}

		public void setR13_TOTAL_CAPITAL_CHARGE(BigDecimal r13_TOTAL_CAPITAL_CHARGE) {
			R13_TOTAL_CAPITAL_CHARGE = r13_TOTAL_CAPITAL_CHARGE;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TOTAL_LONG_POS() {
			return R14_TOTAL_LONG_POS;
		}

		public void setR14_TOTAL_LONG_POS(BigDecimal r14_TOTAL_LONG_POS) {
			R14_TOTAL_LONG_POS = r14_TOTAL_LONG_POS;
		}

		public BigDecimal getR14_TOTAL_SHORT_POS() {
			return R14_TOTAL_SHORT_POS;
		}

		public void setR14_TOTAL_SHORT_POS(BigDecimal r14_TOTAL_SHORT_POS) {
			R14_TOTAL_SHORT_POS = r14_TOTAL_SHORT_POS;
		}

		public BigDecimal getR14_GROSS_OPEN_POS() {
			return R14_GROSS_OPEN_POS;
		}

		public void setR14_GROSS_OPEN_POS(BigDecimal r14_GROSS_OPEN_POS) {
			R14_GROSS_OPEN_POS = r14_GROSS_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_BASIS_RISK() {
			return R14_CHARGE_BASIS_RISK;
		}

		public void setR14_CHARGE_BASIS_RISK(BigDecimal r14_CHARGE_BASIS_RISK) {
			R14_CHARGE_BASIS_RISK = r14_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_BASIS_RISK() {
			return R14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR14_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r14_CAPITAL_CHARGE_BASIS_RISK) {
			R14_CAPITAL_CHARGE_BASIS_RISK = r14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_NET_OPEN_POS() {
			return R14_NET_OPEN_POS;
		}

		public void setR14_NET_OPEN_POS(BigDecimal r14_NET_OPEN_POS) {
			R14_NET_OPEN_POS = r14_NET_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_DIR_RISK() {
			return R14_CHARGE_DIR_RISK;
		}

		public void setR14_CHARGE_DIR_RISK(BigDecimal r14_CHARGE_DIR_RISK) {
			R14_CHARGE_DIR_RISK = r14_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_DIR_RISK() {
			return R14_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR14_CAPITAL_CHARGE_DIR_RISK(BigDecimal r14_CAPITAL_CHARGE_DIR_RISK) {
			R14_CAPITAL_CHARGE_DIR_RISK = r14_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_TOTAL_CAPITAL_CHARGE() {
			return R14_TOTAL_CAPITAL_CHARGE;
		}

		public void setR14_TOTAL_CAPITAL_CHARGE(BigDecimal r14_TOTAL_CAPITAL_CHARGE) {
			R14_TOTAL_CAPITAL_CHARGE = r14_TOTAL_CAPITAL_CHARGE;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TOTAL_LONG_POS() {
			return R15_TOTAL_LONG_POS;
		}

		public void setR15_TOTAL_LONG_POS(BigDecimal r15_TOTAL_LONG_POS) {
			R15_TOTAL_LONG_POS = r15_TOTAL_LONG_POS;
		}

		public BigDecimal getR15_TOTAL_SHORT_POS() {
			return R15_TOTAL_SHORT_POS;
		}

		public void setR15_TOTAL_SHORT_POS(BigDecimal r15_TOTAL_SHORT_POS) {
			R15_TOTAL_SHORT_POS = r15_TOTAL_SHORT_POS;
		}

		public BigDecimal getR15_GROSS_OPEN_POS() {
			return R15_GROSS_OPEN_POS;
		}

		public void setR15_GROSS_OPEN_POS(BigDecimal r15_GROSS_OPEN_POS) {
			R15_GROSS_OPEN_POS = r15_GROSS_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_BASIS_RISK() {
			return R15_CHARGE_BASIS_RISK;
		}

		public void setR15_CHARGE_BASIS_RISK(BigDecimal r15_CHARGE_BASIS_RISK) {
			R15_CHARGE_BASIS_RISK = r15_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_BASIS_RISK() {
			return R15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR15_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r15_CAPITAL_CHARGE_BASIS_RISK) {
			R15_CAPITAL_CHARGE_BASIS_RISK = r15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_NET_OPEN_POS() {
			return R15_NET_OPEN_POS;
		}

		public void setR15_NET_OPEN_POS(BigDecimal r15_NET_OPEN_POS) {
			R15_NET_OPEN_POS = r15_NET_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_DIR_RISK() {
			return R15_CHARGE_DIR_RISK;
		}

		public void setR15_CHARGE_DIR_RISK(BigDecimal r15_CHARGE_DIR_RISK) {
			R15_CHARGE_DIR_RISK = r15_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_DIR_RISK() {
			return R15_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR15_CAPITAL_CHARGE_DIR_RISK(BigDecimal r15_CAPITAL_CHARGE_DIR_RISK) {
			R15_CAPITAL_CHARGE_DIR_RISK = r15_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_TOTAL_CAPITAL_CHARGE() {
			return R15_TOTAL_CAPITAL_CHARGE;
		}

		public void setR15_TOTAL_CAPITAL_CHARGE(BigDecimal r15_TOTAL_CAPITAL_CHARGE) {
			R15_TOTAL_CAPITAL_CHARGE = r15_TOTAL_CAPITAL_CHARGE;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TOTAL_LONG_POS() {
			return R16_TOTAL_LONG_POS;
		}

		public void setR16_TOTAL_LONG_POS(BigDecimal r16_TOTAL_LONG_POS) {
			R16_TOTAL_LONG_POS = r16_TOTAL_LONG_POS;
		}

		public BigDecimal getR16_TOTAL_SHORT_POS() {
			return R16_TOTAL_SHORT_POS;
		}

		public void setR16_TOTAL_SHORT_POS(BigDecimal r16_TOTAL_SHORT_POS) {
			R16_TOTAL_SHORT_POS = r16_TOTAL_SHORT_POS;
		}

		public BigDecimal getR16_GROSS_OPEN_POS() {
			return R16_GROSS_OPEN_POS;
		}

		public void setR16_GROSS_OPEN_POS(BigDecimal r16_GROSS_OPEN_POS) {
			R16_GROSS_OPEN_POS = r16_GROSS_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_BASIS_RISK() {
			return R16_CHARGE_BASIS_RISK;
		}

		public void setR16_CHARGE_BASIS_RISK(BigDecimal r16_CHARGE_BASIS_RISK) {
			R16_CHARGE_BASIS_RISK = r16_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_BASIS_RISK() {
			return R16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR16_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r16_CAPITAL_CHARGE_BASIS_RISK) {
			R16_CAPITAL_CHARGE_BASIS_RISK = r16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_NET_OPEN_POS() {
			return R16_NET_OPEN_POS;
		}

		public void setR16_NET_OPEN_POS(BigDecimal r16_NET_OPEN_POS) {
			R16_NET_OPEN_POS = r16_NET_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_DIR_RISK() {
			return R16_CHARGE_DIR_RISK;
		}

		public void setR16_CHARGE_DIR_RISK(BigDecimal r16_CHARGE_DIR_RISK) {
			R16_CHARGE_DIR_RISK = r16_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_DIR_RISK() {
			return R16_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR16_CAPITAL_CHARGE_DIR_RISK(BigDecimal r16_CAPITAL_CHARGE_DIR_RISK) {
			R16_CAPITAL_CHARGE_DIR_RISK = r16_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_TOTAL_CAPITAL_CHARGE() {
			return R16_TOTAL_CAPITAL_CHARGE;
		}

		public void setR16_TOTAL_CAPITAL_CHARGE(BigDecimal r16_TOTAL_CAPITAL_CHARGE) {
			R16_TOTAL_CAPITAL_CHARGE = r16_TOTAL_CAPITAL_CHARGE;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TOTAL_LONG_POS() {
			return R17_TOTAL_LONG_POS;
		}

		public void setR17_TOTAL_LONG_POS(BigDecimal r17_TOTAL_LONG_POS) {
			R17_TOTAL_LONG_POS = r17_TOTAL_LONG_POS;
		}

		public BigDecimal getR17_TOTAL_SHORT_POS() {
			return R17_TOTAL_SHORT_POS;
		}

		public void setR17_TOTAL_SHORT_POS(BigDecimal r17_TOTAL_SHORT_POS) {
			R17_TOTAL_SHORT_POS = r17_TOTAL_SHORT_POS;
		}

		public BigDecimal getR17_GROSS_OPEN_POS() {
			return R17_GROSS_OPEN_POS;
		}

		public void setR17_GROSS_OPEN_POS(BigDecimal r17_GROSS_OPEN_POS) {
			R17_GROSS_OPEN_POS = r17_GROSS_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_BASIS_RISK() {
			return R17_CHARGE_BASIS_RISK;
		}

		public void setR17_CHARGE_BASIS_RISK(BigDecimal r17_CHARGE_BASIS_RISK) {
			R17_CHARGE_BASIS_RISK = r17_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_BASIS_RISK() {
			return R17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR17_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r17_CAPITAL_CHARGE_BASIS_RISK) {
			R17_CAPITAL_CHARGE_BASIS_RISK = r17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_NET_OPEN_POS() {
			return R17_NET_OPEN_POS;
		}

		public void setR17_NET_OPEN_POS(BigDecimal r17_NET_OPEN_POS) {
			R17_NET_OPEN_POS = r17_NET_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_DIR_RISK() {
			return R17_CHARGE_DIR_RISK;
		}

		public void setR17_CHARGE_DIR_RISK(BigDecimal r17_CHARGE_DIR_RISK) {
			R17_CHARGE_DIR_RISK = r17_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_DIR_RISK() {
			return R17_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR17_CAPITAL_CHARGE_DIR_RISK(BigDecimal r17_CAPITAL_CHARGE_DIR_RISK) {
			R17_CAPITAL_CHARGE_DIR_RISK = r17_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_TOTAL_CAPITAL_CHARGE() {
			return R17_TOTAL_CAPITAL_CHARGE;
		}

		public void setR17_TOTAL_CAPITAL_CHARGE(BigDecimal r17_TOTAL_CAPITAL_CHARGE) {
			R17_TOTAL_CAPITAL_CHARGE = r17_TOTAL_CAPITAL_CHARGE;
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

		public M_CR_Archival_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	public static class M_CR_Resub_Summary_Entity {

		private String R10_PRODUCT;
		private BigDecimal R10_TOTAL_LONG_POS;
		private BigDecimal R10_TOTAL_SHORT_POS;
		private BigDecimal R10_GROSS_OPEN_POS;
		private BigDecimal R10_CHARGE_BASIS_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R10_NET_OPEN_POS;
		private BigDecimal R10_CHARGE_DIR_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R10_TOTAL_CAPITAL_CHARGE;
		private String R11_PRODUCT;
		private BigDecimal R11_TOTAL_LONG_POS;
		private BigDecimal R11_TOTAL_SHORT_POS;
		private BigDecimal R11_GROSS_OPEN_POS;
		private BigDecimal R11_CHARGE_BASIS_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R11_NET_OPEN_POS;
		private BigDecimal R11_CHARGE_DIR_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R11_TOTAL_CAPITAL_CHARGE;
		private String R12_PRODUCT;
		private BigDecimal R12_TOTAL_LONG_POS;
		private BigDecimal R12_TOTAL_SHORT_POS;
		private BigDecimal R12_GROSS_OPEN_POS;
		private BigDecimal R12_CHARGE_BASIS_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R12_NET_OPEN_POS;
		private BigDecimal R12_CHARGE_DIR_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R12_TOTAL_CAPITAL_CHARGE;
		private String R13_PRODUCT;
		private BigDecimal R13_TOTAL_LONG_POS;
		private BigDecimal R13_TOTAL_SHORT_POS;
		private BigDecimal R13_GROSS_OPEN_POS;
		private BigDecimal R13_CHARGE_BASIS_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R13_NET_OPEN_POS;
		private BigDecimal R13_CHARGE_DIR_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R13_TOTAL_CAPITAL_CHARGE;
		private String R14_PRODUCT;
		private BigDecimal R14_TOTAL_LONG_POS;
		private BigDecimal R14_TOTAL_SHORT_POS;
		private BigDecimal R14_GROSS_OPEN_POS;
		private BigDecimal R14_CHARGE_BASIS_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R14_NET_OPEN_POS;
		private BigDecimal R14_CHARGE_DIR_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R14_TOTAL_CAPITAL_CHARGE;
		private String R15_PRODUCT;
		private BigDecimal R15_TOTAL_LONG_POS;
		private BigDecimal R15_TOTAL_SHORT_POS;
		private BigDecimal R15_GROSS_OPEN_POS;
		private BigDecimal R15_CHARGE_BASIS_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R15_NET_OPEN_POS;
		private BigDecimal R15_CHARGE_DIR_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R15_TOTAL_CAPITAL_CHARGE;
		private String R16_PRODUCT;
		private BigDecimal R16_TOTAL_LONG_POS;
		private BigDecimal R16_TOTAL_SHORT_POS;
		private BigDecimal R16_GROSS_OPEN_POS;
		private BigDecimal R16_CHARGE_BASIS_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R16_NET_OPEN_POS;
		private BigDecimal R16_CHARGE_DIR_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R16_TOTAL_CAPITAL_CHARGE;
		private String R17_PRODUCT;
		private BigDecimal R17_TOTAL_LONG_POS;
		private BigDecimal R17_TOTAL_SHORT_POS;
		private BigDecimal R17_GROSS_OPEN_POS;
		private BigDecimal R17_CHARGE_BASIS_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R17_NET_OPEN_POS;
		private BigDecimal R17_CHARGE_DIR_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R17_TOTAL_CAPITAL_CHARGE;

		private Date reportDate;

		private BigDecimal reportVersion;

		private Date reportResubDate;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_TOTAL_LONG_POS() {
			return R10_TOTAL_LONG_POS;
		}

		public void setR10_TOTAL_LONG_POS(BigDecimal r10_TOTAL_LONG_POS) {
			R10_TOTAL_LONG_POS = r10_TOTAL_LONG_POS;
		}

		public BigDecimal getR10_TOTAL_SHORT_POS() {
			return R10_TOTAL_SHORT_POS;
		}

		public void setR10_TOTAL_SHORT_POS(BigDecimal r10_TOTAL_SHORT_POS) {
			R10_TOTAL_SHORT_POS = r10_TOTAL_SHORT_POS;
		}

		public BigDecimal getR10_GROSS_OPEN_POS() {
			return R10_GROSS_OPEN_POS;
		}

		public void setR10_GROSS_OPEN_POS(BigDecimal r10_GROSS_OPEN_POS) {
			R10_GROSS_OPEN_POS = r10_GROSS_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_BASIS_RISK() {
			return R10_CHARGE_BASIS_RISK;
		}

		public void setR10_CHARGE_BASIS_RISK(BigDecimal r10_CHARGE_BASIS_RISK) {
			R10_CHARGE_BASIS_RISK = r10_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_BASIS_RISK() {
			return R10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR10_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r10_CAPITAL_CHARGE_BASIS_RISK) {
			R10_CAPITAL_CHARGE_BASIS_RISK = r10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_NET_OPEN_POS() {
			return R10_NET_OPEN_POS;
		}

		public void setR10_NET_OPEN_POS(BigDecimal r10_NET_OPEN_POS) {
			R10_NET_OPEN_POS = r10_NET_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_DIR_RISK() {
			return R10_CHARGE_DIR_RISK;
		}

		public void setR10_CHARGE_DIR_RISK(BigDecimal r10_CHARGE_DIR_RISK) {
			R10_CHARGE_DIR_RISK = r10_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_DIR_RISK() {
			return R10_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR10_CAPITAL_CHARGE_DIR_RISK(BigDecimal r10_CAPITAL_CHARGE_DIR_RISK) {
			R10_CAPITAL_CHARGE_DIR_RISK = r10_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_TOTAL_CAPITAL_CHARGE() {
			return R10_TOTAL_CAPITAL_CHARGE;
		}

		public void setR10_TOTAL_CAPITAL_CHARGE(BigDecimal r10_TOTAL_CAPITAL_CHARGE) {
			R10_TOTAL_CAPITAL_CHARGE = r10_TOTAL_CAPITAL_CHARGE;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TOTAL_LONG_POS() {
			return R11_TOTAL_LONG_POS;
		}

		public void setR11_TOTAL_LONG_POS(BigDecimal r11_TOTAL_LONG_POS) {
			R11_TOTAL_LONG_POS = r11_TOTAL_LONG_POS;
		}

		public BigDecimal getR11_TOTAL_SHORT_POS() {
			return R11_TOTAL_SHORT_POS;
		}

		public void setR11_TOTAL_SHORT_POS(BigDecimal r11_TOTAL_SHORT_POS) {
			R11_TOTAL_SHORT_POS = r11_TOTAL_SHORT_POS;
		}

		public BigDecimal getR11_GROSS_OPEN_POS() {
			return R11_GROSS_OPEN_POS;
		}

		public void setR11_GROSS_OPEN_POS(BigDecimal r11_GROSS_OPEN_POS) {
			R11_GROSS_OPEN_POS = r11_GROSS_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_BASIS_RISK() {
			return R11_CHARGE_BASIS_RISK;
		}

		public void setR11_CHARGE_BASIS_RISK(BigDecimal r11_CHARGE_BASIS_RISK) {
			R11_CHARGE_BASIS_RISK = r11_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_BASIS_RISK() {
			return R11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR11_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r11_CAPITAL_CHARGE_BASIS_RISK) {
			R11_CAPITAL_CHARGE_BASIS_RISK = r11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_NET_OPEN_POS() {
			return R11_NET_OPEN_POS;
		}

		public void setR11_NET_OPEN_POS(BigDecimal r11_NET_OPEN_POS) {
			R11_NET_OPEN_POS = r11_NET_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_DIR_RISK() {
			return R11_CHARGE_DIR_RISK;
		}

		public void setR11_CHARGE_DIR_RISK(BigDecimal r11_CHARGE_DIR_RISK) {
			R11_CHARGE_DIR_RISK = r11_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_DIR_RISK() {
			return R11_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR11_CAPITAL_CHARGE_DIR_RISK(BigDecimal r11_CAPITAL_CHARGE_DIR_RISK) {
			R11_CAPITAL_CHARGE_DIR_RISK = r11_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_TOTAL_CAPITAL_CHARGE() {
			return R11_TOTAL_CAPITAL_CHARGE;
		}

		public void setR11_TOTAL_CAPITAL_CHARGE(BigDecimal r11_TOTAL_CAPITAL_CHARGE) {
			R11_TOTAL_CAPITAL_CHARGE = r11_TOTAL_CAPITAL_CHARGE;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TOTAL_LONG_POS() {
			return R12_TOTAL_LONG_POS;
		}

		public void setR12_TOTAL_LONG_POS(BigDecimal r12_TOTAL_LONG_POS) {
			R12_TOTAL_LONG_POS = r12_TOTAL_LONG_POS;
		}

		public BigDecimal getR12_TOTAL_SHORT_POS() {
			return R12_TOTAL_SHORT_POS;
		}

		public void setR12_TOTAL_SHORT_POS(BigDecimal r12_TOTAL_SHORT_POS) {
			R12_TOTAL_SHORT_POS = r12_TOTAL_SHORT_POS;
		}

		public BigDecimal getR12_GROSS_OPEN_POS() {
			return R12_GROSS_OPEN_POS;
		}

		public void setR12_GROSS_OPEN_POS(BigDecimal r12_GROSS_OPEN_POS) {
			R12_GROSS_OPEN_POS = r12_GROSS_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_BASIS_RISK() {
			return R12_CHARGE_BASIS_RISK;
		}

		public void setR12_CHARGE_BASIS_RISK(BigDecimal r12_CHARGE_BASIS_RISK) {
			R12_CHARGE_BASIS_RISK = r12_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_BASIS_RISK() {
			return R12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR12_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r12_CAPITAL_CHARGE_BASIS_RISK) {
			R12_CAPITAL_CHARGE_BASIS_RISK = r12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_NET_OPEN_POS() {
			return R12_NET_OPEN_POS;
		}

		public void setR12_NET_OPEN_POS(BigDecimal r12_NET_OPEN_POS) {
			R12_NET_OPEN_POS = r12_NET_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_DIR_RISK() {
			return R12_CHARGE_DIR_RISK;
		}

		public void setR12_CHARGE_DIR_RISK(BigDecimal r12_CHARGE_DIR_RISK) {
			R12_CHARGE_DIR_RISK = r12_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_DIR_RISK() {
			return R12_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR12_CAPITAL_CHARGE_DIR_RISK(BigDecimal r12_CAPITAL_CHARGE_DIR_RISK) {
			R12_CAPITAL_CHARGE_DIR_RISK = r12_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_TOTAL_CAPITAL_CHARGE() {
			return R12_TOTAL_CAPITAL_CHARGE;
		}

		public void setR12_TOTAL_CAPITAL_CHARGE(BigDecimal r12_TOTAL_CAPITAL_CHARGE) {
			R12_TOTAL_CAPITAL_CHARGE = r12_TOTAL_CAPITAL_CHARGE;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TOTAL_LONG_POS() {
			return R13_TOTAL_LONG_POS;
		}

		public void setR13_TOTAL_LONG_POS(BigDecimal r13_TOTAL_LONG_POS) {
			R13_TOTAL_LONG_POS = r13_TOTAL_LONG_POS;
		}

		public BigDecimal getR13_TOTAL_SHORT_POS() {
			return R13_TOTAL_SHORT_POS;
		}

		public void setR13_TOTAL_SHORT_POS(BigDecimal r13_TOTAL_SHORT_POS) {
			R13_TOTAL_SHORT_POS = r13_TOTAL_SHORT_POS;
		}

		public BigDecimal getR13_GROSS_OPEN_POS() {
			return R13_GROSS_OPEN_POS;
		}

		public void setR13_GROSS_OPEN_POS(BigDecimal r13_GROSS_OPEN_POS) {
			R13_GROSS_OPEN_POS = r13_GROSS_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_BASIS_RISK() {
			return R13_CHARGE_BASIS_RISK;
		}

		public void setR13_CHARGE_BASIS_RISK(BigDecimal r13_CHARGE_BASIS_RISK) {
			R13_CHARGE_BASIS_RISK = r13_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_BASIS_RISK() {
			return R13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR13_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r13_CAPITAL_CHARGE_BASIS_RISK) {
			R13_CAPITAL_CHARGE_BASIS_RISK = r13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_NET_OPEN_POS() {
			return R13_NET_OPEN_POS;
		}

		public void setR13_NET_OPEN_POS(BigDecimal r13_NET_OPEN_POS) {
			R13_NET_OPEN_POS = r13_NET_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_DIR_RISK() {
			return R13_CHARGE_DIR_RISK;
		}

		public void setR13_CHARGE_DIR_RISK(BigDecimal r13_CHARGE_DIR_RISK) {
			R13_CHARGE_DIR_RISK = r13_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_DIR_RISK() {
			return R13_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR13_CAPITAL_CHARGE_DIR_RISK(BigDecimal r13_CAPITAL_CHARGE_DIR_RISK) {
			R13_CAPITAL_CHARGE_DIR_RISK = r13_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_TOTAL_CAPITAL_CHARGE() {
			return R13_TOTAL_CAPITAL_CHARGE;
		}

		public void setR13_TOTAL_CAPITAL_CHARGE(BigDecimal r13_TOTAL_CAPITAL_CHARGE) {
			R13_TOTAL_CAPITAL_CHARGE = r13_TOTAL_CAPITAL_CHARGE;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TOTAL_LONG_POS() {
			return R14_TOTAL_LONG_POS;
		}

		public void setR14_TOTAL_LONG_POS(BigDecimal r14_TOTAL_LONG_POS) {
			R14_TOTAL_LONG_POS = r14_TOTAL_LONG_POS;
		}

		public BigDecimal getR14_TOTAL_SHORT_POS() {
			return R14_TOTAL_SHORT_POS;
		}

		public void setR14_TOTAL_SHORT_POS(BigDecimal r14_TOTAL_SHORT_POS) {
			R14_TOTAL_SHORT_POS = r14_TOTAL_SHORT_POS;
		}

		public BigDecimal getR14_GROSS_OPEN_POS() {
			return R14_GROSS_OPEN_POS;
		}

		public void setR14_GROSS_OPEN_POS(BigDecimal r14_GROSS_OPEN_POS) {
			R14_GROSS_OPEN_POS = r14_GROSS_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_BASIS_RISK() {
			return R14_CHARGE_BASIS_RISK;
		}

		public void setR14_CHARGE_BASIS_RISK(BigDecimal r14_CHARGE_BASIS_RISK) {
			R14_CHARGE_BASIS_RISK = r14_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_BASIS_RISK() {
			return R14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR14_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r14_CAPITAL_CHARGE_BASIS_RISK) {
			R14_CAPITAL_CHARGE_BASIS_RISK = r14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_NET_OPEN_POS() {
			return R14_NET_OPEN_POS;
		}

		public void setR14_NET_OPEN_POS(BigDecimal r14_NET_OPEN_POS) {
			R14_NET_OPEN_POS = r14_NET_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_DIR_RISK() {
			return R14_CHARGE_DIR_RISK;
		}

		public void setR14_CHARGE_DIR_RISK(BigDecimal r14_CHARGE_DIR_RISK) {
			R14_CHARGE_DIR_RISK = r14_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_DIR_RISK() {
			return R14_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR14_CAPITAL_CHARGE_DIR_RISK(BigDecimal r14_CAPITAL_CHARGE_DIR_RISK) {
			R14_CAPITAL_CHARGE_DIR_RISK = r14_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_TOTAL_CAPITAL_CHARGE() {
			return R14_TOTAL_CAPITAL_CHARGE;
		}

		public void setR14_TOTAL_CAPITAL_CHARGE(BigDecimal r14_TOTAL_CAPITAL_CHARGE) {
			R14_TOTAL_CAPITAL_CHARGE = r14_TOTAL_CAPITAL_CHARGE;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TOTAL_LONG_POS() {
			return R15_TOTAL_LONG_POS;
		}

		public void setR15_TOTAL_LONG_POS(BigDecimal r15_TOTAL_LONG_POS) {
			R15_TOTAL_LONG_POS = r15_TOTAL_LONG_POS;
		}

		public BigDecimal getR15_TOTAL_SHORT_POS() {
			return R15_TOTAL_SHORT_POS;
		}

		public void setR15_TOTAL_SHORT_POS(BigDecimal r15_TOTAL_SHORT_POS) {
			R15_TOTAL_SHORT_POS = r15_TOTAL_SHORT_POS;
		}

		public BigDecimal getR15_GROSS_OPEN_POS() {
			return R15_GROSS_OPEN_POS;
		}

		public void setR15_GROSS_OPEN_POS(BigDecimal r15_GROSS_OPEN_POS) {
			R15_GROSS_OPEN_POS = r15_GROSS_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_BASIS_RISK() {
			return R15_CHARGE_BASIS_RISK;
		}

		public void setR15_CHARGE_BASIS_RISK(BigDecimal r15_CHARGE_BASIS_RISK) {
			R15_CHARGE_BASIS_RISK = r15_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_BASIS_RISK() {
			return R15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR15_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r15_CAPITAL_CHARGE_BASIS_RISK) {
			R15_CAPITAL_CHARGE_BASIS_RISK = r15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_NET_OPEN_POS() {
			return R15_NET_OPEN_POS;
		}

		public void setR15_NET_OPEN_POS(BigDecimal r15_NET_OPEN_POS) {
			R15_NET_OPEN_POS = r15_NET_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_DIR_RISK() {
			return R15_CHARGE_DIR_RISK;
		}

		public void setR15_CHARGE_DIR_RISK(BigDecimal r15_CHARGE_DIR_RISK) {
			R15_CHARGE_DIR_RISK = r15_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_DIR_RISK() {
			return R15_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR15_CAPITAL_CHARGE_DIR_RISK(BigDecimal r15_CAPITAL_CHARGE_DIR_RISK) {
			R15_CAPITAL_CHARGE_DIR_RISK = r15_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_TOTAL_CAPITAL_CHARGE() {
			return R15_TOTAL_CAPITAL_CHARGE;
		}

		public void setR15_TOTAL_CAPITAL_CHARGE(BigDecimal r15_TOTAL_CAPITAL_CHARGE) {
			R15_TOTAL_CAPITAL_CHARGE = r15_TOTAL_CAPITAL_CHARGE;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TOTAL_LONG_POS() {
			return R16_TOTAL_LONG_POS;
		}

		public void setR16_TOTAL_LONG_POS(BigDecimal r16_TOTAL_LONG_POS) {
			R16_TOTAL_LONG_POS = r16_TOTAL_LONG_POS;
		}

		public BigDecimal getR16_TOTAL_SHORT_POS() {
			return R16_TOTAL_SHORT_POS;
		}

		public void setR16_TOTAL_SHORT_POS(BigDecimal r16_TOTAL_SHORT_POS) {
			R16_TOTAL_SHORT_POS = r16_TOTAL_SHORT_POS;
		}

		public BigDecimal getR16_GROSS_OPEN_POS() {
			return R16_GROSS_OPEN_POS;
		}

		public void setR16_GROSS_OPEN_POS(BigDecimal r16_GROSS_OPEN_POS) {
			R16_GROSS_OPEN_POS = r16_GROSS_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_BASIS_RISK() {
			return R16_CHARGE_BASIS_RISK;
		}

		public void setR16_CHARGE_BASIS_RISK(BigDecimal r16_CHARGE_BASIS_RISK) {
			R16_CHARGE_BASIS_RISK = r16_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_BASIS_RISK() {
			return R16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR16_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r16_CAPITAL_CHARGE_BASIS_RISK) {
			R16_CAPITAL_CHARGE_BASIS_RISK = r16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_NET_OPEN_POS() {
			return R16_NET_OPEN_POS;
		}

		public void setR16_NET_OPEN_POS(BigDecimal r16_NET_OPEN_POS) {
			R16_NET_OPEN_POS = r16_NET_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_DIR_RISK() {
			return R16_CHARGE_DIR_RISK;
		}

		public void setR16_CHARGE_DIR_RISK(BigDecimal r16_CHARGE_DIR_RISK) {
			R16_CHARGE_DIR_RISK = r16_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_DIR_RISK() {
			return R16_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR16_CAPITAL_CHARGE_DIR_RISK(BigDecimal r16_CAPITAL_CHARGE_DIR_RISK) {
			R16_CAPITAL_CHARGE_DIR_RISK = r16_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_TOTAL_CAPITAL_CHARGE() {
			return R16_TOTAL_CAPITAL_CHARGE;
		}

		public void setR16_TOTAL_CAPITAL_CHARGE(BigDecimal r16_TOTAL_CAPITAL_CHARGE) {
			R16_TOTAL_CAPITAL_CHARGE = r16_TOTAL_CAPITAL_CHARGE;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TOTAL_LONG_POS() {
			return R17_TOTAL_LONG_POS;
		}

		public void setR17_TOTAL_LONG_POS(BigDecimal r17_TOTAL_LONG_POS) {
			R17_TOTAL_LONG_POS = r17_TOTAL_LONG_POS;
		}

		public BigDecimal getR17_TOTAL_SHORT_POS() {
			return R17_TOTAL_SHORT_POS;
		}

		public void setR17_TOTAL_SHORT_POS(BigDecimal r17_TOTAL_SHORT_POS) {
			R17_TOTAL_SHORT_POS = r17_TOTAL_SHORT_POS;
		}

		public BigDecimal getR17_GROSS_OPEN_POS() {
			return R17_GROSS_OPEN_POS;
		}

		public void setR17_GROSS_OPEN_POS(BigDecimal r17_GROSS_OPEN_POS) {
			R17_GROSS_OPEN_POS = r17_GROSS_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_BASIS_RISK() {
			return R17_CHARGE_BASIS_RISK;
		}

		public void setR17_CHARGE_BASIS_RISK(BigDecimal r17_CHARGE_BASIS_RISK) {
			R17_CHARGE_BASIS_RISK = r17_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_BASIS_RISK() {
			return R17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR17_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r17_CAPITAL_CHARGE_BASIS_RISK) {
			R17_CAPITAL_CHARGE_BASIS_RISK = r17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_NET_OPEN_POS() {
			return R17_NET_OPEN_POS;
		}

		public void setR17_NET_OPEN_POS(BigDecimal r17_NET_OPEN_POS) {
			R17_NET_OPEN_POS = r17_NET_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_DIR_RISK() {
			return R17_CHARGE_DIR_RISK;
		}

		public void setR17_CHARGE_DIR_RISK(BigDecimal r17_CHARGE_DIR_RISK) {
			R17_CHARGE_DIR_RISK = r17_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_DIR_RISK() {
			return R17_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR17_CAPITAL_CHARGE_DIR_RISK(BigDecimal r17_CAPITAL_CHARGE_DIR_RISK) {
			R17_CAPITAL_CHARGE_DIR_RISK = r17_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_TOTAL_CAPITAL_CHARGE() {
			return R17_TOTAL_CAPITAL_CHARGE;
		}

		public void setR17_TOTAL_CAPITAL_CHARGE(BigDecimal r17_TOTAL_CAPITAL_CHARGE) {
			R17_TOTAL_CAPITAL_CHARGE = r17_TOTAL_CAPITAL_CHARGE;
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

		public M_CR_Resub_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	public static class M_CR_Resub_Detail_Entity {

		private String R10_PRODUCT;
		private BigDecimal R10_TOTAL_LONG_POS;
		private BigDecimal R10_TOTAL_SHORT_POS;
		private BigDecimal R10_GROSS_OPEN_POS;
		private BigDecimal R10_CHARGE_BASIS_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R10_NET_OPEN_POS;
		private BigDecimal R10_CHARGE_DIR_RISK;
		private BigDecimal R10_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R10_TOTAL_CAPITAL_CHARGE;
		private String R11_PRODUCT;
		private BigDecimal R11_TOTAL_LONG_POS;
		private BigDecimal R11_TOTAL_SHORT_POS;
		private BigDecimal R11_GROSS_OPEN_POS;
		private BigDecimal R11_CHARGE_BASIS_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R11_NET_OPEN_POS;
		private BigDecimal R11_CHARGE_DIR_RISK;
		private BigDecimal R11_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R11_TOTAL_CAPITAL_CHARGE;
		private String R12_PRODUCT;
		private BigDecimal R12_TOTAL_LONG_POS;
		private BigDecimal R12_TOTAL_SHORT_POS;
		private BigDecimal R12_GROSS_OPEN_POS;
		private BigDecimal R12_CHARGE_BASIS_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R12_NET_OPEN_POS;
		private BigDecimal R12_CHARGE_DIR_RISK;
		private BigDecimal R12_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R12_TOTAL_CAPITAL_CHARGE;
		private String R13_PRODUCT;
		private BigDecimal R13_TOTAL_LONG_POS;
		private BigDecimal R13_TOTAL_SHORT_POS;
		private BigDecimal R13_GROSS_OPEN_POS;
		private BigDecimal R13_CHARGE_BASIS_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R13_NET_OPEN_POS;
		private BigDecimal R13_CHARGE_DIR_RISK;
		private BigDecimal R13_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R13_TOTAL_CAPITAL_CHARGE;
		private String R14_PRODUCT;
		private BigDecimal R14_TOTAL_LONG_POS;
		private BigDecimal R14_TOTAL_SHORT_POS;
		private BigDecimal R14_GROSS_OPEN_POS;
		private BigDecimal R14_CHARGE_BASIS_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R14_NET_OPEN_POS;
		private BigDecimal R14_CHARGE_DIR_RISK;
		private BigDecimal R14_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R14_TOTAL_CAPITAL_CHARGE;
		private String R15_PRODUCT;
		private BigDecimal R15_TOTAL_LONG_POS;
		private BigDecimal R15_TOTAL_SHORT_POS;
		private BigDecimal R15_GROSS_OPEN_POS;
		private BigDecimal R15_CHARGE_BASIS_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R15_NET_OPEN_POS;
		private BigDecimal R15_CHARGE_DIR_RISK;
		private BigDecimal R15_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R15_TOTAL_CAPITAL_CHARGE;
		private String R16_PRODUCT;
		private BigDecimal R16_TOTAL_LONG_POS;
		private BigDecimal R16_TOTAL_SHORT_POS;
		private BigDecimal R16_GROSS_OPEN_POS;
		private BigDecimal R16_CHARGE_BASIS_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R16_NET_OPEN_POS;
		private BigDecimal R16_CHARGE_DIR_RISK;
		private BigDecimal R16_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R16_TOTAL_CAPITAL_CHARGE;
		private String R17_PRODUCT;
		private BigDecimal R17_TOTAL_LONG_POS;
		private BigDecimal R17_TOTAL_SHORT_POS;
		private BigDecimal R17_GROSS_OPEN_POS;
		private BigDecimal R17_CHARGE_BASIS_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_BASIS_RISK;
		private BigDecimal R17_NET_OPEN_POS;
		private BigDecimal R17_CHARGE_DIR_RISK;
		private BigDecimal R17_CAPITAL_CHARGE_DIR_RISK;
		private BigDecimal R17_TOTAL_CAPITAL_CHARGE;

		private Date reportDate;

		private BigDecimal reportVersion;

		private Date reportResubDate;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_TOTAL_LONG_POS() {
			return R10_TOTAL_LONG_POS;
		}

		public void setR10_TOTAL_LONG_POS(BigDecimal r10_TOTAL_LONG_POS) {
			R10_TOTAL_LONG_POS = r10_TOTAL_LONG_POS;
		}

		public BigDecimal getR10_TOTAL_SHORT_POS() {
			return R10_TOTAL_SHORT_POS;
		}

		public void setR10_TOTAL_SHORT_POS(BigDecimal r10_TOTAL_SHORT_POS) {
			R10_TOTAL_SHORT_POS = r10_TOTAL_SHORT_POS;
		}

		public BigDecimal getR10_GROSS_OPEN_POS() {
			return R10_GROSS_OPEN_POS;
		}

		public void setR10_GROSS_OPEN_POS(BigDecimal r10_GROSS_OPEN_POS) {
			R10_GROSS_OPEN_POS = r10_GROSS_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_BASIS_RISK() {
			return R10_CHARGE_BASIS_RISK;
		}

		public void setR10_CHARGE_BASIS_RISK(BigDecimal r10_CHARGE_BASIS_RISK) {
			R10_CHARGE_BASIS_RISK = r10_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_BASIS_RISK() {
			return R10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR10_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r10_CAPITAL_CHARGE_BASIS_RISK) {
			R10_CAPITAL_CHARGE_BASIS_RISK = r10_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR10_NET_OPEN_POS() {
			return R10_NET_OPEN_POS;
		}

		public void setR10_NET_OPEN_POS(BigDecimal r10_NET_OPEN_POS) {
			R10_NET_OPEN_POS = r10_NET_OPEN_POS;
		}

		public BigDecimal getR10_CHARGE_DIR_RISK() {
			return R10_CHARGE_DIR_RISK;
		}

		public void setR10_CHARGE_DIR_RISK(BigDecimal r10_CHARGE_DIR_RISK) {
			R10_CHARGE_DIR_RISK = r10_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_CAPITAL_CHARGE_DIR_RISK() {
			return R10_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR10_CAPITAL_CHARGE_DIR_RISK(BigDecimal r10_CAPITAL_CHARGE_DIR_RISK) {
			R10_CAPITAL_CHARGE_DIR_RISK = r10_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR10_TOTAL_CAPITAL_CHARGE() {
			return R10_TOTAL_CAPITAL_CHARGE;
		}

		public void setR10_TOTAL_CAPITAL_CHARGE(BigDecimal r10_TOTAL_CAPITAL_CHARGE) {
			R10_TOTAL_CAPITAL_CHARGE = r10_TOTAL_CAPITAL_CHARGE;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_TOTAL_LONG_POS() {
			return R11_TOTAL_LONG_POS;
		}

		public void setR11_TOTAL_LONG_POS(BigDecimal r11_TOTAL_LONG_POS) {
			R11_TOTAL_LONG_POS = r11_TOTAL_LONG_POS;
		}

		public BigDecimal getR11_TOTAL_SHORT_POS() {
			return R11_TOTAL_SHORT_POS;
		}

		public void setR11_TOTAL_SHORT_POS(BigDecimal r11_TOTAL_SHORT_POS) {
			R11_TOTAL_SHORT_POS = r11_TOTAL_SHORT_POS;
		}

		public BigDecimal getR11_GROSS_OPEN_POS() {
			return R11_GROSS_OPEN_POS;
		}

		public void setR11_GROSS_OPEN_POS(BigDecimal r11_GROSS_OPEN_POS) {
			R11_GROSS_OPEN_POS = r11_GROSS_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_BASIS_RISK() {
			return R11_CHARGE_BASIS_RISK;
		}

		public void setR11_CHARGE_BASIS_RISK(BigDecimal r11_CHARGE_BASIS_RISK) {
			R11_CHARGE_BASIS_RISK = r11_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_BASIS_RISK() {
			return R11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR11_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r11_CAPITAL_CHARGE_BASIS_RISK) {
			R11_CAPITAL_CHARGE_BASIS_RISK = r11_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR11_NET_OPEN_POS() {
			return R11_NET_OPEN_POS;
		}

		public void setR11_NET_OPEN_POS(BigDecimal r11_NET_OPEN_POS) {
			R11_NET_OPEN_POS = r11_NET_OPEN_POS;
		}

		public BigDecimal getR11_CHARGE_DIR_RISK() {
			return R11_CHARGE_DIR_RISK;
		}

		public void setR11_CHARGE_DIR_RISK(BigDecimal r11_CHARGE_DIR_RISK) {
			R11_CHARGE_DIR_RISK = r11_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_CAPITAL_CHARGE_DIR_RISK() {
			return R11_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR11_CAPITAL_CHARGE_DIR_RISK(BigDecimal r11_CAPITAL_CHARGE_DIR_RISK) {
			R11_CAPITAL_CHARGE_DIR_RISK = r11_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR11_TOTAL_CAPITAL_CHARGE() {
			return R11_TOTAL_CAPITAL_CHARGE;
		}

		public void setR11_TOTAL_CAPITAL_CHARGE(BigDecimal r11_TOTAL_CAPITAL_CHARGE) {
			R11_TOTAL_CAPITAL_CHARGE = r11_TOTAL_CAPITAL_CHARGE;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_TOTAL_LONG_POS() {
			return R12_TOTAL_LONG_POS;
		}

		public void setR12_TOTAL_LONG_POS(BigDecimal r12_TOTAL_LONG_POS) {
			R12_TOTAL_LONG_POS = r12_TOTAL_LONG_POS;
		}

		public BigDecimal getR12_TOTAL_SHORT_POS() {
			return R12_TOTAL_SHORT_POS;
		}

		public void setR12_TOTAL_SHORT_POS(BigDecimal r12_TOTAL_SHORT_POS) {
			R12_TOTAL_SHORT_POS = r12_TOTAL_SHORT_POS;
		}

		public BigDecimal getR12_GROSS_OPEN_POS() {
			return R12_GROSS_OPEN_POS;
		}

		public void setR12_GROSS_OPEN_POS(BigDecimal r12_GROSS_OPEN_POS) {
			R12_GROSS_OPEN_POS = r12_GROSS_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_BASIS_RISK() {
			return R12_CHARGE_BASIS_RISK;
		}

		public void setR12_CHARGE_BASIS_RISK(BigDecimal r12_CHARGE_BASIS_RISK) {
			R12_CHARGE_BASIS_RISK = r12_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_BASIS_RISK() {
			return R12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR12_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r12_CAPITAL_CHARGE_BASIS_RISK) {
			R12_CAPITAL_CHARGE_BASIS_RISK = r12_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR12_NET_OPEN_POS() {
			return R12_NET_OPEN_POS;
		}

		public void setR12_NET_OPEN_POS(BigDecimal r12_NET_OPEN_POS) {
			R12_NET_OPEN_POS = r12_NET_OPEN_POS;
		}

		public BigDecimal getR12_CHARGE_DIR_RISK() {
			return R12_CHARGE_DIR_RISK;
		}

		public void setR12_CHARGE_DIR_RISK(BigDecimal r12_CHARGE_DIR_RISK) {
			R12_CHARGE_DIR_RISK = r12_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_CAPITAL_CHARGE_DIR_RISK() {
			return R12_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR12_CAPITAL_CHARGE_DIR_RISK(BigDecimal r12_CAPITAL_CHARGE_DIR_RISK) {
			R12_CAPITAL_CHARGE_DIR_RISK = r12_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR12_TOTAL_CAPITAL_CHARGE() {
			return R12_TOTAL_CAPITAL_CHARGE;
		}

		public void setR12_TOTAL_CAPITAL_CHARGE(BigDecimal r12_TOTAL_CAPITAL_CHARGE) {
			R12_TOTAL_CAPITAL_CHARGE = r12_TOTAL_CAPITAL_CHARGE;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_TOTAL_LONG_POS() {
			return R13_TOTAL_LONG_POS;
		}

		public void setR13_TOTAL_LONG_POS(BigDecimal r13_TOTAL_LONG_POS) {
			R13_TOTAL_LONG_POS = r13_TOTAL_LONG_POS;
		}

		public BigDecimal getR13_TOTAL_SHORT_POS() {
			return R13_TOTAL_SHORT_POS;
		}

		public void setR13_TOTAL_SHORT_POS(BigDecimal r13_TOTAL_SHORT_POS) {
			R13_TOTAL_SHORT_POS = r13_TOTAL_SHORT_POS;
		}

		public BigDecimal getR13_GROSS_OPEN_POS() {
			return R13_GROSS_OPEN_POS;
		}

		public void setR13_GROSS_OPEN_POS(BigDecimal r13_GROSS_OPEN_POS) {
			R13_GROSS_OPEN_POS = r13_GROSS_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_BASIS_RISK() {
			return R13_CHARGE_BASIS_RISK;
		}

		public void setR13_CHARGE_BASIS_RISK(BigDecimal r13_CHARGE_BASIS_RISK) {
			R13_CHARGE_BASIS_RISK = r13_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_BASIS_RISK() {
			return R13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR13_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r13_CAPITAL_CHARGE_BASIS_RISK) {
			R13_CAPITAL_CHARGE_BASIS_RISK = r13_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR13_NET_OPEN_POS() {
			return R13_NET_OPEN_POS;
		}

		public void setR13_NET_OPEN_POS(BigDecimal r13_NET_OPEN_POS) {
			R13_NET_OPEN_POS = r13_NET_OPEN_POS;
		}

		public BigDecimal getR13_CHARGE_DIR_RISK() {
			return R13_CHARGE_DIR_RISK;
		}

		public void setR13_CHARGE_DIR_RISK(BigDecimal r13_CHARGE_DIR_RISK) {
			R13_CHARGE_DIR_RISK = r13_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_CAPITAL_CHARGE_DIR_RISK() {
			return R13_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR13_CAPITAL_CHARGE_DIR_RISK(BigDecimal r13_CAPITAL_CHARGE_DIR_RISK) {
			R13_CAPITAL_CHARGE_DIR_RISK = r13_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR13_TOTAL_CAPITAL_CHARGE() {
			return R13_TOTAL_CAPITAL_CHARGE;
		}

		public void setR13_TOTAL_CAPITAL_CHARGE(BigDecimal r13_TOTAL_CAPITAL_CHARGE) {
			R13_TOTAL_CAPITAL_CHARGE = r13_TOTAL_CAPITAL_CHARGE;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_TOTAL_LONG_POS() {
			return R14_TOTAL_LONG_POS;
		}

		public void setR14_TOTAL_LONG_POS(BigDecimal r14_TOTAL_LONG_POS) {
			R14_TOTAL_LONG_POS = r14_TOTAL_LONG_POS;
		}

		public BigDecimal getR14_TOTAL_SHORT_POS() {
			return R14_TOTAL_SHORT_POS;
		}

		public void setR14_TOTAL_SHORT_POS(BigDecimal r14_TOTAL_SHORT_POS) {
			R14_TOTAL_SHORT_POS = r14_TOTAL_SHORT_POS;
		}

		public BigDecimal getR14_GROSS_OPEN_POS() {
			return R14_GROSS_OPEN_POS;
		}

		public void setR14_GROSS_OPEN_POS(BigDecimal r14_GROSS_OPEN_POS) {
			R14_GROSS_OPEN_POS = r14_GROSS_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_BASIS_RISK() {
			return R14_CHARGE_BASIS_RISK;
		}

		public void setR14_CHARGE_BASIS_RISK(BigDecimal r14_CHARGE_BASIS_RISK) {
			R14_CHARGE_BASIS_RISK = r14_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_BASIS_RISK() {
			return R14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR14_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r14_CAPITAL_CHARGE_BASIS_RISK) {
			R14_CAPITAL_CHARGE_BASIS_RISK = r14_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR14_NET_OPEN_POS() {
			return R14_NET_OPEN_POS;
		}

		public void setR14_NET_OPEN_POS(BigDecimal r14_NET_OPEN_POS) {
			R14_NET_OPEN_POS = r14_NET_OPEN_POS;
		}

		public BigDecimal getR14_CHARGE_DIR_RISK() {
			return R14_CHARGE_DIR_RISK;
		}

		public void setR14_CHARGE_DIR_RISK(BigDecimal r14_CHARGE_DIR_RISK) {
			R14_CHARGE_DIR_RISK = r14_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_CAPITAL_CHARGE_DIR_RISK() {
			return R14_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR14_CAPITAL_CHARGE_DIR_RISK(BigDecimal r14_CAPITAL_CHARGE_DIR_RISK) {
			R14_CAPITAL_CHARGE_DIR_RISK = r14_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR14_TOTAL_CAPITAL_CHARGE() {
			return R14_TOTAL_CAPITAL_CHARGE;
		}

		public void setR14_TOTAL_CAPITAL_CHARGE(BigDecimal r14_TOTAL_CAPITAL_CHARGE) {
			R14_TOTAL_CAPITAL_CHARGE = r14_TOTAL_CAPITAL_CHARGE;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_TOTAL_LONG_POS() {
			return R15_TOTAL_LONG_POS;
		}

		public void setR15_TOTAL_LONG_POS(BigDecimal r15_TOTAL_LONG_POS) {
			R15_TOTAL_LONG_POS = r15_TOTAL_LONG_POS;
		}

		public BigDecimal getR15_TOTAL_SHORT_POS() {
			return R15_TOTAL_SHORT_POS;
		}

		public void setR15_TOTAL_SHORT_POS(BigDecimal r15_TOTAL_SHORT_POS) {
			R15_TOTAL_SHORT_POS = r15_TOTAL_SHORT_POS;
		}

		public BigDecimal getR15_GROSS_OPEN_POS() {
			return R15_GROSS_OPEN_POS;
		}

		public void setR15_GROSS_OPEN_POS(BigDecimal r15_GROSS_OPEN_POS) {
			R15_GROSS_OPEN_POS = r15_GROSS_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_BASIS_RISK() {
			return R15_CHARGE_BASIS_RISK;
		}

		public void setR15_CHARGE_BASIS_RISK(BigDecimal r15_CHARGE_BASIS_RISK) {
			R15_CHARGE_BASIS_RISK = r15_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_BASIS_RISK() {
			return R15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR15_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r15_CAPITAL_CHARGE_BASIS_RISK) {
			R15_CAPITAL_CHARGE_BASIS_RISK = r15_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR15_NET_OPEN_POS() {
			return R15_NET_OPEN_POS;
		}

		public void setR15_NET_OPEN_POS(BigDecimal r15_NET_OPEN_POS) {
			R15_NET_OPEN_POS = r15_NET_OPEN_POS;
		}

		public BigDecimal getR15_CHARGE_DIR_RISK() {
			return R15_CHARGE_DIR_RISK;
		}

		public void setR15_CHARGE_DIR_RISK(BigDecimal r15_CHARGE_DIR_RISK) {
			R15_CHARGE_DIR_RISK = r15_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_CAPITAL_CHARGE_DIR_RISK() {
			return R15_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR15_CAPITAL_CHARGE_DIR_RISK(BigDecimal r15_CAPITAL_CHARGE_DIR_RISK) {
			R15_CAPITAL_CHARGE_DIR_RISK = r15_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR15_TOTAL_CAPITAL_CHARGE() {
			return R15_TOTAL_CAPITAL_CHARGE;
		}

		public void setR15_TOTAL_CAPITAL_CHARGE(BigDecimal r15_TOTAL_CAPITAL_CHARGE) {
			R15_TOTAL_CAPITAL_CHARGE = r15_TOTAL_CAPITAL_CHARGE;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_TOTAL_LONG_POS() {
			return R16_TOTAL_LONG_POS;
		}

		public void setR16_TOTAL_LONG_POS(BigDecimal r16_TOTAL_LONG_POS) {
			R16_TOTAL_LONG_POS = r16_TOTAL_LONG_POS;
		}

		public BigDecimal getR16_TOTAL_SHORT_POS() {
			return R16_TOTAL_SHORT_POS;
		}

		public void setR16_TOTAL_SHORT_POS(BigDecimal r16_TOTAL_SHORT_POS) {
			R16_TOTAL_SHORT_POS = r16_TOTAL_SHORT_POS;
		}

		public BigDecimal getR16_GROSS_OPEN_POS() {
			return R16_GROSS_OPEN_POS;
		}

		public void setR16_GROSS_OPEN_POS(BigDecimal r16_GROSS_OPEN_POS) {
			R16_GROSS_OPEN_POS = r16_GROSS_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_BASIS_RISK() {
			return R16_CHARGE_BASIS_RISK;
		}

		public void setR16_CHARGE_BASIS_RISK(BigDecimal r16_CHARGE_BASIS_RISK) {
			R16_CHARGE_BASIS_RISK = r16_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_BASIS_RISK() {
			return R16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR16_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r16_CAPITAL_CHARGE_BASIS_RISK) {
			R16_CAPITAL_CHARGE_BASIS_RISK = r16_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR16_NET_OPEN_POS() {
			return R16_NET_OPEN_POS;
		}

		public void setR16_NET_OPEN_POS(BigDecimal r16_NET_OPEN_POS) {
			R16_NET_OPEN_POS = r16_NET_OPEN_POS;
		}

		public BigDecimal getR16_CHARGE_DIR_RISK() {
			return R16_CHARGE_DIR_RISK;
		}

		public void setR16_CHARGE_DIR_RISK(BigDecimal r16_CHARGE_DIR_RISK) {
			R16_CHARGE_DIR_RISK = r16_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_CAPITAL_CHARGE_DIR_RISK() {
			return R16_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR16_CAPITAL_CHARGE_DIR_RISK(BigDecimal r16_CAPITAL_CHARGE_DIR_RISK) {
			R16_CAPITAL_CHARGE_DIR_RISK = r16_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR16_TOTAL_CAPITAL_CHARGE() {
			return R16_TOTAL_CAPITAL_CHARGE;
		}

		public void setR16_TOTAL_CAPITAL_CHARGE(BigDecimal r16_TOTAL_CAPITAL_CHARGE) {
			R16_TOTAL_CAPITAL_CHARGE = r16_TOTAL_CAPITAL_CHARGE;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_TOTAL_LONG_POS() {
			return R17_TOTAL_LONG_POS;
		}

		public void setR17_TOTAL_LONG_POS(BigDecimal r17_TOTAL_LONG_POS) {
			R17_TOTAL_LONG_POS = r17_TOTAL_LONG_POS;
		}

		public BigDecimal getR17_TOTAL_SHORT_POS() {
			return R17_TOTAL_SHORT_POS;
		}

		public void setR17_TOTAL_SHORT_POS(BigDecimal r17_TOTAL_SHORT_POS) {
			R17_TOTAL_SHORT_POS = r17_TOTAL_SHORT_POS;
		}

		public BigDecimal getR17_GROSS_OPEN_POS() {
			return R17_GROSS_OPEN_POS;
		}

		public void setR17_GROSS_OPEN_POS(BigDecimal r17_GROSS_OPEN_POS) {
			R17_GROSS_OPEN_POS = r17_GROSS_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_BASIS_RISK() {
			return R17_CHARGE_BASIS_RISK;
		}

		public void setR17_CHARGE_BASIS_RISK(BigDecimal r17_CHARGE_BASIS_RISK) {
			R17_CHARGE_BASIS_RISK = r17_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_BASIS_RISK() {
			return R17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public void setR17_CAPITAL_CHARGE_BASIS_RISK(BigDecimal r17_CAPITAL_CHARGE_BASIS_RISK) {
			R17_CAPITAL_CHARGE_BASIS_RISK = r17_CAPITAL_CHARGE_BASIS_RISK;
		}

		public BigDecimal getR17_NET_OPEN_POS() {
			return R17_NET_OPEN_POS;
		}

		public void setR17_NET_OPEN_POS(BigDecimal r17_NET_OPEN_POS) {
			R17_NET_OPEN_POS = r17_NET_OPEN_POS;
		}

		public BigDecimal getR17_CHARGE_DIR_RISK() {
			return R17_CHARGE_DIR_RISK;
		}

		public void setR17_CHARGE_DIR_RISK(BigDecimal r17_CHARGE_DIR_RISK) {
			R17_CHARGE_DIR_RISK = r17_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_CAPITAL_CHARGE_DIR_RISK() {
			return R17_CAPITAL_CHARGE_DIR_RISK;
		}

		public void setR17_CAPITAL_CHARGE_DIR_RISK(BigDecimal r17_CAPITAL_CHARGE_DIR_RISK) {
			R17_CAPITAL_CHARGE_DIR_RISK = r17_CAPITAL_CHARGE_DIR_RISK;
		}

		public BigDecimal getR17_TOTAL_CAPITAL_CHARGE() {
			return R17_TOTAL_CAPITAL_CHARGE;
		}

		public void setR17_TOTAL_CAPITAL_CHARGE(BigDecimal r17_TOTAL_CAPITAL_CHARGE) {
			R17_TOTAL_CAPITAL_CHARGE = r17_TOTAL_CAPITAL_CHARGE;
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

		public M_CR_Resub_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

}
