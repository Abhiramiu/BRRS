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

import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional
public class BRRS_M_MRC_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_MRC_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;


	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// =====================================================================
	// JDBC QUERY METHODS
	// =====================================================================

	public List<M_MRC_Summary_Entity> getSummaryByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_MRC_SUMMARYTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate}, new M_MRCSummaryRowMapper());
	}

	public List<M_MRC_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_MRC_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate, version}, new M_MRCArchivalSummaryRowMapper());
	}

	public List<M_MRC_Archival_Summary_Entity> getArchivalSummaryWithVersion() {
		String sql = "SELECT * FROM BRRS_M_MRC_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_MRCArchivalSummaryRowMapper());
	}

	public List<M_MRC_Detail_Entity> getDetailByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_MRC_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate}, new M_MRCDetailRowMapper());
	}

	public int getDetailCount(Date reportDate) {
		String sql = "SELECT COUNT(*) FROM BRRS_M_MRC_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{reportDate}, Integer.class);
	}

	public List<M_MRC_Detail_Entity> getDetailByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1, Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_MRC_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[]{reportLabel, reportAddlCriteria1, reportDate}, new M_MRCDetailRowMapper());
	}

	public M_MRC_Detail_Entity findDetailByAcctNumber(String acctNumber) {
		String sql = "SELECT * FROM BRRS_M_MRC_DETAILTABLE WHERE ACCT_NUMBER = ?";
		List<M_MRC_Detail_Entity> list = jdbcTemplate.query(sql, new Object[]{acctNumber}, new M_MRCDetailRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	public List<M_MRC_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, String version) {
		String sql = "SELECT * FROM BRRS_M_MRC_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate, version}, new M_MRCArchivalDetailRowMapper());
	}

	public List<M_MRC_Archival_Detail_Entity> getArchivalDetailByRowIdAndColumnId(
			String reportLabel, String reportAddlCriteria1, Date reportDate, String version) {
		String sql = "SELECT * FROM BRRS_M_MRC_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{reportLabel, reportAddlCriteria1, reportDate, version}, new M_MRCArchivalDetailRowMapper());
	}

	public int getArchivalDetailCount(Date reportDate, String version) {
		String sql = "SELECT COUNT(*) FROM BRRS_M_MRC_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{reportDate, version}, Integer.class);
	}

	// =====================================================================
	// JDBC WRITE METHODS
	// =====================================================================

	public void updateSummaryTotals(M_MRC_Summary_Entity existing) {
		String sql = "UPDATE BRRS_M_MRC_SUMMARYTABLE SET R33_TOTAL=?, R34_TOTAL=?, REPORT_VERSION=?, "
				+ "REPORT_CODE=?, REPORT_DESC=?, ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=? WHERE REPORT_DATE=?";
		jdbcTemplate.update(sql, existing.getR33_TOTAL(), existing.getR34_TOTAL(), existing.getReportVersion(),
				existing.getREPORT_CODE(), existing.getREPORT_DESC(), existing.getENTITY_FLG(),
				existing.getMODIFY_FLG(), existing.getDEL_FLG(), existing.getReportDate());
	}

	public void updateDetail(String acctNumber, String acctName, BigDecimal acctBalanceInpula) {
		String sql = "UPDATE BRRS_M_MRC_DETAILTABLE SET ACCT_NAME=?, ACCT_BALANCE_IN_PULA=? WHERE ACCT_NUMBER=?";
		jdbcTemplate.update(sql, acctName, acctBalanceInpula, acctNumber);
	}

	public ModelAndView getM_MRCview(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {
		ModelAndView mv = new ModelAndView();
		

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

//Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		System.out.println("testing");
		System.out.println(version);

		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_MRC_Archival_Summary_Entity> T2Master = new ArrayList<M_MRC_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				T2Master = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary1", T2Master);
		} else {
			List<M_MRC_Summary_Entity> T2Master = new ArrayList<M_MRC_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				T2Master = getSummaryByDate(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary1", T2Master);
		}

// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_MRC");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public void updateReport(M_MRC_Summary_Entity updatedEntity) {

		System.out.println("Came to services1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// Fetch existing record
		List<M_MRC_Summary_Entity> existingRows = getSummaryByDate(updatedEntity.getReportDate());
		if (existingRows.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate());
		}
		M_MRC_Summary_Entity existing = existingRows.get(0);

		try {

			// ===== UPDATE R-CODE FIELDS USING REFLECTION =====
			int[] rowCodes = { 33 ,34 }; // Add more codes if needed
			String[] fields = { "TOTAL" }; // Add more fields if required

			for (int code : rowCodes) {

				String prefix = "R" + code + "_";

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_MRC_Summary_Entity.class.getMethod(getterName);
						Method setter = M_MRC_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						if (newValue != null) {
							setter.invoke(existing, newValue);
						}

					} catch (NoSuchMethodException ignore) {
						// Field does not exist → skip
					}
				}
			}

			// ===== UPDATE METADATA FIELDS =====
			existing.setReportVersion(updatedEntity.getReportVersion());
			existing.setREPORT_CODE(updatedEntity.getREPORT_CODE());
			existing.setREPORT_DESC(updatedEntity.getREPORT_DESC());
			existing.setENTITY_FLG(updatedEntity.getENTITY_FLG());
			existing.setMODIFY_FLG(updatedEntity.getMODIFY_FLG());
			existing.setDEL_FLG(updatedEntity.getDEL_FLG());

		} catch (Exception e) {
			throw new RuntimeException("❌ Error while updating MRC Summary fields", e);
		}

		// ===== SAVE CHANGES =====
		updateSummaryTotals(existing);

		System.out.println("✅ MRC Summary updated successfully");
	}

	public ModelAndView getM_MRCcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version,HttpServletRequest req1,Model md) {

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

				List<M_MRC_Archival_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = getArchivalDetailByRowIdAndColumnId(reportLable, reportAddlCriteria_1,
							parsedDate, version);
				} else {
					T1Dt1 = getArchivalDetailByDateAndVersion(parsedDate, version);

					totalPages = getArchivalDetailCount(parsedDate, version);

					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);

				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			} else {
				// 🔹 Current branch
				List<M_MRC_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = getDetailByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = getDetailByDate(parsedDate);
					totalPages = getDetailCount(parsedDate);
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

		mv.setViewName("BRRS/M_MRC");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public byte[] getM_MRCDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_MRC Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SECADetail");

//Common border style
			BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
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

//Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE",
					"REPORT ADDL CRITERIA 1", "REPORT_DATE" };

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

//Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_MRC_Detail_Entity> reportData = getDetailByDate(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_MRC_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

//ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportDate());
					row.createCell(5).setCellValue(item.getReportName());
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
				logger.info("No data found for M_MRC — only header will be written.");
			}

//Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_MRC Excel", e);
			return new byte[0];
		}
	}

//	public byte[] BRRS_M_SECADetailExcel(String filename, String fromdate, String todate, String currency,
//										   String dtltype, String type, String version) {
//
//		try {
//			logger.info("Generating Excel for BRRS_M_SECA Details...");
//			System.out.println("came to Detail download service");
//			if (type.equals("ARCHIVAL") & version != null) {
//				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
//						version);
//				return ARCHIVALreport;
//			}
//			XSSFWorkbook workbook = new XSSFWorkbook();
//			XSSFSheet sheet = workbook.createSheet("BRRS_M_SECADetails");
//
//			// Common border style
//			BorderStyle border = BorderStyle.THIN;
//			// Header style (left aligned)
//			CellStyle headerStyle = workbook.createCellStyle();
//			Font headerFont = workbook.createFont();
//			headerFont.setBold(true);
//			headerFont.setFontHeightInPoints((short) 10);
//			headerStyle.setFont(headerFont);
//			headerStyle.setAlignment(HorizontalAlignment.LEFT);
//			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//			headerStyle.setBorderTop(border);
//			headerStyle.setBorderBottom(border);
//			headerStyle.setBorderLeft(border);
//			headerStyle.setBorderRight(border);
//
//			// Right-aligned header style for ACCT BALANCE
//			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
//			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
//			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
//
//			// Default data style (left aligned)
//			CellStyle dataStyle = workbook.createCellStyle();
//			dataStyle.setAlignment(HorizontalAlignment.LEFT);
//			dataStyle.setBorderTop(border);
//			dataStyle.setBorderBottom(border);
//			dataStyle.setBorderLeft(border);
//			dataStyle.setBorderRight(border);
//
//			// ACCT BALANCE style (right aligned with 3 decimals)
//			CellStyle balanceStyle = workbook.createCellStyle();
//			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
//			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
//			balanceStyle.setBorderTop(border);
//			balanceStyle.setBorderBottom(border);
//			balanceStyle.setBorderLeft(border);
//			balanceStyle.setBorderRight(border);
//			// Header row
//			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "reportLable", "reportAddlCriteria1",
//					"REPORT_DATE" };
//			XSSFRow headerRow = sheet.createRow(0);
//			for (int i = 0; i < headers.length; i++) {
//				Cell cell = headerRow.createCell(i);
//				cell.setCellValue(headers[i]);
//				if (i == 3) { // ACCT BALANCE
//					cell.setCellStyle(rightAlignedHeaderStyle);
//				} else {
//					cell.setCellStyle(headerStyle);
//				}
//				sheet.setColumnWidth(i, 5000);
//			}
//			// Get data
//			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
//			List<BRRS_M_SECA_Detail_Entity> reportData = BRRS_M_SECA_Detail_Repo.getdatabydateList(parsedToDate);
//			if (reportData != null && !reportData.isEmpty()) {
//				int rowIndex = 1;
//				for (BRRS_M_SECA_Detail_Entity item : reportData) {
//					XSSFRow row = sheet.createRow(rowIndex++);
//					row.createCell(0).setCellValue(item.getCUST_ID());
//					row.createCell(1).setCellValue(item.getACCT_NUMBER());
//					row.createCell(2).setCellValue(item.getACCT_NAME());
//					// ACCT BALANCE (right aligned, 3 decimal places)
//					Cell balanceCell = row.createCell(3);
//					if (item.getACCT_BALANCE_IN_PULA() != null) {
//						balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
//					} else {
//						balanceCell.setCellValue(0.000);
//					}
//					balanceCell.setCellStyle(balanceStyle);
//					row.createCell(4).setCellValue(item.getROW_ID());
//					row.createCell(5).setCellValue(item.getCOLUMN_ID());
//					row.createCell(6)
//							.setCellValue(item.getREPORT_DATE() != null
//									? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
//									: "");
//					// Apply data style for all other cells
//					for (int j = 0; j < 7; j++) {
//						if (j != 3) {
//							row.getCell(j).setCellStyle(dataStyle);
//						}
//					}
//				}
//			} else {
//				logger.info("No data found for BRRS_M_SECA — only header will be written.");
//			}
//			// Write to byte[]
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			workbook.write(bos);
//			workbook.close();
//			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
//			return bos.toByteArray();
//		} catch (Exception e) {
//			logger.error("Error generating BRRS_M_SECA Excel", e);
//			return new byte[0];
//		}
//	}
	public List<Object[]> getM_MRCResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_MRC_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_MRC_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_MRC Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

//	// Archival View
	public List<Object[]> getM_MRCArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_MRC_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_MRC_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_MRC_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_MRC  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_MRC ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SECADetail");

//Common border style
			BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

//Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE",
					"REPORT ADDL CRITERIA 1", "REPORT_DATE" };

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

//Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_MRC_Archival_Detail_Entity> reportData = getArchivalDetailByDateAndVersion(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_MRC_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

//ACCT BALANCE (right aligned, 3 decimal places with comma separator)
					Cell balanceCell = row.createCell(3);

					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}

//Create style with thousand separator and decimal point
					DataFormat format = workbook.createDataFormat();

//Format: 1,234,567
					balanceStyle.setDataFormat(format.getFormat("#,##0"));

//Right alignment (optional)
					balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportDate());
					row.createCell(5).setCellValue(item.getReportName());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

//Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_MRC — only header will be written.");
			}
//Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_MRC Excel", e);
			return new byte[0];
		}
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_MRC"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			M_MRC_Detail_Entity mrcEntity = findDetailByAcctNumber(acctNo);
			if (mrcEntity != null && mrcEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(mrcEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", mrcEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	public ModelAndView updateDetailEdit(String acctNo, String formMode) {

	    ModelAndView mv = new ModelAndView("BRRS/M_MRC");

	    M_MRC_Detail_Entity mrcEntity;

	    if (acctNo != null) {
	        mrcEntity = findDetailByAcctNumber(acctNo);
	    } else {
	        mrcEntity = new M_MRC_Detail_Entity(); // empty object
	    }

	    mv.addObject("Data", mrcEntity);

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");

	    return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_MRC_Detail_Entity existing = findDetailByAcctNumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

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
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInpula(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (isChanged) {
				updateDetail(existing.getAcctNumber(), existing.getAcctName(), existing.getAcctBalanceInpula());
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_MRC_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_MRC_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_MRC record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// Normal format Excel

	public byte[] BRRS_M_MRCExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_MRCARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
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
//				return BRRS_M_MRCResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
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
				return BRRS_M_MRCEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_MRC_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_MRC report. Returning empty result.");
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
							M_MRC_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
//NORMAL
							Cell R12Cell = row.createCell(2);

							if (record.getReportDate() != null) {

								R12Cell.setCellValue(record.getReportDate());

								R12Cell.setCellStyle(dateStyle);

							} else {

								R12Cell.setCellValue("");

								R12Cell.setCellStyle(textStyle);
							}
							row = sheet.getRow(8);

							// row9
							// Column C
							Cell cell2 = row.getCell(2);
							if (record.getR9_TOTAL() != null) {
								cell2.setCellValue(record.getR9_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row11
							row = sheet.getRow(10);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR11_TOTAL() != null) {
								cell2.setCellValue(record.getR11_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR12_TOTAL() != null) {
								cell2.setCellValue(record.getR12_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR13_TOTAL() != null) {
								cell2.setCellValue(record.getR13_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR14_TOTAL() != null) {
								cell2.setCellValue(record.getR14_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR17_TOTAL() != null) {
								cell2.setCellValue(record.getR17_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR18_TOTAL() != null) {
								cell2.setCellValue(record.getR18_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR19_TOTAL() != null) {
								cell2.setCellValue(record.getR19_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR20_TOTAL() != null) {
								cell2.setCellValue(record.getR20_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR23_TOTAL() != null) {
								cell2.setCellValue(record.getR23_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR24_TOTAL() != null) {
								cell2.setCellValue(record.getR24_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR25_TOTAL() != null) {
								cell2.setCellValue(record.getR25_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row28
							row = sheet.getRow(27);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR28_TOTAL() != null) {
								cell2.setCellValue(record.getR28_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row29
							row = sheet.getRow(28);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR29_TOTAL() != null) {
								cell2.setCellValue(record.getR29_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row30
							row = sheet.getRow(29);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR30_TOTAL() != null) {
								cell2.setCellValue(record.getR30_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							// row33
							row = sheet.getRow(32);
							// Column C
							cell2 = row.createCell(2);
							if (record.getR33_TOTAL() != null) {
								cell2.setCellValue(record.getR33_TOTAL().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_MRC SUMMARY", null, "M_MRC_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_MRCEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_MRCEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
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
//				return BRRS_M_MRCResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
		} else {
			List<M_MRC_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_MRC report. Returning empty result.");
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
						M_MRC_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL
						Cell R12Cell = row.createCell(2);

						if (record.getReportDate() != null) {

							R12Cell.setCellValue(record.getReportDate());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(8);

						// row9
						// Column C
						Cell cell2 = row.getCell(2);
						if (record.getR9_TOTAL() != null) {
							cell2.setCellValue(record.getR9_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row11
						row = sheet.getRow(10);
						// Column C
						cell2 = row.getCell(2);
						if (record.getR11_TOTAL() != null) {
							cell2.setCellValue(record.getR11_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR12_TOTAL() != null) {
							cell2.setCellValue(record.getR12_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR13_TOTAL() != null) {
							cell2.setCellValue(record.getR13_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row14
						row = sheet.getRow(13);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR14_TOTAL() != null) {
							cell2.setCellValue(record.getR14_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR17_TOTAL() != null) {
							cell2.setCellValue(record.getR17_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR18_TOTAL() != null) {
							cell2.setCellValue(record.getR18_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row19
						row = sheet.getRow(18);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR19_TOTAL() != null) {
							cell2.setCellValue(record.getR19_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR20_TOTAL() != null) {
							cell2.setCellValue(record.getR20_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row23
						row = sheet.getRow(22);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR23_TOTAL() != null) {
							cell2.setCellValue(record.getR23_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row24
						row = sheet.getRow(23);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR24_TOTAL() != null) {
							cell2.setCellValue(record.getR24_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row25
						row = sheet.getRow(24);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR25_TOTAL() != null) {
							cell2.setCellValue(record.getR25_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row28
						row = sheet.getRow(27);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR28_TOTAL() != null) {
							cell2.setCellValue(record.getR28_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row29
						row = sheet.getRow(28);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR29_TOTAL() != null) {
							cell2.setCellValue(record.getR29_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row30
						row = sheet.getRow(29);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR30_TOTAL() != null) {
							cell2.setCellValue(record.getR30_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						// row33
						row = sheet.getRow(32);
						// Column C
						cell2 = row.createCell(2);
						if (record.getR33_TOTAL() != null) {
							cell2.setCellValue(record.getR33_TOTAL().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_MRC EMAIL SUMMARY", null, "M_MRC_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_MRCARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_MRCEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_MRC_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_MRC report. Returning empty result.");
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
					M_MRC_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL
					Cell R12Cell = row.createCell(2);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);

					// row9
					// Column C
					Cell cell2 = row.getCell(2);
					if (record.getR9_TOTAL() != null) {
						cell2.setCellValue(record.getR9_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR11_TOTAL() != null) {
						cell2.setCellValue(record.getR11_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR12_TOTAL() != null) {
						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_TOTAL() != null) {
						cell2.setCellValue(record.getR13_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR14_TOTAL() != null) {
						cell2.setCellValue(record.getR14_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_TOTAL() != null) {
						cell2.setCellValue(record.getR17_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_TOTAL() != null) {
						cell2.setCellValue(record.getR18_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_TOTAL() != null) {
						cell2.setCellValue(record.getR19_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_TOTAL() != null) {
						cell2.setCellValue(record.getR20_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_TOTAL() != null) {
						cell2.setCellValue(record.getR23_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_TOTAL() != null) {
						cell2.setCellValue(record.getR24_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_TOTAL() != null) {
						cell2.setCellValue(record.getR25_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_TOTAL() != null) {
						cell2.setCellValue(record.getR28_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR29_TOTAL() != null) {
						cell2.setCellValue(record.getR29_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR30_TOTAL() != null) {
						cell2.setCellValue(record.getR30_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR33_TOTAL() != null) {
						cell2.setCellValue(record.getR33_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_MRC ARCHIVAL SUMMARY", null, "M_MRC_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_MRCEmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_MRC_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_MRC report. Returning empty result.");
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
					M_MRC_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL
					Cell R12Cell = row.createCell(2);

					if (record.getReportDate() != null) {

						R12Cell.setCellValue(record.getReportDate());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);

					// row9
					// Column C
					Cell cell2 = row.getCell(2);
					if (record.getR9_TOTAL() != null) {
						cell2.setCellValue(record.getR9_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR11_TOTAL() != null) {
						cell2.setCellValue(record.getR11_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR12_TOTAL() != null) {
						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_TOTAL() != null) {
						cell2.setCellValue(record.getR13_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR14_TOTAL() != null) {
						cell2.setCellValue(record.getR14_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR17_TOTAL() != null) {
						cell2.setCellValue(record.getR17_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR18_TOTAL() != null) {
						cell2.setCellValue(record.getR18_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_TOTAL() != null) {
						cell2.setCellValue(record.getR19_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_TOTAL() != null) {
						cell2.setCellValue(record.getR20_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_TOTAL() != null) {
						cell2.setCellValue(record.getR23_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_TOTAL() != null) {
						cell2.setCellValue(record.getR24_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR25_TOTAL() != null) {
						cell2.setCellValue(record.getR25_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_TOTAL() != null) {
						cell2.setCellValue(record.getR28_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR29_TOTAL() != null) {
						cell2.setCellValue(record.getR29_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR30_TOTAL() != null) {
						cell2.setCellValue(record.getR30_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR33_TOTAL() != null) {
						cell2.setCellValue(record.getR33_TOTAL().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_MRC EMAIL ARCHIVAL SUMMARY", null, "M_MRC_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

//	// Resub Format excel
//	public byte[] BRRS_M_MRCResubExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");
//
//		if ("email".equalsIgnoreCase(format) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_MRCResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
//		}
//
//		List<M_MRC_Resub_Summary_Entity> dataList = brrs_M_MRC_resub_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for M_MRC report. Returning empty result.");
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
//			int startRow = 5;
//
//			if (!dataList.isEmpty()) {
//				for (int i = 0; i < dataList.size(); i++) {
//
//					M_MRC_Resub_Summary_Entity record = dataList.get(i);
//					System.out.println("rownumber=" + startRow + i);
//					System.out.println("rownumber=" + startRow + i);
//					Row row = sheet.getRow(startRow + i);
//					if (row == null) {
//						row = sheet.createRow(startRow + i);
//					}
////NORMAL
//				
//
//					//row9
//					// Column C
//					Cell cell2 = row.getCell(2);
//					if (record.getR9_TOTAL() != null) {
//						cell2.setCellValue(record.getR9_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					
//					//row11
//					row = sheet.getRow(10);
//					// Column C
//					cell2 = row.getCell(2);
//					if (record.getR11_TOTAL() != null) {
//						cell2.setCellValue(record.getR11_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row12
//					row = sheet.getRow(11);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR12_TOTAL() != null) {
//						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row13
//					row = sheet.getRow(12);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR13_TOTAL() != null) {
//						cell2.setCellValue(record.getR13_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row14
//					row = sheet.getRow(13);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR14_TOTAL() != null) {
//						cell2.setCellValue(record.getR14_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row17
//					row = sheet.getRow(16);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR17_TOTAL() != null) {
//						cell2.setCellValue(record.getR17_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row18
//					row = sheet.getRow(17);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR18_TOTAL() != null) {
//						cell2.setCellValue(record.getR18_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row19
//					row = sheet.getRow(18);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR19_TOTAL() != null) {
//						cell2.setCellValue(record.getR19_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row20
//					row = sheet.getRow(19);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR20_TOTAL() != null) {
//						cell2.setCellValue(record.getR20_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row23
//					row = sheet.getRow(22);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR23_TOTAL() != null) {
//						cell2.setCellValue(record.getR23_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row24
//					row = sheet.getRow(23);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR24_TOTAL() != null) {
//						cell2.setCellValue(record.getR24_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row25
//					row = sheet.getRow(24);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR25_TOTAL() != null) {
//						cell2.setCellValue(record.getR25_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row28
//					row = sheet.getRow(27);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR28_TOTAL() != null) {
//						cell2.setCellValue(record.getR28_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row29
//					row = sheet.getRow(28);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR29_TOTAL() != null) {
//						cell2.setCellValue(record.getR29_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row30
//					row = sheet.getRow(29);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR30_TOTAL() != null) {
//						cell2.setCellValue(record.getR30_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row33
//					row = sheet.getRow(32);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR33_TOTAL() != null) {
//						cell2.setCellValue(record.getR33_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
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
//	public byte[] BRRS_M_MRCResubEmailExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Archival Email Excel generation process in memory.");
//
//		List<M_MRC_Resub_Summary_Entity> dataList = brrs_M_MRC_resub_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for BRRS_M_MRC report. Returning empty result.");
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
//			int startRow = 5;
//
//			if (!dataList.isEmpty()) {
//				for (int i = 0; i < dataList.size(); i++) {
//					M_MRC_Resub_Summary_Entity record = dataList.get(i);
//					System.out.println("rownumber=" + startRow + i);
//					Row row = sheet.getRow(startRow + i);
//					if (row == null) {
//						row = sheet.createRow(startRow + i);
//					}
////EMAIL
//
//
//					//row9
//					// Column C
//					Cell cell2 = row.getCell(2);
//					if (record.getR9_TOTAL() != null) {
//						cell2.setCellValue(record.getR9_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					
//					//row11
//					row = sheet.getRow(10);
//					// Column C
//					cell2 = row.getCell(2);
//					if (record.getR11_TOTAL() != null) {
//						cell2.setCellValue(record.getR11_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row12
//					row = sheet.getRow(11);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR12_TOTAL() != null) {
//						cell2.setCellValue(record.getR12_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row13
//					row = sheet.getRow(12);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR13_TOTAL() != null) {
//						cell2.setCellValue(record.getR13_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row14
//					row = sheet.getRow(13);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR14_TOTAL() != null) {
//						cell2.setCellValue(record.getR14_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row17
//					row = sheet.getRow(16);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR17_TOTAL() != null) {
//						cell2.setCellValue(record.getR17_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row18
//					row = sheet.getRow(17);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR18_TOTAL() != null) {
//						cell2.setCellValue(record.getR18_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row19
//					row = sheet.getRow(18);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR19_TOTAL() != null) {
//						cell2.setCellValue(record.getR19_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row20
//					row = sheet.getRow(19);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR20_TOTAL() != null) {
//						cell2.setCellValue(record.getR20_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row23
//					row = sheet.getRow(22);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR23_TOTAL() != null) {
//						cell2.setCellValue(record.getR23_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row24
//					row = sheet.getRow(23);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR24_TOTAL() != null) {
//						cell2.setCellValue(record.getR24_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row25
//					row = sheet.getRow(24);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR25_TOTAL() != null) {
//						cell2.setCellValue(record.getR25_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row28
//					row = sheet.getRow(27);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR28_TOTAL() != null) {
//						cell2.setCellValue(record.getR28_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row29
//					row = sheet.getRow(28);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR29_TOTAL() != null) {
//						cell2.setCellValue(record.getR29_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row30
//					row = sheet.getRow(29);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR30_TOTAL() != null) {
//						cell2.setCellValue(record.getR30_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
//					
//					//row33
//					row = sheet.getRow(32);
//					// Column C
//					cell2 = row.createCell(2);
//					if (record.getR33_TOTAL() != null) {
//						cell2.setCellValue(record.getR33_TOTAL().doubleValue());
//						cell2.setCellStyle(numberStyle);
//					} else {
//						cell2.setCellValue("");
//						cell2.setCellStyle(textStyle);
//					}
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
//	}

	// =====================================================================
	// ROW MAPPER — M_MRC_Summary_Entity
	// =====================================================================
	class M_MRCSummaryRowMapper implements RowMapper<M_MRC_Summary_Entity> {
		@Override
		public M_MRC_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_MRC_Summary_Entity obj = new M_MRC_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setR9_LINE(rs.getBigDecimal("R9_LINE"));
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));
			obj.setR10_LINE(rs.getBigDecimal("R10_LINE"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));
			obj.setR11_LINE(rs.getBigDecimal("R11_LINE"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));
			obj.setR12_LINE(rs.getBigDecimal("R12_LINE"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));
			obj.setR13_LINE(rs.getBigDecimal("R13_LINE"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));
			obj.setR14_LINE(rs.getBigDecimal("R14_LINE"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));
			obj.setR15_LINE(rs.getBigDecimal("R15_LINE"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));
			obj.setR16_LINE(rs.getBigDecimal("R16_LINE"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));
			obj.setR17_LINE(rs.getBigDecimal("R17_LINE"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));
			obj.setR18_LINE(rs.getBigDecimal("R18_LINE"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));
			obj.setR19_LINE(rs.getBigDecimal("R19_LINE"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));
			obj.setR20_LINE(rs.getBigDecimal("R20_LINE"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));
			obj.setR21_LINE(rs.getBigDecimal("R21_LINE"));
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));
			obj.setR22_LINE(rs.getBigDecimal("R22_LINE"));
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));
			obj.setR23_LINE(rs.getBigDecimal("R23_LINE"));
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));
			obj.setR24_LINE(rs.getBigDecimal("R24_LINE"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));
			obj.setR25_LINE(rs.getBigDecimal("R25_LINE"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));
			obj.setR26_LINE(rs.getBigDecimal("R26_LINE"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));
			obj.setR27_LINE(rs.getBigDecimal("R27_LINE"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));
			obj.setR28_LINE(rs.getBigDecimal("R28_LINE"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));
			obj.setR29_LINE(rs.getBigDecimal("R29_LINE"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_TOTAL(rs.getBigDecimal("R29_TOTAL"));
			obj.setR30_LINE(rs.getBigDecimal("R30_LINE"));
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_TOTAL(rs.getBigDecimal("R30_TOTAL"));
			obj.setR31_LINE(rs.getBigDecimal("R31_LINE"));
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_TOTAL(rs.getBigDecimal("R31_TOTAL"));
			obj.setR32_LINE(rs.getBigDecimal("R32_LINE"));
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_TOTAL(rs.getBigDecimal("R32_TOTAL"));
			obj.setR33_LINE(rs.getBigDecimal("R33_LINE"));
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_TOTAL(rs.getBigDecimal("R33_TOTAL"));
			obj.setR34_LINE(rs.getBigDecimal("R34_LINE"));
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_TOTAL(rs.getBigDecimal("R34_TOTAL"));
			obj.setR35_LINE(rs.getBigDecimal("R35_LINE"));
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_TOTAL(rs.getBigDecimal("R35_TOTAL"));
			obj.setR36_LINE(rs.getBigDecimal("R36_LINE"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_TOTAL(rs.getBigDecimal("R36_TOTAL"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// =====================================================================
	// ENTITY CLASS — M_MRC_Summary_Entity
	// =====================================================================
	public static class M_MRC_Summary_Entity {
		private BigDecimal R9_LINE;
		private String R9_PRODUCT;
		private BigDecimal R9_TOTAL;
		private BigDecimal R10_LINE;
		private String R10_PRODUCT;
		private BigDecimal R10_TOTAL;
		private BigDecimal R11_LINE;
		private String R11_PRODUCT;
		private BigDecimal R11_TOTAL;
		private BigDecimal R12_LINE;
		private String R12_PRODUCT;
		private BigDecimal R12_TOTAL;
		private BigDecimal R13_LINE;
		private String R13_PRODUCT;
		private BigDecimal R13_TOTAL;
		private BigDecimal R14_LINE;
		private String R14_PRODUCT;
		private BigDecimal R14_TOTAL;
		private BigDecimal R15_LINE;
		private String R15_PRODUCT;
		private BigDecimal R15_TOTAL;
		private BigDecimal R16_LINE;
		private String R16_PRODUCT;
		private BigDecimal R16_TOTAL;
		private BigDecimal R17_LINE;
		private String R17_PRODUCT;
		private BigDecimal R17_TOTAL;
		private BigDecimal R18_LINE;
		private String R18_PRODUCT;
		private BigDecimal R18_TOTAL;
		private BigDecimal R19_LINE;
		private String R19_PRODUCT;
		private BigDecimal R19_TOTAL;
		private BigDecimal R20_LINE;
		private String R20_PRODUCT;
		private BigDecimal R20_TOTAL;
		private BigDecimal R21_LINE;
		private String R21_PRODUCT;
		private BigDecimal R21_TOTAL;
		private BigDecimal R22_LINE;
		private String R22_PRODUCT;
		private BigDecimal R22_TOTAL;
		private BigDecimal R23_LINE;
		private String R23_PRODUCT;
		private BigDecimal R23_TOTAL;
		private BigDecimal R24_LINE;
		private String R24_PRODUCT;
		private BigDecimal R24_TOTAL;
		private BigDecimal R25_LINE;
		private String R25_PRODUCT;
		private BigDecimal R25_TOTAL;
		private BigDecimal R26_LINE;
		private String R26_PRODUCT;
		private BigDecimal R26_TOTAL;
		private BigDecimal R27_LINE;
		private String R27_PRODUCT;
		private BigDecimal R27_TOTAL;
		private BigDecimal R28_LINE;
		private String R28_PRODUCT;
		private BigDecimal R28_TOTAL;
		private BigDecimal R29_LINE;
		private String R29_PRODUCT;
		private BigDecimal R29_TOTAL;
		private BigDecimal R30_LINE;
		private String R30_PRODUCT;
		private BigDecimal R30_TOTAL;
		private BigDecimal R31_LINE;
		private String R31_PRODUCT;
		private BigDecimal R31_TOTAL;
		private BigDecimal R32_LINE;
		private String R32_PRODUCT;
		private BigDecimal R32_TOTAL;
		private BigDecimal R33_LINE;
		private String R33_PRODUCT;
		private BigDecimal R33_TOTAL;
		private BigDecimal R34_LINE;
		private String R34_PRODUCT;
		private BigDecimal R34_TOTAL;
		private BigDecimal R35_LINE;
		private String R35_PRODUCT;
		private BigDecimal R35_TOTAL;
		private BigDecimal R36_LINE;
		private String R36_PRODUCT;
		private BigDecimal R36_TOTAL;
		private Date reportDate;
		private BigDecimal reportVersion;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date v) { reportDate = v; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal v) { reportVersion = v; }
		public BigDecimal getR9_LINE() { return R9_LINE; }
		public void setR9_LINE(BigDecimal v) { R9_LINE = v; }
		public String getR9_PRODUCT() { return R9_PRODUCT; }
		public void setR9_PRODUCT(String v) { R9_PRODUCT = v; }
		public BigDecimal getR9_TOTAL() { return R9_TOTAL; }
		public void setR9_TOTAL(BigDecimal v) { R9_TOTAL = v; }
		public BigDecimal getR10_LINE() { return R10_LINE; }
		public void setR10_LINE(BigDecimal v) { R10_LINE = v; }
		public String getR10_PRODUCT() { return R10_PRODUCT; }
		public void setR10_PRODUCT(String v) { R10_PRODUCT = v; }
		public BigDecimal getR10_TOTAL() { return R10_TOTAL; }
		public void setR10_TOTAL(BigDecimal v) { R10_TOTAL = v; }
		public BigDecimal getR11_LINE() { return R11_LINE; }
		public void setR11_LINE(BigDecimal v) { R11_LINE = v; }
		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String v) { R11_PRODUCT = v; }
		public BigDecimal getR11_TOTAL() { return R11_TOTAL; }
		public void setR11_TOTAL(BigDecimal v) { R11_TOTAL = v; }
		public BigDecimal getR12_LINE() { return R12_LINE; }
		public void setR12_LINE(BigDecimal v) { R12_LINE = v; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String v) { R12_PRODUCT = v; }
		public BigDecimal getR12_TOTAL() { return R12_TOTAL; }
		public void setR12_TOTAL(BigDecimal v) { R12_TOTAL = v; }
		public BigDecimal getR13_LINE() { return R13_LINE; }
		public void setR13_LINE(BigDecimal v) { R13_LINE = v; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String v) { R13_PRODUCT = v; }
		public BigDecimal getR13_TOTAL() { return R13_TOTAL; }
		public void setR13_TOTAL(BigDecimal v) { R13_TOTAL = v; }
		public BigDecimal getR14_LINE() { return R14_LINE; }
		public void setR14_LINE(BigDecimal v) { R14_LINE = v; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String v) { R14_PRODUCT = v; }
		public BigDecimal getR14_TOTAL() { return R14_TOTAL; }
		public void setR14_TOTAL(BigDecimal v) { R14_TOTAL = v; }
		public BigDecimal getR15_LINE() { return R15_LINE; }
		public void setR15_LINE(BigDecimal v) { R15_LINE = v; }
		public String getR15_PRODUCT() { return R15_PRODUCT; }
		public void setR15_PRODUCT(String v) { R15_PRODUCT = v; }
		public BigDecimal getR15_TOTAL() { return R15_TOTAL; }
		public void setR15_TOTAL(BigDecimal v) { R15_TOTAL = v; }
		public BigDecimal getR16_LINE() { return R16_LINE; }
		public void setR16_LINE(BigDecimal v) { R16_LINE = v; }
		public String getR16_PRODUCT() { return R16_PRODUCT; }
		public void setR16_PRODUCT(String v) { R16_PRODUCT = v; }
		public BigDecimal getR16_TOTAL() { return R16_TOTAL; }
		public void setR16_TOTAL(BigDecimal v) { R16_TOTAL = v; }
		public BigDecimal getR17_LINE() { return R17_LINE; }
		public void setR17_LINE(BigDecimal v) { R17_LINE = v; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String v) { R17_PRODUCT = v; }
		public BigDecimal getR17_TOTAL() { return R17_TOTAL; }
		public void setR17_TOTAL(BigDecimal v) { R17_TOTAL = v; }
		public BigDecimal getR18_LINE() { return R18_LINE; }
		public void setR18_LINE(BigDecimal v) { R18_LINE = v; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String v) { R18_PRODUCT = v; }
		public BigDecimal getR18_TOTAL() { return R18_TOTAL; }
		public void setR18_TOTAL(BigDecimal v) { R18_TOTAL = v; }
		public BigDecimal getR19_LINE() { return R19_LINE; }
		public void setR19_LINE(BigDecimal v) { R19_LINE = v; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String v) { R19_PRODUCT = v; }
		public BigDecimal getR19_TOTAL() { return R19_TOTAL; }
		public void setR19_TOTAL(BigDecimal v) { R19_TOTAL = v; }
		public BigDecimal getR20_LINE() { return R20_LINE; }
		public void setR20_LINE(BigDecimal v) { R20_LINE = v; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String v) { R20_PRODUCT = v; }
		public BigDecimal getR20_TOTAL() { return R20_TOTAL; }
		public void setR20_TOTAL(BigDecimal v) { R20_TOTAL = v; }
		public BigDecimal getR21_LINE() { return R21_LINE; }
		public void setR21_LINE(BigDecimal v) { R21_LINE = v; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String v) { R21_PRODUCT = v; }
		public BigDecimal getR21_TOTAL() { return R21_TOTAL; }
		public void setR21_TOTAL(BigDecimal v) { R21_TOTAL = v; }
		public BigDecimal getR22_LINE() { return R22_LINE; }
		public void setR22_LINE(BigDecimal v) { R22_LINE = v; }
		public String getR22_PRODUCT() { return R22_PRODUCT; }
		public void setR22_PRODUCT(String v) { R22_PRODUCT = v; }
		public BigDecimal getR22_TOTAL() { return R22_TOTAL; }
		public void setR22_TOTAL(BigDecimal v) { R22_TOTAL = v; }
		public BigDecimal getR23_LINE() { return R23_LINE; }
		public void setR23_LINE(BigDecimal v) { R23_LINE = v; }
		public String getR23_PRODUCT() { return R23_PRODUCT; }
		public void setR23_PRODUCT(String v) { R23_PRODUCT = v; }
		public BigDecimal getR23_TOTAL() { return R23_TOTAL; }
		public void setR23_TOTAL(BigDecimal v) { R23_TOTAL = v; }
		public BigDecimal getR24_LINE() { return R24_LINE; }
		public void setR24_LINE(BigDecimal v) { R24_LINE = v; }
		public String getR24_PRODUCT() { return R24_PRODUCT; }
		public void setR24_PRODUCT(String v) { R24_PRODUCT = v; }
		public BigDecimal getR24_TOTAL() { return R24_TOTAL; }
		public void setR24_TOTAL(BigDecimal v) { R24_TOTAL = v; }
		public BigDecimal getR25_LINE() { return R25_LINE; }
		public void setR25_LINE(BigDecimal v) { R25_LINE = v; }
		public String getR25_PRODUCT() { return R25_PRODUCT; }
		public void setR25_PRODUCT(String v) { R25_PRODUCT = v; }
		public BigDecimal getR25_TOTAL() { return R25_TOTAL; }
		public void setR25_TOTAL(BigDecimal v) { R25_TOTAL = v; }
		public BigDecimal getR26_LINE() { return R26_LINE; }
		public void setR26_LINE(BigDecimal v) { R26_LINE = v; }
		public String getR26_PRODUCT() { return R26_PRODUCT; }
		public void setR26_PRODUCT(String v) { R26_PRODUCT = v; }
		public BigDecimal getR26_TOTAL() { return R26_TOTAL; }
		public void setR26_TOTAL(BigDecimal v) { R26_TOTAL = v; }
		public BigDecimal getR27_LINE() { return R27_LINE; }
		public void setR27_LINE(BigDecimal v) { R27_LINE = v; }
		public String getR27_PRODUCT() { return R27_PRODUCT; }
		public void setR27_PRODUCT(String v) { R27_PRODUCT = v; }
		public BigDecimal getR27_TOTAL() { return R27_TOTAL; }
		public void setR27_TOTAL(BigDecimal v) { R27_TOTAL = v; }
		public BigDecimal getR28_LINE() { return R28_LINE; }
		public void setR28_LINE(BigDecimal v) { R28_LINE = v; }
		public String getR28_PRODUCT() { return R28_PRODUCT; }
		public void setR28_PRODUCT(String v) { R28_PRODUCT = v; }
		public BigDecimal getR28_TOTAL() { return R28_TOTAL; }
		public void setR28_TOTAL(BigDecimal v) { R28_TOTAL = v; }
		public BigDecimal getR29_LINE() { return R29_LINE; }
		public void setR29_LINE(BigDecimal v) { R29_LINE = v; }
		public String getR29_PRODUCT() { return R29_PRODUCT; }
		public void setR29_PRODUCT(String v) { R29_PRODUCT = v; }
		public BigDecimal getR29_TOTAL() { return R29_TOTAL; }
		public void setR29_TOTAL(BigDecimal v) { R29_TOTAL = v; }
		public BigDecimal getR30_LINE() { return R30_LINE; }
		public void setR30_LINE(BigDecimal v) { R30_LINE = v; }
		public String getR30_PRODUCT() { return R30_PRODUCT; }
		public void setR30_PRODUCT(String v) { R30_PRODUCT = v; }
		public BigDecimal getR30_TOTAL() { return R30_TOTAL; }
		public void setR30_TOTAL(BigDecimal v) { R30_TOTAL = v; }
		public BigDecimal getR31_LINE() { return R31_LINE; }
		public void setR31_LINE(BigDecimal v) { R31_LINE = v; }
		public String getR31_PRODUCT() { return R31_PRODUCT; }
		public void setR31_PRODUCT(String v) { R31_PRODUCT = v; }
		public BigDecimal getR31_TOTAL() { return R31_TOTAL; }
		public void setR31_TOTAL(BigDecimal v) { R31_TOTAL = v; }
		public BigDecimal getR32_LINE() { return R32_LINE; }
		public void setR32_LINE(BigDecimal v) { R32_LINE = v; }
		public String getR32_PRODUCT() { return R32_PRODUCT; }
		public void setR32_PRODUCT(String v) { R32_PRODUCT = v; }
		public BigDecimal getR32_TOTAL() { return R32_TOTAL; }
		public void setR32_TOTAL(BigDecimal v) { R32_TOTAL = v; }
		public BigDecimal getR33_LINE() { return R33_LINE; }
		public void setR33_LINE(BigDecimal v) { R33_LINE = v; }
		public String getR33_PRODUCT() { return R33_PRODUCT; }
		public void setR33_PRODUCT(String v) { R33_PRODUCT = v; }
		public BigDecimal getR33_TOTAL() { return R33_TOTAL; }
		public void setR33_TOTAL(BigDecimal v) { R33_TOTAL = v; }
		public BigDecimal getR34_LINE() { return R34_LINE; }
		public void setR34_LINE(BigDecimal v) { R34_LINE = v; }
		public String getR34_PRODUCT() { return R34_PRODUCT; }
		public void setR34_PRODUCT(String v) { R34_PRODUCT = v; }
		public BigDecimal getR34_TOTAL() { return R34_TOTAL; }
		public void setR34_TOTAL(BigDecimal v) { R34_TOTAL = v; }
		public BigDecimal getR35_LINE() { return R35_LINE; }
		public void setR35_LINE(BigDecimal v) { R35_LINE = v; }
		public String getR35_PRODUCT() { return R35_PRODUCT; }
		public void setR35_PRODUCT(String v) { R35_PRODUCT = v; }
		public BigDecimal getR35_TOTAL() { return R35_TOTAL; }
		public void setR35_TOTAL(BigDecimal v) { R35_TOTAL = v; }
		public BigDecimal getR36_LINE() { return R36_LINE; }
		public void setR36_LINE(BigDecimal v) { R36_LINE = v; }
		public String getR36_PRODUCT() { return R36_PRODUCT; }
		public void setR36_PRODUCT(String v) { R36_PRODUCT = v; }
		public BigDecimal getR36_TOTAL() { return R36_TOTAL; }
		public void setR36_TOTAL(BigDecimal v) { R36_TOTAL = v; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; }
		public void setREPORT_FREQUENCY(String v) { REPORT_FREQUENCY = v; }
		public String getREPORT_CODE() { return REPORT_CODE; }
		public void setREPORT_CODE(String v) { REPORT_CODE = v; }
		public String getREPORT_DESC() { return REPORT_DESC; }
		public void setREPORT_DESC(String v) { REPORT_DESC = v; }
		public String getENTITY_FLG() { return ENTITY_FLG; }
		public void setENTITY_FLG(String v) { ENTITY_FLG = v; }
		public String getMODIFY_FLG() { return MODIFY_FLG; }
		public void setMODIFY_FLG(String v) { MODIFY_FLG = v; }
		public String getDEL_FLG() { return DEL_FLG; }
		public void setDEL_FLG(String v) { DEL_FLG = v; }
	}

	// =====================================================================
	// ROW MAPPER — M_MRC_Archival_Summary_Entity
	// =====================================================================
	class M_MRCArchivalSummaryRowMapper implements RowMapper<M_MRC_Archival_Summary_Entity> {
		@Override
		public M_MRC_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_MRC_Archival_Summary_Entity obj = new M_MRC_Archival_Summary_Entity();
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportVersion(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setR9_LINE(rs.getBigDecimal("R9_LINE"));
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));
			obj.setR10_LINE(rs.getBigDecimal("R10_LINE"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));
			obj.setR11_LINE(rs.getBigDecimal("R11_LINE"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));
			obj.setR12_LINE(rs.getBigDecimal("R12_LINE"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));
			obj.setR13_LINE(rs.getBigDecimal("R13_LINE"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));
			obj.setR14_LINE(rs.getBigDecimal("R14_LINE"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));
			obj.setR15_LINE(rs.getBigDecimal("R15_LINE"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));
			obj.setR16_LINE(rs.getBigDecimal("R16_LINE"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));
			obj.setR17_LINE(rs.getBigDecimal("R17_LINE"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));
			obj.setR18_LINE(rs.getBigDecimal("R18_LINE"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));
			obj.setR19_LINE(rs.getBigDecimal("R19_LINE"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));
			obj.setR20_LINE(rs.getBigDecimal("R20_LINE"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_TOTAL(rs.getBigDecimal("R20_TOTAL"));
			obj.setR21_LINE(rs.getBigDecimal("R21_LINE"));
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));
			obj.setR22_LINE(rs.getBigDecimal("R22_LINE"));
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));
			obj.setR23_LINE(rs.getBigDecimal("R23_LINE"));
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));
			obj.setR24_LINE(rs.getBigDecimal("R24_LINE"));
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));
			obj.setR25_LINE(rs.getBigDecimal("R25_LINE"));
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));
			obj.setR26_LINE(rs.getBigDecimal("R26_LINE"));
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));
			obj.setR27_LINE(rs.getBigDecimal("R27_LINE"));
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));
			obj.setR28_LINE(rs.getBigDecimal("R28_LINE"));
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));
			obj.setR29_LINE(rs.getBigDecimal("R29_LINE"));
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_TOTAL(rs.getBigDecimal("R29_TOTAL"));
			obj.setR30_LINE(rs.getBigDecimal("R30_LINE"));
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_TOTAL(rs.getBigDecimal("R30_TOTAL"));
			obj.setR31_LINE(rs.getBigDecimal("R31_LINE"));
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_TOTAL(rs.getBigDecimal("R31_TOTAL"));
			obj.setR32_LINE(rs.getBigDecimal("R32_LINE"));
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_TOTAL(rs.getBigDecimal("R32_TOTAL"));
			obj.setR33_LINE(rs.getBigDecimal("R33_LINE"));
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_TOTAL(rs.getBigDecimal("R33_TOTAL"));
			obj.setR34_LINE(rs.getBigDecimal("R34_LINE"));
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_TOTAL(rs.getBigDecimal("R34_TOTAL"));
			obj.setR35_LINE(rs.getBigDecimal("R35_LINE"));
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_TOTAL(rs.getBigDecimal("R35_TOTAL"));
			obj.setR36_LINE(rs.getBigDecimal("R36_LINE"));
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_TOTAL(rs.getBigDecimal("R36_TOTAL"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));
			return obj;
		}
	}

	// =====================================================================
	// ENTITY CLASS — M_MRC_Archival_Summary_Entity
	// =====================================================================
	public static class M_MRC_Archival_Summary_Entity {
		private BigDecimal R9_LINE;
		private String R9_PRODUCT;
		private BigDecimal R9_TOTAL;
		private BigDecimal R10_LINE;
		private String R10_PRODUCT;
		private BigDecimal R10_TOTAL;
		private BigDecimal R11_LINE;
		private String R11_PRODUCT;
		private BigDecimal R11_TOTAL;
		private BigDecimal R12_LINE;
		private String R12_PRODUCT;
		private BigDecimal R12_TOTAL;
		private BigDecimal R13_LINE;
		private String R13_PRODUCT;
		private BigDecimal R13_TOTAL;
		private BigDecimal R14_LINE;
		private String R14_PRODUCT;
		private BigDecimal R14_TOTAL;
		private BigDecimal R15_LINE;
		private String R15_PRODUCT;
		private BigDecimal R15_TOTAL;
		private BigDecimal R16_LINE;
		private String R16_PRODUCT;
		private BigDecimal R16_TOTAL;
		private BigDecimal R17_LINE;
		private String R17_PRODUCT;
		private BigDecimal R17_TOTAL;
		private BigDecimal R18_LINE;
		private String R18_PRODUCT;
		private BigDecimal R18_TOTAL;
		private BigDecimal R19_LINE;
		private String R19_PRODUCT;
		private BigDecimal R19_TOTAL;
		private BigDecimal R20_LINE;
		private String R20_PRODUCT;
		private BigDecimal R20_TOTAL;
		private BigDecimal R21_LINE;
		private String R21_PRODUCT;
		private BigDecimal R21_TOTAL;
		private BigDecimal R22_LINE;
		private String R22_PRODUCT;
		private BigDecimal R22_TOTAL;
		private BigDecimal R23_LINE;
		private String R23_PRODUCT;
		private BigDecimal R23_TOTAL;
		private BigDecimal R24_LINE;
		private String R24_PRODUCT;
		private BigDecimal R24_TOTAL;
		private BigDecimal R25_LINE;
		private String R25_PRODUCT;
		private BigDecimal R25_TOTAL;
		private BigDecimal R26_LINE;
		private String R26_PRODUCT;
		private BigDecimal R26_TOTAL;
		private BigDecimal R27_LINE;
		private String R27_PRODUCT;
		private BigDecimal R27_TOTAL;
		private BigDecimal R28_LINE;
		private String R28_PRODUCT;
		private BigDecimal R28_TOTAL;
		private BigDecimal R29_LINE;
		private String R29_PRODUCT;
		private BigDecimal R29_TOTAL;
		private BigDecimal R30_LINE;
		private String R30_PRODUCT;
		private BigDecimal R30_TOTAL;
		private BigDecimal R31_LINE;
		private String R31_PRODUCT;
		private BigDecimal R31_TOTAL;
		private BigDecimal R32_LINE;
		private String R32_PRODUCT;
		private BigDecimal R32_TOTAL;
		private BigDecimal R33_LINE;
		private String R33_PRODUCT;
		private BigDecimal R33_TOTAL;
		private BigDecimal R34_LINE;
		private String R34_PRODUCT;
		private BigDecimal R34_TOTAL;
		private BigDecimal R35_LINE;
		private String R35_PRODUCT;
		private BigDecimal R35_TOTAL;
		private BigDecimal R36_LINE;
		private String R36_PRODUCT;
		private BigDecimal R36_TOTAL;
		private Date reportDate;
		private BigDecimal reportVersion;
		private Date reportResubDate;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date v) { reportDate = v; }
		public BigDecimal getReportVersion() { return reportVersion; }
		public void setReportVersion(BigDecimal v) { reportVersion = v; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date v) { reportResubDate = v; }
		public BigDecimal getR9_LINE() { return R9_LINE; }
		public void setR9_LINE(BigDecimal v) { R9_LINE = v; }
		public String getR9_PRODUCT() { return R9_PRODUCT; }
		public void setR9_PRODUCT(String v) { R9_PRODUCT = v; }
		public BigDecimal getR9_TOTAL() { return R9_TOTAL; }
		public void setR9_TOTAL(BigDecimal v) { R9_TOTAL = v; }
		public BigDecimal getR10_LINE() { return R10_LINE; }
		public void setR10_LINE(BigDecimal v) { R10_LINE = v; }
		public String getR10_PRODUCT() { return R10_PRODUCT; }
		public void setR10_PRODUCT(String v) { R10_PRODUCT = v; }
		public BigDecimal getR10_TOTAL() { return R10_TOTAL; }
		public void setR10_TOTAL(BigDecimal v) { R10_TOTAL = v; }
		public BigDecimal getR11_LINE() { return R11_LINE; }
		public void setR11_LINE(BigDecimal v) { R11_LINE = v; }
		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String v) { R11_PRODUCT = v; }
		public BigDecimal getR11_TOTAL() { return R11_TOTAL; }
		public void setR11_TOTAL(BigDecimal v) { R11_TOTAL = v; }
		public BigDecimal getR12_LINE() { return R12_LINE; }
		public void setR12_LINE(BigDecimal v) { R12_LINE = v; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String v) { R12_PRODUCT = v; }
		public BigDecimal getR12_TOTAL() { return R12_TOTAL; }
		public void setR12_TOTAL(BigDecimal v) { R12_TOTAL = v; }
		public BigDecimal getR13_LINE() { return R13_LINE; }
		public void setR13_LINE(BigDecimal v) { R13_LINE = v; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String v) { R13_PRODUCT = v; }
		public BigDecimal getR13_TOTAL() { return R13_TOTAL; }
		public void setR13_TOTAL(BigDecimal v) { R13_TOTAL = v; }
		public BigDecimal getR14_LINE() { return R14_LINE; }
		public void setR14_LINE(BigDecimal v) { R14_LINE = v; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String v) { R14_PRODUCT = v; }
		public BigDecimal getR14_TOTAL() { return R14_TOTAL; }
		public void setR14_TOTAL(BigDecimal v) { R14_TOTAL = v; }
		public BigDecimal getR15_LINE() { return R15_LINE; }
		public void setR15_LINE(BigDecimal v) { R15_LINE = v; }
		public String getR15_PRODUCT() { return R15_PRODUCT; }
		public void setR15_PRODUCT(String v) { R15_PRODUCT = v; }
		public BigDecimal getR15_TOTAL() { return R15_TOTAL; }
		public void setR15_TOTAL(BigDecimal v) { R15_TOTAL = v; }
		public BigDecimal getR16_LINE() { return R16_LINE; }
		public void setR16_LINE(BigDecimal v) { R16_LINE = v; }
		public String getR16_PRODUCT() { return R16_PRODUCT; }
		public void setR16_PRODUCT(String v) { R16_PRODUCT = v; }
		public BigDecimal getR16_TOTAL() { return R16_TOTAL; }
		public void setR16_TOTAL(BigDecimal v) { R16_TOTAL = v; }
		public BigDecimal getR17_LINE() { return R17_LINE; }
		public void setR17_LINE(BigDecimal v) { R17_LINE = v; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String v) { R17_PRODUCT = v; }
		public BigDecimal getR17_TOTAL() { return R17_TOTAL; }
		public void setR17_TOTAL(BigDecimal v) { R17_TOTAL = v; }
		public BigDecimal getR18_LINE() { return R18_LINE; }
		public void setR18_LINE(BigDecimal v) { R18_LINE = v; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String v) { R18_PRODUCT = v; }
		public BigDecimal getR18_TOTAL() { return R18_TOTAL; }
		public void setR18_TOTAL(BigDecimal v) { R18_TOTAL = v; }
		public BigDecimal getR19_LINE() { return R19_LINE; }
		public void setR19_LINE(BigDecimal v) { R19_LINE = v; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String v) { R19_PRODUCT = v; }
		public BigDecimal getR19_TOTAL() { return R19_TOTAL; }
		public void setR19_TOTAL(BigDecimal v) { R19_TOTAL = v; }
		public BigDecimal getR20_LINE() { return R20_LINE; }
		public void setR20_LINE(BigDecimal v) { R20_LINE = v; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String v) { R20_PRODUCT = v; }
		public BigDecimal getR20_TOTAL() { return R20_TOTAL; }
		public void setR20_TOTAL(BigDecimal v) { R20_TOTAL = v; }
		public BigDecimal getR21_LINE() { return R21_LINE; }
		public void setR21_LINE(BigDecimal v) { R21_LINE = v; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String v) { R21_PRODUCT = v; }
		public BigDecimal getR21_TOTAL() { return R21_TOTAL; }
		public void setR21_TOTAL(BigDecimal v) { R21_TOTAL = v; }
		public BigDecimal getR22_LINE() { return R22_LINE; }
		public void setR22_LINE(BigDecimal v) { R22_LINE = v; }
		public String getR22_PRODUCT() { return R22_PRODUCT; }
		public void setR22_PRODUCT(String v) { R22_PRODUCT = v; }
		public BigDecimal getR22_TOTAL() { return R22_TOTAL; }
		public void setR22_TOTAL(BigDecimal v) { R22_TOTAL = v; }
		public BigDecimal getR23_LINE() { return R23_LINE; }
		public void setR23_LINE(BigDecimal v) { R23_LINE = v; }
		public String getR23_PRODUCT() { return R23_PRODUCT; }
		public void setR23_PRODUCT(String v) { R23_PRODUCT = v; }
		public BigDecimal getR23_TOTAL() { return R23_TOTAL; }
		public void setR23_TOTAL(BigDecimal v) { R23_TOTAL = v; }
		public BigDecimal getR24_LINE() { return R24_LINE; }
		public void setR24_LINE(BigDecimal v) { R24_LINE = v; }
		public String getR24_PRODUCT() { return R24_PRODUCT; }
		public void setR24_PRODUCT(String v) { R24_PRODUCT = v; }
		public BigDecimal getR24_TOTAL() { return R24_TOTAL; }
		public void setR24_TOTAL(BigDecimal v) { R24_TOTAL = v; }
		public BigDecimal getR25_LINE() { return R25_LINE; }
		public void setR25_LINE(BigDecimal v) { R25_LINE = v; }
		public String getR25_PRODUCT() { return R25_PRODUCT; }
		public void setR25_PRODUCT(String v) { R25_PRODUCT = v; }
		public BigDecimal getR25_TOTAL() { return R25_TOTAL; }
		public void setR25_TOTAL(BigDecimal v) { R25_TOTAL = v; }
		public BigDecimal getR26_LINE() { return R26_LINE; }
		public void setR26_LINE(BigDecimal v) { R26_LINE = v; }
		public String getR26_PRODUCT() { return R26_PRODUCT; }
		public void setR26_PRODUCT(String v) { R26_PRODUCT = v; }
		public BigDecimal getR26_TOTAL() { return R26_TOTAL; }
		public void setR26_TOTAL(BigDecimal v) { R26_TOTAL = v; }
		public BigDecimal getR27_LINE() { return R27_LINE; }
		public void setR27_LINE(BigDecimal v) { R27_LINE = v; }
		public String getR27_PRODUCT() { return R27_PRODUCT; }
		public void setR27_PRODUCT(String v) { R27_PRODUCT = v; }
		public BigDecimal getR27_TOTAL() { return R27_TOTAL; }
		public void setR27_TOTAL(BigDecimal v) { R27_TOTAL = v; }
		public BigDecimal getR28_LINE() { return R28_LINE; }
		public void setR28_LINE(BigDecimal v) { R28_LINE = v; }
		public String getR28_PRODUCT() { return R28_PRODUCT; }
		public void setR28_PRODUCT(String v) { R28_PRODUCT = v; }
		public BigDecimal getR28_TOTAL() { return R28_TOTAL; }
		public void setR28_TOTAL(BigDecimal v) { R28_TOTAL = v; }
		public BigDecimal getR29_LINE() { return R29_LINE; }
		public void setR29_LINE(BigDecimal v) { R29_LINE = v; }
		public String getR29_PRODUCT() { return R29_PRODUCT; }
		public void setR29_PRODUCT(String v) { R29_PRODUCT = v; }
		public BigDecimal getR29_TOTAL() { return R29_TOTAL; }
		public void setR29_TOTAL(BigDecimal v) { R29_TOTAL = v; }
		public BigDecimal getR30_LINE() { return R30_LINE; }
		public void setR30_LINE(BigDecimal v) { R30_LINE = v; }
		public String getR30_PRODUCT() { return R30_PRODUCT; }
		public void setR30_PRODUCT(String v) { R30_PRODUCT = v; }
		public BigDecimal getR30_TOTAL() { return R30_TOTAL; }
		public void setR30_TOTAL(BigDecimal v) { R30_TOTAL = v; }
		public BigDecimal getR31_LINE() { return R31_LINE; }
		public void setR31_LINE(BigDecimal v) { R31_LINE = v; }
		public String getR31_PRODUCT() { return R31_PRODUCT; }
		public void setR31_PRODUCT(String v) { R31_PRODUCT = v; }
		public BigDecimal getR31_TOTAL() { return R31_TOTAL; }
		public void setR31_TOTAL(BigDecimal v) { R31_TOTAL = v; }
		public BigDecimal getR32_LINE() { return R32_LINE; }
		public void setR32_LINE(BigDecimal v) { R32_LINE = v; }
		public String getR32_PRODUCT() { return R32_PRODUCT; }
		public void setR32_PRODUCT(String v) { R32_PRODUCT = v; }
		public BigDecimal getR32_TOTAL() { return R32_TOTAL; }
		public void setR32_TOTAL(BigDecimal v) { R32_TOTAL = v; }
		public BigDecimal getR33_LINE() { return R33_LINE; }
		public void setR33_LINE(BigDecimal v) { R33_LINE = v; }
		public String getR33_PRODUCT() { return R33_PRODUCT; }
		public void setR33_PRODUCT(String v) { R33_PRODUCT = v; }
		public BigDecimal getR33_TOTAL() { return R33_TOTAL; }
		public void setR33_TOTAL(BigDecimal v) { R33_TOTAL = v; }
		public BigDecimal getR34_LINE() { return R34_LINE; }
		public void setR34_LINE(BigDecimal v) { R34_LINE = v; }
		public String getR34_PRODUCT() { return R34_PRODUCT; }
		public void setR34_PRODUCT(String v) { R34_PRODUCT = v; }
		public BigDecimal getR34_TOTAL() { return R34_TOTAL; }
		public void setR34_TOTAL(BigDecimal v) { R34_TOTAL = v; }
		public BigDecimal getR35_LINE() { return R35_LINE; }
		public void setR35_LINE(BigDecimal v) { R35_LINE = v; }
		public String getR35_PRODUCT() { return R35_PRODUCT; }
		public void setR35_PRODUCT(String v) { R35_PRODUCT = v; }
		public BigDecimal getR35_TOTAL() { return R35_TOTAL; }
		public void setR35_TOTAL(BigDecimal v) { R35_TOTAL = v; }
		public BigDecimal getR36_LINE() { return R36_LINE; }
		public void setR36_LINE(BigDecimal v) { R36_LINE = v; }
		public String getR36_PRODUCT() { return R36_PRODUCT; }
		public void setR36_PRODUCT(String v) { R36_PRODUCT = v; }
		public BigDecimal getR36_TOTAL() { return R36_TOTAL; }
		public void setR36_TOTAL(BigDecimal v) { R36_TOTAL = v; }
		public String getREPORT_FREQUENCY() { return REPORT_FREQUENCY; }
		public void setREPORT_FREQUENCY(String v) { REPORT_FREQUENCY = v; }
		public String getREPORT_CODE() { return REPORT_CODE; }
		public void setREPORT_CODE(String v) { REPORT_CODE = v; }
		public String getREPORT_DESC() { return REPORT_DESC; }
		public void setREPORT_DESC(String v) { REPORT_DESC = v; }
		public String getENTITY_FLG() { return ENTITY_FLG; }
		public void setENTITY_FLG(String v) { ENTITY_FLG = v; }
		public String getMODIFY_FLG() { return MODIFY_FLG; }
		public void setMODIFY_FLG(String v) { MODIFY_FLG = v; }
		public String getDEL_FLG() { return DEL_FLG; }
		public void setDEL_FLG(String v) { DEL_FLG = v; }
	}

	// =====================================================================
	// ROW MAPPER — M_MRC_Detail_Entity
	// =====================================================================
	class M_MRCDetailRowMapper implements RowMapper<M_MRC_Detail_Entity> {
		@Override
		public M_MRC_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_MRC_Detail_Entity obj = new M_MRC_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			String entityFlgStr = rs.getString("ENTITY_FLG");
			obj.setEntityFlg(entityFlgStr != null && !entityFlgStr.isEmpty() ? entityFlgStr.charAt(0) : ' ');
			String modifyFlgStr = rs.getString("MODIFY_FLG");
			obj.setModifyFlg(modifyFlgStr != null && !modifyFlgStr.isEmpty() ? modifyFlgStr.charAt(0) : ' ');
			String delFlgStr = rs.getString("DEL_FLG");
			obj.setDelFlg(delFlgStr != null && !delFlgStr.isEmpty() ? delFlgStr.charAt(0) : ' ');
			return obj;
		}
	}

	// =====================================================================
	// ENTITY CLASS — M_MRC_Detail_Entity
	// =====================================================================
	public static class M_MRC_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLabel;
		private String reportAddlCriteria1;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;
		private Date reportDate;
		private String reportName;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private char entityFlg;
		private char modifyFlg;
		private char delFlg;

		public String getCustId() { return custId; }
		public void setCustId(String v) { custId = v; }
		public String getAcctNumber() { return acctNumber; }
		public void setAcctNumber(String v) { acctNumber = v; }
		public String getAcctName() { return acctName; }
		public void setAcctName(String v) { acctName = v; }
		public String getDataType() { return dataType; }
		public void setDataType(String v) { dataType = v; }
		public String getReportLabel() { return reportLabel; }
		public void setReportLabel(String v) { reportLabel = v; }
		public String getReportAddlCriteria1() { return reportAddlCriteria1; }
		public void setReportAddlCriteria1(String v) { reportAddlCriteria1 = v; }
		public String getReportRemarks() { return reportRemarks; }
		public void setReportRemarks(String v) { reportRemarks = v; }
		public String getModificationRemarks() { return modificationRemarks; }
		public void setModificationRemarks(String v) { modificationRemarks = v; }
		public String getDataEntryVersion() { return dataEntryVersion; }
		public void setDataEntryVersion(String v) { dataEntryVersion = v; }
		public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; }
		public void setAcctBalanceInpula(BigDecimal v) { acctBalanceInpula = v; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date v) { reportDate = v; }
		public String getReportName() { return reportName; }
		public void setReportName(String v) { reportName = v; }
		public String getCreateUser() { return createUser; }
		public void setCreateUser(String v) { createUser = v; }
		public Date getCreateTime() { return createTime; }
		public void setCreateTime(Date v) { createTime = v; }
		public String getModifyUser() { return modifyUser; }
		public void setModifyUser(String v) { modifyUser = v; }
		public Date getModifyTime() { return modifyTime; }
		public void setModifyTime(Date v) { modifyTime = v; }
		public String getVerifyUser() { return verifyUser; }
		public void setVerifyUser(String v) { verifyUser = v; }
		public Date getVerifyTime() { return verifyTime; }
		public void setVerifyTime(Date v) { verifyTime = v; }
		public char getEntityFlg() { return entityFlg; }
		public void setEntityFlg(char v) { entityFlg = v; }
		public char getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(char v) { modifyFlg = v; }
		public char getDelFlg() { return delFlg; }
		public void setDelFlg(char v) { delFlg = v; }
	}

	// =====================================================================
	// ROW MAPPER — M_MRC_Archival_Detail_Entity
	// =====================================================================
	class M_MRCArchivalDetailRowMapper implements RowMapper<M_MRC_Archival_Detail_Entity> {
		@Override
		public M_MRC_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_MRC_Archival_Detail_Entity obj = new M_MRC_Archival_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			String entityFlgStr = rs.getString("ENTITY_FLG");
			obj.setEntityFlg(entityFlgStr != null && !entityFlgStr.isEmpty() ? entityFlgStr.charAt(0) : ' ');
			String modifyFlgStr = rs.getString("MODIFY_FLG");
			obj.setModifyFlg(modifyFlgStr != null && !modifyFlgStr.isEmpty() ? modifyFlgStr.charAt(0) : ' ');
			String delFlgStr = rs.getString("DEL_FLG");
			obj.setDelFlg(delFlgStr != null && !delFlgStr.isEmpty() ? delFlgStr.charAt(0) : ' ');
			return obj;
		}
	}

	// =====================================================================
	// ENTITY CLASS — M_MRC_Archival_Detail_Entity
	// =====================================================================
	public static class M_MRC_Archival_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportLabel;
		private String reportAddlCriteria1;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;
		private Date reportDate;
		private String reportName;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private char entityFlg;
		private char modifyFlg;
		private char delFlg;

		public String getCustId() { return custId; }
		public void setCustId(String v) { custId = v; }
		public String getAcctNumber() { return acctNumber; }
		public void setAcctNumber(String v) { acctNumber = v; }
		public String getAcctName() { return acctName; }
		public void setAcctName(String v) { acctName = v; }
		public String getDataType() { return dataType; }
		public void setDataType(String v) { dataType = v; }
		public String getReportLabel() { return reportLabel; }
		public void setReportLabel(String v) { reportLabel = v; }
		public String getReportAddlCriteria1() { return reportAddlCriteria1; }
		public void setReportAddlCriteria1(String v) { reportAddlCriteria1 = v; }
		public String getReportRemarks() { return reportRemarks; }
		public void setReportRemarks(String v) { reportRemarks = v; }
		public String getModificationRemarks() { return modificationRemarks; }
		public void setModificationRemarks(String v) { modificationRemarks = v; }
		public String getDataEntryVersion() { return dataEntryVersion; }
		public void setDataEntryVersion(String v) { dataEntryVersion = v; }
		public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; }
		public void setAcctBalanceInpula(BigDecimal v) { acctBalanceInpula = v; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date v) { reportDate = v; }
		public String getReportName() { return reportName; }
		public void setReportName(String v) { reportName = v; }
		public String getCreateUser() { return createUser; }
		public void setCreateUser(String v) { createUser = v; }
		public Date getCreateTime() { return createTime; }
		public void setCreateTime(Date v) { createTime = v; }
		public String getModifyUser() { return modifyUser; }
		public void setModifyUser(String v) { modifyUser = v; }
		public Date getModifyTime() { return modifyTime; }
		public void setModifyTime(Date v) { modifyTime = v; }
		public String getVerifyUser() { return verifyUser; }
		public void setVerifyUser(String v) { verifyUser = v; }
		public Date getVerifyTime() { return verifyTime; }
		public void setVerifyTime(Date v) { verifyTime = v; }
		public char getEntityFlg() { return entityFlg; }
		public void setEntityFlg(char v) { entityFlg = v; }
		public char getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(char v) { modifyFlg = v; }
		public char getDelFlg() { return delFlg; }
		public void setDelFlg(char v) { delFlg = v; }
	}

}
