package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.dto.ReportLineItemDTO;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Summary_Repo;
import com.bornfire.brrs.entities.M_SFINP2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SFINP2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SFINP2_Detail_Entity;
import com.bornfire.brrs.entities.M_SFINP2_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_SFINP2_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_SFINP2_Summary_Entity;

@Component
@Service
public class BRRS_M_SFINP2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SFINP2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_SFINP2_Detail_Repo M_SFINP2_DETAIL_Repo;

	@Autowired
	BRRS_M_SFINP2_Summary_Repo M_SFINP2_Summary_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Detail_Repo M_SFINP2_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Summary_Repo M_SFINP2_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_SFINP2_Resub_Summary_Repo  M_SFINP2_resub_summary_repo;
	
	
	@Autowired
	BRRS_M_SFINP2_Resub_Detail_Repo M_SFINP2_resub_detail_repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	

public ModelAndView getM_SFINP2View(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,     // kept but not used
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

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

	            List<M_SFINP2_Archival_Summary_Entity> summaryList =
	                    M_SFINP2_Archival_Summary_Repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Archival Summary Size : " + summaryList.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", summaryList);
	        }

	        /* ---------- RESUB SUMMARY ---------- */
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_SFINP2_RESUB_Summary_Entity> summaryList =
	                    M_SFINP2_resub_summary_repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Resub Summary Size : " + summaryList.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", summaryList);
	        }

	        /* ---------- NORMAL SUMMARY ---------- */
	        else {

	            List<M_SFINP2_Summary_Entity> summaryList =
	                    M_SFINP2_Summary_Repo
	                            .getdatabydateList(d1);

	            System.out.println("Normal Summary Size : " + summaryList.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", summaryList);
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_SFINP2");
	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}


public ModelAndView getM_SFINP2currentDtl(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String filter,
	        String type,
	        String version) {

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

	            List<M_SFINP2_Archival_Detail_Entity> T1Dt1;

	            if (rowId != null && columnId != null) {
	                T1Dt1 = M_SFINP2_Archival_Detail_Repo
	                        .GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
	            } else {
	                T1Dt1 = M_SFINP2_Archival_Detail_Repo
	                        .getdatabydateList(parsedDate, version);
	            }

	            mv.addObject("reportdetails", T1Dt1);
	            System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
	        }

	        /* =========================================================
	           RESUB DETAIL  ✅ ADDED
	        ========================================================= */
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_SFINP2_RESUB_Detail_Entity> T1Dt1;

	            if (rowId != null && columnId != null) {
	                T1Dt1 = M_SFINP2_resub_detail_repo
	                        .GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
	            } else {
	                T1Dt1 = M_SFINP2_resub_detail_repo
	                        .getdatabydateList(parsedDate, version);
	            }

	            mv.addObject("reportdetails", T1Dt1);
	            System.out.println("RESUB COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
	        }

	        /* =========================================================
	           CURRENT DETAIL
	        ========================================================= */
	        else {

	            List<M_SFINP2_Detail_Entity> T1Dt1;

	            if (rowId != null && columnId != null) {
	                T1Dt1 = M_SFINP2_DETAIL_Repo
	                        .GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
	            } else {
	                T1Dt1 = M_SFINP2_DETAIL_Repo
	                        .getdatabydateList(parsedDate);

	                totalPages = M_SFINP2_DETAIL_Repo
	                        .getdatacount(parsedDate);

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
	    mv.setViewName("BRRS/M_SFINP2");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("currentPage", currentPage);
	    mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("reportsflag", "reportsflag");
	    mv.addObject("menu", reportId);

	    return mv;
	}
	

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void updateReport(M_SFINP2_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_SFINP2_Summary_Entity existing =
	            M_SFINP2_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() ->
	                    new RuntimeException("Record not found for REPORT_DATE: "
	                            + updatedEntity.getREPORT_DATE()));

	    // ✅ Only allowed R-numbers
	    int[] allowedIndexes = {
	        34, 35, 39, 40, 43, 47, 48,
	        51, 52, 53, 54, 55, 56, 57, 58,
	        61, 62,
	        77, 78
	    };

	    try {
	        for (int i : allowedIndexes) {

	            String field = "MONTH_END";

	            String getterName = "getR" + i + "_" + field;
	            String setterName = "setR" + i + "_" + field;

	            try {
	                Method getter =
	                        M_SFINP2_Summary_Entity.class.getMethod(getterName);

	                Method setter =
	                        M_SFINP2_Summary_Entity.class.getMethod(
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
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // ✅ Save only intended updates
	    M_SFINP2_Summary_Repo.save(existing);
	}
	
	@Autowired BRRS_M_SFINP2_Detail_Repo M_SFINP2_detail_repo;
	
	
	

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_SFINP2"); 

		if (acctNo != null) {
			M_SFINP2_Detail_Entity msfinp2Entity = M_SFINP2_detail_repo.findByAcctnumber(acctNo);
			if (msfinp2Entity != null && msfinp2Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(msfinp2Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("msfinp2Data", msfinp2Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			M_SFINP2_Detail_Entity existing = M_SFINP2_detail_repo.findByAcctnumber(acctNo);
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

			 if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
		            BigDecimal newacctBalanceInpula = new BigDecimal(acctBalanceInpula);
		            if (existing.getAcctBalanceInpula()  == null ||
		                existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
		            	 existing.setAcctBalanceInpula(newacctBalanceInpula);
		                isChanged = true;
		                logger.info("Balance updated to {}", newacctBalanceInpula);
		            }
		        }
		        
			if (isChanged) {
				M_SFINP2_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_SFINP2_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_SFINP2_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_SFINP2 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	
	//Archival View
		public List<Object[]> getM_SFINP2Archival() {
			List<Object[]> archivalList = new ArrayList<>();

			try {
				List<M_SFINP2_Archival_Summary_Entity> repoData = M_SFINP2_Archival_Summary_Repo
						.getdatabydateListWithVersion();

				if (repoData != null && !repoData.isEmpty()) {
					for (M_SFINP2_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getREPORT_DATE(), 
								entity.getREPORT_VERSION(), 
								 entity.getReportResubDate()
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					M_SFINP2_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getREPORT_VERSION());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching  M_SFINP2  Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}

		
		public List<Object[]> getM_SFINP2Resub() {
		    List<Object[]> resubList = new ArrayList<>();
		    try {
		        List<M_SFINP2_Archival_Summary_Entity> latestArchivalList =
		        		M_SFINP2_Archival_Summary_Repo.getdatabydateListWithVersion();

		        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
		            for (M_SFINP2_Archival_Summary_Entity entity : latestArchivalList) {
		                resubList.add(new Object[] {
		                    entity.getREPORT_DATE(),
		                    entity.getREPORT_VERSION(),
		                    entity.getReportResubDate()
		                });
		            }
		            System.out.println("Fetched " + resubList.size() + " record(s)");
		        } else {
		            System.out.println("No archival data found.");
		        }

		    } catch (Exception e) {
		        System.err.println("Error fetching M_SFINP2 Resub data: " + e.getMessage());
		        e.printStackTrace();
		    }
		    return resubList;
		}

	public byte[] BRRS_M_SFINP2DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_SFINP2 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_SFINP2Details");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE","REPORT LABEL", "REPORT ADDL CRETIRIA",
					"REPORT_DATE" };
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
			List<M_SFINP2_Detail_Entity> reportData = M_SFINP2_DETAIL_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP2_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					// Average (right aligned, 3 decimal places)
					 balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
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
				logger.info("No data found for BRRS_M_SFINP2 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			 logger.error("Error generating BRRS_M_SFINP2 Excel", e);
		     return null;  // important
		}
	}

	
	

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_SFINP2 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MSFinP2Detail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE","REPORT LABEL", "REPORT ADDL CRETIRIA",
					"REPORT_DATE" };

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
			List<M_SFINP2_Archival_Detail_Entity> reportData = M_SFINP2_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP2_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					
					// Average (right aligned, 3 decimal places)
					 balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
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
				logger.info("No data found for BRRS_M_SFINP2 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_SFINP2Excel", e);
			return new byte[0];
		}
	}
	
	
	public List<ReportLineItemDTO> getReportData(String filename) throws Exception {
		List<ReportLineItemDTO> reportData = new ArrayList<>();

		File file = new File(filename);
		if (!file.exists()) {
			throw new Exception("File not found: " + filename);
		}

		FileInputStream fis = new FileInputStream(file);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);

		final int START_ROW_INDEX = 10;
		final int END_ROW_INDEX = 80;

		Iterator<Row> rowIterator = sheet.iterator();
		int srlNo = 1;

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			int currentRowIndex = row.getRowNum();

			if (currentRowIndex < START_ROW_INDEX) {
				continue;
			}

			if (currentRowIndex > END_ROW_INDEX) {
				break;
			}

			Cell fieldDescCell = row.getCell(0);

			if (fieldDescCell == null || fieldDescCell.getCellType() == Cell.CELL_TYPE_BLANK) {
				continue;
			}

			String fieldDesc = "";
			try {
				fieldDesc = fieldDescCell.getStringCellValue();
			} catch (IllegalStateException e) {

				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				CellValue cellValue = evaluator.evaluate(fieldDescCell);
				if (cellValue != null) {
					if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
						fieldDesc = cellValue.getStringValue();
					} else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						fieldDesc = String.valueOf(cellValue.getNumberValue());
					} else if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						fieldDesc = String.valueOf(cellValue.getBooleanValue());
					}

				}
				if (fieldDesc.isEmpty() && fieldDescCell.getCellType() == Cell.CELL_TYPE_FORMULA) {

					fieldDesc = fieldDescCell.getCellFormula();
				}
			} catch (Exception e) {
				System.err.println("Error reading cell A" + (currentRowIndex + 1) + ": " + e.getMessage());
				continue;
			}

			if (fieldDesc == null || fieldDesc.trim().isEmpty()) {
				continue;
			}

			ReportLineItemDTO dto = new ReportLineItemDTO();
			dto.setSrlNo(srlNo++);
			dto.setFieldDescription(fieldDesc.trim());

			dto.setReportLabel("R" + (currentRowIndex + 1));

			boolean hasFormula = false;
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					hasFormula = true;
					break;
				}
			}
			dto.setHeader(hasFormula ? "Y" : " ");

			dto.setRemarks("");

			reportData.add(dto);
		}

		workbook.close();
		fis.close();

		System.out.println("✅ M_SFINP2 Report data processed (Excel Row " + (START_ROW_INDEX + 1) + " to "
				+ (END_ROW_INDEX + 1) + "). Total items: " + reportData.size());
		return reportData;
	}
	
	







//Normal format Excel

public byte[] BRRS_M_SFINP2Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
return getExcelM_SFINP2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);
} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);

try {
// ✅ Redirecting to Resub Excel
return BRRS_M_SFINP2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,format, version);

} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} else {

if ("email".equalsIgnoreCase(format) && version == null) {
logger.info("Got format as Email");
logger.info("Service: Generating Email report for version {}", version);
return BRRS_M_SFINP2EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
} else {

// Fetch data

List<M_SFINP2_Summary_Entity> dataList = M_SFINP2_Summary_Repo
.getdatabydateList(dateformat.parse(todate));

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_SFINP2 report. Returning empty result.");
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

int startRow = 10;

if (!dataList.isEmpty()) {
for (int i = 0; i < dataList.size(); i++) {
M_SFINP2_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}
// row11
// Column C
Cell cell2 = row.createCell(2);
if (record.getR11_MONTH_END() != null) {
cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row11
// Column D
Cell cell3 = row.createCell(3);
if (record.getR11_AVERAGE() != null) {
cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row12
row = sheet.getRow(11);
// Column C
cell2 = row.createCell(2);
if (record.getR12_MONTH_END() != null) {
cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row12
// Column D
cell3 = row.createCell(3);
if (record.getR12_AVERAGE() != null) {
cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row13
row = sheet.getRow(12);
// Column C
cell2 = row.createCell(2);
if (record.getR13_MONTH_END() != null) {
cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row13
// Column D
cell3 = row.createCell(3);
if (record.getR13_AVERAGE() != null) {
cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row14
row = sheet.getRow(13);
// Column C
cell2 = row.createCell(2);
if (record.getR14_MONTH_END() != null) {
cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row14
// Column D
cell3 = row.createCell(3);
if (record.getR14_AVERAGE() != null) {
cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row15
row = sheet.getRow(14);
// Column C
cell2 = row.createCell(2);
if (record.getR15_MONTH_END() != null) {
cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row15
// Column D
cell3 = row.createCell(3);
if (record.getR15_AVERAGE() != null) {
cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row16
row = sheet.getRow(15);
// Column C
cell2 = row.createCell(2);
if (record.getR16_MONTH_END() != null) {
cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row16
// Column D
cell3 = row.createCell(3);
if (record.getR16_AVERAGE() != null) {
cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row19
row = sheet.getRow(18);
// Column C
cell2 = row.createCell(2);
if (record.getR19_MONTH_END() != null) {
cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
row = sheet.getRow(19);
// Column C
cell2 = row.createCell(2);
if (record.getR20_MONTH_END() != null) {
cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
// Column D
cell3 = row.createCell(3);
if (record.getR20_AVERAGE() != null) {
cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row21
row = sheet.getRow(20);
// Column C
cell2 = row.createCell(2);
if (record.getR21_MONTH_END() != null) {
cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row21
// Column D
cell3 = row.createCell(3);
if (record.getR21_AVERAGE() != null) {
cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row23
row = sheet.getRow(22);
// Column C
cell2 = row.createCell(2);
if (record.getR23_MONTH_END() != null) {
cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row23
// Column D
cell3 = row.createCell(3);
if (record.getR23_AVERAGE() != null) {
cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row24
row = sheet.getRow(23);
// Column C
cell2 = row.createCell(2);
if (record.getR24_MONTH_END() != null) {
cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row24
// Column D
cell3 = row.createCell(3);
if (record.getR24_AVERAGE() != null) {
cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row27
row = sheet.getRow(26);
// Column C
cell2 = row.createCell(2);
if (record.getR27_MONTH_END() != null) {
cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row27
// Column D
cell3 = row.createCell(3);
if (record.getR27_AVERAGE() != null) {
cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row28
row = sheet.getRow(27);
// Column C
cell2 = row.createCell(2);
if (record.getR28_MONTH_END() != null) {
cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row28
// Column D
cell3 = row.createCell(3);
if (record.getR28_AVERAGE() != null) {
cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

row = sheet.getRow(28);
// row29
// Column D
cell3 = row.createCell(3);
if (record.getR29_AVERAGE() != null) {
cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row30
row = sheet.getRow(29);
// Column C
cell2 = row.createCell(2);
if (record.getR30_MONTH_END() != null) {
cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row30
// Column D
cell3 = row.createCell(3);
if (record.getR30_AVERAGE() != null) {
cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row31
row = sheet.getRow(30);
// Column C
cell2 = row.createCell(2);
if (record.getR31_MONTH_END() != null) {
cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row31
// Column D
cell3 = row.createCell(3);
if (record.getR31_AVERAGE() != null) {
cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row34
row = sheet.getRow(33);
// Column C
cell2 = row.createCell(2);
if (record.getR34_MONTH_END() != null) {
cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row34
// Column D
cell3 = row.createCell(3);
if (record.getR34_AVERAGE() != null) {
cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row35
row = sheet.getRow(34);
// Column C
cell2 = row.createCell(2);
if (record.getR35_MONTH_END() != null) {
cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row35
// Column D
cell3 = row.createCell(3);
if (record.getR35_AVERAGE() != null) {
cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row36
row = sheet.getRow(35);
// Column C
cell2 = row.createCell(2);
if (record.getR36_MONTH_END() != null) {
cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row36
// Column D
cell3 = row.createCell(3);
if (record.getR36_AVERAGE() != null) {
cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row37
row = sheet.getRow(36);
// Column C
cell2 = row.createCell(2);
if (record.getR37_MONTH_END() != null) {
cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row37
// Column D
cell3 = row.createCell(3);
if (record.getR37_AVERAGE() != null) {
cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row38
row = sheet.getRow(37);
// Column C
cell2 = row.createCell(2);
if (record.getR38_MONTH_END() != null) {
cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row38
// Column D
cell3 = row.createCell(3);
if (record.getR38_AVERAGE() != null) {
cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row39
row = sheet.getRow(38);
// Column C
cell2 = row.createCell(2);
if (record.getR39_MONTH_END() != null) {
cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row39
// Column D
cell3 = row.createCell(3);
if (record.getR39_AVERAGE() != null) {
cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row40
row = sheet.getRow(39);
// Column C
cell2 = row.createCell(2);
if (record.getR40_MONTH_END() != null) {
cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row40
// Column D
cell3 = row.createCell(3);
if (record.getR40_AVERAGE() != null) {
cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row42
row = sheet.getRow(41);
// Column C
cell2 = row.createCell(2);
if (record.getR42_MONTH_END() != null) {
cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row42
// Column D
cell3 = row.createCell(3);
if (record.getR42_AVERAGE() != null) {
cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row43
row = sheet.getRow(42);
// Column C
cell2 = row.createCell(2);
if (record.getR43_MONTH_END() != null) {
cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row43
// Column D
cell3 = row.createCell(3);
if (record.getR43_AVERAGE() != null) {
cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row44
row = sheet.getRow(43);
// Column C
cell2 = row.createCell(2);
if (record.getR44_MONTH_END() != null) {
cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row44
// Column D
cell3 = row.createCell(3);
if (record.getR44_AVERAGE() != null) {
cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row45
row = sheet.getRow(44);
// Column C
cell2 = row.createCell(2);
if (record.getR45_MONTH_END() != null) {
cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row45
// Column D
cell3 = row.createCell(3);
if (record.getR45_AVERAGE() != null) {
cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row47
row = sheet.getRow(46);
// Column C
cell2 = row.createCell(2);
if (record.getR47_MONTH_END() != null) {
cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row47
// Column D
cell3 = row.createCell(3);
if (record.getR47_AVERAGE() != null) {
cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row48
row = sheet.getRow(47);
// Column C
cell2 = row.createCell(2);
if (record.getR48_MONTH_END() != null) {
cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row48
// Column D
cell3 = row.createCell(3);
if (record.getR48_AVERAGE() != null) {
cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row49
row = sheet.getRow(48);
// Column C
cell2 = row.createCell(2);
if (record.getR49_MONTH_END() != null) {
cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row49
// Column D
cell3 = row.createCell(3);
if (record.getR49_AVERAGE() != null) {
cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row51
row = sheet.getRow(50);
// Column C
cell2 = row.createCell(2);
if (record.getR51_MONTH_END() != null) {
cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row51
// Column D
cell3 = row.createCell(3);
if (record.getR51_AVERAGE() != null) {
cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row52
row = sheet.getRow(51);
// Column C
cell2 = row.createCell(2);
if (record.getR52_MONTH_END() != null) {
cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row52
// Column D
cell3 = row.createCell(3);
if (record.getR52_AVERAGE() != null) {
cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row53
row = sheet.getRow(52);
// Column C
cell2 = row.createCell(2);
if (record.getR53_MONTH_END() != null) {
cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row53
// Column D
cell3 = row.createCell(3);
if (record.getR53_AVERAGE() != null) {
cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row54
row = sheet.getRow(53);
// Column C
cell2 = row.createCell(2);
if (record.getR54_MONTH_END() != null) {
cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row54
// Column D
cell3 = row.createCell(3);
if (record.getR54_AVERAGE() != null) {
cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row55
row = sheet.getRow(54);
// Column C
cell2 = row.createCell(2);
if (record.getR55_MONTH_END() != null) {
cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row55
// Column D
cell3 = row.createCell(3);
if (record.getR55_AVERAGE() != null) {
cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row56
row = sheet.getRow(55);
// Column C
cell2 = row.createCell(2);
if (record.getR56_MONTH_END() != null) {
cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row56
// Column D
cell3 = row.createCell(3);
if (record.getR56_AVERAGE() != null) {
cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row57
row = sheet.getRow(56);
// Column C
cell2 = row.createCell(2);
if (record.getR57_MONTH_END() != null) {
cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row57
// Column D
cell3 = row.createCell(3);
if (record.getR57_AVERAGE() != null) {
cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row58
row = sheet.getRow(57);
// Column C
cell2 = row.createCell(2);
if (record.getR58_MONTH_END() != null) {
cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row58
// Column D
cell3 = row.createCell(3);
if (record.getR58_AVERAGE() != null) {
cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row60
row = sheet.getRow(59);
// Column C
cell2 = row.createCell(2);
if (record.getR60_MONTH_END() != null) {
cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row60
// Column D
cell3 = row.createCell(3);
if (record.getR60_AVERAGE() != null) {
cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row61
row = sheet.getRow(60);
// Column C
cell2 = row.createCell(2);
if (record.getR61_MONTH_END() != null) {
cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row61
// Column D
cell3 = row.createCell(3);
if (record.getR61_AVERAGE() != null) {
cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row62
row = sheet.getRow(61);
// Column C
cell2 = row.createCell(2);
if (record.getR62_MONTH_END() != null) {
cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row62
// Column D
cell3 = row.createCell(3);
if (record.getR62_AVERAGE() != null) {
cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row64
row = sheet.getRow(63);
// Column C
cell2 = row.createCell(2);
if (record.getR64_MONTH_END() != null) {
cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row64
// Column D
cell3 = row.createCell(3);
if (record.getR64_AVERAGE() != null) {
cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row66
row = sheet.getRow(65);
// Column C
cell2 = row.createCell(2);
if (record.getR66_MONTH_END() != null) {
cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row66
// Column D
cell3 = row.createCell(3);
if (record.getR66_AVERAGE() != null) {
cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row67
row = sheet.getRow(66);
// Column C
cell2 = row.createCell(2);
if (record.getR67_MONTH_END() != null) {
cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row67
// Column D
cell3 = row.createCell(3);
if (record.getR67_AVERAGE() != null) {
cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row69
row = sheet.getRow(68);
// Column C
cell2 = row.createCell(2);
if (record.getR69_MONTH_END() != null) {
cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row69
// Column D
cell3 = row.createCell(3);
if (record.getR69_AVERAGE() != null) {
cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row70
row = sheet.getRow(69);
// Column C
cell2 = row.createCell(2);
if (record.getR70_MONTH_END() != null) {
cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row70
// Column D
cell3 = row.createCell(3);
if (record.getR70_AVERAGE() != null) {
cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row71
row = sheet.getRow(70);
// Column C
cell2 = row.createCell(2);
if (record.getR71_MONTH_END() != null) {
cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row71
// Column D
cell3 = row.createCell(3);
if (record.getR71_AVERAGE() != null) {
cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

row = sheet.getRow(71);
// row72
// Column D
cell3 = row.createCell(3);
if (record.getR72_AVERAGE() != null) {
cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row74
row = sheet.getRow(73);
// Column C
cell2 = row.createCell(2);
if (record.getR74_MONTH_END() != null) {
cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row74
// Column D
cell3 = row.createCell(3);
if (record.getR74_AVERAGE() != null) {
cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row75
row = sheet.getRow(74);
// Column C
cell2 = row.createCell(2);
if (record.getR75_MONTH_END() != null) {
cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row75
// Column D
cell3 = row.createCell(3);
if (record.getR75_AVERAGE() != null) {
cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row76
row = sheet.getRow(75);
// Column C
cell2 = row.createCell(2);
if (record.getR76_MONTH_END() != null) {
cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row76
// Column D
cell3 = row.createCell(3);
if (record.getR76_AVERAGE() != null) {
cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row77
row = sheet.getRow(76);
// Column C
cell2 = row.createCell(2);
if (record.getR77_MONTH_END() != null) {
cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row77
// Column D
cell3 = row.createCell(3);
if (record.getR77_AVERAGE() != null) {
cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row78
row = sheet.getRow(77);
// Column C
cell2 = row.createCell(2);
if (record.getR78_MONTH_END() != null) {
cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row78
// Column D
cell3 = row.createCell(3);
if (record.getR78_AVERAGE() != null) {
cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row79
row = sheet.getRow(78);
// Column C
cell2 = row.createCell(2);
if (record.getR79_MONTH_END() != null) {
cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row79
// Column D
cell3 = row.createCell(3);
if (record.getR79_AVERAGE() != null) {
cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
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
public byte[] BRRS_M_SFINP2EmailExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Email Excel generation process in memory.");

if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
try {
// Redirecting to Archival
return BRRS_M_SFINP2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);

try {
// ✅ Redirecting to Resub Excel
return BRRS_M_SFINP2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} 
else {
List<M_SFINP2_Summary_Entity> dataList = M_SFINP2_Summary_Repo.getdatabydateList(dateformat.parse(todate));

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_SFINP2 report. Returning empty result.");
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

int startRow = 10;

if (!dataList.isEmpty()) {
for (int i = 0; i < dataList.size(); i++) {
M_SFINP2_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}





//row11--------------->23. Current deposits

// Column C
Cell cell2 = row.createCell(2);
if (record.getR11_MONTH_END() != null) {
cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row11
// Column D
Cell cell3 = row.createCell(3);
if (record.getR11_AVERAGE() != null) {
cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row12------------------>24. Call deposits

row = sheet.getRow(11);
// Column C
cell2 = row.createCell(2);
if (record.getR12_MONTH_END() != null) {
cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row12
// Column D
cell3 = row.createCell(3);
if (record.getR12_AVERAGE() != null) {
cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row13------------->25. Savings deposits

row = sheet.getRow(12);
// Column C
cell2 = row.createCell(2);
if (record.getR13_MONTH_END() != null) {
cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row13
// Column D
cell3 = row.createCell(3);
if (record.getR13_AVERAGE() != null) {
cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}




// row14------------26. Notice deposits

row = sheet.getRow(13);
// Column C
cell2 = row.createCell(2);
if (record.getR14_MONTH_END() != null) {
cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row14
// Column D
cell3 = row.createCell(3);
if (record.getR14_AVERAGE() != null) {
cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row15----------->27. Fixed deposits

row = sheet.getRow(14);
// Column C
cell2 = row.createCell(2);
if (record.getR15_MONTH_END() != null) {
cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row15
// Column D
cell3 = row.createCell(3);
if (record.getR15_AVERAGE() != null) {
cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}



// row16---------------28. Certificates of deposit 

row = sheet.getRow(15);
// Column C
cell2 = row.createCell(2);
if (record.getR16_MONTH_END() != null) {
cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row16
// Column D
cell3 = row.createCell(3);
if (record.getR16_AVERAGE() != null) {
cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row19-------------(a) Affiliated

row = sheet.getRow(18);
// Column C
cell2 = row.createCell(2);
if (record.getR19_MONTH_END() != null) {
cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
row = sheet.getRow(19);
// Column C
cell2 = row.createCell(2);
if (record.getR20_MONTH_END() != null) {
cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
// Column D
cell3 = row.createCell(3);
if (record.getR20_AVERAGE() != null) {
cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row21
row = sheet.getRow(20);
// Column C
cell2 = row.createCell(2);
if (record.getR21_MONTH_END() != null) {
cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


// row21
// Column D
cell3 = row.createCell(3);
if (record.getR21_AVERAGE() != null) {
cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row23
row = sheet.getRow(22);
// Column C
cell2 = row.createCell(2);
if (record.getR23_MONTH_END() != null) {
cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row23
// Column D
cell3 = row.createCell(3);
if (record.getR23_AVERAGE() != null) {
cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row24
row = sheet.getRow(23);
// Column C
cell2 = row.createCell(2);
if (record.getR24_MONTH_END() != null) {
cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row24
// Column D
cell3 = row.createCell(3);
if (record.getR24_AVERAGE() != null) {
cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row27
row = sheet.getRow(26);
// Column C
cell2 = row.createCell(2);
if (record.getR27_MONTH_END() != null) {
cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row27
// Column D
cell3 = row.createCell(3);
if (record.getR27_AVERAGE() != null) {
cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row28
row = sheet.getRow(27);
// Column C
cell2 = row.createCell(2);
if (record.getR28_MONTH_END() != null) {
cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row28
// Column D
cell3 = row.createCell(3);
if (record.getR28_AVERAGE() != null) {
cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

row = sheet.getRow(28);
// row29
// Column D
cell3 = row.createCell(3);
if (record.getR29_AVERAGE() != null) {
cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row30
row = sheet.getRow(29);
// Column C
cell2 = row.createCell(2);
if (record.getR30_MONTH_END() != null) {
cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row30
// Column D
cell3 = row.createCell(3);
if (record.getR30_AVERAGE() != null) {
cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row31
row = sheet.getRow(30);
// Column C
cell2 = row.createCell(2);
if (record.getR31_MONTH_END() != null) {
cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row31
// Column D
cell3 = row.createCell(3);
if (record.getR31_AVERAGE() != null) {
cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row34
row = sheet.getRow(33);
// Column C
cell2 = row.createCell(2);
if (record.getR34_MONTH_END() != null) {
cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row34
// Column D
cell3 = row.createCell(3);
if (record.getR34_AVERAGE() != null) {
cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row35
row = sheet.getRow(34);
// Column C
cell2 = row.createCell(2);
if (record.getR35_MONTH_END() != null) {
cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row35
// Column D
cell3 = row.createCell(3);
if (record.getR35_AVERAGE() != null) {
cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row36
row = sheet.getRow(35);
// Column C
cell2 = row.createCell(2);
if (record.getR36_MONTH_END() != null) {
cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row36
// Column D
cell3 = row.createCell(3);
if (record.getR36_AVERAGE() != null) {
cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row37
row = sheet.getRow(36);
// Column C
cell2 = row.createCell(2);
if (record.getR37_MONTH_END() != null) {
cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row37
// Column D
cell3 = row.createCell(3);
if (record.getR37_AVERAGE() != null) {
cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row38
row = sheet.getRow(37);
// Column C
cell2 = row.createCell(2);
if (record.getR38_MONTH_END() != null) {
cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row38
// Column D
cell3 = row.createCell(3);
if (record.getR38_AVERAGE() != null) {
cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row39
row = sheet.getRow(38);
// Column C
cell2 = row.createCell(2);
if (record.getR39_MONTH_END() != null) {
cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row39
// Column D
cell3 = row.createCell(3);
if (record.getR39_AVERAGE() != null) {
cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row40
row = sheet.getRow(39);
// Column C
cell2 = row.createCell(2);
if (record.getR40_MONTH_END() != null) {
cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row40
// Column D
cell3 = row.createCell(3);
if (record.getR40_AVERAGE() != null) {
cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row42
row = sheet.getRow(41);
// Column C
cell2 = row.createCell(2);
if (record.getR42_MONTH_END() != null) {
cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row42
// Column D
cell3 = row.createCell(3);
if (record.getR42_AVERAGE() != null) {
cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row43
row = sheet.getRow(42);
// Column C
cell2 = row.createCell(2);
if (record.getR43_MONTH_END() != null) {
cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row43
// Column D
cell3 = row.createCell(3);
if (record.getR43_AVERAGE() != null) {
cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row44
row = sheet.getRow(43);
// Column C
cell2 = row.createCell(2);
if (record.getR44_MONTH_END() != null) {
cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row44
// Column D
cell3 = row.createCell(3);
if (record.getR44_AVERAGE() != null) {
cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row45
row = sheet.getRow(44);
// Column C
cell2 = row.createCell(2);
if (record.getR45_MONTH_END() != null) {
cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row45
// Column D
cell3 = row.createCell(3);
if (record.getR45_AVERAGE() != null) {
cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row47
row = sheet.getRow(46);
// Column C
cell2 = row.createCell(2);
if (record.getR47_MONTH_END() != null) {
cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row47
// Column D
cell3 = row.createCell(3);
if (record.getR47_AVERAGE() != null) {
cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row48
row = sheet.getRow(47);
// Column C
cell2 = row.createCell(2);
if (record.getR48_MONTH_END() != null) {
cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row48
// Column D
cell3 = row.createCell(3);
if (record.getR48_AVERAGE() != null) {
cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row49
row = sheet.getRow(48);
// Column C
cell2 = row.createCell(2);
if (record.getR49_MONTH_END() != null) {
cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row49
// Column D
cell3 = row.createCell(3);
if (record.getR49_AVERAGE() != null) {
cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row51
row = sheet.getRow(50);
// Column C
cell2 = row.createCell(2);
if (record.getR51_MONTH_END() != null) {
cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row51
// Column D
cell3 = row.createCell(3);
if (record.getR51_AVERAGE() != null) {
cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row52
row = sheet.getRow(51);
// Column C
cell2 = row.createCell(2);
if (record.getR52_MONTH_END() != null) {
cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row52
// Column D
cell3 = row.createCell(3);
if (record.getR52_AVERAGE() != null) {
cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row53
row = sheet.getRow(52);
// Column C
cell2 = row.createCell(2);
if (record.getR53_MONTH_END() != null) {
cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row53
// Column D
cell3 = row.createCell(3);
if (record.getR53_AVERAGE() != null) {
cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row54
row = sheet.getRow(53);
// Column C
cell2 = row.createCell(2);
if (record.getR54_MONTH_END() != null) {
cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row54
// Column D
cell3 = row.createCell(3);
if (record.getR54_AVERAGE() != null) {
cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row55
row = sheet.getRow(54);
// Column C
cell2 = row.createCell(2);
if (record.getR55_MONTH_END() != null) {
cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row55
// Column D
cell3 = row.createCell(3);
if (record.getR55_AVERAGE() != null) {
cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row56
row = sheet.getRow(55);
// Column C
cell2 = row.createCell(2);
if (record.getR56_MONTH_END() != null) {
cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row56
// Column D
cell3 = row.createCell(3);
if (record.getR56_AVERAGE() != null) {
cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row57
row = sheet.getRow(56);
// Column C
cell2 = row.createCell(2);
if (record.getR57_MONTH_END() != null) {
cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row57
// Column D
cell3 = row.createCell(3);
if (record.getR57_AVERAGE() != null) {
cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row58
row = sheet.getRow(57);
// Column C
cell2 = row.createCell(2);
if (record.getR58_MONTH_END() != null) {
cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row58
// Column D
cell3 = row.createCell(3);
if (record.getR58_AVERAGE() != null) {
cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row60
row = sheet.getRow(59);
// Column C
cell2 = row.createCell(2);
if (record.getR60_MONTH_END() != null) {
cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row60
// Column D
cell3 = row.createCell(3);
if (record.getR60_AVERAGE() != null) {
cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row61
row = sheet.getRow(60);
// Column C
cell2 = row.createCell(2);
if (record.getR61_MONTH_END() != null) {
cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row61
// Column D
cell3 = row.createCell(3);
if (record.getR61_AVERAGE() != null) {
cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row62
row = sheet.getRow(61);
// Column C
cell2 = row.createCell(2);
if (record.getR62_MONTH_END() != null) {
cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row62
// Column D
cell3 = row.createCell(3);
if (record.getR62_AVERAGE() != null) {
cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row64
row = sheet.getRow(63);
// Column C
cell2 = row.createCell(2);
if (record.getR64_MONTH_END() != null) {
cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row64
// Column D
cell3 = row.createCell(3);
if (record.getR64_AVERAGE() != null) {
cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row66
row = sheet.getRow(65);
// Column C
cell2 = row.createCell(2);
if (record.getR66_MONTH_END() != null) {
cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row66
// Column D
cell3 = row.createCell(3);
if (record.getR66_AVERAGE() != null) {
cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row67
row = sheet.getRow(66);
// Column C
cell2 = row.createCell(2);
if (record.getR67_MONTH_END() != null) {
cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row67
// Column D
cell3 = row.createCell(3);
if (record.getR67_AVERAGE() != null) {
cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row69
row = sheet.getRow(68);
// Column C
cell2 = row.createCell(2);
if (record.getR69_MONTH_END() != null) {
cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row69
// Column D
cell3 = row.createCell(3);
if (record.getR69_AVERAGE() != null) {
cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row70
row = sheet.getRow(69);
// Column C
cell2 = row.createCell(2);
if (record.getR70_MONTH_END() != null) {
cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row70
// Column D
cell3 = row.createCell(3);
if (record.getR70_AVERAGE() != null) {
cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row71
row = sheet.getRow(70);
// Column C
cell2 = row.createCell(2);
if (record.getR71_MONTH_END() != null) {
cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row71
// Column D
cell3 = row.createCell(3);
if (record.getR71_AVERAGE() != null) {
cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


row = sheet.getRow(71);
// row72
// Column D
cell3 = row.createCell(3);
if (record.getR72_AVERAGE() != null) {
cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row74
row = sheet.getRow(73);
// Column C
cell2 = row.createCell(2);
if (record.getR74_MONTH_END() != null) {
cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row74
// Column D
cell3 = row.createCell(3);
if (record.getR74_AVERAGE() != null) {
cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row75
row = sheet.getRow(74);
// Column C
cell2 = row.createCell(2);
if (record.getR75_MONTH_END() != null) {
cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row75
// Column D
cell3 = row.createCell(3);
if (record.getR75_AVERAGE() != null) {
cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row76
row = sheet.getRow(75);
// Column C
cell2 = row.createCell(2);
if (record.getR76_MONTH_END() != null) {
cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row76
// Column D
cell3 = row.createCell(3);
if (record.getR76_AVERAGE() != null) {
cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row77
row = sheet.getRow(76);
// Column C
cell2 = row.createCell(2);
if (record.getR77_MONTH_END() != null) {
cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row77
// Column D
cell3 = row.createCell(3);
if (record.getR77_AVERAGE() != null) {
cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row78
row = sheet.getRow(77);
// Column C
cell2 = row.createCell(2);
if (record.getR78_MONTH_END() != null) {
cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row78
// Column D
cell3 = row.createCell(3);
if (record.getR78_AVERAGE() != null) {
cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row79
row = sheet.getRow(78);
// Column C
cell2 = row.createCell(2);
if (record.getR79_MONTH_END() != null) {
cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row79
// Column D
cell3 = row.createCell(3);
if (record.getR79_AVERAGE() != null) {
cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
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
public byte[] getExcelM_SFINP2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

logger.info("Service: Starting Excel generation process in memory in Archival.");

if ("email".equalsIgnoreCase(format) && version != null) {
try {
// Redirecting to Archival
return BRRS_M_SFINP2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
} 

List<M_SFINP2_Archival_Summary_Entity> dataList = M_SFINP2_Archival_Summary_Repo
.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for M_SFINP2 report. Returning empty result.");
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

int startRow = 10;

if (!dataList.isEmpty()) {
for (int i = 0; i < dataList.size(); i++) {
M_SFINP2_Archival_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

// row11
// Column C
Cell cell2 = row.createCell(2);
if (record.getR11_MONTH_END() != null) {
cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row11
// Column D
Cell cell3 = row.createCell(3);
if (record.getR11_AVERAGE() != null) {
cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row12
row = sheet.getRow(11);
// Column C
cell2 = row.createCell(2);
if (record.getR12_MONTH_END() != null) {
cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row12
// Column D
cell3 = row.createCell(3);
if (record.getR12_AVERAGE() != null) {
cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row13
row = sheet.getRow(12);
// Column C
cell2 = row.createCell(2);
if (record.getR13_MONTH_END() != null) {
cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row13
// Column D
cell3 = row.createCell(3);
if (record.getR13_AVERAGE() != null) {
cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row14
row = sheet.getRow(13);
// Column C
cell2 = row.createCell(2);
if (record.getR14_MONTH_END() != null) {
cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row14
// Column D
cell3 = row.createCell(3);
if (record.getR14_AVERAGE() != null) {
cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row15
row = sheet.getRow(14);
// Column C
cell2 = row.createCell(2);
if (record.getR15_MONTH_END() != null) {
cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row15
// Column D
cell3 = row.createCell(3);
if (record.getR15_AVERAGE() != null) {
cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row16
row = sheet.getRow(15);
// Column C
cell2 = row.createCell(2);
if (record.getR16_MONTH_END() != null) {
cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row16
// Column D
cell3 = row.createCell(3);
if (record.getR16_AVERAGE() != null) {
cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row19
row = sheet.getRow(18);
// Column C
cell2 = row.createCell(2);
if (record.getR19_MONTH_END() != null) {
cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
row = sheet.getRow(19);
// Column C
cell2 = row.createCell(2);
if (record.getR20_MONTH_END() != null) {
cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
// Column D
cell3 = row.createCell(3);
if (record.getR20_AVERAGE() != null) {
cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row21
row = sheet.getRow(20);
// Column C
cell2 = row.createCell(2);
if (record.getR21_MONTH_END() != null) {
cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row21
// Column D
cell3 = row.createCell(3);
if (record.getR21_AVERAGE() != null) {
cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row23
row = sheet.getRow(22);
// Column C
cell2 = row.createCell(2);
if (record.getR23_MONTH_END() != null) {
cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row23
// Column D
cell3 = row.createCell(3);
if (record.getR23_AVERAGE() != null) {
cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row24
row = sheet.getRow(23);
// Column C
cell2 = row.createCell(2);
if (record.getR24_MONTH_END() != null) {
cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row24
// Column D
cell3 = row.createCell(3);
if (record.getR24_AVERAGE() != null) {
cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row27
row = sheet.getRow(26);
// Column C
cell2 = row.createCell(2);
if (record.getR27_MONTH_END() != null) {
cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row27
// Column D
cell3 = row.createCell(3);
if (record.getR27_AVERAGE() != null) {
cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row28
row = sheet.getRow(27);
// Column C
cell2 = row.createCell(2);
if (record.getR28_MONTH_END() != null) {
cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row28
// Column D
cell3 = row.createCell(3);
if (record.getR28_AVERAGE() != null) {
cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

row = sheet.getRow(28);
// row29
// Column D
cell3 = row.createCell(3);
if (record.getR29_AVERAGE() != null) {
cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row30
row = sheet.getRow(29);
// Column C
cell2 = row.createCell(2);
if (record.getR30_MONTH_END() != null) {
cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row30
// Column D
cell3 = row.createCell(3);
if (record.getR30_AVERAGE() != null) {
cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row31
row = sheet.getRow(30);
// Column C
cell2 = row.createCell(2);
if (record.getR31_MONTH_END() != null) {
cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row31
// Column D
cell3 = row.createCell(3);
if (record.getR31_AVERAGE() != null) {
cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row34
row = sheet.getRow(33);
// Column C
cell2 = row.createCell(2);
if (record.getR34_MONTH_END() != null) {
cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row34
// Column D
cell3 = row.createCell(3);
if (record.getR34_AVERAGE() != null) {
cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row35
row = sheet.getRow(34);
// Column C
cell2 = row.createCell(2);
if (record.getR35_MONTH_END() != null) {
cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row35
// Column D
cell3 = row.createCell(3);
if (record.getR35_AVERAGE() != null) {
cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row36
row = sheet.getRow(35);
// Column C
cell2 = row.createCell(2);
if (record.getR36_MONTH_END() != null) {
cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row36
// Column D
cell3 = row.createCell(3);
if (record.getR36_AVERAGE() != null) {
cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row37
row = sheet.getRow(36);
// Column C
cell2 = row.createCell(2);
if (record.getR37_MONTH_END() != null) {
cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row37
// Column D
cell3 = row.createCell(3);
if (record.getR37_AVERAGE() != null) {
cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row38
row = sheet.getRow(37);
// Column C
cell2 = row.createCell(2);
if (record.getR38_MONTH_END() != null) {
cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row38
// Column D
cell3 = row.createCell(3);
if (record.getR38_AVERAGE() != null) {
cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row39
row = sheet.getRow(38);
// Column C
cell2 = row.createCell(2);
if (record.getR39_MONTH_END() != null) {
cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row39
// Column D
cell3 = row.createCell(3);
if (record.getR39_AVERAGE() != null) {
cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row40
row = sheet.getRow(39);
// Column C
cell2 = row.createCell(2);
if (record.getR40_MONTH_END() != null) {
cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row40
// Column D
cell3 = row.createCell(3);
if (record.getR40_AVERAGE() != null) {
cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row42
row = sheet.getRow(41);
// Column C
cell2 = row.createCell(2);
if (record.getR42_MONTH_END() != null) {
cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row42
// Column D
cell3 = row.createCell(3);
if (record.getR42_AVERAGE() != null) {
cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row43
row = sheet.getRow(42);
// Column C
cell2 = row.createCell(2);
if (record.getR43_MONTH_END() != null) {
cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row43
// Column D
cell3 = row.createCell(3);
if (record.getR43_AVERAGE() != null) {
cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row44
row = sheet.getRow(43);
// Column C
cell2 = row.createCell(2);
if (record.getR44_MONTH_END() != null) {
cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row44
// Column D
cell3 = row.createCell(3);
if (record.getR44_AVERAGE() != null) {
cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row45
row = sheet.getRow(44);
// Column C
cell2 = row.createCell(2);
if (record.getR45_MONTH_END() != null) {
cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row45
// Column D
cell3 = row.createCell(3);
if (record.getR45_AVERAGE() != null) {
cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row47
row = sheet.getRow(46);
// Column C
cell2 = row.createCell(2);
if (record.getR47_MONTH_END() != null) {
cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row47
// Column D
cell3 = row.createCell(3);
if (record.getR47_AVERAGE() != null) {
cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row48
row = sheet.getRow(47);
// Column C
cell2 = row.createCell(2);
if (record.getR48_MONTH_END() != null) {
cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row48
// Column D
cell3 = row.createCell(3);
if (record.getR48_AVERAGE() != null) {
cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row49
row = sheet.getRow(48);
// Column C
cell2 = row.createCell(2);
if (record.getR49_MONTH_END() != null) {
cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row49
// Column D
cell3 = row.createCell(3);
if (record.getR49_AVERAGE() != null) {
cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row51
row = sheet.getRow(50);
// Column C
cell2 = row.createCell(2);
if (record.getR51_MONTH_END() != null) {
cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row51
// Column D
cell3 = row.createCell(3);
if (record.getR51_AVERAGE() != null) {
cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row52
row = sheet.getRow(51);
// Column C
cell2 = row.createCell(2);
if (record.getR52_MONTH_END() != null) {
cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row52
// Column D
cell3 = row.createCell(3);
if (record.getR52_AVERAGE() != null) {
cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row53
row = sheet.getRow(52);
// Column C
cell2 = row.createCell(2);
if (record.getR53_MONTH_END() != null) {
cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row53
// Column D
cell3 = row.createCell(3);
if (record.getR53_AVERAGE() != null) {
cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row54
row = sheet.getRow(53);
// Column C
cell2 = row.createCell(2);
if (record.getR54_MONTH_END() != null) {
cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row54
// Column D
cell3 = row.createCell(3);
if (record.getR54_AVERAGE() != null) {
cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row55
row = sheet.getRow(54);
// Column C
cell2 = row.createCell(2);
if (record.getR55_MONTH_END() != null) {
cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row55
// Column D
cell3 = row.createCell(3);
if (record.getR55_AVERAGE() != null) {
cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row56
row = sheet.getRow(55);
// Column C
cell2 = row.createCell(2);
if (record.getR56_MONTH_END() != null) {
cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row56
// Column D
cell3 = row.createCell(3);
if (record.getR56_AVERAGE() != null) {
cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row57
row = sheet.getRow(56);
// Column C
cell2 = row.createCell(2);
if (record.getR57_MONTH_END() != null) {
cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row57
// Column D
cell3 = row.createCell(3);
if (record.getR57_AVERAGE() != null) {
cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row58
row = sheet.getRow(57);
// Column C
cell2 = row.createCell(2);
if (record.getR58_MONTH_END() != null) {
cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row58
// Column D
cell3 = row.createCell(3);
if (record.getR58_AVERAGE() != null) {
cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row60
row = sheet.getRow(59);
// Column C
cell2 = row.createCell(2);
if (record.getR60_MONTH_END() != null) {
cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row60
// Column D
cell3 = row.createCell(3);
if (record.getR60_AVERAGE() != null) {
cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row61
row = sheet.getRow(60);
// Column C
cell2 = row.createCell(2);
if (record.getR61_MONTH_END() != null) {
cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row61
// Column D
cell3 = row.createCell(3);
if (record.getR61_AVERAGE() != null) {
cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row62
row = sheet.getRow(61);
// Column C
cell2 = row.createCell(2);
if (record.getR62_MONTH_END() != null) {
cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row62
// Column D
cell3 = row.createCell(3);
if (record.getR62_AVERAGE() != null) {
cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row64
row = sheet.getRow(63);
// Column C
cell2 = row.createCell(2);
if (record.getR64_MONTH_END() != null) {
cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row64
// Column D
cell3 = row.createCell(3);
if (record.getR64_AVERAGE() != null) {
cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row66
row = sheet.getRow(65);
// Column C
cell2 = row.createCell(2);
if (record.getR66_MONTH_END() != null) {
cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row66
// Column D
cell3 = row.createCell(3);
if (record.getR66_AVERAGE() != null) {
cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row67
row = sheet.getRow(66);
// Column C
cell2 = row.createCell(2);
if (record.getR67_MONTH_END() != null) {
cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row67
// Column D
cell3 = row.createCell(3);
if (record.getR67_AVERAGE() != null) {
cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row69
row = sheet.getRow(68);
// Column C
cell2 = row.createCell(2);
if (record.getR69_MONTH_END() != null) {
cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row69
// Column D
cell3 = row.createCell(3);
if (record.getR69_AVERAGE() != null) {
cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row70
row = sheet.getRow(69);
// Column C
cell2 = row.createCell(2);
if (record.getR70_MONTH_END() != null) {
cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row70
// Column D
cell3 = row.createCell(3);
if (record.getR70_AVERAGE() != null) {
cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row71
row = sheet.getRow(70);
// Column C
cell2 = row.createCell(2);
if (record.getR71_MONTH_END() != null) {
cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row71
// Column D
cell3 = row.createCell(3);
if (record.getR71_AVERAGE() != null) {
cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

row = sheet.getRow(71);
// row72
// Column D
cell3 = row.createCell(3);
if (record.getR72_AVERAGE() != null) {
cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row74
row = sheet.getRow(73);
// Column C
cell2 = row.createCell(2);
if (record.getR74_MONTH_END() != null) {
cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row74
// Column D
cell3 = row.createCell(3);
if (record.getR74_AVERAGE() != null) {
cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row75
row = sheet.getRow(74);
// Column C
cell2 = row.createCell(2);
if (record.getR75_MONTH_END() != null) {
cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row75
// Column D
cell3 = row.createCell(3);
if (record.getR75_AVERAGE() != null) {
cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row76
row = sheet.getRow(75);
// Column C
cell2 = row.createCell(2);
if (record.getR76_MONTH_END() != null) {
cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row76
// Column D
cell3 = row.createCell(3);
if (record.getR76_AVERAGE() != null) {
cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row77
row = sheet.getRow(76);
// Column C
cell2 = row.createCell(2);
if (record.getR77_MONTH_END() != null) {
cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row77
// Column D
cell3 = row.createCell(3);
if (record.getR77_AVERAGE() != null) {
cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row78
row = sheet.getRow(77);
// Column C
cell2 = row.createCell(2);
if (record.getR78_MONTH_END() != null) {
cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row78
// Column D
cell3 = row.createCell(3);
if (record.getR78_AVERAGE() != null) {
cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row79
row = sheet.getRow(78);
// Column C
cell2 = row.createCell(2);
if (record.getR79_MONTH_END() != null) {
cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row79
// Column D
cell3 = row.createCell(3);
if (record.getR79_AVERAGE() != null) {
cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}



}

workbook.setForceFormulaRecalculation(true);
} else {

}

//Write the final workbook content to the in-memory stream.
workbook.write(out);

logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

return out.toByteArray();
}

}

// Archival Email Excel
public byte[] BRRS_M_SFINP2ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Archival Email Excel generation process in memory.");

List<M_SFINP2_Archival_Summary_Entity> dataList = M_SFINP2_Archival_Summary_Repo
.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_SFINP2 report. Returning empty result.");
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

int startRow = 10;

if (!dataList.isEmpty()) {
for (int i = 0; i < dataList.size(); i++) {
M_SFINP2_Archival_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}




//row11--------------->23. Current deposits

// Column C
Cell cell2 = row.createCell(2);
if (record.getR11_MONTH_END() != null) {
cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row11
// Column D
Cell cell3 = row.createCell(3);
if (record.getR11_AVERAGE() != null) {
cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row12------------------>24. Call deposits

row = sheet.getRow(11);
// Column C
cell2 = row.createCell(2);
if (record.getR12_MONTH_END() != null) {
cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row12
// Column D
cell3 = row.createCell(3);
if (record.getR12_AVERAGE() != null) {
cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row13------------->25. Savings deposits

row = sheet.getRow(12);
// Column C
cell2 = row.createCell(2);
if (record.getR13_MONTH_END() != null) {
cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row13
// Column D
cell3 = row.createCell(3);
if (record.getR13_AVERAGE() != null) {
cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}




// row14------------26. Notice deposits

row = sheet.getRow(13);
// Column C
cell2 = row.createCell(2);
if (record.getR14_MONTH_END() != null) {
cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row14
// Column D
cell3 = row.createCell(3);
if (record.getR14_AVERAGE() != null) {
cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row15----------->27. Fixed deposits

row = sheet.getRow(14);
// Column C
cell2 = row.createCell(2);
if (record.getR15_MONTH_END() != null) {
cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row15
// Column D
cell3 = row.createCell(3);
if (record.getR15_AVERAGE() != null) {
cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}



// row16---------------28. Certificates of deposit 

row = sheet.getRow(15);
// Column C
cell2 = row.createCell(2);
if (record.getR16_MONTH_END() != null) {
cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row16
// Column D
cell3 = row.createCell(3);
if (record.getR16_AVERAGE() != null) {
cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row19-------------(a) Affiliated

row = sheet.getRow(18);
// Column C
cell2 = row.createCell(2);
if (record.getR19_MONTH_END() != null) {
cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
row = sheet.getRow(19);
// Column C
cell2 = row.createCell(2);
if (record.getR20_MONTH_END() != null) {
cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
// Column D
cell3 = row.createCell(3);
if (record.getR20_AVERAGE() != null) {
cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row21
row = sheet.getRow(20);
// Column C
cell2 = row.createCell(2);
if (record.getR21_MONTH_END() != null) {
cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


// row21
// Column D
cell3 = row.createCell(3);
if (record.getR21_AVERAGE() != null) {
cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row23
row = sheet.getRow(22);
// Column C
cell2 = row.createCell(2);
if (record.getR23_MONTH_END() != null) {
cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row23
// Column D
cell3 = row.createCell(3);
if (record.getR23_AVERAGE() != null) {
cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row24
row = sheet.getRow(23);
// Column C
cell2 = row.createCell(2);
if (record.getR24_MONTH_END() != null) {
cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row24
// Column D
cell3 = row.createCell(3);
if (record.getR24_AVERAGE() != null) {
cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row27
row = sheet.getRow(26);
// Column C
cell2 = row.createCell(2);
if (record.getR27_MONTH_END() != null) {
cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row27
// Column D
cell3 = row.createCell(3);
if (record.getR27_AVERAGE() != null) {
cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row28
row = sheet.getRow(27);
// Column C
cell2 = row.createCell(2);
if (record.getR28_MONTH_END() != null) {
cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row28
// Column D
cell3 = row.createCell(3);
if (record.getR28_AVERAGE() != null) {
cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

row = sheet.getRow(28);
// row29
// Column D
cell3 = row.createCell(3);
if (record.getR29_AVERAGE() != null) {
cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row30
row = sheet.getRow(29);
// Column C
cell2 = row.createCell(2);
if (record.getR30_MONTH_END() != null) {
cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row30
// Column D
cell3 = row.createCell(3);
if (record.getR30_AVERAGE() != null) {
cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row31
row = sheet.getRow(30);
// Column C
cell2 = row.createCell(2);
if (record.getR31_MONTH_END() != null) {
cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row31
// Column D
cell3 = row.createCell(3);
if (record.getR31_AVERAGE() != null) {
cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row34
row = sheet.getRow(33);
// Column C
cell2 = row.createCell(2);
if (record.getR34_MONTH_END() != null) {
cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row34
// Column D
cell3 = row.createCell(3);
if (record.getR34_AVERAGE() != null) {
cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row35
row = sheet.getRow(34);
// Column C
cell2 = row.createCell(2);
if (record.getR35_MONTH_END() != null) {
cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row35
// Column D
cell3 = row.createCell(3);
if (record.getR35_AVERAGE() != null) {
cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row36
row = sheet.getRow(35);
// Column C
cell2 = row.createCell(2);
if (record.getR36_MONTH_END() != null) {
cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row36
// Column D
cell3 = row.createCell(3);
if (record.getR36_AVERAGE() != null) {
cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row37
row = sheet.getRow(36);
// Column C
cell2 = row.createCell(2);
if (record.getR37_MONTH_END() != null) {
cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row37
// Column D
cell3 = row.createCell(3);
if (record.getR37_AVERAGE() != null) {
cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row38
row = sheet.getRow(37);
// Column C
cell2 = row.createCell(2);
if (record.getR38_MONTH_END() != null) {
cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row38
// Column D
cell3 = row.createCell(3);
if (record.getR38_AVERAGE() != null) {
cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row39
row = sheet.getRow(38);
// Column C
cell2 = row.createCell(2);
if (record.getR39_MONTH_END() != null) {
cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row39
// Column D
cell3 = row.createCell(3);
if (record.getR39_AVERAGE() != null) {
cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row40
row = sheet.getRow(39);
// Column C
cell2 = row.createCell(2);
if (record.getR40_MONTH_END() != null) {
cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row40
// Column D
cell3 = row.createCell(3);
if (record.getR40_AVERAGE() != null) {
cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row42
row = sheet.getRow(41);
// Column C
cell2 = row.createCell(2);
if (record.getR42_MONTH_END() != null) {
cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row42
// Column D
cell3 = row.createCell(3);
if (record.getR42_AVERAGE() != null) {
cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row43
row = sheet.getRow(42);
// Column C
cell2 = row.createCell(2);
if (record.getR43_MONTH_END() != null) {
cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row43
// Column D
cell3 = row.createCell(3);
if (record.getR43_AVERAGE() != null) {
cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row44
row = sheet.getRow(43);
// Column C
cell2 = row.createCell(2);
if (record.getR44_MONTH_END() != null) {
cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row44
// Column D
cell3 = row.createCell(3);
if (record.getR44_AVERAGE() != null) {
cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row45
row = sheet.getRow(44);
// Column C
cell2 = row.createCell(2);
if (record.getR45_MONTH_END() != null) {
cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row45
// Column D
cell3 = row.createCell(3);
if (record.getR45_AVERAGE() != null) {
cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row47
row = sheet.getRow(46);
// Column C
cell2 = row.createCell(2);
if (record.getR47_MONTH_END() != null) {
cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row47
// Column D
cell3 = row.createCell(3);
if (record.getR47_AVERAGE() != null) {
cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row48
row = sheet.getRow(47);
// Column C
cell2 = row.createCell(2);
if (record.getR48_MONTH_END() != null) {
cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row48
// Column D
cell3 = row.createCell(3);
if (record.getR48_AVERAGE() != null) {
cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row49
row = sheet.getRow(48);
// Column C
cell2 = row.createCell(2);
if (record.getR49_MONTH_END() != null) {
cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row49
// Column D
cell3 = row.createCell(3);
if (record.getR49_AVERAGE() != null) {
cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row51
row = sheet.getRow(50);
// Column C
cell2 = row.createCell(2);
if (record.getR51_MONTH_END() != null) {
cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row51
// Column D
cell3 = row.createCell(3);
if (record.getR51_AVERAGE() != null) {
cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row52
row = sheet.getRow(51);
// Column C
cell2 = row.createCell(2);
if (record.getR52_MONTH_END() != null) {
cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row52
// Column D
cell3 = row.createCell(3);
if (record.getR52_AVERAGE() != null) {
cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row53
row = sheet.getRow(52);
// Column C
cell2 = row.createCell(2);
if (record.getR53_MONTH_END() != null) {
cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row53
// Column D
cell3 = row.createCell(3);
if (record.getR53_AVERAGE() != null) {
cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row54
row = sheet.getRow(53);
// Column C
cell2 = row.createCell(2);
if (record.getR54_MONTH_END() != null) {
cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row54
// Column D
cell3 = row.createCell(3);
if (record.getR54_AVERAGE() != null) {
cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row55
row = sheet.getRow(54);
// Column C
cell2 = row.createCell(2);
if (record.getR55_MONTH_END() != null) {
cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row55
// Column D
cell3 = row.createCell(3);
if (record.getR55_AVERAGE() != null) {
cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row56
row = sheet.getRow(55);
// Column C
cell2 = row.createCell(2);
if (record.getR56_MONTH_END() != null) {
cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row56
// Column D
cell3 = row.createCell(3);
if (record.getR56_AVERAGE() != null) {
cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row57
row = sheet.getRow(56);
// Column C
cell2 = row.createCell(2);
if (record.getR57_MONTH_END() != null) {
cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row57
// Column D
cell3 = row.createCell(3);
if (record.getR57_AVERAGE() != null) {
cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row58
row = sheet.getRow(57);
// Column C
cell2 = row.createCell(2);
if (record.getR58_MONTH_END() != null) {
cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row58
// Column D
cell3 = row.createCell(3);
if (record.getR58_AVERAGE() != null) {
cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row60
row = sheet.getRow(59);
// Column C
cell2 = row.createCell(2);
if (record.getR60_MONTH_END() != null) {
cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row60
// Column D
cell3 = row.createCell(3);
if (record.getR60_AVERAGE() != null) {
cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row61
row = sheet.getRow(60);
// Column C
cell2 = row.createCell(2);
if (record.getR61_MONTH_END() != null) {
cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row61
// Column D
cell3 = row.createCell(3);
if (record.getR61_AVERAGE() != null) {
cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row62
row = sheet.getRow(61);
// Column C
cell2 = row.createCell(2);
if (record.getR62_MONTH_END() != null) {
cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row62
// Column D
cell3 = row.createCell(3);
if (record.getR62_AVERAGE() != null) {
cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row64
row = sheet.getRow(63);
// Column C
cell2 = row.createCell(2);
if (record.getR64_MONTH_END() != null) {
cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row64
// Column D
cell3 = row.createCell(3);
if (record.getR64_AVERAGE() != null) {
cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row66
row = sheet.getRow(65);
// Column C
cell2 = row.createCell(2);
if (record.getR66_MONTH_END() != null) {
cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row66
// Column D
cell3 = row.createCell(3);
if (record.getR66_AVERAGE() != null) {
cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row67
row = sheet.getRow(66);
// Column C
cell2 = row.createCell(2);
if (record.getR67_MONTH_END() != null) {
cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row67
// Column D
cell3 = row.createCell(3);
if (record.getR67_AVERAGE() != null) {
cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row69
row = sheet.getRow(68);
// Column C
cell2 = row.createCell(2);
if (record.getR69_MONTH_END() != null) {
cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row69
// Column D
cell3 = row.createCell(3);
if (record.getR69_AVERAGE() != null) {
cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row70
row = sheet.getRow(69);
// Column C
cell2 = row.createCell(2);
if (record.getR70_MONTH_END() != null) {
cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row70
// Column D
cell3 = row.createCell(3);
if (record.getR70_AVERAGE() != null) {
cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row71
row = sheet.getRow(70);
// Column C
cell2 = row.createCell(2);
if (record.getR71_MONTH_END() != null) {
cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row71
// Column D
cell3 = row.createCell(3);
if (record.getR71_AVERAGE() != null) {
cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


row = sheet.getRow(71);
// row72
// Column D
cell3 = row.createCell(3);
if (record.getR72_AVERAGE() != null) {
cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row74
row = sheet.getRow(73);
// Column C
cell2 = row.createCell(2);
if (record.getR74_MONTH_END() != null) {
cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row74
// Column D
cell3 = row.createCell(3);
if (record.getR74_AVERAGE() != null) {
cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row75
row = sheet.getRow(74);
// Column C
cell2 = row.createCell(2);
if (record.getR75_MONTH_END() != null) {
cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row75
// Column D
cell3 = row.createCell(3);
if (record.getR75_AVERAGE() != null) {
cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row76
row = sheet.getRow(75);
// Column C
cell2 = row.createCell(2);
if (record.getR76_MONTH_END() != null) {
cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row76
// Column D
cell3 = row.createCell(3);
if (record.getR76_AVERAGE() != null) {
cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row77
row = sheet.getRow(76);
// Column C
cell2 = row.createCell(2);
if (record.getR77_MONTH_END() != null) {
cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row77
// Column D
cell3 = row.createCell(3);
if (record.getR77_AVERAGE() != null) {
cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row78
row = sheet.getRow(77);
// Column C
cell2 = row.createCell(2);
if (record.getR78_MONTH_END() != null) {
cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row78
// Column D
cell3 = row.createCell(3);
if (record.getR78_AVERAGE() != null) {
cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row79
row = sheet.getRow(78);
// Column C
cell2 = row.createCell(2);
if (record.getR79_MONTH_END() != null) {
cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row79
// Column D
cell3 = row.createCell(3);
if (record.getR79_AVERAGE() != null) {
cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
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

// Resub Format excel
public byte[] BRRS_M_SFINP2ResubExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type,String format, BigDecimal version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

if ("email".equalsIgnoreCase(format) && version != null) {
logger.info("Service: Generating RESUB report for version {}", version);

try {
// ✅ Redirecting to Resub Excel
return BRRS_M_SFINP2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

} catch (ParseException e) {
logger.error("Invalid report date format: {}", fromdate, e);
throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
}
}

List<M_SFINP2_RESUB_Summary_Entity> dataList = M_SFINP2_resub_summary_repo
.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for M_SFINP2 report. Returning empty result.");
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

int startRow = 10;

if (!dataList.isEmpty()) {
for (int i = 0; i < dataList.size(); i++) {

M_SFINP2_RESUB_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}

// row11
// Column C
Cell cell2 = row.createCell(2);
if (record.getR11_MONTH_END() != null) {
cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row11
// Column D
Cell cell3 = row.createCell(3);
if (record.getR11_AVERAGE() != null) {
cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row12
row = sheet.getRow(11);
// Column C
cell2 = row.createCell(2);
if (record.getR12_MONTH_END() != null) {
cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row12
// Column D
cell3 = row.createCell(3);
if (record.getR12_AVERAGE() != null) {
cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row13
row = sheet.getRow(12);
// Column C
cell2 = row.createCell(2);
if (record.getR13_MONTH_END() != null) {
cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row13
// Column D
cell3 = row.createCell(3);
if (record.getR13_AVERAGE() != null) {
cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row14
row = sheet.getRow(13);
// Column C
cell2 = row.createCell(2);
if (record.getR14_MONTH_END() != null) {
cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row14
// Column D
cell3 = row.createCell(3);
if (record.getR14_AVERAGE() != null) {
cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row15
row = sheet.getRow(14);
// Column C
cell2 = row.createCell(2);
if (record.getR15_MONTH_END() != null) {
cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row15
// Column D
cell3 = row.createCell(3);
if (record.getR15_AVERAGE() != null) {
cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row16
row = sheet.getRow(15);
// Column C
cell2 = row.createCell(2);
if (record.getR16_MONTH_END() != null) {
cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row16
// Column D
cell3 = row.createCell(3);
if (record.getR16_AVERAGE() != null) {
cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row19
row = sheet.getRow(18);
// Column C
cell2 = row.createCell(2);
if (record.getR19_MONTH_END() != null) {
cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
row = sheet.getRow(19);
// Column C
cell2 = row.createCell(2);
if (record.getR20_MONTH_END() != null) {
cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
// Column D
cell3 = row.createCell(3);
if (record.getR20_AVERAGE() != null) {
cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row21
row = sheet.getRow(20);
// Column C
cell2 = row.createCell(2);
if (record.getR21_MONTH_END() != null) {
cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row21
// Column D
cell3 = row.createCell(3);
if (record.getR21_AVERAGE() != null) {
cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row23
row = sheet.getRow(22);
// Column C
cell2 = row.createCell(2);
if (record.getR23_MONTH_END() != null) {
cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row23
// Column D
cell3 = row.createCell(3);
if (record.getR23_AVERAGE() != null) {
cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row24
row = sheet.getRow(23);
// Column C
cell2 = row.createCell(2);
if (record.getR24_MONTH_END() != null) {
cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row24
// Column D
cell3 = row.createCell(3);
if (record.getR24_AVERAGE() != null) {
cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row27
row = sheet.getRow(26);
// Column C
cell2 = row.createCell(2);
if (record.getR27_MONTH_END() != null) {
cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row27
// Column D
cell3 = row.createCell(3);
if (record.getR27_AVERAGE() != null) {
cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row28
row = sheet.getRow(27);
// Column C
cell2 = row.createCell(2);
if (record.getR28_MONTH_END() != null) {
cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row28
// Column D
cell3 = row.createCell(3);
if (record.getR28_AVERAGE() != null) {
cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

row = sheet.getRow(28);
// row29
// Column D
cell3 = row.createCell(3);
if (record.getR29_AVERAGE() != null) {
cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row30
row = sheet.getRow(29);
// Column C
cell2 = row.createCell(2);
if (record.getR30_MONTH_END() != null) {
cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row30
// Column D
cell3 = row.createCell(3);
if (record.getR30_AVERAGE() != null) {
cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row31
row = sheet.getRow(30);
// Column C
cell2 = row.createCell(2);
if (record.getR31_MONTH_END() != null) {
cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row31
// Column D
cell3 = row.createCell(3);
if (record.getR31_AVERAGE() != null) {
cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row34
row = sheet.getRow(33);
// Column C
cell2 = row.createCell(2);
if (record.getR34_MONTH_END() != null) {
cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row34
// Column D
cell3 = row.createCell(3);
if (record.getR34_AVERAGE() != null) {
cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row35
row = sheet.getRow(34);
// Column C
cell2 = row.createCell(2);
if (record.getR35_MONTH_END() != null) {
cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row35
// Column D
cell3 = row.createCell(3);
if (record.getR35_AVERAGE() != null) {
cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row36
row = sheet.getRow(35);
// Column C
cell2 = row.createCell(2);
if (record.getR36_MONTH_END() != null) {
cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row36
// Column D
cell3 = row.createCell(3);
if (record.getR36_AVERAGE() != null) {
cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row37
row = sheet.getRow(36);
// Column C
cell2 = row.createCell(2);
if (record.getR37_MONTH_END() != null) {
cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row37
// Column D
cell3 = row.createCell(3);
if (record.getR37_AVERAGE() != null) {
cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row38
row = sheet.getRow(37);
// Column C
cell2 = row.createCell(2);
if (record.getR38_MONTH_END() != null) {
cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row38
// Column D
cell3 = row.createCell(3);
if (record.getR38_AVERAGE() != null) {
cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row39
row = sheet.getRow(38);
// Column C
cell2 = row.createCell(2);
if (record.getR39_MONTH_END() != null) {
cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row39
// Column D
cell3 = row.createCell(3);
if (record.getR39_AVERAGE() != null) {
cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row40
row = sheet.getRow(39);
// Column C
cell2 = row.createCell(2);
if (record.getR40_MONTH_END() != null) {
cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row40
// Column D
cell3 = row.createCell(3);
if (record.getR40_AVERAGE() != null) {
cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row42
row = sheet.getRow(41);
// Column C
cell2 = row.createCell(2);
if (record.getR42_MONTH_END() != null) {
cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row42
// Column D
cell3 = row.createCell(3);
if (record.getR42_AVERAGE() != null) {
cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row43
row = sheet.getRow(42);
// Column C
cell2 = row.createCell(2);
if (record.getR43_MONTH_END() != null) {
cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row43
// Column D
cell3 = row.createCell(3);
if (record.getR43_AVERAGE() != null) {
cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row44
row = sheet.getRow(43);
// Column C
cell2 = row.createCell(2);
if (record.getR44_MONTH_END() != null) {
cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row44
// Column D
cell3 = row.createCell(3);
if (record.getR44_AVERAGE() != null) {
cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row45
row = sheet.getRow(44);
// Column C
cell2 = row.createCell(2);
if (record.getR45_MONTH_END() != null) {
cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row45
// Column D
cell3 = row.createCell(3);
if (record.getR45_AVERAGE() != null) {
cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row47
row = sheet.getRow(46);
// Column C
cell2 = row.createCell(2);
if (record.getR47_MONTH_END() != null) {
cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row47
// Column D
cell3 = row.createCell(3);
if (record.getR47_AVERAGE() != null) {
cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row48
row = sheet.getRow(47);
// Column C
cell2 = row.createCell(2);
if (record.getR48_MONTH_END() != null) {
cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row48
// Column D
cell3 = row.createCell(3);
if (record.getR48_AVERAGE() != null) {
cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row49
row = sheet.getRow(48);
// Column C
cell2 = row.createCell(2);
if (record.getR49_MONTH_END() != null) {
cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row49
// Column D
cell3 = row.createCell(3);
if (record.getR49_AVERAGE() != null) {
cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row51
row = sheet.getRow(50);
// Column C
cell2 = row.createCell(2);
if (record.getR51_MONTH_END() != null) {
cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row51
// Column D
cell3 = row.createCell(3);
if (record.getR51_AVERAGE() != null) {
cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row52
row = sheet.getRow(51);
// Column C
cell2 = row.createCell(2);
if (record.getR52_MONTH_END() != null) {
cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row52
// Column D
cell3 = row.createCell(3);
if (record.getR52_AVERAGE() != null) {
cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row53
row = sheet.getRow(52);
// Column C
cell2 = row.createCell(2);
if (record.getR53_MONTH_END() != null) {
cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row53
// Column D
cell3 = row.createCell(3);
if (record.getR53_AVERAGE() != null) {
cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row54
row = sheet.getRow(53);
// Column C
cell2 = row.createCell(2);
if (record.getR54_MONTH_END() != null) {
cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row54
// Column D
cell3 = row.createCell(3);
if (record.getR54_AVERAGE() != null) {
cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row55
row = sheet.getRow(54);
// Column C
cell2 = row.createCell(2);
if (record.getR55_MONTH_END() != null) {
cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row55
// Column D
cell3 = row.createCell(3);
if (record.getR55_AVERAGE() != null) {
cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row56
row = sheet.getRow(55);
// Column C
cell2 = row.createCell(2);
if (record.getR56_MONTH_END() != null) {
cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row56
// Column D
cell3 = row.createCell(3);
if (record.getR56_AVERAGE() != null) {
cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row57
row = sheet.getRow(56);
// Column C
cell2 = row.createCell(2);
if (record.getR57_MONTH_END() != null) {
cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row57
// Column D
cell3 = row.createCell(3);
if (record.getR57_AVERAGE() != null) {
cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row58
row = sheet.getRow(57);
// Column C
cell2 = row.createCell(2);
if (record.getR58_MONTH_END() != null) {
cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row58
// Column D
cell3 = row.createCell(3);
if (record.getR58_AVERAGE() != null) {
cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row60
row = sheet.getRow(59);
// Column C
cell2 = row.createCell(2);
if (record.getR60_MONTH_END() != null) {
cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row60
// Column D
cell3 = row.createCell(3);
if (record.getR60_AVERAGE() != null) {
cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row61
row = sheet.getRow(60);
// Column C
cell2 = row.createCell(2);
if (record.getR61_MONTH_END() != null) {
cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row61
// Column D
cell3 = row.createCell(3);
if (record.getR61_AVERAGE() != null) {
cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row62
row = sheet.getRow(61);
// Column C
cell2 = row.createCell(2);
if (record.getR62_MONTH_END() != null) {
cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row62
// Column D
cell3 = row.createCell(3);
if (record.getR62_AVERAGE() != null) {
cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row64
row = sheet.getRow(63);
// Column C
cell2 = row.createCell(2);
if (record.getR64_MONTH_END() != null) {
cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row64
// Column D
cell3 = row.createCell(3);
if (record.getR64_AVERAGE() != null) {
cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row66
row = sheet.getRow(65);
// Column C
cell2 = row.createCell(2);
if (record.getR66_MONTH_END() != null) {
cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row66
// Column D
cell3 = row.createCell(3);
if (record.getR66_AVERAGE() != null) {
cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row67
row = sheet.getRow(66);
// Column C
cell2 = row.createCell(2);
if (record.getR67_MONTH_END() != null) {
cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row67
// Column D
cell3 = row.createCell(3);
if (record.getR67_AVERAGE() != null) {
cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row69
row = sheet.getRow(68);
// Column C
cell2 = row.createCell(2);
if (record.getR69_MONTH_END() != null) {
cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row69
// Column D
cell3 = row.createCell(3);
if (record.getR69_AVERAGE() != null) {
cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row70
row = sheet.getRow(69);
// Column C
cell2 = row.createCell(2);
if (record.getR70_MONTH_END() != null) {
cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row70
// Column D
cell3 = row.createCell(3);
if (record.getR70_AVERAGE() != null) {
cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row71
row = sheet.getRow(70);
// Column C
cell2 = row.createCell(2);
if (record.getR71_MONTH_END() != null) {
cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row71
// Column D
cell3 = row.createCell(3);
if (record.getR71_AVERAGE() != null) {
cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

row = sheet.getRow(71);
// row72
// Column D
cell3 = row.createCell(3);
if (record.getR72_AVERAGE() != null) {
cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row74
row = sheet.getRow(73);
// Column C
cell2 = row.createCell(2);
if (record.getR74_MONTH_END() != null) {
cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row74
// Column D
cell3 = row.createCell(3);
if (record.getR74_AVERAGE() != null) {
cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row75
row = sheet.getRow(74);
// Column C
cell2 = row.createCell(2);
if (record.getR75_MONTH_END() != null) {
cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row75
// Column D
cell3 = row.createCell(3);
if (record.getR75_AVERAGE() != null) {
cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row76
row = sheet.getRow(75);
// Column C
cell2 = row.createCell(2);
if (record.getR76_MONTH_END() != null) {
cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row76
// Column D
cell3 = row.createCell(3);
if (record.getR76_AVERAGE() != null) {
cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row77
row = sheet.getRow(76);
// Column C
cell2 = row.createCell(2);
if (record.getR77_MONTH_END() != null) {
cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row77
// Column D
cell3 = row.createCell(3);
if (record.getR77_AVERAGE() != null) {
cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row78
row = sheet.getRow(77);
// Column C
cell2 = row.createCell(2);
if (record.getR78_MONTH_END() != null) {
cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row78
// Column D
cell3 = row.createCell(3);
if (record.getR78_AVERAGE() != null) {
cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row79
row = sheet.getRow(78);
// Column C
cell2 = row.createCell(2);
if (record.getR79_MONTH_END() != null) {
cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row79
// Column D
cell3 = row.createCell(3);
if (record.getR79_AVERAGE() != null) {
cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
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

// Resub Email Excel
public byte[] BRRS_M_SFINP2EmailResubExcel(String filename, String reportId, String fromdate, String todate,
String currency, String dtltype, String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Archival Email Excel generation process in memory.");

List<M_SFINP2_RESUB_Summary_Entity> dataList = M_SFINP2_resub_summary_repo
.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList.isEmpty()) {
logger.warn("Service: No data found for BRRS_M_SFINP2 report. Returning empty result.");
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

int startRow = 10;

if (!dataList.isEmpty()) {
for (int i = 0; i < dataList.size(); i++) {
M_SFINP2_RESUB_Summary_Entity record = dataList.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}





//row11--------------->23. Current deposits

// Column C
Cell cell2 = row.createCell(2);
if (record.getR11_MONTH_END() != null) {
cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row11
// Column D
Cell cell3 = row.createCell(3);
if (record.getR11_AVERAGE() != null) {
cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row12------------------>24. Call deposits

row = sheet.getRow(11);
// Column C
cell2 = row.createCell(2);
if (record.getR12_MONTH_END() != null) {
cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row12
// Column D
cell3 = row.createCell(3);
if (record.getR12_AVERAGE() != null) {
cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row13------------->25. Savings deposits

row = sheet.getRow(12);
// Column C
cell2 = row.createCell(2);
if (record.getR13_MONTH_END() != null) {
cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row13
// Column D
cell3 = row.createCell(3);
if (record.getR13_AVERAGE() != null) {
cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}




// row14------------26. Notice deposits

row = sheet.getRow(13);
// Column C
cell2 = row.createCell(2);
if (record.getR14_MONTH_END() != null) {
cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row14
// Column D
cell3 = row.createCell(3);
if (record.getR14_AVERAGE() != null) {
cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row15----------->27. Fixed deposits

row = sheet.getRow(14);
// Column C
cell2 = row.createCell(2);
if (record.getR15_MONTH_END() != null) {
cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row15
// Column D
cell3 = row.createCell(3);
if (record.getR15_AVERAGE() != null) {
cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}



// row16---------------28. Certificates of deposit 

row = sheet.getRow(15);
// Column C
cell2 = row.createCell(2);
if (record.getR16_MONTH_END() != null) {
cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row16
// Column D
cell3 = row.createCell(3);
if (record.getR16_AVERAGE() != null) {
cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row19-------------(a) Affiliated

row = sheet.getRow(18);
// Column C
cell2 = row.createCell(2);
if (record.getR19_MONTH_END() != null) {
cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
row = sheet.getRow(19);
// Column C
cell2 = row.createCell(2);
if (record.getR20_MONTH_END() != null) {
cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row20
// Column D
cell3 = row.createCell(3);
if (record.getR20_AVERAGE() != null) {
cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row21
row = sheet.getRow(20);
// Column C
cell2 = row.createCell(2);
if (record.getR21_MONTH_END() != null) {
cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}


// row21
// Column D
cell3 = row.createCell(3);
if (record.getR21_AVERAGE() != null) {
cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row23
row = sheet.getRow(22);
// Column C
cell2 = row.createCell(2);
if (record.getR23_MONTH_END() != null) {
cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row23
// Column D
cell3 = row.createCell(3);
if (record.getR23_AVERAGE() != null) {
cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row24
row = sheet.getRow(23);
// Column C
cell2 = row.createCell(2);
if (record.getR24_MONTH_END() != null) {
cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row24
// Column D
cell3 = row.createCell(3);
if (record.getR24_AVERAGE() != null) {
cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row27
row = sheet.getRow(26);
// Column C
cell2 = row.createCell(2);
if (record.getR27_MONTH_END() != null) {
cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row27
// Column D
cell3 = row.createCell(3);
if (record.getR27_AVERAGE() != null) {
cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row28
row = sheet.getRow(27);
// Column C
cell2 = row.createCell(2);
if (record.getR28_MONTH_END() != null) {
cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row28
// Column D
cell3 = row.createCell(3);
if (record.getR28_AVERAGE() != null) {
cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

row = sheet.getRow(28);
// row29
// Column D
cell3 = row.createCell(3);
if (record.getR29_AVERAGE() != null) {
cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row30
row = sheet.getRow(29);
// Column C
cell2 = row.createCell(2);
if (record.getR30_MONTH_END() != null) {
cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row30
// Column D
cell3 = row.createCell(3);
if (record.getR30_AVERAGE() != null) {
cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row31
row = sheet.getRow(30);
// Column C
cell2 = row.createCell(2);
if (record.getR31_MONTH_END() != null) {
cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row31
// Column D
cell3 = row.createCell(3);
if (record.getR31_AVERAGE() != null) {
cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row34
row = sheet.getRow(33);
// Column C
cell2 = row.createCell(2);
if (record.getR34_MONTH_END() != null) {
cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row34
// Column D
cell3 = row.createCell(3);
if (record.getR34_AVERAGE() != null) {
cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row35
row = sheet.getRow(34);
// Column C
cell2 = row.createCell(2);
if (record.getR35_MONTH_END() != null) {
cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row35
// Column D
cell3 = row.createCell(3);
if (record.getR35_AVERAGE() != null) {
cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row36
row = sheet.getRow(35);
// Column C
cell2 = row.createCell(2);
if (record.getR36_MONTH_END() != null) {
cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row36
// Column D
cell3 = row.createCell(3);
if (record.getR36_AVERAGE() != null) {
cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row37
row = sheet.getRow(36);
// Column C
cell2 = row.createCell(2);
if (record.getR37_MONTH_END() != null) {
cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row37
// Column D
cell3 = row.createCell(3);
if (record.getR37_AVERAGE() != null) {
cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row38
row = sheet.getRow(37);
// Column C
cell2 = row.createCell(2);
if (record.getR38_MONTH_END() != null) {
cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row38
// Column D
cell3 = row.createCell(3);
if (record.getR38_AVERAGE() != null) {
cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row39
row = sheet.getRow(38);
// Column C
cell2 = row.createCell(2);
if (record.getR39_MONTH_END() != null) {
cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row39
// Column D
cell3 = row.createCell(3);
if (record.getR39_AVERAGE() != null) {
cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row40
row = sheet.getRow(39);
// Column C
cell2 = row.createCell(2);
if (record.getR40_MONTH_END() != null) {
cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row40
// Column D
cell3 = row.createCell(3);
if (record.getR40_AVERAGE() != null) {
cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row42
row = sheet.getRow(41);
// Column C
cell2 = row.createCell(2);
if (record.getR42_MONTH_END() != null) {
cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row42
// Column D
cell3 = row.createCell(3);
if (record.getR42_AVERAGE() != null) {
cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row43
row = sheet.getRow(42);
// Column C
cell2 = row.createCell(2);
if (record.getR43_MONTH_END() != null) {
cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row43
// Column D
cell3 = row.createCell(3);
if (record.getR43_AVERAGE() != null) {
cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row44
row = sheet.getRow(43);
// Column C
cell2 = row.createCell(2);
if (record.getR44_MONTH_END() != null) {
cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row44
// Column D
cell3 = row.createCell(3);
if (record.getR44_AVERAGE() != null) {
cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}
// row45
row = sheet.getRow(44);
// Column C
cell2 = row.createCell(2);
if (record.getR45_MONTH_END() != null) {
cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row45
// Column D
cell3 = row.createCell(3);
if (record.getR45_AVERAGE() != null) {
cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row47
row = sheet.getRow(46);
// Column C
cell2 = row.createCell(2);
if (record.getR47_MONTH_END() != null) {
cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row47
// Column D
cell3 = row.createCell(3);
if (record.getR47_AVERAGE() != null) {
cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row48
row = sheet.getRow(47);
// Column C
cell2 = row.createCell(2);
if (record.getR48_MONTH_END() != null) {
cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row48
// Column D
cell3 = row.createCell(3);
if (record.getR48_AVERAGE() != null) {
cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row49
row = sheet.getRow(48);
// Column C
cell2 = row.createCell(2);
if (record.getR49_MONTH_END() != null) {
cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row49
// Column D
cell3 = row.createCell(3);
if (record.getR49_AVERAGE() != null) {
cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row51
row = sheet.getRow(50);
// Column C
cell2 = row.createCell(2);
if (record.getR51_MONTH_END() != null) {
cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row51
// Column D
cell3 = row.createCell(3);
if (record.getR51_AVERAGE() != null) {
cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row52
row = sheet.getRow(51);
// Column C
cell2 = row.createCell(2);
if (record.getR52_MONTH_END() != null) {
cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row52
// Column D
cell3 = row.createCell(3);
if (record.getR52_AVERAGE() != null) {
cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row53
row = sheet.getRow(52);
// Column C
cell2 = row.createCell(2);
if (record.getR53_MONTH_END() != null) {
cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row53
// Column D
cell3 = row.createCell(3);
if (record.getR53_AVERAGE() != null) {
cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row54
row = sheet.getRow(53);
// Column C
cell2 = row.createCell(2);
if (record.getR54_MONTH_END() != null) {
cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row54
// Column D
cell3 = row.createCell(3);
if (record.getR54_AVERAGE() != null) {
cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row55
row = sheet.getRow(54);
// Column C
cell2 = row.createCell(2);
if (record.getR55_MONTH_END() != null) {
cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row55
// Column D
cell3 = row.createCell(3);
if (record.getR55_AVERAGE() != null) {
cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row56
row = sheet.getRow(55);
// Column C
cell2 = row.createCell(2);
if (record.getR56_MONTH_END() != null) {
cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row56
// Column D
cell3 = row.createCell(3);
if (record.getR56_AVERAGE() != null) {
cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row57
row = sheet.getRow(56);
// Column C
cell2 = row.createCell(2);
if (record.getR57_MONTH_END() != null) {
cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row57
// Column D
cell3 = row.createCell(3);
if (record.getR57_AVERAGE() != null) {
cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row58
row = sheet.getRow(57);
// Column C
cell2 = row.createCell(2);
if (record.getR58_MONTH_END() != null) {
cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row58
// Column D
cell3 = row.createCell(3);
if (record.getR58_AVERAGE() != null) {
cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row60
row = sheet.getRow(59);
// Column C
cell2 = row.createCell(2);
if (record.getR60_MONTH_END() != null) {
cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row60
// Column D
cell3 = row.createCell(3);
if (record.getR60_AVERAGE() != null) {
cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row61
row = sheet.getRow(60);
// Column C
cell2 = row.createCell(2);
if (record.getR61_MONTH_END() != null) {
cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row61
// Column D
cell3 = row.createCell(3);
if (record.getR61_AVERAGE() != null) {
cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row62
row = sheet.getRow(61);
// Column C
cell2 = row.createCell(2);
if (record.getR62_MONTH_END() != null) {
cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row62
// Column D
cell3 = row.createCell(3);
if (record.getR62_AVERAGE() != null) {
cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row64
row = sheet.getRow(63);
// Column C
cell2 = row.createCell(2);
if (record.getR64_MONTH_END() != null) {
cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row64
// Column D
cell3 = row.createCell(3);
if (record.getR64_AVERAGE() != null) {
cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row66
row = sheet.getRow(65);
// Column C
cell2 = row.createCell(2);
if (record.getR66_MONTH_END() != null) {
cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row66
// Column D
cell3 = row.createCell(3);
if (record.getR66_AVERAGE() != null) {
cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row67
row = sheet.getRow(66);
// Column C
cell2 = row.createCell(2);
if (record.getR67_MONTH_END() != null) {
cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row67
// Column D
cell3 = row.createCell(3);
if (record.getR67_AVERAGE() != null) {
cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row69
row = sheet.getRow(68);
// Column C
cell2 = row.createCell(2);
if (record.getR69_MONTH_END() != null) {
cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row69
// Column D
cell3 = row.createCell(3);
if (record.getR69_AVERAGE() != null) {
cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row70
row = sheet.getRow(69);
// Column C
cell2 = row.createCell(2);
if (record.getR70_MONTH_END() != null) {
cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row70
// Column D
cell3 = row.createCell(3);
if (record.getR70_AVERAGE() != null) {
cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row71
row = sheet.getRow(70);
// Column C
cell2 = row.createCell(2);
if (record.getR71_MONTH_END() != null) {
cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row71
// Column D
cell3 = row.createCell(3);
if (record.getR71_AVERAGE() != null) {
cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


row = sheet.getRow(71);
// row72
// Column D
cell3 = row.createCell(3);
if (record.getR72_AVERAGE() != null) {
cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}


// row74
row = sheet.getRow(73);
// Column C
cell2 = row.createCell(2);
if (record.getR74_MONTH_END() != null) {
cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row74
// Column D
cell3 = row.createCell(3);
if (record.getR74_AVERAGE() != null) {
cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row75
row = sheet.getRow(74);
// Column C
cell2 = row.createCell(2);
if (record.getR75_MONTH_END() != null) {
cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row75
// Column D
cell3 = row.createCell(3);
if (record.getR75_AVERAGE() != null) {
cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row76
row = sheet.getRow(75);
// Column C
cell2 = row.createCell(2);
if (record.getR76_MONTH_END() != null) {
cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row76
// Column D
cell3 = row.createCell(3);
if (record.getR76_AVERAGE() != null) {
cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row77
row = sheet.getRow(76);
// Column C
cell2 = row.createCell(2);
if (record.getR77_MONTH_END() != null) {
cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row77
// Column D
cell3 = row.createCell(3);
if (record.getR77_AVERAGE() != null) {
cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row78
row = sheet.getRow(77);
// Column C
cell2 = row.createCell(2);
if (record.getR78_MONTH_END() != null) {
cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row78
// Column D
cell3 = row.createCell(3);
if (record.getR78_AVERAGE() != null) {
cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
}

// row79
row = sheet.getRow(78);
// Column C
cell2 = row.createCell(2);
if (record.getR79_MONTH_END() != null) {
cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
cell2.setCellStyle(numberStyle);
} else {
cell2.setCellValue("");
cell2.setCellStyle(textStyle);
}

// row79
// Column D
cell3 = row.createCell(3);
if (record.getR79_AVERAGE() != null) {
cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
cell3.setCellStyle(numberStyle);
} else {
cell3.setCellValue("");
cell3.setCellStyle(textStyle);
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
