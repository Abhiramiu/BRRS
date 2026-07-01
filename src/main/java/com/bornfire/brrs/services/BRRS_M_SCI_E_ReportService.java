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

import com.bornfire.brrs.entities.M_SCI_E_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SCI_E_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SCI_E_Detail_Entity;
import com.bornfire.brrs.entities.M_SCI_E_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_SCI_E_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_SCI_E_Summary_Entity;
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