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
import com.bornfire.brrs.entities.Common_Disclosure_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Common_Disclosure_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Common_Disclosure_Detail_Entity;
import com.bornfire.brrs.entities.Common_Disclosure_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_Common_Disclosure_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Common_Disclosure_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Common_Disclosure_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Common_Disclosure_Summary_Repo;

@Component
@Service

public class BRRS_Common_Disclosure_ReportService {
    private static final Logger logger = LoggerFactory.getLogger(BRRS_Common_Disclosure_ReportService.class);

    @Autowired
    private Environment env;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    BRRS_Common_Disclosure_Summary_Repo Common_Disclosure_summary_repo;

    @Autowired
    BRRS_Common_Disclosure_Archival_Summary_Repo Common_Disclosure_Archival_Summary_Repo;

    @Autowired
    BRRS_Common_Disclosure_Detail_Repo Common_Disclosure_detail_repo;

    @Autowired
    BRRS_Common_Disclosure_Archival_Detail_Repo Common_Disclosure_Archival_Detail_Repo;

    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

    public ModelAndView getCommon_DisclosureView(String reportId, String fromdate, String todate, String currency,
            String dtltype,
            Pageable pageable, String type, String version) {

        ModelAndView mv = new ModelAndView();

        System.out.println("testing");
        System.out.println(version);

        if ("ARCHIVAL".equals(type) && version != null && !version.isEmpty()) {

            System.out.println("ARCHIVAL MODE");
            System.out.println("version = " + version);

            List<Common_Disclosure_Archival_Summary_Entity> T1Master = new ArrayList<>();

            try {
                Date dt = dateformat.parse(todate);

                T1Master = Common_Disclosure_Archival_Summary_Repo.getdatabydateListarchival(dt, version);

                System.out.println("T1Master size = " + T1Master.size());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            mv.addObject("reportsummary", T1Master);

        } else {

            List<Common_Disclosure_Summary_Entity> T1Master = new ArrayList<Common_Disclosure_Summary_Entity>();

            try {
                Date d1 = dateformat.parse(todate);

                T1Master = Common_Disclosure_summary_repo.getdatabydateList(dateformat.parse(todate));

                System.out.println("T1Master size " + T1Master.size());
                mv.addObject("report_date", dateformat.format(d1));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            mv.addObject("reportsummary", T1Master);

        }

        mv.setViewName("BRRS/Common_Disclosure");

        mv.addObject("displaymode", "summary");

        System.out.println("scv" + mv.getViewName());

        return mv;

    }

    public ModelAndView getCommon_DisclosurecurrentDtl(String reportId, String fromdate, String todate, String currency,
            String dtltype, Pageable pageable, String filter, String type, String version) {

        int pageSize = pageable != null ? pageable.getPageSize() : 10;
        int currentPage = pageable != null ? pageable.getPageNumber() : 0;
        int totalPages = 0;

        ModelAndView mv = new ModelAndView();

        // Session hs = sessionFactory.getCurrentSession();

        try {
            Date parsedDate = null;

            if (todate != null && !todate.isEmpty()) {
                parsedDate = dateformat.parse(todate);
            }

            String reportLable = null;
            String reportAddlCriteria_1 = null;
            // ? Split filter string into rowId & columnId
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
                // ?? Archival branch
                List<Common_Disclosure_Archival_Detail_Entity> T1Dt1;
                if (reportLable != null && reportAddlCriteria_1 != null) {
                    T1Dt1 = Common_Disclosure_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable,
                            reportAddlCriteria_1,
                            parsedDate, version);
                } else {
                    T1Dt1 = Common_Disclosure_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
                }

                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);
                System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

            } else {
                // ?? Current branch
                List<Common_Disclosure_Detail_Entity> T1Dt1;

                if (reportLable != null && reportAddlCriteria_1 != null) {
                    T1Dt1 = Common_Disclosure_detail_repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1,
                            parsedDate);
                } else {
                    T1Dt1 = Common_Disclosure_detail_repo.getdatabydateList(parsedDate);
                    totalPages = Common_Disclosure_detail_repo.getdatacount(parsedDate);
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

        mv.setViewName("BRRS/Common_Disclosure");
        mv.addObject("displaymode", "Details");
        mv.addObject("currentPage", currentPage);
        System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
        mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
        mv.addObject("reportsflag", "reportsflag");
        mv.addObject("menu", reportId);
        return mv;
    }

    public byte[] getCommon_DisclosureExcel(String filename, String reportId, String fromdate, String todate,
            String currency,
            String dtltype, String type, String version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");

        // ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelCommon_DisclosureARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
                    version);
        }

        // Fetch data

        List<Common_Disclosure_Summary_Entity> dataList = Common_Disclosure_summary_repo
                .getdatabydateList(dateformat.parse(todate));

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for  Common_Disclosure report. Returning empty result.");
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
                    Common_Disclosure_Summary_Entity record = dataList.get(i);

                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }

  // A TABLE
                    // ---------- R7 (Row 7 -> index 6) ----------
                    row = sheet.getRow(6);
                    Cell cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR7_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR7_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ---------- R8 (Row 8 -> index 7) ----------
                    row = sheet.getRow(7);
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR8_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR8_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ---------- R9 (Row 9 -> index 8) ----------
                    row = sheet.getRow(8);
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR9_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR9_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ---------- R10 (Row 10 -> index 9) ----------
                    row = sheet.getRow(9);
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR10_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR10_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ---------- R11 (Row 11 -> index 10) ----------
                    row = sheet.getRow(10);
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR11_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR11_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ROW 12
                    row = sheet.getRow(11);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR12_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR12_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ROW 13
                    row = sheet.getRow(12);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR13_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR13_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ROW 14
                    row = sheet.getRow(13);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR14_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR14_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }


                }

                workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            } else {

            }

            // Write the final workbook content to the in-memory stream.
            workbook.write(out);

            logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

            return out.toByteArray();
        }

    }

    public byte[] getExcelCommon_DisclosureARCHIVAL(String filename, String reportId, String fromdate, String todate,
            String currency, String dtltype, String type, String version) throws Exception {

        logger.info("Service: Starting Excel generation process in memory.");

        if (type.equals("ARCHIVAL") & version != null) {

        }

        List<Common_Disclosure_Archival_Summary_Entity> dataList = Common_Disclosure_Archival_Summary_Repo
                .getdatabydateListarchival(dateformat.parse(todate), version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for Common_Disclosure report. Returning empty result.");
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
                    Common_Disclosure_Archival_Summary_Entity record = dataList.get(i);

                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }
                    // A TABLE
                    // ---------- R7 (Row 7 -> index 6) ----------
                    row = sheet.getRow(6);
                    Cell cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR7_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR7_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ---------- R8 (Row 8 -> index 7) ----------
                    row = sheet.getRow(7);
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR8_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR8_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ---------- R9 (Row 9 -> index 8) ----------
                    row = sheet.getRow(8);
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR9_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR9_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ---------- R10 (Row 10 -> index 9) ----------
                    row = sheet.getRow(9);
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR10_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR10_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ---------- R11 (Row 11 -> index 10) ----------
                    row = sheet.getRow(10);
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);

                    if (record.getR11_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR11_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ROW 12
                    row = sheet.getRow(11);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR12_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR12_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ROW 13
                    row = sheet.getRow(12);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR13_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR13_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ROW 14
                    row = sheet.getRow(13);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR14_COMPONENT_OF_REGU() != null) {
                        cellC.setCellValue(record.getR14_COMPONENT_OF_REGU().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                }

                workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            } else {

            }

            // Write the final workbook content to the in-memory stream.
            workbook.write(out);

            logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

            return out.toByteArray();
        }

    }

    public List<Object> getCommon_DisclosureArchival() {
        List<Object> Common_DisclosureArchivallist = new ArrayList<>();
        try {
            Common_DisclosureArchivallist = Common_Disclosure_Archival_Summary_Repo.getCommon_Disclosurearchival();

            System.out.println("countser" + Common_DisclosureArchivallist.size());

        } catch (Exception e) {
            // Log the exception
            System.err.println("Error fetching Common_DisclosureArchivallist Archival data: " + e.getMessage());
            e.printStackTrace();

            // Optionally, you can rethrow it or return empty list
            // throw new RuntimeException("Failed to fetch data", e);
        }
        return Common_DisclosureArchivallist;
    }

    public byte[] getCommon_DisclosureDetailExcel(String filename, String fromdate, String todate, String currency,
            String dtltype,
            String type, String version) {
        try {
            logger.info("Generating Excel for Common_Disclosure Details...");
            System.out.println("came to Detail download service");

            if (type.equals("ARCHIVAL") & version != null) {
                byte[] ARCHIVALreport = getCommon_DisclosureDetailExcelARCHIVAL(filename, fromdate, todate, currency,
                        dtltype, type,
                        version);
                return ARCHIVALreport;
            }

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Common_DisclosureDetails");

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
            String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE",
                    "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
            List<Common_Disclosure_Detail_Entity> reportData = Common_Disclosure_detail_repo
                    .getdatabydateList(parsedToDate);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (Common_Disclosure_Detail_Entity item : reportData) {
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

                    row.createCell(4).setCellValue(item.getReportLable());
                    row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
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
                logger.info("No data found for Common_Disclosure — only header will be written.");
            }

            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating Common_Disclosure Excel", e);
            return new byte[0];
        }
    }

    public byte[] getCommon_DisclosureDetailExcelARCHIVAL(String filename, String fromdate, String todate,
            String currency,
            String dtltype, String type, String version) {
        try {
            logger.info("Generating Excel for Common_Disclosure ARCHIVAL Details...");
            System.out.println("came to ARCHIVAL Detail download service");
            if (type.equals("ARCHIVAL") & version != null) {

            }
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Common_DisclosureDetail");

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
            String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE",
                    "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
            List<Common_Disclosure_Archival_Detail_Entity> reportData = Common_Disclosure_Archival_Detail_Repo
                    .getdatabydateList(parsedToDate,
                            version);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (Common_Disclosure_Archival_Detail_Entity item : reportData) {
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

                    row.createCell(4).setCellValue(item.getReportLable());
                    row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
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
                logger.info("No data found for Common_Disclosure — only header will be written.");
            }

            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating  Common_Disclosure Excel", e);
            return new byte[0];
        }
    }

    @Autowired
    BRRS_Common_Disclosure_Detail_Repo brrs_Common_Disclosure_detail_repo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
        ModelAndView mv = new ModelAndView("BRRS/Common_Disclosure");

        if (acctNo != null) {
            Common_Disclosure_Detail_Entity Common_DisclosureEntity = brrs_Common_Disclosure_detail_repo
                    .findByAcctnumber(acctNo);
            if (Common_DisclosureEntity != null && Common_DisclosureEntity.getReportDate() != null) {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
                        .format(Common_DisclosureEntity.getReportDate());
                mv.addObject("asondate", formattedDate);
            }
            mv.addObject("Common_DisclosureData", Common_DisclosureEntity);
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

            Common_Disclosure_Detail_Entity existing = brrs_Common_Disclosure_detail_repo.findByAcctnumber(acctNo);
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
                if (existing.getAcctBalanceInpula() == null ||
                        existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
                    existing.setAcctBalanceInpula(newacctBalanceInpula);
                    isChanged = true;
                    logger.info("Balance updated to {}", newacctBalanceInpula);
                }
            }

            if (isChanged) {
                brrs_Common_Disclosure_detail_repo.save(existing);
                logger.info("Record updated successfully for account {}", acctNo);

                // Format date for procedure
                String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
                        .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

                // Run summary procedure after commit
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        try {
                            logger.info("Transaction committed — calling BRRS_Common_Disclosure_SUMMARY_PROCEDURE({})",
                                    formattedDate);
                            jdbcTemplate.update("BEGIN BRRS_Common_Disclosure_SUMMARY_PROCEDURE(?); END;",
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
            logger.error("Error updating Common_Disclosure record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating record: " + e.getMessage());
        }
    }

}