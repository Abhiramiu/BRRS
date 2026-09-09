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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service
public class BRRS_UFCE_CALCULATION_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_UFCE_CALCULATION_ReportService.class);

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

	// =====================================================
	// DATE HANDLING HELPER METHODS
	// =====================================================

	/**
	 * Parses date strings in multiple formats gracefully Supports: dd/MM/yyyy,
	 * dd-MMM-yyyy
	 */
	private Date parseDate(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return null;
		}
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(dateStr.trim());
		} catch (Exception e) {
			try {
				return new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(dateStr.trim());
			} catch (Exception ex) {
				logger.error("Failed to parse date: {}", dateStr);
				return null;
			}
		}
	}

	// =====================================================
	// HELPER METHODS
	// =====================================================

	private BigDecimal nvl(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}

	/**
	 * Generic cell value setter supporting multiple types
	 */
	private void setCellValue(Row row, int column, Object value, CellStyle style) {
		Cell cell = row.createCell(column);
		if (value == null) {
			cell.setCellValue("");
			cell.setCellStyle(style);
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
			cell.setCellStyle(style);
		} else if (value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;
			if (bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0) {
				cell.setCellValue(bd.longValue());
			} else {
				cell.setCellValue(bd.doubleValue());
			}
			cell.setCellStyle(style);
		} else if (value instanceof Date) {
			cell.setCellValue((Date) value);
			cell.setCellStyle(style);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
			cell.setCellStyle(style);
		} else if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
			cell.setCellStyle(style);
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
			cell.setCellStyle(style);
		} else {
			cell.setCellValue(value.toString());
			cell.setCellStyle(style);
		}
	}

	/**
	 * Creates styled total header style for Excel
	 */
	private CellStyle createTotalHeaderStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.RIGHT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	/**
	 * Creates styled total number style for Excel
	 */
	private CellStyle createTotalNumberStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);

		DataFormat dataFormat = workbook.createDataFormat();
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setDataFormat(dataFormat.getFormat("#,##0.00"));
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	// =====================================================
	// SECTION 1: BRRS_UFCE_CALCULATION_SUMMARYTABLE - Repository Methods
	// =====================================================

	public List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY> getCalculationSummaryDataByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BRRS_UFCE_CALCULATION_SUMMARYTABLE_ROWMAPPER());
	}

	public BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY findCalculationSummaryByReportDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate },
					new BRRS_UFCE_CALCULATION_SUMMARYTABLE_ROWMAPPER());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY> getCalculationSummaryDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ? AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_SUMMARYTABLE_ROWMAPPER());
	}

	// =====================================================
	// SECTION 2: BRRS_UFCE_CALCULATION_DETAILTABLE - Repository Methods
	// =====================================================

	public List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> getCalculationDetailDataByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BRRS_UFCE_CALCULATION_DETAILTABLE_ROWMAPPER());
	}

	public BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY findCalculationDetailByReportDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate },
					new BRRS_UFCE_CALCULATION_DETAILTABLE_ROWMAPPER());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> getCalculationDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ? AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_DETAILTABLE_ROWMAPPER());
	}

	// =====================================================
	// SECTION 3: BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARY - Repository Methods
	// =====================================================

	public List<Object[]> getCalculationArchivalSummaryList() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY> getCalculationArchivalSummaryDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ROWMAPPER());
	}

	public BigDecimal findMaxCalculationArchivalSummaryVersion(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
		return result != null ? result : BigDecimal.ZERO;
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY> getAllArchivalWithVersion() {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ROWMAPPER());
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY> getAllResubWithVersion() {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ROWMAPPER());
	}

	// =====================================================
	// SECTION 4: BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE - Repository Methods
	// =====================================================

	public List<Object[]> getCalculationArchivalDetailList() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> getCalculationArchivalDetailDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ROWMAPPER());
	}

	// =====================================================
	// SECTION 5: BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL - Repository Methods
	// =====================================================

	public List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> getManualEntrySummaryDataByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate },
				new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	public BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY findManualEntrySummaryByReportDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate },
					new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ROWMAPPER());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> getManualEntrySummaryDataByDateAndVersion(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ? AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	// =====================================================
	// SECTION 6: BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL - Repository
	// Methods
	// =====================================================

	public List<Object[]> getManualEntryArchivalSummaryList() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> getManualEntryArchivalSummaryDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	public BigDecimal findMaxManualEntryArchivalSummaryVersion(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
		return result != null ? result : BigDecimal.ZERO;
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> getAllManualArchivalWithVersion() {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL WHERE REPORT_VERSION IS NOT NULL GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> getAllManualResubWithVersion() {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL WHERE REPORT_VERSION IS NOT NULL GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	// =====================================================
	// SECTION 7: RESUB REPOSITORY METHODS
	// =====================================================

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY> getResubCalculationSummaryDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ROWMAPPER());
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> getResubCalculationDetailDataByDate(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ROWMAPPER());
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> getResubManualEntrySummaryDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	public BigDecimal findMaxResubCalculationVersion(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
		return result != null ? result : BigDecimal.ZERO;
	}

	public BigDecimal findMaxResubManualEntryVersion(Date reportDate) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
		return result != null ? result : BigDecimal.ZERO;
	}

	public BigDecimal getLatestManualEntryVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
			BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return result != null ? result : BigDecimal.ZERO;
		} catch (Exception e) {
			logger.warn("Error getting latest manual entry version: {}", e.getMessage());
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal getLatestCalculationVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
			BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return result != null ? result : BigDecimal.ZERO;
		} catch (Exception e) {
			logger.warn("Error getting latest calculation version: {}", e.getMessage());
			return BigDecimal.ZERO;
		}
	}

	/**
	 * Gets next archival version with improved error handling
	 */
	private BigDecimal getNextCalculationArchivalVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			BigDecimal maxVersion = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			if (maxVersion == null) {
				return BigDecimal.ONE;
			}
			return maxVersion.add(BigDecimal.ONE);
		} catch (Exception e) {
			logger.warn("Error getting next calculation archival version: {}", e.getMessage());
			return BigDecimal.ONE;
		}
	}

	/**
	 * Gets next manual entry archival version with improved error handling
	 */
	private BigDecimal getNextManualEntryArchivalVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			BigDecimal maxVersion = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			if (maxVersion == null) {
				return BigDecimal.ONE;
			}
			return maxVersion.add(BigDecimal.ONE);
		} catch (Exception e) {
			logger.warn("Error getting next manual entry archival version: {}", e.getMessage());
			return BigDecimal.ONE;
		}
	}

	public BigDecimal findMaxArchivalVersion(Date reportDate) {
		return getNextCalculationArchivalVersion(reportDate).subtract(BigDecimal.ONE);
	}

	public BigDecimal findMaxManualArchivalVersion(Date reportDate) {
		return getNextManualEntryArchivalVersion(reportDate).subtract(BigDecimal.ONE);
	}

	// =====================================================
	// SECTION 8: DELETE METHODS
	// =====================================================

	@Transactional
	public void softDeleteManualEntrySummary(Date reportDate) {
		String sql = "UPDATE BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL SET DEL_FLG = 'Y', MODIFY_FLG = 'Y' WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		jdbcTemplate.update(sql, reportDate);
	}

	@Transactional
	public void softDeleteCalculationDetail(Date reportDate) {
		String sql = "UPDATE BRRS_UFCE_CALCULATION_DETAILTABLE SET DEL_FLG = 'Y', MODIFY_FLG = 'Y' WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		jdbcTemplate.update(sql, reportDate);
	}

	// =====================================================
	// SECTION 9: VIEW METHODS
	// =====================================================

	public ModelAndView getUFCEView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		logger.info("User Id: {}", userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);

		try {
			Date dt = parseDate(todate);

			if ("detail".equalsIgnoreCase(dtltype)) {

				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					List<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> detailData = getCalculationArchivalDetailDataByDate(
							dt, version);
					mv.addObject("reportdetail", detailData);
					mv.addObject("displaymode", "archivalDetail");
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					List<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> detailData = getResubCalculationDetailDataByDate(
							dt, version);
					mv.addObject("reportdetail", detailData);
					mv.addObject("displaymode", "resubDetail");
				} else {
					List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> detailData;
					if (version != null) {
						detailData = getCalculationDetailDataByDateAndVersion(dt, version);
						mv.addObject("displaymode", "normalDetailWithVersion");
					} else {
						detailData = getCalculationDetailDataByDate(dt);
						mv.addObject("displaymode", "detail");
					}
					mv.addObject("reportdetail", detailData);
				}
			} else {

				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
					List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> summaryData = getManualEntryArchivalSummaryDataByDate(
							dt, version);
					List<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> detailData = getCalculationArchivalDetailDataByDate(
							dt, version);
					mv.addObject("reportsummary", summaryData);
					mv.addObject("reportdetail", detailData);
					mv.addObject("displaymode", "archivalSummary");
				} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> summaryData = getResubManualEntrySummaryDataByDate(
							dt, version);
					List<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> detailData = getResubCalculationDetailDataByDate(
							dt, version);
					mv.addObject("reportsummary", summaryData);
					mv.addObject("reportdetail", detailData);
					mv.addObject("displaymode", "resubSummary");
				} else {
					List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> summaryData;
					List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> detailData;
					if (version != null) {
						summaryData = getManualEntrySummaryDataByDateAndVersion(dt, version);
						detailData = getCalculationDetailDataByDateAndVersion(dt, version);
						mv.addObject("displaymode", "normalSummaryWithVersion");
					} else {
						summaryData = getManualEntrySummaryDataByDate(dt);
						detailData = getCalculationDetailDataByDate(dt);
						mv.addObject("displaymode", "summary");
					}
					mv.addObject("reportsummary", summaryData);
					mv.addObject("reportdetail", detailData);
				}
			}

			mv.addObject("report_date", dt != null ? dateformat.format(dt) : todate);

		} catch (Exception e) {
			logger.error("Error in getUFCEView: {}", e.getMessage(), e);
		}

		mv.setViewName("BRRS/UFCE_CALCULATION");
		return mv;
	}

	public ModelAndView getUFCEModifyView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);

		try {
			Date dt = parseDate(todate);

			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> archivalSummary = getManualEntryArchivalSummaryDataByDate(
						dt, version);
				mv.addObject("reportsummary", archivalSummary);
				mv.addObject("displaymode", "archivalModify");
				mv.addObject("isArchival", true);
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> resubSummary = getResubManualEntrySummaryDataByDate(
						dt, version);
				mv.addObject("reportsummary", resubSummary);
				mv.addObject("displaymode", "resubModify");
				mv.addObject("isResub", true);
			} else {
				List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> summaryData = getManualEntrySummaryDataByDate(
						dt);
				mv.addObject("reportsummary", summaryData);
				mv.addObject("displaymode", "modify");
				mv.addObject("isNormal", true);
			}

			mv.addObject("report_date", dt != null ? dateformat.format(dt) : todate);
			mv.addObject("menu", reportId);
			mv.addObject("reportId", reportId);
			mv.addObject("type", type);
			mv.addObject("version", version);

		} catch (Exception e) {
			logger.error("Error in getUFCEModifyView: {}", e.getMessage(), e);
		}

		mv.setViewName("BRRS/UFCE_CALCULATION");
		return mv;
	}

	// =====================================================
	// SECTION 10: UPDATE SUMMARY REPORT - MANUAL TABLE
	// =====================================================

	@Transactional
	public void updateSummaryReport(BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY updatedEntity) {
		logger.info("Came to UFCE Summary Update");

		if (updatedEntity == null || updatedEntity.getReport_date() == null) {
			throw new IllegalArgumentException("Updated entity and report date cannot be null");
		}

		BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY existingSummary = findManualEntrySummaryByReportDate(
				updatedEntity.getReport_date());

		if (existingSummary == null) {
			logger.info("No existing record found for date: {}. Creating new record.", updatedEntity.getReport_date());
			// Insert new record instead of throwing exception
			insertNewSummaryRecord(updatedEntity);
			logger.info("New record created successfully for date: {}", updatedEntity.getReport_date());
			return;
		}

		// Continue with existing update logic...
		BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY oldcopy = new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY();
		BeanUtils.copyProperties(existingSummary, oldcopy);

		try {
			// Validate non-negative values
			validateNonNegativeValues(updatedEntity);

			// Auto-calculate totals (R22) = SUM(R11 to R21)
			BigDecimal totalAdvance = calculateTotalAdvance(updatedEntity);
			BigDecimal totalProvision = calculateTotalProvision(updatedEntity);

			updatedEntity.setR22_total_adv(totalAdvance);
			updatedEntity.setR22_provision(totalProvision);

			// Update using correct column names from SQL
			String updateSummarySQL = "UPDATE BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL SET "
					+ "R11_SUBSIDIARY = ?, R11_TOTAL_ADV = ?, R11_PROVISION = ?, "
					+ "R12_SUBSIDIARY = ?, R12_TOTAL_ADV = ?, R12_PROVISION = ?, "
					+ "R13_SUBSIDIARY = ?, R13_TOTAL_ADV = ?, R13_PROVISION = ?, "
					+ "R14_SUBSIDIARY = ?, R14_TOTAL_ADV = ?, R14_PROVISION = ?, "
					+ "R15_SUBSIDIARY = ?, R15_TOTAL_ADV = ?, R15_PROVISION = ?, "
					+ "R16_SUBSIDIARY = ?, R16_TOTAL_ADV = ?, R16_PROVISION = ?, "
					+ "R17_SUBSIDIARY = ?, R17_TOTAL_ADV = ?, R17_PROVISION = ?, "
					+ "R18_SUBSIDIARY = ?, R18_TOTAL_ADV = ?, R18_PROVISION = ?, "
					+ "R19_SUBSIDIARY = ?, R19_TOTAL_ADV = ?, R19_PROVISION = ?, "
					+ "R20_SUBSIDIARY = ?, R20_TOTAL_ADV = ?, R20_PROVISION = ?, "
					+ "R21_SUBSIDIARY = ?, R21_TOTAL_ADV = ?, R21_PROVISION = ?, "
					+ "R22_TOTAL_ADV = ?, R22_PROVISION = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(updateSummarySQL, updatedEntity.getR11_subsidiary(), updatedEntity.getR11_total_adv(),
					updatedEntity.getR11_provision(), updatedEntity.getR12_subsidiary(),
					updatedEntity.getR12_total_adv(), updatedEntity.getR12_provision(),
					updatedEntity.getR13_subsidiary(), updatedEntity.getR13_total_adv(),
					updatedEntity.getR13_provision(), updatedEntity.getR14_subsidiary(),
					updatedEntity.getR14_total_adv(), updatedEntity.getR14_provision(),
					updatedEntity.getR15_subsidiary(), updatedEntity.getR15_total_adv(),
					updatedEntity.getR15_provision(), updatedEntity.getR16_subsidiary(),
					updatedEntity.getR16_total_adv(), updatedEntity.getR16_provision(),
					updatedEntity.getR17_subsidiary(), updatedEntity.getR17_total_adv(),
					updatedEntity.getR17_provision(), updatedEntity.getR18_subsidiary(),
					updatedEntity.getR18_total_adv(), updatedEntity.getR18_provision(),
					updatedEntity.getR19_subsidiary(), updatedEntity.getR19_total_adv(),
					updatedEntity.getR19_provision(), updatedEntity.getR20_subsidiary(),
					updatedEntity.getR20_total_adv(), updatedEntity.getR20_provision(),
					updatedEntity.getR21_subsidiary(), updatedEntity.getR21_total_adv(),
					updatedEntity.getR21_provision(), totalAdvance, totalProvision, updatedEntity.getReport_date(),
					existingSummary.getReport_version());

			if (auditService != null) {
				String changes = auditService.getChanges(oldcopy, updatedEntity);
				if (!changes.isEmpty()) {
					auditService.compareEntitiesmanual(oldcopy, updatedEntity,
							updatedEntity.getReport_date().toString(), "UFCE Calculation Summary Screen",
							"BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL");
				}
			}

			logger.info("UFCE Summary Update Completed Successfully for date: {}", updatedEntity.getReport_date());

		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Error while updating UFCE Summary fields", e);
			throw new RuntimeException("Error while updating UFCE Summary fields: " + e.getMessage(), e);
		}
	}

	@Transactional
	public void insertNewSummaryRecord(BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY entity) {
		logger.info("Inserting new summary record for date: {}", entity.getReport_date());

		try {
			// Calculate totals
			BigDecimal totalAdvance = calculateTotalAdvance(entity);
			BigDecimal totalProvision = calculateTotalProvision(entity);

			// Get next version number
			BigDecimal maxVersion = getLatestManualEntryVersion(entity.getReport_date());
			BigDecimal newVersion = (maxVersion == null || maxVersion.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ONE
					: maxVersion.add(BigDecimal.ONE);

			// Use a simpler insert with direct column mapping
			String insertSQL = "INSERT INTO BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
					+ "R11_SUBSIDIARY, R11_TOTAL_ADV, R11_PROVISION, "
					+ "R12_SUBSIDIARY, R12_TOTAL_ADV, R12_PROVISION, "
					+ "R13_SUBSIDIARY, R13_TOTAL_ADV, R13_PROVISION, "
					+ "R14_SUBSIDIARY, R14_TOTAL_ADV, R14_PROVISION, "
					+ "R15_SUBSIDIARY, R15_TOTAL_ADV, R15_PROVISION, "
					+ "R16_SUBSIDIARY, R16_TOTAL_ADV, R16_PROVISION, "
					+ "R17_SUBSIDIARY, R17_TOTAL_ADV, R17_PROVISION, "
					+ "R18_SUBSIDIARY, R18_TOTAL_ADV, R18_PROVISION, "
					+ "R19_SUBSIDIARY, R19_TOTAL_ADV, R19_PROVISION, "
					+ "R20_SUBSIDIARY, R20_TOTAL_ADV, R20_PROVISION, "
					+ "R21_SUBSIDIARY, R21_TOTAL_ADV, R21_PROVISION, " + "R22_TOTAL_ADV, R22_PROVISION, "
					+ "DEL_FLG, ENTITY_FLG, MODIFY_FLG) " + "VALUES (?, ?, ?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, "
					+ "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, "
					+ "?, ?, ?, " + "?, ?, " + "?, ?, ?)";

			Object[] params = new Object[] {
					// 1-5: Metadata
					entity.getReport_date(), newVersion,
					entity.getReport_frequency() != null ? entity.getReport_frequency() : "Monthly",
					entity.getReport_code() != null ? entity.getReport_code() : "UFCE_CALC",
					entity.getReport_desc() != null ? entity.getReport_desc() : "UFCE Calculation Summary",
					// 6-8: R11
					entity.getR11_subsidiary(), nvl(entity.getR11_total_adv()), nvl(entity.getR11_provision()),
					// 9-11: R12
					entity.getR12_subsidiary(), nvl(entity.getR12_total_adv()), nvl(entity.getR12_provision()),
					// 12-14: R13
					entity.getR13_subsidiary(), nvl(entity.getR13_total_adv()), nvl(entity.getR13_provision()),
					// 15-17: R14
					entity.getR14_subsidiary(), nvl(entity.getR14_total_adv()), nvl(entity.getR14_provision()),
					// 18-20: R15
					entity.getR15_subsidiary(), nvl(entity.getR15_total_adv()), nvl(entity.getR15_provision()),
					// 21-23: R16
					entity.getR16_subsidiary(), nvl(entity.getR16_total_adv()), nvl(entity.getR16_provision()),
					// 24-26: R17
					entity.getR17_subsidiary(), nvl(entity.getR17_total_adv()), nvl(entity.getR17_provision()),
					// 27-29: R18
					entity.getR18_subsidiary(), nvl(entity.getR18_total_adv()), nvl(entity.getR18_provision()),
					// 30-32: R19
					entity.getR19_subsidiary(), nvl(entity.getR19_total_adv()), nvl(entity.getR19_provision()),
					// 33-35: R20
					entity.getR20_subsidiary(), nvl(entity.getR20_total_adv()), nvl(entity.getR20_provision()),
					// 36-38: R21
					entity.getR21_subsidiary(), nvl(entity.getR21_total_adv()), nvl(entity.getR21_provision()),
					// 39-40: R22 totals
					nvl(entity.getR22_total_adv()), nvl(entity.getR22_provision()),
					// 41-43: Flags
					"N", "Y", "Y" };

			// Log parameter count for debugging
			logger.debug("Number of parameters: {}", params.length);
			logger.debug("Number of placeholders: 43");

			int result = jdbcTemplate.update(insertSQL, params);

			logger.info("New summary record inserted successfully for date: {} with version: {}, rows affected: {}",
					entity.getReport_date(), newVersion, result);

		} catch (Exception e) {
			logger.error("Error inserting new summary record for date: {}", entity.getReport_date(), e);
			throw new RuntimeException("Error inserting new summary record: " + e.getMessage(), e);
		}
	}

	// =====================================================
	// SECTION 11: UPDATE DETAIL REPORT
	// =====================================================

	@Transactional
	public void updateDetailReport(BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY updatedEntity) {
		logger.info("Came to UFCE Detail Update");

		if (updatedEntity == null || updatedEntity.getReport_date() == null) {
			throw new IllegalArgumentException("Updated entity and report date cannot be null");
		}

		BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY existingDetail = findCalculationDetailByReportDate(
				updatedEntity.getReport_date());

		if (existingDetail == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date());
		}

		BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY oldcopy = new BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY();
		BeanUtils.copyProperties(existingDetail, oldcopy);

		try {
			// Update using correct column names from SQL
			String updateDetailSQL = "UPDATE BRRS_UFCE_CALCULATION_DETAILTABLE SET "
					+ "R6_VALUE = ?, R7_VALUE = ?, R8_VALUE = ?, " + "R27_VALUE = ?, R28_VALUE = ?, R29_VALUE = ?, "
					+ "R30_VALUE = ?, R31_VALUE = ?, R32_VALUE = ?, R33_VALUE = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(updateDetailSQL, updatedEntity.getR6_value(), updatedEntity.getR7_value(),
					updatedEntity.getR8_value(), updatedEntity.getR27_value(), updatedEntity.getR28_value(),
					updatedEntity.getR29_value(), updatedEntity.getR30_value(), updatedEntity.getR31_value(),
					updatedEntity.getR32_value(), updatedEntity.getR33_value(), updatedEntity.getReport_date(),
					existingDetail.getReport_version());

			if (auditService != null) {
				String changes = auditService.getChanges(oldcopy, updatedEntity);
				if (!changes.isEmpty()) {
					auditService.compareEntitiesmanual(oldcopy, updatedEntity,
							updatedEntity.getReport_date().toString(), "UFCE Calculation Detail Screen",
							"BRRS_UFCE_CALCULATION_DETAILTABLE");
				}
			}

			logger.info("UFCE Detail Update Completed Successfully for date: {}", updatedEntity.getReport_date());

		} catch (Exception e) {
			logger.error("Error while updating UFCE Detail fields", e);
			throw new RuntimeException("Error while updating UFCE Detail fields: " + e.getMessage(), e);
		}
	}

	// =====================================================
	// SECTION 12: RESUB UPDATE - CALCULATION TABLE
	// =====================================================

	@Transactional
	public void updateResubCalculationReport(BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY updatedEntity) {
		logger.info("Came to UFCE Calculation Resub Update");

		Date reportDate = updatedEntity.getReport_date();
		BigDecimal maxVersion = findMaxResubCalculationVersion(reportDate);

		if (maxVersion == null || maxVersion.compareTo(BigDecimal.ZERO) == 0) {
			throw new RuntimeException("No archival record found for REPORT_DATE : " + reportDate);
		}

		BigDecimal newVersion = getNextCalculationArchivalVersion(reportDate);
		Date now = new Date();

		try {
			// Get max SNO for Summary table
			BigDecimal maxSummarySno = jdbcTemplate.queryForObject(
					"SELECT COALESCE(MAX(SNO), 0) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE", BigDecimal.class);

			// Insert into ARCHIVAL SUMMARY
			String insertArchivalSummarySQL = "INSERT INTO BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE ("
					+ "SNO, REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, " + "R5_LABEL, R5_RS_IN_LACS, "
					+ "R6_DESCRIPTION, R6_VALUE, R7_DESCRIPTION, R7_VALUE, R8_DESCRIPTION, R8_VALUE, "
					+ "R25_LABEL, R25_RS_IN_LACS, " + "R27_DESCRIPTION, R27_VALUE, R28_DESCRIPTION, R28_VALUE, "
					+ "R29_DESCRIPTION, R29_VALUE, R30_DESCRIPTION, R30_VALUE, "
					+ "R31_DESCRIPTION, R31_VALUE, R32_DESCRIPTION, R32_VALUE, " + "R33_DESCRIPTION, R33_VALUE, "
					+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "SELECT ?, ?, ?, ?, " + "R5_LABEL, R5_RS_IN_LACS, "
					+ "R6_DESCRIPTION, ?, R7_DESCRIPTION, ?, R8_DESCRIPTION, ?, " + "R25_LABEL, R25_RS_IN_LACS, "
					+ "R27_DESCRIPTION, R27_VALUE, R28_DESCRIPTION, R28_VALUE, "
					+ "R29_DESCRIPTION, ?, R30_DESCRIPTION, R30_VALUE, "
					+ "R31_DESCRIPTION, R31_VALUE, R32_DESCRIPTION, ?, " + "R33_DESCRIPTION, ?, "
					+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG " + "FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(insertArchivalSummarySQL, maxSummarySno.add(BigDecimal.ONE), reportDate, newVersion,
					now, updatedEntity.getR6_value(), updatedEntity.getR7_value(), updatedEntity.getR8_value(),
					updatedEntity.getR29_value(), updatedEntity.getR32_value(), updatedEntity.getR33_value(),
					reportDate, maxVersion);

			// Clone DETAIL records
			String cloneDetailSQL = "INSERT INTO BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE ("
					+ "SNO, REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, " + "R5_LABEL, R5_RS_IN_LACS, "
					+ "R6_DESCRIPTION, R6_VALUE, R7_DESCRIPTION, R7_VALUE, R8_DESCRIPTION, R8_VALUE, "
					+ "R25_LABEL, R25_RS_IN_LACS, " + "R27_DESCRIPTION, R27_VALUE, R28_DESCRIPTION, R28_VALUE, "
					+ "R29_DESCRIPTION, R29_VALUE, R30_DESCRIPTION, R30_VALUE, "
					+ "R31_DESCRIPTION, R31_VALUE, R32_DESCRIPTION, R32_VALUE, " + "R33_DESCRIPTION, R33_VALUE, "
					+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE) + ROWNUM, "
					+ "?, ?, ?, " + "R5_LABEL, R5_RS_IN_LACS, "
					+ "R6_DESCRIPTION, ?, R7_DESCRIPTION, ?, R8_DESCRIPTION, ?, " + "R25_LABEL, R25_RS_IN_LACS, "
					+ "R27_DESCRIPTION, R27_VALUE, R28_DESCRIPTION, R28_VALUE, "
					+ "R29_DESCRIPTION, ?, R30_DESCRIPTION, R30_VALUE, "
					+ "R31_DESCRIPTION, R31_VALUE, R32_DESCRIPTION, ?, " + "R33_DESCRIPTION, ?, "
					+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG " + "FROM BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(cloneDetailSQL, reportDate, newVersion, now, updatedEntity.getR6_value(),
					updatedEntity.getR7_value(), updatedEntity.getR8_value(), updatedEntity.getR29_value(),
					updatedEntity.getR32_value(), updatedEntity.getR33_value(), reportDate, maxVersion);

			// UPDATE the newly inserted summary records with modified values
			String updateSummarySQL = "UPDATE BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE SET "
					+ "R6_VALUE = ?, R7_VALUE = ?, R8_VALUE = ?, " + "R29_VALUE = ?, R32_VALUE = ?, R33_VALUE = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(updateSummarySQL, updatedEntity.getR6_value(), updatedEntity.getR7_value(),
					updatedEntity.getR8_value(), updatedEntity.getR29_value(), updatedEntity.getR32_value(),
					updatedEntity.getR33_value(), reportDate, newVersion);

			String updateDetailSQL = "UPDATE BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE SET "
					+ "R6_VALUE = ?, R7_VALUE = ?, R8_VALUE = ?, " + "R29_VALUE = ?, R32_VALUE = ?, R33_VALUE = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(updateDetailSQL, updatedEntity.getR6_value(), updatedEntity.getR7_value(),
					updatedEntity.getR8_value(), updatedEntity.getR29_value(), updatedEntity.getR32_value(),
					updatedEntity.getR33_value(), reportDate, newVersion);

			logger.info("UFCE Calculation Resub Version Process Completed : {}", newVersion);

		} catch (Exception e) {
			logger.error("Error while updating UFCE Calculation Resub", e);
			throw new RuntimeException("Error while updating UFCE Calculation Resub: " + e.getMessage(), e);
		}
	}

	// =====================================================
	// SECTION 13: RESUB UPDATE - MANUAL ENTRY TABLE
	// =====================================================

	@Transactional
	public void updateResubManualEntryReport(BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY updatedEntity) {
		logger.info("Came to UFCE Manual Entry Resub Update");

		Date reportDate = updatedEntity.getReport_date();
		BigDecimal maxVersion = findMaxResubManualEntryVersion(reportDate);

		if (maxVersion == null || maxVersion.compareTo(BigDecimal.ZERO) == 0) {
			throw new RuntimeException("No archival record found for REPORT_DATE : " + reportDate);
		}

		BigDecimal newVersion = getNextManualEntryArchivalVersion(reportDate);
		Date now = new Date();

		try {
			// Auto-calculate totals
			BigDecimal totalAdvance = calculateTotalAdvanceManual(updatedEntity);
			BigDecimal totalProvision = calculateTotalProvisionManual(updatedEntity);

			// Get max SNO
			BigDecimal maxSno = jdbcTemplate.queryForObject(
					"SELECT COALESCE(MAX(SNO), 0) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL",
					BigDecimal.class);

			// Insert into ARCHIVAL SUMMARY
			String insertSQL = "INSERT INTO BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL ("
					+ "SNO, REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, "
					+ "R10_SERIAL_NO, R10_SUBSIDIARY, R10_TOTAL_ADV, R10_PROVISION, "
					+ "R11_SERIAL_NO, R11_SUBSIDIARY, R11_TOTAL_ADV, R11_PROVISION, "
					+ "R12_SERIAL_NO, R12_SUBSIDIARY, R12_TOTAL_ADV, R12_PROVISION, "
					+ "R13_SERIAL_NO, R13_SUBSIDIARY, R13_TOTAL_ADV, R13_PROVISION, "
					+ "R14_SERIAL_NO, R14_SUBSIDIARY, R14_TOTAL_ADV, R14_PROVISION, "
					+ "R15_SERIAL_NO, R15_SUBSIDIARY, R15_TOTAL_ADV, R15_PROVISION, "
					+ "R16_SERIAL_NO, R16_SUBSIDIARY, R16_TOTAL_ADV, R16_PROVISION, "
					+ "R17_SERIAL_NO, R17_SUBSIDIARY, R17_TOTAL_ADV, R17_PROVISION, "
					+ "R18_SERIAL_NO, R18_SUBSIDIARY, R18_TOTAL_ADV, R18_PROVISION, "
					+ "R19_SERIAL_NO, R19_SUBSIDIARY, R19_TOTAL_ADV, R19_PROVISION, "
					+ "R20_SERIAL_NO, R20_SUBSIDIARY, R20_TOTAL_ADV, R20_PROVISION, "
					+ "R21_SERIAL_NO, R21_SUBSIDIARY, R21_TOTAL_ADV, R21_PROVISION, " + "R22_TOTAL_ADV, R22_PROVISION, "
					+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "SELECT ?, ?, ?, ?, "
					+ "R10_SERIAL_NO, R10_SUBSIDIARY, ?, ?, " + "R11_SERIAL_NO, R11_SUBSIDIARY, ?, ?, "
					+ "R12_SERIAL_NO, R12_SUBSIDIARY, ?, ?, " + "R13_SERIAL_NO, R13_SUBSIDIARY, ?, ?, "
					+ "R14_SERIAL_NO, R14_SUBSIDIARY, ?, ?, " + "R15_SERIAL_NO, R15_SUBSIDIARY, ?, ?, "
					+ "R16_SERIAL_NO, R16_SUBSIDIARY, ?, ?, " + "R17_SERIAL_NO, R17_SUBSIDIARY, ?, ?, "
					+ "R18_SERIAL_NO, R18_SUBSIDIARY, ?, ?, " + "R19_SERIAL_NO, R19_SUBSIDIARY, ?, ?, "
					+ "R20_SERIAL_NO, R20_SUBSIDIARY, ?, ?, " + "R21_SERIAL_NO, R21_SUBSIDIARY, ?, ?, "
					+ "?, ?, ENTITY_FLG, MODIFY_FLG, DEL_FLG "
					+ "FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(insertSQL, maxSno.add(BigDecimal.ONE), reportDate, newVersion, now,
					updatedEntity.getR10_total_adv(), updatedEntity.getR10_provision(),
					updatedEntity.getR11_total_adv(), updatedEntity.getR11_provision(),
					updatedEntity.getR12_total_adv(), updatedEntity.getR12_provision(),
					updatedEntity.getR13_total_adv(), updatedEntity.getR13_provision(),
					updatedEntity.getR14_total_adv(), updatedEntity.getR14_provision(),
					updatedEntity.getR15_total_adv(), updatedEntity.getR15_provision(),
					updatedEntity.getR16_total_adv(), updatedEntity.getR16_provision(),
					updatedEntity.getR17_total_adv(), updatedEntity.getR17_provision(),
					updatedEntity.getR18_total_adv(), updatedEntity.getR18_provision(),
					updatedEntity.getR19_total_adv(), updatedEntity.getR19_provision(),
					updatedEntity.getR20_total_adv(), updatedEntity.getR20_provision(),
					updatedEntity.getR21_total_adv(), updatedEntity.getR21_provision(), totalAdvance, totalProvision,
					reportDate, maxVersion);

			// Update the newly inserted records
			String updateSQL = "UPDATE BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL SET "
					+ "R10_TOTAL_ADV = ?, R10_PROVISION = ?, " + "R11_TOTAL_ADV = ?, R11_PROVISION = ?, "
					+ "R12_TOTAL_ADV = ?, R12_PROVISION = ?, " + "R13_TOTAL_ADV = ?, R13_PROVISION = ?, "
					+ "R14_TOTAL_ADV = ?, R14_PROVISION = ?, " + "R15_TOTAL_ADV = ?, R15_PROVISION = ?, "
					+ "R16_TOTAL_ADV = ?, R16_PROVISION = ?, " + "R17_TOTAL_ADV = ?, R17_PROVISION = ?, "
					+ "R18_TOTAL_ADV = ?, R18_PROVISION = ?, " + "R19_TOTAL_ADV = ?, R19_PROVISION = ?, "
					+ "R20_TOTAL_ADV = ?, R20_PROVISION = ?, " + "R21_TOTAL_ADV = ?, R21_PROVISION = ?, "
					+ "R22_TOTAL_ADV = ?, R22_PROVISION = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(updateSQL, updatedEntity.getR10_total_adv(), updatedEntity.getR10_provision(),
					updatedEntity.getR11_total_adv(), updatedEntity.getR11_provision(),
					updatedEntity.getR12_total_adv(), updatedEntity.getR12_provision(),
					updatedEntity.getR13_total_adv(), updatedEntity.getR13_provision(),
					updatedEntity.getR14_total_adv(), updatedEntity.getR14_provision(),
					updatedEntity.getR15_total_adv(), updatedEntity.getR15_provision(),
					updatedEntity.getR16_total_adv(), updatedEntity.getR16_provision(),
					updatedEntity.getR17_total_adv(), updatedEntity.getR17_provision(),
					updatedEntity.getR18_total_adv(), updatedEntity.getR18_provision(),
					updatedEntity.getR19_total_adv(), updatedEntity.getR19_provision(),
					updatedEntity.getR20_total_adv(), updatedEntity.getR20_provision(),
					updatedEntity.getR21_total_adv(), updatedEntity.getR21_provision(), totalAdvance, totalProvision,
					reportDate, newVersion);

			logger.info("UFCE Manual Entry Resub Version Process Completed : {}", newVersion);

		} catch (Exception e) {
			logger.error("Error while updating UFCE Manual Entry Resub", e);
			throw new RuntimeException("Error while updating UFCE Manual Entry Resub: " + e.getMessage(), e);
		}
	}

	// =====================================================
	// SECTION 14: UNIFIED UPDATE METHOD
	// =====================================================

	@Transactional
	public void updateUFCEReport(Object updatedEntity, String type) {
		logger.info("Came to UFCE Report Update, Type = {}", type);

		if ("RESUB".equalsIgnoreCase(type)) {
			if (updatedEntity instanceof BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY) {
				updateResubCalculationReport((BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY) updatedEntity);
			} else if (updatedEntity instanceof BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY) {
				updateResubManualEntryReport((BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY) updatedEntity);
			} else {
				throw new IllegalArgumentException(
						"Unsupported entity type for RESUB: " + updatedEntity.getClass().getName());
			}
		} else {
			if (updatedEntity instanceof BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY) {
				updateDetailReport((BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY) updatedEntity);
			} else if (updatedEntity instanceof BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY) {
				// The updated updateSummaryReport will handle both insert and update
				updateSummaryReport((BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY) updatedEntity);
			} else {
				throw new IllegalArgumentException(
						"Unsupported entity type for NORMAL update: " + updatedEntity.getClass().getName());
			}
		}
	}
	// =====================================================
	// SECTION 15: VALIDATION AND CALCULATION HELPERS
	// =====================================================

	private void validateNonNegativeValues(BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY entity) {
		// R10 are header labels (String) - skip validation
		// Only validate R11 to R21 (actual data rows)
		validateNonNegative(entity.getR11_total_adv(), "R11 Total Advance");
		validateNonNegative(entity.getR11_provision(), "R11 Provision");
		validateNonNegative(entity.getR12_total_adv(), "R12 Total Advance");
		validateNonNegative(entity.getR12_provision(), "R12 Provision");
		validateNonNegative(entity.getR13_total_adv(), "R13 Total Advance");
		validateNonNegative(entity.getR13_provision(), "R13 Provision");
		validateNonNegative(entity.getR14_total_adv(), "R14 Total Advance");
		validateNonNegative(entity.getR14_provision(), "R14 Provision");
		validateNonNegative(entity.getR15_total_adv(), "R15 Total Advance");
		validateNonNegative(entity.getR15_provision(), "R15 Provision");
		validateNonNegative(entity.getR16_total_adv(), "R16 Total Advance");
		validateNonNegative(entity.getR16_provision(), "R16 Provision");
		validateNonNegative(entity.getR17_total_adv(), "R17 Total Advance");
		validateNonNegative(entity.getR17_provision(), "R17 Provision");
		validateNonNegative(entity.getR18_total_adv(), "R18 Total Advance");
		validateNonNegative(entity.getR18_provision(), "R18 Provision");
		validateNonNegative(entity.getR19_total_adv(), "R19 Total Advance");
		validateNonNegative(entity.getR19_provision(), "R19 Provision");
		validateNonNegative(entity.getR20_total_adv(), "R20 Total Advance");
		validateNonNegative(entity.getR20_provision(), "R20 Provision");
		validateNonNegative(entity.getR21_total_adv(), "R21 Total Advance");
		validateNonNegative(entity.getR21_provision(), "R21 Provision");
	}

	private void validateNonNegative(BigDecimal value, String fieldName) {
		if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(fieldName + " cannot be negative. Value: " + value);
		}
	}

	private BigDecimal calculateTotalAdvance(BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY entity) {
		BigDecimal total = BigDecimal.ZERO;
		// R10 are header labels (String) - skip in calculation
		total = total.add(nvl(entity.getR11_total_adv()));
		total = total.add(nvl(entity.getR12_total_adv()));
		total = total.add(nvl(entity.getR13_total_adv()));
		total = total.add(nvl(entity.getR14_total_adv()));
		total = total.add(nvl(entity.getR15_total_adv()));
		total = total.add(nvl(entity.getR16_total_adv()));
		total = total.add(nvl(entity.getR17_total_adv()));
		total = total.add(nvl(entity.getR18_total_adv()));
		total = total.add(nvl(entity.getR19_total_adv()));
		total = total.add(nvl(entity.getR20_total_adv()));
		total = total.add(nvl(entity.getR21_total_adv()));
		return total;
	}

	private BigDecimal calculateTotalProvision(BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY entity) {
		BigDecimal total = BigDecimal.ZERO;
		// R10 are header labels (String) - skip in calculation
		total = total.add(nvl(entity.getR11_provision()));
		total = total.add(nvl(entity.getR12_provision()));
		total = total.add(nvl(entity.getR13_provision()));
		total = total.add(nvl(entity.getR14_provision()));
		total = total.add(nvl(entity.getR15_provision()));
		total = total.add(nvl(entity.getR16_provision()));
		total = total.add(nvl(entity.getR17_provision()));
		total = total.add(nvl(entity.getR18_provision()));
		total = total.add(nvl(entity.getR19_provision()));
		total = total.add(nvl(entity.getR20_provision()));
		total = total.add(nvl(entity.getR21_provision()));
		return total;
	}

	private BigDecimal calculateTotalAdvanceManual(BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY entity) {
		BigDecimal total = BigDecimal.ZERO;
		// R10 are header labels (String) - skip in calculation
		total = total.add(nvl(entity.getR11_total_adv()));
		total = total.add(nvl(entity.getR12_total_adv()));
		total = total.add(nvl(entity.getR13_total_adv()));
		total = total.add(nvl(entity.getR14_total_adv()));
		total = total.add(nvl(entity.getR15_total_adv()));
		total = total.add(nvl(entity.getR16_total_adv()));
		total = total.add(nvl(entity.getR17_total_adv()));
		total = total.add(nvl(entity.getR18_total_adv()));
		total = total.add(nvl(entity.getR19_total_adv()));
		total = total.add(nvl(entity.getR20_total_adv()));
		total = total.add(nvl(entity.getR21_total_adv()));
		return total;
	}

	private BigDecimal calculateTotalProvisionManual(BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY entity) {
		BigDecimal total = BigDecimal.ZERO;
		// R10 are header labels (String) - skip in calculation
		total = total.add(nvl(entity.getR11_provision()));
		total = total.add(nvl(entity.getR12_provision()));
		total = total.add(nvl(entity.getR13_provision()));
		total = total.add(nvl(entity.getR14_provision()));
		total = total.add(nvl(entity.getR15_provision()));
		total = total.add(nvl(entity.getR16_provision()));
		total = total.add(nvl(entity.getR17_provision()));
		total = total.add(nvl(entity.getR18_provision()));
		total = total.add(nvl(entity.getR19_provision()));
		total = total.add(nvl(entity.getR20_provision()));
		total = total.add(nvl(entity.getR21_provision()));
		return total;
	}

	// =====================================================
	// SECTION 16: EXCEL GENERATION
	// =====================================================

	@Async
	public byte[] getUFCEUploadableExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version, HttpServletRequest req1)
			throws Exception {

		logger.info("Service: Starting Uploadable Excel generation process.");

		try {
			Date reportDate = parseDate(todate);

			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> archivalData = getManualEntryArchivalSummaryDataByDate(
						reportDate, version);
				List<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> archivalDetailData = getCalculationArchivalDetailDataByDate(
						reportDate, version);
				return generateUploadableExcelFromArchivalData(archivalData, archivalDetailData, filename, reportDate);
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> resubData = getResubManualEntrySummaryDataByDate(
						reportDate, version);
				List<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> resubDetailData = getResubCalculationDetailDataByDate(
						reportDate, version);
				return generateUploadableExcelFromArchivalData(resubData, resubDetailData, filename, reportDate);
			} else {
				List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> summaryData = getManualEntrySummaryDataByDate(
						reportDate);
				List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> detailData = getCalculationDetailDataByDate(reportDate);
				return generateUploadableExcelFromNormalData(summaryData, detailData, filename, reportDate);
			}

		} catch (Exception e) {
			logger.error("Error generating uploadable Excel: {}", e.getMessage(), e);
			throw new RuntimeException("Error generating uploadable Excel: " + e.getMessage(), e);
		}
	}

	/**
	 * Generates Excel from normal data with enhanced styling
	 */
	private byte[] generateUploadableExcelFromNormalData(
			List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> summaryData,
			List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> detailData, String filename, Date reportDate)
			throws Exception {

		if (summaryData == null || summaryData.isEmpty()) {
			logger.warn("No data found for UFCE report.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found: " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			Font font = workbook.createFont();
			font.setFontName("Calibri");
			font.setFontHeightInPoints((short) 11);

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setFont(font);
			textStyle.setWrapText(true);
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			CreationHelper createHelper = workbook.getCreationHelper();
			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setFont(font);
			numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);

			CellStyle totalHeaderStyle = createTotalHeaderStyle(workbook);
			CellStyle totalNumberStyle = createTotalNumberStyle(workbook);

			BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY record = summaryData.get(0);

			// Row 6: Provision above 25 Cr (from R22 provision from manual table)
			Row row6 = sheet.getRow(5);
			if (row6 == null)
				row6 = sheet.createRow(5);
			setCellValue(row6, 6, record.getR22_provision(), numberStyle);

			// Row 7: Provision upto 25 Cr (from R33 in detail)
			Row row7 = sheet.getRow(6);
			if (row7 == null)
				row7 = sheet.createRow(6);
			BigDecimal r33 = detailData != null && !detailData.isEmpty() ? detailData.get(0).getR33_value()
					: BigDecimal.ZERO;
			setCellValue(row7, 6, r33, numberStyle);

			// Row 8: Total Provision (R6 + R7)
			Row row8 = sheet.getRow(7);
			if (row8 == null)
				row8 = sheet.createRow(7);
			BigDecimal totalProv = nvl(record.getR22_provision()).add(r33);
			setCellValue(row8, 6, totalProv, numberStyle);

			// SUBSIDIARY SECTION - Rows 10-21
			int subRow = 9;
			setSubsidiaryRow(sheet, subRow++, record.getR11_subsidiary(), record.getR11_total_adv(),
					record.getR11_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR12_subsidiary(), record.getR12_total_adv(),
					record.getR12_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR13_subsidiary(), record.getR13_total_adv(),
					record.getR13_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR14_subsidiary(), record.getR14_total_adv(),
					record.getR14_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR15_subsidiary(), record.getR15_total_adv(),
					record.getR15_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR16_subsidiary(), record.getR16_total_adv(),
					record.getR16_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR17_subsidiary(), record.getR17_total_adv(),
					record.getR17_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR18_subsidiary(), record.getR18_total_adv(),
					record.getR18_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR19_subsidiary(), record.getR19_total_adv(),
					record.getR19_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR20_subsidiary(), record.getR20_total_adv(),
					record.getR20_provision(), textStyle, numberStyle);
			setSubsidiaryRow(sheet, subRow++, record.getR21_subsidiary(), record.getR21_total_adv(),
					record.getR21_provision(), textStyle, numberStyle);

			// TOTAL Row (Row 22)
			Row row22 = sheet.getRow(subRow);
			if (row22 == null)
				row22 = sheet.createRow(subRow);
			setCellValue(row22, 4, record.getR22_total_adv(), totalNumberStyle);
			setCellValue(row22, 7, record.getR22_provision(), totalNumberStyle);

			// CALCULATION SECTION - Rows 27-33
			if (detailData != null && !detailData.isEmpty()) {
				BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY detail = detailData.get(0);
				int calcRow = 26;

				Row row27 = sheet.getRow(calcRow);
				if (row27 == null)
					row27 = sheet.createRow(calcRow);
				setCellValue(row27, 9, detail.getR27_value(), numberStyle);

				Row row28 = sheet.getRow(++calcRow);
				if (row28 == null)
					row28 = sheet.createRow(calcRow);
				setCellValue(row28, 9, detail.getR28_value(), numberStyle);

				Row row29 = sheet.getRow(++calcRow);
				if (row29 == null)
					row29 = sheet.createRow(calcRow);
				setCellValue(row29, 9, detail.getR29_value(), numberStyle);

				Row row30 = sheet.getRow(++calcRow);
				if (row30 == null)
					row30 = sheet.createRow(calcRow);
				setCellValue(row30, 9, detail.getR30_value(), numberStyle);

				Row row31 = sheet.getRow(++calcRow);
				if (row31 == null)
					row31 = sheet.createRow(calcRow);
				setCellValue(row31, 9, detail.getR31_value(), numberStyle);

				Row row32 = sheet.getRow(++calcRow);
				if (row32 == null)
					row32 = sheet.createRow(calcRow);
				setCellValue(row32, 9, detail.getR32_value(), numberStyle);

				Row row33 = sheet.getRow(++calcRow);
				if (row33 == null)
					row33 = sheet.createRow(calcRow);
				setCellValue(row33, 9, detail.getR33_value(), numberStyle);
			}

			workbook.setForceFormulaRecalculation(true);
			workbook.write(out);

			// Audit
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				if (auditService != null) {
					auditService.createBusinessAudit(userid, "DOWNLOAD_UPLOADABLE", "UFCE Uploadable Excel", null,
							"BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL");
				}
			}

			logger.info("Uploadable Excel generated successfully ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	private void setSubsidiaryRow(Sheet sheet, int rowIndex, String name, BigDecimal advance, BigDecimal provision,
			CellStyle textStyle, CellStyle numberStyle) {
		Row row = sheet.getRow(rowIndex);
		if (row == null)
			row = sheet.createRow(rowIndex);
		setCellValue(row, 1, name, textStyle);
		setCellValue(row, 4, advance, numberStyle);
		setCellValue(row, 7, provision, numberStyle);
	}

	/**
	 * Generates Excel from archival data with enhanced styling
	 */
	private byte[] generateUploadableExcelFromArchivalData(
			List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> dataList,
			List<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> detailList, String filename, Date reportDate)
			throws Exception {

		if (dataList == null || dataList.isEmpty()) {
			logger.warn("No archival data found for UFCE report.");
			return new byte[0];
		}

		// Convert archival summary to normal summary entities
		List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> convertedList = new ArrayList<>();
		for (BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY archival : dataList) {
			BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY normal = new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY();
			BeanUtils.copyProperties(archival, normal);
			convertedList.add(normal);
		}

		// Convert archival detail (R27-R33 calculation section) to normal detail
		// entities
		// so the Excel export doesn't silently drop the calculation section for
		// ARCHIVAL/RESUB downloads
		List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> convertedDetailList = null;
		if (detailList != null && !detailList.isEmpty()) {
			convertedDetailList = new ArrayList<>();
			for (BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY archivalDetail : detailList) {
				BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY normalDetail = new BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY();
				BeanUtils.copyProperties(archivalDetail, normalDetail);
				convertedDetailList.add(normalDetail);
			}
		}

		return generateUploadableExcelFromNormalData(convertedList, convertedDetailList, filename, reportDate);
	}

	// =====================================================
	// SECTION 17: DOWNLOAD - UPLOADABLE PDF
	// =====================================================

	public byte[] getUFCEUploadablePDF(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version, HttpServletRequest req1)
			throws Exception {

		logger.info("Service: Starting Uploadable PDF generation process.");

		byte[] excelBytes = getUFCEUploadableExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
				version, req1);

		if (excelBytes == null || excelBytes.length == 0) {
			throw new RuntimeException("No data available for PDF generation");
		}

		logger.info("Uploadable PDF generated successfully.");
		return excelBytes;
	}

	// =====================================================
	// SECTION 18: NAVIGATION - Back to Summary
	// =====================================================

	public ModelAndView navigateToSummary(String reportId, String fromdate, String todate, String currency,
			HttpServletRequest req1) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/UFCE_CALCULATION?reportId=" + reportId + "&fromdate=" + fromdate + "&todate=" + todate
				+ "&currency=" + currency);
		return mv;
	}

	// =====================================================
	// RESUB METHODS - Return Object[] for RegulatoryReportServices
	// =====================================================

	public List<Object[]> getResubCalculationSummaryDataAsArray(Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<Object[]> getResubManualEntrySummaryDataAsArray(Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<Object[]> getResubCalculationDetailDataAsArray(Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE ORDER BY REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	// =====================================================
	// ENTITY CLASSES AND ROW MAPPERS
	// =====================================================

	// =====================================================
	// ENTITY: BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY
	// Table: BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL
	// =====================================================
	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL")
	public static class BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY {

		// Metadata
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_resubdate;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// R10 - Header labels (not data)
		private String r10_serial_no;
		private String r10_subsidiary;
		private String r10_total_adv;
		private String r10_provision;

		// R11 to R21 - 11 Subsidiary rows
		private BigDecimal r11_serial_no;
		private String r11_subsidiary;
		private BigDecimal r11_total_adv;
		private BigDecimal r11_provision;

		private BigDecimal r12_serial_no;
		private String r12_subsidiary;
		private BigDecimal r12_total_adv;
		private BigDecimal r12_provision;

		private BigDecimal r13_serial_no;
		private String r13_subsidiary;
		private BigDecimal r13_total_adv;
		private BigDecimal r13_provision;

		private BigDecimal r14_serial_no;
		private String r14_subsidiary;
		private BigDecimal r14_total_adv;
		private BigDecimal r14_provision;

		private BigDecimal r15_serial_no;
		private String r15_subsidiary;
		private BigDecimal r15_total_adv;
		private BigDecimal r15_provision;

		private BigDecimal r16_serial_no;
		private String r16_subsidiary;
		private BigDecimal r16_total_adv;
		private BigDecimal r16_provision;

		private BigDecimal r17_serial_no;
		private String r17_subsidiary;
		private BigDecimal r17_total_adv;
		private BigDecimal r17_provision;

		private BigDecimal r18_serial_no;
		private String r18_subsidiary;
		private BigDecimal r18_total_adv;
		private BigDecimal r18_provision;

		private BigDecimal r19_serial_no;
		private String r19_subsidiary;
		private BigDecimal r19_total_adv;
		private BigDecimal r19_provision;

		private BigDecimal r20_serial_no;
		private String r20_subsidiary;
		private BigDecimal r20_total_adv;
		private BigDecimal r20_provision;

		private BigDecimal r21_serial_no;
		private String r21_subsidiary;
		private BigDecimal r21_total_adv;
		private BigDecimal r21_provision;

		// R22 - Totals
		private BigDecimal r22_total_adv;
		private BigDecimal r22_provision;

		// Default Constructor
		public BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY() {
			super();
		}

		// Getters and Setters

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setReport_resubdate(Date report_resubdate) {
			this.report_resubdate = report_resubdate;
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

		public String getR10_serial_no() {
			return r10_serial_no;
		}

		public void setR10_serial_no(String r10_serial_no) {
			this.r10_serial_no = r10_serial_no;
		}

		public String getR10_subsidiary() {
			return r10_subsidiary;
		}

		public void setR10_subsidiary(String r10_subsidiary) {
			this.r10_subsidiary = r10_subsidiary;
		}

		public String getR10_total_adv() {
			return r10_total_adv;
		}

		public void setR10_total_adv(String r10_total_adv) {
			this.r10_total_adv = r10_total_adv;
		}

		public String getR10_provision() {
			return r10_provision;
		}

		public void setR10_provision(String r10_provision) {
			this.r10_provision = r10_provision;
		}

		public BigDecimal getR11_serial_no() {
			return r11_serial_no;
		}

		public void setR11_serial_no(BigDecimal r11_serial_no) {
			this.r11_serial_no = r11_serial_no;
		}

		public String getR11_subsidiary() {
			return r11_subsidiary;
		}

		public void setR11_subsidiary(String r11_subsidiary) {
			this.r11_subsidiary = r11_subsidiary;
		}

		public BigDecimal getR11_total_adv() {
			return r11_total_adv;
		}

		public void setR11_total_adv(BigDecimal r11_total_adv) {
			this.r11_total_adv = r11_total_adv;
		}

		public BigDecimal getR11_provision() {
			return r11_provision;
		}

		public void setR11_provision(BigDecimal r11_provision) {
			this.r11_provision = r11_provision;
		}

		public BigDecimal getR12_serial_no() {
			return r12_serial_no;
		}

		public void setR12_serial_no(BigDecimal r12_serial_no) {
			this.r12_serial_no = r12_serial_no;
		}

		public String getR12_subsidiary() {
			return r12_subsidiary;
		}

		public void setR12_subsidiary(String r12_subsidiary) {
			this.r12_subsidiary = r12_subsidiary;
		}

		public BigDecimal getR12_total_adv() {
			return r12_total_adv;
		}

		public void setR12_total_adv(BigDecimal r12_total_adv) {
			this.r12_total_adv = r12_total_adv;
		}

		public BigDecimal getR12_provision() {
			return r12_provision;
		}

		public void setR12_provision(BigDecimal r12_provision) {
			this.r12_provision = r12_provision;
		}

		public BigDecimal getR13_serial_no() {
			return r13_serial_no;
		}

		public void setR13_serial_no(BigDecimal r13_serial_no) {
			this.r13_serial_no = r13_serial_no;
		}

		public String getR13_subsidiary() {
			return r13_subsidiary;
		}

		public void setR13_subsidiary(String r13_subsidiary) {
			this.r13_subsidiary = r13_subsidiary;
		}

		public BigDecimal getR13_total_adv() {
			return r13_total_adv;
		}

		public void setR13_total_adv(BigDecimal r13_total_adv) {
			this.r13_total_adv = r13_total_adv;
		}

		public BigDecimal getR13_provision() {
			return r13_provision;
		}

		public void setR13_provision(BigDecimal r13_provision) {
			this.r13_provision = r13_provision;
		}

		public BigDecimal getR14_serial_no() {
			return r14_serial_no;
		}

		public void setR14_serial_no(BigDecimal r14_serial_no) {
			this.r14_serial_no = r14_serial_no;
		}

		public String getR14_subsidiary() {
			return r14_subsidiary;
		}

		public void setR14_subsidiary(String r14_subsidiary) {
			this.r14_subsidiary = r14_subsidiary;
		}

		public BigDecimal getR14_total_adv() {
			return r14_total_adv;
		}

		public void setR14_total_adv(BigDecimal r14_total_adv) {
			this.r14_total_adv = r14_total_adv;
		}

		public BigDecimal getR14_provision() {
			return r14_provision;
		}

		public void setR14_provision(BigDecimal r14_provision) {
			this.r14_provision = r14_provision;
		}

		public BigDecimal getR15_serial_no() {
			return r15_serial_no;
		}

		public void setR15_serial_no(BigDecimal r15_serial_no) {
			this.r15_serial_no = r15_serial_no;
		}

		public String getR15_subsidiary() {
			return r15_subsidiary;
		}

		public void setR15_subsidiary(String r15_subsidiary) {
			this.r15_subsidiary = r15_subsidiary;
		}

		public BigDecimal getR15_total_adv() {
			return r15_total_adv;
		}

		public void setR15_total_adv(BigDecimal r15_total_adv) {
			this.r15_total_adv = r15_total_adv;
		}

		public BigDecimal getR15_provision() {
			return r15_provision;
		}

		public void setR15_provision(BigDecimal r15_provision) {
			this.r15_provision = r15_provision;
		}

		public BigDecimal getR16_serial_no() {
			return r16_serial_no;
		}

		public void setR16_serial_no(BigDecimal r16_serial_no) {
			this.r16_serial_no = r16_serial_no;
		}

		public String getR16_subsidiary() {
			return r16_subsidiary;
		}

		public void setR16_subsidiary(String r16_subsidiary) {
			this.r16_subsidiary = r16_subsidiary;
		}

		public BigDecimal getR16_total_adv() {
			return r16_total_adv;
		}

		public void setR16_total_adv(BigDecimal r16_total_adv) {
			this.r16_total_adv = r16_total_adv;
		}

		public BigDecimal getR16_provision() {
			return r16_provision;
		}

		public void setR16_provision(BigDecimal r16_provision) {
			this.r16_provision = r16_provision;
		}

		public BigDecimal getR17_serial_no() {
			return r17_serial_no;
		}

		public void setR17_serial_no(BigDecimal r17_serial_no) {
			this.r17_serial_no = r17_serial_no;
		}

		public String getR17_subsidiary() {
			return r17_subsidiary;
		}

		public void setR17_subsidiary(String r17_subsidiary) {
			this.r17_subsidiary = r17_subsidiary;
		}

		public BigDecimal getR17_total_adv() {
			return r17_total_adv;
		}

		public void setR17_total_adv(BigDecimal r17_total_adv) {
			this.r17_total_adv = r17_total_adv;
		}

		public BigDecimal getR17_provision() {
			return r17_provision;
		}

		public void setR17_provision(BigDecimal r17_provision) {
			this.r17_provision = r17_provision;
		}

		public BigDecimal getR18_serial_no() {
			return r18_serial_no;
		}

		public void setR18_serial_no(BigDecimal r18_serial_no) {
			this.r18_serial_no = r18_serial_no;
		}

		public String getR18_subsidiary() {
			return r18_subsidiary;
		}

		public void setR18_subsidiary(String r18_subsidiary) {
			this.r18_subsidiary = r18_subsidiary;
		}

		public BigDecimal getR18_total_adv() {
			return r18_total_adv;
		}

		public void setR18_total_adv(BigDecimal r18_total_adv) {
			this.r18_total_adv = r18_total_adv;
		}

		public BigDecimal getR18_provision() {
			return r18_provision;
		}

		public void setR18_provision(BigDecimal r18_provision) {
			this.r18_provision = r18_provision;
		}

		public BigDecimal getR19_serial_no() {
			return r19_serial_no;
		}

		public void setR19_serial_no(BigDecimal r19_serial_no) {
			this.r19_serial_no = r19_serial_no;
		}

		public String getR19_subsidiary() {
			return r19_subsidiary;
		}

		public void setR19_subsidiary(String r19_subsidiary) {
			this.r19_subsidiary = r19_subsidiary;
		}

		public BigDecimal getR19_total_adv() {
			return r19_total_adv;
		}

		public void setR19_total_adv(BigDecimal r19_total_adv) {
			this.r19_total_adv = r19_total_adv;
		}

		public BigDecimal getR19_provision() {
			return r19_provision;
		}

		public void setR19_provision(BigDecimal r19_provision) {
			this.r19_provision = r19_provision;
		}

		public BigDecimal getR20_serial_no() {
			return r20_serial_no;
		}

		public void setR20_serial_no(BigDecimal r20_serial_no) {
			this.r20_serial_no = r20_serial_no;
		}

		public String getR20_subsidiary() {
			return r20_subsidiary;
		}

		public void setR20_subsidiary(String r20_subsidiary) {
			this.r20_subsidiary = r20_subsidiary;
		}

		public BigDecimal getR20_total_adv() {
			return r20_total_adv;
		}

		public void setR20_total_adv(BigDecimal r20_total_adv) {
			this.r20_total_adv = r20_total_adv;
		}

		public BigDecimal getR20_provision() {
			return r20_provision;
		}

		public void setR20_provision(BigDecimal r20_provision) {
			this.r20_provision = r20_provision;
		}

		public BigDecimal getR21_serial_no() {
			return r21_serial_no;
		}

		public void setR21_serial_no(BigDecimal r21_serial_no) {
			this.r21_serial_no = r21_serial_no;
		}

		public String getR21_subsidiary() {
			return r21_subsidiary;
		}

		public void setR21_subsidiary(String r21_subsidiary) {
			this.r21_subsidiary = r21_subsidiary;
		}

		public BigDecimal getR21_total_adv() {
			return r21_total_adv;
		}

		public void setR21_total_adv(BigDecimal r21_total_adv) {
			this.r21_total_adv = r21_total_adv;
		}

		public BigDecimal getR21_provision() {
			return r21_provision;
		}

		public void setR21_provision(BigDecimal r21_provision) {
			this.r21_provision = r21_provision;
		}

		public BigDecimal getR22_total_adv() {
			return r22_total_adv;
		}

		public void setR22_total_adv(BigDecimal r22_total_adv) {
			this.r22_total_adv = r22_total_adv;
		}

		public BigDecimal getR22_provision() {
			return r22_provision;
		}

		public void setR22_provision(BigDecimal r22_provision) {
			this.r22_provision = r22_provision;
		}
	}

	// =====================================================
	// ROW MAPPER: BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ROWMAPPER
	// =====================================================
	public class BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ROWMAPPER
			implements RowMapper<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> {

		@Override
		public BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY obj = new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY();

			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setReport_resubdate(rs.getDate("REPORT_RESUBDATE"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setR10_serial_no(rs.getString("R10_SERIAL_NO"));
			obj.setR10_subsidiary(rs.getString("R10_SUBSIDIARY"));
			obj.setR10_total_adv(rs.getString("R10_TOTAL_ADV"));
			obj.setR10_provision(rs.getString("R10_PROVISION"));

			obj.setR11_serial_no(rs.getBigDecimal("R11_SERIAL_NO"));
			obj.setR11_subsidiary(rs.getString("R11_SUBSIDIARY"));
			obj.setR11_total_adv(rs.getBigDecimal("R11_TOTAL_ADV"));
			obj.setR11_provision(rs.getBigDecimal("R11_PROVISION"));

			obj.setR12_serial_no(rs.getBigDecimal("R12_SERIAL_NO"));
			obj.setR12_subsidiary(rs.getString("R12_SUBSIDIARY"));
			obj.setR12_total_adv(rs.getBigDecimal("R12_TOTAL_ADV"));
			obj.setR12_provision(rs.getBigDecimal("R12_PROVISION"));

			obj.setR13_serial_no(rs.getBigDecimal("R13_SERIAL_NO"));
			obj.setR13_subsidiary(rs.getString("R13_SUBSIDIARY"));
			obj.setR13_total_adv(rs.getBigDecimal("R13_TOTAL_ADV"));
			obj.setR13_provision(rs.getBigDecimal("R13_PROVISION"));

			obj.setR14_serial_no(rs.getBigDecimal("R14_SERIAL_NO"));
			obj.setR14_subsidiary(rs.getString("R14_SUBSIDIARY"));
			obj.setR14_total_adv(rs.getBigDecimal("R14_TOTAL_ADV"));
			obj.setR14_provision(rs.getBigDecimal("R14_PROVISION"));

			obj.setR15_serial_no(rs.getBigDecimal("R15_SERIAL_NO"));
			obj.setR15_subsidiary(rs.getString("R15_SUBSIDIARY"));
			obj.setR15_total_adv(rs.getBigDecimal("R15_TOTAL_ADV"));
			obj.setR15_provision(rs.getBigDecimal("R15_PROVISION"));

			obj.setR16_serial_no(rs.getBigDecimal("R16_SERIAL_NO"));
			obj.setR16_subsidiary(rs.getString("R16_SUBSIDIARY"));
			obj.setR16_total_adv(rs.getBigDecimal("R16_TOTAL_ADV"));
			obj.setR16_provision(rs.getBigDecimal("R16_PROVISION"));

			obj.setR17_serial_no(rs.getBigDecimal("R17_SERIAL_NO"));
			obj.setR17_subsidiary(rs.getString("R17_SUBSIDIARY"));
			obj.setR17_total_adv(rs.getBigDecimal("R17_TOTAL_ADV"));
			obj.setR17_provision(rs.getBigDecimal("R17_PROVISION"));

			obj.setR18_serial_no(rs.getBigDecimal("R18_SERIAL_NO"));
			obj.setR18_subsidiary(rs.getString("R18_SUBSIDIARY"));
			obj.setR18_total_adv(rs.getBigDecimal("R18_TOTAL_ADV"));
			obj.setR18_provision(rs.getBigDecimal("R18_PROVISION"));

			obj.setR19_serial_no(rs.getBigDecimal("R19_SERIAL_NO"));
			obj.setR19_subsidiary(rs.getString("R19_SUBSIDIARY"));
			obj.setR19_total_adv(rs.getBigDecimal("R19_TOTAL_ADV"));
			obj.setR19_provision(rs.getBigDecimal("R19_PROVISION"));

			obj.setR20_serial_no(rs.getBigDecimal("R20_SERIAL_NO"));
			obj.setR20_subsidiary(rs.getString("R20_SUBSIDIARY"));
			obj.setR20_total_adv(rs.getBigDecimal("R20_TOTAL_ADV"));
			obj.setR20_provision(rs.getBigDecimal("R20_PROVISION"));

			obj.setR21_serial_no(rs.getBigDecimal("R21_SERIAL_NO"));
			obj.setR21_subsidiary(rs.getString("R21_SUBSIDIARY"));
			obj.setR21_total_adv(rs.getBigDecimal("R21_TOTAL_ADV"));
			obj.setR21_provision(rs.getBigDecimal("R21_PROVISION"));

			obj.setR22_total_adv(rs.getBigDecimal("R22_TOTAL_ADV"));
			obj.setR22_provision(rs.getBigDecimal("R22_PROVISION"));

			return obj;
		}
	}

	// =====================================================
	// ENTITY: BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY
	// Table: BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL
	// =====================================================
	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL")
	public static class BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY {

		// Metadata
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_resubdate;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Archive specific
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date archive_date;
		private String archive_reason;
		private String archived_by;

		// R10 - Header labels (not data)
		private String r10_serial_no;
		private String r10_subsidiary;
		private String r10_total_adv;
		private String r10_provision;

		// R11 to R21 - 11 Subsidiary rows
		private BigDecimal r11_serial_no;
		private String r11_subsidiary;
		private BigDecimal r11_total_adv;
		private BigDecimal r11_provision;

		private BigDecimal r12_serial_no;
		private String r12_subsidiary;
		private BigDecimal r12_total_adv;
		private BigDecimal r12_provision;

		private BigDecimal r13_serial_no;
		private String r13_subsidiary;
		private BigDecimal r13_total_adv;
		private BigDecimal r13_provision;

		private BigDecimal r14_serial_no;
		private String r14_subsidiary;
		private BigDecimal r14_total_adv;
		private BigDecimal r14_provision;

		private BigDecimal r15_serial_no;
		private String r15_subsidiary;
		private BigDecimal r15_total_adv;
		private BigDecimal r15_provision;

		private BigDecimal r16_serial_no;
		private String r16_subsidiary;
		private BigDecimal r16_total_adv;
		private BigDecimal r16_provision;

		private BigDecimal r17_serial_no;
		private String r17_subsidiary;
		private BigDecimal r17_total_adv;
		private BigDecimal r17_provision;

		private BigDecimal r18_serial_no;
		private String r18_subsidiary;
		private BigDecimal r18_total_adv;
		private BigDecimal r18_provision;

		private BigDecimal r19_serial_no;
		private String r19_subsidiary;
		private BigDecimal r19_total_adv;
		private BigDecimal r19_provision;

		private BigDecimal r20_serial_no;
		private String r20_subsidiary;
		private BigDecimal r20_total_adv;
		private BigDecimal r20_provision;

		private BigDecimal r21_serial_no;
		private String r21_subsidiary;
		private BigDecimal r21_total_adv;
		private BigDecimal r21_provision;

		// R22 - Totals
		private BigDecimal r22_total_adv;
		private BigDecimal r22_provision;

		// Default Constructor
		public BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY() {
			super();
		}

		// Getters and Setters

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setReport_resubdate(Date report_resubdate) {
			this.report_resubdate = report_resubdate;
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

		public Date getArchive_date() {
			return archive_date;
		}

		public void setArchive_date(Date archive_date) {
			this.archive_date = archive_date;
		}

		public String getArchive_reason() {
			return archive_reason;
		}

		public void setArchive_reason(String archive_reason) {
			this.archive_reason = archive_reason;
		}

		public String getArchived_by() {
			return archived_by;
		}

		public void setArchived_by(String archived_by) {
			this.archived_by = archived_by;
		}

		public String getR10_serial_no() {
			return r10_serial_no;
		}

		public void setR10_serial_no(String r10_serial_no) {
			this.r10_serial_no = r10_serial_no;
		}

		public String getR10_subsidiary() {
			return r10_subsidiary;
		}

		public void setR10_subsidiary(String r10_subsidiary) {
			this.r10_subsidiary = r10_subsidiary;
		}

		public String getR10_total_adv() {
			return r10_total_adv;
		}

		public void setR10_total_adv(String r10_total_adv) {
			this.r10_total_adv = r10_total_adv;
		}

		public String getR10_provision() {
			return r10_provision;
		}

		public void setR10_provision(String r10_provision) {
			this.r10_provision = r10_provision;
		}

		public BigDecimal getR11_serial_no() {
			return r11_serial_no;
		}

		public void setR11_serial_no(BigDecimal r11_serial_no) {
			this.r11_serial_no = r11_serial_no;
		}

		public String getR11_subsidiary() {
			return r11_subsidiary;
		}

		public void setR11_subsidiary(String r11_subsidiary) {
			this.r11_subsidiary = r11_subsidiary;
		}

		public BigDecimal getR11_total_adv() {
			return r11_total_adv;
		}

		public void setR11_total_adv(BigDecimal r11_total_adv) {
			this.r11_total_adv = r11_total_adv;
		}

		public BigDecimal getR11_provision() {
			return r11_provision;
		}

		public void setR11_provision(BigDecimal r11_provision) {
			this.r11_provision = r11_provision;
		}

		// Getters and Setters - R12
		public BigDecimal getR12_serial_no() {
			return r12_serial_no;
		}

		public void setR12_serial_no(BigDecimal r12_serial_no) {
			this.r12_serial_no = r12_serial_no;
		}

		public String getR12_subsidiary() {
			return r12_subsidiary;
		}

		public void setR12_subsidiary(String r12_subsidiary) {
			this.r12_subsidiary = r12_subsidiary;
		}

		public BigDecimal getR12_total_adv() {
			return r12_total_adv;
		}

		public void setR12_total_adv(BigDecimal r12_total_adv) {
			this.r12_total_adv = r12_total_adv;
		}

		public BigDecimal getR12_provision() {
			return r12_provision;
		}

		public void setR12_provision(BigDecimal r12_provision) {
			this.r12_provision = r12_provision;
		}

		// Getters and Setters - R13
		public BigDecimal getR13_serial_no() {
			return r13_serial_no;
		}

		public void setR13_serial_no(BigDecimal r13_serial_no) {
			this.r13_serial_no = r13_serial_no;
		}

		public String getR13_subsidiary() {
			return r13_subsidiary;
		}

		public void setR13_subsidiary(String r13_subsidiary) {
			this.r13_subsidiary = r13_subsidiary;
		}

		public BigDecimal getR13_total_adv() {
			return r13_total_adv;
		}

		public void setR13_total_adv(BigDecimal r13_total_adv) {
			this.r13_total_adv = r13_total_adv;
		}

		public BigDecimal getR13_provision() {
			return r13_provision;
		}

		public void setR13_provision(BigDecimal r13_provision) {
			this.r13_provision = r13_provision;
		}

		// Getters and Setters - R14
		public BigDecimal getR14_serial_no() {
			return r14_serial_no;
		}

		public void setR14_serial_no(BigDecimal r14_serial_no) {
			this.r14_serial_no = r14_serial_no;
		}

		public String getR14_subsidiary() {
			return r14_subsidiary;
		}

		public void setR14_subsidiary(String r14_subsidiary) {
			this.r14_subsidiary = r14_subsidiary;
		}

		public BigDecimal getR14_total_adv() {
			return r14_total_adv;
		}

		public void setR14_total_adv(BigDecimal r14_total_adv) {
			this.r14_total_adv = r14_total_adv;
		}

		public BigDecimal getR14_provision() {
			return r14_provision;
		}

		public void setR14_provision(BigDecimal r14_provision) {
			this.r14_provision = r14_provision;
		}

		// Getters and Setters - R15
		public BigDecimal getR15_serial_no() {
			return r15_serial_no;
		}

		public void setR15_serial_no(BigDecimal r15_serial_no) {
			this.r15_serial_no = r15_serial_no;
		}

		public String getR15_subsidiary() {
			return r15_subsidiary;
		}

		public void setR15_subsidiary(String r15_subsidiary) {
			this.r15_subsidiary = r15_subsidiary;
		}

		public BigDecimal getR15_total_adv() {
			return r15_total_adv;
		}

		public void setR15_total_adv(BigDecimal r15_total_adv) {
			this.r15_total_adv = r15_total_adv;
		}

		public BigDecimal getR15_provision() {
			return r15_provision;
		}

		public void setR15_provision(BigDecimal r15_provision) {
			this.r15_provision = r15_provision;
		}

		// Getters and Setters - R16
		public BigDecimal getR16_serial_no() {
			return r16_serial_no;
		}

		public void setR16_serial_no(BigDecimal r16_serial_no) {
			this.r16_serial_no = r16_serial_no;
		}

		public String getR16_subsidiary() {
			return r16_subsidiary;
		}

		public void setR16_subsidiary(String r16_subsidiary) {
			this.r16_subsidiary = r16_subsidiary;
		}

		public BigDecimal getR16_total_adv() {
			return r16_total_adv;
		}

		public void setR16_total_adv(BigDecimal r16_total_adv) {
			this.r16_total_adv = r16_total_adv;
		}

		public BigDecimal getR16_provision() {
			return r16_provision;
		}

		public void setR16_provision(BigDecimal r16_provision) {
			this.r16_provision = r16_provision;
		}

		// Getters and Setters - R17
		public BigDecimal getR17_serial_no() {
			return r17_serial_no;
		}

		public void setR17_serial_no(BigDecimal r17_serial_no) {
			this.r17_serial_no = r17_serial_no;
		}

		public String getR17_subsidiary() {
			return r17_subsidiary;
		}

		public void setR17_subsidiary(String r17_subsidiary) {
			this.r17_subsidiary = r17_subsidiary;
		}

		public BigDecimal getR17_total_adv() {
			return r17_total_adv;
		}

		public void setR17_total_adv(BigDecimal r17_total_adv) {
			this.r17_total_adv = r17_total_adv;
		}

		public BigDecimal getR17_provision() {
			return r17_provision;
		}

		public void setR17_provision(BigDecimal r17_provision) {
			this.r17_provision = r17_provision;
		}

		// Getters and Setters - R18
		public BigDecimal getR18_serial_no() {
			return r18_serial_no;
		}

		public void setR18_serial_no(BigDecimal r18_serial_no) {
			this.r18_serial_no = r18_serial_no;
		}

		public String getR18_subsidiary() {
			return r18_subsidiary;
		}

		public void setR18_subsidiary(String r18_subsidiary) {
			this.r18_subsidiary = r18_subsidiary;
		}

		public BigDecimal getR18_total_adv() {
			return r18_total_adv;
		}

		public void setR18_total_adv(BigDecimal r18_total_adv) {
			this.r18_total_adv = r18_total_adv;
		}

		public BigDecimal getR18_provision() {
			return r18_provision;
		}

		public void setR18_provision(BigDecimal r18_provision) {
			this.r18_provision = r18_provision;
		}

		// Getters and Setters - R19
		public BigDecimal getR19_serial_no() {
			return r19_serial_no;
		}

		public void setR19_serial_no(BigDecimal r19_serial_no) {
			this.r19_serial_no = r19_serial_no;
		}

		public String getR19_subsidiary() {
			return r19_subsidiary;
		}

		public void setR19_subsidiary(String r19_subsidiary) {
			this.r19_subsidiary = r19_subsidiary;
		}

		public BigDecimal getR19_total_adv() {
			return r19_total_adv;
		}

		public void setR19_total_adv(BigDecimal r19_total_adv) {
			this.r19_total_adv = r19_total_adv;
		}

		public BigDecimal getR19_provision() {
			return r19_provision;
		}

		public void setR19_provision(BigDecimal r19_provision) {
			this.r19_provision = r19_provision;
		}

		// Getters and Setters - R20
		public BigDecimal getR20_serial_no() {
			return r20_serial_no;
		}

		public void setR20_serial_no(BigDecimal r20_serial_no) {
			this.r20_serial_no = r20_serial_no;
		}

		public String getR20_subsidiary() {
			return r20_subsidiary;
		}

		public void setR20_subsidiary(String r20_subsidiary) {
			this.r20_subsidiary = r20_subsidiary;
		}

		public BigDecimal getR20_total_adv() {
			return r20_total_adv;
		}

		public void setR20_total_adv(BigDecimal r20_total_adv) {
			this.r20_total_adv = r20_total_adv;
		}

		public BigDecimal getR20_provision() {
			return r20_provision;
		}

		public void setR20_provision(BigDecimal r20_provision) {
			this.r20_provision = r20_provision;
		}

		// Getters and Setters - R21
		public BigDecimal getR21_serial_no() {
			return r21_serial_no;
		}

		public void setR21_serial_no(BigDecimal r21_serial_no) {
			this.r21_serial_no = r21_serial_no;
		}

		public String getR21_subsidiary() {
			return r21_subsidiary;
		}

		public void setR21_subsidiary(String r21_subsidiary) {
			this.r21_subsidiary = r21_subsidiary;
		}

		public BigDecimal getR21_total_adv() {
			return r21_total_adv;
		}

		public void setR21_total_adv(BigDecimal r21_total_adv) {
			this.r21_total_adv = r21_total_adv;
		}

		public BigDecimal getR21_provision() {
			return r21_provision;
		}

		public void setR21_provision(BigDecimal r21_provision) {
			this.r21_provision = r21_provision;
		}

		// Getters and Setters - R22 Totals
		public BigDecimal getR22_total_adv() {
			return r22_total_adv;
		}

		public void setR22_total_adv(BigDecimal r22_total_adv) {
			this.r22_total_adv = r22_total_adv;
		}

		public BigDecimal getR22_provision() {
			return r22_provision;
		}

		public void setR22_provision(BigDecimal r22_provision) {
			this.r22_provision = r22_provision;
		}
	}

	// =====================================================
	// ROW MAPPER: BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ROWMAPPER
	// =====================================================
	public class BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ROWMAPPER
			implements RowMapper<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> {

		@Override
		public BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY obj = new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY();

			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setReport_resubdate(rs.getDate("REPORT_RESUBDATE"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setArchive_date(rs.getDate("ARCHIVE_DATE"));
			obj.setArchive_reason(rs.getString("ARCHIVE_REASON"));
			obj.setArchived_by(rs.getString("ARCHIVED_BY"));

			obj.setR10_serial_no(rs.getString("R10_SERIAL_NO"));
			obj.setR10_subsidiary(rs.getString("R10_SUBSIDIARY"));
			obj.setR10_total_adv(rs.getString("R10_TOTAL_ADV"));
			obj.setR10_provision(rs.getString("R10_PROVISION"));

			obj.setR11_serial_no(rs.getBigDecimal("R11_SERIAL_NO"));
			obj.setR11_subsidiary(rs.getString("R11_SUBSIDIARY"));
			obj.setR11_total_adv(rs.getBigDecimal("R11_TOTAL_ADV"));
			obj.setR11_provision(rs.getBigDecimal("R11_PROVISION"));

			// R12
			obj.setR12_serial_no(rs.getBigDecimal("R12_SERIAL_NO"));
			obj.setR12_subsidiary(rs.getString("R12_SUBSIDIARY"));
			obj.setR12_total_adv(rs.getBigDecimal("R12_TOTAL_ADV"));
			obj.setR12_provision(rs.getBigDecimal("R12_PROVISION"));

			// R13
			obj.setR13_serial_no(rs.getBigDecimal("R13_SERIAL_NO"));
			obj.setR13_subsidiary(rs.getString("R13_SUBSIDIARY"));
			obj.setR13_total_adv(rs.getBigDecimal("R13_TOTAL_ADV"));
			obj.setR13_provision(rs.getBigDecimal("R13_PROVISION"));

			// R14
			obj.setR14_serial_no(rs.getBigDecimal("R14_SERIAL_NO"));
			obj.setR14_subsidiary(rs.getString("R14_SUBSIDIARY"));
			obj.setR14_total_adv(rs.getBigDecimal("R14_TOTAL_ADV"));
			obj.setR14_provision(rs.getBigDecimal("R14_PROVISION"));

			// R15
			obj.setR15_serial_no(rs.getBigDecimal("R15_SERIAL_NO"));
			obj.setR15_subsidiary(rs.getString("R15_SUBSIDIARY"));
			obj.setR15_total_adv(rs.getBigDecimal("R15_TOTAL_ADV"));
			obj.setR15_provision(rs.getBigDecimal("R15_PROVISION"));

			// R16
			obj.setR16_serial_no(rs.getBigDecimal("R16_SERIAL_NO"));
			obj.setR16_subsidiary(rs.getString("R16_SUBSIDIARY"));
			obj.setR16_total_adv(rs.getBigDecimal("R16_TOTAL_ADV"));
			obj.setR16_provision(rs.getBigDecimal("R16_PROVISION"));

			// R17
			obj.setR17_serial_no(rs.getBigDecimal("R17_SERIAL_NO"));
			obj.setR17_subsidiary(rs.getString("R17_SUBSIDIARY"));
			obj.setR17_total_adv(rs.getBigDecimal("R17_TOTAL_ADV"));
			obj.setR17_provision(rs.getBigDecimal("R17_PROVISION"));

			// R18
			obj.setR18_serial_no(rs.getBigDecimal("R18_SERIAL_NO"));
			obj.setR18_subsidiary(rs.getString("R18_SUBSIDIARY"));
			obj.setR18_total_adv(rs.getBigDecimal("R18_TOTAL_ADV"));
			obj.setR18_provision(rs.getBigDecimal("R18_PROVISION"));

			// R19
			obj.setR19_serial_no(rs.getBigDecimal("R19_SERIAL_NO"));
			obj.setR19_subsidiary(rs.getString("R19_SUBSIDIARY"));
			obj.setR19_total_adv(rs.getBigDecimal("R19_TOTAL_ADV"));
			obj.setR19_provision(rs.getBigDecimal("R19_PROVISION"));

			// R20
			obj.setR20_serial_no(rs.getBigDecimal("R20_SERIAL_NO"));
			obj.setR20_subsidiary(rs.getString("R20_SUBSIDIARY"));
			obj.setR20_total_adv(rs.getBigDecimal("R20_TOTAL_ADV"));
			obj.setR20_provision(rs.getBigDecimal("R20_PROVISION"));

			// R21
			obj.setR21_serial_no(rs.getBigDecimal("R21_SERIAL_NO"));
			obj.setR21_subsidiary(rs.getString("R21_SUBSIDIARY"));
			obj.setR21_total_adv(rs.getBigDecimal("R21_TOTAL_ADV"));
			obj.setR21_provision(rs.getBigDecimal("R21_PROVISION"));

			// R22 - Totals
			obj.setR22_total_adv(rs.getBigDecimal("R22_TOTAL_ADV"));
			obj.setR22_provision(rs.getBigDecimal("R22_PROVISION"));

			return obj;
		}
	}

	// =====================================================
	// ENTITY: BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY
	// Table: BRRS_UFCE_CALCULATION_DETAILTABLE
	// =====================================================
	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_DETAILTABLE")
	public static class BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY {

		@Id
		private Long sno;

		// Metadata
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_resubdate;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Labels
		private String r5_label;
		private String r5_rs_in_lacs;

		// R6 - Provision above 25 Cr
		private String r6_description;
		private String r6_value; // 'NIL' as string

		// R7 - Provision upto 25 Cr
		private String r7_description;
		private BigDecimal r7_value;

		// R8 - Total
		private String r8_description;
		private BigDecimal r8_value;

		// R25 - Calculation header
		private String r25_label;
		private String r25_rs_in_lacs;

		// R27 - Total Standard Advances
		private String r27_description;
		private BigDecimal r27_value;

		// R28 - Less: Standard Advances above 25 Cr
		private String r28_description;
		private BigDecimal r28_value;

		// R29 - Total Standard Advances to borrowers with exposure upto 25 Cr
		private String r29_description;
		private BigDecimal r29_value;

		// R30 - Less: Individual borrowers
		private String r30_description;
		private BigDecimal r30_value;

		// R31 - Less: Bank advances
		private String r31_description;
		private BigDecimal r31_value;

		// R32 - Balance for provision
		private String r32_description;
		private BigDecimal r32_value;

		// R33 - Provision amount
		private String r33_description;
		private BigDecimal r33_value;

		// Default Constructor
		public BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY() {
			super();
		}

		// Getters and Setters
		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setReport_resubdate(Date report_resubdate) {
			this.report_resubdate = report_resubdate;
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

		public String getR5_label() {
			return r5_label;
		}

		public void setR5_label(String r5_label) {
			this.r5_label = r5_label;
		}

		public String getR5_rs_in_lacs() {
			return r5_rs_in_lacs;
		}

		public void setR5_rs_in_lacs(String r5_rs_in_lacs) {
			this.r5_rs_in_lacs = r5_rs_in_lacs;
		}

		public String getR6_description() {
			return r6_description;
		}

		public void setR6_description(String r6_description) {
			this.r6_description = r6_description;
		}

		public String getR6_value() {
			return r6_value;
		}

		public void setR6_value(String r6_value) {
			this.r6_value = r6_value;
		}

		public String getR7_description() {
			return r7_description;
		}

		public void setR7_description(String r7_description) {
			this.r7_description = r7_description;
		}

		public BigDecimal getR7_value() {
			return r7_value;
		}

		public void setR7_value(BigDecimal r7_value) {
			this.r7_value = r7_value;
		}

		public String getR8_description() {
			return r8_description;
		}

		public void setR8_description(String r8_description) {
			this.r8_description = r8_description;
		}

		public BigDecimal getR8_value() {
			return r8_value;
		}

		public void setR8_value(BigDecimal r8_value) {
			this.r8_value = r8_value;
		}

		public String getR25_label() {
			return r25_label;
		}

		public void setR25_label(String r25_label) {
			this.r25_label = r25_label;
		}

		public String getR25_rs_in_lacs() {
			return r25_rs_in_lacs;
		}

		public void setR25_rs_in_lacs(String r25_rs_in_lacs) {
			this.r25_rs_in_lacs = r25_rs_in_lacs;
		}

		public String getR27_description() {
			return r27_description;
		}

		public void setR27_description(String r27_description) {
			this.r27_description = r27_description;
		}

		public BigDecimal getR27_value() {
			return r27_value;
		}

		public void setR27_value(BigDecimal r27_value) {
			this.r27_value = r27_value;
		}

		public String getR28_description() {
			return r28_description;
		}

		public void setR28_description(String r28_description) {
			this.r28_description = r28_description;
		}

		public BigDecimal getR28_value() {
			return r28_value;
		}

		public void setR28_value(BigDecimal r28_value) {
			this.r28_value = r28_value;
		}

		public String getR29_description() {
			return r29_description;
		}

		public void setR29_description(String r29_description) {
			this.r29_description = r29_description;
		}

		public BigDecimal getR29_value() {
			return r29_value;
		}

		public void setR29_value(BigDecimal r29_value) {
			this.r29_value = r29_value;
		}

		public String getR30_description() {
			return r30_description;
		}

		public void setR30_description(String r30_description) {
			this.r30_description = r30_description;
		}

		public BigDecimal getR30_value() {
			return r30_value;
		}

		public void setR30_value(BigDecimal r30_value) {
			this.r30_value = r30_value;
		}

		public String getR31_description() {
			return r31_description;
		}

		public void setR31_description(String r31_description) {
			this.r31_description = r31_description;
		}

		public BigDecimal getR31_value() {
			return r31_value;
		}

		public void setR31_value(BigDecimal r31_value) {
			this.r31_value = r31_value;
		}

		public String getR32_description() {
			return r32_description;
		}

		public void setR32_description(String r32_description) {
			this.r32_description = r32_description;
		}

		public BigDecimal getR32_value() {
			return r32_value;
		}

		public void setR32_value(BigDecimal r32_value) {
			this.r32_value = r32_value;
		}

		public String getR33_description() {
			return r33_description;
		}

		public void setR33_description(String r33_description) {
			this.r33_description = r33_description;
		}

		public BigDecimal getR33_value() {
			return r33_value;
		}

		public void setR33_value(BigDecimal r33_value) {
			this.r33_value = r33_value;
		}
	}

	// =====================================================
	// ROW MAPPER: BRRS_UFCE_CALCULATION_DETAILTABLE_ROWMAPPER
	// =====================================================
	public class BRRS_UFCE_CALCULATION_DETAILTABLE_ROWMAPPER
			implements RowMapper<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> {

		@Override
		public BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY obj = new BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY();

			obj.setSno(rs.getLong("SNO"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setReport_resubdate(rs.getDate("REPORT_RESUBDATE"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setR5_label(rs.getString("R5_LABEL"));
			obj.setR5_rs_in_lacs(rs.getString("R5_RS_IN_LACS"));

			obj.setR6_description(rs.getString("R6_DESCRIPTION"));
			obj.setR6_value(rs.getString("R6_VALUE"));

			obj.setR7_description(rs.getString("R7_DESCRIPTION"));
			obj.setR7_value(rs.getBigDecimal("R7_VALUE"));

			obj.setR8_description(rs.getString("R8_DESCRIPTION"));
			obj.setR8_value(rs.getBigDecimal("R8_VALUE"));

			obj.setR25_label(rs.getString("R25_LABEL"));
			obj.setR25_rs_in_lacs(rs.getString("R25_RS_IN_LACS"));

			obj.setR27_description(rs.getString("R27_DESCRIPTION"));
			obj.setR27_value(rs.getBigDecimal("R27_VALUE"));

			obj.setR28_description(rs.getString("R28_DESCRIPTION"));
			obj.setR28_value(rs.getBigDecimal("R28_VALUE"));

			obj.setR29_description(rs.getString("R29_DESCRIPTION"));
			obj.setR29_value(rs.getBigDecimal("R29_VALUE"));

			obj.setR30_description(rs.getString("R30_DESCRIPTION"));
			obj.setR30_value(rs.getBigDecimal("R30_VALUE"));

			obj.setR31_description(rs.getString("R31_DESCRIPTION"));
			obj.setR31_value(rs.getBigDecimal("R31_VALUE"));

			obj.setR32_description(rs.getString("R32_DESCRIPTION"));
			obj.setR32_value(rs.getBigDecimal("R32_VALUE"));

			obj.setR33_description(rs.getString("R33_DESCRIPTION"));
			obj.setR33_value(rs.getBigDecimal("R33_VALUE"));

			return obj;
		}
	}

	// =====================================================
	// ENTITY: BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY
	// Table: BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE
	// =====================================================
	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE")
	public static class BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY {

		@Id
		private Long sno;

		// Metadata
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_resubdate;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Archive specific
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date archive_date;
		private String archive_reason;
		private String archived_by;

		// Labels
		private String r5_label;
		private String r5_rs_in_lacs;

		// R6 - Provision above 25 Cr
		private String r6_description;
		private String r6_value;

		// R7 - Provision upto 25 Cr
		private String r7_description;
		private BigDecimal r7_value;

		// R8 - Total
		private String r8_description;
		private BigDecimal r8_value;

		// R25 - Calculation header
		private String r25_label;
		private String r25_rs_in_lacs;

		// R27 to R33
		private String r27_description;
		private BigDecimal r27_value;
		private String r28_description;
		private BigDecimal r28_value;
		private String r29_description;
		private BigDecimal r29_value;
		private String r30_description;
		private BigDecimal r30_value;
		private String r31_description;
		private BigDecimal r31_value;
		private String r32_description;
		private BigDecimal r32_value;
		private String r33_description;
		private BigDecimal r33_value;

		// Default Constructor
		public BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY() {
			super();
		}

		// Getters and Setters
		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setReport_resubdate(Date report_resubdate) {
			this.report_resubdate = report_resubdate;
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

		public Date getArchive_date() {
			return archive_date;
		}

		public void setArchive_date(Date archive_date) {
			this.archive_date = archive_date;
		}

		public String getArchive_reason() {
			return archive_reason;
		}

		public void setArchive_reason(String archive_reason) {
			this.archive_reason = archive_reason;
		}

		public String getArchived_by() {
			return archived_by;
		}

		public void setArchived_by(String archived_by) {
			this.archived_by = archived_by;
		}

		public String getR5_label() {
			return r5_label;
		}

		public void setR5_label(String r5_label) {
			this.r5_label = r5_label;
		}

		public String getR5_rs_in_lacs() {
			return r5_rs_in_lacs;
		}

		public void setR5_rs_in_lacs(String r5_rs_in_lacs) {
			this.r5_rs_in_lacs = r5_rs_in_lacs;
		}

		public String getR6_description() {
			return r6_description;
		}

		public void setR6_description(String r6_description) {
			this.r6_description = r6_description;
		}

		public String getR6_value() {
			return r6_value;
		}

		public void setR6_value(String r6_value) {
			this.r6_value = r6_value;
		}

		public String getR7_description() {
			return r7_description;
		}

		public void setR7_description(String r7_description) {
			this.r7_description = r7_description;
		}

		public BigDecimal getR7_value() {
			return r7_value;
		}

		public void setR7_value(BigDecimal r7_value) {
			this.r7_value = r7_value;
		}

		public String getR8_description() {
			return r8_description;
		}

		public void setR8_description(String r8_description) {
			this.r8_description = r8_description;
		}

		public BigDecimal getR8_value() {
			return r8_value;
		}

		public void setR8_value(BigDecimal r8_value) {
			this.r8_value = r8_value;
		}

		public String getR25_label() {
			return r25_label;
		}

		public void setR25_label(String r25_label) {
			this.r25_label = r25_label;
		}

		public String getR25_rs_in_lacs() {
			return r25_rs_in_lacs;
		}

		public void setR25_rs_in_lacs(String r25_rs_in_lacs) {
			this.r25_rs_in_lacs = r25_rs_in_lacs;
		}

		public String getR27_description() {
			return r27_description;
		}

		public void setR27_description(String r27_description) {
			this.r27_description = r27_description;
		}

		public BigDecimal getR27_value() {
			return r27_value;
		}

		public void setR27_value(BigDecimal r27_value) {
			this.r27_value = r27_value;
		}

		public String getR28_description() {
			return r28_description;
		}

		public void setR28_description(String r28_description) {
			this.r28_description = r28_description;
		}

		public BigDecimal getR28_value() {
			return r28_value;
		}

		public void setR28_value(BigDecimal r28_value) {
			this.r28_value = r28_value;
		}

		public String getR29_description() {
			return r29_description;
		}

		public void setR29_description(String r29_description) {
			this.r29_description = r29_description;
		}

		public BigDecimal getR29_value() {
			return r29_value;
		}

		public void setR29_value(BigDecimal r29_value) {
			this.r29_value = r29_value;
		}

		public String getR30_description() {
			return r30_description;
		}

		public void setR30_description(String r30_description) {
			this.r30_description = r30_description;
		}

		public BigDecimal getR30_value() {
			return r30_value;
		}

		public void setR30_value(BigDecimal r30_value) {
			this.r30_value = r30_value;
		}

		public String getR31_description() {
			return r31_description;
		}

		public void setR31_description(String r31_description) {
			this.r31_description = r31_description;
		}

		public BigDecimal getR31_value() {
			return r31_value;
		}

		public void setR31_value(BigDecimal r31_value) {
			this.r31_value = r31_value;
		}

		public String getR32_description() {
			return r32_description;
		}

		public void setR32_description(String r32_description) {
			this.r32_description = r32_description;
		}

		public BigDecimal getR32_value() {
			return r32_value;
		}

		public void setR32_value(BigDecimal r32_value) {
			this.r32_value = r32_value;
		}

		public String getR33_description() {
			return r33_description;
		}

		public void setR33_description(String r33_description) {
			this.r33_description = r33_description;
		}

		public BigDecimal getR33_value() {
			return r33_value;
		}

		public void setR33_value(BigDecimal r33_value) {
			this.r33_value = r33_value;
		}
	}

	// =====================================================
	// ROW MAPPER: BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ROWMAPPER
	// =====================================================
	public class BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ROWMAPPER
			implements RowMapper<BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY> {

		@Override
		public BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY obj = new BRRS_UFCE_CALCULATION_ARCHIVAL_DETAILTABLE_ENTITY();

			obj.setSno(rs.getLong("SNO"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setReport_resubdate(rs.getDate("REPORT_RESUBDATE"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setArchive_date(rs.getDate("ARCHIVE_DATE"));
			obj.setArchive_reason(rs.getString("ARCHIVE_REASON"));
			obj.setArchived_by(rs.getString("ARCHIVED_BY"));

			obj.setR5_label(rs.getString("R5_LABEL"));
			obj.setR5_rs_in_lacs(rs.getString("R5_RS_IN_LACS"));

			obj.setR6_description(rs.getString("R6_DESCRIPTION"));
			obj.setR6_value(rs.getString("R6_VALUE"));

			obj.setR7_description(rs.getString("R7_DESCRIPTION"));
			obj.setR7_value(rs.getBigDecimal("R7_VALUE"));

			obj.setR8_description(rs.getString("R8_DESCRIPTION"));
			obj.setR8_value(rs.getBigDecimal("R8_VALUE"));

			obj.setR25_label(rs.getString("R25_LABEL"));
			obj.setR25_rs_in_lacs(rs.getString("R25_RS_IN_LACS"));

			obj.setR27_description(rs.getString("R27_DESCRIPTION"));
			obj.setR27_value(rs.getBigDecimal("R27_VALUE"));

			obj.setR28_description(rs.getString("R28_DESCRIPTION"));
			obj.setR28_value(rs.getBigDecimal("R28_VALUE"));

			obj.setR29_description(rs.getString("R29_DESCRIPTION"));
			obj.setR29_value(rs.getBigDecimal("R29_VALUE"));

			obj.setR30_description(rs.getString("R30_DESCRIPTION"));
			obj.setR30_value(rs.getBigDecimal("R30_VALUE"));

			obj.setR31_description(rs.getString("R31_DESCRIPTION"));
			obj.setR31_value(rs.getBigDecimal("R31_VALUE"));

			obj.setR32_description(rs.getString("R32_DESCRIPTION"));
			obj.setR32_value(rs.getBigDecimal("R32_VALUE"));

			obj.setR33_description(rs.getString("R33_DESCRIPTION"));
			obj.setR33_value(rs.getBigDecimal("R33_VALUE"));

			return obj;
		}
	}

	// =====================================================
	// ENTITY: BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY
	// Table: BRRS_UFCE_CALCULATION_SUMMARYTABLE
	// =====================================================
	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_SUMMARYTABLE")
	public static class BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY {

		@Id
		private Long sno;

		// Metadata
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_resubdate;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Labels
		private String r5_label;
		private String r5_rs_in_lacs;
		private String r6_description;
		private String r6_value;
		private String r7_description;
		private BigDecimal r7_value;
		private String r8_description;
		private BigDecimal r8_value;
		private String r25_label;
		private String r25_rs_in_lacs;
		private String r27_description;
		private BigDecimal r27_value;
		private String r28_description;
		private BigDecimal r28_value;
		private String r29_description;
		private BigDecimal r29_value;
		private String r30_description;
		private BigDecimal r30_value;
		private String r31_description;
		private BigDecimal r31_value;
		private String r32_description;
		private BigDecimal r32_value;
		private String r33_description;
		private BigDecimal r33_value;

		// Default Constructor
		public BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY() {
			super();
		}

		// Getters and Setters (same as Detail entity)
		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setReport_resubdate(Date report_resubdate) {
			this.report_resubdate = report_resubdate;
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

		public String getR5_label() {
			return r5_label;
		}

		public void setR5_label(String r5_label) {
			this.r5_label = r5_label;
		}

		public String getR5_rs_in_lacs() {
			return r5_rs_in_lacs;
		}

		public void setR5_rs_in_lacs(String r5_rs_in_lacs) {
			this.r5_rs_in_lacs = r5_rs_in_lacs;
		}

		public String getR6_description() {
			return r6_description;
		}

		public void setR6_description(String r6_description) {
			this.r6_description = r6_description;
		}

		public String getR6_value() {
			return r6_value;
		}

		public void setR6_value(String r6_value) {
			this.r6_value = r6_value;
		}

		public String getR7_description() {
			return r7_description;
		}

		public void setR7_description(String r7_description) {
			this.r7_description = r7_description;
		}

		public BigDecimal getR7_value() {
			return r7_value;
		}

		public void setR7_value(BigDecimal r7_value) {
			this.r7_value = r7_value;
		}

		public String getR8_description() {
			return r8_description;
		}

		public void setR8_description(String r8_description) {
			this.r8_description = r8_description;
		}

		public BigDecimal getR8_value() {
			return r8_value;
		}

		public void setR8_value(BigDecimal r8_value) {
			this.r8_value = r8_value;
		}

		public String getR25_label() {
			return r25_label;
		}

		public void setR25_label(String r25_label) {
			this.r25_label = r25_label;
		}

		public String getR25_rs_in_lacs() {
			return r25_rs_in_lacs;
		}

		public void setR25_rs_in_lacs(String r25_rs_in_lacs) {
			this.r25_rs_in_lacs = r25_rs_in_lacs;
		}

		public String getR27_description() {
			return r27_description;
		}

		public void setR27_description(String r27_description) {
			this.r27_description = r27_description;
		}

		public BigDecimal getR27_value() {
			return r27_value;
		}

		public void setR27_value(BigDecimal r27_value) {
			this.r27_value = r27_value;
		}

		public String getR28_description() {
			return r28_description;
		}

		public void setR28_description(String r28_description) {
			this.r28_description = r28_description;
		}

		public BigDecimal getR28_value() {
			return r28_value;
		}

		public void setR28_value(BigDecimal r28_value) {
			this.r28_value = r28_value;
		}

		public String getR29_description() {
			return r29_description;
		}

		public void setR29_description(String r29_description) {
			this.r29_description = r29_description;
		}

		public BigDecimal getR29_value() {
			return r29_value;
		}

		public void setR29_value(BigDecimal r29_value) {
			this.r29_value = r29_value;
		}

		public String getR30_description() {
			return r30_description;
		}

		public void setR30_description(String r30_description) {
			this.r30_description = r30_description;
		}

		public BigDecimal getR30_value() {
			return r30_value;
		}

		public void setR30_value(BigDecimal r30_value) {
			this.r30_value = r30_value;
		}

		public String getR31_description() {
			return r31_description;
		}

		public void setR31_description(String r31_description) {
			this.r31_description = r31_description;
		}

		public BigDecimal getR31_value() {
			return r31_value;
		}

		public void setR31_value(BigDecimal r31_value) {
			this.r31_value = r31_value;
		}

		public String getR32_description() {
			return r32_description;
		}

		public void setR32_description(String r32_description) {
			this.r32_description = r32_description;
		}

		public BigDecimal getR32_value() {
			return r32_value;
		}

		public void setR32_value(BigDecimal r32_value) {
			this.r32_value = r32_value;
		}

		public String getR33_description() {
			return r33_description;
		}

		public void setR33_description(String r33_description) {
			this.r33_description = r33_description;
		}

		public BigDecimal getR33_value() {
			return r33_value;
		}

		public void setR33_value(BigDecimal r33_value) {
			this.r33_value = r33_value;
		}
	}

	// =====================================================
	// ROW MAPPER: BRRS_UFCE_CALCULATION_SUMMARYTABLE_ROWMAPPER
	// =====================================================
	public class BRRS_UFCE_CALCULATION_SUMMARYTABLE_ROWMAPPER
			implements RowMapper<BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY> {

		@Override
		public BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY obj = new BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY();

			obj.setSno(rs.getLong("SNO"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setReport_resubdate(rs.getDate("REPORT_RESUBDATE"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setR5_label(rs.getString("R5_LABEL"));
			obj.setR5_rs_in_lacs(rs.getString("R5_RS_IN_LACS"));

			obj.setR6_description(rs.getString("R6_DESCRIPTION"));
			obj.setR6_value(rs.getString("R6_VALUE"));

			obj.setR7_description(rs.getString("R7_DESCRIPTION"));
			obj.setR7_value(rs.getBigDecimal("R7_VALUE"));

			obj.setR8_description(rs.getString("R8_DESCRIPTION"));
			obj.setR8_value(rs.getBigDecimal("R8_VALUE"));

			obj.setR25_label(rs.getString("R25_LABEL"));
			obj.setR25_rs_in_lacs(rs.getString("R25_RS_IN_LACS"));

			obj.setR27_description(rs.getString("R27_DESCRIPTION"));
			obj.setR27_value(rs.getBigDecimal("R27_VALUE"));

			obj.setR28_description(rs.getString("R28_DESCRIPTION"));
			obj.setR28_value(rs.getBigDecimal("R28_VALUE"));

			obj.setR29_description(rs.getString("R29_DESCRIPTION"));
			obj.setR29_value(rs.getBigDecimal("R29_VALUE"));

			obj.setR30_description(rs.getString("R30_DESCRIPTION"));
			obj.setR30_value(rs.getBigDecimal("R30_VALUE"));

			obj.setR31_description(rs.getString("R31_DESCRIPTION"));
			obj.setR31_value(rs.getBigDecimal("R31_VALUE"));

			obj.setR32_description(rs.getString("R32_DESCRIPTION"));
			obj.setR32_value(rs.getBigDecimal("R32_VALUE"));

			obj.setR33_description(rs.getString("R33_DESCRIPTION"));
			obj.setR33_value(rs.getBigDecimal("R33_VALUE"));

			return obj;
		}
	}

	// =====================================================
	// ENTITY: BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY
	// Table: BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE
	// =====================================================
	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE")
	public static class BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY {

		@Id
		private Long sno;

		// Metadata
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date report_resubdate;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Archive specific
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date archive_date;
		private String archive_reason;
		private String archived_by;

		// Labels
		private String r5_label;
		private String r5_rs_in_lacs;
		private String r6_description;
		private String r6_value;
		private String r7_description;
		private BigDecimal r7_value;
		private String r8_description;
		private BigDecimal r8_value;
		private String r25_label;
		private String r25_rs_in_lacs;
		private String r27_description;
		private BigDecimal r27_value;
		private String r28_description;
		private BigDecimal r28_value;
		private String r29_description;
		private BigDecimal r29_value;
		private String r30_description;
		private BigDecimal r30_value;
		private String r31_description;
		private BigDecimal r31_value;
		private String r32_description;
		private BigDecimal r32_value;
		private String r33_description;
		private BigDecimal r33_value;

		// Default Constructor
		public BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY() {
			super();
		}

		// Getters and Setters (same as Summary entity + archive)
		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setReport_resubdate(Date report_resubdate) {
			this.report_resubdate = report_resubdate;
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

		public Date getArchive_date() {
			return archive_date;
		}

		public void setArchive_date(Date archive_date) {
			this.archive_date = archive_date;
		}

		public String getArchive_reason() {
			return archive_reason;
		}

		public void setArchive_reason(String archive_reason) {
			this.archive_reason = archive_reason;
		}

		public String getArchived_by() {
			return archived_by;
		}

		public void setArchived_by(String archived_by) {
			this.archived_by = archived_by;
		}

		public String getR5_label() {
			return r5_label;
		}

		public void setR5_label(String r5_label) {
			this.r5_label = r5_label;
		}

		public String getR5_rs_in_lacs() {
			return r5_rs_in_lacs;
		}

		public void setR5_rs_in_lacs(String r5_rs_in_lacs) {
			this.r5_rs_in_lacs = r5_rs_in_lacs;
		}

		public String getR6_description() {
			return r6_description;
		}

		public void setR6_description(String r6_description) {
			this.r6_description = r6_description;
		}

		public String getR6_value() {
			return r6_value;
		}

		public void setR6_value(String r6_value) {
			this.r6_value = r6_value;
		}

		public String getR7_description() {
			return r7_description;
		}

		public void setR7_description(String r7_description) {
			this.r7_description = r7_description;
		}

		public BigDecimal getR7_value() {
			return r7_value;
		}

		public void setR7_value(BigDecimal r7_value) {
			this.r7_value = r7_value;
		}

		public String getR8_description() {
			return r8_description;
		}

		public void setR8_description(String r8_description) {
			this.r8_description = r8_description;
		}

		public BigDecimal getR8_value() {
			return r8_value;
		}

		public void setR8_value(BigDecimal r8_value) {
			this.r8_value = r8_value;
		}

		public String getR25_label() {
			return r25_label;
		}

		public void setR25_label(String r25_label) {
			this.r25_label = r25_label;
		}

		public String getR25_rs_in_lacs() {
			return r25_rs_in_lacs;
		}

		public void setR25_rs_in_lacs(String r25_rs_in_lacs) {
			this.r25_rs_in_lacs = r25_rs_in_lacs;
		}

		public String getR27_description() {
			return r27_description;
		}

		public void setR27_description(String r27_description) {
			this.r27_description = r27_description;
		}

		public BigDecimal getR27_value() {
			return r27_value;
		}

		public void setR27_value(BigDecimal r27_value) {
			this.r27_value = r27_value;
		}

		public String getR28_description() {
			return r28_description;
		}

		public void setR28_description(String r28_description) {
			this.r28_description = r28_description;
		}

		public BigDecimal getR28_value() {
			return r28_value;
		}

		public void setR28_value(BigDecimal r28_value) {
			this.r28_value = r28_value;
		}

		public String getR29_description() {
			return r29_description;
		}

		public void setR29_description(String r29_description) {
			this.r29_description = r29_description;
		}

		public BigDecimal getR29_value() {
			return r29_value;
		}

		public void setR29_value(BigDecimal r29_value) {
			this.r29_value = r29_value;
		}

		public String getR30_description() {
			return r30_description;
		}

		public void setR30_description(String r30_description) {
			this.r30_description = r30_description;
		}

		public BigDecimal getR30_value() {
			return r30_value;
		}

		public void setR30_value(BigDecimal r30_value) {
			this.r30_value = r30_value;
		}

		public String getR31_description() {
			return r31_description;
		}

		public void setR31_description(String r31_description) {
			this.r31_description = r31_description;
		}

		public BigDecimal getR31_value() {
			return r31_value;
		}

		public void setR31_value(BigDecimal r31_value) {
			this.r31_value = r31_value;
		}

		public String getR32_description() {
			return r32_description;
		}

		public void setR32_description(String r32_description) {
			this.r32_description = r32_description;
		}

		public BigDecimal getR32_value() {
			return r32_value;
		}

		public void setR32_value(BigDecimal r32_value) {
			this.r32_value = r32_value;
		}

		public String getR33_description() {
			return r33_description;
		}

		public void setR33_description(String r33_description) {
			this.r33_description = r33_description;
		}

		public BigDecimal getR33_value() {
			return r33_value;
		}

		public void setR33_value(BigDecimal r33_value) {
			this.r33_value = r33_value;
		}
	}

	// =====================================================
	// ROW MAPPER: BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ROWMAPPER
	// =====================================================
	public class BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ROWMAPPER
			implements RowMapper<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY> {

		@Override
		public BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY mapRow(ResultSet rs, int rowNum) throws SQLException {

			BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY obj = new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_ENTITY();

			obj.setSno(rs.getLong("SNO"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setReport_resubdate(rs.getDate("REPORT_RESUBDATE"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));

			obj.setArchive_date(rs.getDate("ARCHIVE_DATE"));
			obj.setArchive_reason(rs.getString("ARCHIVE_REASON"));
			obj.setArchived_by(rs.getString("ARCHIVED_BY"));

			obj.setR5_label(rs.getString("R5_LABEL"));
			obj.setR5_rs_in_lacs(rs.getString("R5_RS_IN_LACS"));

			obj.setR6_description(rs.getString("R6_DESCRIPTION"));
			obj.setR6_value(rs.getString("R6_VALUE"));

			obj.setR7_description(rs.getString("R7_DESCRIPTION"));
			obj.setR7_value(rs.getBigDecimal("R7_VALUE"));

			obj.setR8_description(rs.getString("R8_DESCRIPTION"));
			obj.setR8_value(rs.getBigDecimal("R8_VALUE"));

			obj.setR25_label(rs.getString("R25_LABEL"));
			obj.setR25_rs_in_lacs(rs.getString("R25_RS_IN_LACS"));

			obj.setR27_description(rs.getString("R27_DESCRIPTION"));
			obj.setR27_value(rs.getBigDecimal("R27_VALUE"));

			obj.setR28_description(rs.getString("R28_DESCRIPTION"));
			obj.setR28_value(rs.getBigDecimal("R28_VALUE"));

			obj.setR29_description(rs.getString("R29_DESCRIPTION"));
			obj.setR29_value(rs.getBigDecimal("R29_VALUE"));

			obj.setR30_description(rs.getString("R30_DESCRIPTION"));
			obj.setR30_value(rs.getBigDecimal("R30_VALUE"));

			obj.setR31_description(rs.getString("R31_DESCRIPTION"));
			obj.setR31_value(rs.getBigDecimal("R31_VALUE"));

			obj.setR32_description(rs.getString("R32_DESCRIPTION"));
			obj.setR32_value(rs.getBigDecimal("R32_VALUE"));

			obj.setR33_description(rs.getString("R33_DESCRIPTION"));
			obj.setR33_value(rs.getBigDecimal("R33_VALUE"));

			return obj;
		}
	}
}