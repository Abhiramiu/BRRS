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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service

public class BRRS_M_FAS_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_FAS_ReportService.class);

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

	private static String getColumnName(java.lang.reflect.Field field) {
		if (field.isAnnotationPresent(Column.class)) {
			Column col = field.getAnnotation(Column.class);
			return col.name();
		}
		return field.getName();
	}

	private static <T> T mapRowToEntity(ResultSet rs, Class<T> clazz) throws SQLException {
		try {
			T entity = clazz.getDeclaredConstructor().newInstance();
			for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers()))
					continue;
				String columnName = getColumnName(field);
				try {
					Object value = null;
					if (field.getType() == String.class) {
						value = rs.getString(columnName);
					} else if (field.getType() == BigDecimal.class) {
						value = rs.getBigDecimal(columnName);
					} else if (field.getType() == Date.class) {
						java.sql.Timestamp ts = rs.getTimestamp(columnName);
						if (ts != null) {
							value = new Date(ts.getTime());
						}
					} else if (field.getType() == Integer.class || field.getType() == int.class) {
						value = rs.getInt(columnName);
					}
					field.set(entity, value);
				} catch (SQLException e) {
					// column not found in result set, ignore
				}
			}
			return entity;
		} catch (Exception e) {
			throw new SQLException("Error mapping row to " + clazz.getName(), e);
		}
	}

	public static class GenericRowMapper<T> implements RowMapper<T> {
		private final Class<T> clazz;

		public GenericRowMapper(Class<T> clazz) {
			this.clazz = clazz;
		}

		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			return mapRowToEntity(rs, clazz);
		}
	}

	public M_FAS_Detail_Entity findDetailByAcctnumber(String acctNumber) {
		String sql = "SELECT * FROM BRRS_M_FAS_DETAILTABLE WHERE ACCT_NUMBER = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber },
					new GenericRowMapper<>(M_FAS_Detail_Entity.class));
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		}
	}

	public M_FAS_Summary_Entity findSummaryById(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_FAS_SUMMARYTABLE WHERE REPORT_DATE = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { reportDate },
					new GenericRowMapper<>(M_FAS_Summary_Entity.class));
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		}
	}

	private void saveSummary(M_FAS_Summary_Entity entity) {
		if (entity.getReportDate() == null) {
			throw new IllegalArgumentException("Report Date cannot be null");
		}

		String checkSql = "SELECT COUNT(*) FROM BRRS_M_FAS_SUMMARYTABLE WHERE REPORT_DATE = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReportDate() }, Integer.class);

		if (count != null && count > 0) {
			StringBuilder sql = new StringBuilder("UPDATE BRRS_M_FAS_SUMMARYTABLE SET ");
			List<Object> params = new ArrayList<>();
			for (java.lang.reflect.Field field : M_FAS_Summary_Entity.class.getDeclaredFields()) {
				field.setAccessible(true);
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers()))
					continue;
				String colName = getColumnName(field);
				if ("REPORT_DATE".equalsIgnoreCase(colName))
					continue;

				sql.append(colName).append(" = ?, ");
				try {
					params.add(field.get(entity));
				} catch (Exception e) {
					params.add(null);
				}
			}
			sql.setLength(sql.length() - 2);
			sql.append(" WHERE REPORT_DATE = ?");
			params.add(entity.getReportDate());
			jdbcTemplate.update(sql.toString(), params.toArray());
		} else {
			StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_FAS_SUMMARYTABLE (");
			StringBuilder values = new StringBuilder("VALUES (");
			List<Object> params = new ArrayList<>();
			for (java.lang.reflect.Field field : M_FAS_Summary_Entity.class.getDeclaredFields()) {
				field.setAccessible(true);
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers()))
					continue;
				String colName = getColumnName(field);

				sql.append(colName).append(", ");
				values.append("?, ");
				try {
					params.add(field.get(entity));
				} catch (Exception e) {
					params.add(null);
				}
			}
			sql.setLength(sql.length() - 2);
			values.setLength(values.length() - 2);
			sql.append(") ").append(values).append(")");
			jdbcTemplate.update(sql.toString(), params.toArray());
		}
	}

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_FASView(String reportId, String fromdate, String todate, String currency,
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

		try {
			Date d1 = dateformat.parse(todate);

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_FAS_Archival_Summary_Entity> T1Master = jdbcTemplate.query(
						"select * from BRRS_M_FAS_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
						new Object[] { d1, version }, new GenericRowMapper<>(M_FAS_Archival_Summary_Entity.class));
				mv.addObject("reportsummary", T1Master);
				System.out.println("T1Master Size " + T1Master.size());

			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_FAS_Summary_Entity> T1Master = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_FAS_SUMMARYTABLE WHERE REPORT_DATE=?",
						new Object[] { dateformat.parse(todate) }, new GenericRowMapper<>(M_FAS_Summary_Entity.class));

				mv.addObject("reportsummary", T1Master);
				System.out.println("T1Master Size " + T1Master.size());
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_FAS");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public ModelAndView getBRRS_M_FAScurrentDtl(String reportId, String fromdate, String todate, String currency,
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

		// Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLable = null;
			String reportAddlCriteria_1 = null;
			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria_1 = parts[1];
				}
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<M_FAS_Archival_Detail_Entity> T1Dt1;
				if (reportLable != null && reportAddlCriteria_1 != null) {
					String sql = "select * from BRRS_M_FAS_ARCHIVALTABLE_DETAIL where REPORT_LABEL =? and REPORT_ADDL_CRITERIA_1=? AND REPORT_DATE=? AND DATA_ENTRY_VERSION=?";
					T1Dt1 = jdbcTemplate.query(sql,
							new Object[] { reportLable, reportAddlCriteria_1, parsedDate, version },
							new GenericRowMapper<>(M_FAS_Archival_Detail_Entity.class));
				} else {
					String sql = "select * from BRRS_M_FAS_ARCHIVALTABLE_DETAIL where REPORT_DATE=? AND DATA_ENTRY_VERSION=?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { parsedDate, version },
							new GenericRowMapper<>(M_FAS_Archival_Detail_Entity.class));
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_FAS_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					String sql = "select * from BRRS_M_FAS_DETAILTABLE where REPORT_LABEL =? and REPORT_ADDL_CRITERIA_1=? AND REPORT_DATE=?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { reportLable, reportAddlCriteria_1, parsedDate },
							new GenericRowMapper<>(M_FAS_Detail_Entity.class));
				} else {
					String sql = "select * from BRRS_M_FAS_DETAILTABLE where REPORT_DATE = ? offset ? rows fetch next ? rows only";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { parsedDate, currentPage, pageSize },
							new GenericRowMapper<>(M_FAS_Detail_Entity.class));
					String countSql = "select count(*) from BRRS_M_FAS_DETAILTABLE where REPORT_DATE = ?";
					totalPages = jdbcTemplate.queryForObject(countSql, new Object[] { parsedDate }, Integer.class);
					mv.addObject("pagination", "YES");

				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);

				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		mv.setViewName("BRRS/M_FAS");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public byte[] BRRS_M_FASDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_FAS Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_FASDetails");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);
			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "CREDIT EQUIVALENT", "DEBIT EQUIVALENT",
					"REPORT LABEL", "REPORT ADDL CRETIRIA", "REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				// Amount columns: ACCT BALANCE (i=3) and average (i=4)
				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}
			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_FAS_Detail_Entity> reportData = jdbcTemplate.query(
					"select * from BRRS_M_FAS_DETAILTABLE where REPORT_DATE = ?", new Object[] { parsedToDate },
					new GenericRowMapper<>(M_FAS_Detail_Entity.class));
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_FAS_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getCreditEquivalent() != null) {
						balanceCell.setCellValue(item.getCreditEquivalent().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					// Average (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getDebitEquivalent() != null) {
						balanceCell.setCellValue(item.getDebitEquivalent().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");
					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_FAS — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_FAS Excel", e);
			return null; // important
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_FAS ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_FASDetail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "CREDIT EQUIVALENT", "DEBIT EQUIVALENT",
					"REPORT LABEL", "REPORT ADDL CRETIRIA", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				// Amount columns: ACCT BALANCE (i=3) and average (i=4)
				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_FAS_Archival_Detail_Entity> reportData = jdbcTemplate.query(
					"select * from BRRS_M_FAS_ARCHIVALTABLE_DETAIL where REPORT_DATE=? AND DATA_ENTRY_VERSION=?",
					new Object[] { parsedToDate, version }, new GenericRowMapper<>(M_FAS_Archival_Detail_Entity.class));

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_FAS_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getCreditEquivalent() != null) {
						balanceCell.setCellValue(item.getCreditEquivalent().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// Average (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getDebitEquivalent() != null) {
						balanceCell.setCellValue(item.getDebitEquivalent().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_FAS — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_FASExcel", e);
			return new byte[0];
		}
	}

	// @Autowired
	// private M_FAS_Archival_Detail_Repo m_FAS_Archival_Detail_Repo;

	@Transactional
	public void updateReport1(M_FAS_Summary_Entity updatedEntity) {
		System.out.println("Came to services 3");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_FAS_Summary_Entity existing = findSummaryById(updatedEntity.getReportDate());

		if (existing == null) {
			System.out.println("⚠️ No existing record found — creating new record");
			saveSummary(updatedEntity);
			return;
		}

		// 🔹 Audit copy: Create a clone of the original database state before making
		// any changes
		M_FAS_Summary_Entity oldcopy = new M_FAS_Summary_Entity();
		BeanUtils.copyProperties(existing, oldcopy);

		try {

			/*
			 * ----------------------------------------------------------- 🔵 1) UPDATE R12
			 * FIELDS ------------------------------------------------------------
			 */
			String[] r12Fields = { "fix_ass", "cost", "add", "disposals", "depreciation", "net_book_value" };

			for (String field : r12Fields) {
				String getterName = "getR12_" + field;
				String setterName = "setR12_" + field;

				try {
					Method getter = M_FAS_Summary_Entity.class.getMethod(getterName);
					Method setter = M_FAS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

					Object value = getter.invoke(updatedEntity);
					Object existingValue = getter.invoke(existing);

					// --- FIX: Normalize state differences to keep audit log payload minimal ---
					String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
					String newValStr = (value == null) ? "" : value.toString().trim();

					if (currentValStr.equals(newValStr)) {
						continue;
					}

					setter.invoke(existing, value);
				} catch (NoSuchMethodException e) {
					System.out.println("Missing (R12) method: " + getterName);
				}
			}

			/*
			 * ----------------------------------------------------------- 🔵 2) UPDATE
			 * R23–R28 ------------------------------------------------------------
			 */
			String[] fields = { "intangible_ass", "cost_rev", "useful_life", "res_value", "month_amort",
					"acc_amort_amt", "close_bal" };

			for (int i = 23; i <= 28; i++) {
				for (String field : fields) {
					String base = "R" + i + "_" + field; // match getter/setter names
					String getterName = "get" + base;
					String setterName = "set" + base;

					try {
						Method getter = M_FAS_Summary_Entity.class.getMethod(getterName);
						Method setter = M_FAS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object value = getter.invoke(updatedEntity);
						Object existingValue = getter.invoke(existing);

						// --- FIX: Normalize state differences ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (value == null) ? "" : value.toString().trim();

						if (currentValStr.equals(newValStr)) {
							continue;
						}

						setter.invoke(existing, value);
					} catch (NoSuchMethodException e) {
						System.out.println("Missing (R" + i + ") method: " + getterName);
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error updating record", e);
		}

		saveSummary(existing);
		System.out.println("✔ UPDATED successfully");

		// =====================================================
		// EVALUATE AND LOG AUDIT TRAIL
		// =====================================================
		String changes = auditService.getChanges(oldcopy, existing);
		System.out.println("M_FAS Changes Length = " + changes.length());

		if (changes != null && !changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existing, updatedEntity.getReportDate().toString(),
					"M_FAS Summary Screen", "BRRS_M_FAS_SUMMARY");
		}
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_FAS");

		if (acctNo != null) {
			M_FAS_Detail_Entity fas = findDetailByAcctnumber(acctNo);
			if (fas != null && fas.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(fas.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("FASData", fas);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("creditEquivalent");
			String average = request.getParameter("debitEquivalent");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate"); // yyyy-MM-dd from HTML

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_FAS_Detail_Entity existing = findDetailByAcctnumber(acctNo);

			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			// Update account name
			if (acctName != null && !acctName.isEmpty() && !acctName.equals(existing.getAcctName())) {

				existing.setAcctName(acctName);
				isChanged = true;
				logger.info("Updated acctName → {}", acctName);
			}

			// Update Pula balance
			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

				BigDecimal newBalance = new BigDecimal(acctBalanceInpula.replace(",", ""));

				if (existing.getCreditEquivalent() == null
						|| existing.getCreditEquivalent().compareTo(newBalance) != 0) {

					existing.setCreditEquivalent(newBalance);
					isChanged = true;
					logger.info("Updated acctBalanceInPula → {}", newBalance);
				}
			}
			if (average != null && !average.isEmpty()) {
				BigDecimal newaverage = new BigDecimal(average);
				if (existing.getDebitEquivalent() == null || existing.getDebitEquivalent().compareTo(newaverage) != 0) {
					existing.setDebitEquivalent(newaverage);
					isChanged = true;
					logger.info("Balance updated to {}", newaverage);
				}
			}

			if (!isChanged) {
				logger.info("No changes detected for ACCT_NO {}", acctNo);
				return ResponseEntity.ok("No changes were made.");
			}

			// Save updated data
			jdbcTemplate.update(
					"UPDATE BRRS_M_FAS_DETAILTABLE SET ACCT_NAME = ?, CREDIT_EQUIVALENT = ?, DEBIT_EQUIVALENT = ? WHERE ACCT_NUMBER = ?",
					existing.getAcctName(), existing.getCreditEquivalent(), existing.getDebitEquivalent(), acctNo);

			logger.info("Record updated successfully for ACCT_NO {}", acctNo);

			// Format date "yyyy-MM-dd" → "dd-MM-yyyy"
			String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
					.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

			// Register after-commit callback
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					try {
						logger.info("AFTER COMMIT → Executing BRRS_M_FAS_SUMMARY_PROCEDURE({})", formattedDate);

						jdbcTemplate.update("BEGIN BRRS_M_FAS_SUMMARY_PROCEDURE(?); END;", formattedDate);

					} catch (Exception e) {
						logger.error("Error executing after-commit procedure", e);
					}
				}
			});

			return ResponseEntity.ok("Record updated successfully!");

		} catch (Exception e) {
			logger.error("Error updating M_FAS record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// download

	public List<Object[]> getM_FASResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM BRRS_M_FAS_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC";
			List<M_FAS_Archival_Summary_Entity> latestArchivalList = jdbcTemplate.query(sql,
					new GenericRowMapper<>(M_FAS_Archival_Summary_Entity.class));

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_FAS_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_FAS Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getM_FASArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			String sql = "SELECT * FROM BRRS_M_FAS_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC";
			List<M_FAS_Archival_Summary_Entity> repoData = jdbcTemplate.query(sql,
					new GenericRowMapper<>(M_FAS_Archival_Summary_Entity.class));

			if (repoData != null && !repoData.isEmpty()) {
				for (M_FAS_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_FAS_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_FAS Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getM_FASExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getSummaryExcelARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
//		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_FASResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_FASEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_FAS_Summary_Entity> dataList = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_FAS_SUMMARYTABLE WHERE REPORT_DATE=?",
						new Object[] { dateformat.parse(todate) }, new GenericRowMapper<>(M_FAS_Summary_Entity.class));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_FAS report. Returning empty result.");
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
							M_FAS_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
//NORMAL
							Cell R12Cell = row.createCell(1);

							if (record.getReportDate() != null) {

								R12Cell.setCellValue(record.getReportDate());

								R12Cell.setCellStyle(dateStyle);

							} else {

								R12Cell.setCellValue("");

								R12Cell.setCellStyle(textStyle);
							}
							row = sheet.getRow(9);

							// R10 Col B
							Cell R10cell1 = row.createCell(1);
							if (record.getR10_cost() != null) {
								R10cell1.setCellValue(record.getR10_cost().doubleValue());
								R10cell1.setCellStyle(numberStyle);
							} else {
								R10cell1.setCellValue("");
								R10cell1.setCellStyle(textStyle);
							}

							// R10 Col C
							Cell R10cell2 = row.createCell(2);
							if (record.getR10_add() != null) {
								R10cell2.setCellValue(record.getR10_add().doubleValue());
								R10cell2.setCellStyle(numberStyle);
							} else {
								R10cell2.setCellValue("");
								R10cell2.setCellStyle(textStyle);
							}

							// R10 Col D
							Cell R10cell3 = row.createCell(3);
							if (record.getR10_disposals() != null) {
								R10cell3.setCellValue(record.getR10_disposals().doubleValue());
								R10cell3.setCellStyle(numberStyle);
							} else {
								R10cell3.setCellValue("");
								R10cell3.setCellStyle(textStyle);
							}

							// R10 Col E
							Cell R10cell4 = row.createCell(4);
							if (record.getR10_depreciation() != null) {
								R10cell4.setCellValue(record.getR10_depreciation().doubleValue());
								R10cell4.setCellStyle(numberStyle);
							} else {
								R10cell4.setCellValue("");
								R10cell4.setCellStyle(textStyle);
							}
							// ==================== R11 ====================
							// R11 Col B
							row = sheet.getRow(10);
							Cell R11cell1 = row.createCell(1);
							if (record.getR11_cost() != null) {
								R11cell1.setCellValue(record.getR11_cost().doubleValue());
								R11cell1.setCellStyle(numberStyle);
							} else {
								R11cell1.setCellValue("");
								R11cell1.setCellStyle(textStyle);
							}

							// R11 Col C
							Cell R11cell2 = row.createCell(2);
							if (record.getR11_add() != null) {
								R11cell2.setCellValue(record.getR11_add().doubleValue());
								R11cell2.setCellStyle(numberStyle);
							} else {
								R11cell2.setCellValue("");
								R11cell2.setCellStyle(textStyle);
							}

							// R11 Col D
							Cell R11cell3 = row.createCell(3);
							if (record.getR11_disposals() != null) {
								R11cell3.setCellValue(record.getR11_disposals().doubleValue());
								R11cell3.setCellStyle(numberStyle);
							} else {
								R11cell3.setCellValue("");
								R11cell3.setCellStyle(textStyle);
							}

							// R11 Col E
							Cell R11cell4 = row.createCell(4);
							if (record.getR11_depreciation() != null) {
								R11cell4.setCellValue(record.getR11_depreciation().doubleValue());
								R11cell4.setCellStyle(numberStyle);
							} else {
								R11cell4.setCellValue("");
								R11cell4.setCellStyle(textStyle);
							}

							// ==================== R12 ====================
							row = sheet.getRow(11);
							Cell R12cell1 = row.createCell(1);
							if (record.getR12_cost() != null) {
								R12cell1.setCellValue(record.getR12_cost().doubleValue());
								R12cell1.setCellStyle(numberStyle);
							} else {
								R12cell1.setCellValue("");
								R12cell1.setCellStyle(textStyle);
							}

							Cell R12cell2 = row.createCell(2);
							if (record.getR12_add() != null) {
								R12cell2.setCellValue(record.getR12_add().doubleValue());
								R12cell2.setCellStyle(numberStyle);
							} else {
								R12cell2.setCellValue("");
								R12cell2.setCellStyle(textStyle);
							}

							Cell R12cell3 = row.createCell(3);
							if (record.getR12_disposals() != null) {
								R12cell3.setCellValue(record.getR12_disposals().doubleValue());
								R12cell3.setCellStyle(numberStyle);
							} else {
								R12cell3.setCellValue("");
								R12cell3.setCellStyle(textStyle);
							}

							Cell R12cell4 = row.createCell(4);
							if (record.getR12_depreciation() != null) {
								R12cell4.setCellValue(record.getR12_depreciation().doubleValue());
								R12cell4.setCellStyle(numberStyle);
							} else {
								R12cell4.setCellValue("");
								R12cell4.setCellStyle(textStyle);
							}

							// ==================== R13 ====================
							row = sheet.getRow(12);
							Cell R13cell1 = row.createCell(1);
							if (record.getR13_cost() != null) {
								R13cell1.setCellValue(record.getR13_cost().doubleValue());
								R13cell1.setCellStyle(numberStyle);
							} else {
								R13cell1.setCellValue("");
								R13cell1.setCellStyle(textStyle);
							}

							Cell R13cell2 = row.createCell(2);
							if (record.getR13_add() != null) {
								R13cell2.setCellValue(record.getR13_add().doubleValue());
								R13cell2.setCellStyle(numberStyle);
							} else {
								R13cell2.setCellValue("");
								R13cell2.setCellStyle(textStyle);
							}

							Cell R13cell3 = row.createCell(3);
							if (record.getR13_disposals() != null) {
								R13cell3.setCellValue(record.getR13_disposals().doubleValue());
								R13cell3.setCellStyle(numberStyle);
							} else {
								R13cell3.setCellValue("");
								R13cell3.setCellStyle(textStyle);
							}

							Cell R13cell4 = row.createCell(4);
							if (record.getR13_depreciation() != null) {
								R13cell4.setCellValue(record.getR13_depreciation().doubleValue());
								R13cell4.setCellStyle(numberStyle);
							} else {
								R13cell4.setCellValue("");
								R13cell4.setCellStyle(textStyle);
							}

							// ==================== R14 ====================
							row = sheet.getRow(13);
							Cell R14cell1 = row.createCell(1);
							if (record.getR14_cost() != null) {
								R14cell1.setCellValue(record.getR14_cost().doubleValue());
								R14cell1.setCellStyle(numberStyle);
							} else {
								R14cell1.setCellValue("");
								R14cell1.setCellStyle(textStyle);
							}

							Cell R14cell2 = row.createCell(2);
							if (record.getR14_add() != null) {
								R14cell2.setCellValue(record.getR14_add().doubleValue());
								R14cell2.setCellStyle(numberStyle);
							} else {
								R14cell2.setCellValue("");
								R14cell2.setCellStyle(textStyle);
							}

							Cell R14cell3 = row.createCell(3);
							if (record.getR14_disposals() != null) {
								R14cell3.setCellValue(record.getR14_disposals().doubleValue());
								R14cell3.setCellStyle(numberStyle);
							} else {
								R14cell3.setCellValue("");
								R14cell3.setCellStyle(textStyle);
							}

							Cell R14cell4 = row.createCell(4);
							if (record.getR14_depreciation() != null) {
								R14cell4.setCellValue(record.getR14_depreciation().doubleValue());
								R14cell4.setCellStyle(numberStyle);
							} else {
								R14cell4.setCellValue("");
								R14cell4.setCellStyle(textStyle);
							}

							// ==================== R15 ====================
							row = sheet.getRow(14);
							Cell R15cell1 = row.createCell(1);
							if (record.getR15_cost() != null) {
								R15cell1.setCellValue(record.getR15_cost().doubleValue());
								R15cell1.setCellStyle(numberStyle);
							} else {
								R15cell1.setCellValue("");
								R15cell1.setCellStyle(textStyle);
							}

							Cell R15cell2 = row.createCell(2);
							if (record.getR15_add() != null) {
								R15cell2.setCellValue(record.getR15_add().doubleValue());
								R15cell2.setCellStyle(numberStyle);
							} else {
								R15cell2.setCellValue("");
								R15cell2.setCellStyle(textStyle);
							}

							Cell R15cell3 = row.createCell(3);
							if (record.getR15_disposals() != null) {
								R15cell3.setCellValue(record.getR15_disposals().doubleValue());
								R15cell3.setCellStyle(numberStyle);
							} else {
								R15cell3.setCellValue("");
								R15cell3.setCellStyle(textStyle);
							}

							Cell R15cell4 = row.createCell(4);
							if (record.getR15_depreciation() != null) {
								R15cell4.setCellValue(record.getR15_depreciation().doubleValue());
								R15cell4.setCellStyle(numberStyle);
							} else {
								R15cell4.setCellValue("");
								R15cell4.setCellStyle(textStyle);
							}

							// ==================== R16 ====================
							row = sheet.getRow(15);
							Cell R16cell1 = row.createCell(1);
							if (record.getR16_cost() != null) {
								R16cell1.setCellValue(record.getR16_cost().doubleValue());
								R16cell1.setCellStyle(numberStyle);
							} else {
								R16cell1.setCellValue("");
								R16cell1.setCellStyle(textStyle);
							}

							Cell R16cell2 = row.createCell(2);
							if (record.getR16_add() != null) {
								R16cell2.setCellValue(record.getR16_add().doubleValue());
								R16cell2.setCellStyle(numberStyle);
							} else {
								R16cell2.setCellValue("");
								R16cell2.setCellStyle(textStyle);
							}

							Cell R16cell3 = row.createCell(3);
							if (record.getR16_disposals() != null) {
								R16cell3.setCellValue(record.getR16_disposals().doubleValue());
								R16cell3.setCellStyle(numberStyle);
							} else {
								R16cell3.setCellValue("");
								R16cell3.setCellStyle(textStyle);
							}

							Cell R16cell4 = row.createCell(4);
							if (record.getR16_depreciation() != null) {
								R16cell4.setCellValue(record.getR16_depreciation().doubleValue());
								R16cell4.setCellStyle(numberStyle);
							} else {
								R16cell4.setCellValue("");
								R16cell4.setCellStyle(textStyle);
							}

							// ==================== R23 ====================
							row = sheet.getRow(22);
							// R23 Col B
							Cell R23cell1 = row.createCell(1);
							if (record.getR23_cost_rev() != null) {
								R23cell1.setCellValue(record.getR23_cost_rev().doubleValue());
								R23cell1.setCellStyle(numberStyle);
							} else {
								R23cell1.setCellValue("");
								R23cell1.setCellStyle(textStyle);
							}

							// R23 Col C
							Cell R23cell2 = row.createCell(2);
							if (record.getR23_useful_life() != null) {
								R23cell2.setCellValue(record.getR23_useful_life().doubleValue());
								R23cell2.setCellStyle(numberStyle);
							} else {
								R23cell2.setCellValue("");
								R23cell2.setCellStyle(textStyle);
							}

							// R23 Col D
							Cell R23cell3 = row.createCell(3);
							if (record.getR23_res_value() != null) {
								R23cell3.setCellValue(record.getR23_res_value().doubleValue());
								R23cell3.setCellStyle(numberStyle);
							} else {
								R23cell3.setCellValue("");
								R23cell3.setCellStyle(textStyle);
							}

							// R23 Col E
							Cell R23cell4 = row.createCell(4);
							if (record.getR23_month_amort() != null) {
								R23cell4.setCellValue(record.getR23_month_amort().doubleValue());
								R23cell4.setCellStyle(numberStyle);
							} else {
								R23cell4.setCellValue("");
								R23cell4.setCellStyle(textStyle);
							}
							// R23 Col F
							Cell R23cell5 = row.createCell(5);
							if (record.getR23_acc_amort_amt() != null) {
								R23cell5.setCellValue(record.getR23_acc_amort_amt().doubleValue());
								R23cell5.setCellStyle(numberStyle);
							} else {
								R23cell5.setCellValue("");
								R23cell5.setCellStyle(textStyle);
							}
							// R23 Col F
							Cell R23cell6 = row.createCell(6);
							if (record.getR23_close_bal() != null) {
								R23cell6.setCellValue(record.getR23_close_bal().doubleValue());
								R23cell6.setCellStyle(numberStyle);
							} else {
								R23cell6.setCellValue("");
								R23cell6.setCellStyle(textStyle);
							}
							// ==================== R24 ====================

							row = sheet.getRow(23);
							// R24 Col B
							Cell R24cell1 = row.createCell(1);
							if (record.getR24_cost_rev() != null) {
								R24cell1.setCellValue(record.getR24_cost_rev().doubleValue());
								R24cell1.setCellStyle(numberStyle);
							} else {
								R24cell1.setCellValue("");
								R24cell1.setCellStyle(textStyle);
							}

							// R24 Col C
							Cell R24cell2 = row.createCell(2);
							if (record.getR24_useful_life() != null) {
								R24cell2.setCellValue(record.getR24_useful_life().doubleValue());
								R24cell2.setCellStyle(numberStyle);
							} else {
								R24cell2.setCellValue("");
								R24cell2.setCellStyle(textStyle);
							}

							// R24 Col D
							Cell R24cell3 = row.createCell(3);
							if (record.getR24_res_value() != null) {
								R24cell3.setCellValue(record.getR24_res_value().doubleValue());
								R24cell3.setCellStyle(numberStyle);
							} else {
								R24cell3.setCellValue("");
								R24cell3.setCellStyle(textStyle);
							}

							// R24 Col E
							Cell R24cell4 = row.createCell(4);
							if (record.getR24_month_amort() != null) {
								R24cell4.setCellValue(record.getR24_month_amort().doubleValue());
								R24cell4.setCellStyle(numberStyle);
							} else {
								R24cell4.setCellValue("");
								R24cell4.setCellStyle(textStyle);
							}
							// R24 Col F
							Cell R24cell5 = row.createCell(5);
							if (record.getR24_acc_amort_amt() != null) {
								R24cell5.setCellValue(record.getR24_acc_amort_amt().doubleValue());
								R24cell5.setCellStyle(numberStyle);
							} else {
								R24cell5.setCellValue("");
								R24cell5.setCellStyle(textStyle);
							}
							// R24 Col F
							Cell R24cell6 = row.createCell(6);
							if (record.getR24_close_bal() != null) {
								R24cell6.setCellValue(record.getR24_close_bal().doubleValue());
								R24cell6.setCellStyle(numberStyle);
							} else {
								R24cell6.setCellValue("");
								R24cell6.setCellStyle(textStyle);
							}

							// ==================== R25 ====================
							row = sheet.getRow(24);
							// R25 Col B
							Cell R25cell1 = row.createCell(1);
							if (record.getR25_cost_rev() != null) {
								R25cell1.setCellValue(record.getR25_cost_rev().doubleValue());
								R25cell1.setCellStyle(numberStyle);
							} else {
								R25cell1.setCellValue("");
								R25cell1.setCellStyle(textStyle);
							}

							// R25 Col C
							Cell R25cell2 = row.createCell(2);
							if (record.getR25_useful_life() != null) {
								R25cell2.setCellValue(record.getR25_useful_life().doubleValue());
								R25cell2.setCellStyle(numberStyle);
							} else {
								R25cell2.setCellValue("");
								R25cell2.setCellStyle(textStyle);
							}

							// R25 Col D
							Cell R25cell3 = row.createCell(3);
							if (record.getR25_res_value() != null) {
								R25cell3.setCellValue(record.getR25_res_value().doubleValue());
								R25cell3.setCellStyle(numberStyle);
							} else {
								R25cell3.setCellValue("");
								R25cell3.setCellStyle(textStyle);
							}

							// R25 Col E
							Cell R25cell4 = row.createCell(4);
							if (record.getR25_month_amort() != null) {
								R25cell4.setCellValue(record.getR25_month_amort().doubleValue());
								R25cell4.setCellStyle(numberStyle);
							} else {
								R25cell4.setCellValue("");
								R25cell4.setCellStyle(textStyle);
							}
							// R25 Col F
							Cell R25cell5 = row.createCell(5);
							if (record.getR25_acc_amort_amt() != null) {
								R25cell5.setCellValue(record.getR25_acc_amort_amt().doubleValue());
								R25cell5.setCellStyle(numberStyle);
							} else {
								R25cell5.setCellValue("");
								R25cell5.setCellStyle(textStyle);
							}
							// R25 Col F
							Cell R25cell6 = row.createCell(6);
							if (record.getR25_close_bal() != null) {
								R25cell6.setCellValue(record.getR25_close_bal().doubleValue());
								R25cell6.setCellStyle(numberStyle);
							} else {
								R25cell6.setCellValue("");
								R25cell6.setCellStyle(textStyle);
							}

							// ==================== R26 ====================
							row = sheet.getRow(25);
							// R26 Col B
							Cell R26cell1 = row.createCell(1);
							if (record.getR26_cost_rev() != null) {
								R26cell1.setCellValue(record.getR26_cost_rev().doubleValue());
								R26cell1.setCellStyle(numberStyle);
							} else {
								R26cell1.setCellValue("");
								R26cell1.setCellStyle(textStyle);
							}

							// R26 Col C
							Cell R26cell2 = row.createCell(2);
							if (record.getR26_useful_life() != null) {
								R26cell2.setCellValue(record.getR26_useful_life().doubleValue());
								R26cell2.setCellStyle(numberStyle);
							} else {
								R26cell2.setCellValue("");
								R26cell2.setCellStyle(textStyle);
							}

							// R26 Col D
							Cell R26cell3 = row.createCell(3);
							if (record.getR26_res_value() != null) {
								R26cell3.setCellValue(record.getR26_res_value().doubleValue());
								R26cell3.setCellStyle(numberStyle);
							} else {
								R26cell3.setCellValue("");
								R26cell3.setCellStyle(textStyle);
							}

							// R26 Col E
							Cell R26cell4 = row.createCell(4);
							if (record.getR26_month_amort() != null) {
								R26cell4.setCellValue(record.getR26_month_amort().doubleValue());
								R26cell4.setCellStyle(numberStyle);
							} else {
								R26cell4.setCellValue("");
								R26cell4.setCellStyle(textStyle);
							}
							// R26 Col F
							Cell R26cell5 = row.createCell(5);
							if (record.getR26_acc_amort_amt() != null) {
								R26cell5.setCellValue(record.getR26_acc_amort_amt().doubleValue());
								R26cell5.setCellStyle(numberStyle);
							} else {
								R26cell5.setCellValue("");
								R26cell5.setCellStyle(textStyle);
							}
							// R26 Col F
							Cell R26cell6 = row.createCell(6);
							if (record.getR26_close_bal() != null) {
								R26cell6.setCellValue(record.getR26_close_bal().doubleValue());
								R26cell6.setCellStyle(numberStyle);
							} else {
								R26cell6.setCellValue("");
								R26cell6.setCellStyle(textStyle);
							}

							// ==================== R27 ====================
							row = sheet.getRow(26);
							// R27 Col B
							Cell R27cell1 = row.createCell(1);
							if (record.getR27_cost_rev() != null) {
								R27cell1.setCellValue(record.getR27_cost_rev().doubleValue());
								R27cell1.setCellStyle(numberStyle);
							} else {
								R27cell1.setCellValue("");
								R27cell1.setCellStyle(textStyle);
							}

							// R27 Col C
							Cell R27cell2 = row.createCell(2);
							if (record.getR27_useful_life() != null) {
								R27cell2.setCellValue(record.getR27_useful_life().doubleValue());
								R27cell2.setCellStyle(numberStyle);
							} else {
								R27cell2.setCellValue("");
								R27cell2.setCellStyle(textStyle);
							}

							// R27 Col D
							Cell R27cell3 = row.createCell(3);
							if (record.getR27_res_value() != null) {
								R27cell3.setCellValue(record.getR27_res_value().doubleValue());
								R27cell3.setCellStyle(numberStyle);
							} else {
								R27cell3.setCellValue("");
								R27cell3.setCellStyle(textStyle);
							}

							// R27 Col E
							Cell R27cell4 = row.createCell(4);
							if (record.getR27_month_amort() != null) {
								R27cell4.setCellValue(record.getR27_month_amort().doubleValue());
								R27cell4.setCellStyle(numberStyle);
							} else {
								R27cell4.setCellValue("");
								R27cell4.setCellStyle(textStyle);
							}
							// R27 Col F
							Cell R27cell5 = row.createCell(5);
							if (record.getR27_acc_amort_amt() != null) {
								R27cell5.setCellValue(record.getR27_acc_amort_amt().doubleValue());
								R27cell5.setCellStyle(numberStyle);
							} else {
								R27cell5.setCellValue("");
								R27cell5.setCellStyle(textStyle);
							}
							// R27 Col F
							Cell R27cell6 = row.createCell(6);
							if (record.getR27_close_bal() != null) {
								R27cell6.setCellValue(record.getR27_close_bal().doubleValue());
								R27cell6.setCellStyle(numberStyle);
							} else {
								R27cell6.setCellValue("");
								R27cell6.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FAS SUMMARY", null,
								"M_FAS_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_FASEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_FASEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
//		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_FASResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
		} else {
			List<M_FAS_Summary_Entity> dataList = jdbcTemplate.query(
					"SELECT * FROM BRRS_M_FAS_SUMMARYTABLE WHERE REPORT_DATE=?",
					new Object[] { dateformat.parse(todate) }, new GenericRowMapper<>(M_FAS_Summary_Entity.class));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_FAS report. Returning empty result.");
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
						M_FAS_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL
						Cell R12Cell = row.createCell(5);

						if (record.getReportDate() != null) {

							R12Cell.setCellValue(record.getReportDate());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(8);
						// R10 Col B
						Cell R10cell1 = row.createCell(3);
						if (record.getR10_cost() != null) {
							R10cell1.setCellValue(record.getR10_cost().doubleValue());
							R10cell1.setCellStyle(numberStyle);
						} else {
							R10cell1.setCellValue("");
							R10cell1.setCellStyle(textStyle);
						}

						// R10 Col C
						Cell R10cell2 = row.createCell(4);
						if (record.getR10_add() != null) {
							R10cell2.setCellValue(record.getR10_add().doubleValue());
							R10cell2.setCellStyle(numberStyle);
						} else {
							R10cell2.setCellValue("");
							R10cell2.setCellStyle(textStyle);
						}

						// R10 Col D
						Cell R10cell3 = row.createCell(5);
						if (record.getR10_disposals() != null) {
							R10cell3.setCellValue(record.getR10_disposals().doubleValue());
							R10cell3.setCellStyle(numberStyle);
						} else {
							R10cell3.setCellValue("");
							R10cell3.setCellStyle(textStyle);
						}

						// R10 Col E
						Cell R10cell4 = row.createCell(6);
						if (record.getR10_depreciation() != null) {
							R10cell4.setCellValue(record.getR10_depreciation().doubleValue());
							R10cell4.setCellStyle(numberStyle);
						} else {
							R10cell4.setCellValue("");
							R10cell4.setCellStyle(textStyle);
						}
						// ==================== R11 ====================
						// R11 Col B
						row = sheet.getRow(9);
						Cell R11cell1 = row.createCell(3);
						if (record.getR11_cost() != null) {
							R11cell1.setCellValue(record.getR11_cost().doubleValue());
							R11cell1.setCellStyle(numberStyle);
						} else {
							R11cell1.setCellValue("");
							R11cell1.setCellStyle(textStyle);
						}

						// R11 Col C
						Cell R11cell2 = row.createCell(4);
						if (record.getR11_add() != null) {
							R11cell2.setCellValue(record.getR11_add().doubleValue());
							R11cell2.setCellStyle(numberStyle);
						} else {
							R11cell2.setCellValue("");
							R11cell2.setCellStyle(textStyle);
						}

						// R11 Col D
						Cell R11cell3 = row.createCell(5);
						if (record.getR11_disposals() != null) {
							R11cell3.setCellValue(record.getR11_disposals().doubleValue());
							R11cell3.setCellStyle(numberStyle);
						} else {
							R11cell3.setCellValue("");
							R11cell3.setCellStyle(textStyle);
						}

						// R11 Col E
						Cell R11cell4 = row.createCell(6);
						if (record.getR11_depreciation() != null) {
							R11cell4.setCellValue(record.getR11_depreciation().doubleValue());
							R11cell4.setCellStyle(numberStyle);
						} else {
							R11cell4.setCellValue("");
							R11cell4.setCellStyle(textStyle);
						}

						// ==================== R12 ====================
						row = sheet.getRow(10);
						Cell R12cell1 = row.createCell(3);
						if (record.getR12_cost() != null) {
							R12cell1.setCellValue(record.getR12_cost().doubleValue());
							R12cell1.setCellStyle(numberStyle);
						} else {
							R12cell1.setCellValue("");
							R12cell1.setCellStyle(textStyle);
						}

						Cell R12cell2 = row.createCell(4);
						if (record.getR12_add() != null) {
							R12cell2.setCellValue(record.getR12_add().doubleValue());
							R12cell2.setCellStyle(numberStyle);
						} else {
							R12cell2.setCellValue("");
							R12cell2.setCellStyle(textStyle);
						}

						Cell R12cell3 = row.createCell(5);
						if (record.getR12_disposals() != null) {
							R12cell3.setCellValue(record.getR12_disposals().doubleValue());
							R12cell3.setCellStyle(numberStyle);
						} else {
							R12cell3.setCellValue("");
							R12cell3.setCellStyle(textStyle);
						}

						Cell R12cell4 = row.createCell(6);
						if (record.getR12_depreciation() != null) {
							R12cell4.setCellValue(record.getR12_depreciation().doubleValue());
							R12cell4.setCellStyle(numberStyle);
						} else {
							R12cell4.setCellValue("");
							R12cell4.setCellStyle(textStyle);
						}

						// ==================== R13 ====================
						row = sheet.getRow(11);
						Cell R13cell1 = row.createCell(3);
						if (record.getR13_cost() != null) {
							R13cell1.setCellValue(record.getR13_cost().doubleValue());
							R13cell1.setCellStyle(numberStyle);
						} else {
							R13cell1.setCellValue("");
							R13cell1.setCellStyle(textStyle);
						}

						Cell R13cell2 = row.createCell(4);
						if (record.getR13_add() != null) {
							R13cell2.setCellValue(record.getR13_add().doubleValue());
							R13cell2.setCellStyle(numberStyle);
						} else {
							R13cell2.setCellValue("");
							R13cell2.setCellStyle(textStyle);
						}

						Cell R13cell3 = row.createCell(5);
						if (record.getR13_disposals() != null) {
							R13cell3.setCellValue(record.getR13_disposals().doubleValue());
							R13cell3.setCellStyle(numberStyle);
						} else {
							R13cell3.setCellValue("");
							R13cell3.setCellStyle(textStyle);
						}

						Cell R13cell4 = row.createCell(6);
						if (record.getR13_depreciation() != null) {
							R13cell4.setCellValue(record.getR13_depreciation().doubleValue());
							R13cell4.setCellStyle(numberStyle);
						} else {
							R13cell4.setCellValue("");
							R13cell4.setCellStyle(textStyle);
						}

						// ==================== R14 ====================
						row = sheet.getRow(12);
						Cell R14cell1 = row.createCell(3);
						if (record.getR14_cost() != null) {
							R14cell1.setCellValue(record.getR14_cost().doubleValue());
							R14cell1.setCellStyle(numberStyle);
						} else {
							R14cell1.setCellValue("");
							R14cell1.setCellStyle(textStyle);
						}

						Cell R14cell2 = row.createCell(4);
						if (record.getR14_add() != null) {
							R14cell2.setCellValue(record.getR14_add().doubleValue());
							R14cell2.setCellStyle(numberStyle);
						} else {
							R14cell2.setCellValue("");
							R14cell2.setCellStyle(textStyle);
						}

						Cell R14cell3 = row.createCell(5);
						if (record.getR14_disposals() != null) {
							R14cell3.setCellValue(record.getR14_disposals().doubleValue());
							R14cell3.setCellStyle(numberStyle);
						} else {
							R14cell3.setCellValue("");
							R14cell3.setCellStyle(textStyle);
						}

						Cell R14cell4 = row.createCell(6);
						if (record.getR14_depreciation() != null) {
							R14cell4.setCellValue(record.getR14_depreciation().doubleValue());
							R14cell4.setCellStyle(numberStyle);
						} else {
							R14cell4.setCellValue("");
							R14cell4.setCellStyle(textStyle);
						}

						// ==================== R15 ====================
						row = sheet.getRow(13);
						Cell R15cell1 = row.createCell(3);
						if (record.getR15_cost() != null) {
							R15cell1.setCellValue(record.getR15_cost().doubleValue());
							R15cell1.setCellStyle(numberStyle);
						} else {
							R15cell1.setCellValue("");
							R15cell1.setCellStyle(textStyle);
						}

						Cell R15cell2 = row.createCell(4);
						if (record.getR15_add() != null) {
							R15cell2.setCellValue(record.getR15_add().doubleValue());
							R15cell2.setCellStyle(numberStyle);
						} else {
							R15cell2.setCellValue("");
							R15cell2.setCellStyle(textStyle);
						}

						Cell R15cell3 = row.createCell(5);
						if (record.getR15_disposals() != null) {
							R15cell3.setCellValue(record.getR15_disposals().doubleValue());
							R15cell3.setCellStyle(numberStyle);
						} else {
							R15cell3.setCellValue("");
							R15cell3.setCellStyle(textStyle);
						}

						Cell R15cell4 = row.createCell(6);
						if (record.getR15_depreciation() != null) {
							R15cell4.setCellValue(record.getR15_depreciation().doubleValue());
							R15cell4.setCellStyle(numberStyle);
						} else {
							R15cell4.setCellValue("");
							R15cell4.setCellStyle(textStyle);
						}

						// ==================== R16 ====================
						row = sheet.getRow(14);
						Cell R16cell1 = row.createCell(3);
						if (record.getR16_cost() != null) {
							R16cell1.setCellValue(record.getR16_cost().doubleValue());
							R16cell1.setCellStyle(numberStyle);
						} else {
							R16cell1.setCellValue("");
							R16cell1.setCellStyle(textStyle);
						}

						Cell R16cell2 = row.createCell(4);
						if (record.getR16_add() != null) {
							R16cell2.setCellValue(record.getR16_add().doubleValue());
							R16cell2.setCellStyle(numberStyle);
						} else {
							R16cell2.setCellValue("");
							R16cell2.setCellStyle(textStyle);
						}

						Cell R16cell3 = row.createCell(5);
						if (record.getR16_disposals() != null) {
							R16cell3.setCellValue(record.getR16_disposals().doubleValue());
							R16cell3.setCellStyle(numberStyle);
						} else {
							R16cell3.setCellValue("");
							R16cell3.setCellStyle(textStyle);
						}

						Cell R16cell4 = row.createCell(6);
						if (record.getR16_depreciation() != null) {
							R16cell4.setCellValue(record.getR16_depreciation().doubleValue());
							R16cell4.setCellStyle(numberStyle);
						} else {
							R16cell4.setCellValue("");
							R16cell4.setCellStyle(textStyle);
						}

						// ==================== R23 ====================
						row = sheet.getRow(21);
						// R23 Col B
						Cell R23cell1 = row.createCell(3);
						if (record.getR23_cost_rev() != null) {
							R23cell1.setCellValue(record.getR23_cost_rev().doubleValue());
							R23cell1.setCellStyle(numberStyle);
						} else {
							R23cell1.setCellValue("");
							R23cell1.setCellStyle(textStyle);
						}

						// R23 Col C
						Cell R23cell2 = row.createCell(4);
						if (record.getR23_useful_life() != null) {
							R23cell2.setCellValue(record.getR23_useful_life().doubleValue());
							R23cell2.setCellStyle(numberStyle);
						} else {
							R23cell2.setCellValue("");
							R23cell2.setCellStyle(textStyle);
						}

						// R23 Col D
						Cell R23cell3 = row.createCell(5);
						if (record.getR23_res_value() != null) {
							R23cell3.setCellValue(record.getR23_res_value().doubleValue());
							R23cell3.setCellStyle(numberStyle);
						} else {
							R23cell3.setCellValue("");
							R23cell3.setCellStyle(textStyle);
						}

						// R23 Col E
						Cell R23cell4 = row.createCell(6);
						if (record.getR23_month_amort() != null) {
							R23cell4.setCellValue(record.getR23_month_amort().doubleValue());
							R23cell4.setCellStyle(numberStyle);
						} else {
							R23cell4.setCellValue("");
							R23cell4.setCellStyle(textStyle);
						}
						// R23 Col F
						Cell R23cell5 = row.createCell(7);
						if (record.getR23_acc_amort_amt() != null) {
							R23cell5.setCellValue(record.getR23_acc_amort_amt().doubleValue());
							R23cell5.setCellStyle(numberStyle);
						} else {
							R23cell5.setCellValue("");
							R23cell5.setCellStyle(textStyle);
						}

						// ==================== R24 ====================

						row = sheet.getRow(22);
						// R24 Col B
						Cell R24cell1 = row.createCell(3);
						if (record.getR24_cost_rev() != null) {
							R24cell1.setCellValue(record.getR24_cost_rev().doubleValue());
							R24cell1.setCellStyle(numberStyle);
						} else {
							R24cell1.setCellValue("");
							R24cell1.setCellStyle(textStyle);
						}

						// R24 Col C
						Cell R24cell2 = row.createCell(4);
						if (record.getR24_useful_life() != null) {
							R24cell2.setCellValue(record.getR24_useful_life().doubleValue());
							R24cell2.setCellStyle(numberStyle);
						} else {
							R24cell2.setCellValue("");
							R24cell2.setCellStyle(textStyle);
						}

						// R24 Col D
						Cell R24cell3 = row.createCell(5);
						if (record.getR24_res_value() != null) {
							R24cell3.setCellValue(record.getR24_res_value().doubleValue());
							R24cell3.setCellStyle(numberStyle);
						} else {
							R24cell3.setCellValue("");
							R24cell3.setCellStyle(textStyle);
						}

						// R24 Col E
						Cell R24cell4 = row.createCell(6);
						if (record.getR24_month_amort() != null) {
							R24cell4.setCellValue(record.getR24_month_amort().doubleValue());
							R24cell4.setCellStyle(numberStyle);
						} else {
							R24cell4.setCellValue("");
							R24cell4.setCellStyle(textStyle);
						}
						// R24 Col F
						Cell R24cell5 = row.createCell(7);
						if (record.getR24_acc_amort_amt() != null) {
							R24cell5.setCellValue(record.getR24_acc_amort_amt().doubleValue());
							R24cell5.setCellStyle(numberStyle);
						} else {
							R24cell5.setCellValue("");
							R24cell5.setCellStyle(textStyle);
						}

						// ==================== R25 ====================
						row = sheet.getRow(23);
						// R25 Col B
						Cell R25cell1 = row.createCell(3);
						if (record.getR25_cost_rev() != null) {
							R25cell1.setCellValue(record.getR25_cost_rev().doubleValue());
							R25cell1.setCellStyle(numberStyle);
						} else {
							R25cell1.setCellValue("");
							R25cell1.setCellStyle(textStyle);
						}

						// R25 Col C
						Cell R25cell2 = row.createCell(4);
						if (record.getR25_useful_life() != null) {
							R25cell2.setCellValue(record.getR25_useful_life().doubleValue());
							R25cell2.setCellStyle(numberStyle);
						} else {
							R25cell2.setCellValue("");
							R25cell2.setCellStyle(textStyle);
						}

						// R25 Col D
						Cell R25cell3 = row.createCell(5);
						if (record.getR25_res_value() != null) {
							R25cell3.setCellValue(record.getR25_res_value().doubleValue());
							R25cell3.setCellStyle(numberStyle);
						} else {
							R25cell3.setCellValue("");
							R25cell3.setCellStyle(textStyle);
						}

						// R25 Col E
						Cell R25cell4 = row.createCell(6);
						if (record.getR25_month_amort() != null) {
							R25cell4.setCellValue(record.getR25_month_amort().doubleValue());
							R25cell4.setCellStyle(numberStyle);
						} else {
							R25cell4.setCellValue("");
							R25cell4.setCellStyle(textStyle);
						}
						// R25 Col F
						Cell R25cell5 = row.createCell(7);
						if (record.getR25_acc_amort_amt() != null) {
							R25cell5.setCellValue(record.getR25_acc_amort_amt().doubleValue());
							R25cell5.setCellStyle(numberStyle);
						} else {
							R25cell5.setCellValue("");
							R25cell5.setCellStyle(textStyle);
						}

						// ==================== R26 ====================
						row = sheet.getRow(24);
						// R26 Col B
						Cell R26cell1 = row.createCell(3);
						if (record.getR26_cost_rev() != null) {
							R26cell1.setCellValue(record.getR26_cost_rev().doubleValue());
							R26cell1.setCellStyle(numberStyle);
						} else {
							R26cell1.setCellValue("");
							R26cell1.setCellStyle(textStyle);
						}

						// R26 Col C
						Cell R26cell2 = row.createCell(4);
						if (record.getR26_useful_life() != null) {
							R26cell2.setCellValue(record.getR26_useful_life().doubleValue());
							R26cell2.setCellStyle(numberStyle);
						} else {
							R26cell2.setCellValue("");
							R26cell2.setCellStyle(textStyle);
						}

						// R26 Col D
						Cell R26cell3 = row.createCell(5);
						if (record.getR26_res_value() != null) {
							R26cell3.setCellValue(record.getR26_res_value().doubleValue());
							R26cell3.setCellStyle(numberStyle);
						} else {
							R26cell3.setCellValue("");
							R26cell3.setCellStyle(textStyle);
						}

						// R26 Col E
						Cell R26cell4 = row.createCell(6);
						if (record.getR26_month_amort() != null) {
							R26cell4.setCellValue(record.getR26_month_amort().doubleValue());
							R26cell4.setCellStyle(numberStyle);
						} else {
							R26cell4.setCellValue("");
							R26cell4.setCellStyle(textStyle);
						}
						// R26 Col F
						Cell R26cell5 = row.createCell(7);
						if (record.getR26_acc_amort_amt() != null) {
							R26cell5.setCellValue(record.getR26_acc_amort_amt().doubleValue());
							R26cell5.setCellStyle(numberStyle);
						} else {
							R26cell5.setCellValue("");
							R26cell5.setCellStyle(textStyle);
						}

						// ==================== R27 ====================
						row = sheet.getRow(25);
						// R27 Col B
						Cell R27cell1 = row.createCell(3);
						if (record.getR27_cost_rev() != null) {
							R27cell1.setCellValue(record.getR27_cost_rev().doubleValue());
							R27cell1.setCellStyle(numberStyle);
						} else {
							R27cell1.setCellValue("");
							R27cell1.setCellStyle(textStyle);
						}

						// R27 Col C
						Cell R27cell2 = row.createCell(4);
						if (record.getR27_useful_life() != null) {
							R27cell2.setCellValue(record.getR27_useful_life().doubleValue());
							R27cell2.setCellStyle(numberStyle);
						} else {
							R27cell2.setCellValue("");
							R27cell2.setCellStyle(textStyle);
						}

						// R27 Col D
						Cell R27cell3 = row.createCell(5);
						if (record.getR27_res_value() != null) {
							R27cell3.setCellValue(record.getR27_res_value().doubleValue());
							R27cell3.setCellStyle(numberStyle);
						} else {
							R27cell3.setCellValue("");
							R27cell3.setCellStyle(textStyle);
						}

						// R27 Col E
						Cell R27cell4 = row.createCell(6);
						if (record.getR27_month_amort() != null) {
							R27cell4.setCellValue(record.getR27_month_amort().doubleValue());
							R27cell4.setCellStyle(numberStyle);
						} else {
							R27cell4.setCellValue("");
							R27cell4.setCellStyle(textStyle);
						}
						// R27 Col F
						Cell R27cell5 = row.createCell(7);
						if (record.getR27_acc_amort_amt() != null) {
							R27cell5.setCellValue(record.getR27_acc_amort_amt().doubleValue());
							R27cell5.setCellStyle(numberStyle);
						} else {
							R27cell5.setCellValue("");
							R27cell5.setCellStyle(textStyle);
						}

						// ==================== R28 ====================
						row = sheet.getRow(26);
						// R28 Col B
						Cell R28cell1 = row.createCell(3);
						if (record.getR28_cost_rev() != null) {
							R28cell1.setCellValue(record.getR28_cost_rev().doubleValue());
							R28cell1.setCellStyle(numberStyle);
						} else {
							R28cell1.setCellValue("");
							R28cell1.setCellStyle(textStyle);
						}

						// R28 Col C
						Cell R28cell2 = row.createCell(4);
						if (record.getR28_useful_life() != null) {
							R28cell2.setCellValue(record.getR28_useful_life().doubleValue());
							R28cell2.setCellStyle(numberStyle);
						} else {
							R28cell2.setCellValue("");
							R28cell2.setCellStyle(textStyle);
						}

						// R28 Col D
						Cell R28cell3 = row.createCell(5);
						if (record.getR28_res_value() != null) {
							R28cell3.setCellValue(record.getR28_res_value().doubleValue());
							R28cell3.setCellStyle(numberStyle);
						} else {
							R28cell3.setCellValue("");
							R28cell3.setCellStyle(textStyle);
						}

						// R28 Col E
						Cell R28cell4 = row.createCell(6);
						if (record.getR28_month_amort() != null) {
							R28cell4.setCellValue(record.getR28_month_amort().doubleValue());
							R28cell4.setCellStyle(numberStyle);
						} else {
							R28cell4.setCellValue("");
							R28cell4.setCellStyle(textStyle);
						}
						// R28 Col F
						Cell R28cell5 = row.createCell(7);
						if (record.getR28_acc_amort_amt() != null) {
							R28cell5.setCellValue(record.getR28_acc_amort_amt().doubleValue());
							R28cell5.setCellStyle(numberStyle);
						} else {
							R28cell5.setCellValue("");
							R28cell5.setCellStyle(textStyle);
						}

					}
					workbook.setForceFormulaRecalculation(true);
				} else

				{

				}

				// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FAS EMAIL SUMMARY", null,
							"M_FAS_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getSummaryExcelARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_FASEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_FAS_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_FAS_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version },
				new GenericRowMapper<>(M_FAS_Archival_Summary_Entity.class));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_FAS report. Returning empty result.");
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
					M_FAS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL
					Cell R12Cell = row.createCell(1);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(9);

					// R10 Col B
					Cell R10cell1 = row.createCell(1);
					if (record.getR10_cost() != null) {
						R10cell1.setCellValue(record.getR10_cost().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(2);
					if (record.getR10_add() != null) {
						R10cell2.setCellValue(record.getR10_add().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}

					// R10 Col D
					Cell R10cell3 = row.createCell(3);
					if (record.getR10_disposals() != null) {
						R10cell3.setCellValue(record.getR10_disposals().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10cell4 = row.createCell(4);
					if (record.getR10_depreciation() != null) {
						R10cell4.setCellValue(record.getR10_depreciation().doubleValue());
						R10cell4.setCellStyle(numberStyle);
					} else {
						R10cell4.setCellValue("");
						R10cell4.setCellStyle(textStyle);
					}
					// ==================== R11 ====================
					// R11 Col B
					row = sheet.getRow(10);
					Cell R11cell1 = row.createCell(1);
					if (record.getR11_cost() != null) {
						R11cell1.setCellValue(record.getR11_cost().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(2);
					if (record.getR11_add() != null) {
						R11cell2.setCellValue(record.getR11_add().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 Col D
					Cell R11cell3 = row.createCell(3);
					if (record.getR11_disposals() != null) {
						R11cell3.setCellValue(record.getR11_disposals().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}

					// R11 Col E
					Cell R11cell4 = row.createCell(4);
					if (record.getR11_depreciation() != null) {
						R11cell4.setCellValue(record.getR11_depreciation().doubleValue());
						R11cell4.setCellStyle(numberStyle);
					} else {
						R11cell4.setCellValue("");
						R11cell4.setCellStyle(textStyle);
					}

					// ==================== R12 ====================
					row = sheet.getRow(11);
					Cell R12cell1 = row.createCell(1);
					if (record.getR12_cost() != null) {
						R12cell1.setCellValue(record.getR12_cost().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					Cell R12cell2 = row.createCell(2);
					if (record.getR12_add() != null) {
						R12cell2.setCellValue(record.getR12_add().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					Cell R12cell3 = row.createCell(3);
					if (record.getR12_disposals() != null) {
						R12cell3.setCellValue(record.getR12_disposals().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					Cell R12cell4 = row.createCell(4);
					if (record.getR12_depreciation() != null) {
						R12cell4.setCellValue(record.getR12_depreciation().doubleValue());
						R12cell4.setCellStyle(numberStyle);
					} else {
						R12cell4.setCellValue("");
						R12cell4.setCellStyle(textStyle);
					}

					// ==================== R13 ====================
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(1);
					if (record.getR13_cost() != null) {
						R13cell1.setCellValue(record.getR13_cost().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					Cell R13cell2 = row.createCell(2);
					if (record.getR13_add() != null) {
						R13cell2.setCellValue(record.getR13_add().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					Cell R13cell3 = row.createCell(3);
					if (record.getR13_disposals() != null) {
						R13cell3.setCellValue(record.getR13_disposals().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					Cell R13cell4 = row.createCell(4);
					if (record.getR13_depreciation() != null) {
						R13cell4.setCellValue(record.getR13_depreciation().doubleValue());
						R13cell4.setCellStyle(numberStyle);
					} else {
						R13cell4.setCellValue("");
						R13cell4.setCellStyle(textStyle);
					}

					// ==================== R14 ====================
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_cost() != null) {
						R14cell1.setCellValue(record.getR14_cost().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					Cell R14cell2 = row.createCell(2);
					if (record.getR14_add() != null) {
						R14cell2.setCellValue(record.getR14_add().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					Cell R14cell3 = row.createCell(3);
					if (record.getR14_disposals() != null) {
						R14cell3.setCellValue(record.getR14_disposals().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					Cell R14cell4 = row.createCell(4);
					if (record.getR14_depreciation() != null) {
						R14cell4.setCellValue(record.getR14_depreciation().doubleValue());
						R14cell4.setCellStyle(numberStyle);
					} else {
						R14cell4.setCellValue("");
						R14cell4.setCellStyle(textStyle);
					}

					// ==================== R15 ====================
					row = sheet.getRow(14);
					Cell R15cell1 = row.createCell(1);
					if (record.getR15_cost() != null) {
						R15cell1.setCellValue(record.getR15_cost().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

					Cell R15cell2 = row.createCell(2);
					if (record.getR15_add() != null) {
						R15cell2.setCellValue(record.getR15_add().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

					Cell R15cell3 = row.createCell(3);
					if (record.getR15_disposals() != null) {
						R15cell3.setCellValue(record.getR15_disposals().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					Cell R15cell4 = row.createCell(4);
					if (record.getR15_depreciation() != null) {
						R15cell4.setCellValue(record.getR15_depreciation().doubleValue());
						R15cell4.setCellStyle(numberStyle);
					} else {
						R15cell4.setCellValue("");
						R15cell4.setCellStyle(textStyle);
					}

					// ==================== R16 ====================
					row = sheet.getRow(15);
					Cell R16cell1 = row.createCell(1);
					if (record.getR16_cost() != null) {
						R16cell1.setCellValue(record.getR16_cost().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

					Cell R16cell2 = row.createCell(2);
					if (record.getR16_add() != null) {
						R16cell2.setCellValue(record.getR16_add().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

					Cell R16cell3 = row.createCell(3);
					if (record.getR16_disposals() != null) {
						R16cell3.setCellValue(record.getR16_disposals().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					Cell R16cell4 = row.createCell(4);
					if (record.getR16_depreciation() != null) {
						R16cell4.setCellValue(record.getR16_depreciation().doubleValue());
						R16cell4.setCellStyle(numberStyle);
					} else {
						R16cell4.setCellValue("");
						R16cell4.setCellStyle(textStyle);
					}

					// ==================== R23 ====================
					row = sheet.getRow(22);
					// R23 Col B
					Cell R23cell1 = row.createCell(1);
					if (record.getR23_cost_rev() != null) {
						R23cell1.setCellValue(record.getR23_cost_rev().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					// R23 Col C
					Cell R23cell2 = row.createCell(2);
					if (record.getR23_useful_life() != null) {
						R23cell2.setCellValue(record.getR23_useful_life().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					// R23 Col D
					Cell R23cell3 = row.createCell(3);
					if (record.getR23_res_value() != null) {
						R23cell3.setCellValue(record.getR23_res_value().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// R23 Col E
					Cell R23cell4 = row.createCell(4);
					if (record.getR23_month_amort() != null) {
						R23cell4.setCellValue(record.getR23_month_amort().doubleValue());
						R23cell4.setCellStyle(numberStyle);
					} else {
						R23cell4.setCellValue("");
						R23cell4.setCellStyle(textStyle);
					}
					// R23 Col F
					Cell R23cell5 = row.createCell(5);
					if (record.getR23_acc_amort_amt() != null) {
						R23cell5.setCellValue(record.getR23_acc_amort_amt().doubleValue());
						R23cell5.setCellStyle(numberStyle);
					} else {
						R23cell5.setCellValue("");
						R23cell5.setCellStyle(textStyle);
					}
					// R23 Col F
					Cell R23cell6 = row.createCell(6);
					if (record.getR23_close_bal() != null) {
						R23cell6.setCellValue(record.getR23_close_bal().doubleValue());
						R23cell6.setCellStyle(numberStyle);
					} else {
						R23cell6.setCellValue("");
						R23cell6.setCellStyle(textStyle);
					}
					// ==================== R24 ====================

					row = sheet.getRow(23);
					// R24 Col B
					Cell R24cell1 = row.createCell(1);
					if (record.getR24_cost_rev() != null) {
						R24cell1.setCellValue(record.getR24_cost_rev().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					// R24 Col C
					Cell R24cell2 = row.createCell(2);
					if (record.getR24_useful_life() != null) {
						R24cell2.setCellValue(record.getR24_useful_life().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					// R24 Col D
					Cell R24cell3 = row.createCell(3);
					if (record.getR24_res_value() != null) {
						R24cell3.setCellValue(record.getR24_res_value().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// R24 Col E
					Cell R24cell4 = row.createCell(4);
					if (record.getR24_month_amort() != null) {
						R24cell4.setCellValue(record.getR24_month_amort().doubleValue());
						R24cell4.setCellStyle(numberStyle);
					} else {
						R24cell4.setCellValue("");
						R24cell4.setCellStyle(textStyle);
					}
					// R24 Col F
					Cell R24cell5 = row.createCell(5);
					if (record.getR24_acc_amort_amt() != null) {
						R24cell5.setCellValue(record.getR24_acc_amort_amt().doubleValue());
						R24cell5.setCellStyle(numberStyle);
					} else {
						R24cell5.setCellValue("");
						R24cell5.setCellStyle(textStyle);
					}
					// R24 Col F
					Cell R24cell6 = row.createCell(6);
					if (record.getR24_close_bal() != null) {
						R24cell6.setCellValue(record.getR24_close_bal().doubleValue());
						R24cell6.setCellStyle(numberStyle);
					} else {
						R24cell6.setCellValue("");
						R24cell6.setCellStyle(textStyle);
					}

					// ==================== R25 ====================
					row = sheet.getRow(24);
					// R25 Col B
					Cell R25cell1 = row.createCell(1);
					if (record.getR25_cost_rev() != null) {
						R25cell1.setCellValue(record.getR25_cost_rev().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col C
					Cell R25cell2 = row.createCell(2);
					if (record.getR25_useful_life() != null) {
						R25cell2.setCellValue(record.getR25_useful_life().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col D
					Cell R25cell3 = row.createCell(3);
					if (record.getR25_res_value() != null) {
						R25cell3.setCellValue(record.getR25_res_value().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// R25 Col E
					Cell R25cell4 = row.createCell(4);
					if (record.getR25_month_amort() != null) {
						R25cell4.setCellValue(record.getR25_month_amort().doubleValue());
						R25cell4.setCellStyle(numberStyle);
					} else {
						R25cell4.setCellValue("");
						R25cell4.setCellStyle(textStyle);
					}
					// R25 Col F
					Cell R25cell5 = row.createCell(5);
					if (record.getR25_acc_amort_amt() != null) {
						R25cell5.setCellValue(record.getR25_acc_amort_amt().doubleValue());
						R25cell5.setCellStyle(numberStyle);
					} else {
						R25cell5.setCellValue("");
						R25cell5.setCellStyle(textStyle);
					}
					// R25 Col F
					Cell R25cell6 = row.createCell(6);
					if (record.getR25_close_bal() != null) {
						R25cell6.setCellValue(record.getR25_close_bal().doubleValue());
						R25cell6.setCellStyle(numberStyle);
					} else {
						R25cell6.setCellValue("");
						R25cell6.setCellStyle(textStyle);
					}

					// ==================== R26 ====================
					row = sheet.getRow(25);
					// R26 Col B
					Cell R26cell1 = row.createCell(1);
					if (record.getR26_cost_rev() != null) {
						R26cell1.setCellValue(record.getR26_cost_rev().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col C
					Cell R26cell2 = row.createCell(2);
					if (record.getR26_useful_life() != null) {
						R26cell2.setCellValue(record.getR26_useful_life().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col D
					Cell R26cell3 = row.createCell(3);
					if (record.getR26_res_value() != null) {
						R26cell3.setCellValue(record.getR26_res_value().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// R26 Col E
					Cell R26cell4 = row.createCell(4);
					if (record.getR26_month_amort() != null) {
						R26cell4.setCellValue(record.getR26_month_amort().doubleValue());
						R26cell4.setCellStyle(numberStyle);
					} else {
						R26cell4.setCellValue("");
						R26cell4.setCellStyle(textStyle);
					}
					// R26 Col F
					Cell R26cell5 = row.createCell(5);
					if (record.getR26_acc_amort_amt() != null) {
						R26cell5.setCellValue(record.getR26_acc_amort_amt().doubleValue());
						R26cell5.setCellStyle(numberStyle);
					} else {
						R26cell5.setCellValue("");
						R26cell5.setCellStyle(textStyle);
					}
					// R26 Col F
					Cell R26cell6 = row.createCell(6);
					if (record.getR26_close_bal() != null) {
						R26cell6.setCellValue(record.getR26_close_bal().doubleValue());
						R26cell6.setCellStyle(numberStyle);
					} else {
						R26cell6.setCellValue("");
						R26cell6.setCellStyle(textStyle);
					}

					// ==================== R27 ====================
					row = sheet.getRow(26);
					// R27 Col B
					Cell R27cell1 = row.createCell(1);
					if (record.getR27_cost_rev() != null) {
						R27cell1.setCellValue(record.getR27_cost_rev().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col C
					Cell R27cell2 = row.createCell(2);
					if (record.getR27_useful_life() != null) {
						R27cell2.setCellValue(record.getR27_useful_life().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col D
					Cell R27cell3 = row.createCell(3);
					if (record.getR27_res_value() != null) {
						R27cell3.setCellValue(record.getR27_res_value().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// R27 Col E
					Cell R27cell4 = row.createCell(4);
					if (record.getR27_month_amort() != null) {
						R27cell4.setCellValue(record.getR27_month_amort().doubleValue());
						R27cell4.setCellStyle(numberStyle);
					} else {
						R27cell4.setCellValue("");
						R27cell4.setCellStyle(textStyle);
					}
					// R27 Col F
					Cell R27cell5 = row.createCell(5);
					if (record.getR27_acc_amort_amt() != null) {
						R27cell5.setCellValue(record.getR27_acc_amort_amt().doubleValue());
						R27cell5.setCellStyle(numberStyle);
					} else {
						R27cell5.setCellValue("");
						R27cell5.setCellStyle(textStyle);
					}
					// R27 Col F
					Cell R27cell6 = row.createCell(6);
					if (record.getR27_close_bal() != null) {
						R27cell6.setCellValue(record.getR27_close_bal().doubleValue());
						R27cell6.setCellStyle(numberStyle);
					} else {
						R27cell6.setCellValue("");
						R27cell6.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FAS ARCHIVAL SUMMARY", null,
						"M_FAS_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_FASEmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_FAS_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"select * from BRRS_M_FAS_ARCHIVALTABLE_SUMMARY where REPORT_DATE = ? and REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version },
				new GenericRowMapper<>(M_FAS_Archival_Summary_Entity.class));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_FAS report. Returning empty result.");
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
					M_FAS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL
					Cell R12Cell = row.createCell(5);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);
					// R10 Col B
					Cell R10cell1 = row.createCell(3);
					if (record.getR10_cost() != null) {
						R10cell1.setCellValue(record.getR10_cost().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(4);
					if (record.getR10_add() != null) {
						R10cell2.setCellValue(record.getR10_add().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}

					// R10 Col D
					Cell R10cell3 = row.createCell(5);
					if (record.getR10_disposals() != null) {
						R10cell3.setCellValue(record.getR10_disposals().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10cell4 = row.createCell(6);
					if (record.getR10_depreciation() != null) {
						R10cell4.setCellValue(record.getR10_depreciation().doubleValue());
						R10cell4.setCellStyle(numberStyle);
					} else {
						R10cell4.setCellValue("");
						R10cell4.setCellStyle(textStyle);
					}
					// ==================== R11 ====================
					// R11 Col B
					row = sheet.getRow(9);
					Cell R11cell1 = row.createCell(3);
					if (record.getR11_cost() != null) {
						R11cell1.setCellValue(record.getR11_cost().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(4);
					if (record.getR11_add() != null) {
						R11cell2.setCellValue(record.getR11_add().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 Col D
					Cell R11cell3 = row.createCell(5);
					if (record.getR11_disposals() != null) {
						R11cell3.setCellValue(record.getR11_disposals().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}

					// R11 Col E
					Cell R11cell4 = row.createCell(6);
					if (record.getR11_depreciation() != null) {
						R11cell4.setCellValue(record.getR11_depreciation().doubleValue());
						R11cell4.setCellStyle(numberStyle);
					} else {
						R11cell4.setCellValue("");
						R11cell4.setCellStyle(textStyle);
					}

					// ==================== R12 ====================
					row = sheet.getRow(10);
					Cell R12cell1 = row.createCell(3);
					if (record.getR12_cost() != null) {
						R12cell1.setCellValue(record.getR12_cost().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					Cell R12cell2 = row.createCell(4);
					if (record.getR12_add() != null) {
						R12cell2.setCellValue(record.getR12_add().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					Cell R12cell3 = row.createCell(5);
					if (record.getR12_disposals() != null) {
						R12cell3.setCellValue(record.getR12_disposals().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					Cell R12cell4 = row.createCell(6);
					if (record.getR12_depreciation() != null) {
						R12cell4.setCellValue(record.getR12_depreciation().doubleValue());
						R12cell4.setCellStyle(numberStyle);
					} else {
						R12cell4.setCellValue("");
						R12cell4.setCellStyle(textStyle);
					}

					// ==================== R13 ====================
					row = sheet.getRow(11);
					Cell R13cell1 = row.createCell(3);
					if (record.getR13_cost() != null) {
						R13cell1.setCellValue(record.getR13_cost().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					Cell R13cell2 = row.createCell(4);
					if (record.getR13_add() != null) {
						R13cell2.setCellValue(record.getR13_add().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					Cell R13cell3 = row.createCell(5);
					if (record.getR13_disposals() != null) {
						R13cell3.setCellValue(record.getR13_disposals().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					Cell R13cell4 = row.createCell(6);
					if (record.getR13_depreciation() != null) {
						R13cell4.setCellValue(record.getR13_depreciation().doubleValue());
						R13cell4.setCellStyle(numberStyle);
					} else {
						R13cell4.setCellValue("");
						R13cell4.setCellStyle(textStyle);
					}

					// ==================== R14 ====================
					row = sheet.getRow(12);
					Cell R14cell1 = row.createCell(3);
					if (record.getR14_cost() != null) {
						R14cell1.setCellValue(record.getR14_cost().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					Cell R14cell2 = row.createCell(4);
					if (record.getR14_add() != null) {
						R14cell2.setCellValue(record.getR14_add().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					Cell R14cell3 = row.createCell(5);
					if (record.getR14_disposals() != null) {
						R14cell3.setCellValue(record.getR14_disposals().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					Cell R14cell4 = row.createCell(6);
					if (record.getR14_depreciation() != null) {
						R14cell4.setCellValue(record.getR14_depreciation().doubleValue());
						R14cell4.setCellStyle(numberStyle);
					} else {
						R14cell4.setCellValue("");
						R14cell4.setCellStyle(textStyle);
					}

					// ==================== R15 ====================
					row = sheet.getRow(13);
					Cell R15cell1 = row.createCell(3);
					if (record.getR15_cost() != null) {
						R15cell1.setCellValue(record.getR15_cost().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

					Cell R15cell2 = row.createCell(4);
					if (record.getR15_add() != null) {
						R15cell2.setCellValue(record.getR15_add().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

					Cell R15cell3 = row.createCell(5);
					if (record.getR15_disposals() != null) {
						R15cell3.setCellValue(record.getR15_disposals().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					Cell R15cell4 = row.createCell(6);
					if (record.getR15_depreciation() != null) {
						R15cell4.setCellValue(record.getR15_depreciation().doubleValue());
						R15cell4.setCellStyle(numberStyle);
					} else {
						R15cell4.setCellValue("");
						R15cell4.setCellStyle(textStyle);
					}

					// ==================== R16 ====================
					row = sheet.getRow(14);
					Cell R16cell1 = row.createCell(3);
					if (record.getR16_cost() != null) {
						R16cell1.setCellValue(record.getR16_cost().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

					Cell R16cell2 = row.createCell(4);
					if (record.getR16_add() != null) {
						R16cell2.setCellValue(record.getR16_add().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

					Cell R16cell3 = row.createCell(5);
					if (record.getR16_disposals() != null) {
						R16cell3.setCellValue(record.getR16_disposals().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					Cell R16cell4 = row.createCell(6);
					if (record.getR16_depreciation() != null) {
						R16cell4.setCellValue(record.getR16_depreciation().doubleValue());
						R16cell4.setCellStyle(numberStyle);
					} else {
						R16cell4.setCellValue("");
						R16cell4.setCellStyle(textStyle);
					}

					// ==================== R23 ====================
					row = sheet.getRow(21);
					// R23 Col B
					Cell R23cell1 = row.createCell(3);
					if (record.getR23_cost_rev() != null) {
						R23cell1.setCellValue(record.getR23_cost_rev().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					// R23 Col C
					Cell R23cell2 = row.createCell(4);
					if (record.getR23_useful_life() != null) {
						R23cell2.setCellValue(record.getR23_useful_life().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					// R23 Col D
					Cell R23cell3 = row.createCell(5);
					if (record.getR23_res_value() != null) {
						R23cell3.setCellValue(record.getR23_res_value().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// R23 Col E
					Cell R23cell4 = row.createCell(6);
					if (record.getR23_month_amort() != null) {
						R23cell4.setCellValue(record.getR23_month_amort().doubleValue());
						R23cell4.setCellStyle(numberStyle);
					} else {
						R23cell4.setCellValue("");
						R23cell4.setCellStyle(textStyle);
					}
					// R23 Col F
					Cell R23cell5 = row.createCell(7);
					if (record.getR23_acc_amort_amt() != null) {
						R23cell5.setCellValue(record.getR23_acc_amort_amt().doubleValue());
						R23cell5.setCellStyle(numberStyle);
					} else {
						R23cell5.setCellValue("");
						R23cell5.setCellStyle(textStyle);
					}

					// ==================== R24 ====================

					row = sheet.getRow(22);
					// R24 Col B
					Cell R24cell1 = row.createCell(3);
					if (record.getR24_cost_rev() != null) {
						R24cell1.setCellValue(record.getR24_cost_rev().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					// R24 Col C
					Cell R24cell2 = row.createCell(4);
					if (record.getR24_useful_life() != null) {
						R24cell2.setCellValue(record.getR24_useful_life().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					// R24 Col D
					Cell R24cell3 = row.createCell(5);
					if (record.getR24_res_value() != null) {
						R24cell3.setCellValue(record.getR24_res_value().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// R24 Col E
					Cell R24cell4 = row.createCell(6);
					if (record.getR24_month_amort() != null) {
						R24cell4.setCellValue(record.getR24_month_amort().doubleValue());
						R24cell4.setCellStyle(numberStyle);
					} else {
						R24cell4.setCellValue("");
						R24cell4.setCellStyle(textStyle);
					}
					// R24 Col F
					Cell R24cell5 = row.createCell(7);
					if (record.getR24_acc_amort_amt() != null) {
						R24cell5.setCellValue(record.getR24_acc_amort_amt().doubleValue());
						R24cell5.setCellStyle(numberStyle);
					} else {
						R24cell5.setCellValue("");
						R24cell5.setCellStyle(textStyle);
					}

					// ==================== R25 ====================
					row = sheet.getRow(23);
					// R25 Col B
					Cell R25cell1 = row.createCell(3);
					if (record.getR25_cost_rev() != null) {
						R25cell1.setCellValue(record.getR25_cost_rev().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col C
					Cell R25cell2 = row.createCell(4);
					if (record.getR25_useful_life() != null) {
						R25cell2.setCellValue(record.getR25_useful_life().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col D
					Cell R25cell3 = row.createCell(5);
					if (record.getR25_res_value() != null) {
						R25cell3.setCellValue(record.getR25_res_value().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// R25 Col E
					Cell R25cell4 = row.createCell(6);
					if (record.getR25_month_amort() != null) {
						R25cell4.setCellValue(record.getR25_month_amort().doubleValue());
						R25cell4.setCellStyle(numberStyle);
					} else {
						R25cell4.setCellValue("");
						R25cell4.setCellStyle(textStyle);
					}
					// R25 Col F
					Cell R25cell5 = row.createCell(7);
					if (record.getR25_acc_amort_amt() != null) {
						R25cell5.setCellValue(record.getR25_acc_amort_amt().doubleValue());
						R25cell5.setCellStyle(numberStyle);
					} else {
						R25cell5.setCellValue("");
						R25cell5.setCellStyle(textStyle);
					}

					// ==================== R26 ====================
					row = sheet.getRow(24);
					// R26 Col B
					Cell R26cell1 = row.createCell(3);
					if (record.getR26_cost_rev() != null) {
						R26cell1.setCellValue(record.getR26_cost_rev().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col C
					Cell R26cell2 = row.createCell(4);
					if (record.getR26_useful_life() != null) {
						R26cell2.setCellValue(record.getR26_useful_life().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col D
					Cell R26cell3 = row.createCell(5);
					if (record.getR26_res_value() != null) {
						R26cell3.setCellValue(record.getR26_res_value().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// R26 Col E
					Cell R26cell4 = row.createCell(6);
					if (record.getR26_month_amort() != null) {
						R26cell4.setCellValue(record.getR26_month_amort().doubleValue());
						R26cell4.setCellStyle(numberStyle);
					} else {
						R26cell4.setCellValue("");
						R26cell4.setCellStyle(textStyle);
					}
					// R26 Col F
					Cell R26cell5 = row.createCell(7);
					if (record.getR26_acc_amort_amt() != null) {
						R26cell5.setCellValue(record.getR26_acc_amort_amt().doubleValue());
						R26cell5.setCellStyle(numberStyle);
					} else {
						R26cell5.setCellValue("");
						R26cell5.setCellStyle(textStyle);
					}

					// ==================== R27 ====================
					row = sheet.getRow(25);
					// R27 Col B
					Cell R27cell1 = row.createCell(3);
					if (record.getR27_cost_rev() != null) {
						R27cell1.setCellValue(record.getR27_cost_rev().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col C
					Cell R27cell2 = row.createCell(4);
					if (record.getR27_useful_life() != null) {
						R27cell2.setCellValue(record.getR27_useful_life().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col D
					Cell R27cell3 = row.createCell(5);
					if (record.getR27_res_value() != null) {
						R27cell3.setCellValue(record.getR27_res_value().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// R27 Col E
					Cell R27cell4 = row.createCell(6);
					if (record.getR27_month_amort() != null) {
						R27cell4.setCellValue(record.getR27_month_amort().doubleValue());
						R27cell4.setCellStyle(numberStyle);
					} else {
						R27cell4.setCellValue("");
						R27cell4.setCellStyle(textStyle);
					}
					// R27 Col F
					Cell R27cell5 = row.createCell(7);
					if (record.getR27_acc_amort_amt() != null) {
						R27cell5.setCellValue(record.getR27_acc_amort_amt().doubleValue());
						R27cell5.setCellStyle(numberStyle);
					} else {
						R27cell5.setCellValue("");
						R27cell5.setCellStyle(textStyle);
					}

					// ==================== R28 ====================
					row = sheet.getRow(26);
					// R28 Col B
					Cell R28cell1 = row.createCell(3);
					if (record.getR28_cost_rev() != null) {
						R28cell1.setCellValue(record.getR28_cost_rev().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// R28 Col C
					Cell R28cell2 = row.createCell(4);
					if (record.getR28_useful_life() != null) {
						R28cell2.setCellValue(record.getR28_useful_life().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// R28 Col D
					Cell R28cell3 = row.createCell(5);
					if (record.getR28_res_value() != null) {
						R28cell3.setCellValue(record.getR28_res_value().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}

					// R28 Col E
					Cell R28cell4 = row.createCell(6);
					if (record.getR28_month_amort() != null) {
						R28cell4.setCellValue(record.getR28_month_amort().doubleValue());
						R28cell4.setCellStyle(numberStyle);
					} else {
						R28cell4.setCellValue("");
						R28cell4.setCellStyle(textStyle);
					}
					// R28 Col F
					Cell R28cell5 = row.createCell(7);
					if (record.getR28_acc_amort_amt() != null) {
						R28cell5.setCellValue(record.getR28_acc_amort_amt().doubleValue());
						R28cell5.setCellStyle(numberStyle);
					} else {
						R28cell5.setCellValue("");
						R28cell5.setCellStyle(textStyle);

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_FAS EMAIL ARCHIVAL SUMMARY", null,
						"M_FAS_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

//	// Resub Format excel
//	public byte[] BRRS_M_FASResubExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");
//
//		if ("email".equalsIgnoreCase(format) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_FASResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
//		}
//
//		List<M_FAS_Resub_Summary_Entity> dataList = brrs_M_FAS_resub_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for M_FAS report. Returning empty result.");
//			return new byte[0];
//		}
//
//		String templateDir = env.getProperty("output.exportpathtemp");
//		String templateFileName = filename;
//		System.out.println(filename);
//		Path templatePath = Paths.get(templateDir, templateFileName);
//		System.out.println(templatePath);
//
//		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
//
//		if (!Files.exists(templatePath)) {
//			// This specific exception will be caught by the controller.
//			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
//		}
//		if (!Files.isReadable(templatePath)) {
//			// A specific exception for permission errors.
//			throw new SecurityException(
//					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
//		}
//
//		// This try-with-resources block is perfect. It guarantees all resources are
//		// closed automatically.
//		try (InputStream templateInputStream = Files.newInputStream(templatePath);
//				Workbook workbook = WorkbookFactory.create(templateInputStream);
//				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//
//			Sheet sheet = workbook.getSheetAt(0);
//
//			// --- Style Definitions ---
//			CreationHelper createHelper = workbook.getCreationHelper();
//
//			CellStyle dateStyle = workbook.createCellStyle();
//			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//			dateStyle.setBorderBottom(BorderStyle.THIN);
//			dateStyle.setBorderTop(BorderStyle.THIN);
//			dateStyle.setBorderLeft(BorderStyle.THIN);
//			dateStyle.setBorderRight(BorderStyle.THIN);
//
//			CellStyle textStyle = workbook.createCellStyle();
//			textStyle.setBorderBottom(BorderStyle.THIN);
//			textStyle.setBorderTop(BorderStyle.THIN);
//			textStyle.setBorderLeft(BorderStyle.THIN);
//			textStyle.setBorderRight(BorderStyle.THIN);
//
//			// Create the font
//			Font font = workbook.createFont();
//			font.setFontHeightInPoints((short) 8); // size 8
//			font.setFontName("Arial");
//
//			CellStyle numberStyle = workbook.createCellStyle();
//			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
//			numberStyle.setBorderBottom(BorderStyle.THIN);
//			numberStyle.setBorderTop(BorderStyle.THIN);
//			numberStyle.setBorderLeft(BorderStyle.THIN);
//			numberStyle.setBorderRight(BorderStyle.THIN);
//			numberStyle.setFont(font);
//			// --- End of Style Definitions ---
//
//			int startRow = 9;
//
//			if (!dataList.isEmpty()) {
//				for (int i = 0; i < dataList.size(); i++) {
//
//					M_FAS_Resub_Summary_Entity record = dataList.get(i);
//					System.out.println("rownumber=" + startRow + i);
//					System.out.println("rownumber=" + startRow + i);
//					Row row = sheet.getRow(startRow + i);
//					if (row == null) {
//						row = sheet.createRow(startRow + i);
//					}
////NORMAL
//					
//                    // R10 Col B
//                    Cell R10cell1 = row.createCell(1);
//                    if (record.getR10_cost() != null) {
//                        R10cell1.setCellValue(record.getR10_cost().doubleValue());
//                        R10cell1.setCellStyle(numberStyle);
//                    } else {
//                        R10cell1.setCellValue("");
//                        R10cell1.setCellStyle(textStyle);
//                    }
//
//                    // R10 Col C
//                    Cell R10cell2 = row.createCell(2);
//                    if (record.getR10_add() != null) {
//                        R10cell2.setCellValue(record.getR10_add().doubleValue());
//                        R10cell2.setCellStyle(numberStyle);
//                    } else {
//                        R10cell2.setCellValue("");
//                        R10cell2.setCellStyle(textStyle);
//                    }
//
//                    // R10 Col D
//                    Cell R10cell3 = row.createCell(3);
//                    if (record.getR10_disposals() != null) {
//                        R10cell3.setCellValue(record.getR10_disposals().doubleValue());
//                        R10cell3.setCellStyle(numberStyle);
//                    } else {
//                        R10cell3.setCellValue("");
//                        R10cell3.setCellStyle(textStyle);
//                    }
//
//                    // R10 Col E
//                    Cell R10cell4 = row.createCell(4);
//                    if (record.getR10_depreciation() != null) {
//                        R10cell4.setCellValue(record.getR10_depreciation().doubleValue());
//                        R10cell4.setCellStyle(numberStyle);
//                    } else {
//                        R10cell4.setCellValue("");
//                        R10cell4.setCellStyle(textStyle);
//                    }
//                    // ==================== R11 ====================
//                    // R11 Col B
//                    row = sheet.getRow(10);
//                    Cell R11cell1 = row.createCell(1);
//                    if (record.getR11_cost() != null) {
//                        R11cell1.setCellValue(record.getR11_cost().doubleValue());
//                        R11cell1.setCellStyle(numberStyle);
//                    } else {
//                        R11cell1.setCellValue("");
//                        R11cell1.setCellStyle(textStyle);
//                    }
//
//                    // R11 Col C
//                    Cell R11cell2 = row.createCell(2);
//                    if (record.getR11_add() != null) {
//                        R11cell2.setCellValue(record.getR11_add().doubleValue());
//                        R11cell2.setCellStyle(numberStyle);
//                    } else {
//                        R11cell2.setCellValue("");
//                        R11cell2.setCellStyle(textStyle);
//                    }
//
//                    // R11 Col D
//                    Cell R11cell3 = row.createCell(3);
//                    if (record.getR11_disposals() != null) {
//                        R11cell3.setCellValue(record.getR11_disposals().doubleValue());
//                        R11cell3.setCellStyle(numberStyle);
//                    } else {
//                        R11cell3.setCellValue("");
//                        R11cell3.setCellStyle(textStyle);
//                    }
//
//                    // R11 Col E
//                    Cell R11cell4 = row.createCell(4);
//                    if (record.getR11_depreciation() != null) {
//                        R11cell4.setCellValue(record.getR11_depreciation().doubleValue());
//                        R11cell4.setCellStyle(numberStyle);
//                    } else {
//                        R11cell4.setCellValue("");
//                        R11cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R12 ====================
//                    row = sheet.getRow(11);
//                    Cell R12cell1 = row.createCell(1);
//                    if (record.getR12_cost() != null) {
//                        R12cell1.setCellValue(record.getR12_cost().doubleValue());
//                        R12cell1.setCellStyle(numberStyle);
//                    } else {
//                        R12cell1.setCellValue("");
//                        R12cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R12cell2 = row.createCell(2);
//                    if (record.getR12_add() != null) {
//                        R12cell2.setCellValue(record.getR12_add().doubleValue());
//                        R12cell2.setCellStyle(numberStyle);
//                    } else {
//                        R12cell2.setCellValue("");
//                        R12cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R12cell3 = row.createCell(3);
//                    if (record.getR12_disposals() != null) {
//                        R12cell3.setCellValue(record.getR12_disposals().doubleValue());
//                        R12cell3.setCellStyle(numberStyle);
//                    } else {
//                        R12cell3.setCellValue("");
//                        R12cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R12cell4 = row.createCell(4);
//                    if (record.getR12_depreciation() != null) {
//                        R12cell4.setCellValue(record.getR12_depreciation().doubleValue());
//                        R12cell4.setCellStyle(numberStyle);
//                    } else {
//                        R12cell4.setCellValue("");
//                        R12cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R13 ====================
//                    row = sheet.getRow(12);
//                    Cell R13cell1 = row.createCell(1);
//                    if (record.getR13_cost() != null) {
//                        R13cell1.setCellValue(record.getR13_cost().doubleValue());
//                        R13cell1.setCellStyle(numberStyle);
//                    } else {
//                        R13cell1.setCellValue("");
//                        R13cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R13cell2 = row.createCell(2);
//                    if (record.getR13_add() != null) {
//                        R13cell2.setCellValue(record.getR13_add().doubleValue());
//                        R13cell2.setCellStyle(numberStyle);
//                    } else {
//                        R13cell2.setCellValue("");
//                        R13cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R13cell3 = row.createCell(3);
//                    if (record.getR13_disposals() != null) {
//                        R13cell3.setCellValue(record.getR13_disposals().doubleValue());
//                        R13cell3.setCellStyle(numberStyle);
//                    } else {
//                        R13cell3.setCellValue("");
//                        R13cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R13cell4 = row.createCell(4);
//                    if (record.getR13_depreciation() != null) {
//                        R13cell4.setCellValue(record.getR13_depreciation().doubleValue());
//                        R13cell4.setCellStyle(numberStyle);
//                    } else {
//                        R13cell4.setCellValue("");
//                        R13cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R14 ====================
//                    row = sheet.getRow(13);
//                    Cell R14cell1 = row.createCell(1);
//                    if (record.getR14_cost() != null) {
//                        R14cell1.setCellValue(record.getR14_cost().doubleValue());
//                        R14cell1.setCellStyle(numberStyle);
//                    } else {
//                        R14cell1.setCellValue("");
//                        R14cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R14cell2 = row.createCell(2);
//                    if (record.getR14_add() != null) {
//                        R14cell2.setCellValue(record.getR14_add().doubleValue());
//                        R14cell2.setCellStyle(numberStyle);
//                    } else {
//                        R14cell2.setCellValue("");
//                        R14cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R14cell3 = row.createCell(3);
//                    if (record.getR14_disposals() != null) {
//                        R14cell3.setCellValue(record.getR14_disposals().doubleValue());
//                        R14cell3.setCellStyle(numberStyle);
//                    } else {
//                        R14cell3.setCellValue("");
//                        R14cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R14cell4 = row.createCell(4);
//                    if (record.getR14_depreciation() != null) {
//                        R14cell4.setCellValue(record.getR14_depreciation().doubleValue());
//                        R14cell4.setCellStyle(numberStyle);
//                    } else {
//                        R14cell4.setCellValue("");
//                        R14cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R15 ====================
//                    row = sheet.getRow(14);
//                    Cell R15cell1 = row.createCell(1);
//                    if (record.getR15_cost() != null) {
//                        R15cell1.setCellValue(record.getR15_cost().doubleValue());
//                        R15cell1.setCellStyle(numberStyle);
//                    } else {
//                        R15cell1.setCellValue("");
//                        R15cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R15cell2 = row.createCell(2);
//                    if (record.getR15_add() != null) {
//                        R15cell2.setCellValue(record.getR15_add().doubleValue());
//                        R15cell2.setCellStyle(numberStyle);
//                    } else {
//                        R15cell2.setCellValue("");
//                        R15cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R15cell3 = row.createCell(3);
//                    if (record.getR15_disposals() != null) {
//                        R15cell3.setCellValue(record.getR15_disposals().doubleValue());
//                        R15cell3.setCellStyle(numberStyle);
//                    } else {
//                        R15cell3.setCellValue("");
//                        R15cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R15cell4 = row.createCell(4);
//                    if (record.getR15_depreciation() != null) {
//                        R15cell4.setCellValue(record.getR15_depreciation().doubleValue());
//                        R15cell4.setCellStyle(numberStyle);
//                    } else {
//                        R15cell4.setCellValue("");
//                        R15cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R16 ====================
//                    row = sheet.getRow(15);
//                    Cell R16cell1 = row.createCell(1);
//                    if (record.getR16_cost() != null) {
//                        R16cell1.setCellValue(record.getR16_cost().doubleValue());
//                        R16cell1.setCellStyle(numberStyle);
//                    } else {
//                        R16cell1.setCellValue("");
//                        R16cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R16cell2 = row.createCell(2);
//                    if (record.getR16_add() != null) {
//                        R16cell2.setCellValue(record.getR16_add().doubleValue());
//                        R16cell2.setCellStyle(numberStyle);
//                    } else {
//                        R16cell2.setCellValue("");
//                        R16cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R16cell3 = row.createCell(3);
//                    if (record.getR16_disposals() != null) {
//                        R16cell3.setCellValue(record.getR16_disposals().doubleValue());
//                        R16cell3.setCellStyle(numberStyle);
//                    } else {
//                        R16cell3.setCellValue("");
//                        R16cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R16cell4 = row.createCell(4);
//                    if (record.getR16_depreciation() != null) {
//                        R16cell4.setCellValue(record.getR16_depreciation().doubleValue());
//                        R16cell4.setCellStyle(numberStyle);
//                    } else {
//                        R16cell4.setCellValue("");
//                        R16cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R23 ====================
//                    row = sheet.getRow(22);
//                    // R23 Col B
//                    Cell R23cell1 = row.createCell(1);
//                    if (record.getR23_cost_rev() != null) {
//                        R23cell1.setCellValue(record.getR23_cost_rev().doubleValue());
//                        R23cell1.setCellStyle(numberStyle);
//                    } else {
//                        R23cell1.setCellValue("");
//                        R23cell1.setCellStyle(textStyle);
//                    }
//
//                    // R23 Col C
//                    Cell R23cell2 = row.createCell(2);
//                    if (record.getR23_useful_life() != null) {
//                        R23cell2.setCellValue(record.getR23_useful_life().doubleValue());
//                        R23cell2.setCellStyle(numberStyle);
//                    } else {
//                        R23cell2.setCellValue("");
//                        R23cell2.setCellStyle(textStyle);
//                    }
//
//                    // R23 Col D
//                    Cell R23cell3 = row.createCell(3);
//                    if (record.getR23_res_value() != null) {
//                        R23cell3.setCellValue(record.getR23_res_value().doubleValue());
//                        R23cell3.setCellStyle(numberStyle);
//                    } else {
//                        R23cell3.setCellValue("");
//                        R23cell3.setCellStyle(textStyle);
//                    }
//
//                    // R23 Col E
//                    Cell R23cell4 = row.createCell(4);
//                    if (record.getR23_month_amort() != null) {
//                        R23cell4.setCellValue(record.getR23_month_amort().doubleValue());
//                        R23cell4.setCellStyle(numberStyle);
//                    } else {
//                        R23cell4.setCellValue("");
//                        R23cell4.setCellStyle(textStyle);
//                    }
//                    // R23 Col F
//                    Cell R23cell5 = row.createCell(5);
//                    if (record.getR23_acc_amort_amt() != null) {
//                        R23cell5.setCellValue(record.getR23_acc_amort_amt().doubleValue());
//                        R23cell5.setCellStyle(numberStyle);
//                    } else {
//                        R23cell5.setCellValue("");
//                        R23cell5.setCellStyle(textStyle);
//                    }
//                    // R23 Col F
//                    Cell R23cell6 = row.createCell(6);
//                    if (record.getR23_close_bal() != null) {
//                        R23cell6.setCellValue(record.getR23_close_bal().doubleValue());
//                        R23cell6.setCellStyle(numberStyle);
//                    } else {
//                        R23cell6.setCellValue("");
//                        R23cell6.setCellStyle(textStyle);
//                    }
//                    // ==================== R24 ====================
//
//                    row = sheet.getRow(23);
//                    // R24 Col B
//                    Cell R24cell1 = row.createCell(1);
//                    if (record.getR24_cost_rev() != null) {
//                        R24cell1.setCellValue(record.getR24_cost_rev().doubleValue());
//                        R24cell1.setCellStyle(numberStyle);
//                    } else {
//                        R24cell1.setCellValue("");
//                        R24cell1.setCellStyle(textStyle);
//                    }
//
//                    // R24 Col C
//                    Cell R24cell2 = row.createCell(2);
//                    if (record.getR24_useful_life() != null) {
//                        R24cell2.setCellValue(record.getR24_useful_life().doubleValue());
//                        R24cell2.setCellStyle(numberStyle);
//                    } else {
//                        R24cell2.setCellValue("");
//                        R24cell2.setCellStyle(textStyle);
//                    }
//
//                    // R24 Col D
//                    Cell R24cell3 = row.createCell(3);
//                    if (record.getR24_res_value() != null) {
//                        R24cell3.setCellValue(record.getR24_res_value().doubleValue());
//                        R24cell3.setCellStyle(numberStyle);
//                    } else {
//                        R24cell3.setCellValue("");
//                        R24cell3.setCellStyle(textStyle);
//                    }
//
//                    // R24 Col E
//                    Cell R24cell4 = row.createCell(4);
//                    if (record.getR24_month_amort() != null) {
//                        R24cell4.setCellValue(record.getR24_month_amort().doubleValue());
//                        R24cell4.setCellStyle(numberStyle);
//                    } else {
//                        R24cell4.setCellValue("");
//                        R24cell4.setCellStyle(textStyle);
//                    }
//                    // R24 Col F
//                    Cell R24cell5 = row.createCell(5);
//                    if (record.getR24_acc_amort_amt() != null) {
//                        R24cell5.setCellValue(record.getR24_acc_amort_amt().doubleValue());
//                        R24cell5.setCellStyle(numberStyle);
//                    } else {
//                        R24cell5.setCellValue("");
//                        R24cell5.setCellStyle(textStyle);
//                    }
//                    // R24 Col F
//                    Cell R24cell6 = row.createCell(6);
//                    if (record.getR24_close_bal() != null) {
//                        R24cell6.setCellValue(record.getR24_close_bal().doubleValue());
//                        R24cell6.setCellStyle(numberStyle);
//                    } else {
//                        R24cell6.setCellValue("");
//                        R24cell6.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R25 ====================
//                    row = sheet.getRow(24);
//                    // R25 Col B
//                    Cell R25cell1 = row.createCell(1);
//                    if (record.getR25_cost_rev() != null) {
//                        R25cell1.setCellValue(record.getR25_cost_rev().doubleValue());
//                        R25cell1.setCellStyle(numberStyle);
//                    } else {
//                        R25cell1.setCellValue("");
//                        R25cell1.setCellStyle(textStyle);
//                    }
//
//                    // R25 Col C
//                    Cell R25cell2 = row.createCell(2);
//                    if (record.getR25_useful_life() != null) {
//                        R25cell2.setCellValue(record.getR25_useful_life().doubleValue());
//                        R25cell2.setCellStyle(numberStyle);
//                    } else {
//                        R25cell2.setCellValue("");
//                        R25cell2.setCellStyle(textStyle);
//                    }
//
//                    // R25 Col D
//                    Cell R25cell3 = row.createCell(3);
//                    if (record.getR25_res_value() != null) {
//                        R25cell3.setCellValue(record.getR25_res_value().doubleValue());
//                        R25cell3.setCellStyle(numberStyle);
//                    } else {
//                        R25cell3.setCellValue("");
//                        R25cell3.setCellStyle(textStyle);
//                    }
//
//                    // R25 Col E
//                    Cell R25cell4 = row.createCell(4);
//                    if (record.getR25_month_amort() != null) {
//                        R25cell4.setCellValue(record.getR25_month_amort().doubleValue());
//                        R25cell4.setCellStyle(numberStyle);
//                    } else {
//                        R25cell4.setCellValue("");
//                        R25cell4.setCellStyle(textStyle);
//                    }
//                    // R25 Col F
//                    Cell R25cell5 = row.createCell(5);
//                    if (record.getR25_acc_amort_amt() != null) {
//                        R25cell5.setCellValue(record.getR25_acc_amort_amt().doubleValue());
//                        R25cell5.setCellStyle(numberStyle);
//                    } else {
//                        R25cell5.setCellValue("");
//                        R25cell5.setCellStyle(textStyle);
//                    }
//                    // R25 Col F
//                    Cell R25cell6 = row.createCell(6);
//                    if (record.getR25_close_bal() != null) {
//                        R25cell6.setCellValue(record.getR25_close_bal().doubleValue());
//                        R25cell6.setCellStyle(numberStyle);
//                    } else {
//                        R25cell6.setCellValue("");
//                        R25cell6.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R26 ====================
//                    row = sheet.getRow(25);
//                    // R26 Col B
//                    Cell R26cell1 = row.createCell(1);
//                    if (record.getR26_cost_rev() != null) {
//                        R26cell1.setCellValue(record.getR26_cost_rev().doubleValue());
//                        R26cell1.setCellStyle(numberStyle);
//                    } else {
//                        R26cell1.setCellValue("");
//                        R26cell1.setCellStyle(textStyle);
//                    }
//
//                    // R26 Col C
//                    Cell R26cell2 = row.createCell(2);
//                    if (record.getR26_useful_life() != null) {
//                        R26cell2.setCellValue(record.getR26_useful_life().doubleValue());
//                        R26cell2.setCellStyle(numberStyle);
//                    } else {
//                        R26cell2.setCellValue("");
//                        R26cell2.setCellStyle(textStyle);
//                    }
//
//                    // R26 Col D
//                    Cell R26cell3 = row.createCell(3);
//                    if (record.getR26_res_value() != null) {
//                        R26cell3.setCellValue(record.getR26_res_value().doubleValue());
//                        R26cell3.setCellStyle(numberStyle);
//                    } else {
//                        R26cell3.setCellValue("");
//                        R26cell3.setCellStyle(textStyle);
//                    }
//
//                    // R26 Col E
//                    Cell R26cell4 = row.createCell(4);
//                    if (record.getR26_month_amort() != null) {
//                        R26cell4.setCellValue(record.getR26_month_amort().doubleValue());
//                        R26cell4.setCellStyle(numberStyle);
//                    } else {
//                        R26cell4.setCellValue("");
//                        R26cell4.setCellStyle(textStyle);
//                    }
//                    // R26 Col F
//                    Cell R26cell5 = row.createCell(5);
//                    if (record.getR26_acc_amort_amt() != null) {
//                        R26cell5.setCellValue(record.getR26_acc_amort_amt().doubleValue());
//                        R26cell5.setCellStyle(numberStyle);
//                    } else {
//                        R26cell5.setCellValue("");
//                        R26cell5.setCellStyle(textStyle);
//                    }
//                    // R26 Col F
//                    Cell R26cell6 = row.createCell(6);
//                    if (record.getR26_close_bal() != null) {
//                        R26cell6.setCellValue(record.getR26_close_bal().doubleValue());
//                        R26cell6.setCellStyle(numberStyle);
//                    } else {
//                        R26cell6.setCellValue("");
//                        R26cell6.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R27 ====================
//                    row = sheet.getRow(26);
//                    // R27 Col B
//                    Cell R27cell1 = row.createCell(1);
//                    if (record.getR27_cost_rev() != null) {
//                        R27cell1.setCellValue(record.getR27_cost_rev().doubleValue());
//                        R27cell1.setCellStyle(numberStyle);
//                    } else {
//                        R27cell1.setCellValue("");
//                        R27cell1.setCellStyle(textStyle);
//                    }
//
//                    // R27 Col C
//                    Cell R27cell2 = row.createCell(2);
//                    if (record.getR27_useful_life() != null) {
//                        R27cell2.setCellValue(record.getR27_useful_life().doubleValue());
//                        R27cell2.setCellStyle(numberStyle);
//                    } else {
//                        R27cell2.setCellValue("");
//                        R27cell2.setCellStyle(textStyle);
//                    }
//
//                    // R27 Col D
//                    Cell R27cell3 = row.createCell(3);
//                    if (record.getR27_res_value() != null) {
//                        R27cell3.setCellValue(record.getR27_res_value().doubleValue());
//                        R27cell3.setCellStyle(numberStyle);
//                    } else {
//                        R27cell3.setCellValue("");
//                        R27cell3.setCellStyle(textStyle);
//                    }
//
//                    // R27 Col E
//                    Cell R27cell4 = row.createCell(4);
//                    if (record.getR27_month_amort() != null) {
//                        R27cell4.setCellValue(record.getR27_month_amort().doubleValue());
//                        R27cell4.setCellStyle(numberStyle);
//                    } else {
//                        R27cell4.setCellValue("");
//                        R27cell4.setCellStyle(textStyle);
//                    }
//                    // R27 Col F
//                    Cell R27cell5 = row.createCell(5);
//                    if (record.getR27_acc_amort_amt() != null) {
//                        R27cell5.setCellValue(record.getR27_acc_amort_amt().doubleValue());
//                        R27cell5.setCellStyle(numberStyle);
//                    } else {
//                        R27cell5.setCellValue("");
//                        R27cell5.setCellStyle(textStyle);
//                    }
//                    // R27 Col F
//                    Cell R27cell6 = row.createCell(6);
//                    if (record.getR27_close_bal() != null) {
//                        R27cell6.setCellValue(record.getR27_close_bal().doubleValue());
//                        R27cell6.setCellStyle(numberStyle);
//                    } else {
//                        R27cell6.setCellValue("");
//                        R27cell6.setCellStyle(textStyle);
//                    }
//
//
//
//
//
//				}
//				workbook.setForceFormulaRecalculation(true);
//			} else {
//
//			}
//
//			// Write the final workbook content to the in-memory stream.
//			workbook.write(out);
//
//			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
//
//			return out.toByteArray();
//		}
//
//	}
//
//	// Resub Email Excel
//	public byte[] BRRS_M_FASResubEmailExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Archival Email Excel generation process in memory.");
//
//		List<M_FAS_Resub_Summary_Entity> dataList = brrs_M_FAS_resub_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for BRRS_M_FAS report. Returning empty result.");
//			return new byte[0];
//		}
//
//		String templateDir = env.getProperty("output.exportpathtemp");
//		String templateFileName = filename;
//		System.out.println(filename);
//		Path templatePath = Paths.get(templateDir, templateFileName);
//		System.out.println(templatePath);
//
//		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
//
//		if (!Files.exists(templatePath)) {
//			// This specific exception will be caught by the controller.
//			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
//		}
//		if (!Files.isReadable(templatePath)) {
//			// A specific exception for permission errors.
//			throw new SecurityException(
//					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
//		}
//
//		// This try-with-resources block is perfect. It guarantees all resources are
//		// closed automatically.
//		try (InputStream templateInputStream = Files.newInputStream(templatePath);
//				Workbook workbook = WorkbookFactory.create(templateInputStream);
//				ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//
//			Sheet sheet = workbook.getSheetAt(0);
//
//			// --- Style Definitions ---
//			CreationHelper createHelper = workbook.getCreationHelper();
//
//			CellStyle dateStyle = workbook.createCellStyle();
//			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//			dateStyle.setBorderBottom(BorderStyle.THIN);
//			dateStyle.setBorderTop(BorderStyle.THIN);
//			dateStyle.setBorderLeft(BorderStyle.THIN);
//			dateStyle.setBorderRight(BorderStyle.THIN);
//
//			CellStyle textStyle = workbook.createCellStyle();
//			textStyle.setBorderBottom(BorderStyle.THIN);
//			textStyle.setBorderTop(BorderStyle.THIN);
//			textStyle.setBorderLeft(BorderStyle.THIN);
//			textStyle.setBorderRight(BorderStyle.THIN);
//
//			// Create the font
//			Font font = workbook.createFont();
//			font.setFontHeightInPoints((short) 8); // size 8
//			font.setFontName("Arial");
//
//			CellStyle numberStyle = workbook.createCellStyle();
//			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
//			numberStyle.setBorderBottom(BorderStyle.THIN);
//			numberStyle.setBorderTop(BorderStyle.THIN);
//			numberStyle.setBorderLeft(BorderStyle.THIN);
//			numberStyle.setBorderRight(BorderStyle.THIN);
//			numberStyle.setFont(font);
//			// --- End of Style Definitions ---
//
//			int startRow = 9;
//
//			if (!dataList.isEmpty()) {
//				for (int i = 0; i < dataList.size(); i++) {
//					M_FAS_Resub_Summary_Entity record = dataList.get(i);
//					System.out.println("rownumber=" + startRow + i);
//					Row row = sheet.getRow(startRow + i);
//					if (row == null) {
//						row = sheet.createRow(startRow + i);
//					}
////EMAIL
//
// R10 Col B
//                    Cell R10cell1 = row.createCell(3);
//                    if (record.getR10_cost() != null) {
//                        R10cell1.setCellValue(record.getR10_cost().doubleValue());
//                        R10cell1.setCellStyle(numberStyle);
//                    } else {
//                        R10cell1.setCellValue("");
//                        R10cell1.setCellStyle(textStyle);
//                    }
//
//                    // R10 Col C
//                    Cell R10cell2 = row.createCell(4);
//                    if (record.getR10_add() != null) {
//                        R10cell2.setCellValue(record.getR10_add().doubleValue());
//                        R10cell2.setCellStyle(numberStyle);
//                    } else {
//                        R10cell2.setCellValue("");
//                        R10cell2.setCellStyle(textStyle);
//                    }
//
//                    // R10 Col D
//                    Cell R10cell3 = row.createCell(5);
//                    if (record.getR10_disposals() != null) {
//                        R10cell3.setCellValue(record.getR10_disposals().doubleValue());
//                        R10cell3.setCellStyle(numberStyle);
//                    } else {
//                        R10cell3.setCellValue("");
//                        R10cell3.setCellStyle(textStyle);
//                    }
//
//                    // R10 Col E
//                    Cell R10cell4 = row.createCell(6);
//                    if (record.getR10_depreciation() != null) {
//                        R10cell4.setCellValue(record.getR10_depreciation().doubleValue());
//                        R10cell4.setCellStyle(numberStyle);
//                    } else {
//                        R10cell4.setCellValue("");
//                        R10cell4.setCellStyle(textStyle);
//                    }
//                    // ==================== R11 ====================
//                    // R11 Col B
//                    row = sheet.getRow(10);
//                    Cell R11cell1 = row.createCell(3);
//                    if (record.getR11_cost() != null) {
//                        R11cell1.setCellValue(record.getR11_cost().doubleValue());
//                        R11cell1.setCellStyle(numberStyle);
//                    } else {
//                        R11cell1.setCellValue("");
//                        R11cell1.setCellStyle(textStyle);
//                    }
//
//                    // R11 Col C
//                    Cell R11cell2 = row.createCell(4);
//                    if (record.getR11_add() != null) {
//                        R11cell2.setCellValue(record.getR11_add().doubleValue());
//                        R11cell2.setCellStyle(numberStyle);
//                    } else {
//                        R11cell2.setCellValue("");
//                        R11cell2.setCellStyle(textStyle);
//                    }
//
//                    // R11 Col D
//                    Cell R11cell3 = row.createCell(5);
//                    if (record.getR11_disposals() != null) {
//                        R11cell3.setCellValue(record.getR11_disposals().doubleValue());
//                        R11cell3.setCellStyle(numberStyle);
//                    } else {
//                        R11cell3.setCellValue("");
//                        R11cell3.setCellStyle(textStyle);
//                    }
//
//                    // R11 Col E
//                    Cell R11cell4 = row.createCell(6);
//                    if (record.getR11_depreciation() != null) {
//                        R11cell4.setCellValue(record.getR11_depreciation().doubleValue());
//                        R11cell4.setCellStyle(numberStyle);
//                    } else {
//                        R11cell4.setCellValue("");
//                        R11cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R12 ====================
//                    row = sheet.getRow(11);
//                    Cell R12cell1 = row.createCell(3);
//                    if (record.getR12_cost() != null) {
//                        R12cell1.setCellValue(record.getR12_cost().doubleValue());
//                        R12cell1.setCellStyle(numberStyle);
//                    } else {
//                        R12cell1.setCellValue("");
//                        R12cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R12cell2 = row.createCell(4);
//                    if (record.getR12_add() != null) {
//                        R12cell2.setCellValue(record.getR12_add().doubleValue());
//                        R12cell2.setCellStyle(numberStyle);
//                    } else {
//                        R12cell2.setCellValue("");
//                        R12cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R12cell3 = row.createCell(5);
//                    if (record.getR12_disposals() != null) {
//                        R12cell3.setCellValue(record.getR12_disposals().doubleValue());
//                        R12cell3.setCellStyle(numberStyle);
//                    } else {
//                        R12cell3.setCellValue("");
//                        R12cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R12cell4 = row.createCell(6);
//                    if (record.getR12_depreciation() != null) {
//                        R12cell4.setCellValue(record.getR12_depreciation().doubleValue());
//                        R12cell4.setCellStyle(numberStyle);
//                    } else {
//                        R12cell4.setCellValue("");
//                        R12cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R13 ====================
//                    row = sheet.getRow(12);
//                    Cell R13cell1 = row.createCell(3);
//                    if (record.getR13_cost() != null) {
//                        R13cell1.setCellValue(record.getR13_cost().doubleValue());
//                        R13cell1.setCellStyle(numberStyle);
//                    } else {
//                        R13cell1.setCellValue("");
//                        R13cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R13cell2 = row.createCell(4);
//                    if (record.getR13_add() != null) {
//                        R13cell2.setCellValue(record.getR13_add().doubleValue());
//                        R13cell2.setCellStyle(numberStyle);
//                    } else {
//                        R13cell2.setCellValue("");
//                        R13cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R13cell3 = row.createCell(5);
//                    if (record.getR13_disposals() != null) {
//                        R13cell3.setCellValue(record.getR13_disposals().doubleValue());
//                        R13cell3.setCellStyle(numberStyle);
//                    } else {
//                        R13cell3.setCellValue("");
//                        R13cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R13cell4 = row.createCell(6);
//                    if (record.getR13_depreciation() != null) {
//                        R13cell4.setCellValue(record.getR13_depreciation().doubleValue());
//                        R13cell4.setCellStyle(numberStyle);
//                    } else {
//                        R13cell4.setCellValue("");
//                        R13cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R14 ====================
//                    row = sheet.getRow(13);
//                    Cell R14cell1 = row.createCell(3);
//                    if (record.getR14_cost() != null) {
//                        R14cell1.setCellValue(record.getR14_cost().doubleValue());
//                        R14cell1.setCellStyle(numberStyle);
//                    } else {
//                        R14cell1.setCellValue("");
//                        R14cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R14cell2 = row.createCell(4);
//                    if (record.getR14_add() != null) {
//                        R14cell2.setCellValue(record.getR14_add().doubleValue());
//                        R14cell2.setCellStyle(numberStyle);
//                    } else {
//                        R14cell2.setCellValue("");
//                        R14cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R14cell3 = row.createCell(5);
//                    if (record.getR14_disposals() != null) {
//                        R14cell3.setCellValue(record.getR14_disposals().doubleValue());
//                        R14cell3.setCellStyle(numberStyle);
//                    } else {
//                        R14cell3.setCellValue("");
//                        R14cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R14cell4 = row.createCell(6);
//                    if (record.getR14_depreciation() != null) {
//                        R14cell4.setCellValue(record.getR14_depreciation().doubleValue());
//                        R14cell4.setCellStyle(numberStyle);
//                    } else {
//                        R14cell4.setCellValue("");
//                        R14cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R15 ====================
//                    row = sheet.getRow(14);
//                    Cell R15cell1 = row.createCell(3);
//                    if (record.getR15_cost() != null) {
//                        R15cell1.setCellValue(record.getR15_cost().doubleValue());
//                        R15cell1.setCellStyle(numberStyle);
//                    } else {
//                        R15cell1.setCellValue("");
//                        R15cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R15cell2 = row.createCell(4);
//                    if (record.getR15_add() != null) {
//                        R15cell2.setCellValue(record.getR15_add().doubleValue());
//                        R15cell2.setCellStyle(numberStyle);
//                    } else {
//                        R15cell2.setCellValue("");
//                        R15cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R15cell3 = row.createCell(5);
//                    if (record.getR15_disposals() != null) {
//                        R15cell3.setCellValue(record.getR15_disposals().doubleValue());
//                        R15cell3.setCellStyle(numberStyle);
//                    } else {
//                        R15cell3.setCellValue("");
//                        R15cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R15cell4 = row.createCell(6);
//                    if (record.getR15_depreciation() != null) {
//                        R15cell4.setCellValue(record.getR15_depreciation().doubleValue());
//                        R15cell4.setCellStyle(numberStyle);
//                    } else {
//                        R15cell4.setCellValue("");
//                        R15cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R16 ====================
//                    row = sheet.getRow(15);
//                    Cell R16cell1 = row.createCell(3);
//                    if (record.getR16_cost() != null) {
//                        R16cell1.setCellValue(record.getR16_cost().doubleValue());
//                        R16cell1.setCellStyle(numberStyle);
//                    } else {
//                        R16cell1.setCellValue("");
//                        R16cell1.setCellStyle(textStyle);
//                    }
//
//                    Cell R16cell2 = row.createCell(4);
//                    if (record.getR16_add() != null) {
//                        R16cell2.setCellValue(record.getR16_add().doubleValue());
//                        R16cell2.setCellStyle(numberStyle);
//                    } else {
//                        R16cell2.setCellValue("");
//                        R16cell2.setCellStyle(textStyle);
//                    }
//
//                    Cell R16cell3 = row.createCell(5);
//                    if (record.getR16_disposals() != null) {
//                        R16cell3.setCellValue(record.getR16_disposals().doubleValue());
//                        R16cell3.setCellStyle(numberStyle);
//                    } else {
//                        R16cell3.setCellValue("");
//                        R16cell3.setCellStyle(textStyle);
//                    }
//
//                    Cell R16cell4 = row.createCell(6);
//                    if (record.getR16_depreciation() != null) {
//                        R16cell4.setCellValue(record.getR16_depreciation().doubleValue());
//                        R16cell4.setCellStyle(numberStyle);
//                    } else {
//                        R16cell4.setCellValue("");
//                        R16cell4.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R23 ====================
//                    row = sheet.getRow(21);
//                    // R23 Col B
//                    Cell R23cell1 = row.createCell(3);
//                    if (record.getR23_cost_rev() != null) {
//                        R23cell1.setCellValue(record.getR23_cost_rev().doubleValue());
//                        R23cell1.setCellStyle(numberStyle);
//                    } else {
//                        R23cell1.setCellValue("");
//                        R23cell1.setCellStyle(textStyle);
//                    }
//
//                    // R23 Col C
//                    Cell R23cell2 = row.createCell(4);
//                    if (record.getR23_useful_life() != null) {
//                        R23cell2.setCellValue(record.getR23_useful_life().doubleValue());
//                        R23cell2.setCellStyle(numberStyle);
//                    } else {
//                        R23cell2.setCellValue("");
//                        R23cell2.setCellStyle(textStyle);
//                    }
//
//                    // R23 Col D
//                    Cell R23cell3 = row.createCell(5);
//                    if (record.getR23_res_value() != null) {
//                        R23cell3.setCellValue(record.getR23_res_value().doubleValue());
//                        R23cell3.setCellStyle(numberStyle);
//                    } else {
//                        R23cell3.setCellValue("");
//                        R23cell3.setCellStyle(textStyle);
//                    }
//
//                    // R23 Col E
//                    Cell R23cell4 = row.createCell(6);
//                    if (record.getR23_month_amort() != null) {
//                        R23cell4.setCellValue(record.getR23_month_amort().doubleValue());
//                        R23cell4.setCellStyle(numberStyle);
//                    } else {
//                        R23cell4.setCellValue("");
//                        R23cell4.setCellStyle(textStyle);
//                    }
//                    // R23 Col F
//                    Cell R23cell5 = row.createCell(5);
//                    if (record.getR23_acc_amort_amt() != null) {
//                        R23cell5.setCellValue(record.getR23_acc_amort_amt().doubleValue());
//                        R23cell5.setCellStyle(numberStyle);
//                    } else {
//                        R23cell5.setCellValue("");
//                        R23cell5.setCellStyle(textStyle);
//                    }
//                    // R23 Col F
//                    Cell R23cell6 = row.createCell(6);
//                    if (record.getR23_close_bal() != null) {
//                        R23cell6.setCellValue(record.getR23_close_bal().doubleValue());
//                        R23cell6.setCellStyle(numberStyle);
//                    } else {
//                        R23cell6.setCellValue("");
//                        R23cell6.setCellStyle(textStyle);
//                    }
//                    // ==================== R24 ====================
//
//                    row = sheet.getRow(22);
//                    // R24 Col B
//                    Cell R24cell1 = row.createCell(3);
//                    if (record.getR24_cost_rev() != null) {
//                        R24cell1.setCellValue(record.getR24_cost_rev().doubleValue());
//                        R24cell1.setCellStyle(numberStyle);
//                    } else {
//                        R24cell1.setCellValue("");
//                        R24cell1.setCellStyle(textStyle);
//                    }
//
//                    // R24 Col C
//                    Cell R24cell2 = row.createCell(4);
//                    if (record.getR24_useful_life() != null) {
//                        R24cell2.setCellValue(record.getR24_useful_life().doubleValue());
//                        R24cell2.setCellStyle(numberStyle);
//                    } else {
//                        R24cell2.setCellValue("");
//                        R24cell2.setCellStyle(textStyle);
//                    }
//
//                    // R24 Col D
//                    Cell R24cell3 = row.createCell(5);
//                    if (record.getR24_res_value() != null) {
//                        R24cell3.setCellValue(record.getR24_res_value().doubleValue());
//                        R24cell3.setCellStyle(numberStyle);
//                    } else {
//                        R24cell3.setCellValue("");
//                        R24cell3.setCellStyle(textStyle);
//                    }
//
//                    // R24 Col E
//                    Cell R24cell4 = row.createCell(6);
//                    if (record.getR24_month_amort() != null) {
//                        R24cell4.setCellValue(record.getR24_month_amort().doubleValue());
//                        R24cell4.setCellStyle(numberStyle);
//                    } else {
//                        R24cell4.setCellValue("");
//                        R24cell4.setCellStyle(textStyle);
//                    }
//                    // R24 Col F
//                    Cell R24cell5 = row.createCell(5);
//                    if (record.getR24_acc_amort_amt() != null) {
//                        R24cell5.setCellValue(record.getR24_acc_amort_amt().doubleValue());
//                        R24cell5.setCellStyle(numberStyle);
//                    } else {
//                        R24cell5.setCellValue("");
//                        R24cell5.setCellStyle(textStyle);
//                    }
//                    // R24 Col F
//                    Cell R24cell6 = row.createCell(6);
//                    if (record.getR24_close_bal() != null) {
//                        R24cell6.setCellValue(record.getR24_close_bal().doubleValue());
//                        R24cell6.setCellStyle(numberStyle);
//                    } else {
//                        R24cell6.setCellValue("");
//                        R24cell6.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R25 ====================
//                    row = sheet.getRow(23);
//                    // R25 Col B
//                    Cell R25cell1 = row.createCell(3);
//                    if (record.getR25_cost_rev() != null) {
//                        R25cell1.setCellValue(record.getR25_cost_rev().doubleValue());
//                        R25cell1.setCellStyle(numberStyle);
//                    } else {
//                        R25cell1.setCellValue("");
//                        R25cell1.setCellStyle(textStyle);
//                    }
//
//                    // R25 Col C
//                    Cell R25cell2 = row.createCell(4);
//                    if (record.getR25_useful_life() != null) {
//                        R25cell2.setCellValue(record.getR25_useful_life().doubleValue());
//                        R25cell2.setCellStyle(numberStyle);
//                    } else {
//                        R25cell2.setCellValue("");
//                        R25cell2.setCellStyle(textStyle);
//                    }
//
//                    // R25 Col D
//                    Cell R25cell3 = row.createCell(5);
//                    if (record.getR25_res_value() != null) {
//                        R25cell3.setCellValue(record.getR25_res_value().doubleValue());
//                        R25cell3.setCellStyle(numberStyle);
//                    } else {
//                        R25cell3.setCellValue("");
//                        R25cell3.setCellStyle(textStyle);
//                    }
//
//                    // R25 Col E
//                    Cell R25cell4 = row.createCell(6);
//                    if (record.getR25_month_amort() != null) {
//                        R25cell4.setCellValue(record.getR25_month_amort().doubleValue());
//                        R25cell4.setCellStyle(numberStyle);
//                    } else {
//                        R25cell4.setCellValue("");
//                        R25cell4.setCellStyle(textStyle);
//                    }
//                    // R25 Col F
//                    Cell R25cell5 = row.createCell(5);
//                    if (record.getR25_acc_amort_amt() != null) {
//                        R25cell5.setCellValue(record.getR25_acc_amort_amt().doubleValue());
//                        R25cell5.setCellStyle(numberStyle);
//                    } else {
//                        R25cell5.setCellValue("");
//                        R25cell5.setCellStyle(textStyle);
//                    }
//                    // R25 Col F
//                    Cell R25cell6 = row.createCell(6);
//                    if (record.getR25_close_bal() != null) {
//                        R25cell6.setCellValue(record.getR25_close_bal().doubleValue());
//                        R25cell6.setCellStyle(numberStyle);
//                    } else {
//                        R25cell6.setCellValue("");
//                        R25cell6.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R26 ====================
//                    row = sheet.getRow(24);
//                    // R26 Col B
//                    Cell R26cell1 = row.createCell(3);
//                    if (record.getR26_cost_rev() != null) {
//                        R26cell1.setCellValue(record.getR26_cost_rev().doubleValue());
//                        R26cell1.setCellStyle(numberStyle);
//                    } else {
//                        R26cell1.setCellValue("");
//                        R26cell1.setCellStyle(textStyle);
//                    }
//
//                    // R26 Col C
//                    Cell R26cell2 = row.createCell(4);
//                    if (record.getR26_useful_life() != null) {
//                        R26cell2.setCellValue(record.getR26_useful_life().doubleValue());
//                        R26cell2.setCellStyle(numberStyle);
//                    } else {
//                        R26cell2.setCellValue("");
//                        R26cell2.setCellStyle(textStyle);
//                    }
//
//                    // R26 Col D
//                    Cell R26cell3 = row.createCell(5);
//                    if (record.getR26_res_value() != null) {
//                        R26cell3.setCellValue(record.getR26_res_value().doubleValue());
//                        R26cell3.setCellStyle(numberStyle);
//                    } else {
//                        R26cell3.setCellValue("");
//                        R26cell3.setCellStyle(textStyle);
//                    }
//
//                    // R26 Col E
//                    Cell R26cell4 = row.createCell(6);
//                    if (record.getR26_month_amort() != null) {
//                        R26cell4.setCellValue(record.getR26_month_amort().doubleValue());
//                        R26cell4.setCellStyle(numberStyle);
//                    } else {
//                        R26cell4.setCellValue("");
//                        R26cell4.setCellStyle(textStyle);
//                    }
//                    // R26 Col F
//                    Cell R26cell5 = row.createCell(5);
//                    if (record.getR26_acc_amort_amt() != null) {
//                        R26cell5.setCellValue(record.getR26_acc_amort_amt().doubleValue());
//                        R26cell5.setCellStyle(numberStyle);
//                    } else {
//                        R26cell5.setCellValue("");
//                        R26cell5.setCellStyle(textStyle);
//                    }
//                    // R26 Col F
//                    Cell R26cell6 = row.createCell(6);
//                    if (record.getR26_close_bal() != null) {
//                        R26cell6.setCellValue(record.getR26_close_bal().doubleValue());
//                        R26cell6.setCellStyle(numberStyle);
//                    } else {
//                        R26cell6.setCellValue("");
//                        R26cell6.setCellStyle(textStyle);
//                    }
//
//                    // ==================== R27 ====================
//                    row = sheet.getRow(25);
//                    // R27 Col B
//                    Cell R27cell1 = row.createCell(3);
//                    if (record.getR27_cost_rev() != null) {
//                        R27cell1.setCellValue(record.getR27_cost_rev().doubleValue());
//                        R27cell1.setCellStyle(numberStyle);
//                    } else {
//                        R27cell1.setCellValue("");
//                        R27cell1.setCellStyle(textStyle);
//                    }
//
//                    // R27 Col C
//                    Cell R27cell2 = row.createCell(4);
//                    if (record.getR27_useful_life() != null) {
//                        R27cell2.setCellValue(record.getR27_useful_life().doubleValue());
//                        R27cell2.setCellStyle(numberStyle);
//                    } else {
//                        R27cell2.setCellValue("");
//                        R27cell2.setCellStyle(textStyle);
//                    }
//
//                    // R27 Col D
//                    Cell R27cell3 = row.createCell(5);
//                    if (record.getR27_res_value() != null) {
//                        R27cell3.setCellValue(record.getR27_res_value().doubleValue());
//                        R27cell3.setCellStyle(numberStyle);
//                    } else {
//                        R27cell3.setCellValue("");
//                        R27cell3.setCellStyle(textStyle);
//                    }
//
//                    // R27 Col E
//                    Cell R27cell4 = row.createCell(6);
//                    if (record.getR27_month_amort() != null) {
//                        R27cell4.setCellValue(record.getR27_month_amort().doubleValue());
//                        R27cell4.setCellStyle(numberStyle);
//                    } else {
//                        R27cell4.setCellValue("");
//                        R27cell4.setCellStyle(textStyle);
//                    }
//                    // R27 Col F
//                    Cell R27cell5 = row.createCell(5);
//                    if (record.getR27_acc_amort_amt() != null) {
//                        R27cell5.setCellValue(record.getR27_acc_amort_amt().doubleValue());
//                        R27cell5.setCellStyle(numberStyle);
//                    } else {
//                        R27cell5.setCellValue("");
//                        R27cell5.setCellStyle(textStyle);
//                    }
//                    // R27 Col F
//                    Cell R27cell6 = row.createCell(6);
//                    if (record.getR27_close_bal() != null) {
//                        R27cell6.setCellValue(record.getR27_close_bal().doubleValue());
//                        R27cell6.setCellStyle(numberStyle);
//                    } else {
//                        R27cell6.setCellValue("");
//                        R27cell6.setCellStyle(textStyle);
//                    }
//					// ==================== R28 ====================
//                    row = sheet.getRow(26);
//                    // R28 Col B
//                    Cell R28cell1 = row.createCell(3);
//                    if (record.getR28_cost_rev() != null) {
//                        R28cell1.setCellValue(record.getR28_cost_rev().doubleValue());
//                        R28cell1.setCellStyle(numberStyle);
//                    } else {
//                        R28cell1.setCellValue("");
//                        R28cell1.setCellStyle(textStyle);
//                    }
//
//                    // R28 Col C
//                    Cell R28cell2 = row.createCell(4);
//                    if (record.getR28_useful_life() != null) {
//                        R28cell2.setCellValue(record.getR28_useful_life().doubleValue());
//                        R28cell2.setCellStyle(numberStyle);
//                    } else {
//                        R28cell2.setCellValue("");
//                        R28cell2.setCellStyle(textStyle);
//                    }
//
//                    // R28 Col D
//                    Cell R28cell3 = row.createCell(5);
//                    if (record.getR28_res_value() != null) {
//                        R28cell3.setCellValue(record.getR28_res_value().doubleValue());
//                        R28cell3.setCellStyle(numberStyle);
//                    } else {
//                        R28cell3.setCellValue("");
//                        R28cell3.setCellStyle(textStyle);
//                    }
//
//                    // R28 Col E
//                    Cell R28cell4 = row.createCell(6);
//                    if (record.getR28_month_amort() != null) {
//                        R28cell4.setCellValue(record.getR28_month_amort().doubleValue());
//                        R28cell4.setCellStyle(numberStyle);
//                    } else {
//                        R28cell4.setCellValue("");
//                        R28cell4.setCellStyle(textStyle);
//                    }
//                    // R28 Col F
//                    Cell R28cell5 = row.createCell(5);
//                    if (record.getR28_acc_amort_amt() != null) {
//                        R28cell5.setCellValue(record.getR28_acc_amort_amt().doubleValue());
//                        R28cell5.setCellStyle(numberStyle);
//                    } else {
//                        R28cell5.setCellValue("");
//                        R28cell5.setCellStyle(textStyle);
//                    }
//                    // R28 Col F
//                    Cell R28cell6 = row.createCell(6);
//                    if (record.getR28_close_bal() != null) {
//                        R28cell6.setCellValue(record.getR28_close_bal().doubleValue());
//                        R28cell6.setCellStyle(numberStyle);
//                    } else {
//                        R28cell6.setCellValue("");
//                        R28cell6.setCellStyle(textStyle);
//                    }

//
//
//				}
//				workbook.setForceFormulaRecalculation(true);
//			} else {
//
//			}
//
//			// Write the final workbook content to the in-memory stream.
//			workbook.write(out);
//
//			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
//
//			return out.toByteArray();
//		}
//	}

	public static class M_FAS_PK implements Serializable {

		private Date reportDate;
		private BigDecimal reportVersion;

		// default constructor
		public M_FAS_PK() {
		}

		// parameterized constructor
		public M_FAS_PK(Date reportDate, BigDecimal reportVersion) {
			this.reportDate = reportDate;
			this.reportVersion = reportVersion;
		}

		// equals and hashCode
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_FAS_PK))
				return false;
			M_FAS_PK that = (M_FAS_PK) o;
			return Objects.equals(reportDate, that.reportDate) && Objects.equals(reportVersion, that.reportVersion);
		}

		@Override
		public int hashCode() {
			return Objects.hash(reportDate, reportVersion);
		}

		// getters & setters
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
	}

	public static class M_FAS_Summary_Entity {
		private String r10_fix_ass;
		private BigDecimal r10_cost;
		private BigDecimal r10_add;
		private BigDecimal r10_disposals;
		private BigDecimal r10_depreciation;
		private BigDecimal r10_net_book_value;

		private String r11_fix_ass;
		private BigDecimal r11_cost;
		private BigDecimal r11_add;
		private BigDecimal r11_disposals;
		private BigDecimal r11_depreciation;
		private BigDecimal r11_net_book_value;

		private String r12_fix_ass;
		private BigDecimal r12_cost;
		private BigDecimal r12_add;
		private BigDecimal r12_disposals;
		private BigDecimal r12_depreciation;
		private BigDecimal r12_net_book_value;

		private String r13_fix_ass;
		private BigDecimal r13_cost;
		private BigDecimal r13_add;
		private BigDecimal r13_disposals;
		private BigDecimal r13_depreciation;
		private BigDecimal r13_net_book_value;

		private String r14_fix_ass;
		private BigDecimal r14_cost;
		private BigDecimal r14_add;
		private BigDecimal r14_disposals;
		private BigDecimal r14_depreciation;
		private BigDecimal r14_net_book_value;

		private String r15_fix_ass;
		private BigDecimal r15_cost;
		private BigDecimal r15_add;
		private BigDecimal r15_disposals;
		private BigDecimal r15_depreciation;
		private BigDecimal r15_net_book_value;

		private String r16_fix_ass;
		private BigDecimal r16_cost;
		private BigDecimal r16_add;
		private BigDecimal r16_disposals;
		private BigDecimal r16_depreciation;
		private BigDecimal r16_net_book_value;

		private String r17_fix_ass;
		private BigDecimal r17_cost;
		private BigDecimal r17_add;
		private BigDecimal r17_disposals;
		private BigDecimal r17_depreciation;
		private BigDecimal r17_net_book_value;

		private String r23_intangible_ass;
		private BigDecimal r23_cost_rev;
		private BigDecimal r23_useful_life;
		private BigDecimal r23_res_value;
		private BigDecimal r23_month_amort;
		private BigDecimal r23_acc_amort_amt;
		private BigDecimal r23_close_bal;

		private String r24_intangible_ass;
		private BigDecimal r24_cost_rev;
		private BigDecimal r24_useful_life;
		private BigDecimal r24_res_value;
		private BigDecimal r24_month_amort;
		private BigDecimal r24_acc_amort_amt;
		private BigDecimal r24_close_bal;

		private String r25_intangible_ass;
		private BigDecimal r25_cost_rev;
		private BigDecimal r25_useful_life;
		private BigDecimal r25_res_value;
		private BigDecimal r25_month_amort;
		private BigDecimal r25_acc_amort_amt;
		private BigDecimal r25_close_bal;

		private String r26_intangible_ass;
		private BigDecimal r26_cost_rev;
		private BigDecimal r26_useful_life;
		private BigDecimal r26_res_value;
		private BigDecimal r26_month_amort;
		private BigDecimal r26_acc_amort_amt;
		private BigDecimal r26_close_bal;

		private String r27_intangible_ass;
		private BigDecimal r27_cost_rev;
		private BigDecimal r27_useful_life;
		private BigDecimal r27_res_value;
		private BigDecimal r27_month_amort;
		private BigDecimal r27_acc_amort_amt;
		private BigDecimal r27_close_bal;

		private String r28_intangible_ass;
		private BigDecimal r28_cost_rev;
		private BigDecimal r28_useful_life;
		private BigDecimal r28_res_value;
		private BigDecimal r28_month_amort;
		private BigDecimal r28_acc_amort_amt;
		private BigDecimal r28_close_bal;
		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date reportDate;

		@Column(name = "REPORT_VERSION")
		private BigDecimal reportVersion;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_fix_ass() {
			return r10_fix_ass;
		}

		public void setR10_fix_ass(String r10_fix_ass) {
			this.r10_fix_ass = r10_fix_ass;
		}

		public BigDecimal getR10_cost() {
			return r10_cost;
		}

		public void setR10_cost(BigDecimal r10_cost) {
			this.r10_cost = r10_cost;
		}

		public BigDecimal getR10_add() {
			return r10_add;
		}

		public void setR10_add(BigDecimal r10_add) {
			this.r10_add = r10_add;
		}

		public BigDecimal getR10_disposals() {
			return r10_disposals;
		}

		public void setR10_disposals(BigDecimal r10_disposals) {
			this.r10_disposals = r10_disposals;
		}

		public BigDecimal getR10_depreciation() {
			return r10_depreciation;
		}

		public void setR10_depreciation(BigDecimal r10_depreciation) {
			this.r10_depreciation = r10_depreciation;
		}

		public BigDecimal getR10_net_book_value() {
			return r10_net_book_value;
		}

		public void setR10_net_book_value(BigDecimal r10_net_book_value) {
			this.r10_net_book_value = r10_net_book_value;
		}

		public String getR11_fix_ass() {
			return r11_fix_ass;
		}

		public void setR11_fix_ass(String r11_fix_ass) {
			this.r11_fix_ass = r11_fix_ass;
		}

		public BigDecimal getR11_cost() {
			return r11_cost;
		}

		public void setR11_cost(BigDecimal r11_cost) {
			this.r11_cost = r11_cost;
		}

		public BigDecimal getR11_add() {
			return r11_add;
		}

		public void setR11_add(BigDecimal r11_add) {
			this.r11_add = r11_add;
		}

		public BigDecimal getR11_disposals() {
			return r11_disposals;
		}

		public void setR11_disposals(BigDecimal r11_disposals) {
			this.r11_disposals = r11_disposals;
		}

		public BigDecimal getR11_depreciation() {
			return r11_depreciation;
		}

		public void setR11_depreciation(BigDecimal r11_depreciation) {
			this.r11_depreciation = r11_depreciation;
		}

		public BigDecimal getR11_net_book_value() {
			return r11_net_book_value;
		}

		public void setR11_net_book_value(BigDecimal r11_net_book_value) {
			this.r11_net_book_value = r11_net_book_value;
		}

		public String getR12_fix_ass() {
			return r12_fix_ass;
		}

		public void setR12_fix_ass(String r12_fix_ass) {
			this.r12_fix_ass = r12_fix_ass;
		}

		public BigDecimal getR12_cost() {
			return r12_cost;
		}

		public void setR12_cost(BigDecimal r12_cost) {
			this.r12_cost = r12_cost;
		}

		public BigDecimal getR12_add() {
			return r12_add;
		}

		public void setR12_add(BigDecimal r12_add) {
			this.r12_add = r12_add;
		}

		public BigDecimal getR12_disposals() {
			return r12_disposals;
		}

		public void setR12_disposals(BigDecimal r12_disposals) {
			this.r12_disposals = r12_disposals;
		}

		public BigDecimal getR12_depreciation() {
			return r12_depreciation;
		}

		public void setR12_depreciation(BigDecimal r12_depreciation) {
			this.r12_depreciation = r12_depreciation;
		}

		public BigDecimal getR12_net_book_value() {
			return r12_net_book_value;
		}

		public void setR12_net_book_value(BigDecimal r12_net_book_value) {
			this.r12_net_book_value = r12_net_book_value;
		}

		public String getR13_fix_ass() {
			return r13_fix_ass;
		}

		public void setR13_fix_ass(String r13_fix_ass) {
			this.r13_fix_ass = r13_fix_ass;
		}

		public BigDecimal getR13_cost() {
			return r13_cost;
		}

		public void setR13_cost(BigDecimal r13_cost) {
			this.r13_cost = r13_cost;
		}

		public BigDecimal getR13_add() {
			return r13_add;
		}

		public void setR13_add(BigDecimal r13_add) {
			this.r13_add = r13_add;
		}

		public BigDecimal getR13_disposals() {
			return r13_disposals;
		}

		public void setR13_disposals(BigDecimal r13_disposals) {
			this.r13_disposals = r13_disposals;
		}

		public BigDecimal getR13_depreciation() {
			return r13_depreciation;
		}

		public void setR13_depreciation(BigDecimal r13_depreciation) {
			this.r13_depreciation = r13_depreciation;
		}

		public BigDecimal getR13_net_book_value() {
			return r13_net_book_value;
		}

		public void setR13_net_book_value(BigDecimal r13_net_book_value) {
			this.r13_net_book_value = r13_net_book_value;
		}

		public String getR14_fix_ass() {
			return r14_fix_ass;
		}

		public void setR14_fix_ass(String r14_fix_ass) {
			this.r14_fix_ass = r14_fix_ass;
		}

		public BigDecimal getR14_cost() {
			return r14_cost;
		}

		public void setR14_cost(BigDecimal r14_cost) {
			this.r14_cost = r14_cost;
		}

		public BigDecimal getR14_add() {
			return r14_add;
		}

		public void setR14_add(BigDecimal r14_add) {
			this.r14_add = r14_add;
		}

		public BigDecimal getR14_disposals() {
			return r14_disposals;
		}

		public void setR14_disposals(BigDecimal r14_disposals) {
			this.r14_disposals = r14_disposals;
		}

		public BigDecimal getR14_depreciation() {
			return r14_depreciation;
		}

		public void setR14_depreciation(BigDecimal r14_depreciation) {
			this.r14_depreciation = r14_depreciation;
		}

		public BigDecimal getR14_net_book_value() {
			return r14_net_book_value;
		}

		public void setR14_net_book_value(BigDecimal r14_net_book_value) {
			this.r14_net_book_value = r14_net_book_value;
		}

		public String getR15_fix_ass() {
			return r15_fix_ass;
		}

		public void setR15_fix_ass(String r15_fix_ass) {
			this.r15_fix_ass = r15_fix_ass;
		}

		public BigDecimal getR15_cost() {
			return r15_cost;
		}

		public void setR15_cost(BigDecimal r15_cost) {
			this.r15_cost = r15_cost;
		}

		public BigDecimal getR15_add() {
			return r15_add;
		}

		public void setR15_add(BigDecimal r15_add) {
			this.r15_add = r15_add;
		}

		public BigDecimal getR15_disposals() {
			return r15_disposals;
		}

		public void setR15_disposals(BigDecimal r15_disposals) {
			this.r15_disposals = r15_disposals;
		}

		public BigDecimal getR15_depreciation() {
			return r15_depreciation;
		}

		public void setR15_depreciation(BigDecimal r15_depreciation) {
			this.r15_depreciation = r15_depreciation;
		}

		public BigDecimal getR15_net_book_value() {
			return r15_net_book_value;
		}

		public void setR15_net_book_value(BigDecimal r15_net_book_value) {
			this.r15_net_book_value = r15_net_book_value;
		}

		public String getR16_fix_ass() {
			return r16_fix_ass;
		}

		public void setR16_fix_ass(String r16_fix_ass) {
			this.r16_fix_ass = r16_fix_ass;
		}

		public BigDecimal getR16_cost() {
			return r16_cost;
		}

		public void setR16_cost(BigDecimal r16_cost) {
			this.r16_cost = r16_cost;
		}

		public BigDecimal getR16_add() {
			return r16_add;
		}

		public void setR16_add(BigDecimal r16_add) {
			this.r16_add = r16_add;
		}

		public BigDecimal getR16_disposals() {
			return r16_disposals;
		}

		public void setR16_disposals(BigDecimal r16_disposals) {
			this.r16_disposals = r16_disposals;
		}

		public BigDecimal getR16_depreciation() {
			return r16_depreciation;
		}

		public void setR16_depreciation(BigDecimal r16_depreciation) {
			this.r16_depreciation = r16_depreciation;
		}

		public BigDecimal getR16_net_book_value() {
			return r16_net_book_value;
		}

		public void setR16_net_book_value(BigDecimal r16_net_book_value) {
			this.r16_net_book_value = r16_net_book_value;
		}

		public String getR17_fix_ass() {
			return r17_fix_ass;
		}

		public void setR17_fix_ass(String r17_fix_ass) {
			this.r17_fix_ass = r17_fix_ass;
		}

		public BigDecimal getR17_cost() {
			return r17_cost;
		}

		public void setR17_cost(BigDecimal r17_cost) {
			this.r17_cost = r17_cost;
		}

		public BigDecimal getR17_add() {
			return r17_add;
		}

		public void setR17_add(BigDecimal r17_add) {
			this.r17_add = r17_add;
		}

		public BigDecimal getR17_disposals() {
			return r17_disposals;
		}

		public void setR17_disposals(BigDecimal r17_disposals) {
			this.r17_disposals = r17_disposals;
		}

		public BigDecimal getR17_depreciation() {
			return r17_depreciation;
		}

		public void setR17_depreciation(BigDecimal r17_depreciation) {
			this.r17_depreciation = r17_depreciation;
		}

		public BigDecimal getR17_net_book_value() {
			return r17_net_book_value;
		}

		public void setR17_net_book_value(BigDecimal r17_net_book_value) {
			this.r17_net_book_value = r17_net_book_value;
		}

		public String getR23_intangible_ass() {
			return r23_intangible_ass;
		}

		public void setR23_intangible_ass(String r23_intangible_ass) {
			this.r23_intangible_ass = r23_intangible_ass;
		}

		public BigDecimal getR23_cost_rev() {
			return r23_cost_rev;
		}

		public void setR23_cost_rev(BigDecimal r23_cost_rev) {
			this.r23_cost_rev = r23_cost_rev;
		}

		public BigDecimal getR23_useful_life() {
			return r23_useful_life;
		}

		public void setR23_useful_life(BigDecimal r23_useful_life) {
			this.r23_useful_life = r23_useful_life;
		}

		public BigDecimal getR23_res_value() {
			return r23_res_value;
		}

		public void setR23_res_value(BigDecimal r23_res_value) {
			this.r23_res_value = r23_res_value;
		}

		public BigDecimal getR23_month_amort() {
			return r23_month_amort;
		}

		public void setR23_month_amort(BigDecimal r23_month_amort) {
			this.r23_month_amort = r23_month_amort;
		}

		public BigDecimal getR23_acc_amort_amt() {
			return r23_acc_amort_amt;
		}

		public void setR23_acc_amort_amt(BigDecimal r23_acc_amort_amt) {
			this.r23_acc_amort_amt = r23_acc_amort_amt;
		}

		public BigDecimal getR23_close_bal() {
			return r23_close_bal;
		}

		public void setR23_close_bal(BigDecimal r23_close_bal) {
			this.r23_close_bal = r23_close_bal;
		}

		public String getR24_intangible_ass() {
			return r24_intangible_ass;
		}

		public void setR24_intangible_ass(String r24_intangible_ass) {
			this.r24_intangible_ass = r24_intangible_ass;
		}

		public BigDecimal getR24_cost_rev() {
			return r24_cost_rev;
		}

		public void setR24_cost_rev(BigDecimal r24_cost_rev) {
			this.r24_cost_rev = r24_cost_rev;
		}

		public BigDecimal getR24_useful_life() {
			return r24_useful_life;
		}

		public void setR24_useful_life(BigDecimal r24_useful_life) {
			this.r24_useful_life = r24_useful_life;
		}

		public BigDecimal getR24_res_value() {
			return r24_res_value;
		}

		public void setR24_res_value(BigDecimal r24_res_value) {
			this.r24_res_value = r24_res_value;
		}

		public BigDecimal getR24_month_amort() {
			return r24_month_amort;
		}

		public void setR24_month_amort(BigDecimal r24_month_amort) {
			this.r24_month_amort = r24_month_amort;
		}

		public BigDecimal getR24_acc_amort_amt() {
			return r24_acc_amort_amt;
		}

		public void setR24_acc_amort_amt(BigDecimal r24_acc_amort_amt) {
			this.r24_acc_amort_amt = r24_acc_amort_amt;
		}

		public BigDecimal getR24_close_bal() {
			return r24_close_bal;
		}

		public void setR24_close_bal(BigDecimal r24_close_bal) {
			this.r24_close_bal = r24_close_bal;
		}

		public String getR25_intangible_ass() {
			return r25_intangible_ass;
		}

		public void setR25_intangible_ass(String r25_intangible_ass) {
			this.r25_intangible_ass = r25_intangible_ass;
		}

		public BigDecimal getR25_cost_rev() {
			return r25_cost_rev;
		}

		public void setR25_cost_rev(BigDecimal r25_cost_rev) {
			this.r25_cost_rev = r25_cost_rev;
		}

		public BigDecimal getR25_useful_life() {
			return r25_useful_life;
		}

		public void setR25_useful_life(BigDecimal r25_useful_life) {
			this.r25_useful_life = r25_useful_life;
		}

		public BigDecimal getR25_res_value() {
			return r25_res_value;
		}

		public void setR25_res_value(BigDecimal r25_res_value) {
			this.r25_res_value = r25_res_value;
		}

		public BigDecimal getR25_month_amort() {
			return r25_month_amort;
		}

		public void setR25_month_amort(BigDecimal r25_month_amort) {
			this.r25_month_amort = r25_month_amort;
		}

		public BigDecimal getR25_acc_amort_amt() {
			return r25_acc_amort_amt;
		}

		public void setR25_acc_amort_amt(BigDecimal r25_acc_amort_amt) {
			this.r25_acc_amort_amt = r25_acc_amort_amt;
		}

		public BigDecimal getR25_close_bal() {
			return r25_close_bal;
		}

		public void setR25_close_bal(BigDecimal r25_close_bal) {
			this.r25_close_bal = r25_close_bal;
		}

		public String getR26_intangible_ass() {
			return r26_intangible_ass;
		}

		public void setR26_intangible_ass(String r26_intangible_ass) {
			this.r26_intangible_ass = r26_intangible_ass;
		}

		public BigDecimal getR26_cost_rev() {
			return r26_cost_rev;
		}

		public void setR26_cost_rev(BigDecimal r26_cost_rev) {
			this.r26_cost_rev = r26_cost_rev;
		}

		public BigDecimal getR26_useful_life() {
			return r26_useful_life;
		}

		public void setR26_useful_life(BigDecimal r26_useful_life) {
			this.r26_useful_life = r26_useful_life;
		}

		public BigDecimal getR26_res_value() {
			return r26_res_value;
		}

		public void setR26_res_value(BigDecimal r26_res_value) {
			this.r26_res_value = r26_res_value;
		}

		public BigDecimal getR26_month_amort() {
			return r26_month_amort;
		}

		public void setR26_month_amort(BigDecimal r26_month_amort) {
			this.r26_month_amort = r26_month_amort;
		}

		public BigDecimal getR26_acc_amort_amt() {
			return r26_acc_amort_amt;
		}

		public void setR26_acc_amort_amt(BigDecimal r26_acc_amort_amt) {
			this.r26_acc_amort_amt = r26_acc_amort_amt;
		}

		public BigDecimal getR26_close_bal() {
			return r26_close_bal;
		}

		public void setR26_close_bal(BigDecimal r26_close_bal) {
			this.r26_close_bal = r26_close_bal;
		}

		public String getR27_intangible_ass() {
			return r27_intangible_ass;
		}

		public void setR27_intangible_ass(String r27_intangible_ass) {
			this.r27_intangible_ass = r27_intangible_ass;
		}

		public BigDecimal getR27_cost_rev() {
			return r27_cost_rev;
		}

		public void setR27_cost_rev(BigDecimal r27_cost_rev) {
			this.r27_cost_rev = r27_cost_rev;
		}

		public BigDecimal getR27_useful_life() {
			return r27_useful_life;
		}

		public void setR27_useful_life(BigDecimal r27_useful_life) {
			this.r27_useful_life = r27_useful_life;
		}

		public BigDecimal getR27_res_value() {
			return r27_res_value;
		}

		public void setR27_res_value(BigDecimal r27_res_value) {
			this.r27_res_value = r27_res_value;
		}

		public BigDecimal getR27_month_amort() {
			return r27_month_amort;
		}

		public void setR27_month_amort(BigDecimal r27_month_amort) {
			this.r27_month_amort = r27_month_amort;
		}

		public BigDecimal getR27_acc_amort_amt() {
			return r27_acc_amort_amt;
		}

		public void setR27_acc_amort_amt(BigDecimal r27_acc_amort_amt) {
			this.r27_acc_amort_amt = r27_acc_amort_amt;
		}

		public BigDecimal getR27_close_bal() {
			return r27_close_bal;
		}

		public void setR27_close_bal(BigDecimal r27_close_bal) {
			this.r27_close_bal = r27_close_bal;
		}

		public String getR28_intangible_ass() {
			return r28_intangible_ass;
		}

		public void setR28_intangible_ass(String r28_intangible_ass) {
			this.r28_intangible_ass = r28_intangible_ass;
		}

		public BigDecimal getR28_cost_rev() {
			return r28_cost_rev;
		}

		public void setR28_cost_rev(BigDecimal r28_cost_rev) {
			this.r28_cost_rev = r28_cost_rev;
		}

		public BigDecimal getR28_useful_life() {
			return r28_useful_life;
		}

		public void setR28_useful_life(BigDecimal r28_useful_life) {
			this.r28_useful_life = r28_useful_life;
		}

		public BigDecimal getR28_res_value() {
			return r28_res_value;
		}

		public void setR28_res_value(BigDecimal r28_res_value) {
			this.r28_res_value = r28_res_value;
		}

		public BigDecimal getR28_month_amort() {
			return r28_month_amort;
		}

		public void setR28_month_amort(BigDecimal r28_month_amort) {
			this.r28_month_amort = r28_month_amort;
		}

		public BigDecimal getR28_acc_amort_amt() {
			return r28_acc_amort_amt;
		}

		public void setR28_acc_amort_amt(BigDecimal r28_acc_amort_amt) {
			this.r28_acc_amort_amt = r28_acc_amort_amt;
		}

		public BigDecimal getR28_close_bal() {
			return r28_close_bal;
		}

		public void setR28_close_bal(BigDecimal r28_close_bal) {
			this.r28_close_bal = r28_close_bal;
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

		public M_FAS_Summary_Entity() {
			super();
		}

	}

	public static class M_FAS_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;
		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_ADDL_CRITERIA_2")
		private String reportAddlCriteria2;

		@Column(name = "REPORT_ADDL_CRITERIA_3")
		private String reportAddlCriteria3;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 2)
		private BigDecimal acctBalanceInPula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private String entityFlg;

		@Column(name = "MODIFY_FLG")
		private String modifyFlg;

		@Column(name = "DEL_FLG")
		private String delFlg;

		@Column(name = "DEBIT_EQUIVALENT", precision = 18, scale = 2)
		private BigDecimal debitEquivalent;

		@Column(name = "CREDIT_EQUIVALENT", precision = 18, scale = 2)
		private BigDecimal creditEquivalent;

		public M_FAS_Detail_Entity() {
		}

		// ------ GETTERS & SETTERS BELOW ------

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String reportAddlCriteria3) {
			this.reportAddlCriteria3 = reportAddlCriteria3;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}

		public BigDecimal getDebitEquivalent() {
			return debitEquivalent;
		}

		public void setDebitEquivalent(BigDecimal debitEquivalent) {
			this.debitEquivalent = debitEquivalent;
		}

		public BigDecimal getCreditEquivalent() {
			return creditEquivalent;
		}

		public void setCreditEquivalent(BigDecimal creditEquivalent) {
			this.creditEquivalent = creditEquivalent;
		}
	}

	public static class M_FAS_Archival_Summary_Entity {
		private String r10_fix_ass;
		private BigDecimal r10_cost;
		private BigDecimal r10_add;
		private BigDecimal r10_disposals;
		private BigDecimal r10_depreciation;
		private BigDecimal r10_net_book_value;

		private String r11_fix_ass;
		private BigDecimal r11_cost;
		private BigDecimal r11_add;
		private BigDecimal r11_disposals;
		private BigDecimal r11_depreciation;
		private BigDecimal r11_net_book_value;

		private String r12_fix_ass;
		private BigDecimal r12_cost;
		private BigDecimal r12_add;
		private BigDecimal r12_disposals;
		private BigDecimal r12_depreciation;
		private BigDecimal r12_net_book_value;

		private String r13_fix_ass;
		private BigDecimal r13_cost;
		private BigDecimal r13_add;
		private BigDecimal r13_disposals;
		private BigDecimal r13_depreciation;
		private BigDecimal r13_net_book_value;

		private String r14_fix_ass;
		private BigDecimal r14_cost;
		private BigDecimal r14_add;
		private BigDecimal r14_disposals;
		private BigDecimal r14_depreciation;
		private BigDecimal r14_net_book_value;

		private String r15_fix_ass;
		private BigDecimal r15_cost;
		private BigDecimal r15_add;
		private BigDecimal r15_disposals;
		private BigDecimal r15_depreciation;
		private BigDecimal r15_net_book_value;

		private String r16_fix_ass;
		private BigDecimal r16_cost;
		private BigDecimal r16_add;
		private BigDecimal r16_disposals;
		private BigDecimal r16_depreciation;
		private BigDecimal r16_net_book_value;

		private String r17_fix_ass;
		private BigDecimal r17_cost;
		private BigDecimal r17_add;
		private BigDecimal r17_disposals;
		private BigDecimal r17_depreciation;
		private BigDecimal r17_net_book_value;

		private String r23_intangible_ass;
		private BigDecimal r23_cost_rev;
		private BigDecimal r23_useful_life;
		private BigDecimal r23_res_value;
		private BigDecimal r23_month_amort;
		private BigDecimal r23_acc_amort_amt;
		private BigDecimal r23_close_bal;

		private String r24_intangible_ass;
		private BigDecimal r24_cost_rev;
		private BigDecimal r24_useful_life;
		private BigDecimal r24_res_value;
		private BigDecimal r24_month_amort;
		private BigDecimal r24_acc_amort_amt;
		private BigDecimal r24_close_bal;

		private String r25_intangible_ass;
		private BigDecimal r25_cost_rev;
		private BigDecimal r25_useful_life;
		private BigDecimal r25_res_value;
		private BigDecimal r25_month_amort;
		private BigDecimal r25_acc_amort_amt;
		private BigDecimal r25_close_bal;

		private String r26_intangible_ass;
		private BigDecimal r26_cost_rev;
		private BigDecimal r26_useful_life;
		private BigDecimal r26_res_value;
		private BigDecimal r26_month_amort;
		private BigDecimal r26_acc_amort_amt;
		private BigDecimal r26_close_bal;

		private String r27_intangible_ass;
		private BigDecimal r27_cost_rev;
		private BigDecimal r27_useful_life;
		private BigDecimal r27_res_value;
		private BigDecimal r27_month_amort;
		private BigDecimal r27_acc_amort_amt;
		private BigDecimal r27_close_bal;

		private String r28_intangible_ass;
		private BigDecimal r28_cost_rev;
		private BigDecimal r28_useful_life;
		private BigDecimal r28_res_value;
		private BigDecimal r28_month_amort;
		private BigDecimal r28_acc_amort_amt;
		private BigDecimal r28_close_bal;
		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date reportDate;

		@Id
		@Column(name = "REPORT_VERSION")
		private BigDecimal reportVersion;

		@Column(name = "REPORT_RESUBDATE")
		@Temporal(TemporalType.TIMESTAMP)
		private Date reportResubDate;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_fix_ass() {
			return r10_fix_ass;
		}

		public void setR10_fix_ass(String r10_fix_ass) {
			this.r10_fix_ass = r10_fix_ass;
		}

		public BigDecimal getR10_cost() {
			return r10_cost;
		}

		public void setR10_cost(BigDecimal r10_cost) {
			this.r10_cost = r10_cost;
		}

		public BigDecimal getR10_add() {
			return r10_add;
		}

		public void setR10_add(BigDecimal r10_add) {
			this.r10_add = r10_add;
		}

		public BigDecimal getR10_disposals() {
			return r10_disposals;
		}

		public void setR10_disposals(BigDecimal r10_disposals) {
			this.r10_disposals = r10_disposals;
		}

		public BigDecimal getR10_depreciation() {
			return r10_depreciation;
		}

		public void setR10_depreciation(BigDecimal r10_depreciation) {
			this.r10_depreciation = r10_depreciation;
		}

		public BigDecimal getR10_net_book_value() {
			return r10_net_book_value;
		}

		public void setR10_net_book_value(BigDecimal r10_net_book_value) {
			this.r10_net_book_value = r10_net_book_value;
		}

		public String getR11_fix_ass() {
			return r11_fix_ass;
		}

		public void setR11_fix_ass(String r11_fix_ass) {
			this.r11_fix_ass = r11_fix_ass;
		}

		public BigDecimal getR11_cost() {
			return r11_cost;
		}

		public void setR11_cost(BigDecimal r11_cost) {
			this.r11_cost = r11_cost;
		}

		public BigDecimal getR11_add() {
			return r11_add;
		}

		public void setR11_add(BigDecimal r11_add) {
			this.r11_add = r11_add;
		}

		public BigDecimal getR11_disposals() {
			return r11_disposals;
		}

		public void setR11_disposals(BigDecimal r11_disposals) {
			this.r11_disposals = r11_disposals;
		}

		public BigDecimal getR11_depreciation() {
			return r11_depreciation;
		}

		public void setR11_depreciation(BigDecimal r11_depreciation) {
			this.r11_depreciation = r11_depreciation;
		}

		public BigDecimal getR11_net_book_value() {
			return r11_net_book_value;
		}

		public void setR11_net_book_value(BigDecimal r11_net_book_value) {
			this.r11_net_book_value = r11_net_book_value;
		}

		public String getR12_fix_ass() {
			return r12_fix_ass;
		}

		public void setR12_fix_ass(String r12_fix_ass) {
			this.r12_fix_ass = r12_fix_ass;
		}

		public BigDecimal getR12_cost() {
			return r12_cost;
		}

		public void setR12_cost(BigDecimal r12_cost) {
			this.r12_cost = r12_cost;
		}

		public BigDecimal getR12_add() {
			return r12_add;
		}

		public void setR12_add(BigDecimal r12_add) {
			this.r12_add = r12_add;
		}

		public BigDecimal getR12_disposals() {
			return r12_disposals;
		}

		public void setR12_disposals(BigDecimal r12_disposals) {
			this.r12_disposals = r12_disposals;
		}

		public BigDecimal getR12_depreciation() {
			return r12_depreciation;
		}

		public void setR12_depreciation(BigDecimal r12_depreciation) {
			this.r12_depreciation = r12_depreciation;
		}

		public BigDecimal getR12_net_book_value() {
			return r12_net_book_value;
		}

		public void setR12_net_book_value(BigDecimal r12_net_book_value) {
			this.r12_net_book_value = r12_net_book_value;
		}

		public String getR13_fix_ass() {
			return r13_fix_ass;
		}

		public void setR13_fix_ass(String r13_fix_ass) {
			this.r13_fix_ass = r13_fix_ass;
		}

		public BigDecimal getR13_cost() {
			return r13_cost;
		}

		public void setR13_cost(BigDecimal r13_cost) {
			this.r13_cost = r13_cost;
		}

		public BigDecimal getR13_add() {
			return r13_add;
		}

		public void setR13_add(BigDecimal r13_add) {
			this.r13_add = r13_add;
		}

		public BigDecimal getR13_disposals() {
			return r13_disposals;
		}

		public void setR13_disposals(BigDecimal r13_disposals) {
			this.r13_disposals = r13_disposals;
		}

		public BigDecimal getR13_depreciation() {
			return r13_depreciation;
		}

		public void setR13_depreciation(BigDecimal r13_depreciation) {
			this.r13_depreciation = r13_depreciation;
		}

		public BigDecimal getR13_net_book_value() {
			return r13_net_book_value;
		}

		public void setR13_net_book_value(BigDecimal r13_net_book_value) {
			this.r13_net_book_value = r13_net_book_value;
		}

		public String getR14_fix_ass() {
			return r14_fix_ass;
		}

		public void setR14_fix_ass(String r14_fix_ass) {
			this.r14_fix_ass = r14_fix_ass;
		}

		public BigDecimal getR14_cost() {
			return r14_cost;
		}

		public void setR14_cost(BigDecimal r14_cost) {
			this.r14_cost = r14_cost;
		}

		public BigDecimal getR14_add() {
			return r14_add;
		}

		public void setR14_add(BigDecimal r14_add) {
			this.r14_add = r14_add;
		}

		public BigDecimal getR14_disposals() {
			return r14_disposals;
		}

		public void setR14_disposals(BigDecimal r14_disposals) {
			this.r14_disposals = r14_disposals;
		}

		public BigDecimal getR14_depreciation() {
			return r14_depreciation;
		}

		public void setR14_depreciation(BigDecimal r14_depreciation) {
			this.r14_depreciation = r14_depreciation;
		}

		public BigDecimal getR14_net_book_value() {
			return r14_net_book_value;
		}

		public void setR14_net_book_value(BigDecimal r14_net_book_value) {
			this.r14_net_book_value = r14_net_book_value;
		}

		public String getR15_fix_ass() {
			return r15_fix_ass;
		}

		public void setR15_fix_ass(String r15_fix_ass) {
			this.r15_fix_ass = r15_fix_ass;
		}

		public BigDecimal getR15_cost() {
			return r15_cost;
		}

		public void setR15_cost(BigDecimal r15_cost) {
			this.r15_cost = r15_cost;
		}

		public BigDecimal getR15_add() {
			return r15_add;
		}

		public void setR15_add(BigDecimal r15_add) {
			this.r15_add = r15_add;
		}

		public BigDecimal getR15_disposals() {
			return r15_disposals;
		}

		public void setR15_disposals(BigDecimal r15_disposals) {
			this.r15_disposals = r15_disposals;
		}

		public BigDecimal getR15_depreciation() {
			return r15_depreciation;
		}

		public void setR15_depreciation(BigDecimal r15_depreciation) {
			this.r15_depreciation = r15_depreciation;
		}

		public BigDecimal getR15_net_book_value() {
			return r15_net_book_value;
		}

		public void setR15_net_book_value(BigDecimal r15_net_book_value) {
			this.r15_net_book_value = r15_net_book_value;
		}

		public String getR16_fix_ass() {
			return r16_fix_ass;
		}

		public void setR16_fix_ass(String r16_fix_ass) {
			this.r16_fix_ass = r16_fix_ass;
		}

		public BigDecimal getR16_cost() {
			return r16_cost;
		}

		public void setR16_cost(BigDecimal r16_cost) {
			this.r16_cost = r16_cost;
		}

		public BigDecimal getR16_add() {
			return r16_add;
		}

		public void setR16_add(BigDecimal r16_add) {
			this.r16_add = r16_add;
		}

		public BigDecimal getR16_disposals() {
			return r16_disposals;
		}

		public void setR16_disposals(BigDecimal r16_disposals) {
			this.r16_disposals = r16_disposals;
		}

		public BigDecimal getR16_depreciation() {
			return r16_depreciation;
		}

		public void setR16_depreciation(BigDecimal r16_depreciation) {
			this.r16_depreciation = r16_depreciation;
		}

		public BigDecimal getR16_net_book_value() {
			return r16_net_book_value;
		}

		public void setR16_net_book_value(BigDecimal r16_net_book_value) {
			this.r16_net_book_value = r16_net_book_value;
		}

		public String getR17_fix_ass() {
			return r17_fix_ass;
		}

		public void setR17_fix_ass(String r17_fix_ass) {
			this.r17_fix_ass = r17_fix_ass;
		}

		public BigDecimal getR17_cost() {
			return r17_cost;
		}

		public void setR17_cost(BigDecimal r17_cost) {
			this.r17_cost = r17_cost;
		}

		public BigDecimal getR17_add() {
			return r17_add;
		}

		public void setR17_add(BigDecimal r17_add) {
			this.r17_add = r17_add;
		}

		public BigDecimal getR17_disposals() {
			return r17_disposals;
		}

		public void setR17_disposals(BigDecimal r17_disposals) {
			this.r17_disposals = r17_disposals;
		}

		public BigDecimal getR17_depreciation() {
			return r17_depreciation;
		}

		public void setR17_depreciation(BigDecimal r17_depreciation) {
			this.r17_depreciation = r17_depreciation;
		}

		public BigDecimal getR17_net_book_value() {
			return r17_net_book_value;
		}

		public void setR17_net_book_value(BigDecimal r17_net_book_value) {
			this.r17_net_book_value = r17_net_book_value;
		}

		public String getR23_intangible_ass() {
			return r23_intangible_ass;
		}

		public void setR23_intangible_ass(String r23_intangible_ass) {
			this.r23_intangible_ass = r23_intangible_ass;
		}

		public BigDecimal getR23_cost_rev() {
			return r23_cost_rev;
		}

		public void setR23_cost_rev(BigDecimal r23_cost_rev) {
			this.r23_cost_rev = r23_cost_rev;
		}

		public BigDecimal getR23_useful_life() {
			return r23_useful_life;
		}

		public void setR23_useful_life(BigDecimal r23_useful_life) {
			this.r23_useful_life = r23_useful_life;
		}

		public BigDecimal getR23_res_value() {
			return r23_res_value;
		}

		public void setR23_res_value(BigDecimal r23_res_value) {
			this.r23_res_value = r23_res_value;
		}

		public BigDecimal getR23_month_amort() {
			return r23_month_amort;
		}

		public void setR23_month_amort(BigDecimal r23_month_amort) {
			this.r23_month_amort = r23_month_amort;
		}

		public BigDecimal getR23_acc_amort_amt() {
			return r23_acc_amort_amt;
		}

		public void setR23_acc_amort_amt(BigDecimal r23_acc_amort_amt) {
			this.r23_acc_amort_amt = r23_acc_amort_amt;
		}

		public BigDecimal getR23_close_bal() {
			return r23_close_bal;
		}

		public void setR23_close_bal(BigDecimal r23_close_bal) {
			this.r23_close_bal = r23_close_bal;
		}

		public String getR24_intangible_ass() {
			return r24_intangible_ass;
		}

		public void setR24_intangible_ass(String r24_intangible_ass) {
			this.r24_intangible_ass = r24_intangible_ass;
		}

		public BigDecimal getR24_cost_rev() {
			return r24_cost_rev;
		}

		public void setR24_cost_rev(BigDecimal r24_cost_rev) {
			this.r24_cost_rev = r24_cost_rev;
		}

		public BigDecimal getR24_useful_life() {
			return r24_useful_life;
		}

		public void setR24_useful_life(BigDecimal r24_useful_life) {
			this.r24_useful_life = r24_useful_life;
		}

		public BigDecimal getR24_res_value() {
			return r24_res_value;
		}

		public void setR24_res_value(BigDecimal r24_res_value) {
			this.r24_res_value = r24_res_value;
		}

		public BigDecimal getR24_month_amort() {
			return r24_month_amort;
		}

		public void setR24_month_amort(BigDecimal r24_month_amort) {
			this.r24_month_amort = r24_month_amort;
		}

		public BigDecimal getR24_acc_amort_amt() {
			return r24_acc_amort_amt;
		}

		public void setR24_acc_amort_amt(BigDecimal r24_acc_amort_amt) {
			this.r24_acc_amort_amt = r24_acc_amort_amt;
		}

		public BigDecimal getR24_close_bal() {
			return r24_close_bal;
		}

		public void setR24_close_bal(BigDecimal r24_close_bal) {
			this.r24_close_bal = r24_close_bal;
		}

		public String getR25_intangible_ass() {
			return r25_intangible_ass;
		}

		public void setR25_intangible_ass(String r25_intangible_ass) {
			this.r25_intangible_ass = r25_intangible_ass;
		}

		public BigDecimal getR25_cost_rev() {
			return r25_cost_rev;
		}

		public void setR25_cost_rev(BigDecimal r25_cost_rev) {
			this.r25_cost_rev = r25_cost_rev;
		}

		public BigDecimal getR25_useful_life() {
			return r25_useful_life;
		}

		public void setR25_useful_life(BigDecimal r25_useful_life) {
			this.r25_useful_life = r25_useful_life;
		}

		public BigDecimal getR25_res_value() {
			return r25_res_value;
		}

		public void setR25_res_value(BigDecimal r25_res_value) {
			this.r25_res_value = r25_res_value;
		}

		public BigDecimal getR25_month_amort() {
			return r25_month_amort;
		}

		public void setR25_month_amort(BigDecimal r25_month_amort) {
			this.r25_month_amort = r25_month_amort;
		}

		public BigDecimal getR25_acc_amort_amt() {
			return r25_acc_amort_amt;
		}

		public void setR25_acc_amort_amt(BigDecimal r25_acc_amort_amt) {
			this.r25_acc_amort_amt = r25_acc_amort_amt;
		}

		public BigDecimal getR25_close_bal() {
			return r25_close_bal;
		}

		public void setR25_close_bal(BigDecimal r25_close_bal) {
			this.r25_close_bal = r25_close_bal;
		}

		public String getR26_intangible_ass() {
			return r26_intangible_ass;
		}

		public void setR26_intangible_ass(String r26_intangible_ass) {
			this.r26_intangible_ass = r26_intangible_ass;
		}

		public BigDecimal getR26_cost_rev() {
			return r26_cost_rev;
		}

		public void setR26_cost_rev(BigDecimal r26_cost_rev) {
			this.r26_cost_rev = r26_cost_rev;
		}

		public BigDecimal getR26_useful_life() {
			return r26_useful_life;
		}

		public void setR26_useful_life(BigDecimal r26_useful_life) {
			this.r26_useful_life = r26_useful_life;
		}

		public BigDecimal getR26_res_value() {
			return r26_res_value;
		}

		public void setR26_res_value(BigDecimal r26_res_value) {
			this.r26_res_value = r26_res_value;
		}

		public BigDecimal getR26_month_amort() {
			return r26_month_amort;
		}

		public void setR26_month_amort(BigDecimal r26_month_amort) {
			this.r26_month_amort = r26_month_amort;
		}

		public BigDecimal getR26_acc_amort_amt() {
			return r26_acc_amort_amt;
		}

		public void setR26_acc_amort_amt(BigDecimal r26_acc_amort_amt) {
			this.r26_acc_amort_amt = r26_acc_amort_amt;
		}

		public BigDecimal getR26_close_bal() {
			return r26_close_bal;
		}

		public void setR26_close_bal(BigDecimal r26_close_bal) {
			this.r26_close_bal = r26_close_bal;
		}

		public String getR27_intangible_ass() {
			return r27_intangible_ass;
		}

		public void setR27_intangible_ass(String r27_intangible_ass) {
			this.r27_intangible_ass = r27_intangible_ass;
		}

		public BigDecimal getR27_cost_rev() {
			return r27_cost_rev;
		}

		public void setR27_cost_rev(BigDecimal r27_cost_rev) {
			this.r27_cost_rev = r27_cost_rev;
		}

		public BigDecimal getR27_useful_life() {
			return r27_useful_life;
		}

		public void setR27_useful_life(BigDecimal r27_useful_life) {
			this.r27_useful_life = r27_useful_life;
		}

		public BigDecimal getR27_res_value() {
			return r27_res_value;
		}

		public void setR27_res_value(BigDecimal r27_res_value) {
			this.r27_res_value = r27_res_value;
		}

		public BigDecimal getR27_month_amort() {
			return r27_month_amort;
		}

		public void setR27_month_amort(BigDecimal r27_month_amort) {
			this.r27_month_amort = r27_month_amort;
		}

		public BigDecimal getR27_acc_amort_amt() {
			return r27_acc_amort_amt;
		}

		public void setR27_acc_amort_amt(BigDecimal r27_acc_amort_amt) {
			this.r27_acc_amort_amt = r27_acc_amort_amt;
		}

		public BigDecimal getR27_close_bal() {
			return r27_close_bal;
		}

		public void setR27_close_bal(BigDecimal r27_close_bal) {
			this.r27_close_bal = r27_close_bal;
		}

		public String getR28_intangible_ass() {
			return r28_intangible_ass;
		}

		public void setR28_intangible_ass(String r28_intangible_ass) {
			this.r28_intangible_ass = r28_intangible_ass;
		}

		public BigDecimal getR28_cost_rev() {
			return r28_cost_rev;
		}

		public void setR28_cost_rev(BigDecimal r28_cost_rev) {
			this.r28_cost_rev = r28_cost_rev;
		}

		public BigDecimal getR28_useful_life() {
			return r28_useful_life;
		}

		public void setR28_useful_life(BigDecimal r28_useful_life) {
			this.r28_useful_life = r28_useful_life;
		}

		public BigDecimal getR28_res_value() {
			return r28_res_value;
		}

		public void setR28_res_value(BigDecimal r28_res_value) {
			this.r28_res_value = r28_res_value;
		}

		public BigDecimal getR28_month_amort() {
			return r28_month_amort;
		}

		public void setR28_month_amort(BigDecimal r28_month_amort) {
			this.r28_month_amort = r28_month_amort;
		}

		public BigDecimal getR28_acc_amort_amt() {
			return r28_acc_amort_amt;
		}

		public void setR28_acc_amort_amt(BigDecimal r28_acc_amort_amt) {
			this.r28_acc_amort_amt = r28_acc_amort_amt;
		}

		public BigDecimal getR28_close_bal() {
			return r28_close_bal;
		}

		public void setR28_close_bal(BigDecimal r28_close_bal) {
			this.r28_close_bal = r28_close_bal;
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

		public M_FAS_Archival_Summary_Entity() {
			super();
		}

	}

	public static class M_FAS_Archival_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;
		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_ADDL_CRITERIA_2")
		private String reportAddlCriteria2;

		@Column(name = "REPORT_ADDL_CRITERIA_3")
		private String reportAddlCriteria3;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 2)
		private BigDecimal acctBalanceInPula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private String entityFlg;

		@Column(name = "MODIFY_FLG")
		private String modifyFlg;

		@Column(name = "DEL_FLG")
		private String delFlg;

		@Column(name = "DEBIT_EQUIVALENT", precision = 18, scale = 2)
		private BigDecimal debitEquivalent;

		@Column(name = "CREDIT_EQUIVALENT", precision = 18, scale = 2)
		private BigDecimal creditEquivalent;

		public M_FAS_Archival_Detail_Entity() {
		}

		// ------ GETTERS & SETTERS BELOW ------

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String reportAddlCriteria3) {
			this.reportAddlCriteria3 = reportAddlCriteria3;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}

		public BigDecimal getDebitEquivalent() {
			return debitEquivalent;
		}

		public void setDebitEquivalent(BigDecimal debitEquivalent) {
			this.debitEquivalent = debitEquivalent;
		}

		public BigDecimal getCreditEquivalent() {
			return creditEquivalent;
		}

		public void setCreditEquivalent(BigDecimal creditEquivalent) {
			this.creditEquivalent = creditEquivalent;
		}

	}

	public static class M_FAS_Manual_Summary_Entity {

		/* ------------------- R12 --------------------- */
		@Column(name = "R12_FIX_ASS")
		private String r12_fix_ass;

		@Column(name = "R12_COST")
		private BigDecimal r12_cost;

		@Column(name = "R12_ADD")
		private BigDecimal r12_add;

		@Column(name = "R12_DISPOSALS")
		private BigDecimal r12_disposals;

		@Column(name = "R12_DEPRECIATION")
		private BigDecimal r12_depreciation;

		@Column(name = "R12_NET_BOOK_VALUE")
		private BigDecimal r12_net_book_value;

		/* ------------------- R23 --------------------- */
		@Column(name = "R23_INTANGIBLE_ASS")
		private String r23_intangible_ass;

		@Column(name = "R23_COST_REV")
		private BigDecimal r23_cost_rev;

		@Column(name = "R23_USEFUL_LIFE")
		private BigDecimal r23_useful_life;

		@Column(name = "R23_RES_VALUE")
		private BigDecimal r23_res_value;

		@Column(name = "R23_MONTH_AMORT")
		private BigDecimal r23_month_amort;

		@Column(name = "R23_ACC_AMORT_AMT")
		private BigDecimal r23_acc_amort_amt;

		@Column(name = "R23_CLOSE_BAL")
		private BigDecimal r23_close_bal;

		/* ------------------- R24 --------------------- */
		@Column(name = "R24_INTANGIBLE_ASS")
		private String r24_intangible_ass;

		@Column(name = "R24_COST_REV")
		private BigDecimal r24_cost_rev;

		@Column(name = "R24_USEFUL_LIFE")
		private BigDecimal r24_useful_life;

		@Column(name = "R24_RES_VALUE")
		private BigDecimal r24_res_value;

		@Column(name = "R24_MONTH_AMORT")
		private BigDecimal r24_month_amort;

		@Column(name = "R24_ACC_AMORT_AMT")
		private BigDecimal r24_acc_amort_amt;

		@Column(name = "R24_CLOSE_BAL")
		private BigDecimal r24_close_bal;

		/* ------------------- R25 --------------------- */
		@Column(name = "R25_INTANGIBLE_ASS")
		private String r25_intangible_ass;

		@Column(name = "R25_COST_REV")
		private BigDecimal r25_cost_rev;

		@Column(name = "R25_USEFUL_LIFE")
		private BigDecimal r25_useful_life;

		@Column(name = "R25_RES_VALUE")
		private BigDecimal r25_res_value;

		@Column(name = "R25_MONTH_AMORT")
		private BigDecimal r25_month_amort;

		@Column(name = "R25_ACC_AMORT_AMT")
		private BigDecimal r25_acc_amort_amt;

		@Column(name = "R25_CLOSE_BAL")
		private BigDecimal r25_close_bal;

		/* ------------------- R26 --------------------- */
		@Column(name = "R26_INTANGIBLE_ASS")
		private String r26_intangible_ass;

		@Column(name = "R26_COST_REV")
		private BigDecimal r26_cost_rev;

		@Column(name = "R26_USEFUL_LIFE")
		private BigDecimal r26_useful_life;

		@Column(name = "R26_RES_VALUE")
		private BigDecimal r26_res_value;

		@Column(name = "R26_MONTH_AMORT")
		private BigDecimal r26_month_amort;

		@Column(name = "R26_ACC_AMORT_AMT")
		private BigDecimal r26_acc_amort_amt;

		@Column(name = "R26_CLOSE_BAL")
		private BigDecimal r26_close_bal;

		/* ------------------- R27 --------------------- */
		@Column(name = "R27_INTANGIBLE_ASS")
		private String r27_intangible_ass;

		@Column(name = "R27_COST_REV")
		private BigDecimal r27_cost_rev;

		@Column(name = "R27_USEFUL_LIFE")
		private BigDecimal r27_useful_life;

		@Column(name = "R27_RES_VALUE")
		private BigDecimal r27_res_value;

		@Column(name = "R27_MONTH_AMORT")
		private BigDecimal r27_month_amort;

		@Column(name = "R27_ACC_AMORT_AMT")
		private BigDecimal r27_acc_amort_amt;

		@Column(name = "R27_CLOSE_BAL")
		private BigDecimal r27_close_bal;

		/* ------------------- R28 --------------------- */
		@Column(name = "R28_INTANGIBLE_ASS")
		private String r28_intangible_ass;

		@Column(name = "R28_COST_REV")
		private BigDecimal r28_cost_rev;

		@Column(name = "R28_USEFUL_LIFE")
		private BigDecimal r28_useful_life;

		@Column(name = "R28_RES_VALUE")
		private BigDecimal r28_res_value;

		@Column(name = "R28_MONTH_AMORT")
		private BigDecimal r28_month_amort;

		@Column(name = "R28_ACC_AMORT_AMT")
		private BigDecimal r28_acc_amort_amt;

		@Column(name = "R28_CLOSE_BAL")
		private BigDecimal r28_close_bal;

		/* ------------------- REPORT INFO --------------------- */
		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Column(name = "REPORT_DATE")
		private Date report_date;

		@Column(name = "REPORT_VERSION")
		private String report_version;

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

		public M_FAS_Manual_Summary_Entity() {
			super();
		}

		public String getR12_fix_ass() {
			return r12_fix_ass;
		}

		public void setR12_fix_ass(String r12_fix_ass) {
			this.r12_fix_ass = r12_fix_ass;
		}

		public BigDecimal getR12_cost() {
			return r12_cost;
		}

		public void setR12_cost(BigDecimal r12_cost) {
			this.r12_cost = r12_cost;
		}

		public BigDecimal getR12_add() {
			return r12_add;
		}

		public void setR12_add(BigDecimal r12_add) {
			this.r12_add = r12_add;
		}

		public BigDecimal getR12_disposals() {
			return r12_disposals;
		}

		public void setR12_disposals(BigDecimal r12_disposals) {
			this.r12_disposals = r12_disposals;
		}

		public BigDecimal getR12_depreciation() {
			return r12_depreciation;
		}

		public void setR12_depreciation(BigDecimal r12_depreciation) {
			this.r12_depreciation = r12_depreciation;
		}

		public BigDecimal getR12_net_book_value() {
			return r12_net_book_value;
		}

		public void setR12_net_book_value(BigDecimal r12_net_book_value) {
			this.r12_net_book_value = r12_net_book_value;
		}

		public String getR23_intangible_ass() {
			return r23_intangible_ass;
		}

		public void setR23_intangible_ass(String r23_intangible_ass) {
			this.r23_intangible_ass = r23_intangible_ass;
		}

		public BigDecimal getR23_cost_rev() {
			return r23_cost_rev;
		}

		public void setR23_cost_rev(BigDecimal r23_cost_rev) {
			this.r23_cost_rev = r23_cost_rev;
		}

		public BigDecimal getR23_useful_life() {
			return r23_useful_life;
		}

		public void setR23_useful_life(BigDecimal r23_useful_life) {
			this.r23_useful_life = r23_useful_life;
		}

		public BigDecimal getR23_res_value() {
			return r23_res_value;
		}

		public void setR23_res_value(BigDecimal r23_res_value) {
			this.r23_res_value = r23_res_value;
		}

		public BigDecimal getR23_month_amort() {
			return r23_month_amort;
		}

		public void setR23_month_amort(BigDecimal r23_month_amort) {
			this.r23_month_amort = r23_month_amort;
		}

		public BigDecimal getR23_acc_amort_amt() {
			return r23_acc_amort_amt;
		}

		public void setR23_acc_amort_amt(BigDecimal r23_acc_amort_amt) {
			this.r23_acc_amort_amt = r23_acc_amort_amt;
		}

		public BigDecimal getR23_close_bal() {
			return r23_close_bal;
		}

		public void setR23_close_bal(BigDecimal r23_close_bal) {
			this.r23_close_bal = r23_close_bal;
		}

		public String getR24_intangible_ass() {
			return r24_intangible_ass;
		}

		public void setR24_intangible_ass(String r24_intangible_ass) {
			this.r24_intangible_ass = r24_intangible_ass;
		}

		public BigDecimal getR24_cost_rev() {
			return r24_cost_rev;
		}

		public void setR24_cost_rev(BigDecimal r24_cost_rev) {
			this.r24_cost_rev = r24_cost_rev;
		}

		public BigDecimal getR24_useful_life() {
			return r24_useful_life;
		}

		public void setR24_useful_life(BigDecimal r24_useful_life) {
			this.r24_useful_life = r24_useful_life;
		}

		public BigDecimal getR24_res_value() {
			return r24_res_value;
		}

		public void setR24_res_value(BigDecimal r24_res_value) {
			this.r24_res_value = r24_res_value;
		}

		public BigDecimal getR24_month_amort() {
			return r24_month_amort;
		}

		public void setR24_month_amort(BigDecimal r24_month_amort) {
			this.r24_month_amort = r24_month_amort;
		}

		public BigDecimal getR24_acc_amort_amt() {
			return r24_acc_amort_amt;
		}

		public void setR24_acc_amort_amt(BigDecimal r24_acc_amort_amt) {
			this.r24_acc_amort_amt = r24_acc_amort_amt;
		}

		public BigDecimal getR24_close_bal() {
			return r24_close_bal;
		}

		public void setR24_close_bal(BigDecimal r24_close_bal) {
			this.r24_close_bal = r24_close_bal;
		}

		public String getR25_intangible_ass() {
			return r25_intangible_ass;
		}

		public void setR25_intangible_ass(String r25_intangible_ass) {
			this.r25_intangible_ass = r25_intangible_ass;
		}

		public BigDecimal getR25_cost_rev() {
			return r25_cost_rev;
		}

		public void setR25_cost_rev(BigDecimal r25_cost_rev) {
			this.r25_cost_rev = r25_cost_rev;
		}

		public BigDecimal getR25_useful_life() {
			return r25_useful_life;
		}

		public void setR25_useful_life(BigDecimal r25_useful_life) {
			this.r25_useful_life = r25_useful_life;
		}

		public BigDecimal getR25_res_value() {
			return r25_res_value;
		}

		public void setR25_res_value(BigDecimal r25_res_value) {
			this.r25_res_value = r25_res_value;
		}

		public BigDecimal getR25_month_amort() {
			return r25_month_amort;
		}

		public void setR25_month_amort(BigDecimal r25_month_amort) {
			this.r25_month_amort = r25_month_amort;
		}

		public BigDecimal getR25_acc_amort_amt() {
			return r25_acc_amort_amt;
		}

		public void setR25_acc_amort_amt(BigDecimal r25_acc_amort_amt) {
			this.r25_acc_amort_amt = r25_acc_amort_amt;
		}

		public BigDecimal getR25_close_bal() {
			return r25_close_bal;
		}

		public void setR25_close_bal(BigDecimal r25_close_bal) {
			this.r25_close_bal = r25_close_bal;
		}

		public String getR26_intangible_ass() {
			return r26_intangible_ass;
		}

		public void setR26_intangible_ass(String r26_intangible_ass) {
			this.r26_intangible_ass = r26_intangible_ass;
		}

		public BigDecimal getR26_cost_rev() {
			return r26_cost_rev;
		}

		public void setR26_cost_rev(BigDecimal r26_cost_rev) {
			this.r26_cost_rev = r26_cost_rev;
		}

		public BigDecimal getR26_useful_life() {
			return r26_useful_life;
		}

		public void setR26_useful_life(BigDecimal r26_useful_life) {
			this.r26_useful_life = r26_useful_life;
		}

		public BigDecimal getR26_res_value() {
			return r26_res_value;
		}

		public void setR26_res_value(BigDecimal r26_res_value) {
			this.r26_res_value = r26_res_value;
		}

		public BigDecimal getR26_month_amort() {
			return r26_month_amort;
		}

		public void setR26_month_amort(BigDecimal r26_month_amort) {
			this.r26_month_amort = r26_month_amort;
		}

		public BigDecimal getR26_acc_amort_amt() {
			return r26_acc_amort_amt;
		}

		public void setR26_acc_amort_amt(BigDecimal r26_acc_amort_amt) {
			this.r26_acc_amort_amt = r26_acc_amort_amt;
		}

		public BigDecimal getR26_close_bal() {
			return r26_close_bal;
		}

		public void setR26_close_bal(BigDecimal r26_close_bal) {
			this.r26_close_bal = r26_close_bal;
		}

		public String getR27_intangible_ass() {
			return r27_intangible_ass;
		}

		public void setR27_intangible_ass(String r27_intangible_ass) {
			this.r27_intangible_ass = r27_intangible_ass;
		}

		public BigDecimal getR27_cost_rev() {
			return r27_cost_rev;
		}

		public void setR27_cost_rev(BigDecimal r27_cost_rev) {
			this.r27_cost_rev = r27_cost_rev;
		}

		public BigDecimal getR27_useful_life() {
			return r27_useful_life;
		}

		public void setR27_useful_life(BigDecimal r27_useful_life) {
			this.r27_useful_life = r27_useful_life;
		}

		public BigDecimal getR27_res_value() {
			return r27_res_value;
		}

		public void setR27_res_value(BigDecimal r27_res_value) {
			this.r27_res_value = r27_res_value;
		}

		public BigDecimal getR27_month_amort() {
			return r27_month_amort;
		}

		public void setR27_month_amort(BigDecimal r27_month_amort) {
			this.r27_month_amort = r27_month_amort;
		}

		public BigDecimal getR27_acc_amort_amt() {
			return r27_acc_amort_amt;
		}

		public void setR27_acc_amort_amt(BigDecimal r27_acc_amort_amt) {
			this.r27_acc_amort_amt = r27_acc_amort_amt;
		}

		public BigDecimal getR27_close_bal() {
			return r27_close_bal;
		}

		public void setR27_close_bal(BigDecimal r27_close_bal) {
			this.r27_close_bal = r27_close_bal;
		}

		public String getR28_intangible_ass() {
			return r28_intangible_ass;
		}

		public void setR28_intangible_ass(String r28_intangible_ass) {
			this.r28_intangible_ass = r28_intangible_ass;
		}

		public BigDecimal getR28_cost_rev() {
			return r28_cost_rev;
		}

		public void setR28_cost_rev(BigDecimal r28_cost_rev) {
			this.r28_cost_rev = r28_cost_rev;
		}

		public BigDecimal getR28_useful_life() {
			return r28_useful_life;
		}

		public void setR28_useful_life(BigDecimal r28_useful_life) {
			this.r28_useful_life = r28_useful_life;
		}

		public BigDecimal getR28_res_value() {
			return r28_res_value;
		}

		public void setR28_res_value(BigDecimal r28_res_value) {
			this.r28_res_value = r28_res_value;
		}

		public BigDecimal getR28_month_amort() {
			return r28_month_amort;
		}

		public void setR28_month_amort(BigDecimal r28_month_amort) {
			this.r28_month_amort = r28_month_amort;
		}

		public BigDecimal getR28_acc_amort_amt() {
			return r28_acc_amort_amt;
		}

		public void setR28_acc_amort_amt(BigDecimal r28_acc_amort_amt) {
			this.r28_acc_amort_amt = r28_acc_amort_amt;
		}

		public BigDecimal getR28_close_bal() {
			return r28_close_bal;
		}

		public void setR28_close_bal(BigDecimal r28_close_bal) {
			this.r28_close_bal = r28_close_bal;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
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

	}

	public static class M_FAS_Manual_Archival_Summary_Entity {

		/* ------------------- R12 --------------------- */
		@Column(name = "R12_FIX_ASS")
		private String r12_fix_ass;

		@Column(name = "R12_COST")
		private BigDecimal r12_cost;

		@Column(name = "R12_ADD")
		private BigDecimal r12_add;

		@Column(name = "R12_DISPOSALS")
		private BigDecimal r12_disposals;

		@Column(name = "R12_DEPRECIATION")
		private BigDecimal r12_depreciation;

		@Column(name = "R12_NET_BOOK_VALUE")
		private BigDecimal r12_net_book_value;

		/* ------------------- R23 --------------------- */
		@Column(name = "R23_INTANGIBLE_ASS")
		private String r23_intangible_ass;

		@Column(name = "R23_COST_REV")
		private BigDecimal r23_cost_rev;

		@Column(name = "R23_USEFUL_LIFE")
		private BigDecimal r23_useful_life;

		@Column(name = "R23_RES_VALUE")
		private BigDecimal r23_res_value;

		@Column(name = "R23_MONTH_AMORT")
		private BigDecimal r23_month_amort;

		@Column(name = "R23_ACC_AMORT_AMT")
		private BigDecimal r23_acc_amort_amt;

		@Column(name = "R23_CLOSE_BAL")
		private BigDecimal r23_close_bal;

		/* ------------------- R24 --------------------- */
		@Column(name = "R24_INTANGIBLE_ASS")
		private String r24_intangible_ass;

		@Column(name = "R24_COST_REV")
		private BigDecimal r24_cost_rev;

		@Column(name = "R24_USEFUL_LIFE")
		private BigDecimal r24_useful_life;

		@Column(name = "R24_RES_VALUE")
		private BigDecimal r24_res_value;

		@Column(name = "R24_MONTH_AMORT")
		private BigDecimal r24_month_amort;

		@Column(name = "R24_ACC_AMORT_AMT")
		private BigDecimal r24_acc_amort_amt;

		@Column(name = "R24_CLOSE_BAL")
		private BigDecimal r24_close_bal;

		/* ------------------- R25 --------------------- */
		@Column(name = "R25_INTANGIBLE_ASS")
		private String r25_intangible_ass;

		@Column(name = "R25_COST_REV")
		private BigDecimal r25_cost_rev;

		@Column(name = "R25_USEFUL_LIFE")
		private BigDecimal r25_useful_life;

		@Column(name = "R25_RES_VALUE")
		private BigDecimal r25_res_value;

		@Column(name = "R25_MONTH_AMORT")
		private BigDecimal r25_month_amort;

		@Column(name = "R25_ACC_AMORT_AMT")
		private BigDecimal r25_acc_amort_amt;

		@Column(name = "R25_CLOSE_BAL")
		private BigDecimal r25_close_bal;

		/* ------------------- R26 --------------------- */
		@Column(name = "R26_INTANGIBLE_ASS")
		private String r26_intangible_ass;

		@Column(name = "R26_COST_REV")
		private BigDecimal r26_cost_rev;

		@Column(name = "R26_USEFUL_LIFE")
		private BigDecimal r26_useful_life;

		@Column(name = "R26_RES_VALUE")
		private BigDecimal r26_res_value;

		@Column(name = "R26_MONTH_AMORT")
		private BigDecimal r26_month_amort;

		@Column(name = "R26_ACC_AMORT_AMT")
		private BigDecimal r26_acc_amort_amt;

		@Column(name = "R26_CLOSE_BAL")
		private BigDecimal r26_close_bal;

		/* ------------------- R27 --------------------- */
		@Column(name = "R27_INTANGIBLE_ASS")
		private String r27_intangible_ass;

		@Column(name = "R27_COST_REV")
		private BigDecimal r27_cost_rev;

		@Column(name = "R27_USEFUL_LIFE")
		private BigDecimal r27_useful_life;

		@Column(name = "R27_RES_VALUE")
		private BigDecimal r27_res_value;

		@Column(name = "R27_MONTH_AMORT")
		private BigDecimal r27_month_amort;

		@Column(name = "R27_ACC_AMORT_AMT")
		private BigDecimal r27_acc_amort_amt;

		@Column(name = "R27_CLOSE_BAL")
		private BigDecimal r27_close_bal;

		/* ------------------- R28 --------------------- */
		@Column(name = "R28_INTANGIBLE_ASS")
		private String r28_intangible_ass;

		@Column(name = "R28_COST_REV")
		private BigDecimal r28_cost_rev;

		@Column(name = "R28_USEFUL_LIFE")
		private BigDecimal r28_useful_life;

		@Column(name = "R28_RES_VALUE")
		private BigDecimal r28_res_value;

		@Column(name = "R28_MONTH_AMORT")
		private BigDecimal r28_month_amort;

		@Column(name = "R28_ACC_AMORT_AMT")
		private BigDecimal r28_acc_amort_amt;

		@Column(name = "R28_CLOSE_BAL")
		private BigDecimal r28_close_bal;

		/* ------------------- REPORT INFO --------------------- */
		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Column(name = "REPORT_DATE")
		private Date report_date;

		@Column(name = "REPORT_VERSION")
		private String report_version;

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

		public M_FAS_Manual_Archival_Summary_Entity() {
			super();
		}

		public String getR12_fix_ass() {
			return r12_fix_ass;
		}

		public void setR12_fix_ass(String r12_fix_ass) {
			this.r12_fix_ass = r12_fix_ass;
		}

		public BigDecimal getR12_cost() {
			return r12_cost;
		}

		public void setR12_cost(BigDecimal r12_cost) {
			this.r12_cost = r12_cost;
		}

		public BigDecimal getR12_add() {
			return r12_add;
		}

		public void setR12_add(BigDecimal r12_add) {
			this.r12_add = r12_add;
		}

		public BigDecimal getR12_disposals() {
			return r12_disposals;
		}

		public void setR12_disposals(BigDecimal r12_disposals) {
			this.r12_disposals = r12_disposals;
		}

		public BigDecimal getR12_depreciation() {
			return r12_depreciation;
		}

		public void setR12_depreciation(BigDecimal r12_depreciation) {
			this.r12_depreciation = r12_depreciation;
		}

		public BigDecimal getR12_net_book_value() {
			return r12_net_book_value;
		}

		public void setR12_net_book_value(BigDecimal r12_net_book_value) {
			this.r12_net_book_value = r12_net_book_value;
		}

		public String getR23_intangible_ass() {
			return r23_intangible_ass;
		}

		public void setR23_intangible_ass(String r23_intangible_ass) {
			this.r23_intangible_ass = r23_intangible_ass;
		}

		public BigDecimal getR23_cost_rev() {
			return r23_cost_rev;
		}

		public void setR23_cost_rev(BigDecimal r23_cost_rev) {
			this.r23_cost_rev = r23_cost_rev;
		}

		public BigDecimal getR23_useful_life() {
			return r23_useful_life;
		}

		public void setR23_useful_life(BigDecimal r23_useful_life) {
			this.r23_useful_life = r23_useful_life;
		}

		public BigDecimal getR23_res_value() {
			return r23_res_value;
		}

		public void setR23_res_value(BigDecimal r23_res_value) {
			this.r23_res_value = r23_res_value;
		}

		public BigDecimal getR23_month_amort() {
			return r23_month_amort;
		}

		public void setR23_month_amort(BigDecimal r23_month_amort) {
			this.r23_month_amort = r23_month_amort;
		}

		public BigDecimal getR23_acc_amort_amt() {
			return r23_acc_amort_amt;
		}

		public void setR23_acc_amort_amt(BigDecimal r23_acc_amort_amt) {
			this.r23_acc_amort_amt = r23_acc_amort_amt;
		}

		public BigDecimal getR23_close_bal() {
			return r23_close_bal;
		}

		public void setR23_close_bal(BigDecimal r23_close_bal) {
			this.r23_close_bal = r23_close_bal;
		}

		public String getR24_intangible_ass() {
			return r24_intangible_ass;
		}

		public void setR24_intangible_ass(String r24_intangible_ass) {
			this.r24_intangible_ass = r24_intangible_ass;
		}

		public BigDecimal getR24_cost_rev() {
			return r24_cost_rev;
		}

		public void setR24_cost_rev(BigDecimal r24_cost_rev) {
			this.r24_cost_rev = r24_cost_rev;
		}

		public BigDecimal getR24_useful_life() {
			return r24_useful_life;
		}

		public void setR24_useful_life(BigDecimal r24_useful_life) {
			this.r24_useful_life = r24_useful_life;
		}

		public BigDecimal getR24_res_value() {
			return r24_res_value;
		}

		public void setR24_res_value(BigDecimal r24_res_value) {
			this.r24_res_value = r24_res_value;
		}

		public BigDecimal getR24_month_amort() {
			return r24_month_amort;
		}

		public void setR24_month_amort(BigDecimal r24_month_amort) {
			this.r24_month_amort = r24_month_amort;
		}

		public BigDecimal getR24_acc_amort_amt() {
			return r24_acc_amort_amt;
		}

		public void setR24_acc_amort_amt(BigDecimal r24_acc_amort_amt) {
			this.r24_acc_amort_amt = r24_acc_amort_amt;
		}

		public BigDecimal getR24_close_bal() {
			return r24_close_bal;
		}

		public void setR24_close_bal(BigDecimal r24_close_bal) {
			this.r24_close_bal = r24_close_bal;
		}

		public String getR25_intangible_ass() {
			return r25_intangible_ass;
		}

		public void setR25_intangible_ass(String r25_intangible_ass) {
			this.r25_intangible_ass = r25_intangible_ass;
		}

		public BigDecimal getR25_cost_rev() {
			return r25_cost_rev;
		}

		public void setR25_cost_rev(BigDecimal r25_cost_rev) {
			this.r25_cost_rev = r25_cost_rev;
		}

		public BigDecimal getR25_useful_life() {
			return r25_useful_life;
		}

		public void setR25_useful_life(BigDecimal r25_useful_life) {
			this.r25_useful_life = r25_useful_life;
		}

		public BigDecimal getR25_res_value() {
			return r25_res_value;
		}

		public void setR25_res_value(BigDecimal r25_res_value) {
			this.r25_res_value = r25_res_value;
		}

		public BigDecimal getR25_month_amort() {
			return r25_month_amort;
		}

		public void setR25_month_amort(BigDecimal r25_month_amort) {
			this.r25_month_amort = r25_month_amort;
		}

		public BigDecimal getR25_acc_amort_amt() {
			return r25_acc_amort_amt;
		}

		public void setR25_acc_amort_amt(BigDecimal r25_acc_amort_amt) {
			this.r25_acc_amort_amt = r25_acc_amort_amt;
		}

		public BigDecimal getR25_close_bal() {
			return r25_close_bal;
		}

		public void setR25_close_bal(BigDecimal r25_close_bal) {
			this.r25_close_bal = r25_close_bal;
		}

		public String getR26_intangible_ass() {
			return r26_intangible_ass;
		}

		public void setR26_intangible_ass(String r26_intangible_ass) {
			this.r26_intangible_ass = r26_intangible_ass;
		}

		public BigDecimal getR26_cost_rev() {
			return r26_cost_rev;
		}

		public void setR26_cost_rev(BigDecimal r26_cost_rev) {
			this.r26_cost_rev = r26_cost_rev;
		}

		public BigDecimal getR26_useful_life() {
			return r26_useful_life;
		}

		public void setR26_useful_life(BigDecimal r26_useful_life) {
			this.r26_useful_life = r26_useful_life;
		}

		public BigDecimal getR26_res_value() {
			return r26_res_value;
		}

		public void setR26_res_value(BigDecimal r26_res_value) {
			this.r26_res_value = r26_res_value;
		}

		public BigDecimal getR26_month_amort() {
			return r26_month_amort;
		}

		public void setR26_month_amort(BigDecimal r26_month_amort) {
			this.r26_month_amort = r26_month_amort;
		}

		public BigDecimal getR26_acc_amort_amt() {
			return r26_acc_amort_amt;
		}

		public void setR26_acc_amort_amt(BigDecimal r26_acc_amort_amt) {
			this.r26_acc_amort_amt = r26_acc_amort_amt;
		}

		public BigDecimal getR26_close_bal() {
			return r26_close_bal;
		}

		public void setR26_close_bal(BigDecimal r26_close_bal) {
			this.r26_close_bal = r26_close_bal;
		}

		public String getR27_intangible_ass() {
			return r27_intangible_ass;
		}

		public void setR27_intangible_ass(String r27_intangible_ass) {
			this.r27_intangible_ass = r27_intangible_ass;
		}

		public BigDecimal getR27_cost_rev() {
			return r27_cost_rev;
		}

		public void setR27_cost_rev(BigDecimal r27_cost_rev) {
			this.r27_cost_rev = r27_cost_rev;
		}

		public BigDecimal getR27_useful_life() {
			return r27_useful_life;
		}

		public void setR27_useful_life(BigDecimal r27_useful_life) {
			this.r27_useful_life = r27_useful_life;
		}

		public BigDecimal getR27_res_value() {
			return r27_res_value;
		}

		public void setR27_res_value(BigDecimal r27_res_value) {
			this.r27_res_value = r27_res_value;
		}

		public BigDecimal getR27_month_amort() {
			return r27_month_amort;
		}

		public void setR27_month_amort(BigDecimal r27_month_amort) {
			this.r27_month_amort = r27_month_amort;
		}

		public BigDecimal getR27_acc_amort_amt() {
			return r27_acc_amort_amt;
		}

		public void setR27_acc_amort_amt(BigDecimal r27_acc_amort_amt) {
			this.r27_acc_amort_amt = r27_acc_amort_amt;
		}

		public BigDecimal getR27_close_bal() {
			return r27_close_bal;
		}

		public void setR27_close_bal(BigDecimal r27_close_bal) {
			this.r27_close_bal = r27_close_bal;
		}

		public String getR28_intangible_ass() {
			return r28_intangible_ass;
		}

		public void setR28_intangible_ass(String r28_intangible_ass) {
			this.r28_intangible_ass = r28_intangible_ass;
		}

		public BigDecimal getR28_cost_rev() {
			return r28_cost_rev;
		}

		public void setR28_cost_rev(BigDecimal r28_cost_rev) {
			this.r28_cost_rev = r28_cost_rev;
		}

		public BigDecimal getR28_useful_life() {
			return r28_useful_life;
		}

		public void setR28_useful_life(BigDecimal r28_useful_life) {
			this.r28_useful_life = r28_useful_life;
		}

		public BigDecimal getR28_res_value() {
			return r28_res_value;
		}

		public void setR28_res_value(BigDecimal r28_res_value) {
			this.r28_res_value = r28_res_value;
		}

		public BigDecimal getR28_month_amort() {
			return r28_month_amort;
		}

		public void setR28_month_amort(BigDecimal r28_month_amort) {
			this.r28_month_amort = r28_month_amort;
		}

		public BigDecimal getR28_acc_amort_amt() {
			return r28_acc_amort_amt;
		}

		public void setR28_acc_amort_amt(BigDecimal r28_acc_amort_amt) {
			this.r28_acc_amort_amt = r28_acc_amort_amt;
		}

		public BigDecimal getR28_close_bal() {
			return r28_close_bal;
		}

		public void setR28_close_bal(BigDecimal r28_close_bal) {
			this.r28_close_bal = r28_close_bal;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
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

	}

}