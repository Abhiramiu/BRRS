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

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
/*import org.apache.poi.ss.usermodel.FillPatternType;*/
import org.apache.poi.ss.usermodel.Font;
/*import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;*/
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
/*import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/
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

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.format.annotation.DateTimeFormat;

import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service

public class BRRS_M_GMIRT_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_GMIRT_ReportService.class);

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
	// Method to display view page for M_GMIRT report
	// ------------------------------
	public ModelAndView getM_GMIRTView(String reportId, String fromdate, String todate, String currency, String dtltype,
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

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ---------- CASE 1: ARCHIVAL ----------
			if (type.equals("ARCHIVAL") & version != null) {

				String sql = "SELECT * FROM BRRS_M_GMIRT_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
				List<M_GMIRT_Archival_Summary_Entity> T1Master = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Archival_Summary_Entity.class), d1, version);

				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);

			}
			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				String sql = "SELECT * FROM BRRS_M_GMIRT_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
				List<M_GMIRT_RESUB_Summary_Entity> T1Master = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_RESUB_Summary_Entity.class), d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}
			// ---------- CASE 3: NORMAL ----------
			else {

				String sql = "SELECT * FROM BRRS_M_GMIRT_SUMMARYTABLE WHERE REPORT_DATE = ?";
				List<M_GMIRT_Summary_Entity> T1Master = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Summary_Entity.class), dateformat.parse(todate));
				
				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");

			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					
					String sql = "SELECT * FROM BRRS_M_GMIRT_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
					List<M_GMIRT_Archival_Detail_Entity> T1Master = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Archival_Detail_Entity.class), d1, version);
					
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					String sql = "SELECT * FROM BRRS_M_GMIRT_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
					List<M_GMIRT_RESUB_Detail_Entity> T1Master = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_RESUB_Detail_Entity.class), d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					String sql = "SELECT * FROM BRRS_M_GMIRT_DETAILTABLE WHERE REPORT_DATE = ?";
					List<M_GMIRT_Detail_Entity> T1Master = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Detail_Entity.class), dateformat.parse(todate));
					
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_GMIRT");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}


	// ------------------------------
	// Method to update summary report fields
	// ------------------------------
	public void updateReport(M_GMIRT_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		M_GMIRT_Summary_Entity existing = findSummaryById(updatedEntity.getReport_date());
		if (existing == null) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		try {
			// 1️⃣ Loop from R11 to R23 and copy fields

			for (int i = 9; i <= 12; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "currency", "pula", "usd", "zar", "gbp", "euro", "jpy", "rupee", "renminbi",
						"other", "tot_cap_req" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_GMIRT_Summary_Entity.class.getMethod(getterName);
						Method setter = M_GMIRT_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue; // skip field that does not exist
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		saveEntity(existing, "BRRS_M_GMIRT_SUMMARYTABLE");
	}

	// ------------------------------
	// Method to update detail report fields
	// ------------------------------
	public void updateDetail(M_GMIRT_Detail_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		M_GMIRT_Detail_Entity existing = findDetailById(updatedEntity.getReport_date());
		if (existing == null) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		try {
			// 1️⃣ Loop from R11 to R23 and copy fields

			for (int i = 9; i <= 12; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "currency", "pula", "usd", "zar", "gbp", "euro", "jpy", "rupee", "renminbi",
						"other", "tot_cap_req" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_GMIRT_Detail_Entity.class.getMethod(getterName);
						Method setter = M_GMIRT_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue; // skip field that does not exist
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// 3️⃣ Save updated entity
		saveEntity(existing, "BRRS_M_GMIRT_DETAILTABLE");
	}

	// ------------------------------
	// Method to perform resubmission update for M_GMIRT report
	// ------------------------------
	public void updateResubReport(M_GMIRT_RESUB_Summary_Entity updatedEntity) {


		Date reportDate = updatedEntity.getReport_date();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = findMaxVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_GMIRT_RESUB_Summary_Entity resubSummary = new M_GMIRT_RESUB_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "report_date", "report_version", "reportResubDate");

		resubSummary.setReport_date(reportDate);
		resubSummary.setReport_version(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_GMIRT_RESUB_Detail_Entity resubDetail = new M_GMIRT_RESUB_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "report_date", "report_version", "reportResubDate");

		resubDetail.setReport_date(reportDate);
		resubDetail.setReport_version(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_GMIRT_Archival_Summary_Entity archSummary = new M_GMIRT_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "report_date", "report_version", "reportResubDate");

		archSummary.setReport_date(reportDate);
		archSummary.setReport_version(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_GMIRT_Archival_Detail_Entity archDetail = new M_GMIRT_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "report_date", "report_version", "reportResubDate");

		archDetail.setReport_date(reportDate);
		archDetail.setReport_version(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		saveEntity(resubSummary, "BRRS_M_GMIRT_RESUB_SUMMARYTABLE");
		saveEntity(resubDetail, "BRRS_M_GMIRT_RESUB_DETAILTABLE");

		saveEntity(archSummary, "BRRS_M_GMIRT_ARCHIVALTABLE_SUMMARY");
		saveEntity(archDetail, "BRRS_M_GMIRT_ARCHIVALTABLE_DETAIL");
	}

	// ------------------------------
	// Method to fetch resubmission versions list
	// ------------------------------
	public List<Object[]> getM_GMIRTResub() {

		List<Object[]> resubList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM BRRS_M_GMIRT_RESUB_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL";
			List<M_GMIRT_RESUB_Summary_Entity> latestArchivalList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_RESUB_Summary_Entity.class));

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_GMIRT_RESUB_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_GMIRT Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// ------------------------------
	// Method to fetch archival versions list
	// ------------------------------
	public List<Object[]> getM_GMIRTArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			String sql = "SELECT * FROM BRRS_M_GMIRT_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
			List<M_GMIRT_Archival_Summary_Entity> repoData = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Archival_Summary_Entity.class));

			if (repoData != null && !repoData.isEmpty()) {
				for (M_GMIRT_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version(), 
							 entity.getReportResubDate()
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_GMIRT_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_GMIRT  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// ------------------------------
	// Method to query a summary record by report date
	// ------------------------------
	private M_GMIRT_Summary_Entity findSummaryById(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_GMIRT_SUMMARYTABLE WHERE REPORT_DATE = ?";
		List<M_GMIRT_Summary_Entity> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Summary_Entity.class), reportDate);
		return list.isEmpty() ? null : list.get(0);
	}

	// ------------------------------
	// Method to query a detail record by report date
	// ------------------------------
	private M_GMIRT_Detail_Entity findDetailById(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_GMIRT_DETAILTABLE WHERE REPORT_DATE = ?";
		List<M_GMIRT_Detail_Entity> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Detail_Entity.class), reportDate);
		return list.isEmpty() ? null : list.get(0);
	}

	// ------------------------------
	// Method to find the maximum version from resub table for a date
	// ------------------------------
	private BigDecimal findMaxVersion(Date date) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_GMIRT_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, BigDecimal.class, date);
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
			throw new RuntimeException("Error executing saveEntity on " + tableName, e);
		}
	}

	//Normal Format Excel
	public byte[] getM_GMIRTExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= DOWNLOAD DETAILS =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");
		
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return getExcelM_GMIRTARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return getExcelM_GMIRTResub(filename, reportId, fromdate, todate, currency, dtltype, type, format, version);
				
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}
		 else {
			 if ("email".equalsIgnoreCase(format) && version == null) {
					logger.info("Got format as Email");
					logger.info("Service: Generating Email report for version {}", version);
					return BRRS_M_GMIRTEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {


		

		String sql = "SELECT * FROM BRRS_M_GMIRT_SUMMARYTABLE WHERE REPORT_DATE = ?";
		List<M_GMIRT_Summary_Entity> dataList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Summary_Entity.class), dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			       // Column 3 = Excel column D
			       Cell dateCell = dateRow.getCell(3);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(3);
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
			   
			  
			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GMIRT SUMMARY", null, "BRRS_M_GMIRT_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}
	 }}

	//Normal Email Excel
	public byte[] BRRS_M_GMIRTEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// Email check

		// Fetch data

		String sql = "SELECT * FROM BRRS_M_GMIRT_SUMMARYTABLE WHERE REPORT_DATE = ?";
		List<M_GMIRT_Summary_Entity> dataList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Summary_Entity.class), dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			       // Column 3 = Excel column D
			       Cell dateCell = dateRow.getCell(3);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(3);
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
			   
			
			
			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GMIRT EMAIL SUMMARY", null, "BRRS_M_GMIRT_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	//Archival Format Excel
	public byte[] getExcelM_GMIRTARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format,BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");
		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_GMIRTARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} 
		String sql = "SELECT * FROM BRRS_M_GMIRT_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_GMIRT_Archival_Summary_Entity> dataList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Archival_Summary_Entity.class), dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			       // Column 3 = Excel column D
			       Cell dateCell = dateRow.getCell(3);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(3);
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
			   
			 
			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GMIRT ARCHIVAL SUMMARY", null, "BRRS_M_GMIRT_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	//Archival Email Excel
	public byte[] BRRS_M_GMIRTARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		String sql = "SELECT * FROM BRRS_M_GMIRT_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_GMIRT_Archival_Summary_Entity> dataList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_Archival_Summary_Entity.class), dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			       // Column 3 = Excel column D
			       Cell dateCell = dateRow.getCell(3);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(3);
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
			   
			 
			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GMIRT EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_GMIRT_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	//RESUB Format Excel
	public byte[] getExcelM_GMIRTResub(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_GMIRTResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
		}
	}

		String sql = "SELECT * FROM BRRS_M_GMIRT_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_GMIRT_RESUB_Summary_Entity> dataList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_RESUB_Summary_Entity.class), dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

			       // Column 3 = Excel column D
			       Cell dateCell = dateRow.getCell(3);

			       if (dateCell == null) {
			           dateCell = dateRow.createCell(3);
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
			   
			 
			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_GMIRT_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row9
					// Column E
					Cell cellE = row.createCell(4);
					if (record.getR9_pula() != null) {
						cellE.setCellValue(record.getR9_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row9
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR9_usd() != null) {
						cellF.setCellValue(record.getR9_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row9
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR9_zar() != null) {
						cellG.setCellValue(record.getR9_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row9
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR9_gbp() != null) {
						cellH.setCellValue(record.getR9_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row9
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR9_euro() != null) {
						cellI.setCellValue(record.getR9_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row9
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR9_jpy() != null) {
						cellJ.setCellValue(record.getR9_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column K
					Cell cellK = row.createCell(10);
					if (record.getR9_rupee() != null) {
						cellK.setCellValue(record.getR9_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row9
					// Column L
					Cell cellL = row.createCell(11);
					if (record.getR9_renminbi() != null) {
						cellL.setCellValue(record.getR9_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row9
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR9_other() != null) {
						cellM.setCellValue(record.getR9_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row9
					// Column N
					Cell cellN = row.createCell(13);
					if (record.getR9_tot_cap_req() != null) {
						cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row10

					// Column E
					row = sheet.getRow(9);
					cellE = row.createCell(4);
					if (record.getR10_pula() != null) {
						cellE.setCellValue(record.getR10_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row10
					// Column F
					cellF = row.createCell(5);
					if (record.getR10_usd() != null) {
						cellF.setCellValue(record.getR10_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cellG = row.createCell(6);
					if (record.getR10_zar() != null) {
						cellG.setCellValue(record.getR10_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cellH = row.createCell(7);
					if (record.getR10_gbp() != null) {
						cellH.setCellValue(record.getR10_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row10
					// Column I
					cellI = row.createCell(8);
					if (record.getR10_euro() != null) {
						cellI.setCellValue(record.getR10_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row10
					// Column J
					cellJ = row.createCell(9);
					if (record.getR10_jpy() != null) {
						cellJ.setCellValue(record.getR10_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row10
					// Column K
					cellK = row.createCell(10);
					if (record.getR10_rupee() != null) {
						cellK.setCellValue(record.getR10_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row10
					// Column L
					cellL = row.createCell(11);
					if (record.getR10_renminbi() != null) {
						cellL.setCellValue(record.getR10_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row10
					// Column M
					cellM = row.createCell(12);
					if (record.getR10_other() != null) {
						cellM.setCellValue(record.getR10_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row10
					// Column N
					cellN = row.createCell(13);
					if (record.getR10_tot_cap_req() != null) {
						cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// row11
					// Column E
					row = sheet.getRow(10);
					cellE = row.createCell(4);
					if (record.getR11_pula() != null) {
						cellE.setCellValue(record.getR11_pula().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cellF = row.createCell(5);
					if (record.getR11_usd() != null) {
						cellF.setCellValue(record.getR11_usd().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cellG = row.createCell(6);
					if (record.getR11_zar() != null) {
						cellG.setCellValue(record.getR11_zar().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cellH = row.createCell(7);
					if (record.getR11_gbp() != null) {
						cellH.setCellValue(record.getR11_gbp().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// row11
					// Column I
					cellI = row.createCell(8);
					if (record.getR11_euro() != null) {
						cellI.setCellValue(record.getR11_euro().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					cellJ = row.createCell(9);
					if (record.getR11_jpy() != null) {
						cellJ.setCellValue(record.getR11_jpy().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column K
					cellK = row.createCell(10);
					if (record.getR11_rupee() != null) {
						cellK.setCellValue(record.getR11_rupee().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// row11
					// Column L
					cellL = row.createCell(11);
					if (record.getR11_renminbi() != null) {
						cellL.setCellValue(record.getR11_renminbi().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// row11
					// Column M
					cellM = row.createCell(12);
					if (record.getR11_other() != null) {
						cellM.setCellValue(record.getR11_other().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// row11
					// Column N
					cellN = row.createCell(13);
					if (record.getR11_tot_cap_req() != null) {
						cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GMIRT RESUB SUMMARY", null, "BRRS_M_GMIRT_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	//RESUB Email Excel
	public byte[] BRRS_M_GMIRTResubEmailExcel(String filename, String reportId, String fromdate, String todate,
				String currency, String dtltype, String type, BigDecimal version) throws Exception {

			logger.info("Service: Starting Excel generation process in memory.");

			String sql = "SELECT * FROM BRRS_M_GMIRT_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			List<M_GMIRT_RESUB_Summary_Entity> dataList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(M_GMIRT_RESUB_Summary_Entity.class), dateformat.parse(todate), version);

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for M_GMIRT report. Returning empty result.");
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

				       // Column 3 = Excel column D
				       Cell dateCell = dateRow.getCell(3);

				       if (dateCell == null) {
				           dateCell = dateRow.createCell(3);
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
				   
				 
				int startRow = 8;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_GMIRT_RESUB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// row9
						// Column E
						Cell cellE = row.createCell(4);
						if (record.getR9_pula() != null) {
							cellE.setCellValue(record.getR9_pula().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// row9
						// Column F
						Cell cellF = row.createCell(5);
						if (record.getR9_usd() != null) {
							cellF.setCellValue(record.getR9_usd().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row9
						// Column G
						Cell cellG = row.createCell(6);
						if (record.getR9_zar() != null) {
							cellG.setCellValue(record.getR9_zar().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row9
						// Column H
						Cell cellH = row.createCell(7);
						if (record.getR9_gbp() != null) {
							cellH.setCellValue(record.getR9_gbp().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row9
						// Column I
						Cell cellI = row.createCell(8);
						if (record.getR9_euro() != null) {
							cellI.setCellValue(record.getR9_euro().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row9
						// Column J
						Cell cellJ = row.createCell(9);
						if (record.getR9_jpy() != null) {
							cellJ.setCellValue(record.getR9_jpy().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row9
						// Column K
						Cell cellK = row.createCell(10);
						if (record.getR9_rupee() != null) {
							cellK.setCellValue(record.getR9_rupee().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row9
						// Column L
						Cell cellL = row.createCell(11);
						if (record.getR9_renminbi() != null) {
							cellL.setCellValue(record.getR9_renminbi().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// row9
						// Column M
						Cell cellM = row.createCell(12);
						if (record.getR9_other() != null) {
							cellM.setCellValue(record.getR9_other().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// row9
						// Column N
						Cell cellN = row.createCell(13);
						if (record.getR9_tot_cap_req() != null) {
							cellN.setCellValue(record.getR9_tot_cap_req().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// row10

						// Column E
						row = sheet.getRow(9);
						cellE = row.createCell(4);
						if (record.getR10_pula() != null) {
							cellE.setCellValue(record.getR10_pula().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// row10
						// Column F
						cellF = row.createCell(5);
						if (record.getR10_usd() != null) {
							cellF.setCellValue(record.getR10_usd().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row10
						// Column G
						cellG = row.createCell(6);
						if (record.getR10_zar() != null) {
							cellG.setCellValue(record.getR10_zar().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row10
						// Column H
						cellH = row.createCell(7);
						if (record.getR10_gbp() != null) {
							cellH.setCellValue(record.getR10_gbp().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row10
						// Column I
						cellI = row.createCell(8);
						if (record.getR10_euro() != null) {
							cellI.setCellValue(record.getR10_euro().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row10
						// Column J
						cellJ = row.createCell(9);
						if (record.getR10_jpy() != null) {
							cellJ.setCellValue(record.getR10_jpy().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row10
						// Column K
						cellK = row.createCell(10);
						if (record.getR10_rupee() != null) {
							cellK.setCellValue(record.getR10_rupee().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row10
						// Column L
						cellL = row.createCell(11);
						if (record.getR10_renminbi() != null) {
							cellL.setCellValue(record.getR10_renminbi().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// row10
						// Column M
						cellM = row.createCell(12);
						if (record.getR10_other() != null) {
							cellM.setCellValue(record.getR10_other().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// row10
						// Column N
						cellN = row.createCell(13);
						if (record.getR10_tot_cap_req() != null) {
							cellN.setCellValue(record.getR10_tot_cap_req().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
						}

						// row11
						// Column E
						row = sheet.getRow(10);
						cellE = row.createCell(4);
						if (record.getR11_pula() != null) {
							cellE.setCellValue(record.getR11_pula().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}

						// row11
						// Column F
						cellF = row.createCell(5);
						if (record.getR11_usd() != null) {
							cellF.setCellValue(record.getR11_usd().doubleValue());
							cellF.setCellStyle(numberStyle);
						} else {
							cellF.setCellValue("");
							cellF.setCellStyle(textStyle);
						}

						// row11
						// Column G
						cellG = row.createCell(6);
						if (record.getR11_zar() != null) {
							cellG.setCellValue(record.getR11_zar().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

						// row11
						// Column H
						cellH = row.createCell(7);
						if (record.getR11_gbp() != null) {
							cellH.setCellValue(record.getR11_gbp().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
						}

						// row11
						// Column I
						cellI = row.createCell(8);
						if (record.getR11_euro() != null) {
							cellI.setCellValue(record.getR11_euro().doubleValue());
							cellI.setCellStyle(numberStyle);
						} else {
							cellI.setCellValue("");
							cellI.setCellStyle(textStyle);
						}

						// row11
						// Column J
						cellJ = row.createCell(9);
						if (record.getR11_jpy() != null) {
							cellJ.setCellValue(record.getR11_jpy().doubleValue());
							cellJ.setCellStyle(numberStyle);
						} else {
							cellJ.setCellValue("");
							cellJ.setCellStyle(textStyle);
						}

						// row11
						// Column K
						cellK = row.createCell(10);
						if (record.getR11_rupee() != null) {
							cellK.setCellValue(record.getR11_rupee().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}

						// row11
						// Column L
						cellL = row.createCell(11);
						if (record.getR11_renminbi() != null) {
							cellL.setCellValue(record.getR11_renminbi().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}

						// row11
						// Column M
						cellM = row.createCell(12);
						if (record.getR11_other() != null) {
							cellM.setCellValue(record.getR11_other().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}

						// row11
						// Column N
						cellN = row.createCell(13);
						if (record.getR11_tot_cap_req() != null) {
							cellN.setCellValue(record.getR11_tot_cap_req().doubleValue());
							cellN.setCellStyle(numberStyle);
						} else {
							cellN.setCellValue("");
							cellN.setCellStyle(textStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_GMIRT EMAIL RESUB SUMMARY", null, "BRRS_M_GMIRT_RESUB_SUMMARYTABLE");
				}
				return out.toByteArray();
			}

		}



	// ------------------------------
	// Primary key class for M_GMIRT archival and resubmission entities
	// ------------------------------
	public static class M_GMIRT_PK implements Serializable {
		private Date report_date;
		private BigDecimal report_version;

		public M_GMIRT_PK() {}

		public M_GMIRT_PK(Date report_date, BigDecimal report_version) {
			this.report_date = report_date;
			this.report_version = report_version;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof M_GMIRT_PK)) return false;
			M_GMIRT_PK that = (M_GMIRT_PK) o;
			return Objects.equals(report_date, that.report_date) &&
			       Objects.equals(report_version, that.report_version);
		}

		@Override
		public int hashCode() {
			return Objects.hash(report_date, report_version);
		}

		public Date getReport_date() { return report_date; }
		public void setReport_date(Date report_date) { this.report_date = report_date; }

		public BigDecimal getReport_version() { return report_version; }
		public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
	}

	// ------------------------------
	// Entity representing M_GMIRT_Summary_Entity
	// ------------------------------
	public static class M_GMIRT_Summary_Entity implements Serializable {

	private String	r9_currency;
	private BigDecimal	r9_pula;
	private BigDecimal	r9_usd;
	private BigDecimal	r9_zar;
	private BigDecimal	r9_gbp;
	private BigDecimal	r9_euro;
	private BigDecimal	r9_jpy;
	private BigDecimal	r9_rupee;
	private BigDecimal	r9_renminbi;
	private BigDecimal	r9_other;
	private BigDecimal	r9_tot_cap_req;

	private String	r10_currency;
	private BigDecimal	r10_pula;
	private BigDecimal	r10_usd;
	private BigDecimal	r10_zar;
	private BigDecimal	r10_gbp;
	private BigDecimal	r10_euro;
	private BigDecimal	r10_jpy;
	private BigDecimal	r10_rupee;
	private BigDecimal	r10_renminbi;
	private BigDecimal	r10_other;
	private BigDecimal	r10_tot_cap_req;

	private String	r11_currency;
	private BigDecimal	r11_pula;
	private BigDecimal	r11_usd;
	private BigDecimal	r11_zar;
	private BigDecimal	r11_gbp;
	private BigDecimal	r11_euro;
	private BigDecimal	r11_jpy;
	private BigDecimal	r11_rupee;
	private BigDecimal	r11_renminbi;
	private BigDecimal	r11_other;
	private BigDecimal	r11_tot_cap_req;

	private String	r12_currency;
	private BigDecimal	r12_pula;
	private BigDecimal	r12_usd;
	private BigDecimal	r12_zar;
	private BigDecimal	r12_gbp;
	private BigDecimal	r12_euro;
	private BigDecimal	r12_jpy;
	private BigDecimal	r12_rupee;
	private BigDecimal	r12_renminbi;
	private BigDecimal	r12_other;
	private BigDecimal	r12_tot_cap_req;

	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;

	public M_GMIRT_Summary_Entity() { super(); }

	public String getR9_currency() { return r9_currency; }
	public void setR9_currency(String r9_currency) { this.r9_currency = r9_currency; }
	public BigDecimal getR9_pula() { return r9_pula; }
	public void setR9_pula(BigDecimal r9_pula) { this.r9_pula = r9_pula; }
	public BigDecimal getR9_usd() { return r9_usd; }
	public void setR9_usd(BigDecimal r9_usd) { this.r9_usd = r9_usd; }
	public BigDecimal getR9_zar() { return r9_zar; }
	public void setR9_zar(BigDecimal r9_zar) { this.r9_zar = r9_zar; }
	public BigDecimal getR9_gbp() { return r9_gbp; }
	public void setR9_gbp(BigDecimal r9_gbp) { this.r9_gbp = r9_gbp; }
	public BigDecimal getR9_euro() { return r9_euro; }
	public void setR9_euro(BigDecimal r9_euro) { this.r9_euro = r9_euro; }
	public BigDecimal getR9_jpy() { return r9_jpy; }
	public void setR9_jpy(BigDecimal r9_jpy) { this.r9_jpy = r9_jpy; }
	public BigDecimal getR9_rupee() { return r9_rupee; }
	public void setR9_rupee(BigDecimal r9_rupee) { this.r9_rupee = r9_rupee; }
	public BigDecimal getR9_renminbi() { return r9_renminbi; }
	public void setR9_renminbi(BigDecimal r9_renminbi) { this.r9_renminbi = r9_renminbi; }
	public BigDecimal getR9_other() { return r9_other; }
	public void setR9_other(BigDecimal r9_other) { this.r9_other = r9_other; }
	public BigDecimal getR9_tot_cap_req() { return r9_tot_cap_req; }
	public void setR9_tot_cap_req(BigDecimal r9_tot_cap_req) { this.r9_tot_cap_req = r9_tot_cap_req; }

	public String getR10_currency() { return r10_currency; }
	public void setR10_currency(String r10_currency) { this.r10_currency = r10_currency; }
	public BigDecimal getR10_pula() { return r10_pula; }
	public void setR10_pula(BigDecimal r10_pula) { this.r10_pula = r10_pula; }
	public BigDecimal getR10_usd() { return r10_usd; }
	public void setR10_usd(BigDecimal r10_usd) { this.r10_usd = r10_usd; }
	public BigDecimal getR10_zar() { return r10_zar; }
	public void setR10_zar(BigDecimal r10_zar) { this.r10_zar = r10_zar; }
	public BigDecimal getR10_gbp() { return r10_gbp; }
	public void setR10_gbp(BigDecimal r10_gbp) { this.r10_gbp = r10_gbp; }
	public BigDecimal getR10_euro() { return r10_euro; }
	public void setR10_euro(BigDecimal r10_euro) { this.r10_euro = r10_euro; }
	public BigDecimal getR10_jpy() { return r10_jpy; }
	public void setR10_jpy(BigDecimal r10_jpy) { this.r10_jpy = r10_jpy; }
	public BigDecimal getR10_rupee() { return r10_rupee; }
	public void setR10_rupee(BigDecimal r10_rupee) { this.r10_rupee = r10_rupee; }
	public BigDecimal getR10_renminbi() { return r10_renminbi; }
	public void setR10_renminbi(BigDecimal r10_renminbi) { this.r10_renminbi = r10_renminbi; }
	public BigDecimal getR10_other() { return r10_other; }
	public void setR10_other(BigDecimal r10_other) { this.r10_other = r10_other; }
	public BigDecimal getR10_tot_cap_req() { return r10_tot_cap_req; }
	public void setR10_tot_cap_req(BigDecimal r10_tot_cap_req) { this.r10_tot_cap_req = r10_tot_cap_req; }

	public String getR11_currency() { return r11_currency; }
	public void setR11_currency(String r11_currency) { this.r11_currency = r11_currency; }
	public BigDecimal getR11_pula() { return r11_pula; }
	public void setR11_pula(BigDecimal r11_pula) { this.r11_pula = r11_pula; }
	public BigDecimal getR11_usd() { return r11_usd; }
	public void setR11_usd(BigDecimal r11_usd) { this.r11_usd = r11_usd; }
	public BigDecimal getR11_zar() { return r11_zar; }
	public void setR11_zar(BigDecimal r11_zar) { this.r11_zar = r11_zar; }
	public BigDecimal getR11_gbp() { return r11_gbp; }
	public void setR11_gbp(BigDecimal r11_gbp) { this.r11_gbp = r11_gbp; }
	public BigDecimal getR11_euro() { return r11_euro; }
	public void setR11_euro(BigDecimal r11_euro) { this.r11_euro = r11_euro; }
	public BigDecimal getR11_jpy() { return r11_jpy; }
	public void setR11_jpy(BigDecimal r11_jpy) { this.r11_jpy = r11_jpy; }
	public BigDecimal getR11_rupee() { return r11_rupee; }
	public void setR11_rupee(BigDecimal r11_rupee) { this.r11_rupee = r11_rupee; }
	public BigDecimal getR11_renminbi() { return r11_renminbi; }
	public void setR11_renminbi(BigDecimal r11_renminbi) { this.r11_renminbi = r11_renminbi; }
	public BigDecimal getR11_other() { return r11_other; }
	public void setR11_other(BigDecimal r11_other) { this.r11_other = r11_other; }
	public BigDecimal getR11_tot_cap_req() { return r11_tot_cap_req; }
	public void setR11_tot_cap_req(BigDecimal r11_tot_cap_req) { this.r11_tot_cap_req = r11_tot_cap_req; }

	public String getR12_currency() { return r12_currency; }
	public void setR12_currency(String r12_currency) { this.r12_currency = r12_currency; }
	public BigDecimal getR12_pula() { return r12_pula; }
	public void setR12_pula(BigDecimal r12_pula) { this.r12_pula = r12_pula; }
	public BigDecimal getR12_usd() { return r12_usd; }
	public void setR12_usd(BigDecimal r12_usd) { this.r12_usd = r12_usd; }
	public BigDecimal getR12_zar() { return r12_zar; }
	public void setR12_zar(BigDecimal r12_zar) { this.r12_zar = r12_zar; }
	public BigDecimal getR12_gbp() { return r12_gbp; }
	public void setR12_gbp(BigDecimal r12_gbp) { this.r12_gbp = r12_gbp; }
	public BigDecimal getR12_euro() { return r12_euro; }
	public void setR12_euro(BigDecimal r12_euro) { this.r12_euro = r12_euro; }
	public BigDecimal getR12_jpy() { return r12_jpy; }
	public void setR12_jpy(BigDecimal r12_jpy) { this.r12_jpy = r12_jpy; }
	public BigDecimal getR12_rupee() { return r12_rupee; }
	public void setR12_rupee(BigDecimal r12_rupee) { this.r12_rupee = r12_rupee; }
	public BigDecimal getR12_renminbi() { return r12_renminbi; }
	public void setR12_renminbi(BigDecimal r12_renminbi) { this.r12_renminbi = r12_renminbi; }
	public BigDecimal getR12_other() { return r12_other; }
	public void setR12_other(BigDecimal r12_other) { this.r12_other = r12_other; }
	public BigDecimal getR12_tot_cap_req() { return r12_tot_cap_req; }
	public void setR12_tot_cap_req(BigDecimal r12_tot_cap_req) { this.r12_tot_cap_req = r12_tot_cap_req; }

	public Date getReport_date() { return report_date; }
	public void setReport_date(Date report_date) { this.report_date = report_date; }
	public BigDecimal getReport_version() { return report_version; }
	public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
	public String getReport_frequency() { return report_frequency; }
	public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
	public String getReport_code() { return report_code; }
	public void setReport_code(String report_code) { this.report_code = report_code; }
	public String getReport_desc() { return report_desc; }
	public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
	public String getEntity_flg() { return entity_flg; }
	public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
	public String getModify_flg() { return modify_flg; }
	public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
	public String getDel_flg() { return del_flg; }
	public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	// ------------------------------
	// Entity representing M_GMIRT_Detail_Entity
	// ------------------------------
	public static class M_GMIRT_Detail_Entity implements Serializable {

	private String	r9_currency;
	private BigDecimal	r9_pula;
	private BigDecimal	r9_usd;
	private BigDecimal	r9_zar;
	private BigDecimal	r9_gbp;
	private BigDecimal	r9_euro;
	private BigDecimal	r9_jpy;
	private BigDecimal	r9_rupee;
	private BigDecimal	r9_renminbi;
	private BigDecimal	r9_other;
	private BigDecimal	r9_tot_cap_req;

	private String	r10_currency;
	private BigDecimal	r10_pula;
	private BigDecimal	r10_usd;
	private BigDecimal	r10_zar;
	private BigDecimal	r10_gbp;
	private BigDecimal	r10_euro;
	private BigDecimal	r10_jpy;
	private BigDecimal	r10_rupee;
	private BigDecimal	r10_renminbi;
	private BigDecimal	r10_other;
	private BigDecimal	r10_tot_cap_req;

	private String	r11_currency;
	private BigDecimal	r11_pula;
	private BigDecimal	r11_usd;
	private BigDecimal	r11_zar;
	private BigDecimal	r11_gbp;
	private BigDecimal	r11_euro;
	private BigDecimal	r11_jpy;
	private BigDecimal	r11_rupee;
	private BigDecimal	r11_renminbi;
	private BigDecimal	r11_other;
	private BigDecimal	r11_tot_cap_req;

	private String	r12_currency;
	private BigDecimal	r12_pula;
	private BigDecimal	r12_usd;
	private BigDecimal	r12_zar;
	private BigDecimal	r12_gbp;
	private BigDecimal	r12_euro;
	private BigDecimal	r12_jpy;
	private BigDecimal	r12_rupee;
	private BigDecimal	r12_renminbi;
	private BigDecimal	r12_other;
	private BigDecimal	r12_tot_cap_req;

	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;

	public M_GMIRT_Detail_Entity() { super(); }

	public String getR9_currency() { return r9_currency; }
	public void setR9_currency(String r9_currency) { this.r9_currency = r9_currency; }
	public BigDecimal getR9_pula() { return r9_pula; }
	public void setR9_pula(BigDecimal r9_pula) { this.r9_pula = r9_pula; }
	public BigDecimal getR9_usd() { return r9_usd; }
	public void setR9_usd(BigDecimal r9_usd) { this.r9_usd = r9_usd; }
	public BigDecimal getR9_zar() { return r9_zar; }
	public void setR9_zar(BigDecimal r9_zar) { this.r9_zar = r9_zar; }
	public BigDecimal getR9_gbp() { return r9_gbp; }
	public void setR9_gbp(BigDecimal r9_gbp) { this.r9_gbp = r9_gbp; }
	public BigDecimal getR9_euro() { return r9_euro; }
	public void setR9_euro(BigDecimal r9_euro) { this.r9_euro = r9_euro; }
	public BigDecimal getR9_jpy() { return r9_jpy; }
	public void setR9_jpy(BigDecimal r9_jpy) { this.r9_jpy = r9_jpy; }
	public BigDecimal getR9_rupee() { return r9_rupee; }
	public void setR9_rupee(BigDecimal r9_rupee) { this.r9_rupee = r9_rupee; }
	public BigDecimal getR9_renminbi() { return r9_renminbi; }
	public void setR9_renminbi(BigDecimal r9_renminbi) { this.r9_renminbi = r9_renminbi; }
	public BigDecimal getR9_other() { return r9_other; }
	public void setR9_other(BigDecimal r9_other) { this.r9_other = r9_other; }
	public BigDecimal getR9_tot_cap_req() { return r9_tot_cap_req; }
	public void setR9_tot_cap_req(BigDecimal r9_tot_cap_req) { this.r9_tot_cap_req = r9_tot_cap_req; }

	public String getR10_currency() { return r10_currency; }
	public void setR10_currency(String r10_currency) { this.r10_currency = r10_currency; }
	public BigDecimal getR10_pula() { return r10_pula; }
	public void setR10_pula(BigDecimal r10_pula) { this.r10_pula = r10_pula; }
	public BigDecimal getR10_usd() { return r10_usd; }
	public void setR10_usd(BigDecimal r10_usd) { this.r10_usd = r10_usd; }
	public BigDecimal getR10_zar() { return r10_zar; }
	public void setR10_zar(BigDecimal r10_zar) { this.r10_zar = r10_zar; }
	public BigDecimal getR10_gbp() { return r10_gbp; }
	public void setR10_gbp(BigDecimal r10_gbp) { this.r10_gbp = r10_gbp; }
	public BigDecimal getR10_euro() { return r10_euro; }
	public void setR10_euro(BigDecimal r10_euro) { this.r10_euro = r10_euro; }
	public BigDecimal getR10_jpy() { return r10_jpy; }
	public void setR10_jpy(BigDecimal r10_jpy) { this.r10_jpy = r10_jpy; }
	public BigDecimal getR10_rupee() { return r10_rupee; }
	public void setR10_rupee(BigDecimal r10_rupee) { this.r10_rupee = r10_rupee; }
	public BigDecimal getR10_renminbi() { return r10_renminbi; }
	public void setR10_renminbi(BigDecimal r10_renminbi) { this.r10_renminbi = r10_renminbi; }
	public BigDecimal getR10_other() { return r10_other; }
	public void setR10_other(BigDecimal r10_other) { this.r10_other = r10_other; }
	public BigDecimal getR10_tot_cap_req() { return r10_tot_cap_req; }
	public void setR10_tot_cap_req(BigDecimal r10_tot_cap_req) { this.r10_tot_cap_req = r10_tot_cap_req; }

	public String getR11_currency() { return r11_currency; }
	public void setR11_currency(String r11_currency) { this.r11_currency = r11_currency; }
	public BigDecimal getR11_pula() { return r11_pula; }
	public void setR11_pula(BigDecimal r11_pula) { this.r11_pula = r11_pula; }
	public BigDecimal getR11_usd() { return r11_usd; }
	public void setR11_usd(BigDecimal r11_usd) { this.r11_usd = r11_usd; }
	public BigDecimal getR11_zar() { return r11_zar; }
	public void setR11_zar(BigDecimal r11_zar) { this.r11_zar = r11_zar; }
	public BigDecimal getR11_gbp() { return r11_gbp; }
	public void setR11_gbp(BigDecimal r11_gbp) { this.r11_gbp = r11_gbp; }
	public BigDecimal getR11_euro() { return r11_euro; }
	public void setR11_euro(BigDecimal r11_euro) { this.r11_euro = r11_euro; }
	public BigDecimal getR11_jpy() { return r11_jpy; }
	public void setR11_jpy(BigDecimal r11_jpy) { this.r11_jpy = r11_jpy; }
	public BigDecimal getR11_rupee() { return r11_rupee; }
	public void setR11_rupee(BigDecimal r11_rupee) { this.r11_rupee = r11_rupee; }
	public BigDecimal getR11_renminbi() { return r11_renminbi; }
	public void setR11_renminbi(BigDecimal r11_renminbi) { this.r11_renminbi = r11_renminbi; }
	public BigDecimal getR11_other() { return r11_other; }
	public void setR11_other(BigDecimal r11_other) { this.r11_other = r11_other; }
	public BigDecimal getR11_tot_cap_req() { return r11_tot_cap_req; }
	public void setR11_tot_cap_req(BigDecimal r11_tot_cap_req) { this.r11_tot_cap_req = r11_tot_cap_req; }

	public String getR12_currency() { return r12_currency; }
	public void setR12_currency(String r12_currency) { this.r12_currency = r12_currency; }
	public BigDecimal getR12_pula() { return r12_pula; }
	public void setR12_pula(BigDecimal r12_pula) { this.r12_pula = r12_pula; }
	public BigDecimal getR12_usd() { return r12_usd; }
	public void setR12_usd(BigDecimal r12_usd) { this.r12_usd = r12_usd; }
	public BigDecimal getR12_zar() { return r12_zar; }
	public void setR12_zar(BigDecimal r12_zar) { this.r12_zar = r12_zar; }
	public BigDecimal getR12_gbp() { return r12_gbp; }
	public void setR12_gbp(BigDecimal r12_gbp) { this.r12_gbp = r12_gbp; }
	public BigDecimal getR12_euro() { return r12_euro; }
	public void setR12_euro(BigDecimal r12_euro) { this.r12_euro = r12_euro; }
	public BigDecimal getR12_jpy() { return r12_jpy; }
	public void setR12_jpy(BigDecimal r12_jpy) { this.r12_jpy = r12_jpy; }
	public BigDecimal getR12_rupee() { return r12_rupee; }
	public void setR12_rupee(BigDecimal r12_rupee) { this.r12_rupee = r12_rupee; }
	public BigDecimal getR12_renminbi() { return r12_renminbi; }
	public void setR12_renminbi(BigDecimal r12_renminbi) { this.r12_renminbi = r12_renminbi; }
	public BigDecimal getR12_other() { return r12_other; }
	public void setR12_other(BigDecimal r12_other) { this.r12_other = r12_other; }
	public BigDecimal getR12_tot_cap_req() { return r12_tot_cap_req; }
	public void setR12_tot_cap_req(BigDecimal r12_tot_cap_req) { this.r12_tot_cap_req = r12_tot_cap_req; }

	public Date getReport_date() { return report_date; }
	public void setReport_date(Date report_date) { this.report_date = report_date; }
	public BigDecimal getReport_version() { return report_version; }
	public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
	public String getReport_frequency() { return report_frequency; }
	public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
	public String getReport_code() { return report_code; }
	public void setReport_code(String report_code) { this.report_code = report_code; }
	public String getReport_desc() { return report_desc; }
	public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
	public String getEntity_flg() { return entity_flg; }
	public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
	public String getModify_flg() { return modify_flg; }
	public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
	public String getDel_flg() { return del_flg; }
	public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	// ------------------------------
	// Entity representing M_GMIRT_Archival_Summary_Entity
	// ------------------------------
	@IdClass(M_GMIRT_PK.class)
	public static class M_GMIRT_Archival_Summary_Entity implements Serializable {

	private String	r9_currency;
	private BigDecimal	r9_pula;
	private BigDecimal	r9_usd;
	private BigDecimal	r9_zar;
	private BigDecimal	r9_gbp;
	private BigDecimal	r9_euro;
	private BigDecimal	r9_jpy;
	private BigDecimal	r9_rupee;
	private BigDecimal	r9_renminbi;
	private BigDecimal	r9_other;
	private BigDecimal	r9_tot_cap_req;

	private String	r10_currency;
	private BigDecimal	r10_pula;
	private BigDecimal	r10_usd;
	private BigDecimal	r10_zar;
	private BigDecimal	r10_gbp;
	private BigDecimal	r10_euro;
	private BigDecimal	r10_jpy;
	private BigDecimal	r10_rupee;
	private BigDecimal	r10_renminbi;
	private BigDecimal	r10_other;
	private BigDecimal	r10_tot_cap_req;

	private String	r11_currency;
	private BigDecimal	r11_pula;
	private BigDecimal	r11_usd;
	private BigDecimal	r11_zar;
	private BigDecimal	r11_gbp;
	private BigDecimal	r11_euro;
	private BigDecimal	r11_jpy;
	private BigDecimal	r11_rupee;
	private BigDecimal	r11_renminbi;
	private BigDecimal	r11_other;
	private BigDecimal	r11_tot_cap_req;

	private String	r12_currency;
	private BigDecimal	r12_pula;
	private BigDecimal	r12_usd;
	private BigDecimal	r12_zar;
	private BigDecimal	r12_gbp;
	private BigDecimal	r12_euro;
	private BigDecimal	r12_jpy;
	private BigDecimal	r12_rupee;
	private BigDecimal	r12_renminbi;
	private BigDecimal	r12_other;
	private BigDecimal	r12_tot_cap_req;

	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;

	public M_GMIRT_Archival_Summary_Entity() { super(); }

	public String getR9_currency() { return r9_currency; }
	public void setR9_currency(String r9_currency) { this.r9_currency = r9_currency; }
	public BigDecimal getR9_pula() { return r9_pula; }
	public void setR9_pula(BigDecimal r9_pula) { this.r9_pula = r9_pula; }
	public BigDecimal getR9_usd() { return r9_usd; }
	public void setR9_usd(BigDecimal r9_usd) { this.r9_usd = r9_usd; }
	public BigDecimal getR9_zar() { return r9_zar; }
	public void setR9_zar(BigDecimal r9_zar) { this.r9_zar = r9_zar; }
	public BigDecimal getR9_gbp() { return r9_gbp; }
	public void setR9_gbp(BigDecimal r9_gbp) { this.r9_gbp = r9_gbp; }
	public BigDecimal getR9_euro() { return r9_euro; }
	public void setR9_euro(BigDecimal r9_euro) { this.r9_euro = r9_euro; }
	public BigDecimal getR9_jpy() { return r9_jpy; }
	public void setR9_jpy(BigDecimal r9_jpy) { this.r9_jpy = r9_jpy; }
	public BigDecimal getR9_rupee() { return r9_rupee; }
	public void setR9_rupee(BigDecimal r9_rupee) { this.r9_rupee = r9_rupee; }
	public BigDecimal getR9_renminbi() { return r9_renminbi; }
	public void setR9_renminbi(BigDecimal r9_renminbi) { this.r9_renminbi = r9_renminbi; }
	public BigDecimal getR9_other() { return r9_other; }
	public void setR9_other(BigDecimal r9_other) { this.r9_other = r9_other; }
	public BigDecimal getR9_tot_cap_req() { return r9_tot_cap_req; }
	public void setR9_tot_cap_req(BigDecimal r9_tot_cap_req) { this.r9_tot_cap_req = r9_tot_cap_req; }

	public String getR10_currency() { return r10_currency; }
	public void setR10_currency(String r10_currency) { this.r10_currency = r10_currency; }
	public BigDecimal getR10_pula() { return r10_pula; }
	public void setR10_pula(BigDecimal r10_pula) { this.r10_pula = r10_pula; }
	public BigDecimal getR10_usd() { return r10_usd; }
	public void setR10_usd(BigDecimal r10_usd) { this.r10_usd = r10_usd; }
	public BigDecimal getR10_zar() { return r10_zar; }
	public void setR10_zar(BigDecimal r10_zar) { this.r10_zar = r10_zar; }
	public BigDecimal getR10_gbp() { return r10_gbp; }
	public void setR10_gbp(BigDecimal r10_gbp) { this.r10_gbp = r10_gbp; }
	public BigDecimal getR10_euro() { return r10_euro; }
	public void setR10_euro(BigDecimal r10_euro) { this.r10_euro = r10_euro; }
	public BigDecimal getR10_jpy() { return r10_jpy; }
	public void setR10_jpy(BigDecimal r10_jpy) { this.r10_jpy = r10_jpy; }
	public BigDecimal getR10_rupee() { return r10_rupee; }
	public void setR10_rupee(BigDecimal r10_rupee) { this.r10_rupee = r10_rupee; }
	public BigDecimal getR10_renminbi() { return r10_renminbi; }
	public void setR10_renminbi(BigDecimal r10_renminbi) { this.r10_renminbi = r10_renminbi; }
	public BigDecimal getR10_other() { return r10_other; }
	public void setR10_other(BigDecimal r10_other) { this.r10_other = r10_other; }
	public BigDecimal getR10_tot_cap_req() { return r10_tot_cap_req; }
	public void setR10_tot_cap_req(BigDecimal r10_tot_cap_req) { this.r10_tot_cap_req = r10_tot_cap_req; }

	public String getR11_currency() { return r11_currency; }
	public void setR11_currency(String r11_currency) { this.r11_currency = r11_currency; }
	public BigDecimal getR11_pula() { return r11_pula; }
	public void setR11_pula(BigDecimal r11_pula) { this.r11_pula = r11_pula; }
	public BigDecimal getR11_usd() { return r11_usd; }
	public void setR11_usd(BigDecimal r11_usd) { this.r11_usd = r11_usd; }
	public BigDecimal getR11_zar() { return r11_zar; }
	public void setR11_zar(BigDecimal r11_zar) { this.r11_zar = r11_zar; }
	public BigDecimal getR11_gbp() { return r11_gbp; }
	public void setR11_gbp(BigDecimal r11_gbp) { this.r11_gbp = r11_gbp; }
	public BigDecimal getR11_euro() { return r11_euro; }
	public void setR11_euro(BigDecimal r11_euro) { this.r11_euro = r11_euro; }
	public BigDecimal getR11_jpy() { return r11_jpy; }
	public void setR11_jpy(BigDecimal r11_jpy) { this.r11_jpy = r11_jpy; }
	public BigDecimal getR11_rupee() { return r11_rupee; }
	public void setR11_rupee(BigDecimal r11_rupee) { this.r11_rupee = r11_rupee; }
	public BigDecimal getR11_renminbi() { return r11_renminbi; }
	public void setR11_renminbi(BigDecimal r11_renminbi) { this.r11_renminbi = r11_renminbi; }
	public BigDecimal getR11_other() { return r11_other; }
	public void setR11_other(BigDecimal r11_other) { this.r11_other = r11_other; }
	public BigDecimal getR11_tot_cap_req() { return r11_tot_cap_req; }
	public void setR11_tot_cap_req(BigDecimal r11_tot_cap_req) { this.r11_tot_cap_req = r11_tot_cap_req; }

	public String getR12_currency() { return r12_currency; }
	public void setR12_currency(String r12_currency) { this.r12_currency = r12_currency; }
	public BigDecimal getR12_pula() { return r12_pula; }
	public void setR12_pula(BigDecimal r12_pula) { this.r12_pula = r12_pula; }
	public BigDecimal getR12_usd() { return r12_usd; }
	public void setR12_usd(BigDecimal r12_usd) { this.r12_usd = r12_usd; }
	public BigDecimal getR12_zar() { return r12_zar; }
	public void setR12_zar(BigDecimal r12_zar) { this.r12_zar = r12_zar; }
	public BigDecimal getR12_gbp() { return r12_gbp; }
	public void setR12_gbp(BigDecimal r12_gbp) { this.r12_gbp = r12_gbp; }
	public BigDecimal getR12_euro() { return r12_euro; }
	public void setR12_euro(BigDecimal r12_euro) { this.r12_euro = r12_euro; }
	public BigDecimal getR12_jpy() { return r12_jpy; }
	public void setR12_jpy(BigDecimal r12_jpy) { this.r12_jpy = r12_jpy; }
	public BigDecimal getR12_rupee() { return r12_rupee; }
	public void setR12_rupee(BigDecimal r12_rupee) { this.r12_rupee = r12_rupee; }
	public BigDecimal getR12_renminbi() { return r12_renminbi; }
	public void setR12_renminbi(BigDecimal r12_renminbi) { this.r12_renminbi = r12_renminbi; }
	public BigDecimal getR12_other() { return r12_other; }
	public void setR12_other(BigDecimal r12_other) { this.r12_other = r12_other; }
	public BigDecimal getR12_tot_cap_req() { return r12_tot_cap_req; }
	public void setR12_tot_cap_req(BigDecimal r12_tot_cap_req) { this.r12_tot_cap_req = r12_tot_cap_req; }

	public Date getReport_date() { return report_date; }
	public void setReport_date(Date report_date) { this.report_date = report_date; }
	public BigDecimal getReport_version() { return report_version; }
	public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
	public String getReport_frequency() { return report_frequency; }
	public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
	public String getReport_code() { return report_code; }
	public void setReport_code(String report_code) { this.report_code = report_code; }
	public String getReport_desc() { return report_desc; }
	public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
	public String getEntity_flg() { return entity_flg; }
	public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
	public String getModify_flg() { return modify_flg; }
	public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
	public String getDel_flg() { return del_flg; }
	public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	public Date getReportResubDate() { return reportResubDate; }
	public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
	}

	// ------------------------------
	// Entity representing M_GMIRT_Archival_Detail_Entity
	// ------------------------------
	@IdClass(M_GMIRT_PK.class)
	public static class M_GMIRT_Archival_Detail_Entity implements Serializable {

	private String	r9_currency;
	private BigDecimal	r9_pula;
	private BigDecimal	r9_usd;
	private BigDecimal	r9_zar;
	private BigDecimal	r9_gbp;
	private BigDecimal	r9_euro;
	private BigDecimal	r9_jpy;
	private BigDecimal	r9_rupee;
	private BigDecimal	r9_renminbi;
	private BigDecimal	r9_other;
	private BigDecimal	r9_tot_cap_req;

	private String	r10_currency;
	private BigDecimal	r10_pula;
	private BigDecimal	r10_usd;
	private BigDecimal	r10_zar;
	private BigDecimal	r10_gbp;
	private BigDecimal	r10_euro;
	private BigDecimal	r10_jpy;
	private BigDecimal	r10_rupee;
	private BigDecimal	r10_renminbi;
	private BigDecimal	r10_other;
	private BigDecimal	r10_tot_cap_req;

	private String	r11_currency;
	private BigDecimal	r11_pula;
	private BigDecimal	r11_usd;
	private BigDecimal	r11_zar;
	private BigDecimal	r11_gbp;
	private BigDecimal	r11_euro;
	private BigDecimal	r11_jpy;
	private BigDecimal	r11_rupee;
	private BigDecimal	r11_renminbi;
	private BigDecimal	r11_other;
	private BigDecimal	r11_tot_cap_req;

	private String	r12_currency;
	private BigDecimal	r12_pula;
	private BigDecimal	r12_usd;
	private BigDecimal	r12_zar;
	private BigDecimal	r12_gbp;
	private BigDecimal	r12_euro;
	private BigDecimal	r12_jpy;
	private BigDecimal	r12_rupee;
	private BigDecimal	r12_renminbi;
	private BigDecimal	r12_other;
	private BigDecimal	r12_tot_cap_req;

	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;

	public M_GMIRT_Archival_Detail_Entity() { super(); }

	public String getR9_currency() { return r9_currency; }
	public void setR9_currency(String r9_currency) { this.r9_currency = r9_currency; }
	public BigDecimal getR9_pula() { return r9_pula; }
	public void setR9_pula(BigDecimal r9_pula) { this.r9_pula = r9_pula; }
	public BigDecimal getR9_usd() { return r9_usd; }
	public void setR9_usd(BigDecimal r9_usd) { this.r9_usd = r9_usd; }
	public BigDecimal getR9_zar() { return r9_zar; }
	public void setR9_zar(BigDecimal r9_zar) { this.r9_zar = r9_zar; }
	public BigDecimal getR9_gbp() { return r9_gbp; }
	public void setR9_gbp(BigDecimal r9_gbp) { this.r9_gbp = r9_gbp; }
	public BigDecimal getR9_euro() { return r9_euro; }
	public void setR9_euro(BigDecimal r9_euro) { this.r9_euro = r9_euro; }
	public BigDecimal getR9_jpy() { return r9_jpy; }
	public void setR9_jpy(BigDecimal r9_jpy) { this.r9_jpy = r9_jpy; }
	public BigDecimal getR9_rupee() { return r9_rupee; }
	public void setR9_rupee(BigDecimal r9_rupee) { this.r9_rupee = r9_rupee; }
	public BigDecimal getR9_renminbi() { return r9_renminbi; }
	public void setR9_renminbi(BigDecimal r9_renminbi) { this.r9_renminbi = r9_renminbi; }
	public BigDecimal getR9_other() { return r9_other; }
	public void setR9_other(BigDecimal r9_other) { this.r9_other = r9_other; }
	public BigDecimal getR9_tot_cap_req() { return r9_tot_cap_req; }
	public void setR9_tot_cap_req(BigDecimal r9_tot_cap_req) { this.r9_tot_cap_req = r9_tot_cap_req; }

	public String getR10_currency() { return r10_currency; }
	public void setR10_currency(String r10_currency) { this.r10_currency = r10_currency; }
	public BigDecimal getR10_pula() { return r10_pula; }
	public void setR10_pula(BigDecimal r10_pula) { this.r10_pula = r10_pula; }
	public BigDecimal getR10_usd() { return r10_usd; }
	public void setR10_usd(BigDecimal r10_usd) { this.r10_usd = r10_usd; }
	public BigDecimal getR10_zar() { return r10_zar; }
	public void setR10_zar(BigDecimal r10_zar) { this.r10_zar = r10_zar; }
	public BigDecimal getR10_gbp() { return r10_gbp; }
	public void setR10_gbp(BigDecimal r10_gbp) { this.r10_gbp = r10_gbp; }
	public BigDecimal getR10_euro() { return r10_euro; }
	public void setR10_euro(BigDecimal r10_euro) { this.r10_euro = r10_euro; }
	public BigDecimal getR10_jpy() { return r10_jpy; }
	public void setR10_jpy(BigDecimal r10_jpy) { this.r10_jpy = r10_jpy; }
	public BigDecimal getR10_rupee() { return r10_rupee; }
	public void setR10_rupee(BigDecimal r10_rupee) { this.r10_rupee = r10_rupee; }
	public BigDecimal getR10_renminbi() { return r10_renminbi; }
	public void setR10_renminbi(BigDecimal r10_renminbi) { this.r10_renminbi = r10_renminbi; }
	public BigDecimal getR10_other() { return r10_other; }
	public void setR10_other(BigDecimal r10_other) { this.r10_other = r10_other; }
	public BigDecimal getR10_tot_cap_req() { return r10_tot_cap_req; }
	public void setR10_tot_cap_req(BigDecimal r10_tot_cap_req) { this.r10_tot_cap_req = r10_tot_cap_req; }

	public String getR11_currency() { return r11_currency; }
	public void setR11_currency(String r11_currency) { this.r11_currency = r11_currency; }
	public BigDecimal getR11_pula() { return r11_pula; }
	public void setR11_pula(BigDecimal r11_pula) { this.r11_pula = r11_pula; }
	public BigDecimal getR11_usd() { return r11_usd; }
	public void setR11_usd(BigDecimal r11_usd) { this.r11_usd = r11_usd; }
	public BigDecimal getR11_zar() { return r11_zar; }
	public void setR11_zar(BigDecimal r11_zar) { this.r11_zar = r11_zar; }
	public BigDecimal getR11_gbp() { return r11_gbp; }
	public void setR11_gbp(BigDecimal r11_gbp) { this.r11_gbp = r11_gbp; }
	public BigDecimal getR11_euro() { return r11_euro; }
	public void setR11_euro(BigDecimal r11_euro) { this.r11_euro = r11_euro; }
	public BigDecimal getR11_jpy() { return r11_jpy; }
	public void setR11_jpy(BigDecimal r11_jpy) { this.r11_jpy = r11_jpy; }
	public BigDecimal getR11_rupee() { return r11_rupee; }
	public void setR11_rupee(BigDecimal r11_rupee) { this.r11_rupee = r11_rupee; }
	public BigDecimal getR11_renminbi() { return r11_renminbi; }
	public void setR11_renminbi(BigDecimal r11_renminbi) { this.r11_renminbi = r11_renminbi; }
	public BigDecimal getR11_other() { return r11_other; }
	public void setR11_other(BigDecimal r11_other) { this.r11_other = r11_other; }
	public BigDecimal getR11_tot_cap_req() { return r11_tot_cap_req; }
	public void setR11_tot_cap_req(BigDecimal r11_tot_cap_req) { this.r11_tot_cap_req = r11_tot_cap_req; }

	public String getR12_currency() { return r12_currency; }
	public void setR12_currency(String r12_currency) { this.r12_currency = r12_currency; }
	public BigDecimal getR12_pula() { return r12_pula; }
	public void setR12_pula(BigDecimal r12_pula) { this.r12_pula = r12_pula; }
	public BigDecimal getR12_usd() { return r12_usd; }
	public void setR12_usd(BigDecimal r12_usd) { this.r12_usd = r12_usd; }
	public BigDecimal getR12_zar() { return r12_zar; }
	public void setR12_zar(BigDecimal r12_zar) { this.r12_zar = r12_zar; }
	public BigDecimal getR12_gbp() { return r12_gbp; }
	public void setR12_gbp(BigDecimal r12_gbp) { this.r12_gbp = r12_gbp; }
	public BigDecimal getR12_euro() { return r12_euro; }
	public void setR12_euro(BigDecimal r12_euro) { this.r12_euro = r12_euro; }
	public BigDecimal getR12_jpy() { return r12_jpy; }
	public void setR12_jpy(BigDecimal r12_jpy) { this.r12_jpy = r12_jpy; }
	public BigDecimal getR12_rupee() { return r12_rupee; }
	public void setR12_rupee(BigDecimal r12_rupee) { this.r12_rupee = r12_rupee; }
	public BigDecimal getR12_renminbi() { return r12_renminbi; }
	public void setR12_renminbi(BigDecimal r12_renminbi) { this.r12_renminbi = r12_renminbi; }
	public BigDecimal getR12_other() { return r12_other; }
	public void setR12_other(BigDecimal r12_other) { this.r12_other = r12_other; }
	public BigDecimal getR12_tot_cap_req() { return r12_tot_cap_req; }
	public void setR12_tot_cap_req(BigDecimal r12_tot_cap_req) { this.r12_tot_cap_req = r12_tot_cap_req; }

	public Date getReport_date() { return report_date; }
	public void setReport_date(Date report_date) { this.report_date = report_date; }
	public BigDecimal getReport_version() { return report_version; }
	public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
	public String getReport_frequency() { return report_frequency; }
	public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
	public String getReport_code() { return report_code; }
	public void setReport_code(String report_code) { this.report_code = report_code; }
	public String getReport_desc() { return report_desc; }
	public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
	public String getEntity_flg() { return entity_flg; }
	public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
	public String getModify_flg() { return modify_flg; }
	public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
	public String getDel_flg() { return del_flg; }
	public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	public Date getReportResubDate() { return reportResubDate; }
	public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
	}

	// ------------------------------
	// Entity representing M_GMIRT_RESUB_Summary_Entity
	// ------------------------------
	@IdClass(M_GMIRT_PK.class)
	public static class M_GMIRT_RESUB_Summary_Entity implements Serializable {

	private String	r9_currency;
	private BigDecimal	r9_pula;
	private BigDecimal	r9_usd;
	private BigDecimal	r9_zar;
	private BigDecimal	r9_gbp;
	private BigDecimal	r9_euro;
	private BigDecimal	r9_jpy;
	private BigDecimal	r9_rupee;
	private BigDecimal	r9_renminbi;
	private BigDecimal	r9_other;
	private BigDecimal	r9_tot_cap_req;

	private String	r10_currency;
	private BigDecimal	r10_pula;
	private BigDecimal	r10_usd;
	private BigDecimal	r10_zar;
	private BigDecimal	r10_gbp;
	private BigDecimal	r10_euro;
	private BigDecimal	r10_jpy;
	private BigDecimal	r10_rupee;
	private BigDecimal	r10_renminbi;
	private BigDecimal	r10_other;
	private BigDecimal	r10_tot_cap_req;

	private String	r11_currency;
	private BigDecimal	r11_pula;
	private BigDecimal	r11_usd;
	private BigDecimal	r11_zar;
	private BigDecimal	r11_gbp;
	private BigDecimal	r11_euro;
	private BigDecimal	r11_jpy;
	private BigDecimal	r11_rupee;
	private BigDecimal	r11_renminbi;
	private BigDecimal	r11_other;
	private BigDecimal	r11_tot_cap_req;

	private String	r12_currency;
	private BigDecimal	r12_pula;
	private BigDecimal	r12_usd;
	private BigDecimal	r12_zar;
	private BigDecimal	r12_gbp;
	private BigDecimal	r12_euro;
	private BigDecimal	r12_jpy;
	private BigDecimal	r12_rupee;
	private BigDecimal	r12_renminbi;
	private BigDecimal	r12_other;
	private BigDecimal	r12_tot_cap_req;

	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;

	public M_GMIRT_RESUB_Summary_Entity() { super(); }

	public String getR9_currency() { return r9_currency; }
	public void setR9_currency(String r9_currency) { this.r9_currency = r9_currency; }
	public BigDecimal getR9_pula() { return r9_pula; }
	public void setR9_pula(BigDecimal r9_pula) { this.r9_pula = r9_pula; }
	public BigDecimal getR9_usd() { return r9_usd; }
	public void setR9_usd(BigDecimal r9_usd) { this.r9_usd = r9_usd; }
	public BigDecimal getR9_zar() { return r9_zar; }
	public void setR9_zar(BigDecimal r9_zar) { this.r9_zar = r9_zar; }
	public BigDecimal getR9_gbp() { return r9_gbp; }
	public void setR9_gbp(BigDecimal r9_gbp) { this.r9_gbp = r9_gbp; }
	public BigDecimal getR9_euro() { return r9_euro; }
	public void setR9_euro(BigDecimal r9_euro) { this.r9_euro = r9_euro; }
	public BigDecimal getR9_jpy() { return r9_jpy; }
	public void setR9_jpy(BigDecimal r9_jpy) { this.r9_jpy = r9_jpy; }
	public BigDecimal getR9_rupee() { return r9_rupee; }
	public void setR9_rupee(BigDecimal r9_rupee) { this.r9_rupee = r9_rupee; }
	public BigDecimal getR9_renminbi() { return r9_renminbi; }
	public void setR9_renminbi(BigDecimal r9_renminbi) { this.r9_renminbi = r9_renminbi; }
	public BigDecimal getR9_other() { return r9_other; }
	public void setR9_other(BigDecimal r9_other) { this.r9_other = r9_other; }
	public BigDecimal getR9_tot_cap_req() { return r9_tot_cap_req; }
	public void setR9_tot_cap_req(BigDecimal r9_tot_cap_req) { this.r9_tot_cap_req = r9_tot_cap_req; }

	public String getR10_currency() { return r10_currency; }
	public void setR10_currency(String r10_currency) { this.r10_currency = r10_currency; }
	public BigDecimal getR10_pula() { return r10_pula; }
	public void setR10_pula(BigDecimal r10_pula) { this.r10_pula = r10_pula; }
	public BigDecimal getR10_usd() { return r10_usd; }
	public void setR10_usd(BigDecimal r10_usd) { this.r10_usd = r10_usd; }
	public BigDecimal getR10_zar() { return r10_zar; }
	public void setR10_zar(BigDecimal r10_zar) { this.r10_zar = r10_zar; }
	public BigDecimal getR10_gbp() { return r10_gbp; }
	public void setR10_gbp(BigDecimal r10_gbp) { this.r10_gbp = r10_gbp; }
	public BigDecimal getR10_euro() { return r10_euro; }
	public void setR10_euro(BigDecimal r10_euro) { this.r10_euro = r10_euro; }
	public BigDecimal getR10_jpy() { return r10_jpy; }
	public void setR10_jpy(BigDecimal r10_jpy) { this.r10_jpy = r10_jpy; }
	public BigDecimal getR10_rupee() { return r10_rupee; }
	public void setR10_rupee(BigDecimal r10_rupee) { this.r10_rupee = r10_rupee; }
	public BigDecimal getR10_renminbi() { return r10_renminbi; }
	public void setR10_renminbi(BigDecimal r10_renminbi) { this.r10_renminbi = r10_renminbi; }
	public BigDecimal getR10_other() { return r10_other; }
	public void setR10_other(BigDecimal r10_other) { this.r10_other = r10_other; }
	public BigDecimal getR10_tot_cap_req() { return r10_tot_cap_req; }
	public void setR10_tot_cap_req(BigDecimal r10_tot_cap_req) { this.r10_tot_cap_req = r10_tot_cap_req; }

	public String getR11_currency() { return r11_currency; }
	public void setR11_currency(String r11_currency) { this.r11_currency = r11_currency; }
	public BigDecimal getR11_pula() { return r11_pula; }
	public void setR11_pula(BigDecimal r11_pula) { this.r11_pula = r11_pula; }
	public BigDecimal getR11_usd() { return r11_usd; }
	public void setR11_usd(BigDecimal r11_usd) { this.r11_usd = r11_usd; }
	public BigDecimal getR11_zar() { return r11_zar; }
	public void setR11_zar(BigDecimal r11_zar) { this.r11_zar = r11_zar; }
	public BigDecimal getR11_gbp() { return r11_gbp; }
	public void setR11_gbp(BigDecimal r11_gbp) { this.r11_gbp = r11_gbp; }
	public BigDecimal getR11_euro() { return r11_euro; }
	public void setR11_euro(BigDecimal r11_euro) { this.r11_euro = r11_euro; }
	public BigDecimal getR11_jpy() { return r11_jpy; }
	public void setR11_jpy(BigDecimal r11_jpy) { this.r11_jpy = r11_jpy; }
	public BigDecimal getR11_rupee() { return r11_rupee; }
	public void setR11_rupee(BigDecimal r11_rupee) { this.r11_rupee = r11_rupee; }
	public BigDecimal getR11_renminbi() { return r11_renminbi; }
	public void setR11_renminbi(BigDecimal r11_renminbi) { this.r11_renminbi = r11_renminbi; }
	public BigDecimal getR11_other() { return r11_other; }
	public void setR11_other(BigDecimal r11_other) { this.r11_other = r11_other; }
	public BigDecimal getR11_tot_cap_req() { return r11_tot_cap_req; }
	public void setR11_tot_cap_req(BigDecimal r11_tot_cap_req) { this.r11_tot_cap_req = r11_tot_cap_req; }

	public String getR12_currency() { return r12_currency; }
	public void setR12_currency(String r12_currency) { this.r12_currency = r12_currency; }
	public BigDecimal getR12_pula() { return r12_pula; }
	public void setR12_pula(BigDecimal r12_pula) { this.r12_pula = r12_pula; }
	public BigDecimal getR12_usd() { return r12_usd; }
	public void setR12_usd(BigDecimal r12_usd) { this.r12_usd = r12_usd; }
	public BigDecimal getR12_zar() { return r12_zar; }
	public void setR12_zar(BigDecimal r12_zar) { this.r12_zar = r12_zar; }
	public BigDecimal getR12_gbp() { return r12_gbp; }
	public void setR12_gbp(BigDecimal r12_gbp) { this.r12_gbp = r12_gbp; }
	public BigDecimal getR12_euro() { return r12_euro; }
	public void setR12_euro(BigDecimal r12_euro) { this.r12_euro = r12_euro; }
	public BigDecimal getR12_jpy() { return r12_jpy; }
	public void setR12_jpy(BigDecimal r12_jpy) { this.r12_jpy = r12_jpy; }
	public BigDecimal getR12_rupee() { return r12_rupee; }
	public void setR12_rupee(BigDecimal r12_rupee) { this.r12_rupee = r12_rupee; }
	public BigDecimal getR12_renminbi() { return r12_renminbi; }
	public void setR12_renminbi(BigDecimal r12_renminbi) { this.r12_renminbi = r12_renminbi; }
	public BigDecimal getR12_other() { return r12_other; }
	public void setR12_other(BigDecimal r12_other) { this.r12_other = r12_other; }
	public BigDecimal getR12_tot_cap_req() { return r12_tot_cap_req; }
	public void setR12_tot_cap_req(BigDecimal r12_tot_cap_req) { this.r12_tot_cap_req = r12_tot_cap_req; }

	public Date getReport_date() { return report_date; }
	public void setReport_date(Date report_date) { this.report_date = report_date; }
	public BigDecimal getReport_version() { return report_version; }
	public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
	public String getReport_frequency() { return report_frequency; }
	public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
	public String getReport_code() { return report_code; }
	public void setReport_code(String report_code) { this.report_code = report_code; }
	public String getReport_desc() { return report_desc; }
	public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
	public String getEntity_flg() { return entity_flg; }
	public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
	public String getModify_flg() { return modify_flg; }
	public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
	public String getDel_flg() { return del_flg; }
	public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	public Date getReportResubDate() { return reportResubDate; }
	public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
	}

	// ------------------------------
	// Entity representing M_GMIRT_RESUB_Detail_Entity
	// ------------------------------
	@IdClass(M_GMIRT_PK.class)
	public static class M_GMIRT_RESUB_Detail_Entity implements Serializable {

	private String	r9_currency;
	private BigDecimal	r9_pula;
	private BigDecimal	r9_usd;
	private BigDecimal	r9_zar;
	private BigDecimal	r9_gbp;
	private BigDecimal	r9_euro;
	private BigDecimal	r9_jpy;
	private BigDecimal	r9_rupee;
	private BigDecimal	r9_renminbi;
	private BigDecimal	r9_other;
	private BigDecimal	r9_tot_cap_req;

	private String	r10_currency;
	private BigDecimal	r10_pula;
	private BigDecimal	r10_usd;
	private BigDecimal	r10_zar;
	private BigDecimal	r10_gbp;
	private BigDecimal	r10_euro;
	private BigDecimal	r10_jpy;
	private BigDecimal	r10_rupee;
	private BigDecimal	r10_renminbi;
	private BigDecimal	r10_other;
	private BigDecimal	r10_tot_cap_req;

	private String	r11_currency;
	private BigDecimal	r11_pula;
	private BigDecimal	r11_usd;
	private BigDecimal	r11_zar;
	private BigDecimal	r11_gbp;
	private BigDecimal	r11_euro;
	private BigDecimal	r11_jpy;
	private BigDecimal	r11_rupee;
	private BigDecimal	r11_renminbi;
	private BigDecimal	r11_other;
	private BigDecimal	r11_tot_cap_req;

	private String	r12_currency;
	private BigDecimal	r12_pula;
	private BigDecimal	r12_usd;
	private BigDecimal	r12_zar;
	private BigDecimal	r12_gbp;
	private BigDecimal	r12_euro;
	private BigDecimal	r12_jpy;
	private BigDecimal	r12_rupee;
	private BigDecimal	r12_renminbi;
	private BigDecimal	r12_other;
	private BigDecimal	r12_tot_cap_req;

	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date	report_date;
	private BigDecimal	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;

	public M_GMIRT_RESUB_Detail_Entity() { super(); }

	public String getR9_currency() { return r9_currency; }
	public void setR9_currency(String r9_currency) { this.r9_currency = r9_currency; }
	public BigDecimal getR9_pula() { return r9_pula; }
	public void setR9_pula(BigDecimal r9_pula) { this.r9_pula = r9_pula; }
	public BigDecimal getR9_usd() { return r9_usd; }
	public void setR9_usd(BigDecimal r9_usd) { this.r9_usd = r9_usd; }
	public BigDecimal getR9_zar() { return r9_zar; }
	public void setR9_zar(BigDecimal r9_zar) { this.r9_zar = r9_zar; }
	public BigDecimal getR9_gbp() { return r9_gbp; }
	public void setR9_gbp(BigDecimal r9_gbp) { this.r9_gbp = r9_gbp; }
	public BigDecimal getR9_euro() { return r9_euro; }
	public void setR9_euro(BigDecimal r9_euro) { this.r9_euro = r9_euro; }
	public BigDecimal getR9_jpy() { return r9_jpy; }
	public void setR9_jpy(BigDecimal r9_jpy) { this.r9_jpy = r9_jpy; }
	public BigDecimal getR9_rupee() { return r9_rupee; }
	public void setR9_rupee(BigDecimal r9_rupee) { this.r9_rupee = r9_rupee; }
	public BigDecimal getR9_renminbi() { return r9_renminbi; }
	public void setR9_renminbi(BigDecimal r9_renminbi) { this.r9_renminbi = r9_renminbi; }
	public BigDecimal getR9_other() { return r9_other; }
	public void setR9_other(BigDecimal r9_other) { this.r9_other = r9_other; }
	public BigDecimal getR9_tot_cap_req() { return r9_tot_cap_req; }
	public void setR9_tot_cap_req(BigDecimal r9_tot_cap_req) { this.r9_tot_cap_req = r9_tot_cap_req; }

	public String getR10_currency() { return r10_currency; }
	public void setR10_currency(String r10_currency) { this.r10_currency = r10_currency; }
	public BigDecimal getR10_pula() { return r10_pula; }
	public void setR10_pula(BigDecimal r10_pula) { this.r10_pula = r10_pula; }
	public BigDecimal getR10_usd() { return r10_usd; }
	public void setR10_usd(BigDecimal r10_usd) { this.r10_usd = r10_usd; }
	public BigDecimal getR10_zar() { return r10_zar; }
	public void setR10_zar(BigDecimal r10_zar) { this.r10_zar = r10_zar; }
	public BigDecimal getR10_gbp() { return r10_gbp; }
	public void setR10_gbp(BigDecimal r10_gbp) { this.r10_gbp = r10_gbp; }
	public BigDecimal getR10_euro() { return r10_euro; }
	public void setR10_euro(BigDecimal r10_euro) { this.r10_euro = r10_euro; }
	public BigDecimal getR10_jpy() { return r10_jpy; }
	public void setR10_jpy(BigDecimal r10_jpy) { this.r10_jpy = r10_jpy; }
	public BigDecimal getR10_rupee() { return r10_rupee; }
	public void setR10_rupee(BigDecimal r10_rupee) { this.r10_rupee = r10_rupee; }
	public BigDecimal getR10_renminbi() { return r10_renminbi; }
	public void setR10_renminbi(BigDecimal r10_renminbi) { this.r10_renminbi = r10_renminbi; }
	public BigDecimal getR10_other() { return r10_other; }
	public void setR10_other(BigDecimal r10_other) { this.r10_other = r10_other; }
	public BigDecimal getR10_tot_cap_req() { return r10_tot_cap_req; }
	public void setR10_tot_cap_req(BigDecimal r10_tot_cap_req) { this.r10_tot_cap_req = r10_tot_cap_req; }

	public String getR11_currency() { return r11_currency; }
	public void setR11_currency(String r11_currency) { this.r11_currency = r11_currency; }
	public BigDecimal getR11_pula() { return r11_pula; }
	public void setR11_pula(BigDecimal r11_pula) { this.r11_pula = r11_pula; }
	public BigDecimal getR11_usd() { return r11_usd; }
	public void setR11_usd(BigDecimal r11_usd) { this.r11_usd = r11_usd; }
	public BigDecimal getR11_zar() { return r11_zar; }
	public void setR11_zar(BigDecimal r11_zar) { this.r11_zar = r11_zar; }
	public BigDecimal getR11_gbp() { return r11_gbp; }
	public void setR11_gbp(BigDecimal r11_gbp) { this.r11_gbp = r11_gbp; }
	public BigDecimal getR11_euro() { return r11_euro; }
	public void setR11_euro(BigDecimal r11_euro) { this.r11_euro = r11_euro; }
	public BigDecimal getR11_jpy() { return r11_jpy; }
	public void setR11_jpy(BigDecimal r11_jpy) { this.r11_jpy = r11_jpy; }
	public BigDecimal getR11_rupee() { return r11_rupee; }
	public void setR11_rupee(BigDecimal r11_rupee) { this.r11_rupee = r11_rupee; }
	public BigDecimal getR11_renminbi() { return r11_renminbi; }
	public void setR11_renminbi(BigDecimal r11_renminbi) { this.r11_renminbi = r11_renminbi; }
	public BigDecimal getR11_other() { return r11_other; }
	public void setR11_other(BigDecimal r11_other) { this.r11_other = r11_other; }
	public BigDecimal getR11_tot_cap_req() { return r11_tot_cap_req; }
	public void setR11_tot_cap_req(BigDecimal r11_tot_cap_req) { this.r11_tot_cap_req = r11_tot_cap_req; }

	public String getR12_currency() { return r12_currency; }
	public void setR12_currency(String r12_currency) { this.r12_currency = r12_currency; }
	public BigDecimal getR12_pula() { return r12_pula; }
	public void setR12_pula(BigDecimal r12_pula) { this.r12_pula = r12_pula; }
	public BigDecimal getR12_usd() { return r12_usd; }
	public void setR12_usd(BigDecimal r12_usd) { this.r12_usd = r12_usd; }
	public BigDecimal getR12_zar() { return r12_zar; }
	public void setR12_zar(BigDecimal r12_zar) { this.r12_zar = r12_zar; }
	public BigDecimal getR12_gbp() { return r12_gbp; }
	public void setR12_gbp(BigDecimal r12_gbp) { this.r12_gbp = r12_gbp; }
	public BigDecimal getR12_euro() { return r12_euro; }
	public void setR12_euro(BigDecimal r12_euro) { this.r12_euro = r12_euro; }
	public BigDecimal getR12_jpy() { return r12_jpy; }
	public void setR12_jpy(BigDecimal r12_jpy) { this.r12_jpy = r12_jpy; }
	public BigDecimal getR12_rupee() { return r12_rupee; }
	public void setR12_rupee(BigDecimal r12_rupee) { this.r12_rupee = r12_rupee; }
	public BigDecimal getR12_renminbi() { return r12_renminbi; }
	public void setR12_renminbi(BigDecimal r12_renminbi) { this.r12_renminbi = r12_renminbi; }
	public BigDecimal getR12_other() { return r12_other; }
	public void setR12_other(BigDecimal r12_other) { this.r12_other = r12_other; }
	public BigDecimal getR12_tot_cap_req() { return r12_tot_cap_req; }
	public void setR12_tot_cap_req(BigDecimal r12_tot_cap_req) { this.r12_tot_cap_req = r12_tot_cap_req; }

	public Date getReport_date() { return report_date; }
	public void setReport_date(Date report_date) { this.report_date = report_date; }
	public BigDecimal getReport_version() { return report_version; }
	public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
	public String getReport_frequency() { return report_frequency; }
	public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
	public String getReport_code() { return report_code; }
	public void setReport_code(String report_code) { this.report_code = report_code; }
	public String getReport_desc() { return report_desc; }
	public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
	public String getEntity_flg() { return entity_flg; }
	public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
	public String getModify_flg() { return modify_flg; }
	public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
	public String getDel_flg() { return del_flg; }
	public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	public Date getReportResubDate() { return reportResubDate; }
	public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
	}


}