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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_GL_SCH_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_GL_SCH_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_GL_SCH_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_GL_SCH_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_GL_SCH_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_GL_SCH_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_GL_SCH_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_GL_SCH_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_GL_SCH_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_GL_SCH_Summary_Repo3;
import com.bornfire.brrs.entities.FORMAT_II_Manual_Summary_Entity;
import com.bornfire.brrs.entities.GL_SCH_Archival_Detail_Entity;
import com.bornfire.brrs.entities.GL_SCH_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.GL_SCH_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.GL_SCH_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.GL_SCH_Detail_Entity;
import com.bornfire.brrs.entities.GL_SCH_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.GL_SCH_Manual_Summary_Entity;
import com.bornfire.brrs.entities.GL_SCH_Summary_Entity1;
import com.bornfire.brrs.entities.GL_SCH_Summary_Entity2;
import com.bornfire.brrs.entities.GL_SCH_Summary_Entity3;

@Component
@Service

public class BRRS_GL_SCH_ReportService {
    private static final Logger logger = LoggerFactory.getLogger(BRRS_GL_SCH_ReportService.class);

    @Autowired
    private Environment env;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    BRRS_GL_SCH_Summary_Repo1 GL_SCH_summary_repo1;

    @Autowired
    BRRS_GL_SCH_Archival_Summary_Repo1 GL_SCH_Archival_Summary_Repo1;

    @Autowired
    BRRS_GL_SCH_Summary_Repo2 GL_SCH_summary_repo2;

    @Autowired
    BRRS_GL_SCH_Archival_Summary_Repo2 GL_SCH_Archival_Summary_Repo2;
    
    @Autowired
    BRRS_GL_SCH_Summary_Repo3 GL_SCH_summary_repo3;

    @Autowired
    BRRS_GL_SCH_Archival_Summary_Repo3 GL_SCH_Archival_Summary_Repo3;

    @Autowired
    BRRS_GL_SCH_Detail_Repo GL_SCH_detail_repo;

    @Autowired
    BRRS_GL_SCH_Archival_Detail_Repo GL_SCH_Archival_Detail_Repo;

    @Autowired 
   	BRRS_GL_SCH_Manual_Summary_Repo                     GL_SCH_Manual_summary_repo;
   	 
   	@Autowired
   	BRRS_GL_SCH_Manual_Archival_Summary_Repo            GL_SCH_Manual_Archival_Summary_Repo;
   	
   	
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

    public ModelAndView getGL_SCHView(String reportId, String fromdate, String todate,
            String currency, String dtltype, Pageable pageable,
            String type, String version) {

        ModelAndView mv = new ModelAndView();
        Session hs = sessionFactory.getCurrentSession();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        try {
            Date d1 = dateformat.parse(todate);

            // ---------- CASE 1: ARCHIVAL ----------
            if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
                List<GL_SCH_Archival_Summary_Entity1> T1Master = GL_SCH_Archival_Summary_Repo1
                        .getdatabydateListarchival(d1, version);
                List<GL_SCH_Archival_Summary_Entity2> T2Master = GL_SCH_Archival_Summary_Repo2
                        .getdatabydateListarchival(d1, version);
                List<GL_SCH_Archival_Summary_Entity3> T3Master = GL_SCH_Archival_Summary_Repo3
                        .getdatabydateListarchival(d1, version);
                List<GL_SCH_Manual_Archival_Summary_Entity> T4Master = GL_SCH_Manual_Archival_Summary_Repo
                        .getdatabydateListarchival(d1, version);
               
                mv.addObject("reportsummary", T1Master);
                mv.addObject("reportsummary1", T2Master);
                mv.addObject("reportsummary2", T3Master);
                mv.addObject("reportsummary3", T4Master);
                
                System.out.println("T1Master Size " + T1Master.size());
                System.out.println("T2Master Size " + T2Master.size());
                System.out.println("T3Master Size " + T3Master.size());
                System.out.println("T4Master Size " + T4Master.size());

            }

            // ---------- CASE 3: NORMAL ----------
            else {
                List<GL_SCH_Summary_Entity1> T1Master = GL_SCH_summary_repo1
                        .getdatabydateList(dateformat.parse(todate));
                        List<GL_SCH_Summary_Entity2> T2Master = GL_SCH_summary_repo2
                        .getdatabydateList(dateformat.parse(todate));
                        List<GL_SCH_Summary_Entity3> T3Master = GL_SCH_summary_repo3
                                .getdatabydateList(dateformat.parse(todate));
                        List<GL_SCH_Manual_Summary_Entity> T4Master = GL_SCH_Manual_summary_repo
                                .getdatabydateList(dateformat.parse(todate));
          

                        mv.addObject("reportsummary", T1Master);
                        mv.addObject("reportsummary1", T2Master);
                        mv.addObject("reportsummary2", T3Master);
                        mv.addObject("reportsummary3", T4Master);
                        
                        System.out.println("T1Master Size " + T1Master.size());
                        System.out.println("T2Master Size " + T2Master.size());
                        System.out.println("T3Master Size " + T3Master.size());
                        System.out.println("T4Master Size " + T4Master.size());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mv.setViewName("BRRS/GL_SCH");
        mv.addObject("displaymode", "summary");
        System.out.println("View set to: " + mv.getViewName());
        return mv;
    }

   

    public ModelAndView getGL_SCHcurrentDtl(String reportId, String fromdate, String todate, String currency,
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

            String reportLabel = null;
            String reportAddlCriteria1 = null;
            // ? Split filter string into rowId & columnId
            if (filter != null && filter.contains(",")) {
                String[] parts = filter.split(",");
                if (parts.length >= 2) {
                    reportLabel = parts[0];
                    reportAddlCriteria1 = parts[1];
                }
            }

            System.out.println(type);
            if ("ARCHIVAL".equals(type) && version != null) {
                System.out.println(type);
                // ?? Archival branch
                List<GL_SCH_Archival_Detail_Entity> T1Dt1;
                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = GL_SCH_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel,
                            reportAddlCriteria1,
                            parsedDate, version);
                } else {
                    T1Dt1 = GL_SCH_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
                }

                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);
                System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

            } else {
                // ?? Current branch
                List<GL_SCH_Detail_Entity> T1Dt1;

                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = GL_SCH_detail_repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
                            parsedDate);
                } else {
                    T1Dt1 = GL_SCH_detail_repo.getdatabydateList(parsedDate);
                    totalPages = GL_SCH_detail_repo.getdatacount(parsedDate);
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

        mv.setViewName("BRRS/GL_SCH");
        mv.addObject("displaymode", "Details");
        mv.addObject("currentPage", currentPage);
        System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
        mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
        mv.addObject("reportsflag", "reportsflag");
        mv.addObject("menu", reportId);
        return mv;
    }

    public byte[] getGL_SCHExcel(String filename, String reportId, String fromdate, String todate,
            String currency,
            String dtltype, String type, String version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");
        System.out.println(type);
        System.out.println(version);
        Date reportDate = dateformat.parse(todate);

        // ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelGL_SCHARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

        }

        List<GL_SCH_Summary_Entity1> dataList = GL_SCH_summary_repo1
                .getdatabydateList(dateformat.parse(todate));
                 List<GL_SCH_Summary_Entity2> dataList1 = GL_SCH_summary_repo2
                .getdatabydateList(dateformat.parse(todate));
                 List<GL_SCH_Summary_Entity3>dataList2= GL_SCH_summary_repo3
                         .getdatabydateList(dateformat.parse(todate));
                 List<GL_SCH_Manual_Summary_Entity>dataList3= GL_SCH_Manual_summary_repo
                         .getdatabydateList(dateformat.parse(todate));
        

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for  GL_SCH report. Returning empty result.");
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

            // --- End of Style Definitions ---
            int startRow = 8;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {

                    GL_SCH_Summary_Entity1 record = dataList.get(i);
                     GL_SCH_Summary_Entity2 record1 = dataList1.get(i); 
                     GL_SCH_Summary_Entity3 record2 = dataList2.get(i);
                     GL_SCH_Manual_Summary_Entity record3= dataList3.get(i);
                    System.out.println("rownumber=" + startRow + i);
                    
                    
                    
                    
                    
                    
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }
                    // Cell R9Cell1 = row.createCell(3);
                    // if (record.getR9_fig_bal_sheet() != null) {
                    // R9Cell1.setCellValue(record.getR9_fig_bal_sheet().doubleValue());
                    // R9Cell1.setCellStyle(numberStyle);
                    // } else {
                    // R9Cell1.setCellValue("");
                    // R9Cell1.setCellStyle(textStyle);
                    // }

                }
                // workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
                // ✅ Let Excel calculate formulas on open
                workbook.setForceFormulaRecalculation(true);
            } else {

            }
            // Write the final workbook content to the in-memory stream.
            workbook.write(out);
            logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
            return out.toByteArray();
        }
    }

    public byte[] getExcelGL_SCHARCHIVAL(String filename, String reportId, String fromdate, String todate,
            String currency, String dtltype, String type, String version) throws Exception {

        logger.info("Service: Starting Excel generation process in memory.");

        if (type.equals("ARCHIVAL") & version != null) {

        }

        List<GL_SCH_Archival_Summary_Entity1> dataList = GL_SCH_Archival_Summary_Repo1
                .getdatabydateListarchival(dateformat.parse(todate), version);
           List<GL_SCH_Archival_Summary_Entity2> dataList1 = GL_SCH_Archival_Summary_Repo2
                .getdatabydateListarchival(dateformat.parse(todate), version);        
           List<GL_SCH_Archival_Summary_Entity3>dataList2= GL_SCH_Archival_Summary_Repo3
        		   .getdatabydateListarchival(dateformat.parse(todate), version);  
           List<GL_SCH_Manual_Archival_Summary_Entity>dataList3= GL_SCH_Manual_Archival_Summary_Repo
        		   .getdatabydateListarchival(dateformat.parse(todate), version);  

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for GL_SCH report. Returning empty result.");
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

            int startRow = 8;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    GL_SCH_Archival_Summary_Entity1 record = dataList.get(i);
                    GL_SCH_Archival_Summary_Entity1 record1 = dataList.get(i);
                    GL_SCH_Archival_Summary_Entity3 record2 = dataList2.get(i);
                    GL_SCH_Manual_Archival_Summary_Entity record3= dataList3.get(i);

                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }
                    // Cell R9Cell1 = row.createCell(3);
                    // if (record.getR9_fig_bal_sheet() != null) {
                    // R9Cell1.setCellValue(record.getR9_fig_bal_sheet().doubleValue());
                    // R9Cell1.setCellStyle(numberStyle);
                    // } else {
                    // R9Cell1.setCellValue("");
                    // R9Cell1.setCellStyle(textStyle);
                    // }

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

    public List<Object> getGL_SCHArchival() {
        List<Object> GL_SCHArchivallist = new ArrayList<>();
        try {
            GL_SCHArchivallist = GL_SCH_Archival_Summary_Repo1.getGL_SCHarchival();
GL_SCHArchivallist = GL_SCH_Archival_Summary_Repo2.getGL_SCHarchival();
GL_SCHArchivallist = GL_SCH_Archival_Summary_Repo3.getGL_SCHarchival();
GL_SCHArchivallist = GL_SCH_Manual_Archival_Summary_Repo.getGL_SCHarchival();





            System.out.println("countser" + GL_SCHArchivallist.size());

        } catch (Exception e) {
            // Log the exception
            System.err.println("Error fetching GL_SCHArchivallist Archival data: " + e.getMessage());
            e.printStackTrace();

            // Optionally, you can rethrow it or return empty list
            // throw new RuntimeException("Failed to fetch data", e);
        }
        return GL_SCHArchivallist;
    }

    public byte[] getGL_SCHDetailExcel(String filename, String fromdate, String todate, String currency,
            String dtltype,
            String type, String version) {
        try {
            logger.info("Generating Excel for GL_SCH Details...");
            System.out.println("came to Detail download service");

            if (type.equals("ARCHIVAL") & version != null) {
                byte[] ARCHIVALreport = getGL_SCHDetailExcelARCHIVAL(filename, fromdate, todate, currency,
                        dtltype, type,
                        version);
                return ARCHIVALreport;
            }

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("GL_SCHDetails");

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
            List<GL_SCH_Detail_Entity> reportData = GL_SCH_detail_repo
                    .getdatabydateList(parsedToDate);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (GL_SCH_Detail_Entity item : reportData) {
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

                    row.createCell(4).setCellValue(item.getReportLabel());
                    row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
                logger.info("No data found for GL_SCH — only header will be written.");
            }

            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating GL_SCH Excel", e);
            return new byte[0];
        }
    }

    public byte[] getGL_SCHDetailExcelARCHIVAL(String filename, String fromdate, String todate,
            String currency,
            String dtltype, String type, String version) {
        try {
            logger.info("Generating Excel for GL_SCH ARCHIVAL Details...");
            System.out.println("came to ARCHIVAL Detail download service");
            if (type.equals("ARCHIVAL") & version != null) {

            }
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("GL_SCHDetail");

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
            List<GL_SCH_Archival_Detail_Entity> reportData = GL_SCH_Archival_Detail_Repo
                    .getdatabydateList(parsedToDate,
                            version);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (GL_SCH_Archival_Detail_Entity item : reportData) {
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

                    row.createCell(4).setCellValue(item.getReportLabel());
                    row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
                logger.info("No data found for GL_SCH — only header will be written.");
            }

            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating  GL_SCH Excel", e);
            return new byte[0];
        }
    }

    @Autowired
    BRRS_GL_SCH_Detail_Repo brrs_GL_SCH_detail_repo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
        ModelAndView mv = new ModelAndView("BRRS/GL_SCH");

        if (acctNo != null) {
            GL_SCH_Detail_Entity GL_SCHEntity = brrs_GL_SCH_detail_repo
                    .findByAcctnumber(acctNo);
            if (GL_SCHEntity != null && GL_SCHEntity.getReportDate() != null) {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
                        .format(GL_SCHEntity.getReportDate());
                mv.addObject("asondate", formattedDate);
            }
            mv.addObject("GL_SCHData", GL_SCHEntity);
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

            GL_SCH_Detail_Entity existing = brrs_GL_SCH_detail_repo.findByAcctnumber(acctNo);
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
                brrs_GL_SCH_detail_repo.save(existing);
                logger.info("Record updated successfully for account {}", acctNo);

                // Format date for procedure
                String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
                        .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

                // Run summary procedure after commit
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        try {
                            logger.info("Transaction committed — calling BRRS_GL_SCH_SUMMARY_PROCEDURE({})",
                                    formattedDate);
                            jdbcTemplate.update("BEGIN BRRS_GL_SCH_SUMMARY_PROCEDURE(?); END;",
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
            logger.error("Error updating GL_SCH record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating record: " + e.getMessage());
        }
    }
    
    
    
    
    
    
    public void updateReport(GL_SCH_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    //  Use your query to fetch by date
	    List<GL_SCH_Manual_Summary_Entity> list = GL_SCH_Manual_summary_repo
	        .getdatabydateList(updatedEntity.getREPORT_DATE());

	    GL_SCH_Manual_Summary_Entity existing;
	    if (list.isEmpty()) {
	        // Record not found — optionally create it
	        System.out.println("No record found for REPORT_DATE: " + updatedEntity.getREPORT_DATE());
	        existing = new GL_SCH_Manual_Summary_Entity();
	        existing.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	    } else {
	        existing = list.get(0);
	    }

	    try {
	        //  Only for specific row numbers
	    	int[] rows = {61, 103, 130, 139, 241, 243,245};

	    	// Common fields for all these rows
	    	String[] fields = {
	    	    "PRODUCT",
	    	    "FIG_BAL_BWP1",
	    	    "FIG_BAL_BWP2",
	    	    "AMT_ADJ_BWP1",
	    	    "AMT_ADJ_BWP2",
	    	    "NET_AMT_BWP1",
	    	    "NET_AMT_BWP2",
	    	    "BAL_SUB_BWP1",
	    	    "BAL_SUB_BWP2",
	    	    "BAL_ACT_SUB_BWP1",
	    	    "BAL_ACT_SUB_BWP2"
	    	};
	    	
	    	for (int row : rows) {

	    	    String prefix = "R" + row + "_";

	            for (String field : fields) {
	                String getterName = "get" + prefix + field; 
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = GL_SCH_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = GL_SCH_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields gracefully
	                    continue;
	                }
	            }
	        }

	        // Metadata
	        existing.setREPORT_VERSION(updatedEntity.getREPORT_VERSION());
	        existing.setREPORT_FREQUENCY(updatedEntity.getREPORT_FREQUENCY());
	        existing.setREPORT_CODE(updatedEntity.getREPORT_CODE());
	        existing.setREPORT_DESC(updatedEntity.getREPORT_DESC());
	        existing.setENTITY_FLG(updatedEntity.getENTITY_FLG());
	        existing.setMODIFY_FLG(updatedEntity.getMODIFY_FLG());
	        existing.setDEL_FLG(updatedEntity.getDEL_FLG());

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating GL_SCH Summary fields", e);
	    }

	    //  FIRST COMMIT — forces immediate commit
	    GL_SCH_Manual_summary_repo.saveAndFlush(existing);
	    System.out.println("GL_SCH Summary updated and COMMITTED");

	    //  Execute procedure with updated data
	    String oracleDate = new SimpleDateFormat("dd-MM-yyyy")
	            .format(updatedEntity.getREPORT_DATE())
	            .toUpperCase();

	    String sql = "BEGIN BRRS.BRRS_GL_SCH_SUMMARY_PROCEDURE ('" + oracleDate + "'); END;";
	    jdbcTemplate.execute(sql);

	    System.out.println("Procedure executed for date: " + oracleDate);
	}

    
    
    
    
    
    
    
    
    
    
    

}