package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional

public class BRRS_Q_LARADV_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_LARADV_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// =========================================================
	// JDBC QUERY METHODS
	// =========================================================

	public List<Q_LARADV_Summary_Entity> getSummaryByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_Q_LARADV_SUMMARYTABLE WHERE REPORT_DATE = ? ORDER BY SNO",
			new Object[]{reportDate}, new Q_LARADVSummaryRowMapper());
	}

	public List<Q_LARADV_Detail_Entity> getDetailByDate(Date reportDate) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_Q_LARADV_DETAILTABLE WHERE REPORT_DATE = ?",
			new Object[]{reportDate}, new Q_LARADVDetailRowMapper());
	}

	public List<Q_LARADV_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_Q_LARADV_ARCHIVAL_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new Q_LARADVArchivalSummaryRowMapper());
	}

	public List<Q_LARADV_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_Q_LARADV_ARCHIVAL_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new Q_LARADVArchivalDetailRowMapper());
	}

	public List<Q_LARADV_Resub_Summary_Entity> getResubSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_Q_LARADV_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new Q_LARADVResubSummaryRowMapper());
	}

	public List<Q_LARADV_Resub_Detail_Entity> getResubDetailByDateAndVersion(Date reportDate, BigDecimal version) {
		return jdbcTemplate.query(
			"SELECT * FROM BRRS_Q_LARADV_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
			new Object[]{reportDate, version}, new Q_LARADVResubDetailRowMapper());
	}

	private List<Object[]> getArchivalResubData() {
		return jdbcTemplate.query(
			"SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_Q_LARADV_ARCHIVAL_SUMMARYTABLE " +
			"WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION",
			(rs, rowNum) -> new Object[]{ rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	private List<Object[]> getResubResubData() {
		return jdbcTemplate.query(
			"SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_Q_LARADV_RESUB_SUMMARYTABLE " +
			"WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION",
			(rs, rowNum) -> new Object[]{ rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	private Q_LARADV_Summary_Entity findSummaryBySno(Long sno) {
		List<Q_LARADV_Summary_Entity> list = jdbcTemplate.query(
			"SELECT * FROM BRRS_Q_LARADV_SUMMARYTABLE WHERE SNO = ?",
			new Object[]{sno}, new Q_LARADVSummaryRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	private int countDetailBySno(Long sno) {
		Integer count = jdbcTemplate.queryForObject(
			"SELECT COUNT(*) FROM BRRS_Q_LARADV_DETAILTABLE WHERE SNO = ?",
			new Object[]{sno}, Integer.class);
		return count != null ? count : 0;
	}

	// =========================================================
	// JDBC WRITE METHODS
	// =========================================================

	private void insertSummary(Q_LARADV_Summary_Entity e) {
		jdbcTemplate.update(
			"INSERT INTO BRRS_Q_LARADV_SUMMARYTABLE " +
			"(SNO, GROUP_NAME, CUSTOMER_GROUP_NAME, SECTOR_TYPE, FACILITY_TYPE, ORIGINAL_AMOUNT, " +
			"UTILISATION_OUTSTANDING_BALANCE, EFFECTIVE_DATE, REPAYMENT_PERIOD, PERFORMANCE_STATUS, " +
			"SECURITY_DETAILS, BOARD_APPROVAL, INTEREST_RATE, OUTSTANDING_BALANCE_PERCENT, LIMIT_PERCENT, " +
			"REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, " +
			"ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) " +
			"VALUES (BRRS_Q_LARADV_SUMMARYTABLE_SNO_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
			e.getGroupName(), e.getCustomerGroupName(), e.getSectorType(), e.getFacilityType(),
			e.getOriginalAmount(), e.getUtilisationOutstandingBalance(), e.getEffectiveDate(),
			e.getRepaymentPeriod(), e.getPerformanceStatus(), e.getSecurityDetails(), e.getBoardApproval(),
			e.getInterestRate(), e.getOutstandingBalancePercent(), e.getLimitPercent(),
			e.getReportDate(), e.getReportVersion(), e.getReportFrequency(), e.getReportCode(), e.getReportDesc(),
			e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(), e.getReportResubdate());
	}

	private void insertDetail(Q_LARADV_Detail_Entity e) {
		jdbcTemplate.update(
			"INSERT INTO BRRS_Q_LARADV_DETAILTABLE " +
			"(SNO, GROUP_NAME, CUSTOMER_GROUP_NAME, SECTOR_TYPE, FACILITY_TYPE, ORIGINAL_AMOUNT, " +
			"UTILISATION_OUTSTANDING_BALANCE, EFFECTIVE_DATE, REPAYMENT_PERIOD, PERFORMANCE_STATUS, " +
			"SECURITY_DETAILS, BOARD_APPROVAL, INTEREST_RATE, OUTSTANDING_BALANCE_PERCENT, LIMIT_PERCENT, " +
			"REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, " +
			"ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) " +
			"VALUES (BRRS_Q_LARADV_DETAILTABLE_SNO_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
			e.getGroupName(), e.getCustomerGroupName(), e.getSectorType(), e.getFacilityType(),
			e.getOriginalAmount(), e.getUtilisationOutstandingBalance(), e.getEffectiveDate(),
			e.getRepaymentPeriod(), e.getPerformanceStatus(), e.getSecurityDetails(), e.getBoardApproval(),
			e.getInterestRate(), e.getOutstandingBalancePercent(), e.getLimitPercent(),
			e.getReportDate(), e.getReportVersion(), e.getReportFrequency(), e.getReportCode(), e.getReportDesc(),
			e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(), e.getReportResubdate());
	}

	private void updateSummaryBySno(Q_LARADV_Summary_Entity e) {
		jdbcTemplate.update(
			"UPDATE BRRS_Q_LARADV_SUMMARYTABLE SET " +
			"GROUP_NAME=?, CUSTOMER_GROUP_NAME=?, SECTOR_TYPE=?, FACILITY_TYPE=?, " +
			"ORIGINAL_AMOUNT=?, UTILISATION_OUTSTANDING_BALANCE=?, EFFECTIVE_DATE=?, " +
			"REPAYMENT_PERIOD=?, PERFORMANCE_STATUS=?, SECURITY_DETAILS=?, BOARD_APPROVAL=?, " +
			"INTEREST_RATE=?, OUTSTANDING_BALANCE_PERCENT=?, LIMIT_PERCENT=? WHERE SNO=?",
			e.getGroupName(), e.getCustomerGroupName(), e.getSectorType(), e.getFacilityType(),
			e.getOriginalAmount(), e.getUtilisationOutstandingBalance(), e.getEffectiveDate(),
			e.getRepaymentPeriod(), e.getPerformanceStatus(), e.getSecurityDetails(), e.getBoardApproval(),
			e.getInterestRate(), e.getOutstandingBalancePercent(), e.getLimitPercent(),
			e.getSno());
	}

	private void upsertDetailBySno(Q_LARADV_Detail_Entity e) {
		if (countDetailBySno(e.getSno()) > 0) {
			jdbcTemplate.update(
				"UPDATE BRRS_Q_LARADV_DETAILTABLE SET " +
				"GROUP_NAME=?, CUSTOMER_GROUP_NAME=?, SECTOR_TYPE=?, FACILITY_TYPE=?, " +
				"ORIGINAL_AMOUNT=?, UTILISATION_OUTSTANDING_BALANCE=?, EFFECTIVE_DATE=?, " +
				"REPAYMENT_PERIOD=?, PERFORMANCE_STATUS=?, SECURITY_DETAILS=?, BOARD_APPROVAL=?, " +
				"INTEREST_RATE=?, OUTSTANDING_BALANCE_PERCENT=?, LIMIT_PERCENT=?, MODIFY_FLG=? WHERE SNO=?",
				e.getGroupName(), e.getCustomerGroupName(), e.getSectorType(), e.getFacilityType(),
				e.getOriginalAmount(), e.getUtilisationOutstandingBalance(), e.getEffectiveDate(),
				e.getRepaymentPeriod(), e.getPerformanceStatus(), e.getSecurityDetails(), e.getBoardApproval(),
				e.getInterestRate(), e.getOutstandingBalancePercent(), e.getLimitPercent(),
				e.getModifyFlg(), e.getSno());
		} else {
			jdbcTemplate.update(
				"INSERT INTO BRRS_Q_LARADV_DETAILTABLE " +
				"(SNO, GROUP_NAME, CUSTOMER_GROUP_NAME, SECTOR_TYPE, FACILITY_TYPE, ORIGINAL_AMOUNT, " +
				"UTILISATION_OUTSTANDING_BALANCE, EFFECTIVE_DATE, REPAYMENT_PERIOD, PERFORMANCE_STATUS, " +
				"SECURITY_DETAILS, BOARD_APPROVAL, INTEREST_RATE, OUTSTANDING_BALANCE_PERCENT, LIMIT_PERCENT, " +
				"REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, " +
				"ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				e.getSno(),
				e.getGroupName(), e.getCustomerGroupName(), e.getSectorType(), e.getFacilityType(),
				e.getOriginalAmount(), e.getUtilisationOutstandingBalance(), e.getEffectiveDate(),
				e.getRepaymentPeriod(), e.getPerformanceStatus(), e.getSecurityDetails(), e.getBoardApproval(),
				e.getInterestRate(), e.getOutstandingBalancePercent(), e.getLimitPercent(),
				e.getReportDate(), e.getReportVersion(), e.getReportFrequency(), e.getReportCode(), e.getReportDesc(),
				e.getEntityFlg(), e.getModifyFlg(), e.getDelFlg(), e.getReportResubdate());
		}
	}

	// =========================================================
	// SERVICE METHODS
	// =========================================================

	public ModelAndView getBRRS_Q_LARADV_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {

		ModelAndView mv = new ModelAndView();
		
		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);
		
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
				List<Q_LARADV_Archival_Summary_Entity> T1Master = getArchivalSummaryByDateAndVersion(
						dateformat.parse(todate), version);

				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<Q_LARADV_Resub_Summary_Entity> T1Master = getResubSummaryByDateAndVersion(
						dateformat.parse(todate), version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<Q_LARADV_Summary_Entity> T1Master = getSummaryByDate(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_LARADV_Archival_Detail_Entity> T1Master = getArchivalDetailByDateAndVersion(
							dateformat.parse(todate), version);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_LARADV_Resub_Detail_Entity> T1Master = getResubDetailByDateAndVersion(
							dateformat.parse(todate), version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<Q_LARADV_Detail_Entity> T1Master = getDetailByDate(dateformat.parse(todate));

					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/Q_LARADV");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public Q_LARADV_Summary_Entity findById(Long id) {

		return findSummaryBySno(id);

	}

	public byte[] getBRRS_Q_LARADV_EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= DOWNLOAD DETAILS =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT    : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");

		Date reportDate = dateformat.parse(todate);

		List<Q_LARADV_Summary_Entity> dataList = getSummaryByDate(reportDate);

		if (dataList == null || dataList.isEmpty()) {
			logger.warn("No data found for BRRS_Q_LARADV report.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);

		logger.info("Loading template from : {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found : " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
			throw new SecurityException("Template file exists but not readable : " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			// Set report date in Row 6, Column 1 (B7)

			Row dateRow = sheet.getRow(6);
			if (dateRow == null) {
				dateRow = sheet.createRow(6);
			}

			Cell dateCell = dateRow.getCell(1);
			if (dateCell == null) {
				dateCell = dateRow.createCell(1);
			}

			// Convert to DD/MM/YYYY format
			SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");

			Date reportDisplayDate = dateformat.parse(todate);

			dateCell.setCellValue(displayFormat.format(reportDisplayDate));

			/*
			 * ========================================================== FONT
			 * ==========================================================
			 */

			Font font = workbook.createFont();
			font.setFontName("Arial");
			font.setFontHeightInPoints((short) 8);

			/*
			 * ========================================================== TEXT STYLE
			 * ==========================================================
			 */

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setFont(font);
			textStyle.setWrapText(true);

			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			/*
			 * ========================================================== NUMBER STYLE
			 * ==========================================================
			 */

			DataFormat dataFormat = workbook.createDataFormat();

			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setFont(font);
			numberStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);

			/*
			 * ========================================================== DATE STYLE
			 * ==========================================================
			 */

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setFont(font);
			dateStyle.setDataFormat(dataFormat.getFormat("dd-MM-yyyy"));

			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			/*
			 * ========================================================== TOTAL VARIABLES
			 * ==========================================================
			 */

			double totalOriginalAmount = 0.00;
			double totalOutstandingBalance = 0.00;

			/*
			 * ========================================================== DATA FILLING
			 * ==========================================================
			 */

			int rowIndex = 9;

			for (Q_LARADV_Summary_Entity item : dataList) {

				Row row = sheet.createRow(rowIndex++);

				Cell cell;

				// Column A
				cell = row.createCell(0);
				cell.setCellValue(item.getCustomerGroupName() != null ? item.getCustomerGroupName() : "");
				cell.setCellStyle(textStyle);

				// Column D
				cell = row.createCell(1);
				cell.setCellValue(item.getFacilityType() != null ? item.getFacilityType() : "");
				cell.setCellStyle(textStyle);

				// Column E - Original Amount
				cell = row.createCell(2);

				double originalAmt = item.getOriginalAmount() != null ? item.getOriginalAmount().doubleValue() : 0.00;

				cell.setCellValue(originalAmt);
				cell.setCellStyle(numberStyle);

				totalOriginalAmount += originalAmt;

				// Column F - Outstanding Balance
				cell = row.createCell(3);

				double outstandingAmt = item.getUtilisationOutstandingBalance() != null
						? item.getUtilisationOutstandingBalance().doubleValue()
						: 0.00;

				cell.setCellValue(outstandingAmt);
				cell.setCellStyle(numberStyle);

				totalOutstandingBalance += outstandingAmt;

				// Column G - Effective Date
				cell = row.createCell(4);

				if (item.getEffectiveDate() != null) {
					cell.setCellValue(item.getEffectiveDate());
					cell.setCellStyle(dateStyle);
				} else {
					cell.setCellValue("");
					cell.setCellStyle(textStyle);
				}

				// Column H
				cell = row.createCell(5);
				cell.setCellValue(item.getRepaymentPeriod() != null ? item.getRepaymentPeriod() : "");
				cell.setCellStyle(textStyle);

				// Column I
				cell = row.createCell(6);
				cell.setCellValue(item.getPerformanceStatus() != null ? item.getPerformanceStatus() : "");
				cell.setCellStyle(textStyle);

				// Column J
				cell = row.createCell(7);
				cell.setCellValue(item.getSecurityDetails() != null ? item.getSecurityDetails() : "");
				cell.setCellStyle(textStyle);

				// Column K
				cell = row.createCell(8);
				cell.setCellValue(item.getBoardApproval() != null ? item.getBoardApproval() : "");
				cell.setCellStyle(textStyle);

				// Column L
				cell = row.createCell(9);
				cell.setCellValue(item.getInterestRate() != null ? item.getInterestRate().doubleValue() : 0.00);
				cell.setCellStyle(numberStyle);

				// Column M
				cell = row.createCell(10);
				cell.setCellValue(
						item.getOutstandingBalancePercent() != null ? item.getOutstandingBalancePercent().doubleValue()
								: 0.00);
				cell.setCellStyle(numberStyle);

				// Column N
				cell = row.createCell(11);
				cell.setCellValue(item.getLimitPercent() != null ? item.getLimitPercent().doubleValue() : 0.00);
				cell.setCellStyle(numberStyle);
			}

			/*
			 * ========================================================== TOTAL ROW
			 * ==========================================================
			 */

			Row totalRow = sheet.createRow(rowIndex);

			Cell cell = totalRow.createCell(1);
			cell.setCellValue("TOTAL");
			cell.setCellStyle(textStyle);

			// Original Amount Total
			cell = totalRow.createCell(2);
			cell.setCellValue(totalOriginalAmount);
			cell.setCellStyle(numberStyle);

			// Outstanding Balance Total
			cell = totalRow.createCell(3);
			cell.setCellValue(totalOutstandingBalance);
			cell.setCellStyle(numberStyle);

			/*
			 * ========================================================== AUTO SIZE
			 * ==========================================================
			 */

			for (int i = 0; i <= 11; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(out);

			logger.info("Excel generated successfully. Size : {} bytes", out.size());

			return out.toByteArray();
		}
	}

	public void saveQlaradv(Q_LARADV_Summary_Entity summary) {

		// Save Summary Table
		insertSummary(summary);

		// Copy to Detail Entity
		Q_LARADV_Detail_Entity detail = new Q_LARADV_Detail_Entity();

		detail.setGroupName(summary.getGroupName());
		detail.setCustomerGroupName(summary.getCustomerGroupName());
		detail.setSectorType(summary.getSectorType());
		detail.setFacilityType(summary.getFacilityType());

		detail.setOriginalAmount(summary.getOriginalAmount());
		detail.setUtilisationOutstandingBalance(summary.getUtilisationOutstandingBalance());

		detail.setEffectiveDate(summary.getEffectiveDate());
		detail.setRepaymentPeriod(summary.getRepaymentPeriod());
		detail.setPerformanceStatus(summary.getPerformanceStatus());
		detail.setSecurityDetails(summary.getSecurityDetails());
		detail.setBoardApproval(summary.getBoardApproval());

		detail.setInterestRate(summary.getInterestRate());

		detail.setOutstandingBalancePercent(summary.getOutstandingBalancePercent());

		detail.setLimitPercent(summary.getLimitPercent());

		detail.setReportDate(summary.getReportDate());
		detail.setReportVersion(summary.getReportVersion());
		detail.setReportFrequency(summary.getReportFrequency());
		detail.setReportCode(summary.getReportCode());
		detail.setReportDesc(summary.getReportDesc());

		detail.setEntityFlg(summary.getEntityFlg());
		detail.setModifyFlg(summary.getModifyFlg());
		detail.setDelFlg(summary.getDelFlg());

		detail.setReportResubdate(summary.getReportResubdate());

		// Save Detail Table
		insertDetail(detail);
	}

	public void updateQlaradv(Q_LARADV_Summary_Entity summary) {

		// ==========================
		// UPDATE SUMMARY TABLE
		// ==========================

		Q_LARADV_Summary_Entity existingSummary = findSummaryBySno(summary.getSno());
		if (existingSummary == null) {
			throw new RuntimeException("Summary Record Not Found");
		}

		updateSummaryBySno(summary);

		// ==========================
		// UPDATE DETAIL TABLE
		// ==========================

		Q_LARADV_Detail_Entity detail = new Q_LARADV_Detail_Entity();

		detail.setSno(summary.getSno());

		detail.setGroupName(summary.getGroupName());
		detail.setCustomerGroupName(summary.getCustomerGroupName());

		detail.setSectorType(summary.getSectorType());

		detail.setFacilityType(summary.getFacilityType());

		detail.setOriginalAmount(summary.getOriginalAmount());

		detail.setUtilisationOutstandingBalance(summary.getUtilisationOutstandingBalance());

		detail.setEffectiveDate(summary.getEffectiveDate());

		detail.setRepaymentPeriod(summary.getRepaymentPeriod());

		detail.setPerformanceStatus(summary.getPerformanceStatus());

		detail.setSecurityDetails(summary.getSecurityDetails());

		detail.setBoardApproval(summary.getBoardApproval());

		detail.setInterestRate(summary.getInterestRate());

		detail.setOutstandingBalancePercent(summary.getOutstandingBalancePercent());

		detail.setLimitPercent(summary.getLimitPercent());

		detail.setModifyFlg("Y");

		upsertDetailBySno(detail);
	}

	public List<Object[]> getQ_LARADVResub() {

		List<Object[]> mergedList = new ArrayList<>();

		try {
			List<Object[]> list5 = getResubResubData();

			for (Object[] e : list5) {
				Date reportDate = e[0] != null ? (Date) e[0] : null;
				BigDecimal reportVersion = e[1] != null ? (BigDecimal) e[1] : null;
				Date reportResubDate = e[2] != null ? (Date) e[2] : null;

				mergedList.add(new Object[] { reportDate, reportVersion, reportResubDate });
			}

			Map<String, Object[]> uniqueMap = new LinkedHashMap<>();

			for (Object[] row : mergedList) {
				Date reportDate = (Date) row[0];
				Object reportVersion = row[1];

				String key = reportDate + "_" + reportVersion;
				uniqueMap.putIfAbsent(key, row);
			}

			List<Object[]> result = new ArrayList<>(uniqueMap.values());

			return result;

		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.emptyList();
		}
	}


	public List<Object[]> getQ_LARADVArchival() {

		List<Object[]> mergedList = new ArrayList<>();

		try {
			List<Object[]> list5 = getArchivalResubData();

			for (Object[] e : list5) {
				Date reportDate = e[0] != null ? (Date) e[0] : null;
				BigDecimal reportVersion = e[1] != null ? (BigDecimal) e[1] : null;
				Date reportResubDate = e[2] != null ? (Date) e[2] : null;

				mergedList.add(new Object[] { reportDate, reportVersion, reportResubDate });
			}

			Map<String, Object[]> uniqueMap = new LinkedHashMap<>();

			for (Object[] row : mergedList) {
				Date reportDate = (Date) row[0];
				Object reportVersion = row[1];

				String key = reportDate + "_" + reportVersion;
				uniqueMap.putIfAbsent(key, row);
			}

			List<Object[]> result = new ArrayList<>(uniqueMap.values());

			return result;

		} catch (Exception ex) {
			ex.printStackTrace();
			return Collections.emptyList();
		}
	}

	// =========================================================
	// ROW MAPPERS
	// =========================================================

	// =========================================================
	// Inner entity: Q_LARADV_Summary_Entity
	// =========================================================
	public static class Q_LARADV_Summary_Entity {
		private Long sno;
		private String groupName;
		private String customerGroupName;
		private String sectorType;
		private String facilityType;
		private BigDecimal originalAmount;
		private BigDecimal utilisationOutstandingBalance;
		private Date effectiveDate;
		private String repaymentPeriod;
		private String performanceStatus;
		private String securityDetails;
		private String boardApproval;
		private BigDecimal interestRate;
		private BigDecimal outstandingBalancePercent;
		private BigDecimal limitPercent;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private Date reportResubdate;

		public Long getSno() { return sno; }
		public void setSno(Long sno) { this.sno = sno; }
		public String getGroupName() { return groupName; }
		public void setGroupName(String groupName) { this.groupName = groupName; }
		public String getCustomerGroupName() { return customerGroupName; }
		public void setCustomerGroupName(String customerGroupName) { this.customerGroupName = customerGroupName; }
		public String getSectorType() { return sectorType; }
		public void setSectorType(String sectorType) { this.sectorType = sectorType; }
		public String getFacilityType() { return facilityType; }
		public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
		public BigDecimal getOriginalAmount() { return originalAmount; }
		public void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }
		public BigDecimal getUtilisationOutstandingBalance() { return utilisationOutstandingBalance; }
		public void setUtilisationOutstandingBalance(BigDecimal utilisationOutstandingBalance) { this.utilisationOutstandingBalance = utilisationOutstandingBalance; }
		public Date getEffectiveDate() { return effectiveDate; }
		public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
		public String getRepaymentPeriod() { return repaymentPeriod; }
		public void setRepaymentPeriod(String repaymentPeriod) { this.repaymentPeriod = repaymentPeriod; }
		public String getPerformanceStatus() { return performanceStatus; }
		public void setPerformanceStatus(String performanceStatus) { this.performanceStatus = performanceStatus; }
		public String getSecurityDetails() { return securityDetails; }
		public void setSecurityDetails(String securityDetails) { this.securityDetails = securityDetails; }
		public String getBoardApproval() { return boardApproval; }
		public void setBoardApproval(String boardApproval) { this.boardApproval = boardApproval; }
		public BigDecimal getInterestRate() { return interestRate; }
		public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
		public BigDecimal getOutstandingBalancePercent() { return outstandingBalancePercent; }
		public void setOutstandingBalancePercent(BigDecimal outstandingBalancePercent) { this.outstandingBalancePercent = outstandingBalancePercent; }
		public BigDecimal getLimitPercent() { return limitPercent; }
		public void setLimitPercent(BigDecimal limitPercent) { this.limitPercent = limitPercent; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getReportFrequency() { return reportFrequency; }
		public void setReportFrequency(String reportFrequency) { this.reportFrequency = reportFrequency; }
		public String getReportCode() { return reportCode; }
		public void setReportCode(String reportCode) { this.reportCode = reportCode; }
		public String getReportDesc() { return reportDesc; }
		public void setReportDesc(String reportDesc) { this.reportDesc = reportDesc; }
		public String getEntityFlg() { return entityFlg; }
		public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }
		public String getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(String modifyFlg) { this.modifyFlg = modifyFlg; }
		public String getDelFlg() { return delFlg; }
		public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
		public Date getReportResubdate() { return reportResubdate; }
		public void setReportResubdate(Date reportResubdate) { this.reportResubdate = reportResubdate; }
	}

	// =========================================================
	// Inner entity: Q_LARADV_Detail_Entity
	// =========================================================
	public static class Q_LARADV_Detail_Entity {
		private Long sno;
		private String groupName;
		private String customerGroupName;
		private String sectorType;
		private String facilityType;
		private BigDecimal originalAmount;
		private BigDecimal utilisationOutstandingBalance;
		private Date effectiveDate;
		private String repaymentPeriod;
		private String performanceStatus;
		private String securityDetails;
		private String boardApproval;
		private BigDecimal interestRate;
		private BigDecimal outstandingBalancePercent;
		private BigDecimal limitPercent;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private Date reportResubdate;

		public Long getSno() { return sno; }
		public void setSno(Long sno) { this.sno = sno; }
		public String getGroupName() { return groupName; }
		public void setGroupName(String groupName) { this.groupName = groupName; }
		public String getCustomerGroupName() { return customerGroupName; }
		public void setCustomerGroupName(String customerGroupName) { this.customerGroupName = customerGroupName; }
		public String getSectorType() { return sectorType; }
		public void setSectorType(String sectorType) { this.sectorType = sectorType; }
		public String getFacilityType() { return facilityType; }
		public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
		public BigDecimal getOriginalAmount() { return originalAmount; }
		public void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }
		public BigDecimal getUtilisationOutstandingBalance() { return utilisationOutstandingBalance; }
		public void setUtilisationOutstandingBalance(BigDecimal utilisationOutstandingBalance) { this.utilisationOutstandingBalance = utilisationOutstandingBalance; }
		public Date getEffectiveDate() { return effectiveDate; }
		public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
		public String getRepaymentPeriod() { return repaymentPeriod; }
		public void setRepaymentPeriod(String repaymentPeriod) { this.repaymentPeriod = repaymentPeriod; }
		public String getPerformanceStatus() { return performanceStatus; }
		public void setPerformanceStatus(String performanceStatus) { this.performanceStatus = performanceStatus; }
		public String getSecurityDetails() { return securityDetails; }
		public void setSecurityDetails(String securityDetails) { this.securityDetails = securityDetails; }
		public String getBoardApproval() { return boardApproval; }
		public void setBoardApproval(String boardApproval) { this.boardApproval = boardApproval; }
		public BigDecimal getInterestRate() { return interestRate; }
		public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
		public BigDecimal getOutstandingBalancePercent() { return outstandingBalancePercent; }
		public void setOutstandingBalancePercent(BigDecimal outstandingBalancePercent) { this.outstandingBalancePercent = outstandingBalancePercent; }
		public BigDecimal getLimitPercent() { return limitPercent; }
		public void setLimitPercent(BigDecimal limitPercent) { this.limitPercent = limitPercent; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getReportFrequency() { return reportFrequency; }
		public void setReportFrequency(String reportFrequency) { this.reportFrequency = reportFrequency; }
		public String getReportCode() { return reportCode; }
		public void setReportCode(String reportCode) { this.reportCode = reportCode; }
		public String getReportDesc() { return reportDesc; }
		public void setReportDesc(String reportDesc) { this.reportDesc = reportDesc; }
		public String getEntityFlg() { return entityFlg; }
		public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }
		public String getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(String modifyFlg) { this.modifyFlg = modifyFlg; }
		public String getDelFlg() { return delFlg; }
		public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
		public Date getReportResubdate() { return reportResubdate; }
		public void setReportResubdate(Date reportResubdate) { this.reportResubdate = reportResubdate; }
	}

	// =========================================================
	// Inner entity: Q_LARADV_Archival_Summary_Entity
	// =========================================================
	public static class Q_LARADV_Archival_Summary_Entity {
		private Long sno;
		private String groupName;
		private String customerGroupName;
		private String sectorType;
		private String facilityType;
		private BigDecimal originalAmount;
		private BigDecimal utilisationOutstandingBalance;
		private Date effectiveDate;
		private String repaymentPeriod;
		private String performanceStatus;
		private String securityDetails;
		private String boardApproval;
		private BigDecimal interestRate;
		private BigDecimal outstandingBalancePercent;
		private BigDecimal limitPercent;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private Date reportResubdate;

		public Long getSno() { return sno; }
		public void setSno(Long sno) { this.sno = sno; }
		public String getGroupName() { return groupName; }
		public void setGroupName(String groupName) { this.groupName = groupName; }
		public String getCustomerGroupName() { return customerGroupName; }
		public void setCustomerGroupName(String customerGroupName) { this.customerGroupName = customerGroupName; }
		public String getSectorType() { return sectorType; }
		public void setSectorType(String sectorType) { this.sectorType = sectorType; }
		public String getFacilityType() { return facilityType; }
		public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
		public BigDecimal getOriginalAmount() { return originalAmount; }
		public void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }
		public BigDecimal getUtilisationOutstandingBalance() { return utilisationOutstandingBalance; }
		public void setUtilisationOutstandingBalance(BigDecimal utilisationOutstandingBalance) { this.utilisationOutstandingBalance = utilisationOutstandingBalance; }
		public Date getEffectiveDate() { return effectiveDate; }
		public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
		public String getRepaymentPeriod() { return repaymentPeriod; }
		public void setRepaymentPeriod(String repaymentPeriod) { this.repaymentPeriod = repaymentPeriod; }
		public String getPerformanceStatus() { return performanceStatus; }
		public void setPerformanceStatus(String performanceStatus) { this.performanceStatus = performanceStatus; }
		public String getSecurityDetails() { return securityDetails; }
		public void setSecurityDetails(String securityDetails) { this.securityDetails = securityDetails; }
		public String getBoardApproval() { return boardApproval; }
		public void setBoardApproval(String boardApproval) { this.boardApproval = boardApproval; }
		public BigDecimal getInterestRate() { return interestRate; }
		public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
		public BigDecimal getOutstandingBalancePercent() { return outstandingBalancePercent; }
		public void setOutstandingBalancePercent(BigDecimal outstandingBalancePercent) { this.outstandingBalancePercent = outstandingBalancePercent; }
		public BigDecimal getLimitPercent() { return limitPercent; }
		public void setLimitPercent(BigDecimal limitPercent) { this.limitPercent = limitPercent; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getReportFrequency() { return reportFrequency; }
		public void setReportFrequency(String reportFrequency) { this.reportFrequency = reportFrequency; }
		public String getReportCode() { return reportCode; }
		public void setReportCode(String reportCode) { this.reportCode = reportCode; }
		public String getReportDesc() { return reportDesc; }
		public void setReportDesc(String reportDesc) { this.reportDesc = reportDesc; }
		public String getEntityFlg() { return entityFlg; }
		public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }
		public String getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(String modifyFlg) { this.modifyFlg = modifyFlg; }
		public String getDelFlg() { return delFlg; }
		public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
		public Date getReportResubdate() { return reportResubdate; }
		public void setReportResubdate(Date reportResubdate) { this.reportResubdate = reportResubdate; }
	}

	// =========================================================
	// Inner entity: Q_LARADV_Archival_Detail_Entity
	// =========================================================
	public static class Q_LARADV_Archival_Detail_Entity {
		private Long sno;
		private String groupName;
		private String customerGroupName;
		private String sectorType;
		private String facilityType;
		private BigDecimal originalAmount;
		private BigDecimal utilisationOutstandingBalance;
		private Date effectiveDate;
		private String repaymentPeriod;
		private String performanceStatus;
		private String securityDetails;
		private String boardApproval;
		private BigDecimal interestRate;
		private BigDecimal outstandingBalancePercent;
		private BigDecimal limitPercent;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private Date reportResubdate;

		public Long getSno() { return sno; }
		public void setSno(Long sno) { this.sno = sno; }
		public String getGroupName() { return groupName; }
		public void setGroupName(String groupName) { this.groupName = groupName; }
		public String getCustomerGroupName() { return customerGroupName; }
		public void setCustomerGroupName(String customerGroupName) { this.customerGroupName = customerGroupName; }
		public String getSectorType() { return sectorType; }
		public void setSectorType(String sectorType) { this.sectorType = sectorType; }
		public String getFacilityType() { return facilityType; }
		public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
		public BigDecimal getOriginalAmount() { return originalAmount; }
		public void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }
		public BigDecimal getUtilisationOutstandingBalance() { return utilisationOutstandingBalance; }
		public void setUtilisationOutstandingBalance(BigDecimal utilisationOutstandingBalance) { this.utilisationOutstandingBalance = utilisationOutstandingBalance; }
		public Date getEffectiveDate() { return effectiveDate; }
		public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
		public String getRepaymentPeriod() { return repaymentPeriod; }
		public void setRepaymentPeriod(String repaymentPeriod) { this.repaymentPeriod = repaymentPeriod; }
		public String getPerformanceStatus() { return performanceStatus; }
		public void setPerformanceStatus(String performanceStatus) { this.performanceStatus = performanceStatus; }
		public String getSecurityDetails() { return securityDetails; }
		public void setSecurityDetails(String securityDetails) { this.securityDetails = securityDetails; }
		public String getBoardApproval() { return boardApproval; }
		public void setBoardApproval(String boardApproval) { this.boardApproval = boardApproval; }
		public BigDecimal getInterestRate() { return interestRate; }
		public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
		public BigDecimal getOutstandingBalancePercent() { return outstandingBalancePercent; }
		public void setOutstandingBalancePercent(BigDecimal outstandingBalancePercent) { this.outstandingBalancePercent = outstandingBalancePercent; }
		public BigDecimal getLimitPercent() { return limitPercent; }
		public void setLimitPercent(BigDecimal limitPercent) { this.limitPercent = limitPercent; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getReportFrequency() { return reportFrequency; }
		public void setReportFrequency(String reportFrequency) { this.reportFrequency = reportFrequency; }
		public String getReportCode() { return reportCode; }
		public void setReportCode(String reportCode) { this.reportCode = reportCode; }
		public String getReportDesc() { return reportDesc; }
		public void setReportDesc(String reportDesc) { this.reportDesc = reportDesc; }
		public String getEntityFlg() { return entityFlg; }
		public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }
		public String getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(String modifyFlg) { this.modifyFlg = modifyFlg; }
		public String getDelFlg() { return delFlg; }
		public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
		public Date getReportResubdate() { return reportResubdate; }
		public void setReportResubdate(Date reportResubdate) { this.reportResubdate = reportResubdate; }
	}

	// =========================================================
	// Inner entity: Q_LARADV_Resub_Summary_Entity
	// =========================================================
	public static class Q_LARADV_Resub_Summary_Entity {
		private Long sno;
		private String groupName;
		private String customerGroupName;
		private String sectorType;
		private String facilityType;
		private BigDecimal originalAmount;
		private BigDecimal utilisationOutstandingBalance;
		private Date effectiveDate;
		private String repaymentPeriod;
		private String performanceStatus;
		private String securityDetails;
		private String boardApproval;
		private BigDecimal interestRate;
		private BigDecimal outstandingBalancePercent;
		private BigDecimal limitPercent;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private Date reportResubdate;

		public Long getSno() { return sno; }
		public void setSno(Long sno) { this.sno = sno; }
		public String getGroupName() { return groupName; }
		public void setGroupName(String groupName) { this.groupName = groupName; }
		public String getCustomerGroupName() { return customerGroupName; }
		public void setCustomerGroupName(String customerGroupName) { this.customerGroupName = customerGroupName; }
		public String getSectorType() { return sectorType; }
		public void setSectorType(String sectorType) { this.sectorType = sectorType; }
		public String getFacilityType() { return facilityType; }
		public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
		public BigDecimal getOriginalAmount() { return originalAmount; }
		public void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }
		public BigDecimal getUtilisationOutstandingBalance() { return utilisationOutstandingBalance; }
		public void setUtilisationOutstandingBalance(BigDecimal utilisationOutstandingBalance) { this.utilisationOutstandingBalance = utilisationOutstandingBalance; }
		public Date getEffectiveDate() { return effectiveDate; }
		public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
		public String getRepaymentPeriod() { return repaymentPeriod; }
		public void setRepaymentPeriod(String repaymentPeriod) { this.repaymentPeriod = repaymentPeriod; }
		public String getPerformanceStatus() { return performanceStatus; }
		public void setPerformanceStatus(String performanceStatus) { this.performanceStatus = performanceStatus; }
		public String getSecurityDetails() { return securityDetails; }
		public void setSecurityDetails(String securityDetails) { this.securityDetails = securityDetails; }
		public String getBoardApproval() { return boardApproval; }
		public void setBoardApproval(String boardApproval) { this.boardApproval = boardApproval; }
		public BigDecimal getInterestRate() { return interestRate; }
		public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
		public BigDecimal getOutstandingBalancePercent() { return outstandingBalancePercent; }
		public void setOutstandingBalancePercent(BigDecimal outstandingBalancePercent) { this.outstandingBalancePercent = outstandingBalancePercent; }
		public BigDecimal getLimitPercent() { return limitPercent; }
		public void setLimitPercent(BigDecimal limitPercent) { this.limitPercent = limitPercent; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getReportFrequency() { return reportFrequency; }
		public void setReportFrequency(String reportFrequency) { this.reportFrequency = reportFrequency; }
		public String getReportCode() { return reportCode; }
		public void setReportCode(String reportCode) { this.reportCode = reportCode; }
		public String getReportDesc() { return reportDesc; }
		public void setReportDesc(String reportDesc) { this.reportDesc = reportDesc; }
		public String getEntityFlg() { return entityFlg; }
		public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }
		public String getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(String modifyFlg) { this.modifyFlg = modifyFlg; }
		public String getDelFlg() { return delFlg; }
		public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
		public Date getReportResubdate() { return reportResubdate; }
		public void setReportResubdate(Date reportResubdate) { this.reportResubdate = reportResubdate; }
	}

	// =========================================================
	// Inner entity: Q_LARADV_Resub_Detail_Entity
	// =========================================================
	public static class Q_LARADV_Resub_Detail_Entity {
		private Long sno;
		private String groupName;
		private String customerGroupName;
		private String sectorType;
		private String facilityType;
		private BigDecimal originalAmount;
		private BigDecimal utilisationOutstandingBalance;
		private Date effectiveDate;
		private String repaymentPeriod;
		private String performanceStatus;
		private String securityDetails;
		private String boardApproval;
		private BigDecimal interestRate;
		private BigDecimal outstandingBalancePercent;
		private BigDecimal limitPercent;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String reportFrequency;
		private String reportCode;
		private String reportDesc;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;
		private Date reportResubdate;

		public Long getSno() { return sno; }
		public void setSno(Long sno) { this.sno = sno; }
		public String getGroupName() { return groupName; }
		public void setGroupName(String groupName) { this.groupName = groupName; }
		public String getCustomerGroupName() { return customerGroupName; }
		public void setCustomerGroupName(String customerGroupName) { this.customerGroupName = customerGroupName; }
		public String getSectorType() { return sectorType; }
		public void setSectorType(String sectorType) { this.sectorType = sectorType; }
		public String getFacilityType() { return facilityType; }
		public void setFacilityType(String facilityType) { this.facilityType = facilityType; }
		public BigDecimal getOriginalAmount() { return originalAmount; }
		public void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }
		public BigDecimal getUtilisationOutstandingBalance() { return utilisationOutstandingBalance; }
		public void setUtilisationOutstandingBalance(BigDecimal utilisationOutstandingBalance) { this.utilisationOutstandingBalance = utilisationOutstandingBalance; }
		public Date getEffectiveDate() { return effectiveDate; }
		public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
		public String getRepaymentPeriod() { return repaymentPeriod; }
		public void setRepaymentPeriod(String repaymentPeriod) { this.repaymentPeriod = repaymentPeriod; }
		public String getPerformanceStatus() { return performanceStatus; }
		public void setPerformanceStatus(String performanceStatus) { this.performanceStatus = performanceStatus; }
		public String getSecurityDetails() { return securityDetails; }
		public void setSecurityDetails(String securityDetails) { this.securityDetails = securityDetails; }
		public String getBoardApproval() { return boardApproval; }
		public void setBoardApproval(String boardApproval) { this.boardApproval = boardApproval; }
		public BigDecimal getInterestRate() { return interestRate; }
		public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
		public BigDecimal getOutstandingBalancePercent() { return outstandingBalancePercent; }
		public void setOutstandingBalancePercent(BigDecimal outstandingBalancePercent) { this.outstandingBalancePercent = outstandingBalancePercent; }
		public BigDecimal getLimitPercent() { return limitPercent; }
		public void setLimitPercent(BigDecimal limitPercent) { this.limitPercent = limitPercent; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal reportVersion) { this.reportVersion = reportVersion; }
		public String getReportFrequency() { return reportFrequency; }
		public void setReportFrequency(String reportFrequency) { this.reportFrequency = reportFrequency; }
		public String getReportCode() { return reportCode; }
		public void setReportCode(String reportCode) { this.reportCode = reportCode; }
		public String getReportDesc() { return reportDesc; }
		public void setReportDesc(String reportDesc) { this.reportDesc = reportDesc; }
		public String getEntityFlg() { return entityFlg; }
		public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }
		public String getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(String modifyFlg) { this.modifyFlg = modifyFlg; }
		public String getDelFlg() { return delFlg; }
		public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
		public Date getReportResubdate() { return reportResubdate; }
		public void setReportResubdate(Date reportResubdate) { this.reportResubdate = reportResubdate; }
	}


	class Q_LARADVSummaryRowMapper implements RowMapper<Q_LARADV_Summary_Entity> {
		@Override
		public Q_LARADV_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Q_LARADV_Summary_Entity obj = new Q_LARADV_Summary_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setGroupName(rs.getString("GROUP_NAME"));
			obj.setCustomerGroupName(rs.getString("CUSTOMER_GROUP_NAME"));
			obj.setSectorType(rs.getString("SECTOR_TYPE"));
			obj.setFacilityType(rs.getString("FACILITY_TYPE"));
			obj.setOriginalAmount(rs.getBigDecimal("ORIGINAL_AMOUNT"));
			obj.setUtilisationOutstandingBalance(rs.getBigDecimal("UTILISATION_OUTSTANDING_BALANCE"));
			obj.setEffectiveDate(rs.getDate("EFFECTIVE_DATE"));
			obj.setRepaymentPeriod(rs.getString("REPAYMENT_PERIOD"));
			obj.setPerformanceStatus(rs.getString("PERFORMANCE_STATUS"));
			obj.setSecurityDetails(rs.getString("SECURITY_DETAILS"));
			obj.setBoardApproval(rs.getString("BOARD_APPROVAL"));
			obj.setInterestRate(rs.getBigDecimal("INTEREST_RATE"));
			obj.setOutstandingBalancePercent(rs.getBigDecimal("OUTSTANDING_BALANCE_PERCENT"));
			obj.setLimitPercent(rs.getBigDecimal("LIMIT_PERCENT"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReportCode(rs.getString("REPORT_CODE"));
			obj.setReportDesc(rs.getString("REPORT_DESC"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			return obj;
		}
	}

	class Q_LARADVDetailRowMapper implements RowMapper<Q_LARADV_Detail_Entity> {
		@Override
		public Q_LARADV_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Q_LARADV_Detail_Entity obj = new Q_LARADV_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setGroupName(rs.getString("GROUP_NAME"));
			obj.setCustomerGroupName(rs.getString("CUSTOMER_GROUP_NAME"));
			obj.setSectorType(rs.getString("SECTOR_TYPE"));
			obj.setFacilityType(rs.getString("FACILITY_TYPE"));
			obj.setOriginalAmount(rs.getBigDecimal("ORIGINAL_AMOUNT"));
			obj.setUtilisationOutstandingBalance(rs.getBigDecimal("UTILISATION_OUTSTANDING_BALANCE"));
			obj.setEffectiveDate(rs.getDate("EFFECTIVE_DATE"));
			obj.setRepaymentPeriod(rs.getString("REPAYMENT_PERIOD"));
			obj.setPerformanceStatus(rs.getString("PERFORMANCE_STATUS"));
			obj.setSecurityDetails(rs.getString("SECURITY_DETAILS"));
			obj.setBoardApproval(rs.getString("BOARD_APPROVAL"));
			obj.setInterestRate(rs.getBigDecimal("INTEREST_RATE"));
			obj.setOutstandingBalancePercent(rs.getBigDecimal("OUTSTANDING_BALANCE_PERCENT"));
			obj.setLimitPercent(rs.getBigDecimal("LIMIT_PERCENT"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReportCode(rs.getString("REPORT_CODE"));
			obj.setReportDesc(rs.getString("REPORT_DESC"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			return obj;
		}
	}

	class Q_LARADVArchivalSummaryRowMapper implements RowMapper<Q_LARADV_Archival_Summary_Entity> {
		@Override
		public Q_LARADV_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Q_LARADV_Archival_Summary_Entity obj = new Q_LARADV_Archival_Summary_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setGroupName(rs.getString("GROUP_NAME"));
			obj.setCustomerGroupName(rs.getString("CUSTOMER_GROUP_NAME"));
			obj.setSectorType(rs.getString("SECTOR_TYPE"));
			obj.setFacilityType(rs.getString("FACILITY_TYPE"));
			obj.setOriginalAmount(rs.getBigDecimal("ORIGINAL_AMOUNT"));
			obj.setUtilisationOutstandingBalance(rs.getBigDecimal("UTILISATION_OUTSTANDING_BALANCE"));
			obj.setEffectiveDate(rs.getDate("EFFECTIVE_DATE"));
			obj.setRepaymentPeriod(rs.getString("REPAYMENT_PERIOD"));
			obj.setPerformanceStatus(rs.getString("PERFORMANCE_STATUS"));
			obj.setSecurityDetails(rs.getString("SECURITY_DETAILS"));
			obj.setBoardApproval(rs.getString("BOARD_APPROVAL"));
			obj.setInterestRate(rs.getBigDecimal("INTEREST_RATE"));
			obj.setOutstandingBalancePercent(rs.getBigDecimal("OUTSTANDING_BALANCE_PERCENT"));
			obj.setLimitPercent(rs.getBigDecimal("LIMIT_PERCENT"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReportCode(rs.getString("REPORT_CODE"));
			obj.setReportDesc(rs.getString("REPORT_DESC"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			return obj;
		}
	}

	class Q_LARADVArchivalDetailRowMapper implements RowMapper<Q_LARADV_Archival_Detail_Entity> {
		@Override
		public Q_LARADV_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Q_LARADV_Archival_Detail_Entity obj = new Q_LARADV_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setGroupName(rs.getString("GROUP_NAME"));
			obj.setCustomerGroupName(rs.getString("CUSTOMER_GROUP_NAME"));
			obj.setSectorType(rs.getString("SECTOR_TYPE"));
			obj.setFacilityType(rs.getString("FACILITY_TYPE"));
			obj.setOriginalAmount(rs.getBigDecimal("ORIGINAL_AMOUNT"));
			obj.setUtilisationOutstandingBalance(rs.getBigDecimal("UTILISATION_OUTSTANDING_BALANCE"));
			obj.setEffectiveDate(rs.getDate("EFFECTIVE_DATE"));
			obj.setRepaymentPeriod(rs.getString("REPAYMENT_PERIOD"));
			obj.setPerformanceStatus(rs.getString("PERFORMANCE_STATUS"));
			obj.setSecurityDetails(rs.getString("SECURITY_DETAILS"));
			obj.setBoardApproval(rs.getString("BOARD_APPROVAL"));
			obj.setInterestRate(rs.getBigDecimal("INTEREST_RATE"));
			obj.setOutstandingBalancePercent(rs.getBigDecimal("OUTSTANDING_BALANCE_PERCENT"));
			obj.setLimitPercent(rs.getBigDecimal("LIMIT_PERCENT"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReportCode(rs.getString("REPORT_CODE"));
			obj.setReportDesc(rs.getString("REPORT_DESC"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			return obj;
		}
	}

	class Q_LARADVResubSummaryRowMapper implements RowMapper<Q_LARADV_Resub_Summary_Entity> {
		@Override
		public Q_LARADV_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Q_LARADV_Resub_Summary_Entity obj = new Q_LARADV_Resub_Summary_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setGroupName(rs.getString("GROUP_NAME"));
			obj.setCustomerGroupName(rs.getString("CUSTOMER_GROUP_NAME"));
			obj.setSectorType(rs.getString("SECTOR_TYPE"));
			obj.setFacilityType(rs.getString("FACILITY_TYPE"));
			obj.setOriginalAmount(rs.getBigDecimal("ORIGINAL_AMOUNT"));
			obj.setUtilisationOutstandingBalance(rs.getBigDecimal("UTILISATION_OUTSTANDING_BALANCE"));
			obj.setEffectiveDate(rs.getDate("EFFECTIVE_DATE"));
			obj.setRepaymentPeriod(rs.getString("REPAYMENT_PERIOD"));
			obj.setPerformanceStatus(rs.getString("PERFORMANCE_STATUS"));
			obj.setSecurityDetails(rs.getString("SECURITY_DETAILS"));
			obj.setBoardApproval(rs.getString("BOARD_APPROVAL"));
			obj.setInterestRate(rs.getBigDecimal("INTEREST_RATE"));
			obj.setOutstandingBalancePercent(rs.getBigDecimal("OUTSTANDING_BALANCE_PERCENT"));
			obj.setLimitPercent(rs.getBigDecimal("LIMIT_PERCENT"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReportCode(rs.getString("REPORT_CODE"));
			obj.setReportDesc(rs.getString("REPORT_DESC"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			return obj;
		}
	}

	class Q_LARADVResubDetailRowMapper implements RowMapper<Q_LARADV_Resub_Detail_Entity> {
		@Override
		public Q_LARADV_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Q_LARADV_Resub_Detail_Entity obj = new Q_LARADV_Resub_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setGroupName(rs.getString("GROUP_NAME"));
			obj.setCustomerGroupName(rs.getString("CUSTOMER_GROUP_NAME"));
			obj.setSectorType(rs.getString("SECTOR_TYPE"));
			obj.setFacilityType(rs.getString("FACILITY_TYPE"));
			obj.setOriginalAmount(rs.getBigDecimal("ORIGINAL_AMOUNT"));
			obj.setUtilisationOutstandingBalance(rs.getBigDecimal("UTILISATION_OUTSTANDING_BALANCE"));
			obj.setEffectiveDate(rs.getDate("EFFECTIVE_DATE"));
			obj.setRepaymentPeriod(rs.getString("REPAYMENT_PERIOD"));
			obj.setPerformanceStatus(rs.getString("PERFORMANCE_STATUS"));
			obj.setSecurityDetails(rs.getString("SECURITY_DETAILS"));
			obj.setBoardApproval(rs.getString("BOARD_APPROVAL"));
			obj.setInterestRate(rs.getBigDecimal("INTEREST_RATE"));
			obj.setOutstandingBalancePercent(rs.getBigDecimal("OUTSTANDING_BALANCE_PERCENT"));
			obj.setLimitPercent(rs.getBigDecimal("LIMIT_PERCENT"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportFrequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReportCode(rs.getString("REPORT_CODE"));
			obj.setReportDesc(rs.getString("REPORT_DESC"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG"));
			obj.setModifyFlg(rs.getString("MODIFY_FLG"));
			obj.setDelFlg(rs.getString("DEL_FLG"));
			obj.setReportResubdate(rs.getDate("REPORT_RESUBDATE"));
			return obj;
		}
	}

}
