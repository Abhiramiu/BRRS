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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_LA4_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA4_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA4_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_LA4_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA4_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_LA4_Summary_Repo2;
import com.bornfire.brrs.entities.M_LA1_Detail_Entity;
import com.bornfire.brrs.entities.M_LA4_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_LA4_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA4_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_LA4_Detail_Entity;
import com.bornfire.brrs.entities.M_LA4_Summary_Entity1;
import com.bornfire.brrs.entities.M_LA4_Summary_Entity2;
import com.bornfire.brrs.entities.Q_STAFF_Archival_Summary_Entity;

@Component
@Service
public class BRRS_M_LA4_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA4_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_LA4_Detail_Repo M_LA4_DETAIL_Repo;

	@Autowired
	BRRS_M_LA4_Summary_Repo M_LA4_Summary_Repo;

	@Autowired
	BRRS_M_LA4_Summary_Repo2 M_LA4_Summary_Repo2;

	@Autowired
	BRRS_M_LA4_Archival_Detail_Repo M_LA4_Archival_Detail_Repo;

	@Autowired
	BRRS_M_LA4_Archival_Summary_Repo M_LA4_Archival_Summary_Repo;

	@Autowired
	BRRS_M_LA4_Archival_Summary_Repo2 M_LA4_Archival_Summary_Repo2;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LA4View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_LA4_Archival_Summary_Entity> T1Master = new ArrayList<M_LA4_Archival_Summary_Entity>();
			List<M_LA4_Archival_Summary_Entity2> T2Master = new ArrayList<M_LA4_Archival_Summary_Entity2>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_LA4_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = M_LA4_Archival_Summary_Repo2.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
		} else {
			List<M_LA4_Summary_Entity1> T1Master = new ArrayList<>();
			List<M_LA4_Summary_Entity2> T2Master = new ArrayList<>();

			try {

				// FIX the month name before parsing
				if (todate != null) {
					todate = todate.trim().replace("Sept", "Sep");
				}

				// Matches "30-Sep-2025"
				SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

				Date d1 = dateformat.parse(todate); // todate = "30-Sep-2025"

				T1Master = M_LA4_Summary_Repo.getdatabydateList(d1);
				T2Master = M_LA4_Summary_Repo2.getdatabydateList(d1);

				System.out.println("T1Master size for LA4 is: " + T1Master.size());

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_LA4");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getM_LA4currentDtl(String reportId, String fromdate, String todate, String currency,
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

			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}

			if ("ARCHIVAL".equals(type) && version != null) {
				// 🔹 Archival branch
				List<M_LA4_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = M_LA4_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
				} else {
					T1Dt1 = M_LA4_Archival_Detail_Repo.getdatabydateList(parsedDate, version);

				}

				mv.addObject("reportdetails", T1Dt1);

				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_LA4_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = M_LA4_DETAIL_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = M_LA4_DETAIL_Repo.getdatabydateList(parsedDate);
					totalPages = M_LA4_DETAIL_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_LA4");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	public byte[] BRRS_M_LA4DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_LA4 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_LA4Details");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRETIRIA", "REPORT_DATE" };
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
			List<M_LA4_Detail_Entity> reportData = M_LA4_DETAIL_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA4_Detail_Entity item : reportData) {
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
					row.createCell(5).setCellValue(item.getReport_addl_criteria1());
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
				logger.info("No data found for BRRS_M_LA4 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_LA4 Excel", e);
			return new byte[0];
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_LA4 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MLA4Detail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRETIRIA", "REPORT_DATE" };

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
			List<M_LA4_Archival_Detail_Entity> reportData = M_LA4_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA4_Archival_Detail_Entity item : reportData) {
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
					row.createCell(5).setCellValue(item.getReport_addl_criteria1());
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
				logger.info("No data found for BRRS_M_LA4 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_LA4Excel", e);
			return new byte[0];
		}
	}

	@Transactional
	public void updateReport(M_LA4_Summary_Entity2 updatedEntity) {

		System.out.println("Came to services1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		// ENTITY 2
		M_LA4_Summary_Entity2 existing = M_LA4_Summary_Repo2.findById(updatedEntity.getReportDate()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		// ENTITY 1
		M_LA4_Summary_Entity1 existing1 = M_LA4_Summary_Repo.findById(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found in ENTITY1 for REPORT_DATE: " + updatedEntity.getReportDate()));

		try {

			// -----------------------------
			// UPDATE ENTITY 2
			// -----------------------------
			for (int i = 11; i <= 64; i++) {

				String prefix = "R" + i;
				String[] fields = { "FactoringDebtors", "Leasing" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {

						Method getter = M_LA4_Summary_Entity2.class.getMethod(getterName);
						Method setter = M_LA4_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						if (newValue != null) {
							setter.invoke(existing, newValue);
						}

					} catch (NoSuchMethodException e) {
						// ignore
					}
				}
			}

			// -----------------------------
			// UPDATE ENTITY 1 (TOTAL)
			// -----------------------------
			for (int i = 11; i <= 64; i++) {

				String prefix = "R" + i;
				String[] fields = { "Total" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {

						Method getter = M_LA4_Summary_Entity2.class.getMethod(getterName);
						Method setter = M_LA4_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);

						if (newValue != null) {
							setter.invoke(existing1, newValue);
						}

					} catch (NoSuchMethodException e) {
						// ignore
					}
				}
			}

			// SAVE BOTH
			M_LA4_Summary_Repo2.save(existing);
			M_LA4_Summary_Repo.save(existing1);

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_LA4"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			M_LA4_Detail_Entity la1Entity = M_LA4_DETAIL_Repo.findByAcctnumber(acctNo);
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
		ModelAndView mv = new ModelAndView("BRRS/M_LA4"); // ✅ match the report name

		if (acctNo != null) {
			M_LA4_Detail_Entity la1Entity = M_LA4_DETAIL_Repo.findByAcctnumber(acctNo);
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

			M_LA4_Detail_Entity existing = M_LA4_DETAIL_Repo.findByAcctnumber(acctNo);
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
				if (existing.getAcct_balance_in_pula() == null
						|| existing.getAcct_balance_in_pula().compareTo(newProvision) != 0) {
					existing.setAcct_balance_in_pula(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}

			if (isChanged) {
				M_LA4_DETAIL_Repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_LA4_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_LA4_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_LA4 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	public List<Object[]> getM_LA4Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_LA4_Archival_Summary_Entity> latestArchivalList = M_LA4_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_LA4_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_LA4 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getM_LA4Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_LA4_Archival_Summary_Entity> repoData = M_LA4_Archival_Summary_Repo.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_LA4_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_LA4_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_LA4 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] BRRS_M_LA4Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_LA4ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
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
//				return BRRS_M_LA4ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
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
				return BRRS_M_LA4EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_LA4_Summary_Entity1> dataList = M_LA4_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				List<M_LA4_Summary_Entity2> dataList1 = M_LA4_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_LA4 report. Returning empty result.");
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
//NORMAL
					int startRow = 11;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_LA4_Summary_Entity1 record = dataList.get(i);
							M_LA4_Summary_Entity2 record1 = dataList1.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							// R12 Col B
							Cell R12Cell1 = row.createCell(1);
							if (record1.getR12FactoringDebtors() != null) {
								R12Cell1.setCellValue(record1.getR12FactoringDebtors().doubleValue());
								R12Cell1.setCellStyle(numberStyle);
							} else {
								R12Cell1.setCellValue("");
								R12Cell1.setCellStyle(textStyle);
							}

							// R12 Col C
							Cell R12Cell2 = row.createCell(2);
							if (record1.getR12Leasing() != null) {
								R12Cell2.setCellValue(record1.getR12Leasing().doubleValue());
								R12Cell2.setCellStyle(numberStyle);
							} else {
								R12Cell2.setCellValue("");
								R12Cell2.setCellStyle(textStyle);
							}
							// R12 Col D
							Cell R12Cell3 = row.createCell(3);
							if (record.getR12Overdrafts() != null) {
								R12Cell3.setCellValue(record.getR12Overdrafts().doubleValue());
								R12Cell3.setCellStyle(numberStyle);
							} else {
								R12Cell3.setCellValue("");
								R12Cell3.setCellStyle(textStyle);
							}

							// R12 Col E
							Cell R12Cell4 = row.createCell(4);
							if (record.getR12OtherInstallmentLoans() != null) {
								R12Cell4.setCellValue(record.getR12OtherInstallmentLoans().doubleValue());
								R12Cell4.setCellStyle(numberStyle);
							} else {
								R12Cell4.setCellValue("");
								R12Cell4.setCellStyle(textStyle);
							}
							// R13 Col B
							row = sheet.getRow(12);
							// R13 Col B
							Cell R13Cell1 = row.createCell(1);
							if (record1.getR13FactoringDebtors() != null) {
								R13Cell1.setCellValue(record1.getR13FactoringDebtors().doubleValue());
								R13Cell1.setCellStyle(numberStyle);
							} else {
								R13Cell1.setCellValue("");
								R13Cell1.setCellStyle(textStyle);
							}

							// R13 Col C
							Cell R13Cell2 = row.createCell(2);
							if (record1.getR13Leasing() != null) {
								R13Cell2.setCellValue(record1.getR13Leasing().doubleValue());
								R13Cell2.setCellStyle(numberStyle);
							} else {
								R13Cell2.setCellValue("");
								R13Cell2.setCellStyle(textStyle);
							}
							// R13 Col D
							Cell R13Cell3 = row.createCell(3);
							if (record.getR13Overdrafts() != null) {
								R13Cell3.setCellValue(record.getR13Overdrafts().doubleValue());
								R13Cell3.setCellStyle(numberStyle);
							} else {
								R13Cell3.setCellValue("");
								R13Cell3.setCellStyle(textStyle);
							}

							// R13 Col E
							Cell R13Cell4 = row.createCell(4);
							if (record.getR13OtherInstallmentLoans() != null) {
								R13Cell4.setCellValue(record.getR13OtherInstallmentLoans().doubleValue());
								R13Cell4.setCellStyle(numberStyle);
							} else {
								R13Cell4.setCellValue("");
								R13Cell4.setCellStyle(textStyle);
							}
							// R14 Col B
							row = sheet.getRow(13); // Row index 13 is Excel Row 14
							// R14 Col B
							Cell R14Cell1 = row.createCell(1);
							if (record1.getR14FactoringDebtors() != null) {
								R14Cell1.setCellValue(record1.getR14FactoringDebtors().doubleValue());
								R14Cell1.setCellStyle(numberStyle);
							} else {
								R14Cell1.setCellValue("");
								R14Cell1.setCellStyle(textStyle);
							}

							// R14 Col C
							Cell R14Cell2 = row.createCell(2);
							if (record1.getR14Leasing() != null) {
								R14Cell2.setCellValue(record1.getR14Leasing().doubleValue());
								R14Cell2.setCellStyle(numberStyle);
							} else {
								R14Cell2.setCellValue("");
								R14Cell2.setCellStyle(textStyle);
							}

							// R14 Col D
							Cell R14Cell3 = row.createCell(3);
							if (record.getR14Overdrafts() != null) {
								R14Cell3.setCellValue(record.getR14Overdrafts().doubleValue());
								R14Cell3.setCellStyle(numberStyle);
							} else {
								R14Cell3.setCellValue("");
								R14Cell3.setCellStyle(textStyle);
							}

							// R14 Col E
							Cell R14Cell4 = row.createCell(4);
							if (record.getR14OtherInstallmentLoans() != null) {
								R14Cell4.setCellValue(record.getR14OtherInstallmentLoans().doubleValue());
								R14Cell4.setCellStyle(numberStyle);
							} else {
								R14Cell4.setCellValue("");
								R14Cell4.setCellStyle(textStyle);
							}

							// --- R16 (Row Index 15) ---
							row = sheet.getRow(15);
							Cell R16Cell1 = row.createCell(1);
							if (record1.getR16FactoringDebtors() != null) {
								R16Cell1.setCellValue(record1.getR16FactoringDebtors().doubleValue());
								R16Cell1.setCellStyle(numberStyle);
							} else {
								R16Cell1.setCellValue("");
								R16Cell1.setCellStyle(textStyle);
							}
							Cell R16Cell2 = row.createCell(2);
							if (record1.getR16Leasing() != null) {
								R16Cell2.setCellValue(record1.getR16Leasing().doubleValue());
								R16Cell2.setCellStyle(numberStyle);
							} else {
								R16Cell2.setCellValue("");
								R16Cell2.setCellStyle(textStyle);
							}
							Cell R16Cell3 = row.createCell(3);
							if (record.getR16Overdrafts() != null) {
								R16Cell3.setCellValue(record.getR16Overdrafts().doubleValue());
								R16Cell3.setCellStyle(numberStyle);
							} else {
								R16Cell3.setCellValue("");
								R16Cell3.setCellStyle(textStyle);
							}
							Cell R16Cell4 = row.createCell(4);
							if (record.getR16OtherInstallmentLoans() != null) {
								R16Cell4.setCellValue(record.getR16OtherInstallmentLoans().doubleValue());
								R16Cell4.setCellStyle(numberStyle);
							} else {
								R16Cell4.setCellValue("");
								R16Cell4.setCellStyle(textStyle);
							}

							// --- R17 (Row Index 16) ---
							row = sheet.getRow(16);
							Cell R17Cell1 = row.createCell(1);
							if (record1.getR17FactoringDebtors() != null) {
								R17Cell1.setCellValue(record1.getR17FactoringDebtors().doubleValue());
								R17Cell1.setCellStyle(numberStyle);
							} else {
								R17Cell1.setCellValue("");
								R17Cell1.setCellStyle(textStyle);
							}
							Cell R17Cell2 = row.createCell(2);
							if (record1.getR17Leasing() != null) {
								R17Cell2.setCellValue(record1.getR17Leasing().doubleValue());
								R17Cell2.setCellStyle(numberStyle);
							} else {
								R17Cell2.setCellValue("");
								R17Cell2.setCellStyle(textStyle);
							}
							Cell R17Cell3 = row.createCell(3);
							if (record.getR17Overdrafts() != null) {
								R17Cell3.setCellValue(record.getR17Overdrafts().doubleValue());
								R17Cell3.setCellStyle(numberStyle);
							} else {
								R17Cell3.setCellValue("");
								R17Cell3.setCellStyle(textStyle);
							}
							Cell R17Cell4 = row.createCell(4);
							if (record.getR17OtherInstallmentLoans() != null) {
								R17Cell4.setCellValue(record.getR17OtherInstallmentLoans().doubleValue());
								R17Cell4.setCellStyle(numberStyle);
							} else {
								R17Cell4.setCellValue("");
								R17Cell4.setCellStyle(textStyle);
							}

							// --- R18 (Row Index 17) ---
							row = sheet.getRow(17);
							Cell R18Cell1 = row.createCell(1);
							if (record1.getR18FactoringDebtors() != null) {
								R18Cell1.setCellValue(record1.getR18FactoringDebtors().doubleValue());
								R18Cell1.setCellStyle(numberStyle);
							} else {
								R18Cell1.setCellValue("");
								R18Cell1.setCellStyle(textStyle);
							}
							Cell R18Cell2 = row.createCell(2);
							if (record1.getR18Leasing() != null) {
								R18Cell2.setCellValue(record1.getR18Leasing().doubleValue());
								R18Cell2.setCellStyle(numberStyle);
							} else {
								R18Cell2.setCellValue("");
								R18Cell2.setCellStyle(textStyle);
							}
							Cell R18Cell3 = row.createCell(3);
							if (record.getR18Overdrafts() != null) {
								R18Cell3.setCellValue(record.getR18Overdrafts().doubleValue());
								R18Cell3.setCellStyle(numberStyle);
							} else {
								R18Cell3.setCellValue("");
								R18Cell3.setCellStyle(textStyle);
							}
							Cell R18Cell4 = row.createCell(4);
							if (record.getR18OtherInstallmentLoans() != null) {
								R18Cell4.setCellValue(record.getR18OtherInstallmentLoans().doubleValue());
								R18Cell4.setCellStyle(numberStyle);
							} else {
								R18Cell4.setCellValue("");
								R18Cell4.setCellStyle(textStyle);
							}

							// --- R19 (Row Index 18) ---
							row = sheet.getRow(18);
							Cell R19Cell1 = row.createCell(1);
							if (record1.getR19FactoringDebtors() != null) {
								R19Cell1.setCellValue(record1.getR19FactoringDebtors().doubleValue());
								R19Cell1.setCellStyle(numberStyle);
							} else {
								R19Cell1.setCellValue("");
								R19Cell1.setCellStyle(textStyle);
							}
							Cell R19Cell2 = row.createCell(2);
							if (record1.getR19Leasing() != null) {
								R19Cell2.setCellValue(record1.getR19Leasing().doubleValue());
								R19Cell2.setCellStyle(numberStyle);
							} else {
								R19Cell2.setCellValue("");
								R19Cell2.setCellStyle(textStyle);
							}
							Cell R19Cell3 = row.createCell(3);
							if (record.getR19Overdrafts() != null) {
								R19Cell3.setCellValue(record.getR19Overdrafts().doubleValue());
								R19Cell3.setCellStyle(numberStyle);
							} else {
								R19Cell3.setCellValue("");
								R19Cell3.setCellStyle(textStyle);
							}
							Cell R19Cell4 = row.createCell(4);
							if (record.getR19OtherInstallmentLoans() != null) {
								R19Cell4.setCellValue(record.getR19OtherInstallmentLoans().doubleValue());
								R19Cell4.setCellStyle(numberStyle);
							} else {
								R19Cell4.setCellValue("");
								R19Cell4.setCellStyle(textStyle);
							}
							// --- R20 (Row Index 19) ---
							row = sheet.getRow(19);
							Cell R20Cell1 = row.createCell(1);
							if (record1.getR20FactoringDebtors() != null) {
								R20Cell1.setCellValue(record1.getR20FactoringDebtors().doubleValue());
								R20Cell1.setCellStyle(numberStyle);
							} else {
								R20Cell1.setCellValue("");
								R20Cell1.setCellStyle(textStyle);
							}
							Cell R20Cell2 = row.createCell(2);
							if (record1.getR20Leasing() != null) {
								R20Cell2.setCellValue(record1.getR20Leasing().doubleValue());
								R20Cell2.setCellStyle(numberStyle);
							} else {
								R20Cell2.setCellValue("");
								R20Cell2.setCellStyle(textStyle);
							}
							Cell R20Cell3 = row.createCell(3);
							if (record.getR20Overdrafts() != null) {
								R20Cell3.setCellValue(record.getR20Overdrafts().doubleValue());
								R20Cell3.setCellStyle(numberStyle);
							} else {
								R20Cell3.setCellValue("");
								R20Cell3.setCellStyle(textStyle);
							}
							Cell R20Cell4 = row.createCell(4);
							if (record.getR20OtherInstallmentLoans() != null) {
								R20Cell4.setCellValue(record.getR20OtherInstallmentLoans().doubleValue());
								R20Cell4.setCellStyle(numberStyle);
							} else {
								R20Cell4.setCellValue("");
								R20Cell4.setCellStyle(textStyle);
							}

							// --- R21 (Row Index 20) ---
							row = sheet.getRow(20);
							Cell R21Cell1 = row.createCell(1);
							if (record1.getR21FactoringDebtors() != null) {
								R21Cell1.setCellValue(record1.getR21FactoringDebtors().doubleValue());
								R21Cell1.setCellStyle(numberStyle);
							} else {
								R21Cell1.setCellValue("");
								R21Cell1.setCellStyle(textStyle);
							}
							Cell R21Cell2 = row.createCell(2);
							if (record1.getR21Leasing() != null) {
								R21Cell2.setCellValue(record1.getR21Leasing().doubleValue());
								R21Cell2.setCellStyle(numberStyle);
							} else {
								R21Cell2.setCellValue("");
								R21Cell2.setCellStyle(textStyle);
							}
							Cell R21Cell3 = row.createCell(3);
							if (record.getR21Overdrafts() != null) {
								R21Cell3.setCellValue(record.getR21Overdrafts().doubleValue());
								R21Cell3.setCellStyle(numberStyle);
							} else {
								R21Cell3.setCellValue("");
								R21Cell3.setCellStyle(textStyle);
							}
							Cell R21Cell4 = row.createCell(4);
							if (record.getR21OtherInstallmentLoans() != null) {
								R21Cell4.setCellValue(record.getR21OtherInstallmentLoans().doubleValue());
								R21Cell4.setCellStyle(numberStyle);
							} else {
								R21Cell4.setCellValue("");
								R21Cell4.setCellStyle(textStyle);
							}

							// --- R22 (Row Index 21) ---
							row = sheet.getRow(21);
							Cell R22Cell1 = row.createCell(1);
							if (record1.getR22FactoringDebtors() != null) {
								R22Cell1.setCellValue(record1.getR22FactoringDebtors().doubleValue());
								R22Cell1.setCellStyle(numberStyle);
							} else {
								R22Cell1.setCellValue("");
								R22Cell1.setCellStyle(textStyle);
							}
							Cell R22Cell2 = row.createCell(2);
							if (record1.getR22Leasing() != null) {
								R22Cell2.setCellValue(record1.getR22Leasing().doubleValue());
								R22Cell2.setCellStyle(numberStyle);
							} else {
								R22Cell2.setCellValue("");
								R22Cell2.setCellStyle(textStyle);
							}
							Cell R22Cell3 = row.createCell(3);
							if (record.getR22Overdrafts() != null) {
								R22Cell3.setCellValue(record.getR22Overdrafts().doubleValue());
								R22Cell3.setCellStyle(numberStyle);
							} else {
								R22Cell3.setCellValue("");
								R22Cell3.setCellStyle(textStyle);
							}
							Cell R22Cell4 = row.createCell(4);
							if (record.getR22OtherInstallmentLoans() != null) {
								R22Cell4.setCellValue(record.getR22OtherInstallmentLoans().doubleValue());
								R22Cell4.setCellStyle(numberStyle);
							} else {
								R22Cell4.setCellValue("");
								R22Cell4.setCellStyle(textStyle);
							}

							// --- R23 (Row Index 22) ---
							row = sheet.getRow(22);
							Cell R23Cell1 = row.createCell(1);
							if (record1.getR23FactoringDebtors() != null) {
								R23Cell1.setCellValue(record1.getR23FactoringDebtors().doubleValue());
								R23Cell1.setCellStyle(numberStyle);
							} else {
								R23Cell1.setCellValue("");
								R23Cell1.setCellStyle(textStyle);
							}
							Cell R23Cell2 = row.createCell(2);
							if (record1.getR23Leasing() != null) {
								R23Cell2.setCellValue(record1.getR23Leasing().doubleValue());
								R23Cell2.setCellStyle(numberStyle);
							} else {
								R23Cell2.setCellValue("");
								R23Cell2.setCellStyle(textStyle);
							}
							Cell R23Cell3 = row.createCell(3);
							if (record.getR23Overdrafts() != null) {
								R23Cell3.setCellValue(record.getR23Overdrafts().doubleValue());
								R23Cell3.setCellStyle(numberStyle);
							} else {
								R23Cell3.setCellValue("");
								R23Cell3.setCellStyle(textStyle);
							}
							Cell R23Cell4 = row.createCell(4);
							if (record.getR23OtherInstallmentLoans() != null) {
								R23Cell4.setCellValue(record.getR23OtherInstallmentLoans().doubleValue());
								R23Cell4.setCellStyle(numberStyle);
							} else {
								R23Cell4.setCellValue("");
								R23Cell4.setCellStyle(textStyle);
							}

							// --- R24 (Row Index 23) ---
							row = sheet.getRow(23);
							Cell R24Cell1 = row.createCell(1);
							if (record1.getR24FactoringDebtors() != null) {
								R24Cell1.setCellValue(record1.getR24FactoringDebtors().doubleValue());
								R24Cell1.setCellStyle(numberStyle);
							} else {
								R24Cell1.setCellValue("");
								R24Cell1.setCellStyle(textStyle);
							}
							Cell R24Cell2 = row.createCell(2);
							if (record1.getR24Leasing() != null) {
								R24Cell2.setCellValue(record1.getR24Leasing().doubleValue());
								R24Cell2.setCellStyle(numberStyle);
							} else {
								R24Cell2.setCellValue("");
								R24Cell2.setCellStyle(textStyle);
							}
							Cell R24Cell3 = row.createCell(3);
							if (record.getR24Overdrafts() != null) {
								R24Cell3.setCellValue(record.getR24Overdrafts().doubleValue());
								R24Cell3.setCellStyle(numberStyle);
							} else {
								R24Cell3.setCellValue("");
								R24Cell3.setCellStyle(textStyle);
							}
							Cell R24Cell4 = row.createCell(4);
							if (record.getR24OtherInstallmentLoans() != null) {
								R24Cell4.setCellValue(record.getR24OtherInstallmentLoans().doubleValue());
								R24Cell4.setCellStyle(numberStyle);
							} else {
								R24Cell4.setCellValue("");
								R24Cell4.setCellStyle(textStyle);
							}

							// --- R25 (Row Index 24) ---
							row = sheet.getRow(24);
							Cell R25Cell1 = row.createCell(1);
							if (record1.getR25FactoringDebtors() != null) {
								R25Cell1.setCellValue(record1.getR25FactoringDebtors().doubleValue());
								R25Cell1.setCellStyle(numberStyle);
							} else {
								R25Cell1.setCellValue("");
								R25Cell1.setCellStyle(textStyle);
							}
							Cell R25Cell2 = row.createCell(2);
							if (record1.getR25Leasing() != null) {
								R25Cell2.setCellValue(record1.getR25Leasing().doubleValue());
								R25Cell2.setCellStyle(numberStyle);
							} else {
								R25Cell2.setCellValue("");
								R25Cell2.setCellStyle(textStyle);
							}
							Cell R25Cell3 = row.createCell(3);
							if (record.getR25Overdrafts() != null) {
								R25Cell3.setCellValue(record.getR25Overdrafts().doubleValue());
								R25Cell3.setCellStyle(numberStyle);
							} else {
								R25Cell3.setCellValue("");
								R25Cell3.setCellStyle(textStyle);
							}
							Cell R25Cell4 = row.createCell(4);
							if (record.getR25OtherInstallmentLoans() != null) {
								R25Cell4.setCellValue(record.getR25OtherInstallmentLoans().doubleValue());
								R25Cell4.setCellStyle(numberStyle);
							} else {
								R25Cell4.setCellValue("");
								R25Cell4.setCellStyle(textStyle);
							}

							// --- R26 (Row Index 25) ---
							row = sheet.getRow(25);
							Cell R26Cell1 = row.createCell(1);
							if (record1.getR26FactoringDebtors() != null) {
								R26Cell1.setCellValue(record1.getR26FactoringDebtors().doubleValue());
								R26Cell1.setCellStyle(numberStyle);
							} else {
								R26Cell1.setCellValue("");
								R26Cell1.setCellStyle(textStyle);
							}
							Cell R26Cell2 = row.createCell(2);
							if (record1.getR26Leasing() != null) {
								R26Cell2.setCellValue(record1.getR26Leasing().doubleValue());
								R26Cell2.setCellStyle(numberStyle);
							} else {
								R26Cell2.setCellValue("");
								R26Cell2.setCellStyle(textStyle);
							}
							Cell R26Cell3 = row.createCell(3);
							if (record.getR26Overdrafts() != null) {
								R26Cell3.setCellValue(record.getR26Overdrafts().doubleValue());
								R26Cell3.setCellStyle(numberStyle);
							} else {
								R26Cell3.setCellValue("");
								R26Cell3.setCellStyle(textStyle);
							}
							Cell R26Cell4 = row.createCell(4);
							if (record.getR26OtherInstallmentLoans() != null) {
								R26Cell4.setCellValue(record.getR26OtherInstallmentLoans().doubleValue());
								R26Cell4.setCellStyle(numberStyle);
							} else {
								R26Cell4.setCellValue("");
								R26Cell4.setCellStyle(textStyle);
							}

							// --- R27 (Row Index 26) ---
							row = sheet.getRow(26);
							Cell R27Cell1 = row.createCell(1);
							if (record1.getR27FactoringDebtors() != null) {
								R27Cell1.setCellValue(record1.getR27FactoringDebtors().doubleValue());
								R27Cell1.setCellStyle(numberStyle);
							} else {
								R27Cell1.setCellValue("");
								R27Cell1.setCellStyle(textStyle);
							}
							Cell R27Cell2 = row.createCell(2);
							if (record1.getR27Leasing() != null) {
								R27Cell2.setCellValue(record1.getR27Leasing().doubleValue());
								R27Cell2.setCellStyle(numberStyle);
							} else {
								R27Cell2.setCellValue("");
								R27Cell2.setCellStyle(textStyle);
							}
							Cell R27Cell3 = row.createCell(3);
							if (record.getR27Overdrafts() != null) {
								R27Cell3.setCellValue(record.getR27Overdrafts().doubleValue());
								R27Cell3.setCellStyle(numberStyle);
							} else {
								R27Cell3.setCellValue("");
								R27Cell3.setCellStyle(textStyle);
							}
							Cell R27Cell4 = row.createCell(4);
							if (record.getR27OtherInstallmentLoans() != null) {
								R27Cell4.setCellValue(record.getR27OtherInstallmentLoans().doubleValue());
								R27Cell4.setCellStyle(numberStyle);
							} else {
								R27Cell4.setCellValue("");
								R27Cell4.setCellStyle(textStyle);
							}

							// --- R28 (Row Index 27) ---
							row = sheet.getRow(27);
							Cell R28Cell1 = row.createCell(1);
							if (record1.getR28FactoringDebtors() != null) {
								R28Cell1.setCellValue(record1.getR28FactoringDebtors().doubleValue());
								R28Cell1.setCellStyle(numberStyle);
							} else {
								R28Cell1.setCellValue("");
								R28Cell1.setCellStyle(textStyle);
							}
							Cell R28Cell2 = row.createCell(2);
							if (record1.getR28Leasing() != null) {
								R28Cell2.setCellValue(record1.getR28Leasing().doubleValue());
								R28Cell2.setCellStyle(numberStyle);
							} else {
								R28Cell2.setCellValue("");
								R28Cell2.setCellStyle(textStyle);
							}
							Cell R28Cell3 = row.createCell(3);
							if (record.getR28Overdrafts() != null) {
								R28Cell3.setCellValue(record.getR28Overdrafts().doubleValue());
								R28Cell3.setCellStyle(numberStyle);
							} else {
								R28Cell3.setCellValue("");
								R28Cell3.setCellStyle(textStyle);
							}
							Cell R28Cell4 = row.createCell(4);
							if (record.getR28OtherInstallmentLoans() != null) {
								R28Cell4.setCellValue(record.getR28OtherInstallmentLoans().doubleValue());
								R28Cell4.setCellStyle(numberStyle);
							} else {
								R28Cell4.setCellValue("");
								R28Cell4.setCellStyle(textStyle);
							}

							// --- R30 (Row Index 29) ---
							row = sheet.getRow(29);
							Cell R30Cell1 = row.createCell(1);
							if (record1.getR30FactoringDebtors() != null) {
								R30Cell1.setCellValue(record1.getR30FactoringDebtors().doubleValue());
								R30Cell1.setCellStyle(numberStyle);
							} else {
								R30Cell1.setCellValue("");
								R30Cell1.setCellStyle(textStyle);
							}
							Cell R30Cell2 = row.createCell(2);
							if (record1.getR30Leasing() != null) {
								R30Cell2.setCellValue(record1.getR30Leasing().doubleValue());
								R30Cell2.setCellStyle(numberStyle);
							} else {
								R30Cell2.setCellValue("");
								R30Cell2.setCellStyle(textStyle);
							}
							Cell R30Cell3 = row.createCell(3);
							if (record.getR30Overdrafts() != null) {
								R30Cell3.setCellValue(record.getR30Overdrafts().doubleValue());
								R30Cell3.setCellStyle(numberStyle);
							} else {
								R30Cell3.setCellValue("");
								R30Cell3.setCellStyle(textStyle);
							}
							Cell R30Cell4 = row.createCell(4);
							if (record.getR30OtherInstallmentLoans() != null) {
								R30Cell4.setCellValue(record.getR30OtherInstallmentLoans().doubleValue());
								R30Cell4.setCellStyle(numberStyle);
							} else {
								R30Cell4.setCellValue("");
								R30Cell4.setCellStyle(textStyle);
							}

							// --- R31 (Row Index 30) ---
							row = sheet.getRow(30);
							Cell R31Cell1 = row.createCell(1);
							if (record1.getR31FactoringDebtors() != null) {
								R31Cell1.setCellValue(record1.getR31FactoringDebtors().doubleValue());
								R31Cell1.setCellStyle(numberStyle);
							} else {
								R31Cell1.setCellValue("");
								R31Cell1.setCellStyle(textStyle);
							}
							Cell R31Cell2 = row.createCell(2);
							if (record1.getR31Leasing() != null) {
								R31Cell2.setCellValue(record1.getR31Leasing().doubleValue());
								R31Cell2.setCellStyle(numberStyle);
							} else {
								R31Cell2.setCellValue("");
								R31Cell2.setCellStyle(textStyle);
							}
							Cell R31Cell3 = row.createCell(3);
							if (record.getR31Overdrafts() != null) {
								R31Cell3.setCellValue(record.getR31Overdrafts().doubleValue());
								R31Cell3.setCellStyle(numberStyle);
							} else {
								R31Cell3.setCellValue("");
								R31Cell3.setCellStyle(textStyle);
							}
							Cell R31Cell4 = row.createCell(4);
							if (record.getR31OtherInstallmentLoans() != null) {
								R31Cell4.setCellValue(record.getR31OtherInstallmentLoans().doubleValue());
								R31Cell4.setCellStyle(numberStyle);
							} else {
								R31Cell4.setCellValue("");
								R31Cell4.setCellStyle(textStyle);
							}

							// --- R32 (Row Index 31) ---
							row = sheet.getRow(31);
							Cell R32Cell1 = row.createCell(1);
							if (record1.getR32FactoringDebtors() != null) {
								R32Cell1.setCellValue(record1.getR32FactoringDebtors().doubleValue());
								R32Cell1.setCellStyle(numberStyle);
							} else {
								R32Cell1.setCellValue("");
								R32Cell1.setCellStyle(textStyle);
							}
							Cell R32Cell2 = row.createCell(2);
							if (record1.getR32Leasing() != null) {
								R32Cell2.setCellValue(record1.getR32Leasing().doubleValue());
								R32Cell2.setCellStyle(numberStyle);
							} else {
								R32Cell2.setCellValue("");
								R32Cell2.setCellStyle(textStyle);
							}
							Cell R32Cell3 = row.createCell(3);
							if (record.getR32Overdrafts() != null) {
								R32Cell3.setCellValue(record.getR32Overdrafts().doubleValue());
								R32Cell3.setCellStyle(numberStyle);
							} else {
								R32Cell3.setCellValue("");
								R32Cell3.setCellStyle(textStyle);
							}
							Cell R32Cell4 = row.createCell(4);
							if (record.getR32OtherInstallmentLoans() != null) {
								R32Cell4.setCellValue(record.getR32OtherInstallmentLoans().doubleValue());
								R32Cell4.setCellStyle(numberStyle);
							} else {
								R32Cell4.setCellValue("");
								R32Cell4.setCellStyle(textStyle);
							}

							// --- R33 (Row Index 32) ---
							row = sheet.getRow(32);
							Cell R33Cell1 = row.createCell(1);
							if (record1.getR33FactoringDebtors() != null) {
								R33Cell1.setCellValue(record1.getR33FactoringDebtors().doubleValue());
								R33Cell1.setCellStyle(numberStyle);
							} else {
								R33Cell1.setCellValue("");
								R33Cell1.setCellStyle(textStyle);
							}
							Cell R33Cell2 = row.createCell(2);
							if (record1.getR33Leasing() != null) {
								R33Cell2.setCellValue(record1.getR33Leasing().doubleValue());
								R33Cell2.setCellStyle(numberStyle);
							} else {
								R33Cell2.setCellValue("");
								R33Cell2.setCellStyle(textStyle);
							}
							Cell R33Cell3 = row.createCell(3);
							if (record.getR33Overdrafts() != null) {
								R33Cell3.setCellValue(record.getR33Overdrafts().doubleValue());
								R33Cell3.setCellStyle(numberStyle);
							} else {
								R33Cell3.setCellValue("");
								R33Cell3.setCellStyle(textStyle);
							}
							Cell R33Cell4 = row.createCell(4);
							if (record.getR33OtherInstallmentLoans() != null) {
								R33Cell4.setCellValue(record.getR33OtherInstallmentLoans().doubleValue());
								R33Cell4.setCellStyle(numberStyle);
							} else {
								R33Cell4.setCellValue("");
								R33Cell4.setCellStyle(textStyle);
							}

							// --- R34 (Row Index 33) ---
							row = sheet.getRow(33);
							Cell R34Cell1 = row.createCell(1);
							if (record1.getR34FactoringDebtors() != null) {
								R34Cell1.setCellValue(record1.getR34FactoringDebtors().doubleValue());
								R34Cell1.setCellStyle(numberStyle);
							} else {
								R34Cell1.setCellValue("");
								R34Cell1.setCellStyle(textStyle);
							}
							Cell R34Cell2 = row.createCell(2);
							if (record1.getR34Leasing() != null) {
								R34Cell2.setCellValue(record1.getR34Leasing().doubleValue());
								R34Cell2.setCellStyle(numberStyle);
							} else {
								R34Cell2.setCellValue("");
								R34Cell2.setCellStyle(textStyle);
							}
							Cell R34Cell3 = row.createCell(3);
							if (record.getR34Overdrafts() != null) {
								R34Cell3.setCellValue(record.getR34Overdrafts().doubleValue());
								R34Cell3.setCellStyle(numberStyle);
							} else {
								R34Cell3.setCellValue("");
								R34Cell3.setCellStyle(textStyle);
							}
							Cell R34Cell4 = row.createCell(4);
							if (record.getR34OtherInstallmentLoans() != null) {
								R34Cell4.setCellValue(record.getR34OtherInstallmentLoans().doubleValue());
								R34Cell4.setCellStyle(numberStyle);
							} else {
								R34Cell4.setCellValue("");
								R34Cell4.setCellStyle(textStyle);
							}

							// --- R35 (Row Index 34) ---
							row = sheet.getRow(34);
							Cell R35Cell1 = row.createCell(1);
							if (record1.getR35FactoringDebtors() != null) {
								R35Cell1.setCellValue(record1.getR35FactoringDebtors().doubleValue());
								R35Cell1.setCellStyle(numberStyle);
							} else {
								R35Cell1.setCellValue("");
								R35Cell1.setCellStyle(textStyle);
							}
							Cell R35Cell2 = row.createCell(2);
							if (record1.getR35Leasing() != null) {
								R35Cell2.setCellValue(record1.getR35Leasing().doubleValue());
								R35Cell2.setCellStyle(numberStyle);
							} else {
								R35Cell2.setCellValue("");
								R35Cell2.setCellStyle(textStyle);
							}
							Cell R35Cell3 = row.createCell(3);
							if (record.getR35Overdrafts() != null) {
								R35Cell3.setCellValue(record.getR35Overdrafts().doubleValue());
								R35Cell3.setCellStyle(numberStyle);
							} else {
								R35Cell3.setCellValue("");
								R35Cell3.setCellStyle(textStyle);
							}
							Cell R35Cell4 = row.createCell(4);
							if (record.getR35OtherInstallmentLoans() != null) {
								R35Cell4.setCellValue(record.getR35OtherInstallmentLoans().doubleValue());
								R35Cell4.setCellStyle(numberStyle);
							} else {
								R35Cell4.setCellValue("");
								R35Cell4.setCellStyle(textStyle);
							}

							// --- R36 (Row Index 35) ---
							row = sheet.getRow(35);
							Cell R36Cell1 = row.createCell(1);
							if (record1.getR36FactoringDebtors() != null) {
								R36Cell1.setCellValue(record1.getR36FactoringDebtors().doubleValue());
								R36Cell1.setCellStyle(numberStyle);
							} else {
								R36Cell1.setCellValue("");
								R36Cell1.setCellStyle(textStyle);
							}
							Cell R36Cell2 = row.createCell(2);
							if (record1.getR36Leasing() != null) {
								R36Cell2.setCellValue(record1.getR36Leasing().doubleValue());
								R36Cell2.setCellStyle(numberStyle);
							} else {
								R36Cell2.setCellValue("");
								R36Cell2.setCellStyle(textStyle);
							}
							Cell R36Cell3 = row.createCell(3);
							if (record.getR36Overdrafts() != null) {
								R36Cell3.setCellValue(record.getR36Overdrafts().doubleValue());
								R36Cell3.setCellStyle(numberStyle);
							} else {
								R36Cell3.setCellValue("");
								R36Cell3.setCellStyle(textStyle);
							}
							Cell R36Cell4 = row.createCell(4);
							if (record.getR36OtherInstallmentLoans() != null) {
								R36Cell4.setCellValue(record.getR36OtherInstallmentLoans().doubleValue());
								R36Cell4.setCellStyle(numberStyle);
							} else {
								R36Cell4.setCellValue("");
								R36Cell4.setCellStyle(textStyle);
							}

							// --- R37 (Row Index 36) ---
							row = sheet.getRow(36);
							Cell R37Cell1 = row.createCell(1);
							if (record1.getR37FactoringDebtors() != null) {
								R37Cell1.setCellValue(record1.getR37FactoringDebtors().doubleValue());
								R37Cell1.setCellStyle(numberStyle);
							} else {
								R37Cell1.setCellValue("");
								R37Cell1.setCellStyle(textStyle);
							}
							Cell R37Cell2 = row.createCell(2);
							if (record1.getR37Leasing() != null) {
								R37Cell2.setCellValue(record1.getR37Leasing().doubleValue());
								R37Cell2.setCellStyle(numberStyle);
							} else {
								R37Cell2.setCellValue("");
								R37Cell2.setCellStyle(textStyle);
							}
							Cell R37Cell3 = row.createCell(3);
							if (record.getR37Overdrafts() != null) {
								R37Cell3.setCellValue(record.getR37Overdrafts().doubleValue());
								R37Cell3.setCellStyle(numberStyle);
							} else {
								R37Cell3.setCellValue("");
								R37Cell3.setCellStyle(textStyle);
							}
							Cell R37Cell4 = row.createCell(4);
							if (record.getR37OtherInstallmentLoans() != null) {
								R37Cell4.setCellValue(record.getR37OtherInstallmentLoans().doubleValue());
								R37Cell4.setCellStyle(numberStyle);
							} else {
								R37Cell4.setCellValue("");
								R37Cell4.setCellStyle(textStyle);
							}
							// --- R39 (Row Index 38) ---
							row = sheet.getRow(38);
							Cell R39Cell1 = row.createCell(1);
							if (record1.getR39FactoringDebtors() != null) {
								R39Cell1.setCellValue(record1.getR39FactoringDebtors().doubleValue());
								R39Cell1.setCellStyle(numberStyle);
							} else {
								R39Cell1.setCellValue("");
								R39Cell1.setCellStyle(textStyle);
							}
							Cell R39Cell2 = row.createCell(2);
							if (record1.getR39Leasing() != null) {
								R39Cell2.setCellValue(record1.getR39Leasing().doubleValue());
								R39Cell2.setCellStyle(numberStyle);
							} else {
								R39Cell2.setCellValue("");
								R39Cell2.setCellStyle(textStyle);
							}
							Cell R39Cell3 = row.createCell(3);
							if (record.getR39Overdrafts() != null) {
								R39Cell3.setCellValue(record.getR39Overdrafts().doubleValue());
								R39Cell3.setCellStyle(numberStyle);
							} else {
								R39Cell3.setCellValue("");
								R39Cell3.setCellStyle(textStyle);
							}
							Cell R39Cell4 = row.createCell(4);
							if (record.getR39OtherInstallmentLoans() != null) {
								R39Cell4.setCellValue(record.getR39OtherInstallmentLoans().doubleValue());
								R39Cell4.setCellStyle(numberStyle);
							} else {
								R39Cell4.setCellValue("");
								R39Cell4.setCellStyle(textStyle);
							}

							// --- R40 (Row Index 39) ---
							row = sheet.getRow(39);
							Cell R40Cell1 = row.createCell(1);
							if (record1.getR40FactoringDebtors() != null) {
								R40Cell1.setCellValue(record1.getR40FactoringDebtors().doubleValue());
								R40Cell1.setCellStyle(numberStyle);
							} else {
								R40Cell1.setCellValue("");
								R40Cell1.setCellStyle(textStyle);
							}
							Cell R40Cell2 = row.createCell(2);
							if (record1.getR40Leasing() != null) {
								R40Cell2.setCellValue(record1.getR40Leasing().doubleValue());
								R40Cell2.setCellStyle(numberStyle);
							} else {
								R40Cell2.setCellValue("");
								R40Cell2.setCellStyle(textStyle);
							}
							Cell R40Cell3 = row.createCell(3);
							if (record.getR40Overdrafts() != null) {
								R40Cell3.setCellValue(record.getR40Overdrafts().doubleValue());
								R40Cell3.setCellStyle(numberStyle);
							} else {
								R40Cell3.setCellValue("");
								R40Cell3.setCellStyle(textStyle);
							}
							Cell R40Cell4 = row.createCell(4);
							if (record.getR40OtherInstallmentLoans() != null) {
								R40Cell4.setCellValue(record.getR40OtherInstallmentLoans().doubleValue());
								R40Cell4.setCellStyle(numberStyle);
							} else {
								R40Cell4.setCellValue("");
								R40Cell4.setCellStyle(textStyle);
							}
							// --- R42 (Row Index 41) ---
							row = sheet.getRow(41);
							Cell R42Cell1 = row.createCell(1);
							if (record1.getR42FactoringDebtors() != null) {
								R42Cell1.setCellValue(record1.getR42FactoringDebtors().doubleValue());
								R42Cell1.setCellStyle(numberStyle);
							} else {
								R42Cell1.setCellValue("");
								R42Cell1.setCellStyle(textStyle);
							}
							Cell R42Cell2 = row.createCell(2);
							if (record1.getR42Leasing() != null) {
								R42Cell2.setCellValue(record1.getR42Leasing().doubleValue());
								R42Cell2.setCellStyle(numberStyle);
							} else {
								R42Cell2.setCellValue("");
								R42Cell2.setCellStyle(textStyle);
							}
							Cell R42Cell3 = row.createCell(3);
							if (record.getR42Overdrafts() != null) {
								R42Cell3.setCellValue(record.getR42Overdrafts().doubleValue());
								R42Cell3.setCellStyle(numberStyle);
							} else {
								R42Cell3.setCellValue("");
								R42Cell3.setCellStyle(textStyle);
							}
							Cell R42Cell4 = row.createCell(4);
							if (record.getR42OtherInstallmentLoans() != null) {
								R42Cell4.setCellValue(record.getR42OtherInstallmentLoans().doubleValue());
								R42Cell4.setCellStyle(numberStyle);
							} else {
								R42Cell4.setCellValue("");
								R42Cell4.setCellStyle(textStyle);
							}

							// --- R43 (Row Index 42) ---
							row = sheet.getRow(42);
							Cell R43Cell1 = row.createCell(1);
							if (record1.getR43FactoringDebtors() != null) {
								R43Cell1.setCellValue(record1.getR43FactoringDebtors().doubleValue());
								R43Cell1.setCellStyle(numberStyle);
							} else {
								R43Cell1.setCellValue("");
								R43Cell1.setCellStyle(textStyle);
							}
							Cell R43Cell2 = row.createCell(2);
							if (record1.getR43Leasing() != null) {
								R43Cell2.setCellValue(record1.getR43Leasing().doubleValue());
								R43Cell2.setCellStyle(numberStyle);
							} else {
								R43Cell2.setCellValue("");
								R43Cell2.setCellStyle(textStyle);
							}
							Cell R43Cell3 = row.createCell(3);
							if (record.getR43Overdrafts() != null) {
								R43Cell3.setCellValue(record.getR43Overdrafts().doubleValue());
								R43Cell3.setCellStyle(numberStyle);
							} else {
								R43Cell3.setCellValue("");
								R43Cell3.setCellStyle(textStyle);
							}
							Cell R43Cell4 = row.createCell(4);
							if (record.getR43OtherInstallmentLoans() != null) {
								R43Cell4.setCellValue(record.getR43OtherInstallmentLoans().doubleValue());
								R43Cell4.setCellStyle(numberStyle);
							} else {
								R43Cell4.setCellValue("");
								R43Cell4.setCellStyle(textStyle);
							}
							// --- R45 (Row Index 44) ---
							row = sheet.getRow(44);
							Cell R45Cell1 = row.createCell(1);
							if (record1.getR45FactoringDebtors() != null) {
								R45Cell1.setCellValue(record1.getR45FactoringDebtors().doubleValue());
								R45Cell1.setCellStyle(numberStyle);
							} else {
								R45Cell1.setCellValue("");
								R45Cell1.setCellStyle(textStyle);
							}
							Cell R45Cell2 = row.createCell(2);
							if (record1.getR45Leasing() != null) {
								R45Cell2.setCellValue(record1.getR45Leasing().doubleValue());
								R45Cell2.setCellStyle(numberStyle);
							} else {
								R45Cell2.setCellValue("");
								R45Cell2.setCellStyle(textStyle);
							}
							Cell R45Cell3 = row.createCell(3);
							if (record.getR45Overdrafts() != null) {
								R45Cell3.setCellValue(record.getR45Overdrafts().doubleValue());
								R45Cell3.setCellStyle(numberStyle);
							} else {
								R45Cell3.setCellValue("");
								R45Cell3.setCellStyle(textStyle);
							}
							Cell R45Cell4 = row.createCell(4);
							if (record.getR45OtherInstallmentLoans() != null) {
								R45Cell4.setCellValue(record.getR45OtherInstallmentLoans().doubleValue());
								R45Cell4.setCellStyle(numberStyle);
							} else {
								R45Cell4.setCellValue("");
								R45Cell4.setCellStyle(textStyle);
							}

							// --- R46 (Row Index 45) ---
							row = sheet.getRow(45);
							Cell R46Cell1 = row.createCell(1);
							if (record1.getR46FactoringDebtors() != null) {
								R46Cell1.setCellValue(record1.getR46FactoringDebtors().doubleValue());
								R46Cell1.setCellStyle(numberStyle);
							} else {
								R46Cell1.setCellValue("");
								R46Cell1.setCellStyle(textStyle);
							}
							Cell R46Cell2 = row.createCell(2);
							if (record1.getR46Leasing() != null) {
								R46Cell2.setCellValue(record1.getR46Leasing().doubleValue());
								R46Cell2.setCellStyle(numberStyle);
							} else {
								R46Cell2.setCellValue("");
								R46Cell2.setCellStyle(textStyle);
							}
							Cell R46Cell3 = row.createCell(3);
							if (record.getR46Overdrafts() != null) {
								R46Cell3.setCellValue(record.getR46Overdrafts().doubleValue());
								R46Cell3.setCellStyle(numberStyle);
							} else {
								R46Cell3.setCellValue("");
								R46Cell3.setCellStyle(textStyle);
							}
							Cell R46Cell4 = row.createCell(4);
							if (record.getR46OtherInstallmentLoans() != null) {
								R46Cell4.setCellValue(record.getR46OtherInstallmentLoans().doubleValue());
								R46Cell4.setCellStyle(numberStyle);
							} else {
								R46Cell4.setCellValue("");
								R46Cell4.setCellStyle(textStyle);
							}

							// --- R47 (Row Index 46) ---
							row = sheet.getRow(46);
							Cell R47Cell1 = row.createCell(1);
							if (record1.getR47FactoringDebtors() != null) {
								R47Cell1.setCellValue(record1.getR47FactoringDebtors().doubleValue());
								R47Cell1.setCellStyle(numberStyle);
							} else {
								R47Cell1.setCellValue("");
								R47Cell1.setCellStyle(textStyle);
							}
							Cell R47Cell2 = row.createCell(2);
							if (record1.getR47Leasing() != null) {
								R47Cell2.setCellValue(record1.getR47Leasing().doubleValue());
								R47Cell2.setCellStyle(numberStyle);
							} else {
								R47Cell2.setCellValue("");
								R47Cell2.setCellStyle(textStyle);
							}
							Cell R47Cell3 = row.createCell(3);
							if (record.getR47Overdrafts() != null) {
								R47Cell3.setCellValue(record.getR47Overdrafts().doubleValue());
								R47Cell3.setCellStyle(numberStyle);
							} else {
								R47Cell3.setCellValue("");
								R47Cell3.setCellStyle(textStyle);
							}
							Cell R47Cell4 = row.createCell(4);
							if (record.getR47OtherInstallmentLoans() != null) {
								R47Cell4.setCellValue(record.getR47OtherInstallmentLoans().doubleValue());
								R47Cell4.setCellStyle(numberStyle);
							} else {
								R47Cell4.setCellValue("");
								R47Cell4.setCellStyle(textStyle);
							}

							// --- R48 (Row Index 47) ---
							row = sheet.getRow(47);
							Cell R48Cell1 = row.createCell(1);
							if (record1.getR48FactoringDebtors() != null) {
								R48Cell1.setCellValue(record1.getR48FactoringDebtors().doubleValue());
								R48Cell1.setCellStyle(numberStyle);
							} else {
								R48Cell1.setCellValue("");
								R48Cell1.setCellStyle(textStyle);
							}
							Cell R48Cell2 = row.createCell(2);
							if (record1.getR48Leasing() != null) {
								R48Cell2.setCellValue(record1.getR48Leasing().doubleValue());
								R48Cell2.setCellStyle(numberStyle);
							} else {
								R48Cell2.setCellValue("");
								R48Cell2.setCellStyle(textStyle);
							}
							Cell R48Cell3 = row.createCell(3);
							if (record.getR48Overdrafts() != null) {
								R48Cell3.setCellValue(record.getR48Overdrafts().doubleValue());
								R48Cell3.setCellStyle(numberStyle);
							} else {
								R48Cell3.setCellValue("");
								R48Cell3.setCellStyle(textStyle);
							}
							Cell R48Cell4 = row.createCell(4);
							if (record.getR48OtherInstallmentLoans() != null) {
								R48Cell4.setCellValue(record.getR48OtherInstallmentLoans().doubleValue());
								R48Cell4.setCellStyle(numberStyle);
							} else {
								R48Cell4.setCellValue("");
								R48Cell4.setCellStyle(textStyle);
							}

							// --- R50 (Row Index 49) ---
							row = sheet.getRow(49);
							Cell R50Cell1 = row.createCell(1);
							if (record1.getR50FactoringDebtors() != null) {
								R50Cell1.setCellValue(record1.getR50FactoringDebtors().doubleValue());
								R50Cell1.setCellStyle(numberStyle);
							} else {
								R50Cell1.setCellValue("");
								R50Cell1.setCellStyle(textStyle);
							}
							Cell R50Cell2 = row.createCell(2);
							if (record1.getR50Leasing() != null) {
								R50Cell2.setCellValue(record1.getR50Leasing().doubleValue());
								R50Cell2.setCellStyle(numberStyle);
							} else {
								R50Cell2.setCellValue("");
								R50Cell2.setCellStyle(textStyle);
							}
							Cell R50Cell3 = row.createCell(3);
							if (record.getR50Overdrafts() != null) {
								R50Cell3.setCellValue(record.getR50Overdrafts().doubleValue());
								R50Cell3.setCellStyle(numberStyle);
							} else {
								R50Cell3.setCellValue("");
								R50Cell3.setCellStyle(textStyle);
							}
							Cell R50Cell4 = row.createCell(4);
							if (record.getR50OtherInstallmentLoans() != null) {
								R50Cell4.setCellValue(record.getR50OtherInstallmentLoans().doubleValue());
								R50Cell4.setCellStyle(numberStyle);
							} else {
								R50Cell4.setCellValue("");
								R50Cell4.setCellStyle(textStyle);
							}

							// --- R51 (Row Index 50) ---
							row = sheet.getRow(50);
							Cell R51Cell1 = row.createCell(1);
							if (record1.getR51FactoringDebtors() != null) {
								R51Cell1.setCellValue(record1.getR51FactoringDebtors().doubleValue());
								R51Cell1.setCellStyle(numberStyle);
							} else {
								R51Cell1.setCellValue("");
								R51Cell1.setCellStyle(textStyle);
							}
							Cell R51Cell2 = row.createCell(2);
							if (record1.getR51Leasing() != null) {
								R51Cell2.setCellValue(record1.getR51Leasing().doubleValue());
								R51Cell2.setCellStyle(numberStyle);
							} else {
								R51Cell2.setCellValue("");
								R51Cell2.setCellStyle(textStyle);
							}
							Cell R51Cell3 = row.createCell(3);
							if (record.getR51Overdrafts() != null) {
								R51Cell3.setCellValue(record.getR51Overdrafts().doubleValue());
								R51Cell3.setCellStyle(numberStyle);
							} else {
								R51Cell3.setCellValue("");
								R51Cell3.setCellStyle(textStyle);
							}
							Cell R51Cell4 = row.createCell(4);
							if (record.getR51OtherInstallmentLoans() != null) {
								R51Cell4.setCellValue(record.getR51OtherInstallmentLoans().doubleValue());
								R51Cell4.setCellStyle(numberStyle);
							} else {
								R51Cell4.setCellValue("");
								R51Cell4.setCellStyle(textStyle);
							}

							// --- R52 (Row Index 51) ---
							row = sheet.getRow(51);
							Cell R52Cell1 = row.createCell(1);
							if (record1.getR52FactoringDebtors() != null) {
								R52Cell1.setCellValue(record1.getR52FactoringDebtors().doubleValue());
								R52Cell1.setCellStyle(numberStyle);
							} else {
								R52Cell1.setCellValue("");
								R52Cell1.setCellStyle(textStyle);
							}
							Cell R52Cell2 = row.createCell(2);
							if (record1.getR52Leasing() != null) {
								R52Cell2.setCellValue(record1.getR52Leasing().doubleValue());
								R52Cell2.setCellStyle(numberStyle);
							} else {
								R52Cell2.setCellValue("");
								R52Cell2.setCellStyle(textStyle);
							}
							Cell R52Cell3 = row.createCell(3);
							if (record.getR52Overdrafts() != null) {
								R52Cell3.setCellValue(record.getR52Overdrafts().doubleValue());
								R52Cell3.setCellStyle(numberStyle);
							} else {
								R52Cell3.setCellValue("");
								R52Cell3.setCellStyle(textStyle);
							}
							Cell R52Cell4 = row.createCell(4);
							if (record.getR52OtherInstallmentLoans() != null) {
								R52Cell4.setCellValue(record.getR52OtherInstallmentLoans().doubleValue());
								R52Cell4.setCellStyle(numberStyle);
							} else {
								R52Cell4.setCellValue("");
								R52Cell4.setCellStyle(textStyle);
							}
							// --- R54 (Row Index 53) ---
							row = sheet.getRow(53);
							Cell R54Cell1 = row.createCell(1);
							if (record1.getR54FactoringDebtors() != null) {
								R54Cell1.setCellValue(record1.getR54FactoringDebtors().doubleValue());
								R54Cell1.setCellStyle(numberStyle);
							} else {
								R54Cell1.setCellValue("");
								R54Cell1.setCellStyle(textStyle);
							}
							Cell R54Cell2 = row.createCell(2);
							if (record1.getR54Leasing() != null) {
								R54Cell2.setCellValue(record1.getR54Leasing().doubleValue());
								R54Cell2.setCellStyle(numberStyle);
							} else {
								R54Cell2.setCellValue("");
								R54Cell2.setCellStyle(textStyle);
							}
							Cell R54Cell3 = row.createCell(3);
							if (record.getR54Overdrafts() != null) {
								R54Cell3.setCellValue(record.getR54Overdrafts().doubleValue());
								R54Cell3.setCellStyle(numberStyle);
							} else {
								R54Cell3.setCellValue("");
								R54Cell3.setCellStyle(textStyle);
							}
							Cell R54Cell4 = row.createCell(4);
							if (record.getR54OtherInstallmentLoans() != null) {
								R54Cell4.setCellValue(record.getR54OtherInstallmentLoans().doubleValue());
								R54Cell4.setCellStyle(numberStyle);
							} else {
								R54Cell4.setCellValue("");
								R54Cell4.setCellStyle(textStyle);
							}

							// --- R55 (Row Index 54) ---
							row = sheet.getRow(54);
							Cell R55Cell1 = row.createCell(1);
							if (record1.getR55FactoringDebtors() != null) {
								R55Cell1.setCellValue(record1.getR55FactoringDebtors().doubleValue());
								R55Cell1.setCellStyle(numberStyle);
							} else {
								R55Cell1.setCellValue("");
								R55Cell1.setCellStyle(textStyle);
							}
							Cell R55Cell2 = row.createCell(2);
							if (record1.getR55Leasing() != null) {
								R55Cell2.setCellValue(record1.getR55Leasing().doubleValue());
								R55Cell2.setCellStyle(numberStyle);
							} else {
								R55Cell2.setCellValue("");
								R55Cell2.setCellStyle(textStyle);
							}
							Cell R55Cell3 = row.createCell(3);
							if (record.getR55Overdrafts() != null) {
								R55Cell3.setCellValue(record.getR55Overdrafts().doubleValue());
								R55Cell3.setCellStyle(numberStyle);
							} else {
								R55Cell3.setCellValue("");
								R55Cell3.setCellStyle(textStyle);
							}
							Cell R55Cell4 = row.createCell(4);
							if (record.getR55OtherInstallmentLoans() != null) {
								R55Cell4.setCellValue(record.getR55OtherInstallmentLoans().doubleValue());
								R55Cell4.setCellStyle(numberStyle);
							} else {
								R55Cell4.setCellValue("");
								R55Cell4.setCellStyle(textStyle);
							}

							// --- R56 (Row Index 55) ---
							row = sheet.getRow(55);
							Cell R56Cell1 = row.createCell(1);
							if (record1.getR56FactoringDebtors() != null) {
								R56Cell1.setCellValue(record1.getR56FactoringDebtors().doubleValue());
								R56Cell1.setCellStyle(numberStyle);
							} else {
								R56Cell1.setCellValue("");
								R56Cell1.setCellStyle(textStyle);
							}
							Cell R56Cell2 = row.createCell(2);
							if (record1.getR56Leasing() != null) {
								R56Cell2.setCellValue(record1.getR56Leasing().doubleValue());
								R56Cell2.setCellStyle(numberStyle);
							} else {
								R56Cell2.setCellValue("");
								R56Cell2.setCellStyle(textStyle);
							}
							Cell R56Cell3 = row.createCell(3);
							if (record.getR56Overdrafts() != null) {
								R56Cell3.setCellValue(record.getR56Overdrafts().doubleValue());
								R56Cell3.setCellStyle(numberStyle);
							} else {
								R56Cell3.setCellValue("");
								R56Cell3.setCellStyle(textStyle);
							}
							Cell R56Cell4 = row.createCell(4);
							if (record.getR56OtherInstallmentLoans() != null) {
								R56Cell4.setCellValue(record.getR56OtherInstallmentLoans().doubleValue());
								R56Cell4.setCellStyle(numberStyle);
							} else {
								R56Cell4.setCellValue("");
								R56Cell4.setCellStyle(textStyle);
							}
							// --- R58 (Row Index 57) ---
							row = sheet.getRow(57);
							Cell R58Cell1 = row.createCell(1);
							if (record1.getR58FactoringDebtors() != null) {
								R58Cell1.setCellValue(record1.getR58FactoringDebtors().doubleValue());
								R58Cell1.setCellStyle(numberStyle);
							} else {
								R58Cell1.setCellValue("");
								R58Cell1.setCellStyle(textStyle);
							}
							Cell R58Cell2 = row.createCell(2);
							if (record1.getR58Leasing() != null) {
								R58Cell2.setCellValue(record1.getR58Leasing().doubleValue());
								R58Cell2.setCellStyle(numberStyle);
							} else {
								R58Cell2.setCellValue("");
								R58Cell2.setCellStyle(textStyle);
							}
							Cell R58Cell3 = row.createCell(3);
							if (record.getR58Overdrafts() != null) {
								R58Cell3.setCellValue(record.getR58Overdrafts().doubleValue());
								R58Cell3.setCellStyle(numberStyle);
							} else {
								R58Cell3.setCellValue("");
								R58Cell3.setCellStyle(textStyle);
							}
							Cell R58Cell4 = row.createCell(4);
							if (record.getR58OtherInstallmentLoans() != null) {
								R58Cell4.setCellValue(record.getR58OtherInstallmentLoans().doubleValue());
								R58Cell4.setCellStyle(numberStyle);
							} else {
								R58Cell4.setCellValue("");
								R58Cell4.setCellStyle(textStyle);
							}

							// --- R59 (Row Index 58) ---
							row = sheet.getRow(58);
							Cell R59Cell1 = row.createCell(1);
							if (record1.getR59FactoringDebtors() != null) {
								R59Cell1.setCellValue(record1.getR59FactoringDebtors().doubleValue());
								R59Cell1.setCellStyle(numberStyle);
							} else {
								R59Cell1.setCellValue("");
								R59Cell1.setCellStyle(textStyle);
							}
							Cell R59Cell2 = row.createCell(2);
							if (record1.getR59Leasing() != null) {
								R59Cell2.setCellValue(record1.getR59Leasing().doubleValue());
								R59Cell2.setCellStyle(numberStyle);
							} else {
								R59Cell2.setCellValue("");
								R59Cell2.setCellStyle(textStyle);
							}
							Cell R59Cell3 = row.createCell(3);
							if (record.getR59Overdrafts() != null) {
								R59Cell3.setCellValue(record.getR59Overdrafts().doubleValue());
								R59Cell3.setCellStyle(numberStyle);
							} else {
								R59Cell3.setCellValue("");
								R59Cell3.setCellStyle(textStyle);
							}
							Cell R59Cell4 = row.createCell(4);
							if (record.getR59OtherInstallmentLoans() != null) {
								R59Cell4.setCellValue(record.getR59OtherInstallmentLoans().doubleValue());
								R59Cell4.setCellStyle(numberStyle);
							} else {
								R59Cell4.setCellValue("");
								R59Cell4.setCellStyle(textStyle);
							}

							// --- R60 (Row Index 59) ---
							row = sheet.getRow(59);
							Cell R60Cell1 = row.createCell(1);
							if (record1.getR60FactoringDebtors() != null) {
								R60Cell1.setCellValue(record1.getR60FactoringDebtors().doubleValue());
								R60Cell1.setCellStyle(numberStyle);
							} else {
								R60Cell1.setCellValue("");
								R60Cell1.setCellStyle(textStyle);
							}
							Cell R60Cell2 = row.createCell(2);
							if (record1.getR60Leasing() != null) {
								R60Cell2.setCellValue(record1.getR60Leasing().doubleValue());
								R60Cell2.setCellStyle(numberStyle);
							} else {
								R60Cell2.setCellValue("");
								R60Cell2.setCellStyle(textStyle);
							}
							Cell R60Cell3 = row.createCell(3);
							if (record.getR60Overdrafts() != null) {
								R60Cell3.setCellValue(record.getR60Overdrafts().doubleValue());
								R60Cell3.setCellStyle(numberStyle);
							} else {
								R60Cell3.setCellValue("");
								R60Cell3.setCellStyle(textStyle);
							}
							Cell R60Cell4 = row.createCell(4);
							if (record.getR60OtherInstallmentLoans() != null) {
								R60Cell4.setCellValue(record.getR60OtherInstallmentLoans().doubleValue());
								R60Cell4.setCellStyle(numberStyle);
							} else {
								R60Cell4.setCellValue("");
								R60Cell4.setCellStyle(textStyle);
							}

							// --- R61 (Row Index 60) ---
							row = sheet.getRow(60);
							Cell R61Cell1 = row.createCell(1);
							if (record1.getR61FactoringDebtors() != null) {
								R61Cell1.setCellValue(record1.getR61FactoringDebtors().doubleValue());
								R61Cell1.setCellStyle(numberStyle);
							} else {
								R61Cell1.setCellValue("");
								R61Cell1.setCellStyle(textStyle);
							}
							Cell R61Cell2 = row.createCell(2);
							if (record1.getR61Leasing() != null) {
								R61Cell2.setCellValue(record1.getR61Leasing().doubleValue());
								R61Cell2.setCellStyle(numberStyle);
							} else {
								R61Cell2.setCellValue("");
								R61Cell2.setCellStyle(textStyle);
							}
							Cell R61Cell3 = row.createCell(3);
							if (record.getR61Overdrafts() != null) {
								R61Cell3.setCellValue(record.getR61Overdrafts().doubleValue());
								R61Cell3.setCellStyle(numberStyle);
							} else {
								R61Cell3.setCellValue("");
								R61Cell3.setCellStyle(textStyle);
							}
							Cell R61Cell4 = row.createCell(4);
							if (record.getR61OtherInstallmentLoans() != null) {
								R61Cell4.setCellValue(record.getR61OtherInstallmentLoans().doubleValue());
								R61Cell4.setCellStyle(numberStyle);
							} else {
								R61Cell4.setCellValue("");
								R61Cell4.setCellStyle(textStyle);
							}

							// --- R62 (Row Index 61) ---
							row = sheet.getRow(61);
							Cell R62Cell1 = row.createCell(1);
							if (record1.getR62FactoringDebtors() != null) {
								R62Cell1.setCellValue(record1.getR62FactoringDebtors().doubleValue());
								R62Cell1.setCellStyle(numberStyle);
							} else {
								R62Cell1.setCellValue("");
								R62Cell1.setCellStyle(textStyle);
							}
							Cell R62Cell2 = row.createCell(2);
							if (record1.getR62Leasing() != null) {
								R62Cell2.setCellValue(record1.getR62Leasing().doubleValue());
								R62Cell2.setCellStyle(numberStyle);
							} else {
								R62Cell2.setCellValue("");
								R62Cell2.setCellStyle(textStyle);
							}
							Cell R62Cell3 = row.createCell(3);
							if (record.getR62Overdrafts() != null) {
								R62Cell3.setCellValue(record.getR62Overdrafts().doubleValue());
								R62Cell3.setCellStyle(numberStyle);
							} else {
								R62Cell3.setCellValue("");
								R62Cell3.setCellStyle(textStyle);
							}
							Cell R62Cell4 = row.createCell(4);
							if (record.getR62OtherInstallmentLoans() != null) {
								R62Cell4.setCellValue(record.getR62OtherInstallmentLoans().doubleValue());
								R62Cell4.setCellStyle(numberStyle);
							} else {
								R62Cell4.setCellValue("");
								R62Cell4.setCellStyle(textStyle);
							}

							// --- R63 (Row Index 62) ---
							row = sheet.getRow(62);
							Cell R63Cell1 = row.createCell(1);
							if (record1.getR63FactoringDebtors() != null) {
								R63Cell1.setCellValue(record1.getR63FactoringDebtors().doubleValue());
								R63Cell1.setCellStyle(numberStyle);
							} else {
								R63Cell1.setCellValue("");
								R63Cell1.setCellStyle(textStyle);
							}
							Cell R63Cell2 = row.createCell(2);
							if (record1.getR63Leasing() != null) {
								R63Cell2.setCellValue(record1.getR63Leasing().doubleValue());
								R63Cell2.setCellStyle(numberStyle);
							} else {
								R63Cell2.setCellValue("");
								R63Cell2.setCellStyle(textStyle);
							}
							Cell R63Cell3 = row.createCell(3);
							if (record.getR63Overdrafts() != null) {
								R63Cell3.setCellValue(record.getR63Overdrafts().doubleValue());
								R63Cell3.setCellStyle(numberStyle);
							} else {
								R63Cell3.setCellValue("");
								R63Cell3.setCellStyle(textStyle);
							}
							Cell R63Cell4 = row.createCell(4);
							if (record.getR63OtherInstallmentLoans() != null) {
								R63Cell4.setCellValue(record.getR63OtherInstallmentLoans().doubleValue());
								R63Cell4.setCellStyle(numberStyle);
							} else {
								R63Cell4.setCellValue("");
								R63Cell4.setCellStyle(textStyle);
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
	public byte[] BRRS_M_LA4EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA4EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
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
//				return BRRS_M_LA4ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
		} else {
			List<M_LA4_Summary_Entity1> dataList = M_LA4_Summary_Repo.getdatabydateList(dateformat.parse(todate));
			List<M_LA4_Summary_Entity2> dataList1 = M_LA4_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_LA4 report. Returning empty result.");
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

				int startRow = 11;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_LA4_Summary_Entity1 record = dataList.get(i);
						M_LA4_Summary_Entity2 record1 = dataList1.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL

						// R12 Col B
						Cell R12Cell1 = row.createCell(1);
						if (record1.getR12FactoringDebtors() != null) {
							R12Cell1.setCellValue(record1.getR12FactoringDebtors().doubleValue());
							R12Cell1.setCellStyle(numberStyle);
						} else {
							R12Cell1.setCellValue("");
							R12Cell1.setCellStyle(textStyle);
						}

						// R12 Col C
						Cell R12Cell2 = row.createCell(2);
						if (record1.getR12Leasing() != null) {
							R12Cell2.setCellValue(record1.getR12Leasing().doubleValue());
							R12Cell2.setCellStyle(numberStyle);
						} else {
							R12Cell2.setCellValue("");
							R12Cell2.setCellStyle(textStyle);
						}
						// R12 Col D
						Cell R12Cell3 = row.createCell(3);
						if (record.getR12Overdrafts() != null) {
							R12Cell3.setCellValue(record.getR12Overdrafts().doubleValue());
							R12Cell3.setCellStyle(numberStyle);
						} else {
							R12Cell3.setCellValue("");
							R12Cell3.setCellStyle(textStyle);
						}

						// R12 Col E
						Cell R12Cell4 = row.createCell(4);
						if (record.getR12OtherInstallmentLoans() != null) {
							R12Cell4.setCellValue(record.getR12OtherInstallmentLoans().doubleValue());
							R12Cell4.setCellStyle(numberStyle);
						} else {
							R12Cell4.setCellValue("");
							R12Cell4.setCellStyle(textStyle);
						}
						// R13 Col B
						row = sheet.getRow(12);
						// R13 Col B
						Cell R13Cell1 = row.createCell(1);
						if (record1.getR13FactoringDebtors() != null) {
							R13Cell1.setCellValue(record1.getR13FactoringDebtors().doubleValue());
							R13Cell1.setCellStyle(numberStyle);
						} else {
							R13Cell1.setCellValue("");
							R13Cell1.setCellStyle(textStyle);
						}

						// R13 Col C
						Cell R13Cell2 = row.createCell(2);
						if (record1.getR13Leasing() != null) {
							R13Cell2.setCellValue(record1.getR13Leasing().doubleValue());
							R13Cell2.setCellStyle(numberStyle);
						} else {
							R13Cell2.setCellValue("");
							R13Cell2.setCellStyle(textStyle);
						}
						// R13 Col D
						Cell R13Cell3 = row.createCell(3);
						if (record.getR13Overdrafts() != null) {
							R13Cell3.setCellValue(record.getR13Overdrafts().doubleValue());
							R13Cell3.setCellStyle(numberStyle);
						} else {
							R13Cell3.setCellValue("");
							R13Cell3.setCellStyle(textStyle);
						}

						// R13 Col E
						Cell R13Cell4 = row.createCell(4);
						if (record.getR13OtherInstallmentLoans() != null) {
							R13Cell4.setCellValue(record.getR13OtherInstallmentLoans().doubleValue());
							R13Cell4.setCellStyle(numberStyle);
						} else {
							R13Cell4.setCellValue("");
							R13Cell4.setCellStyle(textStyle);
						}
						// R14 Col B
						row = sheet.getRow(13); // Row index 13 is Excel Row 14
						// R14 Col B
						Cell R14Cell1 = row.createCell(1);
						if (record1.getR14FactoringDebtors() != null) {
							R14Cell1.setCellValue(record1.getR14FactoringDebtors().doubleValue());
							R14Cell1.setCellStyle(numberStyle);
						} else {
							R14Cell1.setCellValue("");
							R14Cell1.setCellStyle(textStyle);
						}

						// R14 Col C
						Cell R14Cell2 = row.createCell(2);
						if (record1.getR14Leasing() != null) {
							R14Cell2.setCellValue(record1.getR14Leasing().doubleValue());
							R14Cell2.setCellStyle(numberStyle);
						} else {
							R14Cell2.setCellValue("");
							R14Cell2.setCellStyle(textStyle);
						}

						// R14 Col D
						Cell R14Cell3 = row.createCell(3);
						if (record.getR14Overdrafts() != null) {
							R14Cell3.setCellValue(record.getR14Overdrafts().doubleValue());
							R14Cell3.setCellStyle(numberStyle);
						} else {
							R14Cell3.setCellValue("");
							R14Cell3.setCellStyle(textStyle);
						}

						// R14 Col E
						Cell R14Cell4 = row.createCell(4);
						if (record.getR14OtherInstallmentLoans() != null) {
							R14Cell4.setCellValue(record.getR14OtherInstallmentLoans().doubleValue());
							R14Cell4.setCellStyle(numberStyle);
						} else {
							R14Cell4.setCellValue("");
							R14Cell4.setCellStyle(textStyle);
						}

						// --- R16 (Row Index 15) ---
						row = sheet.getRow(15);
						Cell R16Cell1 = row.createCell(1);
						if (record1.getR16FactoringDebtors() != null) {
							R16Cell1.setCellValue(record1.getR16FactoringDebtors().doubleValue());
							R16Cell1.setCellStyle(numberStyle);
						} else {
							R16Cell1.setCellValue("");
							R16Cell1.setCellStyle(textStyle);
						}
						Cell R16Cell2 = row.createCell(2);
						if (record1.getR16Leasing() != null) {
							R16Cell2.setCellValue(record1.getR16Leasing().doubleValue());
							R16Cell2.setCellStyle(numberStyle);
						} else {
							R16Cell2.setCellValue("");
							R16Cell2.setCellStyle(textStyle);
						}
						Cell R16Cell3 = row.createCell(3);
						if (record.getR16Overdrafts() != null) {
							R16Cell3.setCellValue(record.getR16Overdrafts().doubleValue());
							R16Cell3.setCellStyle(numberStyle);
						} else {
							R16Cell3.setCellValue("");
							R16Cell3.setCellStyle(textStyle);
						}
						Cell R16Cell4 = row.createCell(4);
						if (record.getR16OtherInstallmentLoans() != null) {
							R16Cell4.setCellValue(record.getR16OtherInstallmentLoans().doubleValue());
							R16Cell4.setCellStyle(numberStyle);
						} else {
							R16Cell4.setCellValue("");
							R16Cell4.setCellStyle(textStyle);
						}

						// --- R17 (Row Index 16) ---
						row = sheet.getRow(16);
						Cell R17Cell1 = row.createCell(1);
						if (record1.getR17FactoringDebtors() != null) {
							R17Cell1.setCellValue(record1.getR17FactoringDebtors().doubleValue());
							R17Cell1.setCellStyle(numberStyle);
						} else {
							R17Cell1.setCellValue("");
							R17Cell1.setCellStyle(textStyle);
						}
						Cell R17Cell2 = row.createCell(2);
						if (record1.getR17Leasing() != null) {
							R17Cell2.setCellValue(record1.getR17Leasing().doubleValue());
							R17Cell2.setCellStyle(numberStyle);
						} else {
							R17Cell2.setCellValue("");
							R17Cell2.setCellStyle(textStyle);
						}
						Cell R17Cell3 = row.createCell(3);
						if (record.getR17Overdrafts() != null) {
							R17Cell3.setCellValue(record.getR17Overdrafts().doubleValue());
							R17Cell3.setCellStyle(numberStyle);
						} else {
							R17Cell3.setCellValue("");
							R17Cell3.setCellStyle(textStyle);
						}
						Cell R17Cell4 = row.createCell(4);
						if (record.getR17OtherInstallmentLoans() != null) {
							R17Cell4.setCellValue(record.getR17OtherInstallmentLoans().doubleValue());
							R17Cell4.setCellStyle(numberStyle);
						} else {
							R17Cell4.setCellValue("");
							R17Cell4.setCellStyle(textStyle);
						}

						// --- R18 (Row Index 17) ---
						row = sheet.getRow(17);
						Cell R18Cell1 = row.createCell(1);
						if (record1.getR18FactoringDebtors() != null) {
							R18Cell1.setCellValue(record1.getR18FactoringDebtors().doubleValue());
							R18Cell1.setCellStyle(numberStyle);
						} else {
							R18Cell1.setCellValue("");
							R18Cell1.setCellStyle(textStyle);
						}
						Cell R18Cell2 = row.createCell(2);
						if (record1.getR18Leasing() != null) {
							R18Cell2.setCellValue(record1.getR18Leasing().doubleValue());
							R18Cell2.setCellStyle(numberStyle);
						} else {
							R18Cell2.setCellValue("");
							R18Cell2.setCellStyle(textStyle);
						}
						Cell R18Cell3 = row.createCell(3);
						if (record.getR18Overdrafts() != null) {
							R18Cell3.setCellValue(record.getR18Overdrafts().doubleValue());
							R18Cell3.setCellStyle(numberStyle);
						} else {
							R18Cell3.setCellValue("");
							R18Cell3.setCellStyle(textStyle);
						}
						Cell R18Cell4 = row.createCell(4);
						if (record.getR18OtherInstallmentLoans() != null) {
							R18Cell4.setCellValue(record.getR18OtherInstallmentLoans().doubleValue());
							R18Cell4.setCellStyle(numberStyle);
						} else {
							R18Cell4.setCellValue("");
							R18Cell4.setCellStyle(textStyle);
						}

						// --- R19 (Row Index 18) ---
						row = sheet.getRow(18);
						Cell R19Cell1 = row.createCell(1);
						if (record1.getR19FactoringDebtors() != null) {
							R19Cell1.setCellValue(record1.getR19FactoringDebtors().doubleValue());
							R19Cell1.setCellStyle(numberStyle);
						} else {
							R19Cell1.setCellValue("");
							R19Cell1.setCellStyle(textStyle);
						}
						Cell R19Cell2 = row.createCell(2);
						if (record1.getR19Leasing() != null) {
							R19Cell2.setCellValue(record1.getR19Leasing().doubleValue());
							R19Cell2.setCellStyle(numberStyle);
						} else {
							R19Cell2.setCellValue("");
							R19Cell2.setCellStyle(textStyle);
						}
						Cell R19Cell3 = row.createCell(3);
						if (record.getR19Overdrafts() != null) {
							R19Cell3.setCellValue(record.getR19Overdrafts().doubleValue());
							R19Cell3.setCellStyle(numberStyle);
						} else {
							R19Cell3.setCellValue("");
							R19Cell3.setCellStyle(textStyle);
						}
						Cell R19Cell4 = row.createCell(4);
						if (record.getR19OtherInstallmentLoans() != null) {
							R19Cell4.setCellValue(record.getR19OtherInstallmentLoans().doubleValue());
							R19Cell4.setCellStyle(numberStyle);
						} else {
							R19Cell4.setCellValue("");
							R19Cell4.setCellStyle(textStyle);
						}
						// --- R20 (Row Index 19) ---
						row = sheet.getRow(19);
						Cell R20Cell1 = row.createCell(1);
						if (record1.getR20FactoringDebtors() != null) {
							R20Cell1.setCellValue(record1.getR20FactoringDebtors().doubleValue());
							R20Cell1.setCellStyle(numberStyle);
						} else {
							R20Cell1.setCellValue("");
							R20Cell1.setCellStyle(textStyle);
						}
						Cell R20Cell2 = row.createCell(2);
						if (record1.getR20Leasing() != null) {
							R20Cell2.setCellValue(record1.getR20Leasing().doubleValue());
							R20Cell2.setCellStyle(numberStyle);
						} else {
							R20Cell2.setCellValue("");
							R20Cell2.setCellStyle(textStyle);
						}
						Cell R20Cell3 = row.createCell(3);
						if (record.getR20Overdrafts() != null) {
							R20Cell3.setCellValue(record.getR20Overdrafts().doubleValue());
							R20Cell3.setCellStyle(numberStyle);
						} else {
							R20Cell3.setCellValue("");
							R20Cell3.setCellStyle(textStyle);
						}
						Cell R20Cell4 = row.createCell(4);
						if (record.getR20OtherInstallmentLoans() != null) {
							R20Cell4.setCellValue(record.getR20OtherInstallmentLoans().doubleValue());
							R20Cell4.setCellStyle(numberStyle);
						} else {
							R20Cell4.setCellValue("");
							R20Cell4.setCellStyle(textStyle);
						}

						// --- R21 (Row Index 20) ---
						row = sheet.getRow(20);
						Cell R21Cell1 = row.createCell(1);
						if (record1.getR21FactoringDebtors() != null) {
							R21Cell1.setCellValue(record1.getR21FactoringDebtors().doubleValue());
							R21Cell1.setCellStyle(numberStyle);
						} else {
							R21Cell1.setCellValue("");
							R21Cell1.setCellStyle(textStyle);
						}
						Cell R21Cell2 = row.createCell(2);
						if (record1.getR21Leasing() != null) {
							R21Cell2.setCellValue(record1.getR21Leasing().doubleValue());
							R21Cell2.setCellStyle(numberStyle);
						} else {
							R21Cell2.setCellValue("");
							R21Cell2.setCellStyle(textStyle);
						}
						Cell R21Cell3 = row.createCell(3);
						if (record.getR21Overdrafts() != null) {
							R21Cell3.setCellValue(record.getR21Overdrafts().doubleValue());
							R21Cell3.setCellStyle(numberStyle);
						} else {
							R21Cell3.setCellValue("");
							R21Cell3.setCellStyle(textStyle);
						}
						Cell R21Cell4 = row.createCell(4);
						if (record.getR21OtherInstallmentLoans() != null) {
							R21Cell4.setCellValue(record.getR21OtherInstallmentLoans().doubleValue());
							R21Cell4.setCellStyle(numberStyle);
						} else {
							R21Cell4.setCellValue("");
							R21Cell4.setCellStyle(textStyle);
						}

						// --- R22 (Row Index 21) ---
						row = sheet.getRow(21);
						Cell R22Cell1 = row.createCell(1);
						if (record1.getR22FactoringDebtors() != null) {
							R22Cell1.setCellValue(record1.getR22FactoringDebtors().doubleValue());
							R22Cell1.setCellStyle(numberStyle);
						} else {
							R22Cell1.setCellValue("");
							R22Cell1.setCellStyle(textStyle);
						}
						Cell R22Cell2 = row.createCell(2);
						if (record1.getR22Leasing() != null) {
							R22Cell2.setCellValue(record1.getR22Leasing().doubleValue());
							R22Cell2.setCellStyle(numberStyle);
						} else {
							R22Cell2.setCellValue("");
							R22Cell2.setCellStyle(textStyle);
						}
						Cell R22Cell3 = row.createCell(3);
						if (record.getR22Overdrafts() != null) {
							R22Cell3.setCellValue(record.getR22Overdrafts().doubleValue());
							R22Cell3.setCellStyle(numberStyle);
						} else {
							R22Cell3.setCellValue("");
							R22Cell3.setCellStyle(textStyle);
						}
						Cell R22Cell4 = row.createCell(4);
						if (record.getR22OtherInstallmentLoans() != null) {
							R22Cell4.setCellValue(record.getR22OtherInstallmentLoans().doubleValue());
							R22Cell4.setCellStyle(numberStyle);
						} else {
							R22Cell4.setCellValue("");
							R22Cell4.setCellStyle(textStyle);
						}

						// --- R23 (Row Index 22) ---
						row = sheet.getRow(22);
						Cell R23Cell1 = row.createCell(1);
						if (record1.getR23FactoringDebtors() != null) {
							R23Cell1.setCellValue(record1.getR23FactoringDebtors().doubleValue());
							R23Cell1.setCellStyle(numberStyle);
						} else {
							R23Cell1.setCellValue("");
							R23Cell1.setCellStyle(textStyle);
						}
						Cell R23Cell2 = row.createCell(2);
						if (record1.getR23Leasing() != null) {
							R23Cell2.setCellValue(record1.getR23Leasing().doubleValue());
							R23Cell2.setCellStyle(numberStyle);
						} else {
							R23Cell2.setCellValue("");
							R23Cell2.setCellStyle(textStyle);
						}
						Cell R23Cell3 = row.createCell(3);
						if (record.getR23Overdrafts() != null) {
							R23Cell3.setCellValue(record.getR23Overdrafts().doubleValue());
							R23Cell3.setCellStyle(numberStyle);
						} else {
							R23Cell3.setCellValue("");
							R23Cell3.setCellStyle(textStyle);
						}
						Cell R23Cell4 = row.createCell(4);
						if (record.getR23OtherInstallmentLoans() != null) {
							R23Cell4.setCellValue(record.getR23OtherInstallmentLoans().doubleValue());
							R23Cell4.setCellStyle(numberStyle);
						} else {
							R23Cell4.setCellValue("");
							R23Cell4.setCellStyle(textStyle);
						}

						// --- R24 (Row Index 23) ---
						row = sheet.getRow(23);
						Cell R24Cell1 = row.createCell(1);
						if (record1.getR24FactoringDebtors() != null) {
							R24Cell1.setCellValue(record1.getR24FactoringDebtors().doubleValue());
							R24Cell1.setCellStyle(numberStyle);
						} else {
							R24Cell1.setCellValue("");
							R24Cell1.setCellStyle(textStyle);
						}
						Cell R24Cell2 = row.createCell(2);
						if (record1.getR24Leasing() != null) {
							R24Cell2.setCellValue(record1.getR24Leasing().doubleValue());
							R24Cell2.setCellStyle(numberStyle);
						} else {
							R24Cell2.setCellValue("");
							R24Cell2.setCellStyle(textStyle);
						}
						Cell R24Cell3 = row.createCell(3);
						if (record.getR24Overdrafts() != null) {
							R24Cell3.setCellValue(record.getR24Overdrafts().doubleValue());
							R24Cell3.setCellStyle(numberStyle);
						} else {
							R24Cell3.setCellValue("");
							R24Cell3.setCellStyle(textStyle);
						}
						Cell R24Cell4 = row.createCell(4);
						if (record.getR24OtherInstallmentLoans() != null) {
							R24Cell4.setCellValue(record.getR24OtherInstallmentLoans().doubleValue());
							R24Cell4.setCellStyle(numberStyle);
						} else {
							R24Cell4.setCellValue("");
							R24Cell4.setCellStyle(textStyle);
						}

						// --- R25 (Row Index 24) ---
						row = sheet.getRow(24);
						Cell R25Cell1 = row.createCell(1);
						if (record1.getR25FactoringDebtors() != null) {
							R25Cell1.setCellValue(record1.getR25FactoringDebtors().doubleValue());
							R25Cell1.setCellStyle(numberStyle);
						} else {
							R25Cell1.setCellValue("");
							R25Cell1.setCellStyle(textStyle);
						}
						Cell R25Cell2 = row.createCell(2);
						if (record1.getR25Leasing() != null) {
							R25Cell2.setCellValue(record1.getR25Leasing().doubleValue());
							R25Cell2.setCellStyle(numberStyle);
						} else {
							R25Cell2.setCellValue("");
							R25Cell2.setCellStyle(textStyle);
						}
						Cell R25Cell3 = row.createCell(3);
						if (record.getR25Overdrafts() != null) {
							R25Cell3.setCellValue(record.getR25Overdrafts().doubleValue());
							R25Cell3.setCellStyle(numberStyle);
						} else {
							R25Cell3.setCellValue("");
							R25Cell3.setCellStyle(textStyle);
						}
						Cell R25Cell4 = row.createCell(4);
						if (record.getR25OtherInstallmentLoans() != null) {
							R25Cell4.setCellValue(record.getR25OtherInstallmentLoans().doubleValue());
							R25Cell4.setCellStyle(numberStyle);
						} else {
							R25Cell4.setCellValue("");
							R25Cell4.setCellStyle(textStyle);
						}

						// --- R26 (Row Index 25) ---
						row = sheet.getRow(25);
						Cell R26Cell1 = row.createCell(1);
						if (record1.getR26FactoringDebtors() != null) {
							R26Cell1.setCellValue(record1.getR26FactoringDebtors().doubleValue());
							R26Cell1.setCellStyle(numberStyle);
						} else {
							R26Cell1.setCellValue("");
							R26Cell1.setCellStyle(textStyle);
						}
						Cell R26Cell2 = row.createCell(2);
						if (record1.getR26Leasing() != null) {
							R26Cell2.setCellValue(record1.getR26Leasing().doubleValue());
							R26Cell2.setCellStyle(numberStyle);
						} else {
							R26Cell2.setCellValue("");
							R26Cell2.setCellStyle(textStyle);
						}
						Cell R26Cell3 = row.createCell(3);
						if (record.getR26Overdrafts() != null) {
							R26Cell3.setCellValue(record.getR26Overdrafts().doubleValue());
							R26Cell3.setCellStyle(numberStyle);
						} else {
							R26Cell3.setCellValue("");
							R26Cell3.setCellStyle(textStyle);
						}
						Cell R26Cell4 = row.createCell(4);
						if (record.getR26OtherInstallmentLoans() != null) {
							R26Cell4.setCellValue(record.getR26OtherInstallmentLoans().doubleValue());
							R26Cell4.setCellStyle(numberStyle);
						} else {
							R26Cell4.setCellValue("");
							R26Cell4.setCellStyle(textStyle);
						}

						// --- R27 (Row Index 26) ---
						row = sheet.getRow(26);
						Cell R27Cell1 = row.createCell(1);
						if (record1.getR27FactoringDebtors() != null) {
							R27Cell1.setCellValue(record1.getR27FactoringDebtors().doubleValue());
							R27Cell1.setCellStyle(numberStyle);
						} else {
							R27Cell1.setCellValue("");
							R27Cell1.setCellStyle(textStyle);
						}
						Cell R27Cell2 = row.createCell(2);
						if (record1.getR27Leasing() != null) {
							R27Cell2.setCellValue(record1.getR27Leasing().doubleValue());
							R27Cell2.setCellStyle(numberStyle);
						} else {
							R27Cell2.setCellValue("");
							R27Cell2.setCellStyle(textStyle);
						}
						Cell R27Cell3 = row.createCell(3);
						if (record.getR27Overdrafts() != null) {
							R27Cell3.setCellValue(record.getR27Overdrafts().doubleValue());
							R27Cell3.setCellStyle(numberStyle);
						} else {
							R27Cell3.setCellValue("");
							R27Cell3.setCellStyle(textStyle);
						}
						Cell R27Cell4 = row.createCell(4);
						if (record.getR27OtherInstallmentLoans() != null) {
							R27Cell4.setCellValue(record.getR27OtherInstallmentLoans().doubleValue());
							R27Cell4.setCellStyle(numberStyle);
						} else {
							R27Cell4.setCellValue("");
							R27Cell4.setCellStyle(textStyle);
						}

						// --- R28 (Row Index 27) ---
						row = sheet.getRow(27);
						Cell R28Cell1 = row.createCell(1);
						if (record1.getR28FactoringDebtors() != null) {
							R28Cell1.setCellValue(record1.getR28FactoringDebtors().doubleValue());
							R28Cell1.setCellStyle(numberStyle);
						} else {
							R28Cell1.setCellValue("");
							R28Cell1.setCellStyle(textStyle);
						}
						Cell R28Cell2 = row.createCell(2);
						if (record1.getR28Leasing() != null) {
							R28Cell2.setCellValue(record1.getR28Leasing().doubleValue());
							R28Cell2.setCellStyle(numberStyle);
						} else {
							R28Cell2.setCellValue("");
							R28Cell2.setCellStyle(textStyle);
						}
						Cell R28Cell3 = row.createCell(3);
						if (record.getR28Overdrafts() != null) {
							R28Cell3.setCellValue(record.getR28Overdrafts().doubleValue());
							R28Cell3.setCellStyle(numberStyle);
						} else {
							R28Cell3.setCellValue("");
							R28Cell3.setCellStyle(textStyle);
						}
						Cell R28Cell4 = row.createCell(4);
						if (record.getR28OtherInstallmentLoans() != null) {
							R28Cell4.setCellValue(record.getR28OtherInstallmentLoans().doubleValue());
							R28Cell4.setCellStyle(numberStyle);
						} else {
							R28Cell4.setCellValue("");
							R28Cell4.setCellStyle(textStyle);
						}

						// --- R30 (Row Index 29) ---
						row = sheet.getRow(29);
						Cell R30Cell1 = row.createCell(1);
						if (record1.getR30FactoringDebtors() != null) {
							R30Cell1.setCellValue(record1.getR30FactoringDebtors().doubleValue());
							R30Cell1.setCellStyle(numberStyle);
						} else {
							R30Cell1.setCellValue("");
							R30Cell1.setCellStyle(textStyle);
						}
						Cell R30Cell2 = row.createCell(2);
						if (record1.getR30Leasing() != null) {
							R30Cell2.setCellValue(record1.getR30Leasing().doubleValue());
							R30Cell2.setCellStyle(numberStyle);
						} else {
							R30Cell2.setCellValue("");
							R30Cell2.setCellStyle(textStyle);
						}
						Cell R30Cell3 = row.createCell(3);
						if (record.getR30Overdrafts() != null) {
							R30Cell3.setCellValue(record.getR30Overdrafts().doubleValue());
							R30Cell3.setCellStyle(numberStyle);
						} else {
							R30Cell3.setCellValue("");
							R30Cell3.setCellStyle(textStyle);
						}
						Cell R30Cell4 = row.createCell(4);
						if (record.getR30OtherInstallmentLoans() != null) {
							R30Cell4.setCellValue(record.getR30OtherInstallmentLoans().doubleValue());
							R30Cell4.setCellStyle(numberStyle);
						} else {
							R30Cell4.setCellValue("");
							R30Cell4.setCellStyle(textStyle);
						}

						// --- R31 (Row Index 30) ---
						row = sheet.getRow(30);
						Cell R31Cell1 = row.createCell(1);
						if (record1.getR31FactoringDebtors() != null) {
							R31Cell1.setCellValue(record1.getR31FactoringDebtors().doubleValue());
							R31Cell1.setCellStyle(numberStyle);
						} else {
							R31Cell1.setCellValue("");
							R31Cell1.setCellStyle(textStyle);
						}
						Cell R31Cell2 = row.createCell(2);
						if (record1.getR31Leasing() != null) {
							R31Cell2.setCellValue(record1.getR31Leasing().doubleValue());
							R31Cell2.setCellStyle(numberStyle);
						} else {
							R31Cell2.setCellValue("");
							R31Cell2.setCellStyle(textStyle);
						}
						Cell R31Cell3 = row.createCell(3);
						if (record.getR31Overdrafts() != null) {
							R31Cell3.setCellValue(record.getR31Overdrafts().doubleValue());
							R31Cell3.setCellStyle(numberStyle);
						} else {
							R31Cell3.setCellValue("");
							R31Cell3.setCellStyle(textStyle);
						}
						Cell R31Cell4 = row.createCell(4);
						if (record.getR31OtherInstallmentLoans() != null) {
							R31Cell4.setCellValue(record.getR31OtherInstallmentLoans().doubleValue());
							R31Cell4.setCellStyle(numberStyle);
						} else {
							R31Cell4.setCellValue("");
							R31Cell4.setCellStyle(textStyle);
						}

						// --- R32 (Row Index 31) ---
						row = sheet.getRow(31);
						Cell R32Cell1 = row.createCell(1);
						if (record1.getR32FactoringDebtors() != null) {
							R32Cell1.setCellValue(record1.getR32FactoringDebtors().doubleValue());
							R32Cell1.setCellStyle(numberStyle);
						} else {
							R32Cell1.setCellValue("");
							R32Cell1.setCellStyle(textStyle);
						}
						Cell R32Cell2 = row.createCell(2);
						if (record1.getR32Leasing() != null) {
							R32Cell2.setCellValue(record1.getR32Leasing().doubleValue());
							R32Cell2.setCellStyle(numberStyle);
						} else {
							R32Cell2.setCellValue("");
							R32Cell2.setCellStyle(textStyle);
						}
						Cell R32Cell3 = row.createCell(3);
						if (record.getR32Overdrafts() != null) {
							R32Cell3.setCellValue(record.getR32Overdrafts().doubleValue());
							R32Cell3.setCellStyle(numberStyle);
						} else {
							R32Cell3.setCellValue("");
							R32Cell3.setCellStyle(textStyle);
						}
						Cell R32Cell4 = row.createCell(4);
						if (record.getR32OtherInstallmentLoans() != null) {
							R32Cell4.setCellValue(record.getR32OtherInstallmentLoans().doubleValue());
							R32Cell4.setCellStyle(numberStyle);
						} else {
							R32Cell4.setCellValue("");
							R32Cell4.setCellStyle(textStyle);
						}

						// --- R33 (Row Index 32) ---
						row = sheet.getRow(32);
						Cell R33Cell1 = row.createCell(1);
						if (record1.getR33FactoringDebtors() != null) {
							R33Cell1.setCellValue(record1.getR33FactoringDebtors().doubleValue());
							R33Cell1.setCellStyle(numberStyle);
						} else {
							R33Cell1.setCellValue("");
							R33Cell1.setCellStyle(textStyle);
						}
						Cell R33Cell2 = row.createCell(2);
						if (record1.getR33Leasing() != null) {
							R33Cell2.setCellValue(record1.getR33Leasing().doubleValue());
							R33Cell2.setCellStyle(numberStyle);
						} else {
							R33Cell2.setCellValue("");
							R33Cell2.setCellStyle(textStyle);
						}
						Cell R33Cell3 = row.createCell(3);
						if (record.getR33Overdrafts() != null) {
							R33Cell3.setCellValue(record.getR33Overdrafts().doubleValue());
							R33Cell3.setCellStyle(numberStyle);
						} else {
							R33Cell3.setCellValue("");
							R33Cell3.setCellStyle(textStyle);
						}
						Cell R33Cell4 = row.createCell(4);
						if (record.getR33OtherInstallmentLoans() != null) {
							R33Cell4.setCellValue(record.getR33OtherInstallmentLoans().doubleValue());
							R33Cell4.setCellStyle(numberStyle);
						} else {
							R33Cell4.setCellValue("");
							R33Cell4.setCellStyle(textStyle);
						}

						// --- R34 (Row Index 33) ---
						row = sheet.getRow(33);
						Cell R34Cell1 = row.createCell(1);
						if (record1.getR34FactoringDebtors() != null) {
							R34Cell1.setCellValue(record1.getR34FactoringDebtors().doubleValue());
							R34Cell1.setCellStyle(numberStyle);
						} else {
							R34Cell1.setCellValue("");
							R34Cell1.setCellStyle(textStyle);
						}
						Cell R34Cell2 = row.createCell(2);
						if (record1.getR34Leasing() != null) {
							R34Cell2.setCellValue(record1.getR34Leasing().doubleValue());
							R34Cell2.setCellStyle(numberStyle);
						} else {
							R34Cell2.setCellValue("");
							R34Cell2.setCellStyle(textStyle);
						}
						Cell R34Cell3 = row.createCell(3);
						if (record.getR34Overdrafts() != null) {
							R34Cell3.setCellValue(record.getR34Overdrafts().doubleValue());
							R34Cell3.setCellStyle(numberStyle);
						} else {
							R34Cell3.setCellValue("");
							R34Cell3.setCellStyle(textStyle);
						}
						Cell R34Cell4 = row.createCell(4);
						if (record.getR34OtherInstallmentLoans() != null) {
							R34Cell4.setCellValue(record.getR34OtherInstallmentLoans().doubleValue());
							R34Cell4.setCellStyle(numberStyle);
						} else {
							R34Cell4.setCellValue("");
							R34Cell4.setCellStyle(textStyle);
						}
						Cell R34Cell5 = row.createCell(5);
						if (record.getR34Total() != null) {
							R34Cell5.setCellValue(record.getR34Total().doubleValue());
							R34Cell5.setCellStyle(numberStyle);
						} else {
							R34Cell5.setCellValue("");
							R34Cell5.setCellStyle(textStyle);
						}
						// --- R35 (Row Index 34) ---
						row = sheet.getRow(34);
						Cell R35Cell1 = row.createCell(1);
						if (record1.getR35FactoringDebtors() != null) {
							R35Cell1.setCellValue(record1.getR35FactoringDebtors().doubleValue());
							R35Cell1.setCellStyle(numberStyle);
						} else {
							R35Cell1.setCellValue("");
							R35Cell1.setCellStyle(textStyle);
						}
						Cell R35Cell2 = row.createCell(2);
						if (record1.getR35Leasing() != null) {
							R35Cell2.setCellValue(record1.getR35Leasing().doubleValue());
							R35Cell2.setCellStyle(numberStyle);
						} else {
							R35Cell2.setCellValue("");
							R35Cell2.setCellStyle(textStyle);
						}
						Cell R35Cell3 = row.createCell(3);
						if (record.getR35Overdrafts() != null) {
							R35Cell3.setCellValue(record.getR35Overdrafts().doubleValue());
							R35Cell3.setCellStyle(numberStyle);
						} else {
							R35Cell3.setCellValue("");
							R35Cell3.setCellStyle(textStyle);
						}
						Cell R35Cell4 = row.createCell(4);
						if (record.getR35OtherInstallmentLoans() != null) {
							R35Cell4.setCellValue(record.getR35OtherInstallmentLoans().doubleValue());
							R35Cell4.setCellStyle(numberStyle);
						} else {
							R35Cell4.setCellValue("");
							R35Cell4.setCellStyle(textStyle);
						}

						// --- R36 (Row Index 35) ---
						row = sheet.getRow(35);
						Cell R36Cell1 = row.createCell(1);
						if (record1.getR36FactoringDebtors() != null) {
							R36Cell1.setCellValue(record1.getR36FactoringDebtors().doubleValue());
							R36Cell1.setCellStyle(numberStyle);
						} else {
							R36Cell1.setCellValue("");
							R36Cell1.setCellStyle(textStyle);
						}
						Cell R36Cell2 = row.createCell(2);
						if (record1.getR36Leasing() != null) {
							R36Cell2.setCellValue(record1.getR36Leasing().doubleValue());
							R36Cell2.setCellStyle(numberStyle);
						} else {
							R36Cell2.setCellValue("");
							R36Cell2.setCellStyle(textStyle);
						}
						Cell R36Cell3 = row.createCell(3);
						if (record.getR36Overdrafts() != null) {
							R36Cell3.setCellValue(record.getR36Overdrafts().doubleValue());
							R36Cell3.setCellStyle(numberStyle);
						} else {
							R36Cell3.setCellValue("");
							R36Cell3.setCellStyle(textStyle);
						}
						Cell R36Cell4 = row.createCell(4);
						if (record.getR36OtherInstallmentLoans() != null) {
							R36Cell4.setCellValue(record.getR36OtherInstallmentLoans().doubleValue());
							R36Cell4.setCellStyle(numberStyle);
						} else {
							R36Cell4.setCellValue("");
							R36Cell4.setCellStyle(textStyle);
						}

						// --- R37 (Row Index 36) ---
						row = sheet.getRow(36);
						Cell R37Cell1 = row.createCell(1);
						if (record1.getR37FactoringDebtors() != null) {
							R37Cell1.setCellValue(record1.getR37FactoringDebtors().doubleValue());
							R37Cell1.setCellStyle(numberStyle);
						} else {
							R37Cell1.setCellValue("");
							R37Cell1.setCellStyle(textStyle);
						}
						Cell R37Cell2 = row.createCell(2);
						if (record1.getR37Leasing() != null) {
							R37Cell2.setCellValue(record1.getR37Leasing().doubleValue());
							R37Cell2.setCellStyle(numberStyle);
						} else {
							R37Cell2.setCellValue("");
							R37Cell2.setCellStyle(textStyle);
						}
						Cell R37Cell3 = row.createCell(3);
						if (record.getR37Overdrafts() != null) {
							R37Cell3.setCellValue(record.getR37Overdrafts().doubleValue());
							R37Cell3.setCellStyle(numberStyle);
						} else {
							R37Cell3.setCellValue("");
							R37Cell3.setCellStyle(textStyle);
						}
						Cell R37Cell4 = row.createCell(4);
						if (record.getR37OtherInstallmentLoans() != null) {
							R37Cell4.setCellValue(record.getR37OtherInstallmentLoans().doubleValue());
							R37Cell4.setCellStyle(numberStyle);
						} else {
							R37Cell4.setCellValue("");
							R37Cell4.setCellStyle(textStyle);
						}
						// --- R39 (Row Index 38) ---
						row = sheet.getRow(38);
						Cell R39Cell1 = row.createCell(1);
						if (record1.getR39FactoringDebtors() != null) {
							R39Cell1.setCellValue(record1.getR39FactoringDebtors().doubleValue());
							R39Cell1.setCellStyle(numberStyle);
						} else {
							R39Cell1.setCellValue("");
							R39Cell1.setCellStyle(textStyle);
						}
						Cell R39Cell2 = row.createCell(2);
						if (record1.getR39Leasing() != null) {
							R39Cell2.setCellValue(record1.getR39Leasing().doubleValue());
							R39Cell2.setCellStyle(numberStyle);
						} else {
							R39Cell2.setCellValue("");
							R39Cell2.setCellStyle(textStyle);
						}
						Cell R39Cell3 = row.createCell(3);
						if (record.getR39Overdrafts() != null) {
							R39Cell3.setCellValue(record.getR39Overdrafts().doubleValue());
							R39Cell3.setCellStyle(numberStyle);
						} else {
							R39Cell3.setCellValue("");
							R39Cell3.setCellStyle(textStyle);
						}
						Cell R39Cell4 = row.createCell(4);
						if (record.getR39OtherInstallmentLoans() != null) {
							R39Cell4.setCellValue(record.getR39OtherInstallmentLoans().doubleValue());
							R39Cell4.setCellStyle(numberStyle);
						} else {
							R39Cell4.setCellValue("");
							R39Cell4.setCellStyle(textStyle);
						}

						// --- R40 (Row Index 39) ---
						row = sheet.getRow(39);
						Cell R40Cell1 = row.createCell(1);
						if (record1.getR40FactoringDebtors() != null) {
							R40Cell1.setCellValue(record1.getR40FactoringDebtors().doubleValue());
							R40Cell1.setCellStyle(numberStyle);
						} else {
							R40Cell1.setCellValue("");
							R40Cell1.setCellStyle(textStyle);
						}
						Cell R40Cell2 = row.createCell(2);
						if (record1.getR40Leasing() != null) {
							R40Cell2.setCellValue(record1.getR40Leasing().doubleValue());
							R40Cell2.setCellStyle(numberStyle);
						} else {
							R40Cell2.setCellValue("");
							R40Cell2.setCellStyle(textStyle);
						}
						Cell R40Cell3 = row.createCell(3);
						if (record.getR40Overdrafts() != null) {
							R40Cell3.setCellValue(record.getR40Overdrafts().doubleValue());
							R40Cell3.setCellStyle(numberStyle);
						} else {
							R40Cell3.setCellValue("");
							R40Cell3.setCellStyle(textStyle);
						}
						Cell R40Cell4 = row.createCell(4);
						if (record.getR40OtherInstallmentLoans() != null) {
							R40Cell4.setCellValue(record.getR40OtherInstallmentLoans().doubleValue());
							R40Cell4.setCellStyle(numberStyle);
						} else {
							R40Cell4.setCellValue("");
							R40Cell4.setCellStyle(textStyle);
						}
						// --- R42 (Row Index 41) ---
						row = sheet.getRow(41);
						Cell R42Cell1 = row.createCell(1);
						if (record1.getR42FactoringDebtors() != null) {
							R42Cell1.setCellValue(record1.getR42FactoringDebtors().doubleValue());
							R42Cell1.setCellStyle(numberStyle);
						} else {
							R42Cell1.setCellValue("");
							R42Cell1.setCellStyle(textStyle);
						}
						Cell R42Cell2 = row.createCell(2);
						if (record1.getR42Leasing() != null) {
							R42Cell2.setCellValue(record1.getR42Leasing().doubleValue());
							R42Cell2.setCellStyle(numberStyle);
						} else {
							R42Cell2.setCellValue("");
							R42Cell2.setCellStyle(textStyle);
						}
						Cell R42Cell3 = row.createCell(3);
						if (record.getR42Overdrafts() != null) {
							R42Cell3.setCellValue(record.getR42Overdrafts().doubleValue());
							R42Cell3.setCellStyle(numberStyle);
						} else {
							R42Cell3.setCellValue("");
							R42Cell3.setCellStyle(textStyle);
						}
						Cell R42Cell4 = row.createCell(4);
						if (record.getR42OtherInstallmentLoans() != null) {
							R42Cell4.setCellValue(record.getR42OtherInstallmentLoans().doubleValue());
							R42Cell4.setCellStyle(numberStyle);
						} else {
							R42Cell4.setCellValue("");
							R42Cell4.setCellStyle(textStyle);
						}

						// --- R43 (Row Index 42) ---
						row = sheet.getRow(42);
						Cell R43Cell1 = row.createCell(1);
						if (record1.getR43FactoringDebtors() != null) {
							R43Cell1.setCellValue(record1.getR43FactoringDebtors().doubleValue());
							R43Cell1.setCellStyle(numberStyle);
						} else {
							R43Cell1.setCellValue("");
							R43Cell1.setCellStyle(textStyle);
						}
						Cell R43Cell2 = row.createCell(2);
						if (record1.getR43Leasing() != null) {
							R43Cell2.setCellValue(record1.getR43Leasing().doubleValue());
							R43Cell2.setCellStyle(numberStyle);
						} else {
							R43Cell2.setCellValue("");
							R43Cell2.setCellStyle(textStyle);
						}
						Cell R43Cell3 = row.createCell(3);
						if (record.getR43Overdrafts() != null) {
							R43Cell3.setCellValue(record.getR43Overdrafts().doubleValue());
							R43Cell3.setCellStyle(numberStyle);
						} else {
							R43Cell3.setCellValue("");
							R43Cell3.setCellStyle(textStyle);
						}
						Cell R43Cell4 = row.createCell(4);
						if (record.getR43OtherInstallmentLoans() != null) {
							R43Cell4.setCellValue(record.getR43OtherInstallmentLoans().doubleValue());
							R43Cell4.setCellStyle(numberStyle);
						} else {
							R43Cell4.setCellValue("");
							R43Cell4.setCellStyle(textStyle);
						}
						// --- R45 (Row Index 44) ---
						row = sheet.getRow(44);
						Cell R45Cell1 = row.createCell(1);
						if (record1.getR45FactoringDebtors() != null) {
							R45Cell1.setCellValue(record1.getR45FactoringDebtors().doubleValue());
							R45Cell1.setCellStyle(numberStyle);
						} else {
							R45Cell1.setCellValue("");
							R45Cell1.setCellStyle(textStyle);
						}
						Cell R45Cell2 = row.createCell(2);
						if (record1.getR45Leasing() != null) {
							R45Cell2.setCellValue(record1.getR45Leasing().doubleValue());
							R45Cell2.setCellStyle(numberStyle);
						} else {
							R45Cell2.setCellValue("");
							R45Cell2.setCellStyle(textStyle);
						}
						Cell R45Cell3 = row.createCell(3);
						if (record.getR45Overdrafts() != null) {
							R45Cell3.setCellValue(record.getR45Overdrafts().doubleValue());
							R45Cell3.setCellStyle(numberStyle);
						} else {
							R45Cell3.setCellValue("");
							R45Cell3.setCellStyle(textStyle);
						}
						Cell R45Cell4 = row.createCell(4);
						if (record.getR45OtherInstallmentLoans() != null) {
							R45Cell4.setCellValue(record.getR45OtherInstallmentLoans().doubleValue());
							R45Cell4.setCellStyle(numberStyle);
						} else {
							R45Cell4.setCellValue("");
							R45Cell4.setCellStyle(textStyle);
						}

						// --- R46 (Row Index 45) ---
						row = sheet.getRow(45);
						Cell R46Cell1 = row.createCell(1);
						if (record1.getR46FactoringDebtors() != null) {
							R46Cell1.setCellValue(record1.getR46FactoringDebtors().doubleValue());
							R46Cell1.setCellStyle(numberStyle);
						} else {
							R46Cell1.setCellValue("");
							R46Cell1.setCellStyle(textStyle);
						}
						Cell R46Cell2 = row.createCell(2);
						if (record1.getR46Leasing() != null) {
							R46Cell2.setCellValue(record1.getR46Leasing().doubleValue());
							R46Cell2.setCellStyle(numberStyle);
						} else {
							R46Cell2.setCellValue("");
							R46Cell2.setCellStyle(textStyle);
						}
						Cell R46Cell3 = row.createCell(3);
						if (record.getR46Overdrafts() != null) {
							R46Cell3.setCellValue(record.getR46Overdrafts().doubleValue());
							R46Cell3.setCellStyle(numberStyle);
						} else {
							R46Cell3.setCellValue("");
							R46Cell3.setCellStyle(textStyle);
						}
						Cell R46Cell4 = row.createCell(4);
						if (record.getR46OtherInstallmentLoans() != null) {
							R46Cell4.setCellValue(record.getR46OtherInstallmentLoans().doubleValue());
							R46Cell4.setCellStyle(numberStyle);
						} else {
							R46Cell4.setCellValue("");
							R46Cell4.setCellStyle(textStyle);
						}

						// --- R47 (Row Index 46) ---
						row = sheet.getRow(46);
						Cell R48Cell1 = row.createCell(1);
						if (record1.getR48FactoringDebtors() != null) {
							R48Cell1.setCellValue(record1.getR48FactoringDebtors().doubleValue());
							R48Cell1.setCellStyle(numberStyle);
						} else {
							R48Cell1.setCellValue("");
							R48Cell1.setCellStyle(textStyle);
						}
						Cell R48Cell2 = row.createCell(2);
						if (record1.getR48Leasing() != null) {
							R48Cell2.setCellValue(record1.getR48Leasing().doubleValue());
							R48Cell2.setCellStyle(numberStyle);
						} else {
							R48Cell2.setCellValue("");
							R48Cell2.setCellStyle(textStyle);
						}
						Cell R48Cell3 = row.createCell(3);
						if (record.getR48Overdrafts() != null) {
							R48Cell3.setCellValue(record.getR48Overdrafts().doubleValue());
							R48Cell3.setCellStyle(numberStyle);
						} else {
							R48Cell3.setCellValue("");
							R48Cell3.setCellStyle(textStyle);
						}
						Cell R48Cell4 = row.createCell(4);
						if (record.getR48OtherInstallmentLoans() != null) {
							R48Cell4.setCellValue(record.getR48OtherInstallmentLoans().doubleValue());
							R48Cell4.setCellStyle(numberStyle);
						} else {
							R48Cell4.setCellValue("");
							R48Cell4.setCellStyle(textStyle);
						}

						// --- R50 (Row Index 48) ---
						row = sheet.getRow(48);
						Cell R50Cell1 = row.createCell(1);
						if (record1.getR50FactoringDebtors() != null) {
							R50Cell1.setCellValue(record1.getR50FactoringDebtors().doubleValue());
							R50Cell1.setCellStyle(numberStyle);
						} else {
							R50Cell1.setCellValue("");
							R50Cell1.setCellStyle(textStyle);
						}
						Cell R50Cell2 = row.createCell(2);
						if (record1.getR50Leasing() != null) {
							R50Cell2.setCellValue(record1.getR50Leasing().doubleValue());
							R50Cell2.setCellStyle(numberStyle);
						} else {
							R50Cell2.setCellValue("");
							R50Cell2.setCellStyle(textStyle);
						}
						Cell R50Cell3 = row.createCell(3);
						if (record.getR50Overdrafts() != null) {
							R50Cell3.setCellValue(record.getR50Overdrafts().doubleValue());
							R50Cell3.setCellStyle(numberStyle);
						} else {
							R50Cell3.setCellValue("");
							R50Cell3.setCellStyle(textStyle);
						}
						Cell R50Cell4 = row.createCell(4);
						if (record.getR50OtherInstallmentLoans() != null) {
							R50Cell4.setCellValue(record.getR50OtherInstallmentLoans().doubleValue());
							R50Cell4.setCellStyle(numberStyle);
						} else {
							R50Cell4.setCellValue("");
							R50Cell4.setCellStyle(textStyle);
						}

						// --- R51 (Row Index 49) ---
						row = sheet.getRow(49);
						Cell R51Cell1 = row.createCell(1);
						if (record1.getR51FactoringDebtors() != null) {
							R51Cell1.setCellValue(record1.getR51FactoringDebtors().doubleValue());
							R51Cell1.setCellStyle(numberStyle);
						} else {
							R51Cell1.setCellValue("");
							R51Cell1.setCellStyle(textStyle);
						}
						Cell R51Cell2 = row.createCell(2);
						if (record1.getR51Leasing() != null) {
							R51Cell2.setCellValue(record1.getR51Leasing().doubleValue());
							R51Cell2.setCellStyle(numberStyle);
						} else {
							R51Cell2.setCellValue("");
							R51Cell2.setCellStyle(textStyle);
						}
						Cell R51Cell3 = row.createCell(3);
						if (record.getR51Overdrafts() != null) {
							R51Cell3.setCellValue(record.getR51Overdrafts().doubleValue());
							R51Cell3.setCellStyle(numberStyle);
						} else {
							R51Cell3.setCellValue("");
							R51Cell3.setCellStyle(textStyle);
						}
						Cell R51Cell4 = row.createCell(4);
						if (record.getR51OtherInstallmentLoans() != null) {
							R51Cell4.setCellValue(record.getR51OtherInstallmentLoans().doubleValue());
							R51Cell4.setCellStyle(numberStyle);
						} else {
							R51Cell4.setCellValue("");
							R51Cell4.setCellStyle(textStyle);
						}

						// --- R52 (Row Index 50) ---
						row = sheet.getRow(50);
						Cell R52Cell1 = row.createCell(1);
						if (record1.getR52FactoringDebtors() != null) {
							R52Cell1.setCellValue(record1.getR52FactoringDebtors().doubleValue());
							R52Cell1.setCellStyle(numberStyle);
						} else {
							R52Cell1.setCellValue("");
							R52Cell1.setCellStyle(textStyle);
						}
						Cell R52Cell2 = row.createCell(2);
						if (record1.getR52Leasing() != null) {
							R52Cell2.setCellValue(record1.getR52Leasing().doubleValue());
							R52Cell2.setCellStyle(numberStyle);
						} else {
							R52Cell2.setCellValue("");
							R52Cell2.setCellStyle(textStyle);
						}
						Cell R52Cell3 = row.createCell(3);
						if (record.getR52Overdrafts() != null) {
							R52Cell3.setCellValue(record.getR52Overdrafts().doubleValue());
							R52Cell3.setCellStyle(numberStyle);
						} else {
							R52Cell3.setCellValue("");
							R52Cell3.setCellStyle(textStyle);
						}
						Cell R52Cell4 = row.createCell(4);
						if (record.getR52OtherInstallmentLoans() != null) {
							R52Cell4.setCellValue(record.getR52OtherInstallmentLoans().doubleValue());
							R52Cell4.setCellStyle(numberStyle);
						} else {
							R52Cell4.setCellValue("");
							R52Cell4.setCellStyle(textStyle);
						}
						Cell R52Cell5 = row.createCell(5);
						if (record.getR52Total() != null) {
							R52Cell5.setCellValue(record.getR52Total().doubleValue());
							R52Cell5.setCellStyle(numberStyle);
						} else {
							R52Cell5.setCellValue("");
							R52Cell5.setCellStyle(textStyle);
						}
						// --- R54 (Row Index 56) ---
						row = sheet.getRow(52);
						Cell R54Cell1 = row.createCell(1);
						if (record1.getR54FactoringDebtors() != null) {
							R54Cell1.setCellValue(record1.getR54FactoringDebtors().doubleValue());
							R54Cell1.setCellStyle(numberStyle);
						} else {
							R54Cell1.setCellValue("");
							R54Cell1.setCellStyle(textStyle);
						}
						Cell R54Cell2 = row.createCell(2);
						if (record1.getR54Leasing() != null) {
							R54Cell2.setCellValue(record1.getR54Leasing().doubleValue());
							R54Cell2.setCellStyle(numberStyle);
						} else {
							R54Cell2.setCellValue("");
							R54Cell2.setCellStyle(textStyle);
						}
						Cell R54Cell3 = row.createCell(3);
						if (record.getR54Overdrafts() != null) {
							R54Cell3.setCellValue(record.getR54Overdrafts().doubleValue());
							R54Cell3.setCellStyle(numberStyle);
						} else {
							R54Cell3.setCellValue("");
							R54Cell3.setCellStyle(textStyle);
						}
						Cell R54Cell4 = row.createCell(4);
						if (record.getR54OtherInstallmentLoans() != null) {
							R54Cell4.setCellValue(record.getR54OtherInstallmentLoans().doubleValue());
							R54Cell4.setCellStyle(numberStyle);
						} else {
							R54Cell4.setCellValue("");
							R54Cell4.setCellStyle(textStyle);
						}

						// --- R55 (Row Index 53) ---
						row = sheet.getRow(53);
						Cell R55Cell1 = row.createCell(1);
						if (record1.getR55FactoringDebtors() != null) {
							R55Cell1.setCellValue(record1.getR55FactoringDebtors().doubleValue());
							R55Cell1.setCellStyle(numberStyle);
						} else {
							R55Cell1.setCellValue("");
							R55Cell1.setCellStyle(textStyle);
						}
						Cell R55Cell2 = row.createCell(2);
						if (record1.getR55Leasing() != null) {
							R55Cell2.setCellValue(record1.getR55Leasing().doubleValue());
							R55Cell2.setCellStyle(numberStyle);
						} else {
							R55Cell2.setCellValue("");
							R55Cell2.setCellStyle(textStyle);
						}
						Cell R55Cell3 = row.createCell(3);
						if (record.getR55Overdrafts() != null) {
							R55Cell3.setCellValue(record.getR55Overdrafts().doubleValue());
							R55Cell3.setCellStyle(numberStyle);
						} else {
							R55Cell3.setCellValue("");
							R55Cell3.setCellStyle(textStyle);
						}
						Cell R55Cell4 = row.createCell(4);
						if (record.getR55OtherInstallmentLoans() != null) {
							R55Cell4.setCellValue(record.getR55OtherInstallmentLoans().doubleValue());
							R55Cell4.setCellStyle(numberStyle);
						} else {
							R55Cell4.setCellValue("");
							R55Cell4.setCellStyle(textStyle);
						}

						// --- R56 (Row Index 54) ---
						row = sheet.getRow(54);
						Cell R56Cell1 = row.createCell(1);
						if (record1.getR56FactoringDebtors() != null) {
							R56Cell1.setCellValue(record1.getR56FactoringDebtors().doubleValue());
							R56Cell1.setCellStyle(numberStyle);
						} else {
							R56Cell1.setCellValue("");
							R56Cell1.setCellStyle(textStyle);
						}
						Cell R56Cell2 = row.createCell(2);
						if (record1.getR56Leasing() != null) {
							R56Cell2.setCellValue(record1.getR56Leasing().doubleValue());
							R56Cell2.setCellStyle(numberStyle);
						} else {
							R56Cell2.setCellValue("");
							R56Cell2.setCellStyle(textStyle);
						}
						Cell R56Cell3 = row.createCell(3);
						if (record.getR56Overdrafts() != null) {
							R56Cell3.setCellValue(record.getR56Overdrafts().doubleValue());
							R56Cell3.setCellStyle(numberStyle);
						} else {
							R56Cell3.setCellValue("");
							R56Cell3.setCellStyle(textStyle);
						}
						Cell R56Cell4 = row.createCell(4);
						if (record.getR56OtherInstallmentLoans() != null) {
							R56Cell4.setCellValue(record.getR56OtherInstallmentLoans().doubleValue());
							R56Cell4.setCellStyle(numberStyle);
						} else {
							R56Cell4.setCellValue("");
							R56Cell4.setCellStyle(textStyle);
						}
						// --- R58 (Row Index 56) ---
						row = sheet.getRow(56);
						Cell R58Cell1 = row.createCell(1);
						if (record1.getR58FactoringDebtors() != null) {
							R58Cell1.setCellValue(record1.getR58FactoringDebtors().doubleValue());
							R58Cell1.setCellStyle(numberStyle);
						} else {
							R58Cell1.setCellValue("");
							R58Cell1.setCellStyle(textStyle);
						}
						Cell R58Cell2 = row.createCell(2);
						if (record1.getR58Leasing() != null) {
							R58Cell2.setCellValue(record1.getR58Leasing().doubleValue());
							R58Cell2.setCellStyle(numberStyle);
						} else {
							R58Cell2.setCellValue("");
							R58Cell2.setCellStyle(textStyle);
						}
						Cell R58Cell3 = row.createCell(3);
						if (record.getR58Overdrafts() != null) {
							R58Cell3.setCellValue(record.getR58Overdrafts().doubleValue());
							R58Cell3.setCellStyle(numberStyle);
						} else {
							R58Cell3.setCellValue("");
							R58Cell3.setCellStyle(textStyle);
						}
						Cell R58Cell4 = row.createCell(4);
						if (record.getR58OtherInstallmentLoans() != null) {
							R58Cell4.setCellValue(record.getR58OtherInstallmentLoans().doubleValue());
							R58Cell4.setCellStyle(numberStyle);
						} else {
							R58Cell4.setCellValue("");
							R58Cell4.setCellStyle(textStyle);
						}
						Cell R58Cell5 = row.createCell(5);
						if (record.getR58Total() != null) {
							R58Cell5.setCellValue(record.getR58Total().doubleValue());
							R58Cell5.setCellStyle(numberStyle);
						} else {
							R58Cell5.setCellValue("");
							R58Cell5.setCellStyle(textStyle);
						}

						// --- R59 (Row Index 57) ---
						row = sheet.getRow(57);
						Cell R59Cell1 = row.createCell(1);
						if (record1.getR59FactoringDebtors() != null) {
							R59Cell1.setCellValue(record1.getR59FactoringDebtors().doubleValue());
							R59Cell1.setCellStyle(numberStyle);
						} else {
							R59Cell1.setCellValue("");
							R59Cell1.setCellStyle(textStyle);
						}
						Cell R59Cell2 = row.createCell(2);
						if (record1.getR59Leasing() != null) {
							R59Cell2.setCellValue(record1.getR59Leasing().doubleValue());
							R59Cell2.setCellStyle(numberStyle);
						} else {
							R59Cell2.setCellValue("");
							R59Cell2.setCellStyle(textStyle);
						}
						Cell R59Cell3 = row.createCell(3);
						if (record.getR59Overdrafts() != null) {
							R59Cell3.setCellValue(record.getR59Overdrafts().doubleValue());
							R59Cell3.setCellStyle(numberStyle);
						} else {
							R59Cell3.setCellValue("");
							R59Cell3.setCellStyle(textStyle);
						}
						Cell R59Cell4 = row.createCell(4);
						if (record.getR59OtherInstallmentLoans() != null) {
							R59Cell4.setCellValue(record.getR59OtherInstallmentLoans().doubleValue());
							R59Cell4.setCellStyle(numberStyle);
						} else {
							R59Cell4.setCellValue("");
							R59Cell4.setCellStyle(textStyle);
						}

						// --- R60 (Row Index 58) ---
						row = sheet.getRow(58);
						Cell R60Cell1 = row.createCell(1);
						if (record1.getR60FactoringDebtors() != null) {
							R60Cell1.setCellValue(record1.getR60FactoringDebtors().doubleValue());
							R60Cell1.setCellStyle(numberStyle);
						} else {
							R60Cell1.setCellValue("");
							R60Cell1.setCellStyle(textStyle);
						}
						Cell R60Cell2 = row.createCell(2);
						if (record1.getR60Leasing() != null) {
							R60Cell2.setCellValue(record1.getR60Leasing().doubleValue());
							R60Cell2.setCellStyle(numberStyle);
						} else {
							R60Cell2.setCellValue("");
							R60Cell2.setCellStyle(textStyle);
						}
						Cell R60Cell3 = row.createCell(3);
						if (record.getR60Overdrafts() != null) {
							R60Cell3.setCellValue(record.getR60Overdrafts().doubleValue());
							R60Cell3.setCellStyle(numberStyle);
						} else {
							R60Cell3.setCellValue("");
							R60Cell3.setCellStyle(textStyle);
						}
						Cell R60Cell4 = row.createCell(4);
						if (record.getR60OtherInstallmentLoans() != null) {
							R60Cell4.setCellValue(record.getR60OtherInstallmentLoans().doubleValue());
							R60Cell4.setCellStyle(numberStyle);
						} else {
							R60Cell4.setCellValue("");
							R60Cell4.setCellStyle(textStyle);
						}

						// --- R61 (Row Index 59) ---
						row = sheet.getRow(59);
						Cell R61Cell1 = row.createCell(1);
						if (record1.getR61FactoringDebtors() != null) {
							R61Cell1.setCellValue(record1.getR61FactoringDebtors().doubleValue());
							R61Cell1.setCellStyle(numberStyle);
						} else {
							R61Cell1.setCellValue("");
							R61Cell1.setCellStyle(textStyle);
						}
						Cell R61Cell2 = row.createCell(2);
						if (record1.getR61Leasing() != null) {
							R61Cell2.setCellValue(record1.getR61Leasing().doubleValue());
							R61Cell2.setCellStyle(numberStyle);
						} else {
							R61Cell2.setCellValue("");
							R61Cell2.setCellStyle(textStyle);
						}
						Cell R61Cell3 = row.createCell(3);
						if (record.getR61Overdrafts() != null) {
							R61Cell3.setCellValue(record.getR61Overdrafts().doubleValue());
							R61Cell3.setCellStyle(numberStyle);
						} else {
							R61Cell3.setCellValue("");
							R61Cell3.setCellStyle(textStyle);
						}
						Cell R61Cell4 = row.createCell(4);
						if (record.getR61OtherInstallmentLoans() != null) {
							R61Cell4.setCellValue(record.getR61OtherInstallmentLoans().doubleValue());
							R61Cell4.setCellStyle(numberStyle);
						} else {
							R61Cell4.setCellValue("");
							R61Cell4.setCellStyle(textStyle);
						}

						// --- R62 (Row Index 60) ---
						row = sheet.getRow(60);
						Cell R62Cell1 = row.createCell(1);
						if (record1.getR62FactoringDebtors() != null) {
							R62Cell1.setCellValue(record1.getR62FactoringDebtors().doubleValue());
							R62Cell1.setCellStyle(numberStyle);
						} else {
							R62Cell1.setCellValue("");
							R62Cell1.setCellStyle(textStyle);
						}
						Cell R62Cell2 = row.createCell(2);
						if (record1.getR62Leasing() != null) {
							R62Cell2.setCellValue(record1.getR62Leasing().doubleValue());
							R62Cell2.setCellStyle(numberStyle);
						} else {
							R62Cell2.setCellValue("");
							R62Cell2.setCellStyle(textStyle);
						}
						Cell R62Cell3 = row.createCell(3);
						if (record.getR62Overdrafts() != null) {
							R62Cell3.setCellValue(record.getR62Overdrafts().doubleValue());
							R62Cell3.setCellStyle(numberStyle);
						} else {
							R62Cell3.setCellValue("");
							R62Cell3.setCellStyle(textStyle);
						}
						Cell R62Cell4 = row.createCell(4);
						if (record.getR62OtherInstallmentLoans() != null) {
							R62Cell4.setCellValue(record.getR62OtherInstallmentLoans().doubleValue());
							R62Cell4.setCellStyle(numberStyle);
						} else {
							R62Cell4.setCellValue("");
							R62Cell4.setCellStyle(textStyle);
						}

						// --- R63 (Row Index 61) ---
						row = sheet.getRow(61);
						Cell R63Cell1 = row.createCell(1);
						if (record1.getR63FactoringDebtors() != null) {
							R63Cell1.setCellValue(record1.getR63FactoringDebtors().doubleValue());
							R63Cell1.setCellStyle(numberStyle);
						} else {
							R63Cell1.setCellValue("");
							R63Cell1.setCellStyle(textStyle);
						}
						Cell R63Cell2 = row.createCell(2);
						if (record1.getR63Leasing() != null) {
							R63Cell2.setCellValue(record1.getR63Leasing().doubleValue());
							R63Cell2.setCellStyle(numberStyle);
						} else {
							R63Cell2.setCellValue("");
							R63Cell2.setCellStyle(textStyle);
						}
						Cell R63Cell3 = row.createCell(3);
						if (record.getR63Overdrafts() != null) {
							R63Cell3.setCellValue(record.getR63Overdrafts().doubleValue());
							R63Cell3.setCellStyle(numberStyle);
						} else {
							R63Cell3.setCellValue("");
							R63Cell3.setCellStyle(textStyle);
						}
						Cell R63Cell4 = row.createCell(4);
						if (record.getR63OtherInstallmentLoans() != null) {
							R63Cell4.setCellValue(record.getR63OtherInstallmentLoans().doubleValue());
							R63Cell4.setCellStyle(numberStyle);
						} else {
							R63Cell4.setCellValue("");
							R63Cell4.setCellStyle(textStyle);
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
	public byte[] getExcelM_LA4ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA4EmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_LA4_Archival_Summary_Entity> dataList = M_LA4_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_LA4_Archival_Summary_Entity2> dataList1 = M_LA4_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA4 report. Returning empty result.");
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA4_Archival_Summary_Entity record = dataList.get(i);
					M_LA4_Archival_Summary_Entity2 record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
//NORMAL

					// R12 Col B
					Cell R12Cell1 = row.createCell(1);
					if (record1.getR12FactoringDebtors() != null) {
						R12Cell1.setCellValue(record1.getR12FactoringDebtors().doubleValue());
						R12Cell1.setCellStyle(numberStyle);
					} else {
						R12Cell1.setCellValue("");
						R12Cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12Cell2 = row.createCell(2);
					if (record1.getR12Leasing() != null) {
						R12Cell2.setCellValue(record1.getR12Leasing().doubleValue());
						R12Cell2.setCellStyle(numberStyle);
					} else {
						R12Cell2.setCellValue("");
						R12Cell2.setCellStyle(textStyle);
					}
					// R12 Col D
					Cell R12Cell3 = row.createCell(3);
					if (record.getR12Overdrafts() != null) {
						R12Cell3.setCellValue(record.getR12Overdrafts().doubleValue());
						R12Cell3.setCellStyle(numberStyle);
					} else {
						R12Cell3.setCellValue("");
						R12Cell3.setCellStyle(textStyle);
					}

					// R12 Col E
					Cell R12Cell4 = row.createCell(4);
					if (record.getR12OtherInstallmentLoans() != null) {
						R12Cell4.setCellValue(record.getR12OtherInstallmentLoans().doubleValue());
						R12Cell4.setCellStyle(numberStyle);
					} else {
						R12Cell4.setCellValue("");
						R12Cell4.setCellStyle(textStyle);
					}
					// R13 Col B
					row = sheet.getRow(12);
					// R13 Col B
					Cell R13Cell1 = row.createCell(1);
					if (record1.getR13FactoringDebtors() != null) {
						R13Cell1.setCellValue(record1.getR13FactoringDebtors().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13Cell2 = row.createCell(2);
					if (record1.getR13Leasing() != null) {
						R13Cell2.setCellValue(record1.getR13Leasing().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}
					// R13 Col D
					Cell R13Cell3 = row.createCell(3);
					if (record.getR13Overdrafts() != null) {
						R13Cell3.setCellValue(record.getR13Overdrafts().doubleValue());
						R13Cell3.setCellStyle(numberStyle);
					} else {
						R13Cell3.setCellValue("");
						R13Cell3.setCellStyle(textStyle);
					}

					// R13 Col E
					Cell R13Cell4 = row.createCell(4);
					if (record.getR13OtherInstallmentLoans() != null) {
						R13Cell4.setCellValue(record.getR13OtherInstallmentLoans().doubleValue());
						R13Cell4.setCellStyle(numberStyle);
					} else {
						R13Cell4.setCellValue("");
						R13Cell4.setCellStyle(textStyle);
					}
					// R14 Col B
					row = sheet.getRow(13); // Row index 13 is Excel Row 14
					// R14 Col B
					Cell R14Cell1 = row.createCell(1);
					if (record1.getR14FactoringDebtors() != null) {
						R14Cell1.setCellValue(record1.getR14FactoringDebtors().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14Cell2 = row.createCell(2);
					if (record1.getR14Leasing() != null) {
						R14Cell2.setCellValue(record1.getR14Leasing().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

					// R14 Col D
					Cell R14Cell3 = row.createCell(3);
					if (record.getR14Overdrafts() != null) {
						R14Cell3.setCellValue(record.getR14Overdrafts().doubleValue());
						R14Cell3.setCellStyle(numberStyle);
					} else {
						R14Cell3.setCellValue("");
						R14Cell3.setCellStyle(textStyle);
					}

					// R14 Col E
					Cell R14Cell4 = row.createCell(4);
					if (record.getR14OtherInstallmentLoans() != null) {
						R14Cell4.setCellValue(record.getR14OtherInstallmentLoans().doubleValue());
						R14Cell4.setCellStyle(numberStyle);
					} else {
						R14Cell4.setCellValue("");
						R14Cell4.setCellStyle(textStyle);
					}

					// --- R16 (Row Index 15) ---
					row = sheet.getRow(15);
					Cell R16Cell1 = row.createCell(1);
					if (record1.getR16FactoringDebtors() != null) {
						R16Cell1.setCellValue(record1.getR16FactoringDebtors().doubleValue());
						R16Cell1.setCellStyle(numberStyle);
					} else {
						R16Cell1.setCellValue("");
						R16Cell1.setCellStyle(textStyle);
					}
					Cell R16Cell2 = row.createCell(2);
					if (record1.getR16Leasing() != null) {
						R16Cell2.setCellValue(record1.getR16Leasing().doubleValue());
						R16Cell2.setCellStyle(numberStyle);
					} else {
						R16Cell2.setCellValue("");
						R16Cell2.setCellStyle(textStyle);
					}
					Cell R16Cell3 = row.createCell(3);
					if (record.getR16Overdrafts() != null) {
						R16Cell3.setCellValue(record.getR16Overdrafts().doubleValue());
						R16Cell3.setCellStyle(numberStyle);
					} else {
						R16Cell3.setCellValue("");
						R16Cell3.setCellStyle(textStyle);
					}
					Cell R16Cell4 = row.createCell(4);
					if (record.getR16OtherInstallmentLoans() != null) {
						R16Cell4.setCellValue(record.getR16OtherInstallmentLoans().doubleValue());
						R16Cell4.setCellStyle(numberStyle);
					} else {
						R16Cell4.setCellValue("");
						R16Cell4.setCellStyle(textStyle);
					}

					// --- R17 (Row Index 16) ---
					row = sheet.getRow(16);
					Cell R17Cell1 = row.createCell(1);
					if (record1.getR17FactoringDebtors() != null) {
						R17Cell1.setCellValue(record1.getR17FactoringDebtors().doubleValue());
						R17Cell1.setCellStyle(numberStyle);
					} else {
						R17Cell1.setCellValue("");
						R17Cell1.setCellStyle(textStyle);
					}
					Cell R17Cell2 = row.createCell(2);
					if (record1.getR17Leasing() != null) {
						R17Cell2.setCellValue(record1.getR17Leasing().doubleValue());
						R17Cell2.setCellStyle(numberStyle);
					} else {
						R17Cell2.setCellValue("");
						R17Cell2.setCellStyle(textStyle);
					}
					Cell R17Cell3 = row.createCell(3);
					if (record.getR17Overdrafts() != null) {
						R17Cell3.setCellValue(record.getR17Overdrafts().doubleValue());
						R17Cell3.setCellStyle(numberStyle);
					} else {
						R17Cell3.setCellValue("");
						R17Cell3.setCellStyle(textStyle);
					}
					Cell R17Cell4 = row.createCell(4);
					if (record.getR17OtherInstallmentLoans() != null) {
						R17Cell4.setCellValue(record.getR17OtherInstallmentLoans().doubleValue());
						R17Cell4.setCellStyle(numberStyle);
					} else {
						R17Cell4.setCellValue("");
						R17Cell4.setCellStyle(textStyle);
					}

					// --- R18 (Row Index 17) ---
					row = sheet.getRow(17);
					Cell R18Cell1 = row.createCell(1);
					if (record1.getR18FactoringDebtors() != null) {
						R18Cell1.setCellValue(record1.getR18FactoringDebtors().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}
					Cell R18Cell2 = row.createCell(2);
					if (record1.getR18Leasing() != null) {
						R18Cell2.setCellValue(record1.getR18Leasing().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}
					Cell R18Cell3 = row.createCell(3);
					if (record.getR18Overdrafts() != null) {
						R18Cell3.setCellValue(record.getR18Overdrafts().doubleValue());
						R18Cell3.setCellStyle(numberStyle);
					} else {
						R18Cell3.setCellValue("");
						R18Cell3.setCellStyle(textStyle);
					}
					Cell R18Cell4 = row.createCell(4);
					if (record.getR18OtherInstallmentLoans() != null) {
						R18Cell4.setCellValue(record.getR18OtherInstallmentLoans().doubleValue());
						R18Cell4.setCellStyle(numberStyle);
					} else {
						R18Cell4.setCellValue("");
						R18Cell4.setCellStyle(textStyle);
					}

					// --- R19 (Row Index 18) ---
					row = sheet.getRow(18);
					Cell R19Cell1 = row.createCell(1);
					if (record1.getR19FactoringDebtors() != null) {
						R19Cell1.setCellValue(record1.getR19FactoringDebtors().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}
					Cell R19Cell2 = row.createCell(2);
					if (record1.getR19Leasing() != null) {
						R19Cell2.setCellValue(record1.getR19Leasing().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}
					Cell R19Cell3 = row.createCell(3);
					if (record.getR19Overdrafts() != null) {
						R19Cell3.setCellValue(record.getR19Overdrafts().doubleValue());
						R19Cell3.setCellStyle(numberStyle);
					} else {
						R19Cell3.setCellValue("");
						R19Cell3.setCellStyle(textStyle);
					}
					Cell R19Cell4 = row.createCell(4);
					if (record.getR19OtherInstallmentLoans() != null) {
						R19Cell4.setCellValue(record.getR19OtherInstallmentLoans().doubleValue());
						R19Cell4.setCellStyle(numberStyle);
					} else {
						R19Cell4.setCellValue("");
						R19Cell4.setCellStyle(textStyle);
					}
					// --- R20 (Row Index 19) ---
					row = sheet.getRow(19);
					Cell R20Cell1 = row.createCell(1);
					if (record1.getR20FactoringDebtors() != null) {
						R20Cell1.setCellValue(record1.getR20FactoringDebtors().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}
					Cell R20Cell2 = row.createCell(2);
					if (record1.getR20Leasing() != null) {
						R20Cell2.setCellValue(record1.getR20Leasing().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}
					Cell R20Cell3 = row.createCell(3);
					if (record.getR20Overdrafts() != null) {
						R20Cell3.setCellValue(record.getR20Overdrafts().doubleValue());
						R20Cell3.setCellStyle(numberStyle);
					} else {
						R20Cell3.setCellValue("");
						R20Cell3.setCellStyle(textStyle);
					}
					Cell R20Cell4 = row.createCell(4);
					if (record.getR20OtherInstallmentLoans() != null) {
						R20Cell4.setCellValue(record.getR20OtherInstallmentLoans().doubleValue());
						R20Cell4.setCellStyle(numberStyle);
					} else {
						R20Cell4.setCellValue("");
						R20Cell4.setCellStyle(textStyle);
					}

					// --- R21 (Row Index 20) ---
					row = sheet.getRow(20);
					Cell R21Cell1 = row.createCell(1);
					if (record1.getR21FactoringDebtors() != null) {
						R21Cell1.setCellValue(record1.getR21FactoringDebtors().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}
					Cell R21Cell2 = row.createCell(2);
					if (record1.getR21Leasing() != null) {
						R21Cell2.setCellValue(record1.getR21Leasing().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}
					Cell R21Cell3 = row.createCell(3);
					if (record.getR21Overdrafts() != null) {
						R21Cell3.setCellValue(record.getR21Overdrafts().doubleValue());
						R21Cell3.setCellStyle(numberStyle);
					} else {
						R21Cell3.setCellValue("");
						R21Cell3.setCellStyle(textStyle);
					}
					Cell R21Cell4 = row.createCell(4);
					if (record.getR21OtherInstallmentLoans() != null) {
						R21Cell4.setCellValue(record.getR21OtherInstallmentLoans().doubleValue());
						R21Cell4.setCellStyle(numberStyle);
					} else {
						R21Cell4.setCellValue("");
						R21Cell4.setCellStyle(textStyle);
					}

					// --- R22 (Row Index 21) ---
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(1);
					if (record1.getR22FactoringDebtors() != null) {
						R22Cell1.setCellValue(record1.getR22FactoringDebtors().doubleValue());
						R22Cell1.setCellStyle(numberStyle);
					} else {
						R22Cell1.setCellValue("");
						R22Cell1.setCellStyle(textStyle);
					}
					Cell R22Cell2 = row.createCell(2);
					if (record1.getR22Leasing() != null) {
						R22Cell2.setCellValue(record1.getR22Leasing().doubleValue());
						R22Cell2.setCellStyle(numberStyle);
					} else {
						R22Cell2.setCellValue("");
						R22Cell2.setCellStyle(textStyle);
					}
					Cell R22Cell3 = row.createCell(3);
					if (record.getR22Overdrafts() != null) {
						R22Cell3.setCellValue(record.getR22Overdrafts().doubleValue());
						R22Cell3.setCellStyle(numberStyle);
					} else {
						R22Cell3.setCellValue("");
						R22Cell3.setCellStyle(textStyle);
					}
					Cell R22Cell4 = row.createCell(4);
					if (record.getR22OtherInstallmentLoans() != null) {
						R22Cell4.setCellValue(record.getR22OtherInstallmentLoans().doubleValue());
						R22Cell4.setCellStyle(numberStyle);
					} else {
						R22Cell4.setCellValue("");
						R22Cell4.setCellStyle(textStyle);
					}

					// --- R23 (Row Index 22) ---
					row = sheet.getRow(22);
					Cell R23Cell1 = row.createCell(1);
					if (record1.getR23FactoringDebtors() != null) {
						R23Cell1.setCellValue(record1.getR23FactoringDebtors().doubleValue());
						R23Cell1.setCellStyle(numberStyle);
					} else {
						R23Cell1.setCellValue("");
						R23Cell1.setCellStyle(textStyle);
					}
					Cell R23Cell2 = row.createCell(2);
					if (record1.getR23Leasing() != null) {
						R23Cell2.setCellValue(record1.getR23Leasing().doubleValue());
						R23Cell2.setCellStyle(numberStyle);
					} else {
						R23Cell2.setCellValue("");
						R23Cell2.setCellStyle(textStyle);
					}
					Cell R23Cell3 = row.createCell(3);
					if (record.getR23Overdrafts() != null) {
						R23Cell3.setCellValue(record.getR23Overdrafts().doubleValue());
						R23Cell3.setCellStyle(numberStyle);
					} else {
						R23Cell3.setCellValue("");
						R23Cell3.setCellStyle(textStyle);
					}
					Cell R23Cell4 = row.createCell(4);
					if (record.getR23OtherInstallmentLoans() != null) {
						R23Cell4.setCellValue(record.getR23OtherInstallmentLoans().doubleValue());
						R23Cell4.setCellStyle(numberStyle);
					} else {
						R23Cell4.setCellValue("");
						R23Cell4.setCellStyle(textStyle);
					}

					// --- R24 (Row Index 23) ---
					row = sheet.getRow(23);
					Cell R24Cell1 = row.createCell(1);
					if (record1.getR24FactoringDebtors() != null) {
						R24Cell1.setCellValue(record1.getR24FactoringDebtors().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}
					Cell R24Cell2 = row.createCell(2);
					if (record1.getR24Leasing() != null) {
						R24Cell2.setCellValue(record1.getR24Leasing().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}
					Cell R24Cell3 = row.createCell(3);
					if (record.getR24Overdrafts() != null) {
						R24Cell3.setCellValue(record.getR24Overdrafts().doubleValue());
						R24Cell3.setCellStyle(numberStyle);
					} else {
						R24Cell3.setCellValue("");
						R24Cell3.setCellStyle(textStyle);
					}
					Cell R24Cell4 = row.createCell(4);
					if (record.getR24OtherInstallmentLoans() != null) {
						R24Cell4.setCellValue(record.getR24OtherInstallmentLoans().doubleValue());
						R24Cell4.setCellStyle(numberStyle);
					} else {
						R24Cell4.setCellValue("");
						R24Cell4.setCellStyle(textStyle);
					}

					// --- R25 (Row Index 24) ---
					row = sheet.getRow(24);
					Cell R25Cell1 = row.createCell(1);
					if (record1.getR25FactoringDebtors() != null) {
						R25Cell1.setCellValue(record1.getR25FactoringDebtors().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}
					Cell R25Cell2 = row.createCell(2);
					if (record1.getR25Leasing() != null) {
						R25Cell2.setCellValue(record1.getR25Leasing().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}
					Cell R25Cell3 = row.createCell(3);
					if (record.getR25Overdrafts() != null) {
						R25Cell3.setCellValue(record.getR25Overdrafts().doubleValue());
						R25Cell3.setCellStyle(numberStyle);
					} else {
						R25Cell3.setCellValue("");
						R25Cell3.setCellStyle(textStyle);
					}
					Cell R25Cell4 = row.createCell(4);
					if (record.getR25OtherInstallmentLoans() != null) {
						R25Cell4.setCellValue(record.getR25OtherInstallmentLoans().doubleValue());
						R25Cell4.setCellStyle(numberStyle);
					} else {
						R25Cell4.setCellValue("");
						R25Cell4.setCellStyle(textStyle);
					}

					// --- R26 (Row Index 25) ---
					row = sheet.getRow(25);
					Cell R26Cell1 = row.createCell(1);
					if (record1.getR26FactoringDebtors() != null) {
						R26Cell1.setCellValue(record1.getR26FactoringDebtors().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}
					Cell R26Cell2 = row.createCell(2);
					if (record1.getR26Leasing() != null) {
						R26Cell2.setCellValue(record1.getR26Leasing().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}
					Cell R26Cell3 = row.createCell(3);
					if (record.getR26Overdrafts() != null) {
						R26Cell3.setCellValue(record.getR26Overdrafts().doubleValue());
						R26Cell3.setCellStyle(numberStyle);
					} else {
						R26Cell3.setCellValue("");
						R26Cell3.setCellStyle(textStyle);
					}
					Cell R26Cell4 = row.createCell(4);
					if (record.getR26OtherInstallmentLoans() != null) {
						R26Cell4.setCellValue(record.getR26OtherInstallmentLoans().doubleValue());
						R26Cell4.setCellStyle(numberStyle);
					} else {
						R26Cell4.setCellValue("");
						R26Cell4.setCellStyle(textStyle);
					}

					// --- R27 (Row Index 26) ---
					row = sheet.getRow(26);
					Cell R27Cell1 = row.createCell(1);
					if (record1.getR27FactoringDebtors() != null) {
						R27Cell1.setCellValue(record1.getR27FactoringDebtors().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}
					Cell R27Cell2 = row.createCell(2);
					if (record1.getR27Leasing() != null) {
						R27Cell2.setCellValue(record1.getR27Leasing().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}
					Cell R27Cell3 = row.createCell(3);
					if (record.getR27Overdrafts() != null) {
						R27Cell3.setCellValue(record.getR27Overdrafts().doubleValue());
						R27Cell3.setCellStyle(numberStyle);
					} else {
						R27Cell3.setCellValue("");
						R27Cell3.setCellStyle(textStyle);
					}
					Cell R27Cell4 = row.createCell(4);
					if (record.getR27OtherInstallmentLoans() != null) {
						R27Cell4.setCellValue(record.getR27OtherInstallmentLoans().doubleValue());
						R27Cell4.setCellStyle(numberStyle);
					} else {
						R27Cell4.setCellValue("");
						R27Cell4.setCellStyle(textStyle);
					}

					// --- R28 (Row Index 27) ---
					row = sheet.getRow(27);
					Cell R28Cell1 = row.createCell(1);
					if (record1.getR28FactoringDebtors() != null) {
						R28Cell1.setCellValue(record1.getR28FactoringDebtors().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}
					Cell R28Cell2 = row.createCell(2);
					if (record1.getR28Leasing() != null) {
						R28Cell2.setCellValue(record1.getR28Leasing().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}
					Cell R28Cell3 = row.createCell(3);
					if (record.getR28Overdrafts() != null) {
						R28Cell3.setCellValue(record.getR28Overdrafts().doubleValue());
						R28Cell3.setCellStyle(numberStyle);
					} else {
						R28Cell3.setCellValue("");
						R28Cell3.setCellStyle(textStyle);
					}
					Cell R28Cell4 = row.createCell(4);
					if (record.getR28OtherInstallmentLoans() != null) {
						R28Cell4.setCellValue(record.getR28OtherInstallmentLoans().doubleValue());
						R28Cell4.setCellStyle(numberStyle);
					} else {
						R28Cell4.setCellValue("");
						R28Cell4.setCellStyle(textStyle);
					}

					// --- R30 (Row Index 29) ---
					row = sheet.getRow(29);
					Cell R30Cell1 = row.createCell(1);
					if (record1.getR30FactoringDebtors() != null) {
						R30Cell1.setCellValue(record1.getR30FactoringDebtors().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}
					Cell R30Cell2 = row.createCell(2);
					if (record1.getR30Leasing() != null) {
						R30Cell2.setCellValue(record1.getR30Leasing().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}
					Cell R30Cell3 = row.createCell(3);
					if (record.getR30Overdrafts() != null) {
						R30Cell3.setCellValue(record.getR30Overdrafts().doubleValue());
						R30Cell3.setCellStyle(numberStyle);
					} else {
						R30Cell3.setCellValue("");
						R30Cell3.setCellStyle(textStyle);
					}
					Cell R30Cell4 = row.createCell(4);
					if (record.getR30OtherInstallmentLoans() != null) {
						R30Cell4.setCellValue(record.getR30OtherInstallmentLoans().doubleValue());
						R30Cell4.setCellStyle(numberStyle);
					} else {
						R30Cell4.setCellValue("");
						R30Cell4.setCellStyle(textStyle);
					}

					// --- R31 (Row Index 30) ---
					row = sheet.getRow(30);
					Cell R31Cell1 = row.createCell(1);
					if (record1.getR31FactoringDebtors() != null) {
						R31Cell1.setCellValue(record1.getR31FactoringDebtors().doubleValue());
						R31Cell1.setCellStyle(numberStyle);
					} else {
						R31Cell1.setCellValue("");
						R31Cell1.setCellStyle(textStyle);
					}
					Cell R31Cell2 = row.createCell(2);
					if (record1.getR31Leasing() != null) {
						R31Cell2.setCellValue(record1.getR31Leasing().doubleValue());
						R31Cell2.setCellStyle(numberStyle);
					} else {
						R31Cell2.setCellValue("");
						R31Cell2.setCellStyle(textStyle);
					}
					Cell R31Cell3 = row.createCell(3);
					if (record.getR31Overdrafts() != null) {
						R31Cell3.setCellValue(record.getR31Overdrafts().doubleValue());
						R31Cell3.setCellStyle(numberStyle);
					} else {
						R31Cell3.setCellValue("");
						R31Cell3.setCellStyle(textStyle);
					}
					Cell R31Cell4 = row.createCell(4);
					if (record.getR31OtherInstallmentLoans() != null) {
						R31Cell4.setCellValue(record.getR31OtherInstallmentLoans().doubleValue());
						R31Cell4.setCellStyle(numberStyle);
					} else {
						R31Cell4.setCellValue("");
						R31Cell4.setCellStyle(textStyle);
					}

					// --- R32 (Row Index 31) ---
					row = sheet.getRow(31);
					Cell R32Cell1 = row.createCell(1);
					if (record1.getR32FactoringDebtors() != null) {
						R32Cell1.setCellValue(record1.getR32FactoringDebtors().doubleValue());
						R32Cell1.setCellStyle(numberStyle);
					} else {
						R32Cell1.setCellValue("");
						R32Cell1.setCellStyle(textStyle);
					}
					Cell R32Cell2 = row.createCell(2);
					if (record1.getR32Leasing() != null) {
						R32Cell2.setCellValue(record1.getR32Leasing().doubleValue());
						R32Cell2.setCellStyle(numberStyle);
					} else {
						R32Cell2.setCellValue("");
						R32Cell2.setCellStyle(textStyle);
					}
					Cell R32Cell3 = row.createCell(3);
					if (record.getR32Overdrafts() != null) {
						R32Cell3.setCellValue(record.getR32Overdrafts().doubleValue());
						R32Cell3.setCellStyle(numberStyle);
					} else {
						R32Cell3.setCellValue("");
						R32Cell3.setCellStyle(textStyle);
					}
					Cell R32Cell4 = row.createCell(4);
					if (record.getR32OtherInstallmentLoans() != null) {
						R32Cell4.setCellValue(record.getR32OtherInstallmentLoans().doubleValue());
						R32Cell4.setCellStyle(numberStyle);
					} else {
						R32Cell4.setCellValue("");
						R32Cell4.setCellStyle(textStyle);
					}

					// --- R33 (Row Index 32) ---
					row = sheet.getRow(32);
					Cell R33Cell1 = row.createCell(1);
					if (record1.getR33FactoringDebtors() != null) {
						R33Cell1.setCellValue(record1.getR33FactoringDebtors().doubleValue());
						R33Cell1.setCellStyle(numberStyle);
					} else {
						R33Cell1.setCellValue("");
						R33Cell1.setCellStyle(textStyle);
					}
					Cell R33Cell2 = row.createCell(2);
					if (record1.getR33Leasing() != null) {
						R33Cell2.setCellValue(record1.getR33Leasing().doubleValue());
						R33Cell2.setCellStyle(numberStyle);
					} else {
						R33Cell2.setCellValue("");
						R33Cell2.setCellStyle(textStyle);
					}
					Cell R33Cell3 = row.createCell(3);
					if (record.getR33Overdrafts() != null) {
						R33Cell3.setCellValue(record.getR33Overdrafts().doubleValue());
						R33Cell3.setCellStyle(numberStyle);
					} else {
						R33Cell3.setCellValue("");
						R33Cell3.setCellStyle(textStyle);
					}
					Cell R33Cell4 = row.createCell(4);
					if (record.getR33OtherInstallmentLoans() != null) {
						R33Cell4.setCellValue(record.getR33OtherInstallmentLoans().doubleValue());
						R33Cell4.setCellStyle(numberStyle);
					} else {
						R33Cell4.setCellValue("");
						R33Cell4.setCellStyle(textStyle);
					}

					// --- R34 (Row Index 33) ---
					row = sheet.getRow(33);
					Cell R34Cell1 = row.createCell(1);
					if (record1.getR34FactoringDebtors() != null) {
						R34Cell1.setCellValue(record1.getR34FactoringDebtors().doubleValue());
						R34Cell1.setCellStyle(numberStyle);
					} else {
						R34Cell1.setCellValue("");
						R34Cell1.setCellStyle(textStyle);
					}
					Cell R34Cell2 = row.createCell(2);
					if (record1.getR34Leasing() != null) {
						R34Cell2.setCellValue(record1.getR34Leasing().doubleValue());
						R34Cell2.setCellStyle(numberStyle);
					} else {
						R34Cell2.setCellValue("");
						R34Cell2.setCellStyle(textStyle);
					}
					Cell R34Cell3 = row.createCell(3);
					if (record.getR34Overdrafts() != null) {
						R34Cell3.setCellValue(record.getR34Overdrafts().doubleValue());
						R34Cell3.setCellStyle(numberStyle);
					} else {
						R34Cell3.setCellValue("");
						R34Cell3.setCellStyle(textStyle);
					}
					Cell R34Cell4 = row.createCell(4);
					if (record.getR34OtherInstallmentLoans() != null) {
						R34Cell4.setCellValue(record.getR34OtherInstallmentLoans().doubleValue());
						R34Cell4.setCellStyle(numberStyle);
					} else {
						R34Cell4.setCellValue("");
						R34Cell4.setCellStyle(textStyle);
					}

					// --- R35 (Row Index 34) ---
					row = sheet.getRow(34);
					Cell R35Cell1 = row.createCell(1);
					if (record1.getR35FactoringDebtors() != null) {
						R35Cell1.setCellValue(record1.getR35FactoringDebtors().doubleValue());
						R35Cell1.setCellStyle(numberStyle);
					} else {
						R35Cell1.setCellValue("");
						R35Cell1.setCellStyle(textStyle);
					}
					Cell R35Cell2 = row.createCell(2);
					if (record1.getR35Leasing() != null) {
						R35Cell2.setCellValue(record1.getR35Leasing().doubleValue());
						R35Cell2.setCellStyle(numberStyle);
					} else {
						R35Cell2.setCellValue("");
						R35Cell2.setCellStyle(textStyle);
					}
					Cell R35Cell3 = row.createCell(3);
					if (record.getR35Overdrafts() != null) {
						R35Cell3.setCellValue(record.getR35Overdrafts().doubleValue());
						R35Cell3.setCellStyle(numberStyle);
					} else {
						R35Cell3.setCellValue("");
						R35Cell3.setCellStyle(textStyle);
					}
					Cell R35Cell4 = row.createCell(4);
					if (record.getR35OtherInstallmentLoans() != null) {
						R35Cell4.setCellValue(record.getR35OtherInstallmentLoans().doubleValue());
						R35Cell4.setCellStyle(numberStyle);
					} else {
						R35Cell4.setCellValue("");
						R35Cell4.setCellStyle(textStyle);
					}

					// --- R36 (Row Index 35) ---
					row = sheet.getRow(35);
					Cell R36Cell1 = row.createCell(1);
					if (record1.getR36FactoringDebtors() != null) {
						R36Cell1.setCellValue(record1.getR36FactoringDebtors().doubleValue());
						R36Cell1.setCellStyle(numberStyle);
					} else {
						R36Cell1.setCellValue("");
						R36Cell1.setCellStyle(textStyle);
					}
					Cell R36Cell2 = row.createCell(2);
					if (record1.getR36Leasing() != null) {
						R36Cell2.setCellValue(record1.getR36Leasing().doubleValue());
						R36Cell2.setCellStyle(numberStyle);
					} else {
						R36Cell2.setCellValue("");
						R36Cell2.setCellStyle(textStyle);
					}
					Cell R36Cell3 = row.createCell(3);
					if (record.getR36Overdrafts() != null) {
						R36Cell3.setCellValue(record.getR36Overdrafts().doubleValue());
						R36Cell3.setCellStyle(numberStyle);
					} else {
						R36Cell3.setCellValue("");
						R36Cell3.setCellStyle(textStyle);
					}
					Cell R36Cell4 = row.createCell(4);
					if (record.getR36OtherInstallmentLoans() != null) {
						R36Cell4.setCellValue(record.getR36OtherInstallmentLoans().doubleValue());
						R36Cell4.setCellStyle(numberStyle);
					} else {
						R36Cell4.setCellValue("");
						R36Cell4.setCellStyle(textStyle);
					}

					// --- R37 (Row Index 36) ---
					row = sheet.getRow(36);
					Cell R37Cell1 = row.createCell(1);
					if (record1.getR37FactoringDebtors() != null) {
						R37Cell1.setCellValue(record1.getR37FactoringDebtors().doubleValue());
						R37Cell1.setCellStyle(numberStyle);
					} else {
						R37Cell1.setCellValue("");
						R37Cell1.setCellStyle(textStyle);
					}
					Cell R37Cell2 = row.createCell(2);
					if (record1.getR37Leasing() != null) {
						R37Cell2.setCellValue(record1.getR37Leasing().doubleValue());
						R37Cell2.setCellStyle(numberStyle);
					} else {
						R37Cell2.setCellValue("");
						R37Cell2.setCellStyle(textStyle);
					}
					Cell R37Cell3 = row.createCell(3);
					if (record.getR37Overdrafts() != null) {
						R37Cell3.setCellValue(record.getR37Overdrafts().doubleValue());
						R37Cell3.setCellStyle(numberStyle);
					} else {
						R37Cell3.setCellValue("");
						R37Cell3.setCellStyle(textStyle);
					}
					Cell R37Cell4 = row.createCell(4);
					if (record.getR37OtherInstallmentLoans() != null) {
						R37Cell4.setCellValue(record.getR37OtherInstallmentLoans().doubleValue());
						R37Cell4.setCellStyle(numberStyle);
					} else {
						R37Cell4.setCellValue("");
						R37Cell4.setCellStyle(textStyle);
					}
					// --- R39 (Row Index 38) ---
					row = sheet.getRow(38);
					Cell R39Cell1 = row.createCell(1);
					if (record1.getR39FactoringDebtors() != null) {
						R39Cell1.setCellValue(record1.getR39FactoringDebtors().doubleValue());
						R39Cell1.setCellStyle(numberStyle);
					} else {
						R39Cell1.setCellValue("");
						R39Cell1.setCellStyle(textStyle);
					}
					Cell R39Cell2 = row.createCell(2);
					if (record1.getR39Leasing() != null) {
						R39Cell2.setCellValue(record1.getR39Leasing().doubleValue());
						R39Cell2.setCellStyle(numberStyle);
					} else {
						R39Cell2.setCellValue("");
						R39Cell2.setCellStyle(textStyle);
					}
					Cell R39Cell3 = row.createCell(3);
					if (record.getR39Overdrafts() != null) {
						R39Cell3.setCellValue(record.getR39Overdrafts().doubleValue());
						R39Cell3.setCellStyle(numberStyle);
					} else {
						R39Cell3.setCellValue("");
						R39Cell3.setCellStyle(textStyle);
					}
					Cell R39Cell4 = row.createCell(4);
					if (record.getR39OtherInstallmentLoans() != null) {
						R39Cell4.setCellValue(record.getR39OtherInstallmentLoans().doubleValue());
						R39Cell4.setCellStyle(numberStyle);
					} else {
						R39Cell4.setCellValue("");
						R39Cell4.setCellStyle(textStyle);
					}

					// --- R40 (Row Index 39) ---
					row = sheet.getRow(39);
					Cell R40Cell1 = row.createCell(1);
					if (record1.getR40FactoringDebtors() != null) {
						R40Cell1.setCellValue(record1.getR40FactoringDebtors().doubleValue());
						R40Cell1.setCellStyle(numberStyle);
					} else {
						R40Cell1.setCellValue("");
						R40Cell1.setCellStyle(textStyle);
					}
					Cell R40Cell2 = row.createCell(2);
					if (record1.getR40Leasing() != null) {
						R40Cell2.setCellValue(record1.getR40Leasing().doubleValue());
						R40Cell2.setCellStyle(numberStyle);
					} else {
						R40Cell2.setCellValue("");
						R40Cell2.setCellStyle(textStyle);
					}
					Cell R40Cell3 = row.createCell(3);
					if (record.getR40Overdrafts() != null) {
						R40Cell3.setCellValue(record.getR40Overdrafts().doubleValue());
						R40Cell3.setCellStyle(numberStyle);
					} else {
						R40Cell3.setCellValue("");
						R40Cell3.setCellStyle(textStyle);
					}
					Cell R40Cell4 = row.createCell(4);
					if (record.getR40OtherInstallmentLoans() != null) {
						R40Cell4.setCellValue(record.getR40OtherInstallmentLoans().doubleValue());
						R40Cell4.setCellStyle(numberStyle);
					} else {
						R40Cell4.setCellValue("");
						R40Cell4.setCellStyle(textStyle);
					}
					// --- R42 (Row Index 41) ---
					row = sheet.getRow(41);
					Cell R42Cell1 = row.createCell(1);
					if (record1.getR42FactoringDebtors() != null) {
						R42Cell1.setCellValue(record1.getR42FactoringDebtors().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}
					Cell R42Cell2 = row.createCell(2);
					if (record1.getR42Leasing() != null) {
						R42Cell2.setCellValue(record1.getR42Leasing().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}
					Cell R42Cell3 = row.createCell(3);
					if (record.getR42Overdrafts() != null) {
						R42Cell3.setCellValue(record.getR42Overdrafts().doubleValue());
						R42Cell3.setCellStyle(numberStyle);
					} else {
						R42Cell3.setCellValue("");
						R42Cell3.setCellStyle(textStyle);
					}
					Cell R42Cell4 = row.createCell(4);
					if (record.getR42OtherInstallmentLoans() != null) {
						R42Cell4.setCellValue(record.getR42OtherInstallmentLoans().doubleValue());
						R42Cell4.setCellStyle(numberStyle);
					} else {
						R42Cell4.setCellValue("");
						R42Cell4.setCellStyle(textStyle);
					}

					// --- R43 (Row Index 42) ---
					row = sheet.getRow(42);
					Cell R43Cell1 = row.createCell(1);
					if (record1.getR43FactoringDebtors() != null) {
						R43Cell1.setCellValue(record1.getR43FactoringDebtors().doubleValue());
						R43Cell1.setCellStyle(numberStyle);
					} else {
						R43Cell1.setCellValue("");
						R43Cell1.setCellStyle(textStyle);
					}
					Cell R43Cell2 = row.createCell(2);
					if (record1.getR43Leasing() != null) {
						R43Cell2.setCellValue(record1.getR43Leasing().doubleValue());
						R43Cell2.setCellStyle(numberStyle);
					} else {
						R43Cell2.setCellValue("");
						R43Cell2.setCellStyle(textStyle);
					}
					Cell R43Cell3 = row.createCell(3);
					if (record.getR43Overdrafts() != null) {
						R43Cell3.setCellValue(record.getR43Overdrafts().doubleValue());
						R43Cell3.setCellStyle(numberStyle);
					} else {
						R43Cell3.setCellValue("");
						R43Cell3.setCellStyle(textStyle);
					}
					Cell R43Cell4 = row.createCell(4);
					if (record.getR43OtherInstallmentLoans() != null) {
						R43Cell4.setCellValue(record.getR43OtherInstallmentLoans().doubleValue());
						R43Cell4.setCellStyle(numberStyle);
					} else {
						R43Cell4.setCellValue("");
						R43Cell4.setCellStyle(textStyle);
					}
					// --- R45 (Row Index 44) ---
					row = sheet.getRow(44);
					Cell R45Cell1 = row.createCell(1);
					if (record1.getR45FactoringDebtors() != null) {
						R45Cell1.setCellValue(record1.getR45FactoringDebtors().doubleValue());
						R45Cell1.setCellStyle(numberStyle);
					} else {
						R45Cell1.setCellValue("");
						R45Cell1.setCellStyle(textStyle);
					}
					Cell R45Cell2 = row.createCell(2);
					if (record1.getR45Leasing() != null) {
						R45Cell2.setCellValue(record1.getR45Leasing().doubleValue());
						R45Cell2.setCellStyle(numberStyle);
					} else {
						R45Cell2.setCellValue("");
						R45Cell2.setCellStyle(textStyle);
					}
					Cell R45Cell3 = row.createCell(3);
					if (record.getR45Overdrafts() != null) {
						R45Cell3.setCellValue(record.getR45Overdrafts().doubleValue());
						R45Cell3.setCellStyle(numberStyle);
					} else {
						R45Cell3.setCellValue("");
						R45Cell3.setCellStyle(textStyle);
					}
					Cell R45Cell4 = row.createCell(4);
					if (record.getR45OtherInstallmentLoans() != null) {
						R45Cell4.setCellValue(record.getR45OtherInstallmentLoans().doubleValue());
						R45Cell4.setCellStyle(numberStyle);
					} else {
						R45Cell4.setCellValue("");
						R45Cell4.setCellStyle(textStyle);
					}

					// --- R46 (Row Index 45) ---
					row = sheet.getRow(45);
					Cell R46Cell1 = row.createCell(1);
					if (record1.getR46FactoringDebtors() != null) {
						R46Cell1.setCellValue(record1.getR46FactoringDebtors().doubleValue());
						R46Cell1.setCellStyle(numberStyle);
					} else {
						R46Cell1.setCellValue("");
						R46Cell1.setCellStyle(textStyle);
					}
					Cell R46Cell2 = row.createCell(2);
					if (record1.getR46Leasing() != null) {
						R46Cell2.setCellValue(record1.getR46Leasing().doubleValue());
						R46Cell2.setCellStyle(numberStyle);
					} else {
						R46Cell2.setCellValue("");
						R46Cell2.setCellStyle(textStyle);
					}
					Cell R46Cell3 = row.createCell(3);
					if (record.getR46Overdrafts() != null) {
						R46Cell3.setCellValue(record.getR46Overdrafts().doubleValue());
						R46Cell3.setCellStyle(numberStyle);
					} else {
						R46Cell3.setCellValue("");
						R46Cell3.setCellStyle(textStyle);
					}
					Cell R46Cell4 = row.createCell(4);
					if (record.getR46OtherInstallmentLoans() != null) {
						R46Cell4.setCellValue(record.getR46OtherInstallmentLoans().doubleValue());
						R46Cell4.setCellStyle(numberStyle);
					} else {
						R46Cell4.setCellValue("");
						R46Cell4.setCellStyle(textStyle);
					}

					// --- R47 (Row Index 46) ---
					row = sheet.getRow(46);
					Cell R47Cell1 = row.createCell(1);
					if (record1.getR47FactoringDebtors() != null) {
						R47Cell1.setCellValue(record1.getR47FactoringDebtors().doubleValue());
						R47Cell1.setCellStyle(numberStyle);
					} else {
						R47Cell1.setCellValue("");
						R47Cell1.setCellStyle(textStyle);
					}
					Cell R47Cell2 = row.createCell(2);
					if (record1.getR47Leasing() != null) {
						R47Cell2.setCellValue(record1.getR47Leasing().doubleValue());
						R47Cell2.setCellStyle(numberStyle);
					} else {
						R47Cell2.setCellValue("");
						R47Cell2.setCellStyle(textStyle);
					}
					Cell R47Cell3 = row.createCell(3);
					if (record.getR47Overdrafts() != null) {
						R47Cell3.setCellValue(record.getR47Overdrafts().doubleValue());
						R47Cell3.setCellStyle(numberStyle);
					} else {
						R47Cell3.setCellValue("");
						R47Cell3.setCellStyle(textStyle);
					}
					Cell R47Cell4 = row.createCell(4);
					if (record.getR47OtherInstallmentLoans() != null) {
						R47Cell4.setCellValue(record.getR47OtherInstallmentLoans().doubleValue());
						R47Cell4.setCellStyle(numberStyle);
					} else {
						R47Cell4.setCellValue("");
						R47Cell4.setCellStyle(textStyle);
					}

					// --- R48 (Row Index 47) ---
					row = sheet.getRow(47);
					Cell R48Cell1 = row.createCell(1);
					if (record1.getR48FactoringDebtors() != null) {
						R48Cell1.setCellValue(record1.getR48FactoringDebtors().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}
					Cell R48Cell2 = row.createCell(2);
					if (record1.getR48Leasing() != null) {
						R48Cell2.setCellValue(record1.getR48Leasing().doubleValue());
						R48Cell2.setCellStyle(numberStyle);
					} else {
						R48Cell2.setCellValue("");
						R48Cell2.setCellStyle(textStyle);
					}
					Cell R48Cell3 = row.createCell(3);
					if (record.getR48Overdrafts() != null) {
						R48Cell3.setCellValue(record.getR48Overdrafts().doubleValue());
						R48Cell3.setCellStyle(numberStyle);
					} else {
						R48Cell3.setCellValue("");
						R48Cell3.setCellStyle(textStyle);
					}
					Cell R48Cell4 = row.createCell(4);
					if (record.getR48OtherInstallmentLoans() != null) {
						R48Cell4.setCellValue(record.getR48OtherInstallmentLoans().doubleValue());
						R48Cell4.setCellStyle(numberStyle);
					} else {
						R48Cell4.setCellValue("");
						R48Cell4.setCellStyle(textStyle);
					}

					// --- R50 (Row Index 49) ---
					row = sheet.getRow(49);
					Cell R50Cell1 = row.createCell(1);
					if (record1.getR50FactoringDebtors() != null) {
						R50Cell1.setCellValue(record1.getR50FactoringDebtors().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}
					Cell R50Cell2 = row.createCell(2);
					if (record1.getR50Leasing() != null) {
						R50Cell2.setCellValue(record1.getR50Leasing().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}
					Cell R50Cell3 = row.createCell(3);
					if (record.getR50Overdrafts() != null) {
						R50Cell3.setCellValue(record.getR50Overdrafts().doubleValue());
						R50Cell3.setCellStyle(numberStyle);
					} else {
						R50Cell3.setCellValue("");
						R50Cell3.setCellStyle(textStyle);
					}
					Cell R50Cell4 = row.createCell(4);
					if (record.getR50OtherInstallmentLoans() != null) {
						R50Cell4.setCellValue(record.getR50OtherInstallmentLoans().doubleValue());
						R50Cell4.setCellStyle(numberStyle);
					} else {
						R50Cell4.setCellValue("");
						R50Cell4.setCellStyle(textStyle);
					}

					// --- R51 (Row Index 50) ---
					row = sheet.getRow(50);
					Cell R51Cell1 = row.createCell(1);
					if (record1.getR51FactoringDebtors() != null) {
						R51Cell1.setCellValue(record1.getR51FactoringDebtors().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}
					Cell R51Cell2 = row.createCell(2);
					if (record1.getR51Leasing() != null) {
						R51Cell2.setCellValue(record1.getR51Leasing().doubleValue());
						R51Cell2.setCellStyle(numberStyle);
					} else {
						R51Cell2.setCellValue("");
						R51Cell2.setCellStyle(textStyle);
					}
					Cell R51Cell3 = row.createCell(3);
					if (record.getR51Overdrafts() != null) {
						R51Cell3.setCellValue(record.getR51Overdrafts().doubleValue());
						R51Cell3.setCellStyle(numberStyle);
					} else {
						R51Cell3.setCellValue("");
						R51Cell3.setCellStyle(textStyle);
					}
					Cell R51Cell4 = row.createCell(4);
					if (record.getR51OtherInstallmentLoans() != null) {
						R51Cell4.setCellValue(record.getR51OtherInstallmentLoans().doubleValue());
						R51Cell4.setCellStyle(numberStyle);
					} else {
						R51Cell4.setCellValue("");
						R51Cell4.setCellStyle(textStyle);
					}

					// --- R52 (Row Index 51) ---
					row = sheet.getRow(51);
					Cell R52Cell1 = row.createCell(1);
					if (record1.getR52FactoringDebtors() != null) {
						R52Cell1.setCellValue(record1.getR52FactoringDebtors().doubleValue());
						R52Cell1.setCellStyle(numberStyle);
					} else {
						R52Cell1.setCellValue("");
						R52Cell1.setCellStyle(textStyle);
					}
					Cell R52Cell2 = row.createCell(2);
					if (record1.getR52Leasing() != null) {
						R52Cell2.setCellValue(record1.getR52Leasing().doubleValue());
						R52Cell2.setCellStyle(numberStyle);
					} else {
						R52Cell2.setCellValue("");
						R52Cell2.setCellStyle(textStyle);
					}
					Cell R52Cell3 = row.createCell(3);
					if (record.getR52Overdrafts() != null) {
						R52Cell3.setCellValue(record.getR52Overdrafts().doubleValue());
						R52Cell3.setCellStyle(numberStyle);
					} else {
						R52Cell3.setCellValue("");
						R52Cell3.setCellStyle(textStyle);
					}
					Cell R52Cell4 = row.createCell(4);
					if (record.getR52OtherInstallmentLoans() != null) {
						R52Cell4.setCellValue(record.getR52OtherInstallmentLoans().doubleValue());
						R52Cell4.setCellStyle(numberStyle);
					} else {
						R52Cell4.setCellValue("");
						R52Cell4.setCellStyle(textStyle);
					}
					// --- R54 (Row Index 53) ---
					row = sheet.getRow(53);
					Cell R54Cell1 = row.createCell(1);
					if (record1.getR54FactoringDebtors() != null) {
						R54Cell1.setCellValue(record1.getR54FactoringDebtors().doubleValue());
						R54Cell1.setCellStyle(numberStyle);
					} else {
						R54Cell1.setCellValue("");
						R54Cell1.setCellStyle(textStyle);
					}
					Cell R54Cell2 = row.createCell(2);
					if (record1.getR54Leasing() != null) {
						R54Cell2.setCellValue(record1.getR54Leasing().doubleValue());
						R54Cell2.setCellStyle(numberStyle);
					} else {
						R54Cell2.setCellValue("");
						R54Cell2.setCellStyle(textStyle);
					}
					Cell R54Cell3 = row.createCell(3);
					if (record.getR54Overdrafts() != null) {
						R54Cell3.setCellValue(record.getR54Overdrafts().doubleValue());
						R54Cell3.setCellStyle(numberStyle);
					} else {
						R54Cell3.setCellValue("");
						R54Cell3.setCellStyle(textStyle);
					}
					Cell R54Cell4 = row.createCell(4);
					if (record.getR54OtherInstallmentLoans() != null) {
						R54Cell4.setCellValue(record.getR54OtherInstallmentLoans().doubleValue());
						R54Cell4.setCellStyle(numberStyle);
					} else {
						R54Cell4.setCellValue("");
						R54Cell4.setCellStyle(textStyle);
					}

					// --- R55 (Row Index 54) ---
					row = sheet.getRow(54);
					Cell R55Cell1 = row.createCell(1);
					if (record1.getR55FactoringDebtors() != null) {
						R55Cell1.setCellValue(record1.getR55FactoringDebtors().doubleValue());
						R55Cell1.setCellStyle(numberStyle);
					} else {
						R55Cell1.setCellValue("");
						R55Cell1.setCellStyle(textStyle);
					}
					Cell R55Cell2 = row.createCell(2);
					if (record1.getR55Leasing() != null) {
						R55Cell2.setCellValue(record1.getR55Leasing().doubleValue());
						R55Cell2.setCellStyle(numberStyle);
					} else {
						R55Cell2.setCellValue("");
						R55Cell2.setCellStyle(textStyle);
					}
					Cell R55Cell3 = row.createCell(3);
					if (record.getR55Overdrafts() != null) {
						R55Cell3.setCellValue(record.getR55Overdrafts().doubleValue());
						R55Cell3.setCellStyle(numberStyle);
					} else {
						R55Cell3.setCellValue("");
						R55Cell3.setCellStyle(textStyle);
					}
					Cell R55Cell4 = row.createCell(4);
					if (record.getR55OtherInstallmentLoans() != null) {
						R55Cell4.setCellValue(record.getR55OtherInstallmentLoans().doubleValue());
						R55Cell4.setCellStyle(numberStyle);
					} else {
						R55Cell4.setCellValue("");
						R55Cell4.setCellStyle(textStyle);
					}

					// --- R56 (Row Index 55) ---
					row = sheet.getRow(55);
					Cell R56Cell1 = row.createCell(1);
					if (record1.getR56FactoringDebtors() != null) {
						R56Cell1.setCellValue(record1.getR56FactoringDebtors().doubleValue());
						R56Cell1.setCellStyle(numberStyle);
					} else {
						R56Cell1.setCellValue("");
						R56Cell1.setCellStyle(textStyle);
					}
					Cell R56Cell2 = row.createCell(2);
					if (record1.getR56Leasing() != null) {
						R56Cell2.setCellValue(record1.getR56Leasing().doubleValue());
						R56Cell2.setCellStyle(numberStyle);
					} else {
						R56Cell2.setCellValue("");
						R56Cell2.setCellStyle(textStyle);
					}
					Cell R56Cell3 = row.createCell(3);
					if (record.getR56Overdrafts() != null) {
						R56Cell3.setCellValue(record.getR56Overdrafts().doubleValue());
						R56Cell3.setCellStyle(numberStyle);
					} else {
						R56Cell3.setCellValue("");
						R56Cell3.setCellStyle(textStyle);
					}
					Cell R56Cell4 = row.createCell(4);
					if (record.getR56OtherInstallmentLoans() != null) {
						R56Cell4.setCellValue(record.getR56OtherInstallmentLoans().doubleValue());
						R56Cell4.setCellStyle(numberStyle);
					} else {
						R56Cell4.setCellValue("");
						R56Cell4.setCellStyle(textStyle);
					}
					// --- R58 (Row Index 57) ---
					row = sheet.getRow(57);
					Cell R58Cell1 = row.createCell(1);
					if (record1.getR58FactoringDebtors() != null) {
						R58Cell1.setCellValue(record1.getR58FactoringDebtors().doubleValue());
						R58Cell1.setCellStyle(numberStyle);
					} else {
						R58Cell1.setCellValue("");
						R58Cell1.setCellStyle(textStyle);
					}
					Cell R58Cell2 = row.createCell(2);
					if (record1.getR58Leasing() != null) {
						R58Cell2.setCellValue(record1.getR58Leasing().doubleValue());
						R58Cell2.setCellStyle(numberStyle);
					} else {
						R58Cell2.setCellValue("");
						R58Cell2.setCellStyle(textStyle);
					}
					Cell R58Cell3 = row.createCell(3);
					if (record.getR58Overdrafts() != null) {
						R58Cell3.setCellValue(record.getR58Overdrafts().doubleValue());
						R58Cell3.setCellStyle(numberStyle);
					} else {
						R58Cell3.setCellValue("");
						R58Cell3.setCellStyle(textStyle);
					}
					Cell R58Cell4 = row.createCell(4);
					if (record.getR58OtherInstallmentLoans() != null) {
						R58Cell4.setCellValue(record.getR58OtherInstallmentLoans().doubleValue());
						R58Cell4.setCellStyle(numberStyle);
					} else {
						R58Cell4.setCellValue("");
						R58Cell4.setCellStyle(textStyle);
					}

					// --- R59 (Row Index 58) ---
					row = sheet.getRow(58);
					Cell R59Cell1 = row.createCell(1);
					if (record1.getR59FactoringDebtors() != null) {
						R59Cell1.setCellValue(record1.getR59FactoringDebtors().doubleValue());
						R59Cell1.setCellStyle(numberStyle);
					} else {
						R59Cell1.setCellValue("");
						R59Cell1.setCellStyle(textStyle);
					}
					Cell R59Cell2 = row.createCell(2);
					if (record1.getR59Leasing() != null) {
						R59Cell2.setCellValue(record1.getR59Leasing().doubleValue());
						R59Cell2.setCellStyle(numberStyle);
					} else {
						R59Cell2.setCellValue("");
						R59Cell2.setCellStyle(textStyle);
					}
					Cell R59Cell3 = row.createCell(3);
					if (record.getR59Overdrafts() != null) {
						R59Cell3.setCellValue(record.getR59Overdrafts().doubleValue());
						R59Cell3.setCellStyle(numberStyle);
					} else {
						R59Cell3.setCellValue("");
						R59Cell3.setCellStyle(textStyle);
					}
					Cell R59Cell4 = row.createCell(4);
					if (record.getR59OtherInstallmentLoans() != null) {
						R59Cell4.setCellValue(record.getR59OtherInstallmentLoans().doubleValue());
						R59Cell4.setCellStyle(numberStyle);
					} else {
						R59Cell4.setCellValue("");
						R59Cell4.setCellStyle(textStyle);
					}

					// --- R60 (Row Index 59) ---
					row = sheet.getRow(59);
					Cell R60Cell1 = row.createCell(1);
					if (record1.getR60FactoringDebtors() != null) {
						R60Cell1.setCellValue(record1.getR60FactoringDebtors().doubleValue());
						R60Cell1.setCellStyle(numberStyle);
					} else {
						R60Cell1.setCellValue("");
						R60Cell1.setCellStyle(textStyle);
					}
					Cell R60Cell2 = row.createCell(2);
					if (record1.getR60Leasing() != null) {
						R60Cell2.setCellValue(record1.getR60Leasing().doubleValue());
						R60Cell2.setCellStyle(numberStyle);
					} else {
						R60Cell2.setCellValue("");
						R60Cell2.setCellStyle(textStyle);
					}
					Cell R60Cell3 = row.createCell(3);
					if (record.getR60Overdrafts() != null) {
						R60Cell3.setCellValue(record.getR60Overdrafts().doubleValue());
						R60Cell3.setCellStyle(numberStyle);
					} else {
						R60Cell3.setCellValue("");
						R60Cell3.setCellStyle(textStyle);
					}
					Cell R60Cell4 = row.createCell(4);
					if (record.getR60OtherInstallmentLoans() != null) {
						R60Cell4.setCellValue(record.getR60OtherInstallmentLoans().doubleValue());
						R60Cell4.setCellStyle(numberStyle);
					} else {
						R60Cell4.setCellValue("");
						R60Cell4.setCellStyle(textStyle);
					}

					// --- R61 (Row Index 60) ---
					row = sheet.getRow(60);
					Cell R61Cell1 = row.createCell(1);
					if (record1.getR61FactoringDebtors() != null) {
						R61Cell1.setCellValue(record1.getR61FactoringDebtors().doubleValue());
						R61Cell1.setCellStyle(numberStyle);
					} else {
						R61Cell1.setCellValue("");
						R61Cell1.setCellStyle(textStyle);
					}
					Cell R61Cell2 = row.createCell(2);
					if (record1.getR61Leasing() != null) {
						R61Cell2.setCellValue(record1.getR61Leasing().doubleValue());
						R61Cell2.setCellStyle(numberStyle);
					} else {
						R61Cell2.setCellValue("");
						R61Cell2.setCellStyle(textStyle);
					}
					Cell R61Cell3 = row.createCell(3);
					if (record.getR61Overdrafts() != null) {
						R61Cell3.setCellValue(record.getR61Overdrafts().doubleValue());
						R61Cell3.setCellStyle(numberStyle);
					} else {
						R61Cell3.setCellValue("");
						R61Cell3.setCellStyle(textStyle);
					}
					Cell R61Cell4 = row.createCell(4);
					if (record.getR61OtherInstallmentLoans() != null) {
						R61Cell4.setCellValue(record.getR61OtherInstallmentLoans().doubleValue());
						R61Cell4.setCellStyle(numberStyle);
					} else {
						R61Cell4.setCellValue("");
						R61Cell4.setCellStyle(textStyle);
					}

					// --- R62 (Row Index 61) ---
					row = sheet.getRow(61);
					Cell R62Cell1 = row.createCell(1);
					if (record1.getR62FactoringDebtors() != null) {
						R62Cell1.setCellValue(record1.getR62FactoringDebtors().doubleValue());
						R62Cell1.setCellStyle(numberStyle);
					} else {
						R62Cell1.setCellValue("");
						R62Cell1.setCellStyle(textStyle);
					}
					Cell R62Cell2 = row.createCell(2);
					if (record1.getR62Leasing() != null) {
						R62Cell2.setCellValue(record1.getR62Leasing().doubleValue());
						R62Cell2.setCellStyle(numberStyle);
					} else {
						R62Cell2.setCellValue("");
						R62Cell2.setCellStyle(textStyle);
					}
					Cell R62Cell3 = row.createCell(3);
					if (record.getR62Overdrafts() != null) {
						R62Cell3.setCellValue(record.getR62Overdrafts().doubleValue());
						R62Cell3.setCellStyle(numberStyle);
					} else {
						R62Cell3.setCellValue("");
						R62Cell3.setCellStyle(textStyle);
					}
					Cell R62Cell4 = row.createCell(4);
					if (record.getR62OtherInstallmentLoans() != null) {
						R62Cell4.setCellValue(record.getR62OtherInstallmentLoans().doubleValue());
						R62Cell4.setCellStyle(numberStyle);
					} else {
						R62Cell4.setCellValue("");
						R62Cell4.setCellStyle(textStyle);
					}

					// --- R63 (Row Index 62) ---
					row = sheet.getRow(62);
					Cell R63Cell1 = row.createCell(1);
					if (record1.getR63FactoringDebtors() != null) {
						R63Cell1.setCellValue(record1.getR63FactoringDebtors().doubleValue());
						R63Cell1.setCellStyle(numberStyle);
					} else {
						R63Cell1.setCellValue("");
						R63Cell1.setCellStyle(textStyle);
					}
					Cell R63Cell2 = row.createCell(2);
					if (record1.getR63Leasing() != null) {
						R63Cell2.setCellValue(record1.getR63Leasing().doubleValue());
						R63Cell2.setCellStyle(numberStyle);
					} else {
						R63Cell2.setCellValue("");
						R63Cell2.setCellStyle(textStyle);
					}
					Cell R63Cell3 = row.createCell(3);
					if (record.getR63Overdrafts() != null) {
						R63Cell3.setCellValue(record.getR63Overdrafts().doubleValue());
						R63Cell3.setCellStyle(numberStyle);
					} else {
						R63Cell3.setCellValue("");
						R63Cell3.setCellStyle(textStyle);
					}
					Cell R63Cell4 = row.createCell(4);
					if (record.getR63OtherInstallmentLoans() != null) {
						R63Cell4.setCellValue(record.getR63OtherInstallmentLoans().doubleValue());
						R63Cell4.setCellStyle(numberStyle);
					} else {
						R63Cell4.setCellValue("");
						R63Cell4.setCellStyle(textStyle);
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
	public byte[] BRRS_M_LA4EmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_LA4_Archival_Summary_Entity> dataList = M_LA4_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_LA4_Archival_Summary_Entity2> dataList1 = M_LA4_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_LA4 report. Returning empty result.");
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

				int startRow = 11;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_LA4_Archival_Summary_Entity record = dataList.get(i);
						M_LA4_Archival_Summary_Entity2 record1 = dataList1.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
//EMAIL

						// R12 Col B
						Cell R12Cell1 = row.createCell(1);
						if (record1.getR12FactoringDebtors() != null) {
							R12Cell1.setCellValue(record1.getR12FactoringDebtors().doubleValue());
							R12Cell1.setCellStyle(numberStyle);
						} else {
							R12Cell1.setCellValue("");
							R12Cell1.setCellStyle(textStyle);
						}

						// R12 Col C
						Cell R12Cell2 = row.createCell(2);
						if (record1.getR12Leasing() != null) {
							R12Cell2.setCellValue(record1.getR12Leasing().doubleValue());
							R12Cell2.setCellStyle(numberStyle);
						} else {
							R12Cell2.setCellValue("");
							R12Cell2.setCellStyle(textStyle);
						}
						// R12 Col D
						Cell R12Cell3 = row.createCell(3);
						if (record.getR12Overdrafts() != null) {
							R12Cell3.setCellValue(record.getR12Overdrafts().doubleValue());
							R12Cell3.setCellStyle(numberStyle);
						} else {
							R12Cell3.setCellValue("");
							R12Cell3.setCellStyle(textStyle);
						}

						// R12 Col E
						Cell R12Cell4 = row.createCell(4);
						if (record.getR12OtherInstallmentLoans() != null) {
							R12Cell4.setCellValue(record.getR12OtherInstallmentLoans().doubleValue());
							R12Cell4.setCellStyle(numberStyle);
						} else {
							R12Cell4.setCellValue("");
							R12Cell4.setCellStyle(textStyle);
						}
						// R13 Col B
						row = sheet.getRow(12);
						// R13 Col B
						Cell R13Cell1 = row.createCell(1);
						if (record1.getR13FactoringDebtors() != null) {
							R13Cell1.setCellValue(record1.getR13FactoringDebtors().doubleValue());
							R13Cell1.setCellStyle(numberStyle);
						} else {
							R13Cell1.setCellValue("");
							R13Cell1.setCellStyle(textStyle);
						}

						// R13 Col C
						Cell R13Cell2 = row.createCell(2);
						if (record1.getR13Leasing() != null) {
							R13Cell2.setCellValue(record1.getR13Leasing().doubleValue());
							R13Cell2.setCellStyle(numberStyle);
						} else {
							R13Cell2.setCellValue("");
							R13Cell2.setCellStyle(textStyle);
						}
						// R13 Col D
						Cell R13Cell3 = row.createCell(3);
						if (record.getR13Overdrafts() != null) {
							R13Cell3.setCellValue(record.getR13Overdrafts().doubleValue());
							R13Cell3.setCellStyle(numberStyle);
						} else {
							R13Cell3.setCellValue("");
							R13Cell3.setCellStyle(textStyle);
						}

						// R13 Col E
						Cell R13Cell4 = row.createCell(4);
						if (record.getR13OtherInstallmentLoans() != null) {
							R13Cell4.setCellValue(record.getR13OtherInstallmentLoans().doubleValue());
							R13Cell4.setCellStyle(numberStyle);
						} else {
							R13Cell4.setCellValue("");
							R13Cell4.setCellStyle(textStyle);
						}
						// R14 Col B
						row = sheet.getRow(13); // Row index 13 is Excel Row 14
						// R14 Col B
						Cell R14Cell1 = row.createCell(1);
						if (record1.getR14FactoringDebtors() != null) {
							R14Cell1.setCellValue(record1.getR14FactoringDebtors().doubleValue());
							R14Cell1.setCellStyle(numberStyle);
						} else {
							R14Cell1.setCellValue("");
							R14Cell1.setCellStyle(textStyle);
						}

						// R14 Col C
						Cell R14Cell2 = row.createCell(2);
						if (record1.getR14Leasing() != null) {
							R14Cell2.setCellValue(record1.getR14Leasing().doubleValue());
							R14Cell2.setCellStyle(numberStyle);
						} else {
							R14Cell2.setCellValue("");
							R14Cell2.setCellStyle(textStyle);
						}

						// R14 Col D
						Cell R14Cell3 = row.createCell(3);
						if (record.getR14Overdrafts() != null) {
							R14Cell3.setCellValue(record.getR14Overdrafts().doubleValue());
							R14Cell3.setCellStyle(numberStyle);
						} else {
							R14Cell3.setCellValue("");
							R14Cell3.setCellStyle(textStyle);
						}

						// R14 Col E
						Cell R14Cell4 = row.createCell(4);
						if (record.getR14OtherInstallmentLoans() != null) {
							R14Cell4.setCellValue(record.getR14OtherInstallmentLoans().doubleValue());
							R14Cell4.setCellStyle(numberStyle);
						} else {
							R14Cell4.setCellValue("");
							R14Cell4.setCellStyle(textStyle);
						}

						// --- R16 (Row Index 15) ---
						row = sheet.getRow(15);
						Cell R16Cell1 = row.createCell(1);
						if (record1.getR16FactoringDebtors() != null) {
							R16Cell1.setCellValue(record1.getR16FactoringDebtors().doubleValue());
							R16Cell1.setCellStyle(numberStyle);
						} else {
							R16Cell1.setCellValue("");
							R16Cell1.setCellStyle(textStyle);
						}
						Cell R16Cell2 = row.createCell(2);
						if (record1.getR16Leasing() != null) {
							R16Cell2.setCellValue(record1.getR16Leasing().doubleValue());
							R16Cell2.setCellStyle(numberStyle);
						} else {
							R16Cell2.setCellValue("");
							R16Cell2.setCellStyle(textStyle);
						}
						Cell R16Cell3 = row.createCell(3);
						if (record.getR16Overdrafts() != null) {
							R16Cell3.setCellValue(record.getR16Overdrafts().doubleValue());
							R16Cell3.setCellStyle(numberStyle);
						} else {
							R16Cell3.setCellValue("");
							R16Cell3.setCellStyle(textStyle);
						}
						Cell R16Cell4 = row.createCell(4);
						if (record.getR16OtherInstallmentLoans() != null) {
							R16Cell4.setCellValue(record.getR16OtherInstallmentLoans().doubleValue());
							R16Cell4.setCellStyle(numberStyle);
						} else {
							R16Cell4.setCellValue("");
							R16Cell4.setCellStyle(textStyle);
						}

						// --- R17 (Row Index 16) ---
						row = sheet.getRow(16);
						Cell R17Cell1 = row.createCell(1);
						if (record1.getR17FactoringDebtors() != null) {
							R17Cell1.setCellValue(record1.getR17FactoringDebtors().doubleValue());
							R17Cell1.setCellStyle(numberStyle);
						} else {
							R17Cell1.setCellValue("");
							R17Cell1.setCellStyle(textStyle);
						}
						Cell R17Cell2 = row.createCell(2);
						if (record1.getR17Leasing() != null) {
							R17Cell2.setCellValue(record1.getR17Leasing().doubleValue());
							R17Cell2.setCellStyle(numberStyle);
						} else {
							R17Cell2.setCellValue("");
							R17Cell2.setCellStyle(textStyle);
						}
						Cell R17Cell3 = row.createCell(3);
						if (record.getR17Overdrafts() != null) {
							R17Cell3.setCellValue(record.getR17Overdrafts().doubleValue());
							R17Cell3.setCellStyle(numberStyle);
						} else {
							R17Cell3.setCellValue("");
							R17Cell3.setCellStyle(textStyle);
						}
						Cell R17Cell4 = row.createCell(4);
						if (record.getR17OtherInstallmentLoans() != null) {
							R17Cell4.setCellValue(record.getR17OtherInstallmentLoans().doubleValue());
							R17Cell4.setCellStyle(numberStyle);
						} else {
							R17Cell4.setCellValue("");
							R17Cell4.setCellStyle(textStyle);
						}

						// --- R18 (Row Index 17) ---
						row = sheet.getRow(17);
						Cell R18Cell1 = row.createCell(1);
						if (record1.getR18FactoringDebtors() != null) {
							R18Cell1.setCellValue(record1.getR18FactoringDebtors().doubleValue());
							R18Cell1.setCellStyle(numberStyle);
						} else {
							R18Cell1.setCellValue("");
							R18Cell1.setCellStyle(textStyle);
						}
						Cell R18Cell2 = row.createCell(2);
						if (record1.getR18Leasing() != null) {
							R18Cell2.setCellValue(record1.getR18Leasing().doubleValue());
							R18Cell2.setCellStyle(numberStyle);
						} else {
							R18Cell2.setCellValue("");
							R18Cell2.setCellStyle(textStyle);
						}
						Cell R18Cell3 = row.createCell(3);
						if (record.getR18Overdrafts() != null) {
							R18Cell3.setCellValue(record.getR18Overdrafts().doubleValue());
							R18Cell3.setCellStyle(numberStyle);
						} else {
							R18Cell3.setCellValue("");
							R18Cell3.setCellStyle(textStyle);
						}
						Cell R18Cell4 = row.createCell(4);
						if (record.getR18OtherInstallmentLoans() != null) {
							R18Cell4.setCellValue(record.getR18OtherInstallmentLoans().doubleValue());
							R18Cell4.setCellStyle(numberStyle);
						} else {
							R18Cell4.setCellValue("");
							R18Cell4.setCellStyle(textStyle);
						}

						// --- R19 (Row Index 18) ---
						row = sheet.getRow(18);
						Cell R19Cell1 = row.createCell(1);
						if (record1.getR19FactoringDebtors() != null) {
							R19Cell1.setCellValue(record1.getR19FactoringDebtors().doubleValue());
							R19Cell1.setCellStyle(numberStyle);
						} else {
							R19Cell1.setCellValue("");
							R19Cell1.setCellStyle(textStyle);
						}
						Cell R19Cell2 = row.createCell(2);
						if (record1.getR19Leasing() != null) {
							R19Cell2.setCellValue(record1.getR19Leasing().doubleValue());
							R19Cell2.setCellStyle(numberStyle);
						} else {
							R19Cell2.setCellValue("");
							R19Cell2.setCellStyle(textStyle);
						}
						Cell R19Cell3 = row.createCell(3);
						if (record.getR19Overdrafts() != null) {
							R19Cell3.setCellValue(record.getR19Overdrafts().doubleValue());
							R19Cell3.setCellStyle(numberStyle);
						} else {
							R19Cell3.setCellValue("");
							R19Cell3.setCellStyle(textStyle);
						}
						Cell R19Cell4 = row.createCell(4);
						if (record.getR19OtherInstallmentLoans() != null) {
							R19Cell4.setCellValue(record.getR19OtherInstallmentLoans().doubleValue());
							R19Cell4.setCellStyle(numberStyle);
						} else {
							R19Cell4.setCellValue("");
							R19Cell4.setCellStyle(textStyle);
						}
						// --- R20 (Row Index 19) ---
						row = sheet.getRow(19);
						Cell R20Cell1 = row.createCell(1);
						if (record1.getR20FactoringDebtors() != null) {
							R20Cell1.setCellValue(record1.getR20FactoringDebtors().doubleValue());
							R20Cell1.setCellStyle(numberStyle);
						} else {
							R20Cell1.setCellValue("");
							R20Cell1.setCellStyle(textStyle);
						}
						Cell R20Cell2 = row.createCell(2);
						if (record1.getR20Leasing() != null) {
							R20Cell2.setCellValue(record1.getR20Leasing().doubleValue());
							R20Cell2.setCellStyle(numberStyle);
						} else {
							R20Cell2.setCellValue("");
							R20Cell2.setCellStyle(textStyle);
						}
						Cell R20Cell3 = row.createCell(3);
						if (record.getR20Overdrafts() != null) {
							R20Cell3.setCellValue(record.getR20Overdrafts().doubleValue());
							R20Cell3.setCellStyle(numberStyle);
						} else {
							R20Cell3.setCellValue("");
							R20Cell3.setCellStyle(textStyle);
						}
						Cell R20Cell4 = row.createCell(4);
						if (record.getR20OtherInstallmentLoans() != null) {
							R20Cell4.setCellValue(record.getR20OtherInstallmentLoans().doubleValue());
							R20Cell4.setCellStyle(numberStyle);
						} else {
							R20Cell4.setCellValue("");
							R20Cell4.setCellStyle(textStyle);
						}

						// --- R21 (Row Index 20) ---
						row = sheet.getRow(20);
						Cell R21Cell1 = row.createCell(1);
						if (record1.getR21FactoringDebtors() != null) {
							R21Cell1.setCellValue(record1.getR21FactoringDebtors().doubleValue());
							R21Cell1.setCellStyle(numberStyle);
						} else {
							R21Cell1.setCellValue("");
							R21Cell1.setCellStyle(textStyle);
						}
						Cell R21Cell2 = row.createCell(2);
						if (record1.getR21Leasing() != null) {
							R21Cell2.setCellValue(record1.getR21Leasing().doubleValue());
							R21Cell2.setCellStyle(numberStyle);
						} else {
							R21Cell2.setCellValue("");
							R21Cell2.setCellStyle(textStyle);
						}
						Cell R21Cell3 = row.createCell(3);
						if (record.getR21Overdrafts() != null) {
							R21Cell3.setCellValue(record.getR21Overdrafts().doubleValue());
							R21Cell3.setCellStyle(numberStyle);
						} else {
							R21Cell3.setCellValue("");
							R21Cell3.setCellStyle(textStyle);
						}
						Cell R21Cell4 = row.createCell(4);
						if (record.getR21OtherInstallmentLoans() != null) {
							R21Cell4.setCellValue(record.getR21OtherInstallmentLoans().doubleValue());
							R21Cell4.setCellStyle(numberStyle);
						} else {
							R21Cell4.setCellValue("");
							R21Cell4.setCellStyle(textStyle);
						}

						// --- R22 (Row Index 21) ---
						row = sheet.getRow(21);
						Cell R22Cell1 = row.createCell(1);
						if (record1.getR22FactoringDebtors() != null) {
							R22Cell1.setCellValue(record1.getR22FactoringDebtors().doubleValue());
							R22Cell1.setCellStyle(numberStyle);
						} else {
							R22Cell1.setCellValue("");
							R22Cell1.setCellStyle(textStyle);
						}
						Cell R22Cell2 = row.createCell(2);
						if (record1.getR22Leasing() != null) {
							R22Cell2.setCellValue(record1.getR22Leasing().doubleValue());
							R22Cell2.setCellStyle(numberStyle);
						} else {
							R22Cell2.setCellValue("");
							R22Cell2.setCellStyle(textStyle);
						}
						Cell R22Cell3 = row.createCell(3);
						if (record.getR22Overdrafts() != null) {
							R22Cell3.setCellValue(record.getR22Overdrafts().doubleValue());
							R22Cell3.setCellStyle(numberStyle);
						} else {
							R22Cell3.setCellValue("");
							R22Cell3.setCellStyle(textStyle);
						}
						Cell R22Cell4 = row.createCell(4);
						if (record.getR22OtherInstallmentLoans() != null) {
							R22Cell4.setCellValue(record.getR22OtherInstallmentLoans().doubleValue());
							R22Cell4.setCellStyle(numberStyle);
						} else {
							R22Cell4.setCellValue("");
							R22Cell4.setCellStyle(textStyle);
						}

						// --- R23 (Row Index 22) ---
						row = sheet.getRow(22);
						Cell R23Cell1 = row.createCell(1);
						if (record1.getR23FactoringDebtors() != null) {
							R23Cell1.setCellValue(record1.getR23FactoringDebtors().doubleValue());
							R23Cell1.setCellStyle(numberStyle);
						} else {
							R23Cell1.setCellValue("");
							R23Cell1.setCellStyle(textStyle);
						}
						Cell R23Cell2 = row.createCell(2);
						if (record1.getR23Leasing() != null) {
							R23Cell2.setCellValue(record1.getR23Leasing().doubleValue());
							R23Cell2.setCellStyle(numberStyle);
						} else {
							R23Cell2.setCellValue("");
							R23Cell2.setCellStyle(textStyle);
						}
						Cell R23Cell3 = row.createCell(3);
						if (record.getR23Overdrafts() != null) {
							R23Cell3.setCellValue(record.getR23Overdrafts().doubleValue());
							R23Cell3.setCellStyle(numberStyle);
						} else {
							R23Cell3.setCellValue("");
							R23Cell3.setCellStyle(textStyle);
						}
						Cell R23Cell4 = row.createCell(4);
						if (record.getR23OtherInstallmentLoans() != null) {
							R23Cell4.setCellValue(record.getR23OtherInstallmentLoans().doubleValue());
							R23Cell4.setCellStyle(numberStyle);
						} else {
							R23Cell4.setCellValue("");
							R23Cell4.setCellStyle(textStyle);
						}

						// --- R24 (Row Index 23) ---
						row = sheet.getRow(23);
						Cell R24Cell1 = row.createCell(1);
						if (record1.getR24FactoringDebtors() != null) {
							R24Cell1.setCellValue(record1.getR24FactoringDebtors().doubleValue());
							R24Cell1.setCellStyle(numberStyle);
						} else {
							R24Cell1.setCellValue("");
							R24Cell1.setCellStyle(textStyle);
						}
						Cell R24Cell2 = row.createCell(2);
						if (record1.getR24Leasing() != null) {
							R24Cell2.setCellValue(record1.getR24Leasing().doubleValue());
							R24Cell2.setCellStyle(numberStyle);
						} else {
							R24Cell2.setCellValue("");
							R24Cell2.setCellStyle(textStyle);
						}
						Cell R24Cell3 = row.createCell(3);
						if (record.getR24Overdrafts() != null) {
							R24Cell3.setCellValue(record.getR24Overdrafts().doubleValue());
							R24Cell3.setCellStyle(numberStyle);
						} else {
							R24Cell3.setCellValue("");
							R24Cell3.setCellStyle(textStyle);
						}
						Cell R24Cell4 = row.createCell(4);
						if (record.getR24OtherInstallmentLoans() != null) {
							R24Cell4.setCellValue(record.getR24OtherInstallmentLoans().doubleValue());
							R24Cell4.setCellStyle(numberStyle);
						} else {
							R24Cell4.setCellValue("");
							R24Cell4.setCellStyle(textStyle);
						}

						// --- R25 (Row Index 24) ---
						row = sheet.getRow(24);
						Cell R25Cell1 = row.createCell(1);
						if (record1.getR25FactoringDebtors() != null) {
							R25Cell1.setCellValue(record1.getR25FactoringDebtors().doubleValue());
							R25Cell1.setCellStyle(numberStyle);
						} else {
							R25Cell1.setCellValue("");
							R25Cell1.setCellStyle(textStyle);
						}
						Cell R25Cell2 = row.createCell(2);
						if (record1.getR25Leasing() != null) {
							R25Cell2.setCellValue(record1.getR25Leasing().doubleValue());
							R25Cell2.setCellStyle(numberStyle);
						} else {
							R25Cell2.setCellValue("");
							R25Cell2.setCellStyle(textStyle);
						}
						Cell R25Cell3 = row.createCell(3);
						if (record.getR25Overdrafts() != null) {
							R25Cell3.setCellValue(record.getR25Overdrafts().doubleValue());
							R25Cell3.setCellStyle(numberStyle);
						} else {
							R25Cell3.setCellValue("");
							R25Cell3.setCellStyle(textStyle);
						}
						Cell R25Cell4 = row.createCell(4);
						if (record.getR25OtherInstallmentLoans() != null) {
							R25Cell4.setCellValue(record.getR25OtherInstallmentLoans().doubleValue());
							R25Cell4.setCellStyle(numberStyle);
						} else {
							R25Cell4.setCellValue("");
							R25Cell4.setCellStyle(textStyle);
						}

						// --- R26 (Row Index 25) ---
						row = sheet.getRow(25);
						Cell R26Cell1 = row.createCell(1);
						if (record1.getR26FactoringDebtors() != null) {
							R26Cell1.setCellValue(record1.getR26FactoringDebtors().doubleValue());
							R26Cell1.setCellStyle(numberStyle);
						} else {
							R26Cell1.setCellValue("");
							R26Cell1.setCellStyle(textStyle);
						}
						Cell R26Cell2 = row.createCell(2);
						if (record1.getR26Leasing() != null) {
							R26Cell2.setCellValue(record1.getR26Leasing().doubleValue());
							R26Cell2.setCellStyle(numberStyle);
						} else {
							R26Cell2.setCellValue("");
							R26Cell2.setCellStyle(textStyle);
						}
						Cell R26Cell3 = row.createCell(3);
						if (record.getR26Overdrafts() != null) {
							R26Cell3.setCellValue(record.getR26Overdrafts().doubleValue());
							R26Cell3.setCellStyle(numberStyle);
						} else {
							R26Cell3.setCellValue("");
							R26Cell3.setCellStyle(textStyle);
						}
						Cell R26Cell4 = row.createCell(4);
						if (record.getR26OtherInstallmentLoans() != null) {
							R26Cell4.setCellValue(record.getR26OtherInstallmentLoans().doubleValue());
							R26Cell4.setCellStyle(numberStyle);
						} else {
							R26Cell4.setCellValue("");
							R26Cell4.setCellStyle(textStyle);
						}

						// --- R27 (Row Index 26) ---
						row = sheet.getRow(26);
						Cell R27Cell1 = row.createCell(1);
						if (record1.getR27FactoringDebtors() != null) {
							R27Cell1.setCellValue(record1.getR27FactoringDebtors().doubleValue());
							R27Cell1.setCellStyle(numberStyle);
						} else {
							R27Cell1.setCellValue("");
							R27Cell1.setCellStyle(textStyle);
						}
						Cell R27Cell2 = row.createCell(2);
						if (record1.getR27Leasing() != null) {
							R27Cell2.setCellValue(record1.getR27Leasing().doubleValue());
							R27Cell2.setCellStyle(numberStyle);
						} else {
							R27Cell2.setCellValue("");
							R27Cell2.setCellStyle(textStyle);
						}
						Cell R27Cell3 = row.createCell(3);
						if (record.getR27Overdrafts() != null) {
							R27Cell3.setCellValue(record.getR27Overdrafts().doubleValue());
							R27Cell3.setCellStyle(numberStyle);
						} else {
							R27Cell3.setCellValue("");
							R27Cell3.setCellStyle(textStyle);
						}
						Cell R27Cell4 = row.createCell(4);
						if (record.getR27OtherInstallmentLoans() != null) {
							R27Cell4.setCellValue(record.getR27OtherInstallmentLoans().doubleValue());
							R27Cell4.setCellStyle(numberStyle);
						} else {
							R27Cell4.setCellValue("");
							R27Cell4.setCellStyle(textStyle);
						}

						// --- R28 (Row Index 27) ---
						row = sheet.getRow(27);
						Cell R28Cell1 = row.createCell(1);
						if (record1.getR28FactoringDebtors() != null) {
							R28Cell1.setCellValue(record1.getR28FactoringDebtors().doubleValue());
							R28Cell1.setCellStyle(numberStyle);
						} else {
							R28Cell1.setCellValue("");
							R28Cell1.setCellStyle(textStyle);
						}
						Cell R28Cell2 = row.createCell(2);
						if (record1.getR28Leasing() != null) {
							R28Cell2.setCellValue(record1.getR28Leasing().doubleValue());
							R28Cell2.setCellStyle(numberStyle);
						} else {
							R28Cell2.setCellValue("");
							R28Cell2.setCellStyle(textStyle);
						}
						Cell R28Cell3 = row.createCell(3);
						if (record.getR28Overdrafts() != null) {
							R28Cell3.setCellValue(record.getR28Overdrafts().doubleValue());
							R28Cell3.setCellStyle(numberStyle);
						} else {
							R28Cell3.setCellValue("");
							R28Cell3.setCellStyle(textStyle);
						}
						Cell R28Cell4 = row.createCell(4);
						if (record.getR28OtherInstallmentLoans() != null) {
							R28Cell4.setCellValue(record.getR28OtherInstallmentLoans().doubleValue());
							R28Cell4.setCellStyle(numberStyle);
						} else {
							R28Cell4.setCellValue("");
							R28Cell4.setCellStyle(textStyle);
						}

						// --- R30 (Row Index 29) ---
						row = sheet.getRow(29);
						Cell R30Cell1 = row.createCell(1);
						if (record1.getR30FactoringDebtors() != null) {
							R30Cell1.setCellValue(record1.getR30FactoringDebtors().doubleValue());
							R30Cell1.setCellStyle(numberStyle);
						} else {
							R30Cell1.setCellValue("");
							R30Cell1.setCellStyle(textStyle);
						}
						Cell R30Cell2 = row.createCell(2);
						if (record1.getR30Leasing() != null) {
							R30Cell2.setCellValue(record1.getR30Leasing().doubleValue());
							R30Cell2.setCellStyle(numberStyle);
						} else {
							R30Cell2.setCellValue("");
							R30Cell2.setCellStyle(textStyle);
						}
						Cell R30Cell3 = row.createCell(3);
						if (record.getR30Overdrafts() != null) {
							R30Cell3.setCellValue(record.getR30Overdrafts().doubleValue());
							R30Cell3.setCellStyle(numberStyle);
						} else {
							R30Cell3.setCellValue("");
							R30Cell3.setCellStyle(textStyle);
						}
						Cell R30Cell4 = row.createCell(4);
						if (record.getR30OtherInstallmentLoans() != null) {
							R30Cell4.setCellValue(record.getR30OtherInstallmentLoans().doubleValue());
							R30Cell4.setCellStyle(numberStyle);
						} else {
							R30Cell4.setCellValue("");
							R30Cell4.setCellStyle(textStyle);
						}

						// --- R31 (Row Index 30) ---
						row = sheet.getRow(30);
						Cell R31Cell1 = row.createCell(1);
						if (record1.getR31FactoringDebtors() != null) {
							R31Cell1.setCellValue(record1.getR31FactoringDebtors().doubleValue());
							R31Cell1.setCellStyle(numberStyle);
						} else {
							R31Cell1.setCellValue("");
							R31Cell1.setCellStyle(textStyle);
						}
						Cell R31Cell2 = row.createCell(2);
						if (record1.getR31Leasing() != null) {
							R31Cell2.setCellValue(record1.getR31Leasing().doubleValue());
							R31Cell2.setCellStyle(numberStyle);
						} else {
							R31Cell2.setCellValue("");
							R31Cell2.setCellStyle(textStyle);
						}
						Cell R31Cell3 = row.createCell(3);
						if (record.getR31Overdrafts() != null) {
							R31Cell3.setCellValue(record.getR31Overdrafts().doubleValue());
							R31Cell3.setCellStyle(numberStyle);
						} else {
							R31Cell3.setCellValue("");
							R31Cell3.setCellStyle(textStyle);
						}
						Cell R31Cell4 = row.createCell(4);
						if (record.getR31OtherInstallmentLoans() != null) {
							R31Cell4.setCellValue(record.getR31OtherInstallmentLoans().doubleValue());
							R31Cell4.setCellStyle(numberStyle);
						} else {
							R31Cell4.setCellValue("");
							R31Cell4.setCellStyle(textStyle);
						}

						// --- R32 (Row Index 31) ---
						row = sheet.getRow(31);
						Cell R32Cell1 = row.createCell(1);
						if (record1.getR32FactoringDebtors() != null) {
							R32Cell1.setCellValue(record1.getR32FactoringDebtors().doubleValue());
							R32Cell1.setCellStyle(numberStyle);
						} else {
							R32Cell1.setCellValue("");
							R32Cell1.setCellStyle(textStyle);
						}
						Cell R32Cell2 = row.createCell(2);
						if (record1.getR32Leasing() != null) {
							R32Cell2.setCellValue(record1.getR32Leasing().doubleValue());
							R32Cell2.setCellStyle(numberStyle);
						} else {
							R32Cell2.setCellValue("");
							R32Cell2.setCellStyle(textStyle);
						}
						Cell R32Cell3 = row.createCell(3);
						if (record.getR32Overdrafts() != null) {
							R32Cell3.setCellValue(record.getR32Overdrafts().doubleValue());
							R32Cell3.setCellStyle(numberStyle);
						} else {
							R32Cell3.setCellValue("");
							R32Cell3.setCellStyle(textStyle);
						}
						Cell R32Cell4 = row.createCell(4);
						if (record.getR32OtherInstallmentLoans() != null) {
							R32Cell4.setCellValue(record.getR32OtherInstallmentLoans().doubleValue());
							R32Cell4.setCellStyle(numberStyle);
						} else {
							R32Cell4.setCellValue("");
							R32Cell4.setCellStyle(textStyle);
						}

						// --- R33 (Row Index 32) ---
						row = sheet.getRow(32);
						Cell R33Cell1 = row.createCell(1);
						if (record1.getR33FactoringDebtors() != null) {
							R33Cell1.setCellValue(record1.getR33FactoringDebtors().doubleValue());
							R33Cell1.setCellStyle(numberStyle);
						} else {
							R33Cell1.setCellValue("");
							R33Cell1.setCellStyle(textStyle);
						}
						Cell R33Cell2 = row.createCell(2);
						if (record1.getR33Leasing() != null) {
							R33Cell2.setCellValue(record1.getR33Leasing().doubleValue());
							R33Cell2.setCellStyle(numberStyle);
						} else {
							R33Cell2.setCellValue("");
							R33Cell2.setCellStyle(textStyle);
						}
						Cell R33Cell3 = row.createCell(3);
						if (record.getR33Overdrafts() != null) {
							R33Cell3.setCellValue(record.getR33Overdrafts().doubleValue());
							R33Cell3.setCellStyle(numberStyle);
						} else {
							R33Cell3.setCellValue("");
							R33Cell3.setCellStyle(textStyle);
						}
						Cell R33Cell4 = row.createCell(4);
						if (record.getR33OtherInstallmentLoans() != null) {
							R33Cell4.setCellValue(record.getR33OtherInstallmentLoans().doubleValue());
							R33Cell4.setCellStyle(numberStyle);
						} else {
							R33Cell4.setCellValue("");
							R33Cell4.setCellStyle(textStyle);
						}

						// --- R34 (Row Index 33) ---
						row = sheet.getRow(33);
						Cell R34Cell1 = row.createCell(1);
						if (record1.getR34FactoringDebtors() != null) {
							R34Cell1.setCellValue(record1.getR34FactoringDebtors().doubleValue());
							R34Cell1.setCellStyle(numberStyle);
						} else {
							R34Cell1.setCellValue("");
							R34Cell1.setCellStyle(textStyle);
						}
						Cell R34Cell2 = row.createCell(2);
						if (record1.getR34Leasing() != null) {
							R34Cell2.setCellValue(record1.getR34Leasing().doubleValue());
							R34Cell2.setCellStyle(numberStyle);
						} else {
							R34Cell2.setCellValue("");
							R34Cell2.setCellStyle(textStyle);
						}
						Cell R34Cell3 = row.createCell(3);
						if (record.getR34Overdrafts() != null) {
							R34Cell3.setCellValue(record.getR34Overdrafts().doubleValue());
							R34Cell3.setCellStyle(numberStyle);
						} else {
							R34Cell3.setCellValue("");
							R34Cell3.setCellStyle(textStyle);
						}
						Cell R34Cell4 = row.createCell(4);
						if (record.getR34OtherInstallmentLoans() != null) {
							R34Cell4.setCellValue(record.getR34OtherInstallmentLoans().doubleValue());
							R34Cell4.setCellStyle(numberStyle);
						} else {
							R34Cell4.setCellValue("");
							R34Cell4.setCellStyle(textStyle);
						}
						Cell R34Cell5 = row.createCell(5);
						if (record.getR34Total() != null) {
							R34Cell5.setCellValue(record.getR34Total().doubleValue());
							R34Cell5.setCellStyle(numberStyle);
						} else {
							R34Cell5.setCellValue("");
							R34Cell5.setCellStyle(textStyle);
						}
						// --- R35 (Row Index 34) ---
						row = sheet.getRow(34);
						Cell R35Cell1 = row.createCell(1);
						if (record1.getR35FactoringDebtors() != null) {
							R35Cell1.setCellValue(record1.getR35FactoringDebtors().doubleValue());
							R35Cell1.setCellStyle(numberStyle);
						} else {
							R35Cell1.setCellValue("");
							R35Cell1.setCellStyle(textStyle);
						}
						Cell R35Cell2 = row.createCell(2);
						if (record1.getR35Leasing() != null) {
							R35Cell2.setCellValue(record1.getR35Leasing().doubleValue());
							R35Cell2.setCellStyle(numberStyle);
						} else {
							R35Cell2.setCellValue("");
							R35Cell2.setCellStyle(textStyle);
						}
						Cell R35Cell3 = row.createCell(3);
						if (record.getR35Overdrafts() != null) {
							R35Cell3.setCellValue(record.getR35Overdrafts().doubleValue());
							R35Cell3.setCellStyle(numberStyle);
						} else {
							R35Cell3.setCellValue("");
							R35Cell3.setCellStyle(textStyle);
						}
						Cell R35Cell4 = row.createCell(4);
						if (record.getR35OtherInstallmentLoans() != null) {
							R35Cell4.setCellValue(record.getR35OtherInstallmentLoans().doubleValue());
							R35Cell4.setCellStyle(numberStyle);
						} else {
							R35Cell4.setCellValue("");
							R35Cell4.setCellStyle(textStyle);
						}

						// --- R36 (Row Index 35) ---
						row = sheet.getRow(35);
						Cell R36Cell1 = row.createCell(1);
						if (record1.getR36FactoringDebtors() != null) {
							R36Cell1.setCellValue(record1.getR36FactoringDebtors().doubleValue());
							R36Cell1.setCellStyle(numberStyle);
						} else {
							R36Cell1.setCellValue("");
							R36Cell1.setCellStyle(textStyle);
						}
						Cell R36Cell2 = row.createCell(2);
						if (record1.getR36Leasing() != null) {
							R36Cell2.setCellValue(record1.getR36Leasing().doubleValue());
							R36Cell2.setCellStyle(numberStyle);
						} else {
							R36Cell2.setCellValue("");
							R36Cell2.setCellStyle(textStyle);
						}
						Cell R36Cell3 = row.createCell(3);
						if (record.getR36Overdrafts() != null) {
							R36Cell3.setCellValue(record.getR36Overdrafts().doubleValue());
							R36Cell3.setCellStyle(numberStyle);
						} else {
							R36Cell3.setCellValue("");
							R36Cell3.setCellStyle(textStyle);
						}
						Cell R36Cell4 = row.createCell(4);
						if (record.getR36OtherInstallmentLoans() != null) {
							R36Cell4.setCellValue(record.getR36OtherInstallmentLoans().doubleValue());
							R36Cell4.setCellStyle(numberStyle);
						} else {
							R36Cell4.setCellValue("");
							R36Cell4.setCellStyle(textStyle);
						}

						// --- R37 (Row Index 36) ---
						row = sheet.getRow(36);
						Cell R37Cell1 = row.createCell(1);
						if (record1.getR37FactoringDebtors() != null) {
							R37Cell1.setCellValue(record1.getR37FactoringDebtors().doubleValue());
							R37Cell1.setCellStyle(numberStyle);
						} else {
							R37Cell1.setCellValue("");
							R37Cell1.setCellStyle(textStyle);
						}
						Cell R37Cell2 = row.createCell(2);
						if (record1.getR37Leasing() != null) {
							R37Cell2.setCellValue(record1.getR37Leasing().doubleValue());
							R37Cell2.setCellStyle(numberStyle);
						} else {
							R37Cell2.setCellValue("");
							R37Cell2.setCellStyle(textStyle);
						}
						Cell R37Cell3 = row.createCell(3);
						if (record.getR37Overdrafts() != null) {
							R37Cell3.setCellValue(record.getR37Overdrafts().doubleValue());
							R37Cell3.setCellStyle(numberStyle);
						} else {
							R37Cell3.setCellValue("");
							R37Cell3.setCellStyle(textStyle);
						}
						Cell R37Cell4 = row.createCell(4);
						if (record.getR37OtherInstallmentLoans() != null) {
							R37Cell4.setCellValue(record.getR37OtherInstallmentLoans().doubleValue());
							R37Cell4.setCellStyle(numberStyle);
						} else {
							R37Cell4.setCellValue("");
							R37Cell4.setCellStyle(textStyle);
						}
						// --- R39 (Row Index 38) ---
						row = sheet.getRow(38);
						Cell R39Cell1 = row.createCell(1);
						if (record1.getR39FactoringDebtors() != null) {
							R39Cell1.setCellValue(record1.getR39FactoringDebtors().doubleValue());
							R39Cell1.setCellStyle(numberStyle);
						} else {
							R39Cell1.setCellValue("");
							R39Cell1.setCellStyle(textStyle);
						}
						Cell R39Cell2 = row.createCell(2);
						if (record1.getR39Leasing() != null) {
							R39Cell2.setCellValue(record1.getR39Leasing().doubleValue());
							R39Cell2.setCellStyle(numberStyle);
						} else {
							R39Cell2.setCellValue("");
							R39Cell2.setCellStyle(textStyle);
						}
						Cell R39Cell3 = row.createCell(3);
						if (record.getR39Overdrafts() != null) {
							R39Cell3.setCellValue(record.getR39Overdrafts().doubleValue());
							R39Cell3.setCellStyle(numberStyle);
						} else {
							R39Cell3.setCellValue("");
							R39Cell3.setCellStyle(textStyle);
						}
						Cell R39Cell4 = row.createCell(4);
						if (record.getR39OtherInstallmentLoans() != null) {
							R39Cell4.setCellValue(record.getR39OtherInstallmentLoans().doubleValue());
							R39Cell4.setCellStyle(numberStyle);
						} else {
							R39Cell4.setCellValue("");
							R39Cell4.setCellStyle(textStyle);
						}

						// --- R40 (Row Index 39) ---
						row = sheet.getRow(39);
						Cell R40Cell1 = row.createCell(1);
						if (record1.getR40FactoringDebtors() != null) {
							R40Cell1.setCellValue(record1.getR40FactoringDebtors().doubleValue());
							R40Cell1.setCellStyle(numberStyle);
						} else {
							R40Cell1.setCellValue("");
							R40Cell1.setCellStyle(textStyle);
						}
						Cell R40Cell2 = row.createCell(2);
						if (record1.getR40Leasing() != null) {
							R40Cell2.setCellValue(record1.getR40Leasing().doubleValue());
							R40Cell2.setCellStyle(numberStyle);
						} else {
							R40Cell2.setCellValue("");
							R40Cell2.setCellStyle(textStyle);
						}
						Cell R40Cell3 = row.createCell(3);
						if (record.getR40Overdrafts() != null) {
							R40Cell3.setCellValue(record.getR40Overdrafts().doubleValue());
							R40Cell3.setCellStyle(numberStyle);
						} else {
							R40Cell3.setCellValue("");
							R40Cell3.setCellStyle(textStyle);
						}
						Cell R40Cell4 = row.createCell(4);
						if (record.getR40OtherInstallmentLoans() != null) {
							R40Cell4.setCellValue(record.getR40OtherInstallmentLoans().doubleValue());
							R40Cell4.setCellStyle(numberStyle);
						} else {
							R40Cell4.setCellValue("");
							R40Cell4.setCellStyle(textStyle);
						}
						// --- R42 (Row Index 41) ---
						row = sheet.getRow(41);
						Cell R42Cell1 = row.createCell(1);
						if (record1.getR42FactoringDebtors() != null) {
							R42Cell1.setCellValue(record1.getR42FactoringDebtors().doubleValue());
							R42Cell1.setCellStyle(numberStyle);
						} else {
							R42Cell1.setCellValue("");
							R42Cell1.setCellStyle(textStyle);
						}
						Cell R42Cell2 = row.createCell(2);
						if (record1.getR42Leasing() != null) {
							R42Cell2.setCellValue(record1.getR42Leasing().doubleValue());
							R42Cell2.setCellStyle(numberStyle);
						} else {
							R42Cell2.setCellValue("");
							R42Cell2.setCellStyle(textStyle);
						}
						Cell R42Cell3 = row.createCell(3);
						if (record.getR42Overdrafts() != null) {
							R42Cell3.setCellValue(record.getR42Overdrafts().doubleValue());
							R42Cell3.setCellStyle(numberStyle);
						} else {
							R42Cell3.setCellValue("");
							R42Cell3.setCellStyle(textStyle);
						}
						Cell R42Cell4 = row.createCell(4);
						if (record.getR42OtherInstallmentLoans() != null) {
							R42Cell4.setCellValue(record.getR42OtherInstallmentLoans().doubleValue());
							R42Cell4.setCellStyle(numberStyle);
						} else {
							R42Cell4.setCellValue("");
							R42Cell4.setCellStyle(textStyle);
						}

						// --- R43 (Row Index 42) ---
						row = sheet.getRow(42);
						Cell R43Cell1 = row.createCell(1);
						if (record1.getR43FactoringDebtors() != null) {
							R43Cell1.setCellValue(record1.getR43FactoringDebtors().doubleValue());
							R43Cell1.setCellStyle(numberStyle);
						} else {
							R43Cell1.setCellValue("");
							R43Cell1.setCellStyle(textStyle);
						}
						Cell R43Cell2 = row.createCell(2);
						if (record1.getR43Leasing() != null) {
							R43Cell2.setCellValue(record1.getR43Leasing().doubleValue());
							R43Cell2.setCellStyle(numberStyle);
						} else {
							R43Cell2.setCellValue("");
							R43Cell2.setCellStyle(textStyle);
						}
						Cell R43Cell3 = row.createCell(3);
						if (record.getR43Overdrafts() != null) {
							R43Cell3.setCellValue(record.getR43Overdrafts().doubleValue());
							R43Cell3.setCellStyle(numberStyle);
						} else {
							R43Cell3.setCellValue("");
							R43Cell3.setCellStyle(textStyle);
						}
						Cell R43Cell4 = row.createCell(4);
						if (record.getR43OtherInstallmentLoans() != null) {
							R43Cell4.setCellValue(record.getR43OtherInstallmentLoans().doubleValue());
							R43Cell4.setCellStyle(numberStyle);
						} else {
							R43Cell4.setCellValue("");
							R43Cell4.setCellStyle(textStyle);
						}
						// --- R45 (Row Index 44) ---
						row = sheet.getRow(44);
						Cell R45Cell1 = row.createCell(1);
						if (record1.getR45FactoringDebtors() != null) {
							R45Cell1.setCellValue(record1.getR45FactoringDebtors().doubleValue());
							R45Cell1.setCellStyle(numberStyle);
						} else {
							R45Cell1.setCellValue("");
							R45Cell1.setCellStyle(textStyle);
						}
						Cell R45Cell2 = row.createCell(2);
						if (record1.getR45Leasing() != null) {
							R45Cell2.setCellValue(record1.getR45Leasing().doubleValue());
							R45Cell2.setCellStyle(numberStyle);
						} else {
							R45Cell2.setCellValue("");
							R45Cell2.setCellStyle(textStyle);
						}
						Cell R45Cell3 = row.createCell(3);
						if (record.getR45Overdrafts() != null) {
							R45Cell3.setCellValue(record.getR45Overdrafts().doubleValue());
							R45Cell3.setCellStyle(numberStyle);
						} else {
							R45Cell3.setCellValue("");
							R45Cell3.setCellStyle(textStyle);
						}
						Cell R45Cell4 = row.createCell(4);
						if (record.getR45OtherInstallmentLoans() != null) {
							R45Cell4.setCellValue(record.getR45OtherInstallmentLoans().doubleValue());
							R45Cell4.setCellStyle(numberStyle);
						} else {
							R45Cell4.setCellValue("");
							R45Cell4.setCellStyle(textStyle);
						}

						// --- R46 (Row Index 45) ---
						row = sheet.getRow(45);
						Cell R46Cell1 = row.createCell(1);
						if (record1.getR46FactoringDebtors() != null) {
							R46Cell1.setCellValue(record1.getR46FactoringDebtors().doubleValue());
							R46Cell1.setCellStyle(numberStyle);
						} else {
							R46Cell1.setCellValue("");
							R46Cell1.setCellStyle(textStyle);
						}
						Cell R46Cell2 = row.createCell(2);
						if (record1.getR46Leasing() != null) {
							R46Cell2.setCellValue(record1.getR46Leasing().doubleValue());
							R46Cell2.setCellStyle(numberStyle);
						} else {
							R46Cell2.setCellValue("");
							R46Cell2.setCellStyle(textStyle);
						}
						Cell R46Cell3 = row.createCell(3);
						if (record.getR46Overdrafts() != null) {
							R46Cell3.setCellValue(record.getR46Overdrafts().doubleValue());
							R46Cell3.setCellStyle(numberStyle);
						} else {
							R46Cell3.setCellValue("");
							R46Cell3.setCellStyle(textStyle);
						}
						Cell R46Cell4 = row.createCell(4);
						if (record.getR46OtherInstallmentLoans() != null) {
							R46Cell4.setCellValue(record.getR46OtherInstallmentLoans().doubleValue());
							R46Cell4.setCellStyle(numberStyle);
						} else {
							R46Cell4.setCellValue("");
							R46Cell4.setCellStyle(textStyle);
						}

						// --- R47 (Row Index 46) ---
						row = sheet.getRow(46);
						Cell R48Cell1 = row.createCell(1);
						if (record1.getR48FactoringDebtors() != null) {
							R48Cell1.setCellValue(record1.getR48FactoringDebtors().doubleValue());
							R48Cell1.setCellStyle(numberStyle);
						} else {
							R48Cell1.setCellValue("");
							R48Cell1.setCellStyle(textStyle);
						}
						Cell R48Cell2 = row.createCell(2);
						if (record1.getR48Leasing() != null) {
							R48Cell2.setCellValue(record1.getR48Leasing().doubleValue());
							R48Cell2.setCellStyle(numberStyle);
						} else {
							R48Cell2.setCellValue("");
							R48Cell2.setCellStyle(textStyle);
						}
						Cell R48Cell3 = row.createCell(3);
						if (record.getR48Overdrafts() != null) {
							R48Cell3.setCellValue(record.getR48Overdrafts().doubleValue());
							R48Cell3.setCellStyle(numberStyle);
						} else {
							R48Cell3.setCellValue("");
							R48Cell3.setCellStyle(textStyle);
						}
						Cell R48Cell4 = row.createCell(4);
						if (record.getR48OtherInstallmentLoans() != null) {
							R48Cell4.setCellValue(record.getR48OtherInstallmentLoans().doubleValue());
							R48Cell4.setCellStyle(numberStyle);
						} else {
							R48Cell4.setCellValue("");
							R48Cell4.setCellStyle(textStyle);
						}

						// --- R50 (Row Index 48) ---
						row = sheet.getRow(48);
						Cell R50Cell1 = row.createCell(1);
						if (record1.getR50FactoringDebtors() != null) {
							R50Cell1.setCellValue(record1.getR50FactoringDebtors().doubleValue());
							R50Cell1.setCellStyle(numberStyle);
						} else {
							R50Cell1.setCellValue("");
							R50Cell1.setCellStyle(textStyle);
						}
						Cell R50Cell2 = row.createCell(2);
						if (record1.getR50Leasing() != null) {
							R50Cell2.setCellValue(record1.getR50Leasing().doubleValue());
							R50Cell2.setCellStyle(numberStyle);
						} else {
							R50Cell2.setCellValue("");
							R50Cell2.setCellStyle(textStyle);
						}
						Cell R50Cell3 = row.createCell(3);
						if (record.getR50Overdrafts() != null) {
							R50Cell3.setCellValue(record.getR50Overdrafts().doubleValue());
							R50Cell3.setCellStyle(numberStyle);
						} else {
							R50Cell3.setCellValue("");
							R50Cell3.setCellStyle(textStyle);
						}
						Cell R50Cell4 = row.createCell(4);
						if (record.getR50OtherInstallmentLoans() != null) {
							R50Cell4.setCellValue(record.getR50OtherInstallmentLoans().doubleValue());
							R50Cell4.setCellStyle(numberStyle);
						} else {
							R50Cell4.setCellValue("");
							R50Cell4.setCellStyle(textStyle);
						}

						// --- R51 (Row Index 49) ---
						row = sheet.getRow(49);
						Cell R51Cell1 = row.createCell(1);
						if (record1.getR51FactoringDebtors() != null) {
							R51Cell1.setCellValue(record1.getR51FactoringDebtors().doubleValue());
							R51Cell1.setCellStyle(numberStyle);
						} else {
							R51Cell1.setCellValue("");
							R51Cell1.setCellStyle(textStyle);
						}
						Cell R51Cell2 = row.createCell(2);
						if (record1.getR51Leasing() != null) {
							R51Cell2.setCellValue(record1.getR51Leasing().doubleValue());
							R51Cell2.setCellStyle(numberStyle);
						} else {
							R51Cell2.setCellValue("");
							R51Cell2.setCellStyle(textStyle);
						}
						Cell R51Cell3 = row.createCell(3);
						if (record.getR51Overdrafts() != null) {
							R51Cell3.setCellValue(record.getR51Overdrafts().doubleValue());
							R51Cell3.setCellStyle(numberStyle);
						} else {
							R51Cell3.setCellValue("");
							R51Cell3.setCellStyle(textStyle);
						}
						Cell R51Cell4 = row.createCell(4);
						if (record.getR51OtherInstallmentLoans() != null) {
							R51Cell4.setCellValue(record.getR51OtherInstallmentLoans().doubleValue());
							R51Cell4.setCellStyle(numberStyle);
						} else {
							R51Cell4.setCellValue("");
							R51Cell4.setCellStyle(textStyle);
						}

						// --- R52 (Row Index 50) ---
						row = sheet.getRow(50);
						Cell R52Cell1 = row.createCell(1);
						if (record1.getR52FactoringDebtors() != null) {
							R52Cell1.setCellValue(record1.getR52FactoringDebtors().doubleValue());
							R52Cell1.setCellStyle(numberStyle);
						} else {
							R52Cell1.setCellValue("");
							R52Cell1.setCellStyle(textStyle);
						}
						Cell R52Cell2 = row.createCell(2);
						if (record1.getR52Leasing() != null) {
							R52Cell2.setCellValue(record1.getR52Leasing().doubleValue());
							R52Cell2.setCellStyle(numberStyle);
						} else {
							R52Cell2.setCellValue("");
							R52Cell2.setCellStyle(textStyle);
						}
						Cell R52Cell3 = row.createCell(3);
						if (record.getR52Overdrafts() != null) {
							R52Cell3.setCellValue(record.getR52Overdrafts().doubleValue());
							R52Cell3.setCellStyle(numberStyle);
						} else {
							R52Cell3.setCellValue("");
							R52Cell3.setCellStyle(textStyle);
						}
						Cell R52Cell4 = row.createCell(4);
						if (record.getR52OtherInstallmentLoans() != null) {
							R52Cell4.setCellValue(record.getR52OtherInstallmentLoans().doubleValue());
							R52Cell4.setCellStyle(numberStyle);
						} else {
							R52Cell4.setCellValue("");
							R52Cell4.setCellStyle(textStyle);
						}
						Cell R52Cell5 = row.createCell(5);
						if (record.getR52Total() != null) {
							R52Cell5.setCellValue(record.getR52Total().doubleValue());
							R52Cell5.setCellStyle(numberStyle);
						} else {
							R52Cell5.setCellValue("");
							R52Cell5.setCellStyle(textStyle);
						}
						// --- R54 (Row Index 56) ---
						row = sheet.getRow(52);
						Cell R54Cell1 = row.createCell(1);
						if (record1.getR54FactoringDebtors() != null) {
							R54Cell1.setCellValue(record1.getR54FactoringDebtors().doubleValue());
							R54Cell1.setCellStyle(numberStyle);
						} else {
							R54Cell1.setCellValue("");
							R54Cell1.setCellStyle(textStyle);
						}
						Cell R54Cell2 = row.createCell(2);
						if (record1.getR54Leasing() != null) {
							R54Cell2.setCellValue(record1.getR54Leasing().doubleValue());
							R54Cell2.setCellStyle(numberStyle);
						} else {
							R54Cell2.setCellValue("");
							R54Cell2.setCellStyle(textStyle);
						}
						Cell R54Cell3 = row.createCell(3);
						if (record.getR54Overdrafts() != null) {
							R54Cell3.setCellValue(record.getR54Overdrafts().doubleValue());
							R54Cell3.setCellStyle(numberStyle);
						} else {
							R54Cell3.setCellValue("");
							R54Cell3.setCellStyle(textStyle);
						}
						Cell R54Cell4 = row.createCell(4);
						if (record.getR54OtherInstallmentLoans() != null) {
							R54Cell4.setCellValue(record.getR54OtherInstallmentLoans().doubleValue());
							R54Cell4.setCellStyle(numberStyle);
						} else {
							R54Cell4.setCellValue("");
							R54Cell4.setCellStyle(textStyle);
						}

						// --- R55 (Row Index 53) ---
						row = sheet.getRow(53);
						Cell R55Cell1 = row.createCell(1);
						if (record1.getR55FactoringDebtors() != null) {
							R55Cell1.setCellValue(record1.getR55FactoringDebtors().doubleValue());
							R55Cell1.setCellStyle(numberStyle);
						} else {
							R55Cell1.setCellValue("");
							R55Cell1.setCellStyle(textStyle);
						}
						Cell R55Cell2 = row.createCell(2);
						if (record1.getR55Leasing() != null) {
							R55Cell2.setCellValue(record1.getR55Leasing().doubleValue());
							R55Cell2.setCellStyle(numberStyle);
						} else {
							R55Cell2.setCellValue("");
							R55Cell2.setCellStyle(textStyle);
						}
						Cell R55Cell3 = row.createCell(3);
						if (record.getR55Overdrafts() != null) {
							R55Cell3.setCellValue(record.getR55Overdrafts().doubleValue());
							R55Cell3.setCellStyle(numberStyle);
						} else {
							R55Cell3.setCellValue("");
							R55Cell3.setCellStyle(textStyle);
						}
						Cell R55Cell4 = row.createCell(4);
						if (record.getR55OtherInstallmentLoans() != null) {
							R55Cell4.setCellValue(record.getR55OtherInstallmentLoans().doubleValue());
							R55Cell4.setCellStyle(numberStyle);
						} else {
							R55Cell4.setCellValue("");
							R55Cell4.setCellStyle(textStyle);
						}

						// --- R56 (Row Index 54) ---
						row = sheet.getRow(54);
						Cell R56Cell1 = row.createCell(1);
						if (record1.getR56FactoringDebtors() != null) {
							R56Cell1.setCellValue(record1.getR56FactoringDebtors().doubleValue());
							R56Cell1.setCellStyle(numberStyle);
						} else {
							R56Cell1.setCellValue("");
							R56Cell1.setCellStyle(textStyle);
						}
						Cell R56Cell2 = row.createCell(2);
						if (record1.getR56Leasing() != null) {
							R56Cell2.setCellValue(record1.getR56Leasing().doubleValue());
							R56Cell2.setCellStyle(numberStyle);
						} else {
							R56Cell2.setCellValue("");
							R56Cell2.setCellStyle(textStyle);
						}
						Cell R56Cell3 = row.createCell(3);
						if (record.getR56Overdrafts() != null) {
							R56Cell3.setCellValue(record.getR56Overdrafts().doubleValue());
							R56Cell3.setCellStyle(numberStyle);
						} else {
							R56Cell3.setCellValue("");
							R56Cell3.setCellStyle(textStyle);
						}
						Cell R56Cell4 = row.createCell(4);
						if (record.getR56OtherInstallmentLoans() != null) {
							R56Cell4.setCellValue(record.getR56OtherInstallmentLoans().doubleValue());
							R56Cell4.setCellStyle(numberStyle);
						} else {
							R56Cell4.setCellValue("");
							R56Cell4.setCellStyle(textStyle);
						}
						// --- R58 (Row Index 56) ---
						row = sheet.getRow(56);
						Cell R58Cell1 = row.createCell(1);
						if (record1.getR58FactoringDebtors() != null) {
							R58Cell1.setCellValue(record1.getR58FactoringDebtors().doubleValue());
							R58Cell1.setCellStyle(numberStyle);
						} else {
							R58Cell1.setCellValue("");
							R58Cell1.setCellStyle(textStyle);
						}
						Cell R58Cell2 = row.createCell(2);
						if (record1.getR58Leasing() != null) {
							R58Cell2.setCellValue(record1.getR58Leasing().doubleValue());
							R58Cell2.setCellStyle(numberStyle);
						} else {
							R58Cell2.setCellValue("");
							R58Cell2.setCellStyle(textStyle);
						}
						Cell R58Cell3 = row.createCell(3);
						if (record.getR58Overdrafts() != null) {
							R58Cell3.setCellValue(record.getR58Overdrafts().doubleValue());
							R58Cell3.setCellStyle(numberStyle);
						} else {
							R58Cell3.setCellValue("");
							R58Cell3.setCellStyle(textStyle);
						}
						Cell R58Cell4 = row.createCell(4);
						if (record.getR58OtherInstallmentLoans() != null) {
							R58Cell4.setCellValue(record.getR58OtherInstallmentLoans().doubleValue());
							R58Cell4.setCellStyle(numberStyle);
						} else {
							R58Cell4.setCellValue("");
							R58Cell4.setCellStyle(textStyle);
						}
						Cell R58Cell5 = row.createCell(5);
						if (record.getR58Total() != null) {
							R58Cell5.setCellValue(record.getR58Total().doubleValue());
							R58Cell5.setCellStyle(numberStyle);
						} else {
							R58Cell5.setCellValue("");
							R58Cell5.setCellStyle(textStyle);
						}

						// --- R59 (Row Index 57) ---
						row = sheet.getRow(57);
						Cell R59Cell1 = row.createCell(1);
						if (record1.getR59FactoringDebtors() != null) {
							R59Cell1.setCellValue(record1.getR59FactoringDebtors().doubleValue());
							R59Cell1.setCellStyle(numberStyle);
						} else {
							R59Cell1.setCellValue("");
							R59Cell1.setCellStyle(textStyle);
						}
						Cell R59Cell2 = row.createCell(2);
						if (record1.getR59Leasing() != null) {
							R59Cell2.setCellValue(record1.getR59Leasing().doubleValue());
							R59Cell2.setCellStyle(numberStyle);
						} else {
							R59Cell2.setCellValue("");
							R59Cell2.setCellStyle(textStyle);
						}
						Cell R59Cell3 = row.createCell(3);
						if (record.getR59Overdrafts() != null) {
							R59Cell3.setCellValue(record.getR59Overdrafts().doubleValue());
							R59Cell3.setCellStyle(numberStyle);
						} else {
							R59Cell3.setCellValue("");
							R59Cell3.setCellStyle(textStyle);
						}
						Cell R59Cell4 = row.createCell(4);
						if (record.getR59OtherInstallmentLoans() != null) {
							R59Cell4.setCellValue(record.getR59OtherInstallmentLoans().doubleValue());
							R59Cell4.setCellStyle(numberStyle);
						} else {
							R59Cell4.setCellValue("");
							R59Cell4.setCellStyle(textStyle);
						}

						// --- R60 (Row Index 58) ---
						row = sheet.getRow(58);
						Cell R60Cell1 = row.createCell(1);
						if (record1.getR60FactoringDebtors() != null) {
							R60Cell1.setCellValue(record1.getR60FactoringDebtors().doubleValue());
							R60Cell1.setCellStyle(numberStyle);
						} else {
							R60Cell1.setCellValue("");
							R60Cell1.setCellStyle(textStyle);
						}
						Cell R60Cell2 = row.createCell(2);
						if (record1.getR60Leasing() != null) {
							R60Cell2.setCellValue(record1.getR60Leasing().doubleValue());
							R60Cell2.setCellStyle(numberStyle);
						} else {
							R60Cell2.setCellValue("");
							R60Cell2.setCellStyle(textStyle);
						}
						Cell R60Cell3 = row.createCell(3);
						if (record.getR60Overdrafts() != null) {
							R60Cell3.setCellValue(record.getR60Overdrafts().doubleValue());
							R60Cell3.setCellStyle(numberStyle);
						} else {
							R60Cell3.setCellValue("");
							R60Cell3.setCellStyle(textStyle);
						}
						Cell R60Cell4 = row.createCell(4);
						if (record.getR60OtherInstallmentLoans() != null) {
							R60Cell4.setCellValue(record.getR60OtherInstallmentLoans().doubleValue());
							R60Cell4.setCellStyle(numberStyle);
						} else {
							R60Cell4.setCellValue("");
							R60Cell4.setCellStyle(textStyle);
						}

						// --- R61 (Row Index 59) ---
						row = sheet.getRow(59);
						Cell R61Cell1 = row.createCell(1);
						if (record1.getR61FactoringDebtors() != null) {
							R61Cell1.setCellValue(record1.getR61FactoringDebtors().doubleValue());
							R61Cell1.setCellStyle(numberStyle);
						} else {
							R61Cell1.setCellValue("");
							R61Cell1.setCellStyle(textStyle);
						}
						Cell R61Cell2 = row.createCell(2);
						if (record1.getR61Leasing() != null) {
							R61Cell2.setCellValue(record1.getR61Leasing().doubleValue());
							R61Cell2.setCellStyle(numberStyle);
						} else {
							R61Cell2.setCellValue("");
							R61Cell2.setCellStyle(textStyle);
						}
						Cell R61Cell3 = row.createCell(3);
						if (record.getR61Overdrafts() != null) {
							R61Cell3.setCellValue(record.getR61Overdrafts().doubleValue());
							R61Cell3.setCellStyle(numberStyle);
						} else {
							R61Cell3.setCellValue("");
							R61Cell3.setCellStyle(textStyle);
						}
						Cell R61Cell4 = row.createCell(4);
						if (record.getR61OtherInstallmentLoans() != null) {
							R61Cell4.setCellValue(record.getR61OtherInstallmentLoans().doubleValue());
							R61Cell4.setCellStyle(numberStyle);
						} else {
							R61Cell4.setCellValue("");
							R61Cell4.setCellStyle(textStyle);
						}

						// --- R62 (Row Index 60) ---
						row = sheet.getRow(60);
						Cell R62Cell1 = row.createCell(1);
						if (record1.getR62FactoringDebtors() != null) {
							R62Cell1.setCellValue(record1.getR62FactoringDebtors().doubleValue());
							R62Cell1.setCellStyle(numberStyle);
						} else {
							R62Cell1.setCellValue("");
							R62Cell1.setCellStyle(textStyle);
						}
						Cell R62Cell2 = row.createCell(2);
						if (record1.getR62Leasing() != null) {
							R62Cell2.setCellValue(record1.getR62Leasing().doubleValue());
							R62Cell2.setCellStyle(numberStyle);
						} else {
							R62Cell2.setCellValue("");
							R62Cell2.setCellStyle(textStyle);
						}
						Cell R62Cell3 = row.createCell(3);
						if (record.getR62Overdrafts() != null) {
							R62Cell3.setCellValue(record.getR62Overdrafts().doubleValue());
							R62Cell3.setCellStyle(numberStyle);
						} else {
							R62Cell3.setCellValue("");
							R62Cell3.setCellStyle(textStyle);
						}
						Cell R62Cell4 = row.createCell(4);
						if (record.getR62OtherInstallmentLoans() != null) {
							R62Cell4.setCellValue(record.getR62OtherInstallmentLoans().doubleValue());
							R62Cell4.setCellStyle(numberStyle);
						} else {
							R62Cell4.setCellValue("");
							R62Cell4.setCellStyle(textStyle);
						}

						// --- R63 (Row Index 61) ---
						row = sheet.getRow(61);
						Cell R63Cell1 = row.createCell(1);
						if (record1.getR63FactoringDebtors() != null) {
							R63Cell1.setCellValue(record1.getR63FactoringDebtors().doubleValue());
							R63Cell1.setCellStyle(numberStyle);
						} else {
							R63Cell1.setCellValue("");
							R63Cell1.setCellStyle(textStyle);
						}
						Cell R63Cell2 = row.createCell(2);
						if (record1.getR63Leasing() != null) {
							R63Cell2.setCellValue(record1.getR63Leasing().doubleValue());
							R63Cell2.setCellStyle(numberStyle);
						} else {
							R63Cell2.setCellValue("");
							R63Cell2.setCellStyle(textStyle);
						}
						Cell R63Cell3 = row.createCell(3);
						if (record.getR63Overdrafts() != null) {
							R63Cell3.setCellValue(record.getR63Overdrafts().doubleValue());
							R63Cell3.setCellStyle(numberStyle);
						} else {
							R63Cell3.setCellValue("");
							R63Cell3.setCellStyle(textStyle);
						}
						Cell R63Cell4 = row.createCell(4);
						if (record.getR63OtherInstallmentLoans() != null) {
							R63Cell4.setCellValue(record.getR63OtherInstallmentLoans().doubleValue());
							R63Cell4.setCellStyle(numberStyle);
						} else {
							R63Cell4.setCellValue("");
							R63Cell4.setCellStyle(textStyle);
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

//	// Resub Format excel
//	public byte[] BRRS_M_LA4ResubExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");
//
//		if ("email".equalsIgnoreCase(format) && version != null) {
//			logger.info("Service: Generating RESUB report for version {}", version);
//
//			try {
//				// ✅ Redirecting to Resub Excel
//				return BRRS_M_LA4ResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//						version);
//
//			} catch (ParseException e) {
//				logger.error("Invalid report date format: {}", fromdate, e);
//				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//			}
//		}
//
//		List<M_LA4_Resub_Summary_Entity1> dataList = brrs_M_LA4_resub_summary_repo1
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//		List<M_LA4_Resub_Summary_Entity2> dataList1 = brrs_M_LA4_resub_summary_repo2
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for M_LA4 report. Returning empty result.");
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
//					M_LA4_Resub_Summary_Entity1 record = dataList.get(i);
//					M_LA4_Resub_Summary_Entity2 record1 = dataList1.get(i);
//					System.out.println("rownumber=" + startRow + i);
//					System.out.println("rownumber=" + startRow + i);
//					Row row = sheet.getRow(startRow + i);
//					if (row == null) {
//						row = sheet.createRow(startRow + i);
//					}
////NORMAL
//					// R10 Col C
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

//	// Resub Email Excel
//	public byte[] BRRS_M_LA4ResubEmailExcel(String filename, String reportId, String fromdate, String todate,
//			String currency, String dtltype, String type, BigDecimal version) throws Exception {
//
//		logger.info("Service: Starting Archival Email Excel generation process in memory.");
//
//		List<M_LA4_Resub_Summary_Entity1> dataList = brrs_M_LA4_resub_summary_repo1
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//	List<M_LA4_Resub_Summary_Entity2> dataList1 = brrs_M_LA4_resub_summary_repo2
//				.getdatabydateListarchival(dateformat.parse(todate), version);
//		if (dataList.isEmpty()) {
//			logger.warn("Service: No data found for BRRS_M_LA4 report. Returning empty result.");
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
//					M_LA4_Resub_Summary_Entity1 record = dataList.get(i);
//					M_LA4_Resub_Summary_Entity2 record1 = dataList1.get(i);
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
