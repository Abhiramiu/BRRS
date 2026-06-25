package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.M_CA2_Summary_Entity;
import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional

public class BRRS_M_CA2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public List<M_CA2_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_M_CA2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportDate }, new M_CA2SummaryRowMapper());
	}

	public List<M_CA2_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA2ArchivalSummaryRowMapper());
	}

	public List<M_CA2_Archival_Summary_Entity> getArchivalSummaryWithVersion() {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC",
				new M_CA2ArchivalSummaryRowMapper());
	}

	public List<M_CA2_RESUB_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA2ResubSummaryRowMapper());
	}

	public List<M_CA2_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, new M_CA2DetailRowMapper());
	}

	public int getDetailCount(Date reportDate) {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM BRRS_M_CA2_DETAILTABLE WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, Integer.class);
	}

	public List<M_CA2_Detail_Entity> getDetailByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportDate) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?",
				new Object[] { reportLabel, reportAddlCriteria1, reportDate }, new M_CA2DetailRowMapper());
	}

	public M_CA2_Detail_Entity getDetailByAcctNumber(String acctNumber) {
		List<M_CA2_Detail_Entity> list = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNumber },
				new M_CA2DetailRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	public List<M_CA2_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, String version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA2ArchivalDetailRowMapper());
	}

	public List<M_CA2_Archival_Detail_Entity> getArchivalDetailByRowIdAndColumnId(String reportLabel, String criteria1,
			Date reportDate, String version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
				new Object[] { reportLabel, criteria1, reportDate, version }, new M_CA2ArchivalDetailRowMapper());
	}

	public List<M_CA2_RESUB_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate, String version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
				new Object[] { reportDate, version }, new M_CA2ResubDetailRowMapper());
	}

	public List<M_CA2_RESUB_Detail_Entity> getResubDetailByRowIdAndColumnId(String reportLabel, String criteria1,
			Date reportDate, String version) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_CA2_RESUB_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
				new Object[] { reportLabel, criteria1, reportDate, version }, new M_CA2ResubDetailRowMapper());
	}

	public ModelAndView getM_CA2View(String reportId, String fromdate, String todate, String currency, String dtltype, // kept
																														// but
																														// not
																														// used
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();
		String userid = (String) req1.getSession().getAttribute("USERID");

		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		try {

			// Parse date only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW DEBUG =======");
			System.out.println("TYPE    : " + type);
			System.out.println("DATE    : " + d1);
			System.out.println("VERSION : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY ONLY
			// ===========================================================

			/* ---------- ARCHIVAL SUMMARY ---------- */
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<M_CA2_Archival_Summary_Entity> summaryList = getArchivalSummaryByDateAndVersion(d1, version);

				System.out.println("Archival Summary Size : " + summaryList.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", summaryList);
			}

			/* ---------- RESUB SUMMARY ---------- */
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_CA2_RESUB_Summary_Entity> summaryList = getResubSummaryByDateAndVersion(d1, version);

				System.out.println("Resub Summary Size : " + summaryList.size());

				mv.addObject("displaymode", "resub");
				mv.addObject("reportsummary", summaryList);
			}

			/* ---------- NORMAL SUMMARY ---------- */
			else {

				List<M_CA2_Summary_Entity> summaryList = getSummaryByDate(d1);

				System.out.println("Normal Summary Size : " + summaryList.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", summaryList);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_CA2");
		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	public ModelAndView getM_CA2currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
			Model md) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");

		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		/* Session hs = sessionFactory.getCurrentSession(); */

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLabel = null;
			String reportAddlCriteria_1 = null;

			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLabel = parts[0];
					reportAddlCriteria_1 = parts[1];
				}
			}

			/*
			 * ========================================================= ARCHIVAL DETAIL
			 * =========================================================
			 */
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<M_CA2_Archival_Detail_Entity> T1Dt1;

				if (reportLabel != null && reportAddlCriteria_1 != null) {
					T1Dt1 = getArchivalDetailByRowIdAndColumnId(reportLabel, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = getArchivalDetailByDateAndVersion(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

			/*
			 * ========================================================= RESUB DETAIL ✅
			 * ADDED =========================================================
			 */
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_CA2_RESUB_Detail_Entity> T1Dt1;

				if (reportLabel != null && reportAddlCriteria_1 != null) {
					T1Dt1 = getResubDetailByRowIdAndColumnId(reportLabel, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = getResubDetailByDateAndVersion(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				System.out.println("RESUB COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

			/*
			 * ========================================================= CURRENT DETAIL
			 * =========================================================
			 */
			else {

				List<M_CA2_Detail_Entity> T1Dt1;

				if (reportLabel != null && reportAddlCriteria_1 != null) {
					T1Dt1 = getDetailByRowIdAndColumnId(reportLabel, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = getDetailByDate(parsedDate);

					totalPages = getDetailCount(parsedDate);

					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		// ✅ Common attributes
		mv.setViewName("BRRS/M_CA2");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	// Archival View
	public List<Object[]> getM_CA2Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_CA2_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_CA2_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_CA2_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_CA2  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public List<Object[]> getM_CA2Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA2_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA2_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_CA2 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	@Transactional
	public ResponseEntity<?> updateReport(M_CA2_Summary_Entity updatedEntity) {

		try {

			System.out.println("Updating CA2 Summary table");

			// =========================================
			// FETCH EXISTING RECORD
			// =========================================

			List<M_CA2_Summary_Entity> list = getSummaryByDate(updatedEntity.getReport_date());

			M_CA2_Summary_Entity existing;

			if (list.isEmpty()) {

				System.out.println("No record found for REPORT_DATE : " + updatedEntity.getReport_date());

				existing = new M_CA2_Summary_Entity();

				existing.setReport_date(updatedEntity.getReport_date());

			} else {

				existing = list.get(0);
			}

			// =========================================
			// AUDIT OLD COPY
			// =========================================

			M_CA2_Summary_Entity oldcopy = new M_CA2_Summary_Entity();

			BeanUtils.copyProperties(existing, oldcopy);

			boolean isChanged = false;

			// =========================================
			// ALLOWED ROWS
			// =========================================

			int[] amount2Indexes = { 11, 32, 42, 44, 43, 46 };

			int[] amount1Indexes = { 14, 15, 16, 18, 19, 21 };

			// =========================================
			// AMOUNT_2 UPDATE
			// =========================================

			for (int i : amount2Indexes) {

				String getterName = "getR" + i + "_amount_2";

				String setterName = "setR" + i + "_amount_2";

				try {

					Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);

					Method setter = M_CA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);

					Object oldValue = getter.invoke(existing);

					System.out.println("Updating R" + i + "_amount_2 : " + newValue);

					if (newValue != null && !newValue.equals(oldValue)) {

						setter.invoke(existing, newValue);

						isChanged = true;
					}

				} catch (NoSuchMethodException e) {

					continue;
				}
			}

			// =========================================
			// AMOUNT_1 UPDATE
			// =========================================

			for (int i : amount1Indexes) {

				String getterName = "getR" + i + "_amount_1";

				String setterName = "setR" + i + "_amount_1";

				try {

					Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);

					Method setter = M_CA2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);

					Object oldValue = getter.invoke(existing);

					System.out.println("Updating R" + i + "_amount_1 : " + newValue);

					if (newValue != null && !newValue.equals(oldValue)) {

						setter.invoke(existing, newValue);

						isChanged = true;
					}

				} catch (NoSuchMethodException e) {

					continue;
				}
			}

			// =========================================
			// METADATA
			// =========================================

			existing.setReport_version(updatedEntity.getReport_version());

			existing.setReport_frequency(updatedEntity.getReport_frequency());

			existing.setReport_code(updatedEntity.getReport_code());

			existing.setReport_desc(updatedEntity.getReport_desc());

			existing.setEntity_flg(updatedEntity.getEntity_flg());

			existing.setModify_flg(updatedEntity.getModify_flg());

			existing.setDel_flg(updatedEntity.getDel_flg());

			// =========================================
			// SAVE ONLY IF CHANGED
			// =========================================

			if (isChanged) {

				String changes = auditService.getChanges(oldcopy, existing);

				jdbcTemplate.update("UPDATE BRRS_M_CA2_SUMMARYTABLE SET "
						+ "R11_AMOUNT_2=?, R32_AMOUNT_2=?, R42_AMOUNT_2=?, R44_AMOUNT_2=?, R43_AMOUNT_2=?, R46_AMOUNT_2=?, "
						+ "R14_AMOUNT_1=?, R15_AMOUNT_1=?, R16_AMOUNT_1=?, R18_AMOUNT_1=?, R19_AMOUNT_1=?, R21_AMOUNT_1=?, "
						+ "REPORT_VERSION=?, REPORT_FREQUENCY=?, REPORT_CODE=?, REPORT_DESC=?, ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=? "
						+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)", existing.getR11_amount_2(), existing.getR32_amount_2(),
						existing.getR42_amount_2(), existing.getR44_amount_2(), existing.getR43_amount_2(),
						existing.getR46_amount_2(), existing.getR14_amount_1(), existing.getR15_amount_1(),
						existing.getR16_amount_1(), existing.getR18_amount_1(), existing.getR19_amount_1(),
						existing.getR21_amount_1(), existing.getReport_version(), existing.getReport_frequency(),
						existing.getReport_code(), existing.getReport_desc(), existing.getEntity_flg(),
						existing.getModify_flg(), existing.getDel_flg(), existing.getReport_date());

				if (!changes.isEmpty()) {

					auditService.compareEntitiesmanual(oldcopy, existing, updatedEntity.getReport_date().toString(),
							"M CA2 Summary Screen", "BRRS_M_CA2_SUMMARY");
				}

				System.out.println("CA2 Summary updated successfully");

				// =========================================
				// FORMAT DATE
				// =========================================

				String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(updatedEntity.getReport_date());

				// =========================================
				// CALL PROCEDURE AFTER COMMIT
				// =========================================

				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

					@Override
					public void afterCommit() {

						try {

							System.out.println("Transaction committed - calling procedure");

							jdbcTemplate.update("BEGIN BRRS_M_CA2_SUMMARY_PROCEDURE(?); END;", formattedDate);

							System.out.println("Procedure executed successfully");

						} catch (Exception e) {

							e.printStackTrace();
						}
					}
				});

				return ResponseEntity.ok("CA2 Summary updated successfully");

			} else {

				return ResponseEntity.ok("No changes detected");
			}

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating CA2 Summary : " + e.getMessage());
		}
	}

//	@Transactional
//	public void updateReport(M_CA2_Summary_Entity updatedEntity) {
//
//	    System.out.println("Came to services");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    M_CA2_Summary_Entity existing =
//	            BRRS_M_CA2_Summary_Repo.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() ->
//	                    new RuntimeException("Record not found for REPORT_DATE: "
//	                            + updatedEntity.getReport_date()));
//
//	    // Only allowed R-numbers
//	    int[] amount2Indexes = { 11, 32, 42, 44,43, 46,48 };     // AMOUNT_2	
//	    int[] amount1Indexes = {  14, 15, 16, 18, 19, 21 }; // AMOUNT_1
//
//	    try {
//
//	        // ===== AMOUNT_2 updates =====
//	        for (int i : amount2Indexes) {
//
//	            String getterName = "getR" + i + "_amount_2";
//	            String setterName = "setR" + i + "_amount_2";
//
//	            try {
//	                Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);
//	                Method setter = M_CA2_Summary_Entity.class.getMethod(
//	                        setterName,
//	                        getter.getReturnType()
//	                );
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                continue; // skip safely
//	            }
//	        }
//
//	        // ===== AMOUNT_1 updates =====
//	        for (int i : amount1Indexes) {
//
//	            String getterName = "getR" + i + "_amount_1";
//	            String setterName = "setR" + i + "_amount_1";
//
//	            try {
//	                Method getter = M_CA2_Summary_Entity.class.getMethod(getterName);
//	                Method setter = M_CA2_Summary_Entity.class.getMethod(
//	                        setterName,
//	                        getter.getReturnType()
//	                );
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                continue; // skip safely
//	            }
//	        }
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // Save only intended updates
//	    BRRS_M_CA2_Summary_Repo.saveAndFlush(existing);
//	}

	public byte[] getM_CA2DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_CA2 Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_CA2Detail");

// Common border style
			BorderStyle border = BorderStyle.THIN;

// Header style (left aligned)
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.LEFT);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(border);
			headerStyle.setBorderBottom(border);
			headerStyle.setBorderLeft(border);
			headerStyle.setBorderRight(border);

// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with thousand separator)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "ROWID", "COLUMNID",
					"REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_CA2_Detail_Entity> reportData = getDetailByDate(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_CA2_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_CA2 — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_CA2 Excel", e);
			return new byte[0];
		}
	}
//	public byte[] getM_CA2DetailExcel(String filename, String fromdate, String todate, String currency,
//										   String dtltype, String type, String version) {
//	    try {
//	        logger.info("Generating Excel for M_CA2 Details...");
//	        System.out.println("came to Detail download service");
//
//			if (type.equals("ARCHIVAL") & version != null) {
//				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
//						version);
//				return ARCHIVALreport;
//			}
//
//	        XSSFWorkbook workbook = new XSSFWorkbook();
//	        XSSFSheet sheet = workbook.createSheet("M_CA2Details");
//
//	        // Common border style
//	        BorderStyle border = BorderStyle.THIN;
//
//	        // Header style (left aligned)
//	        CellStyle headerStyle = workbook.createCellStyle();
//	        Font headerFont = workbook.createFont();
//	        headerFont.setBold(true);
//	        headerFont.setFontHeightInPoints((short) 10);
//	        headerStyle.setFont(headerFont);
//	        headerStyle.setAlignment(HorizontalAlignment.LEFT);
//	        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//	        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//	        headerStyle.setBorderTop(border);
//	        headerStyle.setBorderBottom(border);
//	        headerStyle.setBorderLeft(border);
//	        headerStyle.setBorderRight(border);
//
//	        // Right-aligned header style for ACCT BALANCE
//	        CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
//	        rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
//	        rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
//
//	        // Default data style (left aligned)
//	        CellStyle dataStyle = workbook.createCellStyle();
//	        dataStyle.setAlignment(HorizontalAlignment.LEFT);
//	        dataStyle.setBorderTop(border);
//	        dataStyle.setBorderBottom(border);
//	        dataStyle.setBorderLeft(border);
//	        dataStyle.setBorderRight(border);
//
//	        // ACCT BALANCE style (right aligned with 3 decimals)
//	        CellStyle balanceStyle = workbook.createCellStyle();
//	        balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
//	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
//	        balanceStyle.setBorderTop(border);
//	        balanceStyle.setBorderBottom(border);
//	        balanceStyle.setBorderLeft(border);
//	        balanceStyle.setBorderRight(border);
//
//	        // Header row
//	        String[] headers = {
//	            "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE"
//	        };
//
//	        XSSFRow headerRow = sheet.createRow(0);
//	        for (int i = 0; i < headers.length; i++) {
//	            Cell cell = headerRow.createCell(i);
//	            cell.setCellValue(headers[i]);
//
//	            if (i == 3) { // ACCT BALANCE
//	                cell.setCellStyle(rightAlignedHeaderStyle);
//	            } else {
//	                cell.setCellStyle(headerStyle);
//	            }
//
//	            sheet.setColumnWidth(i, 5000);
//	        }
//
//	        // Get data
//	        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
//	        List<M_CA2_Detail_Entity> reportData = getDetailByDate(parsedToDate);
//
//	        if (reportData != null && !reportData.isEmpty()) {
//	            int rowIndex = 1;
//	            for (M_CA2_Detail_Entity item : reportData) {
//	                XSSFRow row = sheet.createRow(rowIndex++);
//
//	                row.createCell(0).setCellValue(item.getCustId());
//	                row.createCell(1).setCellValue(item.getAcctNumber());
//	                row.createCell(2).setCellValue(item.getAcctName());
//
//	                // ACCT BALANCE (right aligned, 3 decimal places)
//	                Cell balanceCell = row.createCell(3);
//	                if (item.getAcctBalanceInPula() != null) {
//	                    balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
//	                } else {
//	                    balanceCell.setCellValue(0.000);
//	                }
//	                balanceCell.setCellStyle(balanceStyle);
//
//	                row.createCell(4).setCellValue(item.getRowId());
//	                row.createCell(5).setCellValue(item.getColumnId());
//	                row.createCell(6).setCellValue(
//	                    item.getReportDate() != null ?
//	                    new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
//	                );
//
//	                // Apply data style for all other cells
//	                for (int j = 0; j < 7; j++) {
//	                    if (j != 3) {
//	                        row.getCell(j).setCellStyle(dataStyle);
//	                    }
//	                }
//	            }
//	        } else {
//	            logger.info("No data found for M_CA2 — only header will be written.");
//	        }
//
//	        // Write to byte[]
//	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	        workbook.write(bos);
//	        workbook.close();
//
//	        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
//	        return bos.toByteArray();
//
//	    } catch (Exception e) {
//	        logger.error("Error generating M_CA2 Excel", e);
//	        return new byte[0];
//	    }
//	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_CA2 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_CA2Detail");

			// Common border style
			BorderStyle border = BorderStyle.THIN;

			// Header style (left aligned)
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.LEFT);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(border);
			headerStyle.setBorderBottom(border);
			headerStyle.setBorderLeft(border);
			headerStyle.setBorderRight(border);

			// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "ROWID", "COLUMNID",
					"REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_CA2_Archival_Detail_Entity> reportData = getArchivalDetailByDateAndVersion(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_CA2_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places with comma separator)
					Cell balanceCell = row.createCell(3);

					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}

					// Create style with thousand separator and decimal point
					DataFormat format = workbook.createDataFormat();

					// Format: 1,234,567
					balanceStyle.setDataFormat(format.getFormat("#,##0"));

					// Right alignment (optional)
					balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_CA2 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_CA2 Excel", e);
			return new byte[0];
		}
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_CA2"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			M_CA2_Detail_Entity la1Entity = getDetailByAcctNumber(acctNo);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_CA2"); // ✅ match the report name

		if (acctNo != null) {
			M_CA2_Detail_Entity la1Entity = getDetailByAcctNumber(acctNo);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
				System.out.println(formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInPula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_CA2_Detail_Entity existing = getDetailByAcctNumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			// Create old copy for audit comparison
			M_CA2_Detail_Entity oldcopy = new M_CA2_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
					existing.setAcctName(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			if (provisionStr != null && !provisionStr.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr);
				if (existing.getAcctBalanceInPula() == null
						|| existing.getAcctBalanceInPula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInPula(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (isChanged) {
				jdbcTemplate.update(
						"UPDATE BRRS_M_CA2_DETAILTABLE SET ACCT_NAME=?, ACCT_BALANCE_IN_PULA=? WHERE ACCT_NUMBER=?",
						existing.getAcctName(), existing.getAcctBalanceInPula(), existing.getAcctNumber());

				// Audit comparison
				auditService.compareEntitiesmanual(oldcopy, existing, acctNo, "M_CA2 Detail Screen",
						"BRRS_M_CA2_DETAIL");

				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit

				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

					@Override
					public void afterCommit() {
						try {

							logger.info("Transaction committed — calling BRRS_M_CA2_SUMMARY_PROCEDURE({})",
									formattedDate);

							jdbcTemplate.update("BEGIN BRRS_M_CA2_SUMMARY_PROCEDURE(?); END;", formattedDate);

							logger.info("Procedure executed successfully after commit.");

						} catch (Exception e) {
							logger.error("Error executing procedure after commit", e);
						}
					}
				});

				return ResponseEntity.ok("Record updated successfully!");
			} else {
				logger.info("No changes detected for ACCT_NO: {}", acctNo);
				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {
			logger.error("Error updating M_CA2 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

//Normal format Excel

	public byte[] getM_CA2Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_CA2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
// ✅ Redirecting to Resub Excel
				return BRRS_M_CA2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_CA2EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

// Fetch data

				List<M_CA2_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_CA2 report. Returning empty result.");
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
							M_CA2_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							Cell cell3, cell4;
							CellStyle originalStyle;

							// row6
							// Column C
							Cell cellBdate = row.createCell(2);
							if (record.getReport_date() != null) {
								cellBdate.setCellValue(record.getReport_date());
								cellBdate.setCellStyle(dateStyle);
							} else {
								cellBdate.setCellValue("");
								cellBdate.setCellStyle(textStyle);
							}

// ===== Row 10 / Col D =====
							row = sheet.getRow(9);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR10_amount_2() != null)
								cell4.setCellValue(record.getR10_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 11 / Col D =====
							row = sheet.getRow(10);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR11_amount_2() != null)
								cell4.setCellValue(record.getR11_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 13 / Col C =====
							row = sheet.getRow(12);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR13_amount_1() != null)
								cell3.setCellValue(record.getR13_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 14 / Col C =====
							row = sheet.getRow(13);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR14_amount_1() != null)
								cell3.setCellValue(record.getR14_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 15 / Col C =====
							row = sheet.getRow(14);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR15_amount_1() != null)
								cell3.setCellValue(record.getR15_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 16 / Col C =====
							row = sheet.getRow(15);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR16_amount_1() != null)
								cell3.setCellValue(record.getR16_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 18 / Col C =====
							row = sheet.getRow(17);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR18_amount_1() != null)
								cell3.setCellValue(record.getR18_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 19 / Col C =====
							row = sheet.getRow(18);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR19_amount_1() != null)
								cell3.setCellValue(record.getR19_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 20 / Col C =====
							row = sheet.getRow(19);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR20_amount_1() != null)
								cell3.setCellValue(record.getR20_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 21 / Col C =====
							row = sheet.getRow(20);
							cell3 = row.getCell(2);
							if (cell3 == null)
								cell3 = row.createCell(2);
							originalStyle = cell3.getCellStyle();
							if (record.getR21_amount_1() != null)
								cell3.setCellValue(record.getR21_amount_1().doubleValue());
							else
								cell3.setCellValue("");
							cell3.setCellStyle(originalStyle);

// ===== Row 22 / Col D =====
							row = sheet.getRow(21);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR22_amount_2() != null)
								cell4.setCellValue(record.getR22_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 23 / Col D =====
							row = sheet.getRow(22);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR23_amount_2() != null)
								cell4.setCellValue(record.getR23_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 24 / Col D =====
							row = sheet.getRow(23);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR24_amount_2() != null)
								cell4.setCellValue(record.getR24_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 25 / Col D =====
							row = sheet.getRow(24);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR25_amount_2() != null)
								cell4.setCellValue(record.getR25_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 31 / Col D =====
							row = sheet.getRow(30);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR31_amount_2() != null)
								cell4.setCellValue(record.getR31_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 32 / Col D =====
							row = sheet.getRow(31);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR32_amount_2() != null)
								cell4.setCellValue(record.getR32_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 33 / Col D =====
							row = sheet.getRow(32);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR33_amount_2() != null)
								cell4.setCellValue(record.getR33_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 34 / Col D =====
							row = sheet.getRow(33);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR34_amount_2() != null)
								cell4.setCellValue(record.getR34_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 41 / Col D =====
							row = sheet.getRow(40);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR41_amount_2() != null)
								cell4.setCellValue(record.getR41_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 42 / Col D =====
							row = sheet.getRow(41);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR42_amount_2() != null)
								cell4.setCellValue(record.getR42_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 43 / Col D =====
							row = sheet.getRow(42);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR43_amount_2() != null)
								cell4.setCellValue(record.getR43_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 44 / Col D =====
							row = sheet.getRow(43);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR44_amount_2() != null)
								cell4.setCellValue(record.getR44_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 45 / Col D =====
							row = sheet.getRow(44);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR45_amount_2() != null)
								cell4.setCellValue(record.getR45_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 46 / Col D =====
							row = sheet.getRow(45);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR46_amount_2() != null)
								cell4.setCellValue(record.getR46_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);

// ===== Row 47 / Col D =====
							row = sheet.getRow(46);
							cell4 = row.getCell(3);
							if (cell4 == null)
								cell4 = row.createCell(3);
							originalStyle = cell4.getCellStyle();
							if (record.getR47_amount_2() != null)
								cell4.setCellValue(record.getR47_amount_2().doubleValue());
							else
								cell4.setCellValue("");
							cell4.setCellStyle(originalStyle);
						}

						workbook.setForceFormulaRecalculation(true);
					} else {

					}

// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

					// audit service summary format

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 SUMMARY", null,
								"BRRS_M_CA2_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}
		}
	}

// Normal Email Excel
	public byte[] BRRS_M_CA2EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
// Redirecting to Archival
				return BRRS_M_CA2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
// ✅ Redirecting to Resub Excel
				return BRRS_M_CA2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_CA2_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_CA2 report. Returning empty result.");
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
						M_CA2_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						Cell cell3, cell4;
						CellStyle originalStyle;

						// row6
						// Column C
						Cell cellBdate = row.createCell(2);
						if (record.getReport_date() != null) {
							cellBdate.setCellValue(record.getReport_date());
							cellBdate.setCellStyle(dateStyle);
						} else {
							cellBdate.setCellValue("");
							cellBdate.setCellStyle(textStyle);
						}

// ROW 10 = Common shares

// ===== Row 10 / Col D =====
						row = sheet.getRow(9);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR10_amount_2() != null)
							cell4.setCellValue(record.getR10_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 11 = Share premium resulting from the issue of common shares

// ===== Row 11 / Col D =====
						row = sheet.getRow(10);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR11_amount_2() != null)
							cell4.setCellValue(record.getR11_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 12 = Retained earnings

//ROW 13 = Retained earnings brought forward from the previous financial year

//===== Row 13 / Col C =====
						row = sheet.getRow(12);
						cell3 = row.getCell(3);
						if (cell3 == null)
							cell3 = row.createCell(2);
						originalStyle = cell3.getCellStyle();
						if (record.getR13_amount_1() != null)
							cell3.setCellValue(record.getR13_amount_1().doubleValue());
						else
							cell3.setCellValue("");
						cell3.setCellStyle(originalStyle);

//ROW 14 = Add: Interim profits (reviewed/audited by external auditor)

// ===== Row 14 / Col C =====
						row = sheet.getRow(13);
						cell3 = row.getCell(3);
						if (cell3 == null)
							cell3 = row.createCell(2);
						originalStyle = cell3.getCellStyle();
						if (record.getR14_amount_1() != null)
							cell3.setCellValue(record.getR14_amount_1().doubleValue());
						else
							cell3.setCellValue("");
						cell3.setCellStyle(originalStyle);

//ROW 15 = Less: Dividend paid (2024–25)

// ===== Row 15 / Col C =====
						row = sheet.getRow(14);
						cell3 = row.getCell(3);
						if (cell3 == null)
							cell3 = row.createCell(2);
						originalStyle = cell3.getCellStyle();
						if (record.getR15_amount_1() != null)
							cell3.setCellValue(record.getR15_amount_1().doubleValue());
						else
							cell3.setCellValue("");
						cell3.setCellStyle(originalStyle);

//ROW 16 = Less: Dividend paid in the current financial year

// ===== Row 16 / Col C =====
						row = sheet.getRow(15);
						cell3 = row.getCell(3);
						if (cell3 == null)
							cell3 = row.createCell(2);
						originalStyle = cell3.getCellStyle();
						if (record.getR16_amount_1() != null)
							cell3.setCellValue(record.getR16_amount_1().doubleValue());
						else
							cell3.setCellValue("");
						cell3.setCellStyle(originalStyle);

//ROW 17 = Net Retained Earnings

//ROW 18 = Accumulated other Comprehensive income and other disclosed reserves

// ===== Row 17 / Col D =====
						row = sheet.getRow(17);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR17_amount_2() != null)
							cell4.setCellValue(record.getR17_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 19 = Common shares issued by consolidated subsidiaries of the bank and held by third parties (Minority interest)

// ===== Row 19 / Col D =====
						row = sheet.getRow(18);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR22_amount_2() != null)
							cell4.setCellValue(record.getR22_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 20 = Regulatory adjustments applied in the calculation of CET1 Capital

// ===== Row 23 / Col D =====
						row = sheet.getRow(19);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR23_amount_2() != null)
							cell4.setCellValue(record.getR23_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 21 = IFRS Provisions Transitional Adjustments

// ===== Row 24 / Col D =====
						row = sheet.getRow(20);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR24_amount_2() != null)
							cell4.setCellValue(record.getR24_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 22 = Transitional Adjustment Amount Added Back to CET1

// ===== Row 25 / Col D =====
						row = sheet.getRow(21);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR25_amount_2() != null)
							cell4.setCellValue(record.getR25_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW 23 = CET1 Capital (Lines 1 + 2 + 3 + 4 + 5 − 6)

//ROW R29 = Instruments issued by the bank that meet the criteria for inclusion in Additional Tier 1 Capital as per paragraph 4.9 of the Capital Directive

// ===== Row 31 / Col D =====
						row = sheet.getRow(28);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR31_amount_2() != null)
							cell4.setCellValue(record.getR31_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R30 = Stock surplus (Share premium) resulting from the issue of Additional Tier 1 capital instruments meeting all relevant criteria for inclusion

// ===== Row 32 / Col D =====
						row = sheet.getRow(29);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR32_amount_2() != null)
							cell4.setCellValue(record.getR32_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R31 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Additional Tier 1 capital and are not included in CET1, subject to terms and conditions in 3.5

// ===== Row 33 / Col D =====
						row = sheet.getRow(30);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR33_amount_2() != null)
							cell4.setCellValue(record.getR33_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R32 = Regulatory adjustments applied in the calculation of Additional Tier 1 Capital

// ===== Row 34 / Col D =====
						row = sheet.getRow(31);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR34_amount_2() != null)
							cell4.setCellValue(record.getR34_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R33 = Additional Tier 1 Capital (Line 9 + 10 + 11 + 12)

//ROW R34 = Total Tier 1 Capital (Line 7 + 13)

//ROW R39 = Instruments issued by the bank that meet the criteria for inclusion in Tier 2 capital (and are not included in Tier 1 capital)

// ===== Row 41 / Col D =====
						row = sheet.getRow(38);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR41_amount_2() != null)
							cell4.setCellValue(record.getR41_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R40 = Stock surplus (share premium) resulting from the issue of instruments included in Tier 2 capital

// ===== Row 42 / Col D =====
						row = sheet.getRow(39);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR42_amount_2() != null)
							cell4.setCellValue(record.getR42_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R41 = Unpublished Current Year's Profits

// ===== Row 43 / Col D =====
						row = sheet.getRow(40);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR43_amount_2() != null)
							cell4.setCellValue(record.getR43_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R42 = Tier 2 capital instruments (subject to gradual phase-out treatment)

// ===== Row 44 / Col D =====
						row = sheet.getRow(41);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR44_amount_2() != null)
							cell4.setCellValue(record.getR44_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R43 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Tier 2 capital and are not included in Tier 1 capital (minority interests)

// ===== Row 45 / Col D =====
						row = sheet.getRow(42);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR45_amount_2() != null)
							cell4.setCellValue(record.getR45_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R44 = General provisions / general loan-loss reserves eligible for inclusion in Tier 2, limited to a maximum of 1.25 percentage points of credit risk-weighted risk assets calculated under the standardised approach

// ===== Row 46 / Col D =====
						row = sheet.getRow(43);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR46_amount_2() != null)
							cell4.setCellValue(record.getR46_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);

//ROW R45 = Regulatory adjustments applied in the calculation of Tier 2 Capital

// ===== Row 47 / Col D =====
						row = sheet.getRow(44);
						cell4 = row.getCell(3);
						if (cell4 == null)
							cell4 = row.createCell(3);
						originalStyle = cell4.getCellStyle();
						if (record.getR47_amount_2() != null)
							cell4.setCellValue(record.getR47_amount_2().doubleValue());
						else
							cell4.setCellValue("");
						cell4.setCellStyle(originalStyle);
					}

					workbook.setForceFormulaRecalculation(true);
				} else {

				}

// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				// audit service summary email

				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 EMAIL SUMMARY", null,
							"BRRS_M_CA2_SUMMARYTABLE");
				}

				return out.toByteArray();
			}
		}
	}

// Archival format excel
	public byte[] getExcelM_CA2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
// Redirecting to Archival
				return BRRS_M_CA2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA2_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA2 report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
//This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
//A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

//This try-with-resources block is perfect. It guarantees all resources are
//closed automatically.
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

//--- Style Definitions ---
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

//Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
//--- End of Style Definitions ---

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell3, cell4;
					CellStyle originalStyle;

					// row6
					// Column C
					Cell cellBdate = row.createCell(2);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

// ===== Row 10 / Col D =====
					row = sheet.getRow(9);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR10_amount_2() != null)
						cell4.setCellValue(record.getR10_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 11 / Col D =====
					row = sheet.getRow(10);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_amount_2() != null)
						cell4.setCellValue(record.getR11_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_amount_1() != null)
						cell3.setCellValue(record.getR13_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_amount_1() != null)
						cell3.setCellValue(record.getR14_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_amount_1() != null)
						cell3.setCellValue(record.getR15_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_amount_1() != null)
						cell3.setCellValue(record.getR16_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR18_amount_1() != null)
						cell3.setCellValue(record.getR18_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR19_amount_1() != null)
						cell3.setCellValue(record.getR19_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 20 / Col C =====
					row = sheet.getRow(19);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR20_amount_1() != null)
						cell3.setCellValue(record.getR20_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 21 / Col C =====
					row = sheet.getRow(20);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR21_amount_1() != null)
						cell3.setCellValue(record.getR21_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 22 / Col D =====
					row = sheet.getRow(21);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR22_amount_2() != null)
						cell4.setCellValue(record.getR22_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 23 / Col D =====
					row = sheet.getRow(22);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR23_amount_2() != null)
						cell4.setCellValue(record.getR23_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 24 / Col D =====
					row = sheet.getRow(23);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR24_amount_2() != null)
						cell4.setCellValue(record.getR24_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 25 / Col D =====
					row = sheet.getRow(24);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR25_amount_2() != null)
						cell4.setCellValue(record.getR25_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 31 / Col D =====
					row = sheet.getRow(30);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR31_amount_2() != null)
						cell4.setCellValue(record.getR31_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 32 / Col D =====
					row = sheet.getRow(31);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR32_amount_2() != null)
						cell4.setCellValue(record.getR32_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 33 / Col D =====
					row = sheet.getRow(32);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR33_amount_2() != null)
						cell4.setCellValue(record.getR33_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 34 / Col D =====
					row = sheet.getRow(33);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR34_amount_2() != null)
						cell4.setCellValue(record.getR34_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 41 / Col D =====
					row = sheet.getRow(40);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR41_amount_2() != null)
						cell4.setCellValue(record.getR41_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 42 / Col D =====
					row = sheet.getRow(41);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR42_amount_2() != null)
						cell4.setCellValue(record.getR42_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 43 / Col D =====
					row = sheet.getRow(42);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR43_amount_2() != null)
						cell4.setCellValue(record.getR43_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 44 / Col D =====
					row = sheet.getRow(43);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR44_amount_2() != null)
						cell4.setCellValue(record.getR44_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 45 / Col D =====
					row = sheet.getRow(44);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR45_amount_2() != null)
						cell4.setCellValue(record.getR45_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 46 / Col D =====
					row = sheet.getRow(45);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR46_amount_2() != null)
						cell4.setCellValue(record.getR46_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 47 / Col D =====
					row = sheet.getRow(46);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR47_amount_2() != null)
						cell4.setCellValue(record.getR47_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

//Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service archival summary format

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 ARCHIVAL SUMMARY", null,
						"BRRS_M_CA2_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}

	}

// Archival Email Excel
	public byte[] BRRS_M_CA2ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA2_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA2 report. Returning empty result.");
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
					M_CA2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell3, cell4;
					CellStyle originalStyle;

					// row6
					// Column C
					Cell cellBdate = row.createCell(2);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

// ROW 10 = Common shares

// ===== Row 10 / Col D =====
					row = sheet.getRow(9);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR10_amount_2() != null)
						cell4.setCellValue(record.getR10_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 11 = Share premium resulting from the issue of common shares

// ===== Row 11 / Col D =====
					row = sheet.getRow(10);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_amount_2() != null)
						cell4.setCellValue(record.getR11_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 12 = Retained earnings

//ROW 13 = Retained earnings brought forward from the previous financial year

//===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_amount_1() != null)
						cell3.setCellValue(record.getR13_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 14 = Add: Interim profits (reviewed/audited by external auditor)

// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_amount_1() != null)
						cell3.setCellValue(record.getR14_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 15 = Less: Dividend paid (2024–25)

// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_amount_1() != null)
						cell3.setCellValue(record.getR15_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 16 = Less: Dividend paid in the current financial year

// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_amount_1() != null)
						cell3.setCellValue(record.getR16_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 17 = Net Retained Earnings

//ROW 18 = Accumulated other Comprehensive income and other disclosed reserves

// ===== Row 17 / Col D =====
					row = sheet.getRow(17);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR17_amount_2() != null)
						cell4.setCellValue(record.getR17_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 19 = Common shares issued by consolidated subsidiaries of the bank and held by third parties (Minority interest)

// ===== Row 19 / Col D =====
					row = sheet.getRow(18);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR22_amount_2() != null)
						cell4.setCellValue(record.getR22_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 20 = Regulatory adjustments applied in the calculation of CET1 Capital

// ===== Row 23 / Col D =====
					row = sheet.getRow(19);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR23_amount_2() != null)
						cell4.setCellValue(record.getR23_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 21 = IFRS Provisions Transitional Adjustments

// ===== Row 24 / Col D =====
					row = sheet.getRow(20);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR24_amount_2() != null)
						cell4.setCellValue(record.getR24_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 22 = Transitional Adjustment Amount Added Back to CET1

// ===== Row 25 / Col D =====
					row = sheet.getRow(21);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR25_amount_2() != null)
						cell4.setCellValue(record.getR25_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 23 = CET1 Capital (Lines 1 + 2 + 3 + 4 + 5 − 6)

//ROW R29 = Instruments issued by the bank that meet the criteria for inclusion in Additional Tier 1 Capital as per paragraph 4.9 of the Capital Directive

// ===== Row 31 / Col D =====
					row = sheet.getRow(28);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR31_amount_2() != null)
						cell4.setCellValue(record.getR31_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R30 = Stock surplus (Share premium) resulting from the issue of Additional Tier 1 capital instruments meeting all relevant criteria for inclusion

// ===== Row 32 / Col D =====
					row = sheet.getRow(29);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR32_amount_2() != null)
						cell4.setCellValue(record.getR32_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R31 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Additional Tier 1 capital and are not included in CET1, subject to terms and conditions in 3.5

// ===== Row 33 / Col D =====
					row = sheet.getRow(30);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR33_amount_2() != null)
						cell4.setCellValue(record.getR33_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R32 = Regulatory adjustments applied in the calculation of Additional Tier 1 Capital

// ===== Row 34 / Col D =====
					row = sheet.getRow(31);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR34_amount_2() != null)
						cell4.setCellValue(record.getR34_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R33 = Additional Tier 1 Capital (Line 9 + 10 + 11 + 12)

//ROW R34 = Total Tier 1 Capital (Line 7 + 13)

//ROW R39 = Instruments issued by the bank that meet the criteria for inclusion in Tier 2 capital (and are not included in Tier 1 capital)

// ===== Row 41 / Col D =====
					row = sheet.getRow(38);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR41_amount_2() != null)
						cell4.setCellValue(record.getR41_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R40 = Stock surplus (share premium) resulting from the issue of instruments included in Tier 2 capital

// ===== Row 42 / Col D =====
					row = sheet.getRow(39);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR42_amount_2() != null)
						cell4.setCellValue(record.getR42_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R41 = Unpublished Current Year's Profits

// ===== Row 43 / Col D =====
					row = sheet.getRow(40);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR43_amount_2() != null)
						cell4.setCellValue(record.getR43_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R42 = Tier 2 capital instruments (subject to gradual phase-out treatment)

// ===== Row 44 / Col D =====
					row = sheet.getRow(41);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR44_amount_2() != null)
						cell4.setCellValue(record.getR44_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R43 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Tier 2 capital and are not included in Tier 1 capital (minority interests)

// ===== Row 45 / Col D =====
					row = sheet.getRow(42);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR45_amount_2() != null)
						cell4.setCellValue(record.getR45_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R44 = General provisions / general loan-loss reserves eligible for inclusion in Tier 2, limited to a maximum of 1.25 percentage points of credit risk-weighted risk assets calculated under the standardised approach

// ===== Row 46 / Col D =====
					row = sheet.getRow(43);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR46_amount_2() != null)
						cell4.setCellValue(record.getR46_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R45 = Regulatory adjustments applied in the calculation of Tier 2 Capital

// ===== Row 47 / Col D =====
					row = sheet.getRow(44);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR47_amount_2() != null)
						cell4.setCellValue(record.getR47_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service archival summary email

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_CA2_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}
	}

// Resub Format excel
	public byte[] BRRS_M_CA2ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
// ✅ Redirecting to Resub Excel
				return BRRS_M_CA2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_CA2_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA2 report. Returning empty result.");
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

					M_CA2_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cell3, cell4;
					CellStyle originalStyle;

					// row6
					// Column C
					Cell cellBdate = row.createCell(2);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

// ===== Row 10 / Col D =====
					row = sheet.getRow(9);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR10_amount_2() != null)
						cell4.setCellValue(record.getR10_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 11 / Col D =====
					row = sheet.getRow(10);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_amount_2() != null)
						cell4.setCellValue(record.getR11_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_amount_1() != null)
						cell3.setCellValue(record.getR13_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_amount_1() != null)
						cell3.setCellValue(record.getR14_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_amount_1() != null)
						cell3.setCellValue(record.getR15_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_amount_1() != null)
						cell3.setCellValue(record.getR16_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 18 / Col C =====
					row = sheet.getRow(17);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR18_amount_1() != null)
						cell3.setCellValue(record.getR18_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 19 / Col C =====
					row = sheet.getRow(18);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR19_amount_1() != null)
						cell3.setCellValue(record.getR19_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 20 / Col C =====
					row = sheet.getRow(19);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR20_amount_1() != null)
						cell3.setCellValue(record.getR20_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 21 / Col C =====
					row = sheet.getRow(20);
					cell3 = row.getCell(2);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR21_amount_1() != null)
						cell3.setCellValue(record.getR21_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

// ===== Row 22 / Col D =====
					row = sheet.getRow(21);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR22_amount_2() != null)
						cell4.setCellValue(record.getR22_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 23 / Col D =====
					row = sheet.getRow(22);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR23_amount_2() != null)
						cell4.setCellValue(record.getR23_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 24 / Col D =====
					row = sheet.getRow(23);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR24_amount_2() != null)
						cell4.setCellValue(record.getR24_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 25 / Col D =====
					row = sheet.getRow(24);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR25_amount_2() != null)
						cell4.setCellValue(record.getR25_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 31 / Col D =====
					row = sheet.getRow(30);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR31_amount_2() != null)
						cell4.setCellValue(record.getR31_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 32 / Col D =====
					row = sheet.getRow(31);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR32_amount_2() != null)
						cell4.setCellValue(record.getR32_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 33 / Col D =====
					row = sheet.getRow(32);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR33_amount_2() != null)
						cell4.setCellValue(record.getR33_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 34 / Col D =====
					row = sheet.getRow(33);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR34_amount_2() != null)
						cell4.setCellValue(record.getR34_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 41 / Col D =====
					row = sheet.getRow(40);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR41_amount_2() != null)
						cell4.setCellValue(record.getR41_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 42 / Col D =====
					row = sheet.getRow(41);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR42_amount_2() != null)
						cell4.setCellValue(record.getR42_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 43 / Col D =====
					row = sheet.getRow(42);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR43_amount_2() != null)
						cell4.setCellValue(record.getR43_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 44 / Col D =====
					row = sheet.getRow(43);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR44_amount_2() != null)
						cell4.setCellValue(record.getR44_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 45 / Col D =====
					row = sheet.getRow(44);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR45_amount_2() != null)
						cell4.setCellValue(record.getR45_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 46 / Col D =====
					row = sheet.getRow(45);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR46_amount_2() != null)
						cell4.setCellValue(record.getR46_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

// ===== Row 47 / Col D =====
					row = sheet.getRow(46);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR47_amount_2() != null)
						cell4.setCellValue(record.getR47_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service summary resub format

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 RESUB SUMMARY", null,
						"BRRS_M_CA2_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}

	}

// Resub Email Excel
	public byte[] BRRS_M_CA2EmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_CA2_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA2 report. Returning empty result.");
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
					M_CA2_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell3, cell4;
					CellStyle originalStyle;

					// row6
					// Column C
					Cell cellBdate = row.createCell(2);
					if (record.getReport_date() != null) {
						cellBdate.setCellValue(record.getReport_date());
						cellBdate.setCellStyle(dateStyle);
					} else {
						cellBdate.setCellValue("");
						cellBdate.setCellStyle(textStyle);
					}

// ROW 10 = Common shares

// ===== Row 10 / Col D =====
					row = sheet.getRow(9);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR10_amount_2() != null)
						cell4.setCellValue(record.getR10_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 11 = Share premium resulting from the issue of common shares

// ===== Row 11 / Col D =====
					row = sheet.getRow(10);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR11_amount_2() != null)
						cell4.setCellValue(record.getR11_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 12 = Retained earnings

//ROW 13 = Retained earnings brought forward from the previous financial year

//===== Row 13 / Col C =====
					row = sheet.getRow(12);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR13_amount_1() != null)
						cell3.setCellValue(record.getR13_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 14 = Add: Interim profits (reviewed/audited by external auditor)

// ===== Row 14 / Col C =====
					row = sheet.getRow(13);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR14_amount_1() != null)
						cell3.setCellValue(record.getR14_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 15 = Less: Dividend paid (2024–25)

// ===== Row 15 / Col C =====
					row = sheet.getRow(14);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR15_amount_1() != null)
						cell3.setCellValue(record.getR15_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 16 = Less: Dividend paid in the current financial year

// ===== Row 16 / Col C =====
					row = sheet.getRow(15);
					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(2);
					originalStyle = cell3.getCellStyle();
					if (record.getR16_amount_1() != null)
						cell3.setCellValue(record.getR16_amount_1().doubleValue());
					else
						cell3.setCellValue("");
					cell3.setCellStyle(originalStyle);

//ROW 17 = Net Retained Earnings

//ROW 18 = Accumulated other Comprehensive income and other disclosed reserves

// ===== Row 17 / Col D =====
					row = sheet.getRow(17);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR17_amount_2() != null)
						cell4.setCellValue(record.getR17_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 19 = Common shares issued by consolidated subsidiaries of the bank and held by third parties (Minority interest)

// ===== Row 19 / Col D =====
					row = sheet.getRow(18);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR22_amount_2() != null)
						cell4.setCellValue(record.getR22_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 20 = Regulatory adjustments applied in the calculation of CET1 Capital

// ===== Row 23 / Col D =====
					row = sheet.getRow(19);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR23_amount_2() != null)
						cell4.setCellValue(record.getR23_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 21 = IFRS Provisions Transitional Adjustments

// ===== Row 24 / Col D =====
					row = sheet.getRow(20);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR24_amount_2() != null)
						cell4.setCellValue(record.getR24_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 22 = Transitional Adjustment Amount Added Back to CET1

// ===== Row 25 / Col D =====
					row = sheet.getRow(21);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR25_amount_2() != null)
						cell4.setCellValue(record.getR25_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW 23 = CET1 Capital (Lines 1 + 2 + 3 + 4 + 5 − 6)

//ROW R29 = Instruments issued by the bank that meet the criteria for inclusion in Additional Tier 1 Capital as per paragraph 4.9 of the Capital Directive

// ===== Row 31 / Col D =====
					row = sheet.getRow(28);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR31_amount_2() != null)
						cell4.setCellValue(record.getR31_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R30 = Stock surplus (Share premium) resulting from the issue of Additional Tier 1 capital instruments meeting all relevant criteria for inclusion

// ===== Row 32 / Col D =====
					row = sheet.getRow(29);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR32_amount_2() != null)
						cell4.setCellValue(record.getR32_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R31 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Additional Tier 1 capital and are not included in CET1, subject to terms and conditions in 3.5

// ===== Row 33 / Col D =====
					row = sheet.getRow(30);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR33_amount_2() != null)
						cell4.setCellValue(record.getR33_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R32 = Regulatory adjustments applied in the calculation of Additional Tier 1 Capital

// ===== Row 34 / Col D =====
					row = sheet.getRow(31);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR34_amount_2() != null)
						cell4.setCellValue(record.getR34_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R33 = Additional Tier 1 Capital (Line 9 + 10 + 11 + 12)

//ROW R34 = Total Tier 1 Capital (Line 7 + 13)

//ROW R39 = Instruments issued by the bank that meet the criteria for inclusion in Tier 2 capital (and are not included in Tier 1 capital)

// ===== Row 41 / Col D =====
					row = sheet.getRow(38);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR41_amount_2() != null)
						cell4.setCellValue(record.getR41_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R40 = Stock surplus (share premium) resulting from the issue of instruments included in Tier 2 capital

// ===== Row 42 / Col D =====
					row = sheet.getRow(39);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR42_amount_2() != null)
						cell4.setCellValue(record.getR42_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R41 = Unpublished Current Year's Profits

// ===== Row 43 / Col D =====
					row = sheet.getRow(40);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR43_amount_2() != null)
						cell4.setCellValue(record.getR43_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R42 = Tier 2 capital instruments (subject to gradual phase-out treatment)

// ===== Row 44 / Col D =====
					row = sheet.getRow(41);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR44_amount_2() != null)
						cell4.setCellValue(record.getR44_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R43 = Instruments issued by consolidated subsidiaries of the bank and held by third parties that meet the criteria for inclusion in Tier 2 capital and are not included in Tier 1 capital (minority interests)

// ===== Row 45 / Col D =====
					row = sheet.getRow(42);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR45_amount_2() != null)
						cell4.setCellValue(record.getR45_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R44 = General provisions / general loan-loss reserves eligible for inclusion in Tier 2, limited to a maximum of 1.25 percentage points of credit risk-weighted risk assets calculated under the standardised approach

// ===== Row 46 / Col D =====
					row = sheet.getRow(43);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR46_amount_2() != null)
						cell4.setCellValue(record.getR46_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);

//ROW R45 = Regulatory adjustments applied in the calculation of Tier 2 Capital

// ===== Row 47 / Col D =====
					row = sheet.getRow(44);
					cell4 = row.getCell(3);
					if (cell4 == null)
						cell4 = row.createCell(3);
					originalStyle = cell4.getCellStyle();
					if (record.getR47_amount_2() != null)
						cell4.setCellValue(record.getR47_amount_2().doubleValue());
					else
						cell4.setCellValue("");
					cell4.setCellStyle(originalStyle);
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service summary resub email

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA2 EMAIL RESUB SUMMARY", null,
						"BRRS_M_CA2_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}
	}

	// =========================================================
	// RowMapper — external M_CA2_Summary_Entity
	// =========================================================
	class M_CA2SummaryRowMapper implements RowMapper<M_CA2_Summary_Entity> {
		@Override
		public M_CA2_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_Summary_Entity obj = new M_CA2_Summary_Entity();
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_amount_1(rs.getBigDecimal("R10_AMOUNT_1"));
			obj.setR10_amount_2(rs.getBigDecimal("R10_AMOUNT_2"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount_1(rs.getBigDecimal("R11_AMOUNT_1"));
			obj.setR11_amount_2(rs.getBigDecimal("R11_AMOUNT_2"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount_1(rs.getBigDecimal("R12_AMOUNT_1"));
			obj.setR12_amount_2(rs.getBigDecimal("R12_AMOUNT_2"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount_1(rs.getBigDecimal("R13_AMOUNT_1"));
			obj.setR13_amount_2(rs.getBigDecimal("R13_AMOUNT_2"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount_1(rs.getBigDecimal("R14_AMOUNT_1"));
			obj.setR14_amount_2(rs.getBigDecimal("R14_AMOUNT_2"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount_1(rs.getBigDecimal("R15_AMOUNT_1"));
			obj.setR15_amount_2(rs.getBigDecimal("R15_AMOUNT_2"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_amount_1(rs.getBigDecimal("R16_AMOUNT_1"));
			obj.setR16_amount_2(rs.getBigDecimal("R16_AMOUNT_2"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_amount_1(rs.getBigDecimal("R17_AMOUNT_1"));
			obj.setR17_amount_2(rs.getBigDecimal("R17_AMOUNT_2"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_amount_1(rs.getBigDecimal("R18_AMOUNT_1"));
			obj.setR18_amount_2(rs.getBigDecimal("R18_AMOUNT_2"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_amount_1(rs.getBigDecimal("R19_AMOUNT_1"));
			obj.setR19_amount_2(rs.getBigDecimal("R19_AMOUNT_2"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_amount_1(rs.getBigDecimal("R20_AMOUNT_1"));
			obj.setR20_amount_2(rs.getBigDecimal("R20_AMOUNT_2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_amount_1(rs.getBigDecimal("R21_AMOUNT_1"));
			obj.setR21_amount_2(rs.getBigDecimal("R21_AMOUNT_2"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_amount_1(rs.getBigDecimal("R22_AMOUNT_1"));
			obj.setR22_amount_2(rs.getBigDecimal("R22_AMOUNT_2"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_amount_1(rs.getBigDecimal("R23_AMOUNT_1"));
			obj.setR23_amount_2(rs.getBigDecimal("R23_AMOUNT_2"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_amount_1(rs.getBigDecimal("R24_AMOUNT_1"));
			obj.setR24_amount_2(rs.getBigDecimal("R24_AMOUNT_2"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_amount_1(rs.getBigDecimal("R25_AMOUNT_1"));
			obj.setR25_amount_2(rs.getBigDecimal("R25_AMOUNT_2"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_amount_1(rs.getBigDecimal("R26_AMOUNT_1"));
			obj.setR26_amount_2(rs.getBigDecimal("R26_AMOUNT_2"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_amount_1(rs.getBigDecimal("R31_AMOUNT_1"));
			obj.setR31_amount_2(rs.getBigDecimal("R31_AMOUNT_2"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_amount_1(rs.getBigDecimal("R32_AMOUNT_1"));
			obj.setR32_amount_2(rs.getBigDecimal("R32_AMOUNT_2"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_amount_1(rs.getBigDecimal("R33_AMOUNT_1"));
			obj.setR33_amount_2(rs.getBigDecimal("R33_AMOUNT_2"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_amount_1(rs.getBigDecimal("R34_AMOUNT_1"));
			obj.setR34_amount_2(rs.getBigDecimal("R34_AMOUNT_2"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_amount_1(rs.getBigDecimal("R35_AMOUNT_1"));
			obj.setR35_amount_2(rs.getBigDecimal("R35_AMOUNT_2"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_amount_1(rs.getBigDecimal("R36_AMOUNT_1"));
			obj.setR36_amount_2(rs.getBigDecimal("R36_AMOUNT_2"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_amount_1(rs.getBigDecimal("R41_AMOUNT_1"));
			obj.setR41_amount_2(rs.getBigDecimal("R41_AMOUNT_2"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_amount_1(rs.getBigDecimal("R42_AMOUNT_1"));
			obj.setR42_amount_2(rs.getBigDecimal("R42_AMOUNT_2"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_amount_1(rs.getBigDecimal("R43_AMOUNT_1"));
			obj.setR43_amount_2(rs.getBigDecimal("R43_AMOUNT_2"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_amount_1(rs.getBigDecimal("R44_AMOUNT_1"));
			obj.setR44_amount_2(rs.getBigDecimal("R44_AMOUNT_2"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_amount_1(rs.getBigDecimal("R45_AMOUNT_1"));
			obj.setR45_amount_2(rs.getBigDecimal("R45_AMOUNT_2"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_amount_1(rs.getBigDecimal("R46_AMOUNT_1"));
			obj.setR46_amount_2(rs.getBigDecimal("R46_AMOUNT_2"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_amount_1(rs.getBigDecimal("R47_AMOUNT_1"));
			obj.setR47_amount_2(rs.getBigDecimal("R47_AMOUNT_2"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_amount_1(rs.getBigDecimal("R48_AMOUNT_1"));
			obj.setR48_amount_2(rs.getBigDecimal("R48_AMOUNT_2"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_amount_1(rs.getBigDecimal("R49_AMOUNT_1"));
			obj.setR49_amount_2(rs.getBigDecimal("R49_AMOUNT_2"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getString("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_Archival_Summary_Entity
	// =========================================================
	public static class M_CA2_Archival_Summary_Entity {
		private String r10_product;
		private java.math.BigDecimal r10_amount_1;
		private java.math.BigDecimal r10_amount_2;
		private String r11_product;
		private java.math.BigDecimal r11_amount_1;
		private java.math.BigDecimal r11_amount_2;
		private String r12_product;
		private java.math.BigDecimal r12_amount_1;
		private java.math.BigDecimal r12_amount_2;
		private String r13_product;
		private java.math.BigDecimal r13_amount_1;
		private java.math.BigDecimal r13_amount_2;
		private String r14_product;
		private java.math.BigDecimal r14_amount_1;
		private java.math.BigDecimal r14_amount_2;
		private String r15_product;
		private java.math.BigDecimal r15_amount_1;
		private java.math.BigDecimal r15_amount_2;
		private String r16_product;
		private java.math.BigDecimal r16_amount_1;
		private java.math.BigDecimal r16_amount_2;
		private String r17_product;
		private java.math.BigDecimal r17_amount_1;
		private java.math.BigDecimal r17_amount_2;
		private String r18_product;
		private java.math.BigDecimal r18_amount_1;
		private java.math.BigDecimal r18_amount_2;
		private String r19_product;
		private java.math.BigDecimal r19_amount_1;
		private java.math.BigDecimal r19_amount_2;
		private String r20_product;
		private java.math.BigDecimal r20_amount_1;
		private java.math.BigDecimal r20_amount_2;
		private String r21_product;
		private java.math.BigDecimal r21_amount_1;
		private java.math.BigDecimal r21_amount_2;
		private String r22_product;
		private java.math.BigDecimal r22_amount_1;
		private java.math.BigDecimal r22_amount_2;
		private String r23_product;
		private java.math.BigDecimal r23_amount_1;
		private java.math.BigDecimal r23_amount_2;
		private String r24_product;
		private java.math.BigDecimal r24_amount_1;
		private java.math.BigDecimal r24_amount_2;
		private String r25_product;
		private java.math.BigDecimal r25_amount_1;
		private java.math.BigDecimal r25_amount_2;
		private String r26_product;
		private java.math.BigDecimal r26_amount_1;
		private java.math.BigDecimal r26_amount_2;
		private String r31_product;
		private java.math.BigDecimal r31_amount_1;
		private java.math.BigDecimal r31_amount_2;
		private String r32_product;
		private java.math.BigDecimal r32_amount_1;
		private java.math.BigDecimal r32_amount_2;
		private String r33_product;
		private java.math.BigDecimal r33_amount_1;
		private java.math.BigDecimal r33_amount_2;
		private String r34_product;
		private java.math.BigDecimal r34_amount_1;
		private java.math.BigDecimal r34_amount_2;
		private String r35_product;
		private java.math.BigDecimal r35_amount_1;
		private java.math.BigDecimal r35_amount_2;
		private String r36_product;
		private java.math.BigDecimal r36_amount_1;
		private java.math.BigDecimal r36_amount_2;
		private String r41_product;
		private java.math.BigDecimal r41_amount_1;
		private java.math.BigDecimal r41_amount_2;
		private String r42_product;
		private java.math.BigDecimal r42_amount_1;
		private java.math.BigDecimal r42_amount_2;
		private String r43_product;
		private java.math.BigDecimal r43_amount_1;
		private java.math.BigDecimal r43_amount_2;
		private String r44_product;
		private java.math.BigDecimal r44_amount_1;
		private java.math.BigDecimal r44_amount_2;
		private String r45_product;
		private java.math.BigDecimal r45_amount_1;
		private java.math.BigDecimal r45_amount_2;
		private String r46_product;
		private java.math.BigDecimal r46_amount_1;
		private java.math.BigDecimal r46_amount_2;
		private String r47_product;
		private java.math.BigDecimal r47_amount_1;
		private java.math.BigDecimal r47_amount_2;
		private String r48_product;
		private java.math.BigDecimal r48_amount_1;
		private java.math.BigDecimal r48_amount_2;
		private String r49_product;
		private java.math.BigDecimal r49_amount_1;
		private java.math.BigDecimal r49_amount_2;
		private java.util.Date report_date;
		private java.math.BigDecimal report_version;
		private java.util.Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String v) {
			r10_product = v;
		}

		public java.math.BigDecimal getR10_amount_1() {
			return r10_amount_1;
		}

		public void setR10_amount_1(java.math.BigDecimal v) {
			r10_amount_1 = v;
		}

		public java.math.BigDecimal getR10_amount_2() {
			return r10_amount_2;
		}

		public void setR10_amount_2(java.math.BigDecimal v) {
			r10_amount_2 = v;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String v) {
			r11_product = v;
		}

		public java.math.BigDecimal getR11_amount_1() {
			return r11_amount_1;
		}

		public void setR11_amount_1(java.math.BigDecimal v) {
			r11_amount_1 = v;
		}

		public java.math.BigDecimal getR11_amount_2() {
			return r11_amount_2;
		}

		public void setR11_amount_2(java.math.BigDecimal v) {
			r11_amount_2 = v;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String v) {
			r12_product = v;
		}

		public java.math.BigDecimal getR12_amount_1() {
			return r12_amount_1;
		}

		public void setR12_amount_1(java.math.BigDecimal v) {
			r12_amount_1 = v;
		}

		public java.math.BigDecimal getR12_amount_2() {
			return r12_amount_2;
		}

		public void setR12_amount_2(java.math.BigDecimal v) {
			r12_amount_2 = v;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String v) {
			r13_product = v;
		}

		public java.math.BigDecimal getR13_amount_1() {
			return r13_amount_1;
		}

		public void setR13_amount_1(java.math.BigDecimal v) {
			r13_amount_1 = v;
		}

		public java.math.BigDecimal getR13_amount_2() {
			return r13_amount_2;
		}

		public void setR13_amount_2(java.math.BigDecimal v) {
			r13_amount_2 = v;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String v) {
			r14_product = v;
		}

		public java.math.BigDecimal getR14_amount_1() {
			return r14_amount_1;
		}

		public void setR14_amount_1(java.math.BigDecimal v) {
			r14_amount_1 = v;
		}

		public java.math.BigDecimal getR14_amount_2() {
			return r14_amount_2;
		}

		public void setR14_amount_2(java.math.BigDecimal v) {
			r14_amount_2 = v;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String v) {
			r15_product = v;
		}

		public java.math.BigDecimal getR15_amount_1() {
			return r15_amount_1;
		}

		public void setR15_amount_1(java.math.BigDecimal v) {
			r15_amount_1 = v;
		}

		public java.math.BigDecimal getR15_amount_2() {
			return r15_amount_2;
		}

		public void setR15_amount_2(java.math.BigDecimal v) {
			r15_amount_2 = v;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String v) {
			r16_product = v;
		}

		public java.math.BigDecimal getR16_amount_1() {
			return r16_amount_1;
		}

		public void setR16_amount_1(java.math.BigDecimal v) {
			r16_amount_1 = v;
		}

		public java.math.BigDecimal getR16_amount_2() {
			return r16_amount_2;
		}

		public void setR16_amount_2(java.math.BigDecimal v) {
			r16_amount_2 = v;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String v) {
			r17_product = v;
		}

		public java.math.BigDecimal getR17_amount_1() {
			return r17_amount_1;
		}

		public void setR17_amount_1(java.math.BigDecimal v) {
			r17_amount_1 = v;
		}

		public java.math.BigDecimal getR17_amount_2() {
			return r17_amount_2;
		}

		public void setR17_amount_2(java.math.BigDecimal v) {
			r17_amount_2 = v;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String v) {
			r18_product = v;
		}

		public java.math.BigDecimal getR18_amount_1() {
			return r18_amount_1;
		}

		public void setR18_amount_1(java.math.BigDecimal v) {
			r18_amount_1 = v;
		}

		public java.math.BigDecimal getR18_amount_2() {
			return r18_amount_2;
		}

		public void setR18_amount_2(java.math.BigDecimal v) {
			r18_amount_2 = v;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String v) {
			r19_product = v;
		}

		public java.math.BigDecimal getR19_amount_1() {
			return r19_amount_1;
		}

		public void setR19_amount_1(java.math.BigDecimal v) {
			r19_amount_1 = v;
		}

		public java.math.BigDecimal getR19_amount_2() {
			return r19_amount_2;
		}

		public void setR19_amount_2(java.math.BigDecimal v) {
			r19_amount_2 = v;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String v) {
			r20_product = v;
		}

		public java.math.BigDecimal getR20_amount_1() {
			return r20_amount_1;
		}

		public void setR20_amount_1(java.math.BigDecimal v) {
			r20_amount_1 = v;
		}

		public java.math.BigDecimal getR20_amount_2() {
			return r20_amount_2;
		}

		public void setR20_amount_2(java.math.BigDecimal v) {
			r20_amount_2 = v;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String v) {
			r21_product = v;
		}

		public java.math.BigDecimal getR21_amount_1() {
			return r21_amount_1;
		}

		public void setR21_amount_1(java.math.BigDecimal v) {
			r21_amount_1 = v;
		}

		public java.math.BigDecimal getR21_amount_2() {
			return r21_amount_2;
		}

		public void setR21_amount_2(java.math.BigDecimal v) {
			r21_amount_2 = v;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String v) {
			r22_product = v;
		}

		public java.math.BigDecimal getR22_amount_1() {
			return r22_amount_1;
		}

		public void setR22_amount_1(java.math.BigDecimal v) {
			r22_amount_1 = v;
		}

		public java.math.BigDecimal getR22_amount_2() {
			return r22_amount_2;
		}

		public void setR22_amount_2(java.math.BigDecimal v) {
			r22_amount_2 = v;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String v) {
			r23_product = v;
		}

		public java.math.BigDecimal getR23_amount_1() {
			return r23_amount_1;
		}

		public void setR23_amount_1(java.math.BigDecimal v) {
			r23_amount_1 = v;
		}

		public java.math.BigDecimal getR23_amount_2() {
			return r23_amount_2;
		}

		public void setR23_amount_2(java.math.BigDecimal v) {
			r23_amount_2 = v;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String v) {
			r24_product = v;
		}

		public java.math.BigDecimal getR24_amount_1() {
			return r24_amount_1;
		}

		public void setR24_amount_1(java.math.BigDecimal v) {
			r24_amount_1 = v;
		}

		public java.math.BigDecimal getR24_amount_2() {
			return r24_amount_2;
		}

		public void setR24_amount_2(java.math.BigDecimal v) {
			r24_amount_2 = v;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String v) {
			r25_product = v;
		}

		public java.math.BigDecimal getR25_amount_1() {
			return r25_amount_1;
		}

		public void setR25_amount_1(java.math.BigDecimal v) {
			r25_amount_1 = v;
		}

		public java.math.BigDecimal getR25_amount_2() {
			return r25_amount_2;
		}

		public void setR25_amount_2(java.math.BigDecimal v) {
			r25_amount_2 = v;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String v) {
			r26_product = v;
		}

		public java.math.BigDecimal getR26_amount_1() {
			return r26_amount_1;
		}

		public void setR26_amount_1(java.math.BigDecimal v) {
			r26_amount_1 = v;
		}

		public java.math.BigDecimal getR26_amount_2() {
			return r26_amount_2;
		}

		public void setR26_amount_2(java.math.BigDecimal v) {
			r26_amount_2 = v;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String v) {
			r31_product = v;
		}

		public java.math.BigDecimal getR31_amount_1() {
			return r31_amount_1;
		}

		public void setR31_amount_1(java.math.BigDecimal v) {
			r31_amount_1 = v;
		}

		public java.math.BigDecimal getR31_amount_2() {
			return r31_amount_2;
		}

		public void setR31_amount_2(java.math.BigDecimal v) {
			r31_amount_2 = v;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String v) {
			r32_product = v;
		}

		public java.math.BigDecimal getR32_amount_1() {
			return r32_amount_1;
		}

		public void setR32_amount_1(java.math.BigDecimal v) {
			r32_amount_1 = v;
		}

		public java.math.BigDecimal getR32_amount_2() {
			return r32_amount_2;
		}

		public void setR32_amount_2(java.math.BigDecimal v) {
			r32_amount_2 = v;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String v) {
			r33_product = v;
		}

		public java.math.BigDecimal getR33_amount_1() {
			return r33_amount_1;
		}

		public void setR33_amount_1(java.math.BigDecimal v) {
			r33_amount_1 = v;
		}

		public java.math.BigDecimal getR33_amount_2() {
			return r33_amount_2;
		}

		public void setR33_amount_2(java.math.BigDecimal v) {
			r33_amount_2 = v;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String v) {
			r34_product = v;
		}

		public java.math.BigDecimal getR34_amount_1() {
			return r34_amount_1;
		}

		public void setR34_amount_1(java.math.BigDecimal v) {
			r34_amount_1 = v;
		}

		public java.math.BigDecimal getR34_amount_2() {
			return r34_amount_2;
		}

		public void setR34_amount_2(java.math.BigDecimal v) {
			r34_amount_2 = v;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String v) {
			r35_product = v;
		}

		public java.math.BigDecimal getR35_amount_1() {
			return r35_amount_1;
		}

		public void setR35_amount_1(java.math.BigDecimal v) {
			r35_amount_1 = v;
		}

		public java.math.BigDecimal getR35_amount_2() {
			return r35_amount_2;
		}

		public void setR35_amount_2(java.math.BigDecimal v) {
			r35_amount_2 = v;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String v) {
			r36_product = v;
		}

		public java.math.BigDecimal getR36_amount_1() {
			return r36_amount_1;
		}

		public void setR36_amount_1(java.math.BigDecimal v) {
			r36_amount_1 = v;
		}

		public java.math.BigDecimal getR36_amount_2() {
			return r36_amount_2;
		}

		public void setR36_amount_2(java.math.BigDecimal v) {
			r36_amount_2 = v;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String v) {
			r41_product = v;
		}

		public java.math.BigDecimal getR41_amount_1() {
			return r41_amount_1;
		}

		public void setR41_amount_1(java.math.BigDecimal v) {
			r41_amount_1 = v;
		}

		public java.math.BigDecimal getR41_amount_2() {
			return r41_amount_2;
		}

		public void setR41_amount_2(java.math.BigDecimal v) {
			r41_amount_2 = v;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String v) {
			r42_product = v;
		}

		public java.math.BigDecimal getR42_amount_1() {
			return r42_amount_1;
		}

		public void setR42_amount_1(java.math.BigDecimal v) {
			r42_amount_1 = v;
		}

		public java.math.BigDecimal getR42_amount_2() {
			return r42_amount_2;
		}

		public void setR42_amount_2(java.math.BigDecimal v) {
			r42_amount_2 = v;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String v) {
			r43_product = v;
		}

		public java.math.BigDecimal getR43_amount_1() {
			return r43_amount_1;
		}

		public void setR43_amount_1(java.math.BigDecimal v) {
			r43_amount_1 = v;
		}

		public java.math.BigDecimal getR43_amount_2() {
			return r43_amount_2;
		}

		public void setR43_amount_2(java.math.BigDecimal v) {
			r43_amount_2 = v;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String v) {
			r44_product = v;
		}

		public java.math.BigDecimal getR44_amount_1() {
			return r44_amount_1;
		}

		public void setR44_amount_1(java.math.BigDecimal v) {
			r44_amount_1 = v;
		}

		public java.math.BigDecimal getR44_amount_2() {
			return r44_amount_2;
		}

		public void setR44_amount_2(java.math.BigDecimal v) {
			r44_amount_2 = v;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String v) {
			r45_product = v;
		}

		public java.math.BigDecimal getR45_amount_1() {
			return r45_amount_1;
		}

		public void setR45_amount_1(java.math.BigDecimal v) {
			r45_amount_1 = v;
		}

		public java.math.BigDecimal getR45_amount_2() {
			return r45_amount_2;
		}

		public void setR45_amount_2(java.math.BigDecimal v) {
			r45_amount_2 = v;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String v) {
			r46_product = v;
		}

		public java.math.BigDecimal getR46_amount_1() {
			return r46_amount_1;
		}

		public void setR46_amount_1(java.math.BigDecimal v) {
			r46_amount_1 = v;
		}

		public java.math.BigDecimal getR46_amount_2() {
			return r46_amount_2;
		}

		public void setR46_amount_2(java.math.BigDecimal v) {
			r46_amount_2 = v;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String v) {
			r47_product = v;
		}

		public java.math.BigDecimal getR47_amount_1() {
			return r47_amount_1;
		}

		public void setR47_amount_1(java.math.BigDecimal v) {
			r47_amount_1 = v;
		}

		public java.math.BigDecimal getR47_amount_2() {
			return r47_amount_2;
		}

		public void setR47_amount_2(java.math.BigDecimal v) {
			r47_amount_2 = v;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String v) {
			r48_product = v;
		}

		public java.math.BigDecimal getR48_amount_1() {
			return r48_amount_1;
		}

		public void setR48_amount_1(java.math.BigDecimal v) {
			r48_amount_1 = v;
		}

		public java.math.BigDecimal getR48_amount_2() {
			return r48_amount_2;
		}

		public void setR48_amount_2(java.math.BigDecimal v) {
			r48_amount_2 = v;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String v) {
			r49_product = v;
		}

		public java.math.BigDecimal getR49_amount_1() {
			return r49_amount_1;
		}

		public void setR49_amount_1(java.math.BigDecimal v) {
			r49_amount_1 = v;
		}

		public java.math.BigDecimal getR49_amount_2() {
			return r49_amount_2;
		}

		public void setR49_amount_2(java.math.BigDecimal v) {
			r49_amount_2 = v;
		}

		public java.util.Date getReport_date() {
			return report_date;
		}

		public void setReport_date(java.util.Date v) {
			report_date = v;
		}

		public java.math.BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(java.math.BigDecimal v) {
			report_version = v;
		}

		public java.util.Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(java.util.Date v) {
			reportResubDate = v;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String v) {
			report_frequency = v;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String v) {
			report_code = v;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String v) {
			report_desc = v;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String v) {
			entity_flg = v;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String v) {
			modify_flg = v;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String v) {
			del_flg = v;
		}
	}

	class M_CA2ArchivalSummaryRowMapper implements RowMapper<M_CA2_Archival_Summary_Entity> {
		@Override
		public M_CA2_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_Archival_Summary_Entity obj = new M_CA2_Archival_Summary_Entity();
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_amount_1(rs.getBigDecimal("R10_AMOUNT_1"));
			obj.setR10_amount_2(rs.getBigDecimal("R10_AMOUNT_2"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount_1(rs.getBigDecimal("R11_AMOUNT_1"));
			obj.setR11_amount_2(rs.getBigDecimal("R11_AMOUNT_2"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount_1(rs.getBigDecimal("R12_AMOUNT_1"));
			obj.setR12_amount_2(rs.getBigDecimal("R12_AMOUNT_2"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount_1(rs.getBigDecimal("R13_AMOUNT_1"));
			obj.setR13_amount_2(rs.getBigDecimal("R13_AMOUNT_2"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount_1(rs.getBigDecimal("R14_AMOUNT_1"));
			obj.setR14_amount_2(rs.getBigDecimal("R14_AMOUNT_2"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount_1(rs.getBigDecimal("R15_AMOUNT_1"));
			obj.setR15_amount_2(rs.getBigDecimal("R15_AMOUNT_2"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_amount_1(rs.getBigDecimal("R16_AMOUNT_1"));
			obj.setR16_amount_2(rs.getBigDecimal("R16_AMOUNT_2"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_amount_1(rs.getBigDecimal("R17_AMOUNT_1"));
			obj.setR17_amount_2(rs.getBigDecimal("R17_AMOUNT_2"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_amount_1(rs.getBigDecimal("R18_AMOUNT_1"));
			obj.setR18_amount_2(rs.getBigDecimal("R18_AMOUNT_2"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_amount_1(rs.getBigDecimal("R19_AMOUNT_1"));
			obj.setR19_amount_2(rs.getBigDecimal("R19_AMOUNT_2"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_amount_1(rs.getBigDecimal("R20_AMOUNT_1"));
			obj.setR20_amount_2(rs.getBigDecimal("R20_AMOUNT_2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_amount_1(rs.getBigDecimal("R21_AMOUNT_1"));
			obj.setR21_amount_2(rs.getBigDecimal("R21_AMOUNT_2"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_amount_1(rs.getBigDecimal("R22_AMOUNT_1"));
			obj.setR22_amount_2(rs.getBigDecimal("R22_AMOUNT_2"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_amount_1(rs.getBigDecimal("R23_AMOUNT_1"));
			obj.setR23_amount_2(rs.getBigDecimal("R23_AMOUNT_2"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_amount_1(rs.getBigDecimal("R24_AMOUNT_1"));
			obj.setR24_amount_2(rs.getBigDecimal("R24_AMOUNT_2"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_amount_1(rs.getBigDecimal("R25_AMOUNT_1"));
			obj.setR25_amount_2(rs.getBigDecimal("R25_AMOUNT_2"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_amount_1(rs.getBigDecimal("R26_AMOUNT_1"));
			obj.setR26_amount_2(rs.getBigDecimal("R26_AMOUNT_2"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_amount_1(rs.getBigDecimal("R31_AMOUNT_1"));
			obj.setR31_amount_2(rs.getBigDecimal("R31_AMOUNT_2"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_amount_1(rs.getBigDecimal("R32_AMOUNT_1"));
			obj.setR32_amount_2(rs.getBigDecimal("R32_AMOUNT_2"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_amount_1(rs.getBigDecimal("R33_AMOUNT_1"));
			obj.setR33_amount_2(rs.getBigDecimal("R33_AMOUNT_2"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_amount_1(rs.getBigDecimal("R34_AMOUNT_1"));
			obj.setR34_amount_2(rs.getBigDecimal("R34_AMOUNT_2"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_amount_1(rs.getBigDecimal("R35_AMOUNT_1"));
			obj.setR35_amount_2(rs.getBigDecimal("R35_AMOUNT_2"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_amount_1(rs.getBigDecimal("R36_AMOUNT_1"));
			obj.setR36_amount_2(rs.getBigDecimal("R36_AMOUNT_2"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_amount_1(rs.getBigDecimal("R41_AMOUNT_1"));
			obj.setR41_amount_2(rs.getBigDecimal("R41_AMOUNT_2"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_amount_1(rs.getBigDecimal("R42_AMOUNT_1"));
			obj.setR42_amount_2(rs.getBigDecimal("R42_AMOUNT_2"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_amount_1(rs.getBigDecimal("R43_AMOUNT_1"));
			obj.setR43_amount_2(rs.getBigDecimal("R43_AMOUNT_2"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_amount_1(rs.getBigDecimal("R44_AMOUNT_1"));
			obj.setR44_amount_2(rs.getBigDecimal("R44_AMOUNT_2"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_amount_1(rs.getBigDecimal("R45_AMOUNT_1"));
			obj.setR45_amount_2(rs.getBigDecimal("R45_AMOUNT_2"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_amount_1(rs.getBigDecimal("R46_AMOUNT_1"));
			obj.setR46_amount_2(rs.getBigDecimal("R46_AMOUNT_2"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_amount_1(rs.getBigDecimal("R47_AMOUNT_1"));
			obj.setR47_amount_2(rs.getBigDecimal("R47_AMOUNT_2"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_amount_1(rs.getBigDecimal("R48_AMOUNT_1"));
			obj.setR48_amount_2(rs.getBigDecimal("R48_AMOUNT_2"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_amount_1(rs.getBigDecimal("R49_AMOUNT_1"));
			obj.setR49_amount_2(rs.getBigDecimal("R49_AMOUNT_2"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_RESUB_Summary_Entity
	// =========================================================
	public static class M_CA2_RESUB_Summary_Entity {
		private String r10_product;
		private java.math.BigDecimal r10_amount_1;
		private java.math.BigDecimal r10_amount_2;
		private String r11_product;
		private java.math.BigDecimal r11_amount_1;
		private java.math.BigDecimal r11_amount_2;
		private String r12_product;
		private java.math.BigDecimal r12_amount_1;
		private java.math.BigDecimal r12_amount_2;
		private String r13_product;
		private java.math.BigDecimal r13_amount_1;
		private java.math.BigDecimal r13_amount_2;
		private String r14_product;
		private java.math.BigDecimal r14_amount_1;
		private java.math.BigDecimal r14_amount_2;
		private String r15_product;
		private java.math.BigDecimal r15_amount_1;
		private java.math.BigDecimal r15_amount_2;
		private String r16_product;
		private java.math.BigDecimal r16_amount_1;
		private java.math.BigDecimal r16_amount_2;
		private String r17_product;
		private java.math.BigDecimal r17_amount_1;
		private java.math.BigDecimal r17_amount_2;
		private String r18_product;
		private java.math.BigDecimal r18_amount_1;
		private java.math.BigDecimal r18_amount_2;
		private String r19_product;
		private java.math.BigDecimal r19_amount_1;
		private java.math.BigDecimal r19_amount_2;
		private String r20_product;
		private java.math.BigDecimal r20_amount_1;
		private java.math.BigDecimal r20_amount_2;
		private String r21_product;
		private java.math.BigDecimal r21_amount_1;
		private java.math.BigDecimal r21_amount_2;
		private String r22_product;
		private java.math.BigDecimal r22_amount_1;
		private java.math.BigDecimal r22_amount_2;
		private String r23_product;
		private java.math.BigDecimal r23_amount_1;
		private java.math.BigDecimal r23_amount_2;
		private String r24_product;
		private java.math.BigDecimal r24_amount_1;
		private java.math.BigDecimal r24_amount_2;
		private String r25_product;
		private java.math.BigDecimal r25_amount_1;
		private java.math.BigDecimal r25_amount_2;
		private String r26_product;
		private java.math.BigDecimal r26_amount_1;
		private java.math.BigDecimal r26_amount_2;
		private String r31_product;
		private java.math.BigDecimal r31_amount_1;
		private java.math.BigDecimal r31_amount_2;
		private String r32_product;
		private java.math.BigDecimal r32_amount_1;
		private java.math.BigDecimal r32_amount_2;
		private String r33_product;
		private java.math.BigDecimal r33_amount_1;
		private java.math.BigDecimal r33_amount_2;
		private String r34_product;
		private java.math.BigDecimal r34_amount_1;
		private java.math.BigDecimal r34_amount_2;
		private String r35_product;
		private java.math.BigDecimal r35_amount_1;
		private java.math.BigDecimal r35_amount_2;
		private String r36_product;
		private java.math.BigDecimal r36_amount_1;
		private java.math.BigDecimal r36_amount_2;
		private String r41_product;
		private java.math.BigDecimal r41_amount_1;
		private java.math.BigDecimal r41_amount_2;
		private String r42_product;
		private java.math.BigDecimal r42_amount_1;
		private java.math.BigDecimal r42_amount_2;
		private String r43_product;
		private java.math.BigDecimal r43_amount_1;
		private java.math.BigDecimal r43_amount_2;
		private String r44_product;
		private java.math.BigDecimal r44_amount_1;
		private java.math.BigDecimal r44_amount_2;
		private String r45_product;
		private java.math.BigDecimal r45_amount_1;
		private java.math.BigDecimal r45_amount_2;
		private String r46_product;
		private java.math.BigDecimal r46_amount_1;
		private java.math.BigDecimal r46_amount_2;
		private String r47_product;
		private java.math.BigDecimal r47_amount_1;
		private java.math.BigDecimal r47_amount_2;
		private String r48_product;
		private java.math.BigDecimal r48_amount_1;
		private java.math.BigDecimal r48_amount_2;
		private String r49_product;
		private java.math.BigDecimal r49_amount_1;
		private java.math.BigDecimal r49_amount_2;
		private java.util.Date report_date;
		private java.math.BigDecimal report_version;
		private java.util.Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String v) {
			r10_product = v;
		}

		public java.math.BigDecimal getR10_amount_1() {
			return r10_amount_1;
		}

		public void setR10_amount_1(java.math.BigDecimal v) {
			r10_amount_1 = v;
		}

		public java.math.BigDecimal getR10_amount_2() {
			return r10_amount_2;
		}

		public void setR10_amount_2(java.math.BigDecimal v) {
			r10_amount_2 = v;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String v) {
			r11_product = v;
		}

		public java.math.BigDecimal getR11_amount_1() {
			return r11_amount_1;
		}

		public void setR11_amount_1(java.math.BigDecimal v) {
			r11_amount_1 = v;
		}

		public java.math.BigDecimal getR11_amount_2() {
			return r11_amount_2;
		}

		public void setR11_amount_2(java.math.BigDecimal v) {
			r11_amount_2 = v;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String v) {
			r12_product = v;
		}

		public java.math.BigDecimal getR12_amount_1() {
			return r12_amount_1;
		}

		public void setR12_amount_1(java.math.BigDecimal v) {
			r12_amount_1 = v;
		}

		public java.math.BigDecimal getR12_amount_2() {
			return r12_amount_2;
		}

		public void setR12_amount_2(java.math.BigDecimal v) {
			r12_amount_2 = v;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String v) {
			r13_product = v;
		}

		public java.math.BigDecimal getR13_amount_1() {
			return r13_amount_1;
		}

		public void setR13_amount_1(java.math.BigDecimal v) {
			r13_amount_1 = v;
		}

		public java.math.BigDecimal getR13_amount_2() {
			return r13_amount_2;
		}

		public void setR13_amount_2(java.math.BigDecimal v) {
			r13_amount_2 = v;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String v) {
			r14_product = v;
		}

		public java.math.BigDecimal getR14_amount_1() {
			return r14_amount_1;
		}

		public void setR14_amount_1(java.math.BigDecimal v) {
			r14_amount_1 = v;
		}

		public java.math.BigDecimal getR14_amount_2() {
			return r14_amount_2;
		}

		public void setR14_amount_2(java.math.BigDecimal v) {
			r14_amount_2 = v;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String v) {
			r15_product = v;
		}

		public java.math.BigDecimal getR15_amount_1() {
			return r15_amount_1;
		}

		public void setR15_amount_1(java.math.BigDecimal v) {
			r15_amount_1 = v;
		}

		public java.math.BigDecimal getR15_amount_2() {
			return r15_amount_2;
		}

		public void setR15_amount_2(java.math.BigDecimal v) {
			r15_amount_2 = v;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String v) {
			r16_product = v;
		}

		public java.math.BigDecimal getR16_amount_1() {
			return r16_amount_1;
		}

		public void setR16_amount_1(java.math.BigDecimal v) {
			r16_amount_1 = v;
		}

		public java.math.BigDecimal getR16_amount_2() {
			return r16_amount_2;
		}

		public void setR16_amount_2(java.math.BigDecimal v) {
			r16_amount_2 = v;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String v) {
			r17_product = v;
		}

		public java.math.BigDecimal getR17_amount_1() {
			return r17_amount_1;
		}

		public void setR17_amount_1(java.math.BigDecimal v) {
			r17_amount_1 = v;
		}

		public java.math.BigDecimal getR17_amount_2() {
			return r17_amount_2;
		}

		public void setR17_amount_2(java.math.BigDecimal v) {
			r17_amount_2 = v;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String v) {
			r18_product = v;
		}

		public java.math.BigDecimal getR18_amount_1() {
			return r18_amount_1;
		}

		public void setR18_amount_1(java.math.BigDecimal v) {
			r18_amount_1 = v;
		}

		public java.math.BigDecimal getR18_amount_2() {
			return r18_amount_2;
		}

		public void setR18_amount_2(java.math.BigDecimal v) {
			r18_amount_2 = v;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String v) {
			r19_product = v;
		}

		public java.math.BigDecimal getR19_amount_1() {
			return r19_amount_1;
		}

		public void setR19_amount_1(java.math.BigDecimal v) {
			r19_amount_1 = v;
		}

		public java.math.BigDecimal getR19_amount_2() {
			return r19_amount_2;
		}

		public void setR19_amount_2(java.math.BigDecimal v) {
			r19_amount_2 = v;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String v) {
			r20_product = v;
		}

		public java.math.BigDecimal getR20_amount_1() {
			return r20_amount_1;
		}

		public void setR20_amount_1(java.math.BigDecimal v) {
			r20_amount_1 = v;
		}

		public java.math.BigDecimal getR20_amount_2() {
			return r20_amount_2;
		}

		public void setR20_amount_2(java.math.BigDecimal v) {
			r20_amount_2 = v;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String v) {
			r21_product = v;
		}

		public java.math.BigDecimal getR21_amount_1() {
			return r21_amount_1;
		}

		public void setR21_amount_1(java.math.BigDecimal v) {
			r21_amount_1 = v;
		}

		public java.math.BigDecimal getR21_amount_2() {
			return r21_amount_2;
		}

		public void setR21_amount_2(java.math.BigDecimal v) {
			r21_amount_2 = v;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String v) {
			r22_product = v;
		}

		public java.math.BigDecimal getR22_amount_1() {
			return r22_amount_1;
		}

		public void setR22_amount_1(java.math.BigDecimal v) {
			r22_amount_1 = v;
		}

		public java.math.BigDecimal getR22_amount_2() {
			return r22_amount_2;
		}

		public void setR22_amount_2(java.math.BigDecimal v) {
			r22_amount_2 = v;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String v) {
			r23_product = v;
		}

		public java.math.BigDecimal getR23_amount_1() {
			return r23_amount_1;
		}

		public void setR23_amount_1(java.math.BigDecimal v) {
			r23_amount_1 = v;
		}

		public java.math.BigDecimal getR23_amount_2() {
			return r23_amount_2;
		}

		public void setR23_amount_2(java.math.BigDecimal v) {
			r23_amount_2 = v;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String v) {
			r24_product = v;
		}

		public java.math.BigDecimal getR24_amount_1() {
			return r24_amount_1;
		}

		public void setR24_amount_1(java.math.BigDecimal v) {
			r24_amount_1 = v;
		}

		public java.math.BigDecimal getR24_amount_2() {
			return r24_amount_2;
		}

		public void setR24_amount_2(java.math.BigDecimal v) {
			r24_amount_2 = v;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String v) {
			r25_product = v;
		}

		public java.math.BigDecimal getR25_amount_1() {
			return r25_amount_1;
		}

		public void setR25_amount_1(java.math.BigDecimal v) {
			r25_amount_1 = v;
		}

		public java.math.BigDecimal getR25_amount_2() {
			return r25_amount_2;
		}

		public void setR25_amount_2(java.math.BigDecimal v) {
			r25_amount_2 = v;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String v) {
			r26_product = v;
		}

		public java.math.BigDecimal getR26_amount_1() {
			return r26_amount_1;
		}

		public void setR26_amount_1(java.math.BigDecimal v) {
			r26_amount_1 = v;
		}

		public java.math.BigDecimal getR26_amount_2() {
			return r26_amount_2;
		}

		public void setR26_amount_2(java.math.BigDecimal v) {
			r26_amount_2 = v;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String v) {
			r31_product = v;
		}

		public java.math.BigDecimal getR31_amount_1() {
			return r31_amount_1;
		}

		public void setR31_amount_1(java.math.BigDecimal v) {
			r31_amount_1 = v;
		}

		public java.math.BigDecimal getR31_amount_2() {
			return r31_amount_2;
		}

		public void setR31_amount_2(java.math.BigDecimal v) {
			r31_amount_2 = v;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String v) {
			r32_product = v;
		}

		public java.math.BigDecimal getR32_amount_1() {
			return r32_amount_1;
		}

		public void setR32_amount_1(java.math.BigDecimal v) {
			r32_amount_1 = v;
		}

		public java.math.BigDecimal getR32_amount_2() {
			return r32_amount_2;
		}

		public void setR32_amount_2(java.math.BigDecimal v) {
			r32_amount_2 = v;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String v) {
			r33_product = v;
		}

		public java.math.BigDecimal getR33_amount_1() {
			return r33_amount_1;
		}

		public void setR33_amount_1(java.math.BigDecimal v) {
			r33_amount_1 = v;
		}

		public java.math.BigDecimal getR33_amount_2() {
			return r33_amount_2;
		}

		public void setR33_amount_2(java.math.BigDecimal v) {
			r33_amount_2 = v;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String v) {
			r34_product = v;
		}

		public java.math.BigDecimal getR34_amount_1() {
			return r34_amount_1;
		}

		public void setR34_amount_1(java.math.BigDecimal v) {
			r34_amount_1 = v;
		}

		public java.math.BigDecimal getR34_amount_2() {
			return r34_amount_2;
		}

		public void setR34_amount_2(java.math.BigDecimal v) {
			r34_amount_2 = v;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String v) {
			r35_product = v;
		}

		public java.math.BigDecimal getR35_amount_1() {
			return r35_amount_1;
		}

		public void setR35_amount_1(java.math.BigDecimal v) {
			r35_amount_1 = v;
		}

		public java.math.BigDecimal getR35_amount_2() {
			return r35_amount_2;
		}

		public void setR35_amount_2(java.math.BigDecimal v) {
			r35_amount_2 = v;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String v) {
			r36_product = v;
		}

		public java.math.BigDecimal getR36_amount_1() {
			return r36_amount_1;
		}

		public void setR36_amount_1(java.math.BigDecimal v) {
			r36_amount_1 = v;
		}

		public java.math.BigDecimal getR36_amount_2() {
			return r36_amount_2;
		}

		public void setR36_amount_2(java.math.BigDecimal v) {
			r36_amount_2 = v;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String v) {
			r41_product = v;
		}

		public java.math.BigDecimal getR41_amount_1() {
			return r41_amount_1;
		}

		public void setR41_amount_1(java.math.BigDecimal v) {
			r41_amount_1 = v;
		}

		public java.math.BigDecimal getR41_amount_2() {
			return r41_amount_2;
		}

		public void setR41_amount_2(java.math.BigDecimal v) {
			r41_amount_2 = v;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String v) {
			r42_product = v;
		}

		public java.math.BigDecimal getR42_amount_1() {
			return r42_amount_1;
		}

		public void setR42_amount_1(java.math.BigDecimal v) {
			r42_amount_1 = v;
		}

		public java.math.BigDecimal getR42_amount_2() {
			return r42_amount_2;
		}

		public void setR42_amount_2(java.math.BigDecimal v) {
			r42_amount_2 = v;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String v) {
			r43_product = v;
		}

		public java.math.BigDecimal getR43_amount_1() {
			return r43_amount_1;
		}

		public void setR43_amount_1(java.math.BigDecimal v) {
			r43_amount_1 = v;
		}

		public java.math.BigDecimal getR43_amount_2() {
			return r43_amount_2;
		}

		public void setR43_amount_2(java.math.BigDecimal v) {
			r43_amount_2 = v;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String v) {
			r44_product = v;
		}

		public java.math.BigDecimal getR44_amount_1() {
			return r44_amount_1;
		}

		public void setR44_amount_1(java.math.BigDecimal v) {
			r44_amount_1 = v;
		}

		public java.math.BigDecimal getR44_amount_2() {
			return r44_amount_2;
		}

		public void setR44_amount_2(java.math.BigDecimal v) {
			r44_amount_2 = v;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String v) {
			r45_product = v;
		}

		public java.math.BigDecimal getR45_amount_1() {
			return r45_amount_1;
		}

		public void setR45_amount_1(java.math.BigDecimal v) {
			r45_amount_1 = v;
		}

		public java.math.BigDecimal getR45_amount_2() {
			return r45_amount_2;
		}

		public void setR45_amount_2(java.math.BigDecimal v) {
			r45_amount_2 = v;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String v) {
			r46_product = v;
		}

		public java.math.BigDecimal getR46_amount_1() {
			return r46_amount_1;
		}

		public void setR46_amount_1(java.math.BigDecimal v) {
			r46_amount_1 = v;
		}

		public java.math.BigDecimal getR46_amount_2() {
			return r46_amount_2;
		}

		public void setR46_amount_2(java.math.BigDecimal v) {
			r46_amount_2 = v;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String v) {
			r47_product = v;
		}

		public java.math.BigDecimal getR47_amount_1() {
			return r47_amount_1;
		}

		public void setR47_amount_1(java.math.BigDecimal v) {
			r47_amount_1 = v;
		}

		public java.math.BigDecimal getR47_amount_2() {
			return r47_amount_2;
		}

		public void setR47_amount_2(java.math.BigDecimal v) {
			r47_amount_2 = v;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String v) {
			r48_product = v;
		}

		public java.math.BigDecimal getR48_amount_1() {
			return r48_amount_1;
		}

		public void setR48_amount_1(java.math.BigDecimal v) {
			r48_amount_1 = v;
		}

		public java.math.BigDecimal getR48_amount_2() {
			return r48_amount_2;
		}

		public void setR48_amount_2(java.math.BigDecimal v) {
			r48_amount_2 = v;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String v) {
			r49_product = v;
		}

		public java.math.BigDecimal getR49_amount_1() {
			return r49_amount_1;
		}

		public void setR49_amount_1(java.math.BigDecimal v) {
			r49_amount_1 = v;
		}

		public java.math.BigDecimal getR49_amount_2() {
			return r49_amount_2;
		}

		public void setR49_amount_2(java.math.BigDecimal v) {
			r49_amount_2 = v;
		}

		public java.util.Date getReport_date() {
			return report_date;
		}

		public void setReport_date(java.util.Date v) {
			report_date = v;
		}

		public java.math.BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(java.math.BigDecimal v) {
			report_version = v;
		}

		public java.util.Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(java.util.Date v) {
			reportResubDate = v;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String v) {
			report_frequency = v;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String v) {
			report_code = v;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String v) {
			report_desc = v;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String v) {
			entity_flg = v;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String v) {
			modify_flg = v;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String v) {
			del_flg = v;
		}
	}

	class M_CA2ResubSummaryRowMapper implements RowMapper<M_CA2_RESUB_Summary_Entity> {
		@Override
		public M_CA2_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_RESUB_Summary_Entity obj = new M_CA2_RESUB_Summary_Entity();
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_amount_1(rs.getBigDecimal("R10_AMOUNT_1"));
			obj.setR10_amount_2(rs.getBigDecimal("R10_AMOUNT_2"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_amount_1(rs.getBigDecimal("R11_AMOUNT_1"));
			obj.setR11_amount_2(rs.getBigDecimal("R11_AMOUNT_2"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_amount_1(rs.getBigDecimal("R12_AMOUNT_1"));
			obj.setR12_amount_2(rs.getBigDecimal("R12_AMOUNT_2"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_amount_1(rs.getBigDecimal("R13_AMOUNT_1"));
			obj.setR13_amount_2(rs.getBigDecimal("R13_AMOUNT_2"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_amount_1(rs.getBigDecimal("R14_AMOUNT_1"));
			obj.setR14_amount_2(rs.getBigDecimal("R14_AMOUNT_2"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_amount_1(rs.getBigDecimal("R15_AMOUNT_1"));
			obj.setR15_amount_2(rs.getBigDecimal("R15_AMOUNT_2"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_amount_1(rs.getBigDecimal("R16_AMOUNT_1"));
			obj.setR16_amount_2(rs.getBigDecimal("R16_AMOUNT_2"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_amount_1(rs.getBigDecimal("R17_AMOUNT_1"));
			obj.setR17_amount_2(rs.getBigDecimal("R17_AMOUNT_2"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_amount_1(rs.getBigDecimal("R18_AMOUNT_1"));
			obj.setR18_amount_2(rs.getBigDecimal("R18_AMOUNT_2"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_amount_1(rs.getBigDecimal("R19_AMOUNT_1"));
			obj.setR19_amount_2(rs.getBigDecimal("R19_AMOUNT_2"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_amount_1(rs.getBigDecimal("R20_AMOUNT_1"));
			obj.setR20_amount_2(rs.getBigDecimal("R20_AMOUNT_2"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_amount_1(rs.getBigDecimal("R21_AMOUNT_1"));
			obj.setR21_amount_2(rs.getBigDecimal("R21_AMOUNT_2"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_amount_1(rs.getBigDecimal("R22_AMOUNT_1"));
			obj.setR22_amount_2(rs.getBigDecimal("R22_AMOUNT_2"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_amount_1(rs.getBigDecimal("R23_AMOUNT_1"));
			obj.setR23_amount_2(rs.getBigDecimal("R23_AMOUNT_2"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_amount_1(rs.getBigDecimal("R24_AMOUNT_1"));
			obj.setR24_amount_2(rs.getBigDecimal("R24_AMOUNT_2"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_amount_1(rs.getBigDecimal("R25_AMOUNT_1"));
			obj.setR25_amount_2(rs.getBigDecimal("R25_AMOUNT_2"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_amount_1(rs.getBigDecimal("R26_AMOUNT_1"));
			obj.setR26_amount_2(rs.getBigDecimal("R26_AMOUNT_2"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_amount_1(rs.getBigDecimal("R31_AMOUNT_1"));
			obj.setR31_amount_2(rs.getBigDecimal("R31_AMOUNT_2"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_amount_1(rs.getBigDecimal("R32_AMOUNT_1"));
			obj.setR32_amount_2(rs.getBigDecimal("R32_AMOUNT_2"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_amount_1(rs.getBigDecimal("R33_AMOUNT_1"));
			obj.setR33_amount_2(rs.getBigDecimal("R33_AMOUNT_2"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_amount_1(rs.getBigDecimal("R34_AMOUNT_1"));
			obj.setR34_amount_2(rs.getBigDecimal("R34_AMOUNT_2"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_amount_1(rs.getBigDecimal("R35_AMOUNT_1"));
			obj.setR35_amount_2(rs.getBigDecimal("R35_AMOUNT_2"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_amount_1(rs.getBigDecimal("R36_AMOUNT_1"));
			obj.setR36_amount_2(rs.getBigDecimal("R36_AMOUNT_2"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_amount_1(rs.getBigDecimal("R41_AMOUNT_1"));
			obj.setR41_amount_2(rs.getBigDecimal("R41_AMOUNT_2"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_amount_1(rs.getBigDecimal("R42_AMOUNT_1"));
			obj.setR42_amount_2(rs.getBigDecimal("R42_AMOUNT_2"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_amount_1(rs.getBigDecimal("R43_AMOUNT_1"));
			obj.setR43_amount_2(rs.getBigDecimal("R43_AMOUNT_2"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_amount_1(rs.getBigDecimal("R44_AMOUNT_1"));
			obj.setR44_amount_2(rs.getBigDecimal("R44_AMOUNT_2"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_amount_1(rs.getBigDecimal("R45_AMOUNT_1"));
			obj.setR45_amount_2(rs.getBigDecimal("R45_AMOUNT_2"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_amount_1(rs.getBigDecimal("R46_AMOUNT_1"));
			obj.setR46_amount_2(rs.getBigDecimal("R46_AMOUNT_2"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_amount_1(rs.getBigDecimal("R47_AMOUNT_1"));
			obj.setR47_amount_2(rs.getBigDecimal("R47_AMOUNT_2"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_amount_1(rs.getBigDecimal("R48_AMOUNT_1"));
			obj.setR48_amount_2(rs.getBigDecimal("R48_AMOUNT_2"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_amount_1(rs.getBigDecimal("R49_AMOUNT_1"));
			obj.setR49_amount_2(rs.getBigDecimal("R49_AMOUNT_2"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_Detail_Entity (camelCase, matches @Column names)
	// =========================================================
	public static class M_CA2_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLabel;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private java.math.BigDecimal sanctionLimit;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private java.math.BigDecimal acctBalanceInPula;
		private java.util.Date reportDate;
		private String reportName;
		private String createUser;
		private java.util.Date createTime;
		private String modifyUser;
		private java.util.Date modifyTime;
		private String verifyUser;
		private java.util.Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private String glCode;
		private String glSubCode;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String v) {
			custId = v;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String v) {
			acctNumber = v;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String v) {
			acctName = v;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String v) {
			dataType = v;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String v) {
			reportLabel = v;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String v) {
			reportAddlCriteria1 = v;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String v) {
			reportAddlCriteria2 = v;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String v) {
			reportAddlCriteria3 = v;
		}

		public java.math.BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(java.math.BigDecimal v) {
			sanctionLimit = v;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String v) {
			reportRemarks = v;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String v) {
			modificationRemarks = v;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String v) {
			dataEntryVersion = v;
		}

		public java.math.BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(java.math.BigDecimal v) {
			acctBalanceInPula = v;
		}

		public java.util.Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(java.util.Date v) {
			reportDate = v;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String v) {
			reportName = v;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String v) {
			createUser = v;
		}

		public java.util.Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(java.util.Date v) {
			createTime = v;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String v) {
			modifyUser = v;
		}

		public java.util.Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(java.util.Date v) {
			modifyTime = v;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String v) {
			verifyUser = v;
		}

		public java.util.Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(java.util.Date v) {
			verifyTime = v;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String v) {
			entityFlg = v;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String v) {
			modifyFlg = v;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String v) {
			delFlg = v;
		}

		public String getGlCode() {
			return glCode;
		}

		public void setGlCode(String v) {
			glCode = v;
		}

		public String getGlSubCode() {
			return glSubCode;
		}

		public void setGlSubCode(String v) {
			glSubCode = v;
		}
	}

	class M_CA2DetailRowMapper implements RowMapper<M_CA2_Detail_Entity> {
		@Override
		public M_CA2_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_Detail_Entity obj = new M_CA2_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setGlCode(rs.getString("GL_CODE"));
			obj.setGlSubCode(rs.getString("GL_SUB_CODE"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_Archival_Detail_Entity
	// =========================================================
	public static class M_CA2_Archival_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLabel;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private java.math.BigDecimal sanctionLimit;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private java.math.BigDecimal acctBalanceInPula;
		private java.util.Date reportDate;
		private String reportName;
		private String createUser;
		private java.util.Date createTime;
		private String modifyUser;
		private java.util.Date modifyTime;
		private String verifyUser;
		private java.util.Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private String glCode;
		private String glSubCode;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String v) {
			custId = v;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String v) {
			acctNumber = v;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String v) {
			acctName = v;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String v) {
			dataType = v;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String v) {
			reportLabel = v;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String v) {
			reportAddlCriteria1 = v;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String v) {
			reportAddlCriteria2 = v;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String v) {
			reportAddlCriteria3 = v;
		}

		public java.math.BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(java.math.BigDecimal v) {
			sanctionLimit = v;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String v) {
			reportRemarks = v;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String v) {
			modificationRemarks = v;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String v) {
			dataEntryVersion = v;
		}

		public java.math.BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(java.math.BigDecimal v) {
			acctBalanceInPula = v;
		}

		public java.util.Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(java.util.Date v) {
			reportDate = v;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String v) {
			reportName = v;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String v) {
			createUser = v;
		}

		public java.util.Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(java.util.Date v) {
			createTime = v;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String v) {
			modifyUser = v;
		}

		public java.util.Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(java.util.Date v) {
			modifyTime = v;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String v) {
			verifyUser = v;
		}

		public java.util.Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(java.util.Date v) {
			verifyTime = v;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String v) {
			entityFlg = v;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String v) {
			modifyFlg = v;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String v) {
			delFlg = v;
		}

		public String getGlCode() {
			return glCode;
		}

		public void setGlCode(String v) {
			glCode = v;
		}

		public String getGlSubCode() {
			return glSubCode;
		}

		public void setGlSubCode(String v) {
			glSubCode = v;
		}
	}

	class M_CA2ArchivalDetailRowMapper implements RowMapper<M_CA2_Archival_Detail_Entity> {
		@Override
		public M_CA2_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_Archival_Detail_Entity obj = new M_CA2_Archival_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setGlCode(rs.getString("GL_CODE"));
			obj.setGlSubCode(rs.getString("GL_SUB_CODE"));
			return obj;
		}
	}

	// =========================================================
	// Inner entity: M_CA2_RESUB_Detail_Entity
	// =========================================================
	public static class M_CA2_RESUB_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLabel;
		private String reportAddlCriteria1;
		private String reportAddlCriteria2;
		private String reportAddlCriteria3;
		private java.math.BigDecimal sanctionLimit;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private java.math.BigDecimal acctBalanceInPula;
		private java.util.Date reportDate;
		private String reportName;
		private String createUser;
		private java.util.Date createTime;
		private String modifyUser;
		private java.util.Date modifyTime;
		private String verifyUser;
		private java.util.Date verifyTime;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private String glCode;
		private String glSubCode;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String v) {
			custId = v;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String v) {
			acctNumber = v;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String v) {
			acctName = v;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String v) {
			dataType = v;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String v) {
			reportLabel = v;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String v) {
			reportAddlCriteria1 = v;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String v) {
			reportAddlCriteria2 = v;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String v) {
			reportAddlCriteria3 = v;
		}

		public java.math.BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(java.math.BigDecimal v) {
			sanctionLimit = v;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String v) {
			reportRemarks = v;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String v) {
			modificationRemarks = v;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String v) {
			dataEntryVersion = v;
		}

		public java.math.BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(java.math.BigDecimal v) {
			acctBalanceInPula = v;
		}

		public java.util.Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(java.util.Date v) {
			reportDate = v;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String v) {
			reportName = v;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String v) {
			createUser = v;
		}

		public java.util.Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(java.util.Date v) {
			createTime = v;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String v) {
			modifyUser = v;
		}

		public java.util.Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(java.util.Date v) {
			modifyTime = v;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String v) {
			verifyUser = v;
		}

		public java.util.Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(java.util.Date v) {
			verifyTime = v;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String v) {
			entityFlg = v;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String v) {
			modifyFlg = v;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String v) {
			delFlg = v;
		}

		public String getGlCode() {
			return glCode;
		}

		public void setGlCode(String v) {
			glCode = v;
		}

		public String getGlSubCode() {
			return glSubCode;
		}

		public void setGlSubCode(String v) {
			glSubCode = v;
		}
	}

	class M_CA2ResubDetailRowMapper implements RowMapper<M_CA2_RESUB_Detail_Entity> {
		@Override
		public M_CA2_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA2_RESUB_Detail_Entity obj = new M_CA2_RESUB_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setGlCode(rs.getString("GL_CODE"));
			obj.setGlSubCode(rs.getString("GL_SUB_CODE"));
			return obj;
		}
	}

}
