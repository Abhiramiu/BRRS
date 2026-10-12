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
import org.springframework.transaction.annotation.Transactional;

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

	private final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// =====================================================
	// DATE HANDLING HELPERS
	// =====================================================

	public Date parseFlexibleDate(String dateStr) {
		return parseDate(dateStr);
	}

	private Date parseDate(String dateStr) {
		if (dateStr == null || dateStr.trim().isEmpty())
			return null;
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(dateStr.trim());
		} catch (Exception e) {
			try {
				return new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).parse(dateStr.trim());
			} catch (Exception ex) {
				try {
					return new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(dateStr.trim());
				} catch (Exception ex2) {
					logger.error("Failed to parse date: {}", dateStr);
					return null;
				}
			}
		}
	}

	private BigDecimal nvl(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}

	private String nvlStr(String value) {
		return value != null ? value : "";
	}

	// =====================================================
	// CELL + STYLE HELPERS FOR EXCEL EXPORT
	// =====================================================

	private void setCellValue(Row row, int column, Object value, CellStyle style) {
		Cell cell = row.createCell(column);
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
		} else if (value instanceof BigDecimal) {
			cell.setCellValue(((BigDecimal) value).doubleValue());
		} else if (value instanceof Date) {
			cell.setCellValue((Date) value);
		} else if (value instanceof Long) {
			cell.setCellValue(((Long) value).doubleValue());
		} else if (value instanceof Integer) {
			cell.setCellValue(((Integer) value).doubleValue());
		} else if (value instanceof Double) {
			cell.setCellValue((Double) value);
		} else {
			cell.setCellValue(value.toString());
		}
		if (style != null)
			cell.setCellStyle(style);
	}

	private CellStyle createTextStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 11);
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setWrapText(true);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	private CellStyle createNumberStyle(Workbook workbook) {
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 11);
		CreationHelper helper = workbook.getCreationHelper();
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setDataFormat(helper.createDataFormat().getFormat("#,##0.00"));
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

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
	// SECTION 1: BRRS_UFCE_CALCULATION_SUMMARYTABLE
	// =====================================================

	public List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY> getCalculationSummaryDataByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BRRS_UFCE_CALCULATION_SUMMARYTABLE_ROWMAPPER());
	}

	public BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY findCalculationSummaryByReportDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate },
					new BRRS_UFCE_CALCULATION_SUMMARYTABLE_ROWMAPPER());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY> getCalculationSummaryDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ? AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_SUMMARYTABLE_ROWMAPPER());
	}

	// =====================================================
	// SECTION 2: BRRS_UFCE_CALCULATION_DETAILTABLE
	// =====================================================

	public List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> getCalculationDetailDataByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_DETAILTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BRRS_UFCE_CALCULATION_DETAILTABLE_ROWMAPPER());
	}

	public BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY findCalculationDetailByReportDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_DETAILTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate },
					new BRRS_UFCE_CALCULATION_DETAILTABLE_ROWMAPPER());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> getCalculationDetailDataByDateAndVersion(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_DETAILTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ? AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_DETAILTABLE_ROWMAPPER());
	}

	// =====================================================
	// SECTION 3: ARCHIVAL SUMMARY (CALCULATION SHAPE)
	// =====================================================

	public List<Object[]> getCalculationArchivalSummaryList() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, 'CALC' AS SRC "
				+ "FROM BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE"), rs.getString("SRC") });
	}

	public List<BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY> getCalculationArchivalSummaryDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ROWMAPPER());
	}

	public BigDecimal findMaxCalculationArchivalSummaryVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return result != null ? result : BigDecimal.ZERO;
		} catch (EmptyResultDataAccessException e) {
			return BigDecimal.ZERO;
		}
	}

	public List<BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY> getAllArchivalWithVersion() {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE "
				+ "              WHERE REPORT_VERSION IS NOT NULL "
				+ "              GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, new BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ROWMAPPER());
	}

	public List<BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY> getAllResubWithVersion() {
		return getAllArchivalWithVersion();
	}

	// =====================================================
	// SECTION 4: ARCHIVAL DETAIL
	// =====================================================

	public List<Object[]> getCalculationArchivalDetailList() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE FROM "
				+ "BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE " + "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	public List<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> getCalculationArchivalDetailDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ROWMAPPER());
	}

	// =====================================================
	// SECTION 5: MANUAL ENTRY - LIVE
	// =====================================================

	public List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> getManualEntrySummaryDataByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N' " + "AND REPORT_VERSION = ("
				+ "    SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL "
				+ "    WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'" + ")";
		List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> rows = jdbcTemplate.query(sql,
				new Object[] { reportDate, reportDate }, new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ROWMAPPER());
		logger.info("getManualEntrySummaryDataByDate: date={} -> {} row(s) returned", reportDate, rows.size());
		return rows;
	}

	public BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY findManualEntrySummaryByReportDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N' " + "AND REPORT_VERSION = ("
				+ "    SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL "
				+ "    WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'" + ")";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate, reportDate },
					new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ROWMAPPER());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> getManualEntrySummaryDataByDateAndVersion(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ? AND DEL_FLG = 'N'";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	// =====================================================
	// SECTION 6: MANUAL ENTRY - ARCHIVAL
	// =====================================================

	public List<Object[]> getManualEntryArchivalSummaryList() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, 'MANUAL' AS SRC "
				+ "FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE"), rs.getString("SRC") });
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> getManualEntryArchivalSummaryDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	public BigDecimal findMaxManualEntryArchivalSummaryVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return result != null ? result : BigDecimal.ZERO;
		} catch (EmptyResultDataAccessException e) {
			return BigDecimal.ZERO;
		}
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> getAllManualArchivalWithVersion() {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
				+ "WHERE SNO IN (SELECT MIN(SNO) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
				+ "              WHERE REPORT_VERSION IS NOT NULL "
				+ "              GROUP BY REPORT_DATE, REPORT_VERSION) "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> getAllManualResubWithVersion() {
		return getAllManualArchivalWithVersion();
	}

	// =====================================================
	// SECTION 7: RESUB READS + VERSION HELPERS
	// =====================================================

	public List<BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY> getResubCalculationSummaryDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ROWMAPPER());
	}

	public List<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> getResubCalculationDetailDataByDate(Date reportDate,
			BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ROWMAPPER());
	}

	public List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> getResubManualEntrySummaryDataByDate(
			Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT * FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ROWMAPPER());
	}

	public BigDecimal findMaxResubCalculationVersion(Date reportDate) {
		return findMaxCalculationArchivalSummaryVersion(reportDate);
	}

	public BigDecimal findMaxResubManualEntryVersion(Date reportDate) {
		return findMaxManualEntryArchivalSummaryVersion(reportDate);
	}

	public BigDecimal getLatestManualEntryVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
			BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			BigDecimal version = result != null ? result : BigDecimal.ZERO;
			logger.info("getLatestManualEntryVersion: date={} -> version={}", reportDate, version);
			return version;
		} catch (EmptyResultDataAccessException e) {
			logger.info("getLatestManualEntryVersion: no rows for date={} -> returning ZERO", reportDate);
			return BigDecimal.ZERO;
		} catch (Exception e) {
			logger.warn("Error getting latest manual entry version: {}", e.getMessage(), e);
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal getLatestCalculationVersion(Date reportDate) {
		try {
			String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_UFCE_CALCULATION_SUMMARYTABLE "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DEL_FLG = 'N'";
			BigDecimal result = jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
			return result != null ? result : BigDecimal.ZERO;
		} catch (EmptyResultDataAccessException e) {
			return BigDecimal.ZERO;
		} catch (Exception e) {
			logger.warn("Error getting latest calculation version: {}", e.getMessage());
			return BigDecimal.ZERO;
		}
	}

	private BigDecimal getNextCalculationArchivalVersion(Date reportDate) {
		try {
			BigDecimal maxVersion = findMaxCalculationArchivalSummaryVersion(reportDate);
			return (maxVersion == null || maxVersion.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ONE
					: maxVersion.add(BigDecimal.ONE);
		} catch (Exception e) {
			logger.warn("Error getting next calculation archival version: {}", e.getMessage());
			return BigDecimal.ONE;
		}
	}

	private BigDecimal getNextManualEntryArchivalVersion(Date reportDate) {
		try {
			BigDecimal maxVersion = findMaxManualEntryArchivalSummaryVersion(reportDate);
			return (maxVersion == null || maxVersion.compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ONE
					: maxVersion.add(BigDecimal.ONE);
		} catch (Exception e) {
			logger.warn("Error getting next manual entry archival version: {}", e.getMessage());
			return BigDecimal.ONE;
		}
	}

	public BigDecimal findMaxArchivalVersion(Date reportDate) {
		return findMaxCalculationArchivalSummaryVersion(reportDate);
	}

	public BigDecimal findMaxManualArchivalVersion(Date reportDate) {
		return findMaxManualEntryArchivalSummaryVersion(reportDate);
	}

	// =====================================================
	// SECTION 8: SOFT DELETE (VERSION-AWARE)
	// =====================================================

	@Transactional
	public void softDeleteManualEntrySummary(Date reportDate, BigDecimal reportVersion) {
		String sql = "UPDATE BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL " + "SET DEL_FLG='Y', MODIFY_FLG='Y' "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		jdbcTemplate.update(sql, reportDate, reportVersion);
	}

	@Transactional
	public void softDeleteCalculationDetail(Date reportDate, BigDecimal reportVersion) {
		String sql = "UPDATE BRRS_UFCE_CALCULATION_DETAILTABLE " + "SET DEL_FLG='Y', MODIFY_FLG='Y' "
				+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		jdbcTemplate.update(sql, reportDate, reportVersion);
	}

	// =====================================================
	// SECTION 9: VIEWS
	// =====================================================

	public ModelAndView getUFCEView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();
		String userid = (req1 != null) ? (String) req1.getSession().getAttribute("USERID") : null;
		logger.info("UFCE getUFCEView user={} dtltype={} type={} version={}", userid, dtltype, type, version);

		String role = null;
		try {
			if (userid != null)
				role = userProfileRep.getUserRole(userid);
		} catch (Exception e) {
			logger.warn("Could not resolve user role for {}: {}", userid, e.getMessage());
		}
		md.addAttribute("role", role);

		try {
			Date dt = parseDate(todate);
			if (dt == null)
				dt = parseDate(fromdate);
			if (dt == null)
				dt = new Date();

			boolean isDetail = "detail".equalsIgnoreCase(dtltype);
			boolean isArchival = "ARCHIVAL".equalsIgnoreCase(type);
			boolean isResub = "RESUB".equalsIgnoreCase(type);

			if (isDetail) {
				if (isArchival && version != null) {
					List<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> detail = getCalculationArchivalDetailDataByDate(
							dt, version);
					mv.addObject("reportdetail", detail);
					mv.addObject("displaymode", "archivalDetail");
				} else if (isResub && version != null) {
					List<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> detail = getResubCalculationDetailDataByDate(
							dt, version);
					mv.addObject("reportdetail", detail);
					mv.addObject("displaymode", "resubDetail");
				} else {
					List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> detail;
					if (version != null) {
						detail = getCalculationDetailDataByDateAndVersion(dt, version);
						mv.addObject("displaymode", "normalDetailWithVersion");
					} else {
						detail = getCalculationDetailDataByDate(dt);
						mv.addObject("displaymode", "detail");
					}
					mv.addObject("reportdetail", detail);
				}
			} else {
				if (isArchival && version != null) {
					List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> summary = getManualEntryArchivalSummaryDataByDate(
							dt, version);
					List<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> detail = getCalculationArchivalDetailDataByDate(
							dt, version);
					mv.addObject("reportsummary", summary);
					mv.addObject("reportdetail", detail);
					mv.addObject("displaymode", "archivalSummary");
				} else if (isResub && version != null) {
					List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> summary = getResubManualEntrySummaryDataByDate(
							dt, version);
					List<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> detail = getResubCalculationDetailDataByDate(
							dt, version);
					mv.addObject("reportsummary", summary);
					mv.addObject("reportdetail", detail);
					mv.addObject("displaymode", "resubSummary");
				} else {
				    List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> summary;
				    List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> detail;
				    List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY> calcSummary;   // ← NEW

				    if (version != null) {
				        summary = getManualEntrySummaryDataByDateAndVersion(dt, version);
				        detail = getCalculationDetailDataByDateAndVersion(dt, version);
				        calcSummary = getCalculationSummaryDataByDateAndVersion(dt, version);   // ← NEW
				        mv.addObject("displaymode", "normalSummaryWithVersion");
				    } else {
				        summary = getManualEntrySummaryDataByDate(dt);
				        detail = getCalculationDetailDataByDate(dt);
				        calcSummary = getCalculationSummaryDataByDate(dt);   // ← NEW
				        mv.addObject("displaymode", "summary");
				    }
				    mv.addObject("reportsummary", summary);
				    mv.addObject("reportdetail", detail);
				    mv.addObject("calcSummary", calcSummary);   // ← NEW
				}
			}

			mv.addObject("reportid", reportId);
			mv.addObject("fromdate", fromdate);
			mv.addObject("todate", todate);
			mv.addObject("asondate", dateformat.format(dt));
			mv.addObject("currency", currency);
			mv.addObject("dtltype", dtltype);
			mv.addObject("type", type);
			mv.addObject("version", version);
			mv.addObject("report_date", dateformat.format(dt));

		} catch (Exception e) {
			logger.error("Error in getUFCEView: {}", e.getMessage(), e);
		}

		mv.setViewName("BRRS/UFCE_CALCULATION");
		return mv;
	}

	public ModelAndView getUFCEModifyView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {
		ModelAndView mv = new ModelAndView();
		String userid = (req1 != null) ? (String) req1.getSession().getAttribute("USERID") : null;
		String role = null;
		try {
			if (userid != null)
				role = userProfileRep.getUserRole(userid);
		} catch (Exception e) {
			logger.warn("getUFCEModifyView role lookup failed: {}", e.getMessage());
		}
		md.addAttribute("role", role);

		try {
			Date dt = parseDate(todate);
			if (dt == null)
				dt = parseDate(fromdate);
			if (dt == null)
				dt = new Date();

			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				mv.addObject("reportsummary", getManualEntryArchivalSummaryDataByDate(dt, version));
				mv.addObject("displaymode", "archivalModify");
				mv.addObject("isArchival", true);
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				mv.addObject("reportsummary", getResubManualEntrySummaryDataByDate(dt, version));
				mv.addObject("displaymode", "resubModify");
				mv.addObject("isResub", true);
			} else {
				mv.addObject("reportsummary", getManualEntrySummaryDataByDate(dt));
				mv.addObject("displaymode", "modify");
				mv.addObject("isNormal", true);
			}

			mv.addObject("reportid", reportId);
			mv.addObject("fromdate", fromdate);
			mv.addObject("todate", todate);
			mv.addObject("asondate", dateformat.format(dt));
			mv.addObject("currency", currency);
			mv.addObject("dtltype", dtltype);
			mv.addObject("type", type);
			mv.addObject("version", version);
			mv.addObject("report_date", dateformat.format(dt));
			mv.addObject("menu", reportId);

		} catch (Exception e) {
			logger.error("Error in getUFCEModifyView: {}", e.getMessage(), e);
		}

		mv.setViewName("BRRS/UFCE_CALCULATION");
		return mv;
	}

	// =====================================================
	// SECTION 10: UPDATE SUMMARY - MANUAL TABLE
	// =====================================================

	@Transactional
	public void updateSummaryReport(BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY updatedEntity) {
		logger.info("Came to UFCE Summary Update");
		if (updatedEntity == null || updatedEntity.getReport_date() == null) {
			throw new IllegalArgumentException("Updated entity and report date cannot be null");
		}

		BigDecimal versionToUse = updatedEntity.getReport_version();
		if (versionToUse == null || versionToUse.compareTo(BigDecimal.ZERO) == 0) {
			versionToUse = getLatestManualEntryVersion(updatedEntity.getReport_date());
		}

		logger.info("updateSummaryReport: resolved versionToUse={} for date={}", versionToUse,
				updatedEntity.getReport_date());

		if (versionToUse == null || versionToUse.compareTo(BigDecimal.ZERO) == 0) {
			logger.info("updateSummaryReport: no live row found for date {} -> inserting new",
					updatedEntity.getReport_date());
			insertNewSummaryRecord(updatedEntity);
			return;
		}

		List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> matches = getManualEntrySummaryDataByDateAndVersion(
				updatedEntity.getReport_date(), versionToUse);
		if (matches == null || matches.isEmpty()) {
			logger.info("updateSummaryReport: no live row for date={} version={} -> inserting new",
					updatedEntity.getReport_date(), versionToUse);
			insertNewSummaryRecord(updatedEntity);
			return;
		}

		BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY existingSummary = matches.get(0);
		BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY oldcopy = new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY();
		BeanUtils.copyProperties(existingSummary, oldcopy);

		try {
			validateNonNegativeValues(updatedEntity);

			BigDecimal totalAdvance = calculateTotalAdvance(updatedEntity);
			BigDecimal totalProvision = calculateTotalProvision(updatedEntity);
			updatedEntity.setR22_total_adv(totalAdvance);
			updatedEntity.setR22_provision(totalProvision);

			String updateSummarySQL = "UPDATE BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL SET "
			        + "R10_SERIAL_NO = ?, R10_SUBSIDIARY = ?, R10_TOTAL_ADV = ?, R10_PROVISION = ?, "
			        + "R11_SERIAL_NO = ?, R11_SUBSIDIARY = ?, R11_TOTAL_ADV = ?, R11_PROVISION = ?, "
			        + "R12_SERIAL_NO = ?, R12_SUBSIDIARY = ?, R12_TOTAL_ADV = ?, R12_PROVISION = ?, "
			        + "R13_SERIAL_NO = ?, R13_SUBSIDIARY = ?, R13_TOTAL_ADV = ?, R13_PROVISION = ?, "
			        + "R14_SERIAL_NO = ?, R14_SUBSIDIARY = ?, R14_TOTAL_ADV = ?, R14_PROVISION = ?, "
			        + "R15_SERIAL_NO = ?, R15_SUBSIDIARY = ?, R15_TOTAL_ADV = ?, R15_PROVISION = ?, "
			        + "R16_SERIAL_NO = ?, R16_SUBSIDIARY = ?, R16_TOTAL_ADV = ?, R16_PROVISION = ?, "
			        + "R17_SERIAL_NO = ?, R17_SUBSIDIARY = ?, R17_TOTAL_ADV = ?, R17_PROVISION = ?, "
			        + "R18_SERIAL_NO = ?, R18_SUBSIDIARY = ?, R18_TOTAL_ADV = ?, R18_PROVISION = ?, "
			        + "R19_SERIAL_NO = ?, R19_SUBSIDIARY = ?, R19_TOTAL_ADV = ?, R19_PROVISION = ?, "
			        + "R20_SERIAL_NO = ?, R20_SUBSIDIARY = ?, R20_TOTAL_ADV = ?, R20_PROVISION = ?, "
			        + "R21_SERIAL_NO = ?, R21_SUBSIDIARY = ?, R21_TOTAL_ADV = ?, R21_PROVISION = ?, "
			        + "R22_TOTAL_ADV = ?, R22_PROVISION = ?, "
			        + "MODIFY_FLG = 'Y', DEL_FLG = 'N' "
			        + "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			logger.info("updateSummaryReport DIAG: date={} versionToUse={}", updatedEntity.getReport_date(),
					versionToUse);
			logger.info("updateSummaryReport DIAG: R10_subsidiary='{}' R10_total_adv='{}' R10_provision='{}'",
					updatedEntity.getR10_subsidiary(), updatedEntity.getR10_total_adv(),
					updatedEntity.getR10_provision());
			logger.info("updateSummaryReport DIAG: R11_subsidiary='{}' R11_total_adv={} R11_provision={}",
					updatedEntity.getR11_subsidiary(), updatedEntity.getR11_total_adv(),
					updatedEntity.getR11_provision());
			logger.info("updateSummaryReport DIAG: R21_subsidiary='{}' R21_total_adv={} R21_provision={}",
					updatedEntity.getR21_subsidiary(), updatedEntity.getR21_total_adv(),
					updatedEntity.getR21_provision());
			logger.info("updateSummaryReport DIAG: totalAdvance={} totalProvision={}", totalAdvance, totalProvision);

			int rowsUpdated = jdbcTemplate.update(updateSummarySQL,
				    /* R10 */ nvlStr(updatedEntity.getR10_serial_no()),
				              nvlStr(updatedEntity.getR10_subsidiary()),
				              nvlStr(updatedEntity.getR10_total_adv()),
				              nvlStr(updatedEntity.getR10_provision()),
				    /* R11 */ nvl(updatedEntity.getR11_serial_no()),
				              nvlStr(updatedEntity.getR11_subsidiary()),
				              nvl(updatedEntity.getR11_total_adv()),
				              nvl(updatedEntity.getR11_provision()),
				    /* R12 */ nvl(updatedEntity.getR12_serial_no()),
				              nvlStr(updatedEntity.getR12_subsidiary()),
				              nvl(updatedEntity.getR12_total_adv()),
				              nvl(updatedEntity.getR12_provision()),
				    /* R13 */ nvl(updatedEntity.getR13_serial_no()),
				              nvlStr(updatedEntity.getR13_subsidiary()),
				              nvl(updatedEntity.getR13_total_adv()),
				              nvl(updatedEntity.getR13_provision()),
				    /* R14 */ nvl(updatedEntity.getR14_serial_no()),
				              nvlStr(updatedEntity.getR14_subsidiary()),
				              nvl(updatedEntity.getR14_total_adv()),
				              nvl(updatedEntity.getR14_provision()),
				    /* R15 */ nvl(updatedEntity.getR15_serial_no()),
				              nvlStr(updatedEntity.getR15_subsidiary()),
				              nvl(updatedEntity.getR15_total_adv()),
				              nvl(updatedEntity.getR15_provision()),
				    /* R16 */ nvl(updatedEntity.getR16_serial_no()),
				              nvlStr(updatedEntity.getR16_subsidiary()),
				              nvl(updatedEntity.getR16_total_adv()),
				              nvl(updatedEntity.getR16_provision()),
				    /* R17 */ nvl(updatedEntity.getR17_serial_no()),
				              nvlStr(updatedEntity.getR17_subsidiary()),
				              nvl(updatedEntity.getR17_total_adv()),
				              nvl(updatedEntity.getR17_provision()),
				    /* R18 */ nvl(updatedEntity.getR18_serial_no()),
				              nvlStr(updatedEntity.getR18_subsidiary()),
				              nvl(updatedEntity.getR18_total_adv()),
				              nvl(updatedEntity.getR18_provision()),
				    /* R19 */ nvl(updatedEntity.getR19_serial_no()),
				              nvlStr(updatedEntity.getR19_subsidiary()),
				              nvl(updatedEntity.getR19_total_adv()),
				              nvl(updatedEntity.getR19_provision()),
				    /* R20 */ nvl(updatedEntity.getR20_serial_no()),
				              nvlStr(updatedEntity.getR20_subsidiary()),
				              nvl(updatedEntity.getR20_total_adv()),
				              nvl(updatedEntity.getR20_provision()),
				    /* R21 */ nvl(updatedEntity.getR21_serial_no()),
				              nvlStr(updatedEntity.getR21_subsidiary()),
				              nvl(updatedEntity.getR21_total_adv()),
				              nvl(updatedEntity.getR21_provision()),
				    /* R22 */ nvl(totalAdvance),
				              nvl(totalProvision),
				    /* WHERE */ updatedEntity.getReport_date(),
				                versionToUse);

			logger.info("updateSummaryReport: rowsUpdated={} for date={} version={}", rowsUpdated,
			        updatedEntity.getReport_date(), versionToUse);


			if (rowsUpdated == 0) {
			    logger.warn("updateSummaryReport: UPDATE matched 0 rows — falling back to INSERT "
			            + "for date={} version={}", updatedEntity.getReport_date(), versionToUse);
			    insertNewSummaryRecord(updatedEntity);
			    return;
			}

			try {
				if (auditService != null) {
					String changes = auditService.getChanges(oldcopy, updatedEntity);
					if (changes != null && !changes.isEmpty()) {
						auditService.compareEntitiesmanual(oldcopy, updatedEntity,
								updatedEntity.getReport_date().toString(), "UFCE Calculation Summary Screen",
								"BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL");
					}
				}
			} catch (Exception ae) {
				logger.warn("Audit logging for updateSummaryReport failed (non-fatal): {}", ae.getMessage());
			}

			logger.info("UFCE Summary Update Completed Successfully for date: {} version: {}",
					updatedEntity.getReport_date(), versionToUse);

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
			BigDecimal totalAdvance = calculateTotalAdvance(entity);
			BigDecimal totalProvision = calculateTotalProvision(entity);

			BigDecimal liveMax = getLatestManualEntryVersion(entity.getReport_date());
			BigDecimal archMax = findMaxManualEntryArchivalSummaryVersion(entity.getReport_date());
			BigDecimal maxVersion = (liveMax == null ? BigDecimal.ZERO : liveMax)
					.max(archMax == null ? BigDecimal.ZERO : archMax);
			BigDecimal newVersion = maxVersion.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE
					: maxVersion.add(BigDecimal.ONE);

			String insertSQL = "INSERT INTO BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL ("
			        + "  REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC,"
			        + "  R10_SERIAL_NO, R10_SUBSIDIARY, R10_TOTAL_ADV, R10_PROVISION,"
			        + "  R11_SERIAL_NO, R11_SUBSIDIARY, R11_TOTAL_ADV, R11_PROVISION,"
			        + "  R12_SERIAL_NO, R12_SUBSIDIARY, R12_TOTAL_ADV, R12_PROVISION,"
			        + "  R13_SERIAL_NO, R13_SUBSIDIARY, R13_TOTAL_ADV, R13_PROVISION,"
			        + "  R14_SERIAL_NO, R14_SUBSIDIARY, R14_TOTAL_ADV, R14_PROVISION,"
			        + "  R15_SERIAL_NO, R15_SUBSIDIARY, R15_TOTAL_ADV, R15_PROVISION,"
			        + "  R16_SERIAL_NO, R16_SUBSIDIARY, R16_TOTAL_ADV, R16_PROVISION,"
			        + "  R17_SERIAL_NO, R17_SUBSIDIARY, R17_TOTAL_ADV, R17_PROVISION,"
			        + "  R18_SERIAL_NO, R18_SUBSIDIARY, R18_TOTAL_ADV, R18_PROVISION,"
			        + "  R19_SERIAL_NO, R19_SUBSIDIARY, R19_TOTAL_ADV, R19_PROVISION,"
			        + "  R20_SERIAL_NO, R20_SUBSIDIARY, R20_TOTAL_ADV, R20_PROVISION,"
			        + "  R21_SERIAL_NO, R21_SUBSIDIARY, R21_TOTAL_ADV, R21_PROVISION,"
			        + "  R22_TOTAL_ADV, R22_PROVISION,"
			        + "  DEL_FLG, ENTITY_FLG, MODIFY_FLG"
			        + ") VALUES ("
			        + "  ?, ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?, ?, ?,"
			        + "  ?, ?,"
			        + "  ?, ?, ?"
			        + ")";

			Object[] params = new Object[] {
			    /* header */         entity.getReport_date(),
			                         newVersion,
			                         entity.getReport_frequency() != null ? entity.getReport_frequency() : "Monthly",
			                         entity.getReport_code()      != null ? entity.getReport_code()      : "UFCE_CALC",
			                         entity.getReport_desc()      != null ? entity.getReport_desc()      : "UFCE Calculation Summary",
			    /* R10 */            nvlStr(entity.getR10_serial_no()),
			                         nvlStr(entity.getR10_subsidiary()),
			                         nvlStr(entity.getR10_total_adv()),
			                         nvlStr(entity.getR10_provision()),
			    /* R11 */            nvl(entity.getR11_serial_no()),
			                         nvlStr(entity.getR11_subsidiary()),
			                         nvl(entity.getR11_total_adv()),
			                         nvl(entity.getR11_provision()),
			    /* R12 */            nvl(entity.getR12_serial_no()),
			                         nvlStr(entity.getR12_subsidiary()),
			                         nvl(entity.getR12_total_adv()),
			                         nvl(entity.getR12_provision()),
			    /* R13 */            nvl(entity.getR13_serial_no()),
			                         nvlStr(entity.getR13_subsidiary()),
			                         nvl(entity.getR13_total_adv()),
			                         nvl(entity.getR13_provision()),
			    /* R14 */            nvl(entity.getR14_serial_no()),
			                         nvlStr(entity.getR14_subsidiary()),
			                         nvl(entity.getR14_total_adv()),
			                         nvl(entity.getR14_provision()),
			    /* R15 */            nvl(entity.getR15_serial_no()),
			                         nvlStr(entity.getR15_subsidiary()),
			                         nvl(entity.getR15_total_adv()),
			                         nvl(entity.getR15_provision()),
			    /* R16 */            nvl(entity.getR16_serial_no()),
			                         nvlStr(entity.getR16_subsidiary()),
			                         nvl(entity.getR16_total_adv()),
			                         nvl(entity.getR16_provision()),
			    /* R17 */            nvl(entity.getR17_serial_no()),
			                         nvlStr(entity.getR17_subsidiary()),
			                         nvl(entity.getR17_total_adv()),
			                         nvl(entity.getR17_provision()),
			    /* R18 */            nvl(entity.getR18_serial_no()),
			                         nvlStr(entity.getR18_subsidiary()),
			                         nvl(entity.getR18_total_adv()),
			                         nvl(entity.getR18_provision()),
			    /* R19 */            nvl(entity.getR19_serial_no()),
			                         nvlStr(entity.getR19_subsidiary()),
			                         nvl(entity.getR19_total_adv()),
			                         nvl(entity.getR19_provision()),
			    /* R20 */            nvl(entity.getR20_serial_no()),
			                         nvlStr(entity.getR20_subsidiary()),
			                         nvl(entity.getR20_total_adv()),
			                         nvl(entity.getR20_provision()),
			    /* R21 */            nvl(entity.getR21_serial_no()),
			                         nvlStr(entity.getR21_subsidiary()),
			                         nvl(entity.getR21_total_adv()),
			                         nvl(entity.getR21_provision()),
			    /* R22 */            nvl(totalAdvance),
			                         nvl(totalProvision),
			    /* flags */          "N", "Y", "Y"
			};

			int result = jdbcTemplate.update(insertSQL, params);
			logger.info("New summary record inserted. date={} version={} rows={}",
			        entity.getReport_date(), newVersion, result);

		} catch (Exception e) {
			logger.error("Error inserting new summary record for date: {}", entity.getReport_date(), e);
			throw new RuntimeException("Error inserting new summary record: " + e.getMessage(), e);
		}
	}

	// =====================================================
	// SECTION 11: UPDATE DETAIL
	// =====================================================

	@Transactional
	public void updateDetailReport(BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY updatedEntity) {
		logger.info("Came to UFCE Detail Update");
		if (updatedEntity == null || updatedEntity.getReport_date() == null) {
			throw new IllegalArgumentException("Updated entity and report date cannot be null");
		}

		BigDecimal versionToUse = updatedEntity.getReport_version();
		if (versionToUse == null || versionToUse.compareTo(BigDecimal.ZERO) == 0) {
			versionToUse = getLatestCalculationVersion(updatedEntity.getReport_date());
		}

		List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> matches = getCalculationDetailDataByDateAndVersion(
				updatedEntity.getReport_date(), versionToUse);

		if (matches == null || matches.isEmpty()) {
			logger.warn("updateDetailReport: no DETAILTABLE row for REPORT_DATE={} VERSION={}",
					updatedEntity.getReport_date(), versionToUse);
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date()
					+ " VERSION : " + versionToUse);
		}

		BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY existingDetail = matches.get(0);
		BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY oldcopy = new BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY();
		BeanUtils.copyProperties(existingDetail, oldcopy);

		try {
			String updateDetailSQL = "UPDATE BRRS_UFCE_CALCULATION_DETAILTABLE SET "
					+ "R6_VALUE = ?, R7_VALUE = ?, R8_VALUE = ?, " + "R27_VALUE = ?, R28_VALUE = ?, R29_VALUE = ?, "
					+ "R30_VALUE = ?, R31_VALUE = ?, R32_VALUE = ?, R33_VALUE = ?, " + "MODIFY_FLG = 'Y' "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(updateDetailSQL, updatedEntity.getR6_value(), updatedEntity.getR7_value(),
					updatedEntity.getR8_value(), updatedEntity.getR27_value(), updatedEntity.getR28_value(),
					updatedEntity.getR29_value(), updatedEntity.getR30_value(), updatedEntity.getR31_value(),
					updatedEntity.getR32_value(), updatedEntity.getR33_value(), updatedEntity.getReport_date(),
					versionToUse);

			try {
				if (auditService != null) {
					String changes = auditService.getChanges(oldcopy, updatedEntity);
					if (changes != null && !changes.isEmpty()) {
						auditService.compareEntitiesmanual(oldcopy, updatedEntity,
								updatedEntity.getReport_date().toString(), "UFCE Calculation Detail Screen",
								"BRRS_UFCE_CALCULATION_DETAILTABLE");
					}
				}
			} catch (Exception ae) {
				logger.warn("Audit logging for updateDetailReport failed (non-fatal): {}", ae.getMessage());
			}

			logger.info("UFCE Detail Update Completed Successfully for date: {} version: {}",
					updatedEntity.getReport_date(), versionToUse);

		} catch (Exception e) {
			logger.error("Error while updating UFCE Detail fields", e);
			throw new RuntimeException("Error while updating UFCE Detail fields: " + e.getMessage(), e);
		}
	}

	// =====================================================
	// SECTION 12: RESUB UPDATE - CALCULATION TABLE
	// =====================================================

	@Transactional
	public void updateResubCalculationReport(BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY updatedEntity) {
		logger.info("Came to UFCE Calculation Resub Update");

		Date reportDate = updatedEntity.getReport_date();
		BigDecimal maxVersion = findMaxResubCalculationVersion(reportDate);

		if (maxVersion == null || maxVersion.compareTo(BigDecimal.ZERO) == 0) {
			throw new RuntimeException("No archival record found for REPORT_DATE : " + reportDate);
		}

		BigDecimal newVersion = getNextCalculationArchivalVersion(reportDate);
		Date now = new Date();

		try {
			BigDecimal maxSummarySno = jdbcTemplate.queryForObject(
					"SELECT COALESCE(MAX(SNO), 0) FROM BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE", BigDecimal.class);

			String insertArchivalSummarySQL = "INSERT INTO BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE ("
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
					+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG " + "FROM BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(insertArchivalSummarySQL, maxSummarySno.add(BigDecimal.ONE), reportDate, newVersion,
					now, updatedEntity.getR6_value(), updatedEntity.getR7_value(), updatedEntity.getR8_value(),
					updatedEntity.getR29_value(), updatedEntity.getR32_value(), updatedEntity.getR33_value(),
					reportDate, maxVersion);

			String cloneDetailSQL = "INSERT INTO BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE ("
					+ "SNO, REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, " + "R5_LABEL, R5_RS_IN_LACS, "
					+ "R6_DESCRIPTION, R6_VALUE, R7_DESCRIPTION, R7_VALUE, R8_DESCRIPTION, R8_VALUE, "
					+ "R25_LABEL, R25_RS_IN_LACS, " + "R27_DESCRIPTION, R27_VALUE, R28_DESCRIPTION, R28_VALUE, "
					+ "R29_DESCRIPTION, R29_VALUE, R30_DESCRIPTION, R30_VALUE, "
					+ "R31_DESCRIPTION, R31_VALUE, R32_DESCRIPTION, R32_VALUE, " + "R33_DESCRIPTION, R33_VALUE, "
					+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "SELECT (SELECT COALESCE(MAX(SNO), 0) FROM BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE) + ROWNUM, "
					+ "?, ?, ?, " + "R5_LABEL, R5_RS_IN_LACS, "
					+ "R6_DESCRIPTION, ?, R7_DESCRIPTION, ?, R8_DESCRIPTION, ?, " + "R25_LABEL, R25_RS_IN_LACS, "
					+ "R27_DESCRIPTION, R27_VALUE, R28_DESCRIPTION, R28_VALUE, "
					+ "R29_DESCRIPTION, ?, R30_DESCRIPTION, R30_VALUE, "
					+ "R31_DESCRIPTION, R31_VALUE, R32_DESCRIPTION, ?, " + "R33_DESCRIPTION, ?, "
					+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG " + "FROM BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";

			jdbcTemplate.update(cloneDetailSQL, reportDate, newVersion, now, updatedEntity.getR6_value(),
					updatedEntity.getR7_value(), updatedEntity.getR8_value(), updatedEntity.getR29_value(),
					updatedEntity.getR32_value(), updatedEntity.getR33_value(), reportDate, maxVersion);

			String updateSummarySQL = "UPDATE BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE SET "
					+ "R6_VALUE = ?, R7_VALUE = ?, R8_VALUE = ?, " + "R29_VALUE = ?, R32_VALUE = ?, R33_VALUE = ? "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
			jdbcTemplate.update(updateSummarySQL, updatedEntity.getR6_value(), updatedEntity.getR7_value(),
					updatedEntity.getR8_value(), updatedEntity.getR29_value(), updatedEntity.getR32_value(),
					updatedEntity.getR33_value(), reportDate, newVersion);

			String updateDetailSQL = "UPDATE BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE SET "
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
			BigDecimal totalAdvance = calculateTotalAdvanceManual(updatedEntity);
			BigDecimal totalProvision = calculateTotalProvisionManual(updatedEntity);

			BigDecimal maxSno = jdbcTemplate.queryForObject(
					"SELECT COALESCE(MAX(SNO), 0) FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL",
					BigDecimal.class);

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
	// SECTION 14: UNIFIED UPDATE
	// =====================================================

	@Transactional
	public void updateUFCEReport(Object updatedEntity, String type) {
		logger.info("Came to UFCE Report Update, Type = {}", type);

		if (updatedEntity == null) {
			throw new IllegalArgumentException("updatedEntity cannot be null");
		}

		if ("RESUB".equalsIgnoreCase(type)) {
			if (updatedEntity instanceof BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY) {
				updateResubCalculationReport((BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY) updatedEntity);
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
				updateSummaryReport((BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY) updatedEntity);
			} else {
				throw new IllegalArgumentException(
						"Unsupported entity type for NORMAL update: " + updatedEntity.getClass().getName());
			}
		}
	}

	// =====================================================
	// SECTION 15: VALIDATION + TOTALS
	// =====================================================

	private void validateNonNegativeValues(BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY entity) {
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
	// SECTION 16: EXCEL EXPORT
	// =====================================================

	public byte[] getUFCEUploadableExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version, HttpServletRequest req1)
			throws Exception {
		logger.info("Service: Starting Uploadable Excel generation. filename={} type={} version={}", filename, type,
				version);

		try {
			Date reportDate = parseDate(todate);
			if (reportDate == null)
				reportDate = parseDate(fromdate);
			if (reportDate == null)
				reportDate = new Date();

			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> archivalData = getManualEntryArchivalSummaryDataByDate(
						reportDate, version);
				List<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> archivalDetailData = getCalculationArchivalDetailDataByDate(
						reportDate, version);
				return generateUploadableExcelFromArchivalData(archivalData, archivalDetailData, filename, reportDate);
			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> resubData = getResubManualEntrySummaryDataByDate(
						reportDate, version);
				List<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> resubDetailData = getResubCalculationDetailDataByDate(
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

	private byte[] generateUploadableExcelFromNormalData(
			List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> summaryData,
			List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> detailData, String filename, Date reportDate)
			throws Exception {

		if (summaryData == null || summaryData.isEmpty()) {
			logger.warn("No summary data found for UFCE report.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		if (templateDir == null || templateDir.trim().isEmpty()) {
			templateDir = env.getProperty("output.exportpath");
		}
		if (templateDir == null || templateDir.trim().isEmpty()) {
			throw new IllegalStateException("Neither output.exportpathtemp nor output.exportpath is configured.");
		}

		String safeFilename = (filename != null && !filename.trim().isEmpty()) ? filename : "UFCE_CALCULATION.xlsx";
		Path templatePath = Paths.get(templateDir, safeFilename);

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found: " + templatePath.toAbsolutePath());
		}

		logger.info("Loading Excel template from: {}", templatePath.toAbsolutePath());

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			CellStyle textStyle = createTextStyle(workbook);
			CellStyle numberStyle = createNumberStyle(workbook);
			CellStyle totalHeaderStyle = createTotalHeaderStyle(workbook);
			CellStyle totalNumberStyle = createTotalNumberStyle(workbook);

			BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY record = summaryData.get(0);

			Row row5 = sheet.getRow(4);
			if (row5 == null)
				row5 = sheet.createRow(4);
			setCellValue(row5, 6, "Rs in Lacs", textStyle);

			Row row6 = sheet.getRow(5);
			if (row6 == null)
				row6 = sheet.createRow(5);
			setCellValue(row6, 6, "NIL", textStyle);

			BigDecimal r33 = (detailData != null && !detailData.isEmpty() && detailData.get(0).getR33_value() != null)
					? detailData.get(0).getR33_value()
					: BigDecimal.ZERO;

			Row row7 = sheet.getRow(6);
			if (row7 == null)
				row7 = sheet.createRow(6);
			setCellValue(row7, 6, r33, numberStyle);

			Row row8 = sheet.getRow(7);
			if (row8 == null)
				row8 = sheet.createRow(7);
			setCellValue(row8, 6, r33, numberStyle);

			Row row10 = sheet.getRow(9);
			if (row10 == null)
				row10 = sheet.createRow(9);
			setCellValue(row10, 0, nvlStr(record.getR10_serial_no()), textStyle);
			setCellValue(row10, 1, nvlStr(record.getR10_subsidiary()), textStyle);
			setCellValue(row10, 4, nvlStr(record.getR10_total_adv()), textStyle);
			setCellValue(row10, 7, nvlStr(record.getR10_provision()), textStyle);

			int subRow = 10;
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

			Row row22 = sheet.getRow(subRow);
			if (row22 == null)
				row22 = sheet.createRow(subRow);
			setCellValue(row22, 0, "TOTAL", textStyle);
			setCellValue(row22, 4, nvl(record.getR22_total_adv()), totalNumberStyle);
			setCellValue(row22, 7, nvl(record.getR22_provision()), totalNumberStyle);

			if (detailData != null && !detailData.isEmpty()) {
				BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY d = detailData.get(0);
				int calcRow = 26;

				Row r27 = sheet.getRow(calcRow);
				if (r27 == null)
					r27 = sheet.createRow(calcRow);
				setCellValue(r27, 9, d.getR27_value(), numberStyle);

				Row r28 = sheet.getRow(++calcRow);
				if (r28 == null)
					r28 = sheet.createRow(calcRow);
				setCellValue(r28, 9, d.getR28_value(), numberStyle);

				Row r29 = sheet.getRow(++calcRow);
				if (r29 == null)
					r29 = sheet.createRow(calcRow);
				setCellValue(r29, 9, d.getR29_value(), numberStyle);

				Row r30 = sheet.getRow(++calcRow);
				if (r30 == null)
					r30 = sheet.createRow(calcRow);
				setCellValue(r30, 9, d.getR30_value(), numberStyle);

				Row r31 = sheet.getRow(++calcRow);
				if (r31 == null)
					r31 = sheet.createRow(calcRow);
				setCellValue(r31, 9, d.getR31_value(), numberStyle);

				Row r32 = sheet.getRow(++calcRow);
				if (r32 == null)
					r32 = sheet.createRow(calcRow);
				setCellValue(r32, 9, d.getR32_value(), numberStyle);

				Row r33Row = sheet.getRow(++calcRow);
				if (r33Row == null)
					r33Row = sheet.createRow(calcRow);
				setCellValue(r33Row, 9, d.getR33_value(), numberStyle);
			}

			workbook.setForceFormulaRecalculation(true);
			workbook.write(out);

			try {
				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null && auditService != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD_UPLOADABLE", "UFCE Uploadable Excel", null,
							"BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL");
				}
			} catch (Exception ae) {
				logger.warn("Audit logging failed (non-fatal): {}", ae.getMessage());
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
		setCellValue(row, 1, nvlStr(name), textStyle);
		setCellValue(row, 4, nvl(advance), numberStyle);
		setCellValue(row, 7, nvl(provision), numberStyle);
	}

	private byte[] generateUploadableExcelFromArchivalData(
			List<BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY> dataList,
			List<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> detailList, String filename, Date reportDate)
			throws Exception {

		if (dataList == null || dataList.isEmpty()) {
			logger.warn("No archival data found for UFCE report.");
			return new byte[0];
		}

		List<BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY> convertedList = new ArrayList<>();
		for (BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY archival : dataList) {
			BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY normal = new BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY();
			BeanUtils.copyProperties(archival, normal);
			convertedList.add(normal);
		}

		List<BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY> convertedDetailList = null;
		if (detailList != null && !detailList.isEmpty()) {
			convertedDetailList = new ArrayList<>();
			for (BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY archivalDetail : detailList) {
				BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY normalDetail = new BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY();
				BeanUtils.copyProperties(archivalDetail, normalDetail);
				convertedDetailList.add(normalDetail);
			}
		}

		return generateUploadableExcelFromNormalData(convertedList, convertedDetailList, filename, reportDate);
	}

	// =====================================================
	// SECTION 17: PDF (delegates to Excel builder)
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
		logger.info("Uploadable Excel bytes ready for PDF conversion ({} bytes).", excelBytes.length);
		return excelBytes;
	}

	// =====================================================
	// SECTION 18: NAVIGATION
	// =====================================================

	public ModelAndView navigateToSummary(String reportId, String fromdate, String todate, String currency,
			HttpServletRequest req1) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/Reports/UFCE_CALCULATION?reportid=" + reportId + "&fromdate=" + fromdate + "&todate="
				+ todate + "&asondate=" + todate + "&currency=" + currency);
		return mv;
	}

	// =====================================================
	// RESUB ARRAYS (return Object[] for RegulatoryReportServices)
	// =====================================================

	public List<Object[]> getResubCalculationSummaryDataAsArray(Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, 'CALC' AS SRC "
				+ "FROM BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE"), rs.getString("SRC") });
	}

	public List<Object[]> getResubManualEntrySummaryDataAsArray(Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, 'MANUAL' AS SRC "
				+ "FROM BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL "
				+ "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE"), rs.getString("SRC") });
	}

	public List<Object[]> getResubCalculationDetailDataAsArray(Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
				+ "FROM BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE " + "ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	// =====================================================
	// ENTITY: BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY
	// =====================================================

	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL")
	public static class BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY {

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

		private String r10_serial_no;
		private String r10_subsidiary;
		private String r10_total_adv;
		private String r10_provision;

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

		private BigDecimal r22_total_adv;
		private BigDecimal r22_provision;

		public BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL_ENTITY() {
			super();
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

		public Object get(String key) {
			if (key == null) {
				return null;
			}
			switch (key) {
			case "r10_serial_no":
				return r10_serial_no;
			case "r10_subsidiary":
				return r10_subsidiary;
			case "r10_total_adv":
				return r10_total_adv;
			case "r10_provision":
				return r10_provision;
			case "r11_serial_no":
				return r11_serial_no;
			case "r11_subsidiary":
				return r11_subsidiary;
			case "r11_total_adv":
				return r11_total_adv;
			case "r11_provision":
				return r11_provision;
			case "r12_serial_no":
				return r12_serial_no;
			case "r12_subsidiary":
				return r12_subsidiary;
			case "r12_total_adv":
				return r12_total_adv;
			case "r12_provision":
				return r12_provision;
			case "r13_serial_no":
				return r13_serial_no;
			case "r13_subsidiary":
				return r13_subsidiary;
			case "r13_total_adv":
				return r13_total_adv;
			case "r13_provision":
				return r13_provision;
			case "r14_serial_no":
				return r14_serial_no;
			case "r14_subsidiary":
				return r14_subsidiary;
			case "r14_total_adv":
				return r14_total_adv;
			case "r14_provision":
				return r14_provision;
			case "r15_serial_no":
				return r15_serial_no;
			case "r15_subsidiary":
				return r15_subsidiary;
			case "r15_total_adv":
				return r15_total_adv;
			case "r15_provision":
				return r15_provision;
			case "r16_serial_no":
				return r16_serial_no;
			case "r16_subsidiary":
				return r16_subsidiary;
			case "r16_total_adv":
				return r16_total_adv;
			case "r16_provision":
				return r16_provision;
			case "r17_serial_no":
				return r17_serial_no;
			case "r17_subsidiary":
				return r17_subsidiary;
			case "r17_total_adv":
				return r17_total_adv;
			case "r17_provision":
				return r17_provision;
			case "r18_serial_no":
				return r18_serial_no;
			case "r18_subsidiary":
				return r18_subsidiary;
			case "r18_total_adv":
				return r18_total_adv;
			case "r18_provision":
				return r18_provision;
			case "r19_serial_no":
				return r19_serial_no;
			case "r19_subsidiary":
				return r19_subsidiary;
			case "r19_total_adv":
				return r19_total_adv;
			case "r19_provision":
				return r19_provision;
			case "r20_serial_no":
				return r20_serial_no;
			case "r20_subsidiary":
				return r20_subsidiary;
			case "r20_total_adv":
				return r20_total_adv;
			case "r20_provision":
				return r20_provision;
			case "r21_serial_no":
				return r21_serial_no;
			case "r21_subsidiary":
				return r21_subsidiary;
			case "r21_total_adv":
				return r21_total_adv;
			case "r21_provision":
				return r21_provision;
			case "r22_total_adv":
				return r22_total_adv;
			case "r22_provision":
				return r22_provision;
			default:
				return null;
			}
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
	// =====================================================

	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL")
	public static class BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY {

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

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date archive_date;
		private String archive_reason;
		private String archived_by;

		private String r10_serial_no;
		private String r10_subsidiary;
		private String r10_total_adv;
		private String r10_provision;

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

		private BigDecimal r22_total_adv;
		private BigDecimal r22_provision;

		public BRRS_UFCE_CALCULATION_ARCHIVAL_SUMMARYTABLE_MANUAL_ENTITY() {
			super();
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

		public Object get(String key) {
			if (key == null) {
				return null;
			}
			switch (key) {
			case "r10_serial_no":
				return r10_serial_no;
			case "r10_subsidiary":
				return r10_subsidiary;
			case "r10_total_adv":
				return r10_total_adv;
			case "r10_provision":
				return r10_provision;
			case "r11_serial_no":
				return r11_serial_no;
			case "r11_subsidiary":
				return r11_subsidiary;
			case "r11_total_adv":
				return r11_total_adv;
			case "r11_provision":
				return r11_provision;
			case "r12_serial_no":
				return r12_serial_no;
			case "r12_subsidiary":
				return r12_subsidiary;
			case "r12_total_adv":
				return r12_total_adv;
			case "r12_provision":
				return r12_provision;
			case "r13_serial_no":
				return r13_serial_no;
			case "r13_subsidiary":
				return r13_subsidiary;
			case "r13_total_adv":
				return r13_total_adv;
			case "r13_provision":
				return r13_provision;
			case "r14_serial_no":
				return r14_serial_no;
			case "r14_subsidiary":
				return r14_subsidiary;
			case "r14_total_adv":
				return r14_total_adv;
			case "r14_provision":
				return r14_provision;
			case "r15_serial_no":
				return r15_serial_no;
			case "r15_subsidiary":
				return r15_subsidiary;
			case "r15_total_adv":
				return r15_total_adv;
			case "r15_provision":
				return r15_provision;
			case "r16_serial_no":
				return r16_serial_no;
			case "r16_subsidiary":
				return r16_subsidiary;
			case "r16_total_adv":
				return r16_total_adv;
			case "r16_provision":
				return r16_provision;
			case "r17_serial_no":
				return r17_serial_no;
			case "r17_subsidiary":
				return r17_subsidiary;
			case "r17_total_adv":
				return r17_total_adv;
			case "r17_provision":
				return r17_provision;
			case "r18_serial_no":
				return r18_serial_no;
			case "r18_subsidiary":
				return r18_subsidiary;
			case "r18_total_adv":
				return r18_total_adv;
			case "r18_provision":
				return r18_provision;
			case "r19_serial_no":
				return r19_serial_no;
			case "r19_subsidiary":
				return r19_subsidiary;
			case "r19_total_adv":
				return r19_total_adv;
			case "r19_provision":
				return r19_provision;
			case "r20_serial_no":
				return r20_serial_no;
			case "r20_subsidiary":
				return r20_subsidiary;
			case "r20_total_adv":
				return r20_total_adv;
			case "r20_provision":
				return r20_provision;
			case "r21_serial_no":
				return r21_serial_no;
			case "r21_subsidiary":
				return r21_subsidiary;
			case "r21_total_adv":
				return r21_total_adv;
			case "r21_provision":
				return r21_provision;
			case "r22_total_adv":
				return r22_total_adv;
			case "r22_provision":
				return r22_provision;
			default:
				return null;
			}
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
	// ENTITY: BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY
	// =====================================================

	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_SUMMARYTABLE")
	public static class BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY {

		@Id
		private Long sno;

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

		private String r5_label;
		private String r5_rs_in_lacs;
		private String r6_description;
		private BigDecimal r6_value;
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

		public BRRS_UFCE_CALCULATION_SUMMARYTABLE_ENTITY() {
			super();
		}

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

		public BigDecimal getR6_value() {
			return r6_value;
		}

		public void setR6_value(BigDecimal r6_value) {
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
			obj.setR6_value(rs.getBigDecimal("R6_VALUE"));
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
	// ENTITY: BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY
	// =====================================================

	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_DETAILTABLE")
	public static class BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY {

		@Id
		private Long sno;

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

		private String r5_label;
		private String r5_rs_in_lacs;
		private String r6_description;
		private BigDecimal r6_value;
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

		public BRRS_UFCE_CALCULATION_DETAILTABLE_ENTITY() {
			super();
		}

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

		public BigDecimal getR6_value() {
			return r6_value;
		}

		public void setR6_value(BigDecimal r6_value) {
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
			obj.setR6_value(rs.getBigDecimal("R6_VALUE"));
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
	// ENTITY: BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY
	// =====================================================

	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE")
	public static class BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY {

		@Id
		private Long sno;

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

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date archive_date;
		private String archive_reason;
		private String archived_by;

		private String r5_label;
		private String r5_rs_in_lacs;
		private String r6_description;
		private BigDecimal r6_value;
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

		public BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY() {
			super();
		}

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

		public BigDecimal getR6_value() {
			return r6_value;
		}

		public void setR6_value(BigDecimal r6_value) {
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
	// ROW MAPPER: BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ROWMAPPER
	// =====================================================

	public class BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ROWMAPPER
			implements RowMapper<BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY> {

		@Override
		public BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY mapRow(ResultSet rs, int rowNum) throws SQLException {
			BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY obj = new BRRS_UFCE_CALCULATION_SUMMARY_ARCHIVALTABLE_ENTITY();

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
			obj.setR6_value(rs.getBigDecimal("R6_VALUE"));
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
	// ENTITY: BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY
	// =====================================================

	@Entity
	@Table(name = "BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE")
	public static class BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY {

		@Id
		private Long sno;

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

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd-MMM-yyyy")
		private Date archive_date;
		private String archive_reason;
		private String archived_by;

		private String r5_label;
		private String r5_rs_in_lacs;
		private String r6_description;
		private BigDecimal r6_value;
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

		public BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY() {
			super();
		}

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

		public BigDecimal getR6_value() {
			return r6_value;
		}

		public void setR6_value(BigDecimal r6_value) {
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
	// ROW MAPPER: BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ROWMAPPER
	// =====================================================

	public class BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ROWMAPPER
			implements RowMapper<BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY> {

		@Override
		public BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY mapRow(ResultSet rs, int rowNum) throws SQLException {
			BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY obj = new BRRS_UFCE_CALCULATION_DETAIL_ARCHIVALTABLE_ENTITY();

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
			obj.setR6_value(rs.getBigDecimal("R6_VALUE"));
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