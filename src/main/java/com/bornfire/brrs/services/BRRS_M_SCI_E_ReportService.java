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
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
/*import org.apache.poi.ss.usermodel.FillPatternType;*/
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
/*import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;*/
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional
public class BRRS_M_SCI_E_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SCI_E_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ── JDBC query methods ──────────────────────────────────────────────────

	private List<M_SCI_E_Summary_Entity> getSummaryByDate(Date date) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[]{date}, new M_SCI_E_SummaryRowMapper());
	}

	private List<M_SCI_E_Archival_Summary_Entity> getArchivalSummaryByDateAndVersion(Date date, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{date, version}, new M_SCI_E_Archival_SummaryRowMapper());
	}

	private List<M_SCI_E_Archival_Summary_Entity> getArchivalSummaryWithVersion() {
		String sql = "SELECT * FROM BRRS_M_SCI_E_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_SCI_E_Archival_SummaryRowMapper());
	}

	private List<M_SCI_E_RESUB_Summary_Entity> getResubSummaryByDateAndVersion(Date date, BigDecimal version) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_RESUB_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{date, version}, new M_SCI_E_RESUB_SummaryRowMapper());
	}

	private List<M_SCI_E_Detail_Entity> getDetailByDate(Date date) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[]{date}, new M_SCI_E_DetailRowMapper());
	}

	private int getDetailCount(Date date) {
		String sql = "SELECT COUNT(*) FROM BRRS_M_SCI_E_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[]{date}, Integer.class);
		return count != null ? count : 0;
	}

	private List<M_SCI_E_Detail_Entity> getDetailByLabelAndCriteria(String label, String criteria, Date date) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[]{label, criteria, date}, new M_SCI_E_DetailRowMapper());
	}

	private List<M_SCI_E_Archival_Detail_Entity> getArchivalDetailByDateAndVersion(Date date, String version) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{date, version}, new M_SCI_E_Archival_DetailRowMapper());
	}

	private List<M_SCI_E_Archival_Detail_Entity> getArchivalDetailByLabelAndCriteria(String label, String criteria, Date date, String version) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{label, criteria, date, version}, new M_SCI_E_Archival_DetailRowMapper());
	}

	private List<M_SCI_E_RESUB_Detail_Entity> getResubDetailByDateAndVersion(Date date, String version) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_RESUB_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{date, version}, new M_SCI_E_RESUB_DetailRowMapper());
	}

	private List<M_SCI_E_RESUB_Detail_Entity> getResubDetailByLabelAndCriteria(String label, String criteria, Date date, String version) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_RESUB_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND TRUNC(REPORT_DATE) = TRUNC(?) AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[]{label, criteria, date, version}, new M_SCI_E_RESUB_DetailRowMapper());
	}

	private M_SCI_E_Summary_Entity findSummaryByDate(Date date) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		List<M_SCI_E_Summary_Entity> list = jdbcTemplate.query(sql, new Object[]{date}, new M_SCI_E_SummaryRowMapper());
		if (list.isEmpty()) throw new RuntimeException("Record not found for REPORT_DATE: " + date);
		return list.get(0);
	}

	private M_SCI_E_Detail_Entity findDetailByAcctNumber(String acctNo) {
		String sql = "SELECT * FROM BRRS_M_SCI_E_DETAILTABLE WHERE ACCT_NUMBER = ?";
		List<M_SCI_E_Detail_Entity> list = jdbcTemplate.query(sql, new Object[]{acctNo}, new M_SCI_E_DetailRowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	// ── End of JDBC query methods ───────────────────────────────────────────

	public ModelAndView getM_SCI_EView(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,     // kept but not used
	        Pageable pageable,
	        String type,
	        BigDecimal version,HttpServletRequest req1,Model md) {

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

	            List<M_SCI_E_Archival_Summary_Entity> summaryList =
                    getArchivalSummaryByDateAndVersion(d1, version);

	            System.out.println("Archival Summary Size : " + summaryList.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", summaryList);
	        }

	        /* ---------- RESUB SUMMARY ---------- */
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_SCI_E_RESUB_Summary_Entity> summaryList =
                    getResubSummaryByDateAndVersion(d1, version);

	            System.out.println("Resub Summary Size : " + summaryList.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", summaryList);
	        }

	        /* ---------- NORMAL SUMMARY ---------- */
	        else {

	            List<M_SCI_E_Summary_Entity> summaryList =
                    getSummaryByDate(d1);

	            System.out.println("Normal Summary Size : " + summaryList.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", summaryList);
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_SCI_E");
	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}
	
	
	
		public ModelAndView getM_SCI_EcurrentDtl(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String filter,
	        String type,
	        String version,HttpServletRequest req1,Model md) {

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

	        String rowId = null;
	        String columnId = null;

	        // ✅ Split filter string into rowId & columnId
	        if (filter != null && filter.contains(",")) {
	            String[] parts = filter.split(",");
	            if (parts.length >= 2) {
	                rowId = parts[0];
	                columnId = parts[1];
	            }
	        }

	        /* =========================================================
	           ARCHIVAL DETAIL
	        ========================================================= */
	        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	            List<M_SCI_E_Archival_Detail_Entity> T1Dt1;

	            if (rowId != null && columnId != null) {
	                T1Dt1 = getArchivalDetailByLabelAndCriteria(rowId, columnId, parsedDate, version);
	            } else {
	                T1Dt1 = getArchivalDetailByDateAndVersion(parsedDate, version);
	            }

	            mv.addObject("reportdetails", T1Dt1);
	            System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
	        }

	        /* =========================================================
	           RESUB DETAIL  ✅ ADDED
	        ========================================================= */
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_SCI_E_RESUB_Detail_Entity> T1Dt1;

	            if (rowId != null && columnId != null) {
	                T1Dt1 = getResubDetailByLabelAndCriteria(rowId, columnId, parsedDate, version);
	            } else {
	                T1Dt1 = getResubDetailByDateAndVersion(parsedDate, version);
	            }

	            mv.addObject("reportdetails", T1Dt1);
	            System.out.println("RESUB COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
	        }

	        /* =========================================================
	           CURRENT DETAIL
	        ========================================================= */
	        else {

	            List<M_SCI_E_Detail_Entity> T1Dt1;

	            if (rowId != null && columnId != null) {
	                T1Dt1 = getDetailByLabelAndCriteria(rowId, columnId, parsedDate);
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
	    mv.setViewName("BRRS/M_SCI_E");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("currentPage", currentPage);
	    mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("reportsflag", "reportsflag");
	    mv.addObject("menu", reportId);

	    return mv;
	}
	
	
		//Archival View
	public List<Object[]> getM_SCI_EArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SCI_E_Archival_Summary_Entity> repoData = getArchivalSummaryWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SCI_E_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version(), 
							 entity.getReportResubDate()
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SCI_E_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_SCI_E  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}
	

	public List<Object[]> getM_SCI_EResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<M_SCI_E_Archival_Summary_Entity> latestArchivalList =
	        		getArchivalSummaryWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (M_SCI_E_Archival_Summary_Entity entity : latestArchivalList) {
	                resubList.add(new Object[] {
	                    entity.getReport_date(),
	                    entity.getReport_version(),
	                    entity.getReportResubDate()
	                });
	            }
	            System.out.println("Fetched " + resubList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }

	    } catch (Exception e) {
	        System.err.println("Error fetching M_SCI_E Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}
	
	
	
	public byte[] getM_SCI_EDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_SCI_E Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getM_SCI_EDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SCI_EDetails");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME","MONTHLY_INT",  "CREDIT_EQUIVALENT","DEBIT_EQUIVALENT", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4 || i == 5) {  // MONTHLY_INT (3) and CREDIT_EQUIVALENT (4) nd DEBIT_EQUIVALENT(5)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_SCI_E_Detail_Entity> reportData = getDetailByDate(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SCI_E_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// MONTHLY_INT (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getMonthlyInt() != null) {
						balanceCell.setCellValue(item.getMonthlyInt().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					
					// CREDIT_EQUIVALENT (right aligned, 3 decimal places)
					 balanceCell = row.createCell(4);
					if (item.getCreditEquivalent() != null) {
						balanceCell.setCellValue(item.getCreditEquivalent().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);


					// CREDIT_EQUIVALENT (right aligned, 3 decimal places)
					 balanceCell = row.createCell(5);
					if (item.getDebitEquivalent() != null) {
						balanceCell.setCellValue(item.getDebitEquivalent().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);


					row.createCell(6).setCellValue(item.getReportLable());
					row.createCell(7).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(8)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 9; j++) {
					    if (j != 3 && j != 4 && j != 5) {
					        row.getCell(j).setCellStyle(dataStyle);
					    }
					}
				}
			} else {
				logger.info("No data found for M_SCI_E — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_SCI_E Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	
	public byte[] getM_SCI_EDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for M_SCI_E ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_SCI_EDetail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME","MONTHLY_INT",  "CREDIT_EQUIVALENT","DEBIT_EQUIVALENT", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4 || i == 5) {  // MONTHLY_INT (3) and CREDIT_EQUIVALENT (4) nd DEBIT_EQUIVALENT(5)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}


// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_SCI_E_Archival_Detail_Entity> reportData = getArchivalDetailByDateAndVersion(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SCI_E_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					 row.createCell(2).setCellValue(item.getAcctName()); 

/// MONTHLY_INT (right aligned, 3 decimal places)
						Cell balanceCell = row.createCell(3);
						if (item.getMonthlyInt() != null) {
							balanceCell.setCellValue(item.getMonthlyInt().doubleValue());
						} else {
							balanceCell.setCellValue(0);
						}
						balanceCell.setCellStyle(balanceStyle);
						
						// CREDIT_EQUIVALENT (right aligned, 3 decimal places)
						 balanceCell = row.createCell(4);
						if (item.getCreditEquivalent() != null) {
							balanceCell.setCellValue(item.getCreditEquivalent().doubleValue());
						} else {
							balanceCell.setCellValue(0);
						}
						balanceCell.setCellStyle(balanceStyle);


						// CREDIT_EQUIVALENT (right aligned, 3 decimal places)
						 balanceCell = row.createCell(5);
						if (item.getDebitEquivalent() != null) {
							balanceCell.setCellValue(item.getDebitEquivalent().doubleValue());
						} else {
							balanceCell.setCellValue(0);
						}
						balanceCell.setCellStyle(balanceStyle);

					
					row.createCell(6).setCellValue(item.getReportLable());
					row.createCell(7).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(8)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");


					// Apply data style for all other cells
					for (int j = 0; j < 9; j++) {
						   if (j != 3 && j != 4 && j != 5) {
					        row.getCell(j).setCellStyle(dataStyle);
					    }
					}
				}
			} else {
				logger.info("No data found for M_SCI_E — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_SCI_EExcel", e);
			return new byte[0];
		}
	}
	
	
	
	@Transactional
	public void updateReport(M_SCI_E_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_SCI_E_Summary_Entity existing =
	            findSummaryByDate(updatedEntity.getReport_date());

	    // Audit old copy
	    M_SCI_E_Summary_Entity oldcopy = new M_SCI_E_Summary_Entity();
	    BeanUtils.copyProperties(existing, oldcopy);

	    // Only allowed R-numbers
	    int[] allowedIndexes = {45, 46, 54, 58, 59, 60, 66, 67, 68, 74, 85};

	    try {

	        for (int i : allowedIndexes) {

	            String field = "month";

	            String getterName = "getR" + i + "_" + field;
	            String setterName = "setR" + i + "_" + field;

	            try {

	                Method getter =
	                        M_SCI_E_Summary_Entity.class.getMethod(getterName);

	                Method setter =
	                        M_SCI_E_Summary_Entity.class.getMethod(
	                                setterName,
	                                getter.getReturnType()
	                        );

	                Object newValue = getter.invoke(updatedEntity);

	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {

	                // Safely skip if field doesn't exist
	                continue;
	            }
	        }

	    } catch (Exception e) {

	        throw new RuntimeException(
	                "Error while updating report fields", e);
	    }

	    // Check changes before save
	    String changes = auditService.getChanges(oldcopy, existing);

	    // Save entity
	    jdbcTemplate.update(
	            "UPDATE BRRS_M_SCI_E_SUMMARYTABLE SET R45_MONTH=?, R46_MONTH=?, R54_MONTH=?, R58_MONTH=?, R59_MONTH=?, R60_MONTH=?, R66_MONTH=?, R67_MONTH=?, R68_MONTH=?, R74_MONTH=?, R85_MONTH=? WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
	            existing.getR45_month(), existing.getR46_month(), existing.getR54_month(),
	            existing.getR58_month(), existing.getR59_month(), existing.getR60_month(),
	            existing.getR66_month(), existing.getR67_month(), existing.getR68_month(),
	            existing.getR74_month(), existing.getR85_month(),
	            existing.getReport_date());

	    // Audit only if changes found
	    if (!changes.isEmpty()) {

	        auditService.compareEntitiesmanual(
	                oldcopy,
	                existing,
	                updatedEntity.getReport_date().toString(),
	                "M SCI E Summary Screen",
	                "BRRS_M_SCI_E_SUMMARY"
	        );
	    }
	}
		
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_SCI_E"); 

		if (acctNo != null) {
			M_SCI_E_Detail_Entity msciEntity = findDetailByAcctNumber(acctNo);
			if (msciEntity != null && msciEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(msciEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("msciData", msciEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String monthlyInt = request.getParameter("monthlyInt");
			String creditEquivalent = request.getParameter("creditEquivalent");
			String debitEquivalent = request.getParameter("debitEquivalent");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_SCI_E_Detail_Entity existing = findDetailByAcctNumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}
			
			 // Create old copy for audit comparison
			M_SCI_E_Detail_Entity oldcopy = new M_SCI_E_Detail_Entity();
	        BeanUtils.copyProperties(existing, oldcopy);

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
					existing.setAcctName(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			 if (monthlyInt != null && !monthlyInt.isEmpty()) {
		            BigDecimal newmonthlyInt = new BigDecimal(monthlyInt);
		            if (existing.getMonthlyInt()  == null ||
		                existing.getMonthlyInt().compareTo(newmonthlyInt) != 0) {
		            	 existing.setMonthlyInt(newmonthlyInt);
		                isChanged = true;
		                logger.info("Balance updated to {}", newmonthlyInt);
		            }
		        }
		        
			 if (creditEquivalent != null && !creditEquivalent.isEmpty()) {
		            BigDecimal newbalanceAmt = new BigDecimal(creditEquivalent);
		            if (existing.getCreditEquivalent()  == null ||
		                existing.getCreditEquivalent().compareTo(newbalanceAmt) != 0) {
		            	 existing.setCreditEquivalent(newbalanceAmt);
		                isChanged = true;
		                logger.info("Balance updated to {}", newbalanceAmt);
		            }
		        }
			 
			   
			 if (debitEquivalent != null && !debitEquivalent.isEmpty()) {
		            BigDecimal newdebitEquivalent = new BigDecimal(debitEquivalent);
		            if (existing.getDebitEquivalent()  == null ||
		                existing.getDebitEquivalent().compareTo(newdebitEquivalent) != 0) {
		            	 existing.setDebitEquivalent(newdebitEquivalent);
		                isChanged = true;
		                logger.info("Balance updated to {}", newdebitEquivalent);
		            }
		        }
			 
			if (isChanged) {
				jdbcTemplate.update(
						"UPDATE BRRS_M_SCI_E_DETAILTABLE SET ACCT_NAME=?, MONTHLY_INT=?, CREDIT_EQUIVALENT=?, DEBIT_EQUIVALENT=? WHERE ACCT_NUMBER=?",
						existing.getAcctName(), existing.getMonthlyInt(),
						existing.getCreditEquivalent(), existing.getDebitEquivalent(),
						existing.getAcctNumber());
				
				  // Audit comparison
	            auditService.compareEntitiesmanual(
	                    oldcopy,
	                    existing,
	                    acctNo,
	                    "M_SCI_E Detail Screen",
	                    "BRRS_M_SCI_E_DETAIL"
	            );
				
				
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				
				TransactionSynchronizationManager.registerSynchronization(
	                    new TransactionSynchronizationAdapter() {

	                        @Override
	                        public void afterCommit() {
	                            try {

	                                logger.info(
	                                        "Transaction committed — calling BRRS_M_SCI_E_SUMMARY_PROCEDURE({})",
	                                        formattedDate);

	                                jdbcTemplate.update(
	                                        "BEGIN BRRS_M_SCI_E_SUMMARY_PROCEDURE(?); END;",
	                                        formattedDate);

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
			logger.error("Error updating M_SCI_E record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	
	


//Normal format Excel

public byte[] getM_SCI_EExcel(String filename, String reportId, String fromdate, String todate, String currency,
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
return getExcelM_SCI_EARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);

try {
// ✅ Redirecting to Resub Excel
return BRRS_M_SCI_EResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} else {

if ("email".equalsIgnoreCase(format) && version == null) {
logger.info("Got format as Email");
logger.info("Service: Generating Email report for version {}", version);
return BRRS_M_SCI_EEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
} else {

// Fetch data

List<M_SCI_E_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_SCI_E report. Returning empty result.");
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
M_SCI_E_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}




//row7
// Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 11
row = sheet.getRow(10);


Cell cellC,cellD;    

// row11

// Column 3 - month
cellC = row.createCell(2);
if (record.getR11_month() != null) {
cellC.setCellValue(record.getR11_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR11_ytd() != null) {
cellD.setCellValue(record.getR11_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row12
row = sheet.getRow(11);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR12_month() != null) {
cellC.setCellValue(record.getR12_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR12_ytd() != null) {
cellD.setCellValue(record.getR12_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row13
row = sheet.getRow(12);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR13_month() != null) {
cellC.setCellValue(record.getR13_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR13_ytd() != null) {
cellD.setCellValue(record.getR13_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row15
row = sheet.getRow(14); // Row index for R15 (0-based index)

// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR15_month() != null) {
cellC.setCellValue(record.getR15_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR15_ytd() != null) {
cellD.setCellValue(record.getR15_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row16
row = sheet.getRow(15);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR16_month() != null) {
cellC.setCellValue(record.getR16_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR16_ytd() != null) {
cellD.setCellValue(record.getR16_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row17
row = sheet.getRow(16);



cellC = row.createCell(2);
if (record.getR17_month() != null) {
cellC.setCellValue(record.getR17_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR17_ytd() != null) {
cellD.setCellValue(record.getR17_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row18
row = sheet.getRow(17);



cellC = row.createCell(2);
if (record.getR18_month() != null) {
cellC.setCellValue(record.getR18_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR18_ytd() != null) {
cellD.setCellValue(record.getR18_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row19
row = sheet.getRow(18);



cellC = row.createCell(2);
if (record.getR19_month() != null) {
cellC.setCellValue(record.getR19_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR19_ytd() != null) {
cellD.setCellValue(record.getR19_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row20
row = sheet.getRow(19);



cellC = row.createCell(2);
if (record.getR20_month() != null) {
cellC.setCellValue(record.getR20_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR20_ytd() != null) {
cellD.setCellValue(record.getR20_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row21
row = sheet.getRow(20);



cellC = row.createCell(2);
if (record.getR21_month() != null) {
cellC.setCellValue(record.getR21_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR21_ytd() != null) {
cellD.setCellValue(record.getR21_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row22
row = sheet.getRow(21);



cellC = row.createCell(2);
if (record.getR22_month() != null) {
cellC.setCellValue(record.getR22_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR22_ytd() != null) {
cellD.setCellValue(record.getR22_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row23
row = sheet.getRow(22);



cellC = row.createCell(2);
if (record.getR23_month() != null) {
cellC.setCellValue(record.getR23_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR23_ytd() != null) {
cellD.setCellValue(record.getR23_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row24
row = sheet.getRow(23);



cellC = row.createCell(2);
if (record.getR24_month() != null) {
cellC.setCellValue(record.getR24_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR24_ytd() != null) {
cellD.setCellValue(record.getR24_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row25
row = sheet.getRow(24);



cellC = row.createCell(2);
if (record.getR25_month() != null) {
cellC.setCellValue(record.getR25_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR25_ytd() != null) {
cellD.setCellValue(record.getR25_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row26
row = sheet.getRow(25);

// Column 1 - product name


// Column 3 - month
cellC = row.createCell(2);
if (record.getR26_month() != null) {
cellC.setCellValue(record.getR26_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR26_ytd() != null) {
cellD.setCellValue(record.getR26_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row27
row = sheet.getRow(26);




// Column 3 - month
cellC = row.createCell(2);
if (record.getR27_month() != null) {
cellC.setCellValue(record.getR27_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR27_ytd() != null) {
cellD.setCellValue(record.getR27_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row29
row = sheet.getRow(28);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR29_month() != null) {
cellC.setCellValue(record.getR29_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR29_ytd() != null) {
cellD.setCellValue(record.getR29_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row30
row = sheet.getRow(29);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR30_month() != null) {
cellC.setCellValue(record.getR30_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR30_ytd() != null) {
cellD.setCellValue(record.getR30_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row31
row = sheet.getRow(30);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR31_month() != null) {
cellC.setCellValue(record.getR31_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR31_ytd() != null) {
cellD.setCellValue(record.getR31_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 32 -------------------------
row = sheet.getRow(31);
// Column 1 - product name

// Column 3 - month
cellC = row.createCell(2);
if (record.getR32_month() != null) {
cellC.setCellValue(record.getR32_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR32_ytd() != null) {
cellD.setCellValue(record.getR32_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 33 -------------------------
row = sheet.getRow(32);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR33_month() != null) {
cellC.setCellValue(record.getR33_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR33_ytd() != null) {
cellD.setCellValue(record.getR33_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 34 -------------------------
row = sheet.getRow(33);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR34_month() != null) {
cellC.setCellValue(record.getR34_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR34_ytd() != null) {
cellD.setCellValue(record.getR34_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 35 -------------------------
row = sheet.getRow(34);

cellC = row.createCell(2);
if (record.getR35_month() != null) {
cellC.setCellValue(record.getR35_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR35_ytd() != null) {
cellD.setCellValue(record.getR35_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 36 -------------------------
row = sheet.getRow(35);


cellC = row.createCell(2);
if (record.getR36_month() != null) {
cellC.setCellValue(record.getR36_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR36_ytd() != null) {
cellD.setCellValue(record.getR36_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// ------------------------- Row 38 -------------------------
row = sheet.getRow(37);



cellC = row.createCell(2);
if (record.getR38_month() != null) {
cellC.setCellValue(record.getR38_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR38_ytd() != null) {
cellD.setCellValue(record.getR38_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row39
row = sheet.getRow(38);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR39_month() != null) {
cellC.setCellValue(record.getR39_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR39_ytd() != null) {
cellD.setCellValue(record.getR39_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row42
row = sheet.getRow(41);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR42_month() != null) {
cellC.setCellValue(record.getR42_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR42_ytd() != null) {
cellD.setCellValue(record.getR42_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row43
row = sheet.getRow(42);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR43_month() != null) {
cellC.setCellValue(record.getR43_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR43_ytd() != null) {
cellD.setCellValue(record.getR43_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row45
row = sheet.getRow(44);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR45_month() != null) {
cellC.setCellValue(record.getR45_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR45_ytd() != null) {
cellD.setCellValue(record.getR45_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row46
row = sheet.getRow(45);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR46_month() != null) {
cellC.setCellValue(record.getR46_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR46_ytd() != null) {
cellD.setCellValue(record.getR46_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row47
row = sheet.getRow(46);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR47_month() != null) {
cellC.setCellValue(record.getR47_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR47_ytd() != null) {
cellD.setCellValue(record.getR47_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row50
row = sheet.getRow(49);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR50_month() != null) {
cellC.setCellValue(record.getR50_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR50_ytd() != null) {
cellD.setCellValue(record.getR50_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row51
row = sheet.getRow(50);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR51_month() != null) {
cellC.setCellValue(record.getR51_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR51_ytd() != null) {
cellD.setCellValue(record.getR51_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row52
row = sheet.getRow(51);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR52_month() != null) {
cellC.setCellValue(record.getR52_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR52_ytd() != null) {
cellD.setCellValue(record.getR52_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row53
row = sheet.getRow(52);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR53_month() != null) {
cellC.setCellValue(record.getR53_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR53_ytd() != null) {
cellD.setCellValue(record.getR53_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row54
row = sheet.getRow(53);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR54_month() != null) {
cellC.setCellValue(record.getR54_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR54_ytd() != null) {
cellD.setCellValue(record.getR54_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row55
row = sheet.getRow(54);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR55_month() != null) {
cellC.setCellValue(record.getR55_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR55_ytd() != null) {
cellD.setCellValue(record.getR55_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row58
row = sheet.getRow(57);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR58_month() != null) {
cellC.setCellValue(record.getR58_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR58_ytd() != null) {
cellD.setCellValue(record.getR58_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row59
row = sheet.getRow(58);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR59_month() != null) {
cellC.setCellValue(record.getR59_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR59_ytd() != null) {
cellD.setCellValue(record.getR59_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row60
row = sheet.getRow(59);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR60_month() != null) {
cellC.setCellValue(record.getR60_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR60_ytd() != null) {
cellD.setCellValue(record.getR60_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




// row63
row = sheet.getRow(62);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR63_month() != null) {
cellC.setCellValue(record.getR63_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR63_ytd() != null) {
cellD.setCellValue(record.getR63_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row64
row = sheet.getRow(63);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR64_month() != null) {
cellC.setCellValue(record.getR64_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR64_ytd() != null) {
cellD.setCellValue(record.getR64_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row65
row = sheet.getRow(64);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR65_month() != null) {
cellC.setCellValue(record.getR65_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR65_ytd() != null) {
cellD.setCellValue(record.getR65_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row66
row = sheet.getRow(65);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR66_month() != null) {
cellC.setCellValue(record.getR66_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR66_ytd() != null) {
cellD.setCellValue(record.getR66_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row67
row = sheet.getRow(66);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR67_month() != null) {
cellC.setCellValue(record.getR67_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR67_ytd() != null) {
cellD.setCellValue(record.getR67_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row68
row = sheet.getRow(67);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR68_month() != null) {
cellC.setCellValue(record.getR68_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR68_ytd() != null) {
cellD.setCellValue(record.getR68_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row69
row = sheet.getRow(68);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR69_month() != null) {
cellC.setCellValue(record.getR69_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR69_ytd() != null) {
cellD.setCellValue(record.getR69_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row71
row = sheet.getRow(70);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR71_month() != null) {
cellC.setCellValue(record.getR71_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR71_ytd() != null) {
cellD.setCellValue(record.getR71_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row72
row = sheet.getRow(71);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR72_month() != null) {
cellC.setCellValue(record.getR72_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR72_ytd() != null) {
cellD.setCellValue(record.getR72_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row73
row = sheet.getRow(72);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR73_month() != null) {
cellC.setCellValue(record.getR73_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR73_ytd() != null) {
cellD.setCellValue(record.getR73_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row74
row = sheet.getRow(73);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR74_month() != null) {
cellC.setCellValue(record.getR74_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR74_ytd() != null) {
cellD.setCellValue(record.getR74_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row76
row = sheet.getRow(75);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR76_month() != null) {
cellC.setCellValue(record.getR76_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR76_ytd() != null) {
cellD.setCellValue(record.getR76_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row77
row = sheet.getRow(76);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR77_month() != null) {
cellC.setCellValue(record.getR77_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR77_ytd() != null) {
cellD.setCellValue(record.getR77_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row78
row = sheet.getRow(77);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR78_month() != null) {
cellC.setCellValue(record.getR78_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR78_ytd() != null) {
cellD.setCellValue(record.getR78_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row79
row = sheet.getRow(78);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR79_month() != null) {
cellC.setCellValue(record.getR79_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR79_ytd() != null) {
cellD.setCellValue(record.getR79_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row80
row = sheet.getRow(79);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR80_month() != null) {
cellC.setCellValue(record.getR80_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR80_ytd() != null) {
cellD.setCellValue(record.getR80_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row83
row = sheet.getRow(82);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR83_month() != null) {
cellC.setCellValue(record.getR83_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR83_ytd() != null) {
cellD.setCellValue(record.getR83_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row85
row = sheet.getRow(84);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR85_month() != null) {
cellC.setCellValue(record.getR85_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR85_ytd() != null) {
cellD.setCellValue(record.getR85_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


}
workbook.setForceFormulaRecalculation(true);
} else {

}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());


//audit service summary format

ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
							if (attrs != null) {
								HttpServletRequest request = attrs.getRequest();
								String userid = (String) request.getSession().getAttribute("USERID");
								auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SCI_E SUMMARY", null, "BRRS_M_SCI_E_SUMMARYTABLE");
							}

return out.toByteArray();
}	
}
}
}

// Normal Email Excel
public byte[] BRRS_M_SCI_EEmailExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Email Excel generation process in memory.");

if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
try {
// Redirecting to Archival
return BRRS_M_SCI_EARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);

try {
// ✅ Redirecting to Resub Excel
return BRRS_M_SCI_EEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} 
else {
List<M_SCI_E_Summary_Entity> dataList = getSummaryByDate(dateformat.parse(todate));

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_SCI_E report. Returning empty result.");
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
M_SCI_E_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 11
row = sheet.getRow(10);

Cell cellC,cellD;   


//--R10 1. Total interest and fee income from loans and advances (sum of lines (i) to (viii))

//---R11 (i) Central Government (Government of Botswana)


//row11

// Column 3 - month
cellC = row.createCell(2);
if (record.getR11_month() != null) {
cellC.setCellValue(record.getR11_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR11_ytd() != null) {
cellD.setCellValue(record.getR11_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R12 (ii) Local Government

// row12
row = sheet.getRow(11);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR12_month() != null) {
cellC.setCellValue(record.getR12_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR12_ytd() != null) {
cellD.setCellValue(record.getR12_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R13 (iii) Public Non-Financial Corporations


//row13
row = sheet.getRow(12);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR13_month() != null) {
cellC.setCellValue(record.getR13_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR13_ytd() != null) {
cellD.setCellValue(record.getR13_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//---R14 (iv) Other Non-Financial Corporations (Private business enterprises) 

//------R15 a) Agriculture, Forestry, Fishing

//row15
row = sheet.getRow(14); // Row index for R15 (0-based index)

// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR15_month() != null) {
cellC.setCellValue(record.getR15_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR15_ytd() != null) {
cellD.setCellValue(record.getR15_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R16 b) Mining and Quarrying
// row16
row = sheet.getRow(15);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR16_month() != null) {
cellC.setCellValue(record.getR16_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR16_ytd() != null) {
cellD.setCellValue(record.getR16_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R17 c) Manufacturing
// row17
row = sheet.getRow(16);



cellC = row.createCell(2);
if (record.getR17_month() != null) {
cellC.setCellValue(record.getR17_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR17_ytd() != null) {
cellD.setCellValue(record.getR17_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R18 d) Construction

//row18
row = sheet.getRow(17);



cellC = row.createCell(2);
if (record.getR18_month() != null) {
cellC.setCellValue(record.getR18_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR18_ytd() != null) {
cellD.setCellValue(record.getR18_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R19 e) Commercial real estate

// row19
row = sheet.getRow(18);



cellC = row.createCell(2);
if (record.getR19_month() != null) {
cellC.setCellValue(record.getR19_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR19_ytd() != null) {
cellD.setCellValue(record.getR19_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R20 f) Electricity

// row20
row = sheet.getRow(19);



cellC = row.createCell(2);
if (record.getR20_month() != null) {
cellC.setCellValue(record.getR20_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR20_ytd() != null) {
cellD.setCellValue(record.getR20_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R21 g) Water

//row21
row = sheet.getRow(20);



cellC = row.createCell(2);
if (record.getR21_month() != null) {
cellC.setCellValue(record.getR21_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR21_ytd() != null) {
cellD.setCellValue(record.getR21_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R22 h) Telecommunication and Post
// row22
row = sheet.getRow(21);



cellC = row.createCell(2);
if (record.getR22_month() != null) {
cellC.setCellValue(record.getR22_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR22_ytd() != null) {
cellD.setCellValue(record.getR22_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R23 i) Tourism and hotels

//row23
row = sheet.getRow(22);



cellC = row.createCell(2);
if (record.getR23_month() != null) {
cellC.setCellValue(record.getR23_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR23_ytd() != null) {
cellD.setCellValue(record.getR23_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R24 j) Transport and Storage

//row24
row = sheet.getRow(23);



cellC = row.createCell(2);
if (record.getR24_month() != null) {
cellC.setCellValue(record.getR24_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR24_ytd() != null) {
cellD.setCellValue(record.getR24_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R25 k) Trade, restaurants and bars

// row25
row = sheet.getRow(24);



cellC = row.createCell(2);
if (record.getR25_month() != null) {
cellC.setCellValue(record.getR25_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR25_ytd() != null) {
cellD.setCellValue(record.getR25_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//------R26 l) Business Services

// row26
row = sheet.getRow(25);

// Column 1 - product name


// Column 3 - month
cellC = row.createCell(2);
if (record.getR26_month() != null) {
cellC.setCellValue(record.getR26_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR26_ytd() != null) {
cellD.setCellValue(record.getR26_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R27 m) Other Community, Social and Personal Services
// row27
row = sheet.getRow(26);




// Column 3 - month
cellC = row.createCell(2);
if (record.getR27_month() != null) {
cellC.setCellValue(record.getR27_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR27_ytd() != null) {
cellD.setCellValue(record.getR27_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R28 (v) Households (sum of lines (a) to (h))
//------R29 a) Residential property (owner occupied)

// row29
row = sheet.getRow(28);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR29_month() != null) {
cellC.setCellValue(record.getR29_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR29_ytd() != null) {
cellD.setCellValue(record.getR29_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R30 b) Residential property (Rented)

//row30
row = sheet.getRow(29);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR30_month() != null) {
cellC.setCellValue(record.getR30_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR30_ytd() != null) {
cellD.setCellValue(record.getR30_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R31 c) Personal Loans

// row31
row = sheet.getRow(30);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR31_month() != null) {
cellC.setCellValue(record.getR31_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR31_ytd() != null) {
cellD.setCellValue(record.getR31_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R32 d) Motor Vehicle

// ------------------------- Row 32 -------------------------
row = sheet.getRow(31);
// Column 1 - product name

// Column 3 - month
cellC = row.createCell(2);
if (record.getR32_month() != null) {
cellC.setCellValue(record.getR32_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR32_ytd() != null) {
cellD.setCellValue(record.getR32_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R33 e) Household goods

// ------------------------- Row 33 -------------------------
row = sheet.getRow(32);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR33_month() != null) {
cellC.setCellValue(record.getR33_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR33_ytd() != null) {
cellD.setCellValue(record.getR33_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R34 f) Credit card loans
// ------------------------- Row 34 -------------------------
row = sheet.getRow(33);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR34_month() != null) {
cellC.setCellValue(record.getR34_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR34_ytd() != null) {
cellD.setCellValue(record.getR34_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R35 g) Non-Profit Institutions Serving Households

//------------------------- Row 35 -------------------------
row = sheet.getRow(34);

cellC = row.createCell(2);
if (record.getR35_month() != null) {
cellC.setCellValue(record.getR35_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR35_ytd() != null) {
cellD.setCellValue(record.getR35_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R36 h) Other*

// ------------------------- Row 36 -------------------------
row = sheet.getRow(35);


cellC = row.createCell(2);
if (record.getR36_month() != null) {
cellC.setCellValue(record.getR36_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR36_ytd() != null) {
cellD.setCellValue(record.getR36_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//---R37 (vi) Total interest income on balances with other banks

//------R38 a) Domestic banks

// ------------------------- Row 38 -------------------------
row = sheet.getRow(37);



cellC = row.createCell(2);
if (record.getR38_month() != null) {
cellC.setCellValue(record.getR38_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR38_ytd() != null) {
cellD.setCellValue(record.getR38_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R39 b) Banks abroad (Foreign banks)

//row39
row = sheet.getRow(38);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR39_month() != null) {
cellC.setCellValue(record.getR39_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR39_ytd() != null) {
cellD.setCellValue(record.getR39_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//---R40 (vii) Total income on investment and securities
//------R41 a) Held to maturity

//---------R42 (i) BOBCs

// row42
row = sheet.getRow(41);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR42_month() != null) {
cellC.setCellValue(record.getR42_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR42_ytd() != null) {
cellD.setCellValue(record.getR42_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---------R43 (ii) Other

// row43
row = sheet.getRow(42);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR43_month() != null) {
cellC.setCellValue(record.getR43_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR43_ytd() != null) {
cellD.setCellValue(record.getR43_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}	

//------R44 b) Available for Sale
//---------R45 (i) BOBCs

// row45
row = sheet.getRow(44);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR45_month() != null) {
cellC.setCellValue(record.getR45_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR45_ytd() != null) {
cellD.setCellValue(record.getR45_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//---------R46 (ii) Other

//row46
row = sheet.getRow(45);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR46_month() != null) {
cellC.setCellValue(record.getR46_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR46_ytd() != null) {
cellD.setCellValue(record.getR46_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R47 (viii) All other interest income*

// row47
row = sheet.getRow(46);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR47_month() != null) {
cellC.setCellValue(record.getR47_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR47_ytd() != null) {
cellD.setCellValue(record.getR47_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//--R48 2. Total interest expenses (sum of lines 2(i) to (iv))

//---R49 (i) Total interest expense on deposit accounts

//------R50 a) Demand

// row50
row = sheet.getRow(49);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR50_month() != null) {
cellC.setCellValue(record.getR50_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR50_ytd() != null) {
cellD.setCellValue(record.getR50_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R51 b) Savings

// row51
row = sheet.getRow(50);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR51_month() != null) {
cellC.setCellValue(record.getR51_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR51_ytd() != null) {
cellD.setCellValue(record.getR51_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R52 c) Time

// row52
row = sheet.getRow(51);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR52_month() != null) {
cellC.setCellValue(record.getR52_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR52_ytd() != null) {
cellD.setCellValue(record.getR52_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R53 (ii) Interest expense on inter-bank loans

// row53
row = sheet.getRow(52);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR53_month() != null) {
cellC.setCellValue(record.getR53_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR53_ytd() != null) {
cellD.setCellValue(record.getR53_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R54 (iii) Interest expense on funds borrowed from Bank of Botswana

// row54
row = sheet.getRow(53);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR54_month() != null) {
cellC.setCellValue(record.getR54_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR54_ytd() != null) {
cellD.setCellValue(record.getR54_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R55 (iv) Interest expense on other borrowed funds*

//row55
row = sheet.getRow(54);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR55_month() != null) {
cellC.setCellValue(record.getR55_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR55_ytd() != null) {
cellD.setCellValue(record.getR55_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//--R56 3. Net interest income (line 1 minus 2)
//--R57 4. Total Impairments (sum of lines 4(i) to (iii))

//---R58 (i) Impairment of loans and advances – Specific


// row58
row = sheet.getRow(57);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR58_month() != null) {
cellC.setCellValue(record.getR58_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR58_ytd() != null) {
cellD.setCellValue(record.getR58_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R59 (ii) Impairment of loans and advances – Portfolio


// row59
row = sheet.getRow(58);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR59_month() != null) {
cellC.setCellValue(record.getR59_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR59_ytd() != null) {
cellD.setCellValue(record.getR59_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R60 (iii) Impairment loss on other financial assets

//row60
row = sheet.getRow(59);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR60_month() != null) {
cellC.setCellValue(record.getR60_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR60_ytd() != null) {
cellD.setCellValue(record.getR60_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//--R61 5. Net Interest Income after Provisions (line 3 minus 4)
//--R62 6. Total non-interest income (sum of lines 6(i) to (viii))

//---R63 (i) Retail banking customer fees

//row63
row = sheet.getRow(62);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR63_month() != null) {
cellC.setCellValue(record.getR63_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR63_ytd() != null) {
cellD.setCellValue(record.getR63_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R64 (ii) Credit related fees

// row64
row = sheet.getRow(63);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR64_month() != null) {
cellC.setCellValue(record.getR64_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR64_ytd() != null) {
cellD.setCellValue(record.getR64_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R65 (iii) Foreign exchange (includes fees and commissions)


// row65
row = sheet.getRow(64);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR65_month() != null) {
cellC.setCellValue(record.getR65_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR65_ytd() != null) {
cellD.setCellValue(record.getR65_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R66 (iv) Bond trading

// row66
row = sheet.getRow(65);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR66_month() != null) {
cellC.setCellValue(record.getR66_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR66_ytd() != null) {
cellD.setCellValue(record.getR66_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R67 (v) Dividends

// row67
row = sheet.getRow(66);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR67_month() != null) {
cellC.setCellValue(record.getR67_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR67_ytd() != null) {
cellD.setCellValue(record.getR67_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R68 (vi) Insurance commissions

// row68
row = sheet.getRow(67);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR68_month() != null) {
cellC.setCellValue(record.getR68_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR68_ytd() != null) {
cellD.setCellValue(record.getR68_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R69 (vii) Professional fees (sum of lines (a) to (d))

////------R70 a) Lawyer's fees
//
//
//// row76
//row = sheet.getRow(69);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR76_month() != null) {
//cellC.setCellValue(record.getR76_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR76_ytd() != null) {
//cellD.setCellValue(record.getR76_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}
//
//
////------R71 b) Auditor's fees
//
//// row77
//row = sheet.getRow(70);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR77_month() != null) {
//cellC.setCellValue(record.getR77_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR77_ytd() != null) {
//cellD.setCellValue(record.getR77_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}
//
//
////------R72 c) Management fees
//
////row78
//row = sheet.getRow(71);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR78_month() != null) {
//cellC.setCellValue(record.getR78_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR78_ytd() != null) {
//cellD.setCellValue(record.getR78_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}
//
//
////------R73 d) Other*
//
//// row79
//row = sheet.getRow(72);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR79_month() != null) {
//cellC.setCellValue(record.getR79_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR79_ytd() != null) {
//cellD.setCellValue(record.getR79_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}

//---R74 (viii) All other non-interest income*

// row69
row = sheet.getRow(73);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR69_month() != null) {
cellC.setCellValue(record.getR69_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR69_ytd() != null) {
cellD.setCellValue(record.getR69_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//--R75 7. Total non-interest expense (sum of lines 7(i) to (vi))

//---R76 (i) Salaries and employee benefits

// row71
row = sheet.getRow(75);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR71_month() != null) {
cellC.setCellValue(record.getR71_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR71_ytd() != null) {
cellD.setCellValue(record.getR71_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R77 (ii) Occupancy (net of rental income)

// row72
row = sheet.getRow(76);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR72_month() != null) {
cellC.setCellValue(record.getR72_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR72_ytd() != null) {
cellD.setCellValue(record.getR72_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R78 (iii) Depreciation

// row73
row = sheet.getRow(77);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR73_month() != null) {
cellC.setCellValue(record.getR73_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR73_ytd() != null) {
cellD.setCellValue(record.getR73_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R79 (iv) Impairment loss on other non-financial assets

// row74
row = sheet.getRow(78);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR74_month() != null) {
cellC.setCellValue(record.getR74_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR74_ytd() != null) {
cellD.setCellValue(record.getR74_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//---R80 (v) All other non-interest expense*

// row80
row = sheet.getRow(79);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR80_month() != null) {
cellC.setCellValue(record.getR80_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR80_ytd() != null) {
cellD.setCellValue(record.getR80_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//---R81 (vi) Professional fees (sum of lines (a) to (d))

//row75
row = sheet.getRow(80);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR75_month() != null) {
cellC.setCellValue(record.getR75_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR75_ytd() != null) {
cellD.setCellValue(record.getR75_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R82 a) Lawyer's fees

// row76
row = sheet.getRow(81);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR76_month() != null) {
cellC.setCellValue(record.getR76_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR76_ytd() != null) {
cellD.setCellValue(record.getR76_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R83 b) Auditor's fees

// row77
row = sheet.getRow(82);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR77_month() != null) {
cellC.setCellValue(record.getR77_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR77_ytd() != null) {
cellD.setCellValue(record.getR77_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R84 c) Management fees

// row78
row = sheet.getRow(83);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR78_month() != null) {
cellC.setCellValue(record.getR78_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR78_ytd() != null) {
cellD.setCellValue(record.getR78_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R85 d) Other*

// row79
row = sheet.getRow(84);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR79_month() != null) {
cellC.setCellValue(record.getR79_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR79_ytd() != null) {
cellD.setCellValue(record.getR79_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//--R87 8. Net non-interest income (line 6 minus 7)
//--R88 9. Income before taxation (line 5 plus 8)

//--R89 10. Taxation

//row83
row = sheet.getRow(88);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR83_month() != null) {
cellC.setCellValue(record.getR83_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR83_ytd() != null) {
cellD.setCellValue(record.getR83_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//--R90 11. Income (loss) after taxes (line 9 minus 10)

//--R91 12. Provision for dividends

// row85
row = sheet.getRow(90);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR85_month() != null) {
cellC.setCellValue(record.getR85_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR85_ytd() != null) {
cellD.setCellValue(record.getR85_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



}
workbook.setForceFormulaRecalculation(true);
} else {

}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

//audit service summary email

ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SCI_E EMAIL SUMMARY", null, "BRRS_M_SCI_E_SUMMARYTABLE");
					}

return out.toByteArray();
}
}
}



// Archival format excel
public byte[] getExcelM_SCI_EARCHIVAL(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

logger.info("Service: Starting Excel generation process in memory in Archival.");

if ("email".equalsIgnoreCase(format) && version != null) {
try {
// Redirecting to Archival
return BRRS_M_SCI_EARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} 

List<M_SCI_E_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for M_SCI_E report. Returning empty result.");
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

int startRow = 6;

if (!dataList.isEmpty()) {
for (int i = 0; i < dataList.size(); i++) {
M_SCI_E_Archival_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 11
row = sheet.getRow(10);

Cell cellC,cellD;    

// row11

// Column 3 - month
cellC = row.createCell(2);
if (record.getR11_month() != null) {
cellC.setCellValue(record.getR11_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR11_ytd() != null) {
cellD.setCellValue(record.getR11_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row12
row = sheet.getRow(11);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR12_month() != null) {
cellC.setCellValue(record.getR12_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR12_ytd() != null) {
cellD.setCellValue(record.getR12_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row13
row = sheet.getRow(12);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR13_month() != null) {
cellC.setCellValue(record.getR13_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR13_ytd() != null) {
cellD.setCellValue(record.getR13_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row15
row = sheet.getRow(14); // Row index for R15 (0-based index)

// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR15_month() != null) {
cellC.setCellValue(record.getR15_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR15_ytd() != null) {
cellD.setCellValue(record.getR15_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row16
row = sheet.getRow(15);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR16_month() != null) {
cellC.setCellValue(record.getR16_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR16_ytd() != null) {
cellD.setCellValue(record.getR16_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row17
row = sheet.getRow(16);



cellC = row.createCell(2);
if (record.getR17_month() != null) {
cellC.setCellValue(record.getR17_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR17_ytd() != null) {
cellD.setCellValue(record.getR17_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row18
row = sheet.getRow(17);



cellC = row.createCell(2);
if (record.getR18_month() != null) {
cellC.setCellValue(record.getR18_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR18_ytd() != null) {
cellD.setCellValue(record.getR18_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row19
row = sheet.getRow(18);



cellC = row.createCell(2);
if (record.getR19_month() != null) {
cellC.setCellValue(record.getR19_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR19_ytd() != null) {
cellD.setCellValue(record.getR19_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row20
row = sheet.getRow(19);



cellC = row.createCell(2);
if (record.getR20_month() != null) {
cellC.setCellValue(record.getR20_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR20_ytd() != null) {
cellD.setCellValue(record.getR20_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row21
row = sheet.getRow(20);



cellC = row.createCell(2);
if (record.getR21_month() != null) {
cellC.setCellValue(record.getR21_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR21_ytd() != null) {
cellD.setCellValue(record.getR21_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row22
row = sheet.getRow(21);



cellC = row.createCell(2);
if (record.getR22_month() != null) {
cellC.setCellValue(record.getR22_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR22_ytd() != null) {
cellD.setCellValue(record.getR22_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row23
row = sheet.getRow(22);



cellC = row.createCell(2);
if (record.getR23_month() != null) {
cellC.setCellValue(record.getR23_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR23_ytd() != null) {
cellD.setCellValue(record.getR23_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row24
row = sheet.getRow(23);



cellC = row.createCell(2);
if (record.getR24_month() != null) {
cellC.setCellValue(record.getR24_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR24_ytd() != null) {
cellD.setCellValue(record.getR24_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row25
row = sheet.getRow(24);



cellC = row.createCell(2);
if (record.getR25_month() != null) {
cellC.setCellValue(record.getR25_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR25_ytd() != null) {
cellD.setCellValue(record.getR25_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row26
row = sheet.getRow(25);

// Column 1 - product name


// Column 3 - month
cellC = row.createCell(2);
if (record.getR26_month() != null) {
cellC.setCellValue(record.getR26_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR26_ytd() != null) {
cellD.setCellValue(record.getR26_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row27
row = sheet.getRow(26);




// Column 3 - month
cellC = row.createCell(2);
if (record.getR27_month() != null) {
cellC.setCellValue(record.getR27_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR27_ytd() != null) {
cellD.setCellValue(record.getR27_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row29
row = sheet.getRow(28);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR29_month() != null) {
cellC.setCellValue(record.getR29_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR29_ytd() != null) {
cellD.setCellValue(record.getR29_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row30
row = sheet.getRow(29);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR30_month() != null) {
cellC.setCellValue(record.getR30_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR30_ytd() != null) {
cellD.setCellValue(record.getR30_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row31
row = sheet.getRow(30);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR31_month() != null) {
cellC.setCellValue(record.getR31_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR31_ytd() != null) {
cellD.setCellValue(record.getR31_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 32 -------------------------
row = sheet.getRow(31);
// Column 1 - product name

// Column 3 - month
cellC = row.createCell(2);
if (record.getR32_month() != null) {
cellC.setCellValue(record.getR32_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR32_ytd() != null) {
cellD.setCellValue(record.getR32_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 33 -------------------------
row = sheet.getRow(32);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR33_month() != null) {
cellC.setCellValue(record.getR33_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR33_ytd() != null) {
cellD.setCellValue(record.getR33_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 34 -------------------------
row = sheet.getRow(33);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR34_month() != null) {
cellC.setCellValue(record.getR34_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR34_ytd() != null) {
cellD.setCellValue(record.getR34_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 35 -------------------------
row = sheet.getRow(34);

cellC = row.createCell(2);
if (record.getR35_month() != null) {
cellC.setCellValue(record.getR35_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR35_ytd() != null) {
cellD.setCellValue(record.getR35_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 36 -------------------------
row = sheet.getRow(35);


cellC = row.createCell(2);
if (record.getR36_month() != null) {
cellC.setCellValue(record.getR36_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR36_ytd() != null) {
cellD.setCellValue(record.getR36_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// ------------------------- Row 38 -------------------------
row = sheet.getRow(37);



cellC = row.createCell(2);
if (record.getR38_month() != null) {
cellC.setCellValue(record.getR38_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR38_ytd() != null) {
cellD.setCellValue(record.getR38_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row39
row = sheet.getRow(38);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR39_month() != null) {
cellC.setCellValue(record.getR39_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR39_ytd() != null) {
cellD.setCellValue(record.getR39_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row42
row = sheet.getRow(41);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR42_month() != null) {
cellC.setCellValue(record.getR42_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR42_ytd() != null) {
cellD.setCellValue(record.getR42_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row43
row = sheet.getRow(42);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR43_month() != null) {
cellC.setCellValue(record.getR43_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR43_ytd() != null) {
cellD.setCellValue(record.getR43_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row45
row = sheet.getRow(44);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR45_month() != null) {
cellC.setCellValue(record.getR45_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR45_ytd() != null) {
cellD.setCellValue(record.getR45_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row46
row = sheet.getRow(45);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR46_month() != null) {
cellC.setCellValue(record.getR46_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR46_ytd() != null) {
cellD.setCellValue(record.getR46_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row47
row = sheet.getRow(46);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR47_month() != null) {
cellC.setCellValue(record.getR47_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR47_ytd() != null) {
cellD.setCellValue(record.getR47_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row50
row = sheet.getRow(49);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR50_month() != null) {
cellC.setCellValue(record.getR50_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR50_ytd() != null) {
cellD.setCellValue(record.getR50_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row51
row = sheet.getRow(50);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR51_month() != null) {
cellC.setCellValue(record.getR51_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR51_ytd() != null) {
cellD.setCellValue(record.getR51_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row52
row = sheet.getRow(51);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR52_month() != null) {
cellC.setCellValue(record.getR52_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR52_ytd() != null) {
cellD.setCellValue(record.getR52_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row53
row = sheet.getRow(52);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR53_month() != null) {
cellC.setCellValue(record.getR53_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR53_ytd() != null) {
cellD.setCellValue(record.getR53_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row54
row = sheet.getRow(53);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR54_month() != null) {
cellC.setCellValue(record.getR54_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR54_ytd() != null) {
cellD.setCellValue(record.getR54_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row55
row = sheet.getRow(54);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR55_month() != null) {
cellC.setCellValue(record.getR55_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR55_ytd() != null) {
cellD.setCellValue(record.getR55_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row58
row = sheet.getRow(57);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR58_month() != null) {
cellC.setCellValue(record.getR58_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR58_ytd() != null) {
cellD.setCellValue(record.getR58_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row59
row = sheet.getRow(58);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR59_month() != null) {
cellC.setCellValue(record.getR59_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR59_ytd() != null) {
cellD.setCellValue(record.getR59_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row60
row = sheet.getRow(59);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR60_month() != null) {
cellC.setCellValue(record.getR60_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR60_ytd() != null) {
cellD.setCellValue(record.getR60_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




// row63
row = sheet.getRow(62);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR63_month() != null) {
cellC.setCellValue(record.getR63_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR63_ytd() != null) {
cellD.setCellValue(record.getR63_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row64
row = sheet.getRow(63);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR64_month() != null) {
cellC.setCellValue(record.getR64_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR64_ytd() != null) {
cellD.setCellValue(record.getR64_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row65
row = sheet.getRow(64);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR65_month() != null) {
cellC.setCellValue(record.getR65_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR65_ytd() != null) {
cellD.setCellValue(record.getR65_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row66
row = sheet.getRow(65);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR66_month() != null) {
cellC.setCellValue(record.getR66_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR66_ytd() != null) {
cellD.setCellValue(record.getR66_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row67
row = sheet.getRow(66);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR67_month() != null) {
cellC.setCellValue(record.getR67_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR67_ytd() != null) {
cellD.setCellValue(record.getR67_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row68
row = sheet.getRow(67);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR68_month() != null) {
cellC.setCellValue(record.getR68_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR68_ytd() != null) {
cellD.setCellValue(record.getR68_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row69
row = sheet.getRow(68);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR69_month() != null) {
cellC.setCellValue(record.getR69_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR69_ytd() != null) {
cellD.setCellValue(record.getR69_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row71
row = sheet.getRow(70);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR71_month() != null) {
cellC.setCellValue(record.getR71_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR71_ytd() != null) {
cellD.setCellValue(record.getR71_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row72
row = sheet.getRow(71);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR72_month() != null) {
cellC.setCellValue(record.getR72_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR72_ytd() != null) {
cellD.setCellValue(record.getR72_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row73
row = sheet.getRow(72);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR73_month() != null) {
cellC.setCellValue(record.getR73_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR73_ytd() != null) {
cellD.setCellValue(record.getR73_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row74
row = sheet.getRow(73);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR74_month() != null) {
cellC.setCellValue(record.getR74_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR74_ytd() != null) {
cellD.setCellValue(record.getR74_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row76
row = sheet.getRow(75);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR76_month() != null) {
cellC.setCellValue(record.getR76_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR76_ytd() != null) {
cellD.setCellValue(record.getR76_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row77
row = sheet.getRow(76);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR77_month() != null) {
cellC.setCellValue(record.getR77_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR77_ytd() != null) {
cellD.setCellValue(record.getR77_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row78
row = sheet.getRow(77);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR78_month() != null) {
cellC.setCellValue(record.getR78_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR78_ytd() != null) {
cellD.setCellValue(record.getR78_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row79
row = sheet.getRow(78);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR79_month() != null) {
cellC.setCellValue(record.getR79_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR79_ytd() != null) {
cellD.setCellValue(record.getR79_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row80
row = sheet.getRow(79);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR80_month() != null) {
cellC.setCellValue(record.getR80_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR80_ytd() != null) {
cellD.setCellValue(record.getR80_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row83
row = sheet.getRow(82);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR83_month() != null) {
cellC.setCellValue(record.getR83_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR83_ytd() != null) {
cellD.setCellValue(record.getR83_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row85
row = sheet.getRow(84);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR85_month() != null) {
cellC.setCellValue(record.getR85_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR85_ytd() != null) {
cellD.setCellValue(record.getR85_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

}

workbook.setForceFormulaRecalculation(true);
} else {

}

//Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

//audit service archival summary format

ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SCI_E ARCHIVAL SUMMARY", null, "BRRS_M_SCI_E_ARCHIVALTABLE_SUMMARY");
					}

return out.toByteArray();
}

}

// Archival Email Excel
public byte[] BRRS_M_SCI_EARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Archival Email Excel generation process in memory.");

List<M_SCI_E_Archival_Summary_Entity> dataList = getArchivalSummaryByDateAndVersion(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_SCI_E report. Returning empty result.");
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
M_SCI_E_Archival_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 11
row = sheet.getRow(10);


Cell cellC,cellD;   


//--R10 1. Total interest and fee income from loans and advances (sum of lines (i) to (viii))

//---R11 (i) Central Government (Government of Botswana)


//row11

// Column 3 - month
cellC = row.createCell(2);
if (record.getR11_month() != null) {
cellC.setCellValue(record.getR11_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR11_ytd() != null) {
cellD.setCellValue(record.getR11_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R12 (ii) Local Government

// row12
row = sheet.getRow(11);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR12_month() != null) {
cellC.setCellValue(record.getR12_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR12_ytd() != null) {
cellD.setCellValue(record.getR12_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R13 (iii) Public Non-Financial Corporations


//row13
row = sheet.getRow(12);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR13_month() != null) {
cellC.setCellValue(record.getR13_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR13_ytd() != null) {
cellD.setCellValue(record.getR13_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//---R14 (iv) Other Non-Financial Corporations (Private business enterprises) 

//------R15 a) Agriculture, Forestry, Fishing

//row15
row = sheet.getRow(14); // Row index for R15 (0-based index)

// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR15_month() != null) {
cellC.setCellValue(record.getR15_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR15_ytd() != null) {
cellD.setCellValue(record.getR15_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R16 b) Mining and Quarrying
// row16
row = sheet.getRow(15);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR16_month() != null) {
cellC.setCellValue(record.getR16_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR16_ytd() != null) {
cellD.setCellValue(record.getR16_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R17 c) Manufacturing
// row17
row = sheet.getRow(16);



cellC = row.createCell(2);
if (record.getR17_month() != null) {
cellC.setCellValue(record.getR17_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR17_ytd() != null) {
cellD.setCellValue(record.getR17_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R18 d) Construction

//row18
row = sheet.getRow(17);



cellC = row.createCell(2);
if (record.getR18_month() != null) {
cellC.setCellValue(record.getR18_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR18_ytd() != null) {
cellD.setCellValue(record.getR18_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R19 e) Commercial real estate

// row19
row = sheet.getRow(18);



cellC = row.createCell(2);
if (record.getR19_month() != null) {
cellC.setCellValue(record.getR19_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR19_ytd() != null) {
cellD.setCellValue(record.getR19_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R20 f) Electricity

// row20
row = sheet.getRow(19);



cellC = row.createCell(2);
if (record.getR20_month() != null) {
cellC.setCellValue(record.getR20_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR20_ytd() != null) {
cellD.setCellValue(record.getR20_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R21 g) Water

//row21
row = sheet.getRow(20);



cellC = row.createCell(2);
if (record.getR21_month() != null) {
cellC.setCellValue(record.getR21_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR21_ytd() != null) {
cellD.setCellValue(record.getR21_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R22 h) Telecommunication and Post
// row22
row = sheet.getRow(21);



cellC = row.createCell(2);
if (record.getR22_month() != null) {
cellC.setCellValue(record.getR22_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR22_ytd() != null) {
cellD.setCellValue(record.getR22_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R23 i) Tourism and hotels

//row23
row = sheet.getRow(22);



cellC = row.createCell(2);
if (record.getR23_month() != null) {
cellC.setCellValue(record.getR23_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR23_ytd() != null) {
cellD.setCellValue(record.getR23_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R24 j) Transport and Storage

//row24
row = sheet.getRow(23);



cellC = row.createCell(2);
if (record.getR24_month() != null) {
cellC.setCellValue(record.getR24_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR24_ytd() != null) {
cellD.setCellValue(record.getR24_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R25 k) Trade, restaurants and bars

// row25
row = sheet.getRow(24);



cellC = row.createCell(2);
if (record.getR25_month() != null) {
cellC.setCellValue(record.getR25_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR25_ytd() != null) {
cellD.setCellValue(record.getR25_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//------R26 l) Business Services

// row26
row = sheet.getRow(25);

// Column 1 - product name


// Column 3 - month
cellC = row.createCell(2);
if (record.getR26_month() != null) {
cellC.setCellValue(record.getR26_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR26_ytd() != null) {
cellD.setCellValue(record.getR26_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R27 m) Other Community, Social and Personal Services
// row27
row = sheet.getRow(26);




// Column 3 - month
cellC = row.createCell(2);
if (record.getR27_month() != null) {
cellC.setCellValue(record.getR27_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR27_ytd() != null) {
cellD.setCellValue(record.getR27_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R28 (v) Households (sum of lines (a) to (h))
//------R29 a) Residential property (owner occupied)

// row29
row = sheet.getRow(28);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR29_month() != null) {
cellC.setCellValue(record.getR29_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR29_ytd() != null) {
cellD.setCellValue(record.getR29_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R30 b) Residential property (Rented)

//row30
row = sheet.getRow(29);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR30_month() != null) {
cellC.setCellValue(record.getR30_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR30_ytd() != null) {
cellD.setCellValue(record.getR30_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R31 c) Personal Loans

// row31
row = sheet.getRow(30);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR31_month() != null) {
cellC.setCellValue(record.getR31_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR31_ytd() != null) {
cellD.setCellValue(record.getR31_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R32 d) Motor Vehicle

// ------------------------- Row 32 -------------------------
row = sheet.getRow(31);
// Column 1 - product name

// Column 3 - month
cellC = row.createCell(2);
if (record.getR32_month() != null) {
cellC.setCellValue(record.getR32_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR32_ytd() != null) {
cellD.setCellValue(record.getR32_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R33 e) Household goods

// ------------------------- Row 33 -------------------------
row = sheet.getRow(32);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR33_month() != null) {
cellC.setCellValue(record.getR33_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR33_ytd() != null) {
cellD.setCellValue(record.getR33_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R34 f) Credit card loans
// ------------------------- Row 34 -------------------------
row = sheet.getRow(33);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR34_month() != null) {
cellC.setCellValue(record.getR34_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR34_ytd() != null) {
cellD.setCellValue(record.getR34_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R35 g) Non-Profit Institutions Serving Households

//------------------------- Row 35 -------------------------
row = sheet.getRow(34);

cellC = row.createCell(2);
if (record.getR35_month() != null) {
cellC.setCellValue(record.getR35_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR35_ytd() != null) {
cellD.setCellValue(record.getR35_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R36 h) Other*

// ------------------------- Row 36 -------------------------
row = sheet.getRow(35);


cellC = row.createCell(2);
if (record.getR36_month() != null) {
cellC.setCellValue(record.getR36_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR36_ytd() != null) {
cellD.setCellValue(record.getR36_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//---R37 (vi) Total interest income on balances with other banks

//------R38 a) Domestic banks

// ------------------------- Row 38 -------------------------
row = sheet.getRow(37);



cellC = row.createCell(2);
if (record.getR38_month() != null) {
cellC.setCellValue(record.getR38_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR38_ytd() != null) {
cellD.setCellValue(record.getR38_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R39 b) Banks abroad (Foreign banks)

//row39
row = sheet.getRow(38);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR39_month() != null) {
cellC.setCellValue(record.getR39_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR39_ytd() != null) {
cellD.setCellValue(record.getR39_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//---R40 (vii) Total income on investment and securities
//------R41 a) Held to maturity

//---------R42 (i) BOBCs

// row42
row = sheet.getRow(41);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR42_month() != null) {
cellC.setCellValue(record.getR42_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR42_ytd() != null) {
cellD.setCellValue(record.getR42_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---------R43 (ii) Other

// row43
row = sheet.getRow(42);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR43_month() != null) {
cellC.setCellValue(record.getR43_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR43_ytd() != null) {
cellD.setCellValue(record.getR43_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}	

//------R44 b) Available for Sale
//---------R45 (i) BOBCs

// row45
row = sheet.getRow(44);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR45_month() != null) {
cellC.setCellValue(record.getR45_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR45_ytd() != null) {
cellD.setCellValue(record.getR45_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//---------R46 (ii) Other

//row46
row = sheet.getRow(45);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR46_month() != null) {
cellC.setCellValue(record.getR46_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR46_ytd() != null) {
cellD.setCellValue(record.getR46_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R47 (viii) All other interest income*

// row47
row = sheet.getRow(46);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR47_month() != null) {
cellC.setCellValue(record.getR47_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR47_ytd() != null) {
cellD.setCellValue(record.getR47_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//--R48 2. Total interest expenses (sum of lines 2(i) to (iv))

//---R49 (i) Total interest expense on deposit accounts

//------R50 a) Demand

// row50
row = sheet.getRow(49);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR50_month() != null) {
cellC.setCellValue(record.getR50_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR50_ytd() != null) {
cellD.setCellValue(record.getR50_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R51 b) Savings

// row51
row = sheet.getRow(50);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR51_month() != null) {
cellC.setCellValue(record.getR51_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR51_ytd() != null) {
cellD.setCellValue(record.getR51_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R52 c) Time

// row52
row = sheet.getRow(51);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR52_month() != null) {
cellC.setCellValue(record.getR52_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR52_ytd() != null) {
cellD.setCellValue(record.getR52_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R53 (ii) Interest expense on inter-bank loans

// row53
row = sheet.getRow(52);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR53_month() != null) {
cellC.setCellValue(record.getR53_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR53_ytd() != null) {
cellD.setCellValue(record.getR53_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R54 (iii) Interest expense on funds borrowed from Bank of Botswana

// row54
row = sheet.getRow(53);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR54_month() != null) {
cellC.setCellValue(record.getR54_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR54_ytd() != null) {
cellD.setCellValue(record.getR54_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R55 (iv) Interest expense on other borrowed funds*

//row55
row = sheet.getRow(54);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR55_month() != null) {
cellC.setCellValue(record.getR55_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR55_ytd() != null) {
cellD.setCellValue(record.getR55_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//--R56 3. Net interest income (line 1 minus 2)
//--R57 4. Total Impairments (sum of lines 4(i) to (iii))

//---R58 (i) Impairment of loans and advances – Specific


// row58
row = sheet.getRow(57);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR58_month() != null) {
cellC.setCellValue(record.getR58_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR58_ytd() != null) {
cellD.setCellValue(record.getR58_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R59 (ii) Impairment of loans and advances – Portfolio


// row59
row = sheet.getRow(58);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR59_month() != null) {
cellC.setCellValue(record.getR59_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR59_ytd() != null) {
cellD.setCellValue(record.getR59_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R60 (iii) Impairment loss on other financial assets

//row60
row = sheet.getRow(59);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR60_month() != null) {
cellC.setCellValue(record.getR60_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR60_ytd() != null) {
cellD.setCellValue(record.getR60_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//--R61 5. Net Interest Income after Provisions (line 3 minus 4)
//--R62 6. Total non-interest income (sum of lines 6(i) to (viii))

//---R63 (i) Retail banking customer fees

//row63
row = sheet.getRow(62);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR63_month() != null) {
cellC.setCellValue(record.getR63_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR63_ytd() != null) {
cellD.setCellValue(record.getR63_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R64 (ii) Credit related fees

// row64
row = sheet.getRow(63);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR64_month() != null) {
cellC.setCellValue(record.getR64_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR64_ytd() != null) {
cellD.setCellValue(record.getR64_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R65 (iii) Foreign exchange (includes fees and commissions)


// row65
row = sheet.getRow(64);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR65_month() != null) {
cellC.setCellValue(record.getR65_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR65_ytd() != null) {
cellD.setCellValue(record.getR65_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R66 (iv) Bond trading

// row66
row = sheet.getRow(65);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR66_month() != null) {
cellC.setCellValue(record.getR66_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR66_ytd() != null) {
cellD.setCellValue(record.getR66_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R67 (v) Dividends

// row67
row = sheet.getRow(66);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR67_month() != null) {
cellC.setCellValue(record.getR67_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR67_ytd() != null) {
cellD.setCellValue(record.getR67_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R68 (vi) Insurance commissions

// row68
row = sheet.getRow(67);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR68_month() != null) {
cellC.setCellValue(record.getR68_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR68_ytd() != null) {
cellD.setCellValue(record.getR68_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

////---R69 (vii) Professional fees (sum of lines (a) to (d))
//
////------R70 a) Lawyer's fees
//
//
//// row76
//row = sheet.getRow(69);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR76_month() != null) {
//cellC.setCellValue(record.getR76_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR76_ytd() != null) {
//cellD.setCellValue(record.getR76_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}
//
//
////------R71 b) Auditor's fees
//
//// row77
//row = sheet.getRow(70);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR77_month() != null) {
//cellC.setCellValue(record.getR77_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR77_ytd() != null) {
//cellD.setCellValue(record.getR77_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}
//
//
////------R72 c) Management fees
//
////row78
//row = sheet.getRow(71);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR78_month() != null) {
//cellC.setCellValue(record.getR78_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR78_ytd() != null) {
//cellD.setCellValue(record.getR78_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}
//
//
////------R73 d) Other*
//
//// row79
//row = sheet.getRow(72);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR79_month() != null) {
//cellC.setCellValue(record.getR79_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR79_ytd() != null) {
//cellD.setCellValue(record.getR79_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}

//---R74 (viii) All other non-interest income*

// row69
row = sheet.getRow(73);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR69_month() != null) {
cellC.setCellValue(record.getR69_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR69_ytd() != null) {
cellD.setCellValue(record.getR69_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//--R75 7. Total non-interest expense (sum of lines 7(i) to (vi))

//---R76 (i) Salaries and employee benefits

// row71
row = sheet.getRow(75);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR71_month() != null) {
cellC.setCellValue(record.getR71_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR71_ytd() != null) {
cellD.setCellValue(record.getR71_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R77 (ii) Occupancy (net of rental income)

// row72
row = sheet.getRow(76);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR72_month() != null) {
cellC.setCellValue(record.getR72_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR72_ytd() != null) {
cellD.setCellValue(record.getR72_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R78 (iii) Depreciation

// row73
row = sheet.getRow(77);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR73_month() != null) {
cellC.setCellValue(record.getR73_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR73_ytd() != null) {
cellD.setCellValue(record.getR73_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R79 (iv) Impairment loss on other non-financial assets

// row74
row = sheet.getRow(78);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR74_month() != null) {
cellC.setCellValue(record.getR74_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR74_ytd() != null) {
cellD.setCellValue(record.getR74_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//---R80 (v) All other non-interest expense*

// row80
row = sheet.getRow(79);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR80_month() != null) {
cellC.setCellValue(record.getR80_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR80_ytd() != null) {
cellD.setCellValue(record.getR80_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//---R81 (vi) Professional fees (sum of lines (a) to (d))

//row75
row = sheet.getRow(80);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR75_month() != null) {
cellC.setCellValue(record.getR75_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR75_ytd() != null) {
cellD.setCellValue(record.getR75_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R82 a) Lawyer's fees

// row76
row = sheet.getRow(81);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR76_month() != null) {
cellC.setCellValue(record.getR76_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR76_ytd() != null) {
cellD.setCellValue(record.getR76_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R83 b) Auditor's fees

// row77
row = sheet.getRow(82);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR77_month() != null) {
cellC.setCellValue(record.getR77_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR77_ytd() != null) {
cellD.setCellValue(record.getR77_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R84 c) Management fees

// row78
row = sheet.getRow(83);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR78_month() != null) {
cellC.setCellValue(record.getR78_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR78_ytd() != null) {
cellD.setCellValue(record.getR78_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R85 d) Other*

// row79
row = sheet.getRow(84);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR79_month() != null) {
cellC.setCellValue(record.getR79_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR79_ytd() != null) {
cellD.setCellValue(record.getR79_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//--R87 8. Net non-interest income (line 6 minus 7)
//--R88 9. Income before taxation (line 5 plus 8)

//--R89 10. Taxation

//row83
row = sheet.getRow(88);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR83_month() != null) {
cellC.setCellValue(record.getR83_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR83_ytd() != null) {
cellD.setCellValue(record.getR83_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//--R90 11. Income (loss) after taxes (line 9 minus 10)

//--R91 12. Provision for dividends

// row85
row = sheet.getRow(90);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR85_month() != null) {
cellC.setCellValue(record.getR85_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR85_ytd() != null) {
cellD.setCellValue(record.getR85_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



}
workbook.setForceFormulaRecalculation(true);
} else {

}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

//audit service archival summary email


	ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SCI_E EMAIL ARCHIVAL SUMMARY", null, "BRRS_M_SCI_E_ARCHIVALTABLE_SUMMARY");
					}

return out.toByteArray();
}
}

// Resub Format excel
public byte[] BRRS_M_SCI_EResubExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

if ("email".equalsIgnoreCase(format) && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);

try {
// ✅ Redirecting to Resub Excel
return BRRS_M_SCI_EEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
}

List<M_SCI_E_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for M_SCI_E report. Returning empty result.");
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

M_SCI_E_RESUB_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 11
row = sheet.getRow(10);

Cell cellC,cellD;    

// row11

// Column 3 - month
cellC = row.createCell(2);
if (record.getR11_month() != null) {
cellC.setCellValue(record.getR11_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR11_ytd() != null) {
cellD.setCellValue(record.getR11_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row12
row = sheet.getRow(11);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR12_month() != null) {
cellC.setCellValue(record.getR12_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR12_ytd() != null) {
cellD.setCellValue(record.getR12_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row13
row = sheet.getRow(12);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR13_month() != null) {
cellC.setCellValue(record.getR13_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR13_ytd() != null) {
cellD.setCellValue(record.getR13_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row15
row = sheet.getRow(14); // Row index for R15 (0-based index)

// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR15_month() != null) {
cellC.setCellValue(record.getR15_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR15_ytd() != null) {
cellD.setCellValue(record.getR15_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row16
row = sheet.getRow(15);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR16_month() != null) {
cellC.setCellValue(record.getR16_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR16_ytd() != null) {
cellD.setCellValue(record.getR16_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row17
row = sheet.getRow(16);



cellC = row.createCell(2);
if (record.getR17_month() != null) {
cellC.setCellValue(record.getR17_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR17_ytd() != null) {
cellD.setCellValue(record.getR17_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row18
row = sheet.getRow(17);



cellC = row.createCell(2);
if (record.getR18_month() != null) {
cellC.setCellValue(record.getR18_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR18_ytd() != null) {
cellD.setCellValue(record.getR18_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row19
row = sheet.getRow(18);



cellC = row.createCell(2);
if (record.getR19_month() != null) {
cellC.setCellValue(record.getR19_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR19_ytd() != null) {
cellD.setCellValue(record.getR19_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row20
row = sheet.getRow(19);



cellC = row.createCell(2);
if (record.getR20_month() != null) {
cellC.setCellValue(record.getR20_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR20_ytd() != null) {
cellD.setCellValue(record.getR20_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row21
row = sheet.getRow(20);



cellC = row.createCell(2);
if (record.getR21_month() != null) {
cellC.setCellValue(record.getR21_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR21_ytd() != null) {
cellD.setCellValue(record.getR21_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row22
row = sheet.getRow(21);



cellC = row.createCell(2);
if (record.getR22_month() != null) {
cellC.setCellValue(record.getR22_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR22_ytd() != null) {
cellD.setCellValue(record.getR22_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row23
row = sheet.getRow(22);



cellC = row.createCell(2);
if (record.getR23_month() != null) {
cellC.setCellValue(record.getR23_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR23_ytd() != null) {
cellD.setCellValue(record.getR23_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row24
row = sheet.getRow(23);



cellC = row.createCell(2);
if (record.getR24_month() != null) {
cellC.setCellValue(record.getR24_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR24_ytd() != null) {
cellD.setCellValue(record.getR24_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row25
row = sheet.getRow(24);



cellC = row.createCell(2);
if (record.getR25_month() != null) {
cellC.setCellValue(record.getR25_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR25_ytd() != null) {
cellD.setCellValue(record.getR25_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row26
row = sheet.getRow(25);

// Column 1 - product name


// Column 3 - month
cellC = row.createCell(2);
if (record.getR26_month() != null) {
cellC.setCellValue(record.getR26_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR26_ytd() != null) {
cellD.setCellValue(record.getR26_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row27
row = sheet.getRow(26);




// Column 3 - month
cellC = row.createCell(2);
if (record.getR27_month() != null) {
cellC.setCellValue(record.getR27_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR27_ytd() != null) {
cellD.setCellValue(record.getR27_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row29
row = sheet.getRow(28);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR29_month() != null) {
cellC.setCellValue(record.getR29_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR29_ytd() != null) {
cellD.setCellValue(record.getR29_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row30
row = sheet.getRow(29);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR30_month() != null) {
cellC.setCellValue(record.getR30_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR30_ytd() != null) {
cellD.setCellValue(record.getR30_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row31
row = sheet.getRow(30);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR31_month() != null) {
cellC.setCellValue(record.getR31_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR31_ytd() != null) {
cellD.setCellValue(record.getR31_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 32 -------------------------
row = sheet.getRow(31);
// Column 1 - product name

// Column 3 - month
cellC = row.createCell(2);
if (record.getR32_month() != null) {
cellC.setCellValue(record.getR32_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR32_ytd() != null) {
cellD.setCellValue(record.getR32_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 33 -------------------------
row = sheet.getRow(32);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR33_month() != null) {
cellC.setCellValue(record.getR33_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR33_ytd() != null) {
cellD.setCellValue(record.getR33_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 34 -------------------------
row = sheet.getRow(33);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR34_month() != null) {
cellC.setCellValue(record.getR34_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR34_ytd() != null) {
cellD.setCellValue(record.getR34_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 35 -------------------------
row = sheet.getRow(34);

cellC = row.createCell(2);
if (record.getR35_month() != null) {
cellC.setCellValue(record.getR35_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR35_ytd() != null) {
cellD.setCellValue(record.getR35_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// ------------------------- Row 36 -------------------------
row = sheet.getRow(35);


cellC = row.createCell(2);
if (record.getR36_month() != null) {
cellC.setCellValue(record.getR36_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR36_ytd() != null) {
cellD.setCellValue(record.getR36_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// ------------------------- Row 38 -------------------------
row = sheet.getRow(37);



cellC = row.createCell(2);
if (record.getR38_month() != null) {
cellC.setCellValue(record.getR38_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR38_ytd() != null) {
cellD.setCellValue(record.getR38_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row39
row = sheet.getRow(38);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR39_month() != null) {
cellC.setCellValue(record.getR39_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR39_ytd() != null) {
cellD.setCellValue(record.getR39_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row42
row = sheet.getRow(41);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR42_month() != null) {
cellC.setCellValue(record.getR42_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR42_ytd() != null) {
cellD.setCellValue(record.getR42_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row43
row = sheet.getRow(42);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR43_month() != null) {
cellC.setCellValue(record.getR43_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR43_ytd() != null) {
cellD.setCellValue(record.getR43_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row45
row = sheet.getRow(44);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR45_month() != null) {
cellC.setCellValue(record.getR45_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR45_ytd() != null) {
cellD.setCellValue(record.getR45_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row46
row = sheet.getRow(45);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR46_month() != null) {
cellC.setCellValue(record.getR46_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR46_ytd() != null) {
cellD.setCellValue(record.getR46_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row47
row = sheet.getRow(46);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR47_month() != null) {
cellC.setCellValue(record.getR47_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR47_ytd() != null) {
cellD.setCellValue(record.getR47_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row50
row = sheet.getRow(49);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR50_month() != null) {
cellC.setCellValue(record.getR50_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR50_ytd() != null) {
cellD.setCellValue(record.getR50_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row51
row = sheet.getRow(50);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR51_month() != null) {
cellC.setCellValue(record.getR51_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR51_ytd() != null) {
cellD.setCellValue(record.getR51_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row52
row = sheet.getRow(51);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR52_month() != null) {
cellC.setCellValue(record.getR52_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR52_ytd() != null) {
cellD.setCellValue(record.getR52_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row53
row = sheet.getRow(52);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR53_month() != null) {
cellC.setCellValue(record.getR53_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR53_ytd() != null) {
cellD.setCellValue(record.getR53_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row54
row = sheet.getRow(53);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR54_month() != null) {
cellC.setCellValue(record.getR54_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR54_ytd() != null) {
cellD.setCellValue(record.getR54_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row55
row = sheet.getRow(54);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR55_month() != null) {
cellC.setCellValue(record.getR55_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR55_ytd() != null) {
cellD.setCellValue(record.getR55_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row58
row = sheet.getRow(57);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR58_month() != null) {
cellC.setCellValue(record.getR58_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR58_ytd() != null) {
cellD.setCellValue(record.getR58_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row59
row = sheet.getRow(58);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR59_month() != null) {
cellC.setCellValue(record.getR59_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR59_ytd() != null) {
cellD.setCellValue(record.getR59_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row60
row = sheet.getRow(59);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR60_month() != null) {
cellC.setCellValue(record.getR60_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR60_ytd() != null) {
cellD.setCellValue(record.getR60_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




// row63
row = sheet.getRow(62);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR63_month() != null) {
cellC.setCellValue(record.getR63_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR63_ytd() != null) {
cellD.setCellValue(record.getR63_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row64
row = sheet.getRow(63);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR64_month() != null) {
cellC.setCellValue(record.getR64_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR64_ytd() != null) {
cellD.setCellValue(record.getR64_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row65
row = sheet.getRow(64);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR65_month() != null) {
cellC.setCellValue(record.getR65_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR65_ytd() != null) {
cellD.setCellValue(record.getR65_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row66
row = sheet.getRow(65);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR66_month() != null) {
cellC.setCellValue(record.getR66_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR66_ytd() != null) {
cellD.setCellValue(record.getR66_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row67
row = sheet.getRow(66);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR67_month() != null) {
cellC.setCellValue(record.getR67_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR67_ytd() != null) {
cellD.setCellValue(record.getR67_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row68
row = sheet.getRow(67);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR68_month() != null) {
cellC.setCellValue(record.getR68_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR68_ytd() != null) {
cellD.setCellValue(record.getR68_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row69
row = sheet.getRow(68);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR69_month() != null) {
cellC.setCellValue(record.getR69_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR69_ytd() != null) {
cellD.setCellValue(record.getR69_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row71
row = sheet.getRow(70);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR71_month() != null) {
cellC.setCellValue(record.getR71_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR71_ytd() != null) {
cellD.setCellValue(record.getR71_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row72
row = sheet.getRow(71);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR72_month() != null) {
cellC.setCellValue(record.getR72_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR72_ytd() != null) {
cellD.setCellValue(record.getR72_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row73
row = sheet.getRow(72);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR73_month() != null) {
cellC.setCellValue(record.getR73_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR73_ytd() != null) {
cellD.setCellValue(record.getR73_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row74
row = sheet.getRow(73);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR74_month() != null) {
cellC.setCellValue(record.getR74_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR74_ytd() != null) {
cellD.setCellValue(record.getR74_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row76
row = sheet.getRow(75);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR76_month() != null) {
cellC.setCellValue(record.getR76_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR76_ytd() != null) {
cellD.setCellValue(record.getR76_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row77
row = sheet.getRow(76);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR77_month() != null) {
cellC.setCellValue(record.getR77_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR77_ytd() != null) {
cellD.setCellValue(record.getR77_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row78
row = sheet.getRow(77);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR78_month() != null) {
cellC.setCellValue(record.getR78_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR78_ytd() != null) {
cellD.setCellValue(record.getR78_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row79
row = sheet.getRow(78);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR79_month() != null) {
cellC.setCellValue(record.getR79_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR79_ytd() != null) {
cellD.setCellValue(record.getR79_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

// row80
row = sheet.getRow(79);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR80_month() != null) {
cellC.setCellValue(record.getR80_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR80_ytd() != null) {
cellD.setCellValue(record.getR80_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}





// row83
row = sheet.getRow(82);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR83_month() != null) {
cellC.setCellValue(record.getR83_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR83_ytd() != null) {
cellD.setCellValue(record.getR83_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



// row85
row = sheet.getRow(84);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR85_month() != null) {
cellC.setCellValue(record.getR85_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR85_ytd() != null) {
cellD.setCellValue(record.getR85_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

}
workbook.setForceFormulaRecalculation(true);
} else {

}

// Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

//audit service summary resub format


ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SCI_E RESUB SUMMARY", null, "BRRS_M_SCI_E_RESUB_SUMMARYTABLE");
					}

return out.toByteArray();
}

}

// Resub Email Excel
public byte[] BRRS_M_SCI_EEmailResubExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Archival Email Excel generation process in memory.");

List<M_SCI_E_RESUB_Summary_Entity> dataList = getResubSummaryByDateAndVersion(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_SCI_E report. Returning empty result.");
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
M_SCI_E_RESUB_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

//row7
//Column B
Cell cellBdate = row.createCell(1);
if (record.getReport_date() != null) {
	cellBdate.setCellValue(record.getReport_date());
	cellBdate.setCellStyle(dateStyle);
} else {
	cellBdate.setCellValue("");
	cellBdate.setCellStyle(textStyle);
}



//ROW 11
row = sheet.getRow(10);

Cell cellC,cellD;   


//--R10 1. Total interest and fee income from loans and advances (sum of lines (i) to (viii))

//---R11 (i) Central Government (Government of Botswana)


//row11

// Column 3 - month
cellC = row.createCell(2);
if (record.getR11_month() != null) {
cellC.setCellValue(record.getR11_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR11_ytd() != null) {
cellD.setCellValue(record.getR11_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R12 (ii) Local Government

// row12
row = sheet.getRow(11);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR12_month() != null) {
cellC.setCellValue(record.getR12_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR12_ytd() != null) {
cellD.setCellValue(record.getR12_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R13 (iii) Public Non-Financial Corporations


//row13
row = sheet.getRow(12);
// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR13_month() != null) {
cellC.setCellValue(record.getR13_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR13_ytd() != null) {
cellD.setCellValue(record.getR13_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//---R14 (iv) Other Non-Financial Corporations (Private business enterprises) 

//------R15 a) Agriculture, Forestry, Fishing

//row15
row = sheet.getRow(14); // Row index for R15 (0-based index)

// Column 1 - product name


// Column 2 - cross_reference


// Column 3 - month
cellC = row.createCell(2);
if (record.getR15_month() != null) {
cellC.setCellValue(record.getR15_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR15_ytd() != null) {
cellD.setCellValue(record.getR15_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R16 b) Mining and Quarrying
// row16
row = sheet.getRow(15);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR16_month() != null) {
cellC.setCellValue(record.getR16_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR16_ytd() != null) {
cellD.setCellValue(record.getR16_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R17 c) Manufacturing
// row17
row = sheet.getRow(16);



cellC = row.createCell(2);
if (record.getR17_month() != null) {
cellC.setCellValue(record.getR17_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR17_ytd() != null) {
cellD.setCellValue(record.getR17_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R18 d) Construction

//row18
row = sheet.getRow(17);



cellC = row.createCell(2);
if (record.getR18_month() != null) {
cellC.setCellValue(record.getR18_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR18_ytd() != null) {
cellD.setCellValue(record.getR18_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R19 e) Commercial real estate

// row19
row = sheet.getRow(18);



cellC = row.createCell(2);
if (record.getR19_month() != null) {
cellC.setCellValue(record.getR19_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR19_ytd() != null) {
cellD.setCellValue(record.getR19_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R20 f) Electricity

// row20
row = sheet.getRow(19);



cellC = row.createCell(2);
if (record.getR20_month() != null) {
cellC.setCellValue(record.getR20_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR20_ytd() != null) {
cellD.setCellValue(record.getR20_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R21 g) Water

//row21
row = sheet.getRow(20);



cellC = row.createCell(2);
if (record.getR21_month() != null) {
cellC.setCellValue(record.getR21_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR21_ytd() != null) {
cellD.setCellValue(record.getR21_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R22 h) Telecommunication and Post
// row22
row = sheet.getRow(21);



cellC = row.createCell(2);
if (record.getR22_month() != null) {
cellC.setCellValue(record.getR22_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR22_ytd() != null) {
cellD.setCellValue(record.getR22_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R23 i) Tourism and hotels

//row23
row = sheet.getRow(22);



cellC = row.createCell(2);
if (record.getR23_month() != null) {
cellC.setCellValue(record.getR23_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR23_ytd() != null) {
cellD.setCellValue(record.getR23_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R24 j) Transport and Storage

//row24
row = sheet.getRow(23);



cellC = row.createCell(2);
if (record.getR24_month() != null) {
cellC.setCellValue(record.getR24_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR24_ytd() != null) {
cellD.setCellValue(record.getR24_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R25 k) Trade, restaurants and bars

// row25
row = sheet.getRow(24);



cellC = row.createCell(2);
if (record.getR25_month() != null) {
cellC.setCellValue(record.getR25_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR25_ytd() != null) {
cellD.setCellValue(record.getR25_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//------R26 l) Business Services

// row26
row = sheet.getRow(25);

// Column 1 - product name


// Column 3 - month
cellC = row.createCell(2);
if (record.getR26_month() != null) {
cellC.setCellValue(record.getR26_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR26_ytd() != null) {
cellD.setCellValue(record.getR26_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R27 m) Other Community, Social and Personal Services
// row27
row = sheet.getRow(26);




// Column 3 - month
cellC = row.createCell(2);
if (record.getR27_month() != null) {
cellC.setCellValue(record.getR27_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR27_ytd() != null) {
cellD.setCellValue(record.getR27_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R28 (v) Households (sum of lines (a) to (h))
//------R29 a) Residential property (owner occupied)

// row29
row = sheet.getRow(28);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR29_month() != null) {
cellC.setCellValue(record.getR29_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR29_ytd() != null) {
cellD.setCellValue(record.getR29_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R30 b) Residential property (Rented)

//row30
row = sheet.getRow(29);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR30_month() != null) {
cellC.setCellValue(record.getR30_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR30_ytd() != null) {
cellD.setCellValue(record.getR30_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R31 c) Personal Loans

// row31
row = sheet.getRow(30);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR31_month() != null) {
cellC.setCellValue(record.getR31_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR31_ytd() != null) {
cellD.setCellValue(record.getR31_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R32 d) Motor Vehicle

// ------------------------- Row 32 -------------------------
row = sheet.getRow(31);
// Column 1 - product name

// Column 3 - month
cellC = row.createCell(2);
if (record.getR32_month() != null) {
cellC.setCellValue(record.getR32_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR32_ytd() != null) {
cellD.setCellValue(record.getR32_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R33 e) Household goods

// ------------------------- Row 33 -------------------------
row = sheet.getRow(32);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR33_month() != null) {
cellC.setCellValue(record.getR33_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR33_ytd() != null) {
cellD.setCellValue(record.getR33_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R34 f) Credit card loans
// ------------------------- Row 34 -------------------------
row = sheet.getRow(33);

// Column 3 - month
cellC = row.createCell(2);
if (record.getR34_month() != null) {
cellC.setCellValue(record.getR34_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR34_ytd() != null) {
cellD.setCellValue(record.getR34_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R35 g) Non-Profit Institutions Serving Households

//------------------------- Row 35 -------------------------
row = sheet.getRow(34);

cellC = row.createCell(2);
if (record.getR35_month() != null) {
cellC.setCellValue(record.getR35_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR35_ytd() != null) {
cellD.setCellValue(record.getR35_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R36 h) Other*

// ------------------------- Row 36 -------------------------
row = sheet.getRow(35);


cellC = row.createCell(2);
if (record.getR36_month() != null) {
cellC.setCellValue(record.getR36_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}
cellD = row.createCell(3);
if (record.getR36_ytd() != null) {
cellD.setCellValue(record.getR36_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//---R37 (vi) Total interest income on balances with other banks

//------R38 a) Domestic banks

// ------------------------- Row 38 -------------------------
row = sheet.getRow(37);



cellC = row.createCell(2);
if (record.getR38_month() != null) {
cellC.setCellValue(record.getR38_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

cellD = row.createCell(3);
if (record.getR38_ytd() != null) {
cellD.setCellValue(record.getR38_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R39 b) Banks abroad (Foreign banks)

//row39
row = sheet.getRow(38);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR39_month() != null) {
cellC.setCellValue(record.getR39_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR39_ytd() != null) {
cellD.setCellValue(record.getR39_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//---R40 (vii) Total income on investment and securities
//------R41 a) Held to maturity

//---------R42 (i) BOBCs

// row42
row = sheet.getRow(41);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR42_month() != null) {
cellC.setCellValue(record.getR42_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR42_ytd() != null) {
cellD.setCellValue(record.getR42_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---------R43 (ii) Other

// row43
row = sheet.getRow(42);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR43_month() != null) {
cellC.setCellValue(record.getR43_month().doubleValue()); // assuming it's BigDecimal or Double
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR43_ytd() != null) {
cellD.setCellValue(record.getR43_ytd().doubleValue()); // assuming it's BigDecimal or Double
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}	

//------R44 b) Available for Sale
//---------R45 (i) BOBCs

// row45
row = sheet.getRow(44);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR45_month() != null) {
cellC.setCellValue(record.getR45_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR45_ytd() != null) {
cellD.setCellValue(record.getR45_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//---------R46 (ii) Other

//row46
row = sheet.getRow(45);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR46_month() != null) {
cellC.setCellValue(record.getR46_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR46_ytd() != null) {
cellD.setCellValue(record.getR46_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R47 (viii) All other interest income*

// row47
row = sheet.getRow(46);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR47_month() != null) {
cellC.setCellValue(record.getR47_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR47_ytd() != null) {
cellD.setCellValue(record.getR47_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//--R48 2. Total interest expenses (sum of lines 2(i) to (iv))

//---R49 (i) Total interest expense on deposit accounts

//------R50 a) Demand

// row50
row = sheet.getRow(49);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR50_month() != null) {
cellC.setCellValue(record.getR50_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR50_ytd() != null) {
cellD.setCellValue(record.getR50_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R51 b) Savings

// row51
row = sheet.getRow(50);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR51_month() != null) {
cellC.setCellValue(record.getR51_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR51_ytd() != null) {
cellD.setCellValue(record.getR51_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R52 c) Time

// row52
row = sheet.getRow(51);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR52_month() != null) {
cellC.setCellValue(record.getR52_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR52_ytd() != null) {
cellD.setCellValue(record.getR52_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R53 (ii) Interest expense on inter-bank loans

// row53
row = sheet.getRow(52);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR53_month() != null) {
cellC.setCellValue(record.getR53_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR53_ytd() != null) {
cellD.setCellValue(record.getR53_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R54 (iii) Interest expense on funds borrowed from Bank of Botswana

// row54
row = sheet.getRow(53);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR54_month() != null) {
cellC.setCellValue(record.getR54_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR54_ytd() != null) {
cellD.setCellValue(record.getR54_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R55 (iv) Interest expense on other borrowed funds*

//row55
row = sheet.getRow(54);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR55_month() != null) {
cellC.setCellValue(record.getR55_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR55_ytd() != null) {
cellD.setCellValue(record.getR55_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//--R56 3. Net interest income (line 1 minus 2)
//--R57 4. Total Impairments (sum of lines 4(i) to (iii))

//---R58 (i) Impairment of loans and advances – Specific


// row58
row = sheet.getRow(57);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR58_month() != null) {
cellC.setCellValue(record.getR58_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR58_ytd() != null) {
cellD.setCellValue(record.getR58_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R59 (ii) Impairment of loans and advances – Portfolio


// row59
row = sheet.getRow(58);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR59_month() != null) {
cellC.setCellValue(record.getR59_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR59_ytd() != null) {
cellD.setCellValue(record.getR59_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R60 (iii) Impairment loss on other financial assets

//row60
row = sheet.getRow(59);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR60_month() != null) {
cellC.setCellValue(record.getR60_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR60_ytd() != null) {
cellD.setCellValue(record.getR60_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//--R61 5. Net Interest Income after Provisions (line 3 minus 4)
//--R62 6. Total non-interest income (sum of lines 6(i) to (viii))

//---R63 (i) Retail banking customer fees

//row63
row = sheet.getRow(62);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR63_month() != null) {
cellC.setCellValue(record.getR63_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR63_ytd() != null) {
cellD.setCellValue(record.getR63_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R64 (ii) Credit related fees

// row64
row = sheet.getRow(63);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR64_month() != null) {
cellC.setCellValue(record.getR64_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR64_ytd() != null) {
cellD.setCellValue(record.getR64_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R65 (iii) Foreign exchange (includes fees and commissions)


// row65
row = sheet.getRow(64);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR65_month() != null) {
cellC.setCellValue(record.getR65_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR65_ytd() != null) {
cellD.setCellValue(record.getR65_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R66 (iv) Bond trading

// row66
row = sheet.getRow(65);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR66_month() != null) {
cellC.setCellValue(record.getR66_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR66_ytd() != null) {
cellD.setCellValue(record.getR66_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R67 (v) Dividends

// row67
row = sheet.getRow(66);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR67_month() != null) {
cellC.setCellValue(record.getR67_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR67_ytd() != null) {
cellD.setCellValue(record.getR67_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R68 (vi) Insurance commissions

// row68
row = sheet.getRow(67);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR68_month() != null) {
cellC.setCellValue(record.getR68_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR68_ytd() != null) {
cellD.setCellValue(record.getR68_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

////---R69 (vii) Professional fees (sum of lines (a) to (d))
//
////------R70 a) Lawyer's fees
//
//
//// row76
//row = sheet.getRow(69);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR76_month() != null) {
//cellC.setCellValue(record.getR76_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR76_ytd() != null) {
//cellD.setCellValue(record.getR76_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}
//
//
////------R71 b) Auditor's fees
//
//// row77
//row = sheet.getRow(70);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR77_month() != null) {
//cellC.setCellValue(record.getR77_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR77_ytd() != null) {
//cellD.setCellValue(record.getR77_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}
//
//
////------R72 c) Management fees
//
////row78
//row = sheet.getRow(71);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR78_month() != null) {
//cellC.setCellValue(record.getR78_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR78_ytd() != null) {
//cellD.setCellValue(record.getR78_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}
//
//
////------R73 d) Other*
//
//// row79
//row = sheet.getRow(72);
//
//
//
//// Column 3 - month
//cellC = row.createCell(2);
//if (record.getR79_month() != null) {
//cellC.setCellValue(record.getR79_month().doubleValue());
//cellC.setCellStyle(numberStyle);
//} else {
//cellC.setCellValue("");
//cellC.setCellStyle(textStyle);
//}
//
//// Column 4 - ytd
//cellD = row.createCell(3);
//if (record.getR79_ytd() != null) {
//cellD.setCellValue(record.getR79_ytd().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}

//---R74 (viii) All other non-interest income*

// row69
row = sheet.getRow(73);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR69_month() != null) {
cellC.setCellValue(record.getR69_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR69_ytd() != null) {
cellD.setCellValue(record.getR69_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//--R75 7. Total non-interest expense (sum of lines 7(i) to (vi))

//---R76 (i) Salaries and employee benefits

// row71
row = sheet.getRow(75);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR71_month() != null) {
cellC.setCellValue(record.getR71_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR71_ytd() != null) {
cellD.setCellValue(record.getR71_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R77 (ii) Occupancy (net of rental income)

// row72
row = sheet.getRow(76);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR72_month() != null) {
cellC.setCellValue(record.getR72_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR72_ytd() != null) {
cellD.setCellValue(record.getR72_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//---R78 (iii) Depreciation

// row73
row = sheet.getRow(77);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR73_month() != null) {
cellC.setCellValue(record.getR73_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR73_ytd() != null) {
cellD.setCellValue(record.getR73_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//---R79 (iv) Impairment loss on other non-financial assets

// row74
row = sheet.getRow(78);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR74_month() != null) {
cellC.setCellValue(record.getR74_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR74_ytd() != null) {
cellD.setCellValue(record.getR74_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}




//---R80 (v) All other non-interest expense*

// row80
row = sheet.getRow(79);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR80_month() != null) {
cellC.setCellValue(record.getR80_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR80_ytd() != null) {
cellD.setCellValue(record.getR80_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



//---R81 (vi) Professional fees (sum of lines (a) to (d))

//row75
row = sheet.getRow(80);


// Column 3 - month
cellC = row.createCell(2);
if (record.getR75_month() != null) {
cellC.setCellValue(record.getR75_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR75_ytd() != null) {
cellD.setCellValue(record.getR75_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}
//------R82 a) Lawyer's fees

// row76
row = sheet.getRow(81);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR76_month() != null) {
cellC.setCellValue(record.getR76_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR76_ytd() != null) {
cellD.setCellValue(record.getR76_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//------R83 b) Auditor's fees

// row77
row = sheet.getRow(82);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR77_month() != null) {
cellC.setCellValue(record.getR77_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR77_ytd() != null) {
cellD.setCellValue(record.getR77_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R84 c) Management fees

// row78
row = sheet.getRow(83);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR78_month() != null) {
cellC.setCellValue(record.getR78_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR78_ytd() != null) {
cellD.setCellValue(record.getR78_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}


//------R85 d) Other*

// row79
row = sheet.getRow(84);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR79_month() != null) {
cellC.setCellValue(record.getR79_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR79_ytd() != null) {
cellD.setCellValue(record.getR79_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//--R87 8. Net non-interest income (line 6 minus 7)
//--R88 9. Income before taxation (line 5 plus 8)

//--R89 10. Taxation

//row83
row = sheet.getRow(88);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR83_month() != null) {
cellC.setCellValue(record.getR83_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR83_ytd() != null) {
cellD.setCellValue(record.getR83_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//--R90 11. Income (loss) after taxes (line 9 minus 10)

//--R91 12. Provision for dividends

// row85
row = sheet.getRow(90);



// Column 3 - month
cellC = row.createCell(2);
if (record.getR85_month() != null) {
cellC.setCellValue(record.getR85_month().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

// Column 4 - ytd
cellD = row.createCell(3);
if (record.getR85_ytd() != null) {
cellD.setCellValue(record.getR85_ytd().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}



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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SCI_E EMAIL RESUB SUMMARY", null, "BRRS_M_SCI_E_RESUB_SUMMARYTABLE");
				}

return out.toByteArray();
}
}


	
	
	
	



	// ── RowMapper inner classes ────────────────────────────────────────────

	// =========================================================
	// Inner entity: M_SCI_E_Summary_Entity
	// =========================================================
	public static class M_SCI_E_Summary_Entity {
		private String r10_product_name;
		private String r10_cross_reference;
		private BigDecimal r10_month;
		private BigDecimal r10_ytd;
		private String r11_product_name;
		private String r11_cross_reference;
		private BigDecimal r11_month;
		private BigDecimal r11_ytd;
		private String r12_product_name;
		private String r12_cross_reference;
		private BigDecimal r12_month;
		private BigDecimal r12_ytd;
		private String r13_product_name;
		private String r13_cross_reference;
		private BigDecimal r13_month;
		private BigDecimal r13_ytd;
		private String r14_product_name;
		private String r14_cross_reference;
		private BigDecimal r14_month;
		private BigDecimal r14_ytd;
		private String r15_product_name;
		private String r15_cross_reference;
		private BigDecimal r15_month;
		private BigDecimal r15_ytd;
		private String r16_product_name;
		private String r16_cross_reference;
		private BigDecimal r16_month;
		private BigDecimal r16_ytd;
		private String r17_product_name;
		private String r17_cross_reference;
		private BigDecimal r17_month;
		private BigDecimal r17_ytd;
		private String r18_product_name;
		private String r18_cross_reference;
		private BigDecimal r18_month;
		private BigDecimal r18_ytd;
		private String r19_product_name;
		private String r19_cross_reference;
		private BigDecimal r19_month;
		private BigDecimal r19_ytd;
		private String r20_product_name;
		private String r20_cross_reference;
		private BigDecimal r20_month;
		private BigDecimal r20_ytd;
		private String r21_product_name;
		private String r21_cross_reference;
		private BigDecimal r21_month;
		private BigDecimal r21_ytd;
		private String r22_product_name;
		private String r22_cross_reference;
		private BigDecimal r22_month;
		private BigDecimal r22_ytd;
		private String r23_product_name;
		private String r23_cross_reference;
		private BigDecimal r23_month;
		private BigDecimal r23_ytd;
		private String r24_product_name;
		private String r24_cross_reference;
		private BigDecimal r24_month;
		private BigDecimal r24_ytd;
		private String r25_product_name;
		private String r25_cross_reference;
		private BigDecimal r25_month;
		private BigDecimal r25_ytd;
		private String r26_product_name;
		private String r26_cross_reference;
		private BigDecimal r26_month;
		private BigDecimal r26_ytd;
		private String r27_product_name;
		private String r27_cross_reference;
		private BigDecimal r27_month;
		private BigDecimal r27_ytd;
		private String r28_product_name;
		private String r28_cross_reference;
		private BigDecimal r28_month;
		private BigDecimal r28_ytd;
		private String r29_product_name;
		private String r29_cross_reference;
		private BigDecimal r29_month;
		private BigDecimal r29_ytd;
		private String r30_product_name;
		private String r30_cross_reference;
		private BigDecimal r30_month;
		private BigDecimal r30_ytd;
		private String r31_product_name;
		private String r31_cross_reference;
		private BigDecimal r31_month;
		private BigDecimal r31_ytd;
		private String r32_product_name;
		private String r32_cross_reference;
		private BigDecimal r32_month;
		private BigDecimal r32_ytd;
		private String r33_product_name;
		private String r33_cross_reference;
		private BigDecimal r33_month;
		private BigDecimal r33_ytd;
		private String r34_product_name;
		private String r34_cross_reference;
		private BigDecimal r34_month;
		private BigDecimal r34_ytd;
		private String r35_product_name;
		private String r35_cross_reference;
		private BigDecimal r35_month;
		private BigDecimal r35_ytd;
		private String r36_product_name;
		private String r36_cross_reference;
		private BigDecimal r36_month;
		private BigDecimal r36_ytd;
		private String r37_product_name;
		private String r37_cross_reference;
		private BigDecimal r37_month;
		private BigDecimal r37_ytd;
		private String r38_product_name;
		private String r38_cross_reference;
		private BigDecimal r38_month;
		private BigDecimal r38_ytd;
		private String r39_product_name;
		private String r39_cross_reference;
		private BigDecimal r39_month;
		private BigDecimal r39_ytd;
		private String r40_product_name;
		private String r40_cross_reference;
		private BigDecimal r40_month;
		private BigDecimal r40_ytd;
		private String r41_product_name;
		private String r41_cross_reference;
		private BigDecimal r41_month;
		private BigDecimal r41_ytd;
		private String r42_product_name;
		private String r42_cross_reference;
		private BigDecimal r42_month;
		private BigDecimal r42_ytd;
		private String r43_product_name;
		private String r43_cross_reference;
		private BigDecimal r43_month;
		private BigDecimal r43_ytd;
		private String r44_product_name;
		private String r44_cross_reference;
		private BigDecimal r44_month;
		private BigDecimal r44_ytd;
		private String r45_product_name;
		private String r45_cross_reference;
		private BigDecimal r45_month;
		private BigDecimal r45_ytd;
		private String r46_product_name;
		private String r46_cross_reference;
		private BigDecimal r46_month;
		private BigDecimal r46_ytd;
		private String r47_product_name;
		private String r47_cross_reference;
		private BigDecimal r47_month;
		private BigDecimal r47_ytd;
		private String r48_product_name;
		private String r48_cross_reference;
		private BigDecimal r48_month;
		private BigDecimal r48_ytd;
		private String r49_product_name;
		private String r49_cross_reference;
		private BigDecimal r49_month;
		private BigDecimal r49_ytd;
		private String r50_product_name;
		private String r50_cross_reference;
		private BigDecimal r50_month;
		private BigDecimal r50_ytd;
		private String r51_product_name;
		private String r51_cross_reference;
		private BigDecimal r51_month;
		private BigDecimal r51_ytd;
		private String r52_product_name;
		private String r52_cross_reference;
		private BigDecimal r52_month;
		private BigDecimal r52_ytd;
		private String r53_product_name;
		private String r53_cross_reference;
		private BigDecimal r53_month;
		private BigDecimal r53_ytd;
		private String r54_product_name;
		private String r54_cross_reference;
		private BigDecimal r54_month;
		private BigDecimal r54_ytd;
		private String r55_product_name;
		private String r55_cross_reference;
		private BigDecimal r55_month;
		private BigDecimal r55_ytd;
		private String r56_product_name;
		private String r56_cross_reference;
		private BigDecimal r56_month;
		private BigDecimal r56_ytd;
		private String r57_product_name;
		private String r57_cross_reference;
		private BigDecimal r57_month;
		private BigDecimal r57_ytd;
		private String r58_product_name;
		private String r58_cross_reference;
		private BigDecimal r58_month;
		private BigDecimal r58_ytd;
		private String r59_product_name;
		private String r59_cross_reference;
		private BigDecimal r59_month;
		private BigDecimal r59_ytd;
		private String r60_product_name;
		private String r60_cross_reference;
		private BigDecimal r60_month;
		private BigDecimal r60_ytd;
		private String r61_product_name;
		private String r61_cross_reference;
		private BigDecimal r61_month;
		private BigDecimal r61_ytd;
		private String r62_product_name;
		private String r62_cross_reference;
		private BigDecimal r62_month;
		private BigDecimal r62_ytd;
		private String r63_product_name;
		private String r63_cross_reference;
		private BigDecimal r63_month;
		private BigDecimal r63_ytd;
		private String r64_product_name;
		private String r64_cross_reference;
		private BigDecimal r64_month;
		private BigDecimal r64_ytd;
		private String r65_product_name;
		private String r65_cross_reference;
		private BigDecimal r65_month;
		private BigDecimal r65_ytd;
		private String r66_product_name;
		private String r66_cross_reference;
		private BigDecimal r66_month;
		private BigDecimal r66_ytd;
		private String r67_product_name;
		private String r67_cross_reference;
		private BigDecimal r67_month;
		private BigDecimal r67_ytd;
		private String r68_product_name;
		private String r68_cross_reference;
		private BigDecimal r68_month;
		private BigDecimal r68_ytd;
		private String r69_product_name;
		private String r69_cross_reference;
		private BigDecimal r69_month;
		private BigDecimal r69_ytd;
		private String r70_product_name;
		private String r70_cross_reference;
		private BigDecimal r70_month;
		private BigDecimal r70_ytd;
		private String r71_product_name;
		private String r71_cross_reference;
		private BigDecimal r71_month;
		private BigDecimal r71_ytd;
		private String r72_product_name;
		private String r72_cross_reference;
		private BigDecimal r72_month;
		private BigDecimal r72_ytd;
		private String r73_product_name;
		private String r73_cross_reference;
		private BigDecimal r73_month;
		private BigDecimal r73_ytd;
		private String r74_product_name;
		private String r74_cross_reference;
		private BigDecimal r74_month;
		private BigDecimal r74_ytd;
		private String r75_product_name;
		private String r75_cross_reference;
		private BigDecimal r75_month;
		private BigDecimal r75_ytd;
		private String r76_product_name;
		private String r76_cross_reference;
		private BigDecimal r76_month;
		private BigDecimal r76_ytd;
		private String r77_product_name;
		private String r77_cross_reference;
		private BigDecimal r77_month;
		private BigDecimal r77_ytd;
		private String r78_product_name;
		private String r78_cross_reference;
		private BigDecimal r78_month;
		private BigDecimal r78_ytd;
		private String r79_product_name;
		private String r79_cross_reference;
		private BigDecimal r79_month;
		private BigDecimal r79_ytd;
		private String r80_product_name;
		private String r80_cross_reference;
		private BigDecimal r80_month;
		private BigDecimal r80_ytd;
		private String r81_product_name;
		private String r81_cross_reference;
		private BigDecimal r81_month;
		private BigDecimal r81_ytd;
		private String r82_product_name;
		private String r82_cross_reference;
		private BigDecimal r82_month;
		private BigDecimal r82_ytd;
		private String r83_product_name;
		private String r83_cross_reference;
		private BigDecimal r83_month;
		private BigDecimal r83_ytd;
		private String r84_product_name;
		private String r84_cross_reference;
		private BigDecimal r84_month;
		private BigDecimal r84_ytd;
		private String r85_product_name;
		private String r85_cross_reference;
		private BigDecimal r85_month;
		private BigDecimal r85_ytd;
		private String r86_product_name;
		private String r86_cross_reference;
		private BigDecimal r86_month;
		private BigDecimal r86_ytd;

		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product_name() { return r10_product_name; }
		public void setR10_product_name(String r10_product_name) { this.r10_product_name = r10_product_name; }
		public String getR10_cross_reference() { return r10_cross_reference; }
		public void setR10_cross_reference(String r10_cross_reference) { this.r10_cross_reference = r10_cross_reference; }
		public BigDecimal getR10_month() { return r10_month; }
		public void setR10_month(BigDecimal r10_month) { this.r10_month = r10_month; }
		public BigDecimal getR10_ytd() { return r10_ytd; }
		public void setR10_ytd(BigDecimal r10_ytd) { this.r10_ytd = r10_ytd; }
		public String getR11_product_name() { return r11_product_name; }
		public void setR11_product_name(String r11_product_name) { this.r11_product_name = r11_product_name; }
		public String getR11_cross_reference() { return r11_cross_reference; }
		public void setR11_cross_reference(String r11_cross_reference) { this.r11_cross_reference = r11_cross_reference; }
		public BigDecimal getR11_month() { return r11_month; }
		public void setR11_month(BigDecimal r11_month) { this.r11_month = r11_month; }
		public BigDecimal getR11_ytd() { return r11_ytd; }
		public void setR11_ytd(BigDecimal r11_ytd) { this.r11_ytd = r11_ytd; }
		public String getR12_product_name() { return r12_product_name; }
		public void setR12_product_name(String r12_product_name) { this.r12_product_name = r12_product_name; }
		public String getR12_cross_reference() { return r12_cross_reference; }
		public void setR12_cross_reference(String r12_cross_reference) { this.r12_cross_reference = r12_cross_reference; }
		public BigDecimal getR12_month() { return r12_month; }
		public void setR12_month(BigDecimal r12_month) { this.r12_month = r12_month; }
		public BigDecimal getR12_ytd() { return r12_ytd; }
		public void setR12_ytd(BigDecimal r12_ytd) { this.r12_ytd = r12_ytd; }
		public String getR13_product_name() { return r13_product_name; }
		public void setR13_product_name(String r13_product_name) { this.r13_product_name = r13_product_name; }
		public String getR13_cross_reference() { return r13_cross_reference; }
		public void setR13_cross_reference(String r13_cross_reference) { this.r13_cross_reference = r13_cross_reference; }
		public BigDecimal getR13_month() { return r13_month; }
		public void setR13_month(BigDecimal r13_month) { this.r13_month = r13_month; }
		public BigDecimal getR13_ytd() { return r13_ytd; }
		public void setR13_ytd(BigDecimal r13_ytd) { this.r13_ytd = r13_ytd; }
		public String getR14_product_name() { return r14_product_name; }
		public void setR14_product_name(String r14_product_name) { this.r14_product_name = r14_product_name; }
		public String getR14_cross_reference() { return r14_cross_reference; }
		public void setR14_cross_reference(String r14_cross_reference) { this.r14_cross_reference = r14_cross_reference; }
		public BigDecimal getR14_month() { return r14_month; }
		public void setR14_month(BigDecimal r14_month) { this.r14_month = r14_month; }
		public BigDecimal getR14_ytd() { return r14_ytd; }
		public void setR14_ytd(BigDecimal r14_ytd) { this.r14_ytd = r14_ytd; }
		public String getR15_product_name() { return r15_product_name; }
		public void setR15_product_name(String r15_product_name) { this.r15_product_name = r15_product_name; }
		public String getR15_cross_reference() { return r15_cross_reference; }
		public void setR15_cross_reference(String r15_cross_reference) { this.r15_cross_reference = r15_cross_reference; }
		public BigDecimal getR15_month() { return r15_month; }
		public void setR15_month(BigDecimal r15_month) { this.r15_month = r15_month; }
		public BigDecimal getR15_ytd() { return r15_ytd; }
		public void setR15_ytd(BigDecimal r15_ytd) { this.r15_ytd = r15_ytd; }
		public String getR16_product_name() { return r16_product_name; }
		public void setR16_product_name(String r16_product_name) { this.r16_product_name = r16_product_name; }
		public String getR16_cross_reference() { return r16_cross_reference; }
		public void setR16_cross_reference(String r16_cross_reference) { this.r16_cross_reference = r16_cross_reference; }
		public BigDecimal getR16_month() { return r16_month; }
		public void setR16_month(BigDecimal r16_month) { this.r16_month = r16_month; }
		public BigDecimal getR16_ytd() { return r16_ytd; }
		public void setR16_ytd(BigDecimal r16_ytd) { this.r16_ytd = r16_ytd; }
		public String getR17_product_name() { return r17_product_name; }
		public void setR17_product_name(String r17_product_name) { this.r17_product_name = r17_product_name; }
		public String getR17_cross_reference() { return r17_cross_reference; }
		public void setR17_cross_reference(String r17_cross_reference) { this.r17_cross_reference = r17_cross_reference; }
		public BigDecimal getR17_month() { return r17_month; }
		public void setR17_month(BigDecimal r17_month) { this.r17_month = r17_month; }
		public BigDecimal getR17_ytd() { return r17_ytd; }
		public void setR17_ytd(BigDecimal r17_ytd) { this.r17_ytd = r17_ytd; }
		public String getR18_product_name() { return r18_product_name; }
		public void setR18_product_name(String r18_product_name) { this.r18_product_name = r18_product_name; }
		public String getR18_cross_reference() { return r18_cross_reference; }
		public void setR18_cross_reference(String r18_cross_reference) { this.r18_cross_reference = r18_cross_reference; }
		public BigDecimal getR18_month() { return r18_month; }
		public void setR18_month(BigDecimal r18_month) { this.r18_month = r18_month; }
		public BigDecimal getR18_ytd() { return r18_ytd; }
		public void setR18_ytd(BigDecimal r18_ytd) { this.r18_ytd = r18_ytd; }
		public String getR19_product_name() { return r19_product_name; }
		public void setR19_product_name(String r19_product_name) { this.r19_product_name = r19_product_name; }
		public String getR19_cross_reference() { return r19_cross_reference; }
		public void setR19_cross_reference(String r19_cross_reference) { this.r19_cross_reference = r19_cross_reference; }
		public BigDecimal getR19_month() { return r19_month; }
		public void setR19_month(BigDecimal r19_month) { this.r19_month = r19_month; }
		public BigDecimal getR19_ytd() { return r19_ytd; }
		public void setR19_ytd(BigDecimal r19_ytd) { this.r19_ytd = r19_ytd; }
		public String getR20_product_name() { return r20_product_name; }
		public void setR20_product_name(String r20_product_name) { this.r20_product_name = r20_product_name; }
		public String getR20_cross_reference() { return r20_cross_reference; }
		public void setR20_cross_reference(String r20_cross_reference) { this.r20_cross_reference = r20_cross_reference; }
		public BigDecimal getR20_month() { return r20_month; }
		public void setR20_month(BigDecimal r20_month) { this.r20_month = r20_month; }
		public BigDecimal getR20_ytd() { return r20_ytd; }
		public void setR20_ytd(BigDecimal r20_ytd) { this.r20_ytd = r20_ytd; }
		public String getR21_product_name() { return r21_product_name; }
		public void setR21_product_name(String r21_product_name) { this.r21_product_name = r21_product_name; }
		public String getR21_cross_reference() { return r21_cross_reference; }
		public void setR21_cross_reference(String r21_cross_reference) { this.r21_cross_reference = r21_cross_reference; }
		public BigDecimal getR21_month() { return r21_month; }
		public void setR21_month(BigDecimal r21_month) { this.r21_month = r21_month; }
		public BigDecimal getR21_ytd() { return r21_ytd; }
		public void setR21_ytd(BigDecimal r21_ytd) { this.r21_ytd = r21_ytd; }
		public String getR22_product_name() { return r22_product_name; }
		public void setR22_product_name(String r22_product_name) { this.r22_product_name = r22_product_name; }
		public String getR22_cross_reference() { return r22_cross_reference; }
		public void setR22_cross_reference(String r22_cross_reference) { this.r22_cross_reference = r22_cross_reference; }
		public BigDecimal getR22_month() { return r22_month; }
		public void setR22_month(BigDecimal r22_month) { this.r22_month = r22_month; }
		public BigDecimal getR22_ytd() { return r22_ytd; }
		public void setR22_ytd(BigDecimal r22_ytd) { this.r22_ytd = r22_ytd; }
		public String getR23_product_name() { return r23_product_name; }
		public void setR23_product_name(String r23_product_name) { this.r23_product_name = r23_product_name; }
		public String getR23_cross_reference() { return r23_cross_reference; }
		public void setR23_cross_reference(String r23_cross_reference) { this.r23_cross_reference = r23_cross_reference; }
		public BigDecimal getR23_month() { return r23_month; }
		public void setR23_month(BigDecimal r23_month) { this.r23_month = r23_month; }
		public BigDecimal getR23_ytd() { return r23_ytd; }
		public void setR23_ytd(BigDecimal r23_ytd) { this.r23_ytd = r23_ytd; }
		public String getR24_product_name() { return r24_product_name; }
		public void setR24_product_name(String r24_product_name) { this.r24_product_name = r24_product_name; }
		public String getR24_cross_reference() { return r24_cross_reference; }
		public void setR24_cross_reference(String r24_cross_reference) { this.r24_cross_reference = r24_cross_reference; }
		public BigDecimal getR24_month() { return r24_month; }
		public void setR24_month(BigDecimal r24_month) { this.r24_month = r24_month; }
		public BigDecimal getR24_ytd() { return r24_ytd; }
		public void setR24_ytd(BigDecimal r24_ytd) { this.r24_ytd = r24_ytd; }
		public String getR25_product_name() { return r25_product_name; }
		public void setR25_product_name(String r25_product_name) { this.r25_product_name = r25_product_name; }
		public String getR25_cross_reference() { return r25_cross_reference; }
		public void setR25_cross_reference(String r25_cross_reference) { this.r25_cross_reference = r25_cross_reference; }
		public BigDecimal getR25_month() { return r25_month; }
		public void setR25_month(BigDecimal r25_month) { this.r25_month = r25_month; }
		public BigDecimal getR25_ytd() { return r25_ytd; }
		public void setR25_ytd(BigDecimal r25_ytd) { this.r25_ytd = r25_ytd; }
		public String getR26_product_name() { return r26_product_name; }
		public void setR26_product_name(String r26_product_name) { this.r26_product_name = r26_product_name; }
		public String getR26_cross_reference() { return r26_cross_reference; }
		public void setR26_cross_reference(String r26_cross_reference) { this.r26_cross_reference = r26_cross_reference; }
		public BigDecimal getR26_month() { return r26_month; }
		public void setR26_month(BigDecimal r26_month) { this.r26_month = r26_month; }
		public BigDecimal getR26_ytd() { return r26_ytd; }
		public void setR26_ytd(BigDecimal r26_ytd) { this.r26_ytd = r26_ytd; }
		public String getR27_product_name() { return r27_product_name; }
		public void setR27_product_name(String r27_product_name) { this.r27_product_name = r27_product_name; }
		public String getR27_cross_reference() { return r27_cross_reference; }
		public void setR27_cross_reference(String r27_cross_reference) { this.r27_cross_reference = r27_cross_reference; }
		public BigDecimal getR27_month() { return r27_month; }
		public void setR27_month(BigDecimal r27_month) { this.r27_month = r27_month; }
		public BigDecimal getR27_ytd() { return r27_ytd; }
		public void setR27_ytd(BigDecimal r27_ytd) { this.r27_ytd = r27_ytd; }
		public String getR28_product_name() { return r28_product_name; }
		public void setR28_product_name(String r28_product_name) { this.r28_product_name = r28_product_name; }
		public String getR28_cross_reference() { return r28_cross_reference; }
		public void setR28_cross_reference(String r28_cross_reference) { this.r28_cross_reference = r28_cross_reference; }
		public BigDecimal getR28_month() { return r28_month; }
		public void setR28_month(BigDecimal r28_month) { this.r28_month = r28_month; }
		public BigDecimal getR28_ytd() { return r28_ytd; }
		public void setR28_ytd(BigDecimal r28_ytd) { this.r28_ytd = r28_ytd; }
		public String getR29_product_name() { return r29_product_name; }
		public void setR29_product_name(String r29_product_name) { this.r29_product_name = r29_product_name; }
		public String getR29_cross_reference() { return r29_cross_reference; }
		public void setR29_cross_reference(String r29_cross_reference) { this.r29_cross_reference = r29_cross_reference; }
		public BigDecimal getR29_month() { return r29_month; }
		public void setR29_month(BigDecimal r29_month) { this.r29_month = r29_month; }
		public BigDecimal getR29_ytd() { return r29_ytd; }
		public void setR29_ytd(BigDecimal r29_ytd) { this.r29_ytd = r29_ytd; }
		public String getR30_product_name() { return r30_product_name; }
		public void setR30_product_name(String r30_product_name) { this.r30_product_name = r30_product_name; }
		public String getR30_cross_reference() { return r30_cross_reference; }
		public void setR30_cross_reference(String r30_cross_reference) { this.r30_cross_reference = r30_cross_reference; }
		public BigDecimal getR30_month() { return r30_month; }
		public void setR30_month(BigDecimal r30_month) { this.r30_month = r30_month; }
		public BigDecimal getR30_ytd() { return r30_ytd; }
		public void setR30_ytd(BigDecimal r30_ytd) { this.r30_ytd = r30_ytd; }
		public String getR31_product_name() { return r31_product_name; }
		public void setR31_product_name(String r31_product_name) { this.r31_product_name = r31_product_name; }
		public String getR31_cross_reference() { return r31_cross_reference; }
		public void setR31_cross_reference(String r31_cross_reference) { this.r31_cross_reference = r31_cross_reference; }
		public BigDecimal getR31_month() { return r31_month; }
		public void setR31_month(BigDecimal r31_month) { this.r31_month = r31_month; }
		public BigDecimal getR31_ytd() { return r31_ytd; }
		public void setR31_ytd(BigDecimal r31_ytd) { this.r31_ytd = r31_ytd; }
		public String getR32_product_name() { return r32_product_name; }
		public void setR32_product_name(String r32_product_name) { this.r32_product_name = r32_product_name; }
		public String getR32_cross_reference() { return r32_cross_reference; }
		public void setR32_cross_reference(String r32_cross_reference) { this.r32_cross_reference = r32_cross_reference; }
		public BigDecimal getR32_month() { return r32_month; }
		public void setR32_month(BigDecimal r32_month) { this.r32_month = r32_month; }
		public BigDecimal getR32_ytd() { return r32_ytd; }
		public void setR32_ytd(BigDecimal r32_ytd) { this.r32_ytd = r32_ytd; }
		public String getR33_product_name() { return r33_product_name; }
		public void setR33_product_name(String r33_product_name) { this.r33_product_name = r33_product_name; }
		public String getR33_cross_reference() { return r33_cross_reference; }
		public void setR33_cross_reference(String r33_cross_reference) { this.r33_cross_reference = r33_cross_reference; }
		public BigDecimal getR33_month() { return r33_month; }
		public void setR33_month(BigDecimal r33_month) { this.r33_month = r33_month; }
		public BigDecimal getR33_ytd() { return r33_ytd; }
		public void setR33_ytd(BigDecimal r33_ytd) { this.r33_ytd = r33_ytd; }
		public String getR34_product_name() { return r34_product_name; }
		public void setR34_product_name(String r34_product_name) { this.r34_product_name = r34_product_name; }
		public String getR34_cross_reference() { return r34_cross_reference; }
		public void setR34_cross_reference(String r34_cross_reference) { this.r34_cross_reference = r34_cross_reference; }
		public BigDecimal getR34_month() { return r34_month; }
		public void setR34_month(BigDecimal r34_month) { this.r34_month = r34_month; }
		public BigDecimal getR34_ytd() { return r34_ytd; }
		public void setR34_ytd(BigDecimal r34_ytd) { this.r34_ytd = r34_ytd; }
		public String getR35_product_name() { return r35_product_name; }
		public void setR35_product_name(String r35_product_name) { this.r35_product_name = r35_product_name; }
		public String getR35_cross_reference() { return r35_cross_reference; }
		public void setR35_cross_reference(String r35_cross_reference) { this.r35_cross_reference = r35_cross_reference; }
		public BigDecimal getR35_month() { return r35_month; }
		public void setR35_month(BigDecimal r35_month) { this.r35_month = r35_month; }
		public BigDecimal getR35_ytd() { return r35_ytd; }
		public void setR35_ytd(BigDecimal r35_ytd) { this.r35_ytd = r35_ytd; }
		public String getR36_product_name() { return r36_product_name; }
		public void setR36_product_name(String r36_product_name) { this.r36_product_name = r36_product_name; }
		public String getR36_cross_reference() { return r36_cross_reference; }
		public void setR36_cross_reference(String r36_cross_reference) { this.r36_cross_reference = r36_cross_reference; }
		public BigDecimal getR36_month() { return r36_month; }
		public void setR36_month(BigDecimal r36_month) { this.r36_month = r36_month; }
		public BigDecimal getR36_ytd() { return r36_ytd; }
		public void setR36_ytd(BigDecimal r36_ytd) { this.r36_ytd = r36_ytd; }
		public String getR37_product_name() { return r37_product_name; }
		public void setR37_product_name(String r37_product_name) { this.r37_product_name = r37_product_name; }
		public String getR37_cross_reference() { return r37_cross_reference; }
		public void setR37_cross_reference(String r37_cross_reference) { this.r37_cross_reference = r37_cross_reference; }
		public BigDecimal getR37_month() { return r37_month; }
		public void setR37_month(BigDecimal r37_month) { this.r37_month = r37_month; }
		public BigDecimal getR37_ytd() { return r37_ytd; }
		public void setR37_ytd(BigDecimal r37_ytd) { this.r37_ytd = r37_ytd; }
		public String getR38_product_name() { return r38_product_name; }
		public void setR38_product_name(String r38_product_name) { this.r38_product_name = r38_product_name; }
		public String getR38_cross_reference() { return r38_cross_reference; }
		public void setR38_cross_reference(String r38_cross_reference) { this.r38_cross_reference = r38_cross_reference; }
		public BigDecimal getR38_month() { return r38_month; }
		public void setR38_month(BigDecimal r38_month) { this.r38_month = r38_month; }
		public BigDecimal getR38_ytd() { return r38_ytd; }
		public void setR38_ytd(BigDecimal r38_ytd) { this.r38_ytd = r38_ytd; }
		public String getR39_product_name() { return r39_product_name; }
		public void setR39_product_name(String r39_product_name) { this.r39_product_name = r39_product_name; }
		public String getR39_cross_reference() { return r39_cross_reference; }
		public void setR39_cross_reference(String r39_cross_reference) { this.r39_cross_reference = r39_cross_reference; }
		public BigDecimal getR39_month() { return r39_month; }
		public void setR39_month(BigDecimal r39_month) { this.r39_month = r39_month; }
		public BigDecimal getR39_ytd() { return r39_ytd; }
		public void setR39_ytd(BigDecimal r39_ytd) { this.r39_ytd = r39_ytd; }
		public String getR40_product_name() { return r40_product_name; }
		public void setR40_product_name(String r40_product_name) { this.r40_product_name = r40_product_name; }
		public String getR40_cross_reference() { return r40_cross_reference; }
		public void setR40_cross_reference(String r40_cross_reference) { this.r40_cross_reference = r40_cross_reference; }
		public BigDecimal getR40_month() { return r40_month; }
		public void setR40_month(BigDecimal r40_month) { this.r40_month = r40_month; }
		public BigDecimal getR40_ytd() { return r40_ytd; }
		public void setR40_ytd(BigDecimal r40_ytd) { this.r40_ytd = r40_ytd; }
		public String getR41_product_name() { return r41_product_name; }
		public void setR41_product_name(String r41_product_name) { this.r41_product_name = r41_product_name; }
		public String getR41_cross_reference() { return r41_cross_reference; }
		public void setR41_cross_reference(String r41_cross_reference) { this.r41_cross_reference = r41_cross_reference; }
		public BigDecimal getR41_month() { return r41_month; }
		public void setR41_month(BigDecimal r41_month) { this.r41_month = r41_month; }
		public BigDecimal getR41_ytd() { return r41_ytd; }
		public void setR41_ytd(BigDecimal r41_ytd) { this.r41_ytd = r41_ytd; }
		public String getR42_product_name() { return r42_product_name; }
		public void setR42_product_name(String r42_product_name) { this.r42_product_name = r42_product_name; }
		public String getR42_cross_reference() { return r42_cross_reference; }
		public void setR42_cross_reference(String r42_cross_reference) { this.r42_cross_reference = r42_cross_reference; }
		public BigDecimal getR42_month() { return r42_month; }
		public void setR42_month(BigDecimal r42_month) { this.r42_month = r42_month; }
		public BigDecimal getR42_ytd() { return r42_ytd; }
		public void setR42_ytd(BigDecimal r42_ytd) { this.r42_ytd = r42_ytd; }
		public String getR43_product_name() { return r43_product_name; }
		public void setR43_product_name(String r43_product_name) { this.r43_product_name = r43_product_name; }
		public String getR43_cross_reference() { return r43_cross_reference; }
		public void setR43_cross_reference(String r43_cross_reference) { this.r43_cross_reference = r43_cross_reference; }
		public BigDecimal getR43_month() { return r43_month; }
		public void setR43_month(BigDecimal r43_month) { this.r43_month = r43_month; }
		public BigDecimal getR43_ytd() { return r43_ytd; }
		public void setR43_ytd(BigDecimal r43_ytd) { this.r43_ytd = r43_ytd; }
		public String getR44_product_name() { return r44_product_name; }
		public void setR44_product_name(String r44_product_name) { this.r44_product_name = r44_product_name; }
		public String getR44_cross_reference() { return r44_cross_reference; }
		public void setR44_cross_reference(String r44_cross_reference) { this.r44_cross_reference = r44_cross_reference; }
		public BigDecimal getR44_month() { return r44_month; }
		public void setR44_month(BigDecimal r44_month) { this.r44_month = r44_month; }
		public BigDecimal getR44_ytd() { return r44_ytd; }
		public void setR44_ytd(BigDecimal r44_ytd) { this.r44_ytd = r44_ytd; }
		public String getR45_product_name() { return r45_product_name; }
		public void setR45_product_name(String r45_product_name) { this.r45_product_name = r45_product_name; }
		public String getR45_cross_reference() { return r45_cross_reference; }
		public void setR45_cross_reference(String r45_cross_reference) { this.r45_cross_reference = r45_cross_reference; }
		public BigDecimal getR45_month() { return r45_month; }
		public void setR45_month(BigDecimal r45_month) { this.r45_month = r45_month; }
		public BigDecimal getR45_ytd() { return r45_ytd; }
		public void setR45_ytd(BigDecimal r45_ytd) { this.r45_ytd = r45_ytd; }
		public String getR46_product_name() { return r46_product_name; }
		public void setR46_product_name(String r46_product_name) { this.r46_product_name = r46_product_name; }
		public String getR46_cross_reference() { return r46_cross_reference; }
		public void setR46_cross_reference(String r46_cross_reference) { this.r46_cross_reference = r46_cross_reference; }
		public BigDecimal getR46_month() { return r46_month; }
		public void setR46_month(BigDecimal r46_month) { this.r46_month = r46_month; }
		public BigDecimal getR46_ytd() { return r46_ytd; }
		public void setR46_ytd(BigDecimal r46_ytd) { this.r46_ytd = r46_ytd; }
		public String getR47_product_name() { return r47_product_name; }
		public void setR47_product_name(String r47_product_name) { this.r47_product_name = r47_product_name; }
		public String getR47_cross_reference() { return r47_cross_reference; }
		public void setR47_cross_reference(String r47_cross_reference) { this.r47_cross_reference = r47_cross_reference; }
		public BigDecimal getR47_month() { return r47_month; }
		public void setR47_month(BigDecimal r47_month) { this.r47_month = r47_month; }
		public BigDecimal getR47_ytd() { return r47_ytd; }
		public void setR47_ytd(BigDecimal r47_ytd) { this.r47_ytd = r47_ytd; }
		public String getR48_product_name() { return r48_product_name; }
		public void setR48_product_name(String r48_product_name) { this.r48_product_name = r48_product_name; }
		public String getR48_cross_reference() { return r48_cross_reference; }
		public void setR48_cross_reference(String r48_cross_reference) { this.r48_cross_reference = r48_cross_reference; }
		public BigDecimal getR48_month() { return r48_month; }
		public void setR48_month(BigDecimal r48_month) { this.r48_month = r48_month; }
		public BigDecimal getR48_ytd() { return r48_ytd; }
		public void setR48_ytd(BigDecimal r48_ytd) { this.r48_ytd = r48_ytd; }
		public String getR49_product_name() { return r49_product_name; }
		public void setR49_product_name(String r49_product_name) { this.r49_product_name = r49_product_name; }
		public String getR49_cross_reference() { return r49_cross_reference; }
		public void setR49_cross_reference(String r49_cross_reference) { this.r49_cross_reference = r49_cross_reference; }
		public BigDecimal getR49_month() { return r49_month; }
		public void setR49_month(BigDecimal r49_month) { this.r49_month = r49_month; }
		public BigDecimal getR49_ytd() { return r49_ytd; }
		public void setR49_ytd(BigDecimal r49_ytd) { this.r49_ytd = r49_ytd; }
		public String getR50_product_name() { return r50_product_name; }
		public void setR50_product_name(String r50_product_name) { this.r50_product_name = r50_product_name; }
		public String getR50_cross_reference() { return r50_cross_reference; }
		public void setR50_cross_reference(String r50_cross_reference) { this.r50_cross_reference = r50_cross_reference; }
		public BigDecimal getR50_month() { return r50_month; }
		public void setR50_month(BigDecimal r50_month) { this.r50_month = r50_month; }
		public BigDecimal getR50_ytd() { return r50_ytd; }
		public void setR50_ytd(BigDecimal r50_ytd) { this.r50_ytd = r50_ytd; }
		public String getR51_product_name() { return r51_product_name; }
		public void setR51_product_name(String r51_product_name) { this.r51_product_name = r51_product_name; }
		public String getR51_cross_reference() { return r51_cross_reference; }
		public void setR51_cross_reference(String r51_cross_reference) { this.r51_cross_reference = r51_cross_reference; }
		public BigDecimal getR51_month() { return r51_month; }
		public void setR51_month(BigDecimal r51_month) { this.r51_month = r51_month; }
		public BigDecimal getR51_ytd() { return r51_ytd; }
		public void setR51_ytd(BigDecimal r51_ytd) { this.r51_ytd = r51_ytd; }
		public String getR52_product_name() { return r52_product_name; }
		public void setR52_product_name(String r52_product_name) { this.r52_product_name = r52_product_name; }
		public String getR52_cross_reference() { return r52_cross_reference; }
		public void setR52_cross_reference(String r52_cross_reference) { this.r52_cross_reference = r52_cross_reference; }
		public BigDecimal getR52_month() { return r52_month; }
		public void setR52_month(BigDecimal r52_month) { this.r52_month = r52_month; }
		public BigDecimal getR52_ytd() { return r52_ytd; }
		public void setR52_ytd(BigDecimal r52_ytd) { this.r52_ytd = r52_ytd; }
		public String getR53_product_name() { return r53_product_name; }
		public void setR53_product_name(String r53_product_name) { this.r53_product_name = r53_product_name; }
		public String getR53_cross_reference() { return r53_cross_reference; }
		public void setR53_cross_reference(String r53_cross_reference) { this.r53_cross_reference = r53_cross_reference; }
		public BigDecimal getR53_month() { return r53_month; }
		public void setR53_month(BigDecimal r53_month) { this.r53_month = r53_month; }
		public BigDecimal getR53_ytd() { return r53_ytd; }
		public void setR53_ytd(BigDecimal r53_ytd) { this.r53_ytd = r53_ytd; }
		public String getR54_product_name() { return r54_product_name; }
		public void setR54_product_name(String r54_product_name) { this.r54_product_name = r54_product_name; }
		public String getR54_cross_reference() { return r54_cross_reference; }
		public void setR54_cross_reference(String r54_cross_reference) { this.r54_cross_reference = r54_cross_reference; }
		public BigDecimal getR54_month() { return r54_month; }
		public void setR54_month(BigDecimal r54_month) { this.r54_month = r54_month; }
		public BigDecimal getR54_ytd() { return r54_ytd; }
		public void setR54_ytd(BigDecimal r54_ytd) { this.r54_ytd = r54_ytd; }
		public String getR55_product_name() { return r55_product_name; }
		public void setR55_product_name(String r55_product_name) { this.r55_product_name = r55_product_name; }
		public String getR55_cross_reference() { return r55_cross_reference; }
		public void setR55_cross_reference(String r55_cross_reference) { this.r55_cross_reference = r55_cross_reference; }
		public BigDecimal getR55_month() { return r55_month; }
		public void setR55_month(BigDecimal r55_month) { this.r55_month = r55_month; }
		public BigDecimal getR55_ytd() { return r55_ytd; }
		public void setR55_ytd(BigDecimal r55_ytd) { this.r55_ytd = r55_ytd; }
		public String getR56_product_name() { return r56_product_name; }
		public void setR56_product_name(String r56_product_name) { this.r56_product_name = r56_product_name; }
		public String getR56_cross_reference() { return r56_cross_reference; }
		public void setR56_cross_reference(String r56_cross_reference) { this.r56_cross_reference = r56_cross_reference; }
		public BigDecimal getR56_month() { return r56_month; }
		public void setR56_month(BigDecimal r56_month) { this.r56_month = r56_month; }
		public BigDecimal getR56_ytd() { return r56_ytd; }
		public void setR56_ytd(BigDecimal r56_ytd) { this.r56_ytd = r56_ytd; }
		public String getR57_product_name() { return r57_product_name; }
		public void setR57_product_name(String r57_product_name) { this.r57_product_name = r57_product_name; }
		public String getR57_cross_reference() { return r57_cross_reference; }
		public void setR57_cross_reference(String r57_cross_reference) { this.r57_cross_reference = r57_cross_reference; }
		public BigDecimal getR57_month() { return r57_month; }
		public void setR57_month(BigDecimal r57_month) { this.r57_month = r57_month; }
		public BigDecimal getR57_ytd() { return r57_ytd; }
		public void setR57_ytd(BigDecimal r57_ytd) { this.r57_ytd = r57_ytd; }
		public String getR58_product_name() { return r58_product_name; }
		public void setR58_product_name(String r58_product_name) { this.r58_product_name = r58_product_name; }
		public String getR58_cross_reference() { return r58_cross_reference; }
		public void setR58_cross_reference(String r58_cross_reference) { this.r58_cross_reference = r58_cross_reference; }
		public BigDecimal getR58_month() { return r58_month; }
		public void setR58_month(BigDecimal r58_month) { this.r58_month = r58_month; }
		public BigDecimal getR58_ytd() { return r58_ytd; }
		public void setR58_ytd(BigDecimal r58_ytd) { this.r58_ytd = r58_ytd; }
		public String getR59_product_name() { return r59_product_name; }
		public void setR59_product_name(String r59_product_name) { this.r59_product_name = r59_product_name; }
		public String getR59_cross_reference() { return r59_cross_reference; }
		public void setR59_cross_reference(String r59_cross_reference) { this.r59_cross_reference = r59_cross_reference; }
		public BigDecimal getR59_month() { return r59_month; }
		public void setR59_month(BigDecimal r59_month) { this.r59_month = r59_month; }
		public BigDecimal getR59_ytd() { return r59_ytd; }
		public void setR59_ytd(BigDecimal r59_ytd) { this.r59_ytd = r59_ytd; }
		public String getR60_product_name() { return r60_product_name; }
		public void setR60_product_name(String r60_product_name) { this.r60_product_name = r60_product_name; }
		public String getR60_cross_reference() { return r60_cross_reference; }
		public void setR60_cross_reference(String r60_cross_reference) { this.r60_cross_reference = r60_cross_reference; }
		public BigDecimal getR60_month() { return r60_month; }
		public void setR60_month(BigDecimal r60_month) { this.r60_month = r60_month; }
		public BigDecimal getR60_ytd() { return r60_ytd; }
		public void setR60_ytd(BigDecimal r60_ytd) { this.r60_ytd = r60_ytd; }
		public String getR61_product_name() { return r61_product_name; }
		public void setR61_product_name(String r61_product_name) { this.r61_product_name = r61_product_name; }
		public String getR61_cross_reference() { return r61_cross_reference; }
		public void setR61_cross_reference(String r61_cross_reference) { this.r61_cross_reference = r61_cross_reference; }
		public BigDecimal getR61_month() { return r61_month; }
		public void setR61_month(BigDecimal r61_month) { this.r61_month = r61_month; }
		public BigDecimal getR61_ytd() { return r61_ytd; }
		public void setR61_ytd(BigDecimal r61_ytd) { this.r61_ytd = r61_ytd; }
		public String getR62_product_name() { return r62_product_name; }
		public void setR62_product_name(String r62_product_name) { this.r62_product_name = r62_product_name; }
		public String getR62_cross_reference() { return r62_cross_reference; }
		public void setR62_cross_reference(String r62_cross_reference) { this.r62_cross_reference = r62_cross_reference; }
		public BigDecimal getR62_month() { return r62_month; }
		public void setR62_month(BigDecimal r62_month) { this.r62_month = r62_month; }
		public BigDecimal getR62_ytd() { return r62_ytd; }
		public void setR62_ytd(BigDecimal r62_ytd) { this.r62_ytd = r62_ytd; }
		public String getR63_product_name() { return r63_product_name; }
		public void setR63_product_name(String r63_product_name) { this.r63_product_name = r63_product_name; }
		public String getR63_cross_reference() { return r63_cross_reference; }
		public void setR63_cross_reference(String r63_cross_reference) { this.r63_cross_reference = r63_cross_reference; }
		public BigDecimal getR63_month() { return r63_month; }
		public void setR63_month(BigDecimal r63_month) { this.r63_month = r63_month; }
		public BigDecimal getR63_ytd() { return r63_ytd; }
		public void setR63_ytd(BigDecimal r63_ytd) { this.r63_ytd = r63_ytd; }
		public String getR64_product_name() { return r64_product_name; }
		public void setR64_product_name(String r64_product_name) { this.r64_product_name = r64_product_name; }
		public String getR64_cross_reference() { return r64_cross_reference; }
		public void setR64_cross_reference(String r64_cross_reference) { this.r64_cross_reference = r64_cross_reference; }
		public BigDecimal getR64_month() { return r64_month; }
		public void setR64_month(BigDecimal r64_month) { this.r64_month = r64_month; }
		public BigDecimal getR64_ytd() { return r64_ytd; }
		public void setR64_ytd(BigDecimal r64_ytd) { this.r64_ytd = r64_ytd; }
		public String getR65_product_name() { return r65_product_name; }
		public void setR65_product_name(String r65_product_name) { this.r65_product_name = r65_product_name; }
		public String getR65_cross_reference() { return r65_cross_reference; }
		public void setR65_cross_reference(String r65_cross_reference) { this.r65_cross_reference = r65_cross_reference; }
		public BigDecimal getR65_month() { return r65_month; }
		public void setR65_month(BigDecimal r65_month) { this.r65_month = r65_month; }
		public BigDecimal getR65_ytd() { return r65_ytd; }
		public void setR65_ytd(BigDecimal r65_ytd) { this.r65_ytd = r65_ytd; }
		public String getR66_product_name() { return r66_product_name; }
		public void setR66_product_name(String r66_product_name) { this.r66_product_name = r66_product_name; }
		public String getR66_cross_reference() { return r66_cross_reference; }
		public void setR66_cross_reference(String r66_cross_reference) { this.r66_cross_reference = r66_cross_reference; }
		public BigDecimal getR66_month() { return r66_month; }
		public void setR66_month(BigDecimal r66_month) { this.r66_month = r66_month; }
		public BigDecimal getR66_ytd() { return r66_ytd; }
		public void setR66_ytd(BigDecimal r66_ytd) { this.r66_ytd = r66_ytd; }
		public String getR67_product_name() { return r67_product_name; }
		public void setR67_product_name(String r67_product_name) { this.r67_product_name = r67_product_name; }
		public String getR67_cross_reference() { return r67_cross_reference; }
		public void setR67_cross_reference(String r67_cross_reference) { this.r67_cross_reference = r67_cross_reference; }
		public BigDecimal getR67_month() { return r67_month; }
		public void setR67_month(BigDecimal r67_month) { this.r67_month = r67_month; }
		public BigDecimal getR67_ytd() { return r67_ytd; }
		public void setR67_ytd(BigDecimal r67_ytd) { this.r67_ytd = r67_ytd; }
		public String getR68_product_name() { return r68_product_name; }
		public void setR68_product_name(String r68_product_name) { this.r68_product_name = r68_product_name; }
		public String getR68_cross_reference() { return r68_cross_reference; }
		public void setR68_cross_reference(String r68_cross_reference) { this.r68_cross_reference = r68_cross_reference; }
		public BigDecimal getR68_month() { return r68_month; }
		public void setR68_month(BigDecimal r68_month) { this.r68_month = r68_month; }
		public BigDecimal getR68_ytd() { return r68_ytd; }
		public void setR68_ytd(BigDecimal r68_ytd) { this.r68_ytd = r68_ytd; }
		public String getR69_product_name() { return r69_product_name; }
		public void setR69_product_name(String r69_product_name) { this.r69_product_name = r69_product_name; }
		public String getR69_cross_reference() { return r69_cross_reference; }
		public void setR69_cross_reference(String r69_cross_reference) { this.r69_cross_reference = r69_cross_reference; }
		public BigDecimal getR69_month() { return r69_month; }
		public void setR69_month(BigDecimal r69_month) { this.r69_month = r69_month; }
		public BigDecimal getR69_ytd() { return r69_ytd; }
		public void setR69_ytd(BigDecimal r69_ytd) { this.r69_ytd = r69_ytd; }
		public String getR70_product_name() { return r70_product_name; }
		public void setR70_product_name(String r70_product_name) { this.r70_product_name = r70_product_name; }
		public String getR70_cross_reference() { return r70_cross_reference; }
		public void setR70_cross_reference(String r70_cross_reference) { this.r70_cross_reference = r70_cross_reference; }
		public BigDecimal getR70_month() { return r70_month; }
		public void setR70_month(BigDecimal r70_month) { this.r70_month = r70_month; }
		public BigDecimal getR70_ytd() { return r70_ytd; }
		public void setR70_ytd(BigDecimal r70_ytd) { this.r70_ytd = r70_ytd; }
		public String getR71_product_name() { return r71_product_name; }
		public void setR71_product_name(String r71_product_name) { this.r71_product_name = r71_product_name; }
		public String getR71_cross_reference() { return r71_cross_reference; }
		public void setR71_cross_reference(String r71_cross_reference) { this.r71_cross_reference = r71_cross_reference; }
		public BigDecimal getR71_month() { return r71_month; }
		public void setR71_month(BigDecimal r71_month) { this.r71_month = r71_month; }
		public BigDecimal getR71_ytd() { return r71_ytd; }
		public void setR71_ytd(BigDecimal r71_ytd) { this.r71_ytd = r71_ytd; }
		public String getR72_product_name() { return r72_product_name; }
		public void setR72_product_name(String r72_product_name) { this.r72_product_name = r72_product_name; }
		public String getR72_cross_reference() { return r72_cross_reference; }
		public void setR72_cross_reference(String r72_cross_reference) { this.r72_cross_reference = r72_cross_reference; }
		public BigDecimal getR72_month() { return r72_month; }
		public void setR72_month(BigDecimal r72_month) { this.r72_month = r72_month; }
		public BigDecimal getR72_ytd() { return r72_ytd; }
		public void setR72_ytd(BigDecimal r72_ytd) { this.r72_ytd = r72_ytd; }
		public String getR73_product_name() { return r73_product_name; }
		public void setR73_product_name(String r73_product_name) { this.r73_product_name = r73_product_name; }
		public String getR73_cross_reference() { return r73_cross_reference; }
		public void setR73_cross_reference(String r73_cross_reference) { this.r73_cross_reference = r73_cross_reference; }
		public BigDecimal getR73_month() { return r73_month; }
		public void setR73_month(BigDecimal r73_month) { this.r73_month = r73_month; }
		public BigDecimal getR73_ytd() { return r73_ytd; }
		public void setR73_ytd(BigDecimal r73_ytd) { this.r73_ytd = r73_ytd; }
		public String getR74_product_name() { return r74_product_name; }
		public void setR74_product_name(String r74_product_name) { this.r74_product_name = r74_product_name; }
		public String getR74_cross_reference() { return r74_cross_reference; }
		public void setR74_cross_reference(String r74_cross_reference) { this.r74_cross_reference = r74_cross_reference; }
		public BigDecimal getR74_month() { return r74_month; }
		public void setR74_month(BigDecimal r74_month) { this.r74_month = r74_month; }
		public BigDecimal getR74_ytd() { return r74_ytd; }
		public void setR74_ytd(BigDecimal r74_ytd) { this.r74_ytd = r74_ytd; }
		public String getR75_product_name() { return r75_product_name; }
		public void setR75_product_name(String r75_product_name) { this.r75_product_name = r75_product_name; }
		public String getR75_cross_reference() { return r75_cross_reference; }
		public void setR75_cross_reference(String r75_cross_reference) { this.r75_cross_reference = r75_cross_reference; }
		public BigDecimal getR75_month() { return r75_month; }
		public void setR75_month(BigDecimal r75_month) { this.r75_month = r75_month; }
		public BigDecimal getR75_ytd() { return r75_ytd; }
		public void setR75_ytd(BigDecimal r75_ytd) { this.r75_ytd = r75_ytd; }
		public String getR76_product_name() { return r76_product_name; }
		public void setR76_product_name(String r76_product_name) { this.r76_product_name = r76_product_name; }
		public String getR76_cross_reference() { return r76_cross_reference; }
		public void setR76_cross_reference(String r76_cross_reference) { this.r76_cross_reference = r76_cross_reference; }
		public BigDecimal getR76_month() { return r76_month; }
		public void setR76_month(BigDecimal r76_month) { this.r76_month = r76_month; }
		public BigDecimal getR76_ytd() { return r76_ytd; }
		public void setR76_ytd(BigDecimal r76_ytd) { this.r76_ytd = r76_ytd; }
		public String getR77_product_name() { return r77_product_name; }
		public void setR77_product_name(String r77_product_name) { this.r77_product_name = r77_product_name; }
		public String getR77_cross_reference() { return r77_cross_reference; }
		public void setR77_cross_reference(String r77_cross_reference) { this.r77_cross_reference = r77_cross_reference; }
		public BigDecimal getR77_month() { return r77_month; }
		public void setR77_month(BigDecimal r77_month) { this.r77_month = r77_month; }
		public BigDecimal getR77_ytd() { return r77_ytd; }
		public void setR77_ytd(BigDecimal r77_ytd) { this.r77_ytd = r77_ytd; }
		public String getR78_product_name() { return r78_product_name; }
		public void setR78_product_name(String r78_product_name) { this.r78_product_name = r78_product_name; }
		public String getR78_cross_reference() { return r78_cross_reference; }
		public void setR78_cross_reference(String r78_cross_reference) { this.r78_cross_reference = r78_cross_reference; }
		public BigDecimal getR78_month() { return r78_month; }
		public void setR78_month(BigDecimal r78_month) { this.r78_month = r78_month; }
		public BigDecimal getR78_ytd() { return r78_ytd; }
		public void setR78_ytd(BigDecimal r78_ytd) { this.r78_ytd = r78_ytd; }
		public String getR79_product_name() { return r79_product_name; }
		public void setR79_product_name(String r79_product_name) { this.r79_product_name = r79_product_name; }
		public String getR79_cross_reference() { return r79_cross_reference; }
		public void setR79_cross_reference(String r79_cross_reference) { this.r79_cross_reference = r79_cross_reference; }
		public BigDecimal getR79_month() { return r79_month; }
		public void setR79_month(BigDecimal r79_month) { this.r79_month = r79_month; }
		public BigDecimal getR79_ytd() { return r79_ytd; }
		public void setR79_ytd(BigDecimal r79_ytd) { this.r79_ytd = r79_ytd; }
		public String getR80_product_name() { return r80_product_name; }
		public void setR80_product_name(String r80_product_name) { this.r80_product_name = r80_product_name; }
		public String getR80_cross_reference() { return r80_cross_reference; }
		public void setR80_cross_reference(String r80_cross_reference) { this.r80_cross_reference = r80_cross_reference; }
		public BigDecimal getR80_month() { return r80_month; }
		public void setR80_month(BigDecimal r80_month) { this.r80_month = r80_month; }
		public BigDecimal getR80_ytd() { return r80_ytd; }
		public void setR80_ytd(BigDecimal r80_ytd) { this.r80_ytd = r80_ytd; }
		public String getR81_product_name() { return r81_product_name; }
		public void setR81_product_name(String r81_product_name) { this.r81_product_name = r81_product_name; }
		public String getR81_cross_reference() { return r81_cross_reference; }
		public void setR81_cross_reference(String r81_cross_reference) { this.r81_cross_reference = r81_cross_reference; }
		public BigDecimal getR81_month() { return r81_month; }
		public void setR81_month(BigDecimal r81_month) { this.r81_month = r81_month; }
		public BigDecimal getR81_ytd() { return r81_ytd; }
		public void setR81_ytd(BigDecimal r81_ytd) { this.r81_ytd = r81_ytd; }
		public String getR82_product_name() { return r82_product_name; }
		public void setR82_product_name(String r82_product_name) { this.r82_product_name = r82_product_name; }
		public String getR82_cross_reference() { return r82_cross_reference; }
		public void setR82_cross_reference(String r82_cross_reference) { this.r82_cross_reference = r82_cross_reference; }
		public BigDecimal getR82_month() { return r82_month; }
		public void setR82_month(BigDecimal r82_month) { this.r82_month = r82_month; }
		public BigDecimal getR82_ytd() { return r82_ytd; }
		public void setR82_ytd(BigDecimal r82_ytd) { this.r82_ytd = r82_ytd; }
		public String getR83_product_name() { return r83_product_name; }
		public void setR83_product_name(String r83_product_name) { this.r83_product_name = r83_product_name; }
		public String getR83_cross_reference() { return r83_cross_reference; }
		public void setR83_cross_reference(String r83_cross_reference) { this.r83_cross_reference = r83_cross_reference; }
		public BigDecimal getR83_month() { return r83_month; }
		public void setR83_month(BigDecimal r83_month) { this.r83_month = r83_month; }
		public BigDecimal getR83_ytd() { return r83_ytd; }
		public void setR83_ytd(BigDecimal r83_ytd) { this.r83_ytd = r83_ytd; }
		public String getR84_product_name() { return r84_product_name; }
		public void setR84_product_name(String r84_product_name) { this.r84_product_name = r84_product_name; }
		public String getR84_cross_reference() { return r84_cross_reference; }
		public void setR84_cross_reference(String r84_cross_reference) { this.r84_cross_reference = r84_cross_reference; }
		public BigDecimal getR84_month() { return r84_month; }
		public void setR84_month(BigDecimal r84_month) { this.r84_month = r84_month; }
		public BigDecimal getR84_ytd() { return r84_ytd; }
		public void setR84_ytd(BigDecimal r84_ytd) { this.r84_ytd = r84_ytd; }
		public String getR85_product_name() { return r85_product_name; }
		public void setR85_product_name(String r85_product_name) { this.r85_product_name = r85_product_name; }
		public String getR85_cross_reference() { return r85_cross_reference; }
		public void setR85_cross_reference(String r85_cross_reference) { this.r85_cross_reference = r85_cross_reference; }
		public BigDecimal getR85_month() { return r85_month; }
		public void setR85_month(BigDecimal r85_month) { this.r85_month = r85_month; }
		public BigDecimal getR85_ytd() { return r85_ytd; }
		public void setR85_ytd(BigDecimal r85_ytd) { this.r85_ytd = r85_ytd; }
		public String getR86_product_name() { return r86_product_name; }
		public void setR86_product_name(String r86_product_name) { this.r86_product_name = r86_product_name; }
		public String getR86_cross_reference() { return r86_cross_reference; }
		public void setR86_cross_reference(String r86_cross_reference) { this.r86_cross_reference = r86_cross_reference; }
		public BigDecimal getR86_month() { return r86_month; }
		public void setR86_month(BigDecimal r86_month) { this.r86_month = r86_month; }
		public BigDecimal getR86_ytd() { return r86_ytd; }
		public void setR86_ytd(BigDecimal r86_ytd) { this.r86_ytd = r86_ytd; }
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

	// =========================================================
	// Inner entity: M_SCI_E_Archival_Summary_Entity
	// =========================================================
	public static class M_SCI_E_Archival_Summary_Entity {
		private String r10_product_name;
		private String r10_cross_reference;
		private BigDecimal r10_month;
		private BigDecimal r10_ytd;
		private String r11_product_name;
		private String r11_cross_reference;
		private BigDecimal r11_month;
		private BigDecimal r11_ytd;
		private String r12_product_name;
		private String r12_cross_reference;
		private BigDecimal r12_month;
		private BigDecimal r12_ytd;
		private String r13_product_name;
		private String r13_cross_reference;
		private BigDecimal r13_month;
		private BigDecimal r13_ytd;
		private String r14_product_name;
		private String r14_cross_reference;
		private BigDecimal r14_month;
		private BigDecimal r14_ytd;
		private String r15_product_name;
		private String r15_cross_reference;
		private BigDecimal r15_month;
		private BigDecimal r15_ytd;
		private String r16_product_name;
		private String r16_cross_reference;
		private BigDecimal r16_month;
		private BigDecimal r16_ytd;
		private String r17_product_name;
		private String r17_cross_reference;
		private BigDecimal r17_month;
		private BigDecimal r17_ytd;
		private String r18_product_name;
		private String r18_cross_reference;
		private BigDecimal r18_month;
		private BigDecimal r18_ytd;
		private String r19_product_name;
		private String r19_cross_reference;
		private BigDecimal r19_month;
		private BigDecimal r19_ytd;
		private String r20_product_name;
		private String r20_cross_reference;
		private BigDecimal r20_month;
		private BigDecimal r20_ytd;
		private String r21_product_name;
		private String r21_cross_reference;
		private BigDecimal r21_month;
		private BigDecimal r21_ytd;
		private String r22_product_name;
		private String r22_cross_reference;
		private BigDecimal r22_month;
		private BigDecimal r22_ytd;
		private String r23_product_name;
		private String r23_cross_reference;
		private BigDecimal r23_month;
		private BigDecimal r23_ytd;
		private String r24_product_name;
		private String r24_cross_reference;
		private BigDecimal r24_month;
		private BigDecimal r24_ytd;
		private String r25_product_name;
		private String r25_cross_reference;
		private BigDecimal r25_month;
		private BigDecimal r25_ytd;
		private String r26_product_name;
		private String r26_cross_reference;
		private BigDecimal r26_month;
		private BigDecimal r26_ytd;
		private String r27_product_name;
		private String r27_cross_reference;
		private BigDecimal r27_month;
		private BigDecimal r27_ytd;
		private String r28_product_name;
		private String r28_cross_reference;
		private BigDecimal r28_month;
		private BigDecimal r28_ytd;
		private String r29_product_name;
		private String r29_cross_reference;
		private BigDecimal r29_month;
		private BigDecimal r29_ytd;
		private String r30_product_name;
		private String r30_cross_reference;
		private BigDecimal r30_month;
		private BigDecimal r30_ytd;
		private String r31_product_name;
		private String r31_cross_reference;
		private BigDecimal r31_month;
		private BigDecimal r31_ytd;
		private String r32_product_name;
		private String r32_cross_reference;
		private BigDecimal r32_month;
		private BigDecimal r32_ytd;
		private String r33_product_name;
		private String r33_cross_reference;
		private BigDecimal r33_month;
		private BigDecimal r33_ytd;
		private String r34_product_name;
		private String r34_cross_reference;
		private BigDecimal r34_month;
		private BigDecimal r34_ytd;
		private String r35_product_name;
		private String r35_cross_reference;
		private BigDecimal r35_month;
		private BigDecimal r35_ytd;
		private String r36_product_name;
		private String r36_cross_reference;
		private BigDecimal r36_month;
		private BigDecimal r36_ytd;
		private String r37_product_name;
		private String r37_cross_reference;
		private BigDecimal r37_month;
		private BigDecimal r37_ytd;
		private String r38_product_name;
		private String r38_cross_reference;
		private BigDecimal r38_month;
		private BigDecimal r38_ytd;
		private String r39_product_name;
		private String r39_cross_reference;
		private BigDecimal r39_month;
		private BigDecimal r39_ytd;
		private String r40_product_name;
		private String r40_cross_reference;
		private BigDecimal r40_month;
		private BigDecimal r40_ytd;
		private String r41_product_name;
		private String r41_cross_reference;
		private BigDecimal r41_month;
		private BigDecimal r41_ytd;
		private String r42_product_name;
		private String r42_cross_reference;
		private BigDecimal r42_month;
		private BigDecimal r42_ytd;
		private String r43_product_name;
		private String r43_cross_reference;
		private BigDecimal r43_month;
		private BigDecimal r43_ytd;
		private String r44_product_name;
		private String r44_cross_reference;
		private BigDecimal r44_month;
		private BigDecimal r44_ytd;
		private String r45_product_name;
		private String r45_cross_reference;
		private BigDecimal r45_month;
		private BigDecimal r45_ytd;
		private String r46_product_name;
		private String r46_cross_reference;
		private BigDecimal r46_month;
		private BigDecimal r46_ytd;
		private String r47_product_name;
		private String r47_cross_reference;
		private BigDecimal r47_month;
		private BigDecimal r47_ytd;
		private String r48_product_name;
		private String r48_cross_reference;
		private BigDecimal r48_month;
		private BigDecimal r48_ytd;
		private String r49_product_name;
		private String r49_cross_reference;
		private BigDecimal r49_month;
		private BigDecimal r49_ytd;
		private String r50_product_name;
		private String r50_cross_reference;
		private BigDecimal r50_month;
		private BigDecimal r50_ytd;
		private String r51_product_name;
		private String r51_cross_reference;
		private BigDecimal r51_month;
		private BigDecimal r51_ytd;
		private String r52_product_name;
		private String r52_cross_reference;
		private BigDecimal r52_month;
		private BigDecimal r52_ytd;
		private String r53_product_name;
		private String r53_cross_reference;
		private BigDecimal r53_month;
		private BigDecimal r53_ytd;
		private String r54_product_name;
		private String r54_cross_reference;
		private BigDecimal r54_month;
		private BigDecimal r54_ytd;
		private String r55_product_name;
		private String r55_cross_reference;
		private BigDecimal r55_month;
		private BigDecimal r55_ytd;
		private String r56_product_name;
		private String r56_cross_reference;
		private BigDecimal r56_month;
		private BigDecimal r56_ytd;
		private String r57_product_name;
		private String r57_cross_reference;
		private BigDecimal r57_month;
		private BigDecimal r57_ytd;
		private String r58_product_name;
		private String r58_cross_reference;
		private BigDecimal r58_month;
		private BigDecimal r58_ytd;
		private String r59_product_name;
		private String r59_cross_reference;
		private BigDecimal r59_month;
		private BigDecimal r59_ytd;
		private String r60_product_name;
		private String r60_cross_reference;
		private BigDecimal r60_month;
		private BigDecimal r60_ytd;
		private String r61_product_name;
		private String r61_cross_reference;
		private BigDecimal r61_month;
		private BigDecimal r61_ytd;
		private String r62_product_name;
		private String r62_cross_reference;
		private BigDecimal r62_month;
		private BigDecimal r62_ytd;
		private String r63_product_name;
		private String r63_cross_reference;
		private BigDecimal r63_month;
		private BigDecimal r63_ytd;
		private String r64_product_name;
		private String r64_cross_reference;
		private BigDecimal r64_month;
		private BigDecimal r64_ytd;
		private String r65_product_name;
		private String r65_cross_reference;
		private BigDecimal r65_month;
		private BigDecimal r65_ytd;
		private String r66_product_name;
		private String r66_cross_reference;
		private BigDecimal r66_month;
		private BigDecimal r66_ytd;
		private String r67_product_name;
		private String r67_cross_reference;
		private BigDecimal r67_month;
		private BigDecimal r67_ytd;
		private String r68_product_name;
		private String r68_cross_reference;
		private BigDecimal r68_month;
		private BigDecimal r68_ytd;
		private String r69_product_name;
		private String r69_cross_reference;
		private BigDecimal r69_month;
		private BigDecimal r69_ytd;
		private String r70_product_name;
		private String r70_cross_reference;
		private BigDecimal r70_month;
		private BigDecimal r70_ytd;
		private String r71_product_name;
		private String r71_cross_reference;
		private BigDecimal r71_month;
		private BigDecimal r71_ytd;
		private String r72_product_name;
		private String r72_cross_reference;
		private BigDecimal r72_month;
		private BigDecimal r72_ytd;
		private String r73_product_name;
		private String r73_cross_reference;
		private BigDecimal r73_month;
		private BigDecimal r73_ytd;
		private String r74_product_name;
		private String r74_cross_reference;
		private BigDecimal r74_month;
		private BigDecimal r74_ytd;
		private String r75_product_name;
		private String r75_cross_reference;
		private BigDecimal r75_month;
		private BigDecimal r75_ytd;
		private String r76_product_name;
		private String r76_cross_reference;
		private BigDecimal r76_month;
		private BigDecimal r76_ytd;
		private String r77_product_name;
		private String r77_cross_reference;
		private BigDecimal r77_month;
		private BigDecimal r77_ytd;
		private String r78_product_name;
		private String r78_cross_reference;
		private BigDecimal r78_month;
		private BigDecimal r78_ytd;
		private String r79_product_name;
		private String r79_cross_reference;
		private BigDecimal r79_month;
		private BigDecimal r79_ytd;
		private String r80_product_name;
		private String r80_cross_reference;
		private BigDecimal r80_month;
		private BigDecimal r80_ytd;
		private String r81_product_name;
		private String r81_cross_reference;
		private BigDecimal r81_month;
		private BigDecimal r81_ytd;
		private String r82_product_name;
		private String r82_cross_reference;
		private BigDecimal r82_month;
		private BigDecimal r82_ytd;
		private String r83_product_name;
		private String r83_cross_reference;
		private BigDecimal r83_month;
		private BigDecimal r83_ytd;
		private String r84_product_name;
		private String r84_cross_reference;
		private BigDecimal r84_month;
		private BigDecimal r84_ytd;
		private String r85_product_name;
		private String r85_cross_reference;
		private BigDecimal r85_month;
		private BigDecimal r85_ytd;
		private String r86_product_name;
		private String r86_cross_reference;
		private BigDecimal r86_month;
		private BigDecimal r86_ytd;

		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;
		private Date reportResubDate;

		public String getR10_product_name() { return r10_product_name; }
		public void setR10_product_name(String r10_product_name) { this.r10_product_name = r10_product_name; }
		public String getR10_cross_reference() { return r10_cross_reference; }
		public void setR10_cross_reference(String r10_cross_reference) { this.r10_cross_reference = r10_cross_reference; }
		public BigDecimal getR10_month() { return r10_month; }
		public void setR10_month(BigDecimal r10_month) { this.r10_month = r10_month; }
		public BigDecimal getR10_ytd() { return r10_ytd; }
		public void setR10_ytd(BigDecimal r10_ytd) { this.r10_ytd = r10_ytd; }
		public String getR11_product_name() { return r11_product_name; }
		public void setR11_product_name(String r11_product_name) { this.r11_product_name = r11_product_name; }
		public String getR11_cross_reference() { return r11_cross_reference; }
		public void setR11_cross_reference(String r11_cross_reference) { this.r11_cross_reference = r11_cross_reference; }
		public BigDecimal getR11_month() { return r11_month; }
		public void setR11_month(BigDecimal r11_month) { this.r11_month = r11_month; }
		public BigDecimal getR11_ytd() { return r11_ytd; }
		public void setR11_ytd(BigDecimal r11_ytd) { this.r11_ytd = r11_ytd; }
		public String getR12_product_name() { return r12_product_name; }
		public void setR12_product_name(String r12_product_name) { this.r12_product_name = r12_product_name; }
		public String getR12_cross_reference() { return r12_cross_reference; }
		public void setR12_cross_reference(String r12_cross_reference) { this.r12_cross_reference = r12_cross_reference; }
		public BigDecimal getR12_month() { return r12_month; }
		public void setR12_month(BigDecimal r12_month) { this.r12_month = r12_month; }
		public BigDecimal getR12_ytd() { return r12_ytd; }
		public void setR12_ytd(BigDecimal r12_ytd) { this.r12_ytd = r12_ytd; }
		public String getR13_product_name() { return r13_product_name; }
		public void setR13_product_name(String r13_product_name) { this.r13_product_name = r13_product_name; }
		public String getR13_cross_reference() { return r13_cross_reference; }
		public void setR13_cross_reference(String r13_cross_reference) { this.r13_cross_reference = r13_cross_reference; }
		public BigDecimal getR13_month() { return r13_month; }
		public void setR13_month(BigDecimal r13_month) { this.r13_month = r13_month; }
		public BigDecimal getR13_ytd() { return r13_ytd; }
		public void setR13_ytd(BigDecimal r13_ytd) { this.r13_ytd = r13_ytd; }
		public String getR14_product_name() { return r14_product_name; }
		public void setR14_product_name(String r14_product_name) { this.r14_product_name = r14_product_name; }
		public String getR14_cross_reference() { return r14_cross_reference; }
		public void setR14_cross_reference(String r14_cross_reference) { this.r14_cross_reference = r14_cross_reference; }
		public BigDecimal getR14_month() { return r14_month; }
		public void setR14_month(BigDecimal r14_month) { this.r14_month = r14_month; }
		public BigDecimal getR14_ytd() { return r14_ytd; }
		public void setR14_ytd(BigDecimal r14_ytd) { this.r14_ytd = r14_ytd; }
		public String getR15_product_name() { return r15_product_name; }
		public void setR15_product_name(String r15_product_name) { this.r15_product_name = r15_product_name; }
		public String getR15_cross_reference() { return r15_cross_reference; }
		public void setR15_cross_reference(String r15_cross_reference) { this.r15_cross_reference = r15_cross_reference; }
		public BigDecimal getR15_month() { return r15_month; }
		public void setR15_month(BigDecimal r15_month) { this.r15_month = r15_month; }
		public BigDecimal getR15_ytd() { return r15_ytd; }
		public void setR15_ytd(BigDecimal r15_ytd) { this.r15_ytd = r15_ytd; }
		public String getR16_product_name() { return r16_product_name; }
		public void setR16_product_name(String r16_product_name) { this.r16_product_name = r16_product_name; }
		public String getR16_cross_reference() { return r16_cross_reference; }
		public void setR16_cross_reference(String r16_cross_reference) { this.r16_cross_reference = r16_cross_reference; }
		public BigDecimal getR16_month() { return r16_month; }
		public void setR16_month(BigDecimal r16_month) { this.r16_month = r16_month; }
		public BigDecimal getR16_ytd() { return r16_ytd; }
		public void setR16_ytd(BigDecimal r16_ytd) { this.r16_ytd = r16_ytd; }
		public String getR17_product_name() { return r17_product_name; }
		public void setR17_product_name(String r17_product_name) { this.r17_product_name = r17_product_name; }
		public String getR17_cross_reference() { return r17_cross_reference; }
		public void setR17_cross_reference(String r17_cross_reference) { this.r17_cross_reference = r17_cross_reference; }
		public BigDecimal getR17_month() { return r17_month; }
		public void setR17_month(BigDecimal r17_month) { this.r17_month = r17_month; }
		public BigDecimal getR17_ytd() { return r17_ytd; }
		public void setR17_ytd(BigDecimal r17_ytd) { this.r17_ytd = r17_ytd; }
		public String getR18_product_name() { return r18_product_name; }
		public void setR18_product_name(String r18_product_name) { this.r18_product_name = r18_product_name; }
		public String getR18_cross_reference() { return r18_cross_reference; }
		public void setR18_cross_reference(String r18_cross_reference) { this.r18_cross_reference = r18_cross_reference; }
		public BigDecimal getR18_month() { return r18_month; }
		public void setR18_month(BigDecimal r18_month) { this.r18_month = r18_month; }
		public BigDecimal getR18_ytd() { return r18_ytd; }
		public void setR18_ytd(BigDecimal r18_ytd) { this.r18_ytd = r18_ytd; }
		public String getR19_product_name() { return r19_product_name; }
		public void setR19_product_name(String r19_product_name) { this.r19_product_name = r19_product_name; }
		public String getR19_cross_reference() { return r19_cross_reference; }
		public void setR19_cross_reference(String r19_cross_reference) { this.r19_cross_reference = r19_cross_reference; }
		public BigDecimal getR19_month() { return r19_month; }
		public void setR19_month(BigDecimal r19_month) { this.r19_month = r19_month; }
		public BigDecimal getR19_ytd() { return r19_ytd; }
		public void setR19_ytd(BigDecimal r19_ytd) { this.r19_ytd = r19_ytd; }
		public String getR20_product_name() { return r20_product_name; }
		public void setR20_product_name(String r20_product_name) { this.r20_product_name = r20_product_name; }
		public String getR20_cross_reference() { return r20_cross_reference; }
		public void setR20_cross_reference(String r20_cross_reference) { this.r20_cross_reference = r20_cross_reference; }
		public BigDecimal getR20_month() { return r20_month; }
		public void setR20_month(BigDecimal r20_month) { this.r20_month = r20_month; }
		public BigDecimal getR20_ytd() { return r20_ytd; }
		public void setR20_ytd(BigDecimal r20_ytd) { this.r20_ytd = r20_ytd; }
		public String getR21_product_name() { return r21_product_name; }
		public void setR21_product_name(String r21_product_name) { this.r21_product_name = r21_product_name; }
		public String getR21_cross_reference() { return r21_cross_reference; }
		public void setR21_cross_reference(String r21_cross_reference) { this.r21_cross_reference = r21_cross_reference; }
		public BigDecimal getR21_month() { return r21_month; }
		public void setR21_month(BigDecimal r21_month) { this.r21_month = r21_month; }
		public BigDecimal getR21_ytd() { return r21_ytd; }
		public void setR21_ytd(BigDecimal r21_ytd) { this.r21_ytd = r21_ytd; }
		public String getR22_product_name() { return r22_product_name; }
		public void setR22_product_name(String r22_product_name) { this.r22_product_name = r22_product_name; }
		public String getR22_cross_reference() { return r22_cross_reference; }
		public void setR22_cross_reference(String r22_cross_reference) { this.r22_cross_reference = r22_cross_reference; }
		public BigDecimal getR22_month() { return r22_month; }
		public void setR22_month(BigDecimal r22_month) { this.r22_month = r22_month; }
		public BigDecimal getR22_ytd() { return r22_ytd; }
		public void setR22_ytd(BigDecimal r22_ytd) { this.r22_ytd = r22_ytd; }
		public String getR23_product_name() { return r23_product_name; }
		public void setR23_product_name(String r23_product_name) { this.r23_product_name = r23_product_name; }
		public String getR23_cross_reference() { return r23_cross_reference; }
		public void setR23_cross_reference(String r23_cross_reference) { this.r23_cross_reference = r23_cross_reference; }
		public BigDecimal getR23_month() { return r23_month; }
		public void setR23_month(BigDecimal r23_month) { this.r23_month = r23_month; }
		public BigDecimal getR23_ytd() { return r23_ytd; }
		public void setR23_ytd(BigDecimal r23_ytd) { this.r23_ytd = r23_ytd; }
		public String getR24_product_name() { return r24_product_name; }
		public void setR24_product_name(String r24_product_name) { this.r24_product_name = r24_product_name; }
		public String getR24_cross_reference() { return r24_cross_reference; }
		public void setR24_cross_reference(String r24_cross_reference) { this.r24_cross_reference = r24_cross_reference; }
		public BigDecimal getR24_month() { return r24_month; }
		public void setR24_month(BigDecimal r24_month) { this.r24_month = r24_month; }
		public BigDecimal getR24_ytd() { return r24_ytd; }
		public void setR24_ytd(BigDecimal r24_ytd) { this.r24_ytd = r24_ytd; }
		public String getR25_product_name() { return r25_product_name; }
		public void setR25_product_name(String r25_product_name) { this.r25_product_name = r25_product_name; }
		public String getR25_cross_reference() { return r25_cross_reference; }
		public void setR25_cross_reference(String r25_cross_reference) { this.r25_cross_reference = r25_cross_reference; }
		public BigDecimal getR25_month() { return r25_month; }
		public void setR25_month(BigDecimal r25_month) { this.r25_month = r25_month; }
		public BigDecimal getR25_ytd() { return r25_ytd; }
		public void setR25_ytd(BigDecimal r25_ytd) { this.r25_ytd = r25_ytd; }
		public String getR26_product_name() { return r26_product_name; }
		public void setR26_product_name(String r26_product_name) { this.r26_product_name = r26_product_name; }
		public String getR26_cross_reference() { return r26_cross_reference; }
		public void setR26_cross_reference(String r26_cross_reference) { this.r26_cross_reference = r26_cross_reference; }
		public BigDecimal getR26_month() { return r26_month; }
		public void setR26_month(BigDecimal r26_month) { this.r26_month = r26_month; }
		public BigDecimal getR26_ytd() { return r26_ytd; }
		public void setR26_ytd(BigDecimal r26_ytd) { this.r26_ytd = r26_ytd; }
		public String getR27_product_name() { return r27_product_name; }
		public void setR27_product_name(String r27_product_name) { this.r27_product_name = r27_product_name; }
		public String getR27_cross_reference() { return r27_cross_reference; }
		public void setR27_cross_reference(String r27_cross_reference) { this.r27_cross_reference = r27_cross_reference; }
		public BigDecimal getR27_month() { return r27_month; }
		public void setR27_month(BigDecimal r27_month) { this.r27_month = r27_month; }
		public BigDecimal getR27_ytd() { return r27_ytd; }
		public void setR27_ytd(BigDecimal r27_ytd) { this.r27_ytd = r27_ytd; }
		public String getR28_product_name() { return r28_product_name; }
		public void setR28_product_name(String r28_product_name) { this.r28_product_name = r28_product_name; }
		public String getR28_cross_reference() { return r28_cross_reference; }
		public void setR28_cross_reference(String r28_cross_reference) { this.r28_cross_reference = r28_cross_reference; }
		public BigDecimal getR28_month() { return r28_month; }
		public void setR28_month(BigDecimal r28_month) { this.r28_month = r28_month; }
		public BigDecimal getR28_ytd() { return r28_ytd; }
		public void setR28_ytd(BigDecimal r28_ytd) { this.r28_ytd = r28_ytd; }
		public String getR29_product_name() { return r29_product_name; }
		public void setR29_product_name(String r29_product_name) { this.r29_product_name = r29_product_name; }
		public String getR29_cross_reference() { return r29_cross_reference; }
		public void setR29_cross_reference(String r29_cross_reference) { this.r29_cross_reference = r29_cross_reference; }
		public BigDecimal getR29_month() { return r29_month; }
		public void setR29_month(BigDecimal r29_month) { this.r29_month = r29_month; }
		public BigDecimal getR29_ytd() { return r29_ytd; }
		public void setR29_ytd(BigDecimal r29_ytd) { this.r29_ytd = r29_ytd; }
		public String getR30_product_name() { return r30_product_name; }
		public void setR30_product_name(String r30_product_name) { this.r30_product_name = r30_product_name; }
		public String getR30_cross_reference() { return r30_cross_reference; }
		public void setR30_cross_reference(String r30_cross_reference) { this.r30_cross_reference = r30_cross_reference; }
		public BigDecimal getR30_month() { return r30_month; }
		public void setR30_month(BigDecimal r30_month) { this.r30_month = r30_month; }
		public BigDecimal getR30_ytd() { return r30_ytd; }
		public void setR30_ytd(BigDecimal r30_ytd) { this.r30_ytd = r30_ytd; }
		public String getR31_product_name() { return r31_product_name; }
		public void setR31_product_name(String r31_product_name) { this.r31_product_name = r31_product_name; }
		public String getR31_cross_reference() { return r31_cross_reference; }
		public void setR31_cross_reference(String r31_cross_reference) { this.r31_cross_reference = r31_cross_reference; }
		public BigDecimal getR31_month() { return r31_month; }
		public void setR31_month(BigDecimal r31_month) { this.r31_month = r31_month; }
		public BigDecimal getR31_ytd() { return r31_ytd; }
		public void setR31_ytd(BigDecimal r31_ytd) { this.r31_ytd = r31_ytd; }
		public String getR32_product_name() { return r32_product_name; }
		public void setR32_product_name(String r32_product_name) { this.r32_product_name = r32_product_name; }
		public String getR32_cross_reference() { return r32_cross_reference; }
		public void setR32_cross_reference(String r32_cross_reference) { this.r32_cross_reference = r32_cross_reference; }
		public BigDecimal getR32_month() { return r32_month; }
		public void setR32_month(BigDecimal r32_month) { this.r32_month = r32_month; }
		public BigDecimal getR32_ytd() { return r32_ytd; }
		public void setR32_ytd(BigDecimal r32_ytd) { this.r32_ytd = r32_ytd; }
		public String getR33_product_name() { return r33_product_name; }
		public void setR33_product_name(String r33_product_name) { this.r33_product_name = r33_product_name; }
		public String getR33_cross_reference() { return r33_cross_reference; }
		public void setR33_cross_reference(String r33_cross_reference) { this.r33_cross_reference = r33_cross_reference; }
		public BigDecimal getR33_month() { return r33_month; }
		public void setR33_month(BigDecimal r33_month) { this.r33_month = r33_month; }
		public BigDecimal getR33_ytd() { return r33_ytd; }
		public void setR33_ytd(BigDecimal r33_ytd) { this.r33_ytd = r33_ytd; }
		public String getR34_product_name() { return r34_product_name; }
		public void setR34_product_name(String r34_product_name) { this.r34_product_name = r34_product_name; }
		public String getR34_cross_reference() { return r34_cross_reference; }
		public void setR34_cross_reference(String r34_cross_reference) { this.r34_cross_reference = r34_cross_reference; }
		public BigDecimal getR34_month() { return r34_month; }
		public void setR34_month(BigDecimal r34_month) { this.r34_month = r34_month; }
		public BigDecimal getR34_ytd() { return r34_ytd; }
		public void setR34_ytd(BigDecimal r34_ytd) { this.r34_ytd = r34_ytd; }
		public String getR35_product_name() { return r35_product_name; }
		public void setR35_product_name(String r35_product_name) { this.r35_product_name = r35_product_name; }
		public String getR35_cross_reference() { return r35_cross_reference; }
		public void setR35_cross_reference(String r35_cross_reference) { this.r35_cross_reference = r35_cross_reference; }
		public BigDecimal getR35_month() { return r35_month; }
		public void setR35_month(BigDecimal r35_month) { this.r35_month = r35_month; }
		public BigDecimal getR35_ytd() { return r35_ytd; }
		public void setR35_ytd(BigDecimal r35_ytd) { this.r35_ytd = r35_ytd; }
		public String getR36_product_name() { return r36_product_name; }
		public void setR36_product_name(String r36_product_name) { this.r36_product_name = r36_product_name; }
		public String getR36_cross_reference() { return r36_cross_reference; }
		public void setR36_cross_reference(String r36_cross_reference) { this.r36_cross_reference = r36_cross_reference; }
		public BigDecimal getR36_month() { return r36_month; }
		public void setR36_month(BigDecimal r36_month) { this.r36_month = r36_month; }
		public BigDecimal getR36_ytd() { return r36_ytd; }
		public void setR36_ytd(BigDecimal r36_ytd) { this.r36_ytd = r36_ytd; }
		public String getR37_product_name() { return r37_product_name; }
		public void setR37_product_name(String r37_product_name) { this.r37_product_name = r37_product_name; }
		public String getR37_cross_reference() { return r37_cross_reference; }
		public void setR37_cross_reference(String r37_cross_reference) { this.r37_cross_reference = r37_cross_reference; }
		public BigDecimal getR37_month() { return r37_month; }
		public void setR37_month(BigDecimal r37_month) { this.r37_month = r37_month; }
		public BigDecimal getR37_ytd() { return r37_ytd; }
		public void setR37_ytd(BigDecimal r37_ytd) { this.r37_ytd = r37_ytd; }
		public String getR38_product_name() { return r38_product_name; }
		public void setR38_product_name(String r38_product_name) { this.r38_product_name = r38_product_name; }
		public String getR38_cross_reference() { return r38_cross_reference; }
		public void setR38_cross_reference(String r38_cross_reference) { this.r38_cross_reference = r38_cross_reference; }
		public BigDecimal getR38_month() { return r38_month; }
		public void setR38_month(BigDecimal r38_month) { this.r38_month = r38_month; }
		public BigDecimal getR38_ytd() { return r38_ytd; }
		public void setR38_ytd(BigDecimal r38_ytd) { this.r38_ytd = r38_ytd; }
		public String getR39_product_name() { return r39_product_name; }
		public void setR39_product_name(String r39_product_name) { this.r39_product_name = r39_product_name; }
		public String getR39_cross_reference() { return r39_cross_reference; }
		public void setR39_cross_reference(String r39_cross_reference) { this.r39_cross_reference = r39_cross_reference; }
		public BigDecimal getR39_month() { return r39_month; }
		public void setR39_month(BigDecimal r39_month) { this.r39_month = r39_month; }
		public BigDecimal getR39_ytd() { return r39_ytd; }
		public void setR39_ytd(BigDecimal r39_ytd) { this.r39_ytd = r39_ytd; }
		public String getR40_product_name() { return r40_product_name; }
		public void setR40_product_name(String r40_product_name) { this.r40_product_name = r40_product_name; }
		public String getR40_cross_reference() { return r40_cross_reference; }
		public void setR40_cross_reference(String r40_cross_reference) { this.r40_cross_reference = r40_cross_reference; }
		public BigDecimal getR40_month() { return r40_month; }
		public void setR40_month(BigDecimal r40_month) { this.r40_month = r40_month; }
		public BigDecimal getR40_ytd() { return r40_ytd; }
		public void setR40_ytd(BigDecimal r40_ytd) { this.r40_ytd = r40_ytd; }
		public String getR41_product_name() { return r41_product_name; }
		public void setR41_product_name(String r41_product_name) { this.r41_product_name = r41_product_name; }
		public String getR41_cross_reference() { return r41_cross_reference; }
		public void setR41_cross_reference(String r41_cross_reference) { this.r41_cross_reference = r41_cross_reference; }
		public BigDecimal getR41_month() { return r41_month; }
		public void setR41_month(BigDecimal r41_month) { this.r41_month = r41_month; }
		public BigDecimal getR41_ytd() { return r41_ytd; }
		public void setR41_ytd(BigDecimal r41_ytd) { this.r41_ytd = r41_ytd; }
		public String getR42_product_name() { return r42_product_name; }
		public void setR42_product_name(String r42_product_name) { this.r42_product_name = r42_product_name; }
		public String getR42_cross_reference() { return r42_cross_reference; }
		public void setR42_cross_reference(String r42_cross_reference) { this.r42_cross_reference = r42_cross_reference; }
		public BigDecimal getR42_month() { return r42_month; }
		public void setR42_month(BigDecimal r42_month) { this.r42_month = r42_month; }
		public BigDecimal getR42_ytd() { return r42_ytd; }
		public void setR42_ytd(BigDecimal r42_ytd) { this.r42_ytd = r42_ytd; }
		public String getR43_product_name() { return r43_product_name; }
		public void setR43_product_name(String r43_product_name) { this.r43_product_name = r43_product_name; }
		public String getR43_cross_reference() { return r43_cross_reference; }
		public void setR43_cross_reference(String r43_cross_reference) { this.r43_cross_reference = r43_cross_reference; }
		public BigDecimal getR43_month() { return r43_month; }
		public void setR43_month(BigDecimal r43_month) { this.r43_month = r43_month; }
		public BigDecimal getR43_ytd() { return r43_ytd; }
		public void setR43_ytd(BigDecimal r43_ytd) { this.r43_ytd = r43_ytd; }
		public String getR44_product_name() { return r44_product_name; }
		public void setR44_product_name(String r44_product_name) { this.r44_product_name = r44_product_name; }
		public String getR44_cross_reference() { return r44_cross_reference; }
		public void setR44_cross_reference(String r44_cross_reference) { this.r44_cross_reference = r44_cross_reference; }
		public BigDecimal getR44_month() { return r44_month; }
		public void setR44_month(BigDecimal r44_month) { this.r44_month = r44_month; }
		public BigDecimal getR44_ytd() { return r44_ytd; }
		public void setR44_ytd(BigDecimal r44_ytd) { this.r44_ytd = r44_ytd; }
		public String getR45_product_name() { return r45_product_name; }
		public void setR45_product_name(String r45_product_name) { this.r45_product_name = r45_product_name; }
		public String getR45_cross_reference() { return r45_cross_reference; }
		public void setR45_cross_reference(String r45_cross_reference) { this.r45_cross_reference = r45_cross_reference; }
		public BigDecimal getR45_month() { return r45_month; }
		public void setR45_month(BigDecimal r45_month) { this.r45_month = r45_month; }
		public BigDecimal getR45_ytd() { return r45_ytd; }
		public void setR45_ytd(BigDecimal r45_ytd) { this.r45_ytd = r45_ytd; }
		public String getR46_product_name() { return r46_product_name; }
		public void setR46_product_name(String r46_product_name) { this.r46_product_name = r46_product_name; }
		public String getR46_cross_reference() { return r46_cross_reference; }
		public void setR46_cross_reference(String r46_cross_reference) { this.r46_cross_reference = r46_cross_reference; }
		public BigDecimal getR46_month() { return r46_month; }
		public void setR46_month(BigDecimal r46_month) { this.r46_month = r46_month; }
		public BigDecimal getR46_ytd() { return r46_ytd; }
		public void setR46_ytd(BigDecimal r46_ytd) { this.r46_ytd = r46_ytd; }
		public String getR47_product_name() { return r47_product_name; }
		public void setR47_product_name(String r47_product_name) { this.r47_product_name = r47_product_name; }
		public String getR47_cross_reference() { return r47_cross_reference; }
		public void setR47_cross_reference(String r47_cross_reference) { this.r47_cross_reference = r47_cross_reference; }
		public BigDecimal getR47_month() { return r47_month; }
		public void setR47_month(BigDecimal r47_month) { this.r47_month = r47_month; }
		public BigDecimal getR47_ytd() { return r47_ytd; }
		public void setR47_ytd(BigDecimal r47_ytd) { this.r47_ytd = r47_ytd; }
		public String getR48_product_name() { return r48_product_name; }
		public void setR48_product_name(String r48_product_name) { this.r48_product_name = r48_product_name; }
		public String getR48_cross_reference() { return r48_cross_reference; }
		public void setR48_cross_reference(String r48_cross_reference) { this.r48_cross_reference = r48_cross_reference; }
		public BigDecimal getR48_month() { return r48_month; }
		public void setR48_month(BigDecimal r48_month) { this.r48_month = r48_month; }
		public BigDecimal getR48_ytd() { return r48_ytd; }
		public void setR48_ytd(BigDecimal r48_ytd) { this.r48_ytd = r48_ytd; }
		public String getR49_product_name() { return r49_product_name; }
		public void setR49_product_name(String r49_product_name) { this.r49_product_name = r49_product_name; }
		public String getR49_cross_reference() { return r49_cross_reference; }
		public void setR49_cross_reference(String r49_cross_reference) { this.r49_cross_reference = r49_cross_reference; }
		public BigDecimal getR49_month() { return r49_month; }
		public void setR49_month(BigDecimal r49_month) { this.r49_month = r49_month; }
		public BigDecimal getR49_ytd() { return r49_ytd; }
		public void setR49_ytd(BigDecimal r49_ytd) { this.r49_ytd = r49_ytd; }
		public String getR50_product_name() { return r50_product_name; }
		public void setR50_product_name(String r50_product_name) { this.r50_product_name = r50_product_name; }
		public String getR50_cross_reference() { return r50_cross_reference; }
		public void setR50_cross_reference(String r50_cross_reference) { this.r50_cross_reference = r50_cross_reference; }
		public BigDecimal getR50_month() { return r50_month; }
		public void setR50_month(BigDecimal r50_month) { this.r50_month = r50_month; }
		public BigDecimal getR50_ytd() { return r50_ytd; }
		public void setR50_ytd(BigDecimal r50_ytd) { this.r50_ytd = r50_ytd; }
		public String getR51_product_name() { return r51_product_name; }
		public void setR51_product_name(String r51_product_name) { this.r51_product_name = r51_product_name; }
		public String getR51_cross_reference() { return r51_cross_reference; }
		public void setR51_cross_reference(String r51_cross_reference) { this.r51_cross_reference = r51_cross_reference; }
		public BigDecimal getR51_month() { return r51_month; }
		public void setR51_month(BigDecimal r51_month) { this.r51_month = r51_month; }
		public BigDecimal getR51_ytd() { return r51_ytd; }
		public void setR51_ytd(BigDecimal r51_ytd) { this.r51_ytd = r51_ytd; }
		public String getR52_product_name() { return r52_product_name; }
		public void setR52_product_name(String r52_product_name) { this.r52_product_name = r52_product_name; }
		public String getR52_cross_reference() { return r52_cross_reference; }
		public void setR52_cross_reference(String r52_cross_reference) { this.r52_cross_reference = r52_cross_reference; }
		public BigDecimal getR52_month() { return r52_month; }
		public void setR52_month(BigDecimal r52_month) { this.r52_month = r52_month; }
		public BigDecimal getR52_ytd() { return r52_ytd; }
		public void setR52_ytd(BigDecimal r52_ytd) { this.r52_ytd = r52_ytd; }
		public String getR53_product_name() { return r53_product_name; }
		public void setR53_product_name(String r53_product_name) { this.r53_product_name = r53_product_name; }
		public String getR53_cross_reference() { return r53_cross_reference; }
		public void setR53_cross_reference(String r53_cross_reference) { this.r53_cross_reference = r53_cross_reference; }
		public BigDecimal getR53_month() { return r53_month; }
		public void setR53_month(BigDecimal r53_month) { this.r53_month = r53_month; }
		public BigDecimal getR53_ytd() { return r53_ytd; }
		public void setR53_ytd(BigDecimal r53_ytd) { this.r53_ytd = r53_ytd; }
		public String getR54_product_name() { return r54_product_name; }
		public void setR54_product_name(String r54_product_name) { this.r54_product_name = r54_product_name; }
		public String getR54_cross_reference() { return r54_cross_reference; }
		public void setR54_cross_reference(String r54_cross_reference) { this.r54_cross_reference = r54_cross_reference; }
		public BigDecimal getR54_month() { return r54_month; }
		public void setR54_month(BigDecimal r54_month) { this.r54_month = r54_month; }
		public BigDecimal getR54_ytd() { return r54_ytd; }
		public void setR54_ytd(BigDecimal r54_ytd) { this.r54_ytd = r54_ytd; }
		public String getR55_product_name() { return r55_product_name; }
		public void setR55_product_name(String r55_product_name) { this.r55_product_name = r55_product_name; }
		public String getR55_cross_reference() { return r55_cross_reference; }
		public void setR55_cross_reference(String r55_cross_reference) { this.r55_cross_reference = r55_cross_reference; }
		public BigDecimal getR55_month() { return r55_month; }
		public void setR55_month(BigDecimal r55_month) { this.r55_month = r55_month; }
		public BigDecimal getR55_ytd() { return r55_ytd; }
		public void setR55_ytd(BigDecimal r55_ytd) { this.r55_ytd = r55_ytd; }
		public String getR56_product_name() { return r56_product_name; }
		public void setR56_product_name(String r56_product_name) { this.r56_product_name = r56_product_name; }
		public String getR56_cross_reference() { return r56_cross_reference; }
		public void setR56_cross_reference(String r56_cross_reference) { this.r56_cross_reference = r56_cross_reference; }
		public BigDecimal getR56_month() { return r56_month; }
		public void setR56_month(BigDecimal r56_month) { this.r56_month = r56_month; }
		public BigDecimal getR56_ytd() { return r56_ytd; }
		public void setR56_ytd(BigDecimal r56_ytd) { this.r56_ytd = r56_ytd; }
		public String getR57_product_name() { return r57_product_name; }
		public void setR57_product_name(String r57_product_name) { this.r57_product_name = r57_product_name; }
		public String getR57_cross_reference() { return r57_cross_reference; }
		public void setR57_cross_reference(String r57_cross_reference) { this.r57_cross_reference = r57_cross_reference; }
		public BigDecimal getR57_month() { return r57_month; }
		public void setR57_month(BigDecimal r57_month) { this.r57_month = r57_month; }
		public BigDecimal getR57_ytd() { return r57_ytd; }
		public void setR57_ytd(BigDecimal r57_ytd) { this.r57_ytd = r57_ytd; }
		public String getR58_product_name() { return r58_product_name; }
		public void setR58_product_name(String r58_product_name) { this.r58_product_name = r58_product_name; }
		public String getR58_cross_reference() { return r58_cross_reference; }
		public void setR58_cross_reference(String r58_cross_reference) { this.r58_cross_reference = r58_cross_reference; }
		public BigDecimal getR58_month() { return r58_month; }
		public void setR58_month(BigDecimal r58_month) { this.r58_month = r58_month; }
		public BigDecimal getR58_ytd() { return r58_ytd; }
		public void setR58_ytd(BigDecimal r58_ytd) { this.r58_ytd = r58_ytd; }
		public String getR59_product_name() { return r59_product_name; }
		public void setR59_product_name(String r59_product_name) { this.r59_product_name = r59_product_name; }
		public String getR59_cross_reference() { return r59_cross_reference; }
		public void setR59_cross_reference(String r59_cross_reference) { this.r59_cross_reference = r59_cross_reference; }
		public BigDecimal getR59_month() { return r59_month; }
		public void setR59_month(BigDecimal r59_month) { this.r59_month = r59_month; }
		public BigDecimal getR59_ytd() { return r59_ytd; }
		public void setR59_ytd(BigDecimal r59_ytd) { this.r59_ytd = r59_ytd; }
		public String getR60_product_name() { return r60_product_name; }
		public void setR60_product_name(String r60_product_name) { this.r60_product_name = r60_product_name; }
		public String getR60_cross_reference() { return r60_cross_reference; }
		public void setR60_cross_reference(String r60_cross_reference) { this.r60_cross_reference = r60_cross_reference; }
		public BigDecimal getR60_month() { return r60_month; }
		public void setR60_month(BigDecimal r60_month) { this.r60_month = r60_month; }
		public BigDecimal getR60_ytd() { return r60_ytd; }
		public void setR60_ytd(BigDecimal r60_ytd) { this.r60_ytd = r60_ytd; }
		public String getR61_product_name() { return r61_product_name; }
		public void setR61_product_name(String r61_product_name) { this.r61_product_name = r61_product_name; }
		public String getR61_cross_reference() { return r61_cross_reference; }
		public void setR61_cross_reference(String r61_cross_reference) { this.r61_cross_reference = r61_cross_reference; }
		public BigDecimal getR61_month() { return r61_month; }
		public void setR61_month(BigDecimal r61_month) { this.r61_month = r61_month; }
		public BigDecimal getR61_ytd() { return r61_ytd; }
		public void setR61_ytd(BigDecimal r61_ytd) { this.r61_ytd = r61_ytd; }
		public String getR62_product_name() { return r62_product_name; }
		public void setR62_product_name(String r62_product_name) { this.r62_product_name = r62_product_name; }
		public String getR62_cross_reference() { return r62_cross_reference; }
		public void setR62_cross_reference(String r62_cross_reference) { this.r62_cross_reference = r62_cross_reference; }
		public BigDecimal getR62_month() { return r62_month; }
		public void setR62_month(BigDecimal r62_month) { this.r62_month = r62_month; }
		public BigDecimal getR62_ytd() { return r62_ytd; }
		public void setR62_ytd(BigDecimal r62_ytd) { this.r62_ytd = r62_ytd; }
		public String getR63_product_name() { return r63_product_name; }
		public void setR63_product_name(String r63_product_name) { this.r63_product_name = r63_product_name; }
		public String getR63_cross_reference() { return r63_cross_reference; }
		public void setR63_cross_reference(String r63_cross_reference) { this.r63_cross_reference = r63_cross_reference; }
		public BigDecimal getR63_month() { return r63_month; }
		public void setR63_month(BigDecimal r63_month) { this.r63_month = r63_month; }
		public BigDecimal getR63_ytd() { return r63_ytd; }
		public void setR63_ytd(BigDecimal r63_ytd) { this.r63_ytd = r63_ytd; }
		public String getR64_product_name() { return r64_product_name; }
		public void setR64_product_name(String r64_product_name) { this.r64_product_name = r64_product_name; }
		public String getR64_cross_reference() { return r64_cross_reference; }
		public void setR64_cross_reference(String r64_cross_reference) { this.r64_cross_reference = r64_cross_reference; }
		public BigDecimal getR64_month() { return r64_month; }
		public void setR64_month(BigDecimal r64_month) { this.r64_month = r64_month; }
		public BigDecimal getR64_ytd() { return r64_ytd; }
		public void setR64_ytd(BigDecimal r64_ytd) { this.r64_ytd = r64_ytd; }
		public String getR65_product_name() { return r65_product_name; }
		public void setR65_product_name(String r65_product_name) { this.r65_product_name = r65_product_name; }
		public String getR65_cross_reference() { return r65_cross_reference; }
		public void setR65_cross_reference(String r65_cross_reference) { this.r65_cross_reference = r65_cross_reference; }
		public BigDecimal getR65_month() { return r65_month; }
		public void setR65_month(BigDecimal r65_month) { this.r65_month = r65_month; }
		public BigDecimal getR65_ytd() { return r65_ytd; }
		public void setR65_ytd(BigDecimal r65_ytd) { this.r65_ytd = r65_ytd; }
		public String getR66_product_name() { return r66_product_name; }
		public void setR66_product_name(String r66_product_name) { this.r66_product_name = r66_product_name; }
		public String getR66_cross_reference() { return r66_cross_reference; }
		public void setR66_cross_reference(String r66_cross_reference) { this.r66_cross_reference = r66_cross_reference; }
		public BigDecimal getR66_month() { return r66_month; }
		public void setR66_month(BigDecimal r66_month) { this.r66_month = r66_month; }
		public BigDecimal getR66_ytd() { return r66_ytd; }
		public void setR66_ytd(BigDecimal r66_ytd) { this.r66_ytd = r66_ytd; }
		public String getR67_product_name() { return r67_product_name; }
		public void setR67_product_name(String r67_product_name) { this.r67_product_name = r67_product_name; }
		public String getR67_cross_reference() { return r67_cross_reference; }
		public void setR67_cross_reference(String r67_cross_reference) { this.r67_cross_reference = r67_cross_reference; }
		public BigDecimal getR67_month() { return r67_month; }
		public void setR67_month(BigDecimal r67_month) { this.r67_month = r67_month; }
		public BigDecimal getR67_ytd() { return r67_ytd; }
		public void setR67_ytd(BigDecimal r67_ytd) { this.r67_ytd = r67_ytd; }
		public String getR68_product_name() { return r68_product_name; }
		public void setR68_product_name(String r68_product_name) { this.r68_product_name = r68_product_name; }
		public String getR68_cross_reference() { return r68_cross_reference; }
		public void setR68_cross_reference(String r68_cross_reference) { this.r68_cross_reference = r68_cross_reference; }
		public BigDecimal getR68_month() { return r68_month; }
		public void setR68_month(BigDecimal r68_month) { this.r68_month = r68_month; }
		public BigDecimal getR68_ytd() { return r68_ytd; }
		public void setR68_ytd(BigDecimal r68_ytd) { this.r68_ytd = r68_ytd; }
		public String getR69_product_name() { return r69_product_name; }
		public void setR69_product_name(String r69_product_name) { this.r69_product_name = r69_product_name; }
		public String getR69_cross_reference() { return r69_cross_reference; }
		public void setR69_cross_reference(String r69_cross_reference) { this.r69_cross_reference = r69_cross_reference; }
		public BigDecimal getR69_month() { return r69_month; }
		public void setR69_month(BigDecimal r69_month) { this.r69_month = r69_month; }
		public BigDecimal getR69_ytd() { return r69_ytd; }
		public void setR69_ytd(BigDecimal r69_ytd) { this.r69_ytd = r69_ytd; }
		public String getR70_product_name() { return r70_product_name; }
		public void setR70_product_name(String r70_product_name) { this.r70_product_name = r70_product_name; }
		public String getR70_cross_reference() { return r70_cross_reference; }
		public void setR70_cross_reference(String r70_cross_reference) { this.r70_cross_reference = r70_cross_reference; }
		public BigDecimal getR70_month() { return r70_month; }
		public void setR70_month(BigDecimal r70_month) { this.r70_month = r70_month; }
		public BigDecimal getR70_ytd() { return r70_ytd; }
		public void setR70_ytd(BigDecimal r70_ytd) { this.r70_ytd = r70_ytd; }
		public String getR71_product_name() { return r71_product_name; }
		public void setR71_product_name(String r71_product_name) { this.r71_product_name = r71_product_name; }
		public String getR71_cross_reference() { return r71_cross_reference; }
		public void setR71_cross_reference(String r71_cross_reference) { this.r71_cross_reference = r71_cross_reference; }
		public BigDecimal getR71_month() { return r71_month; }
		public void setR71_month(BigDecimal r71_month) { this.r71_month = r71_month; }
		public BigDecimal getR71_ytd() { return r71_ytd; }
		public void setR71_ytd(BigDecimal r71_ytd) { this.r71_ytd = r71_ytd; }
		public String getR72_product_name() { return r72_product_name; }
		public void setR72_product_name(String r72_product_name) { this.r72_product_name = r72_product_name; }
		public String getR72_cross_reference() { return r72_cross_reference; }
		public void setR72_cross_reference(String r72_cross_reference) { this.r72_cross_reference = r72_cross_reference; }
		public BigDecimal getR72_month() { return r72_month; }
		public void setR72_month(BigDecimal r72_month) { this.r72_month = r72_month; }
		public BigDecimal getR72_ytd() { return r72_ytd; }
		public void setR72_ytd(BigDecimal r72_ytd) { this.r72_ytd = r72_ytd; }
		public String getR73_product_name() { return r73_product_name; }
		public void setR73_product_name(String r73_product_name) { this.r73_product_name = r73_product_name; }
		public String getR73_cross_reference() { return r73_cross_reference; }
		public void setR73_cross_reference(String r73_cross_reference) { this.r73_cross_reference = r73_cross_reference; }
		public BigDecimal getR73_month() { return r73_month; }
		public void setR73_month(BigDecimal r73_month) { this.r73_month = r73_month; }
		public BigDecimal getR73_ytd() { return r73_ytd; }
		public void setR73_ytd(BigDecimal r73_ytd) { this.r73_ytd = r73_ytd; }
		public String getR74_product_name() { return r74_product_name; }
		public void setR74_product_name(String r74_product_name) { this.r74_product_name = r74_product_name; }
		public String getR74_cross_reference() { return r74_cross_reference; }
		public void setR74_cross_reference(String r74_cross_reference) { this.r74_cross_reference = r74_cross_reference; }
		public BigDecimal getR74_month() { return r74_month; }
		public void setR74_month(BigDecimal r74_month) { this.r74_month = r74_month; }
		public BigDecimal getR74_ytd() { return r74_ytd; }
		public void setR74_ytd(BigDecimal r74_ytd) { this.r74_ytd = r74_ytd; }
		public String getR75_product_name() { return r75_product_name; }
		public void setR75_product_name(String r75_product_name) { this.r75_product_name = r75_product_name; }
		public String getR75_cross_reference() { return r75_cross_reference; }
		public void setR75_cross_reference(String r75_cross_reference) { this.r75_cross_reference = r75_cross_reference; }
		public BigDecimal getR75_month() { return r75_month; }
		public void setR75_month(BigDecimal r75_month) { this.r75_month = r75_month; }
		public BigDecimal getR75_ytd() { return r75_ytd; }
		public void setR75_ytd(BigDecimal r75_ytd) { this.r75_ytd = r75_ytd; }
		public String getR76_product_name() { return r76_product_name; }
		public void setR76_product_name(String r76_product_name) { this.r76_product_name = r76_product_name; }
		public String getR76_cross_reference() { return r76_cross_reference; }
		public void setR76_cross_reference(String r76_cross_reference) { this.r76_cross_reference = r76_cross_reference; }
		public BigDecimal getR76_month() { return r76_month; }
		public void setR76_month(BigDecimal r76_month) { this.r76_month = r76_month; }
		public BigDecimal getR76_ytd() { return r76_ytd; }
		public void setR76_ytd(BigDecimal r76_ytd) { this.r76_ytd = r76_ytd; }
		public String getR77_product_name() { return r77_product_name; }
		public void setR77_product_name(String r77_product_name) { this.r77_product_name = r77_product_name; }
		public String getR77_cross_reference() { return r77_cross_reference; }
		public void setR77_cross_reference(String r77_cross_reference) { this.r77_cross_reference = r77_cross_reference; }
		public BigDecimal getR77_month() { return r77_month; }
		public void setR77_month(BigDecimal r77_month) { this.r77_month = r77_month; }
		public BigDecimal getR77_ytd() { return r77_ytd; }
		public void setR77_ytd(BigDecimal r77_ytd) { this.r77_ytd = r77_ytd; }
		public String getR78_product_name() { return r78_product_name; }
		public void setR78_product_name(String r78_product_name) { this.r78_product_name = r78_product_name; }
		public String getR78_cross_reference() { return r78_cross_reference; }
		public void setR78_cross_reference(String r78_cross_reference) { this.r78_cross_reference = r78_cross_reference; }
		public BigDecimal getR78_month() { return r78_month; }
		public void setR78_month(BigDecimal r78_month) { this.r78_month = r78_month; }
		public BigDecimal getR78_ytd() { return r78_ytd; }
		public void setR78_ytd(BigDecimal r78_ytd) { this.r78_ytd = r78_ytd; }
		public String getR79_product_name() { return r79_product_name; }
		public void setR79_product_name(String r79_product_name) { this.r79_product_name = r79_product_name; }
		public String getR79_cross_reference() { return r79_cross_reference; }
		public void setR79_cross_reference(String r79_cross_reference) { this.r79_cross_reference = r79_cross_reference; }
		public BigDecimal getR79_month() { return r79_month; }
		public void setR79_month(BigDecimal r79_month) { this.r79_month = r79_month; }
		public BigDecimal getR79_ytd() { return r79_ytd; }
		public void setR79_ytd(BigDecimal r79_ytd) { this.r79_ytd = r79_ytd; }
		public String getR80_product_name() { return r80_product_name; }
		public void setR80_product_name(String r80_product_name) { this.r80_product_name = r80_product_name; }
		public String getR80_cross_reference() { return r80_cross_reference; }
		public void setR80_cross_reference(String r80_cross_reference) { this.r80_cross_reference = r80_cross_reference; }
		public BigDecimal getR80_month() { return r80_month; }
		public void setR80_month(BigDecimal r80_month) { this.r80_month = r80_month; }
		public BigDecimal getR80_ytd() { return r80_ytd; }
		public void setR80_ytd(BigDecimal r80_ytd) { this.r80_ytd = r80_ytd; }
		public String getR81_product_name() { return r81_product_name; }
		public void setR81_product_name(String r81_product_name) { this.r81_product_name = r81_product_name; }
		public String getR81_cross_reference() { return r81_cross_reference; }
		public void setR81_cross_reference(String r81_cross_reference) { this.r81_cross_reference = r81_cross_reference; }
		public BigDecimal getR81_month() { return r81_month; }
		public void setR81_month(BigDecimal r81_month) { this.r81_month = r81_month; }
		public BigDecimal getR81_ytd() { return r81_ytd; }
		public void setR81_ytd(BigDecimal r81_ytd) { this.r81_ytd = r81_ytd; }
		public String getR82_product_name() { return r82_product_name; }
		public void setR82_product_name(String r82_product_name) { this.r82_product_name = r82_product_name; }
		public String getR82_cross_reference() { return r82_cross_reference; }
		public void setR82_cross_reference(String r82_cross_reference) { this.r82_cross_reference = r82_cross_reference; }
		public BigDecimal getR82_month() { return r82_month; }
		public void setR82_month(BigDecimal r82_month) { this.r82_month = r82_month; }
		public BigDecimal getR82_ytd() { return r82_ytd; }
		public void setR82_ytd(BigDecimal r82_ytd) { this.r82_ytd = r82_ytd; }
		public String getR83_product_name() { return r83_product_name; }
		public void setR83_product_name(String r83_product_name) { this.r83_product_name = r83_product_name; }
		public String getR83_cross_reference() { return r83_cross_reference; }
		public void setR83_cross_reference(String r83_cross_reference) { this.r83_cross_reference = r83_cross_reference; }
		public BigDecimal getR83_month() { return r83_month; }
		public void setR83_month(BigDecimal r83_month) { this.r83_month = r83_month; }
		public BigDecimal getR83_ytd() { return r83_ytd; }
		public void setR83_ytd(BigDecimal r83_ytd) { this.r83_ytd = r83_ytd; }
		public String getR84_product_name() { return r84_product_name; }
		public void setR84_product_name(String r84_product_name) { this.r84_product_name = r84_product_name; }
		public String getR84_cross_reference() { return r84_cross_reference; }
		public void setR84_cross_reference(String r84_cross_reference) { this.r84_cross_reference = r84_cross_reference; }
		public BigDecimal getR84_month() { return r84_month; }
		public void setR84_month(BigDecimal r84_month) { this.r84_month = r84_month; }
		public BigDecimal getR84_ytd() { return r84_ytd; }
		public void setR84_ytd(BigDecimal r84_ytd) { this.r84_ytd = r84_ytd; }
		public String getR85_product_name() { return r85_product_name; }
		public void setR85_product_name(String r85_product_name) { this.r85_product_name = r85_product_name; }
		public String getR85_cross_reference() { return r85_cross_reference; }
		public void setR85_cross_reference(String r85_cross_reference) { this.r85_cross_reference = r85_cross_reference; }
		public BigDecimal getR85_month() { return r85_month; }
		public void setR85_month(BigDecimal r85_month) { this.r85_month = r85_month; }
		public BigDecimal getR85_ytd() { return r85_ytd; }
		public void setR85_ytd(BigDecimal r85_ytd) { this.r85_ytd = r85_ytd; }
		public String getR86_product_name() { return r86_product_name; }
		public void setR86_product_name(String r86_product_name) { this.r86_product_name = r86_product_name; }
		public String getR86_cross_reference() { return r86_cross_reference; }
		public void setR86_cross_reference(String r86_cross_reference) { this.r86_cross_reference = r86_cross_reference; }
		public BigDecimal getR86_month() { return r86_month; }
		public void setR86_month(BigDecimal r86_month) { this.r86_month = r86_month; }
		public BigDecimal getR86_ytd() { return r86_ytd; }
		public void setR86_ytd(BigDecimal r86_ytd) { this.r86_ytd = r86_ytd; }
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

	// =========================================================
	// Inner entity: M_SCI_E_RESUB_Summary_Entity
	// =========================================================
	public static class M_SCI_E_RESUB_Summary_Entity {
		private String r10_product_name;
		private String r10_cross_reference;
		private BigDecimal r10_month;
		private BigDecimal r10_ytd;
		private String r11_product_name;
		private String r11_cross_reference;
		private BigDecimal r11_month;
		private BigDecimal r11_ytd;
		private String r12_product_name;
		private String r12_cross_reference;
		private BigDecimal r12_month;
		private BigDecimal r12_ytd;
		private String r13_product_name;
		private String r13_cross_reference;
		private BigDecimal r13_month;
		private BigDecimal r13_ytd;
		private String r14_product_name;
		private String r14_cross_reference;
		private BigDecimal r14_month;
		private BigDecimal r14_ytd;
		private String r15_product_name;
		private String r15_cross_reference;
		private BigDecimal r15_month;
		private BigDecimal r15_ytd;
		private String r16_product_name;
		private String r16_cross_reference;
		private BigDecimal r16_month;
		private BigDecimal r16_ytd;
		private String r17_product_name;
		private String r17_cross_reference;
		private BigDecimal r17_month;
		private BigDecimal r17_ytd;
		private String r18_product_name;
		private String r18_cross_reference;
		private BigDecimal r18_month;
		private BigDecimal r18_ytd;
		private String r19_product_name;
		private String r19_cross_reference;
		private BigDecimal r19_month;
		private BigDecimal r19_ytd;
		private String r20_product_name;
		private String r20_cross_reference;
		private BigDecimal r20_month;
		private BigDecimal r20_ytd;
		private String r21_product_name;
		private String r21_cross_reference;
		private BigDecimal r21_month;
		private BigDecimal r21_ytd;
		private String r22_product_name;
		private String r22_cross_reference;
		private BigDecimal r22_month;
		private BigDecimal r22_ytd;
		private String r23_product_name;
		private String r23_cross_reference;
		private BigDecimal r23_month;
		private BigDecimal r23_ytd;
		private String r24_product_name;
		private String r24_cross_reference;
		private BigDecimal r24_month;
		private BigDecimal r24_ytd;
		private String r25_product_name;
		private String r25_cross_reference;
		private BigDecimal r25_month;
		private BigDecimal r25_ytd;
		private String r26_product_name;
		private String r26_cross_reference;
		private BigDecimal r26_month;
		private BigDecimal r26_ytd;
		private String r27_product_name;
		private String r27_cross_reference;
		private BigDecimal r27_month;
		private BigDecimal r27_ytd;
		private String r28_product_name;
		private String r28_cross_reference;
		private BigDecimal r28_month;
		private BigDecimal r28_ytd;
		private String r29_product_name;
		private String r29_cross_reference;
		private BigDecimal r29_month;
		private BigDecimal r29_ytd;
		private String r30_product_name;
		private String r30_cross_reference;
		private BigDecimal r30_month;
		private BigDecimal r30_ytd;
		private String r31_product_name;
		private String r31_cross_reference;
		private BigDecimal r31_month;
		private BigDecimal r31_ytd;
		private String r32_product_name;
		private String r32_cross_reference;
		private BigDecimal r32_month;
		private BigDecimal r32_ytd;
		private String r33_product_name;
		private String r33_cross_reference;
		private BigDecimal r33_month;
		private BigDecimal r33_ytd;
		private String r34_product_name;
		private String r34_cross_reference;
		private BigDecimal r34_month;
		private BigDecimal r34_ytd;
		private String r35_product_name;
		private String r35_cross_reference;
		private BigDecimal r35_month;
		private BigDecimal r35_ytd;
		private String r36_product_name;
		private String r36_cross_reference;
		private BigDecimal r36_month;
		private BigDecimal r36_ytd;
		private String r37_product_name;
		private String r37_cross_reference;
		private BigDecimal r37_month;
		private BigDecimal r37_ytd;
		private String r38_product_name;
		private String r38_cross_reference;
		private BigDecimal r38_month;
		private BigDecimal r38_ytd;
		private String r39_product_name;
		private String r39_cross_reference;
		private BigDecimal r39_month;
		private BigDecimal r39_ytd;
		private String r40_product_name;
		private String r40_cross_reference;
		private BigDecimal r40_month;
		private BigDecimal r40_ytd;
		private String r41_product_name;
		private String r41_cross_reference;
		private BigDecimal r41_month;
		private BigDecimal r41_ytd;
		private String r42_product_name;
		private String r42_cross_reference;
		private BigDecimal r42_month;
		private BigDecimal r42_ytd;
		private String r43_product_name;
		private String r43_cross_reference;
		private BigDecimal r43_month;
		private BigDecimal r43_ytd;
		private String r44_product_name;
		private String r44_cross_reference;
		private BigDecimal r44_month;
		private BigDecimal r44_ytd;
		private String r45_product_name;
		private String r45_cross_reference;
		private BigDecimal r45_month;
		private BigDecimal r45_ytd;
		private String r46_product_name;
		private String r46_cross_reference;
		private BigDecimal r46_month;
		private BigDecimal r46_ytd;
		private String r47_product_name;
		private String r47_cross_reference;
		private BigDecimal r47_month;
		private BigDecimal r47_ytd;
		private String r48_product_name;
		private String r48_cross_reference;
		private BigDecimal r48_month;
		private BigDecimal r48_ytd;
		private String r49_product_name;
		private String r49_cross_reference;
		private BigDecimal r49_month;
		private BigDecimal r49_ytd;
		private String r50_product_name;
		private String r50_cross_reference;
		private BigDecimal r50_month;
		private BigDecimal r50_ytd;
		private String r51_product_name;
		private String r51_cross_reference;
		private BigDecimal r51_month;
		private BigDecimal r51_ytd;
		private String r52_product_name;
		private String r52_cross_reference;
		private BigDecimal r52_month;
		private BigDecimal r52_ytd;
		private String r53_product_name;
		private String r53_cross_reference;
		private BigDecimal r53_month;
		private BigDecimal r53_ytd;
		private String r54_product_name;
		private String r54_cross_reference;
		private BigDecimal r54_month;
		private BigDecimal r54_ytd;
		private String r55_product_name;
		private String r55_cross_reference;
		private BigDecimal r55_month;
		private BigDecimal r55_ytd;
		private String r56_product_name;
		private String r56_cross_reference;
		private BigDecimal r56_month;
		private BigDecimal r56_ytd;
		private String r57_product_name;
		private String r57_cross_reference;
		private BigDecimal r57_month;
		private BigDecimal r57_ytd;
		private String r58_product_name;
		private String r58_cross_reference;
		private BigDecimal r58_month;
		private BigDecimal r58_ytd;
		private String r59_product_name;
		private String r59_cross_reference;
		private BigDecimal r59_month;
		private BigDecimal r59_ytd;
		private String r60_product_name;
		private String r60_cross_reference;
		private BigDecimal r60_month;
		private BigDecimal r60_ytd;
		private String r61_product_name;
		private String r61_cross_reference;
		private BigDecimal r61_month;
		private BigDecimal r61_ytd;
		private String r62_product_name;
		private String r62_cross_reference;
		private BigDecimal r62_month;
		private BigDecimal r62_ytd;
		private String r63_product_name;
		private String r63_cross_reference;
		private BigDecimal r63_month;
		private BigDecimal r63_ytd;
		private String r64_product_name;
		private String r64_cross_reference;
		private BigDecimal r64_month;
		private BigDecimal r64_ytd;
		private String r65_product_name;
		private String r65_cross_reference;
		private BigDecimal r65_month;
		private BigDecimal r65_ytd;
		private String r66_product_name;
		private String r66_cross_reference;
		private BigDecimal r66_month;
		private BigDecimal r66_ytd;
		private String r67_product_name;
		private String r67_cross_reference;
		private BigDecimal r67_month;
		private BigDecimal r67_ytd;
		private String r68_product_name;
		private String r68_cross_reference;
		private BigDecimal r68_month;
		private BigDecimal r68_ytd;
		private String r69_product_name;
		private String r69_cross_reference;
		private BigDecimal r69_month;
		private BigDecimal r69_ytd;
		private String r70_product_name;
		private String r70_cross_reference;
		private BigDecimal r70_month;
		private BigDecimal r70_ytd;
		private String r71_product_name;
		private String r71_cross_reference;
		private BigDecimal r71_month;
		private BigDecimal r71_ytd;
		private String r72_product_name;
		private String r72_cross_reference;
		private BigDecimal r72_month;
		private BigDecimal r72_ytd;
		private String r73_product_name;
		private String r73_cross_reference;
		private BigDecimal r73_month;
		private BigDecimal r73_ytd;
		private String r74_product_name;
		private String r74_cross_reference;
		private BigDecimal r74_month;
		private BigDecimal r74_ytd;
		private String r75_product_name;
		private String r75_cross_reference;
		private BigDecimal r75_month;
		private BigDecimal r75_ytd;
		private String r76_product_name;
		private String r76_cross_reference;
		private BigDecimal r76_month;
		private BigDecimal r76_ytd;
		private String r77_product_name;
		private String r77_cross_reference;
		private BigDecimal r77_month;
		private BigDecimal r77_ytd;
		private String r78_product_name;
		private String r78_cross_reference;
		private BigDecimal r78_month;
		private BigDecimal r78_ytd;
		private String r79_product_name;
		private String r79_cross_reference;
		private BigDecimal r79_month;
		private BigDecimal r79_ytd;
		private String r80_product_name;
		private String r80_cross_reference;
		private BigDecimal r80_month;
		private BigDecimal r80_ytd;
		private String r81_product_name;
		private String r81_cross_reference;
		private BigDecimal r81_month;
		private BigDecimal r81_ytd;
		private String r82_product_name;
		private String r82_cross_reference;
		private BigDecimal r82_month;
		private BigDecimal r82_ytd;
		private String r83_product_name;
		private String r83_cross_reference;
		private BigDecimal r83_month;
		private BigDecimal r83_ytd;
		private String r84_product_name;
		private String r84_cross_reference;
		private BigDecimal r84_month;
		private BigDecimal r84_ytd;
		private String r85_product_name;
		private String r85_cross_reference;
		private BigDecimal r85_month;
		private BigDecimal r85_ytd;
		private String r86_product_name;
		private String r86_cross_reference;
		private BigDecimal r86_month;
		private BigDecimal r86_ytd;

		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;
		private Date reportResubDate;

		public String getR10_product_name() { return r10_product_name; }
		public void setR10_product_name(String r10_product_name) { this.r10_product_name = r10_product_name; }
		public String getR10_cross_reference() { return r10_cross_reference; }
		public void setR10_cross_reference(String r10_cross_reference) { this.r10_cross_reference = r10_cross_reference; }
		public BigDecimal getR10_month() { return r10_month; }
		public void setR10_month(BigDecimal r10_month) { this.r10_month = r10_month; }
		public BigDecimal getR10_ytd() { return r10_ytd; }
		public void setR10_ytd(BigDecimal r10_ytd) { this.r10_ytd = r10_ytd; }
		public String getR11_product_name() { return r11_product_name; }
		public void setR11_product_name(String r11_product_name) { this.r11_product_name = r11_product_name; }
		public String getR11_cross_reference() { return r11_cross_reference; }
		public void setR11_cross_reference(String r11_cross_reference) { this.r11_cross_reference = r11_cross_reference; }
		public BigDecimal getR11_month() { return r11_month; }
		public void setR11_month(BigDecimal r11_month) { this.r11_month = r11_month; }
		public BigDecimal getR11_ytd() { return r11_ytd; }
		public void setR11_ytd(BigDecimal r11_ytd) { this.r11_ytd = r11_ytd; }
		public String getR12_product_name() { return r12_product_name; }
		public void setR12_product_name(String r12_product_name) { this.r12_product_name = r12_product_name; }
		public String getR12_cross_reference() { return r12_cross_reference; }
		public void setR12_cross_reference(String r12_cross_reference) { this.r12_cross_reference = r12_cross_reference; }
		public BigDecimal getR12_month() { return r12_month; }
		public void setR12_month(BigDecimal r12_month) { this.r12_month = r12_month; }
		public BigDecimal getR12_ytd() { return r12_ytd; }
		public void setR12_ytd(BigDecimal r12_ytd) { this.r12_ytd = r12_ytd; }
		public String getR13_product_name() { return r13_product_name; }
		public void setR13_product_name(String r13_product_name) { this.r13_product_name = r13_product_name; }
		public String getR13_cross_reference() { return r13_cross_reference; }
		public void setR13_cross_reference(String r13_cross_reference) { this.r13_cross_reference = r13_cross_reference; }
		public BigDecimal getR13_month() { return r13_month; }
		public void setR13_month(BigDecimal r13_month) { this.r13_month = r13_month; }
		public BigDecimal getR13_ytd() { return r13_ytd; }
		public void setR13_ytd(BigDecimal r13_ytd) { this.r13_ytd = r13_ytd; }
		public String getR14_product_name() { return r14_product_name; }
		public void setR14_product_name(String r14_product_name) { this.r14_product_name = r14_product_name; }
		public String getR14_cross_reference() { return r14_cross_reference; }
		public void setR14_cross_reference(String r14_cross_reference) { this.r14_cross_reference = r14_cross_reference; }
		public BigDecimal getR14_month() { return r14_month; }
		public void setR14_month(BigDecimal r14_month) { this.r14_month = r14_month; }
		public BigDecimal getR14_ytd() { return r14_ytd; }
		public void setR14_ytd(BigDecimal r14_ytd) { this.r14_ytd = r14_ytd; }
		public String getR15_product_name() { return r15_product_name; }
		public void setR15_product_name(String r15_product_name) { this.r15_product_name = r15_product_name; }
		public String getR15_cross_reference() { return r15_cross_reference; }
		public void setR15_cross_reference(String r15_cross_reference) { this.r15_cross_reference = r15_cross_reference; }
		public BigDecimal getR15_month() { return r15_month; }
		public void setR15_month(BigDecimal r15_month) { this.r15_month = r15_month; }
		public BigDecimal getR15_ytd() { return r15_ytd; }
		public void setR15_ytd(BigDecimal r15_ytd) { this.r15_ytd = r15_ytd; }
		public String getR16_product_name() { return r16_product_name; }
		public void setR16_product_name(String r16_product_name) { this.r16_product_name = r16_product_name; }
		public String getR16_cross_reference() { return r16_cross_reference; }
		public void setR16_cross_reference(String r16_cross_reference) { this.r16_cross_reference = r16_cross_reference; }
		public BigDecimal getR16_month() { return r16_month; }
		public void setR16_month(BigDecimal r16_month) { this.r16_month = r16_month; }
		public BigDecimal getR16_ytd() { return r16_ytd; }
		public void setR16_ytd(BigDecimal r16_ytd) { this.r16_ytd = r16_ytd; }
		public String getR17_product_name() { return r17_product_name; }
		public void setR17_product_name(String r17_product_name) { this.r17_product_name = r17_product_name; }
		public String getR17_cross_reference() { return r17_cross_reference; }
		public void setR17_cross_reference(String r17_cross_reference) { this.r17_cross_reference = r17_cross_reference; }
		public BigDecimal getR17_month() { return r17_month; }
		public void setR17_month(BigDecimal r17_month) { this.r17_month = r17_month; }
		public BigDecimal getR17_ytd() { return r17_ytd; }
		public void setR17_ytd(BigDecimal r17_ytd) { this.r17_ytd = r17_ytd; }
		public String getR18_product_name() { return r18_product_name; }
		public void setR18_product_name(String r18_product_name) { this.r18_product_name = r18_product_name; }
		public String getR18_cross_reference() { return r18_cross_reference; }
		public void setR18_cross_reference(String r18_cross_reference) { this.r18_cross_reference = r18_cross_reference; }
		public BigDecimal getR18_month() { return r18_month; }
		public void setR18_month(BigDecimal r18_month) { this.r18_month = r18_month; }
		public BigDecimal getR18_ytd() { return r18_ytd; }
		public void setR18_ytd(BigDecimal r18_ytd) { this.r18_ytd = r18_ytd; }
		public String getR19_product_name() { return r19_product_name; }
		public void setR19_product_name(String r19_product_name) { this.r19_product_name = r19_product_name; }
		public String getR19_cross_reference() { return r19_cross_reference; }
		public void setR19_cross_reference(String r19_cross_reference) { this.r19_cross_reference = r19_cross_reference; }
		public BigDecimal getR19_month() { return r19_month; }
		public void setR19_month(BigDecimal r19_month) { this.r19_month = r19_month; }
		public BigDecimal getR19_ytd() { return r19_ytd; }
		public void setR19_ytd(BigDecimal r19_ytd) { this.r19_ytd = r19_ytd; }
		public String getR20_product_name() { return r20_product_name; }
		public void setR20_product_name(String r20_product_name) { this.r20_product_name = r20_product_name; }
		public String getR20_cross_reference() { return r20_cross_reference; }
		public void setR20_cross_reference(String r20_cross_reference) { this.r20_cross_reference = r20_cross_reference; }
		public BigDecimal getR20_month() { return r20_month; }
		public void setR20_month(BigDecimal r20_month) { this.r20_month = r20_month; }
		public BigDecimal getR20_ytd() { return r20_ytd; }
		public void setR20_ytd(BigDecimal r20_ytd) { this.r20_ytd = r20_ytd; }
		public String getR21_product_name() { return r21_product_name; }
		public void setR21_product_name(String r21_product_name) { this.r21_product_name = r21_product_name; }
		public String getR21_cross_reference() { return r21_cross_reference; }
		public void setR21_cross_reference(String r21_cross_reference) { this.r21_cross_reference = r21_cross_reference; }
		public BigDecimal getR21_month() { return r21_month; }
		public void setR21_month(BigDecimal r21_month) { this.r21_month = r21_month; }
		public BigDecimal getR21_ytd() { return r21_ytd; }
		public void setR21_ytd(BigDecimal r21_ytd) { this.r21_ytd = r21_ytd; }
		public String getR22_product_name() { return r22_product_name; }
		public void setR22_product_name(String r22_product_name) { this.r22_product_name = r22_product_name; }
		public String getR22_cross_reference() { return r22_cross_reference; }
		public void setR22_cross_reference(String r22_cross_reference) { this.r22_cross_reference = r22_cross_reference; }
		public BigDecimal getR22_month() { return r22_month; }
		public void setR22_month(BigDecimal r22_month) { this.r22_month = r22_month; }
		public BigDecimal getR22_ytd() { return r22_ytd; }
		public void setR22_ytd(BigDecimal r22_ytd) { this.r22_ytd = r22_ytd; }
		public String getR23_product_name() { return r23_product_name; }
		public void setR23_product_name(String r23_product_name) { this.r23_product_name = r23_product_name; }
		public String getR23_cross_reference() { return r23_cross_reference; }
		public void setR23_cross_reference(String r23_cross_reference) { this.r23_cross_reference = r23_cross_reference; }
		public BigDecimal getR23_month() { return r23_month; }
		public void setR23_month(BigDecimal r23_month) { this.r23_month = r23_month; }
		public BigDecimal getR23_ytd() { return r23_ytd; }
		public void setR23_ytd(BigDecimal r23_ytd) { this.r23_ytd = r23_ytd; }
		public String getR24_product_name() { return r24_product_name; }
		public void setR24_product_name(String r24_product_name) { this.r24_product_name = r24_product_name; }
		public String getR24_cross_reference() { return r24_cross_reference; }
		public void setR24_cross_reference(String r24_cross_reference) { this.r24_cross_reference = r24_cross_reference; }
		public BigDecimal getR24_month() { return r24_month; }
		public void setR24_month(BigDecimal r24_month) { this.r24_month = r24_month; }
		public BigDecimal getR24_ytd() { return r24_ytd; }
		public void setR24_ytd(BigDecimal r24_ytd) { this.r24_ytd = r24_ytd; }
		public String getR25_product_name() { return r25_product_name; }
		public void setR25_product_name(String r25_product_name) { this.r25_product_name = r25_product_name; }
		public String getR25_cross_reference() { return r25_cross_reference; }
		public void setR25_cross_reference(String r25_cross_reference) { this.r25_cross_reference = r25_cross_reference; }
		public BigDecimal getR25_month() { return r25_month; }
		public void setR25_month(BigDecimal r25_month) { this.r25_month = r25_month; }
		public BigDecimal getR25_ytd() { return r25_ytd; }
		public void setR25_ytd(BigDecimal r25_ytd) { this.r25_ytd = r25_ytd; }
		public String getR26_product_name() { return r26_product_name; }
		public void setR26_product_name(String r26_product_name) { this.r26_product_name = r26_product_name; }
		public String getR26_cross_reference() { return r26_cross_reference; }
		public void setR26_cross_reference(String r26_cross_reference) { this.r26_cross_reference = r26_cross_reference; }
		public BigDecimal getR26_month() { return r26_month; }
		public void setR26_month(BigDecimal r26_month) { this.r26_month = r26_month; }
		public BigDecimal getR26_ytd() { return r26_ytd; }
		public void setR26_ytd(BigDecimal r26_ytd) { this.r26_ytd = r26_ytd; }
		public String getR27_product_name() { return r27_product_name; }
		public void setR27_product_name(String r27_product_name) { this.r27_product_name = r27_product_name; }
		public String getR27_cross_reference() { return r27_cross_reference; }
		public void setR27_cross_reference(String r27_cross_reference) { this.r27_cross_reference = r27_cross_reference; }
		public BigDecimal getR27_month() { return r27_month; }
		public void setR27_month(BigDecimal r27_month) { this.r27_month = r27_month; }
		public BigDecimal getR27_ytd() { return r27_ytd; }
		public void setR27_ytd(BigDecimal r27_ytd) { this.r27_ytd = r27_ytd; }
		public String getR28_product_name() { return r28_product_name; }
		public void setR28_product_name(String r28_product_name) { this.r28_product_name = r28_product_name; }
		public String getR28_cross_reference() { return r28_cross_reference; }
		public void setR28_cross_reference(String r28_cross_reference) { this.r28_cross_reference = r28_cross_reference; }
		public BigDecimal getR28_month() { return r28_month; }
		public void setR28_month(BigDecimal r28_month) { this.r28_month = r28_month; }
		public BigDecimal getR28_ytd() { return r28_ytd; }
		public void setR28_ytd(BigDecimal r28_ytd) { this.r28_ytd = r28_ytd; }
		public String getR29_product_name() { return r29_product_name; }
		public void setR29_product_name(String r29_product_name) { this.r29_product_name = r29_product_name; }
		public String getR29_cross_reference() { return r29_cross_reference; }
		public void setR29_cross_reference(String r29_cross_reference) { this.r29_cross_reference = r29_cross_reference; }
		public BigDecimal getR29_month() { return r29_month; }
		public void setR29_month(BigDecimal r29_month) { this.r29_month = r29_month; }
		public BigDecimal getR29_ytd() { return r29_ytd; }
		public void setR29_ytd(BigDecimal r29_ytd) { this.r29_ytd = r29_ytd; }
		public String getR30_product_name() { return r30_product_name; }
		public void setR30_product_name(String r30_product_name) { this.r30_product_name = r30_product_name; }
		public String getR30_cross_reference() { return r30_cross_reference; }
		public void setR30_cross_reference(String r30_cross_reference) { this.r30_cross_reference = r30_cross_reference; }
		public BigDecimal getR30_month() { return r30_month; }
		public void setR30_month(BigDecimal r30_month) { this.r30_month = r30_month; }
		public BigDecimal getR30_ytd() { return r30_ytd; }
		public void setR30_ytd(BigDecimal r30_ytd) { this.r30_ytd = r30_ytd; }
		public String getR31_product_name() { return r31_product_name; }
		public void setR31_product_name(String r31_product_name) { this.r31_product_name = r31_product_name; }
		public String getR31_cross_reference() { return r31_cross_reference; }
		public void setR31_cross_reference(String r31_cross_reference) { this.r31_cross_reference = r31_cross_reference; }
		public BigDecimal getR31_month() { return r31_month; }
		public void setR31_month(BigDecimal r31_month) { this.r31_month = r31_month; }
		public BigDecimal getR31_ytd() { return r31_ytd; }
		public void setR31_ytd(BigDecimal r31_ytd) { this.r31_ytd = r31_ytd; }
		public String getR32_product_name() { return r32_product_name; }
		public void setR32_product_name(String r32_product_name) { this.r32_product_name = r32_product_name; }
		public String getR32_cross_reference() { return r32_cross_reference; }
		public void setR32_cross_reference(String r32_cross_reference) { this.r32_cross_reference = r32_cross_reference; }
		public BigDecimal getR32_month() { return r32_month; }
		public void setR32_month(BigDecimal r32_month) { this.r32_month = r32_month; }
		public BigDecimal getR32_ytd() { return r32_ytd; }
		public void setR32_ytd(BigDecimal r32_ytd) { this.r32_ytd = r32_ytd; }
		public String getR33_product_name() { return r33_product_name; }
		public void setR33_product_name(String r33_product_name) { this.r33_product_name = r33_product_name; }
		public String getR33_cross_reference() { return r33_cross_reference; }
		public void setR33_cross_reference(String r33_cross_reference) { this.r33_cross_reference = r33_cross_reference; }
		public BigDecimal getR33_month() { return r33_month; }
		public void setR33_month(BigDecimal r33_month) { this.r33_month = r33_month; }
		public BigDecimal getR33_ytd() { return r33_ytd; }
		public void setR33_ytd(BigDecimal r33_ytd) { this.r33_ytd = r33_ytd; }
		public String getR34_product_name() { return r34_product_name; }
		public void setR34_product_name(String r34_product_name) { this.r34_product_name = r34_product_name; }
		public String getR34_cross_reference() { return r34_cross_reference; }
		public void setR34_cross_reference(String r34_cross_reference) { this.r34_cross_reference = r34_cross_reference; }
		public BigDecimal getR34_month() { return r34_month; }
		public void setR34_month(BigDecimal r34_month) { this.r34_month = r34_month; }
		public BigDecimal getR34_ytd() { return r34_ytd; }
		public void setR34_ytd(BigDecimal r34_ytd) { this.r34_ytd = r34_ytd; }
		public String getR35_product_name() { return r35_product_name; }
		public void setR35_product_name(String r35_product_name) { this.r35_product_name = r35_product_name; }
		public String getR35_cross_reference() { return r35_cross_reference; }
		public void setR35_cross_reference(String r35_cross_reference) { this.r35_cross_reference = r35_cross_reference; }
		public BigDecimal getR35_month() { return r35_month; }
		public void setR35_month(BigDecimal r35_month) { this.r35_month = r35_month; }
		public BigDecimal getR35_ytd() { return r35_ytd; }
		public void setR35_ytd(BigDecimal r35_ytd) { this.r35_ytd = r35_ytd; }
		public String getR36_product_name() { return r36_product_name; }
		public void setR36_product_name(String r36_product_name) { this.r36_product_name = r36_product_name; }
		public String getR36_cross_reference() { return r36_cross_reference; }
		public void setR36_cross_reference(String r36_cross_reference) { this.r36_cross_reference = r36_cross_reference; }
		public BigDecimal getR36_month() { return r36_month; }
		public void setR36_month(BigDecimal r36_month) { this.r36_month = r36_month; }
		public BigDecimal getR36_ytd() { return r36_ytd; }
		public void setR36_ytd(BigDecimal r36_ytd) { this.r36_ytd = r36_ytd; }
		public String getR37_product_name() { return r37_product_name; }
		public void setR37_product_name(String r37_product_name) { this.r37_product_name = r37_product_name; }
		public String getR37_cross_reference() { return r37_cross_reference; }
		public void setR37_cross_reference(String r37_cross_reference) { this.r37_cross_reference = r37_cross_reference; }
		public BigDecimal getR37_month() { return r37_month; }
		public void setR37_month(BigDecimal r37_month) { this.r37_month = r37_month; }
		public BigDecimal getR37_ytd() { return r37_ytd; }
		public void setR37_ytd(BigDecimal r37_ytd) { this.r37_ytd = r37_ytd; }
		public String getR38_product_name() { return r38_product_name; }
		public void setR38_product_name(String r38_product_name) { this.r38_product_name = r38_product_name; }
		public String getR38_cross_reference() { return r38_cross_reference; }
		public void setR38_cross_reference(String r38_cross_reference) { this.r38_cross_reference = r38_cross_reference; }
		public BigDecimal getR38_month() { return r38_month; }
		public void setR38_month(BigDecimal r38_month) { this.r38_month = r38_month; }
		public BigDecimal getR38_ytd() { return r38_ytd; }
		public void setR38_ytd(BigDecimal r38_ytd) { this.r38_ytd = r38_ytd; }
		public String getR39_product_name() { return r39_product_name; }
		public void setR39_product_name(String r39_product_name) { this.r39_product_name = r39_product_name; }
		public String getR39_cross_reference() { return r39_cross_reference; }
		public void setR39_cross_reference(String r39_cross_reference) { this.r39_cross_reference = r39_cross_reference; }
		public BigDecimal getR39_month() { return r39_month; }
		public void setR39_month(BigDecimal r39_month) { this.r39_month = r39_month; }
		public BigDecimal getR39_ytd() { return r39_ytd; }
		public void setR39_ytd(BigDecimal r39_ytd) { this.r39_ytd = r39_ytd; }
		public String getR40_product_name() { return r40_product_name; }
		public void setR40_product_name(String r40_product_name) { this.r40_product_name = r40_product_name; }
		public String getR40_cross_reference() { return r40_cross_reference; }
		public void setR40_cross_reference(String r40_cross_reference) { this.r40_cross_reference = r40_cross_reference; }
		public BigDecimal getR40_month() { return r40_month; }
		public void setR40_month(BigDecimal r40_month) { this.r40_month = r40_month; }
		public BigDecimal getR40_ytd() { return r40_ytd; }
		public void setR40_ytd(BigDecimal r40_ytd) { this.r40_ytd = r40_ytd; }
		public String getR41_product_name() { return r41_product_name; }
		public void setR41_product_name(String r41_product_name) { this.r41_product_name = r41_product_name; }
		public String getR41_cross_reference() { return r41_cross_reference; }
		public void setR41_cross_reference(String r41_cross_reference) { this.r41_cross_reference = r41_cross_reference; }
		public BigDecimal getR41_month() { return r41_month; }
		public void setR41_month(BigDecimal r41_month) { this.r41_month = r41_month; }
		public BigDecimal getR41_ytd() { return r41_ytd; }
		public void setR41_ytd(BigDecimal r41_ytd) { this.r41_ytd = r41_ytd; }
		public String getR42_product_name() { return r42_product_name; }
		public void setR42_product_name(String r42_product_name) { this.r42_product_name = r42_product_name; }
		public String getR42_cross_reference() { return r42_cross_reference; }
		public void setR42_cross_reference(String r42_cross_reference) { this.r42_cross_reference = r42_cross_reference; }
		public BigDecimal getR42_month() { return r42_month; }
		public void setR42_month(BigDecimal r42_month) { this.r42_month = r42_month; }
		public BigDecimal getR42_ytd() { return r42_ytd; }
		public void setR42_ytd(BigDecimal r42_ytd) { this.r42_ytd = r42_ytd; }
		public String getR43_product_name() { return r43_product_name; }
		public void setR43_product_name(String r43_product_name) { this.r43_product_name = r43_product_name; }
		public String getR43_cross_reference() { return r43_cross_reference; }
		public void setR43_cross_reference(String r43_cross_reference) { this.r43_cross_reference = r43_cross_reference; }
		public BigDecimal getR43_month() { return r43_month; }
		public void setR43_month(BigDecimal r43_month) { this.r43_month = r43_month; }
		public BigDecimal getR43_ytd() { return r43_ytd; }
		public void setR43_ytd(BigDecimal r43_ytd) { this.r43_ytd = r43_ytd; }
		public String getR44_product_name() { return r44_product_name; }
		public void setR44_product_name(String r44_product_name) { this.r44_product_name = r44_product_name; }
		public String getR44_cross_reference() { return r44_cross_reference; }
		public void setR44_cross_reference(String r44_cross_reference) { this.r44_cross_reference = r44_cross_reference; }
		public BigDecimal getR44_month() { return r44_month; }
		public void setR44_month(BigDecimal r44_month) { this.r44_month = r44_month; }
		public BigDecimal getR44_ytd() { return r44_ytd; }
		public void setR44_ytd(BigDecimal r44_ytd) { this.r44_ytd = r44_ytd; }
		public String getR45_product_name() { return r45_product_name; }
		public void setR45_product_name(String r45_product_name) { this.r45_product_name = r45_product_name; }
		public String getR45_cross_reference() { return r45_cross_reference; }
		public void setR45_cross_reference(String r45_cross_reference) { this.r45_cross_reference = r45_cross_reference; }
		public BigDecimal getR45_month() { return r45_month; }
		public void setR45_month(BigDecimal r45_month) { this.r45_month = r45_month; }
		public BigDecimal getR45_ytd() { return r45_ytd; }
		public void setR45_ytd(BigDecimal r45_ytd) { this.r45_ytd = r45_ytd; }
		public String getR46_product_name() { return r46_product_name; }
		public void setR46_product_name(String r46_product_name) { this.r46_product_name = r46_product_name; }
		public String getR46_cross_reference() { return r46_cross_reference; }
		public void setR46_cross_reference(String r46_cross_reference) { this.r46_cross_reference = r46_cross_reference; }
		public BigDecimal getR46_month() { return r46_month; }
		public void setR46_month(BigDecimal r46_month) { this.r46_month = r46_month; }
		public BigDecimal getR46_ytd() { return r46_ytd; }
		public void setR46_ytd(BigDecimal r46_ytd) { this.r46_ytd = r46_ytd; }
		public String getR47_product_name() { return r47_product_name; }
		public void setR47_product_name(String r47_product_name) { this.r47_product_name = r47_product_name; }
		public String getR47_cross_reference() { return r47_cross_reference; }
		public void setR47_cross_reference(String r47_cross_reference) { this.r47_cross_reference = r47_cross_reference; }
		public BigDecimal getR47_month() { return r47_month; }
		public void setR47_month(BigDecimal r47_month) { this.r47_month = r47_month; }
		public BigDecimal getR47_ytd() { return r47_ytd; }
		public void setR47_ytd(BigDecimal r47_ytd) { this.r47_ytd = r47_ytd; }
		public String getR48_product_name() { return r48_product_name; }
		public void setR48_product_name(String r48_product_name) { this.r48_product_name = r48_product_name; }
		public String getR48_cross_reference() { return r48_cross_reference; }
		public void setR48_cross_reference(String r48_cross_reference) { this.r48_cross_reference = r48_cross_reference; }
		public BigDecimal getR48_month() { return r48_month; }
		public void setR48_month(BigDecimal r48_month) { this.r48_month = r48_month; }
		public BigDecimal getR48_ytd() { return r48_ytd; }
		public void setR48_ytd(BigDecimal r48_ytd) { this.r48_ytd = r48_ytd; }
		public String getR49_product_name() { return r49_product_name; }
		public void setR49_product_name(String r49_product_name) { this.r49_product_name = r49_product_name; }
		public String getR49_cross_reference() { return r49_cross_reference; }
		public void setR49_cross_reference(String r49_cross_reference) { this.r49_cross_reference = r49_cross_reference; }
		public BigDecimal getR49_month() { return r49_month; }
		public void setR49_month(BigDecimal r49_month) { this.r49_month = r49_month; }
		public BigDecimal getR49_ytd() { return r49_ytd; }
		public void setR49_ytd(BigDecimal r49_ytd) { this.r49_ytd = r49_ytd; }
		public String getR50_product_name() { return r50_product_name; }
		public void setR50_product_name(String r50_product_name) { this.r50_product_name = r50_product_name; }
		public String getR50_cross_reference() { return r50_cross_reference; }
		public void setR50_cross_reference(String r50_cross_reference) { this.r50_cross_reference = r50_cross_reference; }
		public BigDecimal getR50_month() { return r50_month; }
		public void setR50_month(BigDecimal r50_month) { this.r50_month = r50_month; }
		public BigDecimal getR50_ytd() { return r50_ytd; }
		public void setR50_ytd(BigDecimal r50_ytd) { this.r50_ytd = r50_ytd; }
		public String getR51_product_name() { return r51_product_name; }
		public void setR51_product_name(String r51_product_name) { this.r51_product_name = r51_product_name; }
		public String getR51_cross_reference() { return r51_cross_reference; }
		public void setR51_cross_reference(String r51_cross_reference) { this.r51_cross_reference = r51_cross_reference; }
		public BigDecimal getR51_month() { return r51_month; }
		public void setR51_month(BigDecimal r51_month) { this.r51_month = r51_month; }
		public BigDecimal getR51_ytd() { return r51_ytd; }
		public void setR51_ytd(BigDecimal r51_ytd) { this.r51_ytd = r51_ytd; }
		public String getR52_product_name() { return r52_product_name; }
		public void setR52_product_name(String r52_product_name) { this.r52_product_name = r52_product_name; }
		public String getR52_cross_reference() { return r52_cross_reference; }
		public void setR52_cross_reference(String r52_cross_reference) { this.r52_cross_reference = r52_cross_reference; }
		public BigDecimal getR52_month() { return r52_month; }
		public void setR52_month(BigDecimal r52_month) { this.r52_month = r52_month; }
		public BigDecimal getR52_ytd() { return r52_ytd; }
		public void setR52_ytd(BigDecimal r52_ytd) { this.r52_ytd = r52_ytd; }
		public String getR53_product_name() { return r53_product_name; }
		public void setR53_product_name(String r53_product_name) { this.r53_product_name = r53_product_name; }
		public String getR53_cross_reference() { return r53_cross_reference; }
		public void setR53_cross_reference(String r53_cross_reference) { this.r53_cross_reference = r53_cross_reference; }
		public BigDecimal getR53_month() { return r53_month; }
		public void setR53_month(BigDecimal r53_month) { this.r53_month = r53_month; }
		public BigDecimal getR53_ytd() { return r53_ytd; }
		public void setR53_ytd(BigDecimal r53_ytd) { this.r53_ytd = r53_ytd; }
		public String getR54_product_name() { return r54_product_name; }
		public void setR54_product_name(String r54_product_name) { this.r54_product_name = r54_product_name; }
		public String getR54_cross_reference() { return r54_cross_reference; }
		public void setR54_cross_reference(String r54_cross_reference) { this.r54_cross_reference = r54_cross_reference; }
		public BigDecimal getR54_month() { return r54_month; }
		public void setR54_month(BigDecimal r54_month) { this.r54_month = r54_month; }
		public BigDecimal getR54_ytd() { return r54_ytd; }
		public void setR54_ytd(BigDecimal r54_ytd) { this.r54_ytd = r54_ytd; }
		public String getR55_product_name() { return r55_product_name; }
		public void setR55_product_name(String r55_product_name) { this.r55_product_name = r55_product_name; }
		public String getR55_cross_reference() { return r55_cross_reference; }
		public void setR55_cross_reference(String r55_cross_reference) { this.r55_cross_reference = r55_cross_reference; }
		public BigDecimal getR55_month() { return r55_month; }
		public void setR55_month(BigDecimal r55_month) { this.r55_month = r55_month; }
		public BigDecimal getR55_ytd() { return r55_ytd; }
		public void setR55_ytd(BigDecimal r55_ytd) { this.r55_ytd = r55_ytd; }
		public String getR56_product_name() { return r56_product_name; }
		public void setR56_product_name(String r56_product_name) { this.r56_product_name = r56_product_name; }
		public String getR56_cross_reference() { return r56_cross_reference; }
		public void setR56_cross_reference(String r56_cross_reference) { this.r56_cross_reference = r56_cross_reference; }
		public BigDecimal getR56_month() { return r56_month; }
		public void setR56_month(BigDecimal r56_month) { this.r56_month = r56_month; }
		public BigDecimal getR56_ytd() { return r56_ytd; }
		public void setR56_ytd(BigDecimal r56_ytd) { this.r56_ytd = r56_ytd; }
		public String getR57_product_name() { return r57_product_name; }
		public void setR57_product_name(String r57_product_name) { this.r57_product_name = r57_product_name; }
		public String getR57_cross_reference() { return r57_cross_reference; }
		public void setR57_cross_reference(String r57_cross_reference) { this.r57_cross_reference = r57_cross_reference; }
		public BigDecimal getR57_month() { return r57_month; }
		public void setR57_month(BigDecimal r57_month) { this.r57_month = r57_month; }
		public BigDecimal getR57_ytd() { return r57_ytd; }
		public void setR57_ytd(BigDecimal r57_ytd) { this.r57_ytd = r57_ytd; }
		public String getR58_product_name() { return r58_product_name; }
		public void setR58_product_name(String r58_product_name) { this.r58_product_name = r58_product_name; }
		public String getR58_cross_reference() { return r58_cross_reference; }
		public void setR58_cross_reference(String r58_cross_reference) { this.r58_cross_reference = r58_cross_reference; }
		public BigDecimal getR58_month() { return r58_month; }
		public void setR58_month(BigDecimal r58_month) { this.r58_month = r58_month; }
		public BigDecimal getR58_ytd() { return r58_ytd; }
		public void setR58_ytd(BigDecimal r58_ytd) { this.r58_ytd = r58_ytd; }
		public String getR59_product_name() { return r59_product_name; }
		public void setR59_product_name(String r59_product_name) { this.r59_product_name = r59_product_name; }
		public String getR59_cross_reference() { return r59_cross_reference; }
		public void setR59_cross_reference(String r59_cross_reference) { this.r59_cross_reference = r59_cross_reference; }
		public BigDecimal getR59_month() { return r59_month; }
		public void setR59_month(BigDecimal r59_month) { this.r59_month = r59_month; }
		public BigDecimal getR59_ytd() { return r59_ytd; }
		public void setR59_ytd(BigDecimal r59_ytd) { this.r59_ytd = r59_ytd; }
		public String getR60_product_name() { return r60_product_name; }
		public void setR60_product_name(String r60_product_name) { this.r60_product_name = r60_product_name; }
		public String getR60_cross_reference() { return r60_cross_reference; }
		public void setR60_cross_reference(String r60_cross_reference) { this.r60_cross_reference = r60_cross_reference; }
		public BigDecimal getR60_month() { return r60_month; }
		public void setR60_month(BigDecimal r60_month) { this.r60_month = r60_month; }
		public BigDecimal getR60_ytd() { return r60_ytd; }
		public void setR60_ytd(BigDecimal r60_ytd) { this.r60_ytd = r60_ytd; }
		public String getR61_product_name() { return r61_product_name; }
		public void setR61_product_name(String r61_product_name) { this.r61_product_name = r61_product_name; }
		public String getR61_cross_reference() { return r61_cross_reference; }
		public void setR61_cross_reference(String r61_cross_reference) { this.r61_cross_reference = r61_cross_reference; }
		public BigDecimal getR61_month() { return r61_month; }
		public void setR61_month(BigDecimal r61_month) { this.r61_month = r61_month; }
		public BigDecimal getR61_ytd() { return r61_ytd; }
		public void setR61_ytd(BigDecimal r61_ytd) { this.r61_ytd = r61_ytd; }
		public String getR62_product_name() { return r62_product_name; }
		public void setR62_product_name(String r62_product_name) { this.r62_product_name = r62_product_name; }
		public String getR62_cross_reference() { return r62_cross_reference; }
		public void setR62_cross_reference(String r62_cross_reference) { this.r62_cross_reference = r62_cross_reference; }
		public BigDecimal getR62_month() { return r62_month; }
		public void setR62_month(BigDecimal r62_month) { this.r62_month = r62_month; }
		public BigDecimal getR62_ytd() { return r62_ytd; }
		public void setR62_ytd(BigDecimal r62_ytd) { this.r62_ytd = r62_ytd; }
		public String getR63_product_name() { return r63_product_name; }
		public void setR63_product_name(String r63_product_name) { this.r63_product_name = r63_product_name; }
		public String getR63_cross_reference() { return r63_cross_reference; }
		public void setR63_cross_reference(String r63_cross_reference) { this.r63_cross_reference = r63_cross_reference; }
		public BigDecimal getR63_month() { return r63_month; }
		public void setR63_month(BigDecimal r63_month) { this.r63_month = r63_month; }
		public BigDecimal getR63_ytd() { return r63_ytd; }
		public void setR63_ytd(BigDecimal r63_ytd) { this.r63_ytd = r63_ytd; }
		public String getR64_product_name() { return r64_product_name; }
		public void setR64_product_name(String r64_product_name) { this.r64_product_name = r64_product_name; }
		public String getR64_cross_reference() { return r64_cross_reference; }
		public void setR64_cross_reference(String r64_cross_reference) { this.r64_cross_reference = r64_cross_reference; }
		public BigDecimal getR64_month() { return r64_month; }
		public void setR64_month(BigDecimal r64_month) { this.r64_month = r64_month; }
		public BigDecimal getR64_ytd() { return r64_ytd; }
		public void setR64_ytd(BigDecimal r64_ytd) { this.r64_ytd = r64_ytd; }
		public String getR65_product_name() { return r65_product_name; }
		public void setR65_product_name(String r65_product_name) { this.r65_product_name = r65_product_name; }
		public String getR65_cross_reference() { return r65_cross_reference; }
		public void setR65_cross_reference(String r65_cross_reference) { this.r65_cross_reference = r65_cross_reference; }
		public BigDecimal getR65_month() { return r65_month; }
		public void setR65_month(BigDecimal r65_month) { this.r65_month = r65_month; }
		public BigDecimal getR65_ytd() { return r65_ytd; }
		public void setR65_ytd(BigDecimal r65_ytd) { this.r65_ytd = r65_ytd; }
		public String getR66_product_name() { return r66_product_name; }
		public void setR66_product_name(String r66_product_name) { this.r66_product_name = r66_product_name; }
		public String getR66_cross_reference() { return r66_cross_reference; }
		public void setR66_cross_reference(String r66_cross_reference) { this.r66_cross_reference = r66_cross_reference; }
		public BigDecimal getR66_month() { return r66_month; }
		public void setR66_month(BigDecimal r66_month) { this.r66_month = r66_month; }
		public BigDecimal getR66_ytd() { return r66_ytd; }
		public void setR66_ytd(BigDecimal r66_ytd) { this.r66_ytd = r66_ytd; }
		public String getR67_product_name() { return r67_product_name; }
		public void setR67_product_name(String r67_product_name) { this.r67_product_name = r67_product_name; }
		public String getR67_cross_reference() { return r67_cross_reference; }
		public void setR67_cross_reference(String r67_cross_reference) { this.r67_cross_reference = r67_cross_reference; }
		public BigDecimal getR67_month() { return r67_month; }
		public void setR67_month(BigDecimal r67_month) { this.r67_month = r67_month; }
		public BigDecimal getR67_ytd() { return r67_ytd; }
		public void setR67_ytd(BigDecimal r67_ytd) { this.r67_ytd = r67_ytd; }
		public String getR68_product_name() { return r68_product_name; }
		public void setR68_product_name(String r68_product_name) { this.r68_product_name = r68_product_name; }
		public String getR68_cross_reference() { return r68_cross_reference; }
		public void setR68_cross_reference(String r68_cross_reference) { this.r68_cross_reference = r68_cross_reference; }
		public BigDecimal getR68_month() { return r68_month; }
		public void setR68_month(BigDecimal r68_month) { this.r68_month = r68_month; }
		public BigDecimal getR68_ytd() { return r68_ytd; }
		public void setR68_ytd(BigDecimal r68_ytd) { this.r68_ytd = r68_ytd; }
		public String getR69_product_name() { return r69_product_name; }
		public void setR69_product_name(String r69_product_name) { this.r69_product_name = r69_product_name; }
		public String getR69_cross_reference() { return r69_cross_reference; }
		public void setR69_cross_reference(String r69_cross_reference) { this.r69_cross_reference = r69_cross_reference; }
		public BigDecimal getR69_month() { return r69_month; }
		public void setR69_month(BigDecimal r69_month) { this.r69_month = r69_month; }
		public BigDecimal getR69_ytd() { return r69_ytd; }
		public void setR69_ytd(BigDecimal r69_ytd) { this.r69_ytd = r69_ytd; }
		public String getR70_product_name() { return r70_product_name; }
		public void setR70_product_name(String r70_product_name) { this.r70_product_name = r70_product_name; }
		public String getR70_cross_reference() { return r70_cross_reference; }
		public void setR70_cross_reference(String r70_cross_reference) { this.r70_cross_reference = r70_cross_reference; }
		public BigDecimal getR70_month() { return r70_month; }
		public void setR70_month(BigDecimal r70_month) { this.r70_month = r70_month; }
		public BigDecimal getR70_ytd() { return r70_ytd; }
		public void setR70_ytd(BigDecimal r70_ytd) { this.r70_ytd = r70_ytd; }
		public String getR71_product_name() { return r71_product_name; }
		public void setR71_product_name(String r71_product_name) { this.r71_product_name = r71_product_name; }
		public String getR71_cross_reference() { return r71_cross_reference; }
		public void setR71_cross_reference(String r71_cross_reference) { this.r71_cross_reference = r71_cross_reference; }
		public BigDecimal getR71_month() { return r71_month; }
		public void setR71_month(BigDecimal r71_month) { this.r71_month = r71_month; }
		public BigDecimal getR71_ytd() { return r71_ytd; }
		public void setR71_ytd(BigDecimal r71_ytd) { this.r71_ytd = r71_ytd; }
		public String getR72_product_name() { return r72_product_name; }
		public void setR72_product_name(String r72_product_name) { this.r72_product_name = r72_product_name; }
		public String getR72_cross_reference() { return r72_cross_reference; }
		public void setR72_cross_reference(String r72_cross_reference) { this.r72_cross_reference = r72_cross_reference; }
		public BigDecimal getR72_month() { return r72_month; }
		public void setR72_month(BigDecimal r72_month) { this.r72_month = r72_month; }
		public BigDecimal getR72_ytd() { return r72_ytd; }
		public void setR72_ytd(BigDecimal r72_ytd) { this.r72_ytd = r72_ytd; }
		public String getR73_product_name() { return r73_product_name; }
		public void setR73_product_name(String r73_product_name) { this.r73_product_name = r73_product_name; }
		public String getR73_cross_reference() { return r73_cross_reference; }
		public void setR73_cross_reference(String r73_cross_reference) { this.r73_cross_reference = r73_cross_reference; }
		public BigDecimal getR73_month() { return r73_month; }
		public void setR73_month(BigDecimal r73_month) { this.r73_month = r73_month; }
		public BigDecimal getR73_ytd() { return r73_ytd; }
		public void setR73_ytd(BigDecimal r73_ytd) { this.r73_ytd = r73_ytd; }
		public String getR74_product_name() { return r74_product_name; }
		public void setR74_product_name(String r74_product_name) { this.r74_product_name = r74_product_name; }
		public String getR74_cross_reference() { return r74_cross_reference; }
		public void setR74_cross_reference(String r74_cross_reference) { this.r74_cross_reference = r74_cross_reference; }
		public BigDecimal getR74_month() { return r74_month; }
		public void setR74_month(BigDecimal r74_month) { this.r74_month = r74_month; }
		public BigDecimal getR74_ytd() { return r74_ytd; }
		public void setR74_ytd(BigDecimal r74_ytd) { this.r74_ytd = r74_ytd; }
		public String getR75_product_name() { return r75_product_name; }
		public void setR75_product_name(String r75_product_name) { this.r75_product_name = r75_product_name; }
		public String getR75_cross_reference() { return r75_cross_reference; }
		public void setR75_cross_reference(String r75_cross_reference) { this.r75_cross_reference = r75_cross_reference; }
		public BigDecimal getR75_month() { return r75_month; }
		public void setR75_month(BigDecimal r75_month) { this.r75_month = r75_month; }
		public BigDecimal getR75_ytd() { return r75_ytd; }
		public void setR75_ytd(BigDecimal r75_ytd) { this.r75_ytd = r75_ytd; }
		public String getR76_product_name() { return r76_product_name; }
		public void setR76_product_name(String r76_product_name) { this.r76_product_name = r76_product_name; }
		public String getR76_cross_reference() { return r76_cross_reference; }
		public void setR76_cross_reference(String r76_cross_reference) { this.r76_cross_reference = r76_cross_reference; }
		public BigDecimal getR76_month() { return r76_month; }
		public void setR76_month(BigDecimal r76_month) { this.r76_month = r76_month; }
		public BigDecimal getR76_ytd() { return r76_ytd; }
		public void setR76_ytd(BigDecimal r76_ytd) { this.r76_ytd = r76_ytd; }
		public String getR77_product_name() { return r77_product_name; }
		public void setR77_product_name(String r77_product_name) { this.r77_product_name = r77_product_name; }
		public String getR77_cross_reference() { return r77_cross_reference; }
		public void setR77_cross_reference(String r77_cross_reference) { this.r77_cross_reference = r77_cross_reference; }
		public BigDecimal getR77_month() { return r77_month; }
		public void setR77_month(BigDecimal r77_month) { this.r77_month = r77_month; }
		public BigDecimal getR77_ytd() { return r77_ytd; }
		public void setR77_ytd(BigDecimal r77_ytd) { this.r77_ytd = r77_ytd; }
		public String getR78_product_name() { return r78_product_name; }
		public void setR78_product_name(String r78_product_name) { this.r78_product_name = r78_product_name; }
		public String getR78_cross_reference() { return r78_cross_reference; }
		public void setR78_cross_reference(String r78_cross_reference) { this.r78_cross_reference = r78_cross_reference; }
		public BigDecimal getR78_month() { return r78_month; }
		public void setR78_month(BigDecimal r78_month) { this.r78_month = r78_month; }
		public BigDecimal getR78_ytd() { return r78_ytd; }
		public void setR78_ytd(BigDecimal r78_ytd) { this.r78_ytd = r78_ytd; }
		public String getR79_product_name() { return r79_product_name; }
		public void setR79_product_name(String r79_product_name) { this.r79_product_name = r79_product_name; }
		public String getR79_cross_reference() { return r79_cross_reference; }
		public void setR79_cross_reference(String r79_cross_reference) { this.r79_cross_reference = r79_cross_reference; }
		public BigDecimal getR79_month() { return r79_month; }
		public void setR79_month(BigDecimal r79_month) { this.r79_month = r79_month; }
		public BigDecimal getR79_ytd() { return r79_ytd; }
		public void setR79_ytd(BigDecimal r79_ytd) { this.r79_ytd = r79_ytd; }
		public String getR80_product_name() { return r80_product_name; }
		public void setR80_product_name(String r80_product_name) { this.r80_product_name = r80_product_name; }
		public String getR80_cross_reference() { return r80_cross_reference; }
		public void setR80_cross_reference(String r80_cross_reference) { this.r80_cross_reference = r80_cross_reference; }
		public BigDecimal getR80_month() { return r80_month; }
		public void setR80_month(BigDecimal r80_month) { this.r80_month = r80_month; }
		public BigDecimal getR80_ytd() { return r80_ytd; }
		public void setR80_ytd(BigDecimal r80_ytd) { this.r80_ytd = r80_ytd; }
		public String getR81_product_name() { return r81_product_name; }
		public void setR81_product_name(String r81_product_name) { this.r81_product_name = r81_product_name; }
		public String getR81_cross_reference() { return r81_cross_reference; }
		public void setR81_cross_reference(String r81_cross_reference) { this.r81_cross_reference = r81_cross_reference; }
		public BigDecimal getR81_month() { return r81_month; }
		public void setR81_month(BigDecimal r81_month) { this.r81_month = r81_month; }
		public BigDecimal getR81_ytd() { return r81_ytd; }
		public void setR81_ytd(BigDecimal r81_ytd) { this.r81_ytd = r81_ytd; }
		public String getR82_product_name() { return r82_product_name; }
		public void setR82_product_name(String r82_product_name) { this.r82_product_name = r82_product_name; }
		public String getR82_cross_reference() { return r82_cross_reference; }
		public void setR82_cross_reference(String r82_cross_reference) { this.r82_cross_reference = r82_cross_reference; }
		public BigDecimal getR82_month() { return r82_month; }
		public void setR82_month(BigDecimal r82_month) { this.r82_month = r82_month; }
		public BigDecimal getR82_ytd() { return r82_ytd; }
		public void setR82_ytd(BigDecimal r82_ytd) { this.r82_ytd = r82_ytd; }
		public String getR83_product_name() { return r83_product_name; }
		public void setR83_product_name(String r83_product_name) { this.r83_product_name = r83_product_name; }
		public String getR83_cross_reference() { return r83_cross_reference; }
		public void setR83_cross_reference(String r83_cross_reference) { this.r83_cross_reference = r83_cross_reference; }
		public BigDecimal getR83_month() { return r83_month; }
		public void setR83_month(BigDecimal r83_month) { this.r83_month = r83_month; }
		public BigDecimal getR83_ytd() { return r83_ytd; }
		public void setR83_ytd(BigDecimal r83_ytd) { this.r83_ytd = r83_ytd; }
		public String getR84_product_name() { return r84_product_name; }
		public void setR84_product_name(String r84_product_name) { this.r84_product_name = r84_product_name; }
		public String getR84_cross_reference() { return r84_cross_reference; }
		public void setR84_cross_reference(String r84_cross_reference) { this.r84_cross_reference = r84_cross_reference; }
		public BigDecimal getR84_month() { return r84_month; }
		public void setR84_month(BigDecimal r84_month) { this.r84_month = r84_month; }
		public BigDecimal getR84_ytd() { return r84_ytd; }
		public void setR84_ytd(BigDecimal r84_ytd) { this.r84_ytd = r84_ytd; }
		public String getR85_product_name() { return r85_product_name; }
		public void setR85_product_name(String r85_product_name) { this.r85_product_name = r85_product_name; }
		public String getR85_cross_reference() { return r85_cross_reference; }
		public void setR85_cross_reference(String r85_cross_reference) { this.r85_cross_reference = r85_cross_reference; }
		public BigDecimal getR85_month() { return r85_month; }
		public void setR85_month(BigDecimal r85_month) { this.r85_month = r85_month; }
		public BigDecimal getR85_ytd() { return r85_ytd; }
		public void setR85_ytd(BigDecimal r85_ytd) { this.r85_ytd = r85_ytd; }
		public String getR86_product_name() { return r86_product_name; }
		public void setR86_product_name(String r86_product_name) { this.r86_product_name = r86_product_name; }
		public String getR86_cross_reference() { return r86_cross_reference; }
		public void setR86_cross_reference(String r86_cross_reference) { this.r86_cross_reference = r86_cross_reference; }
		public BigDecimal getR86_month() { return r86_month; }
		public void setR86_month(BigDecimal r86_month) { this.r86_month = r86_month; }
		public BigDecimal getR86_ytd() { return r86_ytd; }
		public void setR86_ytd(BigDecimal r86_ytd) { this.r86_ytd = r86_ytd; }
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

	// =========================================================
	// Inner entity: M_SCI_E_Detail_Entity
	// =========================================================
	public static class M_SCI_E_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportName;
		private String reportLable;
		private String reportAddlCriteria_1;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;
		private Date reportDate;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private char entityFlg;
		private char modifyFlg;
		private char delFlg;
		private BigDecimal debitEquivalent;
		private BigDecimal creditEquivalent;
		private BigDecimal balanceAmt;
		private String glshCode;
		private String acctCrncyCode;
		private BigDecimal monthlyInt;

		public String getCustId() { return custId; }
		public void setCustId(String custId) { this.custId = custId; }
		public String getAcctNumber() { return acctNumber; }
		public void setAcctNumber(String acctNumber) { this.acctNumber = acctNumber; }
		public String getAcctName() { return acctName; }
		public void setAcctName(String acctName) { this.acctName = acctName; }
		public String getDataType() { return dataType; }
		public void setDataType(String dataType) { this.dataType = dataType; }
		public String getReportName() { return reportName; }
		public void setReportName(String reportName) { this.reportName = reportName; }
		public String getReportLable() { return reportLable; }
		public void setReportLable(String reportLable) { this.reportLable = reportLable; }
		public String getReportAddlCriteria_1() { return reportAddlCriteria_1; }
		public void setReportAddlCriteria_1(String reportAddlCriteria_1) { this.reportAddlCriteria_1 = reportAddlCriteria_1; }
		public String getReportRemarks() { return reportRemarks; }
		public void setReportRemarks(String reportRemarks) { this.reportRemarks = reportRemarks; }
		public String getModificationRemarks() { return modificationRemarks; }
		public void setModificationRemarks(String modificationRemarks) { this.modificationRemarks = modificationRemarks; }
		public String getDataEntryVersion() { return dataEntryVersion; }
		public void setDataEntryVersion(String dataEntryVersion) { this.dataEntryVersion = dataEntryVersion; }
		public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; }
		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) { this.acctBalanceInpula = acctBalanceInpula; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public String getCreateUser() { return createUser; }
		public void setCreateUser(String createUser) { this.createUser = createUser; }
		public Date getCreateTime() { return createTime; }
		public void setCreateTime(Date createTime) { this.createTime = createTime; }
		public String getModifyUser() { return modifyUser; }
		public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }
		public Date getModifyTime() { return modifyTime; }
		public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }
		public String getVerifyUser() { return verifyUser; }
		public void setVerifyUser(String verifyUser) { this.verifyUser = verifyUser; }
		public Date getVerifyTime() { return verifyTime; }
		public void setVerifyTime(Date verifyTime) { this.verifyTime = verifyTime; }
		public char getEntityFlg() { return entityFlg; }
		public void setEntityFlg(char entityFlg) { this.entityFlg = entityFlg; }
		public char getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(char modifyFlg) { this.modifyFlg = modifyFlg; }
		public char getDelFlg() { return delFlg; }
		public void setDelFlg(char delFlg) { this.delFlg = delFlg; }
		public BigDecimal getDebitEquivalent() { return debitEquivalent; }
		public void setDebitEquivalent(BigDecimal debitEquivalent) { this.debitEquivalent = debitEquivalent; }
		public BigDecimal getCreditEquivalent() { return creditEquivalent; }
		public void setCreditEquivalent(BigDecimal creditEquivalent) { this.creditEquivalent = creditEquivalent; }
		public BigDecimal getBalanceAmt() { return balanceAmt; }
		public void setBalanceAmt(BigDecimal balanceAmt) { this.balanceAmt = balanceAmt; }
		public String getGlshCode() { return glshCode; }
		public void setGlshCode(String glshCode) { this.glshCode = glshCode; }
		public String getAcctCrncyCode() { return acctCrncyCode; }
		public void setAcctCrncyCode(String acctCrncyCode) { this.acctCrncyCode = acctCrncyCode; }
		public BigDecimal getMonthlyInt() { return monthlyInt; }
		public void setMonthlyInt(BigDecimal monthlyInt) { this.monthlyInt = monthlyInt; }
	}

	// =========================================================
	// Inner entity: M_SCI_E_Archival_Detail_Entity
	// =========================================================
	public static class M_SCI_E_Archival_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportName;
		private String reportLable;
		private String reportAddlCriteria_1;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;
		private Date reportDate;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private char entityFlg;
		private char modifyFlg;
		private char delFlg;
		private BigDecimal debitEquivalent;
		private BigDecimal creditEquivalent;
		private BigDecimal balanceAmt;
		private String glshCode;
		private String acctCrncyCode;
		private BigDecimal monthlyInt;

		public String getCustId() { return custId; }
		public void setCustId(String custId) { this.custId = custId; }
		public String getAcctNumber() { return acctNumber; }
		public void setAcctNumber(String acctNumber) { this.acctNumber = acctNumber; }
		public String getAcctName() { return acctName; }
		public void setAcctName(String acctName) { this.acctName = acctName; }
		public String getDataType() { return dataType; }
		public void setDataType(String dataType) { this.dataType = dataType; }
		public String getReportName() { return reportName; }
		public void setReportName(String reportName) { this.reportName = reportName; }
		public String getReportLable() { return reportLable; }
		public void setReportLable(String reportLable) { this.reportLable = reportLable; }
		public String getReportAddlCriteria_1() { return reportAddlCriteria_1; }
		public void setReportAddlCriteria_1(String reportAddlCriteria_1) { this.reportAddlCriteria_1 = reportAddlCriteria_1; }
		public String getReportRemarks() { return reportRemarks; }
		public void setReportRemarks(String reportRemarks) { this.reportRemarks = reportRemarks; }
		public String getModificationRemarks() { return modificationRemarks; }
		public void setModificationRemarks(String modificationRemarks) { this.modificationRemarks = modificationRemarks; }
		public String getDataEntryVersion() { return dataEntryVersion; }
		public void setDataEntryVersion(String dataEntryVersion) { this.dataEntryVersion = dataEntryVersion; }
		public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; }
		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) { this.acctBalanceInpula = acctBalanceInpula; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public String getCreateUser() { return createUser; }
		public void setCreateUser(String createUser) { this.createUser = createUser; }
		public Date getCreateTime() { return createTime; }
		public void setCreateTime(Date createTime) { this.createTime = createTime; }
		public String getModifyUser() { return modifyUser; }
		public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }
		public Date getModifyTime() { return modifyTime; }
		public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }
		public String getVerifyUser() { return verifyUser; }
		public void setVerifyUser(String verifyUser) { this.verifyUser = verifyUser; }
		public Date getVerifyTime() { return verifyTime; }
		public void setVerifyTime(Date verifyTime) { this.verifyTime = verifyTime; }
		public char getEntityFlg() { return entityFlg; }
		public void setEntityFlg(char entityFlg) { this.entityFlg = entityFlg; }
		public char getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(char modifyFlg) { this.modifyFlg = modifyFlg; }
		public char getDelFlg() { return delFlg; }
		public void setDelFlg(char delFlg) { this.delFlg = delFlg; }
		public BigDecimal getDebitEquivalent() { return debitEquivalent; }
		public void setDebitEquivalent(BigDecimal debitEquivalent) { this.debitEquivalent = debitEquivalent; }
		public BigDecimal getCreditEquivalent() { return creditEquivalent; }
		public void setCreditEquivalent(BigDecimal creditEquivalent) { this.creditEquivalent = creditEquivalent; }
		public BigDecimal getBalanceAmt() { return balanceAmt; }
		public void setBalanceAmt(BigDecimal balanceAmt) { this.balanceAmt = balanceAmt; }
		public String getGlshCode() { return glshCode; }
		public void setGlshCode(String glshCode) { this.glshCode = glshCode; }
		public String getAcctCrncyCode() { return acctCrncyCode; }
		public void setAcctCrncyCode(String acctCrncyCode) { this.acctCrncyCode = acctCrncyCode; }
		public BigDecimal getMonthlyInt() { return monthlyInt; }
		public void setMonthlyInt(BigDecimal monthlyInt) { this.monthlyInt = monthlyInt; }
	}

	// =========================================================
	// Inner entity: M_SCI_E_RESUB_Detail_Entity
	// =========================================================
	public static class M_SCI_E_RESUB_Detail_Entity {
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportName;
		private String reportLable;
		private String reportAddlCriteria_1;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;
		private Date reportDate;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private char entityFlg;
		private char modifyFlg;
		private char delFlg;
		private BigDecimal debitEquivalent;
		private BigDecimal creditEquivalent;
		private BigDecimal balanceAmt;
		private String glshCode;
		private String acctCrncyCode;
		private BigDecimal monthlyInt;

		public String getCustId() { return custId; }
		public void setCustId(String custId) { this.custId = custId; }
		public String getAcctNumber() { return acctNumber; }
		public void setAcctNumber(String acctNumber) { this.acctNumber = acctNumber; }
		public String getAcctName() { return acctName; }
		public void setAcctName(String acctName) { this.acctName = acctName; }
		public String getDataType() { return dataType; }
		public void setDataType(String dataType) { this.dataType = dataType; }
		public String getReportName() { return reportName; }
		public void setReportName(String reportName) { this.reportName = reportName; }
		public String getReportLable() { return reportLable; }
		public void setReportLable(String reportLable) { this.reportLable = reportLable; }
		public String getReportAddlCriteria_1() { return reportAddlCriteria_1; }
		public void setReportAddlCriteria_1(String reportAddlCriteria_1) { this.reportAddlCriteria_1 = reportAddlCriteria_1; }
		public String getReportRemarks() { return reportRemarks; }
		public void setReportRemarks(String reportRemarks) { this.reportRemarks = reportRemarks; }
		public String getModificationRemarks() { return modificationRemarks; }
		public void setModificationRemarks(String modificationRemarks) { this.modificationRemarks = modificationRemarks; }
		public String getDataEntryVersion() { return dataEntryVersion; }
		public void setDataEntryVersion(String dataEntryVersion) { this.dataEntryVersion = dataEntryVersion; }
		public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; }
		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) { this.acctBalanceInpula = acctBalanceInpula; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public String getCreateUser() { return createUser; }
		public void setCreateUser(String createUser) { this.createUser = createUser; }
		public Date getCreateTime() { return createTime; }
		public void setCreateTime(Date createTime) { this.createTime = createTime; }
		public String getModifyUser() { return modifyUser; }
		public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }
		public Date getModifyTime() { return modifyTime; }
		public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }
		public String getVerifyUser() { return verifyUser; }
		public void setVerifyUser(String verifyUser) { this.verifyUser = verifyUser; }
		public Date getVerifyTime() { return verifyTime; }
		public void setVerifyTime(Date verifyTime) { this.verifyTime = verifyTime; }
		public char getEntityFlg() { return entityFlg; }
		public void setEntityFlg(char entityFlg) { this.entityFlg = entityFlg; }
		public char getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(char modifyFlg) { this.modifyFlg = modifyFlg; }
		public char getDelFlg() { return delFlg; }
		public void setDelFlg(char delFlg) { this.delFlg = delFlg; }
		public BigDecimal getDebitEquivalent() { return debitEquivalent; }
		public void setDebitEquivalent(BigDecimal debitEquivalent) { this.debitEquivalent = debitEquivalent; }
		public BigDecimal getCreditEquivalent() { return creditEquivalent; }
		public void setCreditEquivalent(BigDecimal creditEquivalent) { this.creditEquivalent = creditEquivalent; }
		public BigDecimal getBalanceAmt() { return balanceAmt; }
		public void setBalanceAmt(BigDecimal balanceAmt) { this.balanceAmt = balanceAmt; }
		public String getGlshCode() { return glshCode; }
		public void setGlshCode(String glshCode) { this.glshCode = glshCode; }
		public String getAcctCrncyCode() { return acctCrncyCode; }
		public void setAcctCrncyCode(String acctCrncyCode) { this.acctCrncyCode = acctCrncyCode; }
		public BigDecimal getMonthlyInt() { return monthlyInt; }
		public void setMonthlyInt(BigDecimal monthlyInt) { this.monthlyInt = monthlyInt; }
	}

	class M_SCI_E_SummaryRowMapper implements org.springframework.jdbc.core.RowMapper<M_SCI_E_Summary_Entity> {
		@Override
		public M_SCI_E_Summary_Entity mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			M_SCI_E_Summary_Entity obj = new M_SCI_E_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_product_name(rs.getString("R10_PRODUCT_NAME"));
			obj.setR10_cross_reference(rs.getString("R10_CROSS_REFERENCE"));
			obj.setR10_month(rs.getBigDecimal("R10_MONTH"));
			obj.setR10_ytd(rs.getBigDecimal("R10_YTD"));
			obj.setR11_product_name(rs.getString("R11_PRODUCT_NAME"));
			obj.setR11_cross_reference(rs.getString("R11_CROSS_REFERENCE"));
			obj.setR11_month(rs.getBigDecimal("R11_MONTH"));
			obj.setR11_ytd(rs.getBigDecimal("R11_YTD"));
			obj.setR12_product_name(rs.getString("R12_PRODUCT_NAME"));
			obj.setR12_cross_reference(rs.getString("R12_CROSS_REFERENCE"));
			obj.setR12_month(rs.getBigDecimal("R12_MONTH"));
			obj.setR12_ytd(rs.getBigDecimal("R12_YTD"));
			obj.setR13_product_name(rs.getString("R13_PRODUCT_NAME"));
			obj.setR13_cross_reference(rs.getString("R13_CROSS_REFERENCE"));
			obj.setR13_month(rs.getBigDecimal("R13_MONTH"));
			obj.setR13_ytd(rs.getBigDecimal("R13_YTD"));
			obj.setR14_product_name(rs.getString("R14_PRODUCT_NAME"));
			obj.setR14_cross_reference(rs.getString("R14_CROSS_REFERENCE"));
			obj.setR14_month(rs.getBigDecimal("R14_MONTH"));
			obj.setR14_ytd(rs.getBigDecimal("R14_YTD"));
			obj.setR15_product_name(rs.getString("R15_PRODUCT_NAME"));
			obj.setR15_cross_reference(rs.getString("R15_CROSS_REFERENCE"));
			obj.setR15_month(rs.getBigDecimal("R15_MONTH"));
			obj.setR15_ytd(rs.getBigDecimal("R15_YTD"));
			obj.setR16_product_name(rs.getString("R16_PRODUCT_NAME"));
			obj.setR16_cross_reference(rs.getString("R16_CROSS_REFERENCE"));
			obj.setR16_month(rs.getBigDecimal("R16_MONTH"));
			obj.setR16_ytd(rs.getBigDecimal("R16_YTD"));
			obj.setR17_product_name(rs.getString("R17_PRODUCT_NAME"));
			obj.setR17_cross_reference(rs.getString("R17_CROSS_REFERENCE"));
			obj.setR17_month(rs.getBigDecimal("R17_MONTH"));
			obj.setR17_ytd(rs.getBigDecimal("R17_YTD"));
			obj.setR18_product_name(rs.getString("R18_PRODUCT_NAME"));
			obj.setR18_cross_reference(rs.getString("R18_CROSS_REFERENCE"));
			obj.setR18_month(rs.getBigDecimal("R18_MONTH"));
			obj.setR18_ytd(rs.getBigDecimal("R18_YTD"));
			obj.setR19_product_name(rs.getString("R19_PRODUCT_NAME"));
			obj.setR19_cross_reference(rs.getString("R19_CROSS_REFERENCE"));
			obj.setR19_month(rs.getBigDecimal("R19_MONTH"));
			obj.setR19_ytd(rs.getBigDecimal("R19_YTD"));
			obj.setR20_product_name(rs.getString("R20_PRODUCT_NAME"));
			obj.setR20_cross_reference(rs.getString("R20_CROSS_REFERENCE"));
			obj.setR20_month(rs.getBigDecimal("R20_MONTH"));
			obj.setR20_ytd(rs.getBigDecimal("R20_YTD"));
			obj.setR21_product_name(rs.getString("R21_PRODUCT_NAME"));
			obj.setR21_cross_reference(rs.getString("R21_CROSS_REFERENCE"));
			obj.setR21_month(rs.getBigDecimal("R21_MONTH"));
			obj.setR21_ytd(rs.getBigDecimal("R21_YTD"));
			obj.setR22_product_name(rs.getString("R22_PRODUCT_NAME"));
			obj.setR22_cross_reference(rs.getString("R22_CROSS_REFERENCE"));
			obj.setR22_month(rs.getBigDecimal("R22_MONTH"));
			obj.setR22_ytd(rs.getBigDecimal("R22_YTD"));
			obj.setR23_product_name(rs.getString("R23_PRODUCT_NAME"));
			obj.setR23_cross_reference(rs.getString("R23_CROSS_REFERENCE"));
			obj.setR23_month(rs.getBigDecimal("R23_MONTH"));
			obj.setR23_ytd(rs.getBigDecimal("R23_YTD"));
			obj.setR24_product_name(rs.getString("R24_PRODUCT_NAME"));
			obj.setR24_cross_reference(rs.getString("R24_CROSS_REFERENCE"));
			obj.setR24_month(rs.getBigDecimal("R24_MONTH"));
			obj.setR24_ytd(rs.getBigDecimal("R24_YTD"));
			obj.setR25_product_name(rs.getString("R25_PRODUCT_NAME"));
			obj.setR25_cross_reference(rs.getString("R25_CROSS_REFERENCE"));
			obj.setR25_month(rs.getBigDecimal("R25_MONTH"));
			obj.setR25_ytd(rs.getBigDecimal("R25_YTD"));
			obj.setR26_product_name(rs.getString("R26_PRODUCT_NAME"));
			obj.setR26_cross_reference(rs.getString("R26_CROSS_REFERENCE"));
			obj.setR26_month(rs.getBigDecimal("R26_MONTH"));
			obj.setR26_ytd(rs.getBigDecimal("R26_YTD"));
			obj.setR27_product_name(rs.getString("R27_PRODUCT_NAME"));
			obj.setR27_cross_reference(rs.getString("R27_CROSS_REFERENCE"));
			obj.setR27_month(rs.getBigDecimal("R27_MONTH"));
			obj.setR27_ytd(rs.getBigDecimal("R27_YTD"));
			obj.setR28_product_name(rs.getString("R28_PRODUCT_NAME"));
			obj.setR28_cross_reference(rs.getString("R28_CROSS_REFERENCE"));
			obj.setR28_month(rs.getBigDecimal("R28_MONTH"));
			obj.setR28_ytd(rs.getBigDecimal("R28_YTD"));
			obj.setR29_product_name(rs.getString("R29_PRODUCT_NAME"));
			obj.setR29_cross_reference(rs.getString("R29_CROSS_REFERENCE"));
			obj.setR29_month(rs.getBigDecimal("R29_MONTH"));
			obj.setR29_ytd(rs.getBigDecimal("R29_YTD"));
			obj.setR30_product_name(rs.getString("R30_PRODUCT_NAME"));
			obj.setR30_cross_reference(rs.getString("R30_CROSS_REFERENCE"));
			obj.setR30_month(rs.getBigDecimal("R30_MONTH"));
			obj.setR30_ytd(rs.getBigDecimal("R30_YTD"));
			obj.setR31_product_name(rs.getString("R31_PRODUCT_NAME"));
			obj.setR31_cross_reference(rs.getString("R31_CROSS_REFERENCE"));
			obj.setR31_month(rs.getBigDecimal("R31_MONTH"));
			obj.setR31_ytd(rs.getBigDecimal("R31_YTD"));
			obj.setR32_product_name(rs.getString("R32_PRODUCT_NAME"));
			obj.setR32_cross_reference(rs.getString("R32_CROSS_REFERENCE"));
			obj.setR32_month(rs.getBigDecimal("R32_MONTH"));
			obj.setR32_ytd(rs.getBigDecimal("R32_YTD"));
			obj.setR33_product_name(rs.getString("R33_PRODUCT_NAME"));
			obj.setR33_cross_reference(rs.getString("R33_CROSS_REFERENCE"));
			obj.setR33_month(rs.getBigDecimal("R33_MONTH"));
			obj.setR33_ytd(rs.getBigDecimal("R33_YTD"));
			obj.setR34_product_name(rs.getString("R34_PRODUCT_NAME"));
			obj.setR34_cross_reference(rs.getString("R34_CROSS_REFERENCE"));
			obj.setR34_month(rs.getBigDecimal("R34_MONTH"));
			obj.setR34_ytd(rs.getBigDecimal("R34_YTD"));
			obj.setR35_product_name(rs.getString("R35_PRODUCT_NAME"));
			obj.setR35_cross_reference(rs.getString("R35_CROSS_REFERENCE"));
			obj.setR35_month(rs.getBigDecimal("R35_MONTH"));
			obj.setR35_ytd(rs.getBigDecimal("R35_YTD"));
			obj.setR36_product_name(rs.getString("R36_PRODUCT_NAME"));
			obj.setR36_cross_reference(rs.getString("R36_CROSS_REFERENCE"));
			obj.setR36_month(rs.getBigDecimal("R36_MONTH"));
			obj.setR36_ytd(rs.getBigDecimal("R36_YTD"));
			obj.setR37_product_name(rs.getString("R37_PRODUCT_NAME"));
			obj.setR37_cross_reference(rs.getString("R37_CROSS_REFERENCE"));
			obj.setR37_month(rs.getBigDecimal("R37_MONTH"));
			obj.setR37_ytd(rs.getBigDecimal("R37_YTD"));
			obj.setR38_product_name(rs.getString("R38_PRODUCT_NAME"));
			obj.setR38_cross_reference(rs.getString("R38_CROSS_REFERENCE"));
			obj.setR38_month(rs.getBigDecimal("R38_MONTH"));
			obj.setR38_ytd(rs.getBigDecimal("R38_YTD"));
			obj.setR39_product_name(rs.getString("R39_PRODUCT_NAME"));
			obj.setR39_cross_reference(rs.getString("R39_CROSS_REFERENCE"));
			obj.setR39_month(rs.getBigDecimal("R39_MONTH"));
			obj.setR39_ytd(rs.getBigDecimal("R39_YTD"));
			obj.setR40_product_name(rs.getString("R40_PRODUCT_NAME"));
			obj.setR40_cross_reference(rs.getString("R40_CROSS_REFERENCE"));
			obj.setR40_month(rs.getBigDecimal("R40_MONTH"));
			obj.setR40_ytd(rs.getBigDecimal("R40_YTD"));
			obj.setR41_product_name(rs.getString("R41_PRODUCT_NAME"));
			obj.setR41_cross_reference(rs.getString("R41_CROSS_REFERENCE"));
			obj.setR41_month(rs.getBigDecimal("R41_MONTH"));
			obj.setR41_ytd(rs.getBigDecimal("R41_YTD"));
			obj.setR42_product_name(rs.getString("R42_PRODUCT_NAME"));
			obj.setR42_cross_reference(rs.getString("R42_CROSS_REFERENCE"));
			obj.setR42_month(rs.getBigDecimal("R42_MONTH"));
			obj.setR42_ytd(rs.getBigDecimal("R42_YTD"));
			obj.setR43_product_name(rs.getString("R43_PRODUCT_NAME"));
			obj.setR43_cross_reference(rs.getString("R43_CROSS_REFERENCE"));
			obj.setR43_month(rs.getBigDecimal("R43_MONTH"));
			obj.setR43_ytd(rs.getBigDecimal("R43_YTD"));
			obj.setR44_product_name(rs.getString("R44_PRODUCT_NAME"));
			obj.setR44_cross_reference(rs.getString("R44_CROSS_REFERENCE"));
			obj.setR44_month(rs.getBigDecimal("R44_MONTH"));
			obj.setR44_ytd(rs.getBigDecimal("R44_YTD"));
			obj.setR45_product_name(rs.getString("R45_PRODUCT_NAME"));
			obj.setR45_cross_reference(rs.getString("R45_CROSS_REFERENCE"));
			obj.setR45_month(rs.getBigDecimal("R45_MONTH"));
			obj.setR45_ytd(rs.getBigDecimal("R45_YTD"));
			obj.setR46_product_name(rs.getString("R46_PRODUCT_NAME"));
			obj.setR46_cross_reference(rs.getString("R46_CROSS_REFERENCE"));
			obj.setR46_month(rs.getBigDecimal("R46_MONTH"));
			obj.setR46_ytd(rs.getBigDecimal("R46_YTD"));
			obj.setR47_product_name(rs.getString("R47_PRODUCT_NAME"));
			obj.setR47_cross_reference(rs.getString("R47_CROSS_REFERENCE"));
			obj.setR47_month(rs.getBigDecimal("R47_MONTH"));
			obj.setR47_ytd(rs.getBigDecimal("R47_YTD"));
			obj.setR48_product_name(rs.getString("R48_PRODUCT_NAME"));
			obj.setR48_cross_reference(rs.getString("R48_CROSS_REFERENCE"));
			obj.setR48_month(rs.getBigDecimal("R48_MONTH"));
			obj.setR48_ytd(rs.getBigDecimal("R48_YTD"));
			obj.setR49_product_name(rs.getString("R49_PRODUCT_NAME"));
			obj.setR49_cross_reference(rs.getString("R49_CROSS_REFERENCE"));
			obj.setR49_month(rs.getBigDecimal("R49_MONTH"));
			obj.setR49_ytd(rs.getBigDecimal("R49_YTD"));
			obj.setR50_product_name(rs.getString("R50_PRODUCT_NAME"));
			obj.setR50_cross_reference(rs.getString("R50_CROSS_REFERENCE"));
			obj.setR50_month(rs.getBigDecimal("R50_MONTH"));
			obj.setR50_ytd(rs.getBigDecimal("R50_YTD"));
			obj.setR51_product_name(rs.getString("R51_PRODUCT_NAME"));
			obj.setR51_cross_reference(rs.getString("R51_CROSS_REFERENCE"));
			obj.setR51_month(rs.getBigDecimal("R51_MONTH"));
			obj.setR51_ytd(rs.getBigDecimal("R51_YTD"));
			obj.setR52_product_name(rs.getString("R52_PRODUCT_NAME"));
			obj.setR52_cross_reference(rs.getString("R52_CROSS_REFERENCE"));
			obj.setR52_month(rs.getBigDecimal("R52_MONTH"));
			obj.setR52_ytd(rs.getBigDecimal("R52_YTD"));
			obj.setR53_product_name(rs.getString("R53_PRODUCT_NAME"));
			obj.setR53_cross_reference(rs.getString("R53_CROSS_REFERENCE"));
			obj.setR53_month(rs.getBigDecimal("R53_MONTH"));
			obj.setR53_ytd(rs.getBigDecimal("R53_YTD"));
			obj.setR54_product_name(rs.getString("R54_PRODUCT_NAME"));
			obj.setR54_cross_reference(rs.getString("R54_CROSS_REFERENCE"));
			obj.setR54_month(rs.getBigDecimal("R54_MONTH"));
			obj.setR54_ytd(rs.getBigDecimal("R54_YTD"));
			obj.setR55_product_name(rs.getString("R55_PRODUCT_NAME"));
			obj.setR55_cross_reference(rs.getString("R55_CROSS_REFERENCE"));
			obj.setR55_month(rs.getBigDecimal("R55_MONTH"));
			obj.setR55_ytd(rs.getBigDecimal("R55_YTD"));
			obj.setR56_product_name(rs.getString("R56_PRODUCT_NAME"));
			obj.setR56_cross_reference(rs.getString("R56_CROSS_REFERENCE"));
			obj.setR56_month(rs.getBigDecimal("R56_MONTH"));
			obj.setR56_ytd(rs.getBigDecimal("R56_YTD"));
			obj.setR57_product_name(rs.getString("R57_PRODUCT_NAME"));
			obj.setR57_cross_reference(rs.getString("R57_CROSS_REFERENCE"));
			obj.setR57_month(rs.getBigDecimal("R57_MONTH"));
			obj.setR57_ytd(rs.getBigDecimal("R57_YTD"));
			obj.setR58_product_name(rs.getString("R58_PRODUCT_NAME"));
			obj.setR58_cross_reference(rs.getString("R58_CROSS_REFERENCE"));
			obj.setR58_month(rs.getBigDecimal("R58_MONTH"));
			obj.setR58_ytd(rs.getBigDecimal("R58_YTD"));
			obj.setR59_product_name(rs.getString("R59_PRODUCT_NAME"));
			obj.setR59_cross_reference(rs.getString("R59_CROSS_REFERENCE"));
			obj.setR59_month(rs.getBigDecimal("R59_MONTH"));
			obj.setR59_ytd(rs.getBigDecimal("R59_YTD"));
			obj.setR60_product_name(rs.getString("R60_PRODUCT_NAME"));
			obj.setR60_cross_reference(rs.getString("R60_CROSS_REFERENCE"));
			obj.setR60_month(rs.getBigDecimal("R60_MONTH"));
			obj.setR60_ytd(rs.getBigDecimal("R60_YTD"));
			obj.setR61_product_name(rs.getString("R61_PRODUCT_NAME"));
			obj.setR61_cross_reference(rs.getString("R61_CROSS_REFERENCE"));
			obj.setR61_month(rs.getBigDecimal("R61_MONTH"));
			obj.setR61_ytd(rs.getBigDecimal("R61_YTD"));
			obj.setR62_product_name(rs.getString("R62_PRODUCT_NAME"));
			obj.setR62_cross_reference(rs.getString("R62_CROSS_REFERENCE"));
			obj.setR62_month(rs.getBigDecimal("R62_MONTH"));
			obj.setR62_ytd(rs.getBigDecimal("R62_YTD"));
			obj.setR63_product_name(rs.getString("R63_PRODUCT_NAME"));
			obj.setR63_cross_reference(rs.getString("R63_CROSS_REFERENCE"));
			obj.setR63_month(rs.getBigDecimal("R63_MONTH"));
			obj.setR63_ytd(rs.getBigDecimal("R63_YTD"));
			obj.setR64_product_name(rs.getString("R64_PRODUCT_NAME"));
			obj.setR64_cross_reference(rs.getString("R64_CROSS_REFERENCE"));
			obj.setR64_month(rs.getBigDecimal("R64_MONTH"));
			obj.setR64_ytd(rs.getBigDecimal("R64_YTD"));
			obj.setR65_product_name(rs.getString("R65_PRODUCT_NAME"));
			obj.setR65_cross_reference(rs.getString("R65_CROSS_REFERENCE"));
			obj.setR65_month(rs.getBigDecimal("R65_MONTH"));
			obj.setR65_ytd(rs.getBigDecimal("R65_YTD"));
			obj.setR66_product_name(rs.getString("R66_PRODUCT_NAME"));
			obj.setR66_cross_reference(rs.getString("R66_CROSS_REFERENCE"));
			obj.setR66_month(rs.getBigDecimal("R66_MONTH"));
			obj.setR66_ytd(rs.getBigDecimal("R66_YTD"));
			obj.setR67_product_name(rs.getString("R67_PRODUCT_NAME"));
			obj.setR67_cross_reference(rs.getString("R67_CROSS_REFERENCE"));
			obj.setR67_month(rs.getBigDecimal("R67_MONTH"));
			obj.setR67_ytd(rs.getBigDecimal("R67_YTD"));
			obj.setR68_product_name(rs.getString("R68_PRODUCT_NAME"));
			obj.setR68_cross_reference(rs.getString("R68_CROSS_REFERENCE"));
			obj.setR68_month(rs.getBigDecimal("R68_MONTH"));
			obj.setR68_ytd(rs.getBigDecimal("R68_YTD"));
			obj.setR69_product_name(rs.getString("R69_PRODUCT_NAME"));
			obj.setR69_cross_reference(rs.getString("R69_CROSS_REFERENCE"));
			obj.setR69_month(rs.getBigDecimal("R69_MONTH"));
			obj.setR69_ytd(rs.getBigDecimal("R69_YTD"));
			obj.setR70_product_name(rs.getString("R70_PRODUCT_NAME"));
			obj.setR70_cross_reference(rs.getString("R70_CROSS_REFERENCE"));
			obj.setR70_month(rs.getBigDecimal("R70_MONTH"));
			obj.setR70_ytd(rs.getBigDecimal("R70_YTD"));
			obj.setR71_product_name(rs.getString("R71_PRODUCT_NAME"));
			obj.setR71_cross_reference(rs.getString("R71_CROSS_REFERENCE"));
			obj.setR71_month(rs.getBigDecimal("R71_MONTH"));
			obj.setR71_ytd(rs.getBigDecimal("R71_YTD"));
			obj.setR72_product_name(rs.getString("R72_PRODUCT_NAME"));
			obj.setR72_cross_reference(rs.getString("R72_CROSS_REFERENCE"));
			obj.setR72_month(rs.getBigDecimal("R72_MONTH"));
			obj.setR72_ytd(rs.getBigDecimal("R72_YTD"));
			obj.setR73_product_name(rs.getString("R73_PRODUCT_NAME"));
			obj.setR73_cross_reference(rs.getString("R73_CROSS_REFERENCE"));
			obj.setR73_month(rs.getBigDecimal("R73_MONTH"));
			obj.setR73_ytd(rs.getBigDecimal("R73_YTD"));
			obj.setR74_product_name(rs.getString("R74_PRODUCT_NAME"));
			obj.setR74_cross_reference(rs.getString("R74_CROSS_REFERENCE"));
			obj.setR74_month(rs.getBigDecimal("R74_MONTH"));
			obj.setR74_ytd(rs.getBigDecimal("R74_YTD"));
			obj.setR75_product_name(rs.getString("R75_PRODUCT_NAME"));
			obj.setR75_cross_reference(rs.getString("R75_CROSS_REFERENCE"));
			obj.setR75_month(rs.getBigDecimal("R75_MONTH"));
			obj.setR75_ytd(rs.getBigDecimal("R75_YTD"));
			obj.setR76_product_name(rs.getString("R76_PRODUCT_NAME"));
			obj.setR76_cross_reference(rs.getString("R76_CROSS_REFERENCE"));
			obj.setR76_month(rs.getBigDecimal("R76_MONTH"));
			obj.setR76_ytd(rs.getBigDecimal("R76_YTD"));
			obj.setR77_product_name(rs.getString("R77_PRODUCT_NAME"));
			obj.setR77_cross_reference(rs.getString("R77_CROSS_REFERENCE"));
			obj.setR77_month(rs.getBigDecimal("R77_MONTH"));
			obj.setR77_ytd(rs.getBigDecimal("R77_YTD"));
			obj.setR78_product_name(rs.getString("R78_PRODUCT_NAME"));
			obj.setR78_cross_reference(rs.getString("R78_CROSS_REFERENCE"));
			obj.setR78_month(rs.getBigDecimal("R78_MONTH"));
			obj.setR78_ytd(rs.getBigDecimal("R78_YTD"));
			obj.setR79_product_name(rs.getString("R79_PRODUCT_NAME"));
			obj.setR79_cross_reference(rs.getString("R79_CROSS_REFERENCE"));
			obj.setR79_month(rs.getBigDecimal("R79_MONTH"));
			obj.setR79_ytd(rs.getBigDecimal("R79_YTD"));
			obj.setR80_product_name(rs.getString("R80_PRODUCT_NAME"));
			obj.setR80_cross_reference(rs.getString("R80_CROSS_REFERENCE"));
			obj.setR80_month(rs.getBigDecimal("R80_MONTH"));
			obj.setR80_ytd(rs.getBigDecimal("R80_YTD"));
			obj.setR81_product_name(rs.getString("R81_PRODUCT_NAME"));
			obj.setR81_cross_reference(rs.getString("R81_CROSS_REFERENCE"));
			obj.setR81_month(rs.getBigDecimal("R81_MONTH"));
			obj.setR81_ytd(rs.getBigDecimal("R81_YTD"));
			obj.setR82_product_name(rs.getString("R82_PRODUCT_NAME"));
			obj.setR82_cross_reference(rs.getString("R82_CROSS_REFERENCE"));
			obj.setR82_month(rs.getBigDecimal("R82_MONTH"));
			obj.setR82_ytd(rs.getBigDecimal("R82_YTD"));
			obj.setR83_product_name(rs.getString("R83_PRODUCT_NAME"));
			obj.setR83_cross_reference(rs.getString("R83_CROSS_REFERENCE"));
			obj.setR83_month(rs.getBigDecimal("R83_MONTH"));
			obj.setR83_ytd(rs.getBigDecimal("R83_YTD"));
			obj.setR84_product_name(rs.getString("R84_PRODUCT_NAME"));
			obj.setR84_cross_reference(rs.getString("R84_CROSS_REFERENCE"));
			obj.setR84_month(rs.getBigDecimal("R84_MONTH"));
			obj.setR84_ytd(rs.getBigDecimal("R84_YTD"));
			obj.setR85_product_name(rs.getString("R85_PRODUCT_NAME"));
			obj.setR85_cross_reference(rs.getString("R85_CROSS_REFERENCE"));
			obj.setR85_month(rs.getBigDecimal("R85_MONTH"));
			obj.setR85_ytd(rs.getBigDecimal("R85_YTD"));
			obj.setR86_product_name(rs.getString("R86_PRODUCT_NAME"));
			obj.setR86_cross_reference(rs.getString("R86_CROSS_REFERENCE"));
			obj.setR86_month(rs.getBigDecimal("R86_MONTH"));
			obj.setR86_ytd(rs.getBigDecimal("R86_YTD"));
			return obj;
		}
	}

	class M_SCI_E_Archival_SummaryRowMapper implements org.springframework.jdbc.core.RowMapper<M_SCI_E_Archival_Summary_Entity> {
		@Override
		public M_SCI_E_Archival_Summary_Entity mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			M_SCI_E_Archival_Summary_Entity obj = new M_SCI_E_Archival_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_product_name(rs.getString("R10_PRODUCT_NAME"));
			obj.setR10_cross_reference(rs.getString("R10_CROSS_REFERENCE"));
			obj.setR10_month(rs.getBigDecimal("R10_MONTH"));
			obj.setR10_ytd(rs.getBigDecimal("R10_YTD"));
			obj.setR11_product_name(rs.getString("R11_PRODUCT_NAME"));
			obj.setR11_cross_reference(rs.getString("R11_CROSS_REFERENCE"));
			obj.setR11_month(rs.getBigDecimal("R11_MONTH"));
			obj.setR11_ytd(rs.getBigDecimal("R11_YTD"));
			obj.setR12_product_name(rs.getString("R12_PRODUCT_NAME"));
			obj.setR12_cross_reference(rs.getString("R12_CROSS_REFERENCE"));
			obj.setR12_month(rs.getBigDecimal("R12_MONTH"));
			obj.setR12_ytd(rs.getBigDecimal("R12_YTD"));
			obj.setR13_product_name(rs.getString("R13_PRODUCT_NAME"));
			obj.setR13_cross_reference(rs.getString("R13_CROSS_REFERENCE"));
			obj.setR13_month(rs.getBigDecimal("R13_MONTH"));
			obj.setR13_ytd(rs.getBigDecimal("R13_YTD"));
			obj.setR14_product_name(rs.getString("R14_PRODUCT_NAME"));
			obj.setR14_cross_reference(rs.getString("R14_CROSS_REFERENCE"));
			obj.setR14_month(rs.getBigDecimal("R14_MONTH"));
			obj.setR14_ytd(rs.getBigDecimal("R14_YTD"));
			obj.setR15_product_name(rs.getString("R15_PRODUCT_NAME"));
			obj.setR15_cross_reference(rs.getString("R15_CROSS_REFERENCE"));
			obj.setR15_month(rs.getBigDecimal("R15_MONTH"));
			obj.setR15_ytd(rs.getBigDecimal("R15_YTD"));
			obj.setR16_product_name(rs.getString("R16_PRODUCT_NAME"));
			obj.setR16_cross_reference(rs.getString("R16_CROSS_REFERENCE"));
			obj.setR16_month(rs.getBigDecimal("R16_MONTH"));
			obj.setR16_ytd(rs.getBigDecimal("R16_YTD"));
			obj.setR17_product_name(rs.getString("R17_PRODUCT_NAME"));
			obj.setR17_cross_reference(rs.getString("R17_CROSS_REFERENCE"));
			obj.setR17_month(rs.getBigDecimal("R17_MONTH"));
			obj.setR17_ytd(rs.getBigDecimal("R17_YTD"));
			obj.setR18_product_name(rs.getString("R18_PRODUCT_NAME"));
			obj.setR18_cross_reference(rs.getString("R18_CROSS_REFERENCE"));
			obj.setR18_month(rs.getBigDecimal("R18_MONTH"));
			obj.setR18_ytd(rs.getBigDecimal("R18_YTD"));
			obj.setR19_product_name(rs.getString("R19_PRODUCT_NAME"));
			obj.setR19_cross_reference(rs.getString("R19_CROSS_REFERENCE"));
			obj.setR19_month(rs.getBigDecimal("R19_MONTH"));
			obj.setR19_ytd(rs.getBigDecimal("R19_YTD"));
			obj.setR20_product_name(rs.getString("R20_PRODUCT_NAME"));
			obj.setR20_cross_reference(rs.getString("R20_CROSS_REFERENCE"));
			obj.setR20_month(rs.getBigDecimal("R20_MONTH"));
			obj.setR20_ytd(rs.getBigDecimal("R20_YTD"));
			obj.setR21_product_name(rs.getString("R21_PRODUCT_NAME"));
			obj.setR21_cross_reference(rs.getString("R21_CROSS_REFERENCE"));
			obj.setR21_month(rs.getBigDecimal("R21_MONTH"));
			obj.setR21_ytd(rs.getBigDecimal("R21_YTD"));
			obj.setR22_product_name(rs.getString("R22_PRODUCT_NAME"));
			obj.setR22_cross_reference(rs.getString("R22_CROSS_REFERENCE"));
			obj.setR22_month(rs.getBigDecimal("R22_MONTH"));
			obj.setR22_ytd(rs.getBigDecimal("R22_YTD"));
			obj.setR23_product_name(rs.getString("R23_PRODUCT_NAME"));
			obj.setR23_cross_reference(rs.getString("R23_CROSS_REFERENCE"));
			obj.setR23_month(rs.getBigDecimal("R23_MONTH"));
			obj.setR23_ytd(rs.getBigDecimal("R23_YTD"));
			obj.setR24_product_name(rs.getString("R24_PRODUCT_NAME"));
			obj.setR24_cross_reference(rs.getString("R24_CROSS_REFERENCE"));
			obj.setR24_month(rs.getBigDecimal("R24_MONTH"));
			obj.setR24_ytd(rs.getBigDecimal("R24_YTD"));
			obj.setR25_product_name(rs.getString("R25_PRODUCT_NAME"));
			obj.setR25_cross_reference(rs.getString("R25_CROSS_REFERENCE"));
			obj.setR25_month(rs.getBigDecimal("R25_MONTH"));
			obj.setR25_ytd(rs.getBigDecimal("R25_YTD"));
			obj.setR26_product_name(rs.getString("R26_PRODUCT_NAME"));
			obj.setR26_cross_reference(rs.getString("R26_CROSS_REFERENCE"));
			obj.setR26_month(rs.getBigDecimal("R26_MONTH"));
			obj.setR26_ytd(rs.getBigDecimal("R26_YTD"));
			obj.setR27_product_name(rs.getString("R27_PRODUCT_NAME"));
			obj.setR27_cross_reference(rs.getString("R27_CROSS_REFERENCE"));
			obj.setR27_month(rs.getBigDecimal("R27_MONTH"));
			obj.setR27_ytd(rs.getBigDecimal("R27_YTD"));
			obj.setR28_product_name(rs.getString("R28_PRODUCT_NAME"));
			obj.setR28_cross_reference(rs.getString("R28_CROSS_REFERENCE"));
			obj.setR28_month(rs.getBigDecimal("R28_MONTH"));
			obj.setR28_ytd(rs.getBigDecimal("R28_YTD"));
			obj.setR29_product_name(rs.getString("R29_PRODUCT_NAME"));
			obj.setR29_cross_reference(rs.getString("R29_CROSS_REFERENCE"));
			obj.setR29_month(rs.getBigDecimal("R29_MONTH"));
			obj.setR29_ytd(rs.getBigDecimal("R29_YTD"));
			obj.setR30_product_name(rs.getString("R30_PRODUCT_NAME"));
			obj.setR30_cross_reference(rs.getString("R30_CROSS_REFERENCE"));
			obj.setR30_month(rs.getBigDecimal("R30_MONTH"));
			obj.setR30_ytd(rs.getBigDecimal("R30_YTD"));
			obj.setR31_product_name(rs.getString("R31_PRODUCT_NAME"));
			obj.setR31_cross_reference(rs.getString("R31_CROSS_REFERENCE"));
			obj.setR31_month(rs.getBigDecimal("R31_MONTH"));
			obj.setR31_ytd(rs.getBigDecimal("R31_YTD"));
			obj.setR32_product_name(rs.getString("R32_PRODUCT_NAME"));
			obj.setR32_cross_reference(rs.getString("R32_CROSS_REFERENCE"));
			obj.setR32_month(rs.getBigDecimal("R32_MONTH"));
			obj.setR32_ytd(rs.getBigDecimal("R32_YTD"));
			obj.setR33_product_name(rs.getString("R33_PRODUCT_NAME"));
			obj.setR33_cross_reference(rs.getString("R33_CROSS_REFERENCE"));
			obj.setR33_month(rs.getBigDecimal("R33_MONTH"));
			obj.setR33_ytd(rs.getBigDecimal("R33_YTD"));
			obj.setR34_product_name(rs.getString("R34_PRODUCT_NAME"));
			obj.setR34_cross_reference(rs.getString("R34_CROSS_REFERENCE"));
			obj.setR34_month(rs.getBigDecimal("R34_MONTH"));
			obj.setR34_ytd(rs.getBigDecimal("R34_YTD"));
			obj.setR35_product_name(rs.getString("R35_PRODUCT_NAME"));
			obj.setR35_cross_reference(rs.getString("R35_CROSS_REFERENCE"));
			obj.setR35_month(rs.getBigDecimal("R35_MONTH"));
			obj.setR35_ytd(rs.getBigDecimal("R35_YTD"));
			obj.setR36_product_name(rs.getString("R36_PRODUCT_NAME"));
			obj.setR36_cross_reference(rs.getString("R36_CROSS_REFERENCE"));
			obj.setR36_month(rs.getBigDecimal("R36_MONTH"));
			obj.setR36_ytd(rs.getBigDecimal("R36_YTD"));
			obj.setR37_product_name(rs.getString("R37_PRODUCT_NAME"));
			obj.setR37_cross_reference(rs.getString("R37_CROSS_REFERENCE"));
			obj.setR37_month(rs.getBigDecimal("R37_MONTH"));
			obj.setR37_ytd(rs.getBigDecimal("R37_YTD"));
			obj.setR38_product_name(rs.getString("R38_PRODUCT_NAME"));
			obj.setR38_cross_reference(rs.getString("R38_CROSS_REFERENCE"));
			obj.setR38_month(rs.getBigDecimal("R38_MONTH"));
			obj.setR38_ytd(rs.getBigDecimal("R38_YTD"));
			obj.setR39_product_name(rs.getString("R39_PRODUCT_NAME"));
			obj.setR39_cross_reference(rs.getString("R39_CROSS_REFERENCE"));
			obj.setR39_month(rs.getBigDecimal("R39_MONTH"));
			obj.setR39_ytd(rs.getBigDecimal("R39_YTD"));
			obj.setR40_product_name(rs.getString("R40_PRODUCT_NAME"));
			obj.setR40_cross_reference(rs.getString("R40_CROSS_REFERENCE"));
			obj.setR40_month(rs.getBigDecimal("R40_MONTH"));
			obj.setR40_ytd(rs.getBigDecimal("R40_YTD"));
			obj.setR41_product_name(rs.getString("R41_PRODUCT_NAME"));
			obj.setR41_cross_reference(rs.getString("R41_CROSS_REFERENCE"));
			obj.setR41_month(rs.getBigDecimal("R41_MONTH"));
			obj.setR41_ytd(rs.getBigDecimal("R41_YTD"));
			obj.setR42_product_name(rs.getString("R42_PRODUCT_NAME"));
			obj.setR42_cross_reference(rs.getString("R42_CROSS_REFERENCE"));
			obj.setR42_month(rs.getBigDecimal("R42_MONTH"));
			obj.setR42_ytd(rs.getBigDecimal("R42_YTD"));
			obj.setR43_product_name(rs.getString("R43_PRODUCT_NAME"));
			obj.setR43_cross_reference(rs.getString("R43_CROSS_REFERENCE"));
			obj.setR43_month(rs.getBigDecimal("R43_MONTH"));
			obj.setR43_ytd(rs.getBigDecimal("R43_YTD"));
			obj.setR44_product_name(rs.getString("R44_PRODUCT_NAME"));
			obj.setR44_cross_reference(rs.getString("R44_CROSS_REFERENCE"));
			obj.setR44_month(rs.getBigDecimal("R44_MONTH"));
			obj.setR44_ytd(rs.getBigDecimal("R44_YTD"));
			obj.setR45_product_name(rs.getString("R45_PRODUCT_NAME"));
			obj.setR45_cross_reference(rs.getString("R45_CROSS_REFERENCE"));
			obj.setR45_month(rs.getBigDecimal("R45_MONTH"));
			obj.setR45_ytd(rs.getBigDecimal("R45_YTD"));
			obj.setR46_product_name(rs.getString("R46_PRODUCT_NAME"));
			obj.setR46_cross_reference(rs.getString("R46_CROSS_REFERENCE"));
			obj.setR46_month(rs.getBigDecimal("R46_MONTH"));
			obj.setR46_ytd(rs.getBigDecimal("R46_YTD"));
			obj.setR47_product_name(rs.getString("R47_PRODUCT_NAME"));
			obj.setR47_cross_reference(rs.getString("R47_CROSS_REFERENCE"));
			obj.setR47_month(rs.getBigDecimal("R47_MONTH"));
			obj.setR47_ytd(rs.getBigDecimal("R47_YTD"));
			obj.setR48_product_name(rs.getString("R48_PRODUCT_NAME"));
			obj.setR48_cross_reference(rs.getString("R48_CROSS_REFERENCE"));
			obj.setR48_month(rs.getBigDecimal("R48_MONTH"));
			obj.setR48_ytd(rs.getBigDecimal("R48_YTD"));
			obj.setR49_product_name(rs.getString("R49_PRODUCT_NAME"));
			obj.setR49_cross_reference(rs.getString("R49_CROSS_REFERENCE"));
			obj.setR49_month(rs.getBigDecimal("R49_MONTH"));
			obj.setR49_ytd(rs.getBigDecimal("R49_YTD"));
			obj.setR50_product_name(rs.getString("R50_PRODUCT_NAME"));
			obj.setR50_cross_reference(rs.getString("R50_CROSS_REFERENCE"));
			obj.setR50_month(rs.getBigDecimal("R50_MONTH"));
			obj.setR50_ytd(rs.getBigDecimal("R50_YTD"));
			obj.setR51_product_name(rs.getString("R51_PRODUCT_NAME"));
			obj.setR51_cross_reference(rs.getString("R51_CROSS_REFERENCE"));
			obj.setR51_month(rs.getBigDecimal("R51_MONTH"));
			obj.setR51_ytd(rs.getBigDecimal("R51_YTD"));
			obj.setR52_product_name(rs.getString("R52_PRODUCT_NAME"));
			obj.setR52_cross_reference(rs.getString("R52_CROSS_REFERENCE"));
			obj.setR52_month(rs.getBigDecimal("R52_MONTH"));
			obj.setR52_ytd(rs.getBigDecimal("R52_YTD"));
			obj.setR53_product_name(rs.getString("R53_PRODUCT_NAME"));
			obj.setR53_cross_reference(rs.getString("R53_CROSS_REFERENCE"));
			obj.setR53_month(rs.getBigDecimal("R53_MONTH"));
			obj.setR53_ytd(rs.getBigDecimal("R53_YTD"));
			obj.setR54_product_name(rs.getString("R54_PRODUCT_NAME"));
			obj.setR54_cross_reference(rs.getString("R54_CROSS_REFERENCE"));
			obj.setR54_month(rs.getBigDecimal("R54_MONTH"));
			obj.setR54_ytd(rs.getBigDecimal("R54_YTD"));
			obj.setR55_product_name(rs.getString("R55_PRODUCT_NAME"));
			obj.setR55_cross_reference(rs.getString("R55_CROSS_REFERENCE"));
			obj.setR55_month(rs.getBigDecimal("R55_MONTH"));
			obj.setR55_ytd(rs.getBigDecimal("R55_YTD"));
			obj.setR56_product_name(rs.getString("R56_PRODUCT_NAME"));
			obj.setR56_cross_reference(rs.getString("R56_CROSS_REFERENCE"));
			obj.setR56_month(rs.getBigDecimal("R56_MONTH"));
			obj.setR56_ytd(rs.getBigDecimal("R56_YTD"));
			obj.setR57_product_name(rs.getString("R57_PRODUCT_NAME"));
			obj.setR57_cross_reference(rs.getString("R57_CROSS_REFERENCE"));
			obj.setR57_month(rs.getBigDecimal("R57_MONTH"));
			obj.setR57_ytd(rs.getBigDecimal("R57_YTD"));
			obj.setR58_product_name(rs.getString("R58_PRODUCT_NAME"));
			obj.setR58_cross_reference(rs.getString("R58_CROSS_REFERENCE"));
			obj.setR58_month(rs.getBigDecimal("R58_MONTH"));
			obj.setR58_ytd(rs.getBigDecimal("R58_YTD"));
			obj.setR59_product_name(rs.getString("R59_PRODUCT_NAME"));
			obj.setR59_cross_reference(rs.getString("R59_CROSS_REFERENCE"));
			obj.setR59_month(rs.getBigDecimal("R59_MONTH"));
			obj.setR59_ytd(rs.getBigDecimal("R59_YTD"));
			obj.setR60_product_name(rs.getString("R60_PRODUCT_NAME"));
			obj.setR60_cross_reference(rs.getString("R60_CROSS_REFERENCE"));
			obj.setR60_month(rs.getBigDecimal("R60_MONTH"));
			obj.setR60_ytd(rs.getBigDecimal("R60_YTD"));
			obj.setR61_product_name(rs.getString("R61_PRODUCT_NAME"));
			obj.setR61_cross_reference(rs.getString("R61_CROSS_REFERENCE"));
			obj.setR61_month(rs.getBigDecimal("R61_MONTH"));
			obj.setR61_ytd(rs.getBigDecimal("R61_YTD"));
			obj.setR62_product_name(rs.getString("R62_PRODUCT_NAME"));
			obj.setR62_cross_reference(rs.getString("R62_CROSS_REFERENCE"));
			obj.setR62_month(rs.getBigDecimal("R62_MONTH"));
			obj.setR62_ytd(rs.getBigDecimal("R62_YTD"));
			obj.setR63_product_name(rs.getString("R63_PRODUCT_NAME"));
			obj.setR63_cross_reference(rs.getString("R63_CROSS_REFERENCE"));
			obj.setR63_month(rs.getBigDecimal("R63_MONTH"));
			obj.setR63_ytd(rs.getBigDecimal("R63_YTD"));
			obj.setR64_product_name(rs.getString("R64_PRODUCT_NAME"));
			obj.setR64_cross_reference(rs.getString("R64_CROSS_REFERENCE"));
			obj.setR64_month(rs.getBigDecimal("R64_MONTH"));
			obj.setR64_ytd(rs.getBigDecimal("R64_YTD"));
			obj.setR65_product_name(rs.getString("R65_PRODUCT_NAME"));
			obj.setR65_cross_reference(rs.getString("R65_CROSS_REFERENCE"));
			obj.setR65_month(rs.getBigDecimal("R65_MONTH"));
			obj.setR65_ytd(rs.getBigDecimal("R65_YTD"));
			obj.setR66_product_name(rs.getString("R66_PRODUCT_NAME"));
			obj.setR66_cross_reference(rs.getString("R66_CROSS_REFERENCE"));
			obj.setR66_month(rs.getBigDecimal("R66_MONTH"));
			obj.setR66_ytd(rs.getBigDecimal("R66_YTD"));
			obj.setR67_product_name(rs.getString("R67_PRODUCT_NAME"));
			obj.setR67_cross_reference(rs.getString("R67_CROSS_REFERENCE"));
			obj.setR67_month(rs.getBigDecimal("R67_MONTH"));
			obj.setR67_ytd(rs.getBigDecimal("R67_YTD"));
			obj.setR68_product_name(rs.getString("R68_PRODUCT_NAME"));
			obj.setR68_cross_reference(rs.getString("R68_CROSS_REFERENCE"));
			obj.setR68_month(rs.getBigDecimal("R68_MONTH"));
			obj.setR68_ytd(rs.getBigDecimal("R68_YTD"));
			obj.setR69_product_name(rs.getString("R69_PRODUCT_NAME"));
			obj.setR69_cross_reference(rs.getString("R69_CROSS_REFERENCE"));
			obj.setR69_month(rs.getBigDecimal("R69_MONTH"));
			obj.setR69_ytd(rs.getBigDecimal("R69_YTD"));
			obj.setR70_product_name(rs.getString("R70_PRODUCT_NAME"));
			obj.setR70_cross_reference(rs.getString("R70_CROSS_REFERENCE"));
			obj.setR70_month(rs.getBigDecimal("R70_MONTH"));
			obj.setR70_ytd(rs.getBigDecimal("R70_YTD"));
			obj.setR71_product_name(rs.getString("R71_PRODUCT_NAME"));
			obj.setR71_cross_reference(rs.getString("R71_CROSS_REFERENCE"));
			obj.setR71_month(rs.getBigDecimal("R71_MONTH"));
			obj.setR71_ytd(rs.getBigDecimal("R71_YTD"));
			obj.setR72_product_name(rs.getString("R72_PRODUCT_NAME"));
			obj.setR72_cross_reference(rs.getString("R72_CROSS_REFERENCE"));
			obj.setR72_month(rs.getBigDecimal("R72_MONTH"));
			obj.setR72_ytd(rs.getBigDecimal("R72_YTD"));
			obj.setR73_product_name(rs.getString("R73_PRODUCT_NAME"));
			obj.setR73_cross_reference(rs.getString("R73_CROSS_REFERENCE"));
			obj.setR73_month(rs.getBigDecimal("R73_MONTH"));
			obj.setR73_ytd(rs.getBigDecimal("R73_YTD"));
			obj.setR74_product_name(rs.getString("R74_PRODUCT_NAME"));
			obj.setR74_cross_reference(rs.getString("R74_CROSS_REFERENCE"));
			obj.setR74_month(rs.getBigDecimal("R74_MONTH"));
			obj.setR74_ytd(rs.getBigDecimal("R74_YTD"));
			obj.setR75_product_name(rs.getString("R75_PRODUCT_NAME"));
			obj.setR75_cross_reference(rs.getString("R75_CROSS_REFERENCE"));
			obj.setR75_month(rs.getBigDecimal("R75_MONTH"));
			obj.setR75_ytd(rs.getBigDecimal("R75_YTD"));
			obj.setR76_product_name(rs.getString("R76_PRODUCT_NAME"));
			obj.setR76_cross_reference(rs.getString("R76_CROSS_REFERENCE"));
			obj.setR76_month(rs.getBigDecimal("R76_MONTH"));
			obj.setR76_ytd(rs.getBigDecimal("R76_YTD"));
			obj.setR77_product_name(rs.getString("R77_PRODUCT_NAME"));
			obj.setR77_cross_reference(rs.getString("R77_CROSS_REFERENCE"));
			obj.setR77_month(rs.getBigDecimal("R77_MONTH"));
			obj.setR77_ytd(rs.getBigDecimal("R77_YTD"));
			obj.setR78_product_name(rs.getString("R78_PRODUCT_NAME"));
			obj.setR78_cross_reference(rs.getString("R78_CROSS_REFERENCE"));
			obj.setR78_month(rs.getBigDecimal("R78_MONTH"));
			obj.setR78_ytd(rs.getBigDecimal("R78_YTD"));
			obj.setR79_product_name(rs.getString("R79_PRODUCT_NAME"));
			obj.setR79_cross_reference(rs.getString("R79_CROSS_REFERENCE"));
			obj.setR79_month(rs.getBigDecimal("R79_MONTH"));
			obj.setR79_ytd(rs.getBigDecimal("R79_YTD"));
			obj.setR80_product_name(rs.getString("R80_PRODUCT_NAME"));
			obj.setR80_cross_reference(rs.getString("R80_CROSS_REFERENCE"));
			obj.setR80_month(rs.getBigDecimal("R80_MONTH"));
			obj.setR80_ytd(rs.getBigDecimal("R80_YTD"));
			obj.setR81_product_name(rs.getString("R81_PRODUCT_NAME"));
			obj.setR81_cross_reference(rs.getString("R81_CROSS_REFERENCE"));
			obj.setR81_month(rs.getBigDecimal("R81_MONTH"));
			obj.setR81_ytd(rs.getBigDecimal("R81_YTD"));
			obj.setR82_product_name(rs.getString("R82_PRODUCT_NAME"));
			obj.setR82_cross_reference(rs.getString("R82_CROSS_REFERENCE"));
			obj.setR82_month(rs.getBigDecimal("R82_MONTH"));
			obj.setR82_ytd(rs.getBigDecimal("R82_YTD"));
			obj.setR83_product_name(rs.getString("R83_PRODUCT_NAME"));
			obj.setR83_cross_reference(rs.getString("R83_CROSS_REFERENCE"));
			obj.setR83_month(rs.getBigDecimal("R83_MONTH"));
			obj.setR83_ytd(rs.getBigDecimal("R83_YTD"));
			obj.setR84_product_name(rs.getString("R84_PRODUCT_NAME"));
			obj.setR84_cross_reference(rs.getString("R84_CROSS_REFERENCE"));
			obj.setR84_month(rs.getBigDecimal("R84_MONTH"));
			obj.setR84_ytd(rs.getBigDecimal("R84_YTD"));
			obj.setR85_product_name(rs.getString("R85_PRODUCT_NAME"));
			obj.setR85_cross_reference(rs.getString("R85_CROSS_REFERENCE"));
			obj.setR85_month(rs.getBigDecimal("R85_MONTH"));
			obj.setR85_ytd(rs.getBigDecimal("R85_YTD"));
			obj.setR86_product_name(rs.getString("R86_PRODUCT_NAME"));
			obj.setR86_cross_reference(rs.getString("R86_CROSS_REFERENCE"));
			obj.setR86_month(rs.getBigDecimal("R86_MONTH"));
			obj.setR86_ytd(rs.getBigDecimal("R86_YTD"));
			return obj;
		}
	}

	class M_SCI_E_RESUB_SummaryRowMapper implements org.springframework.jdbc.core.RowMapper<M_SCI_E_RESUB_Summary_Entity> {
		@Override
		public M_SCI_E_RESUB_Summary_Entity mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			M_SCI_E_RESUB_Summary_Entity obj = new M_SCI_E_RESUB_Summary_Entity();
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
			obj.setReport_code(rs.getString("REPORT_CODE"));
			obj.setReport_desc(rs.getString("REPORT_DESC"));
			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setR10_product_name(rs.getString("R10_PRODUCT_NAME"));
			obj.setR10_cross_reference(rs.getString("R10_CROSS_REFERENCE"));
			obj.setR10_month(rs.getBigDecimal("R10_MONTH"));
			obj.setR10_ytd(rs.getBigDecimal("R10_YTD"));
			obj.setR11_product_name(rs.getString("R11_PRODUCT_NAME"));
			obj.setR11_cross_reference(rs.getString("R11_CROSS_REFERENCE"));
			obj.setR11_month(rs.getBigDecimal("R11_MONTH"));
			obj.setR11_ytd(rs.getBigDecimal("R11_YTD"));
			obj.setR12_product_name(rs.getString("R12_PRODUCT_NAME"));
			obj.setR12_cross_reference(rs.getString("R12_CROSS_REFERENCE"));
			obj.setR12_month(rs.getBigDecimal("R12_MONTH"));
			obj.setR12_ytd(rs.getBigDecimal("R12_YTD"));
			obj.setR13_product_name(rs.getString("R13_PRODUCT_NAME"));
			obj.setR13_cross_reference(rs.getString("R13_CROSS_REFERENCE"));
			obj.setR13_month(rs.getBigDecimal("R13_MONTH"));
			obj.setR13_ytd(rs.getBigDecimal("R13_YTD"));
			obj.setR14_product_name(rs.getString("R14_PRODUCT_NAME"));
			obj.setR14_cross_reference(rs.getString("R14_CROSS_REFERENCE"));
			obj.setR14_month(rs.getBigDecimal("R14_MONTH"));
			obj.setR14_ytd(rs.getBigDecimal("R14_YTD"));
			obj.setR15_product_name(rs.getString("R15_PRODUCT_NAME"));
			obj.setR15_cross_reference(rs.getString("R15_CROSS_REFERENCE"));
			obj.setR15_month(rs.getBigDecimal("R15_MONTH"));
			obj.setR15_ytd(rs.getBigDecimal("R15_YTD"));
			obj.setR16_product_name(rs.getString("R16_PRODUCT_NAME"));
			obj.setR16_cross_reference(rs.getString("R16_CROSS_REFERENCE"));
			obj.setR16_month(rs.getBigDecimal("R16_MONTH"));
			obj.setR16_ytd(rs.getBigDecimal("R16_YTD"));
			obj.setR17_product_name(rs.getString("R17_PRODUCT_NAME"));
			obj.setR17_cross_reference(rs.getString("R17_CROSS_REFERENCE"));
			obj.setR17_month(rs.getBigDecimal("R17_MONTH"));
			obj.setR17_ytd(rs.getBigDecimal("R17_YTD"));
			obj.setR18_product_name(rs.getString("R18_PRODUCT_NAME"));
			obj.setR18_cross_reference(rs.getString("R18_CROSS_REFERENCE"));
			obj.setR18_month(rs.getBigDecimal("R18_MONTH"));
			obj.setR18_ytd(rs.getBigDecimal("R18_YTD"));
			obj.setR19_product_name(rs.getString("R19_PRODUCT_NAME"));
			obj.setR19_cross_reference(rs.getString("R19_CROSS_REFERENCE"));
			obj.setR19_month(rs.getBigDecimal("R19_MONTH"));
			obj.setR19_ytd(rs.getBigDecimal("R19_YTD"));
			obj.setR20_product_name(rs.getString("R20_PRODUCT_NAME"));
			obj.setR20_cross_reference(rs.getString("R20_CROSS_REFERENCE"));
			obj.setR20_month(rs.getBigDecimal("R20_MONTH"));
			obj.setR20_ytd(rs.getBigDecimal("R20_YTD"));
			obj.setR21_product_name(rs.getString("R21_PRODUCT_NAME"));
			obj.setR21_cross_reference(rs.getString("R21_CROSS_REFERENCE"));
			obj.setR21_month(rs.getBigDecimal("R21_MONTH"));
			obj.setR21_ytd(rs.getBigDecimal("R21_YTD"));
			obj.setR22_product_name(rs.getString("R22_PRODUCT_NAME"));
			obj.setR22_cross_reference(rs.getString("R22_CROSS_REFERENCE"));
			obj.setR22_month(rs.getBigDecimal("R22_MONTH"));
			obj.setR22_ytd(rs.getBigDecimal("R22_YTD"));
			obj.setR23_product_name(rs.getString("R23_PRODUCT_NAME"));
			obj.setR23_cross_reference(rs.getString("R23_CROSS_REFERENCE"));
			obj.setR23_month(rs.getBigDecimal("R23_MONTH"));
			obj.setR23_ytd(rs.getBigDecimal("R23_YTD"));
			obj.setR24_product_name(rs.getString("R24_PRODUCT_NAME"));
			obj.setR24_cross_reference(rs.getString("R24_CROSS_REFERENCE"));
			obj.setR24_month(rs.getBigDecimal("R24_MONTH"));
			obj.setR24_ytd(rs.getBigDecimal("R24_YTD"));
			obj.setR25_product_name(rs.getString("R25_PRODUCT_NAME"));
			obj.setR25_cross_reference(rs.getString("R25_CROSS_REFERENCE"));
			obj.setR25_month(rs.getBigDecimal("R25_MONTH"));
			obj.setR25_ytd(rs.getBigDecimal("R25_YTD"));
			obj.setR26_product_name(rs.getString("R26_PRODUCT_NAME"));
			obj.setR26_cross_reference(rs.getString("R26_CROSS_REFERENCE"));
			obj.setR26_month(rs.getBigDecimal("R26_MONTH"));
			obj.setR26_ytd(rs.getBigDecimal("R26_YTD"));
			obj.setR27_product_name(rs.getString("R27_PRODUCT_NAME"));
			obj.setR27_cross_reference(rs.getString("R27_CROSS_REFERENCE"));
			obj.setR27_month(rs.getBigDecimal("R27_MONTH"));
			obj.setR27_ytd(rs.getBigDecimal("R27_YTD"));
			obj.setR28_product_name(rs.getString("R28_PRODUCT_NAME"));
			obj.setR28_cross_reference(rs.getString("R28_CROSS_REFERENCE"));
			obj.setR28_month(rs.getBigDecimal("R28_MONTH"));
			obj.setR28_ytd(rs.getBigDecimal("R28_YTD"));
			obj.setR29_product_name(rs.getString("R29_PRODUCT_NAME"));
			obj.setR29_cross_reference(rs.getString("R29_CROSS_REFERENCE"));
			obj.setR29_month(rs.getBigDecimal("R29_MONTH"));
			obj.setR29_ytd(rs.getBigDecimal("R29_YTD"));
			obj.setR30_product_name(rs.getString("R30_PRODUCT_NAME"));
			obj.setR30_cross_reference(rs.getString("R30_CROSS_REFERENCE"));
			obj.setR30_month(rs.getBigDecimal("R30_MONTH"));
			obj.setR30_ytd(rs.getBigDecimal("R30_YTD"));
			obj.setR31_product_name(rs.getString("R31_PRODUCT_NAME"));
			obj.setR31_cross_reference(rs.getString("R31_CROSS_REFERENCE"));
			obj.setR31_month(rs.getBigDecimal("R31_MONTH"));
			obj.setR31_ytd(rs.getBigDecimal("R31_YTD"));
			obj.setR32_product_name(rs.getString("R32_PRODUCT_NAME"));
			obj.setR32_cross_reference(rs.getString("R32_CROSS_REFERENCE"));
			obj.setR32_month(rs.getBigDecimal("R32_MONTH"));
			obj.setR32_ytd(rs.getBigDecimal("R32_YTD"));
			obj.setR33_product_name(rs.getString("R33_PRODUCT_NAME"));
			obj.setR33_cross_reference(rs.getString("R33_CROSS_REFERENCE"));
			obj.setR33_month(rs.getBigDecimal("R33_MONTH"));
			obj.setR33_ytd(rs.getBigDecimal("R33_YTD"));
			obj.setR34_product_name(rs.getString("R34_PRODUCT_NAME"));
			obj.setR34_cross_reference(rs.getString("R34_CROSS_REFERENCE"));
			obj.setR34_month(rs.getBigDecimal("R34_MONTH"));
			obj.setR34_ytd(rs.getBigDecimal("R34_YTD"));
			obj.setR35_product_name(rs.getString("R35_PRODUCT_NAME"));
			obj.setR35_cross_reference(rs.getString("R35_CROSS_REFERENCE"));
			obj.setR35_month(rs.getBigDecimal("R35_MONTH"));
			obj.setR35_ytd(rs.getBigDecimal("R35_YTD"));
			obj.setR36_product_name(rs.getString("R36_PRODUCT_NAME"));
			obj.setR36_cross_reference(rs.getString("R36_CROSS_REFERENCE"));
			obj.setR36_month(rs.getBigDecimal("R36_MONTH"));
			obj.setR36_ytd(rs.getBigDecimal("R36_YTD"));
			obj.setR37_product_name(rs.getString("R37_PRODUCT_NAME"));
			obj.setR37_cross_reference(rs.getString("R37_CROSS_REFERENCE"));
			obj.setR37_month(rs.getBigDecimal("R37_MONTH"));
			obj.setR37_ytd(rs.getBigDecimal("R37_YTD"));
			obj.setR38_product_name(rs.getString("R38_PRODUCT_NAME"));
			obj.setR38_cross_reference(rs.getString("R38_CROSS_REFERENCE"));
			obj.setR38_month(rs.getBigDecimal("R38_MONTH"));
			obj.setR38_ytd(rs.getBigDecimal("R38_YTD"));
			obj.setR39_product_name(rs.getString("R39_PRODUCT_NAME"));
			obj.setR39_cross_reference(rs.getString("R39_CROSS_REFERENCE"));
			obj.setR39_month(rs.getBigDecimal("R39_MONTH"));
			obj.setR39_ytd(rs.getBigDecimal("R39_YTD"));
			obj.setR40_product_name(rs.getString("R40_PRODUCT_NAME"));
			obj.setR40_cross_reference(rs.getString("R40_CROSS_REFERENCE"));
			obj.setR40_month(rs.getBigDecimal("R40_MONTH"));
			obj.setR40_ytd(rs.getBigDecimal("R40_YTD"));
			obj.setR41_product_name(rs.getString("R41_PRODUCT_NAME"));
			obj.setR41_cross_reference(rs.getString("R41_CROSS_REFERENCE"));
			obj.setR41_month(rs.getBigDecimal("R41_MONTH"));
			obj.setR41_ytd(rs.getBigDecimal("R41_YTD"));
			obj.setR42_product_name(rs.getString("R42_PRODUCT_NAME"));
			obj.setR42_cross_reference(rs.getString("R42_CROSS_REFERENCE"));
			obj.setR42_month(rs.getBigDecimal("R42_MONTH"));
			obj.setR42_ytd(rs.getBigDecimal("R42_YTD"));
			obj.setR43_product_name(rs.getString("R43_PRODUCT_NAME"));
			obj.setR43_cross_reference(rs.getString("R43_CROSS_REFERENCE"));
			obj.setR43_month(rs.getBigDecimal("R43_MONTH"));
			obj.setR43_ytd(rs.getBigDecimal("R43_YTD"));
			obj.setR44_product_name(rs.getString("R44_PRODUCT_NAME"));
			obj.setR44_cross_reference(rs.getString("R44_CROSS_REFERENCE"));
			obj.setR44_month(rs.getBigDecimal("R44_MONTH"));
			obj.setR44_ytd(rs.getBigDecimal("R44_YTD"));
			obj.setR45_product_name(rs.getString("R45_PRODUCT_NAME"));
			obj.setR45_cross_reference(rs.getString("R45_CROSS_REFERENCE"));
			obj.setR45_month(rs.getBigDecimal("R45_MONTH"));
			obj.setR45_ytd(rs.getBigDecimal("R45_YTD"));
			obj.setR46_product_name(rs.getString("R46_PRODUCT_NAME"));
			obj.setR46_cross_reference(rs.getString("R46_CROSS_REFERENCE"));
			obj.setR46_month(rs.getBigDecimal("R46_MONTH"));
			obj.setR46_ytd(rs.getBigDecimal("R46_YTD"));
			obj.setR47_product_name(rs.getString("R47_PRODUCT_NAME"));
			obj.setR47_cross_reference(rs.getString("R47_CROSS_REFERENCE"));
			obj.setR47_month(rs.getBigDecimal("R47_MONTH"));
			obj.setR47_ytd(rs.getBigDecimal("R47_YTD"));
			obj.setR48_product_name(rs.getString("R48_PRODUCT_NAME"));
			obj.setR48_cross_reference(rs.getString("R48_CROSS_REFERENCE"));
			obj.setR48_month(rs.getBigDecimal("R48_MONTH"));
			obj.setR48_ytd(rs.getBigDecimal("R48_YTD"));
			obj.setR49_product_name(rs.getString("R49_PRODUCT_NAME"));
			obj.setR49_cross_reference(rs.getString("R49_CROSS_REFERENCE"));
			obj.setR49_month(rs.getBigDecimal("R49_MONTH"));
			obj.setR49_ytd(rs.getBigDecimal("R49_YTD"));
			obj.setR50_product_name(rs.getString("R50_PRODUCT_NAME"));
			obj.setR50_cross_reference(rs.getString("R50_CROSS_REFERENCE"));
			obj.setR50_month(rs.getBigDecimal("R50_MONTH"));
			obj.setR50_ytd(rs.getBigDecimal("R50_YTD"));
			obj.setR51_product_name(rs.getString("R51_PRODUCT_NAME"));
			obj.setR51_cross_reference(rs.getString("R51_CROSS_REFERENCE"));
			obj.setR51_month(rs.getBigDecimal("R51_MONTH"));
			obj.setR51_ytd(rs.getBigDecimal("R51_YTD"));
			obj.setR52_product_name(rs.getString("R52_PRODUCT_NAME"));
			obj.setR52_cross_reference(rs.getString("R52_CROSS_REFERENCE"));
			obj.setR52_month(rs.getBigDecimal("R52_MONTH"));
			obj.setR52_ytd(rs.getBigDecimal("R52_YTD"));
			obj.setR53_product_name(rs.getString("R53_PRODUCT_NAME"));
			obj.setR53_cross_reference(rs.getString("R53_CROSS_REFERENCE"));
			obj.setR53_month(rs.getBigDecimal("R53_MONTH"));
			obj.setR53_ytd(rs.getBigDecimal("R53_YTD"));
			obj.setR54_product_name(rs.getString("R54_PRODUCT_NAME"));
			obj.setR54_cross_reference(rs.getString("R54_CROSS_REFERENCE"));
			obj.setR54_month(rs.getBigDecimal("R54_MONTH"));
			obj.setR54_ytd(rs.getBigDecimal("R54_YTD"));
			obj.setR55_product_name(rs.getString("R55_PRODUCT_NAME"));
			obj.setR55_cross_reference(rs.getString("R55_CROSS_REFERENCE"));
			obj.setR55_month(rs.getBigDecimal("R55_MONTH"));
			obj.setR55_ytd(rs.getBigDecimal("R55_YTD"));
			obj.setR56_product_name(rs.getString("R56_PRODUCT_NAME"));
			obj.setR56_cross_reference(rs.getString("R56_CROSS_REFERENCE"));
			obj.setR56_month(rs.getBigDecimal("R56_MONTH"));
			obj.setR56_ytd(rs.getBigDecimal("R56_YTD"));
			obj.setR57_product_name(rs.getString("R57_PRODUCT_NAME"));
			obj.setR57_cross_reference(rs.getString("R57_CROSS_REFERENCE"));
			obj.setR57_month(rs.getBigDecimal("R57_MONTH"));
			obj.setR57_ytd(rs.getBigDecimal("R57_YTD"));
			obj.setR58_product_name(rs.getString("R58_PRODUCT_NAME"));
			obj.setR58_cross_reference(rs.getString("R58_CROSS_REFERENCE"));
			obj.setR58_month(rs.getBigDecimal("R58_MONTH"));
			obj.setR58_ytd(rs.getBigDecimal("R58_YTD"));
			obj.setR59_product_name(rs.getString("R59_PRODUCT_NAME"));
			obj.setR59_cross_reference(rs.getString("R59_CROSS_REFERENCE"));
			obj.setR59_month(rs.getBigDecimal("R59_MONTH"));
			obj.setR59_ytd(rs.getBigDecimal("R59_YTD"));
			obj.setR60_product_name(rs.getString("R60_PRODUCT_NAME"));
			obj.setR60_cross_reference(rs.getString("R60_CROSS_REFERENCE"));
			obj.setR60_month(rs.getBigDecimal("R60_MONTH"));
			obj.setR60_ytd(rs.getBigDecimal("R60_YTD"));
			obj.setR61_product_name(rs.getString("R61_PRODUCT_NAME"));
			obj.setR61_cross_reference(rs.getString("R61_CROSS_REFERENCE"));
			obj.setR61_month(rs.getBigDecimal("R61_MONTH"));
			obj.setR61_ytd(rs.getBigDecimal("R61_YTD"));
			obj.setR62_product_name(rs.getString("R62_PRODUCT_NAME"));
			obj.setR62_cross_reference(rs.getString("R62_CROSS_REFERENCE"));
			obj.setR62_month(rs.getBigDecimal("R62_MONTH"));
			obj.setR62_ytd(rs.getBigDecimal("R62_YTD"));
			obj.setR63_product_name(rs.getString("R63_PRODUCT_NAME"));
			obj.setR63_cross_reference(rs.getString("R63_CROSS_REFERENCE"));
			obj.setR63_month(rs.getBigDecimal("R63_MONTH"));
			obj.setR63_ytd(rs.getBigDecimal("R63_YTD"));
			obj.setR64_product_name(rs.getString("R64_PRODUCT_NAME"));
			obj.setR64_cross_reference(rs.getString("R64_CROSS_REFERENCE"));
			obj.setR64_month(rs.getBigDecimal("R64_MONTH"));
			obj.setR64_ytd(rs.getBigDecimal("R64_YTD"));
			obj.setR65_product_name(rs.getString("R65_PRODUCT_NAME"));
			obj.setR65_cross_reference(rs.getString("R65_CROSS_REFERENCE"));
			obj.setR65_month(rs.getBigDecimal("R65_MONTH"));
			obj.setR65_ytd(rs.getBigDecimal("R65_YTD"));
			obj.setR66_product_name(rs.getString("R66_PRODUCT_NAME"));
			obj.setR66_cross_reference(rs.getString("R66_CROSS_REFERENCE"));
			obj.setR66_month(rs.getBigDecimal("R66_MONTH"));
			obj.setR66_ytd(rs.getBigDecimal("R66_YTD"));
			obj.setR67_product_name(rs.getString("R67_PRODUCT_NAME"));
			obj.setR67_cross_reference(rs.getString("R67_CROSS_REFERENCE"));
			obj.setR67_month(rs.getBigDecimal("R67_MONTH"));
			obj.setR67_ytd(rs.getBigDecimal("R67_YTD"));
			obj.setR68_product_name(rs.getString("R68_PRODUCT_NAME"));
			obj.setR68_cross_reference(rs.getString("R68_CROSS_REFERENCE"));
			obj.setR68_month(rs.getBigDecimal("R68_MONTH"));
			obj.setR68_ytd(rs.getBigDecimal("R68_YTD"));
			obj.setR69_product_name(rs.getString("R69_PRODUCT_NAME"));
			obj.setR69_cross_reference(rs.getString("R69_CROSS_REFERENCE"));
			obj.setR69_month(rs.getBigDecimal("R69_MONTH"));
			obj.setR69_ytd(rs.getBigDecimal("R69_YTD"));
			obj.setR70_product_name(rs.getString("R70_PRODUCT_NAME"));
			obj.setR70_cross_reference(rs.getString("R70_CROSS_REFERENCE"));
			obj.setR70_month(rs.getBigDecimal("R70_MONTH"));
			obj.setR70_ytd(rs.getBigDecimal("R70_YTD"));
			obj.setR71_product_name(rs.getString("R71_PRODUCT_NAME"));
			obj.setR71_cross_reference(rs.getString("R71_CROSS_REFERENCE"));
			obj.setR71_month(rs.getBigDecimal("R71_MONTH"));
			obj.setR71_ytd(rs.getBigDecimal("R71_YTD"));
			obj.setR72_product_name(rs.getString("R72_PRODUCT_NAME"));
			obj.setR72_cross_reference(rs.getString("R72_CROSS_REFERENCE"));
			obj.setR72_month(rs.getBigDecimal("R72_MONTH"));
			obj.setR72_ytd(rs.getBigDecimal("R72_YTD"));
			obj.setR73_product_name(rs.getString("R73_PRODUCT_NAME"));
			obj.setR73_cross_reference(rs.getString("R73_CROSS_REFERENCE"));
			obj.setR73_month(rs.getBigDecimal("R73_MONTH"));
			obj.setR73_ytd(rs.getBigDecimal("R73_YTD"));
			obj.setR74_product_name(rs.getString("R74_PRODUCT_NAME"));
			obj.setR74_cross_reference(rs.getString("R74_CROSS_REFERENCE"));
			obj.setR74_month(rs.getBigDecimal("R74_MONTH"));
			obj.setR74_ytd(rs.getBigDecimal("R74_YTD"));
			obj.setR75_product_name(rs.getString("R75_PRODUCT_NAME"));
			obj.setR75_cross_reference(rs.getString("R75_CROSS_REFERENCE"));
			obj.setR75_month(rs.getBigDecimal("R75_MONTH"));
			obj.setR75_ytd(rs.getBigDecimal("R75_YTD"));
			obj.setR76_product_name(rs.getString("R76_PRODUCT_NAME"));
			obj.setR76_cross_reference(rs.getString("R76_CROSS_REFERENCE"));
			obj.setR76_month(rs.getBigDecimal("R76_MONTH"));
			obj.setR76_ytd(rs.getBigDecimal("R76_YTD"));
			obj.setR77_product_name(rs.getString("R77_PRODUCT_NAME"));
			obj.setR77_cross_reference(rs.getString("R77_CROSS_REFERENCE"));
			obj.setR77_month(rs.getBigDecimal("R77_MONTH"));
			obj.setR77_ytd(rs.getBigDecimal("R77_YTD"));
			obj.setR78_product_name(rs.getString("R78_PRODUCT_NAME"));
			obj.setR78_cross_reference(rs.getString("R78_CROSS_REFERENCE"));
			obj.setR78_month(rs.getBigDecimal("R78_MONTH"));
			obj.setR78_ytd(rs.getBigDecimal("R78_YTD"));
			obj.setR79_product_name(rs.getString("R79_PRODUCT_NAME"));
			obj.setR79_cross_reference(rs.getString("R79_CROSS_REFERENCE"));
			obj.setR79_month(rs.getBigDecimal("R79_MONTH"));
			obj.setR79_ytd(rs.getBigDecimal("R79_YTD"));
			obj.setR80_product_name(rs.getString("R80_PRODUCT_NAME"));
			obj.setR80_cross_reference(rs.getString("R80_CROSS_REFERENCE"));
			obj.setR80_month(rs.getBigDecimal("R80_MONTH"));
			obj.setR80_ytd(rs.getBigDecimal("R80_YTD"));
			obj.setR81_product_name(rs.getString("R81_PRODUCT_NAME"));
			obj.setR81_cross_reference(rs.getString("R81_CROSS_REFERENCE"));
			obj.setR81_month(rs.getBigDecimal("R81_MONTH"));
			obj.setR81_ytd(rs.getBigDecimal("R81_YTD"));
			obj.setR82_product_name(rs.getString("R82_PRODUCT_NAME"));
			obj.setR82_cross_reference(rs.getString("R82_CROSS_REFERENCE"));
			obj.setR82_month(rs.getBigDecimal("R82_MONTH"));
			obj.setR82_ytd(rs.getBigDecimal("R82_YTD"));
			obj.setR83_product_name(rs.getString("R83_PRODUCT_NAME"));
			obj.setR83_cross_reference(rs.getString("R83_CROSS_REFERENCE"));
			obj.setR83_month(rs.getBigDecimal("R83_MONTH"));
			obj.setR83_ytd(rs.getBigDecimal("R83_YTD"));
			obj.setR84_product_name(rs.getString("R84_PRODUCT_NAME"));
			obj.setR84_cross_reference(rs.getString("R84_CROSS_REFERENCE"));
			obj.setR84_month(rs.getBigDecimal("R84_MONTH"));
			obj.setR84_ytd(rs.getBigDecimal("R84_YTD"));
			obj.setR85_product_name(rs.getString("R85_PRODUCT_NAME"));
			obj.setR85_cross_reference(rs.getString("R85_CROSS_REFERENCE"));
			obj.setR85_month(rs.getBigDecimal("R85_MONTH"));
			obj.setR85_ytd(rs.getBigDecimal("R85_YTD"));
			obj.setR86_product_name(rs.getString("R86_PRODUCT_NAME"));
			obj.setR86_cross_reference(rs.getString("R86_CROSS_REFERENCE"));
			obj.setR86_month(rs.getBigDecimal("R86_MONTH"));
			obj.setR86_ytd(rs.getBigDecimal("R86_YTD"));
			return obj;
		}
	}

	class M_SCI_E_DetailRowMapper implements org.springframework.jdbc.core.RowMapper<M_SCI_E_Detail_Entity> {
		@Override
		public M_SCI_E_Detail_Entity mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			M_SCI_E_Detail_Entity obj = new M_SCI_E_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLable(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			String entityFlgStr = rs.getString("ENTITY_FLG"); if (entityFlgStr != null && entityFlgStr.length() > 0) obj.setEntityFlg(entityFlgStr.charAt(0));
			String modifyFlgStr = rs.getString("MODIFY_FLG"); if (modifyFlgStr != null && modifyFlgStr.length() > 0) obj.setModifyFlg(modifyFlgStr.charAt(0));
			String delFlgStr = rs.getString("DEL_FLG"); if (delFlgStr != null && delFlgStr.length() > 0) obj.setDelFlg(delFlgStr.charAt(0));
			obj.setDebitEquivalent(rs.getBigDecimal("DEBIT_EQUIVALENT"));
			obj.setCreditEquivalent(rs.getBigDecimal("CREDIT_EQUIVALENT"));
			obj.setBalanceAmt(rs.getBigDecimal("BALANCE_AMT"));
			obj.setGlshCode(rs.getString("GLSH_CODE"));
			obj.setAcctCrncyCode(rs.getString("ACCT_CRNCY_CODE"));
			obj.setMonthlyInt(rs.getBigDecimal("MONTHLY_INT"));
			return obj;
		}
	}

	class M_SCI_E_Archival_DetailRowMapper implements org.springframework.jdbc.core.RowMapper<M_SCI_E_Archival_Detail_Entity> {
		@Override
		public M_SCI_E_Archival_Detail_Entity mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			M_SCI_E_Archival_Detail_Entity obj = new M_SCI_E_Archival_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLable(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			String entityFlgStr = rs.getString("ENTITY_FLG"); if (entityFlgStr != null && entityFlgStr.length() > 0) obj.setEntityFlg(entityFlgStr.charAt(0));
			String modifyFlgStr = rs.getString("MODIFY_FLG"); if (modifyFlgStr != null && modifyFlgStr.length() > 0) obj.setModifyFlg(modifyFlgStr.charAt(0));
			String delFlgStr = rs.getString("DEL_FLG"); if (delFlgStr != null && delFlgStr.length() > 0) obj.setDelFlg(delFlgStr.charAt(0));
			obj.setDebitEquivalent(rs.getBigDecimal("DEBIT_EQUIVALENT"));
			obj.setCreditEquivalent(rs.getBigDecimal("CREDIT_EQUIVALENT"));
			obj.setBalanceAmt(rs.getBigDecimal("BALANCE_AMT"));
			obj.setGlshCode(rs.getString("GLSH_CODE"));
			obj.setAcctCrncyCode(rs.getString("ACCT_CRNCY_CODE"));
			obj.setMonthlyInt(rs.getBigDecimal("MONTHLY_INT"));
			return obj;
		}
	}

	class M_SCI_E_RESUB_DetailRowMapper implements org.springframework.jdbc.core.RowMapper<M_SCI_E_RESUB_Detail_Entity> {
		@Override
		public M_SCI_E_RESUB_Detail_Entity mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
			M_SCI_E_RESUB_Detail_Entity obj = new M_SCI_E_RESUB_Detail_Entity();
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLable(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));
			String entityFlgStr = rs.getString("ENTITY_FLG"); if (entityFlgStr != null && entityFlgStr.length() > 0) obj.setEntityFlg(entityFlgStr.charAt(0));
			String modifyFlgStr = rs.getString("MODIFY_FLG"); if (modifyFlgStr != null && modifyFlgStr.length() > 0) obj.setModifyFlg(modifyFlgStr.charAt(0));
			String delFlgStr = rs.getString("DEL_FLG"); if (delFlgStr != null && delFlgStr.length() > 0) obj.setDelFlg(delFlgStr.charAt(0));
			obj.setDebitEquivalent(rs.getBigDecimal("DEBIT_EQUIVALENT"));
			obj.setCreditEquivalent(rs.getBigDecimal("CREDIT_EQUIVALENT"));
			obj.setBalanceAmt(rs.getBigDecimal("BALANCE_AMT"));
			obj.setGlshCode(rs.getString("GLSH_CODE"));
			obj.setAcctCrncyCode(rs.getString("ACCT_CRNCY_CODE"));
			obj.setMonthlyInt(rs.getBigDecimal("MONTHLY_INT"));
			return obj;
		}
	}

}