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
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.transaction.annotation.Transactional;

import com.bornfire.brrs.entities.BRRS_Q_LARADV_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_LARADV_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_LARADV_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_LARADV_Resub_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_LARADV_Resub_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_LARADV_Summary_Repo;
import com.bornfire.brrs.entities.Q_LARADV_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Q_LARADV_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_LARADV_Detail_Entity;
import com.bornfire.brrs.entities.Q_LARADV_Resub_Detail_Entity;
import com.bornfire.brrs.entities.Q_LARADV_Resub_Summary_Entity;
import com.bornfire.brrs.entities.Q_LARADV_Summary_Entity;

@Component
@Service

public class BRRS_Q_LARADV_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_LARADV_ReportService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	BRRS_Q_LARADV_Detail_Repo Q_LARADV_Detail_Repo;
	
	@Autowired
	BRRS_Q_LARADV_Summary_Repo Q_LARADV_Summary_Repo; 
	
	@Autowired
	BRRS_Q_LARADV_Archival_Detail_Repo Q_LARADV_Archival_Detail_Repo;
	
	@Autowired
	BRRS_Q_LARADV_Archival_Summary_Repo Q_LARADV_Archival_Summary_Repo; 

	@Autowired
	BRRS_Q_LARADV_Resub_Detail_Repo Q_LARADV_Resub_Detail_Repo;
	
	@Autowired
	BRRS_Q_LARADV_Resub_Summary_Repo Q_LARADV_Resub_Summary_Repo; 

	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	
	public ModelAndView getBRRS_Q_LARADV_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		System.out.println("dtltype...." + dtltype);
		System.out.println("type...." + type);

		try {

			// Parse only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY SECTION
			// ===========================================================

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<Q_LARADV_Archival_Summary_Entity> 	T1Master = Q_LARADV_Archival_Summary_Repo.getdatabydateList(dateformat.parse(todate), version);

				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<Q_LARADV_Resub_Summary_Entity> T1Master = Q_LARADV_Resub_Summary_Repo.getdatabydateList(dateformat.parse(todate), version);


				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<Q_LARADV_Summary_Entity>	T1Master = Q_LARADV_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_LARADV_Archival_Detail_Entity> T1Master =  Q_LARADV_Archival_Detail_Repo.getdatabydateList(dateformat.parse(todate), version);


					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_LARADV_Resub_Detail_Entity> T1Master = Q_LARADV_Resub_Detail_Repo.getdatabydateList(dateformat.parse(todate), version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<Q_LARADV_Detail_Entity> T1Master =Q_LARADV_Detail_Repo.getdatabydateList(dateformat.parse(todate));
					
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/Q_LARADV");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public Q_LARADV_Summary_Entity findById(Long id) {

        return Q_LARADV_Summary_Repo.findById(id).orElse(null);

    }

	
public byte[] getBRRS_Q_LARADV_EmailExcel(String filename,
                                          String reportId,
                                          String fromdate,
                                          String todate,
                                          String currency,
                                          String dtltype,
                                          String type,
                                          String format,
                                          BigDecimal version) throws Exception {

    logger.info("Service: Starting Excel generation process in memory.");

    System.out.println("======= DOWNLOAD DETAILS =======");
    System.out.println("TYPE      : " + type);
    System.out.println("FORMAT    : " + format);
    System.out.println("DTLTYPE   : " + dtltype);
    System.out.println("DATE      : " + dateformat.parse(todate));
    System.out.println("VERSION   : " + version);
    System.out.println("==========================");

    Date reportDate = dateformat.parse(todate);

    List<Q_LARADV_Summary_Entity> dataList =
            Q_LARADV_Summary_Repo.getdatabydateList(reportDate);

    if (dataList == null || dataList.isEmpty()) {
        logger.warn("No data found for BRRS_Q_LARADV report.");
        return new byte[0];
    }

    String templateDir = env.getProperty("output.exportpathtemp");
    Path templatePath = Paths.get(templateDir, filename);

    logger.info("Loading template from : {}", templatePath.toAbsolutePath());

    if (!Files.exists(templatePath)) {
        throw new FileNotFoundException(
                "Template file not found : " + templatePath.toAbsolutePath());
    }

    if (!Files.isReadable(templatePath)) {
        throw new SecurityException(
                "Template file exists but not readable : "
                        + templatePath.toAbsolutePath());
    }

    try (InputStream templateInputStream = Files.newInputStream(templatePath);
         Workbook workbook = WorkbookFactory.create(templateInputStream);
         ByteArrayOutputStream out = new ByteArrayOutputStream()) {

        Sheet sheet = workbook.getSheetAt(0);

     // Set report date in Row 6, Column 1 (B7)

        Row dateRow = sheet.getRow(6);
        if (dateRow == null) {
            dateRow = sheet.createRow(6);
        }

        Cell dateCell = dateRow.getCell(1);
        if (dateCell == null) {
            dateCell = dateRow.createCell(1);
        }

        // Convert to DD/MM/YYYY format
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date reportDisplayDate = dateformat.parse(todate);

        dateCell.setCellValue(displayFormat.format(reportDisplayDate));
        
        /* ==========================================================
         * FONT
         * ========================================================== */

        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 8);

        /* ==========================================================
         * TEXT STYLE
         * ========================================================== */

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(font);
        textStyle.setWrapText(true);

        textStyle.setBorderBottom(BorderStyle.THIN);
        textStyle.setBorderTop(BorderStyle.THIN);
        textStyle.setBorderLeft(BorderStyle.THIN);
        textStyle.setBorderRight(BorderStyle.THIN);

        /* ==========================================================
         * NUMBER STYLE
         * ========================================================== */

        DataFormat dataFormat = workbook.createDataFormat();

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setFont(font);
        numberStyle.setDataFormat(
                dataFormat.getFormat("#,##0.00"));

        numberStyle.setBorderBottom(BorderStyle.THIN);
        numberStyle.setBorderTop(BorderStyle.THIN);
        numberStyle.setBorderLeft(BorderStyle.THIN);
        numberStyle.setBorderRight(BorderStyle.THIN);

        /* ==========================================================
         * DATE STYLE
         * ========================================================== */

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(font);
        dateStyle.setDataFormat(
                dataFormat.getFormat("dd-MM-yyyy"));

        dateStyle.setBorderBottom(BorderStyle.THIN);
        dateStyle.setBorderTop(BorderStyle.THIN);
        dateStyle.setBorderLeft(BorderStyle.THIN);
        dateStyle.setBorderRight(BorderStyle.THIN);

        /* ==========================================================
         * TOTAL VARIABLES
         * ========================================================== */

        double totalOriginalAmount = 0.00;
        double totalOutstandingBalance = 0.00;

        /* ==========================================================
         * DATA FILLING
         * ========================================================== */

        
        int rowIndex = 9;

        for (Q_LARADV_Summary_Entity item : dataList) {

            Row row = sheet.createRow(rowIndex++);

            Cell cell;

            // Column A
            cell = row.createCell(0);
            cell.setCellValue(
                    item.getCustomerGroupName() != null
                            ? item.getCustomerGroupName()
                            : "");
            cell.setCellStyle(textStyle);

            // Column D
            cell = row.createCell(1);
            cell.setCellValue(
                    item.getFacilityType() != null
                            ? item.getFacilityType()
                            : "");
            cell.setCellStyle(textStyle);

            // Column E - Original Amount
            cell = row.createCell(2);

            double originalAmt =
                    item.getOriginalAmount() != null
                            ? item.getOriginalAmount().doubleValue()
                            : 0.00;

            cell.setCellValue(originalAmt);
            cell.setCellStyle(numberStyle);

            totalOriginalAmount += originalAmt;

            // Column F - Outstanding Balance
            cell = row.createCell(3);

            double outstandingAmt =
                    item.getUtilisationOutstandingBalance() != null
                            ? item.getUtilisationOutstandingBalance().doubleValue()
                            : 0.00;

            cell.setCellValue(outstandingAmt);
            cell.setCellStyle(numberStyle);

            totalOutstandingBalance += outstandingAmt;

            // Column G - Effective Date
            cell = row.createCell(4);

            if (item.getEffectiveDate() != null) {
                cell.setCellValue(item.getEffectiveDate());
                cell.setCellStyle(dateStyle);
            } else {
                cell.setCellValue("");
                cell.setCellStyle(textStyle);
            }

            // Column H
            cell = row.createCell(5);
            cell.setCellValue(
                    item.getRepaymentPeriod() != null
                            ? item.getRepaymentPeriod()
                            : "");
            cell.setCellStyle(textStyle);

            // Column I
            cell = row.createCell(6);
            cell.setCellValue(
                    item.getPerformanceStatus() != null
                            ? item.getPerformanceStatus()
                            : "");
            cell.setCellStyle(textStyle);

            // Column J
            cell = row.createCell(7);
            cell.setCellValue(
                    item.getSecurityDetails() != null
                            ? item.getSecurityDetails()
                            : "");
            cell.setCellStyle(textStyle);

            // Column K
            cell = row.createCell(8);
            cell.setCellValue(
                    item.getBoardApproval() != null
                            ? item.getBoardApproval()
                            : "");
            cell.setCellStyle(textStyle);

            // Column L
            cell = row.createCell(9);
            cell.setCellValue(
                    item.getInterestRate() != null
                            ? item.getInterestRate().doubleValue()
                            : 0.00);
            cell.setCellStyle(numberStyle);

            // Column M
            cell = row.createCell(10);
            cell.setCellValue(
                    item.getOutstandingBalancePercent() != null
                            ? item.getOutstandingBalancePercent().doubleValue()
                            : 0.00);
            cell.setCellStyle(numberStyle);

            // Column N
            cell = row.createCell(11);
            cell.setCellValue(
                    item.getLimitPercent() != null
                            ? item.getLimitPercent().doubleValue()
                            : 0.00);
            cell.setCellStyle(numberStyle);
        }

        /* ==========================================================
         * TOTAL ROW
         * ========================================================== */

        Row totalRow = sheet.createRow(rowIndex);

        Cell cell = totalRow.createCell(1);
        cell.setCellValue("TOTAL");
        cell.setCellStyle(textStyle);

        // Original Amount Total
        cell = totalRow.createCell(2);
        cell.setCellValue(totalOriginalAmount);
        cell.setCellStyle(numberStyle);

        // Outstanding Balance Total
        cell = totalRow.createCell(3);
        cell.setCellValue(totalOutstandingBalance);
        cell.setCellStyle(numberStyle);

        /* ==========================================================
         * AUTO SIZE
         * ========================================================== */

        for (int i = 0; i <= 11; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(out);

        logger.info("Excel generated successfully. Size : {} bytes",
                out.size());

        return out.toByteArray();
    }
}



public void saveQlaradv(Q_LARADV_Summary_Entity summary) {

    // Save Summary Table
	Q_LARADV_Summary_Repo.save(summary);

    // Copy to Detail Entity
    Q_LARADV_Detail_Entity detail =
            new Q_LARADV_Detail_Entity();

    detail.setGroupName(summary.getGroupName());
    detail.setCustomerGroupName(summary.getCustomerGroupName());
    detail.setSectorType(summary.getSectorType());
    detail.setFacilityType(summary.getFacilityType());

    detail.setOriginalAmount(summary.getOriginalAmount());
    detail.setUtilisationOutstandingBalance(
            summary.getUtilisationOutstandingBalance());

    detail.setEffectiveDate(summary.getEffectiveDate());
    detail.setRepaymentPeriod(summary.getRepaymentPeriod());
    detail.setPerformanceStatus(summary.getPerformanceStatus());
    detail.setSecurityDetails(summary.getSecurityDetails());
    detail.setBoardApproval(summary.getBoardApproval());

    detail.setInterestRate(summary.getInterestRate());

    detail.setOutstandingBalancePercent(
            summary.getOutstandingBalancePercent());

    detail.setLimitPercent(summary.getLimitPercent());

    detail.setReportDate(summary.getReportDate());
    detail.setReportVersion(summary.getReportVersion());
    detail.setReportFrequency(summary.getReportFrequency());
    detail.setReportCode(summary.getReportCode());
    detail.setReportDesc(summary.getReportDesc());

    detail.setEntityFlg(summary.getEntityFlg());
    detail.setModifyFlg(summary.getModifyFlg());
    detail.setDelFlg(summary.getDelFlg());

    detail.setReportResubdate(summary.getReportResubdate());

    // Save Detail Table
    Q_LARADV_Detail_Repo.save(detail);
}


public void updateQlaradv(
        Q_LARADV_Summary_Entity summary) {

    // ==========================
    // UPDATE SUMMARY TABLE
    // ==========================

    Q_LARADV_Summary_Entity existingSummary =
            Q_LARADV_Summary_Repo
            .findById(summary.getSno())
            .orElseThrow(() ->
                    new RuntimeException(
                            "Summary Record Not Found"));

    existingSummary.setGroupName(summary.getGroupName());
    existingSummary.setCustomerGroupName(
            summary.getCustomerGroupName());
    existingSummary.setSectorType(
            summary.getSectorType());
    existingSummary.setFacilityType(
            summary.getFacilityType());

    existingSummary.setOriginalAmount(
            summary.getOriginalAmount());

    existingSummary.setUtilisationOutstandingBalance(
            summary.getUtilisationOutstandingBalance());

    existingSummary.setEffectiveDate(
            summary.getEffectiveDate());

    existingSummary.setRepaymentPeriod(
            summary.getRepaymentPeriod());

    existingSummary.setPerformanceStatus(
            summary.getPerformanceStatus());

    existingSummary.setSecurityDetails(
            summary.getSecurityDetails());

    existingSummary.setBoardApproval(
            summary.getBoardApproval());

    existingSummary.setInterestRate(
            summary.getInterestRate());

    existingSummary.setOutstandingBalancePercent(
            summary.getOutstandingBalancePercent());

    existingSummary.setLimitPercent(
            summary.getLimitPercent());

    

    Q_LARADV_Summary_Repo.save(existingSummary);


    // ==========================
    // UPDATE DETAIL TABLE
    // ==========================

    Q_LARADV_Detail_Entity detail =
            Q_LARADV_Detail_Repo
            .findById(summary.getSno())
            .orElse(new Q_LARADV_Detail_Entity());

    detail.setSno(summary.getSno());

    detail.setGroupName(summary.getGroupName());
    detail.setCustomerGroupName(
            summary.getCustomerGroupName());

    detail.setSectorType(summary.getSectorType());

    detail.setFacilityType(
            summary.getFacilityType());

    detail.setOriginalAmount(
            summary.getOriginalAmount());

    detail.setUtilisationOutstandingBalance(
            summary.getUtilisationOutstandingBalance());

    detail.setEffectiveDate(
            summary.getEffectiveDate());

    detail.setRepaymentPeriod(
            summary.getRepaymentPeriod());

    detail.setPerformanceStatus(
            summary.getPerformanceStatus());

    detail.setSecurityDetails(
            summary.getSecurityDetails());

    detail.setBoardApproval(
            summary.getBoardApproval());

    detail.setInterestRate(
            summary.getInterestRate());

    detail.setOutstandingBalancePercent(
            summary.getOutstandingBalancePercent());

    detail.setLimitPercent(
            summary.getLimitPercent());


    detail.setModifyFlg("Y");

   

    Q_LARADV_Detail_Repo.save(detail);
}

}
