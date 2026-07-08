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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service

public class BRRS_M_OB_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OB_ReportService.class);

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
	// Renders the M_OB report view screen
	// ------------------------------
	public ModelAndView getBRRS_M_OBView(String reportId, String fromdate, String todate, String currency,
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
				List<M_OB_Archival_Summary_Entity> T1Master = jdbcTemplate.query(
						"select * from BRRS_M_OB_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
						new Object[] { d1, version }, new M_OB_Archival_Summary_RowMapper());
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_OB_Resub_Summary_Entity> T1Master = jdbcTemplate.query(
						"select * from BRRS_M_OB_RESUB_SUMMARYTABLE where REPORT_DATE = ? and REPORT_VERSION = ?",
						new Object[] { d1, version }, new M_OB_Resub_Summary_RowMapper());

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_OB_Summary_Entity> T1Master = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_OB_SUMMARYTABLE WHERE REPORT_DATE = ?",
						new Object[] { dateformat.parse(todate) }, new M_OB_Summary_RowMapper());
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_OB_Archival_Detail_Entity> T1Master = jdbcTemplate.query(
							"select * from BRRS_M_OB_ARCHIVALTABLE_DETAIL where REPORT_DATE = ? and REPORT_VERSION = ?",
							new Object[] { d1, version }, new M_OB_Archival_Detail_RowMapper());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_OB_Resub_Detail_Entity> T1Master = jdbcTemplate.query(
							"select * from BRRS_M_OB_RESUB_DETAILTABLE where REPORT_DATE = ? and REPORT_VERSION = ?",
							new Object[] { d1, version }, new M_OB_Resub_Detail_RowMapper());

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_OB_Detail_Entity> T1Master = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_OB_DETAILTABLE WHERE REPORT_DATE = ?",
							new Object[] { dateformat.parse(todate) }, new M_OB_Detail_RowMapper());
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_OB");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}


	// ------------------------------
	// Updates the M_OB normal summary and detail records
	// ------------------------------
	public void updateReport(M_OB_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // Fetch existing SUMMARY
	    List<M_OB_Summary_Entity> summaryList = jdbcTemplate.query(
	            "SELECT * FROM BRRS_M_OB_SUMMARYTABLE WHERE REPORT_DATE = ?",
	            new Object[] { updatedEntity.getReportDate() }, new M_OB_Summary_RowMapper());
	    M_OB_Summary_Entity existingSummary = summaryList.stream().findFirst().orElseThrow(() -> new RuntimeException(
	                    "Summary record not found for REPORT_DATE: "
	                            + updatedEntity.getReportDate()));

	    // Fetch or create DETAIL
	    List<M_OB_Detail_Entity> detailList = jdbcTemplate.query(
	            "SELECT * FROM BRRS_M_OB_DETAILTABLE WHERE REPORT_DATE = ?",
	            new Object[] { updatedEntity.getReportDate() }, new M_OB_Detail_RowMapper());
	    M_OB_Detail_Entity existingDetail = detailList.stream().findFirst().orElseGet(() -> {
	                M_OB_Detail_Entity d = new M_OB_Detail_Entity();
	                d.setReportDate(updatedEntity.getReportDate());
	                return d;
	            });

	    // Audit old copy
	    M_OB_Summary_Entity oldcopy = new M_OB_Summary_Entity();
	    BeanUtils.copyProperties(existingSummary, oldcopy);

	    try {

	        // Rows to skip in main loop
	        int[] skipRows = {15, 29, 38, 41, 44, 49, 52, 58, 64};

	        // Main loop: R12 → R63
	        for (int i = 12; i <= 63; i++) {

	            boolean skip = false;

	            for (int s : skipRows) {
	                if (i == s) {
	                    skip = true;
	                    break;
	                }
	            }

	            if (skip) {
	                continue;
	            }

	            String prefix = "R" + i + "_";

	            String[] fields = {"OTHER_BORROW"};

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {

	                    Method getter =
	                            M_OB_Summary_Entity.class.getMethod(getterName);

	                    Object newValue = getter.invoke(updatedEntity);

	                    // Update Summary
	                    Method summarySetter =
	                            M_OB_Summary_Entity.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    summarySetter.invoke(existingSummary, newValue);

	                    // Update Detail
	                    Method detailSetter =
	                            M_OB_Detail_Entity.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                }
	            }
	        }

	        // Formula rows
	        int[] targetRows = {11, 15, 29, 38, 41, 44, 49, 52, 58, 64};

	        for (int i : targetRows) {

	            String prefix = "R" + i + "_";

	            String[] fields = {"OTHER_BORROW"};

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {

	                    Method getter =
	                            M_OB_Summary_Entity.class.getMethod(getterName);

	                    Object newValue = getter.invoke(updatedEntity);

	                    // Update Summary
	                    Method summarySetter =
	                            M_OB_Summary_Entity.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    summarySetter.invoke(existingSummary, newValue);

	                    // Update Detail
	                    Method detailSetter =
	                            M_OB_Detail_Entity.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {

	        throw new RuntimeException(
	                "Error while updating OB report fields", e);
	    }

	    // Audit & Save only if changes exist
	    String changes = auditService.getChanges(oldcopy, existingSummary);

	    if (!changes.isEmpty()) {

	        saveSummary(existingSummary);
	        saveDetail(existingDetail);

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existingSummary,
	                updatedEntity.getReportDate().toString(),
	                "M OB Summary Screen",
	                "BRRS_M_OB_SUMMARY"
	        );

	        System.out.println(
	                "✅ OB Summary and Detail updated successfully.");
	    }
	}
	
	
	// ------------------------------
	// Updates M_OB resubmission summary and detail records with a new version
	// ------------------------------
	public void updateResubReport(M_OB_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReportDate();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = null;
		try {
			maxResubVer = jdbcTemplate.queryForObject(
				"SELECT MAX(REPORT_VERSION) FROM BRRS_M_OB_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, BigDecimal.class);
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

		M_OB_Resub_Summary_Entity resubSummary = new M_OB_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReportDate(reportDate);
		resubSummary.setReportVersion(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_OB_Resub_Detail_Entity resubDetail = new M_OB_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReportDate(reportDate);
		resubDetail.setReportVersion(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_OB_Archival_Summary_Entity archSummary = new M_OB_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReportDate(reportDate);
		archSummary.setReportVersion(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_OB_Archival_Detail_Entity archDetail = new M_OB_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReportDate(reportDate);
		archDetail.setReportVersion(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH SAME DATA
		// ====================================================

		insertResubSummary(resubSummary);
		insertResubDetail(resubDetail);

		insertArchivalSummary(archSummary);
		insertArchivalDetail(archDetail);
	}

	// ------------------------------
	// Fetches the list of M_OB resubmitted report versions
	// ------------------------------
	public List<Object[]> getM_OBResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_OB_Archival_Summary_Entity> latestArchivalList = jdbcTemplate.query(
					"SELECT *  FROM BRRS_M_OB_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ",
					new M_OB_Archival_Summary_RowMapper());

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_OB_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_OB Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// ------------------------------
	// Fetches the list of M_OB archival report versions
	// ------------------------------
	public List<Object[]> getM_OBArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_OB_Archival_Summary_Entity> repoData = jdbcTemplate.query(
					"SELECT *  FROM BRRS_M_OB_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ",
					new M_OB_Archival_Summary_RowMapper());

			if (repoData != null && !repoData.isEmpty()) {
				for (M_OB_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_OB_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_OB Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	// ------------------------------
	// Generates Excel file bytes for the normal M_OB report
	// ------------------------------
	public byte[] getBRRS_M_OBExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_OBARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OBResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_OBEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_OB_Summary_Entity> dataList = jdbcTemplate.query("SELECT * FROM BRRS_M_OB_SUMMARYTABLE WHERE REPORT_DATE = ?", new Object[] { dateformat.parse(todate) }, new M_OB_Summary_RowMapper());

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_OB report. Returning empty result.");
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

					int startRow = 6;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_OB_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							//REPORT_DATE
							row = sheet.getRow(6);
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

							
							
//							Cell cell2 = row.createCell(1);
//							if (record.getR11_OTHER_BORROW() != null) {
//								cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//								cell2.setCellStyle(numberStyle);
//							} else {
//								cell2.setCellValue("");
//								cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(11);

							Cell cell2 = row.createCell(1);
							if (record.getR12_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(12);

							cell2 = row.createCell(1);
							if (record.getR13_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);
							cell2 = row.createCell(1);
							if (record.getR14_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(14);
//							cell2 = row.createCell(1);
//							if (record.getR15_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(15);
							cell2 = row.createCell(1);
							if (record.getR16_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(16);
							cell2 = row.createCell(1);
							if (record.getR17_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(17);
							cell2 = row.createCell(1);
							if (record.getR18_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(18);
							cell2 = row.createCell(1);
							if (record.getR19_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(19);
							cell2 = row.createCell(1);
							if (record.getR20_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(20);
							cell2 = row.createCell(1);
							if (record.getR21_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(21);
							cell2 = row.createCell(1);
							if (record.getR22_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(22);
							cell2 = row.createCell(1);
							if (record.getR23_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(23);
							cell2 = row.createCell(1);
							if (record.getR24_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(24);
							cell2 = row.createCell(1);
							if (record.getR25_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(25);
							cell2 = row.createCell(1);
							if (record.getR26_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(26);
							cell2 = row.createCell(1);
							if (record.getR27_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(27);
							cell2 = row.createCell(1);
							if (record.getR28_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(28);
//							cell2 = row.createCell(1);
//							if (record.getR29_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(29);
							cell2 = row.createCell(1);
							if (record.getR30_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(30);
							cell2 = row.createCell(1);
							if (record.getR31_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(31);
							cell2 = row.createCell(1);
							if (record.getR32_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(32);
							cell2 = row.createCell(1);
							if (record.getR33_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(33);
							cell2 = row.createCell(1);
							if (record.getR34_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(34);
							cell2 = row.createCell(1);
							if (record.getR35_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(35);
							cell2 = row.createCell(1);
							if (record.getR36_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(36);
							cell2 = row.createCell(1);
							if (record.getR37_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(37);
//							cell2 = row.createCell(1);
//							if (record.getR38_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(38);
							cell2 = row.createCell(1);
							if (record.getR39_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(39);
							cell2 = row.createCell(1);
							if (record.getR40_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(40);
//							cell2 = row.createCell(1);
//							if (record.getR41_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(41);
							cell2 = row.createCell(1);
							if (record.getR42_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(42);
							cell2 = row.createCell(1);
							if (record.getR43_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(43);
//							cell2 = row.createCell(1);
//							if (record.getR44_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(44);
							cell2 = row.createCell(1);
							if (record.getR45_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(45);
							cell2 = row.createCell(1);
							if (record.getR46_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(46);
							cell2 = row.createCell(1);
							if (record.getR47_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(47);
							cell2 = row.createCell(1);
							if (record.getR48_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(48);
//							cell2 = row.createCell(1);
//							if (record.getR49_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(49);
							cell2 = row.createCell(1);
							if (record.getR50_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(50);
							cell2 = row.createCell(1);
							if (record.getR51_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(51);
//							cell2 = row.createCell(1);
//							if (record.getR52_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(52);
							cell2 = row.createCell(1);
							if (record.getR53_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(53);
							cell2 = row.createCell(1);
							if (record.getR54_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(54);
							cell2 = row.createCell(1);
							if (record.getR55_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(55);
							cell2 = row.createCell(1);
							if (record.getR56_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(56);
							cell2 = row.createCell(1);
							if (record.getR57_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

//							row = sheet.getRow(57);
//							cell2 = row.createCell(1);
//							if (record.getR58_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

							row = sheet.getRow(58);
							cell2 = row.createCell(1);
							if (record.getR59_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(59);
							cell2 = row.createCell(1);
							if (record.getR60_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(60);
							cell2 = row.createCell(1);
							if (record.getR61_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(61);
							cell2 = row.createCell(1);
							if (record.getR62_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							row = sheet.getRow(62);
							cell2 = row.createCell(1);
							if (record.getR63_OTHER_BORROW() != null) {
								cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}
							row = sheet.getRow(63);
//							
//							cell2 = row.createCell(1);
//							if (record.getR64_OTHER_BORROW() != null) {
//							    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//							    cell2.setCellStyle(numberStyle);
//							} else {
//							    cell2.setCellValue("");
//							    cell2.setCellStyle(textStyle);
//							}

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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OB SUMMARY", null, "BRRS_M_OB_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	// ------------------------------
	// Generates Excel file bytes for the normal M_OB email report
	// ------------------------------
	public byte[] BRRS_M_OBEmailExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OBArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OBResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_OB_Summary_Entity> dataList = jdbcTemplate.query("SELECT * FROM BRRS_M_OB_SUMMARYTABLE WHERE REPORT_DATE = ?", new Object[] { dateformat.parse(todate) }, new M_OB_Summary_RowMapper());

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_OB report. Returning empty result.");
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

				int startRow = 6;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_OB_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						//REPORT_DATE
						row = sheet.getRow(6);
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
						
//						Cell cell2 = row.createCell(1);
//						if (record.getR11_OTHER_BORROW() != null) {
//							cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//							cell2.setCellStyle(numberStyle);
//						} else {
//							cell2.setCellValue("");
//							cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(11);

						Cell cell2 = row.createCell(1);
						if (record.getR12_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(12);

						cell2 = row.createCell(1);
						if (record.getR13_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(13);
						cell2 = row.createCell(1);
						if (record.getR14_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(14);
//						cell2 = row.createCell(1);
//						if (record.getR15_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(15);
						cell2 = row.createCell(1);
						if (record.getR16_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(16);
						cell2 = row.createCell(1);
						if (record.getR17_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(17);
						cell2 = row.createCell(1);
						if (record.getR18_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(18);
						cell2 = row.createCell(1);
						if (record.getR19_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(19);
						cell2 = row.createCell(1);
						if (record.getR20_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(20);
						cell2 = row.createCell(1);
						if (record.getR21_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(21);
						cell2 = row.createCell(1);
						if (record.getR22_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(22);
						cell2 = row.createCell(1);
						if (record.getR23_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(23);
						cell2 = row.createCell(1);
						if (record.getR24_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(24);
						cell2 = row.createCell(1);
						if (record.getR25_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(25);
						cell2 = row.createCell(1);
						if (record.getR26_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(26);
						cell2 = row.createCell(1);
						if (record.getR27_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(27);
						cell2 = row.createCell(1);
						if (record.getR28_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(28);
//						cell2 = row.createCell(1);
//						if (record.getR29_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(29);
						cell2 = row.createCell(1);
						if (record.getR30_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(30);
						cell2 = row.createCell(1);
						if (record.getR31_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(31);
						cell2 = row.createCell(1);
						if (record.getR32_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(32);
						cell2 = row.createCell(1);
						if (record.getR33_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(33);
						cell2 = row.createCell(1);
						if (record.getR34_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(34);
						cell2 = row.createCell(1);
						if (record.getR35_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(35);
						cell2 = row.createCell(1);
						if (record.getR36_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(36);
						cell2 = row.createCell(1);
						if (record.getR37_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(37);
//						cell2 = row.createCell(1);
//						if (record.getR38_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(38);
						cell2 = row.createCell(1);
						if (record.getR39_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(39);
						cell2 = row.createCell(1);
						if (record.getR40_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(40);
//						cell2 = row.createCell(1);
//						if (record.getR41_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(41);
						cell2 = row.createCell(1);
						if (record.getR42_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(42);
						cell2 = row.createCell(1);
						if (record.getR43_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(43);
//						cell2 = row.createCell(1);
//						if (record.getR44_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(44);
						cell2 = row.createCell(1);
						if (record.getR45_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(45);
						cell2 = row.createCell(1);
						if (record.getR46_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(46);
						cell2 = row.createCell(1);
						if (record.getR47_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(47);
						cell2 = row.createCell(1);
						if (record.getR48_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(48);
//						cell2 = row.createCell(1);
//						if (record.getR49_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(49);
						cell2 = row.createCell(1);
						if (record.getR50_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(50);
						cell2 = row.createCell(1);
						if (record.getR51_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(51);
//						cell2 = row.createCell(1);
//						if (record.getR52_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(52);
						cell2 = row.createCell(1);
						if (record.getR53_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(53);
						cell2 = row.createCell(1);
						if (record.getR54_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(54);
						cell2 = row.createCell(1);
						if (record.getR55_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(55);
						cell2 = row.createCell(1);
						if (record.getR56_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(56);
						cell2 = row.createCell(1);
						if (record.getR57_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//						row = sheet.getRow(57);
//						cell2 = row.createCell(1);
//						if (record.getR58_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

						row = sheet.getRow(58);
						cell2 = row.createCell(1);
						if (record.getR59_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(59);
						cell2 = row.createCell(1);
						if (record.getR60_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(60);
						cell2 = row.createCell(1);
						if (record.getR61_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(61);
						cell2 = row.createCell(1);
						if (record.getR62_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						row = sheet.getRow(62);
						cell2 = row.createCell(1);
						if (record.getR63_OTHER_BORROW() != null) {
							cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}
						row = sheet.getRow(63);
//						
//						cell2 = row.createCell(1);
//						if (record.getR64_OTHER_BORROW() != null) {
//						    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//						    cell2.setCellStyle(numberStyle);
//						} else {
//						    cell2.setCellValue("");
//						    cell2.setCellStyle(textStyle);
//						}

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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OB EMAIL SUMMARY", null, "BRRS_M_OB_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	// ------------------------------
	// Generates Excel file bytes for the archival M_OB report
	// ------------------------------
	public byte[] getExcelM_OBARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OBArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OB_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_OB_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version }, new M_OB_Archival_Summary_RowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OB report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_OB_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					
					//REPORT_DATE
					row = sheet.getRow(6);
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
//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(11);

					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OB ARCHIVAL SUMMARY", null, "BRRS_M_OB_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	// ------------------------------
	// Generates Excel file bytes for the archival M_OB email report
	// ------------------------------
	public byte[] BRRS_M_OBArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_OB_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_OB_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version }, new M_OB_Archival_Summary_RowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OB report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_OB_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					//REPORT_DATE
					row = sheet.getRow(6);
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
					
//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(11);

					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OB EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_OB_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	// ------------------------------
	// Generates Excel file bytes for the resubmission M_OB report
	// ------------------------------
	public byte[] BRRS_M_OBResubExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OBResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OB_Resub_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_OB_RESUB_SUMMARYTABLE where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version }, new M_OB_Resub_Summary_RowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OB report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_OB_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					//REPORT_DATE
					row = sheet.getRow(6);
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

//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(11);

					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OB RSUB SUMMARY", null, "BRRS_M_OB_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Resub Email Excel
	// ------------------------------
	// Generates Excel file bytes for the resubmission M_OB email report
	// ------------------------------
	public byte[] BRRS_M_OBResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_OB_Resub_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_OB_RESUB_SUMMARYTABLE where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version }, new M_OB_Resub_Summary_RowMapper());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OB report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_OB_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					//REPORT_DATE
					row = sheet.getRow(6);
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

//					Cell cell2 = row.createCell(1);
//					if (record.getR11_OTHER_BORROW() != null) {
//						cell2.setCellValue(record.getR11_OTHER_BORROW().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(11);

					Cell cell2 = row.createCell(1);
					if (record.getR12_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR12_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cell2 = row.createCell(1);
					if (record.getR13_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR13_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell2 = row.createCell(1);
					if (record.getR14_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR14_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(14);
//					cell2 = row.createCell(1);
//					if (record.getR15_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR15_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(15);
					cell2 = row.createCell(1);
					if (record.getR16_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR16_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell2 = row.createCell(1);
					if (record.getR17_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR17_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell2 = row.createCell(1);
					if (record.getR18_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR18_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell2 = row.createCell(1);
					if (record.getR19_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR19_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell2 = row.createCell(1);
					if (record.getR20_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR20_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell2 = row.createCell(1);
					if (record.getR21_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR21_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell2 = row.createCell(1);
					if (record.getR22_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR22_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell2 = row.createCell(1);
					if (record.getR23_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR23_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell2 = row.createCell(1);
					if (record.getR24_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR24_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell2 = row.createCell(1);
					if (record.getR25_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR25_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell2 = row.createCell(1);
					if (record.getR26_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR26_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell2 = row.createCell(1);
					if (record.getR27_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR27_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell2 = row.createCell(1);
					if (record.getR28_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR28_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(28);
//					cell2 = row.createCell(1);
//					if (record.getR29_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR29_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(29);
					cell2 = row.createCell(1);
					if (record.getR30_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR30_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell2 = row.createCell(1);
					if (record.getR31_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR31_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell2 = row.createCell(1);
					if (record.getR32_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR32_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell2 = row.createCell(1);
					if (record.getR33_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR33_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell2 = row.createCell(1);
					if (record.getR34_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR34_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell2 = row.createCell(1);
					if (record.getR35_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR35_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell2 = row.createCell(1);
					if (record.getR36_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR36_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell2 = row.createCell(1);
					if (record.getR37_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR37_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(37);
//					cell2 = row.createCell(1);
//					if (record.getR38_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR38_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(38);
					cell2 = row.createCell(1);
					if (record.getR39_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR39_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell2 = row.createCell(1);
					if (record.getR40_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR40_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(40);
//					cell2 = row.createCell(1);
//					if (record.getR41_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR41_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(41);
					cell2 = row.createCell(1);
					if (record.getR42_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR42_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell2 = row.createCell(1);
					if (record.getR43_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR43_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(43);
//					cell2 = row.createCell(1);
//					if (record.getR44_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR44_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(44);
					cell2 = row.createCell(1);
					if (record.getR45_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR45_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell2 = row.createCell(1);
					if (record.getR46_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR46_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell2 = row.createCell(1);
					if (record.getR47_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR47_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell2 = row.createCell(1);
					if (record.getR48_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR48_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(48);
//					cell2 = row.createCell(1);
//					if (record.getR49_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR49_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(49);
					cell2 = row.createCell(1);
					if (record.getR50_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR50_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell2 = row.createCell(1);
					if (record.getR51_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR51_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(51);
//					cell2 = row.createCell(1);
//					if (record.getR52_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR52_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(52);
					cell2 = row.createCell(1);
					if (record.getR53_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR53_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(53);
					cell2 = row.createCell(1);
					if (record.getR54_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR54_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell2 = row.createCell(1);
					if (record.getR55_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR55_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(55);
					cell2 = row.createCell(1);
					if (record.getR56_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR56_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(56);
					cell2 = row.createCell(1);
					if (record.getR57_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR57_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//					row = sheet.getRow(57);
//					cell2 = row.createCell(1);
//					if (record.getR58_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR58_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

					row = sheet.getRow(58);
					cell2 = row.createCell(1);
					if (record.getR59_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR59_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(59);
					cell2 = row.createCell(1);
					if (record.getR60_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR60_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(60);
					cell2 = row.createCell(1);
					if (record.getR61_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR61_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(61);
					cell2 = row.createCell(1);
					if (record.getR62_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR62_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(62);
					cell2 = row.createCell(1);
					if (record.getR63_OTHER_BORROW() != null) {
						cell2.setCellValue(record.getR63_OTHER_BORROW().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(63);
//					
//					cell2 = row.createCell(1);
//					if (record.getR64_OTHER_BORROW() != null) {
//					    cell2.setCellValue(record.getR64_OTHER_BORROW().doubleValue());
//					    cell2.setCellStyle(numberStyle);
//					} else {
//					    cell2.setCellValue("");
//					    cell2.setCellStyle(textStyle);
//					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OB EMAIL RESUB SUMMARY", null, "BRRS_M_OB_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}



	// ------------------------------
	// Helper method to insert or update the normal summary record
	// ------------------------------
	private void saveSummary(M_OB_Summary_Entity entity) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BRRS_M_OB_SUMMARYTABLE WHERE REPORT_DATE = ?", new Object[]{entity.getReportDate()}, Integer.class);
		if (count != null && count > 0) {
			StringBuilder sql = new StringBuilder("UPDATE BRRS_M_OB_SUMMARYTABLE SET ");
			List<Object> params = new ArrayList<>();
			sql.append("REPORT_VERSION = ?, report_frequency = ?, report_code = ?, report_desc = ?, entity_flg = ?, modify_flg = ?, del_flg = ?");
			params.add(entity.getReportVersion());
			params.add(entity.getReport_frequency());
			params.add(entity.getReport_code());
			params.add(entity.getReport_desc());
			params.add(entity.getEntity_flg());
			params.add(entity.getModify_flg());
			params.add(entity.getDel_flg());

			for (int i = 11; i <= 64; i++) {
				sql.append(", R").append(i).append("_OTHER_BORROW = ?");
				try {
					Method getter = M_OB_Summary_Entity.class.getMethod("getR" + i + "_OTHER_BORROW");
					params.add(getter.invoke(entity));
				} catch (Exception e) {
					params.add(null);
				}
			}
			sql.append(" WHERE REPORT_DATE = ?");
			params.add(entity.getReportDate());
			jdbcTemplate.update(sql.toString(), params.toArray());
		} else {
			StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_OB_SUMMARYTABLE (");
			StringBuilder vals = new StringBuilder("VALUES (");
			List<Object> params = new ArrayList<>();

			sql.append("REPORT_DATE, REPORT_VERSION, report_frequency, report_code, report_desc, entity_flg, modify_flg, del_flg");
			vals.append("?, ?, ?, ?, ?, ?, ?, ?");
			params.add(entity.getReportDate());
			params.add(entity.getReportVersion());
			params.add(entity.getReport_frequency());
			params.add(entity.getReport_code());
			params.add(entity.getReport_desc());
			params.add(entity.getEntity_flg());
			params.add(entity.getModify_flg());
			params.add(entity.getDel_flg());

			for (int i = 11; i <= 64; i++) {
				sql.append(", R").append(i).append("_OTHER_BORROW");
				vals.append(", ?");
				try {
					Method getter = M_OB_Summary_Entity.class.getMethod("getR" + i + "_OTHER_BORROW");
					params.add(getter.invoke(entity));
				} catch (Exception e) {
					params.add(null);
				}
			}
			sql.append(") ");
			vals.append(")");
			sql.append(vals);
			jdbcTemplate.update(sql.toString(), params.toArray());
		}
	}

	// ------------------------------
	// Helper method to insert or update the normal detail record
	// ------------------------------
	private void saveDetail(M_OB_Detail_Entity entity) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BRRS_M_OB_DETAILTABLE WHERE REPORT_DATE = ?", new Object[]{entity.getReportDate()}, Integer.class);
		if (count != null && count > 0) {
			StringBuilder sql = new StringBuilder("UPDATE BRRS_M_OB_DETAILTABLE SET ");
			List<Object> params = new ArrayList<>();
			sql.append("REPORT_VERSION = ?, report_frequency = ?, report_code = ?, report_desc = ?, entity_flg = ?, modify_flg = ?, del_flg = ?");
			params.add(entity.getReportVersion());
			params.add(entity.getReport_frequency());
			params.add(entity.getReport_code());
			params.add(entity.getReport_desc());
			params.add(entity.getEntity_flg());
			params.add(entity.getModify_flg());
			params.add(entity.getDel_flg());

			for (int i = 11; i <= 64; i++) {
				sql.append(", R").append(i).append("_OTHER_BORROW = ?");
				try {
					Method getter = M_OB_Detail_Entity.class.getMethod("getR" + i + "_OTHER_BORROW");
					params.add(getter.invoke(entity));
				} catch (Exception e) {
					params.add(null);
				}
			}
			sql.append(" WHERE REPORT_DATE = ?");
			params.add(entity.getReportDate());
			jdbcTemplate.update(sql.toString(), params.toArray());
		} else {
			StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_OB_DETAILTABLE (");
			StringBuilder vals = new StringBuilder("VALUES (");
			List<Object> params = new ArrayList<>();

			sql.append("REPORT_DATE, REPORT_VERSION, report_frequency, report_code, report_desc, entity_flg, modify_flg, del_flg");
			vals.append("?, ?, ?, ?, ?, ?, ?, ?");
			params.add(entity.getReportDate());
			params.add(entity.getReportVersion());
			params.add(entity.getReport_frequency());
			params.add(entity.getReport_code());
			params.add(entity.getReport_desc());
			params.add(entity.getEntity_flg());
			params.add(entity.getModify_flg());
			params.add(entity.getDel_flg());

			for (int i = 11; i <= 64; i++) {
				sql.append(", R").append(i).append("_OTHER_BORROW");
				vals.append(", ?");
				try {
					Method getter = M_OB_Detail_Entity.class.getMethod("getR" + i + "_OTHER_BORROW");
					params.add(getter.invoke(entity));
				} catch (Exception e) {
					params.add(null);
				}
			}
			sql.append(") ");
			vals.append(")");
			sql.append(vals);
			jdbcTemplate.update(sql.toString(), params.toArray());
		}
	}

	// ------------------------------
	// Helper method to insert a new resubmission summary record
	// ------------------------------
	private void insertResubSummary(M_OB_Resub_Summary_Entity entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_OB_RESUB_SUMMARYTABLE (");
		StringBuilder vals = new StringBuilder("VALUES (");
		List<Object> params = new ArrayList<>();

		sql.append("REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		vals.append("?, ?, ?, ?, ?, ?, ?, ?, ?");
		params.add(entity.getReportDate());
		params.add(entity.getReportVersion());
		params.add(entity.getReportResubDate());
		params.add(entity.getREPORT_FREQUENCY());
		params.add(entity.getREPORT_CODE());
		params.add(entity.getREPORT_DESC());
		params.add(entity.getENTITY_FLG());
		params.add(entity.getMODIFY_FLG());
		params.add(entity.getDEL_FLG());

		for (int i = 11; i <= 64; i++) {
			sql.append(", R").append(i).append("_OTHER_BORROW");
			vals.append(", ?");
			try {
				Method getter = M_OB_Resub_Summary_Entity.class.getMethod("getR" + i + "_OTHER_BORROW");
				params.add(getter.invoke(entity));
			} catch (Exception e) {
				params.add(null);
			}
		}
		sql.append(") ");
		vals.append(")");
		sql.append(vals);
		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	// ------------------------------
	// Helper method to insert a new resubmission detail record
	// ------------------------------
	private void insertResubDetail(M_OB_Resub_Detail_Entity entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_OB_RESUB_DETAILTABLE (");
		StringBuilder vals = new StringBuilder("VALUES (");
		List<Object> params = new ArrayList<>();

		sql.append("REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		vals.append("?, ?, ?, ?, ?, ?, ?, ?, ?");
		params.add(entity.getReportDate());
		params.add(entity.getReportVersion());
		params.add(entity.getReportResubDate());
		params.add(entity.getREPORT_FREQUENCY());
		params.add(entity.getREPORT_CODE());
		params.add(entity.getREPORT_DESC());
		params.add(entity.getENTITY_FLG());
		params.add(entity.getMODIFY_FLG());
		params.add(entity.getDEL_FLG());

		for (int i = 11; i <= 64; i++) {
			sql.append(", R").append(i).append("_OTHER_BORROW");
			vals.append(", ?");
			try {
				Method getter = M_OB_Resub_Detail_Entity.class.getMethod("getR" + i + "_OTHER_BORROW");
				params.add(getter.invoke(entity));
			} catch (Exception e) {
				params.add(null);
			}
		}
		sql.append(") ");
		vals.append(")");
		sql.append(vals);
		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	// ------------------------------
	// Helper method to insert a new archival summary record
	// ------------------------------
	private void insertArchivalSummary(M_OB_Archival_Summary_Entity entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_OB_ARCHIVALTABLE_SUMMARY (");
		StringBuilder vals = new StringBuilder("VALUES (");
		List<Object> params = new ArrayList<>();

		sql.append("REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		vals.append("?, ?, ?, ?, ?, ?, ?, ?, ?");
		params.add(entity.getReportDate());
		params.add(entity.getReportVersion());
		params.add(entity.getReportResubDate());
		params.add(entity.getREPORT_FREQUENCY());
		params.add(entity.getREPORT_CODE());
		params.add(entity.getREPORT_DESC());
		params.add(entity.getENTITY_FLG());
		params.add(entity.getMODIFY_FLG());
		params.add(entity.getDEL_FLG());

		for (int i = 11; i <= 64; i++) {
			sql.append(", R").append(i).append("_OTHER_BORROW");
			vals.append(", ?");
			try {
				Method getter = M_OB_Archival_Summary_Entity.class.getMethod("getR" + i + "_OTHER_BORROW");
				params.add(getter.invoke(entity));
			} catch (Exception e) {
				params.add(null);
			}
		}
		sql.append(") ");
		vals.append(")");
		sql.append(vals);
		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	// ------------------------------
	// Helper method to insert a new archival detail record
	// ------------------------------
	private void insertArchivalDetail(M_OB_Archival_Detail_Entity entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_OB_ARCHIVALTABLE_DETAIL (");
		StringBuilder vals = new StringBuilder("VALUES (");
		List<Object> params = new ArrayList<>();

		sql.append("REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		vals.append("?, ?, ?, ?, ?, ?, ?, ?, ?");
		params.add(entity.getReportDate());
		params.add(entity.getReportVersion());
		params.add(entity.getReportResubDate());
		params.add(entity.getREPORT_FREQUENCY());
		params.add(entity.getREPORT_CODE());
		params.add(entity.getREPORT_DESC());
		params.add(entity.getENTITY_FLG());
		params.add(entity.getMODIFY_FLG());
		params.add(entity.getDEL_FLG());

		for (int i = 11; i <= 64; i++) {
			sql.append(", R").append(i).append("_OTHER_BORROW");
			vals.append(", ?");
			try {
				Method getter = M_OB_Archival_Detail_Entity.class.getMethod("getR" + i + "_OTHER_BORROW");
				params.add(getter.invoke(entity));
			} catch (Exception e) {
				params.add(null);
			}
		}
		sql.append(") ");
		vals.append(")");
		sql.append(vals);
		jdbcTemplate.update(sql.toString(), params.toArray());
	}


	// ------------------------------
	// RowMapper implementation for M_OB_Summary_Entity
	// ------------------------------
	public static class M_OB_Summary_RowMapper implements RowMapper<M_OB_Summary_Entity> {
		@Override
		public M_OB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OB_Summary_Entity obj = new M_OB_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			for (int i = 11; i <= 64; i++) {
				String colName = "R" + i + "_OTHER_BORROW";
				BigDecimal val = rs.getBigDecimal(colName);
				try {
					Method setter = M_OB_Summary_Entity.class.getMethod("set" + colName, BigDecimal.class);
					setter.invoke(obj, val);
				} catch (Exception e) {
					// ignore
				}
			}
			return obj;
		}
	}

	// ------------------------------
	// RowMapper implementation for M_OB_Detail_Entity
	// ------------------------------
	public static class M_OB_Detail_RowMapper implements RowMapper<M_OB_Detail_Entity> {
		@Override
		public M_OB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OB_Detail_Entity obj = new M_OB_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			for (int i = 11; i <= 64; i++) {
				String colName = "R" + i + "_OTHER_BORROW";
				BigDecimal val = rs.getBigDecimal(colName);
				try {
					Method setter = M_OB_Detail_Entity.class.getMethod("set" + colName, BigDecimal.class);
					setter.invoke(obj, val);
				} catch (Exception e) {
					// ignore
				}
			}
			return obj;
		}
	}

	// ------------------------------
	// RowMapper implementation for M_OB_Archival_Summary_Entity
	// ------------------------------
	public static class M_OB_Archival_Summary_RowMapper implements RowMapper<M_OB_Archival_Summary_Entity> {
		@Override
		public M_OB_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OB_Archival_Summary_Entity obj = new M_OB_Archival_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			for (int i = 11; i <= 64; i++) {
				String colName = "R" + i + "_OTHER_BORROW";
				BigDecimal val = rs.getBigDecimal(colName);
				try {
					Method setter = M_OB_Archival_Summary_Entity.class.getMethod("set" + colName, BigDecimal.class);
					setter.invoke(obj, val);
				} catch (Exception e) {
					// ignore
				}
			}
			return obj;
		}
	}

	// ------------------------------
	// RowMapper implementation for M_OB_Archival_Detail_Entity
	// ------------------------------
	public static class M_OB_Archival_Detail_RowMapper implements RowMapper<M_OB_Archival_Detail_Entity> {
		@Override
		public M_OB_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OB_Archival_Detail_Entity obj = new M_OB_Archival_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			for (int i = 11; i <= 64; i++) {
				String colName = "R" + i + "_OTHER_BORROW";
				BigDecimal val = rs.getBigDecimal(colName);
				try {
					Method setter = M_OB_Archival_Detail_Entity.class.getMethod("set" + colName, BigDecimal.class);
					setter.invoke(obj, val);
				} catch (Exception e) {
					// ignore
				}
			}
			return obj;
		}
	}

	// ------------------------------
	// RowMapper implementation for M_OB_Resub_Summary_Entity
	// ------------------------------
	public static class M_OB_Resub_Summary_RowMapper implements RowMapper<M_OB_Resub_Summary_Entity> {
		@Override
		public M_OB_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OB_Resub_Summary_Entity obj = new M_OB_Resub_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			for (int i = 11; i <= 64; i++) {
				String colName = "R" + i + "_OTHER_BORROW";
				BigDecimal val = rs.getBigDecimal(colName);
				try {
					Method setter = M_OB_Resub_Summary_Entity.class.getMethod("set" + colName, BigDecimal.class);
					setter.invoke(obj, val);
				} catch (Exception e) {
					// ignore
				}
			}
			return obj;
		}
	}

	// ------------------------------
	// RowMapper implementation for M_OB_Resub_Detail_Entity
	// ------------------------------
	public static class M_OB_Resub_Detail_RowMapper implements RowMapper<M_OB_Resub_Detail_Entity> {
		@Override
		public M_OB_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_OB_Resub_Detail_Entity obj = new M_OB_Resub_Detail_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			for (int i = 11; i <= 64; i++) {
				String colName = "R" + i + "_OTHER_BORROW";
				BigDecimal val = rs.getBigDecimal(colName);
				try {
					Method setter = M_OB_Resub_Detail_Entity.class.getMethod("set" + colName, BigDecimal.class);
					setter.invoke(obj, val);
				} catch (Exception e) {
					// ignore
				}
			}
			return obj;
		}
	}

// ------------------------------
// Primary Key class for M_OB archival and resub entities
// ------------------------------
public static class M_OB_PK implements Serializable {

    private Date reportDate;
    private BigDecimal reportVersion;

    // default constructor
    public M_OB_PK() {}

    // parameterized constructor
    public M_OB_PK(Date reportDate, BigDecimal reportVersion) {
        this.reportDate = reportDate;
        this.reportVersion = reportVersion;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof M_OB_PK)) return false;
        M_OB_PK that = (M_OB_PK) o;
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
// Summary Entity class for M_OB
// ------------------------------
public static class M_OB_Summary_Entity {

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;

    
	private BigDecimal R11_OTHER_BORROW;
	private BigDecimal R12_OTHER_BORROW;
	private BigDecimal R13_OTHER_BORROW;
	private BigDecimal R14_OTHER_BORROW;
	private BigDecimal R15_OTHER_BORROW;
	private BigDecimal R16_OTHER_BORROW;
	private BigDecimal R17_OTHER_BORROW;
	private BigDecimal R18_OTHER_BORROW;
	private BigDecimal R19_OTHER_BORROW;
	private BigDecimal R20_OTHER_BORROW;
	private BigDecimal R21_OTHER_BORROW;
	private BigDecimal R22_OTHER_BORROW;
	private BigDecimal R23_OTHER_BORROW;
	private BigDecimal R24_OTHER_BORROW;
	private BigDecimal R25_OTHER_BORROW;
	private BigDecimal R26_OTHER_BORROW;
	private BigDecimal R27_OTHER_BORROW;
	private BigDecimal R28_OTHER_BORROW;
	private BigDecimal R29_OTHER_BORROW;
	private BigDecimal R30_OTHER_BORROW;
	private BigDecimal R31_OTHER_BORROW;
	private BigDecimal R32_OTHER_BORROW;
	private BigDecimal R33_OTHER_BORROW;
	private BigDecimal R34_OTHER_BORROW;
	private BigDecimal R35_OTHER_BORROW;
	private BigDecimal R36_OTHER_BORROW;
	private BigDecimal R37_OTHER_BORROW;
	private BigDecimal R38_OTHER_BORROW;
	private BigDecimal R39_OTHER_BORROW;
	private BigDecimal R40_OTHER_BORROW;
	private BigDecimal R41_OTHER_BORROW;
	private BigDecimal R42_OTHER_BORROW;
	private BigDecimal R43_OTHER_BORROW;
	private BigDecimal R44_OTHER_BORROW;
	private BigDecimal R45_OTHER_BORROW;
	private BigDecimal R46_OTHER_BORROW;
	private BigDecimal R47_OTHER_BORROW;
	private BigDecimal R48_OTHER_BORROW;
	private BigDecimal R49_OTHER_BORROW;
	private BigDecimal R50_OTHER_BORROW;
	private BigDecimal R51_OTHER_BORROW;
	private BigDecimal R52_OTHER_BORROW;
	private BigDecimal R53_OTHER_BORROW;
	private BigDecimal R54_OTHER_BORROW;
	private BigDecimal R55_OTHER_BORROW;
	private BigDecimal R56_OTHER_BORROW;
	private BigDecimal R57_OTHER_BORROW;
	private BigDecimal R58_OTHER_BORROW;
	private BigDecimal R59_OTHER_BORROW;
	private BigDecimal R60_OTHER_BORROW;
	private BigDecimal R61_OTHER_BORROW;
	private BigDecimal R62_OTHER_BORROW;
	private BigDecimal R63_OTHER_BORROW;
	private BigDecimal R64_OTHER_BORROW;

    

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

	public BigDecimal getR11_OTHER_BORROW() {
		return R11_OTHER_BORROW;
	}

	public void setR11_OTHER_BORROW(BigDecimal r11_OTHER_BORROW) {
		R11_OTHER_BORROW = r11_OTHER_BORROW;
	}

	public BigDecimal getR12_OTHER_BORROW() {
		return R12_OTHER_BORROW;
	}

	public void setR12_OTHER_BORROW(BigDecimal r12_OTHER_BORROW) {
		R12_OTHER_BORROW = r12_OTHER_BORROW;
	}

	public BigDecimal getR13_OTHER_BORROW() {
		return R13_OTHER_BORROW;
	}

	public void setR13_OTHER_BORROW(BigDecimal r13_OTHER_BORROW) {
		R13_OTHER_BORROW = r13_OTHER_BORROW;
	}

	public BigDecimal getR14_OTHER_BORROW() {
		return R14_OTHER_BORROW;
	}

	public void setR14_OTHER_BORROW(BigDecimal r14_OTHER_BORROW) {
		R14_OTHER_BORROW = r14_OTHER_BORROW;
	}

	public BigDecimal getR15_OTHER_BORROW() {
		return R15_OTHER_BORROW;
	}

	public void setR15_OTHER_BORROW(BigDecimal r15_OTHER_BORROW) {
		R15_OTHER_BORROW = r15_OTHER_BORROW;
	}

	public BigDecimal getR16_OTHER_BORROW() {
		return R16_OTHER_BORROW;
	}

	public void setR16_OTHER_BORROW(BigDecimal r16_OTHER_BORROW) {
		R16_OTHER_BORROW = r16_OTHER_BORROW;
	}

	public BigDecimal getR17_OTHER_BORROW() {
		return R17_OTHER_BORROW;
	}

	public void setR17_OTHER_BORROW(BigDecimal r17_OTHER_BORROW) {
		R17_OTHER_BORROW = r17_OTHER_BORROW;
	}

	public BigDecimal getR18_OTHER_BORROW() {
		return R18_OTHER_BORROW;
	}

	public void setR18_OTHER_BORROW(BigDecimal r18_OTHER_BORROW) {
		R18_OTHER_BORROW = r18_OTHER_BORROW;
	}

	public BigDecimal getR19_OTHER_BORROW() {
		return R19_OTHER_BORROW;
	}

	public void setR19_OTHER_BORROW(BigDecimal r19_OTHER_BORROW) {
		R19_OTHER_BORROW = r19_OTHER_BORROW;
	}

	public BigDecimal getR20_OTHER_BORROW() {
		return R20_OTHER_BORROW;
	}

	public void setR20_OTHER_BORROW(BigDecimal r20_OTHER_BORROW) {
		R20_OTHER_BORROW = r20_OTHER_BORROW;
	}

	public BigDecimal getR21_OTHER_BORROW() {
		return R21_OTHER_BORROW;
	}

	public void setR21_OTHER_BORROW(BigDecimal r21_OTHER_BORROW) {
		R21_OTHER_BORROW = r21_OTHER_BORROW;
	}

	public BigDecimal getR22_OTHER_BORROW() {
		return R22_OTHER_BORROW;
	}

	public void setR22_OTHER_BORROW(BigDecimal r22_OTHER_BORROW) {
		R22_OTHER_BORROW = r22_OTHER_BORROW;
	}

	public BigDecimal getR23_OTHER_BORROW() {
		return R23_OTHER_BORROW;
	}

	public void setR23_OTHER_BORROW(BigDecimal r23_OTHER_BORROW) {
		R23_OTHER_BORROW = r23_OTHER_BORROW;
	}

	public BigDecimal getR24_OTHER_BORROW() {
		return R24_OTHER_BORROW;
	}

	public void setR24_OTHER_BORROW(BigDecimal r24_OTHER_BORROW) {
		R24_OTHER_BORROW = r24_OTHER_BORROW;
	}

	public BigDecimal getR25_OTHER_BORROW() {
		return R25_OTHER_BORROW;
	}

	public void setR25_OTHER_BORROW(BigDecimal r25_OTHER_BORROW) {
		R25_OTHER_BORROW = r25_OTHER_BORROW;
	}

	public BigDecimal getR26_OTHER_BORROW() {
		return R26_OTHER_BORROW;
	}

	public void setR26_OTHER_BORROW(BigDecimal r26_OTHER_BORROW) {
		R26_OTHER_BORROW = r26_OTHER_BORROW;
	}

	public BigDecimal getR27_OTHER_BORROW() {
		return R27_OTHER_BORROW;
	}

	public void setR27_OTHER_BORROW(BigDecimal r27_OTHER_BORROW) {
		R27_OTHER_BORROW = r27_OTHER_BORROW;
	}

	public BigDecimal getR28_OTHER_BORROW() {
		return R28_OTHER_BORROW;
	}

	public void setR28_OTHER_BORROW(BigDecimal r28_OTHER_BORROW) {
		R28_OTHER_BORROW = r28_OTHER_BORROW;
	}

	public BigDecimal getR29_OTHER_BORROW() {
		return R29_OTHER_BORROW;
	}

	public void setR29_OTHER_BORROW(BigDecimal r29_OTHER_BORROW) {
		R29_OTHER_BORROW = r29_OTHER_BORROW;
	}

	public BigDecimal getR30_OTHER_BORROW() {
		return R30_OTHER_BORROW;
	}

	public void setR30_OTHER_BORROW(BigDecimal r30_OTHER_BORROW) {
		R30_OTHER_BORROW = r30_OTHER_BORROW;
	}

	public BigDecimal getR31_OTHER_BORROW() {
		return R31_OTHER_BORROW;
	}

	public void setR31_OTHER_BORROW(BigDecimal r31_OTHER_BORROW) {
		R31_OTHER_BORROW = r31_OTHER_BORROW;
	}

	public BigDecimal getR32_OTHER_BORROW() {
		return R32_OTHER_BORROW;
	}

	public void setR32_OTHER_BORROW(BigDecimal r32_OTHER_BORROW) {
		R32_OTHER_BORROW = r32_OTHER_BORROW;
	}

	public BigDecimal getR33_OTHER_BORROW() {
		return R33_OTHER_BORROW;
	}

	public void setR33_OTHER_BORROW(BigDecimal r33_OTHER_BORROW) {
		R33_OTHER_BORROW = r33_OTHER_BORROW;
	}

	public BigDecimal getR34_OTHER_BORROW() {
		return R34_OTHER_BORROW;
	}

	public void setR34_OTHER_BORROW(BigDecimal r34_OTHER_BORROW) {
		R34_OTHER_BORROW = r34_OTHER_BORROW;
	}

	public BigDecimal getR35_OTHER_BORROW() {
		return R35_OTHER_BORROW;
	}

	public void setR35_OTHER_BORROW(BigDecimal r35_OTHER_BORROW) {
		R35_OTHER_BORROW = r35_OTHER_BORROW;
	}

	public BigDecimal getR36_OTHER_BORROW() {
		return R36_OTHER_BORROW;
	}

	public void setR36_OTHER_BORROW(BigDecimal r36_OTHER_BORROW) {
		R36_OTHER_BORROW = r36_OTHER_BORROW;
	}

	public BigDecimal getR37_OTHER_BORROW() {
		return R37_OTHER_BORROW;
	}

	public void setR37_OTHER_BORROW(BigDecimal r37_OTHER_BORROW) {
		R37_OTHER_BORROW = r37_OTHER_BORROW;
	}

	public BigDecimal getR38_OTHER_BORROW() {
		return R38_OTHER_BORROW;
	}

	public void setR38_OTHER_BORROW(BigDecimal r38_OTHER_BORROW) {
		R38_OTHER_BORROW = r38_OTHER_BORROW;
	}

	public BigDecimal getR39_OTHER_BORROW() {
		return R39_OTHER_BORROW;
	}

	public void setR39_OTHER_BORROW(BigDecimal r39_OTHER_BORROW) {
		R39_OTHER_BORROW = r39_OTHER_BORROW;
	}

	public BigDecimal getR40_OTHER_BORROW() {
		return R40_OTHER_BORROW;
	}

	public void setR40_OTHER_BORROW(BigDecimal r40_OTHER_BORROW) {
		R40_OTHER_BORROW = r40_OTHER_BORROW;
	}

	public BigDecimal getR41_OTHER_BORROW() {
		return R41_OTHER_BORROW;
	}

	public void setR41_OTHER_BORROW(BigDecimal r41_OTHER_BORROW) {
		R41_OTHER_BORROW = r41_OTHER_BORROW;
	}

	public BigDecimal getR42_OTHER_BORROW() {
		return R42_OTHER_BORROW;
	}

	public void setR42_OTHER_BORROW(BigDecimal r42_OTHER_BORROW) {
		R42_OTHER_BORROW = r42_OTHER_BORROW;
	}

	public BigDecimal getR43_OTHER_BORROW() {
		return R43_OTHER_BORROW;
	}

	public void setR43_OTHER_BORROW(BigDecimal r43_OTHER_BORROW) {
		R43_OTHER_BORROW = r43_OTHER_BORROW;
	}

	public BigDecimal getR44_OTHER_BORROW() {
		return R44_OTHER_BORROW;
	}

	public void setR44_OTHER_BORROW(BigDecimal r44_OTHER_BORROW) {
		R44_OTHER_BORROW = r44_OTHER_BORROW;
	}

	public BigDecimal getR45_OTHER_BORROW() {
		return R45_OTHER_BORROW;
	}

	public void setR45_OTHER_BORROW(BigDecimal r45_OTHER_BORROW) {
		R45_OTHER_BORROW = r45_OTHER_BORROW;
	}

	public BigDecimal getR46_OTHER_BORROW() {
		return R46_OTHER_BORROW;
	}

	public void setR46_OTHER_BORROW(BigDecimal r46_OTHER_BORROW) {
		R46_OTHER_BORROW = r46_OTHER_BORROW;
	}

	public BigDecimal getR47_OTHER_BORROW() {
		return R47_OTHER_BORROW;
	}

	public void setR47_OTHER_BORROW(BigDecimal r47_OTHER_BORROW) {
		R47_OTHER_BORROW = r47_OTHER_BORROW;
	}

	public BigDecimal getR48_OTHER_BORROW() {
		return R48_OTHER_BORROW;
	}

	public void setR48_OTHER_BORROW(BigDecimal r48_OTHER_BORROW) {
		R48_OTHER_BORROW = r48_OTHER_BORROW;
	}

	public BigDecimal getR49_OTHER_BORROW() {
		return R49_OTHER_BORROW;
	}

	public void setR49_OTHER_BORROW(BigDecimal r49_OTHER_BORROW) {
		R49_OTHER_BORROW = r49_OTHER_BORROW;
	}

	public BigDecimal getR50_OTHER_BORROW() {
		return R50_OTHER_BORROW;
	}

	public void setR50_OTHER_BORROW(BigDecimal r50_OTHER_BORROW) {
		R50_OTHER_BORROW = r50_OTHER_BORROW;
	}

	public BigDecimal getR51_OTHER_BORROW() {
		return R51_OTHER_BORROW;
	}

	public void setR51_OTHER_BORROW(BigDecimal r51_OTHER_BORROW) {
		R51_OTHER_BORROW = r51_OTHER_BORROW;
	}

	public BigDecimal getR52_OTHER_BORROW() {
		return R52_OTHER_BORROW;
	}

	public void setR52_OTHER_BORROW(BigDecimal r52_OTHER_BORROW) {
		R52_OTHER_BORROW = r52_OTHER_BORROW;
	}

	public BigDecimal getR53_OTHER_BORROW() {
		return R53_OTHER_BORROW;
	}

	public void setR53_OTHER_BORROW(BigDecimal r53_OTHER_BORROW) {
		R53_OTHER_BORROW = r53_OTHER_BORROW;
	}

	public BigDecimal getR54_OTHER_BORROW() {
		return R54_OTHER_BORROW;
	}

	public void setR54_OTHER_BORROW(BigDecimal r54_OTHER_BORROW) {
		R54_OTHER_BORROW = r54_OTHER_BORROW;
	}

	public BigDecimal getR55_OTHER_BORROW() {
		return R55_OTHER_BORROW;
	}

	public void setR55_OTHER_BORROW(BigDecimal r55_OTHER_BORROW) {
		R55_OTHER_BORROW = r55_OTHER_BORROW;
	}

	public BigDecimal getR56_OTHER_BORROW() {
		return R56_OTHER_BORROW;
	}

	public void setR56_OTHER_BORROW(BigDecimal r56_OTHER_BORROW) {
		R56_OTHER_BORROW = r56_OTHER_BORROW;
	}

	public BigDecimal getR57_OTHER_BORROW() {
		return R57_OTHER_BORROW;
	}

	public void setR57_OTHER_BORROW(BigDecimal r57_OTHER_BORROW) {
		R57_OTHER_BORROW = r57_OTHER_BORROW;
	}

	public BigDecimal getR58_OTHER_BORROW() {
		return R58_OTHER_BORROW;
	}

	public void setR58_OTHER_BORROW(BigDecimal r58_OTHER_BORROW) {
		R58_OTHER_BORROW = r58_OTHER_BORROW;
	}

	public BigDecimal getR59_OTHER_BORROW() {
		return R59_OTHER_BORROW;
	}

	public void setR59_OTHER_BORROW(BigDecimal r59_OTHER_BORROW) {
		R59_OTHER_BORROW = r59_OTHER_BORROW;
	}

	public BigDecimal getR60_OTHER_BORROW() {
		return R60_OTHER_BORROW;
	}

	public void setR60_OTHER_BORROW(BigDecimal r60_OTHER_BORROW) {
		R60_OTHER_BORROW = r60_OTHER_BORROW;
	}

	public BigDecimal getR61_OTHER_BORROW() {
		return R61_OTHER_BORROW;
	}

	public void setR61_OTHER_BORROW(BigDecimal r61_OTHER_BORROW) {
		R61_OTHER_BORROW = r61_OTHER_BORROW;
	}

	public BigDecimal getR62_OTHER_BORROW() {
		return R62_OTHER_BORROW;
	}

	public void setR62_OTHER_BORROW(BigDecimal r62_OTHER_BORROW) {
		R62_OTHER_BORROW = r62_OTHER_BORROW;
	}

	public BigDecimal getR63_OTHER_BORROW() {
		return R63_OTHER_BORROW;
	}

	public void setR63_OTHER_BORROW(BigDecimal r63_OTHER_BORROW) {
		R63_OTHER_BORROW = r63_OTHER_BORROW;
	}

	public BigDecimal getR64_OTHER_BORROW() {
		return R64_OTHER_BORROW;
	}

	public void setR64_OTHER_BORROW(BigDecimal r64_OTHER_BORROW) {
		R64_OTHER_BORROW = r64_OTHER_BORROW;
	}

	public M_OB_Summary_Entity() {
        super();
    }

}

// ------------------------------
// Detail Entity class for M_OB
// ------------------------------
public static class M_OB_Detail_Entity {

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;

    
	private BigDecimal R11_OTHER_BORROW;
	private BigDecimal R12_OTHER_BORROW;
	private BigDecimal R13_OTHER_BORROW;
	private BigDecimal R14_OTHER_BORROW;
	private BigDecimal R15_OTHER_BORROW;
	private BigDecimal R16_OTHER_BORROW;
	private BigDecimal R17_OTHER_BORROW;
	private BigDecimal R18_OTHER_BORROW;
	private BigDecimal R19_OTHER_BORROW;
	private BigDecimal R20_OTHER_BORROW;
	private BigDecimal R21_OTHER_BORROW;
	private BigDecimal R22_OTHER_BORROW;
	private BigDecimal R23_OTHER_BORROW;
	private BigDecimal R24_OTHER_BORROW;
	private BigDecimal R25_OTHER_BORROW;
	private BigDecimal R26_OTHER_BORROW;
	private BigDecimal R27_OTHER_BORROW;
	private BigDecimal R28_OTHER_BORROW;
	private BigDecimal R29_OTHER_BORROW;
	private BigDecimal R30_OTHER_BORROW;
	private BigDecimal R31_OTHER_BORROW;
	private BigDecimal R32_OTHER_BORROW;
	private BigDecimal R33_OTHER_BORROW;
	private BigDecimal R34_OTHER_BORROW;
	private BigDecimal R35_OTHER_BORROW;
	private BigDecimal R36_OTHER_BORROW;
	private BigDecimal R37_OTHER_BORROW;
	private BigDecimal R38_OTHER_BORROW;
	private BigDecimal R39_OTHER_BORROW;
	private BigDecimal R40_OTHER_BORROW;
	private BigDecimal R41_OTHER_BORROW;
	private BigDecimal R42_OTHER_BORROW;
	private BigDecimal R43_OTHER_BORROW;
	private BigDecimal R44_OTHER_BORROW;
	private BigDecimal R45_OTHER_BORROW;
	private BigDecimal R46_OTHER_BORROW;
	private BigDecimal R47_OTHER_BORROW;
	private BigDecimal R48_OTHER_BORROW;
	private BigDecimal R49_OTHER_BORROW;
	private BigDecimal R50_OTHER_BORROW;
	private BigDecimal R51_OTHER_BORROW;
	private BigDecimal R52_OTHER_BORROW;
	private BigDecimal R53_OTHER_BORROW;
	private BigDecimal R54_OTHER_BORROW;
	private BigDecimal R55_OTHER_BORROW;
	private BigDecimal R56_OTHER_BORROW;
	private BigDecimal R57_OTHER_BORROW;
	private BigDecimal R58_OTHER_BORROW;
	private BigDecimal R59_OTHER_BORROW;
	private BigDecimal R60_OTHER_BORROW;
	private BigDecimal R61_OTHER_BORROW;
	private BigDecimal R62_OTHER_BORROW;
	private BigDecimal R63_OTHER_BORROW;
	private BigDecimal R64_OTHER_BORROW;

    

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

	public BigDecimal getR11_OTHER_BORROW() {
		return R11_OTHER_BORROW;
	}

	public void setR11_OTHER_BORROW(BigDecimal r11_OTHER_BORROW) {
		R11_OTHER_BORROW = r11_OTHER_BORROW;
	}

	public BigDecimal getR12_OTHER_BORROW() {
		return R12_OTHER_BORROW;
	}

	public void setR12_OTHER_BORROW(BigDecimal r12_OTHER_BORROW) {
		R12_OTHER_BORROW = r12_OTHER_BORROW;
	}

	public BigDecimal getR13_OTHER_BORROW() {
		return R13_OTHER_BORROW;
	}

	public void setR13_OTHER_BORROW(BigDecimal r13_OTHER_BORROW) {
		R13_OTHER_BORROW = r13_OTHER_BORROW;
	}

	public BigDecimal getR14_OTHER_BORROW() {
		return R14_OTHER_BORROW;
	}

	public void setR14_OTHER_BORROW(BigDecimal r14_OTHER_BORROW) {
		R14_OTHER_BORROW = r14_OTHER_BORROW;
	}

	public BigDecimal getR15_OTHER_BORROW() {
		return R15_OTHER_BORROW;
	}

	public void setR15_OTHER_BORROW(BigDecimal r15_OTHER_BORROW) {
		R15_OTHER_BORROW = r15_OTHER_BORROW;
	}

	public BigDecimal getR16_OTHER_BORROW() {
		return R16_OTHER_BORROW;
	}

	public void setR16_OTHER_BORROW(BigDecimal r16_OTHER_BORROW) {
		R16_OTHER_BORROW = r16_OTHER_BORROW;
	}

	public BigDecimal getR17_OTHER_BORROW() {
		return R17_OTHER_BORROW;
	}

	public void setR17_OTHER_BORROW(BigDecimal r17_OTHER_BORROW) {
		R17_OTHER_BORROW = r17_OTHER_BORROW;
	}

	public BigDecimal getR18_OTHER_BORROW() {
		return R18_OTHER_BORROW;
	}

	public void setR18_OTHER_BORROW(BigDecimal r18_OTHER_BORROW) {
		R18_OTHER_BORROW = r18_OTHER_BORROW;
	}

	public BigDecimal getR19_OTHER_BORROW() {
		return R19_OTHER_BORROW;
	}

	public void setR19_OTHER_BORROW(BigDecimal r19_OTHER_BORROW) {
		R19_OTHER_BORROW = r19_OTHER_BORROW;
	}

	public BigDecimal getR20_OTHER_BORROW() {
		return R20_OTHER_BORROW;
	}

	public void setR20_OTHER_BORROW(BigDecimal r20_OTHER_BORROW) {
		R20_OTHER_BORROW = r20_OTHER_BORROW;
	}

	public BigDecimal getR21_OTHER_BORROW() {
		return R21_OTHER_BORROW;
	}

	public void setR21_OTHER_BORROW(BigDecimal r21_OTHER_BORROW) {
		R21_OTHER_BORROW = r21_OTHER_BORROW;
	}

	public BigDecimal getR22_OTHER_BORROW() {
		return R22_OTHER_BORROW;
	}

	public void setR22_OTHER_BORROW(BigDecimal r22_OTHER_BORROW) {
		R22_OTHER_BORROW = r22_OTHER_BORROW;
	}

	public BigDecimal getR23_OTHER_BORROW() {
		return R23_OTHER_BORROW;
	}

	public void setR23_OTHER_BORROW(BigDecimal r23_OTHER_BORROW) {
		R23_OTHER_BORROW = r23_OTHER_BORROW;
	}

	public BigDecimal getR24_OTHER_BORROW() {
		return R24_OTHER_BORROW;
	}

	public void setR24_OTHER_BORROW(BigDecimal r24_OTHER_BORROW) {
		R24_OTHER_BORROW = r24_OTHER_BORROW;
	}

	public BigDecimal getR25_OTHER_BORROW() {
		return R25_OTHER_BORROW;
	}

	public void setR25_OTHER_BORROW(BigDecimal r25_OTHER_BORROW) {
		R25_OTHER_BORROW = r25_OTHER_BORROW;
	}

	public BigDecimal getR26_OTHER_BORROW() {
		return R26_OTHER_BORROW;
	}

	public void setR26_OTHER_BORROW(BigDecimal r26_OTHER_BORROW) {
		R26_OTHER_BORROW = r26_OTHER_BORROW;
	}

	public BigDecimal getR27_OTHER_BORROW() {
		return R27_OTHER_BORROW;
	}

	public void setR27_OTHER_BORROW(BigDecimal r27_OTHER_BORROW) {
		R27_OTHER_BORROW = r27_OTHER_BORROW;
	}

	public BigDecimal getR28_OTHER_BORROW() {
		return R28_OTHER_BORROW;
	}

	public void setR28_OTHER_BORROW(BigDecimal r28_OTHER_BORROW) {
		R28_OTHER_BORROW = r28_OTHER_BORROW;
	}

	public BigDecimal getR29_OTHER_BORROW() {
		return R29_OTHER_BORROW;
	}

	public void setR29_OTHER_BORROW(BigDecimal r29_OTHER_BORROW) {
		R29_OTHER_BORROW = r29_OTHER_BORROW;
	}

	public BigDecimal getR30_OTHER_BORROW() {
		return R30_OTHER_BORROW;
	}

	public void setR30_OTHER_BORROW(BigDecimal r30_OTHER_BORROW) {
		R30_OTHER_BORROW = r30_OTHER_BORROW;
	}

	public BigDecimal getR31_OTHER_BORROW() {
		return R31_OTHER_BORROW;
	}

	public void setR31_OTHER_BORROW(BigDecimal r31_OTHER_BORROW) {
		R31_OTHER_BORROW = r31_OTHER_BORROW;
	}

	public BigDecimal getR32_OTHER_BORROW() {
		return R32_OTHER_BORROW;
	}

	public void setR32_OTHER_BORROW(BigDecimal r32_OTHER_BORROW) {
		R32_OTHER_BORROW = r32_OTHER_BORROW;
	}

	public BigDecimal getR33_OTHER_BORROW() {
		return R33_OTHER_BORROW;
	}

	public void setR33_OTHER_BORROW(BigDecimal r33_OTHER_BORROW) {
		R33_OTHER_BORROW = r33_OTHER_BORROW;
	}

	public BigDecimal getR34_OTHER_BORROW() {
		return R34_OTHER_BORROW;
	}

	public void setR34_OTHER_BORROW(BigDecimal r34_OTHER_BORROW) {
		R34_OTHER_BORROW = r34_OTHER_BORROW;
	}

	public BigDecimal getR35_OTHER_BORROW() {
		return R35_OTHER_BORROW;
	}

	public void setR35_OTHER_BORROW(BigDecimal r35_OTHER_BORROW) {
		R35_OTHER_BORROW = r35_OTHER_BORROW;
	}

	public BigDecimal getR36_OTHER_BORROW() {
		return R36_OTHER_BORROW;
	}

	public void setR36_OTHER_BORROW(BigDecimal r36_OTHER_BORROW) {
		R36_OTHER_BORROW = r36_OTHER_BORROW;
	}

	public BigDecimal getR37_OTHER_BORROW() {
		return R37_OTHER_BORROW;
	}

	public void setR37_OTHER_BORROW(BigDecimal r37_OTHER_BORROW) {
		R37_OTHER_BORROW = r37_OTHER_BORROW;
	}

	public BigDecimal getR38_OTHER_BORROW() {
		return R38_OTHER_BORROW;
	}

	public void setR38_OTHER_BORROW(BigDecimal r38_OTHER_BORROW) {
		R38_OTHER_BORROW = r38_OTHER_BORROW;
	}

	public BigDecimal getR39_OTHER_BORROW() {
		return R39_OTHER_BORROW;
	}

	public void setR39_OTHER_BORROW(BigDecimal r39_OTHER_BORROW) {
		R39_OTHER_BORROW = r39_OTHER_BORROW;
	}

	public BigDecimal getR40_OTHER_BORROW() {
		return R40_OTHER_BORROW;
	}

	public void setR40_OTHER_BORROW(BigDecimal r40_OTHER_BORROW) {
		R40_OTHER_BORROW = r40_OTHER_BORROW;
	}

	public BigDecimal getR41_OTHER_BORROW() {
		return R41_OTHER_BORROW;
	}

	public void setR41_OTHER_BORROW(BigDecimal r41_OTHER_BORROW) {
		R41_OTHER_BORROW = r41_OTHER_BORROW;
	}

	public BigDecimal getR42_OTHER_BORROW() {
		return R42_OTHER_BORROW;
	}

	public void setR42_OTHER_BORROW(BigDecimal r42_OTHER_BORROW) {
		R42_OTHER_BORROW = r42_OTHER_BORROW;
	}

	public BigDecimal getR43_OTHER_BORROW() {
		return R43_OTHER_BORROW;
	}

	public void setR43_OTHER_BORROW(BigDecimal r43_OTHER_BORROW) {
		R43_OTHER_BORROW = r43_OTHER_BORROW;
	}

	public BigDecimal getR44_OTHER_BORROW() {
		return R44_OTHER_BORROW;
	}

	public void setR44_OTHER_BORROW(BigDecimal r44_OTHER_BORROW) {
		R44_OTHER_BORROW = r44_OTHER_BORROW;
	}

	public BigDecimal getR45_OTHER_BORROW() {
		return R45_OTHER_BORROW;
	}

	public void setR45_OTHER_BORROW(BigDecimal r45_OTHER_BORROW) {
		R45_OTHER_BORROW = r45_OTHER_BORROW;
	}

	public BigDecimal getR46_OTHER_BORROW() {
		return R46_OTHER_BORROW;
	}

	public void setR46_OTHER_BORROW(BigDecimal r46_OTHER_BORROW) {
		R46_OTHER_BORROW = r46_OTHER_BORROW;
	}

	public BigDecimal getR47_OTHER_BORROW() {
		return R47_OTHER_BORROW;
	}

	public void setR47_OTHER_BORROW(BigDecimal r47_OTHER_BORROW) {
		R47_OTHER_BORROW = r47_OTHER_BORROW;
	}

	public BigDecimal getR48_OTHER_BORROW() {
		return R48_OTHER_BORROW;
	}

	public void setR48_OTHER_BORROW(BigDecimal r48_OTHER_BORROW) {
		R48_OTHER_BORROW = r48_OTHER_BORROW;
	}

	public BigDecimal getR49_OTHER_BORROW() {
		return R49_OTHER_BORROW;
	}

	public void setR49_OTHER_BORROW(BigDecimal r49_OTHER_BORROW) {
		R49_OTHER_BORROW = r49_OTHER_BORROW;
	}

	public BigDecimal getR50_OTHER_BORROW() {
		return R50_OTHER_BORROW;
	}

	public void setR50_OTHER_BORROW(BigDecimal r50_OTHER_BORROW) {
		R50_OTHER_BORROW = r50_OTHER_BORROW;
	}

	public BigDecimal getR51_OTHER_BORROW() {
		return R51_OTHER_BORROW;
	}

	public void setR51_OTHER_BORROW(BigDecimal r51_OTHER_BORROW) {
		R51_OTHER_BORROW = r51_OTHER_BORROW;
	}

	public BigDecimal getR52_OTHER_BORROW() {
		return R52_OTHER_BORROW;
	}

	public void setR52_OTHER_BORROW(BigDecimal r52_OTHER_BORROW) {
		R52_OTHER_BORROW = r52_OTHER_BORROW;
	}

	public BigDecimal getR53_OTHER_BORROW() {
		return R53_OTHER_BORROW;
	}

	public void setR53_OTHER_BORROW(BigDecimal r53_OTHER_BORROW) {
		R53_OTHER_BORROW = r53_OTHER_BORROW;
	}

	public BigDecimal getR54_OTHER_BORROW() {
		return R54_OTHER_BORROW;
	}

	public void setR54_OTHER_BORROW(BigDecimal r54_OTHER_BORROW) {
		R54_OTHER_BORROW = r54_OTHER_BORROW;
	}

	public BigDecimal getR55_OTHER_BORROW() {
		return R55_OTHER_BORROW;
	}

	public void setR55_OTHER_BORROW(BigDecimal r55_OTHER_BORROW) {
		R55_OTHER_BORROW = r55_OTHER_BORROW;
	}

	public BigDecimal getR56_OTHER_BORROW() {
		return R56_OTHER_BORROW;
	}

	public void setR56_OTHER_BORROW(BigDecimal r56_OTHER_BORROW) {
		R56_OTHER_BORROW = r56_OTHER_BORROW;
	}

	public BigDecimal getR57_OTHER_BORROW() {
		return R57_OTHER_BORROW;
	}

	public void setR57_OTHER_BORROW(BigDecimal r57_OTHER_BORROW) {
		R57_OTHER_BORROW = r57_OTHER_BORROW;
	}

	public BigDecimal getR58_OTHER_BORROW() {
		return R58_OTHER_BORROW;
	}

	public void setR58_OTHER_BORROW(BigDecimal r58_OTHER_BORROW) {
		R58_OTHER_BORROW = r58_OTHER_BORROW;
	}

	public BigDecimal getR59_OTHER_BORROW() {
		return R59_OTHER_BORROW;
	}

	public void setR59_OTHER_BORROW(BigDecimal r59_OTHER_BORROW) {
		R59_OTHER_BORROW = r59_OTHER_BORROW;
	}

	public BigDecimal getR60_OTHER_BORROW() {
		return R60_OTHER_BORROW;
	}

	public void setR60_OTHER_BORROW(BigDecimal r60_OTHER_BORROW) {
		R60_OTHER_BORROW = r60_OTHER_BORROW;
	}

	public BigDecimal getR61_OTHER_BORROW() {
		return R61_OTHER_BORROW;
	}

	public void setR61_OTHER_BORROW(BigDecimal r61_OTHER_BORROW) {
		R61_OTHER_BORROW = r61_OTHER_BORROW;
	}

	public BigDecimal getR62_OTHER_BORROW() {
		return R62_OTHER_BORROW;
	}

	public void setR62_OTHER_BORROW(BigDecimal r62_OTHER_BORROW) {
		R62_OTHER_BORROW = r62_OTHER_BORROW;
	}

	public BigDecimal getR63_OTHER_BORROW() {
		return R63_OTHER_BORROW;
	}

	public void setR63_OTHER_BORROW(BigDecimal r63_OTHER_BORROW) {
		R63_OTHER_BORROW = r63_OTHER_BORROW;
	}

	public BigDecimal getR64_OTHER_BORROW() {
		return R64_OTHER_BORROW;
	}

	public void setR64_OTHER_BORROW(BigDecimal r64_OTHER_BORROW) {
		R64_OTHER_BORROW = r64_OTHER_BORROW;
	}

	public M_OB_Detail_Entity() {
        super();
    }

}

// ------------------------------
// Archival Summary Entity class for M_OB
// ------------------------------
public static class M_OB_Archival_Summary_Entity {
	
	
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	
	private Date reportResubDate;
	
    
    public String REPORT_FREQUENCY;
    public String REPORT_CODE;
    public String REPORT_DESC;
    public String ENTITY_FLG;
    public String MODIFY_FLG;
    public String DEL_FLG;

    
	
	
	private BigDecimal R11_OTHER_BORROW;
	private BigDecimal R12_OTHER_BORROW;
	private BigDecimal R13_OTHER_BORROW;
	private BigDecimal R14_OTHER_BORROW;
	private BigDecimal R15_OTHER_BORROW;
	private BigDecimal R16_OTHER_BORROW;
	private BigDecimal R17_OTHER_BORROW;
	private BigDecimal R18_OTHER_BORROW;
	private BigDecimal R19_OTHER_BORROW;
	private BigDecimal R20_OTHER_BORROW;
	private BigDecimal R21_OTHER_BORROW;
	private BigDecimal R22_OTHER_BORROW;
	private BigDecimal R23_OTHER_BORROW;
	private BigDecimal R24_OTHER_BORROW;
	private BigDecimal R25_OTHER_BORROW;
	private BigDecimal R26_OTHER_BORROW;
	private BigDecimal R27_OTHER_BORROW;
	private BigDecimal R28_OTHER_BORROW;
	private BigDecimal R29_OTHER_BORROW;
	private BigDecimal R30_OTHER_BORROW;
	private BigDecimal R31_OTHER_BORROW;
	private BigDecimal R32_OTHER_BORROW;
	private BigDecimal R33_OTHER_BORROW;
	private BigDecimal R34_OTHER_BORROW;
	private BigDecimal R35_OTHER_BORROW;
	private BigDecimal R36_OTHER_BORROW;
	private BigDecimal R37_OTHER_BORROW;
	private BigDecimal R38_OTHER_BORROW;
	private BigDecimal R39_OTHER_BORROW;
	private BigDecimal R40_OTHER_BORROW;
	private BigDecimal R41_OTHER_BORROW;
	private BigDecimal R42_OTHER_BORROW;
	private BigDecimal R43_OTHER_BORROW;
	private BigDecimal R44_OTHER_BORROW;
	private BigDecimal R45_OTHER_BORROW;
	private BigDecimal R46_OTHER_BORROW;
	private BigDecimal R47_OTHER_BORROW;
	private BigDecimal R48_OTHER_BORROW;
	private BigDecimal R49_OTHER_BORROW;
	private BigDecimal R50_OTHER_BORROW;
	private BigDecimal R51_OTHER_BORROW;
	private BigDecimal R52_OTHER_BORROW;
	private BigDecimal R53_OTHER_BORROW;
	private BigDecimal R54_OTHER_BORROW;
	private BigDecimal R55_OTHER_BORROW;
	private BigDecimal R56_OTHER_BORROW;
	private BigDecimal R57_OTHER_BORROW;
	private BigDecimal R58_OTHER_BORROW;
	private BigDecimal R59_OTHER_BORROW;
	private BigDecimal R60_OTHER_BORROW;
	private BigDecimal R61_OTHER_BORROW;
	private BigDecimal R62_OTHER_BORROW;
	private BigDecimal R63_OTHER_BORROW;
	private BigDecimal R64_OTHER_BORROW;

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

	public BigDecimal getR11_OTHER_BORROW() {
		return R11_OTHER_BORROW;
	}

	public void setR11_OTHER_BORROW(BigDecimal r11_OTHER_BORROW) {
		R11_OTHER_BORROW = r11_OTHER_BORROW;
	}

	public BigDecimal getR12_OTHER_BORROW() {
		return R12_OTHER_BORROW;
	}

	public void setR12_OTHER_BORROW(BigDecimal r12_OTHER_BORROW) {
		R12_OTHER_BORROW = r12_OTHER_BORROW;
	}

	public BigDecimal getR13_OTHER_BORROW() {
		return R13_OTHER_BORROW;
	}

	public void setR13_OTHER_BORROW(BigDecimal r13_OTHER_BORROW) {
		R13_OTHER_BORROW = r13_OTHER_BORROW;
	}

	public BigDecimal getR14_OTHER_BORROW() {
		return R14_OTHER_BORROW;
	}

	public void setR14_OTHER_BORROW(BigDecimal r14_OTHER_BORROW) {
		R14_OTHER_BORROW = r14_OTHER_BORROW;
	}

	public BigDecimal getR15_OTHER_BORROW() {
		return R15_OTHER_BORROW;
	}

	public void setR15_OTHER_BORROW(BigDecimal r15_OTHER_BORROW) {
		R15_OTHER_BORROW = r15_OTHER_BORROW;
	}

	public BigDecimal getR16_OTHER_BORROW() {
		return R16_OTHER_BORROW;
	}

	public void setR16_OTHER_BORROW(BigDecimal r16_OTHER_BORROW) {
		R16_OTHER_BORROW = r16_OTHER_BORROW;
	}

	public BigDecimal getR17_OTHER_BORROW() {
		return R17_OTHER_BORROW;
	}

	public void setR17_OTHER_BORROW(BigDecimal r17_OTHER_BORROW) {
		R17_OTHER_BORROW = r17_OTHER_BORROW;
	}

	public BigDecimal getR18_OTHER_BORROW() {
		return R18_OTHER_BORROW;
	}

	public void setR18_OTHER_BORROW(BigDecimal r18_OTHER_BORROW) {
		R18_OTHER_BORROW = r18_OTHER_BORROW;
	}

	public BigDecimal getR19_OTHER_BORROW() {
		return R19_OTHER_BORROW;
	}

	public void setR19_OTHER_BORROW(BigDecimal r19_OTHER_BORROW) {
		R19_OTHER_BORROW = r19_OTHER_BORROW;
	}

	public BigDecimal getR20_OTHER_BORROW() {
		return R20_OTHER_BORROW;
	}

	public void setR20_OTHER_BORROW(BigDecimal r20_OTHER_BORROW) {
		R20_OTHER_BORROW = r20_OTHER_BORROW;
	}

	public BigDecimal getR21_OTHER_BORROW() {
		return R21_OTHER_BORROW;
	}

	public void setR21_OTHER_BORROW(BigDecimal r21_OTHER_BORROW) {
		R21_OTHER_BORROW = r21_OTHER_BORROW;
	}

	public BigDecimal getR22_OTHER_BORROW() {
		return R22_OTHER_BORROW;
	}

	public void setR22_OTHER_BORROW(BigDecimal r22_OTHER_BORROW) {
		R22_OTHER_BORROW = r22_OTHER_BORROW;
	}

	public BigDecimal getR23_OTHER_BORROW() {
		return R23_OTHER_BORROW;
	}

	public void setR23_OTHER_BORROW(BigDecimal r23_OTHER_BORROW) {
		R23_OTHER_BORROW = r23_OTHER_BORROW;
	}

	public BigDecimal getR24_OTHER_BORROW() {
		return R24_OTHER_BORROW;
	}

	public void setR24_OTHER_BORROW(BigDecimal r24_OTHER_BORROW) {
		R24_OTHER_BORROW = r24_OTHER_BORROW;
	}

	public BigDecimal getR25_OTHER_BORROW() {
		return R25_OTHER_BORROW;
	}

	public void setR25_OTHER_BORROW(BigDecimal r25_OTHER_BORROW) {
		R25_OTHER_BORROW = r25_OTHER_BORROW;
	}

	public BigDecimal getR26_OTHER_BORROW() {
		return R26_OTHER_BORROW;
	}

	public void setR26_OTHER_BORROW(BigDecimal r26_OTHER_BORROW) {
		R26_OTHER_BORROW = r26_OTHER_BORROW;
	}

	public BigDecimal getR27_OTHER_BORROW() {
		return R27_OTHER_BORROW;
	}

	public void setR27_OTHER_BORROW(BigDecimal r27_OTHER_BORROW) {
		R27_OTHER_BORROW = r27_OTHER_BORROW;
	}

	public BigDecimal getR28_OTHER_BORROW() {
		return R28_OTHER_BORROW;
	}

	public void setR28_OTHER_BORROW(BigDecimal r28_OTHER_BORROW) {
		R28_OTHER_BORROW = r28_OTHER_BORROW;
	}

	public BigDecimal getR29_OTHER_BORROW() {
		return R29_OTHER_BORROW;
	}

	public void setR29_OTHER_BORROW(BigDecimal r29_OTHER_BORROW) {
		R29_OTHER_BORROW = r29_OTHER_BORROW;
	}

	public BigDecimal getR30_OTHER_BORROW() {
		return R30_OTHER_BORROW;
	}

	public void setR30_OTHER_BORROW(BigDecimal r30_OTHER_BORROW) {
		R30_OTHER_BORROW = r30_OTHER_BORROW;
	}

	public BigDecimal getR31_OTHER_BORROW() {
		return R31_OTHER_BORROW;
	}

	public void setR31_OTHER_BORROW(BigDecimal r31_OTHER_BORROW) {
		R31_OTHER_BORROW = r31_OTHER_BORROW;
	}

	public BigDecimal getR32_OTHER_BORROW() {
		return R32_OTHER_BORROW;
	}

	public void setR32_OTHER_BORROW(BigDecimal r32_OTHER_BORROW) {
		R32_OTHER_BORROW = r32_OTHER_BORROW;
	}

	public BigDecimal getR33_OTHER_BORROW() {
		return R33_OTHER_BORROW;
	}

	public void setR33_OTHER_BORROW(BigDecimal r33_OTHER_BORROW) {
		R33_OTHER_BORROW = r33_OTHER_BORROW;
	}

	public BigDecimal getR34_OTHER_BORROW() {
		return R34_OTHER_BORROW;
	}

	public void setR34_OTHER_BORROW(BigDecimal r34_OTHER_BORROW) {
		R34_OTHER_BORROW = r34_OTHER_BORROW;
	}

	public BigDecimal getR35_OTHER_BORROW() {
		return R35_OTHER_BORROW;
	}

	public void setR35_OTHER_BORROW(BigDecimal r35_OTHER_BORROW) {
		R35_OTHER_BORROW = r35_OTHER_BORROW;
	}

	public BigDecimal getR36_OTHER_BORROW() {
		return R36_OTHER_BORROW;
	}

	public void setR36_OTHER_BORROW(BigDecimal r36_OTHER_BORROW) {
		R36_OTHER_BORROW = r36_OTHER_BORROW;
	}

	public BigDecimal getR37_OTHER_BORROW() {
		return R37_OTHER_BORROW;
	}

	public void setR37_OTHER_BORROW(BigDecimal r37_OTHER_BORROW) {
		R37_OTHER_BORROW = r37_OTHER_BORROW;
	}

	public BigDecimal getR38_OTHER_BORROW() {
		return R38_OTHER_BORROW;
	}

	public void setR38_OTHER_BORROW(BigDecimal r38_OTHER_BORROW) {
		R38_OTHER_BORROW = r38_OTHER_BORROW;
	}

	public BigDecimal getR39_OTHER_BORROW() {
		return R39_OTHER_BORROW;
	}

	public void setR39_OTHER_BORROW(BigDecimal r39_OTHER_BORROW) {
		R39_OTHER_BORROW = r39_OTHER_BORROW;
	}

	public BigDecimal getR40_OTHER_BORROW() {
		return R40_OTHER_BORROW;
	}

	public void setR40_OTHER_BORROW(BigDecimal r40_OTHER_BORROW) {
		R40_OTHER_BORROW = r40_OTHER_BORROW;
	}

	public BigDecimal getR41_OTHER_BORROW() {
		return R41_OTHER_BORROW;
	}

	public void setR41_OTHER_BORROW(BigDecimal r41_OTHER_BORROW) {
		R41_OTHER_BORROW = r41_OTHER_BORROW;
	}

	public BigDecimal getR42_OTHER_BORROW() {
		return R42_OTHER_BORROW;
	}

	public void setR42_OTHER_BORROW(BigDecimal r42_OTHER_BORROW) {
		R42_OTHER_BORROW = r42_OTHER_BORROW;
	}

	public BigDecimal getR43_OTHER_BORROW() {
		return R43_OTHER_BORROW;
	}

	public void setR43_OTHER_BORROW(BigDecimal r43_OTHER_BORROW) {
		R43_OTHER_BORROW = r43_OTHER_BORROW;
	}

	public BigDecimal getR44_OTHER_BORROW() {
		return R44_OTHER_BORROW;
	}

	public void setR44_OTHER_BORROW(BigDecimal r44_OTHER_BORROW) {
		R44_OTHER_BORROW = r44_OTHER_BORROW;
	}

	public BigDecimal getR45_OTHER_BORROW() {
		return R45_OTHER_BORROW;
	}

	public void setR45_OTHER_BORROW(BigDecimal r45_OTHER_BORROW) {
		R45_OTHER_BORROW = r45_OTHER_BORROW;
	}

	public BigDecimal getR46_OTHER_BORROW() {
		return R46_OTHER_BORROW;
	}

	public void setR46_OTHER_BORROW(BigDecimal r46_OTHER_BORROW) {
		R46_OTHER_BORROW = r46_OTHER_BORROW;
	}

	public BigDecimal getR47_OTHER_BORROW() {
		return R47_OTHER_BORROW;
	}

	public void setR47_OTHER_BORROW(BigDecimal r47_OTHER_BORROW) {
		R47_OTHER_BORROW = r47_OTHER_BORROW;
	}

	public BigDecimal getR48_OTHER_BORROW() {
		return R48_OTHER_BORROW;
	}

	public void setR48_OTHER_BORROW(BigDecimal r48_OTHER_BORROW) {
		R48_OTHER_BORROW = r48_OTHER_BORROW;
	}

	public BigDecimal getR49_OTHER_BORROW() {
		return R49_OTHER_BORROW;
	}

	public void setR49_OTHER_BORROW(BigDecimal r49_OTHER_BORROW) {
		R49_OTHER_BORROW = r49_OTHER_BORROW;
	}

	public BigDecimal getR50_OTHER_BORROW() {
		return R50_OTHER_BORROW;
	}

	public void setR50_OTHER_BORROW(BigDecimal r50_OTHER_BORROW) {
		R50_OTHER_BORROW = r50_OTHER_BORROW;
	}

	public BigDecimal getR51_OTHER_BORROW() {
		return R51_OTHER_BORROW;
	}

	public void setR51_OTHER_BORROW(BigDecimal r51_OTHER_BORROW) {
		R51_OTHER_BORROW = r51_OTHER_BORROW;
	}

	public BigDecimal getR52_OTHER_BORROW() {
		return R52_OTHER_BORROW;
	}

	public void setR52_OTHER_BORROW(BigDecimal r52_OTHER_BORROW) {
		R52_OTHER_BORROW = r52_OTHER_BORROW;
	}

	public BigDecimal getR53_OTHER_BORROW() {
		return R53_OTHER_BORROW;
	}

	public void setR53_OTHER_BORROW(BigDecimal r53_OTHER_BORROW) {
		R53_OTHER_BORROW = r53_OTHER_BORROW;
	}

	public BigDecimal getR54_OTHER_BORROW() {
		return R54_OTHER_BORROW;
	}

	public void setR54_OTHER_BORROW(BigDecimal r54_OTHER_BORROW) {
		R54_OTHER_BORROW = r54_OTHER_BORROW;
	}

	public BigDecimal getR55_OTHER_BORROW() {
		return R55_OTHER_BORROW;
	}

	public void setR55_OTHER_BORROW(BigDecimal r55_OTHER_BORROW) {
		R55_OTHER_BORROW = r55_OTHER_BORROW;
	}

	public BigDecimal getR56_OTHER_BORROW() {
		return R56_OTHER_BORROW;
	}

	public void setR56_OTHER_BORROW(BigDecimal r56_OTHER_BORROW) {
		R56_OTHER_BORROW = r56_OTHER_BORROW;
	}

	public BigDecimal getR57_OTHER_BORROW() {
		return R57_OTHER_BORROW;
	}

	public void setR57_OTHER_BORROW(BigDecimal r57_OTHER_BORROW) {
		R57_OTHER_BORROW = r57_OTHER_BORROW;
	}

	public BigDecimal getR58_OTHER_BORROW() {
		return R58_OTHER_BORROW;
	}

	public void setR58_OTHER_BORROW(BigDecimal r58_OTHER_BORROW) {
		R58_OTHER_BORROW = r58_OTHER_BORROW;
	}

	public BigDecimal getR59_OTHER_BORROW() {
		return R59_OTHER_BORROW;
	}

	public void setR59_OTHER_BORROW(BigDecimal r59_OTHER_BORROW) {
		R59_OTHER_BORROW = r59_OTHER_BORROW;
	}

	public BigDecimal getR60_OTHER_BORROW() {
		return R60_OTHER_BORROW;
	}

	public void setR60_OTHER_BORROW(BigDecimal r60_OTHER_BORROW) {
		R60_OTHER_BORROW = r60_OTHER_BORROW;
	}

	public BigDecimal getR61_OTHER_BORROW() {
		return R61_OTHER_BORROW;
	}

	public void setR61_OTHER_BORROW(BigDecimal r61_OTHER_BORROW) {
		R61_OTHER_BORROW = r61_OTHER_BORROW;
	}

	public BigDecimal getR62_OTHER_BORROW() {
		return R62_OTHER_BORROW;
	}

	public void setR62_OTHER_BORROW(BigDecimal r62_OTHER_BORROW) {
		R62_OTHER_BORROW = r62_OTHER_BORROW;
	}

	public BigDecimal getR63_OTHER_BORROW() {
		return R63_OTHER_BORROW;
	}

	public void setR63_OTHER_BORROW(BigDecimal r63_OTHER_BORROW) {
		R63_OTHER_BORROW = r63_OTHER_BORROW;
	}

	public BigDecimal getR64_OTHER_BORROW() {
		return R64_OTHER_BORROW;
	}

	public void setR64_OTHER_BORROW(BigDecimal r64_OTHER_BORROW) {
		R64_OTHER_BORROW = r64_OTHER_BORROW;
	}

	public M_OB_Archival_Summary_Entity() {
        super();
    }

}

// ------------------------------
// Archival Detail Entity class for M_OB
// ------------------------------
public static class M_OB_Archival_Detail_Entity {
	
	
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	
	private Date reportResubDate;
	
    
    public String REPORT_FREQUENCY;
    public String REPORT_CODE;
    public String REPORT_DESC;
    public String ENTITY_FLG;
    public String MODIFY_FLG;
    public String DEL_FLG;

    
	
	
	private BigDecimal R11_OTHER_BORROW;
	private BigDecimal R12_OTHER_BORROW;
	private BigDecimal R13_OTHER_BORROW;
	private BigDecimal R14_OTHER_BORROW;
	private BigDecimal R15_OTHER_BORROW;
	private BigDecimal R16_OTHER_BORROW;
	private BigDecimal R17_OTHER_BORROW;
	private BigDecimal R18_OTHER_BORROW;
	private BigDecimal R19_OTHER_BORROW;
	private BigDecimal R20_OTHER_BORROW;
	private BigDecimal R21_OTHER_BORROW;
	private BigDecimal R22_OTHER_BORROW;
	private BigDecimal R23_OTHER_BORROW;
	private BigDecimal R24_OTHER_BORROW;
	private BigDecimal R25_OTHER_BORROW;
	private BigDecimal R26_OTHER_BORROW;
	private BigDecimal R27_OTHER_BORROW;
	private BigDecimal R28_OTHER_BORROW;
	private BigDecimal R29_OTHER_BORROW;
	private BigDecimal R30_OTHER_BORROW;
	private BigDecimal R31_OTHER_BORROW;
	private BigDecimal R32_OTHER_BORROW;
	private BigDecimal R33_OTHER_BORROW;
	private BigDecimal R34_OTHER_BORROW;
	private BigDecimal R35_OTHER_BORROW;
	private BigDecimal R36_OTHER_BORROW;
	private BigDecimal R37_OTHER_BORROW;
	private BigDecimal R38_OTHER_BORROW;
	private BigDecimal R39_OTHER_BORROW;
	private BigDecimal R40_OTHER_BORROW;
	private BigDecimal R41_OTHER_BORROW;
	private BigDecimal R42_OTHER_BORROW;
	private BigDecimal R43_OTHER_BORROW;
	private BigDecimal R44_OTHER_BORROW;
	private BigDecimal R45_OTHER_BORROW;
	private BigDecimal R46_OTHER_BORROW;
	private BigDecimal R47_OTHER_BORROW;
	private BigDecimal R48_OTHER_BORROW;
	private BigDecimal R49_OTHER_BORROW;
	private BigDecimal R50_OTHER_BORROW;
	private BigDecimal R51_OTHER_BORROW;
	private BigDecimal R52_OTHER_BORROW;
	private BigDecimal R53_OTHER_BORROW;
	private BigDecimal R54_OTHER_BORROW;
	private BigDecimal R55_OTHER_BORROW;
	private BigDecimal R56_OTHER_BORROW;
	private BigDecimal R57_OTHER_BORROW;
	private BigDecimal R58_OTHER_BORROW;
	private BigDecimal R59_OTHER_BORROW;
	private BigDecimal R60_OTHER_BORROW;
	private BigDecimal R61_OTHER_BORROW;
	private BigDecimal R62_OTHER_BORROW;
	private BigDecimal R63_OTHER_BORROW;
	private BigDecimal R64_OTHER_BORROW;

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

	public BigDecimal getR11_OTHER_BORROW() {
		return R11_OTHER_BORROW;
	}

	public void setR11_OTHER_BORROW(BigDecimal r11_OTHER_BORROW) {
		R11_OTHER_BORROW = r11_OTHER_BORROW;
	}

	public BigDecimal getR12_OTHER_BORROW() {
		return R12_OTHER_BORROW;
	}

	public void setR12_OTHER_BORROW(BigDecimal r12_OTHER_BORROW) {
		R12_OTHER_BORROW = r12_OTHER_BORROW;
	}

	public BigDecimal getR13_OTHER_BORROW() {
		return R13_OTHER_BORROW;
	}

	public void setR13_OTHER_BORROW(BigDecimal r13_OTHER_BORROW) {
		R13_OTHER_BORROW = r13_OTHER_BORROW;
	}

	public BigDecimal getR14_OTHER_BORROW() {
		return R14_OTHER_BORROW;
	}

	public void setR14_OTHER_BORROW(BigDecimal r14_OTHER_BORROW) {
		R14_OTHER_BORROW = r14_OTHER_BORROW;
	}

	public BigDecimal getR15_OTHER_BORROW() {
		return R15_OTHER_BORROW;
	}

	public void setR15_OTHER_BORROW(BigDecimal r15_OTHER_BORROW) {
		R15_OTHER_BORROW = r15_OTHER_BORROW;
	}

	public BigDecimal getR16_OTHER_BORROW() {
		return R16_OTHER_BORROW;
	}

	public void setR16_OTHER_BORROW(BigDecimal r16_OTHER_BORROW) {
		R16_OTHER_BORROW = r16_OTHER_BORROW;
	}

	public BigDecimal getR17_OTHER_BORROW() {
		return R17_OTHER_BORROW;
	}

	public void setR17_OTHER_BORROW(BigDecimal r17_OTHER_BORROW) {
		R17_OTHER_BORROW = r17_OTHER_BORROW;
	}

	public BigDecimal getR18_OTHER_BORROW() {
		return R18_OTHER_BORROW;
	}

	public void setR18_OTHER_BORROW(BigDecimal r18_OTHER_BORROW) {
		R18_OTHER_BORROW = r18_OTHER_BORROW;
	}

	public BigDecimal getR19_OTHER_BORROW() {
		return R19_OTHER_BORROW;
	}

	public void setR19_OTHER_BORROW(BigDecimal r19_OTHER_BORROW) {
		R19_OTHER_BORROW = r19_OTHER_BORROW;
	}

	public BigDecimal getR20_OTHER_BORROW() {
		return R20_OTHER_BORROW;
	}

	public void setR20_OTHER_BORROW(BigDecimal r20_OTHER_BORROW) {
		R20_OTHER_BORROW = r20_OTHER_BORROW;
	}

	public BigDecimal getR21_OTHER_BORROW() {
		return R21_OTHER_BORROW;
	}

	public void setR21_OTHER_BORROW(BigDecimal r21_OTHER_BORROW) {
		R21_OTHER_BORROW = r21_OTHER_BORROW;
	}

	public BigDecimal getR22_OTHER_BORROW() {
		return R22_OTHER_BORROW;
	}

	public void setR22_OTHER_BORROW(BigDecimal r22_OTHER_BORROW) {
		R22_OTHER_BORROW = r22_OTHER_BORROW;
	}

	public BigDecimal getR23_OTHER_BORROW() {
		return R23_OTHER_BORROW;
	}

	public void setR23_OTHER_BORROW(BigDecimal r23_OTHER_BORROW) {
		R23_OTHER_BORROW = r23_OTHER_BORROW;
	}

	public BigDecimal getR24_OTHER_BORROW() {
		return R24_OTHER_BORROW;
	}

	public void setR24_OTHER_BORROW(BigDecimal r24_OTHER_BORROW) {
		R24_OTHER_BORROW = r24_OTHER_BORROW;
	}

	public BigDecimal getR25_OTHER_BORROW() {
		return R25_OTHER_BORROW;
	}

	public void setR25_OTHER_BORROW(BigDecimal r25_OTHER_BORROW) {
		R25_OTHER_BORROW = r25_OTHER_BORROW;
	}

	public BigDecimal getR26_OTHER_BORROW() {
		return R26_OTHER_BORROW;
	}

	public void setR26_OTHER_BORROW(BigDecimal r26_OTHER_BORROW) {
		R26_OTHER_BORROW = r26_OTHER_BORROW;
	}

	public BigDecimal getR27_OTHER_BORROW() {
		return R27_OTHER_BORROW;
	}

	public void setR27_OTHER_BORROW(BigDecimal r27_OTHER_BORROW) {
		R27_OTHER_BORROW = r27_OTHER_BORROW;
	}

	public BigDecimal getR28_OTHER_BORROW() {
		return R28_OTHER_BORROW;
	}

	public void setR28_OTHER_BORROW(BigDecimal r28_OTHER_BORROW) {
		R28_OTHER_BORROW = r28_OTHER_BORROW;
	}

	public BigDecimal getR29_OTHER_BORROW() {
		return R29_OTHER_BORROW;
	}

	public void setR29_OTHER_BORROW(BigDecimal r29_OTHER_BORROW) {
		R29_OTHER_BORROW = r29_OTHER_BORROW;
	}

	public BigDecimal getR30_OTHER_BORROW() {
		return R30_OTHER_BORROW;
	}

	public void setR30_OTHER_BORROW(BigDecimal r30_OTHER_BORROW) {
		R30_OTHER_BORROW = r30_OTHER_BORROW;
	}

	public BigDecimal getR31_OTHER_BORROW() {
		return R31_OTHER_BORROW;
	}

	public void setR31_OTHER_BORROW(BigDecimal r31_OTHER_BORROW) {
		R31_OTHER_BORROW = r31_OTHER_BORROW;
	}

	public BigDecimal getR32_OTHER_BORROW() {
		return R32_OTHER_BORROW;
	}

	public void setR32_OTHER_BORROW(BigDecimal r32_OTHER_BORROW) {
		R32_OTHER_BORROW = r32_OTHER_BORROW;
	}

	public BigDecimal getR33_OTHER_BORROW() {
		return R33_OTHER_BORROW;
	}

	public void setR33_OTHER_BORROW(BigDecimal r33_OTHER_BORROW) {
		R33_OTHER_BORROW = r33_OTHER_BORROW;
	}

	public BigDecimal getR34_OTHER_BORROW() {
		return R34_OTHER_BORROW;
	}

	public void setR34_OTHER_BORROW(BigDecimal r34_OTHER_BORROW) {
		R34_OTHER_BORROW = r34_OTHER_BORROW;
	}

	public BigDecimal getR35_OTHER_BORROW() {
		return R35_OTHER_BORROW;
	}

	public void setR35_OTHER_BORROW(BigDecimal r35_OTHER_BORROW) {
		R35_OTHER_BORROW = r35_OTHER_BORROW;
	}

	public BigDecimal getR36_OTHER_BORROW() {
		return R36_OTHER_BORROW;
	}

	public void setR36_OTHER_BORROW(BigDecimal r36_OTHER_BORROW) {
		R36_OTHER_BORROW = r36_OTHER_BORROW;
	}

	public BigDecimal getR37_OTHER_BORROW() {
		return R37_OTHER_BORROW;
	}

	public void setR37_OTHER_BORROW(BigDecimal r37_OTHER_BORROW) {
		R37_OTHER_BORROW = r37_OTHER_BORROW;
	}

	public BigDecimal getR38_OTHER_BORROW() {
		return R38_OTHER_BORROW;
	}

	public void setR38_OTHER_BORROW(BigDecimal r38_OTHER_BORROW) {
		R38_OTHER_BORROW = r38_OTHER_BORROW;
	}

	public BigDecimal getR39_OTHER_BORROW() {
		return R39_OTHER_BORROW;
	}

	public void setR39_OTHER_BORROW(BigDecimal r39_OTHER_BORROW) {
		R39_OTHER_BORROW = r39_OTHER_BORROW;
	}

	public BigDecimal getR40_OTHER_BORROW() {
		return R40_OTHER_BORROW;
	}

	public void setR40_OTHER_BORROW(BigDecimal r40_OTHER_BORROW) {
		R40_OTHER_BORROW = r40_OTHER_BORROW;
	}

	public BigDecimal getR41_OTHER_BORROW() {
		return R41_OTHER_BORROW;
	}

	public void setR41_OTHER_BORROW(BigDecimal r41_OTHER_BORROW) {
		R41_OTHER_BORROW = r41_OTHER_BORROW;
	}

	public BigDecimal getR42_OTHER_BORROW() {
		return R42_OTHER_BORROW;
	}

	public void setR42_OTHER_BORROW(BigDecimal r42_OTHER_BORROW) {
		R42_OTHER_BORROW = r42_OTHER_BORROW;
	}

	public BigDecimal getR43_OTHER_BORROW() {
		return R43_OTHER_BORROW;
	}

	public void setR43_OTHER_BORROW(BigDecimal r43_OTHER_BORROW) {
		R43_OTHER_BORROW = r43_OTHER_BORROW;
	}

	public BigDecimal getR44_OTHER_BORROW() {
		return R44_OTHER_BORROW;
	}

	public void setR44_OTHER_BORROW(BigDecimal r44_OTHER_BORROW) {
		R44_OTHER_BORROW = r44_OTHER_BORROW;
	}

	public BigDecimal getR45_OTHER_BORROW() {
		return R45_OTHER_BORROW;
	}

	public void setR45_OTHER_BORROW(BigDecimal r45_OTHER_BORROW) {
		R45_OTHER_BORROW = r45_OTHER_BORROW;
	}

	public BigDecimal getR46_OTHER_BORROW() {
		return R46_OTHER_BORROW;
	}

	public void setR46_OTHER_BORROW(BigDecimal r46_OTHER_BORROW) {
		R46_OTHER_BORROW = r46_OTHER_BORROW;
	}

	public BigDecimal getR47_OTHER_BORROW() {
		return R47_OTHER_BORROW;
	}

	public void setR47_OTHER_BORROW(BigDecimal r47_OTHER_BORROW) {
		R47_OTHER_BORROW = r47_OTHER_BORROW;
	}

	public BigDecimal getR48_OTHER_BORROW() {
		return R48_OTHER_BORROW;
	}

	public void setR48_OTHER_BORROW(BigDecimal r48_OTHER_BORROW) {
		R48_OTHER_BORROW = r48_OTHER_BORROW;
	}

	public BigDecimal getR49_OTHER_BORROW() {
		return R49_OTHER_BORROW;
	}

	public void setR49_OTHER_BORROW(BigDecimal r49_OTHER_BORROW) {
		R49_OTHER_BORROW = r49_OTHER_BORROW;
	}

	public BigDecimal getR50_OTHER_BORROW() {
		return R50_OTHER_BORROW;
	}

	public void setR50_OTHER_BORROW(BigDecimal r50_OTHER_BORROW) {
		R50_OTHER_BORROW = r50_OTHER_BORROW;
	}

	public BigDecimal getR51_OTHER_BORROW() {
		return R51_OTHER_BORROW;
	}

	public void setR51_OTHER_BORROW(BigDecimal r51_OTHER_BORROW) {
		R51_OTHER_BORROW = r51_OTHER_BORROW;
	}

	public BigDecimal getR52_OTHER_BORROW() {
		return R52_OTHER_BORROW;
	}

	public void setR52_OTHER_BORROW(BigDecimal r52_OTHER_BORROW) {
		R52_OTHER_BORROW = r52_OTHER_BORROW;
	}

	public BigDecimal getR53_OTHER_BORROW() {
		return R53_OTHER_BORROW;
	}

	public void setR53_OTHER_BORROW(BigDecimal r53_OTHER_BORROW) {
		R53_OTHER_BORROW = r53_OTHER_BORROW;
	}

	public BigDecimal getR54_OTHER_BORROW() {
		return R54_OTHER_BORROW;
	}

	public void setR54_OTHER_BORROW(BigDecimal r54_OTHER_BORROW) {
		R54_OTHER_BORROW = r54_OTHER_BORROW;
	}

	public BigDecimal getR55_OTHER_BORROW() {
		return R55_OTHER_BORROW;
	}

	public void setR55_OTHER_BORROW(BigDecimal r55_OTHER_BORROW) {
		R55_OTHER_BORROW = r55_OTHER_BORROW;
	}

	public BigDecimal getR56_OTHER_BORROW() {
		return R56_OTHER_BORROW;
	}

	public void setR56_OTHER_BORROW(BigDecimal r56_OTHER_BORROW) {
		R56_OTHER_BORROW = r56_OTHER_BORROW;
	}

	public BigDecimal getR57_OTHER_BORROW() {
		return R57_OTHER_BORROW;
	}

	public void setR57_OTHER_BORROW(BigDecimal r57_OTHER_BORROW) {
		R57_OTHER_BORROW = r57_OTHER_BORROW;
	}

	public BigDecimal getR58_OTHER_BORROW() {
		return R58_OTHER_BORROW;
	}

	public void setR58_OTHER_BORROW(BigDecimal r58_OTHER_BORROW) {
		R58_OTHER_BORROW = r58_OTHER_BORROW;
	}

	public BigDecimal getR59_OTHER_BORROW() {
		return R59_OTHER_BORROW;
	}

	public void setR59_OTHER_BORROW(BigDecimal r59_OTHER_BORROW) {
		R59_OTHER_BORROW = r59_OTHER_BORROW;
	}

	public BigDecimal getR60_OTHER_BORROW() {
		return R60_OTHER_BORROW;
	}

	public void setR60_OTHER_BORROW(BigDecimal r60_OTHER_BORROW) {
		R60_OTHER_BORROW = r60_OTHER_BORROW;
	}

	public BigDecimal getR61_OTHER_BORROW() {
		return R61_OTHER_BORROW;
	}

	public void setR61_OTHER_BORROW(BigDecimal r61_OTHER_BORROW) {
		R61_OTHER_BORROW = r61_OTHER_BORROW;
	}

	public BigDecimal getR62_OTHER_BORROW() {
		return R62_OTHER_BORROW;
	}

	public void setR62_OTHER_BORROW(BigDecimal r62_OTHER_BORROW) {
		R62_OTHER_BORROW = r62_OTHER_BORROW;
	}

	public BigDecimal getR63_OTHER_BORROW() {
		return R63_OTHER_BORROW;
	}

	public void setR63_OTHER_BORROW(BigDecimal r63_OTHER_BORROW) {
		R63_OTHER_BORROW = r63_OTHER_BORROW;
	}

	public BigDecimal getR64_OTHER_BORROW() {
		return R64_OTHER_BORROW;
	}

	public void setR64_OTHER_BORROW(BigDecimal r64_OTHER_BORROW) {
		R64_OTHER_BORROW = r64_OTHER_BORROW;
	}

	public M_OB_Archival_Detail_Entity() {
        super();
    }

}

// ------------------------------
// Resub Summary Entity class for M_OB
// ------------------------------
public static class M_OB_Resub_Summary_Entity {
	
	
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	
	private Date reportResubDate;
	
    
    public String REPORT_FREQUENCY;
    public String REPORT_CODE;
    public String REPORT_DESC;
    public String ENTITY_FLG;
    public String MODIFY_FLG;
    public String DEL_FLG;

    
	
	
	private BigDecimal R11_OTHER_BORROW;
	private BigDecimal R12_OTHER_BORROW;
	private BigDecimal R13_OTHER_BORROW;
	private BigDecimal R14_OTHER_BORROW;
	private BigDecimal R15_OTHER_BORROW;
	private BigDecimal R16_OTHER_BORROW;
	private BigDecimal R17_OTHER_BORROW;
	private BigDecimal R18_OTHER_BORROW;
	private BigDecimal R19_OTHER_BORROW;
	private BigDecimal R20_OTHER_BORROW;
	private BigDecimal R21_OTHER_BORROW;
	private BigDecimal R22_OTHER_BORROW;
	private BigDecimal R23_OTHER_BORROW;
	private BigDecimal R24_OTHER_BORROW;
	private BigDecimal R25_OTHER_BORROW;
	private BigDecimal R26_OTHER_BORROW;
	private BigDecimal R27_OTHER_BORROW;
	private BigDecimal R28_OTHER_BORROW;
	private BigDecimal R29_OTHER_BORROW;
	private BigDecimal R30_OTHER_BORROW;
	private BigDecimal R31_OTHER_BORROW;
	private BigDecimal R32_OTHER_BORROW;
	private BigDecimal R33_OTHER_BORROW;
	private BigDecimal R34_OTHER_BORROW;
	private BigDecimal R35_OTHER_BORROW;
	private BigDecimal R36_OTHER_BORROW;
	private BigDecimal R37_OTHER_BORROW;
	private BigDecimal R38_OTHER_BORROW;
	private BigDecimal R39_OTHER_BORROW;
	private BigDecimal R40_OTHER_BORROW;
	private BigDecimal R41_OTHER_BORROW;
	private BigDecimal R42_OTHER_BORROW;
	private BigDecimal R43_OTHER_BORROW;
	private BigDecimal R44_OTHER_BORROW;
	private BigDecimal R45_OTHER_BORROW;
	private BigDecimal R46_OTHER_BORROW;
	private BigDecimal R47_OTHER_BORROW;
	private BigDecimal R48_OTHER_BORROW;
	private BigDecimal R49_OTHER_BORROW;
	private BigDecimal R50_OTHER_BORROW;
	private BigDecimal R51_OTHER_BORROW;
	private BigDecimal R52_OTHER_BORROW;
	private BigDecimal R53_OTHER_BORROW;
	private BigDecimal R54_OTHER_BORROW;
	private BigDecimal R55_OTHER_BORROW;
	private BigDecimal R56_OTHER_BORROW;
	private BigDecimal R57_OTHER_BORROW;
	private BigDecimal R58_OTHER_BORROW;
	private BigDecimal R59_OTHER_BORROW;
	private BigDecimal R60_OTHER_BORROW;
	private BigDecimal R61_OTHER_BORROW;
	private BigDecimal R62_OTHER_BORROW;
	private BigDecimal R63_OTHER_BORROW;
	private BigDecimal R64_OTHER_BORROW;

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

	public BigDecimal getR11_OTHER_BORROW() {
		return R11_OTHER_BORROW;
	}

	public void setR11_OTHER_BORROW(BigDecimal r11_OTHER_BORROW) {
		R11_OTHER_BORROW = r11_OTHER_BORROW;
	}

	public BigDecimal getR12_OTHER_BORROW() {
		return R12_OTHER_BORROW;
	}

	public void setR12_OTHER_BORROW(BigDecimal r12_OTHER_BORROW) {
		R12_OTHER_BORROW = r12_OTHER_BORROW;
	}

	public BigDecimal getR13_OTHER_BORROW() {
		return R13_OTHER_BORROW;
	}

	public void setR13_OTHER_BORROW(BigDecimal r13_OTHER_BORROW) {
		R13_OTHER_BORROW = r13_OTHER_BORROW;
	}

	public BigDecimal getR14_OTHER_BORROW() {
		return R14_OTHER_BORROW;
	}

	public void setR14_OTHER_BORROW(BigDecimal r14_OTHER_BORROW) {
		R14_OTHER_BORROW = r14_OTHER_BORROW;
	}

	public BigDecimal getR15_OTHER_BORROW() {
		return R15_OTHER_BORROW;
	}

	public void setR15_OTHER_BORROW(BigDecimal r15_OTHER_BORROW) {
		R15_OTHER_BORROW = r15_OTHER_BORROW;
	}

	public BigDecimal getR16_OTHER_BORROW() {
		return R16_OTHER_BORROW;
	}

	public void setR16_OTHER_BORROW(BigDecimal r16_OTHER_BORROW) {
		R16_OTHER_BORROW = r16_OTHER_BORROW;
	}

	public BigDecimal getR17_OTHER_BORROW() {
		return R17_OTHER_BORROW;
	}

	public void setR17_OTHER_BORROW(BigDecimal r17_OTHER_BORROW) {
		R17_OTHER_BORROW = r17_OTHER_BORROW;
	}

	public BigDecimal getR18_OTHER_BORROW() {
		return R18_OTHER_BORROW;
	}

	public void setR18_OTHER_BORROW(BigDecimal r18_OTHER_BORROW) {
		R18_OTHER_BORROW = r18_OTHER_BORROW;
	}

	public BigDecimal getR19_OTHER_BORROW() {
		return R19_OTHER_BORROW;
	}

	public void setR19_OTHER_BORROW(BigDecimal r19_OTHER_BORROW) {
		R19_OTHER_BORROW = r19_OTHER_BORROW;
	}

	public BigDecimal getR20_OTHER_BORROW() {
		return R20_OTHER_BORROW;
	}

	public void setR20_OTHER_BORROW(BigDecimal r20_OTHER_BORROW) {
		R20_OTHER_BORROW = r20_OTHER_BORROW;
	}

	public BigDecimal getR21_OTHER_BORROW() {
		return R21_OTHER_BORROW;
	}

	public void setR21_OTHER_BORROW(BigDecimal r21_OTHER_BORROW) {
		R21_OTHER_BORROW = r21_OTHER_BORROW;
	}

	public BigDecimal getR22_OTHER_BORROW() {
		return R22_OTHER_BORROW;
	}

	public void setR22_OTHER_BORROW(BigDecimal r22_OTHER_BORROW) {
		R22_OTHER_BORROW = r22_OTHER_BORROW;
	}

	public BigDecimal getR23_OTHER_BORROW() {
		return R23_OTHER_BORROW;
	}

	public void setR23_OTHER_BORROW(BigDecimal r23_OTHER_BORROW) {
		R23_OTHER_BORROW = r23_OTHER_BORROW;
	}

	public BigDecimal getR24_OTHER_BORROW() {
		return R24_OTHER_BORROW;
	}

	public void setR24_OTHER_BORROW(BigDecimal r24_OTHER_BORROW) {
		R24_OTHER_BORROW = r24_OTHER_BORROW;
	}

	public BigDecimal getR25_OTHER_BORROW() {
		return R25_OTHER_BORROW;
	}

	public void setR25_OTHER_BORROW(BigDecimal r25_OTHER_BORROW) {
		R25_OTHER_BORROW = r25_OTHER_BORROW;
	}

	public BigDecimal getR26_OTHER_BORROW() {
		return R26_OTHER_BORROW;
	}

	public void setR26_OTHER_BORROW(BigDecimal r26_OTHER_BORROW) {
		R26_OTHER_BORROW = r26_OTHER_BORROW;
	}

	public BigDecimal getR27_OTHER_BORROW() {
		return R27_OTHER_BORROW;
	}

	public void setR27_OTHER_BORROW(BigDecimal r27_OTHER_BORROW) {
		R27_OTHER_BORROW = r27_OTHER_BORROW;
	}

	public BigDecimal getR28_OTHER_BORROW() {
		return R28_OTHER_BORROW;
	}

	public void setR28_OTHER_BORROW(BigDecimal r28_OTHER_BORROW) {
		R28_OTHER_BORROW = r28_OTHER_BORROW;
	}

	public BigDecimal getR29_OTHER_BORROW() {
		return R29_OTHER_BORROW;
	}

	public void setR29_OTHER_BORROW(BigDecimal r29_OTHER_BORROW) {
		R29_OTHER_BORROW = r29_OTHER_BORROW;
	}

	public BigDecimal getR30_OTHER_BORROW() {
		return R30_OTHER_BORROW;
	}

	public void setR30_OTHER_BORROW(BigDecimal r30_OTHER_BORROW) {
		R30_OTHER_BORROW = r30_OTHER_BORROW;
	}

	public BigDecimal getR31_OTHER_BORROW() {
		return R31_OTHER_BORROW;
	}

	public void setR31_OTHER_BORROW(BigDecimal r31_OTHER_BORROW) {
		R31_OTHER_BORROW = r31_OTHER_BORROW;
	}

	public BigDecimal getR32_OTHER_BORROW() {
		return R32_OTHER_BORROW;
	}

	public void setR32_OTHER_BORROW(BigDecimal r32_OTHER_BORROW) {
		R32_OTHER_BORROW = r32_OTHER_BORROW;
	}

	public BigDecimal getR33_OTHER_BORROW() {
		return R33_OTHER_BORROW;
	}

	public void setR33_OTHER_BORROW(BigDecimal r33_OTHER_BORROW) {
		R33_OTHER_BORROW = r33_OTHER_BORROW;
	}

	public BigDecimal getR34_OTHER_BORROW() {
		return R34_OTHER_BORROW;
	}

	public void setR34_OTHER_BORROW(BigDecimal r34_OTHER_BORROW) {
		R34_OTHER_BORROW = r34_OTHER_BORROW;
	}

	public BigDecimal getR35_OTHER_BORROW() {
		return R35_OTHER_BORROW;
	}

	public void setR35_OTHER_BORROW(BigDecimal r35_OTHER_BORROW) {
		R35_OTHER_BORROW = r35_OTHER_BORROW;
	}

	public BigDecimal getR36_OTHER_BORROW() {
		return R36_OTHER_BORROW;
	}

	public void setR36_OTHER_BORROW(BigDecimal r36_OTHER_BORROW) {
		R36_OTHER_BORROW = r36_OTHER_BORROW;
	}

	public BigDecimal getR37_OTHER_BORROW() {
		return R37_OTHER_BORROW;
	}

	public void setR37_OTHER_BORROW(BigDecimal r37_OTHER_BORROW) {
		R37_OTHER_BORROW = r37_OTHER_BORROW;
	}

	public BigDecimal getR38_OTHER_BORROW() {
		return R38_OTHER_BORROW;
	}

	public void setR38_OTHER_BORROW(BigDecimal r38_OTHER_BORROW) {
		R38_OTHER_BORROW = r38_OTHER_BORROW;
	}

	public BigDecimal getR39_OTHER_BORROW() {
		return R39_OTHER_BORROW;
	}

	public void setR39_OTHER_BORROW(BigDecimal r39_OTHER_BORROW) {
		R39_OTHER_BORROW = r39_OTHER_BORROW;
	}

	public BigDecimal getR40_OTHER_BORROW() {
		return R40_OTHER_BORROW;
	}

	public void setR40_OTHER_BORROW(BigDecimal r40_OTHER_BORROW) {
		R40_OTHER_BORROW = r40_OTHER_BORROW;
	}

	public BigDecimal getR41_OTHER_BORROW() {
		return R41_OTHER_BORROW;
	}

	public void setR41_OTHER_BORROW(BigDecimal r41_OTHER_BORROW) {
		R41_OTHER_BORROW = r41_OTHER_BORROW;
	}

	public BigDecimal getR42_OTHER_BORROW() {
		return R42_OTHER_BORROW;
	}

	public void setR42_OTHER_BORROW(BigDecimal r42_OTHER_BORROW) {
		R42_OTHER_BORROW = r42_OTHER_BORROW;
	}

	public BigDecimal getR43_OTHER_BORROW() {
		return R43_OTHER_BORROW;
	}

	public void setR43_OTHER_BORROW(BigDecimal r43_OTHER_BORROW) {
		R43_OTHER_BORROW = r43_OTHER_BORROW;
	}

	public BigDecimal getR44_OTHER_BORROW() {
		return R44_OTHER_BORROW;
	}

	public void setR44_OTHER_BORROW(BigDecimal r44_OTHER_BORROW) {
		R44_OTHER_BORROW = r44_OTHER_BORROW;
	}

	public BigDecimal getR45_OTHER_BORROW() {
		return R45_OTHER_BORROW;
	}

	public void setR45_OTHER_BORROW(BigDecimal r45_OTHER_BORROW) {
		R45_OTHER_BORROW = r45_OTHER_BORROW;
	}

	public BigDecimal getR46_OTHER_BORROW() {
		return R46_OTHER_BORROW;
	}

	public void setR46_OTHER_BORROW(BigDecimal r46_OTHER_BORROW) {
		R46_OTHER_BORROW = r46_OTHER_BORROW;
	}

	public BigDecimal getR47_OTHER_BORROW() {
		return R47_OTHER_BORROW;
	}

	public void setR47_OTHER_BORROW(BigDecimal r47_OTHER_BORROW) {
		R47_OTHER_BORROW = r47_OTHER_BORROW;
	}

	public BigDecimal getR48_OTHER_BORROW() {
		return R48_OTHER_BORROW;
	}

	public void setR48_OTHER_BORROW(BigDecimal r48_OTHER_BORROW) {
		R48_OTHER_BORROW = r48_OTHER_BORROW;
	}

	public BigDecimal getR49_OTHER_BORROW() {
		return R49_OTHER_BORROW;
	}

	public void setR49_OTHER_BORROW(BigDecimal r49_OTHER_BORROW) {
		R49_OTHER_BORROW = r49_OTHER_BORROW;
	}

	public BigDecimal getR50_OTHER_BORROW() {
		return R50_OTHER_BORROW;
	}

	public void setR50_OTHER_BORROW(BigDecimal r50_OTHER_BORROW) {
		R50_OTHER_BORROW = r50_OTHER_BORROW;
	}

	public BigDecimal getR51_OTHER_BORROW() {
		return R51_OTHER_BORROW;
	}

	public void setR51_OTHER_BORROW(BigDecimal r51_OTHER_BORROW) {
		R51_OTHER_BORROW = r51_OTHER_BORROW;
	}

	public BigDecimal getR52_OTHER_BORROW() {
		return R52_OTHER_BORROW;
	}

	public void setR52_OTHER_BORROW(BigDecimal r52_OTHER_BORROW) {
		R52_OTHER_BORROW = r52_OTHER_BORROW;
	}

	public BigDecimal getR53_OTHER_BORROW() {
		return R53_OTHER_BORROW;
	}

	public void setR53_OTHER_BORROW(BigDecimal r53_OTHER_BORROW) {
		R53_OTHER_BORROW = r53_OTHER_BORROW;
	}

	public BigDecimal getR54_OTHER_BORROW() {
		return R54_OTHER_BORROW;
	}

	public void setR54_OTHER_BORROW(BigDecimal r54_OTHER_BORROW) {
		R54_OTHER_BORROW = r54_OTHER_BORROW;
	}

	public BigDecimal getR55_OTHER_BORROW() {
		return R55_OTHER_BORROW;
	}

	public void setR55_OTHER_BORROW(BigDecimal r55_OTHER_BORROW) {
		R55_OTHER_BORROW = r55_OTHER_BORROW;
	}

	public BigDecimal getR56_OTHER_BORROW() {
		return R56_OTHER_BORROW;
	}

	public void setR56_OTHER_BORROW(BigDecimal r56_OTHER_BORROW) {
		R56_OTHER_BORROW = r56_OTHER_BORROW;
	}

	public BigDecimal getR57_OTHER_BORROW() {
		return R57_OTHER_BORROW;
	}

	public void setR57_OTHER_BORROW(BigDecimal r57_OTHER_BORROW) {
		R57_OTHER_BORROW = r57_OTHER_BORROW;
	}

	public BigDecimal getR58_OTHER_BORROW() {
		return R58_OTHER_BORROW;
	}

	public void setR58_OTHER_BORROW(BigDecimal r58_OTHER_BORROW) {
		R58_OTHER_BORROW = r58_OTHER_BORROW;
	}

	public BigDecimal getR59_OTHER_BORROW() {
		return R59_OTHER_BORROW;
	}

	public void setR59_OTHER_BORROW(BigDecimal r59_OTHER_BORROW) {
		R59_OTHER_BORROW = r59_OTHER_BORROW;
	}

	public BigDecimal getR60_OTHER_BORROW() {
		return R60_OTHER_BORROW;
	}

	public void setR60_OTHER_BORROW(BigDecimal r60_OTHER_BORROW) {
		R60_OTHER_BORROW = r60_OTHER_BORROW;
	}

	public BigDecimal getR61_OTHER_BORROW() {
		return R61_OTHER_BORROW;
	}

	public void setR61_OTHER_BORROW(BigDecimal r61_OTHER_BORROW) {
		R61_OTHER_BORROW = r61_OTHER_BORROW;
	}

	public BigDecimal getR62_OTHER_BORROW() {
		return R62_OTHER_BORROW;
	}

	public void setR62_OTHER_BORROW(BigDecimal r62_OTHER_BORROW) {
		R62_OTHER_BORROW = r62_OTHER_BORROW;
	}

	public BigDecimal getR63_OTHER_BORROW() {
		return R63_OTHER_BORROW;
	}

	public void setR63_OTHER_BORROW(BigDecimal r63_OTHER_BORROW) {
		R63_OTHER_BORROW = r63_OTHER_BORROW;
	}

	public BigDecimal getR64_OTHER_BORROW() {
		return R64_OTHER_BORROW;
	}

	public void setR64_OTHER_BORROW(BigDecimal r64_OTHER_BORROW) {
		R64_OTHER_BORROW = r64_OTHER_BORROW;
	}

	public M_OB_Resub_Summary_Entity() {
        super();
    }

}

// ------------------------------
// Resub Detail Entity class for M_OB
// ------------------------------
public static class M_OB_Resub_Detail_Entity {
	
	
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	
	private Date reportResubDate;
	
    
    public String REPORT_FREQUENCY;
    public String REPORT_CODE;
    public String REPORT_DESC;
    public String ENTITY_FLG;
    public String MODIFY_FLG;
    public String DEL_FLG;

    
	
	
	private BigDecimal R11_OTHER_BORROW;
	private BigDecimal R12_OTHER_BORROW;
	private BigDecimal R13_OTHER_BORROW;
	private BigDecimal R14_OTHER_BORROW;
	private BigDecimal R15_OTHER_BORROW;
	private BigDecimal R16_OTHER_BORROW;
	private BigDecimal R17_OTHER_BORROW;
	private BigDecimal R18_OTHER_BORROW;
	private BigDecimal R19_OTHER_BORROW;
	private BigDecimal R20_OTHER_BORROW;
	private BigDecimal R21_OTHER_BORROW;
	private BigDecimal R22_OTHER_BORROW;
	private BigDecimal R23_OTHER_BORROW;
	private BigDecimal R24_OTHER_BORROW;
	private BigDecimal R25_OTHER_BORROW;
	private BigDecimal R26_OTHER_BORROW;
	private BigDecimal R27_OTHER_BORROW;
	private BigDecimal R28_OTHER_BORROW;
	private BigDecimal R29_OTHER_BORROW;
	private BigDecimal R30_OTHER_BORROW;
	private BigDecimal R31_OTHER_BORROW;
	private BigDecimal R32_OTHER_BORROW;
	private BigDecimal R33_OTHER_BORROW;
	private BigDecimal R34_OTHER_BORROW;
	private BigDecimal R35_OTHER_BORROW;
	private BigDecimal R36_OTHER_BORROW;
	private BigDecimal R37_OTHER_BORROW;
	private BigDecimal R38_OTHER_BORROW;
	private BigDecimal R39_OTHER_BORROW;
	private BigDecimal R40_OTHER_BORROW;
	private BigDecimal R41_OTHER_BORROW;
	private BigDecimal R42_OTHER_BORROW;
	private BigDecimal R43_OTHER_BORROW;
	private BigDecimal R44_OTHER_BORROW;
	private BigDecimal R45_OTHER_BORROW;
	private BigDecimal R46_OTHER_BORROW;
	private BigDecimal R47_OTHER_BORROW;
	private BigDecimal R48_OTHER_BORROW;
	private BigDecimal R49_OTHER_BORROW;
	private BigDecimal R50_OTHER_BORROW;
	private BigDecimal R51_OTHER_BORROW;
	private BigDecimal R52_OTHER_BORROW;
	private BigDecimal R53_OTHER_BORROW;
	private BigDecimal R54_OTHER_BORROW;
	private BigDecimal R55_OTHER_BORROW;
	private BigDecimal R56_OTHER_BORROW;
	private BigDecimal R57_OTHER_BORROW;
	private BigDecimal R58_OTHER_BORROW;
	private BigDecimal R59_OTHER_BORROW;
	private BigDecimal R60_OTHER_BORROW;
	private BigDecimal R61_OTHER_BORROW;
	private BigDecimal R62_OTHER_BORROW;
	private BigDecimal R63_OTHER_BORROW;
	private BigDecimal R64_OTHER_BORROW;

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

	public BigDecimal getR11_OTHER_BORROW() {
		return R11_OTHER_BORROW;
	}

	public void setR11_OTHER_BORROW(BigDecimal r11_OTHER_BORROW) {
		R11_OTHER_BORROW = r11_OTHER_BORROW;
	}

	public BigDecimal getR12_OTHER_BORROW() {
		return R12_OTHER_BORROW;
	}

	public void setR12_OTHER_BORROW(BigDecimal r12_OTHER_BORROW) {
		R12_OTHER_BORROW = r12_OTHER_BORROW;
	}

	public BigDecimal getR13_OTHER_BORROW() {
		return R13_OTHER_BORROW;
	}

	public void setR13_OTHER_BORROW(BigDecimal r13_OTHER_BORROW) {
		R13_OTHER_BORROW = r13_OTHER_BORROW;
	}

	public BigDecimal getR14_OTHER_BORROW() {
		return R14_OTHER_BORROW;
	}

	public void setR14_OTHER_BORROW(BigDecimal r14_OTHER_BORROW) {
		R14_OTHER_BORROW = r14_OTHER_BORROW;
	}

	public BigDecimal getR15_OTHER_BORROW() {
		return R15_OTHER_BORROW;
	}

	public void setR15_OTHER_BORROW(BigDecimal r15_OTHER_BORROW) {
		R15_OTHER_BORROW = r15_OTHER_BORROW;
	}

	public BigDecimal getR16_OTHER_BORROW() {
		return R16_OTHER_BORROW;
	}

	public void setR16_OTHER_BORROW(BigDecimal r16_OTHER_BORROW) {
		R16_OTHER_BORROW = r16_OTHER_BORROW;
	}

	public BigDecimal getR17_OTHER_BORROW() {
		return R17_OTHER_BORROW;
	}

	public void setR17_OTHER_BORROW(BigDecimal r17_OTHER_BORROW) {
		R17_OTHER_BORROW = r17_OTHER_BORROW;
	}

	public BigDecimal getR18_OTHER_BORROW() {
		return R18_OTHER_BORROW;
	}

	public void setR18_OTHER_BORROW(BigDecimal r18_OTHER_BORROW) {
		R18_OTHER_BORROW = r18_OTHER_BORROW;
	}

	public BigDecimal getR19_OTHER_BORROW() {
		return R19_OTHER_BORROW;
	}

	public void setR19_OTHER_BORROW(BigDecimal r19_OTHER_BORROW) {
		R19_OTHER_BORROW = r19_OTHER_BORROW;
	}

	public BigDecimal getR20_OTHER_BORROW() {
		return R20_OTHER_BORROW;
	}

	public void setR20_OTHER_BORROW(BigDecimal r20_OTHER_BORROW) {
		R20_OTHER_BORROW = r20_OTHER_BORROW;
	}

	public BigDecimal getR21_OTHER_BORROW() {
		return R21_OTHER_BORROW;
	}

	public void setR21_OTHER_BORROW(BigDecimal r21_OTHER_BORROW) {
		R21_OTHER_BORROW = r21_OTHER_BORROW;
	}

	public BigDecimal getR22_OTHER_BORROW() {
		return R22_OTHER_BORROW;
	}

	public void setR22_OTHER_BORROW(BigDecimal r22_OTHER_BORROW) {
		R22_OTHER_BORROW = r22_OTHER_BORROW;
	}

	public BigDecimal getR23_OTHER_BORROW() {
		return R23_OTHER_BORROW;
	}

	public void setR23_OTHER_BORROW(BigDecimal r23_OTHER_BORROW) {
		R23_OTHER_BORROW = r23_OTHER_BORROW;
	}

	public BigDecimal getR24_OTHER_BORROW() {
		return R24_OTHER_BORROW;
	}

	public void setR24_OTHER_BORROW(BigDecimal r24_OTHER_BORROW) {
		R24_OTHER_BORROW = r24_OTHER_BORROW;
	}

	public BigDecimal getR25_OTHER_BORROW() {
		return R25_OTHER_BORROW;
	}

	public void setR25_OTHER_BORROW(BigDecimal r25_OTHER_BORROW) {
		R25_OTHER_BORROW = r25_OTHER_BORROW;
	}

	public BigDecimal getR26_OTHER_BORROW() {
		return R26_OTHER_BORROW;
	}

	public void setR26_OTHER_BORROW(BigDecimal r26_OTHER_BORROW) {
		R26_OTHER_BORROW = r26_OTHER_BORROW;
	}

	public BigDecimal getR27_OTHER_BORROW() {
		return R27_OTHER_BORROW;
	}

	public void setR27_OTHER_BORROW(BigDecimal r27_OTHER_BORROW) {
		R27_OTHER_BORROW = r27_OTHER_BORROW;
	}

	public BigDecimal getR28_OTHER_BORROW() {
		return R28_OTHER_BORROW;
	}

	public void setR28_OTHER_BORROW(BigDecimal r28_OTHER_BORROW) {
		R28_OTHER_BORROW = r28_OTHER_BORROW;
	}

	public BigDecimal getR29_OTHER_BORROW() {
		return R29_OTHER_BORROW;
	}

	public void setR29_OTHER_BORROW(BigDecimal r29_OTHER_BORROW) {
		R29_OTHER_BORROW = r29_OTHER_BORROW;
	}

	public BigDecimal getR30_OTHER_BORROW() {
		return R30_OTHER_BORROW;
	}

	public void setR30_OTHER_BORROW(BigDecimal r30_OTHER_BORROW) {
		R30_OTHER_BORROW = r30_OTHER_BORROW;
	}

	public BigDecimal getR31_OTHER_BORROW() {
		return R31_OTHER_BORROW;
	}

	public void setR31_OTHER_BORROW(BigDecimal r31_OTHER_BORROW) {
		R31_OTHER_BORROW = r31_OTHER_BORROW;
	}

	public BigDecimal getR32_OTHER_BORROW() {
		return R32_OTHER_BORROW;
	}

	public void setR32_OTHER_BORROW(BigDecimal r32_OTHER_BORROW) {
		R32_OTHER_BORROW = r32_OTHER_BORROW;
	}

	public BigDecimal getR33_OTHER_BORROW() {
		return R33_OTHER_BORROW;
	}

	public void setR33_OTHER_BORROW(BigDecimal r33_OTHER_BORROW) {
		R33_OTHER_BORROW = r33_OTHER_BORROW;
	}

	public BigDecimal getR34_OTHER_BORROW() {
		return R34_OTHER_BORROW;
	}

	public void setR34_OTHER_BORROW(BigDecimal r34_OTHER_BORROW) {
		R34_OTHER_BORROW = r34_OTHER_BORROW;
	}

	public BigDecimal getR35_OTHER_BORROW() {
		return R35_OTHER_BORROW;
	}

	public void setR35_OTHER_BORROW(BigDecimal r35_OTHER_BORROW) {
		R35_OTHER_BORROW = r35_OTHER_BORROW;
	}

	public BigDecimal getR36_OTHER_BORROW() {
		return R36_OTHER_BORROW;
	}

	public void setR36_OTHER_BORROW(BigDecimal r36_OTHER_BORROW) {
		R36_OTHER_BORROW = r36_OTHER_BORROW;
	}

	public BigDecimal getR37_OTHER_BORROW() {
		return R37_OTHER_BORROW;
	}

	public void setR37_OTHER_BORROW(BigDecimal r37_OTHER_BORROW) {
		R37_OTHER_BORROW = r37_OTHER_BORROW;
	}

	public BigDecimal getR38_OTHER_BORROW() {
		return R38_OTHER_BORROW;
	}

	public void setR38_OTHER_BORROW(BigDecimal r38_OTHER_BORROW) {
		R38_OTHER_BORROW = r38_OTHER_BORROW;
	}

	public BigDecimal getR39_OTHER_BORROW() {
		return R39_OTHER_BORROW;
	}

	public void setR39_OTHER_BORROW(BigDecimal r39_OTHER_BORROW) {
		R39_OTHER_BORROW = r39_OTHER_BORROW;
	}

	public BigDecimal getR40_OTHER_BORROW() {
		return R40_OTHER_BORROW;
	}

	public void setR40_OTHER_BORROW(BigDecimal r40_OTHER_BORROW) {
		R40_OTHER_BORROW = r40_OTHER_BORROW;
	}

	public BigDecimal getR41_OTHER_BORROW() {
		return R41_OTHER_BORROW;
	}

	public void setR41_OTHER_BORROW(BigDecimal r41_OTHER_BORROW) {
		R41_OTHER_BORROW = r41_OTHER_BORROW;
	}

	public BigDecimal getR42_OTHER_BORROW() {
		return R42_OTHER_BORROW;
	}

	public void setR42_OTHER_BORROW(BigDecimal r42_OTHER_BORROW) {
		R42_OTHER_BORROW = r42_OTHER_BORROW;
	}

	public BigDecimal getR43_OTHER_BORROW() {
		return R43_OTHER_BORROW;
	}

	public void setR43_OTHER_BORROW(BigDecimal r43_OTHER_BORROW) {
		R43_OTHER_BORROW = r43_OTHER_BORROW;
	}

	public BigDecimal getR44_OTHER_BORROW() {
		return R44_OTHER_BORROW;
	}

	public void setR44_OTHER_BORROW(BigDecimal r44_OTHER_BORROW) {
		R44_OTHER_BORROW = r44_OTHER_BORROW;
	}

	public BigDecimal getR45_OTHER_BORROW() {
		return R45_OTHER_BORROW;
	}

	public void setR45_OTHER_BORROW(BigDecimal r45_OTHER_BORROW) {
		R45_OTHER_BORROW = r45_OTHER_BORROW;
	}

	public BigDecimal getR46_OTHER_BORROW() {
		return R46_OTHER_BORROW;
	}

	public void setR46_OTHER_BORROW(BigDecimal r46_OTHER_BORROW) {
		R46_OTHER_BORROW = r46_OTHER_BORROW;
	}

	public BigDecimal getR47_OTHER_BORROW() {
		return R47_OTHER_BORROW;
	}

	public void setR47_OTHER_BORROW(BigDecimal r47_OTHER_BORROW) {
		R47_OTHER_BORROW = r47_OTHER_BORROW;
	}

	public BigDecimal getR48_OTHER_BORROW() {
		return R48_OTHER_BORROW;
	}

	public void setR48_OTHER_BORROW(BigDecimal r48_OTHER_BORROW) {
		R48_OTHER_BORROW = r48_OTHER_BORROW;
	}

	public BigDecimal getR49_OTHER_BORROW() {
		return R49_OTHER_BORROW;
	}

	public void setR49_OTHER_BORROW(BigDecimal r49_OTHER_BORROW) {
		R49_OTHER_BORROW = r49_OTHER_BORROW;
	}

	public BigDecimal getR50_OTHER_BORROW() {
		return R50_OTHER_BORROW;
	}

	public void setR50_OTHER_BORROW(BigDecimal r50_OTHER_BORROW) {
		R50_OTHER_BORROW = r50_OTHER_BORROW;
	}

	public BigDecimal getR51_OTHER_BORROW() {
		return R51_OTHER_BORROW;
	}

	public void setR51_OTHER_BORROW(BigDecimal r51_OTHER_BORROW) {
		R51_OTHER_BORROW = r51_OTHER_BORROW;
	}

	public BigDecimal getR52_OTHER_BORROW() {
		return R52_OTHER_BORROW;
	}

	public void setR52_OTHER_BORROW(BigDecimal r52_OTHER_BORROW) {
		R52_OTHER_BORROW = r52_OTHER_BORROW;
	}

	public BigDecimal getR53_OTHER_BORROW() {
		return R53_OTHER_BORROW;
	}

	public void setR53_OTHER_BORROW(BigDecimal r53_OTHER_BORROW) {
		R53_OTHER_BORROW = r53_OTHER_BORROW;
	}

	public BigDecimal getR54_OTHER_BORROW() {
		return R54_OTHER_BORROW;
	}

	public void setR54_OTHER_BORROW(BigDecimal r54_OTHER_BORROW) {
		R54_OTHER_BORROW = r54_OTHER_BORROW;
	}

	public BigDecimal getR55_OTHER_BORROW() {
		return R55_OTHER_BORROW;
	}

	public void setR55_OTHER_BORROW(BigDecimal r55_OTHER_BORROW) {
		R55_OTHER_BORROW = r55_OTHER_BORROW;
	}

	public BigDecimal getR56_OTHER_BORROW() {
		return R56_OTHER_BORROW;
	}

	public void setR56_OTHER_BORROW(BigDecimal r56_OTHER_BORROW) {
		R56_OTHER_BORROW = r56_OTHER_BORROW;
	}

	public BigDecimal getR57_OTHER_BORROW() {
		return R57_OTHER_BORROW;
	}

	public void setR57_OTHER_BORROW(BigDecimal r57_OTHER_BORROW) {
		R57_OTHER_BORROW = r57_OTHER_BORROW;
	}

	public BigDecimal getR58_OTHER_BORROW() {
		return R58_OTHER_BORROW;
	}

	public void setR58_OTHER_BORROW(BigDecimal r58_OTHER_BORROW) {
		R58_OTHER_BORROW = r58_OTHER_BORROW;
	}

	public BigDecimal getR59_OTHER_BORROW() {
		return R59_OTHER_BORROW;
	}

	public void setR59_OTHER_BORROW(BigDecimal r59_OTHER_BORROW) {
		R59_OTHER_BORROW = r59_OTHER_BORROW;
	}

	public BigDecimal getR60_OTHER_BORROW() {
		return R60_OTHER_BORROW;
	}

	public void setR60_OTHER_BORROW(BigDecimal r60_OTHER_BORROW) {
		R60_OTHER_BORROW = r60_OTHER_BORROW;
	}

	public BigDecimal getR61_OTHER_BORROW() {
		return R61_OTHER_BORROW;
	}

	public void setR61_OTHER_BORROW(BigDecimal r61_OTHER_BORROW) {
		R61_OTHER_BORROW = r61_OTHER_BORROW;
	}

	public BigDecimal getR62_OTHER_BORROW() {
		return R62_OTHER_BORROW;
	}

	public void setR62_OTHER_BORROW(BigDecimal r62_OTHER_BORROW) {
		R62_OTHER_BORROW = r62_OTHER_BORROW;
	}

	public BigDecimal getR63_OTHER_BORROW() {
		return R63_OTHER_BORROW;
	}

	public void setR63_OTHER_BORROW(BigDecimal r63_OTHER_BORROW) {
		R63_OTHER_BORROW = r63_OTHER_BORROW;
	}

	public BigDecimal getR64_OTHER_BORROW() {
		return R64_OTHER_BORROW;
	}

	public void setR64_OTHER_BORROW(BigDecimal r64_OTHER_BORROW) {
		R64_OTHER_BORROW = r64_OTHER_BORROW;
	}

	public M_OB_Resub_Detail_Entity() {
        super();
    }

}
}
