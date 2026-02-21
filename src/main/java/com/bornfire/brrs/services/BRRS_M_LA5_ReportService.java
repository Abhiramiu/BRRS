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
import javax.transaction.Transactional;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_LA5_Summary_Repo;
import com.bornfire.brrs.entities.M_LA4_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_LA5_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA5_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA5_Archival_Summary_Repo;
import com.bornfire.brrs.entities.M_LA5_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA5_Summary_Entity;
import com.bornfire.brrs.entities.M_LA5_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_LA5_Detail_Entity;

@Component
@Service

public class BRRS_M_LA5_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA5_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_LA5_Summary_Repo BRRS_M_LA5_Summary_Repo;

	@Autowired
	BRRS_M_LA5_Detail_Repo BRRS_M_LA5_Detail_Repo;

	@Autowired
	BRRS_M_LA5_Archival_Detail_Repo BRRS_M_LA5_Archival_Detail_Repo;

	@Autowired
	BRRS_M_LA5_Archival_Summary_Repo BRRS_M_LA5_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LA5View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null & version != null) {
			List<M_LA5_Archival_Summary_Entity> T1Master = new ArrayList<M_LA5_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				T1Master = BRRS_M_LA5_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		} else {

			List<M_LA5_Summary_Entity> T1Master = new ArrayList<M_LA5_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				T1Master = BRRS_M_LA5_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/M_LA5");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		
		return mv;

	}

	public ModelAndView getM_LA5currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String rowId = null;
			String columnId = null;

			// ✅ Split the filter string here
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}

			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<M_LA5_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_LA5_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
				} else {
					T1Dt1 = BRRS_M_LA5_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
					System.out.println(T1Dt1.size());
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_LA5_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BRRS_M_LA5_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BRRS_M_LA5_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
					totalPages = BRRS_M_LA5_Detail_Repo.getdatacount(parsedDate);
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

		// ✅ Common attributes
		mv.setViewName("BRRS/M_LA5");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_LA5 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
							}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LA5Details");

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
// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL", "REPORT ADDL CRITERIA 1",
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
			List<M_LA5_Archival_Detail_Entity> reportData = BRRS_M_LA5_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA5_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());
// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");
// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_LA5 — only header will be written.");
			}
// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
			} catch (Exception e) {
			logger.error("Error generating M_LA5 Excel", e);
			return new byte[0];
		}
	}


	public byte[] BRRS_M_LA5DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_LA5 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LA5Details");

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
// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL", "REPORT ADDL CRITERIA 1",
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
			List<M_LA5_Detail_Entity> reportData = BRRS_M_LA5_Detail_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA5_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());
// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");
// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_LA5 — only header will be written.");
			}
// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating M_LA5 Excel", e);
			return new byte[0];
		}
	}

	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_LA5"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	        M_LA5_Detail_Entity la1Entity = BRRS_M_LA5_Detail_Repo.findByAcctnumber(acctNo);
	        if (la1Entity != null && la1Entity.getReport_date() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReport_date());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", la1Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}
	
	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_LA5"); // ✅ match the report name

	    if (acctNo != null) {
	        M_LA5_Detail_Entity la1Entity = BRRS_M_LA5_Detail_Repo.findByAcctnumber(acctNo);
	        if (la1Entity != null && la1Entity.getReport_date() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReport_date());
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
	        String acctNo = request.getParameter("acct_number");
	        String provisionStr = request.getParameter("acct_balance_in_pula");
	        String acctName = request.getParameter("acct_name");
	        String reportDateStr = request.getParameter("report_date");

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        M_LA5_Detail_Entity existing = BRRS_M_LA5_Detail_Repo.findByAcctnumber(acctNo);
	        if (existing == null) {
	            logger.warn("No record found for ACCT_NO: {}", acctNo);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
	        }

	        boolean isChanged = false;

	        if (acctName != null && !acctName.isEmpty()) {
	            if (existing.getAcct_name() == null || !existing.getAcct_name().equals(acctName)) {
	                existing.setAcct_name(acctName);
	                isChanged = true;
	                logger.info("Account name updated to {}", acctName);
	            }
	        }

	        if (provisionStr != null && !provisionStr.isEmpty()) {
	            BigDecimal newProvision = new BigDecimal(provisionStr);
	            if (existing.getAcct_balance_in_pula() == null ||
	                existing.getAcct_balance_in_pula().compareTo(newProvision) != 0) {
	                existing.setAcct_balance_in_pula(newProvision);
	                isChanged = true;
	                logger.info("ACCOUNT BALANCE updated to {}", newProvision);
	            }
	        }
	        
	        

	        if (isChanged) {
	        	BRRS_M_LA5_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_M_LA5_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_M_LA5_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating M_LA5 record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
//Gopika

	public List<Object[]> getM_LA5Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_LA5_Archival_Summary_Entity> latestArchivalList = BRRS_M_LA5_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_LA5_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_LA5 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

//	// Archival View
	// Archival View
		public List<Object[]> getM_LA5Archival() {
			List<Object[]> archivalList = new ArrayList<>();

			try {
				List<M_LA5_Archival_Summary_Entity> repoData = BRRS_M_LA5_Archival_Summary_Repo
						.getdatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (M_LA5_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
								entity.getReportResubDate() };
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					M_LA5_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReportVersion());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching M_LA5 Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}

//	public List<Object> getM_LA5Archival() {
//		List<Object> LA5Archivallist = new ArrayList<>();
//		try {
//			LA5Archivallist = BRRS_M_LA5_Archival_Summary_Repo.getM_LA5archival();
//			System.out.println("countser" + LA5Archivallist.size());
//		} catch (Exception e) {
//			// Log the exception
//			System.err.println("Error fetching M_SFINP2 Archival data: " + e.getMessage());
//			e.printStackTrace();
//		}
//		return LA5Archivallist;
//	}
//	

	
	// Normal format Excel

	public byte[] BRRS_M_LA5Excel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
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
				return getExcelM_LA5ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
						format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
//		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_LA5ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
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
				return BRRS_M_LA5EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				List<M_LA5_Summary_Entity> dataList = BRRS_M_LA5_Summary_Repo
						.getdatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_LA5 report. Returning empty result.");
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
							M_LA5_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

					// row6
					Cell cell = row.getCell(1);
					if (record.getR6_usd() != null) {
						cell.setCellValue(record.getR6_usd().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(2);
					if (record.getR6_zar() != null) {
						cell.setCellValue(record.getR6_zar().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(3);
					if (record.getR6_gbp() != null) {
						cell.setCellValue(record.getR6_gbp().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(4);
					if (record.getR6_euro() != null) {
						cell.setCellValue(record.getR6_euro().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(5);
					if (record.getR6_yen() != null) {
						cell.setCellValue(record.getR6_yen().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(6);
					if (record.getR6_c6() != null) {
						cell.setCellValue(record.getR6_c6().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(7);
					if (record.getR6_c7() != null) {
						cell.setCellValue(record.getR6_c7().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(8);
					if (record.getR6_c8() != null) {
						cell.setCellValue(record.getR6_c8().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(9);
					if (record.getR6_total() != null) {
						cell.setCellValue(record.getR6_total().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

				
					// row7
					row = sheet.getRow(6);
					 cell = row.getCell(1);
					if (record.getR7_usd() != null) {
					    cell.setCellValue(record.getR7_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR7_zar() != null) {
					    cell.setCellValue(record.getR7_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR7_gbp() != null) {
					    cell.setCellValue(record.getR7_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR7_euro() != null) {
					    cell.setCellValue(record.getR7_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR7_yen() != null) {
					    cell.setCellValue(record.getR7_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR7_c6() != null) {
					    cell.setCellValue(record.getR7_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR7_c7() != null) {
					    cell.setCellValue(record.getR7_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR7_c8() != null) {
					    cell.setCellValue(record.getR7_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR7_total() != null) {
					    cell.setCellValue(record.getR7_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row8
					row = sheet.getRow(7);
					cell = row.getCell(1);
					if (record.getR8_usd() != null) {
					    cell.setCellValue(record.getR8_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR8_zar() != null) {
					    cell.setCellValue(record.getR8_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR8_gbp() != null) {
					    cell.setCellValue(record.getR8_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR8_euro() != null) {
					    cell.setCellValue(record.getR8_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR8_yen() != null) {
					    cell.setCellValue(record.getR8_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR8_c6() != null) {
					    cell.setCellValue(record.getR8_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR8_c7() != null) {
					    cell.setCellValue(record.getR8_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR8_c8() != null) {
					    cell.setCellValue(record.getR8_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR8_total() != null) {
					    cell.setCellValue(record.getR8_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row9
					row = sheet.getRow(8);

					cell = row.getCell(1);
					if (record.getR9_usd() != null) {
					    cell.setCellValue(record.getR9_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR9_zar() != null) {
					    cell.setCellValue(record.getR9_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR9_gbp() != null) {
					    cell.setCellValue(record.getR9_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR9_euro() != null) {
					    cell.setCellValue(record.getR9_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR9_yen() != null) {
					    cell.setCellValue(record.getR9_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR9_c6() != null) {
					    cell.setCellValue(record.getR9_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR9_c7() != null) {
					    cell.setCellValue(record.getR9_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR9_c8() != null) {
					    cell.setCellValue(record.getR9_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR9_total() != null) {
					    cell.setCellValue(record.getR9_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row10
					row = sheet.getRow(9);

					cell = row.getCell(1);
					if (record.getR10_usd() != null) {
					    cell.setCellValue(record.getR10_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR10_zar() != null) {
					    cell.setCellValue(record.getR10_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR10_gbp() != null) {
					    cell.setCellValue(record.getR10_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR10_euro() != null) {
					    cell.setCellValue(record.getR10_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR10_yen() != null) {
					    cell.setCellValue(record.getR10_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR10_c6() != null) {
					    cell.setCellValue(record.getR10_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR10_c7() != null) {
					    cell.setCellValue(record.getR10_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR10_c8() != null) {
					    cell.setCellValue(record.getR10_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR10_total() != null) {
					    cell.setCellValue(record.getR10_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					
					// row11
					row = sheet.getRow(10);

					cell = row.getCell(1);
					if (record.getR11_usd() != null) {
					    cell.setCellValue(record.getR11_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR11_zar() != null) {
					    cell.setCellValue(record.getR11_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR11_gbp() != null) {
					    cell.setCellValue(record.getR11_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR11_euro() != null) {
					    cell.setCellValue(record.getR11_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR11_yen() != null) {
					    cell.setCellValue(record.getR11_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR11_c6() != null) {
					    cell.setCellValue(record.getR11_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR11_c7() != null) {
					    cell.setCellValue(record.getR11_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR11_c8() != null) {
					    cell.setCellValue(record.getR11_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR11_total() != null) {
					    cell.setCellValue(record.getR11_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell = row.getCell(1);
					if (record.getR12_usd() != null) {
					    cell.setCellValue(record.getR12_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR12_zar() != null) {
					    cell.setCellValue(record.getR12_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR12_gbp() != null) {
					    cell.setCellValue(record.getR12_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR12_euro() != null) {
					    cell.setCellValue(record.getR12_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR12_yen() != null) {
					    cell.setCellValue(record.getR12_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR12_c6() != null) {
					    cell.setCellValue(record.getR12_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR12_c7() != null) {
					    cell.setCellValue(record.getR12_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR12_c8() != null) {
					    cell.setCellValue(record.getR12_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR12_total() != null) {
					    cell.setCellValue(record.getR12_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell = row.getCell(1);
					if (record.getR13_usd() != null) {
					    cell.setCellValue(record.getR13_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR13_zar() != null) {
					    cell.setCellValue(record.getR13_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR13_gbp() != null) {
					    cell.setCellValue(record.getR13_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR13_euro() != null) {
					    cell.setCellValue(record.getR13_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR13_yen() != null) {
					    cell.setCellValue(record.getR13_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR13_c6() != null) {
					    cell.setCellValue(record.getR13_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR13_c7() != null) {
					    cell.setCellValue(record.getR13_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR13_c8() != null) {
					    cell.setCellValue(record.getR13_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR13_total() != null) {
					    cell.setCellValue(record.getR13_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell = row.getCell(1);
					if (record.getR14_usd() != null) {
					    cell.setCellValue(record.getR14_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR14_zar() != null) {
					    cell.setCellValue(record.getR14_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR14_gbp() != null) {
					    cell.setCellValue(record.getR14_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR14_euro() != null) {
					    cell.setCellValue(record.getR14_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR14_yen() != null) {
					    cell.setCellValue(record.getR14_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR14_c6() != null) {
					    cell.setCellValue(record.getR14_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR14_c7() != null) {
					    cell.setCellValue(record.getR14_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR14_c8() != null) {
					    cell.setCellValue(record.getR14_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR14_total() != null) {
					    cell.setCellValue(record.getR14_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row15
					row = sheet.getRow(14);

					cell = row.getCell(1);
					if (record.getR15_usd() != null) {
					    cell.setCellValue(record.getR15_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR15_zar() != null) {
					    cell.setCellValue(record.getR15_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR15_gbp() != null) {
					    cell.setCellValue(record.getR15_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR15_euro() != null) {
					    cell.setCellValue(record.getR15_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR15_yen() != null) {
					    cell.setCellValue(record.getR15_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR15_c6() != null) {
					    cell.setCellValue(record.getR15_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR15_c7() != null) {
					    cell.setCellValue(record.getR15_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR15_c8() != null) {
					    cell.setCellValue(record.getR15_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR15_total() != null) {
					    cell.setCellValue(record.getR15_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell = row.getCell(1);
					if (record.getR16_usd() != null) {
					    cell.setCellValue(record.getR16_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR16_zar() != null) {
					    cell.setCellValue(record.getR16_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR16_gbp() != null) {
					    cell.setCellValue(record.getR16_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR16_euro() != null) {
					    cell.setCellValue(record.getR16_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR16_yen() != null) {
					    cell.setCellValue(record.getR16_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR16_c6() != null) {
					    cell.setCellValue(record.getR16_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR16_c7() != null) {
					    cell.setCellValue(record.getR16_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR16_c8() != null) {
					    cell.setCellValue(record.getR16_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR16_total() != null) {
					    cell.setCellValue(record.getR16_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row17
					row = sheet.getRow(16);

					cell = row.getCell(1);
					if (record.getR17_usd() != null) {
					    cell.setCellValue(record.getR17_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR17_zar() != null) {
					    cell.setCellValue(record.getR17_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR17_gbp() != null) {
					    cell.setCellValue(record.getR17_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR17_euro() != null) {
					    cell.setCellValue(record.getR17_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR17_yen() != null) {
					    cell.setCellValue(record.getR17_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR17_c6() != null) {
					    cell.setCellValue(record.getR17_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR17_c7() != null) {
					    cell.setCellValue(record.getR17_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR17_c8() != null) {
					    cell.setCellValue(record.getR17_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR17_total() != null) {
					    cell.setCellValue(record.getR17_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell = row.getCell(1);
					if (record.getR18_usd() != null) {
					    cell.setCellValue(record.getR18_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR18_zar() != null) {
					    cell.setCellValue(record.getR18_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR18_gbp() != null) {
					    cell.setCellValue(record.getR18_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR18_euro() != null) {
					    cell.setCellValue(record.getR18_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR18_yen() != null) {
					    cell.setCellValue(record.getR18_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR18_c6() != null) {
					    cell.setCellValue(record.getR18_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR18_c7() != null) {
					    cell.setCellValue(record.getR18_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR18_c8() != null) {
					    cell.setCellValue(record.getR18_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR18_total() != null) {
					    cell.setCellValue(record.getR18_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row19
					row = sheet.getRow(18);

					cell = row.getCell(1);
					if (record.getR19_usd() != null) {
					    cell.setCellValue(record.getR19_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR19_zar() != null) {
					    cell.setCellValue(record.getR19_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR19_gbp() != null) {
					    cell.setCellValue(record.getR19_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR19_euro() != null) {
					    cell.setCellValue(record.getR19_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR19_yen() != null) {
					    cell.setCellValue(record.getR19_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR19_c6() != null) {
					    cell.setCellValue(record.getR19_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR19_c7() != null) {
					    cell.setCellValue(record.getR19_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR19_c8() != null) {
					    cell.setCellValue(record.getR19_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR19_total() != null) {
					    cell.setCellValue(record.getR19_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell = row.getCell(1);
					if (record.getR20_usd() != null) {
					    cell.setCellValue(record.getR20_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR20_zar() != null) {
					    cell.setCellValue(record.getR20_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR20_gbp() != null) {
					    cell.setCellValue(record.getR20_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR20_euro() != null) {
					    cell.setCellValue(record.getR20_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR20_yen() != null) {
					    cell.setCellValue(record.getR20_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR20_c6() != null) {
					    cell.setCellValue(record.getR20_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR20_c7() != null) {
					    cell.setCellValue(record.getR20_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR20_c8() != null) {
					    cell.setCellValue(record.getR20_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR20_total() != null) {
					    cell.setCellValue(record.getR20_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row21
					row = sheet.getRow(20);

					cell = row.getCell(1);
					if (record.getR21_usd() != null) {
					    cell.setCellValue(record.getR21_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR21_zar() != null) {
					    cell.setCellValue(record.getR21_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR21_gbp() != null) {
					    cell.setCellValue(record.getR21_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR21_euro() != null) {
					    cell.setCellValue(record.getR21_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR21_yen() != null) {
					    cell.setCellValue(record.getR21_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR21_c6() != null) {
					    cell.setCellValue(record.getR21_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR21_c7() != null) {
					    cell.setCellValue(record.getR21_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR21_c8() != null) {
					    cell.setCellValue(record.getR21_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR21_total() != null) {
					    cell.setCellValue(record.getR21_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					cell = row.getCell(1);
					if (record.getR22_usd() != null) {
					    cell.setCellValue(record.getR22_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR22_zar() != null) {
					    cell.setCellValue(record.getR22_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR22_gbp() != null) {
					    cell.setCellValue(record.getR22_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR22_euro() != null) {
					    cell.setCellValue(record.getR22_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR22_yen() != null) {
					    cell.setCellValue(record.getR22_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR22_c6() != null) {
					    cell.setCellValue(record.getR22_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR22_c7() != null) {
					    cell.setCellValue(record.getR22_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR22_c8() != null) {
					    cell.setCellValue(record.getR22_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR22_total() != null) {
					    cell.setCellValue(record.getR22_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row23
					row = sheet.getRow(22);

					cell = row.getCell(1);
					if (record.getR23_usd() != null) {
					    cell.setCellValue(record.getR23_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR23_zar() != null) {
					    cell.setCellValue(record.getR23_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR23_gbp() != null) {
					    cell.setCellValue(record.getR23_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR23_euro() != null) {
					    cell.setCellValue(record.getR23_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR23_yen() != null) {
					    cell.setCellValue(record.getR23_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR23_c6() != null) {
					    cell.setCellValue(record.getR23_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR23_c7() != null) {
					    cell.setCellValue(record.getR23_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR23_c8() != null) {
					    cell.setCellValue(record.getR23_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR23_total() != null) {
					    cell.setCellValue(record.getR23_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					cell = row.getCell(1);
					if (record.getR24_usd() != null) {
					    cell.setCellValue(record.getR24_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR24_zar() != null) {
					    cell.setCellValue(record.getR24_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR24_gbp() != null) {
					    cell.setCellValue(record.getR24_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR24_euro() != null) {
					    cell.setCellValue(record.getR24_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR24_yen() != null) {
					    cell.setCellValue(record.getR24_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR24_c6() != null) {
					    cell.setCellValue(record.getR24_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR24_c7() != null) {
					    cell.setCellValue(record.getR24_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR24_c8() != null) {
					    cell.setCellValue(record.getR24_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR24_total() != null) {
					    cell.setCellValue(record.getR24_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell = row.getCell(1);
					if (record.getR25_usd() != null) {
					    cell.setCellValue(record.getR25_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR25_zar() != null) {
					    cell.setCellValue(record.getR25_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR25_gbp() != null) {
					    cell.setCellValue(record.getR25_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR25_euro() != null) {
					    cell.setCellValue(record.getR25_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR25_yen() != null) {
					    cell.setCellValue(record.getR25_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR25_c6() != null) {
					    cell.setCellValue(record.getR25_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR25_c7() != null) {
					    cell.setCellValue(record.getR25_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR25_c8() != null) {
					    cell.setCellValue(record.getR25_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR25_total() != null) {
					    cell.setCellValue(record.getR25_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell = row.getCell(1);
					if (record.getR26_usd() != null) {
					    cell.setCellValue(record.getR26_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR26_zar() != null) {
					    cell.setCellValue(record.getR26_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR26_gbp() != null) {
					    cell.setCellValue(record.getR26_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR26_euro() != null) {
					    cell.setCellValue(record.getR26_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR26_yen() != null) {
					    cell.setCellValue(record.getR26_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR26_c6() != null) {
					    cell.setCellValue(record.getR26_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR26_c7() != null) {
					    cell.setCellValue(record.getR26_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR26_c8() != null) {
					    cell.setCellValue(record.getR26_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR26_total() != null) {
					    cell.setCellValue(record.getR26_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell = row.getCell(1);
					if (record.getR27_usd() != null) {
					    cell.setCellValue(record.getR27_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR27_zar() != null) {
					    cell.setCellValue(record.getR27_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR27_gbp() != null) {
					    cell.setCellValue(record.getR27_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR27_euro() != null) {
					    cell.setCellValue(record.getR27_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR27_yen() != null) {
					    cell.setCellValue(record.getR27_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR27_c6() != null) {
					    cell.setCellValue(record.getR27_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR27_c7() != null) {
					    cell.setCellValue(record.getR27_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR27_c8() != null) {
					    cell.setCellValue(record.getR27_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR27_total() != null) {
					    cell.setCellValue(record.getR27_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					
					// row28
					row = sheet.getRow(27);

					cell = row.getCell(1);
					if (record.getR28_usd() != null) {
					    cell.setCellValue(record.getR28_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR28_zar() != null) {
					    cell.setCellValue(record.getR28_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR28_gbp() != null) {
					    cell.setCellValue(record.getR28_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR28_euro() != null) {
					    cell.setCellValue(record.getR28_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR28_yen() != null) {
					    cell.setCellValue(record.getR28_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR28_c6() != null) {
					    cell.setCellValue(record.getR28_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR28_c7() != null) {
					    cell.setCellValue(record.getR28_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR28_c8() != null) {
					    cell.setCellValue(record.getR28_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR28_total() != null) {
					    cell.setCellValue(record.getR28_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell = row.getCell(1);
					if (record.getR29_usd() != null) {
					    cell.setCellValue(record.getR29_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR29_zar() != null) {
					    cell.setCellValue(record.getR29_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR29_gbp() != null) {
					    cell.setCellValue(record.getR29_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR29_euro() != null) {
					    cell.setCellValue(record.getR29_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR29_yen() != null) {
					    cell.setCellValue(record.getR29_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR29_c6() != null) {
					    cell.setCellValue(record.getR29_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR29_c7() != null) {
					    cell.setCellValue(record.getR29_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR29_c8() != null) {
					    cell.setCellValue(record.getR29_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR29_total() != null) {
					    cell.setCellValue(record.getR29_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell = row.getCell(1);
					if (record.getR30_usd() != null) {
					    cell.setCellValue(record.getR30_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR30_zar() != null) {
					    cell.setCellValue(record.getR30_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR30_gbp() != null) {
					    cell.setCellValue(record.getR30_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR30_euro() != null) {
					    cell.setCellValue(record.getR30_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR30_yen() != null) {
					    cell.setCellValue(record.getR30_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR30_c6() != null) {
					    cell.setCellValue(record.getR30_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR30_c7() != null) {
					    cell.setCellValue(record.getR30_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR30_c8() != null) {
					    cell.setCellValue(record.getR30_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR30_total() != null) {
					    cell.setCellValue(record.getR30_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					
					// row31
					row = sheet.getRow(30);

					cell = row.getCell(1);
					if (record.getR31_usd() != null) {
					    cell.setCellValue(record.getR31_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR31_zar() != null) {
					    cell.setCellValue(record.getR31_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR31_gbp() != null) {
					    cell.setCellValue(record.getR31_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR31_euro() != null) {
					    cell.setCellValue(record.getR31_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR31_yen() != null) {
					    cell.setCellValue(record.getR31_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR31_c6() != null) {
					    cell.setCellValue(record.getR31_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR31_c7() != null) {
					    cell.setCellValue(record.getR31_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR31_c8() != null) {
					    cell.setCellValue(record.getR31_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR31_total() != null) {
					    cell.setCellValue(record.getR31_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell = row.getCell(1);
					if (record.getR32_usd() != null) {
					    cell.setCellValue(record.getR32_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR32_zar() != null) {
					    cell.setCellValue(record.getR32_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR32_gbp() != null) {
					    cell.setCellValue(record.getR32_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR32_euro() != null) {
					    cell.setCellValue(record.getR32_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR32_yen() != null) {
					    cell.setCellValue(record.getR32_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR32_c6() != null) {
					    cell.setCellValue(record.getR32_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR32_c7() != null) {
					    cell.setCellValue(record.getR32_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR32_c8() != null) {
					    cell.setCellValue(record.getR32_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR32_total() != null) {
					    cell.setCellValue(record.getR32_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell = row.getCell(1);
					if (record.getR33_usd() != null) {
					    cell.setCellValue(record.getR33_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR33_zar() != null) {
					    cell.setCellValue(record.getR33_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR33_gbp() != null) {
					    cell.setCellValue(record.getR33_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR33_euro() != null) {
					    cell.setCellValue(record.getR33_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR33_yen() != null) {
					    cell.setCellValue(record.getR33_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR33_c6() != null) {
					    cell.setCellValue(record.getR33_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR33_c7() != null) {
					    cell.setCellValue(record.getR33_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR33_c8() != null) {
					    cell.setCellValue(record.getR33_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR33_total() != null) {
					    cell.setCellValue(record.getR33_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row34
					row = sheet.getRow(33);

					cell = row.getCell(1);
					if (record.getR34_usd() != null) {
					    cell.setCellValue(record.getR34_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR34_zar() != null) {
					    cell.setCellValue(record.getR34_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR34_gbp() != null) {
					    cell.setCellValue(record.getR34_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR34_euro() != null) {
					    cell.setCellValue(record.getR34_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR34_yen() != null) {
					    cell.setCellValue(record.getR34_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR34_c6() != null) {
					    cell.setCellValue(record.getR34_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR34_c7() != null) {
					    cell.setCellValue(record.getR34_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR34_c8() != null) {
					    cell.setCellValue(record.getR34_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR34_total() != null) {
					    cell.setCellValue(record.getR34_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell = row.getCell(1);
					if (record.getR35_usd() != null) {
					    cell.setCellValue(record.getR35_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR35_zar() != null) {
					    cell.setCellValue(record.getR35_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR35_gbp() != null) {
					    cell.setCellValue(record.getR35_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR35_euro() != null) {
					    cell.setCellValue(record.getR35_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR35_yen() != null) {
					    cell.setCellValue(record.getR35_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR35_c6() != null) {
					    cell.setCellValue(record.getR35_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR35_c7() != null) {
					    cell.setCellValue(record.getR35_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR35_c8() != null) {
					    cell.setCellValue(record.getR35_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR35_total() != null) {
					    cell.setCellValue(record.getR35_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell = row.getCell(1);
					if (record.getR36_usd() != null) {
					    cell.setCellValue(record.getR36_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR36_zar() != null) {
					    cell.setCellValue(record.getR36_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR36_gbp() != null) {
					    cell.setCellValue(record.getR36_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR36_euro() != null) {
					    cell.setCellValue(record.getR36_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR36_yen() != null) {
					    cell.setCellValue(record.getR36_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR36_c6() != null) {
					    cell.setCellValue(record.getR36_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR36_c7() != null) {
					    cell.setCellValue(record.getR36_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR36_c8() != null) {
					    cell.setCellValue(record.getR36_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR36_total() != null) {
					    cell.setCellValue(record.getR36_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row37
					row = sheet.getRow(36);

					cell = row.getCell(1);
					if (record.getR37_usd() != null) {
					    cell.setCellValue(record.getR37_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR37_zar() != null) {
					    cell.setCellValue(record.getR37_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR37_gbp() != null) {
					    cell.setCellValue(record.getR37_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR37_euro() != null) {
					    cell.setCellValue(record.getR37_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR37_yen() != null) {
					    cell.setCellValue(record.getR37_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR37_c6() != null) {
					    cell.setCellValue(record.getR37_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR37_c7() != null) {
					    cell.setCellValue(record.getR37_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR37_c8() != null) {
					    cell.setCellValue(record.getR37_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR37_total() != null) {
					    cell.setCellValue(record.getR37_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);

					cell = row.getCell(1);
					if (record.getR38_usd() != null) {
					    cell.setCellValue(record.getR38_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR38_zar() != null) {
					    cell.setCellValue(record.getR38_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR38_gbp() != null) {
					    cell.setCellValue(record.getR38_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR38_euro() != null) {
					    cell.setCellValue(record.getR38_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR38_yen() != null) {
					    cell.setCellValue(record.getR38_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR38_c6() != null) {
					    cell.setCellValue(record.getR38_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR38_c7() != null) {
					    cell.setCellValue(record.getR38_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR38_c8() != null) {
					    cell.setCellValue(record.getR38_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR38_total() != null) {
					    cell.setCellValue(record.getR38_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					cell = row.getCell(1);
					if (record.getR39_usd() != null) {
					    cell.setCellValue(record.getR39_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR39_zar() != null) {
					    cell.setCellValue(record.getR39_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR39_gbp() != null) {
					    cell.setCellValue(record.getR39_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR39_euro() != null) {
					    cell.setCellValue(record.getR39_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR39_yen() != null) {
					    cell.setCellValue(record.getR39_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR39_c6() != null) {
					    cell.setCellValue(record.getR39_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR39_c7() != null) {
					    cell.setCellValue(record.getR39_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR39_c8() != null) {
					    cell.setCellValue(record.getR39_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR39_total() != null) {
					    cell.setCellValue(record.getR39_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row40
					row = sheet.getRow(39);

					cell = row.getCell(1);
					if (record.getR40_usd() != null) {
					    cell.setCellValue(record.getR40_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR40_zar() != null) {
					    cell.setCellValue(record.getR40_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR40_gbp() != null) {
					    cell.setCellValue(record.getR40_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR40_euro() != null) {
					    cell.setCellValue(record.getR40_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR40_yen() != null) {
					    cell.setCellValue(record.getR40_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR40_c6() != null) {
					    cell.setCellValue(record.getR40_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR40_c7() != null) {
					    cell.setCellValue(record.getR40_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR40_c8() != null) {
					    cell.setCellValue(record.getR40_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR40_total() != null) {
					    cell.setCellValue(record.getR40_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cell = row.getCell(1);
					if (record.getR41_usd() != null) {
					    cell.setCellValue(record.getR41_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR41_zar() != null) {
					    cell.setCellValue(record.getR41_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR41_gbp() != null) {
					    cell.setCellValue(record.getR41_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR41_euro() != null) {
					    cell.setCellValue(record.getR41_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR41_yen() != null) {
					    cell.setCellValue(record.getR41_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR41_c6() != null) {
					    cell.setCellValue(record.getR41_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR41_c7() != null) {
					    cell.setCellValue(record.getR41_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR41_c8() != null) {
					    cell.setCellValue(record.getR41_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR41_total() != null) {
					    cell.setCellValue(record.getR41_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cell = row.getCell(1);
					if (record.getR42_usd() != null) {
					    cell.setCellValue(record.getR42_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR42_zar() != null) {
					    cell.setCellValue(record.getR42_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR42_gbp() != null) {
					    cell.setCellValue(record.getR42_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR42_euro() != null) {
					    cell.setCellValue(record.getR42_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR42_yen() != null) {
					    cell.setCellValue(record.getR42_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR42_c6() != null) {
					    cell.setCellValue(record.getR42_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR42_c7() != null) {
					    cell.setCellValue(record.getR42_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR42_c8() != null) {
					    cell.setCellValue(record.getR42_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR42_total() != null) {
					    cell.setCellValue(record.getR42_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}// row43
					row = sheet.getRow(42);

					cell = row.getCell(1);
					if (record.getR43_usd() != null) {
					    cell.setCellValue(record.getR43_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR43_zar() != null) {
					    cell.setCellValue(record.getR43_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR43_gbp() != null) {
					    cell.setCellValue(record.getR43_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR43_euro() != null) {
					    cell.setCellValue(record.getR43_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR43_yen() != null) {
					    cell.setCellValue(record.getR43_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR43_c6() != null) {
					    cell.setCellValue(record.getR43_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR43_c7() != null) {
					    cell.setCellValue(record.getR43_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR43_c8() != null) {
					    cell.setCellValue(record.getR43_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR43_total() != null) {
					    cell.setCellValue(record.getR43_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);

					cell = row.getCell(1);
					if (record.getR44_usd() != null) {
					    cell.setCellValue(record.getR44_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR44_zar() != null) {
					    cell.setCellValue(record.getR44_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR44_gbp() != null) {
					    cell.setCellValue(record.getR44_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR44_euro() != null) {
					    cell.setCellValue(record.getR44_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR44_yen() != null) {
					    cell.setCellValue(record.getR44_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR44_c6() != null) {
					    cell.setCellValue(record.getR44_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR44_c7() != null) {
					    cell.setCellValue(record.getR44_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR44_c8() != null) {
					    cell.setCellValue(record.getR44_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR44_total() != null) {
					    cell.setCellValue(record.getR44_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					cell = row.getCell(1);
					if (record.getR45_usd() != null) {
					    cell.setCellValue(record.getR45_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR45_zar() != null) {
					    cell.setCellValue(record.getR45_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR45_gbp() != null) {
					    cell.setCellValue(record.getR45_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR45_euro() != null) {
					    cell.setCellValue(record.getR45_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR45_yen() != null) {
					    cell.setCellValue(record.getR45_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR45_c6() != null) {
					    cell.setCellValue(record.getR45_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR45_c7() != null) {
					    cell.setCellValue(record.getR45_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR45_c8() != null) {
					    cell.setCellValue(record.getR45_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR45_total() != null) {
					    cell.setCellValue(record.getR45_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row46
					row = sheet.getRow(45);

					cell = row.getCell(1);
					if (record.getR46_usd() != null) {
					    cell.setCellValue(record.getR46_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR46_zar() != null) {
					    cell.setCellValue(record.getR46_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR46_gbp() != null) {
					    cell.setCellValue(record.getR46_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR46_euro() != null) {
					    cell.setCellValue(record.getR46_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR46_yen() != null) {
					    cell.setCellValue(record.getR46_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR46_c6() != null) {
					    cell.setCellValue(record.getR46_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR46_c7() != null) {
					    cell.setCellValue(record.getR46_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR46_c8() != null) {
					    cell.setCellValue(record.getR46_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR46_total() != null) {
					    cell.setCellValue(record.getR46_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					cell = row.getCell(1);
					if (record.getR47_usd() != null) {
					    cell.setCellValue(record.getR47_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR47_zar() != null) {
					    cell.setCellValue(record.getR47_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR47_gbp() != null) {
					    cell.setCellValue(record.getR47_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR47_euro() != null) {
					    cell.setCellValue(record.getR47_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR47_yen() != null) {
					    cell.setCellValue(record.getR47_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR47_c6() != null) {
					    cell.setCellValue(record.getR47_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR47_c7() != null) {
					    cell.setCellValue(record.getR47_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR47_c8() != null) {
					    cell.setCellValue(record.getR47_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR47_total() != null) {
					    cell.setCellValue(record.getR47_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);

					cell = row.getCell(1);
					if (record.getR48_usd() != null) {
					    cell.setCellValue(record.getR48_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR48_zar() != null) {
					    cell.setCellValue(record.getR48_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR48_gbp() != null) {
					    cell.setCellValue(record.getR48_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR48_euro() != null) {
					    cell.setCellValue(record.getR48_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR48_yen() != null) {
					    cell.setCellValue(record.getR48_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR48_c6() != null) {
					    cell.setCellValue(record.getR48_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR48_c7() != null) {
					    cell.setCellValue(record.getR48_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR48_c8() != null) {
					    cell.setCellValue(record.getR48_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR48_total() != null) {
					    cell.setCellValue(record.getR48_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);

					cell = row.getCell(1);
					if (record.getR49_usd() != null) {
					    cell.setCellValue(record.getR49_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR49_zar() != null) {
					    cell.setCellValue(record.getR49_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR49_gbp() != null) {
					    cell.setCellValue(record.getR49_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR49_euro() != null) {
					    cell.setCellValue(record.getR49_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR49_yen() != null) {
					    cell.setCellValue(record.getR49_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR49_c6() != null) {
					    cell.setCellValue(record.getR49_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR49_c7() != null) {
					    cell.setCellValue(record.getR49_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR49_c8() != null) {
					    cell.setCellValue(record.getR49_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR49_total() != null) {
					    cell.setCellValue(record.getR49_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row50
					row = sheet.getRow(49);

					cell = row.getCell(1);
					if (record.getR50_usd() != null) {
					    cell.setCellValue(record.getR50_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR50_zar() != null) {
					    cell.setCellValue(record.getR50_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR50_gbp() != null) {
					    cell.setCellValue(record.getR50_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR50_euro() != null) {
					    cell.setCellValue(record.getR50_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR50_yen() != null) {
					    cell.setCellValue(record.getR50_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR50_c6() != null) {
					    cell.setCellValue(record.getR50_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR50_c7() != null) {
					    cell.setCellValue(record.getR50_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR50_c8() != null) {
					    cell.setCellValue(record.getR50_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR50_total() != null) {
					    cell.setCellValue(record.getR50_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					cell = row.getCell(1);
					if (record.getR51_usd() != null) {
					    cell.setCellValue(record.getR51_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR51_zar() != null) {
					    cell.setCellValue(record.getR51_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR51_gbp() != null) {
					    cell.setCellValue(record.getR51_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR51_euro() != null) {
					    cell.setCellValue(record.getR51_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR51_yen() != null) {
					    cell.setCellValue(record.getR51_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR51_c6() != null) {
					    cell.setCellValue(record.getR51_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR51_c7() != null) {
					    cell.setCellValue(record.getR51_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR51_c8() != null) {
					    cell.setCellValue(record.getR51_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR51_total() != null) {
					    cell.setCellValue(record.getR51_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);

					cell = row.getCell(1);
					if (record.getR52_usd() != null) {
					    cell.setCellValue(record.getR52_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR52_zar() != null) {
					    cell.setCellValue(record.getR52_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR52_gbp() != null) {
					    cell.setCellValue(record.getR52_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR52_euro() != null) {
					    cell.setCellValue(record.getR52_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR52_yen() != null) {
					    cell.setCellValue(record.getR52_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR52_c6() != null) {
					    cell.setCellValue(record.getR52_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR52_c7() != null) {
					    cell.setCellValue(record.getR52_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR52_c8() != null) {
					    cell.setCellValue(record.getR52_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR52_total() != null) {
					    cell.setCellValue(record.getR52_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					cell = row.getCell(1);
					if (record.getR53_usd() != null) {
					    cell.setCellValue(record.getR53_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR53_zar() != null) {
					    cell.setCellValue(record.getR53_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR53_gbp() != null) {
					    cell.setCellValue(record.getR53_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR53_euro() != null) {
					    cell.setCellValue(record.getR53_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR53_yen() != null) {
					    cell.setCellValue(record.getR53_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR53_c6() != null) {
					    cell.setCellValue(record.getR53_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR53_c7() != null) {
					    cell.setCellValue(record.getR53_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR53_c8() != null) {
					    cell.setCellValue(record.getR53_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR53_total() != null) {
					    cell.setCellValue(record.getR53_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row54
					row = sheet.getRow(53);

					cell = row.getCell(1);
					if (record.getR54_usd() != null) {
					    cell.setCellValue(record.getR54_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR54_zar() != null) {
					    cell.setCellValue(record.getR54_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR54_gbp() != null) {
					    cell.setCellValue(record.getR54_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR54_euro() != null) {
					    cell.setCellValue(record.getR54_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR54_yen() != null) {
					    cell.setCellValue(record.getR54_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR54_c6() != null) {
					    cell.setCellValue(record.getR54_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR54_c7() != null) {
					    cell.setCellValue(record.getR54_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR54_c8() != null) {
					    cell.setCellValue(record.getR54_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR54_total() != null) {
					    cell.setCellValue(record.getR54_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					cell = row.getCell(1);
					if (record.getR55_usd() != null) {
					    cell.setCellValue(record.getR55_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR55_zar() != null) {
					    cell.setCellValue(record.getR55_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR55_gbp() != null) {
					    cell.setCellValue(record.getR55_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR55_euro() != null) {
					    cell.setCellValue(record.getR55_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR55_yen() != null) {
					    cell.setCellValue(record.getR55_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR55_c6() != null) {
					    cell.setCellValue(record.getR55_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR55_c7() != null) {
					    cell.setCellValue(record.getR55_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR55_c8() != null) {
					    cell.setCellValue(record.getR55_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR55_total() != null) {
					    cell.setCellValue(record.getR55_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);

					cell = row.getCell(1);
					if (record.getR56_usd() != null) {
					    cell.setCellValue(record.getR56_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR56_zar() != null) {
					    cell.setCellValue(record.getR56_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR56_gbp() != null) {
					    cell.setCellValue(record.getR56_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR56_euro() != null) {
					    cell.setCellValue(record.getR56_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR56_yen() != null) {
					    cell.setCellValue(record.getR56_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR56_c6() != null) {
					    cell.setCellValue(record.getR56_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR56_c7() != null) {
					    cell.setCellValue(record.getR56_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR56_c8() != null) {
					    cell.setCellValue(record.getR56_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR56_total() != null) {
					    cell.setCellValue(record.getR56_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					cell = row.getCell(1);
					if (record.getR57_usd() != null) {
					    cell.setCellValue(record.getR57_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR57_zar() != null) {
					    cell.setCellValue(record.getR57_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR57_gbp() != null) {
					    cell.setCellValue(record.getR57_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR57_euro() != null) {
					    cell.setCellValue(record.getR57_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR57_yen() != null) {
					    cell.setCellValue(record.getR57_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR57_c6() != null) {
					    cell.setCellValue(record.getR57_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR57_c7() != null) {
					    cell.setCellValue(record.getR57_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR57_c8() != null) {
					    cell.setCellValue(record.getR57_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR57_total() != null) {
					    cell.setCellValue(record.getR57_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row58
					row = sheet.getRow(57);

					cell = row.getCell(1);
					if (record.getR58_usd() != null) {
					    cell.setCellValue(record.getR58_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR58_zar() != null) {
					    cell.setCellValue(record.getR58_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR58_gbp() != null) {
					    cell.setCellValue(record.getR58_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR58_euro() != null) {
					    cell.setCellValue(record.getR58_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR58_yen() != null) {
					    cell.setCellValue(record.getR58_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR58_c6() != null) {
					    cell.setCellValue(record.getR58_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR58_c7() != null) {
					    cell.setCellValue(record.getR58_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR58_c8() != null) {
					    cell.setCellValue(record.getR58_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR58_total() != null) {
					    cell.setCellValue(record.getR58_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					cell = row.getCell(1);
					if (record.getR59_usd() != null) {
					    cell.setCellValue(record.getR59_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR59_zar() != null) {
					    cell.setCellValue(record.getR59_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR59_gbp() != null) {
					    cell.setCellValue(record.getR59_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR59_euro() != null) {
					    cell.setCellValue(record.getR59_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR59_yen() != null) {
					    cell.setCellValue(record.getR59_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR59_c6() != null) {
					    cell.setCellValue(record.getR59_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR59_c7() != null) {
					    cell.setCellValue(record.getR59_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR59_c8() != null) {
					    cell.setCellValue(record.getR59_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR59_total() != null) {
					    cell.setCellValue(record.getR59_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					cell = row.getCell(1);
					if (record.getR60_usd() != null) {
					    cell.setCellValue(record.getR60_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR60_zar() != null) {
					    cell.setCellValue(record.getR60_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR60_gbp() != null) {
					    cell.setCellValue(record.getR60_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR60_euro() != null) {
					    cell.setCellValue(record.getR60_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR60_yen() != null) {
					    cell.setCellValue(record.getR60_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR60_c6() != null) {
					    cell.setCellValue(record.getR60_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR60_c7() != null) {
					    cell.setCellValue(record.getR60_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR60_c8() != null) {
					    cell.setCellValue(record.getR60_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR60_total() != null) {
					    cell.setCellValue(record.getR60_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					cell = row.getCell(1);
					if (record.getR61_usd() != null) {
					    cell.setCellValue(record.getR61_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR61_zar() != null) {
					    cell.setCellValue(record.getR61_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR61_gbp() != null) {
					    cell.setCellValue(record.getR61_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR61_euro() != null) {
					    cell.setCellValue(record.getR61_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR61_yen() != null) {
					    cell.setCellValue(record.getR61_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR61_c6() != null) {
					    cell.setCellValue(record.getR61_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR61_c7() != null) {
					    cell.setCellValue(record.getR61_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR61_c8() != null) {
					    cell.setCellValue(record.getR61_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR61_total() != null) {
					    cell.setCellValue(record.getR61_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					cell = row.getCell(1);
					if (record.getR62_usd() != null) {
					    cell.setCellValue(record.getR62_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR62_zar() != null) {
					    cell.setCellValue(record.getR62_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR62_gbp() != null) {
					    cell.setCellValue(record.getR62_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR62_euro() != null) {
					    cell.setCellValue(record.getR62_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR62_yen() != null) {
					    cell.setCellValue(record.getR62_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR62_c6() != null) {
					    cell.setCellValue(record.getR62_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR62_c7() != null) {
					    cell.setCellValue(record.getR62_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR62_c8() != null) {
					    cell.setCellValue(record.getR62_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR62_total() != null) {
					    cell.setCellValue(record.getR62_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}


						

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_LA5EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA5EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
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
//				return BRRS_M_LA5ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
		} else {
			List<M_LA5_Summary_Entity> dataList = BRRS_M_LA5_Summary_Repo
					.getdatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_LA5 report. Returning empty result.");
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
						M_LA5_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL

					// row8
					row = sheet.getRow(7);
					Cell cell = row.getCell(1);
					if (record.getR8_usd() != null) {
					    cell.setCellValue(record.getR8_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR8_zar() != null) {
					    cell.setCellValue(record.getR8_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR8_gbp() != null) {
					    cell.setCellValue(record.getR8_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR8_euro() != null) {
					    cell.setCellValue(record.getR8_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR8_yen() != null) {
					    cell.setCellValue(record.getR8_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR8_c6() != null) {
					    cell.setCellValue(record.getR8_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR8_c7() != null) {
					    cell.setCellValue(record.getR8_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR8_c8() != null) {
					    cell.setCellValue(record.getR8_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR8_total() != null) {
					    cell.setCellValue(record.getR8_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row9
					row = sheet.getRow(8);

					cell = row.getCell(1);
					if (record.getR9_usd() != null) {
					    cell.setCellValue(record.getR9_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR9_zar() != null) {
					    cell.setCellValue(record.getR9_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR9_gbp() != null) {
					    cell.setCellValue(record.getR9_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR9_euro() != null) {
					    cell.setCellValue(record.getR9_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR9_yen() != null) {
					    cell.setCellValue(record.getR9_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR9_c6() != null) {
					    cell.setCellValue(record.getR9_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR9_c7() != null) {
					    cell.setCellValue(record.getR9_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR9_c8() != null) {
					    cell.setCellValue(record.getR9_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR9_total() != null) {
					    cell.setCellValue(record.getR9_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row10
					row = sheet.getRow(9);

					cell = row.getCell(1);
					if (record.getR10_usd() != null) {
					    cell.setCellValue(record.getR10_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR10_zar() != null) {
					    cell.setCellValue(record.getR10_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR10_gbp() != null) {
					    cell.setCellValue(record.getR10_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR10_euro() != null) {
					    cell.setCellValue(record.getR10_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR10_yen() != null) {
					    cell.setCellValue(record.getR10_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR10_c6() != null) {
					    cell.setCellValue(record.getR10_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR10_c7() != null) {
					    cell.setCellValue(record.getR10_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR10_c8() != null) {
					    cell.setCellValue(record.getR10_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR10_total() != null) {
					    cell.setCellValue(record.getR10_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					
					// row11
					row = sheet.getRow(10);

					cell = row.getCell(1);
					if (record.getR11_usd() != null) {
					    cell.setCellValue(record.getR11_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR11_zar() != null) {
					    cell.setCellValue(record.getR11_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR11_gbp() != null) {
					    cell.setCellValue(record.getR11_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR11_euro() != null) {
					    cell.setCellValue(record.getR11_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR11_yen() != null) {
					    cell.setCellValue(record.getR11_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR11_c6() != null) {
					    cell.setCellValue(record.getR11_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR11_c7() != null) {
					    cell.setCellValue(record.getR11_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR11_c8() != null) {
					    cell.setCellValue(record.getR11_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR11_total() != null) {
					    cell.setCellValue(record.getR11_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell = row.getCell(1);
					if (record.getR12_usd() != null) {
					    cell.setCellValue(record.getR12_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR12_zar() != null) {
					    cell.setCellValue(record.getR12_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR12_gbp() != null) {
					    cell.setCellValue(record.getR12_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR12_euro() != null) {
					    cell.setCellValue(record.getR12_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR12_yen() != null) {
					    cell.setCellValue(record.getR12_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR12_c6() != null) {
					    cell.setCellValue(record.getR12_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR12_c7() != null) {
					    cell.setCellValue(record.getR12_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR12_c8() != null) {
					    cell.setCellValue(record.getR12_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR12_total() != null) {
					    cell.setCellValue(record.getR12_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell = row.getCell(1);
					if (record.getR13_usd() != null) {
					    cell.setCellValue(record.getR13_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR13_zar() != null) {
					    cell.setCellValue(record.getR13_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR13_gbp() != null) {
					    cell.setCellValue(record.getR13_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR13_euro() != null) {
					    cell.setCellValue(record.getR13_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR13_yen() != null) {
					    cell.setCellValue(record.getR13_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR13_c6() != null) {
					    cell.setCellValue(record.getR13_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR13_c7() != null) {
					    cell.setCellValue(record.getR13_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR13_c8() != null) {
					    cell.setCellValue(record.getR13_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR13_total() != null) {
					    cell.setCellValue(record.getR13_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell = row.getCell(1);
					if (record.getR14_usd() != null) {
					    cell.setCellValue(record.getR14_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR14_zar() != null) {
					    cell.setCellValue(record.getR14_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR14_gbp() != null) {
					    cell.setCellValue(record.getR14_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR14_euro() != null) {
					    cell.setCellValue(record.getR14_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR14_yen() != null) {
					    cell.setCellValue(record.getR14_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR14_c6() != null) {
					    cell.setCellValue(record.getR14_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR14_c7() != null) {
					    cell.setCellValue(record.getR14_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR14_c8() != null) {
					    cell.setCellValue(record.getR14_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR14_total() != null) {
					    cell.setCellValue(record.getR14_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row15
					row = sheet.getRow(14);

					cell = row.getCell(1);
					if (record.getR15_usd() != null) {
					    cell.setCellValue(record.getR15_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR15_zar() != null) {
					    cell.setCellValue(record.getR15_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR15_gbp() != null) {
					    cell.setCellValue(record.getR15_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR15_euro() != null) {
					    cell.setCellValue(record.getR15_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR15_yen() != null) {
					    cell.setCellValue(record.getR15_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR15_c6() != null) {
					    cell.setCellValue(record.getR15_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR15_c7() != null) {
					    cell.setCellValue(record.getR15_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR15_c8() != null) {
					    cell.setCellValue(record.getR15_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR15_total() != null) {
					    cell.setCellValue(record.getR15_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell = row.getCell(1);
					if (record.getR16_usd() != null) {
					    cell.setCellValue(record.getR16_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR16_zar() != null) {
					    cell.setCellValue(record.getR16_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR16_gbp() != null) {
					    cell.setCellValue(record.getR16_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR16_euro() != null) {
					    cell.setCellValue(record.getR16_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR16_yen() != null) {
					    cell.setCellValue(record.getR16_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR16_c6() != null) {
					    cell.setCellValue(record.getR16_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR16_c7() != null) {
					    cell.setCellValue(record.getR16_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR16_c8() != null) {
					    cell.setCellValue(record.getR16_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR16_total() != null) {
					    cell.setCellValue(record.getR16_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row17
					row = sheet.getRow(16);

					cell = row.getCell(1);
					if (record.getR17_usd() != null) {
					    cell.setCellValue(record.getR17_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR17_zar() != null) {
					    cell.setCellValue(record.getR17_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR17_gbp() != null) {
					    cell.setCellValue(record.getR17_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR17_euro() != null) {
					    cell.setCellValue(record.getR17_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR17_yen() != null) {
					    cell.setCellValue(record.getR17_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR17_c6() != null) {
					    cell.setCellValue(record.getR17_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR17_c7() != null) {
					    cell.setCellValue(record.getR17_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR17_c8() != null) {
					    cell.setCellValue(record.getR17_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR17_total() != null) {
					    cell.setCellValue(record.getR17_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell = row.getCell(1);
					if (record.getR18_usd() != null) {
					    cell.setCellValue(record.getR18_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR18_zar() != null) {
					    cell.setCellValue(record.getR18_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR18_gbp() != null) {
					    cell.setCellValue(record.getR18_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR18_euro() != null) {
					    cell.setCellValue(record.getR18_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR18_yen() != null) {
					    cell.setCellValue(record.getR18_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR18_c6() != null) {
					    cell.setCellValue(record.getR18_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR18_c7() != null) {
					    cell.setCellValue(record.getR18_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR18_c8() != null) {
					    cell.setCellValue(record.getR18_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR18_total() != null) {
					    cell.setCellValue(record.getR18_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row19
					row = sheet.getRow(18);

					cell = row.getCell(1);
					if (record.getR19_usd() != null) {
					    cell.setCellValue(record.getR19_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR19_zar() != null) {
					    cell.setCellValue(record.getR19_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR19_gbp() != null) {
					    cell.setCellValue(record.getR19_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR19_euro() != null) {
					    cell.setCellValue(record.getR19_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR19_yen() != null) {
					    cell.setCellValue(record.getR19_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR19_c6() != null) {
					    cell.setCellValue(record.getR19_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR19_c7() != null) {
					    cell.setCellValue(record.getR19_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR19_c8() != null) {
					    cell.setCellValue(record.getR19_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR19_total() != null) {
					    cell.setCellValue(record.getR19_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell = row.getCell(1);
					if (record.getR20_usd() != null) {
					    cell.setCellValue(record.getR20_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR20_zar() != null) {
					    cell.setCellValue(record.getR20_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR20_gbp() != null) {
					    cell.setCellValue(record.getR20_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR20_euro() != null) {
					    cell.setCellValue(record.getR20_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR20_yen() != null) {
					    cell.setCellValue(record.getR20_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR20_c6() != null) {
					    cell.setCellValue(record.getR20_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR20_c7() != null) {
					    cell.setCellValue(record.getR20_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR20_c8() != null) {
					    cell.setCellValue(record.getR20_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR20_total() != null) {
					    cell.setCellValue(record.getR20_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row21
					row = sheet.getRow(20);

					cell = row.getCell(1);
					if (record.getR21_usd() != null) {
					    cell.setCellValue(record.getR21_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR21_zar() != null) {
					    cell.setCellValue(record.getR21_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR21_gbp() != null) {
					    cell.setCellValue(record.getR21_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR21_euro() != null) {
					    cell.setCellValue(record.getR21_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR21_yen() != null) {
					    cell.setCellValue(record.getR21_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR21_c6() != null) {
					    cell.setCellValue(record.getR21_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR21_c7() != null) {
					    cell.setCellValue(record.getR21_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR21_c8() != null) {
					    cell.setCellValue(record.getR21_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR21_total() != null) {
					    cell.setCellValue(record.getR21_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					cell = row.getCell(1);
					if (record.getR22_usd() != null) {
					    cell.setCellValue(record.getR22_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR22_zar() != null) {
					    cell.setCellValue(record.getR22_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR22_gbp() != null) {
					    cell.setCellValue(record.getR22_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR22_euro() != null) {
					    cell.setCellValue(record.getR22_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR22_yen() != null) {
					    cell.setCellValue(record.getR22_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR22_c6() != null) {
					    cell.setCellValue(record.getR22_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR22_c7() != null) {
					    cell.setCellValue(record.getR22_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR22_c8() != null) {
					    cell.setCellValue(record.getR22_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR22_total() != null) {
					    cell.setCellValue(record.getR22_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row23
					row = sheet.getRow(22);

					cell = row.getCell(1);
					if (record.getR23_usd() != null) {
					    cell.setCellValue(record.getR23_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR23_zar() != null) {
					    cell.setCellValue(record.getR23_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR23_gbp() != null) {
					    cell.setCellValue(record.getR23_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR23_euro() != null) {
					    cell.setCellValue(record.getR23_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR23_yen() != null) {
					    cell.setCellValue(record.getR23_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR23_c6() != null) {
					    cell.setCellValue(record.getR23_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR23_c7() != null) {
					    cell.setCellValue(record.getR23_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR23_c8() != null) {
					    cell.setCellValue(record.getR23_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR23_total() != null) {
					    cell.setCellValue(record.getR23_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					cell = row.getCell(1);
					if (record.getR24_usd() != null) {
					    cell.setCellValue(record.getR24_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR24_zar() != null) {
					    cell.setCellValue(record.getR24_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR24_gbp() != null) {
					    cell.setCellValue(record.getR24_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR24_euro() != null) {
					    cell.setCellValue(record.getR24_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR24_yen() != null) {
					    cell.setCellValue(record.getR24_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR24_c6() != null) {
					    cell.setCellValue(record.getR24_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR24_c7() != null) {
					    cell.setCellValue(record.getR24_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR24_c8() != null) {
					    cell.setCellValue(record.getR24_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR24_total() != null) {
					    cell.setCellValue(record.getR24_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell = row.getCell(1);
					if (record.getR25_usd() != null) {
					    cell.setCellValue(record.getR25_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR25_zar() != null) {
					    cell.setCellValue(record.getR25_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR25_gbp() != null) {
					    cell.setCellValue(record.getR25_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR25_euro() != null) {
					    cell.setCellValue(record.getR25_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR25_yen() != null) {
					    cell.setCellValue(record.getR25_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR25_c6() != null) {
					    cell.setCellValue(record.getR25_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR25_c7() != null) {
					    cell.setCellValue(record.getR25_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR25_c8() != null) {
					    cell.setCellValue(record.getR25_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR25_total() != null) {
					    cell.setCellValue(record.getR25_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell = row.getCell(1);
					if (record.getR26_usd() != null) {
					    cell.setCellValue(record.getR26_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR26_zar() != null) {
					    cell.setCellValue(record.getR26_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR26_gbp() != null) {
					    cell.setCellValue(record.getR26_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR26_euro() != null) {
					    cell.setCellValue(record.getR26_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR26_yen() != null) {
					    cell.setCellValue(record.getR26_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR26_c6() != null) {
					    cell.setCellValue(record.getR26_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR26_c7() != null) {
					    cell.setCellValue(record.getR26_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR26_c8() != null) {
					    cell.setCellValue(record.getR26_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR26_total() != null) {
					    cell.setCellValue(record.getR26_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell = row.getCell(1);
					if (record.getR27_usd() != null) {
					    cell.setCellValue(record.getR27_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR27_zar() != null) {
					    cell.setCellValue(record.getR27_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR27_gbp() != null) {
					    cell.setCellValue(record.getR27_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR27_euro() != null) {
					    cell.setCellValue(record.getR27_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR27_yen() != null) {
					    cell.setCellValue(record.getR27_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR27_c6() != null) {
					    cell.setCellValue(record.getR27_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR27_c7() != null) {
					    cell.setCellValue(record.getR27_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR27_c8() != null) {
					    cell.setCellValue(record.getR27_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR27_total() != null) {
					    cell.setCellValue(record.getR27_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					
					// row28
					row = sheet.getRow(27);

					cell = row.getCell(1);
					if (record.getR28_usd() != null) {
					    cell.setCellValue(record.getR28_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR28_zar() != null) {
					    cell.setCellValue(record.getR28_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR28_gbp() != null) {
					    cell.setCellValue(record.getR28_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR28_euro() != null) {
					    cell.setCellValue(record.getR28_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR28_yen() != null) {
					    cell.setCellValue(record.getR28_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR28_c6() != null) {
					    cell.setCellValue(record.getR28_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR28_c7() != null) {
					    cell.setCellValue(record.getR28_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR28_c8() != null) {
					    cell.setCellValue(record.getR28_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR28_total() != null) {
					    cell.setCellValue(record.getR28_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell = row.getCell(1);
					if (record.getR29_usd() != null) {
					    cell.setCellValue(record.getR29_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR29_zar() != null) {
					    cell.setCellValue(record.getR29_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR29_gbp() != null) {
					    cell.setCellValue(record.getR29_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR29_euro() != null) {
					    cell.setCellValue(record.getR29_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR29_yen() != null) {
					    cell.setCellValue(record.getR29_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR29_c6() != null) {
					    cell.setCellValue(record.getR29_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR29_c7() != null) {
					    cell.setCellValue(record.getR29_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR29_c8() != null) {
					    cell.setCellValue(record.getR29_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR29_total() != null) {
					    cell.setCellValue(record.getR29_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell = row.getCell(1);
					if (record.getR30_usd() != null) {
					    cell.setCellValue(record.getR30_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR30_zar() != null) {
					    cell.setCellValue(record.getR30_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR30_gbp() != null) {
					    cell.setCellValue(record.getR30_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR30_euro() != null) {
					    cell.setCellValue(record.getR30_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR30_yen() != null) {
					    cell.setCellValue(record.getR30_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR30_c6() != null) {
					    cell.setCellValue(record.getR30_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR30_c7() != null) {
					    cell.setCellValue(record.getR30_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR30_c8() != null) {
					    cell.setCellValue(record.getR30_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR30_total() != null) {
					    cell.setCellValue(record.getR30_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					
					// row31
					row = sheet.getRow(30);

					cell = row.getCell(1);
					if (record.getR31_usd() != null) {
					    cell.setCellValue(record.getR31_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR31_zar() != null) {
					    cell.setCellValue(record.getR31_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR31_gbp() != null) {
					    cell.setCellValue(record.getR31_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR31_euro() != null) {
					    cell.setCellValue(record.getR31_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR31_yen() != null) {
					    cell.setCellValue(record.getR31_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR31_c6() != null) {
					    cell.setCellValue(record.getR31_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR31_c7() != null) {
					    cell.setCellValue(record.getR31_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR31_c8() != null) {
					    cell.setCellValue(record.getR31_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR31_total() != null) {
					    cell.setCellValue(record.getR31_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell = row.getCell(1);
					if (record.getR32_usd() != null) {
					    cell.setCellValue(record.getR32_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR32_zar() != null) {
					    cell.setCellValue(record.getR32_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR32_gbp() != null) {
					    cell.setCellValue(record.getR32_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR32_euro() != null) {
					    cell.setCellValue(record.getR32_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR32_yen() != null) {
					    cell.setCellValue(record.getR32_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR32_c6() != null) {
					    cell.setCellValue(record.getR32_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR32_c7() != null) {
					    cell.setCellValue(record.getR32_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR32_c8() != null) {
					    cell.setCellValue(record.getR32_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR32_total() != null) {
					    cell.setCellValue(record.getR32_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell = row.getCell(1);
					if (record.getR33_usd() != null) {
					    cell.setCellValue(record.getR33_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR33_zar() != null) {
					    cell.setCellValue(record.getR33_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR33_gbp() != null) {
					    cell.setCellValue(record.getR33_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR33_euro() != null) {
					    cell.setCellValue(record.getR33_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR33_yen() != null) {
					    cell.setCellValue(record.getR33_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR33_c6() != null) {
					    cell.setCellValue(record.getR33_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR33_c7() != null) {
					    cell.setCellValue(record.getR33_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR33_c8() != null) {
					    cell.setCellValue(record.getR33_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR33_total() != null) {
					    cell.setCellValue(record.getR33_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row34
					row = sheet.getRow(33);

					cell = row.getCell(1);
					if (record.getR34_usd() != null) {
					    cell.setCellValue(record.getR34_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR34_zar() != null) {
					    cell.setCellValue(record.getR34_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR34_gbp() != null) {
					    cell.setCellValue(record.getR34_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR34_euro() != null) {
					    cell.setCellValue(record.getR34_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR34_yen() != null) {
					    cell.setCellValue(record.getR34_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR34_c6() != null) {
					    cell.setCellValue(record.getR34_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR34_c7() != null) {
					    cell.setCellValue(record.getR34_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR34_c8() != null) {
					    cell.setCellValue(record.getR34_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR34_total() != null) {
					    cell.setCellValue(record.getR34_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell = row.getCell(1);
					if (record.getR35_usd() != null) {
					    cell.setCellValue(record.getR35_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR35_zar() != null) {
					    cell.setCellValue(record.getR35_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR35_gbp() != null) {
					    cell.setCellValue(record.getR35_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR35_euro() != null) {
					    cell.setCellValue(record.getR35_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR35_yen() != null) {
					    cell.setCellValue(record.getR35_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR35_c6() != null) {
					    cell.setCellValue(record.getR35_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR35_c7() != null) {
					    cell.setCellValue(record.getR35_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR35_c8() != null) {
					    cell.setCellValue(record.getR35_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR35_total() != null) {
					    cell.setCellValue(record.getR35_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell = row.getCell(1);
					if (record.getR36_usd() != null) {
					    cell.setCellValue(record.getR36_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR36_zar() != null) {
					    cell.setCellValue(record.getR36_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR36_gbp() != null) {
					    cell.setCellValue(record.getR36_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR36_euro() != null) {
					    cell.setCellValue(record.getR36_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR36_yen() != null) {
					    cell.setCellValue(record.getR36_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR36_c6() != null) {
					    cell.setCellValue(record.getR36_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR36_c7() != null) {
					    cell.setCellValue(record.getR36_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR36_c8() != null) {
					    cell.setCellValue(record.getR36_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR36_total() != null) {
					    cell.setCellValue(record.getR36_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row37
					row = sheet.getRow(36);

					cell = row.getCell(1);
					if (record.getR37_usd() != null) {
					    cell.setCellValue(record.getR37_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR37_zar() != null) {
					    cell.setCellValue(record.getR37_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR37_gbp() != null) {
					    cell.setCellValue(record.getR37_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR37_euro() != null) {
					    cell.setCellValue(record.getR37_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR37_yen() != null) {
					    cell.setCellValue(record.getR37_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR37_c6() != null) {
					    cell.setCellValue(record.getR37_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR37_c7() != null) {
					    cell.setCellValue(record.getR37_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR37_c8() != null) {
					    cell.setCellValue(record.getR37_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR37_total() != null) {
					    cell.setCellValue(record.getR37_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);

					cell = row.getCell(1);
					if (record.getR38_usd() != null) {
					    cell.setCellValue(record.getR38_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR38_zar() != null) {
					    cell.setCellValue(record.getR38_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR38_gbp() != null) {
					    cell.setCellValue(record.getR38_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR38_euro() != null) {
					    cell.setCellValue(record.getR38_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR38_yen() != null) {
					    cell.setCellValue(record.getR38_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR38_c6() != null) {
					    cell.setCellValue(record.getR38_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR38_c7() != null) {
					    cell.setCellValue(record.getR38_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR38_c8() != null) {
					    cell.setCellValue(record.getR38_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR38_total() != null) {
					    cell.setCellValue(record.getR38_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					cell = row.getCell(1);
					if (record.getR39_usd() != null) {
					    cell.setCellValue(record.getR39_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR39_zar() != null) {
					    cell.setCellValue(record.getR39_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR39_gbp() != null) {
					    cell.setCellValue(record.getR39_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR39_euro() != null) {
					    cell.setCellValue(record.getR39_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR39_yen() != null) {
					    cell.setCellValue(record.getR39_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR39_c6() != null) {
					    cell.setCellValue(record.getR39_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR39_c7() != null) {
					    cell.setCellValue(record.getR39_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR39_c8() != null) {
					    cell.setCellValue(record.getR39_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR39_total() != null) {
					    cell.setCellValue(record.getR39_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row40
					row = sheet.getRow(39);

					cell = row.getCell(1);
					if (record.getR40_usd() != null) {
					    cell.setCellValue(record.getR40_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR40_zar() != null) {
					    cell.setCellValue(record.getR40_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR40_gbp() != null) {
					    cell.setCellValue(record.getR40_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR40_euro() != null) {
					    cell.setCellValue(record.getR40_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR40_yen() != null) {
					    cell.setCellValue(record.getR40_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR40_c6() != null) {
					    cell.setCellValue(record.getR40_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR40_c7() != null) {
					    cell.setCellValue(record.getR40_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR40_c8() != null) {
					    cell.setCellValue(record.getR40_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR40_total() != null) {
					    cell.setCellValue(record.getR40_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cell = row.getCell(1);
					if (record.getR41_usd() != null) {
					    cell.setCellValue(record.getR41_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR41_zar() != null) {
					    cell.setCellValue(record.getR41_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR41_gbp() != null) {
					    cell.setCellValue(record.getR41_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR41_euro() != null) {
					    cell.setCellValue(record.getR41_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR41_yen() != null) {
					    cell.setCellValue(record.getR41_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR41_c6() != null) {
					    cell.setCellValue(record.getR41_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR41_c7() != null) {
					    cell.setCellValue(record.getR41_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR41_c8() != null) {
					    cell.setCellValue(record.getR41_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR41_total() != null) {
					    cell.setCellValue(record.getR41_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cell = row.getCell(1);
					if (record.getR42_usd() != null) {
					    cell.setCellValue(record.getR42_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR42_zar() != null) {
					    cell.setCellValue(record.getR42_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR42_gbp() != null) {
					    cell.setCellValue(record.getR42_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR42_euro() != null) {
					    cell.setCellValue(record.getR42_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR42_yen() != null) {
					    cell.setCellValue(record.getR42_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR42_c6() != null) {
					    cell.setCellValue(record.getR42_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR42_c7() != null) {
					    cell.setCellValue(record.getR42_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR42_c8() != null) {
					    cell.setCellValue(record.getR42_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR42_total() != null) {
					    cell.setCellValue(record.getR42_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}// row43
					row = sheet.getRow(42);

					cell = row.getCell(1);
					if (record.getR43_usd() != null) {
					    cell.setCellValue(record.getR43_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR43_zar() != null) {
					    cell.setCellValue(record.getR43_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR43_gbp() != null) {
					    cell.setCellValue(record.getR43_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR43_euro() != null) {
					    cell.setCellValue(record.getR43_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR43_yen() != null) {
					    cell.setCellValue(record.getR43_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR43_c6() != null) {
					    cell.setCellValue(record.getR43_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR43_c7() != null) {
					    cell.setCellValue(record.getR43_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR43_c8() != null) {
					    cell.setCellValue(record.getR43_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR43_total() != null) {
					    cell.setCellValue(record.getR43_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);

					cell = row.getCell(1);
					if (record.getR44_usd() != null) {
					    cell.setCellValue(record.getR44_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR44_zar() != null) {
					    cell.setCellValue(record.getR44_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR44_gbp() != null) {
					    cell.setCellValue(record.getR44_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR44_euro() != null) {
					    cell.setCellValue(record.getR44_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR44_yen() != null) {
					    cell.setCellValue(record.getR44_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR44_c6() != null) {
					    cell.setCellValue(record.getR44_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR44_c7() != null) {
					    cell.setCellValue(record.getR44_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR44_c8() != null) {
					    cell.setCellValue(record.getR44_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR44_total() != null) {
					    cell.setCellValue(record.getR44_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					cell = row.getCell(1);
					if (record.getR45_usd() != null) {
					    cell.setCellValue(record.getR45_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR45_zar() != null) {
					    cell.setCellValue(record.getR45_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR45_gbp() != null) {
					    cell.setCellValue(record.getR45_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR45_euro() != null) {
					    cell.setCellValue(record.getR45_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR45_yen() != null) {
					    cell.setCellValue(record.getR45_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR45_c6() != null) {
					    cell.setCellValue(record.getR45_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR45_c7() != null) {
					    cell.setCellValue(record.getR45_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR45_c8() != null) {
					    cell.setCellValue(record.getR45_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR45_total() != null) {
					    cell.setCellValue(record.getR45_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row46
					row = sheet.getRow(45);

					cell = row.getCell(1);
					if (record.getR46_usd() != null) {
					    cell.setCellValue(record.getR46_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR46_zar() != null) {
					    cell.setCellValue(record.getR46_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR46_gbp() != null) {
					    cell.setCellValue(record.getR46_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR46_euro() != null) {
					    cell.setCellValue(record.getR46_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR46_yen() != null) {
					    cell.setCellValue(record.getR46_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR46_c6() != null) {
					    cell.setCellValue(record.getR46_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR46_c7() != null) {
					    cell.setCellValue(record.getR46_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR46_c8() != null) {
					    cell.setCellValue(record.getR46_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR46_total() != null) {
					    cell.setCellValue(record.getR46_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					cell = row.getCell(1);
					if (record.getR47_usd() != null) {
					    cell.setCellValue(record.getR47_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR47_zar() != null) {
					    cell.setCellValue(record.getR47_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR47_gbp() != null) {
					    cell.setCellValue(record.getR47_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR47_euro() != null) {
					    cell.setCellValue(record.getR47_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR47_yen() != null) {
					    cell.setCellValue(record.getR47_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR47_c6() != null) {
					    cell.setCellValue(record.getR47_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR47_c7() != null) {
					    cell.setCellValue(record.getR47_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR47_c8() != null) {
					    cell.setCellValue(record.getR47_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR47_total() != null) {
					    cell.setCellValue(record.getR47_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);

					cell = row.getCell(1);
					if (record.getR48_usd() != null) {
					    cell.setCellValue(record.getR48_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR48_zar() != null) {
					    cell.setCellValue(record.getR48_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR48_gbp() != null) {
					    cell.setCellValue(record.getR48_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR48_euro() != null) {
					    cell.setCellValue(record.getR48_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR48_yen() != null) {
					    cell.setCellValue(record.getR48_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR48_c6() != null) {
					    cell.setCellValue(record.getR48_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR48_c7() != null) {
					    cell.setCellValue(record.getR48_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR48_c8() != null) {
					    cell.setCellValue(record.getR48_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR48_total() != null) {
					    cell.setCellValue(record.getR48_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);

					cell = row.getCell(1);
					if (record.getR49_usd() != null) {
					    cell.setCellValue(record.getR49_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR49_zar() != null) {
					    cell.setCellValue(record.getR49_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR49_gbp() != null) {
					    cell.setCellValue(record.getR49_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR49_euro() != null) {
					    cell.setCellValue(record.getR49_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR49_yen() != null) {
					    cell.setCellValue(record.getR49_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR49_c6() != null) {
					    cell.setCellValue(record.getR49_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR49_c7() != null) {
					    cell.setCellValue(record.getR49_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR49_c8() != null) {
					    cell.setCellValue(record.getR49_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR49_total() != null) {
					    cell.setCellValue(record.getR49_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row50
					row = sheet.getRow(49);

					cell = row.getCell(1);
					if (record.getR50_usd() != null) {
					    cell.setCellValue(record.getR50_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR50_zar() != null) {
					    cell.setCellValue(record.getR50_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR50_gbp() != null) {
					    cell.setCellValue(record.getR50_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR50_euro() != null) {
					    cell.setCellValue(record.getR50_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR50_yen() != null) {
					    cell.setCellValue(record.getR50_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR50_c6() != null) {
					    cell.setCellValue(record.getR50_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR50_c7() != null) {
					    cell.setCellValue(record.getR50_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR50_c8() != null) {
					    cell.setCellValue(record.getR50_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR50_total() != null) {
					    cell.setCellValue(record.getR50_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					cell = row.getCell(1);
					if (record.getR51_usd() != null) {
					    cell.setCellValue(record.getR51_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR51_zar() != null) {
					    cell.setCellValue(record.getR51_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR51_gbp() != null) {
					    cell.setCellValue(record.getR51_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR51_euro() != null) {
					    cell.setCellValue(record.getR51_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR51_yen() != null) {
					    cell.setCellValue(record.getR51_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR51_c6() != null) {
					    cell.setCellValue(record.getR51_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR51_c7() != null) {
					    cell.setCellValue(record.getR51_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR51_c8() != null) {
					    cell.setCellValue(record.getR51_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR51_total() != null) {
					    cell.setCellValue(record.getR51_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);

					cell = row.getCell(1);
					if (record.getR52_usd() != null) {
					    cell.setCellValue(record.getR52_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR52_zar() != null) {
					    cell.setCellValue(record.getR52_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR52_gbp() != null) {
					    cell.setCellValue(record.getR52_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR52_euro() != null) {
					    cell.setCellValue(record.getR52_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR52_yen() != null) {
					    cell.setCellValue(record.getR52_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR52_c6() != null) {
					    cell.setCellValue(record.getR52_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR52_c7() != null) {
					    cell.setCellValue(record.getR52_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR52_c8() != null) {
					    cell.setCellValue(record.getR52_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR52_total() != null) {
					    cell.setCellValue(record.getR52_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					cell = row.getCell(1);
					if (record.getR53_usd() != null) {
					    cell.setCellValue(record.getR53_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR53_zar() != null) {
					    cell.setCellValue(record.getR53_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR53_gbp() != null) {
					    cell.setCellValue(record.getR53_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR53_euro() != null) {
					    cell.setCellValue(record.getR53_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR53_yen() != null) {
					    cell.setCellValue(record.getR53_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR53_c6() != null) {
					    cell.setCellValue(record.getR53_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR53_c7() != null) {
					    cell.setCellValue(record.getR53_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR53_c8() != null) {
					    cell.setCellValue(record.getR53_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR53_total() != null) {
					    cell.setCellValue(record.getR53_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row54
					row = sheet.getRow(53);

					cell = row.getCell(1);
					if (record.getR54_usd() != null) {
					    cell.setCellValue(record.getR54_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR54_zar() != null) {
					    cell.setCellValue(record.getR54_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR54_gbp() != null) {
					    cell.setCellValue(record.getR54_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR54_euro() != null) {
					    cell.setCellValue(record.getR54_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR54_yen() != null) {
					    cell.setCellValue(record.getR54_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR54_c6() != null) {
					    cell.setCellValue(record.getR54_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR54_c7() != null) {
					    cell.setCellValue(record.getR54_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR54_c8() != null) {
					    cell.setCellValue(record.getR54_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR54_total() != null) {
					    cell.setCellValue(record.getR54_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					cell = row.getCell(1);
					if (record.getR55_usd() != null) {
					    cell.setCellValue(record.getR55_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR55_zar() != null) {
					    cell.setCellValue(record.getR55_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR55_gbp() != null) {
					    cell.setCellValue(record.getR55_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR55_euro() != null) {
					    cell.setCellValue(record.getR55_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR55_yen() != null) {
					    cell.setCellValue(record.getR55_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR55_c6() != null) {
					    cell.setCellValue(record.getR55_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR55_c7() != null) {
					    cell.setCellValue(record.getR55_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR55_c8() != null) {
					    cell.setCellValue(record.getR55_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR55_total() != null) {
					    cell.setCellValue(record.getR55_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);

					cell = row.getCell(1);
					if (record.getR56_usd() != null) {
					    cell.setCellValue(record.getR56_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR56_zar() != null) {
					    cell.setCellValue(record.getR56_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR56_gbp() != null) {
					    cell.setCellValue(record.getR56_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR56_euro() != null) {
					    cell.setCellValue(record.getR56_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR56_yen() != null) {
					    cell.setCellValue(record.getR56_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR56_c6() != null) {
					    cell.setCellValue(record.getR56_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR56_c7() != null) {
					    cell.setCellValue(record.getR56_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR56_c8() != null) {
					    cell.setCellValue(record.getR56_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR56_total() != null) {
					    cell.setCellValue(record.getR56_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					cell = row.getCell(1);
					if (record.getR57_usd() != null) {
					    cell.setCellValue(record.getR57_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR57_zar() != null) {
					    cell.setCellValue(record.getR57_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR57_gbp() != null) {
					    cell.setCellValue(record.getR57_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR57_euro() != null) {
					    cell.setCellValue(record.getR57_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR57_yen() != null) {
					    cell.setCellValue(record.getR57_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR57_c6() != null) {
					    cell.setCellValue(record.getR57_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR57_c7() != null) {
					    cell.setCellValue(record.getR57_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR57_c8() != null) {
					    cell.setCellValue(record.getR57_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR57_total() != null) {
					    cell.setCellValue(record.getR57_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row58
					row = sheet.getRow(57);

					cell = row.getCell(1);
					if (record.getR58_usd() != null) {
					    cell.setCellValue(record.getR58_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR58_zar() != null) {
					    cell.setCellValue(record.getR58_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR58_gbp() != null) {
					    cell.setCellValue(record.getR58_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR58_euro() != null) {
					    cell.setCellValue(record.getR58_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR58_yen() != null) {
					    cell.setCellValue(record.getR58_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR58_c6() != null) {
					    cell.setCellValue(record.getR58_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR58_c7() != null) {
					    cell.setCellValue(record.getR58_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR58_c8() != null) {
					    cell.setCellValue(record.getR58_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR58_total() != null) {
					    cell.setCellValue(record.getR58_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					cell = row.getCell(1);
					if (record.getR59_usd() != null) {
					    cell.setCellValue(record.getR59_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR59_zar() != null) {
					    cell.setCellValue(record.getR59_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR59_gbp() != null) {
					    cell.setCellValue(record.getR59_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR59_euro() != null) {
					    cell.setCellValue(record.getR59_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR59_yen() != null) {
					    cell.setCellValue(record.getR59_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR59_c6() != null) {
					    cell.setCellValue(record.getR59_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR59_c7() != null) {
					    cell.setCellValue(record.getR59_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR59_c8() != null) {
					    cell.setCellValue(record.getR59_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR59_total() != null) {
					    cell.setCellValue(record.getR59_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					cell = row.getCell(1);
					if (record.getR60_usd() != null) {
					    cell.setCellValue(record.getR60_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR60_zar() != null) {
					    cell.setCellValue(record.getR60_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR60_gbp() != null) {
					    cell.setCellValue(record.getR60_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR60_euro() != null) {
					    cell.setCellValue(record.getR60_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR60_yen() != null) {
					    cell.setCellValue(record.getR60_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR60_c6() != null) {
					    cell.setCellValue(record.getR60_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR60_c7() != null) {
					    cell.setCellValue(record.getR60_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR60_c8() != null) {
					    cell.setCellValue(record.getR60_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR60_total() != null) {
					    cell.setCellValue(record.getR60_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					cell = row.getCell(1);
					if (record.getR61_usd() != null) {
					    cell.setCellValue(record.getR61_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR61_zar() != null) {
					    cell.setCellValue(record.getR61_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR61_gbp() != null) {
					    cell.setCellValue(record.getR61_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR61_euro() != null) {
					    cell.setCellValue(record.getR61_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR61_yen() != null) {
					    cell.setCellValue(record.getR61_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR61_c6() != null) {
					    cell.setCellValue(record.getR61_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR61_c7() != null) {
					    cell.setCellValue(record.getR61_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR61_c8() != null) {
					    cell.setCellValue(record.getR61_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR61_total() != null) {
					    cell.setCellValue(record.getR61_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					cell = row.getCell(1);
					if (record.getR62_usd() != null) {
					    cell.setCellValue(record.getR62_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR62_zar() != null) {
					    cell.setCellValue(record.getR62_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR62_gbp() != null) {
					    cell.setCellValue(record.getR62_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR62_euro() != null) {
					    cell.setCellValue(record.getR62_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR62_yen() != null) {
					    cell.setCellValue(record.getR62_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR62_c6() != null) {
					    cell.setCellValue(record.getR62_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR62_c7() != null) {
					    cell.setCellValue(record.getR62_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR62_c8() != null) {
					    cell.setCellValue(record.getR62_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR62_total() != null) {
					    cell.setCellValue(record.getR62_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}


				

					}
					workbook.setForceFormulaRecalculation(true);
				} else {

				}

				// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_LA5ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA5EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_LA5_Archival_Summary_Entity> dataList = BRRS_M_LA5_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA5 report. Returning empty result.");
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
					M_LA5_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL
					
					// row6
					Cell cell = row.getCell(1);
					if (record.getR6_usd() != null) {
						cell.setCellValue(record.getR6_usd().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(2);
					if (record.getR6_zar() != null) {
						cell.setCellValue(record.getR6_zar().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(3);
					if (record.getR6_gbp() != null) {
						cell.setCellValue(record.getR6_gbp().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(4);
					if (record.getR6_euro() != null) {
						cell.setCellValue(record.getR6_euro().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(5);
					if (record.getR6_yen() != null) {
						cell.setCellValue(record.getR6_yen().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(6);
					if (record.getR6_c6() != null) {
						cell.setCellValue(record.getR6_c6().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(7);
					if (record.getR6_c7() != null) {
						cell.setCellValue(record.getR6_c7().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(8);
					if (record.getR6_c8() != null) {
						cell.setCellValue(record.getR6_c8().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
					cell = row.getCell(9);
					if (record.getR6_total() != null) {
						cell.setCellValue(record.getR6_total().doubleValue());

					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}

				
					// row7
					row = sheet.getRow(6);
					 cell = row.getCell(1);
					if (record.getR7_usd() != null) {
					    cell.setCellValue(record.getR7_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR7_zar() != null) {
					    cell.setCellValue(record.getR7_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR7_gbp() != null) {
					    cell.setCellValue(record.getR7_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR7_euro() != null) {
					    cell.setCellValue(record.getR7_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR7_yen() != null) {
					    cell.setCellValue(record.getR7_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR7_c6() != null) {
					    cell.setCellValue(record.getR7_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR7_c7() != null) {
					    cell.setCellValue(record.getR7_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR7_c8() != null) {
					    cell.setCellValue(record.getR7_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR7_total() != null) {
					    cell.setCellValue(record.getR7_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row8
					row = sheet.getRow(7);
					cell = row.getCell(1);
					if (record.getR8_usd() != null) {
					    cell.setCellValue(record.getR8_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR8_zar() != null) {
					    cell.setCellValue(record.getR8_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR8_gbp() != null) {
					    cell.setCellValue(record.getR8_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR8_euro() != null) {
					    cell.setCellValue(record.getR8_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR8_yen() != null) {
					    cell.setCellValue(record.getR8_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR8_c6() != null) {
					    cell.setCellValue(record.getR8_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR8_c7() != null) {
					    cell.setCellValue(record.getR8_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR8_c8() != null) {
					    cell.setCellValue(record.getR8_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR8_total() != null) {
					    cell.setCellValue(record.getR8_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row9
					row = sheet.getRow(8);

					cell = row.getCell(1);
					if (record.getR9_usd() != null) {
					    cell.setCellValue(record.getR9_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR9_zar() != null) {
					    cell.setCellValue(record.getR9_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR9_gbp() != null) {
					    cell.setCellValue(record.getR9_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR9_euro() != null) {
					    cell.setCellValue(record.getR9_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR9_yen() != null) {
					    cell.setCellValue(record.getR9_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR9_c6() != null) {
					    cell.setCellValue(record.getR9_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR9_c7() != null) {
					    cell.setCellValue(record.getR9_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR9_c8() != null) {
					    cell.setCellValue(record.getR9_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR9_total() != null) {
					    cell.setCellValue(record.getR9_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row10
					row = sheet.getRow(9);

					cell = row.getCell(1);
					if (record.getR10_usd() != null) {
					    cell.setCellValue(record.getR10_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR10_zar() != null) {
					    cell.setCellValue(record.getR10_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR10_gbp() != null) {
					    cell.setCellValue(record.getR10_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR10_euro() != null) {
					    cell.setCellValue(record.getR10_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR10_yen() != null) {
					    cell.setCellValue(record.getR10_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR10_c6() != null) {
					    cell.setCellValue(record.getR10_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR10_c7() != null) {
					    cell.setCellValue(record.getR10_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR10_c8() != null) {
					    cell.setCellValue(record.getR10_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR10_total() != null) {
					    cell.setCellValue(record.getR10_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					
					// row11
					row = sheet.getRow(10);

					cell = row.getCell(1);
					if (record.getR11_usd() != null) {
					    cell.setCellValue(record.getR11_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR11_zar() != null) {
					    cell.setCellValue(record.getR11_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR11_gbp() != null) {
					    cell.setCellValue(record.getR11_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR11_euro() != null) {
					    cell.setCellValue(record.getR11_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR11_yen() != null) {
					    cell.setCellValue(record.getR11_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR11_c6() != null) {
					    cell.setCellValue(record.getR11_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR11_c7() != null) {
					    cell.setCellValue(record.getR11_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR11_c8() != null) {
					    cell.setCellValue(record.getR11_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR11_total() != null) {
					    cell.setCellValue(record.getR11_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell = row.getCell(1);
					if (record.getR12_usd() != null) {
					    cell.setCellValue(record.getR12_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR12_zar() != null) {
					    cell.setCellValue(record.getR12_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR12_gbp() != null) {
					    cell.setCellValue(record.getR12_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR12_euro() != null) {
					    cell.setCellValue(record.getR12_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR12_yen() != null) {
					    cell.setCellValue(record.getR12_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR12_c6() != null) {
					    cell.setCellValue(record.getR12_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR12_c7() != null) {
					    cell.setCellValue(record.getR12_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR12_c8() != null) {
					    cell.setCellValue(record.getR12_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR12_total() != null) {
					    cell.setCellValue(record.getR12_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell = row.getCell(1);
					if (record.getR13_usd() != null) {
					    cell.setCellValue(record.getR13_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR13_zar() != null) {
					    cell.setCellValue(record.getR13_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR13_gbp() != null) {
					    cell.setCellValue(record.getR13_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR13_euro() != null) {
					    cell.setCellValue(record.getR13_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR13_yen() != null) {
					    cell.setCellValue(record.getR13_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR13_c6() != null) {
					    cell.setCellValue(record.getR13_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR13_c7() != null) {
					    cell.setCellValue(record.getR13_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR13_c8() != null) {
					    cell.setCellValue(record.getR13_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR13_total() != null) {
					    cell.setCellValue(record.getR13_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell = row.getCell(1);
					if (record.getR14_usd() != null) {
					    cell.setCellValue(record.getR14_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR14_zar() != null) {
					    cell.setCellValue(record.getR14_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR14_gbp() != null) {
					    cell.setCellValue(record.getR14_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR14_euro() != null) {
					    cell.setCellValue(record.getR14_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR14_yen() != null) {
					    cell.setCellValue(record.getR14_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR14_c6() != null) {
					    cell.setCellValue(record.getR14_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR14_c7() != null) {
					    cell.setCellValue(record.getR14_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR14_c8() != null) {
					    cell.setCellValue(record.getR14_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR14_total() != null) {
					    cell.setCellValue(record.getR14_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row15
					row = sheet.getRow(14);

					cell = row.getCell(1);
					if (record.getR15_usd() != null) {
					    cell.setCellValue(record.getR15_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR15_zar() != null) {
					    cell.setCellValue(record.getR15_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR15_gbp() != null) {
					    cell.setCellValue(record.getR15_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR15_euro() != null) {
					    cell.setCellValue(record.getR15_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR15_yen() != null) {
					    cell.setCellValue(record.getR15_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR15_c6() != null) {
					    cell.setCellValue(record.getR15_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR15_c7() != null) {
					    cell.setCellValue(record.getR15_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR15_c8() != null) {
					    cell.setCellValue(record.getR15_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR15_total() != null) {
					    cell.setCellValue(record.getR15_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell = row.getCell(1);
					if (record.getR16_usd() != null) {
					    cell.setCellValue(record.getR16_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR16_zar() != null) {
					    cell.setCellValue(record.getR16_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR16_gbp() != null) {
					    cell.setCellValue(record.getR16_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR16_euro() != null) {
					    cell.setCellValue(record.getR16_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR16_yen() != null) {
					    cell.setCellValue(record.getR16_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR16_c6() != null) {
					    cell.setCellValue(record.getR16_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR16_c7() != null) {
					    cell.setCellValue(record.getR16_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR16_c8() != null) {
					    cell.setCellValue(record.getR16_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR16_total() != null) {
					    cell.setCellValue(record.getR16_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row17
					row = sheet.getRow(16);

					cell = row.getCell(1);
					if (record.getR17_usd() != null) {
					    cell.setCellValue(record.getR17_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR17_zar() != null) {
					    cell.setCellValue(record.getR17_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR17_gbp() != null) {
					    cell.setCellValue(record.getR17_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR17_euro() != null) {
					    cell.setCellValue(record.getR17_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR17_yen() != null) {
					    cell.setCellValue(record.getR17_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR17_c6() != null) {
					    cell.setCellValue(record.getR17_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR17_c7() != null) {
					    cell.setCellValue(record.getR17_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR17_c8() != null) {
					    cell.setCellValue(record.getR17_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR17_total() != null) {
					    cell.setCellValue(record.getR17_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell = row.getCell(1);
					if (record.getR18_usd() != null) {
					    cell.setCellValue(record.getR18_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR18_zar() != null) {
					    cell.setCellValue(record.getR18_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR18_gbp() != null) {
					    cell.setCellValue(record.getR18_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR18_euro() != null) {
					    cell.setCellValue(record.getR18_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR18_yen() != null) {
					    cell.setCellValue(record.getR18_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR18_c6() != null) {
					    cell.setCellValue(record.getR18_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR18_c7() != null) {
					    cell.setCellValue(record.getR18_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR18_c8() != null) {
					    cell.setCellValue(record.getR18_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR18_total() != null) {
					    cell.setCellValue(record.getR18_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row19
					row = sheet.getRow(18);

					cell = row.getCell(1);
					if (record.getR19_usd() != null) {
					    cell.setCellValue(record.getR19_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR19_zar() != null) {
					    cell.setCellValue(record.getR19_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR19_gbp() != null) {
					    cell.setCellValue(record.getR19_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR19_euro() != null) {
					    cell.setCellValue(record.getR19_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR19_yen() != null) {
					    cell.setCellValue(record.getR19_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR19_c6() != null) {
					    cell.setCellValue(record.getR19_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR19_c7() != null) {
					    cell.setCellValue(record.getR19_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR19_c8() != null) {
					    cell.setCellValue(record.getR19_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR19_total() != null) {
					    cell.setCellValue(record.getR19_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell = row.getCell(1);
					if (record.getR20_usd() != null) {
					    cell.setCellValue(record.getR20_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR20_zar() != null) {
					    cell.setCellValue(record.getR20_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR20_gbp() != null) {
					    cell.setCellValue(record.getR20_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR20_euro() != null) {
					    cell.setCellValue(record.getR20_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR20_yen() != null) {
					    cell.setCellValue(record.getR20_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR20_c6() != null) {
					    cell.setCellValue(record.getR20_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR20_c7() != null) {
					    cell.setCellValue(record.getR20_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR20_c8() != null) {
					    cell.setCellValue(record.getR20_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR20_total() != null) {
					    cell.setCellValue(record.getR20_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row21
					row = sheet.getRow(20);

					cell = row.getCell(1);
					if (record.getR21_usd() != null) {
					    cell.setCellValue(record.getR21_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR21_zar() != null) {
					    cell.setCellValue(record.getR21_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR21_gbp() != null) {
					    cell.setCellValue(record.getR21_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR21_euro() != null) {
					    cell.setCellValue(record.getR21_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR21_yen() != null) {
					    cell.setCellValue(record.getR21_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR21_c6() != null) {
					    cell.setCellValue(record.getR21_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR21_c7() != null) {
					    cell.setCellValue(record.getR21_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR21_c8() != null) {
					    cell.setCellValue(record.getR21_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR21_total() != null) {
					    cell.setCellValue(record.getR21_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					cell = row.getCell(1);
					if (record.getR22_usd() != null) {
					    cell.setCellValue(record.getR22_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR22_zar() != null) {
					    cell.setCellValue(record.getR22_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR22_gbp() != null) {
					    cell.setCellValue(record.getR22_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR22_euro() != null) {
					    cell.setCellValue(record.getR22_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR22_yen() != null) {
					    cell.setCellValue(record.getR22_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR22_c6() != null) {
					    cell.setCellValue(record.getR22_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR22_c7() != null) {
					    cell.setCellValue(record.getR22_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR22_c8() != null) {
					    cell.setCellValue(record.getR22_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR22_total() != null) {
					    cell.setCellValue(record.getR22_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row23
					row = sheet.getRow(22);

					cell = row.getCell(1);
					if (record.getR23_usd() != null) {
					    cell.setCellValue(record.getR23_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR23_zar() != null) {
					    cell.setCellValue(record.getR23_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR23_gbp() != null) {
					    cell.setCellValue(record.getR23_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR23_euro() != null) {
					    cell.setCellValue(record.getR23_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR23_yen() != null) {
					    cell.setCellValue(record.getR23_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR23_c6() != null) {
					    cell.setCellValue(record.getR23_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR23_c7() != null) {
					    cell.setCellValue(record.getR23_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR23_c8() != null) {
					    cell.setCellValue(record.getR23_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR23_total() != null) {
					    cell.setCellValue(record.getR23_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					cell = row.getCell(1);
					if (record.getR24_usd() != null) {
					    cell.setCellValue(record.getR24_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR24_zar() != null) {
					    cell.setCellValue(record.getR24_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR24_gbp() != null) {
					    cell.setCellValue(record.getR24_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR24_euro() != null) {
					    cell.setCellValue(record.getR24_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR24_yen() != null) {
					    cell.setCellValue(record.getR24_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR24_c6() != null) {
					    cell.setCellValue(record.getR24_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR24_c7() != null) {
					    cell.setCellValue(record.getR24_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR24_c8() != null) {
					    cell.setCellValue(record.getR24_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR24_total() != null) {
					    cell.setCellValue(record.getR24_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell = row.getCell(1);
					if (record.getR25_usd() != null) {
					    cell.setCellValue(record.getR25_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR25_zar() != null) {
					    cell.setCellValue(record.getR25_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR25_gbp() != null) {
					    cell.setCellValue(record.getR25_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR25_euro() != null) {
					    cell.setCellValue(record.getR25_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR25_yen() != null) {
					    cell.setCellValue(record.getR25_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR25_c6() != null) {
					    cell.setCellValue(record.getR25_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR25_c7() != null) {
					    cell.setCellValue(record.getR25_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR25_c8() != null) {
					    cell.setCellValue(record.getR25_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR25_total() != null) {
					    cell.setCellValue(record.getR25_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell = row.getCell(1);
					if (record.getR26_usd() != null) {
					    cell.setCellValue(record.getR26_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR26_zar() != null) {
					    cell.setCellValue(record.getR26_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR26_gbp() != null) {
					    cell.setCellValue(record.getR26_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR26_euro() != null) {
					    cell.setCellValue(record.getR26_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR26_yen() != null) {
					    cell.setCellValue(record.getR26_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR26_c6() != null) {
					    cell.setCellValue(record.getR26_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR26_c7() != null) {
					    cell.setCellValue(record.getR26_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR26_c8() != null) {
					    cell.setCellValue(record.getR26_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR26_total() != null) {
					    cell.setCellValue(record.getR26_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell = row.getCell(1);
					if (record.getR27_usd() != null) {
					    cell.setCellValue(record.getR27_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR27_zar() != null) {
					    cell.setCellValue(record.getR27_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR27_gbp() != null) {
					    cell.setCellValue(record.getR27_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR27_euro() != null) {
					    cell.setCellValue(record.getR27_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR27_yen() != null) {
					    cell.setCellValue(record.getR27_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR27_c6() != null) {
					    cell.setCellValue(record.getR27_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR27_c7() != null) {
					    cell.setCellValue(record.getR27_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR27_c8() != null) {
					    cell.setCellValue(record.getR27_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR27_total() != null) {
					    cell.setCellValue(record.getR27_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					
					// row28
					row = sheet.getRow(27);

					cell = row.getCell(1);
					if (record.getR28_usd() != null) {
					    cell.setCellValue(record.getR28_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR28_zar() != null) {
					    cell.setCellValue(record.getR28_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR28_gbp() != null) {
					    cell.setCellValue(record.getR28_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR28_euro() != null) {
					    cell.setCellValue(record.getR28_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR28_yen() != null) {
					    cell.setCellValue(record.getR28_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR28_c6() != null) {
					    cell.setCellValue(record.getR28_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR28_c7() != null) {
					    cell.setCellValue(record.getR28_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR28_c8() != null) {
					    cell.setCellValue(record.getR28_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR28_total() != null) {
					    cell.setCellValue(record.getR28_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell = row.getCell(1);
					if (record.getR29_usd() != null) {
					    cell.setCellValue(record.getR29_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR29_zar() != null) {
					    cell.setCellValue(record.getR29_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR29_gbp() != null) {
					    cell.setCellValue(record.getR29_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR29_euro() != null) {
					    cell.setCellValue(record.getR29_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR29_yen() != null) {
					    cell.setCellValue(record.getR29_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR29_c6() != null) {
					    cell.setCellValue(record.getR29_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR29_c7() != null) {
					    cell.setCellValue(record.getR29_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR29_c8() != null) {
					    cell.setCellValue(record.getR29_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR29_total() != null) {
					    cell.setCellValue(record.getR29_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell = row.getCell(1);
					if (record.getR30_usd() != null) {
					    cell.setCellValue(record.getR30_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR30_zar() != null) {
					    cell.setCellValue(record.getR30_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR30_gbp() != null) {
					    cell.setCellValue(record.getR30_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR30_euro() != null) {
					    cell.setCellValue(record.getR30_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR30_yen() != null) {
					    cell.setCellValue(record.getR30_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR30_c6() != null) {
					    cell.setCellValue(record.getR30_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR30_c7() != null) {
					    cell.setCellValue(record.getR30_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR30_c8() != null) {
					    cell.setCellValue(record.getR30_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR30_total() != null) {
					    cell.setCellValue(record.getR30_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					
					// row31
					row = sheet.getRow(30);

					cell = row.getCell(1);
					if (record.getR31_usd() != null) {
					    cell.setCellValue(record.getR31_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR31_zar() != null) {
					    cell.setCellValue(record.getR31_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR31_gbp() != null) {
					    cell.setCellValue(record.getR31_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR31_euro() != null) {
					    cell.setCellValue(record.getR31_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR31_yen() != null) {
					    cell.setCellValue(record.getR31_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR31_c6() != null) {
					    cell.setCellValue(record.getR31_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR31_c7() != null) {
					    cell.setCellValue(record.getR31_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR31_c8() != null) {
					    cell.setCellValue(record.getR31_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR31_total() != null) {
					    cell.setCellValue(record.getR31_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell = row.getCell(1);
					if (record.getR32_usd() != null) {
					    cell.setCellValue(record.getR32_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR32_zar() != null) {
					    cell.setCellValue(record.getR32_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR32_gbp() != null) {
					    cell.setCellValue(record.getR32_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR32_euro() != null) {
					    cell.setCellValue(record.getR32_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR32_yen() != null) {
					    cell.setCellValue(record.getR32_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR32_c6() != null) {
					    cell.setCellValue(record.getR32_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR32_c7() != null) {
					    cell.setCellValue(record.getR32_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR32_c8() != null) {
					    cell.setCellValue(record.getR32_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR32_total() != null) {
					    cell.setCellValue(record.getR32_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell = row.getCell(1);
					if (record.getR33_usd() != null) {
					    cell.setCellValue(record.getR33_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR33_zar() != null) {
					    cell.setCellValue(record.getR33_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR33_gbp() != null) {
					    cell.setCellValue(record.getR33_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR33_euro() != null) {
					    cell.setCellValue(record.getR33_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR33_yen() != null) {
					    cell.setCellValue(record.getR33_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR33_c6() != null) {
					    cell.setCellValue(record.getR33_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR33_c7() != null) {
					    cell.setCellValue(record.getR33_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR33_c8() != null) {
					    cell.setCellValue(record.getR33_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR33_total() != null) {
					    cell.setCellValue(record.getR33_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row34
					row = sheet.getRow(33);

					cell = row.getCell(1);
					if (record.getR34_usd() != null) {
					    cell.setCellValue(record.getR34_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR34_zar() != null) {
					    cell.setCellValue(record.getR34_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR34_gbp() != null) {
					    cell.setCellValue(record.getR34_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR34_euro() != null) {
					    cell.setCellValue(record.getR34_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR34_yen() != null) {
					    cell.setCellValue(record.getR34_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR34_c6() != null) {
					    cell.setCellValue(record.getR34_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR34_c7() != null) {
					    cell.setCellValue(record.getR34_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR34_c8() != null) {
					    cell.setCellValue(record.getR34_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR34_total() != null) {
					    cell.setCellValue(record.getR34_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell = row.getCell(1);
					if (record.getR35_usd() != null) {
					    cell.setCellValue(record.getR35_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR35_zar() != null) {
					    cell.setCellValue(record.getR35_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR35_gbp() != null) {
					    cell.setCellValue(record.getR35_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR35_euro() != null) {
					    cell.setCellValue(record.getR35_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR35_yen() != null) {
					    cell.setCellValue(record.getR35_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR35_c6() != null) {
					    cell.setCellValue(record.getR35_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR35_c7() != null) {
					    cell.setCellValue(record.getR35_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR35_c8() != null) {
					    cell.setCellValue(record.getR35_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR35_total() != null) {
					    cell.setCellValue(record.getR35_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell = row.getCell(1);
					if (record.getR36_usd() != null) {
					    cell.setCellValue(record.getR36_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR36_zar() != null) {
					    cell.setCellValue(record.getR36_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR36_gbp() != null) {
					    cell.setCellValue(record.getR36_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR36_euro() != null) {
					    cell.setCellValue(record.getR36_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR36_yen() != null) {
					    cell.setCellValue(record.getR36_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR36_c6() != null) {
					    cell.setCellValue(record.getR36_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR36_c7() != null) {
					    cell.setCellValue(record.getR36_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR36_c8() != null) {
					    cell.setCellValue(record.getR36_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR36_total() != null) {
					    cell.setCellValue(record.getR36_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row37
					row = sheet.getRow(36);

					cell = row.getCell(1);
					if (record.getR37_usd() != null) {
					    cell.setCellValue(record.getR37_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR37_zar() != null) {
					    cell.setCellValue(record.getR37_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR37_gbp() != null) {
					    cell.setCellValue(record.getR37_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR37_euro() != null) {
					    cell.setCellValue(record.getR37_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR37_yen() != null) {
					    cell.setCellValue(record.getR37_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR37_c6() != null) {
					    cell.setCellValue(record.getR37_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR37_c7() != null) {
					    cell.setCellValue(record.getR37_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR37_c8() != null) {
					    cell.setCellValue(record.getR37_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR37_total() != null) {
					    cell.setCellValue(record.getR37_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);

					cell = row.getCell(1);
					if (record.getR38_usd() != null) {
					    cell.setCellValue(record.getR38_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR38_zar() != null) {
					    cell.setCellValue(record.getR38_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR38_gbp() != null) {
					    cell.setCellValue(record.getR38_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR38_euro() != null) {
					    cell.setCellValue(record.getR38_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR38_yen() != null) {
					    cell.setCellValue(record.getR38_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR38_c6() != null) {
					    cell.setCellValue(record.getR38_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR38_c7() != null) {
					    cell.setCellValue(record.getR38_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR38_c8() != null) {
					    cell.setCellValue(record.getR38_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR38_total() != null) {
					    cell.setCellValue(record.getR38_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					cell = row.getCell(1);
					if (record.getR39_usd() != null) {
					    cell.setCellValue(record.getR39_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR39_zar() != null) {
					    cell.setCellValue(record.getR39_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR39_gbp() != null) {
					    cell.setCellValue(record.getR39_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR39_euro() != null) {
					    cell.setCellValue(record.getR39_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR39_yen() != null) {
					    cell.setCellValue(record.getR39_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR39_c6() != null) {
					    cell.setCellValue(record.getR39_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR39_c7() != null) {
					    cell.setCellValue(record.getR39_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR39_c8() != null) {
					    cell.setCellValue(record.getR39_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR39_total() != null) {
					    cell.setCellValue(record.getR39_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row40
					row = sheet.getRow(39);

					cell = row.getCell(1);
					if (record.getR40_usd() != null) {
					    cell.setCellValue(record.getR40_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR40_zar() != null) {
					    cell.setCellValue(record.getR40_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR40_gbp() != null) {
					    cell.setCellValue(record.getR40_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR40_euro() != null) {
					    cell.setCellValue(record.getR40_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR40_yen() != null) {
					    cell.setCellValue(record.getR40_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR40_c6() != null) {
					    cell.setCellValue(record.getR40_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR40_c7() != null) {
					    cell.setCellValue(record.getR40_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR40_c8() != null) {
					    cell.setCellValue(record.getR40_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR40_total() != null) {
					    cell.setCellValue(record.getR40_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cell = row.getCell(1);
					if (record.getR41_usd() != null) {
					    cell.setCellValue(record.getR41_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR41_zar() != null) {
					    cell.setCellValue(record.getR41_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR41_gbp() != null) {
					    cell.setCellValue(record.getR41_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR41_euro() != null) {
					    cell.setCellValue(record.getR41_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR41_yen() != null) {
					    cell.setCellValue(record.getR41_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR41_c6() != null) {
					    cell.setCellValue(record.getR41_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR41_c7() != null) {
					    cell.setCellValue(record.getR41_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR41_c8() != null) {
					    cell.setCellValue(record.getR41_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR41_total() != null) {
					    cell.setCellValue(record.getR41_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cell = row.getCell(1);
					if (record.getR42_usd() != null) {
					    cell.setCellValue(record.getR42_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR42_zar() != null) {
					    cell.setCellValue(record.getR42_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR42_gbp() != null) {
					    cell.setCellValue(record.getR42_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR42_euro() != null) {
					    cell.setCellValue(record.getR42_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR42_yen() != null) {
					    cell.setCellValue(record.getR42_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR42_c6() != null) {
					    cell.setCellValue(record.getR42_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR42_c7() != null) {
					    cell.setCellValue(record.getR42_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR42_c8() != null) {
					    cell.setCellValue(record.getR42_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR42_total() != null) {
					    cell.setCellValue(record.getR42_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}// row43
					row = sheet.getRow(42);

					cell = row.getCell(1);
					if (record.getR43_usd() != null) {
					    cell.setCellValue(record.getR43_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR43_zar() != null) {
					    cell.setCellValue(record.getR43_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR43_gbp() != null) {
					    cell.setCellValue(record.getR43_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR43_euro() != null) {
					    cell.setCellValue(record.getR43_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR43_yen() != null) {
					    cell.setCellValue(record.getR43_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR43_c6() != null) {
					    cell.setCellValue(record.getR43_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR43_c7() != null) {
					    cell.setCellValue(record.getR43_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR43_c8() != null) {
					    cell.setCellValue(record.getR43_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR43_total() != null) {
					    cell.setCellValue(record.getR43_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);

					cell = row.getCell(1);
					if (record.getR44_usd() != null) {
					    cell.setCellValue(record.getR44_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR44_zar() != null) {
					    cell.setCellValue(record.getR44_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR44_gbp() != null) {
					    cell.setCellValue(record.getR44_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR44_euro() != null) {
					    cell.setCellValue(record.getR44_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR44_yen() != null) {
					    cell.setCellValue(record.getR44_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR44_c6() != null) {
					    cell.setCellValue(record.getR44_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR44_c7() != null) {
					    cell.setCellValue(record.getR44_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR44_c8() != null) {
					    cell.setCellValue(record.getR44_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR44_total() != null) {
					    cell.setCellValue(record.getR44_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					cell = row.getCell(1);
					if (record.getR45_usd() != null) {
					    cell.setCellValue(record.getR45_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR45_zar() != null) {
					    cell.setCellValue(record.getR45_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR45_gbp() != null) {
					    cell.setCellValue(record.getR45_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR45_euro() != null) {
					    cell.setCellValue(record.getR45_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR45_yen() != null) {
					    cell.setCellValue(record.getR45_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR45_c6() != null) {
					    cell.setCellValue(record.getR45_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR45_c7() != null) {
					    cell.setCellValue(record.getR45_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR45_c8() != null) {
					    cell.setCellValue(record.getR45_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR45_total() != null) {
					    cell.setCellValue(record.getR45_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row46
					row = sheet.getRow(45);

					cell = row.getCell(1);
					if (record.getR46_usd() != null) {
					    cell.setCellValue(record.getR46_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR46_zar() != null) {
					    cell.setCellValue(record.getR46_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR46_gbp() != null) {
					    cell.setCellValue(record.getR46_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR46_euro() != null) {
					    cell.setCellValue(record.getR46_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR46_yen() != null) {
					    cell.setCellValue(record.getR46_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR46_c6() != null) {
					    cell.setCellValue(record.getR46_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR46_c7() != null) {
					    cell.setCellValue(record.getR46_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR46_c8() != null) {
					    cell.setCellValue(record.getR46_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR46_total() != null) {
					    cell.setCellValue(record.getR46_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					cell = row.getCell(1);
					if (record.getR47_usd() != null) {
					    cell.setCellValue(record.getR47_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR47_zar() != null) {
					    cell.setCellValue(record.getR47_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR47_gbp() != null) {
					    cell.setCellValue(record.getR47_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR47_euro() != null) {
					    cell.setCellValue(record.getR47_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR47_yen() != null) {
					    cell.setCellValue(record.getR47_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR47_c6() != null) {
					    cell.setCellValue(record.getR47_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR47_c7() != null) {
					    cell.setCellValue(record.getR47_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR47_c8() != null) {
					    cell.setCellValue(record.getR47_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR47_total() != null) {
					    cell.setCellValue(record.getR47_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);

					cell = row.getCell(1);
					if (record.getR48_usd() != null) {
					    cell.setCellValue(record.getR48_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR48_zar() != null) {
					    cell.setCellValue(record.getR48_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR48_gbp() != null) {
					    cell.setCellValue(record.getR48_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR48_euro() != null) {
					    cell.setCellValue(record.getR48_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR48_yen() != null) {
					    cell.setCellValue(record.getR48_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR48_c6() != null) {
					    cell.setCellValue(record.getR48_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR48_c7() != null) {
					    cell.setCellValue(record.getR48_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR48_c8() != null) {
					    cell.setCellValue(record.getR48_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR48_total() != null) {
					    cell.setCellValue(record.getR48_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);

					cell = row.getCell(1);
					if (record.getR49_usd() != null) {
					    cell.setCellValue(record.getR49_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR49_zar() != null) {
					    cell.setCellValue(record.getR49_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR49_gbp() != null) {
					    cell.setCellValue(record.getR49_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR49_euro() != null) {
					    cell.setCellValue(record.getR49_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR49_yen() != null) {
					    cell.setCellValue(record.getR49_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR49_c6() != null) {
					    cell.setCellValue(record.getR49_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR49_c7() != null) {
					    cell.setCellValue(record.getR49_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR49_c8() != null) {
					    cell.setCellValue(record.getR49_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR49_total() != null) {
					    cell.setCellValue(record.getR49_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row50
					row = sheet.getRow(49);

					cell = row.getCell(1);
					if (record.getR50_usd() != null) {
					    cell.setCellValue(record.getR50_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR50_zar() != null) {
					    cell.setCellValue(record.getR50_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR50_gbp() != null) {
					    cell.setCellValue(record.getR50_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR50_euro() != null) {
					    cell.setCellValue(record.getR50_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR50_yen() != null) {
					    cell.setCellValue(record.getR50_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR50_c6() != null) {
					    cell.setCellValue(record.getR50_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR50_c7() != null) {
					    cell.setCellValue(record.getR50_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR50_c8() != null) {
					    cell.setCellValue(record.getR50_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR50_total() != null) {
					    cell.setCellValue(record.getR50_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					cell = row.getCell(1);
					if (record.getR51_usd() != null) {
					    cell.setCellValue(record.getR51_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR51_zar() != null) {
					    cell.setCellValue(record.getR51_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR51_gbp() != null) {
					    cell.setCellValue(record.getR51_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR51_euro() != null) {
					    cell.setCellValue(record.getR51_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR51_yen() != null) {
					    cell.setCellValue(record.getR51_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR51_c6() != null) {
					    cell.setCellValue(record.getR51_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR51_c7() != null) {
					    cell.setCellValue(record.getR51_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR51_c8() != null) {
					    cell.setCellValue(record.getR51_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR51_total() != null) {
					    cell.setCellValue(record.getR51_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);

					cell = row.getCell(1);
					if (record.getR52_usd() != null) {
					    cell.setCellValue(record.getR52_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR52_zar() != null) {
					    cell.setCellValue(record.getR52_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR52_gbp() != null) {
					    cell.setCellValue(record.getR52_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR52_euro() != null) {
					    cell.setCellValue(record.getR52_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR52_yen() != null) {
					    cell.setCellValue(record.getR52_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR52_c6() != null) {
					    cell.setCellValue(record.getR52_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR52_c7() != null) {
					    cell.setCellValue(record.getR52_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR52_c8() != null) {
					    cell.setCellValue(record.getR52_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR52_total() != null) {
					    cell.setCellValue(record.getR52_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					cell = row.getCell(1);
					if (record.getR53_usd() != null) {
					    cell.setCellValue(record.getR53_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR53_zar() != null) {
					    cell.setCellValue(record.getR53_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR53_gbp() != null) {
					    cell.setCellValue(record.getR53_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR53_euro() != null) {
					    cell.setCellValue(record.getR53_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR53_yen() != null) {
					    cell.setCellValue(record.getR53_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR53_c6() != null) {
					    cell.setCellValue(record.getR53_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR53_c7() != null) {
					    cell.setCellValue(record.getR53_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR53_c8() != null) {
					    cell.setCellValue(record.getR53_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR53_total() != null) {
					    cell.setCellValue(record.getR53_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					
					// row54
					row = sheet.getRow(53);

					cell = row.getCell(1);
					if (record.getR54_usd() != null) {
					    cell.setCellValue(record.getR54_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR54_zar() != null) {
					    cell.setCellValue(record.getR54_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR54_gbp() != null) {
					    cell.setCellValue(record.getR54_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR54_euro() != null) {
					    cell.setCellValue(record.getR54_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR54_yen() != null) {
					    cell.setCellValue(record.getR54_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR54_c6() != null) {
					    cell.setCellValue(record.getR54_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR54_c7() != null) {
					    cell.setCellValue(record.getR54_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR54_c8() != null) {
					    cell.setCellValue(record.getR54_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR54_total() != null) {
					    cell.setCellValue(record.getR54_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					cell = row.getCell(1);
					if (record.getR55_usd() != null) {
					    cell.setCellValue(record.getR55_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR55_zar() != null) {
					    cell.setCellValue(record.getR55_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR55_gbp() != null) {
					    cell.setCellValue(record.getR55_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR55_euro() != null) {
					    cell.setCellValue(record.getR55_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR55_yen() != null) {
					    cell.setCellValue(record.getR55_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR55_c6() != null) {
					    cell.setCellValue(record.getR55_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR55_c7() != null) {
					    cell.setCellValue(record.getR55_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR55_c8() != null) {
					    cell.setCellValue(record.getR55_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR55_total() != null) {
					    cell.setCellValue(record.getR55_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);

					cell = row.getCell(1);
					if (record.getR56_usd() != null) {
					    cell.setCellValue(record.getR56_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR56_zar() != null) {
					    cell.setCellValue(record.getR56_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR56_gbp() != null) {
					    cell.setCellValue(record.getR56_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR56_euro() != null) {
					    cell.setCellValue(record.getR56_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR56_yen() != null) {
					    cell.setCellValue(record.getR56_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR56_c6() != null) {
					    cell.setCellValue(record.getR56_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR56_c7() != null) {
					    cell.setCellValue(record.getR56_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR56_c8() != null) {
					    cell.setCellValue(record.getR56_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR56_total() != null) {
					    cell.setCellValue(record.getR56_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					cell = row.getCell(1);
					if (record.getR57_usd() != null) {
					    cell.setCellValue(record.getR57_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR57_zar() != null) {
					    cell.setCellValue(record.getR57_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR57_gbp() != null) {
					    cell.setCellValue(record.getR57_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR57_euro() != null) {
					    cell.setCellValue(record.getR57_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR57_yen() != null) {
					    cell.setCellValue(record.getR57_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR57_c6() != null) {
					    cell.setCellValue(record.getR57_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR57_c7() != null) {
					    cell.setCellValue(record.getR57_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR57_c8() != null) {
					    cell.setCellValue(record.getR57_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR57_total() != null) {
					    cell.setCellValue(record.getR57_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}
					// row58
					row = sheet.getRow(57);

					cell = row.getCell(1);
					if (record.getR58_usd() != null) {
					    cell.setCellValue(record.getR58_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR58_zar() != null) {
					    cell.setCellValue(record.getR58_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR58_gbp() != null) {
					    cell.setCellValue(record.getR58_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR58_euro() != null) {
					    cell.setCellValue(record.getR58_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR58_yen() != null) {
					    cell.setCellValue(record.getR58_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR58_c6() != null) {
					    cell.setCellValue(record.getR58_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR58_c7() != null) {
					    cell.setCellValue(record.getR58_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR58_c8() != null) {
					    cell.setCellValue(record.getR58_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR58_total() != null) {
					    cell.setCellValue(record.getR58_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					cell = row.getCell(1);
					if (record.getR59_usd() != null) {
					    cell.setCellValue(record.getR59_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR59_zar() != null) {
					    cell.setCellValue(record.getR59_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR59_gbp() != null) {
					    cell.setCellValue(record.getR59_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR59_euro() != null) {
					    cell.setCellValue(record.getR59_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR59_yen() != null) {
					    cell.setCellValue(record.getR59_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR59_c6() != null) {
					    cell.setCellValue(record.getR59_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR59_c7() != null) {
					    cell.setCellValue(record.getR59_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR59_c8() != null) {
					    cell.setCellValue(record.getR59_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR59_total() != null) {
					    cell.setCellValue(record.getR59_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					cell = row.getCell(1);
					if (record.getR60_usd() != null) {
					    cell.setCellValue(record.getR60_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR60_zar() != null) {
					    cell.setCellValue(record.getR60_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR60_gbp() != null) {
					    cell.setCellValue(record.getR60_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR60_euro() != null) {
					    cell.setCellValue(record.getR60_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR60_yen() != null) {
					    cell.setCellValue(record.getR60_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR60_c6() != null) {
					    cell.setCellValue(record.getR60_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR60_c7() != null) {
					    cell.setCellValue(record.getR60_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR60_c8() != null) {
					    cell.setCellValue(record.getR60_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR60_total() != null) {
					    cell.setCellValue(record.getR60_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					cell = row.getCell(1);
					if (record.getR61_usd() != null) {
					    cell.setCellValue(record.getR61_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR61_zar() != null) {
					    cell.setCellValue(record.getR61_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR61_gbp() != null) {
					    cell.setCellValue(record.getR61_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR61_euro() != null) {
					    cell.setCellValue(record.getR61_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR61_yen() != null) {
					    cell.setCellValue(record.getR61_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR61_c6() != null) {
					    cell.setCellValue(record.getR61_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR61_c7() != null) {
					    cell.setCellValue(record.getR61_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR61_c8() != null) {
					    cell.setCellValue(record.getR61_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR61_total() != null) {
					    cell.setCellValue(record.getR61_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					cell = row.getCell(1);
					if (record.getR62_usd() != null) {
					    cell.setCellValue(record.getR62_usd().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(2);
					if (record.getR62_zar() != null) {
					    cell.setCellValue(record.getR62_zar().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(3);
					if (record.getR62_gbp() != null) {
					    cell.setCellValue(record.getR62_gbp().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(4);
					if (record.getR62_euro() != null) {
					    cell.setCellValue(record.getR62_euro().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(5);
					if (record.getR62_yen() != null) {
					    cell.setCellValue(record.getR62_yen().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(6);
					if (record.getR62_c6() != null) {
					    cell.setCellValue(record.getR62_c6().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(7);
					if (record.getR62_c7() != null) {
					    cell.setCellValue(record.getR62_c7().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(8);
					if (record.getR62_c8() != null) {
					    cell.setCellValue(record.getR62_c8().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}

					cell = row.getCell(9);
					if (record.getR62_total() != null) {
					    cell.setCellValue(record.getR62_total().doubleValue());
					} else {
					    cell.setCellValue("");
					    cell.setCellStyle(textStyle);
					}



				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_LA5EmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_LA5_Archival_Summary_Entity> dataList = BRRS_M_LA5_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_LA5 report. Returning empty result.");
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
					M_LA5_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//EMAIL
// R10 Col E

	

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	// Resub Format excel
//	public byte[] BRRS_M_LA5ResubExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");
//
//		if ("email".equalsIgnoreCase(format) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_LA5ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
//		}
//
//		List<M_LA5_Resub_Summary_Entity> dataList = brrs_M_LA5_resub_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for M_LA5 report. Returning empty result.");
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
////					M_LA5_Resub_Summary_Entity record = dataList.get(i);
//					System.out.println("rownumber=" + startRow + i);
//					System.out.println("rownumber=" + startRow + i);
//					Row row = sheet.getRow(startRow + i);
//					if (row == null) {
//						row = sheet.createRow(startRow + i);
//					}
////NORMAL
//					
//					// row6
//					Cell cell = row.getCell(1);
//					if (record.getR6_usd() != null) {
//						cell.setCellValue(record.getR6_usd().doubleValue());
//
//					} else {
//						cell.setCellValue("");
//						cell.setCellStyle(textStyle);
//					}
//					cell = row.getCell(2);
//					if (record.getR6_zar() != null) {
//						cell.setCellValue(record.getR6_zar().doubleValue());
//
//					} else {
//						cell.setCellValue("");
//						cell.setCellStyle(textStyle);
//					}
//					cell = row.getCell(3);
//					if (record.getR6_gbp() != null) {
//						cell.setCellValue(record.getR6_gbp().doubleValue());
//
//					} else {
//						cell.setCellValue("");
//						cell.setCellStyle(textStyle);
//					}
//					cell = row.getCell(4);
//					if (record.getR6_euro() != null) {
//						cell.setCellValue(record.getR6_euro().doubleValue());
//
//					} else {
//						cell.setCellValue("");
//						cell.setCellStyle(textStyle);
//					}
//					cell = row.getCell(5);
//					if (record.getR6_yen() != null) {
//						cell.setCellValue(record.getR6_yen().doubleValue());
//
//					} else {
//						cell.setCellValue("");
//						cell.setCellStyle(textStyle);
//					}
//					cell = row.getCell(6);
//					if (record.getR6_c6() != null) {
//						cell.setCellValue(record.getR6_c6().doubleValue());
//
//					} else {
//						cell.setCellValue("");
//						cell.setCellStyle(textStyle);
//					}
//					cell = row.getCell(7);
//					if (record.getR6_c7() != null) {
//						cell.setCellValue(record.getR6_c7().doubleValue());
//
//					} else {
//						cell.setCellValue("");
//						cell.setCellStyle(textStyle);
//					}
//					cell = row.getCell(8);
//					if (record.getR6_c8() != null) {
//						cell.setCellValue(record.getR6_c8().doubleValue());
//
//					} else {
//						cell.setCellValue("");
//						cell.setCellStyle(textStyle);
//					}
//					cell = row.getCell(9);
//					if (record.getR6_total() != null) {
//						cell.setCellValue(record.getR6_total().doubleValue());
//
//					} else {
//						cell.setCellValue("");
//						cell.setCellStyle(textStyle);
//					}
//
//				
//					// row7
//					row = sheet.getRow(6);
//					 cell = row.getCell(1);
//					if (record.getR7_usd() != null) {
//					    cell.setCellValue(record.getR7_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR7_zar() != null) {
//					    cell.setCellValue(record.getR7_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR7_gbp() != null) {
//					    cell.setCellValue(record.getR7_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR7_euro() != null) {
//					    cell.setCellValue(record.getR7_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR7_yen() != null) {
//					    cell.setCellValue(record.getR7_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR7_c6() != null) {
//					    cell.setCellValue(record.getR7_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR7_c7() != null) {
//					    cell.setCellValue(record.getR7_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR7_c8() != null) {
//					    cell.setCellValue(record.getR7_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR7_total() != null) {
//					    cell.setCellValue(record.getR7_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row8
//					row = sheet.getRow(7);
//					cell = row.getCell(1);
//					if (record.getR8_usd() != null) {
//					    cell.setCellValue(record.getR8_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR8_zar() != null) {
//					    cell.setCellValue(record.getR8_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR8_gbp() != null) {
//					    cell.setCellValue(record.getR8_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR8_euro() != null) {
//					    cell.setCellValue(record.getR8_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR8_yen() != null) {
//					    cell.setCellValue(record.getR8_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR8_c6() != null) {
//					    cell.setCellValue(record.getR8_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR8_c7() != null) {
//					    cell.setCellValue(record.getR8_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR8_c8() != null) {
//					    cell.setCellValue(record.getR8_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR8_total() != null) {
//					    cell.setCellValue(record.getR8_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					
//					// row9
//					row = sheet.getRow(8);
//
//					cell = row.getCell(1);
//					if (record.getR9_usd() != null) {
//					    cell.setCellValue(record.getR9_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR9_zar() != null) {
//					    cell.setCellValue(record.getR9_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR9_gbp() != null) {
//					    cell.setCellValue(record.getR9_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR9_euro() != null) {
//					    cell.setCellValue(record.getR9_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR9_yen() != null) {
//					    cell.setCellValue(record.getR9_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR9_c6() != null) {
//					    cell.setCellValue(record.getR9_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR9_c7() != null) {
//					    cell.setCellValue(record.getR9_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR9_c8() != null) {
//					    cell.setCellValue(record.getR9_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR9_total() != null) {
//					    cell.setCellValue(record.getR9_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row10
//					row = sheet.getRow(9);
//
//					cell = row.getCell(1);
//					if (record.getR10_usd() != null) {
//					    cell.setCellValue(record.getR10_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR10_zar() != null) {
//					    cell.setCellValue(record.getR10_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR10_gbp() != null) {
//					    cell.setCellValue(record.getR10_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR10_euro() != null) {
//					    cell.setCellValue(record.getR10_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR10_yen() != null) {
//					    cell.setCellValue(record.getR10_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR10_c6() != null) {
//					    cell.setCellValue(record.getR10_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR10_c7() != null) {
//					    cell.setCellValue(record.getR10_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR10_c8() != null) {
//					    cell.setCellValue(record.getR10_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR10_total() != null) {
//					    cell.setCellValue(record.getR10_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					
//					
//					// row11
//					row = sheet.getRow(10);
//
//					cell = row.getCell(1);
//					if (record.getR11_usd() != null) {
//					    cell.setCellValue(record.getR11_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR11_zar() != null) {
//					    cell.setCellValue(record.getR11_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR11_gbp() != null) {
//					    cell.setCellValue(record.getR11_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR11_euro() != null) {
//					    cell.setCellValue(record.getR11_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR11_yen() != null) {
//					    cell.setCellValue(record.getR11_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR11_c6() != null) {
//					    cell.setCellValue(record.getR11_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR11_c7() != null) {
//					    cell.setCellValue(record.getR11_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR11_c8() != null) {
//					    cell.setCellValue(record.getR11_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR11_total() != null) {
//					    cell.setCellValue(record.getR11_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row12
//					row = sheet.getRow(11);
//
//					cell = row.getCell(1);
//					if (record.getR12_usd() != null) {
//					    cell.setCellValue(record.getR12_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR12_zar() != null) {
//					    cell.setCellValue(record.getR12_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR12_gbp() != null) {
//					    cell.setCellValue(record.getR12_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR12_euro() != null) {
//					    cell.setCellValue(record.getR12_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR12_yen() != null) {
//					    cell.setCellValue(record.getR12_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR12_c6() != null) {
//					    cell.setCellValue(record.getR12_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR12_c7() != null) {
//					    cell.setCellValue(record.getR12_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR12_c8() != null) {
//					    cell.setCellValue(record.getR12_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR12_total() != null) {
//					    cell.setCellValue(record.getR12_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row13
//					row = sheet.getRow(12);
//
//					cell = row.getCell(1);
//					if (record.getR13_usd() != null) {
//					    cell.setCellValue(record.getR13_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR13_zar() != null) {
//					    cell.setCellValue(record.getR13_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR13_gbp() != null) {
//					    cell.setCellValue(record.getR13_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR13_euro() != null) {
//					    cell.setCellValue(record.getR13_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR13_yen() != null) {
//					    cell.setCellValue(record.getR13_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR13_c6() != null) {
//					    cell.setCellValue(record.getR13_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR13_c7() != null) {
//					    cell.setCellValue(record.getR13_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR13_c8() != null) {
//					    cell.setCellValue(record.getR13_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR13_total() != null) {
//					    cell.setCellValue(record.getR13_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row14
//					row = sheet.getRow(13);
//
//					cell = row.getCell(1);
//					if (record.getR14_usd() != null) {
//					    cell.setCellValue(record.getR14_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR14_zar() != null) {
//					    cell.setCellValue(record.getR14_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR14_gbp() != null) {
//					    cell.setCellValue(record.getR14_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR14_euro() != null) {
//					    cell.setCellValue(record.getR14_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR14_yen() != null) {
//					    cell.setCellValue(record.getR14_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR14_c6() != null) {
//					    cell.setCellValue(record.getR14_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR14_c7() != null) {
//					    cell.setCellValue(record.getR14_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR14_c8() != null) {
//					    cell.setCellValue(record.getR14_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR14_total() != null) {
//					    cell.setCellValue(record.getR14_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row15
//					row = sheet.getRow(14);
//
//					cell = row.getCell(1);
//					if (record.getR15_usd() != null) {
//					    cell.setCellValue(record.getR15_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR15_zar() != null) {
//					    cell.setCellValue(record.getR15_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR15_gbp() != null) {
//					    cell.setCellValue(record.getR15_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR15_euro() != null) {
//					    cell.setCellValue(record.getR15_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR15_yen() != null) {
//					    cell.setCellValue(record.getR15_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR15_c6() != null) {
//					    cell.setCellValue(record.getR15_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR15_c7() != null) {
//					    cell.setCellValue(record.getR15_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR15_c8() != null) {
//					    cell.setCellValue(record.getR15_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR15_total() != null) {
//					    cell.setCellValue(record.getR15_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row16
//					row = sheet.getRow(15);
//
//					cell = row.getCell(1);
//					if (record.getR16_usd() != null) {
//					    cell.setCellValue(record.getR16_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR16_zar() != null) {
//					    cell.setCellValue(record.getR16_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR16_gbp() != null) {
//					    cell.setCellValue(record.getR16_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR16_euro() != null) {
//					    cell.setCellValue(record.getR16_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR16_yen() != null) {
//					    cell.setCellValue(record.getR16_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR16_c6() != null) {
//					    cell.setCellValue(record.getR16_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR16_c7() != null) {
//					    cell.setCellValue(record.getR16_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR16_c8() != null) {
//					    cell.setCellValue(record.getR16_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR16_total() != null) {
//					    cell.setCellValue(record.getR16_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row17
//					row = sheet.getRow(16);
//
//					cell = row.getCell(1);
//					if (record.getR17_usd() != null) {
//					    cell.setCellValue(record.getR17_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR17_zar() != null) {
//					    cell.setCellValue(record.getR17_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR17_gbp() != null) {
//					    cell.setCellValue(record.getR17_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR17_euro() != null) {
//					    cell.setCellValue(record.getR17_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR17_yen() != null) {
//					    cell.setCellValue(record.getR17_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR17_c6() != null) {
//					    cell.setCellValue(record.getR17_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR17_c7() != null) {
//					    cell.setCellValue(record.getR17_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR17_c8() != null) {
//					    cell.setCellValue(record.getR17_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR17_total() != null) {
//					    cell.setCellValue(record.getR17_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row18
//					row = sheet.getRow(17);
//
//					cell = row.getCell(1);
//					if (record.getR18_usd() != null) {
//					    cell.setCellValue(record.getR18_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR18_zar() != null) {
//					    cell.setCellValue(record.getR18_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR18_gbp() != null) {
//					    cell.setCellValue(record.getR18_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR18_euro() != null) {
//					    cell.setCellValue(record.getR18_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR18_yen() != null) {
//					    cell.setCellValue(record.getR18_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR18_c6() != null) {
//					    cell.setCellValue(record.getR18_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR18_c7() != null) {
//					    cell.setCellValue(record.getR18_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR18_c8() != null) {
//					    cell.setCellValue(record.getR18_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR18_total() != null) {
//					    cell.setCellValue(record.getR18_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row19
//					row = sheet.getRow(18);
//
//					cell = row.getCell(1);
//					if (record.getR19_usd() != null) {
//					    cell.setCellValue(record.getR19_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR19_zar() != null) {
//					    cell.setCellValue(record.getR19_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR19_gbp() != null) {
//					    cell.setCellValue(record.getR19_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR19_euro() != null) {
//					    cell.setCellValue(record.getR19_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR19_yen() != null) {
//					    cell.setCellValue(record.getR19_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR19_c6() != null) {
//					    cell.setCellValue(record.getR19_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR19_c7() != null) {
//					    cell.setCellValue(record.getR19_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR19_c8() != null) {
//					    cell.setCellValue(record.getR19_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR19_total() != null) {
//					    cell.setCellValue(record.getR19_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row20
//					row = sheet.getRow(19);
//
//					cell = row.getCell(1);
//					if (record.getR20_usd() != null) {
//					    cell.setCellValue(record.getR20_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR20_zar() != null) {
//					    cell.setCellValue(record.getR20_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR20_gbp() != null) {
//					    cell.setCellValue(record.getR20_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR20_euro() != null) {
//					    cell.setCellValue(record.getR20_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR20_yen() != null) {
//					    cell.setCellValue(record.getR20_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR20_c6() != null) {
//					    cell.setCellValue(record.getR20_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR20_c7() != null) {
//					    cell.setCellValue(record.getR20_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR20_c8() != null) {
//					    cell.setCellValue(record.getR20_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR20_total() != null) {
//					    cell.setCellValue(record.getR20_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row21
//					row = sheet.getRow(20);
//
//					cell = row.getCell(1);
//					if (record.getR21_usd() != null) {
//					    cell.setCellValue(record.getR21_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR21_zar() != null) {
//					    cell.setCellValue(record.getR21_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR21_gbp() != null) {
//					    cell.setCellValue(record.getR21_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR21_euro() != null) {
//					    cell.setCellValue(record.getR21_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR21_yen() != null) {
//					    cell.setCellValue(record.getR21_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR21_c6() != null) {
//					    cell.setCellValue(record.getR21_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR21_c7() != null) {
//					    cell.setCellValue(record.getR21_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR21_c8() != null) {
//					    cell.setCellValue(record.getR21_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR21_total() != null) {
//					    cell.setCellValue(record.getR21_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row22
//					row = sheet.getRow(21);
//
//					cell = row.getCell(1);
//					if (record.getR22_usd() != null) {
//					    cell.setCellValue(record.getR22_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR22_zar() != null) {
//					    cell.setCellValue(record.getR22_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR22_gbp() != null) {
//					    cell.setCellValue(record.getR22_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR22_euro() != null) {
//					    cell.setCellValue(record.getR22_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR22_yen() != null) {
//					    cell.setCellValue(record.getR22_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR22_c6() != null) {
//					    cell.setCellValue(record.getR22_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR22_c7() != null) {
//					    cell.setCellValue(record.getR22_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR22_c8() != null) {
//					    cell.setCellValue(record.getR22_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR22_total() != null) {
//					    cell.setCellValue(record.getR22_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row23
//					row = sheet.getRow(22);
//
//					cell = row.getCell(1);
//					if (record.getR23_usd() != null) {
//					    cell.setCellValue(record.getR23_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR23_zar() != null) {
//					    cell.setCellValue(record.getR23_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR23_gbp() != null) {
//					    cell.setCellValue(record.getR23_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR23_euro() != null) {
//					    cell.setCellValue(record.getR23_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR23_yen() != null) {
//					    cell.setCellValue(record.getR23_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR23_c6() != null) {
//					    cell.setCellValue(record.getR23_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR23_c7() != null) {
//					    cell.setCellValue(record.getR23_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR23_c8() != null) {
//					    cell.setCellValue(record.getR23_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR23_total() != null) {
//					    cell.setCellValue(record.getR23_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row24
//					row = sheet.getRow(23);
//
//					cell = row.getCell(1);
//					if (record.getR24_usd() != null) {
//					    cell.setCellValue(record.getR24_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR24_zar() != null) {
//					    cell.setCellValue(record.getR24_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR24_gbp() != null) {
//					    cell.setCellValue(record.getR24_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR24_euro() != null) {
//					    cell.setCellValue(record.getR24_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR24_yen() != null) {
//					    cell.setCellValue(record.getR24_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR24_c6() != null) {
//					    cell.setCellValue(record.getR24_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR24_c7() != null) {
//					    cell.setCellValue(record.getR24_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR24_c8() != null) {
//					    cell.setCellValue(record.getR24_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR24_total() != null) {
//					    cell.setCellValue(record.getR24_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row25
//					row = sheet.getRow(24);
//
//					cell = row.getCell(1);
//					if (record.getR25_usd() != null) {
//					    cell.setCellValue(record.getR25_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR25_zar() != null) {
//					    cell.setCellValue(record.getR25_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR25_gbp() != null) {
//					    cell.setCellValue(record.getR25_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR25_euro() != null) {
//					    cell.setCellValue(record.getR25_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR25_yen() != null) {
//					    cell.setCellValue(record.getR25_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR25_c6() != null) {
//					    cell.setCellValue(record.getR25_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR25_c7() != null) {
//					    cell.setCellValue(record.getR25_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR25_c8() != null) {
//					    cell.setCellValue(record.getR25_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR25_total() != null) {
//					    cell.setCellValue(record.getR25_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row26
//					row = sheet.getRow(25);
//
//					cell = row.getCell(1);
//					if (record.getR26_usd() != null) {
//					    cell.setCellValue(record.getR26_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR26_zar() != null) {
//					    cell.setCellValue(record.getR26_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR26_gbp() != null) {
//					    cell.setCellValue(record.getR26_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR26_euro() != null) {
//					    cell.setCellValue(record.getR26_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR26_yen() != null) {
//					    cell.setCellValue(record.getR26_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR26_c6() != null) {
//					    cell.setCellValue(record.getR26_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR26_c7() != null) {
//					    cell.setCellValue(record.getR26_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR26_c8() != null) {
//					    cell.setCellValue(record.getR26_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR26_total() != null) {
//					    cell.setCellValue(record.getR26_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row27
//					row = sheet.getRow(26);
//
//					cell = row.getCell(1);
//					if (record.getR27_usd() != null) {
//					    cell.setCellValue(record.getR27_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR27_zar() != null) {
//					    cell.setCellValue(record.getR27_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR27_gbp() != null) {
//					    cell.setCellValue(record.getR27_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR27_euro() != null) {
//					    cell.setCellValue(record.getR27_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR27_yen() != null) {
//					    cell.setCellValue(record.getR27_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR27_c6() != null) {
//					    cell.setCellValue(record.getR27_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR27_c7() != null) {
//					    cell.setCellValue(record.getR27_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR27_c8() != null) {
//					    cell.setCellValue(record.getR27_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR27_total() != null) {
//					    cell.setCellValue(record.getR27_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					
//					
//					// row28
//					row = sheet.getRow(27);
//
//					cell = row.getCell(1);
//					if (record.getR28_usd() != null) {
//					    cell.setCellValue(record.getR28_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR28_zar() != null) {
//					    cell.setCellValue(record.getR28_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR28_gbp() != null) {
//					    cell.setCellValue(record.getR28_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR28_euro() != null) {
//					    cell.setCellValue(record.getR28_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR28_yen() != null) {
//					    cell.setCellValue(record.getR28_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR28_c6() != null) {
//					    cell.setCellValue(record.getR28_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR28_c7() != null) {
//					    cell.setCellValue(record.getR28_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR28_c8() != null) {
//					    cell.setCellValue(record.getR28_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR28_total() != null) {
//					    cell.setCellValue(record.getR28_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row29
//					row = sheet.getRow(28);
//
//					cell = row.getCell(1);
//					if (record.getR29_usd() != null) {
//					    cell.setCellValue(record.getR29_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR29_zar() != null) {
//					    cell.setCellValue(record.getR29_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR29_gbp() != null) {
//					    cell.setCellValue(record.getR29_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR29_euro() != null) {
//					    cell.setCellValue(record.getR29_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR29_yen() != null) {
//					    cell.setCellValue(record.getR29_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR29_c6() != null) {
//					    cell.setCellValue(record.getR29_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR29_c7() != null) {
//					    cell.setCellValue(record.getR29_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR29_c8() != null) {
//					    cell.setCellValue(record.getR29_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR29_total() != null) {
//					    cell.setCellValue(record.getR29_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row30
//					row = sheet.getRow(29);
//
//					cell = row.getCell(1);
//					if (record.getR30_usd() != null) {
//					    cell.setCellValue(record.getR30_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR30_zar() != null) {
//					    cell.setCellValue(record.getR30_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR30_gbp() != null) {
//					    cell.setCellValue(record.getR30_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR30_euro() != null) {
//					    cell.setCellValue(record.getR30_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR30_yen() != null) {
//					    cell.setCellValue(record.getR30_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR30_c6() != null) {
//					    cell.setCellValue(record.getR30_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR30_c7() != null) {
//					    cell.setCellValue(record.getR30_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR30_c8() != null) {
//					    cell.setCellValue(record.getR30_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR30_total() != null) {
//					    cell.setCellValue(record.getR30_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					
//					
//					// row31
//					row = sheet.getRow(30);
//
//					cell = row.getCell(1);
//					if (record.getR31_usd() != null) {
//					    cell.setCellValue(record.getR31_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR31_zar() != null) {
//					    cell.setCellValue(record.getR31_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR31_gbp() != null) {
//					    cell.setCellValue(record.getR31_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR31_euro() != null) {
//					    cell.setCellValue(record.getR31_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR31_yen() != null) {
//					    cell.setCellValue(record.getR31_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR31_c6() != null) {
//					    cell.setCellValue(record.getR31_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR31_c7() != null) {
//					    cell.setCellValue(record.getR31_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR31_c8() != null) {
//					    cell.setCellValue(record.getR31_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR31_total() != null) {
//					    cell.setCellValue(record.getR31_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row32
//					row = sheet.getRow(31);
//
//					cell = row.getCell(1);
//					if (record.getR32_usd() != null) {
//					    cell.setCellValue(record.getR32_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR32_zar() != null) {
//					    cell.setCellValue(record.getR32_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR32_gbp() != null) {
//					    cell.setCellValue(record.getR32_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR32_euro() != null) {
//					    cell.setCellValue(record.getR32_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR32_yen() != null) {
//					    cell.setCellValue(record.getR32_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR32_c6() != null) {
//					    cell.setCellValue(record.getR32_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR32_c7() != null) {
//					    cell.setCellValue(record.getR32_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR32_c8() != null) {
//					    cell.setCellValue(record.getR32_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR32_total() != null) {
//					    cell.setCellValue(record.getR32_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row33
//					row = sheet.getRow(32);
//
//					cell = row.getCell(1);
//					if (record.getR33_usd() != null) {
//					    cell.setCellValue(record.getR33_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR33_zar() != null) {
//					    cell.setCellValue(record.getR33_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR33_gbp() != null) {
//					    cell.setCellValue(record.getR33_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR33_euro() != null) {
//					    cell.setCellValue(record.getR33_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR33_yen() != null) {
//					    cell.setCellValue(record.getR33_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR33_c6() != null) {
//					    cell.setCellValue(record.getR33_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR33_c7() != null) {
//					    cell.setCellValue(record.getR33_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR33_c8() != null) {
//					    cell.setCellValue(record.getR33_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR33_total() != null) {
//					    cell.setCellValue(record.getR33_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					
//					// row34
//					row = sheet.getRow(33);
//
//					cell = row.getCell(1);
//					if (record.getR34_usd() != null) {
//					    cell.setCellValue(record.getR34_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR34_zar() != null) {
//					    cell.setCellValue(record.getR34_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR34_gbp() != null) {
//					    cell.setCellValue(record.getR34_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR34_euro() != null) {
//					    cell.setCellValue(record.getR34_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR34_yen() != null) {
//					    cell.setCellValue(record.getR34_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR34_c6() != null) {
//					    cell.setCellValue(record.getR34_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR34_c7() != null) {
//					    cell.setCellValue(record.getR34_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR34_c8() != null) {
//					    cell.setCellValue(record.getR34_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR34_total() != null) {
//					    cell.setCellValue(record.getR34_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row35
//					row = sheet.getRow(34);
//
//					cell = row.getCell(1);
//					if (record.getR35_usd() != null) {
//					    cell.setCellValue(record.getR35_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR35_zar() != null) {
//					    cell.setCellValue(record.getR35_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR35_gbp() != null) {
//					    cell.setCellValue(record.getR35_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR35_euro() != null) {
//					    cell.setCellValue(record.getR35_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR35_yen() != null) {
//					    cell.setCellValue(record.getR35_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR35_c6() != null) {
//					    cell.setCellValue(record.getR35_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR35_c7() != null) {
//					    cell.setCellValue(record.getR35_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR35_c8() != null) {
//					    cell.setCellValue(record.getR35_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR35_total() != null) {
//					    cell.setCellValue(record.getR35_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row36
//					row = sheet.getRow(35);
//
//					cell = row.getCell(1);
//					if (record.getR36_usd() != null) {
//					    cell.setCellValue(record.getR36_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR36_zar() != null) {
//					    cell.setCellValue(record.getR36_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR36_gbp() != null) {
//					    cell.setCellValue(record.getR36_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR36_euro() != null) {
//					    cell.setCellValue(record.getR36_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR36_yen() != null) {
//					    cell.setCellValue(record.getR36_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR36_c6() != null) {
//					    cell.setCellValue(record.getR36_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR36_c7() != null) {
//					    cell.setCellValue(record.getR36_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR36_c8() != null) {
//					    cell.setCellValue(record.getR36_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR36_total() != null) {
//					    cell.setCellValue(record.getR36_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					
//					// row37
//					row = sheet.getRow(36);
//
//					cell = row.getCell(1);
//					if (record.getR37_usd() != null) {
//					    cell.setCellValue(record.getR37_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR37_zar() != null) {
//					    cell.setCellValue(record.getR37_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR37_gbp() != null) {
//					    cell.setCellValue(record.getR37_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR37_euro() != null) {
//					    cell.setCellValue(record.getR37_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR37_yen() != null) {
//					    cell.setCellValue(record.getR37_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR37_c6() != null) {
//					    cell.setCellValue(record.getR37_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR37_c7() != null) {
//					    cell.setCellValue(record.getR37_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR37_c8() != null) {
//					    cell.setCellValue(record.getR37_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR37_total() != null) {
//					    cell.setCellValue(record.getR37_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row38
//					row = sheet.getRow(37);
//
//					cell = row.getCell(1);
//					if (record.getR38_usd() != null) {
//					    cell.setCellValue(record.getR38_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR38_zar() != null) {
//					    cell.setCellValue(record.getR38_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR38_gbp() != null) {
//					    cell.setCellValue(record.getR38_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR38_euro() != null) {
//					    cell.setCellValue(record.getR38_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR38_yen() != null) {
//					    cell.setCellValue(record.getR38_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR38_c6() != null) {
//					    cell.setCellValue(record.getR38_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR38_c7() != null) {
//					    cell.setCellValue(record.getR38_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR38_c8() != null) {
//					    cell.setCellValue(record.getR38_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR38_total() != null) {
//					    cell.setCellValue(record.getR38_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row39
//					row = sheet.getRow(38);
//
//					cell = row.getCell(1);
//					if (record.getR39_usd() != null) {
//					    cell.setCellValue(record.getR39_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR39_zar() != null) {
//					    cell.setCellValue(record.getR39_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR39_gbp() != null) {
//					    cell.setCellValue(record.getR39_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR39_euro() != null) {
//					    cell.setCellValue(record.getR39_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR39_yen() != null) {
//					    cell.setCellValue(record.getR39_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR39_c6() != null) {
//					    cell.setCellValue(record.getR39_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR39_c7() != null) {
//					    cell.setCellValue(record.getR39_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR39_c8() != null) {
//					    cell.setCellValue(record.getR39_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR39_total() != null) {
//					    cell.setCellValue(record.getR39_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row40
//					row = sheet.getRow(39);
//
//					cell = row.getCell(1);
//					if (record.getR40_usd() != null) {
//					    cell.setCellValue(record.getR40_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR40_zar() != null) {
//					    cell.setCellValue(record.getR40_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR40_gbp() != null) {
//					    cell.setCellValue(record.getR40_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR40_euro() != null) {
//					    cell.setCellValue(record.getR40_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR40_yen() != null) {
//					    cell.setCellValue(record.getR40_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR40_c6() != null) {
//					    cell.setCellValue(record.getR40_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR40_c7() != null) {
//					    cell.setCellValue(record.getR40_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR40_c8() != null) {
//					    cell.setCellValue(record.getR40_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR40_total() != null) {
//					    cell.setCellValue(record.getR40_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row41
//					row = sheet.getRow(40);
//
//					cell = row.getCell(1);
//					if (record.getR41_usd() != null) {
//					    cell.setCellValue(record.getR41_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR41_zar() != null) {
//					    cell.setCellValue(record.getR41_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR41_gbp() != null) {
//					    cell.setCellValue(record.getR41_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR41_euro() != null) {
//					    cell.setCellValue(record.getR41_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR41_yen() != null) {
//					    cell.setCellValue(record.getR41_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR41_c6() != null) {
//					    cell.setCellValue(record.getR41_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR41_c7() != null) {
//					    cell.setCellValue(record.getR41_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR41_c8() != null) {
//					    cell.setCellValue(record.getR41_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR41_total() != null) {
//					    cell.setCellValue(record.getR41_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row42
//					row = sheet.getRow(41);
//
//					cell = row.getCell(1);
//					if (record.getR42_usd() != null) {
//					    cell.setCellValue(record.getR42_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR42_zar() != null) {
//					    cell.setCellValue(record.getR42_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR42_gbp() != null) {
//					    cell.setCellValue(record.getR42_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR42_euro() != null) {
//					    cell.setCellValue(record.getR42_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR42_yen() != null) {
//					    cell.setCellValue(record.getR42_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR42_c6() != null) {
//					    cell.setCellValue(record.getR42_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR42_c7() != null) {
//					    cell.setCellValue(record.getR42_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR42_c8() != null) {
//					    cell.setCellValue(record.getR42_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR42_total() != null) {
//					    cell.setCellValue(record.getR42_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}// row43
//					row = sheet.getRow(42);
//
//					cell = row.getCell(1);
//					if (record.getR43_usd() != null) {
//					    cell.setCellValue(record.getR43_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR43_zar() != null) {
//					    cell.setCellValue(record.getR43_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR43_gbp() != null) {
//					    cell.setCellValue(record.getR43_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR43_euro() != null) {
//					    cell.setCellValue(record.getR43_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR43_yen() != null) {
//					    cell.setCellValue(record.getR43_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR43_c6() != null) {
//					    cell.setCellValue(record.getR43_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR43_c7() != null) {
//					    cell.setCellValue(record.getR43_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR43_c8() != null) {
//					    cell.setCellValue(record.getR43_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR43_total() != null) {
//					    cell.setCellValue(record.getR43_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row44
//					row = sheet.getRow(43);
//
//					cell = row.getCell(1);
//					if (record.getR44_usd() != null) {
//					    cell.setCellValue(record.getR44_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR44_zar() != null) {
//					    cell.setCellValue(record.getR44_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR44_gbp() != null) {
//					    cell.setCellValue(record.getR44_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR44_euro() != null) {
//					    cell.setCellValue(record.getR44_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR44_yen() != null) {
//					    cell.setCellValue(record.getR44_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR44_c6() != null) {
//					    cell.setCellValue(record.getR44_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR44_c7() != null) {
//					    cell.setCellValue(record.getR44_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR44_c8() != null) {
//					    cell.setCellValue(record.getR44_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR44_total() != null) {
//					    cell.setCellValue(record.getR44_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row45
//					row = sheet.getRow(44);
//
//					cell = row.getCell(1);
//					if (record.getR45_usd() != null) {
//					    cell.setCellValue(record.getR45_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR45_zar() != null) {
//					    cell.setCellValue(record.getR45_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR45_gbp() != null) {
//					    cell.setCellValue(record.getR45_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR45_euro() != null) {
//					    cell.setCellValue(record.getR45_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR45_yen() != null) {
//					    cell.setCellValue(record.getR45_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR45_c6() != null) {
//					    cell.setCellValue(record.getR45_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR45_c7() != null) {
//					    cell.setCellValue(record.getR45_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR45_c8() != null) {
//					    cell.setCellValue(record.getR45_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR45_total() != null) {
//					    cell.setCellValue(record.getR45_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row46
//					row = sheet.getRow(45);
//
//					cell = row.getCell(1);
//					if (record.getR46_usd() != null) {
//					    cell.setCellValue(record.getR46_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR46_zar() != null) {
//					    cell.setCellValue(record.getR46_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR46_gbp() != null) {
//					    cell.setCellValue(record.getR46_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR46_euro() != null) {
//					    cell.setCellValue(record.getR46_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR46_yen() != null) {
//					    cell.setCellValue(record.getR46_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR46_c6() != null) {
//					    cell.setCellValue(record.getR46_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR46_c7() != null) {
//					    cell.setCellValue(record.getR46_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR46_c8() != null) {
//					    cell.setCellValue(record.getR46_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR46_total() != null) {
//					    cell.setCellValue(record.getR46_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row47
//					row = sheet.getRow(46);
//
//					cell = row.getCell(1);
//					if (record.getR47_usd() != null) {
//					    cell.setCellValue(record.getR47_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR47_zar() != null) {
//					    cell.setCellValue(record.getR47_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR47_gbp() != null) {
//					    cell.setCellValue(record.getR47_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR47_euro() != null) {
//					    cell.setCellValue(record.getR47_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR47_yen() != null) {
//					    cell.setCellValue(record.getR47_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR47_c6() != null) {
//					    cell.setCellValue(record.getR47_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR47_c7() != null) {
//					    cell.setCellValue(record.getR47_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR47_c8() != null) {
//					    cell.setCellValue(record.getR47_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR47_total() != null) {
//					    cell.setCellValue(record.getR47_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row48
//					row = sheet.getRow(47);
//
//					cell = row.getCell(1);
//					if (record.getR48_usd() != null) {
//					    cell.setCellValue(record.getR48_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR48_zar() != null) {
//					    cell.setCellValue(record.getR48_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR48_gbp() != null) {
//					    cell.setCellValue(record.getR48_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR48_euro() != null) {
//					    cell.setCellValue(record.getR48_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR48_yen() != null) {
//					    cell.setCellValue(record.getR48_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR48_c6() != null) {
//					    cell.setCellValue(record.getR48_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR48_c7() != null) {
//					    cell.setCellValue(record.getR48_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR48_c8() != null) {
//					    cell.setCellValue(record.getR48_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR48_total() != null) {
//					    cell.setCellValue(record.getR48_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row49
//					row = sheet.getRow(48);
//
//					cell = row.getCell(1);
//					if (record.getR49_usd() != null) {
//					    cell.setCellValue(record.getR49_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR49_zar() != null) {
//					    cell.setCellValue(record.getR49_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR49_gbp() != null) {
//					    cell.setCellValue(record.getR49_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR49_euro() != null) {
//					    cell.setCellValue(record.getR49_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR49_yen() != null) {
//					    cell.setCellValue(record.getR49_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR49_c6() != null) {
//					    cell.setCellValue(record.getR49_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR49_c7() != null) {
//					    cell.setCellValue(record.getR49_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR49_c8() != null) {
//					    cell.setCellValue(record.getR49_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR49_total() != null) {
//					    cell.setCellValue(record.getR49_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					
//					// row50
//					row = sheet.getRow(49);
//
//					cell = row.getCell(1);
//					if (record.getR50_usd() != null) {
//					    cell.setCellValue(record.getR50_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR50_zar() != null) {
//					    cell.setCellValue(record.getR50_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR50_gbp() != null) {
//					    cell.setCellValue(record.getR50_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR50_euro() != null) {
//					    cell.setCellValue(record.getR50_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR50_yen() != null) {
//					    cell.setCellValue(record.getR50_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR50_c6() != null) {
//					    cell.setCellValue(record.getR50_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR50_c7() != null) {
//					    cell.setCellValue(record.getR50_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR50_c8() != null) {
//					    cell.setCellValue(record.getR50_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR50_total() != null) {
//					    cell.setCellValue(record.getR50_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row51
//					row = sheet.getRow(50);
//
//					cell = row.getCell(1);
//					if (record.getR51_usd() != null) {
//					    cell.setCellValue(record.getR51_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR51_zar() != null) {
//					    cell.setCellValue(record.getR51_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR51_gbp() != null) {
//					    cell.setCellValue(record.getR51_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR51_euro() != null) {
//					    cell.setCellValue(record.getR51_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR51_yen() != null) {
//					    cell.setCellValue(record.getR51_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR51_c6() != null) {
//					    cell.setCellValue(record.getR51_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR51_c7() != null) {
//					    cell.setCellValue(record.getR51_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR51_c8() != null) {
//					    cell.setCellValue(record.getR51_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR51_total() != null) {
//					    cell.setCellValue(record.getR51_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row52
//					row = sheet.getRow(51);
//
//					cell = row.getCell(1);
//					if (record.getR52_usd() != null) {
//					    cell.setCellValue(record.getR52_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR52_zar() != null) {
//					    cell.setCellValue(record.getR52_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR52_gbp() != null) {
//					    cell.setCellValue(record.getR52_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR52_euro() != null) {
//					    cell.setCellValue(record.getR52_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR52_yen() != null) {
//					    cell.setCellValue(record.getR52_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR52_c6() != null) {
//					    cell.setCellValue(record.getR52_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR52_c7() != null) {
//					    cell.setCellValue(record.getR52_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR52_c8() != null) {
//					    cell.setCellValue(record.getR52_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR52_total() != null) {
//					    cell.setCellValue(record.getR52_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row53
//					row = sheet.getRow(52);
//
//					cell = row.getCell(1);
//					if (record.getR53_usd() != null) {
//					    cell.setCellValue(record.getR53_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR53_zar() != null) {
//					    cell.setCellValue(record.getR53_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR53_gbp() != null) {
//					    cell.setCellValue(record.getR53_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR53_euro() != null) {
//					    cell.setCellValue(record.getR53_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR53_yen() != null) {
//					    cell.setCellValue(record.getR53_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR53_c6() != null) {
//					    cell.setCellValue(record.getR53_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR53_c7() != null) {
//					    cell.setCellValue(record.getR53_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR53_c8() != null) {
//					    cell.setCellValue(record.getR53_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR53_total() != null) {
//					    cell.setCellValue(record.getR53_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					
//					// row54
//					row = sheet.getRow(53);
//
//					cell = row.getCell(1);
//					if (record.getR54_usd() != null) {
//					    cell.setCellValue(record.getR54_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR54_zar() != null) {
//					    cell.setCellValue(record.getR54_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR54_gbp() != null) {
//					    cell.setCellValue(record.getR54_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR54_euro() != null) {
//					    cell.setCellValue(record.getR54_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR54_yen() != null) {
//					    cell.setCellValue(record.getR54_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR54_c6() != null) {
//					    cell.setCellValue(record.getR54_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR54_c7() != null) {
//					    cell.setCellValue(record.getR54_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR54_c8() != null) {
//					    cell.setCellValue(record.getR54_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR54_total() != null) {
//					    cell.setCellValue(record.getR54_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row55
//					row = sheet.getRow(54);
//
//					cell = row.getCell(1);
//					if (record.getR55_usd() != null) {
//					    cell.setCellValue(record.getR55_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR55_zar() != null) {
//					    cell.setCellValue(record.getR55_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR55_gbp() != null) {
//					    cell.setCellValue(record.getR55_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR55_euro() != null) {
//					    cell.setCellValue(record.getR55_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR55_yen() != null) {
//					    cell.setCellValue(record.getR55_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR55_c6() != null) {
//					    cell.setCellValue(record.getR55_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR55_c7() != null) {
//					    cell.setCellValue(record.getR55_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR55_c8() != null) {
//					    cell.setCellValue(record.getR55_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR55_total() != null) {
//					    cell.setCellValue(record.getR55_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row56
//					row = sheet.getRow(55);
//
//					cell = row.getCell(1);
//					if (record.getR56_usd() != null) {
//					    cell.setCellValue(record.getR56_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR56_zar() != null) {
//					    cell.setCellValue(record.getR56_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR56_gbp() != null) {
//					    cell.setCellValue(record.getR56_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR56_euro() != null) {
//					    cell.setCellValue(record.getR56_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR56_yen() != null) {
//					    cell.setCellValue(record.getR56_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR56_c6() != null) {
//					    cell.setCellValue(record.getR56_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR56_c7() != null) {
//					    cell.setCellValue(record.getR56_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR56_c8() != null) {
//					    cell.setCellValue(record.getR56_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR56_total() != null) {
//					    cell.setCellValue(record.getR56_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row57
//					row = sheet.getRow(56);
//
//					cell = row.getCell(1);
//					if (record.getR57_usd() != null) {
//					    cell.setCellValue(record.getR57_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR57_zar() != null) {
//					    cell.setCellValue(record.getR57_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR57_gbp() != null) {
//					    cell.setCellValue(record.getR57_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR57_euro() != null) {
//					    cell.setCellValue(record.getR57_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR57_yen() != null) {
//					    cell.setCellValue(record.getR57_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR57_c6() != null) {
//					    cell.setCellValue(record.getR57_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR57_c7() != null) {
//					    cell.setCellValue(record.getR57_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR57_c8() != null) {
//					    cell.setCellValue(record.getR57_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR57_total() != null) {
//					    cell.setCellValue(record.getR57_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//					// row58
//					row = sheet.getRow(57);
//
//					cell = row.getCell(1);
//					if (record.getR58_usd() != null) {
//					    cell.setCellValue(record.getR58_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR58_zar() != null) {
//					    cell.setCellValue(record.getR58_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR58_gbp() != null) {
//					    cell.setCellValue(record.getR58_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR58_euro() != null) {
//					    cell.setCellValue(record.getR58_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR58_yen() != null) {
//					    cell.setCellValue(record.getR58_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR58_c6() != null) {
//					    cell.setCellValue(record.getR58_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR58_c7() != null) {
//					    cell.setCellValue(record.getR58_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR58_c8() != null) {
//					    cell.setCellValue(record.getR58_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR58_total() != null) {
//					    cell.setCellValue(record.getR58_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row59
//					row = sheet.getRow(58);
//
//					cell = row.getCell(1);
//					if (record.getR59_usd() != null) {
//					    cell.setCellValue(record.getR59_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR59_zar() != null) {
//					    cell.setCellValue(record.getR59_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR59_gbp() != null) {
//					    cell.setCellValue(record.getR59_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR59_euro() != null) {
//					    cell.setCellValue(record.getR59_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR59_yen() != null) {
//					    cell.setCellValue(record.getR59_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR59_c6() != null) {
//					    cell.setCellValue(record.getR59_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR59_c7() != null) {
//					    cell.setCellValue(record.getR59_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR59_c8() != null) {
//					    cell.setCellValue(record.getR59_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR59_total() != null) {
//					    cell.setCellValue(record.getR59_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row60
//					row = sheet.getRow(59);
//
//					cell = row.getCell(1);
//					if (record.getR60_usd() != null) {
//					    cell.setCellValue(record.getR60_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR60_zar() != null) {
//					    cell.setCellValue(record.getR60_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR60_gbp() != null) {
//					    cell.setCellValue(record.getR60_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR60_euro() != null) {
//					    cell.setCellValue(record.getR60_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR60_yen() != null) {
//					    cell.setCellValue(record.getR60_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR60_c6() != null) {
//					    cell.setCellValue(record.getR60_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR60_c7() != null) {
//					    cell.setCellValue(record.getR60_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR60_c8() != null) {
//					    cell.setCellValue(record.getR60_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR60_total() != null) {
//					    cell.setCellValue(record.getR60_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row61
//					row = sheet.getRow(60);
//
//					cell = row.getCell(1);
//					if (record.getR61_usd() != null) {
//					    cell.setCellValue(record.getR61_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR61_zar() != null) {
//					    cell.setCellValue(record.getR61_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR61_gbp() != null) {
//					    cell.setCellValue(record.getR61_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR61_euro() != null) {
//					    cell.setCellValue(record.getR61_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR61_yen() != null) {
//					    cell.setCellValue(record.getR61_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR61_c6() != null) {
//					    cell.setCellValue(record.getR61_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR61_c7() != null) {
//					    cell.setCellValue(record.getR61_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR61_c8() != null) {
//					    cell.setCellValue(record.getR61_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR61_total() != null) {
//					    cell.setCellValue(record.getR61_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					// row62
//					row = sheet.getRow(61);
//
//					cell = row.getCell(1);
//					if (record.getR62_usd() != null) {
//					    cell.setCellValue(record.getR62_usd().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(2);
//					if (record.getR62_zar() != null) {
//					    cell.setCellValue(record.getR62_zar().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(3);
//					if (record.getR62_gbp() != null) {
//					    cell.setCellValue(record.getR62_gbp().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(4);
//					if (record.getR62_euro() != null) {
//					    cell.setCellValue(record.getR62_euro().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(5);
//					if (record.getR62_yen() != null) {
//					    cell.setCellValue(record.getR62_yen().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(6);
//					if (record.getR62_c6() != null) {
//					    cell.setCellValue(record.getR62_c6().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(7);
//					if (record.getR62_c7() != null) {
//					    cell.setCellValue(record.getR62_c7().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(8);
//					if (record.getR62_c8() != null) {
//					    cell.setCellValue(record.getR62_c8().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
//					}
//
//					cell = row.getCell(9);
//					if (record.getR62_total() != null) {
//					    cell.setCellValue(record.getR62_total().doubleValue());
//					} else {
//					    cell.setCellValue("");
//					    cell.setCellStyle(textStyle);
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

	// Resub Email Excel
//	public byte[] BRRS_M_LA5ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Archival Email Excel generation process in memory.");
//
//		List<M_LA5_Resub_Summary_Entity> dataList = brrs_M_LA5_resub_summary_repo
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for BRRS_M_LA5 report. Returning empty result.");
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
//					M_LA5_Resub_Summary_Entity record = dataList.get(i);
//					System.out.println("rownumber=" + startRow + i);
//					Row row = sheet.getRow(startRow + i);
//					if (row == null) {
//						row = sheet.createRow(startRow + i);
//					}
////EMAIL
//// R10 Col E
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

}
