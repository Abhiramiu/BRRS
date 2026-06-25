package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import org.apache.poi.ss.usermodel.CellType;
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
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional

public class BRRS_M_CA1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// =====================================================================
	// JDBC QUERY METHODS
	// =====================================================================

	public List<M_CA1_Summary_Entity> getSummaryByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_CA1_SUMMARYTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate}, new M_CA1SummaryRowMapper());
	}

	public List<M_CA1_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date reportDate, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_CA1_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate, version}, new M_CA1ArchivalSummaryRowMapper());
	}

	public List<M_CA1_Archival_Summary_Entity> getArchivalSummaryWithVersion() {
		String sql = "SELECT * FROM BRRS_M_CA1_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_CA1ArchivalSummaryRowMapper());
	}

	public List<M_CA1_Detail_Entity> getDetailByDate(Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_CA1_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate}, new M_CA1DetailRowMapper());
	}

	public int getDetailCount(Date reportDate) {
		String sql = "SELECT COUNT(*) FROM BRRS_M_CA1_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{reportDate}, Integer.class);
	}

	public List<M_CA1_Detail_Entity> getDetailByRowIdAndColumnId(String rowId, String columnId, Date reportDate) {
		String sql = "SELECT * FROM BRRS_M_CA1_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[]{rowId, columnId, reportDate}, new M_CA1DetailRowMapper());
	}

	public List<M_CA1_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date reportDate, String version) {
		String sql = "SELECT * FROM BRRS_M_CA1_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{reportDate, version}, new M_CA1ArchivalDetailRowMapper());
	}

	public List<M_CA1_Archival_Detail_Entity> getArchivalDetailByRowIdAndColumnId(
			String rowId, String columnId, Date reportDate, String version) {
		String sql = "SELECT * FROM BRRS_M_CA1_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{rowId, columnId, reportDate, version}, new M_CA1ArchivalDetailRowMapper());
	}

	public ModelAndView getM_CA1View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version,HttpServletRequest req1,Model md) {

		ModelAndView mv = new ModelAndView();
		
		String userid = (String) req1.getSession().getAttribute("USERID");

		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);
		
		if (type.equals("ARCHIVAL") & version != null) {
			List<M_CA1_Archival_Summary_Entity> T1Master = new ArrayList<M_CA1_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				T1Master = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		} else {

			List<M_CA1_Summary_Entity> T1Master = new ArrayList<M_CA1_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				T1Master = getSummaryByDate(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_CA1");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_CA1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version,HttpServletRequest req1,Model md) {

		ModelAndView mv = new ModelAndView("BRRS/M_CA1");
		
		String userid = (String) req1.getSession().getAttribute("USERID");

		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);
		
		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalRecords = 0;

		try {
// ✅ Parse toDate
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

// ✅ Parse filter (rowId, columnIds)
			String rowId = null, columnId = null, columnId1 = null, columnId2 = null;
			if (filter != null && !filter.isEmpty()) {
				String[] parts = filter.split(",", -1);
				rowId = parts.length > 0 ? parts[0] : null;
				columnId = parts.length > 1 ? parts[1] : null;
				columnId1 = parts.length > 2 ? parts[2] : null;
				columnId2 = parts.length > 3 ? parts[3] : null;
			}

// ✅ ARCHIVAL DATA BRANCH
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.isEmpty()) {
				logger.info("Fetching ARCHIVAL data for version {}", version);

				List<M_CA1_Archival_Detail_Entity> detailList;

// 🔹 Filtered (ROWID + COLUMNID)
				if (rowId != null && !rowId.isEmpty()
						&& (isNotEmpty(columnId) || isNotEmpty(columnId1) || isNotEmpty(columnId2))) {

					logger.info("➡ ARCHIVAL DETAIL QUERY TRIGGERED (with filters)");
					detailList = getArchivalDetailByRowIdAndColumnId(rowId, columnId, parsedDate, version);

				} else {
					logger.info("➡ ARCHIVAL LIST QUERY TRIGGERED (with pagination)");
					detailList = getArchivalDetailByDateAndVersion(parsedDate, version);
					//totalRecords = M_CA1_Archival_Detail_Repo.getdatacount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);
				logger.info("ARCHIVAL COUNT: {}", (detailList != null ? detailList.size() : 0));

			} else {
// ✅ CURRENT DATA BRANCH
				logger.info("Fetching CURRENT data for M_CA1");

				List<M_CA1_Detail_Entity> detailList;

				if (rowId != null && !rowId.isEmpty()
						&& (isNotEmpty(columnId) || isNotEmpty(columnId1) || isNotEmpty(columnId2))) {

					logger.info("➡ CURRENT DETAIL QUERY TRIGGERED (with filters)");
					detailList = getDetailByRowIdAndColumnId(rowId, columnId, parsedDate);
							

				} else {
					logger.info("➡ CURRENT LIST QUERY TRIGGERED (with pagination)");
					detailList = getDetailByDate(parsedDate);
					totalRecords = getDetailCount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);
				logger.info("CURRENT COUNT: {}", (detailList != null ? detailList.size() : 0));
			}

		} catch (ParseException e) {
			logger.error("Invalid date format: {}", todate, e);
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			logger.error("Unexpected error in getM_CA1currentDtl", e);
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

// ✅ Common model attributes
		int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		mv.addObject("totalPages", totalPages);
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		logger.info("Total pages calculated: {}", totalPages);
		return mv;
	}

//Helper for null/empty check
	private boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	public byte[] BRRS_M_CA1Excel(String filename, String reportId, String fromdate, String todate, String currency,
	        String dtltype, String type, BigDecimal version) throws Exception {

	    logger.info("Service: Starting Excel generation process in memory.");

	    // ARCHIVAL check
	    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
	        logger.info("Service: Generating ARCHIVAL report for version {}", version);
	        return getExcelM_CA1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
	    }

	    List<M_CA1_Summary_Entity> dataList =
	            getSummaryByDate(dateformat.parse(todate));

	    if (dataList.isEmpty()) {
	        logger.warn("Service: No data found for LA1 report. Returning empty result.");
	        return new byte[0];
	    }

	    String templateDir = env.getProperty("output.exportpathtemp");
	    String templateFileName = filename;
	    Path templatePath = Paths.get(templateDir, templateFileName);

	    logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

	    if (!Files.exists(templatePath)) {
	        throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
	    }

	    if (!Files.isReadable(templatePath)) {
	        throw new SecurityException(
	                "Template file exists but is not readable: " + templatePath.toAbsolutePath());
	    }

	    try (InputStream templateInputStream = Files.newInputStream(templatePath);
	         Workbook workbook = WorkbookFactory.create(templateInputStream);
	         ByteArrayOutputStream out = new ByteArrayOutputStream()) {

	        Sheet sheet = workbook.getSheetAt(0);

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

	        Font font = workbook.createFont();
	        font.setFontHeightInPoints((short) 8);
	        font.setFontName("Arial");

	        CellStyle numberStyle = workbook.createCellStyle();
	        numberStyle.setBorderBottom(BorderStyle.THIN);
	        numberStyle.setBorderTop(BorderStyle.THIN);
	        numberStyle.setBorderLeft(BorderStyle.THIN);
	        numberStyle.setBorderRight(BorderStyle.THIN);
	        numberStyle.setFont(font);

	        int startRow = 5;

	        if (!dataList.isEmpty()) {

	            for (int i = 0; i < dataList.size(); i++) {

	                M_CA1_Summary_Entity record = dataList.get(i);

	                
	                Row row = sheet.getRow(startRow);
	                if (row == null) row = sheet.createRow(startRow);
	                
	                
	              //REPORT_DATE
					row = sheet.getRow(5);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
					    cell1 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
					    cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
					    cell1.setCellStyle(dateStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
	                
	             // Row 9
					row = sheet.getRow(8);
	                Cell cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR9_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR9_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }

	                // Row 10
	                row = sheet.getRow(9);
	                if (row == null) row = sheet.createRow(9);
	                cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR10_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR10_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }

	                // Row 11
	                row = sheet.getRow(10);
	                if (row == null) row = sheet.createRow(10);
	                cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR11_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR11_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }

	                // Row 12
	                row = sheet.getRow(11);
	                if (row == null) row = sheet.createRow(11);
	                cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR12_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR12_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }

	                // Row 13
	                row = sheet.getRow(12);
	                if (row == null) row = sheet.createRow(12);
	                cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR13_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR13_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }

	                // Row 17
	                row = sheet.getRow(16);
	                if (row == null) row = sheet.createRow(16);
	                cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR17_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR17_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }

	                // Row 18
	                row = sheet.getRow(17);
	                if (row == null) row = sheet.createRow(17);
	                cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR18_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR18_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }

	                // Row 19
	                row = sheet.getRow(18);
	                if (row == null) row = sheet.createRow(18);
	                cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR19_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR19_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }

	                // Row 20
	                row = sheet.getRow(19);
	                if (row == null) row = sheet.createRow(19);
	                cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR20_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR20_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }

	                // Row 21
	                row = sheet.getRow(20);
	                if (row == null) row = sheet.createRow(20);
	                cell3 = row.getCell(3);
	                if (cell3 == null) cell3 = row.createCell(3);

	                if (record.getR21_AMOUNT() != null) {
	                    cell3.setCellValue(record.getR21_AMOUNT().doubleValue());
	                    cell3.setCellStyle(numberStyle);
	                } else {
	                    cell3.setCellType(CellType.BLANK);
	                    cell3.setCellStyle(textStyle);
	                }
	            }

	            // DO NOT evaluate formulas (causes external workbook error)
	            workbook.setForceFormulaRecalculation(true);
	        }

	        workbook.write(out);

	        logger.info("Service: Excel successfully written to memory ({} bytes).", out.size());

	        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA1 SUMMARY", null, "BRRS_M_CA1_SUMMARYTABLE");
			}
			return out.toByteArray();
	    }
	}

	public byte[] BRRS_M_CA1DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_CA1 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_CA1Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// sanction style (right aligned with 3 decimals)
			CellStyle sanctionStyle = workbook.createCellStyle();
			sanctionStyle.setAlignment(HorizontalAlignment.RIGHT);
			sanctionStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			sanctionStyle.setBorderTop(border);
			sanctionStyle.setBorderBottom(border);
			sanctionStyle.setBorderLeft(border);
			sanctionStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NUMBER", "ACCT BALANCE IN PULA",  "REPORT LABEL",
					"REPORT ADDL CRITERIA 1","REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				if (i == 3|| i == 4) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}
				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_CA1_Detail_Entity> reportData = getDetailByDate(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_CA1_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(2);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					

					row.createCell(3).setCellValue(item.getReport_label());
					row.createCell(4).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(5)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply border style to all cells in the row
					for (int colIndex = 0; colIndex < headers.length; colIndex++) {
						Cell cell = row.getCell(colIndex);
						if (cell != null) {
							if (colIndex == 3) { // ACCT BALANCE
								cell.setCellStyle(balanceStyle);
							} else if (colIndex == 4) { // APPROVED LIMIT
								cell.setCellStyle(sanctionStyle);
							} else {
								cell.setCellStyle(dataStyle);
							}
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_CA1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_CA1 Excel", e);
			return new byte[0];
		}
	}

	// Archival View
	public List<Object[]> getM_CA1Archival() {

	    List<Object[]> archivalList = new ArrayList<>();

	    try {

	        // Fetch data from Repository 1
	        List<M_CA1_Archival_Summary_Entity> repoData1 =
	                getArchivalSummaryWithVersion();

	        if (repoData1 != null && !repoData1.isEmpty()) {

	            for (M_CA1_Archival_Summary_Entity entity : repoData1) {

	                Object[] row = new Object[] {
	                        entity.getREPORT_DATE(),
	                        entity.getREPORT_VERSION(),
	                        entity.getReportResubDate()
	                };

	                archivalList.add(row);
	            }

	            System.out.println("Fetched " + archivalList.size() + " archival records from Repo1");

	            M_CA1_Archival_Summary_Entity first = repoData1.get(0);
	            System.out.println("Latest archival version (Repo1): " + first.getREPORT_VERSION());

	        } else {
	            System.out.println("No archival data found in Repo1.");
	        }

	    } catch (Exception e) {

	        System.err.println("Error fetching M_CA1 Archival data: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return archivalList;
	}
	
	
	public byte[] getExcelM_CA1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_CA1_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA1 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---
			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					   
		              //REPORT_DATE
						row = sheet.getRow(5);
						Cell cell1 = row.getCell(1);
						if (cell1 == null) {
						    cell1 = row.createCell(1);
						}

						if (record.getREPORT_DATE() != null) {
						    cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
						    cell1.setCellStyle(dateStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}
		                
		             // Row 9
						row = sheet.getRow(8);
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR9_AMOUNT() != null) {
						cell3.setCellValue(record.getR9_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
									
					
					//row10
					row = sheet.getRow(9);			
					// Column D 
					 cell3 = row.getCell(3);
					if (record.getR10_AMOUNT() != null) {
						cell3.setCellValue(record.getR10_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row11
					row = sheet.getRow(10);			
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR11_AMOUNT() != null) {
						cell3.setCellValue(record.getR11_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR12_AMOUNT() != null) {
						cell3.setCellValue(record.getR12_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row13
					row = sheet.getRow(12);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR13_AMOUNT() != null) {
						cell3.setCellValue(record.getR13_AMOUNT().doubleValue());
			
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

										
					
					

					//row17
					row = sheet.getRow(16);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR17_AMOUNT() != null) {
						cell3.setCellValue(record.getR17_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row18
					row = sheet.getRow(17);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR18_AMOUNT() != null) {
						cell3.setCellValue(record.getR18_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row19
					row = sheet.getRow(18);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR19_AMOUNT() != null) {
						cell3.setCellValue(record.getR19_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row20
					row = sheet.getRow(19);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR20_AMOUNT() != null) {
						cell3.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row21
					row = sheet.getRow(20);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR21_AMOUNT() != null) {
						cell3.setCellValue(record.getR21_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA1 ARCHIVAL SUMMARY", null, "BRRS_M_CA1_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
	        String dtltype, String type, String version) {
	    try {
	        logger.info("Generating Excel for BRRS_M_CA1 ARCHIVAL Details...");
	        System.out.println("Came to Detail download service");

	        // Only proceed if ARCHIVAL and version provided
	        if (!"ARCHIVAL".equalsIgnoreCase(type) || version == null || version.isEmpty()) {
	            logger.warn("Invalid type/version for archival download.");
	            return new byte[0];
	        }

	        // Create workbook and sheet
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("BRRS_M_CA1_Archival_Detail");

	        // Border style
	        BorderStyle border = BorderStyle.THIN;

	        // Header style
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

	        // Right-aligned header (for numeric columns)
	        CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
	        rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	        rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

	        // Data style (text)
	        CellStyle dataStyle = workbook.createCellStyle();
	        dataStyle.setAlignment(HorizontalAlignment.LEFT);
	        dataStyle.setBorderTop(border);
	        dataStyle.setBorderBottom(border);
	        dataStyle.setBorderLeft(border);
	        dataStyle.setBorderRight(border);

	        // ACCT BALANCE style (right aligned with 3 decimals)
	        CellStyle balanceStyle = workbook.createCellStyle();
	        balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
	        balanceStyle.setBorderTop(border);
	        balanceStyle.setBorderBottom(border);
	        balanceStyle.setBorderLeft(border);
	        balanceStyle.setBorderRight(border);
	        // Header row
	        String[] headers = { "CUST ID", "ACCT NUMBER", "ACCT BALANCE IN PULA","REPORT LABEL",
	                "REPORT ADDL CRITERIA 1", "REPORT_DATE" };

	        XSSFRow headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);

	            if (i == 3|| i == 4) { // ACCT BALANCE
	                cell.setCellStyle(rightAlignedHeaderStyle);
	            } else {
	                cell.setCellStyle(headerStyle);
	            }

	            sheet.setColumnWidth(i, 5000);
	        }

	        // Parse date
	        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);

	        // Fetch data
	        List<M_CA1_Archival_Detail_Entity> reportData =
	                getArchivalDetailByDateAndVersion(parsedToDate, version);

	        if (reportData != null && !reportData.isEmpty()) {
	            int rowIndex = 1;
	            for (M_CA1_Archival_Detail_Entity item : reportData) {
	                XSSFRow row = sheet.createRow(rowIndex++);

	                // Text columns
	                row.createCell(0).setCellValue(item.getCust_id());
	                row.createCell(1).setCellValue(item.getAcct_number());

	             // ACCT BALANCE (right aligned, 3 decimal places)
	                Cell balanceCell = row.createCell(2);
	                if (item.getAcct_balance_in_pula() != null) {
	                    balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
	                } else {
	                    balanceCell.setCellValue(0);
	                }
	                balanceCell.setCellStyle(balanceStyle);

	                
	             

	                // Remaining text columns
	                row.createCell(3).setCellValue(item.getReport_label());
	                row.createCell(4).setCellValue(item.getReport_addl_criteria_1());
	                row.createCell(5).setCellValue(item.getReport_date() != null
	                        ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
	                        : "");

	                // Apply text style to non-numeric cells
	                for (int j = 0; j < headers.length; j++) {
	                    if (j != 3 && j != 4) {
	                        row.getCell(j).setCellStyle(dataStyle);
	                    }
	                }
	            }
	        }

	        // Write to byte array
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        workbook.write(bos);
	        workbook.close();

	        logger.info("Excel generation completed with {} row(s).",
	                reportData != null ? reportData.size() : 0);
	        return bos.toByteArray();

	    } catch (Exception e) {
	        logger.error("Error generating BRRS_M_CA1 ARCHIVAL Excel", e);
	        return new byte[0];
	    }
	}


//	public boolean updateProvision(M_CA1_Detail_Entity la1Data) {
//		try {
//			System.out.println("Came to LA1 Service");
//
//			// ✅ Must match your entity field name exactly
//			M_CA1_Detail_Entity existing = M_CA1_Detail_Repo.findByAcctnumber(la1Data.getAcct_number());
//
//			if (existing != null) {
//
//				existing.setAcct_name(la1Data.getAcct_name());
//
//				// existing.setAcct_name(la1Data.getAcct_name());
//
//				existing.setSanction_limit(la1Data.getSanction_limit());
//				existing.setAcct_balance_in_pula(la1Data.getAcct_balance_in_pula());
//
//				M_CA1_Detail_Repo.save(existing);
//
//				System.out.println("Updated successfully for ACCT_NO: " + la1Data.getAcct_number());
//				return true;
//			} else {
//				System.out.println("Record not found for Account No: " + la1Data.getAcct_number());
//				return false;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	

	/*
	 * @Autowired private JdbcTemplate jdbcTemplate;
	 * 
	 * public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	 * ModelAndView mv = new ModelAndView("BRRS/M_CA1"); // ✅ match the report name
	 * System.out.println("Hello"); if (acctNo != null) { M_CA1_Detail_Entity
	 * la1Entity = M_CA1_Detail_Repo.findByAcctnumber(acctNo); if (la1Entity != null
	 * && la1Entity.getReport_date() != null) { String formattedDate = new
	 * SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReport_date());
	 * mv.addObject("asondate", formattedDate); } mv.addObject("Data", la1Entity); }
	 * 
	 * mv.addObject("displaymode", "edit"); mv.addObject("formmode", formMode !=
	 * null ? formMode : "edit"); return mv; }
	 */
	




	/*
	 * public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	 * ModelAndView mv = new ModelAndView("BRRS/M_CA1"); // ✅ match the report name
	 * 
	 * if (acctNo != null) { M_CA1_Detail_Entity la1Entity =
	 * M_CA1_Detail_Repo.findByAcctnumber(acctNo); if (la1Entity != null &&
	 * la1Entity.getReport_date() != null) { String formattedDate = new
	 * SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReport_date());
	 * mv.addObject("asondate", formattedDate); System.out.println(formattedDate); }
	 * mv.addObject("Data", la1Entity); }
	 * 
	 * mv.addObject("displaymode", "edit"); mv.addObject("formmode", formMode !=
	 * null ? formMode : "edit"); return mv; }
	 * 
	 * @Transactional public ResponseEntity<?> updateDetailEdit(HttpServletRequest
	 * request) { try { String acctNo = request.getParameter("acct_number"); String
	 * provisionStr = request.getParameter("acct_balance_in_pula"); String
	 * sanction_limit = request.getParameter("sanction_limit"); String acctName =
	 * request.getParameter("acct_name"); String reportDateStr =
	 * request.getParameter("report_Date");
	 * 
	 * logger.info("Received update for ACCT_NO: {}", acctNo);
	 * 
	 * M_CA1_Detail_Entity existing = M_CA1_Detail_Repo.findByAcctnumber(acctNo); if
	 * (existing == null) { logger.warn("No record found for ACCT_NO: {}", acctNo);
	 * return ResponseEntity.status(HttpStatus.NOT_FOUND).
	 * body("Record not found for update."); }
	 * 
	 * boolean isChanged = false;
	 * 
	 * if (acctName != null && !acctName.isEmpty()) { if (existing.getAcct_name() ==
	 * null || !existing.getAcct_name().equals(acctName)) {
	 * existing.setAcct_name(acctName); isChanged = true;
	 * logger.info("Account name updated to {}", acctName); } }
	 * 
	 * if (provisionStr != null && !provisionStr.isEmpty()) { BigDecimal
	 * newProvision = new BigDecimal(provisionStr); if
	 * (existing.getAcct_balance_in_pula() == null ||
	 * existing.getAcct_balance_in_pula().compareTo(newProvision) != 0) {
	 * existing.setAcct_balance_in_pula(newProvision); isChanged = true;
	 * logger.info("Provision updated to {}", newProvision); } }
	 * 
	 * if (sanction_limit != null && !sanction_limit.isEmpty()) { BigDecimal
	 * newSanctionLimit = new BigDecimal(sanction_limit); if
	 * (existing.getSanction_limit() == null ||
	 * existing.getSanction_limit().compareTo(newSanctionLimit) != 0) {
	 * existing.setSanction_limit(newSanctionLimit); isChanged = true;
	 * logger.info("Sanction limit updated to {}", newSanctionLimit); } }
	 * 
	 * if (isChanged) { M_CA1_Detail_Repo.save(existing);
	 * logger.info("Record updated successfully for account {}", acctNo);
	 * 
	 * // Format date for procedure String formattedDate = new
	 * SimpleDateFormat("dd-MM-yyyy") .format(new
	 * SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));
	 * 
	 * // Run summary procedure after commit
	 * TransactionSynchronizationManager.registerSynchronization(new
	 * TransactionSynchronizationAdapter() {
	 * 
	 * @Override public void afterCommit() { try { logger.
	 * info("Transaction committed — calling BRRS_M_CA1_SUMMARY_PROCEDURE({})",
	 * formattedDate);
	 * jdbcTemplate.update("BEGIN BRRS_M_CA1_SUMMARY_PROCEDURE(?); END;",
	 * formattedDate); logger.info("Procedure executed successfully after commit.");
	 * } catch (Exception e) {
	 * logger.error("Error executing procedure after commit", e); } } });
	 * 
	 * return ResponseEntity.ok("Record updated successfully!"); } else {
	 * logger.info("No changes detected for ACCT_NO: {}", acctNo); return
	 * ResponseEntity.ok("No changes were made."); }
	 * 
	 * } catch (Exception e) { logger.error("Error updating M_CA1 record", e);
	 * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body("Error updating record: " + e.getMessage()); } }
	 */
	
	
	//Archival Email Excel
	public byte[] BRRS_M_CA1ArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		
		List<M_CA1_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

		if (dataList.isEmpty() ) {
			logger.warn("Service: No data found for M_CA1 report. Returning empty result.");
			return new byte[0];
		}

		
		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
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
					M_CA1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

		
					   
		              //REPORT_DATE
						row = sheet.getRow(5);
						Cell cell1 = row.getCell(1);
						if (cell1 == null) {
						    cell1 = row.createCell(1);
						}

						if (record.getREPORT_DATE() != null) {
						    cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
						    cell1.setCellStyle(dateStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}
		                
		             // Row 9
						row = sheet.getRow(8);
					// Column D
					Cell cell3 = row.getCell(2);
					if (record.getR9_AMOUNT() != null) {
						cell3.setCellValue(record.getR9_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
									
					
					//row10
					row = sheet.getRow(9);			
					// Column D 
					 cell3 = row.getCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell3.setCellValue(record.getR10_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row11
					row = sheet.getRow(10);			
					// Column D
					 cell3 = row.getCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell3.setCellValue(record.getR11_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column F 
					 cell3 = row.getCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell3.setCellValue(record.getR12_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row13
					row = sheet.getRow(12);			
					// Column F 
					 cell3 = row.getCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell3.setCellValue(record.getR13_AMOUNT().doubleValue());
			
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

										
					
					

					//row18
					row = sheet.getRow(17);			
					// Column C
					 cell3 = row.getCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell3.setCellValue(record.getR17_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR17_CAR() != null) {
						cell3.setCellValue(record.getR17_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row19
					row = sheet.getRow(18);			
					// Column C
					 cell3 = row.getCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell3.setCellValue(record.getR18_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR18_CAR() != null) {
						cell3.setCellValue(record.getR18_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					//row20
					row = sheet.getRow(19);			
					// Column C 
					 cell3 = row.getCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell3.setCellValue(record.getR19_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR19_CAR() != null) {
						cell3.setCellValue(record.getR19_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					//row21
					row = sheet.getRow(20);			
					// Column C
					 cell3 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell3.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR20_CAR() != null) {
						cell3.setCellValue(record.getR20_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					//row22
					row = sheet.getRow(21);			
					// Column C 
					 cell3 = row.getCell(2);
					if (record.getR21_AMOUNT() != null) {
						cell3.setCellValue(record.getR21_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
							
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR21_CAR() != null) {
						cell3.setCellValue(record.getR21_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA1 EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_CA1_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_CA1EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return BRRS_M_CA1ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		List<M_CA1_Summary_Entity> dataList =
	    		getSummaryByDate(dateformat.parse(todate));

	    
	    if (dataList.isEmpty()) {
	        logger.warn("Service: No data found for BRF2.4 report. Returning empty result.");
	        return new byte[0];
	    }

		

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
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
					M_CA1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					   
		              //REPORT_DATE
						row = sheet.getRow(5);
						Cell cell1 = row.getCell(1);
						if (cell1 == null) {
						    cell1 = row.createCell(1);
						}

						if (record.getREPORT_DATE() != null) {
						    cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
						    cell1.setCellStyle(dateStyle);
						} else {
						    cell1.setCellValue("");
						    cell1.setCellStyle(textStyle);
						}
		                
		             // Row 9
						row = sheet.getRow(8);
					// Column D
					Cell cell3 = row.getCell(2);
					if (record.getR9_AMOUNT() != null) {
						cell3.setCellValue(record.getR9_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
									
					
					//row10
					row = sheet.getRow(9);			
					// Column D 
					 cell3 = row.getCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell3.setCellValue(record.getR10_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row11
					row = sheet.getRow(10);			
					// Column D
					 cell3 = row.getCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell3.setCellValue(record.getR11_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column F 
					 cell3 = row.getCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell3.setCellValue(record.getR12_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row13
					row = sheet.getRow(12);			
					// Column F 
					 cell3 = row.getCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell3.setCellValue(record.getR13_AMOUNT().doubleValue());
			
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

										
					
					

					//row18
					row = sheet.getRow(17);			
					// Column C
					 cell3 = row.getCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell3.setCellValue(record.getR17_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR17_CAR() != null) {
						cell3.setCellValue(record.getR17_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row19
					row = sheet.getRow(18);			
					// Column C
					 cell3 = row.getCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell3.setCellValue(record.getR18_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR18_CAR() != null) {
						cell3.setCellValue(record.getR18_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					//row20
					row = sheet.getRow(19);			
					// Column C 
					 cell3 = row.getCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell3.setCellValue(record.getR19_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR19_CAR() != null) {
						cell3.setCellValue(record.getR19_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					//row21
					row = sheet.getRow(20);			
					// Column C
					 cell3 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell3.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR20_CAR() != null) {
						cell3.setCellValue(record.getR20_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					//row22
					row = sheet.getRow(21);			
					// Column C 
					 cell3 = row.getCell(2);
					if (record.getR21_AMOUNT() != null) {
						cell3.setCellValue(record.getR21_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
							
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR21_CAR() != null) {
						cell3.setCellValue(record.getR21_CAR().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}					
				}


			
				try {
				    workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
				} catch (RuntimeException e) {
				    logger.warn("Skipping formula evaluation due to external references: {}", e.getMessage());
				}
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA1 EMAIL SUMMARY", null, "BRRS_M_CA1_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}


	// =====================================================================
	// ROW MAPPER — M_CA1_Summary_Entity
	// =====================================================================
	class M_CA1SummaryRowMapper implements RowMapper<M_CA1_Summary_Entity> {
		@Override
		public M_CA1_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA1_Summary_Entity obj = new M_CA1_Summary_Entity();
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setR9_REF_PARM(rs.getString("R9_REF_PARM"));
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));
			obj.setR10_REF_PARM(rs.getString("R10_REF_PARM"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
			obj.setR11_REF_PARM(rs.getString("R11_REF_PARM"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_REF_PARM(rs.getString("R12_REF_PARM"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_REF_PARM(rs.getString("R13_REF_PARM"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_REF_PARM(rs.getString("R14_REF_PARM"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR17_REF_PARM(rs.getString("R17_REF_PARM"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR17_CAR(rs.getBigDecimal("R17_CAR"));
			obj.setR18_REF_PARM(rs.getString("R18_REF_PARM"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR18_CAR(rs.getBigDecimal("R18_CAR"));
			obj.setR19_REF_PARM(rs.getString("R19_REF_PARM"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR19_CAR(rs.getBigDecimal("R19_CAR"));
			obj.setR20_REF_PARM(rs.getString("R20_REF_PARM"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR20_CAR(rs.getBigDecimal("R20_CAR"));
			obj.setR21_REF_PARM(rs.getString("R21_REF_PARM"));
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));
			obj.setR21_CAR(rs.getBigDecimal("R21_CAR"));
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
	// ENTITY CLASS — M_CA1_Summary_Entity
	// =====================================================================
	public static class M_CA1_Summary_Entity {
		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;
		private String R9_REF_PARM;
		private String R9_PRODUCT;
		private BigDecimal R9_AMOUNT;
		private String R10_REF_PARM;
		private String R10_PRODUCT;
		private BigDecimal R10_AMOUNT;
		private String R11_REF_PARM;
		private String R11_PRODUCT;
		private BigDecimal R11_AMOUNT;
		private String R12_REF_PARM;
		private String R12_PRODUCT;
		private BigDecimal R12_AMOUNT;
		private String R13_REF_PARM;
		private String R13_PRODUCT;
		private BigDecimal R13_AMOUNT;
		private String R14_REF_PARM;
		private String R14_PRODUCT;
		private BigDecimal R14_AMOUNT;
		private String R17_REF_PARM;
		private String R17_PRODUCT;
		private BigDecimal R17_AMOUNT;
		private BigDecimal R17_CAR;
		private String R18_REF_PARM;
		private String R18_PRODUCT;
		private BigDecimal R18_AMOUNT;
		private BigDecimal R18_CAR;
		private String R19_REF_PARM;
		private String R19_PRODUCT;
		private BigDecimal R19_AMOUNT;
		private BigDecimal R19_CAR;
		private String R20_REF_PARM;
		private String R20_PRODUCT;
		private BigDecimal R20_AMOUNT;
		private BigDecimal R20_CAR;
		private String R21_REF_PARM;
		private String R21_PRODUCT;
		private BigDecimal R21_AMOUNT;
		private BigDecimal R21_CAR;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public Date getREPORT_DATE() { return REPORT_DATE; }
		public void setREPORT_DATE(Date v) { REPORT_DATE = v; }
		public BigDecimal getREPORT_VERSION() { return REPORT_VERSION; }
		public void setREPORT_VERSION(BigDecimal v) { REPORT_VERSION = v; }
		public String getR9_REF_PARM() { return R9_REF_PARM; }
		public void setR9_REF_PARM(String v) { R9_REF_PARM = v; }
		public String getR9_PRODUCT() { return R9_PRODUCT; }
		public void setR9_PRODUCT(String v) { R9_PRODUCT = v; }
		public BigDecimal getR9_AMOUNT() { return R9_AMOUNT; }
		public void setR9_AMOUNT(BigDecimal v) { R9_AMOUNT = v; }
		public String getR10_REF_PARM() { return R10_REF_PARM; }
		public void setR10_REF_PARM(String v) { R10_REF_PARM = v; }
		public String getR10_PRODUCT() { return R10_PRODUCT; }
		public void setR10_PRODUCT(String v) { R10_PRODUCT = v; }
		public BigDecimal getR10_AMOUNT() { return R10_AMOUNT; }
		public void setR10_AMOUNT(BigDecimal v) { R10_AMOUNT = v; }
		public String getR11_REF_PARM() { return R11_REF_PARM; }
		public void setR11_REF_PARM(String v) { R11_REF_PARM = v; }
		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String v) { R11_PRODUCT = v; }
		public BigDecimal getR11_AMOUNT() { return R11_AMOUNT; }
		public void setR11_AMOUNT(BigDecimal v) { R11_AMOUNT = v; }
		public String getR12_REF_PARM() { return R12_REF_PARM; }
		public void setR12_REF_PARM(String v) { R12_REF_PARM = v; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String v) { R12_PRODUCT = v; }
		public BigDecimal getR12_AMOUNT() { return R12_AMOUNT; }
		public void setR12_AMOUNT(BigDecimal v) { R12_AMOUNT = v; }
		public String getR13_REF_PARM() { return R13_REF_PARM; }
		public void setR13_REF_PARM(String v) { R13_REF_PARM = v; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String v) { R13_PRODUCT = v; }
		public BigDecimal getR13_AMOUNT() { return R13_AMOUNT; }
		public void setR13_AMOUNT(BigDecimal v) { R13_AMOUNT = v; }
		public String getR14_REF_PARM() { return R14_REF_PARM; }
		public void setR14_REF_PARM(String v) { R14_REF_PARM = v; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String v) { R14_PRODUCT = v; }
		public BigDecimal getR14_AMOUNT() { return R14_AMOUNT; }
		public void setR14_AMOUNT(BigDecimal v) { R14_AMOUNT = v; }
		public String getR17_REF_PARM() { return R17_REF_PARM; }
		public void setR17_REF_PARM(String v) { R17_REF_PARM = v; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String v) { R17_PRODUCT = v; }
		public BigDecimal getR17_AMOUNT() { return R17_AMOUNT; }
		public void setR17_AMOUNT(BigDecimal v) { R17_AMOUNT = v; }
		public BigDecimal getR17_CAR() { return R17_CAR; }
		public void setR17_CAR(BigDecimal v) { R17_CAR = v; }
		public String getR18_REF_PARM() { return R18_REF_PARM; }
		public void setR18_REF_PARM(String v) { R18_REF_PARM = v; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String v) { R18_PRODUCT = v; }
		public BigDecimal getR18_AMOUNT() { return R18_AMOUNT; }
		public void setR18_AMOUNT(BigDecimal v) { R18_AMOUNT = v; }
		public BigDecimal getR18_CAR() { return R18_CAR; }
		public void setR18_CAR(BigDecimal v) { R18_CAR = v; }
		public String getR19_REF_PARM() { return R19_REF_PARM; }
		public void setR19_REF_PARM(String v) { R19_REF_PARM = v; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String v) { R19_PRODUCT = v; }
		public BigDecimal getR19_AMOUNT() { return R19_AMOUNT; }
		public void setR19_AMOUNT(BigDecimal v) { R19_AMOUNT = v; }
		public BigDecimal getR19_CAR() { return R19_CAR; }
		public void setR19_CAR(BigDecimal v) { R19_CAR = v; }
		public String getR20_REF_PARM() { return R20_REF_PARM; }
		public void setR20_REF_PARM(String v) { R20_REF_PARM = v; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String v) { R20_PRODUCT = v; }
		public BigDecimal getR20_AMOUNT() { return R20_AMOUNT; }
		public void setR20_AMOUNT(BigDecimal v) { R20_AMOUNT = v; }
		public BigDecimal getR20_CAR() { return R20_CAR; }
		public void setR20_CAR(BigDecimal v) { R20_CAR = v; }
		public String getR21_REF_PARM() { return R21_REF_PARM; }
		public void setR21_REF_PARM(String v) { R21_REF_PARM = v; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String v) { R21_PRODUCT = v; }
		public BigDecimal getR21_AMOUNT() { return R21_AMOUNT; }
		public void setR21_AMOUNT(BigDecimal v) { R21_AMOUNT = v; }
		public BigDecimal getR21_CAR() { return R21_CAR; }
		public void setR21_CAR(BigDecimal v) { R21_CAR = v; }
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
	// ROW MAPPER — M_CA1_Archival_Summary_Entity
	// =====================================================================
	class M_CA1ArchivalSummaryRowMapper implements RowMapper<M_CA1_Archival_Summary_Entity> {
		@Override
		public M_CA1_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA1_Archival_Summary_Entity obj = new M_CA1_Archival_Summary_Entity();
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setR9_REF_PARM(rs.getString("R9_REF_PARM"));
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));
			obj.setR10_REF_PARM(rs.getString("R10_REF_PARM"));
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));
			obj.setR11_REF_PARM(rs.getString("R11_REF_PARM"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));
			obj.setR12_REF_PARM(rs.getString("R12_REF_PARM"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));
			obj.setR13_REF_PARM(rs.getString("R13_REF_PARM"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));
			obj.setR14_REF_PARM(rs.getString("R14_REF_PARM"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));
			obj.setR17_REF_PARM(rs.getString("R17_REF_PARM"));
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_AMOUNT(rs.getBigDecimal("R17_AMOUNT"));
			obj.setR17_CAR(rs.getBigDecimal("R17_CAR"));
			obj.setR18_REF_PARM(rs.getString("R18_REF_PARM"));
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_AMOUNT(rs.getBigDecimal("R18_AMOUNT"));
			obj.setR18_CAR(rs.getBigDecimal("R18_CAR"));
			obj.setR19_REF_PARM(rs.getString("R19_REF_PARM"));
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_AMOUNT(rs.getBigDecimal("R19_AMOUNT"));
			obj.setR19_CAR(rs.getBigDecimal("R19_CAR"));
			obj.setR20_REF_PARM(rs.getString("R20_REF_PARM"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));
			obj.setR20_CAR(rs.getBigDecimal("R20_CAR"));
			obj.setR21_REF_PARM(rs.getString("R21_REF_PARM"));
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));
			obj.setR21_CAR(rs.getBigDecimal("R21_CAR"));
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
	// ENTITY CLASS — M_CA1_Archival_Summary_Entity
	// =====================================================================
	public static class M_CA1_Archival_Summary_Entity {
		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;
		private Date reportResubDate;
		private String R9_REF_PARM;
		private String R9_PRODUCT;
		private BigDecimal R9_AMOUNT;
		private String R10_REF_PARM;
		private String R10_PRODUCT;
		private BigDecimal R10_AMOUNT;
		private String R11_REF_PARM;
		private String R11_PRODUCT;
		private BigDecimal R11_AMOUNT;
		private String R12_REF_PARM;
		private String R12_PRODUCT;
		private BigDecimal R12_AMOUNT;
		private String R13_REF_PARM;
		private String R13_PRODUCT;
		private BigDecimal R13_AMOUNT;
		private String R14_REF_PARM;
		private String R14_PRODUCT;
		private BigDecimal R14_AMOUNT;
		private String R17_REF_PARM;
		private String R17_PRODUCT;
		private BigDecimal R17_AMOUNT;
		private BigDecimal R17_CAR;
		private String R18_REF_PARM;
		private String R18_PRODUCT;
		private BigDecimal R18_AMOUNT;
		private BigDecimal R18_CAR;
		private String R19_REF_PARM;
		private String R19_PRODUCT;
		private BigDecimal R19_AMOUNT;
		private BigDecimal R19_CAR;
		private String R20_REF_PARM;
		private String R20_PRODUCT;
		private BigDecimal R20_AMOUNT;
		private BigDecimal R20_CAR;
		private String R21_REF_PARM;
		private String R21_PRODUCT;
		private BigDecimal R21_AMOUNT;
		private BigDecimal R21_CAR;
		private String REPORT_FREQUENCY;
		private String REPORT_CODE;
		private String REPORT_DESC;
		private String ENTITY_FLG;
		private String MODIFY_FLG;
		private String DEL_FLG;

		public Date getREPORT_DATE() { return REPORT_DATE; }
		public void setREPORT_DATE(Date v) { REPORT_DATE = v; }
		public BigDecimal getREPORT_VERSION() { return REPORT_VERSION; }
		public void setREPORT_VERSION(BigDecimal v) { REPORT_VERSION = v; }
		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date v) { reportResubDate = v; }
		public String getR9_REF_PARM() { return R9_REF_PARM; }
		public void setR9_REF_PARM(String v) { R9_REF_PARM = v; }
		public String getR9_PRODUCT() { return R9_PRODUCT; }
		public void setR9_PRODUCT(String v) { R9_PRODUCT = v; }
		public BigDecimal getR9_AMOUNT() { return R9_AMOUNT; }
		public void setR9_AMOUNT(BigDecimal v) { R9_AMOUNT = v; }
		public String getR10_REF_PARM() { return R10_REF_PARM; }
		public void setR10_REF_PARM(String v) { R10_REF_PARM = v; }
		public String getR10_PRODUCT() { return R10_PRODUCT; }
		public void setR10_PRODUCT(String v) { R10_PRODUCT = v; }
		public BigDecimal getR10_AMOUNT() { return R10_AMOUNT; }
		public void setR10_AMOUNT(BigDecimal v) { R10_AMOUNT = v; }
		public String getR11_REF_PARM() { return R11_REF_PARM; }
		public void setR11_REF_PARM(String v) { R11_REF_PARM = v; }
		public String getR11_PRODUCT() { return R11_PRODUCT; }
		public void setR11_PRODUCT(String v) { R11_PRODUCT = v; }
		public BigDecimal getR11_AMOUNT() { return R11_AMOUNT; }
		public void setR11_AMOUNT(BigDecimal v) { R11_AMOUNT = v; }
		public String getR12_REF_PARM() { return R12_REF_PARM; }
		public void setR12_REF_PARM(String v) { R12_REF_PARM = v; }
		public String getR12_PRODUCT() { return R12_PRODUCT; }
		public void setR12_PRODUCT(String v) { R12_PRODUCT = v; }
		public BigDecimal getR12_AMOUNT() { return R12_AMOUNT; }
		public void setR12_AMOUNT(BigDecimal v) { R12_AMOUNT = v; }
		public String getR13_REF_PARM() { return R13_REF_PARM; }
		public void setR13_REF_PARM(String v) { R13_REF_PARM = v; }
		public String getR13_PRODUCT() { return R13_PRODUCT; }
		public void setR13_PRODUCT(String v) { R13_PRODUCT = v; }
		public BigDecimal getR13_AMOUNT() { return R13_AMOUNT; }
		public void setR13_AMOUNT(BigDecimal v) { R13_AMOUNT = v; }
		public String getR14_REF_PARM() { return R14_REF_PARM; }
		public void setR14_REF_PARM(String v) { R14_REF_PARM = v; }
		public String getR14_PRODUCT() { return R14_PRODUCT; }
		public void setR14_PRODUCT(String v) { R14_PRODUCT = v; }
		public BigDecimal getR14_AMOUNT() { return R14_AMOUNT; }
		public void setR14_AMOUNT(BigDecimal v) { R14_AMOUNT = v; }
		public String getR17_REF_PARM() { return R17_REF_PARM; }
		public void setR17_REF_PARM(String v) { R17_REF_PARM = v; }
		public String getR17_PRODUCT() { return R17_PRODUCT; }
		public void setR17_PRODUCT(String v) { R17_PRODUCT = v; }
		public BigDecimal getR17_AMOUNT() { return R17_AMOUNT; }
		public void setR17_AMOUNT(BigDecimal v) { R17_AMOUNT = v; }
		public BigDecimal getR17_CAR() { return R17_CAR; }
		public void setR17_CAR(BigDecimal v) { R17_CAR = v; }
		public String getR18_REF_PARM() { return R18_REF_PARM; }
		public void setR18_REF_PARM(String v) { R18_REF_PARM = v; }
		public String getR18_PRODUCT() { return R18_PRODUCT; }
		public void setR18_PRODUCT(String v) { R18_PRODUCT = v; }
		public BigDecimal getR18_AMOUNT() { return R18_AMOUNT; }
		public void setR18_AMOUNT(BigDecimal v) { R18_AMOUNT = v; }
		public BigDecimal getR18_CAR() { return R18_CAR; }
		public void setR18_CAR(BigDecimal v) { R18_CAR = v; }
		public String getR19_REF_PARM() { return R19_REF_PARM; }
		public void setR19_REF_PARM(String v) { R19_REF_PARM = v; }
		public String getR19_PRODUCT() { return R19_PRODUCT; }
		public void setR19_PRODUCT(String v) { R19_PRODUCT = v; }
		public BigDecimal getR19_AMOUNT() { return R19_AMOUNT; }
		public void setR19_AMOUNT(BigDecimal v) { R19_AMOUNT = v; }
		public BigDecimal getR19_CAR() { return R19_CAR; }
		public void setR19_CAR(BigDecimal v) { R19_CAR = v; }
		public String getR20_REF_PARM() { return R20_REF_PARM; }
		public void setR20_REF_PARM(String v) { R20_REF_PARM = v; }
		public String getR20_PRODUCT() { return R20_PRODUCT; }
		public void setR20_PRODUCT(String v) { R20_PRODUCT = v; }
		public BigDecimal getR20_AMOUNT() { return R20_AMOUNT; }
		public void setR20_AMOUNT(BigDecimal v) { R20_AMOUNT = v; }
		public BigDecimal getR20_CAR() { return R20_CAR; }
		public void setR20_CAR(BigDecimal v) { R20_CAR = v; }
		public String getR21_REF_PARM() { return R21_REF_PARM; }
		public void setR21_REF_PARM(String v) { R21_REF_PARM = v; }
		public String getR21_PRODUCT() { return R21_PRODUCT; }
		public void setR21_PRODUCT(String v) { R21_PRODUCT = v; }
		public BigDecimal getR21_AMOUNT() { return R21_AMOUNT; }
		public void setR21_AMOUNT(BigDecimal v) { R21_AMOUNT = v; }
		public BigDecimal getR21_CAR() { return R21_CAR; }
		public void setR21_CAR(BigDecimal v) { R21_CAR = v; }
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
	// ROW MAPPER — M_CA1_Detail_Entity
	// =====================================================================
	class M_CA1DetailRowMapper implements RowMapper<M_CA1_Detail_Entity> {
		@Override
		public M_CA1_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA1_Detail_Entity obj = new M_CA1_Detail_Entity();
			obj.setCust_id(rs.getString("CUST_ID"));
			obj.setAcct_number(rs.getString("ACCT_NUMBER"));
			obj.setAcct_name(rs.getString("ACCT_NAME"));
			obj.setData_type(rs.getString("DATA_TYPE"));
			obj.setReport_label(rs.getString("REPORT_LABEL"));
			obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
			obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_name(rs.getString("REPORT_NAME"));
			obj.setCreate_user(rs.getString("CREATE_USER"));
			obj.setCreate_time(rs.getDate("CREATE_TIME"));
			obj.setModify_user(rs.getString("MODIFY_USER"));
			obj.setModify_time(rs.getDate("MODIFY_TIME"));
			obj.setVerify_user(rs.getString("VERIFY_USER"));
			obj.setVerify_time(rs.getDate("VERIFY_TIME"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReport_addl_criteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReport_addl_criteria_3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			return obj;
		}
	}

	// =====================================================================
	// ENTITY CLASS — M_CA1_Detail_Entity
	// =====================================================================
	public static class M_CA1_Detail_Entity {
		private String cust_id;
		private String acct_number;
		private String acct_name;
		private String data_type;
		private String report_label;
		private String report_remarks;
		private String modification_remarks;
		private String data_entry_version;
		private BigDecimal acct_balance_in_pula;
		private Date report_date;
		private String report_name;
		private String create_user;
		private Date create_time;
		private String modify_user;
		private Date modify_time;
		private String verify_user;
		private Date verify_time;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;
		private String report_addl_criteria_1;
		private String report_addl_criteria_2;
		private String report_addl_criteria_3;

		public String getCust_id() { return cust_id; }
		public void setCust_id(String v) { cust_id = v; }
		public String getAcct_number() { return acct_number; }
		public void setAcct_number(String v) { acct_number = v; }
		public String getAcct_name() { return acct_name; }
		public void setAcct_name(String v) { acct_name = v; }
		public String getData_type() { return data_type; }
		public void setData_type(String v) { data_type = v; }
		public String getReport_label() { return report_label; }
		public void setReport_label(String v) { report_label = v; }
		public String getReport_remarks() { return report_remarks; }
		public void setReport_remarks(String v) { report_remarks = v; }
		public String getModification_remarks() { return modification_remarks; }
		public void setModification_remarks(String v) { modification_remarks = v; }
		public String getData_entry_version() { return data_entry_version; }
		public void setData_entry_version(String v) { data_entry_version = v; }
		public BigDecimal getAcct_balance_in_pula() { return acct_balance_in_pula; }
		public void setAcct_balance_in_pula(BigDecimal v) { acct_balance_in_pula = v; }
		public Date getReport_date() { return report_date; }
		public void setReport_date(Date v) { report_date = v; }
		public String getReport_name() { return report_name; }
		public void setReport_name(String v) { report_name = v; }
		public String getCreate_user() { return create_user; }
		public void setCreate_user(String v) { create_user = v; }
		public Date getCreate_time() { return create_time; }
		public void setCreate_time(Date v) { create_time = v; }
		public String getModify_user() { return modify_user; }
		public void setModify_user(String v) { modify_user = v; }
		public Date getModify_time() { return modify_time; }
		public void setModify_time(Date v) { modify_time = v; }
		public String getVerify_user() { return verify_user; }
		public void setVerify_user(String v) { verify_user = v; }
		public Date getVerify_time() { return verify_time; }
		public void setVerify_time(Date v) { verify_time = v; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String v) { entity_flg = v; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String v) { modify_flg = v; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String v) { del_flg = v; }
		public String getReport_addl_criteria_1() { return report_addl_criteria_1; }
		public void setReport_addl_criteria_1(String v) { report_addl_criteria_1 = v; }
		public String getReport_addl_criteria_2() { return report_addl_criteria_2; }
		public void setReport_addl_criteria_2(String v) { report_addl_criteria_2 = v; }
		public String getReport_addl_criteria_3() { return report_addl_criteria_3; }
		public void setReport_addl_criteria_3(String v) { report_addl_criteria_3 = v; }
	}

	// =====================================================================
	// ROW MAPPER — M_CA1_Archival_Detail_Entity
	// =====================================================================
	class M_CA1ArchivalDetailRowMapper implements RowMapper<M_CA1_Archival_Detail_Entity> {
		@Override
		public M_CA1_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_CA1_Archival_Detail_Entity obj = new M_CA1_Archival_Detail_Entity();
			obj.setCust_id(rs.getString("CUST_ID"));
			obj.setAcct_number(rs.getString("ACCT_NUMBER"));
			obj.setAcct_name(rs.getString("ACCT_NAME"));
			obj.setData_type(rs.getString("DATA_TYPE"));
			obj.setReport_label(rs.getString("REPORT_LABEL"));
			obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
			obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_name(rs.getString("REPORT_NAME"));
			obj.setCreate_user(rs.getString("CREATE_USER"));
			obj.setCreate_time(rs.getDate("CREATE_TIME"));
			obj.setModify_user(rs.getString("MODIFY_USER"));
			obj.setModify_time(rs.getDate("MODIFY_TIME"));
			obj.setVerify_user(rs.getString("VERIFY_USER"));
			obj.setVerify_time(rs.getDate("VERIFY_TIME"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReport_addl_criteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReport_addl_criteria_3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			return obj;
		}
	}

	// =====================================================================
	// ENTITY CLASS — M_CA1_Archival_Detail_Entity
	// =====================================================================
	public static class M_CA1_Archival_Detail_Entity {
		private String cust_id;
		private String acct_number;
		private String acct_name;
		private String data_type;
		private String report_label;
		private String report_remarks;
		private String modification_remarks;
		private String data_entry_version;
		private BigDecimal acct_balance_in_pula;
		private Date report_date;
		private String report_name;
		private String create_user;
		private Date create_time;
		private String modify_user;
		private Date modify_time;
		private String verify_user;
		private Date verify_time;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;
		private String report_addl_criteria_1;
		private String report_addl_criteria_2;
		private String report_addl_criteria_3;

		public String getCust_id() { return cust_id; }
		public void setCust_id(String v) { cust_id = v; }
		public String getAcct_number() { return acct_number; }
		public void setAcct_number(String v) { acct_number = v; }
		public String getAcct_name() { return acct_name; }
		public void setAcct_name(String v) { acct_name = v; }
		public String getData_type() { return data_type; }
		public void setData_type(String v) { data_type = v; }
		public String getReport_label() { return report_label; }
		public void setReport_label(String v) { report_label = v; }
		public String getReport_remarks() { return report_remarks; }
		public void setReport_remarks(String v) { report_remarks = v; }
		public String getModification_remarks() { return modification_remarks; }
		public void setModification_remarks(String v) { modification_remarks = v; }
		public String getData_entry_version() { return data_entry_version; }
		public void setData_entry_version(String v) { data_entry_version = v; }
		public BigDecimal getAcct_balance_in_pula() { return acct_balance_in_pula; }
		public void setAcct_balance_in_pula(BigDecimal v) { acct_balance_in_pula = v; }
		public Date getReport_date() { return report_date; }
		public void setReport_date(Date v) { report_date = v; }
		public String getReport_name() { return report_name; }
		public void setReport_name(String v) { report_name = v; }
		public String getCreate_user() { return create_user; }
		public void setCreate_user(String v) { create_user = v; }
		public Date getCreate_time() { return create_time; }
		public void setCreate_time(Date v) { create_time = v; }
		public String getModify_user() { return modify_user; }
		public void setModify_user(String v) { modify_user = v; }
		public Date getModify_time() { return modify_time; }
		public void setModify_time(Date v) { modify_time = v; }
		public String getVerify_user() { return verify_user; }
		public void setVerify_user(String v) { verify_user = v; }
		public Date getVerify_time() { return verify_time; }
		public void setVerify_time(Date v) { verify_time = v; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String v) { entity_flg = v; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String v) { modify_flg = v; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String v) { del_flg = v; }
		public String getReport_addl_criteria_1() { return report_addl_criteria_1; }
		public void setReport_addl_criteria_1(String v) { report_addl_criteria_1 = v; }
		public String getReport_addl_criteria_2() { return report_addl_criteria_2; }
		public void setReport_addl_criteria_2(String v) { report_addl_criteria_2 = v; }
		public String getReport_addl_criteria_3() { return report_addl_criteria_3; }
		public void setReport_addl_criteria_3(String v) { report_addl_criteria_3 = v; }
	}

}

